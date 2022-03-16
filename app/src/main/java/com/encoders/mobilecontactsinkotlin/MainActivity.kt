package com.encoders.mobilecontactsinkotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_ASK_PERMISSIONS = 123
    private val PICK_IMAGE = 100
    private lateinit var contact_list:TextView
    private lateinit var pick_contacts:AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contact_list = findViewById(R.id.contact_list)
        pick_contacts = findViewById(R.id.pick_contacts)

        pick_contacts.setOnClickListener {
            if (isContactPermissionGranted()) {
                Read_Contacts()
            }
        }
    }

    fun isContactPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                // Log.v(TAG, "Permission is granted")
                true
            } else {
                // Log.v(TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),
                    REQUEST_CODE_ASK_PERMISSIONS)
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //  Log.v(TAG, "Permission is granted")
            true
        }
    }

    private fun Read_Contacts() {

        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor = contentResolver.query(uri,null ,null,null,null)
        if (cursor!!.count > 0){
           while (cursor.moveToNext()){
               val contact_Name = cursor.getString(cursor.getColumnIndex( ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
               val contact_Mobile = cursor.getString(cursor.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER))
               contact_list.text = "$contact_Name - $contact_Mobile\n"
           }
        }else{
            contact_list.text = "Contact List is Empty"
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {



        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted, do your stuff
                Read_Contacts()
            } else {
                // Permission Denied
                Toast.makeText(this@MainActivity, "Contact Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}