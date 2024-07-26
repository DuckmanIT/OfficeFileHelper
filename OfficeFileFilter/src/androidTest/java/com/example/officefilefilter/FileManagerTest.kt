package com.example.officefilefilter


import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.officefilefilter.data.FileSource
import com.example.officefilefilter.data.db.FileDAO
import com.example.officefilefilter.data.db.FileDatabase
import com.example.officefilefilter.data.db.toFileModel
import com.example.officefilefilter.domain.model.FileModel
import com.example.officefilefilter.domain.model.toFileEntity
import com.example.officefilefilter.domain.repository.FileRepository
import com.example.officefilefilter.domain.usecase.GetDBFileUseCase
import com.example.officefilefilter.domain.usecase.GetFileUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.test.runTest
import com.google.common.truth.Truth.assertThat

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class FileManagerTest {

    private lateinit var fileManager: FileManager
    private lateinit var fileDAO: FileDAO
    private lateinit var db: FileDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = FileDatabase.getDatabase(context)
        fileDAO = db.fileDAO()
        // Initialize FileManager with a mock implementation
        fileManager =
            FileManager.Builder().useLocalFileStorage().useContext(context).setNotPageSize().build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getFavoriteFiles_returnsCorrectFiles() = runTest {
        val favoriteFile1 = FileModel(
            name = "file1.txt",
            path = "/path/to/file1.txt",
            isDirectory = false,
            size = 1024,
            lastModifiedDate = "2023-01-01"
        ).toFileEntity().copy(isFavorite = true)
        val favoriteFile2 = FileModel(
            name = "file2.pdf",
            path = "/path/to/file2.pdf",
            isDirectory = false,
            size = 2048,
            lastModifiedDate = "2023-02-01"
        ).toFileEntity().copy(isFavorite = true)
        val nonFavoriteFile = FileModel(
            name = "file3.jpg",
            path = "/path/to/file3.jpg",
            isDirectory = false,
            size = 512,
            lastModifiedDate = "2023-03-01"
        ).toFileEntity()

        CoroutineScope(Dispatchers.IO).launch {
            fileDAO.insertFile(favoriteFile1)
            fileDAO.insertFile(favoriteFile2)
            fileDAO.insertFile(nonFavoriteFile)
            val favoriteFiles = fileManager.getFavoriteFiles()

            assertThat(favoriteFiles).hasSize(2)
            assertThat(favoriteFiles).contains(favoriteFile1.toFileModel())
            assertThat(favoriteFiles).contains(favoriteFile2.toFileModel())
            assertThat(favoriteFiles).doesNotContain(nonFavoriteFile.toFileModel())
        }

    }

    @Test
    fun getAllFiles_returnsCorrectFiles() = runTest {
        val file1 = FileModel(
            name = "file1.txt",
            path = "/path/to/file1.txt",
            isDirectory = false,
            isFavorite = true,
            size = 1024,
            lastModifiedDate = "2023-01-01"
        ).toFileEntity()
        val file2 = FileModel(
            name = "file2.pdf",
            path = "/path/to/file2.pdf",
            isDirectory = false,
            isFavorite = true,
            size = 2048,
            lastModifiedDate = "2023-02-01"
        ).toFileEntity()
        val file3 = FileModel(
            name = "file3.jpg",
            path = "/path/to/file3.jpg",
            isDirectory = false,
            isFavorite = false,
            size = 512,
            lastModifiedDate = "2023-03-01"
        ).toFileEntity()

        CoroutineScope(Dispatchers.IO).launch {
            fileDAO.insertFile(file1)
            fileDAO.insertFile(file2)
            fileDAO.insertFile(file3)
            val files = fileManager.getFavoriteFiles()

            assertThat(files).hasSize(2)
            assertThat(files).contains(file1.toFileModel())
            assertThat(files).contains(file2.toFileModel())
        }
    }

    @Test
    fun getAllPdfFiles_returnsCorrectFiles() = runTest {
        val pdfFile1 = FileModel(
            name = "file1.pdf",
            path = "/path/to/file1.pdf",
            isDirectory = false,
            isFavorite = true,
            size = 1024,
            lastModifiedDate = "2023-01-01"
        ).toFileEntity()
        val pdfFile2 = FileModel(
            name = "file2.pdf",
            path = "/path/to/file2.pdf",
            isDirectory = false,
            isFavorite = true,
            size = 2048,
            lastModifiedDate = "2023-02-01"
        ).toFileEntity()
        val nonPdfFile = FileModel(
            name = "file3.jpg",
            path = "/path/to/file3.jpg",
            isDirectory = false,
            isFavorite = false,
            size = 512,
            lastModifiedDate = "2023-03-01"
        ).toFileEntity()

        CoroutineScope(Dispatchers.IO).launch {
            fileDAO.insertFile(pdfFile1)
            fileDAO.insertFile(pdfFile2)
            fileDAO.insertFile(nonPdfFile)
            val pdfFiles = fileManager.getAllPdfFiles()

            assertThat(pdfFiles).hasSize(2)
            assertThat(pdfFiles).contains(pdfFile1.toFileModel())
            assertThat(pdfFiles).contains(pdfFile2.toFileModel())
            assertThat(pdfFiles).doesNotContain(nonPdfFile.toFileModel())
        }
    }

    @Test
    fun getAllWordFiles_returnsCorrectFiles() = runTest {
        val pdfFile1 = FileModel(
            name = "file1.doc",
            path = "/path/to/file1.doc",
            isDirectory = false,
            isFavorite = true,
            size = 1024,
            lastModifiedDate = "2023-01-01"
        ).toFileEntity()
        val pdfFile2 = FileModel(
            name = "file2.doc",
            path = "/path/to/file2.doc",
            isDirectory = false,
            isFavorite = true,
            size = 2048,
            lastModifiedDate = "2023-02-01"
        ).toFileEntity()
        val nonPdfFile = FileModel(
            name = "file3.jpg",
            path = "/path/to/file3.jpg",
            isDirectory = false,
            isFavorite = false,
            size = 512,
            lastModifiedDate = "2023-03-01"
        ).toFileEntity()

        CoroutineScope(Dispatchers.IO).launch {
            fileDAO.insertFile(pdfFile1)
            fileDAO.insertFile(pdfFile2)
            fileDAO.insertFile(nonPdfFile)
            val pdfFiles = fileManager.getAllWordFiles()

            assertThat(pdfFiles).hasSize(2)
            assertThat(pdfFiles).contains(pdfFile1.toFileModel())
            assertThat(pdfFiles).contains(pdfFile2.toFileModel())
            assertThat(pdfFiles).doesNotContain(nonPdfFile.toFileModel())
        }
    }

    @Test
    fun getAllExcelFiles_returnsCorrectFiles() = runTest {
        val pdfFile1 = FileModel(
            name = "file1.xlsx",
            path = "/path/to/file1.xlsx",
            isDirectory = false,
            isFavorite = true,
            size = 1024,
            lastModifiedDate = "2023-01-01"
        ).toFileEntity()
        val pdfFile2 = FileModel(
            name = "file2.xlsx",
            path = "/path/to/file2.xlsx",
            isDirectory = false,
            isFavorite = true,
            size = 2048,
            lastModifiedDate = "2023-02-01"
        ).toFileEntity()
        val nonPdfFile = FileModel(
            name = "file3.jpg",
            path = "/path/to/file3.jpg",
            isDirectory = false,
            isFavorite = false,
            size = 512,
            lastModifiedDate = "2023-03-01"
        ).toFileEntity()

        CoroutineScope(Dispatchers.IO).launch {
            fileDAO.insertFile(pdfFile1)
            fileDAO.insertFile(pdfFile2)
            fileDAO.insertFile(nonPdfFile)
            val pdfFiles = fileManager.getAllExcelFiles()

            assertThat(pdfFiles).hasSize(2)
            assertThat(pdfFiles).contains(pdfFile1.toFileModel())
            assertThat(pdfFiles).contains(pdfFile2.toFileModel())
            assertThat(pdfFiles).doesNotContain(nonPdfFile.toFileModel())
        }
    }

    @Test
    fun getAllPptFiles_returnsCorrectFiles() = runTest {
        val pdfFile1 = FileModel(
            name = "file1.ppt",
            path = "/path/to/file1.ppt",
            isDirectory = false,
            isFavorite = true,
            size = 1024,
            lastModifiedDate = "2023-01-01"
        ).toFileEntity()
        val pdfFile2 = FileModel(
            name = "file2.ppt",
            path = "/path/to/file2.ppt",
            isDirectory = false,
            isFavorite = true,
            size = 2048,
            lastModifiedDate = "2023-02-01"
        ).toFileEntity()
        val nonPdfFile = FileModel(
            name = "file3.jpg",
            path = "/path/to/file3.jpg",
            isDirectory = false,
            isFavorite = false,
            size = 512,
            lastModifiedDate = "2023-03-01"
        ).toFileEntity()

        CoroutineScope(Dispatchers.IO).launch {
            fileDAO.insertFile(pdfFile1)
            fileDAO.insertFile(pdfFile2)
            fileDAO.insertFile(nonPdfFile)
            val pdfFiles = fileManager.getAllPptFiles()

            assertThat(pdfFiles).hasSize(2)
            assertThat(pdfFiles).contains(pdfFile1.toFileModel())
            assertThat(pdfFiles).contains(pdfFile2.toFileModel())
            assertThat(pdfFiles).doesNotContain(nonPdfFile.toFileModel())
        }
    }

    @Test
    fun setAndGetRecentFiles_returnsCorrectFiles() = runTest {
        val recentFile1 = FileModel(
            name = "file1.txt",
            path = "/path/to/file1.txt",
            isDirectory = false,
            isFavorite = true,
            size = 1024,
            lastModifiedDate = "2023-01-01"
        )
        val recentFile2 = FileModel(
            name = "file2.pdf",
            path = "/path/to/file2.pdf",
            isDirectory = false,
            isFavorite = true,
            size = 2048,
            lastModifiedDate = "2023-02-01"
        )
        val recentFile3 = FileModel(
            name = "file3.jpg",
            path = "/path/to/file3.jpg",
            isDirectory = false,
            isFavorite = false,
            size = 512,
            lastModifiedDate = "2023-03-01"
        )
        val entityFile1 = recentFile1.toFileEntity()
        val entityFile2 = recentFile2.toFileEntity()
        val entityFile3 = recentFile3.toFileEntity()

        CoroutineScope(Dispatchers.IO).launch {
            fileDAO.insertFile(entityFile1)
            fileDAO.insertFile(entityFile2)
            fileDAO.insertFile(entityFile3)
            fileManager.updateLastAccessedDate(recentFile1)
            fileManager.updateLastAccessedDate(recentFile2)
            val recentFiles = fileManager.getRecentFiles(10)

            assertThat(recentFiles).hasSize(3)
            assertThat(recentFiles).contains(recentFile1)
            assertThat(recentFiles).contains(recentFile2)
            assertThat(recentFiles).contains(recentFile3)
        }
    }

}

//class FileManagerTest {
//
//    private lateinit var fileManager: FileManager
//
//    private val fileRepository: FileRepository = mockk()
//    private val fileSource: FileSource = mockk()
//    private val getFileUseCase = GetFileUseCase(fileRepository)
//    private val getDbFileUseCase: GetDBFileUseCase = mockk()
//    private val fileDAO: FileDAO = mockk()
//
//    @Before
//    fun setup() {
//        fileManager = FileManager.Builder()
//            .useLocalFileStorage()
//            .useContext(mockk(relaxed = true))
//            .build()
//        MockKAnnotations.init(this)
//
//        coEvery { fileDAO.insertFile(any()) } returns Unit
//        coEvery { fileDAO.deleteFile(any()) } returns Unit
//        every { fileDAO.getFavoriteFiles() } returns flowOf(emptyList())
//        coEvery { getDbFileUseCase.addFileToFavorites(any()) } returns Unit
//        coEvery { getDbFileUseCase.getFavoriteFiles() } returns flowOf(emptyList())
//    }
//
//    @Test
//    fun `getAllPdfFiles should return only PDF files`() = runBlocking {
//        val testFiles = listOf(
//            FileModel(
//                name = "file1.pdf",
//                path = "/path/to/file1.pdf",
//                isDirectory = false,
//                isFavorite = false,
//                size = 1024,
//                lastModifiedDate = "2023-01-01"
//            ),
//            FileModel(
//                name = "file2.docx",
//                path = "/path/to/file2.docx",
//                isDirectory = false,
//                isFavorite = false,
//                size = 2048,
//                lastModifiedDate = "2023-01-02"
//            ),
//            FileModel(
//                name = "file3.pdf",
//                path = "/path/to/file3.pdf",
//                isDirectory = false,
//                isFavorite = false,
//                size = 512,
//                lastModifiedDate = "2023-01-03"
//            )
//        )
//
//        coEvery { fileRepository.getAllFiles(any(), any(), any()) } returns flowOf(testFiles)
//
//        val pdfFiles = fileManager.getAllPdfFiles()
//
//        assertEquals(2, pdfFiles.size)
//        assertEquals("file1.pdf", pdfFiles[0].name)
//        assertEquals("file3.pdf", pdfFiles[1].name)
//    }
//
//    @Test
//    fun `getFileByExtension should return only files with the specified extension`() =
//        runBlocking {
//            val testFiles = listOf(
//                FileModel(
//                    name = "file1.pdf",
//                    path = "/path/to/file1.pdf",
//                    isDirectory = false,
//                    isFavorite = false,
//                    size = 1024,
//                    lastModifiedDate = "2023-01-01"
//                ),
//                FileModel(
//                    name = "file2.docx",
//                    path = "/path/to/file2.docx",
//                    isDirectory = false,
//                    isFavorite = false,
//                    size = 2048,
//                    lastModifiedDate = "2023-01-02"
//                ),
//                FileModel(
//                    name = "file3.pdf",
//                    path = "/path/to/file3.pdf",
//                    isDirectory = false,
//                    isFavorite = false,
//                    size = 512,
//                    lastModifiedDate = "2023-01-03"
//                )
//            )
//            coEvery { fileRepository.getAllFiles(any(), any(), any()) } returns flowOf(testFiles)
//            val pdfFiles = fileManager.getFileByExtension(".pdf")
//            assertEquals(2, pdfFiles.size)
//            assertEquals("file1.pdf", pdfFiles[0].name)
//            assertEquals("file3.pdf", pdfFiles[1].name)
//        }
//
//    @Test
//    fun `getAllFiles should return all files when file type is ALL`() = runBlocking {
//        val testFiles = listOf(
//            FileModel(
//                name = "file1.pdf",
//                path = "/path/to/file1.pdf",
//                isDirectory = false,
//                isFavorite = false,
//                size = 1024,
//                lastModifiedDate = "2023-01-01"
//            ),
//            FileModel(
//                name = "file2.docx",
//                path = "/path/to/file2.docx",
//                isDirectory = false,
//                isFavorite = false,
//                size = 2048,
//                lastModifiedDate = "2023-01-02"
//            ),
//            FileModel(
//                name =  "file3.xlsx",
//                path = "/path/to/file3.xlsx",
//                isDirectory = false,
//                isFavorite = false,
//                size = 512,
//                lastModifiedDate = "2023-01-03"
//            )
//        )
//        coEvery { fileRepository.getAllFiles(any(), any(), any()) } returns flowOf(testFiles)
//        val allFiles = fileManager.getAllFiles()
//        assertEquals(testFiles.size, allFiles.size)
//    }
//}