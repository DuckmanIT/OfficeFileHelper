package com.example.officefilefilter.domain.usecase

import com.example.officefilefilter.data.repository.FileDBRepositoryImpl
import com.example.officefilefilter.domain.model.FileModel

class GetDBFileUseCase (
    private val fileDbRepository: FileDBRepositoryImpl
) {
    fun getFavoriteFiles() = fileDbRepository.getFavoriteFiles()

    fun getRecentFiles(limit: Int) = fileDbRepository.getRecentFiles(limit)

    suspend fun addFileToFavorites(fileModel: FileModel) =
        fileDbRepository.addFileToFavorites(fileModel)

    suspend fun updateLastAccessedDate(fileModel: FileModel) {
        fileDbRepository.updateLastAccessedDate(fileModel)
    }
}