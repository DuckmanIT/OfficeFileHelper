package com.example.officefileloader.domain.usecase

import com.example.officefileloader.domain.model.FileType
import com.example.officefileloader.domain.repository.FileRepository

class GetFileUseCase(
    private val fileRepository: FileRepository
) {
    suspend fun getAllFiles(page: Int, pageSize: Int, fileType: FileType = FileType.ALL) =
        fileRepository.getAllFiles(page, pageSize, fileType)
}