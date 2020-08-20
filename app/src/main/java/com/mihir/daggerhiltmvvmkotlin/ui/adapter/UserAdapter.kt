package com.mihir.daggerhiltmvvmkotlin.ui.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mihir.daggerhiltmvvmkotlin.R
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import com.mihir.daggerhiltmvvmkotlin.ui.adapter.UserAdapter.UserViewHolder
import com.mihir.daggerhiltmvvmkotlin.ui.profile.ProfileActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.user_data.view.*
import java.util.*

class UserAdapter() : RecyclerView.Adapter<UserViewHolder>(){

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    companion object{
        const val TAG: String="UserAdapter"
    }

    private val differCallback = object : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem==newItem
        }
    }

    private val differCallbackFiltered = object : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)
    var userListFiltered = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_data,parent,false))
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        /*if (userListFiltered.currentList.isEmpty()){
            userListFiltered=differ
        }
        var users= userListFiltered.currentList[position]*/
        val users=  differ.currentList[position]
        holder.itemView.apply {
            txName1.text = users?.name
            txMob1.text = users?.mobile_no.toString()
            txAddress1.text = users?.state
            txState1.text = users?.state
            txDistrict1.text = users?.district
            txCity1.text= users?.city
            Glide.with(context)
                .load(users.imageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(imvProfile1)
         }
    }

    /*override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charString = constraint.toString()

                if (charString.isEmpty()){
                    Log.d(TAG, "charString: $charString")
                    userListFiltered=differ
                }else{
                    val filteredList = arrayListOf<UserEntity>()
                    for (row in differ.currentList){
                        if (row.name?.toLowerCase(Locale.ROOT)!!.contains(charString.toLowerCase(Locale.ROOT))
                            || row.mobile_no.toString().contains(charString)){
                            filteredList.add(row)
                            Log.d(TAG,"charString full ${row.name}")
                        }
                    }
                    userListFiltered.submitList(filteredList)
                }
                val filterResult = FilterResults()
                filterResult.values=userListFiltered
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                userListFiltered = results?.values as AsyncListDiffer<UserEntity>
//                notifyDataSetChanged()
            }

        }
        }
    */

}