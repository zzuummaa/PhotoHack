package ru.zuma.photohack

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.*
import ru.zuma.photohack.photolab.PhotolabService
import ru.zuma.photohack.photolab.TemplateIds


val LOAD_IMAGE_REQ_CODE = 1

class MainActivity : AppCompatActivity() {

    private lateinit var rvMessage: RecyclerView
    private lateinit var mesagesAdapter: RecyclerView.Adapter<*>
    private lateinit var messages: ArrayList<Message>

    private lateinit var photolabService: PhotolabService
    private lateinit var dialogFragment: FireMissilesDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dialogFragment = FireMissilesDialogFragment()

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

        photolabService = PhotolabService(this)

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
                    val srcImgURL = photolabService.uploadImage(it)
                    Log.i(javaClass.simpleName, "Image uploaded ${srcImgURL}")
                    val url = photolabService.processTemplate(srcImgURL, TemplateIds.REST_ID)
                    Log.i(javaClass.simpleName, "Image processed ${srcImgURL}")
                    val imageStream = photolabService.downloadImage(url)
                    val imageBitmap = BitmapFactory.decodeStream(imageStream)
                    runOnUiThread {
//                        iv_attach_file.setImageBitmap(imageBitmap)
                        dialogFragment.setBitmapDrawable(imageBitmap)
                        dialogFragment.setOnSubmit {
                            messages.add(Message(tv_message.text.toString(), imageBitmap))
                            mesagesAdapter.notifyDataSetChanged()
                        }
                        dialogFragment.show(supportFragmentManager, "dlg1")
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        val builder = AlertDialog.Builder(this)
        // Get the layout inflater
        val inflater = getLayoutInflater()

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_submit_filter, null))
            // Add action buttons
            .setPositiveButton("Submit",
                DialogInterface.OnClickListener { dialog, id ->
                    // sign in the user ...
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { dialog, id ->
                    // TODO
                })
        return builder.create()
    }
}
