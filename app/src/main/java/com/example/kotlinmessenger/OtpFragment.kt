package com.example.kotlinmessenger


import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.kotlinmessenger.databinding.FragmentOtpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentOtpBinding
    internal  var phone: String = ""
    private lateinit var countDownTimer: CountDownTimer
    internal val countDown: Long = 30000
    internal val countDownInterval: Long = 1000
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startTimer()
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_otp, container, false)
        binding.otpResendButton.visibility = Button.INVISIBLE
        binding.otpSubmitButton.setOnClickListener{
            val credential = PhoneAuthProvider.getCredential(arguments!!.getString("creds").toString(), "${binding.otpCodeEdittext.text}")
            this.createUserWithPhoneAuthCreds(credential)
        }
        return binding.root
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
