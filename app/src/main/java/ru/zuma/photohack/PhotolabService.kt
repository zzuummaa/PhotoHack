package ru.zuma.photohack

import android.app.Activity
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.ByteArrayInputStream
import java.io.InputStream

class PhotolabService(val activity: Activity) {
    val clientPhotolab = ClientPhotolab()
    val templateName = "1001918"

    fun uploadImage(uri: Uri) = clientPhotolab.ImageUpload (
        getFileName(uri),
        activity.contentResolver.openInputStream(uri)
    )

    fun downloadImage(url: String) : InputStream {
        return clientPhotolab.DownloadFile(url)
    }

    fun getFileName(uri: Uri) : String? {
        val cursor = activity.getContentResolver()
            .query(uri, null, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val displayName = cursor.getString(
                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            )
            Log.i(javaClass.simpleName, "Display Name: $displayName")
            return displayName
        }
        return null
    }
}