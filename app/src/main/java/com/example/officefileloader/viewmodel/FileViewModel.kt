package com.example.officefileloader.viewmodel

import android.app.Application
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FileViewModel(application: Application) : AndroidViewModel(application) {

    private val appContext: Context = application.applicationContext

    fun getAllOfficeFiles() {
        Log.e("FileLogging", "File Name")
        viewModelScope.launch(Dispatchers.IO) {
            val officeFiles = loadOfficeFilesFromStorage()
            officeFiles.forEach { file ->
                Log.e("FileLogging", "File Name: ${file.name}, Mime Type: ${file.mimeType}")
            }
        }
    }

    private fun loadOfficeFilesFromStorage(): List<File> {
        val files = mutableListOf<File>()

        val uri = MediaStore.Files.getContentUri("external")

        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.MIME_TYPE
        )

        val selectionMimeType = "${MediaStore.Files.FileColumns.MIME_TYPE} IN (?, ?, ?, ?)"
        val selectionArgs = arrayOf(
            "application/pdf",
            "application/msword",
            "application/vnd.ms-excel",
            "application/vnd.ms-powerpoint"
        )

        val sortOrder = "${MediaStore.Files.FileColumns.DISPLAY_NAME} ASC"
        Log.d("FileViewModel", "Querying office files...")


        val cursor = appContext.contentResolver.query(
            uri,
            projection,
            selectionMimeType,
            selectionArgs,
            sortOrder
        )

        Log.d("FileViewModel", "Cursor count: ${cursor?.count}")


        cursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)

            while (cursor.moveToNext()) {
                val fileId = cursor.getLong(idColumn)
                val fileName = cursor.getString(nameColumn)
                val mimeType = cursor.getString(mimeTypeColumn)
                Log.d("FileViewModel", "File Name: $fileName, Mime Type: $mimeType")
                files.add(File(fileName, mimeType))
            }
        }

        return files
    }

    data class File(val name: String, val mimeType: String)
}