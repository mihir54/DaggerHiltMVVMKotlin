package com.mihir.daggerhiltmvvmkotlin.ui.profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.mihir.daggerhiltmvvmkotlin.R
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import com.mihir.daggerhiltmvvmkotlin.ui.main.UserListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.user_data.*
import kotlinx.android.synthetic.main.user_data.view.*
import java.io.File

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private val profileViewModel: ProfileViewModel by viewModels()
    var stateMap = hashMapOf<String,Int>()
    var districtMap = hashMapOf<String,Int>()
    private var mProfileFile: String? =null
    companion object{
        private const val PROFILE_IMAGE_REQ_CODE = 101
        private const val TAG="ProfileActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        profileViewModel.getStateData()

        buttonSubmit1.setOnClickListener {
            val user=UserEntity(
                name = edtName1.text.toString(),
                mobile_no = edtMob1.text.toString().toLong(),
                address = edtAddress1.text.toString(),
                state = spnState1.selectedItem.toString(),
                district = spnDistrict1.selectedItem.toString(),
                city = spnCity1.selectedItem.toString(),
                imageUri = mProfileFile)
            profileViewModel.insertUser(user)
            startActivity(Intent(this,UserListActivity::class.java))
        }

        profileViewModel.stateListLiveData.observe(this, Observer {
            val stateAdapter = ArrayAdapter<String>(applicationContext,android.R.layout.simple_spinner_item,it)
            stateAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            with(spnState1){
                setSelection(stateAdapter.getPosition("Select state"))
                adapter = stateAdapter
                setSelection(0,false)
                prompt = "Select state"
                gravity = Gravity.CENTER

            }
        })

        profileViewModel.stateMapLiveData.observe(this, Observer {
            stateMap=it
        })

        profileViewModel.districtListLiveData.observe(this, Observer {

            val districtAdapter= ArrayAdapter<String>(applicationContext,android.R.layout.simple_spinner_item,it)
            districtAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            with(spnDistrict1){
                adapter = districtAdapter
                setSelection(0,false)
                prompt = "Select district"
                gravity= Gravity.CENTER
            }
        })

        profileViewModel.districtMapLiveData.observe(this, Observer {
            districtMap = it
        })

        spnState1.onItemSelectedListener =object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val state = parent?.getItemAtPosition(position).toString()
                val stateId= stateMap[state]
                profileViewModel.getDistrictData(stateId)
                Toast.makeText(applicationContext, state,Toast.LENGTH_SHORT).show()
            }
        }
        spnDistrict1.onItemSelectedListener= object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val district = parent?.getItemAtPosition(position).toString()
                val districtId = districtMap[district]
                profileViewModel.getCitiesByDistrictId(districtId)
            }
        }

        profileViewModel.cityListLiveData.observe(this, Observer {
            ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,it)
                .also { cityAdapter ->
                    cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spnCity1.adapter= cityAdapter
            }
            spnCity1.gravity= Gravity.CENTER

        })
        spnCity1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(applicationContext, parent?.getItemIdAtPosition(position).toString(),Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun pickImage(view: View) {
        ImagePicker.with(this)
            .setImageProviderInterceptor { imageProvider ->
                Log.d("ImagePicker" ,"Selected image provide: "+imageProvider.name)
            }
            .maxResultSize(512,512)
            .start(PROFILE_IMAGE_REQ_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                Log.e("TAG", "Path:${ImagePicker.getFilePath(data)}")
                mProfileFile=ImagePicker.getFilePath(data)
                val file = ImagePicker.getFile(data)
                when(requestCode){
                    PROFILE_IMAGE_REQ_CODE -> {

                        Log.i(TAG, "mProfileFile: $mProfileFile");
                        Glide.with(this)
                            .load(file)
                            .apply(RequestOptions.circleCropTransform())
                            .into(imgProfile1)
                    }
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(application,ImagePicker.getError(data),Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(application,"Task cancelled",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        profileViewModel.onDestroy()
    }
}




