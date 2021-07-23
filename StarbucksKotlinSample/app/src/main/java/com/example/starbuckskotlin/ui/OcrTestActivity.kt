package com.example.starbuckskotlin.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.databinding.DataBindingUtil
import com.example.starbuckskotlin.R
import com.example.starbuckskotlin.api.RetrofitBuilder
import com.example.starbuckskotlin.base.BaseActivity
import com.example.starbuckskotlin.databinding.ActivityOcrBinding
import com.example.starbuckskotlin.util.LogUtil
import com.example.starbuckskotlin.util.PermissionUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class OcrTestActivity : BaseActivity() {
    private lateinit var binding: ActivityOcrBinding

    companion object {
        private const val GALLERY_PERMISSIONS_REQUEST = 0
        private const val GALLERY_IMAGE_REQUEST = 1

        private const val FILE_NAME = "photo.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ocr)

        binding.gallery.setOnClickListener {
            startGalleryChooser()
        }
    }

    private fun startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select a photo"), GALLERY_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            uploadImage(data.data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            GALLERY_PERMISSIONS_REQUEST ->
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser()
                }
        }
    }

    private fun uploadImage(uri: Uri?) {
        uri?.let {
            try {
                val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }
                saveBitmapToFile(bitmap, getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + File.separator + FILE_NAME)
                val file = File(getExternalFilesDir(Environment.DIRECTORY_DCIM).toString() + File.separator + FILE_NAME)
                val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                RetrofitBuilder.getApiInterface().getKakaoOcrResult("https://dapi.kakao.com/v2/vision/text/ocr","KakaoAK d6ce60616c246ceaa9d1977bc3c53fa2", requestBody).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        if (response.isSuccessful) {
                            response.body()?.let {
                                LogUtil.d(TAG, "getKakaoOcrResult() result : ${it.string()}")
                            }
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        LogUtil.d(TAG, "getKakaoOcrResult() Throwable : ${t.message}")
                        binding.testTextView.text = t.message
                    }
                })
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap, strFilePath: String) {
        val fileCacheItem = File(strFilePath)
        var out: OutputStream? = null
        try {
            fileCacheItem.createNewFile()
            out = FileOutputStream(fileCacheItem)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}