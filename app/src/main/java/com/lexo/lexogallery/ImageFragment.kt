package com.lexo.lexogallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lexo.lexogallery.databinding.FragmentImageBinding

class ImageFragment : Fragment() {
    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!
    private val args: ImageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        val uri = Uri.parse(args.uriString)
        binding.singleImage.setImageURI(uri)

        binding.bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.galleryFrag -> {
                    val action1 = ImageFragmentDirections.actionImageFragmentToGalleryFrag()
                    findNavController().navigate(action1)
                    true
                }
                R.id.share_item -> {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_STREAM, uri)
                        type = "image/*"
                    }
                    activity?.startActivity(Intent.createChooser(shareIntent, null))
                    true
                }
                else -> {
                    val action2 = ImageFragmentDirections.actionImageFragmentToDetailsFrag(args.uriString, args.displayString, args.sizeString, args.date)
                    findNavController().navigate(action2)
                    true
                }
            }
        }


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    navController.addOnDestinationChangedListener { _, destination, _ ->
//
//        if(destination.id == R.id.first_fragment) {
//            // your intro fragment will hide your bottomNavigationView
//            bottomNavigationView.visibility = View.GONE
//        } else if (destination.id == R.id.second_fragment){
//            // your second fragment will show your bottomNavigationView
//            bottomNavigationView.visibility = View.VISIBLE
//        }
//    }

    

}