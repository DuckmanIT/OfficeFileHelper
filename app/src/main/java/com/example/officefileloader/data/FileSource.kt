package com.example.officefileloader.data

import com.example.officefileloader.domain.model.FileModel
import com.example.officefileloader.domain.model.FileType
import kotlinx.coroutines.flow.Flow

interface FileSource {
    suspend fun getAllFiles(page : Int, pageSize : Int, fileType : FileType) : Flow<List<FileModel>>
}