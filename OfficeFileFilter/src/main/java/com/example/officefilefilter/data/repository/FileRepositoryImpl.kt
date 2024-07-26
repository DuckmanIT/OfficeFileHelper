package com.example.officefilefilter.data.repository

import com.example.officefilefilter.data.FileSource
import com.example.officefilefilter.domain.model.FileModel
import com.example.officefilefilter.domain.model.FileType
import com.example.officefilefilter.domain.repository.FileRepository
import kotlinx.coroutines.flow.Flow

class FileRepositoryImpl(
    private val dataSource: FileSource
) : FileRepository {
    override suspend fun getAllFiles(
        page: Int,
        pageSize: Int,
        fileType: FileType
    ): Flow<List<FileModel>> {
        return dataSource.getAllFiles(page, pageSize, fileType)
    }
}