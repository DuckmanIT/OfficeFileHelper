package com.example.officefileloader.presentation

import com.example.officefileloader.data.FileSource
import com.example.officefileloader.data.local.LocalFileStorage
import com.example.officefileloader.data.repository.FileRepositoryImpl
import com.example.officefileloader.domain.model.FileModel
import com.example.officefileloader.domain.model.FileType
import com.example.officefileloader.domain.usecase.GetFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn

class FileManager private constructor(
    private val getFileUseCase: GetFileUseCase,
    private val defaultPageSize: Int = 20,
) {
    class Builder {
        private var fileSource: FileSource? = null
        private var defaultPageSize: Int = 20

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

        fun build(): FileManager {
            val fileSource = requireNotNull(fileSource) { "FileSource must be set" }
            val fileRepository = FileRepositoryImpl(fileSource)
            val getFileUseCase = GetFileUseCase(fileRepository)
            return FileManager(getFileUseCase, defaultPageSize)
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


    private suspend fun getAllFiles(
        page: Int = 1,
        pageSize: Int = defaultPageSize,
        usePagination: Boolean = true,
        fileType: FileType = FileType.ALL
    ): List<FileModel> {
        return if (usePagination) {
            getFileUseCase.getAllFiles(page, pageSize, fileType).flowOn(Dispatchers.IO).first()
        } else {
            getFileUseCase.getAllFiles(1, Int.MAX_VALUE, fileType).flowOn(Dispatchers.IO).first()
        }
    }
}