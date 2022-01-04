package com.example.tesalgostudio.ui

import android.app.Activity
import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.drawToBitmap
import com.bumptech.glide.Glide
import com.example.tesalgostudio.R
import com.example.tesalgostudio.databinding.ActivityDetailBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!
    private var uriImage: Uri? = null
    private var imageCounter = 1

    companion object {
        private const val TAG = "DetailActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val urlMeme = intent.getStringExtra("EXTRA_URL")

        Glide.with(this)
            .load(urlMeme)
            .centerCrop()
            .into(binding.ivMeme)

        val imagePickLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    createImageView(result.data?.data)
                    Log.d(TAG, "attachImage: ${result.data?.data}")
                }
            }

        binding.btnAddLogo.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickLauncher.launch(intent)
        }

        binding.btnAddText.setOnClickListener {
            showDialogAddText()
        }

        binding.btnSaveImage.setOnClickListener {
            val bitmap = binding.flMeme.drawToBitmap()
            val wrapper = ContextWrapper(applicationContext)
            var file = wrapper.getDir("images", Context.MODE_PRIVATE)
            file = File(file, "${UUID.randomUUID()}.jpg")
            try {
                val output = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 85, output)
                output.flush()
                output.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val savedImageURL = MediaStore.Images.Media.insertImage(
                contentResolver,
                bitmap,
                "tes",
                "Image of tes"
            )
            uriImage = Uri.parse(savedImageURL)
            Toast.makeText(this, savedImageURL.toString(), Toast.LENGTH_SHORT).show()
            binding.btnShareFacebook.isEnabled = true
            binding.btnShareTwitter.isEnabled = true
        }

        binding.btnShareTwitter.setOnClickListener {
            val intent: Intent? = packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                try{
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.setPackage("com.twitter.android")
                    shareIntent.type = "image/*";
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uriImage);
                    startActivity(shareIntent)
                }catch(anfe: ActivityNotFoundException){
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.twitter.android")));
                }
            }
        }

        binding.btnShareFacebook.setOnClickListener {
            val intent: Intent? = packageManager.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                    try{
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.setPackage("com.facebook.katana")
                        shareIntent.type = "image/*";
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uriImage);
                        startActivity(shareIntent)
                    }catch (anfe: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.facebook.katana")));
                    }
            }
        }
    }

    private fun createTextView(newText: String) {
        val textGenerated = TextView(this)
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(10, 10, 10, 10)
        textGenerated.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30F)
        textGenerated.text = newText
        binding.flMeme.addView(textGenerated)
    }

    private fun createImageView(newImage: Uri?) {
        val imageGenerated = ImageView(this)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(10, 10, 10, 10)
        imageGenerated.layoutParams = params
        binding.flMeme.addView(imageGenerated)
        Glide.with(this)
            .load(newImage)
            .into(imageGenerated)
        if(imageCounter >= 2){
            binding.btnSaveImage.isEnabled = true
            imageCounter++
        }else{
            imageCounter++
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showDialogAddText() {
        val builder = AlertDialog.Builder(this)
        val view = View.inflate(this, R.layout.dialog_add_text, null)
        builder.setView(view)
            .setPositiveButton("Ya", DialogInterface.OnClickListener { _, i ->
                createTextView(view.findViewById<EditText>(R.id.et_add_text).text.toString())
                if(imageCounter >= 2){
                    binding.btnSaveImage.isEnabled = true
                    imageCounter++
                }else{
                    imageCounter++
                }
            })
            .setNegativeButton("Tidak", DialogInterface.OnClickListener { dialogInterface, _ ->
                dialogInterface.cancel()
            })
        builder.create()
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}