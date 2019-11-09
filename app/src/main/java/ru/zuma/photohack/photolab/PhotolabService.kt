package ru.zuma.photohack.photolab

import android.app.Activity
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.InputStream

object TemplateIds {
    val REST_ID = "1001965"
    val JOB_ID  = "1001966"
}

class PhotolabService(val activity: Activity) {
    val clientPhotolab = ClientPhotolab()

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

    fun processTemplate(imageURL: String, templateId: String) : String {
        val testArr = ArrayList<ImageRequest>()
        val testIm = ImageRequest()
        testIm.url = imageURL
        testIm.crop = "0,0,1,1"
        testIm.flip = 0
        testIm.rotate = 0
        testArr.add(testIm)
        var testArrRequests = arrayOfNulls<ImageRequest>(testArr.size)
        testArrRequests = testArr.toArray(testArrRequests)

        return clientPhotolab.TemplateProcess(templateId, testArrRequests)
    }
}