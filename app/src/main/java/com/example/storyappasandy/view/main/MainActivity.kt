package com.example.storyappasandy.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyappasandy.R
import com.example.storyappasandy.data.api.ListStoryItem
import com.example.storyappasandy.databinding.ActivityMainBinding
import com.example.storyappasandy.maps.MapsActivity
import com.example.storyappasandy.view.ViewModelFactory
import com.example.storyappasandy.view.adapter.StoryListAdapter
import com.example.storyappasandy.view.camera.PostingActivity
import com.example.storyappasandy.view.detail.DetailActivity
import com.example.storyappasandy.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>{
        ViewModelFactory.getInstance(this)
    }

    private lateinit var adapter: StoryListAdapter

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {


            } else {

                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        adapter = StoryListAdapter()


        adapter.setOnItemClickListener(object : StoryListAdapter.OnItemClickListener{
            override fun onItemClick(user: ListStoryItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("detail", user)
                startActivity(intent)
            }

        })

        viewModel.stories.observe(this){
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = adapter
            viewModel.stories.observe(this, {
                adapter.submitData(lifecycle,it)
            })

            binding.progressBar.visibility = View.GONE
        }


        binding.progressBar.visibility = View.VISIBLE

        binding.fabDetail.setOnClickListener{
            val intent = Intent(this@MainActivity, PostingActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logoutMenu -> {
                viewModel.logout()
            }

            R.id.maps_menu -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }



    override fun onResume() {
        super.onResume()
        adapter.refresh()
        binding.progressBar.visibility = View.GONE
    }


}