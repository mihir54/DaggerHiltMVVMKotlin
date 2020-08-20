package com.mihir.daggerhiltmvvmkotlin.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mihir.daggerhiltmvvmkotlin.R
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import kotlinx.android.synthetic.main.user_data.view.*

class UserPagedListAdapter : PagedListAdapter<UserEntity, UserPagedListAdapter.UserViewHolder>(diffCallback) {
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_data, parent,false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user=getItem(position)
        with(holder.itemView){
            txName1.text= user?.name ?: ""
            txMob1.text = user?.mobile_no.toString()
            txAddress1.text = user?.state
            txState1.text = user?.state
            txDistrict1.text = user?.district
            txCity1.text= user?.city
            Glide.with(context)
                .load(user?.imageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(imvProfile1)
        }
    }


    companion object{
        private val diffCallback = object : DiffUtil.ItemCallback<UserEntity>(){
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
                oldItem.id==newItem.id

            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
                oldItem==newItem
        }
    }
}