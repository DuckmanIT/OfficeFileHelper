package com.example.officefilefilter.domain.repository


import com.example.officefilefilter.domain.model.FileModel
import com.example.officefilefilter.domain.model.FileType
import kotlinx.coroutines.flow.Flow

interface FileRepository {
    suspend fun getAllFiles(page : Int, pageSize : Int, fileType : FileType) : Flow<List<FileModel>>
}