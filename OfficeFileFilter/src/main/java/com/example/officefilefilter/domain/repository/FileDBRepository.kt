package com.example.officefilefilter.domain.repository

import com.example.officefilefilter.domain.model.FileModel
import kotlinx.coroutines.flow.Flow

interface FileDBRepository {
    fun getFavoriteFiles(): Flow<List<FileModel>>
    suspend fun addFileToFavorites(file: FileModel)
    fun getRecentFiles(limit: Int): Flow<List<FileModel>>
    suspend fun updateLastAccessedDate(fileModel: FileModel)
}