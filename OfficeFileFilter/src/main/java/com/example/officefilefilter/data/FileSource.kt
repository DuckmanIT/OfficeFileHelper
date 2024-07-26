package com.example.officefilefilter.data

import com.example.officefilefilter.domain.model.FileModel
import com.example.officefilefilter.domain.model.FileType

import kotlinx.coroutines.flow.Flow

interface FileSource {
    suspend fun getAllFiles(page : Int, pageSize : Int, fileType : FileType) : Flow<List<FileModel>>
}