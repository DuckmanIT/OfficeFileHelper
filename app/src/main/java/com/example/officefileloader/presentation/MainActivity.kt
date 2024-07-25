package com.example.officefileloader.presentation

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.officefileloader.R
import com.example.officefileloader.databinding.ActivityMainBinding
import com.example.officefileloader.viewmodel.FileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//class MainActivity : AppCompatActivity() {
//
//    private lateinit var viewModel: FileViewModel
//    private lateinit var binding: ActivityMainBinding
//    private val MANAGE_EXTERNAL_STORAGE_REQUEST = 100
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        requestManageExternalStoragePermission()
//
//        viewModel = ViewModelProvider(this).get(FileViewModel::class.java)
//
//        binding.btnGetAllFile.setOnClickListener{
//            Log.d("hehe", "dasdsadas")
//            viewModel.getAllOfficeFiles()
//        }
//    }
//
//    private fun requestManageExternalStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            if (!Environment.isExternalStorageManager()) {
//                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
//                startActivityForResult(intent, MANAGE_EXTERNAL_STORAGE_REQUEST)
//            }
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == MANAGE_EXTERNAL_STORAGE_REQUEST) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                if (Environment.isExternalStorageManager()) {
//                    viewModel.getAllOfficeFiles()
//                } else {
//                    Toast.makeText(this, "Quyền truy cập bộ nhớ ngoài bị từ chối", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestStoragePermission(this, {
        }, {
            Log.i("Permission", "Permission denied")
        })

        binding.btnGetAllFile.setOnClickListener {
            fetchAllFiles()
        }

        binding.btWord.setOnClickListener {
            fetchFilesWORD()
        }

        binding.btnExcel.setOnClickListener {
            fetchFilesEXCEL()
        }

        binding.btnPowerPoint.setOnClickListener {
            fetchFilesPPT()
        }

        binding.btnPDF.setOnClickListener {
            fetchFilesPDF()
        }
    }

    private fun requestStoragePermission(
        context: Context, onGranted: () -> Unit, onDenied: () -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                onGranted()
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:${context.packageName}")
                (context as? AppCompatActivity)?.startActivityForResult(
                    intent, REQUEST_CODE_STORAGE_PERMISSION
                )
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as AppCompatActivity,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_STORAGE_PERMISSION
                )
            } else {
                onGranted()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_STORAGE_PERMISSION = 1001
    }

    private fun fetchFilesPDF() {
        val fileManager = FileManager.Builder().useLocalFileStorage().setDefaultPageSize(50).build()
        CoroutineScope(Dispatchers.Default).launch {
            val file = fileManager.getAllPdfFiles(usePagination = false)
            Log.i("File", "File size: ${file.size}")
            file.forEach {
                Log.d("Hehe", "Name : ${it.name} Size: ${it.size / 1024} Last Modified Date : ${it.lastModifiedDate}")
            }
        }
    }

    private fun fetchFilesWORD() {
        val fileManager = FileManager.Builder().useLocalFileStorage().setDefaultPageSize(50).build()
        CoroutineScope(Dispatchers.Default).launch {
            val file = fileManager.getAllWordFiles(usePagination = false)
            Log.i("File", "File size: ${file.size}")
            file.forEach {
                Log.d("Hehe", "Name : ${it.name} Size: ${it.size / 1024} Last Modified Date : ${it.lastModifiedDate}")
            }
        }
    }

    private fun fetchFilesEXCEL() {
        val fileManager = FileManager.Builder().useLocalFileStorage().setDefaultPageSize(50).build()
        CoroutineScope(Dispatchers.Default).launch {
            val file = fileManager.getAllExcelFiles(usePagination = false)
            Log.i("File", "File size: ${file.size}")
            file.forEach {
                Log.d("Hehe", "Name : ${it.name} Size: ${it.size / 1024} Last Modified Date : ${it.lastModifiedDate}")
            }
        }
    }

    private fun fetchFilesPPT() {
        val fileManager = FileManager.Builder().useLocalFileStorage().setDefaultPageSize(50).build()
        CoroutineScope(Dispatchers.Default).launch {
            val file = fileManager.getAllPptFiles(usePagination = false)
            Log.i("File", "File size: ${file.size}")
            file.forEach {
                Log.d("Hehe", "Name : ${it.name} Size: ${it.size / 1024} Last Modified Date : ${it.lastModifiedDate}")
            }
        }
    }

    private fun fetchAllFiles() {
        val fileManager = FileManager.Builder().useLocalFileStorage().setDefaultPageSize(50).build()
        CoroutineScope(Dispatchers.Default).launch {
            val file = fileManager.getAllDocumentFiles(usePagination = false)
            Log.i("File", "File size: ${file.size}")
            file.forEach {
                Log.d("Hehe", "Name : ${it.name} Size: ${it.size / 1024} Last Modified Date : ${it.lastModifiedDate}")
            }
        }
    }

}


