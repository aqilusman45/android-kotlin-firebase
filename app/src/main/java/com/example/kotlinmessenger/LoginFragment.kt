package com.example.kotlinmessenger

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.kotlinmessenger.databinding.FragmentLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var phoneLogin : PhoneAuthProvider
    lateinit var binding: FragmentLoginBinding
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false )

        phoneLogin = PhoneAuthProvider.getInstance()

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.i("FirebaseAuth-onVerificationCompleted", p0.toString())
                createUserWithPhoneAuthCreds(p0)
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                Log.i("FirebaseAuth-onCodeAutoRetrievalTimeOut", p0.toString())
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText( activity ,"${p0.message}",Toast.LENGTH_LONG).show()

            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                Toast.makeText( activity ,"${p0}",Toast.LENGTH_LONG).show()
                val creds = bundleOf("creds" to p0)
            view!!.findNavController().navigate(R.id.action_loginFragment_to_otpFragment, creds)
            }
        }
        binding.signinButton.setOnClickListener{
            this.sendVerificationCode(binding.phoneNumberInputSignin.text.toString())
        }
        return binding.root
    }

    private fun sendVerificationCode(phoneNumber: String){
        phoneLogin.verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, Activity(),callbacks)
    }

    private fun createUserWithPhoneAuthCreds(creds: PhoneAuthCredential){
            auth.signInWithCredential(creds)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        view!!.findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                    }else{
                        if (it.exception is FirebaseAuthInvalidCredentialsException) Toast.makeText( activity ,"${creds.smsCode.toString()}",Toast.LENGTH_LONG).show()
                    }
                }
    }

}
