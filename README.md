<h2 align=center>
<img width="200" height="120" alt="Office File Helper" src="MS-Office-Logo.jpg" /> <br />
    Android library designed to efficiently handle and filter files based on their type, especially focusing on common office document formats. 
</h2>

[![Kotlin Stable](https://kotl.in/badges/stable.svg)](https://kotlinlang.org/docs/components-stability.html)
[![JetBrains official project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Slack channel](https://img.shields.io/badge/chat-slack-green.svg?logo=slack)](https://kotlinlang.slack.com/messages/coroutines/)

## 📝 Description

This library provides a robust and easy-to-use way for Android developers to interact with the device's file system, specifically targeting files commonly used in office environments. It streamlines the process of:

- **File Discovery:**  Retrieving lists of specific file types (PDF, Word, Excel, PowerPoint) from storage.
- **File Filtering:**  Efficiently filter files by their extensions.
- **Recent Files Tracking:** Maintaining a history of recently accessed files, enhancing user experience.
- **Favorites Management:** Allowing users to bookmark frequently used files.
- **Pagination:** Handling large file lists by loading them in manageable chunks.

## 🚀 Features

- **Get all PDF, Word, Excel, PowerPoint, and generic document files.**
- **Get files based on their extension.**
- **Retrieve recent files.**
- **Mark and unmark files as favorites.**
- **Update the last accessed timestamp of a file.**
- **Built-in pagination support.**

## 🛠 Installation

Add JitPack in your root `build.gradle` at the end of repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your module's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.your-github-username:OfficeFileFilter:latest-release'
}
```

**Replace:**
- `your-github-username` with your actual GitHub username.
- `latest-release` with the latest version tag from the releases page.

## 💻 Usage

### 1. Initialization

Create an instance of the `FileManager` using its builder:

```kotlin
val fileManager = FileManager.Builder()
    .useLocalFileStorage() 
    .setDefaultPageSize(50) 
    .useContext(context)
    .build()
```

### 2. Getting Files

```kotlin
// Get all PDF files (first page, 20 files per page)
val pdfFiles = fileManager.getAllPdfFiles()

// Get all Word files (second page, 50 files per page)
val wordFiles = fileManager.getAllWordFiles(page = 2, pageSize = 50)

// Get files with .txt extension (all files)
val textFiles = fileManager.getFileByExtension(extension = "txt", usePagination = false)
```

### 3. Managing Recent and Favorite Files

```kotlin
// Mark a file as favorite
fileManager.setFavoriteFile(file)

// Update last accessed date
fileManager.updateLastAccessedDate(file)

// Get the 5 most recent files
val recentFiles = fileManager.getRecentFiles(limit = 5)

// Get all favorite files
val favoriteFiles = fileManager.getFavoriteFiles()
```

## 🔒 Permissions

Ensure you have the necessary permissions in your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

For Android 11 (API level 30) and above, you also need:

```xml
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
```

Request these permissions at runtime if your app targets API level 23 (Android 6.0) or higher.

## 🤝 Contributing

Contributions are always welcome! Please feel free to open an issue or submit a pull request.

## ©️ License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
```