package com.example.kotlinmessenger


import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import com.example.kotlinmessenger.databinding.FragmentOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentOtpBinding
    lateinit var phoneLogin : PhoneAuthProvider
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    internal val countDown: Long = 30000
    internal val countDownInterval: Long = 1000
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startTimer()
//          activity.supportFragmentManager(container,inflater, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        phoneLogin = PhoneAuthProvider.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false)
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                createUserWithPhoneAuthCreds(p0)
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText( activity ,"${p0.message}",Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                Toast.makeText( activity ,"Code Sent",Toast.LENGTH_LONG).show()
            }
        }
        binding.otpResendButton.visibility = Button.INVISIBLE
        binding.otpResendButton.setOnClickListener{
            startTimer()
            binding.otpResendButton.visibility = Button.INVISIBLE
                this.sendVerificationCode(LoginFragment.State.phone)
        }
        binding.otpSubmitButton.setOnClickListener{
            val credential = PhoneAuthProvider.getCredential(LoginFragment.State.creds, "${binding.otpCodeEdittext.text}")
            this.createUserWithPhoneAuthCreds(credential)
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
                    Toast.makeText( activity ,"Logged In",Toast.LENGTH_LONG).show()
                    view!!.findNavController().navigate(R.id.action_otpFragment_to_dashboardFragment)
                }else{
                    if (it.exception is FirebaseAuthInvalidCredentialsException) Toast.makeText( activity ,"${creds.smsCode.toString()}",Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun startTimer (){
        countDownTimer =  object : CountDownTimer( countDown, countDownInterval ) {
            override fun onTick(millisUntilFinished: Long) {
                binding.otpTimerText.text = "${millisUntilFinished/1000}"
            }

            override fun onFinish() {
                    binding.otpResendButton.visibility = Button.VISIBLE
                    binding.otpTimerText.visibility = TextView.INVISIBLE
            }
        }.start()
    }
}
