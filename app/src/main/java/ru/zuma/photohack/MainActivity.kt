package ru.zuma.photohack

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.provider.OpenableColumns




val LOAD_IMAGE_REQ_CODE = 1

class MainActivity : AppCompatActivity() {

    private lateinit var rvMessage: RecyclerView
    private lateinit var mesagesAdapter: RecyclerView.Adapter<*>
    private lateinit var messages: ArrayList<Message>

    private lateinit var clientPhotolab: ClientPhotolab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        messages = arrayListOf(
            Message("Adadwa"),
            Message("dwda")
        )
        val viewManager = LinearLayoutManager(this)
        mesagesAdapter = MessageAdapter(messages)

        rvMessage = findViewById<RecyclerView>(R.id.rv_messages).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an mesagesAdapter (see also next example)
            adapter = mesagesAdapter
        }

        clientPhotolab = ClientPhotolab()

        findViewById<ImageView>(R.id.iv_attach_file).apply {
            setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    LOAD_IMAGE_REQ_CODE
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LOAD_IMAGE_REQ_CODE && data != null) {
            data.data?.let {
                Log.w(javaClass.simpleName, "uri path: ${it}")
                launch {
                    val url = clientPhotolab.ImageUpload (
                        getFileName(it),
                        contentResolver.openInputStream(it)
                    )
                    Log.i(javaClass.simpleName, url)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getFileName(uri: Uri) : String? {
        val cursor = getContentResolver()
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
