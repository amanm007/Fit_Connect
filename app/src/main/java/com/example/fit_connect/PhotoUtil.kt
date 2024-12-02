package com.example.fit_connect

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.UUID

object PhotoUtil {
    fun checkPermissions(activity: Activity?){
        if(Build.VERSION.SDK_INT < 23){
            return
        }
        if(ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), 0)
        }
    }

    private fun rotation(bitmap: Bitmap, path: String): Float{
        var ei : ExifInterface
        try {
            ei = ExifInterface(path)
        }
        catch(e: Exception){
            e.printStackTrace()
            return 0f
        }
        var orientation: Int = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if(orientation == ExifInterface.ORIENTATION_ROTATE_90){
            return 90f
        }
        else if(orientation == ExifInterface.ORIENTATION_ROTATE_180){
            return 180f
        }
        else if(orientation == ExifInterface.ORIENTATION_ROTATE_270){
            return 270f        }
        return 0f
    }

    //Get Bitmap for Take Picture
    fun getBitmap(context: Context, imgUri: Uri): Bitmap {
        var bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imgUri))
        val matrix = Matrix()
        val file = File(imgUri.path)
        val absolutePath = file.absolutePath

        matrix.setRotate(rotation(bitmap, absolutePath))
        var ret = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        return ret
    }

    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }
}