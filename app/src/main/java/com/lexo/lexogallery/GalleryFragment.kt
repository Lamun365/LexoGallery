package com.lexo.lexogallery

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lexo.lexogallery.databinding.FragmentGalleryBinding
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class GalleryFragment : Fragment(), ImageAdapter.ItemClickListener {
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val storageReq = 101
    private lateinit var images: ArrayList<ImageData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        checkPermission(activity?.applicationContext!!)
//        if (activity is MainActivity) {
//            val mainActivity = activity as MainActivity
//        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun checkPermission(context: Context) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), storageReq)
            }
            else {
                //load image
                loadImage()
            }
        }
        else {
            loadImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == storageReq) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity?.applicationContext!!, "Permission grant", Toast.LENGTH_SHORT).show()
                loadImage()
            }
            else {
                Toast.makeText(activity?.applicationContext!!, "Permission denied", Toast.LENGTH_SHORT).show()
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), storageReq)
            }
        }

    }

    //type real code here

    private fun listOfAllImages(context: Context): ArrayList<ImageData> {
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
            )
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }
        val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_MODIFIED
        )
        val orderBy = MediaStore.Images.Media._ID
        val imgData = arrayListOf<ImageData>()
        val cursor = context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                orderBy
        )

        while (cursor?.moveToNext() == true) {
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val dateColumns = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)

            val size = cursor.getInt(sizeColumn)
            val name = cursor.getString(nameColumn)
            val id = cursor.getLong(idColumn)
            val date = cursor.getLong(dateColumns)
            val imgUri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            imgData += ImageData(imgUri, name, size, date)
        }
        cursor?.close()
        return imgData
    }

    private fun loadImage() {
        images = listOfAllImages(activity?.applicationContext!!)
        binding.recycleView.adapter = ImageAdapter(activity?.applicationContext!!, images, this)
        binding.recycleView.layoutManager = GridLayoutManager(activity?.applicationContext!!, 3)
        binding.recycleView.setHasFixedSize(true)
    }
    private fun imgSize(size: Int): String {
        val mb: Int = size / (1024 * 1024)
        return if (mb == 0) "${(size / 1024)} KB" else "$mb MB"
    }
    fun convertLongToDate(time: Long): String =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter.ofPattern("dd MMMM yyyy").format(
                        Instant.ofEpochMilli(time*1000)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate())
            } else {
                SimpleDateFormat("dd MMMM yyyy").format(
                        Date(time * 1000)
                )
            }

    override fun onClick(item: Int) {
        val image = images[item]
        val date = convertLongToDate(image.date)
        val action = GalleryFragmentDirections.actionGalleryFragToImageFragment(image.uri.toString(), image.displayName, imgSize(image.size), date)
        findNavController().navigate(action)
    }
}