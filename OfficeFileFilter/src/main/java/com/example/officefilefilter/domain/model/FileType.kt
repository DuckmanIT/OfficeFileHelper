package com.example.officefilefilter.domain.model

enum class FileType(val extensions: Array<String>) {
    ALL(emptyArray()),
    DOCUMENT(arrayOf("pdf", "doc", "docx", "xls", "xlsx", "txt", "ppt", "pptx")),
    PDF(arrayOf("pdf")),
    WORD(arrayOf("doc", "docx")),
    EXCEL(arrayOf("xls", "xlsx")),
    PPT(arrayOf("ppt", "pptx")),
    OTHER(emptyArray())
}