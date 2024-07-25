package com.example.officefileloader.data.repository

import com.example.officefileloader.data.FileSource
import com.example.officefileloader.domain.model.FileModel
import com.example.officefileloader.domain.model.FileType
import com.example.officefileloader.domain.repository.FileRepository
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