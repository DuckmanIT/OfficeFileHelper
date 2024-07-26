package com.example.officefilefilter.data.repository

import com.example.officefilefilter.data.db.FileDAO
import com.example.officefilefilter.data.db.toFileModel
import com.example.officefilefilter.domain.model.FileModel
import com.example.officefilefilter.domain.repository.FileDBRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FileDBRepositoryImpl
    (
    private val fileDao: FileDAO
) : FileDBRepository {
    override fun getFavoriteFiles(): Flow<List<FileModel>> = flow {
        fileDao.getFavoriteFiles().collect { files ->
            emit(files.toFileModel())
        }
    }

    override suspend fun addFileToFavorites(file: FileModel) {
        fileDao.addFileToFavorites(file.path)
    }

    override fun getRecentFiles(limit: Int): Flow<List<FileModel>> = flow {
        fileDao.getRecentFiles(limit).collect { files ->
            emit(files.toFileModel())
        }
    }

    override suspend fun updateLastAccessedDate(fileModel: FileModel) {
        fileDao.updateLastAccessedDate(fileModel.path)
    }
}