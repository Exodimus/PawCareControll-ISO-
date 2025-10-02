import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class ImageUploadHelper(private val fragment: Fragment, private val btnImage: TextView, private val imageView: ImageView) {

    var pickedPhoto: Uri? = null
    var pickedBitmap: Bitmap? = null

    init {
        btnImage.setOnClickListener {
            pickPhoto()
        }
    }

    fun pickPhoto() {
        if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                fragment.requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            fragment.startActivityForResult(galleryIntent, 2)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickPhoto()
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            pickedPhoto = data.data
            if (pickedPhoto != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source =
                        ImageDecoder.createSource(fragment.requireContext().contentResolver, pickedPhoto!!)
                    pickedBitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(pickedBitmap)
                } else {
                    pickedBitmap = MediaStore.Images.Media.getBitmap(
                        fragment.requireContext().contentResolver,
                        pickedPhoto
                    )
                    imageView.setImageBitmap(pickedBitmap)
                }
            }
        }
    }
}
