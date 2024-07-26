package com.example.officefilefilter.domain.usecase

import com.example.officefilefilter.domain.model.FileType
import com.example.officefilefilter.domain.repository.FileRepository


class GetFileUseCase(
    private val fileRepository: FileRepository
) {
    suspend fun getAllFiles(page: Int, pageSize: Int, fileType: FileType = FileType.ALL) =
        fileRepository.getAllFiles(page, pageSize, fileType)
}