package com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.Point
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Display
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.AlbumAdapter
import com.example.matrimony.databinding.ActivityAlbumBinding
import com.example.matrimony.db.entities.Album
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AlbumActivity : AppCompatActivity(), OpenCameraDialogFragment.Companion.ButtonClickListener {

    private val albumViewModel by viewModels<AlbumViewModel>()
    private val userProfileViewModel by viewModels<UserProfileViewModel>()

    lateinit var binding: ActivityAlbumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album)

        Log.i(TAG, "loaded ${albumViewModel.loaded}")
        albumViewModel.loaded = true

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val sharedPref =
            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE) ?: return

        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        loadAlbum()
    }

    private val albumList = mutableListOf<Album>()
    private var adapter: AlbumAdapter? = null

    private fun loadAlbum() {
        Log.i(TAG, "album act load album")

        val albumRecyclerView = binding.rvAlbum
        albumRecyclerView.layoutManager = GridLayoutManager(this, 1)
        adapter = AlbumAdapter(userProfileViewModel, albumViewModel, addImage,::getPermission,::checkForPermission)
        albumRecyclerView.adapter = adapter


        lifecycleScope.launch {
            albumViewModel.getUserAlbum(userProfileViewModel.userId)
                .observe(this@AlbumActivity) { album ->
                    albumList.clear()
                    album?.forEach {
                        if (it != null)
                            if (it.isProfilePic)
                                albumList.add(0, it)
                            else
                                albumList.add(it)
                    }

                    adapter?.setList(albumList)
                }

        }

        Log.i(TAG, "Inside AlbumAct getProfilePic")
        registerForContextMenu(albumRecyclerView)
    }

    private val addImage = {
        val dialog = OpenCameraDialogFragment()
        dialog.show(supportFragmentManager, null)

    }

    override fun onButtonClick(clickedItem: String) {

        getPermission()
        if (checkForPermission()) {

            when (clickedItem) {
                "camera" -> {
//                getPermission()
                    Log.i(TAG, "Check Permission ${checkForPermission()}")
//                if (checkForPermission())
                    launchCamera()

                }
                "gallery" -> {
                    startGalleryIntent()
                }
            }
        }
    }

    private fun startGalleryIntent() {
//        if (!hasGalleryPermission()) {
//            askForGalleryPermission()
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if(!hasImagesPermission()){
//                askForImagesPermission()
//
//            }
//        }

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_CHOOSE)
    }

    // Define a constant for the request code
//    private val REQUEST_IMAGE_CAPTURE = 1

    // Define a variable to store the URI of the captured image
    private var imageUri: Uri? = null

    // Function to launch the camera app
    private fun launchCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        imageUri = this.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (intent.resolveActivity(packageManager) != null) {
//            val photoFile: File?
//            val photoFileName = "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}"
//            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            try {
//                photoFile = File.createTempFile(photoFileName, ".jpg", storageDir)
//                imageUri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileprovider", photoFile)
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
//                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
//            } catch (ex: IOException) {
//                ex.printStackTrace()
//            }


    }

    // Function to get the file path of an image URI
    private fun getRealPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(contentUri!!, proj, null, null, null)
        val columnIndex: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(columnIndex!!).also { cursor?.close() }
    }

    // Function to load a new image from file path
    private fun loadNewImage(filePath: String) {
//        val bitmap = BitmapFactory.decodeFile(filePath)
//        lifecycleScope.launch {
//            val album = (Album(0, userProfileViewModel.userId, bitmap, false))
//            albumViewModel.addAlbum(album)
//            albumList.add(album)
//            adapter?.notifyDataSetChanged()
//        }
        Log.i(TAG, "load image: $filePath")
        var mBitmap = BitmapFactory.decodeFile(filePath)
        Log.d("Images New ", mBitmap.toString())
        val display: Display = this.windowManager.getDefaultDisplay()
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y
        Log.i(TAG, "bitmap: " + mBitmap!!.getWidth() + " " + mBitmap!!.getHeight())
        val maxP = Math.max(mBitmap!!.getWidth(), mBitmap!!.getHeight())
        val scale1280 = maxP.toFloat() / 1280
        Log.i(TAG, "scaled: " + scale1280 + " - " + 1 / scale1280)
//        mImageView!!.maxZoom = width * 2 / 1280f
        mBitmap = Bitmap.createScaledBitmap(
            mBitmap,
            (mBitmap.getWidth() / scale1280).toInt(),
            (mBitmap.getHeight() / scale1280).toInt(),
            true
        )
        val exif = ExifInterface(filePath)
        val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(270f)
            else -> return
        }
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.width, mBitmap.height, matrix, true)



        lifecycleScope.launch {
            val album = (Album(0, userProfileViewModel.userId, mBitmap, false))
            albumViewModel.addAlbum(album)
            albumList.add(album)
            adapter?.notifyDataSetChanged()
        }
//        mImageView!!.setImageBitmap(mBitmap)
    }

    // Function to delete an image by its URI
    private fun deleteImage(uri: Uri?) {
        if (uri != null) {
            val contentResolver = this.contentResolver
            contentResolver.delete(uri, null, null)
        }
    }

    // Override the onActivityResult() function to get the result of the camera app
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the result is OK
        if (resultCode == Activity.RESULT_OK) {
            // Check if the request code matches the image capture request code
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                // Get the file path of the captured image
                val path = getRealPathFromURI(imageUri)!!
//                addPostViewModel.galleryImagePath = path
//                galleryViewRecyclerAdapter.cursor = getImagesCursor()!!
//                galleryViewRecyclerAdapter.notifyDataSetChanged()
//                loadNewImage(path)
//                val path = getRealPathFromURI(imageUri)!!
                // Load the new image from file path
                loadNewImage(path)
            }
            else if(requestCode == REQUEST_IMAGE_CHOOSE){
                imageUri=data?.data
                imageUri?.let {
                    val filePath=getRealPathFromURI(imageUri)
                    filePath?.let {
                        loadNewImage(filePath)
                    }
                }
            }
        } else {
            // If the result is not OK, delete the captured image if it exists
            deleteImage(imageUri)
            imageUri = null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }


    private fun getPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQUEST_PERMISSION
                );

            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSION
                );

            }
        }
    }

    private fun checkForPermission(): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            return ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent(this, AddPostActivity::class.java).apply {
//                    startActivity(this)
//                }
                //return
            } else {
                openPermissionNeededDialog()
            }
        }
    }

    private fun openPermissionNeededDialog() {
        val mediaPermissionDialogFragment = MediaPermissionDialogFragment()
        mediaPermissionDialogFragment.show(supportFragmentManager, "permissiondialog")
    }


    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_IMAGE_CHOOSE = 2
        const val REQUEST_PERMISSION = 3
    }
}