package com.farhan.storyapp.view.addStory

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.farhan.storyapp.R
import com.farhan.storyapp.auth.Result
import com.farhan.storyapp.databinding.ActivityAddStoryBinding
import com.farhan.storyapp.di.getImageUri
import com.farhan.storyapp.di.reduceFileImage
import com.farhan.storyapp.di.uriToFile
import com.farhan.storyapp.view.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient

class AddStoryActivity : AppCompatActivity() {
        private lateinit var binding: ActivityAddStoryBinding
        private var currentImageUri: Uri? = null

        private val viewModel by viewModels<AddStoryViewModel> {
            ViewModelFactory.getInstance(this)
        }

         private lateinit var fusedLocationClient: FusedLocationProviderClient

        private var lat: String? = null
        private var lon: String? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityAddStoryBinding.inflate(layoutInflater)
            setContentView(binding.root)

            supportActionBar?.elevation = 0f
            supportActionBar?.setTitle(R.string.upload_story)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding.btnGallery.setOnClickListener { startGallery() }
            binding.btnCamera.setOnClickListener { startCamera() }
            binding.buttonUpload.setOnClickListener { uploadImage() }

        }



        private fun startGallery(){
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        private var launcherGallery = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null){
                currentImageUri = uri
                showImage()
            } else {
                Log.d("Photo Picker", "No Media Selected")
            }
        }

        private fun showImage(){
            currentImageUri?.let {
                Log.d("Image URI", "showImage: $it")
                binding.ivItemImage.setImageURI(it)
            }
        }

        private fun startCamera(){
            currentImageUri = getImageUri(this)
            launcherIntentCamera.launch(currentImageUri)
        }

        private val launcherIntentCamera = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { isSuccess ->
            if (isSuccess) {
                showImage()
            }
        }


        private fun uploadImage() {
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                Log.d("Image File", "showImage: ${imageFile.path}")
                val description = binding.edAddDescription.text.toString()


                showLoading(true)

                viewModel.getSession().observe(this) { user ->
                    viewModel.addNewStory(user.token, description, imageFile).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                    binding.buttonUpload.isEnabled = false
                                    binding.btnGallery.isEnabled = false
                                }

                                is Result.Success -> {
                                    showLoading(false)
                                    binding.buttonUpload.isEnabled = true
                                    binding.btnGallery.isEnabled = true
                                    showToast(getString(R.string.add_new_story_success))
                                    finish()
                                }

                                is Result.Error -> {
                                    showLoading(false)
                                    binding.buttonUpload.isEnabled = true
                                    binding.btnGallery.isEnabled = true
                                    showToast(getString(R.string.add_new_story_failed))
                                }
                            }
                        }
                    }
                }
            } ?: showToast(getString(R.string.empty_image_warning))
        }
        private fun showLoading(isLoading: Boolean) {
            binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        private fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

}