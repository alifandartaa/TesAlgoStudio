package com.example.tesalgostudio.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tesalgostudio.R
import com.example.tesalgostudio.adapter.MainAdapter
import com.example.tesalgostudio.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val mainAdapter = MainAdapter()
    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAlertDialog()

        viewModel.memesResult.observe(this, { memesFromApi->
            mainAdapter.setDataMeme(memesFromApi)
        })

        binding.srMemes.setOnRefreshListener {
            binding.srMemes.isRefreshing = false
            viewModel.getMemesFromApi()
        }

        viewModel.loading.observe(this, {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        })

        binding.rvMemes.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = mainAdapter
        }
    }

    private fun setupAlertDialog() {
        loadingDialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setView(R.layout.custom_progress_dialog)
            .create()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}