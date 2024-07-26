package com.example.officefilefilter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import com.example.officefilefilter.data.FileSource
import com.example.officefilefilter.data.db.FileDatabase
import com.example.officefilefilter.data.local.LocalFileStorage
import com.example.officefilefilter.data.repository.FileDBRepositoryImpl
import com.example.officefilefilter.data.repository.FileRepositoryImpl
import com.example.officefilefilter.domain.model.FileModel
import com.example.officefilefilter.domain.model.FileType
import com.example.officefilefilter.domain.usecase.GetDBFileUseCase
import com.example.officefilefilter.domain.usecase.GetFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn

class FileManager private constructor(
    private val getFileUseCase: GetFileUseCase,
    private val getDbFileUseCase: GetDBFileUseCase?,
    private val context: Context?,
    private val defaultPageSize: Int = 20,
) {

    private constructor(
        getFileUseCase: GetFileUseCase, context: Context, defaultPageSize: Int
    ) : this(getFileUseCase, null, context, defaultPageSize)

    private val dispatcher = Dispatchers.IO

    class Builder {
        private var fileSource: FileSource? = null
        private var defaultPageSize: Int = 20

        private var context: Context? = null

        fun useLocalFileStorage(): Builder {
            this.fileSource = LocalFileStorage()
            return this
        }

        fun useRemoteFileStorage(): Builder {
            return this
        }


        fun setDefaultPageSize(pageSize: Int): Builder {
            this.defaultPageSize = pageSize
            return this
        }

        fun setNotPageSize(): Builder {
            this.defaultPageSize = Int.MAX_VALUE
            return this
        }

        fun useContext(
            context: Context
        ): Builder {
            this.context = context
            return this
        }


        fun build(): FileManager {
            val fileSource = requireNotNull(fileSource) { "FileSource must be set" }
            val fileRepository = FileRepositoryImpl(fileSource)
            val getFileUseCase = GetFileUseCase(fileRepository)
            return if (context != null) {
                val fileDatabase = FileDatabase.getDatabase(context!!)
                val dbFileRepository = FileDBRepositoryImpl(fileDatabase.fileDAO())
                val getDbFileUseCase = GetDBFileUseCase(dbFileRepository)
                FileManager(getFileUseCase, getDbFileUseCase, context!!, defaultPageSize)
            } else {
                throw SecurityException("Context must be set")
            }
        }
    }
    suspend fun getAllPdfFiles(
        page: Int = 1, pageSize: Int = defaultPageSize, usePagination: Boolean = true
    ): List<FileModel> = getAllFiles(page, pageSize, usePagination, FileType.PDF)

    suspend fun getAllWordFiles(
        page: Int = 1, pageSize: Int = defaultPageSize, usePagination: Boolean = true
    ): List<FileModel> = getAllFiles(page, pageSize, usePagination, FileType.WORD)

    suspend fun getAllExcelFiles(
        page: Int = 1, pageSize: Int = defaultPageSize, usePagination: Boolean = true
    ): List<FileModel> = getAllFiles(page, pageSize, usePagination, FileType.EXCEL)

    suspend fun getAllPptFiles(
        page: Int = 1, pageSize: Int = defaultPageSize, usePagination: Boolean = true
    ): List<FileModel> = getAllFiles(page, pageSize, usePagination, FileType.PPT)

    suspend fun getAllDocumentFiles(
        page: Int = 1, pageSize: Int = defaultPageSize, usePagination: Boolean = true
    ): List<FileModel> = getAllFiles(page, pageSize, usePagination, FileType.DOCUMENT)


    suspend fun getFileByExtension(
        extension: String,
        page: Int = 1,
        pageSize: Int = defaultPageSize,
        usePagination: Boolean = true
    ): List<FileModel> {
        val files = getAllFiles(page, pageSize, usePagination, FileType.ALL)
        val ext = if (extension.startsWith(".")) extension else ".$extension"
        return files.filter { it.name.endsWith(ext, true) }
    }

    suspend fun setFavoriteFile(fileModel: FileModel) {
        getDbFileUseCase?.addFileToFavorites(fileModel)
    }

    suspend fun updateLastAccessedDate(fileModel: FileModel) {
        getDbFileUseCase?.updateLastAccessedDate(fileModel)
    }

    suspend fun getRecentFiles(limit: Int = 10) =
        getDbFileUseCase?.getRecentFiles(limit)?.flowOn(dispatcher)?.first()

    suspend fun getFavoriteFiles() =
        getDbFileUseCase?.getFavoriteFiles()?.flowOn(dispatcher)?.first()

    private fun hasStoragePermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    suspend fun getAllFiles(
        page: Int = 1,
        pageSize: Int = defaultPageSize,
        usePagination: Boolean = true,
        fileType: FileType = FileType.ALL
    ): List<FileModel> {
        if (!context?.let { hasStoragePermission(it) }!!) {
            throw SecurityException(
                "Storage permission not granted. Please request READ_EXTERNAL_STORAGE permission or" + "MANAGE_EXTERNAL_STORAGE permission for Android 11 and above."
            )
        }
        return if (usePagination) {
            getFileUseCase.getAllFiles(page, pageSize, fileType).flowOn(dispatcher).first()
        } else {
            getFileUseCase.getAllFiles(1, Int.MAX_VALUE, fileType).flowOn(dispatcher).first()
        }
    }
}