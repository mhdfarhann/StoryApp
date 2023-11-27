package com.farhan.storyapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.farhan.storyapp.R
import com.farhan.storyapp.auth.LoadingStateAdapter
import com.farhan.storyapp.auth.Result
import com.farhan.storyapp.data.response.ListStory
import com.farhan.storyapp.data.response.Story
import com.farhan.storyapp.databinding.ActivityMainBinding
import com.farhan.storyapp.view.ViewModelFactory
import com.farhan.storyapp.view.addStory.AddStoryActivity
import com.farhan.storyapp.view.addStory.StoryAdapter
import com.farhan.storyapp.view.login.LoginActivity
import com.farhan.storyapp.view.map.MapsActivity
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)

        binding.fabAddStory.setOnClickListener { moveToAddStory() }

        viewModel.getSession().observe(this) { user ->
            Log.wtf("user session", "User Token ${user.token}")
            if (!user.isLogin) {
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }
        setupData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = StoryAdapter()
        binding.rvStory.let {
            it.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
        }
    }

    private fun setupData() {
        viewModel.getSession().observe(this) { user ->
            if (user.token.isNotBlank()) {
                processGetAllStories(user.token)
            }
        }
    }

    private fun clearSession() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.sign_out)
            .setMessage(R.string.are_you_sure)
            .setPositiveButton(R.string.oke) { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }

        val alert = builder.create()
        alert.show()
    }

    private fun setListStory(pagingData: PagingData<Story>) {
        lifecycleScope.launch {
            adapter.submitData(pagingData)
        }
    }

    private fun processGetAllStories(token: String) {
        viewModel.getStories(token).observe(this) {
            setListStory(it)
        }
    }

    private fun moveToAddStory() {
        startActivity(Intent(this, AddStoryActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.exit -> {
                clearSession()
            }
            R.id.map -> {
                startActivity(Intent(this, MapsActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
