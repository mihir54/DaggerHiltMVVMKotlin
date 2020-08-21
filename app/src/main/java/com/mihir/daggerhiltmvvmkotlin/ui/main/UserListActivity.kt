package com.mihir.daggerhiltmvvmkotlin.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.mihir.daggerhiltmvvmkotlin.R
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import com.mihir.daggerhiltmvvmkotlin.ui.adapter.UserAdapter
import com.mihir.daggerhiltmvvmkotlin.ui.adapter.UserPagedListAdapter
import com.mihir.daggerhiltmvvmkotlin.ui.profile.ProfileActivity
import com.mihir.daggerhiltmvvmkotlin.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_user_list.*

@AndroidEntryPoint
class UserListActivity : AppCompatActivity() {

    private lateinit var searchView: EditText
    private val profileViewModel : ProfileViewModel by viewModels()
    private lateinit var rvAdapter: UserPagedListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        searchView = findViewById(R.id.searchView)
        setUpRecyclerView()

        /*profileViewModel.getAllUsers().observe(this, Observer<List<UserEntity>> {
            if (it.isNotEmpty())
                 rvAdapter.differ.submitList(it)
            else Toast.makeText(this,"users not available", Toast.LENGTH_SHORT).show()
        })*/

        Log.i("TAG", "second commit")
        println("My fifth commit")
        println("Experiment commit")

        profileViewModel.loadUsers()
        fab1.setOnClickListener {
            startActivity(Intent(this,ProfileActivity::class.java))
        }

        searchObservable()

        profileViewModel.userSearchListLiveData.observe(this, Observer {
            rvAdapter.submitList(it as PagedList<UserEntity>?)
        })
        profileViewModel.userPagedList.observe(this, Observer {
            rvAdapter.submitList(it)
        })
    }

    private fun searchObservable() {
        profileViewModel.searchUsers(searchView)
    }

    private fun setUpRecyclerView() {
        rvAdapter= UserPagedListAdapter()
        recyclerViewUser.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.loadUsers()
    }
}


