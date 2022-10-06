package com.example.imageuploader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.imageuploader.databinding.ActivityManageBankBinding
import com.example.testuploadimg.MyAPI
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class manage_bank : AppCompatActivity() {
    lateinit var binding: ActivityManageBankBinding
    private var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageBankBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.qrCode.setOnClickListener {
            openImageChooser()
        }
        binding.btnAddBank.setOnClickListener {
            uploadImage()
        }
    }
    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 101
    }
    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_CODE_PICK_IMAGE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    selectedImageUri = data?.data
                    binding.qrCode.setImageURI(selectedImageUri)
                }
            }
        }
    }
    private fun uploadImage() {
        val bank_admin_id = "1"
        val bank_name = binding.editBankName.text.toString()
        val account_number = binding.editBankNumber.text.toString()
        val first_name= binding.editBankAccountFirstName.text.toString()
        val last_name= binding.editBankAccountLastName.text.toString()

        if (selectedImageUri == null) {
            binding.root.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(cacheDir, contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

//        progress_bar.progress = 0
        val body = UoloadRequestBodyEdit(file, "image", this)
        MyAPI().edit(
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            ),
            bank_admin_id,
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), bank_name),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), account_number),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), first_name),
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), last_name)

        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
//                binding.root.snackbar(t.message!!)
                Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
//                progress_bar.progress = 0
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

    }
}