package com.example.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import java.util.*

interface GlobalState {
    var fbOtp: String
    var phone: String
}

class MainActivity : AppCompatActivity() {
    var state: Object = object {

    }  
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
} l
