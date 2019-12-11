package com.example.kotlinmessenger


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.kotlinmessenger.databinding.FragmentOtpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

/**
 * A simple [Fragment] subclass.
 */
class OtpFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        val binding = DataBindingUtil.inflate<FragmentOtpBinding>(inflater, R.layout.fragment_otp, container, false)
        binding.otpSubmitButton.setOnClickListener{
            Toast.makeText(activity, "hahaha ${arguments!!.getString("creds").toString()}, ${binding.otpCodeEdittext.text} ", Toast.LENGTH_LONG).show()
            val credential = PhoneAuthProvider.getCredential(arguments!!.getString("creds").toString(), "${binding.otpCodeEdittext.text}")
            this.createUserWithPhoneAuthCreds(credential)
        }

        return binding.root
    }

    private fun createUserWithPhoneAuthCreds(creds: PhoneAuthCredential){
        auth.signInWithCredential(creds)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText( activity ,"${auth.currentUser!!.uid}",Toast.LENGTH_LONG).show()
                    view!!.findNavController().navigate(R.id.action_otpFragment_to_dashboardFragment)
                }else{
                    if (it.exception is FirebaseAuthInvalidCredentialsException) Toast.makeText( activity ,"${creds.smsCode.toString()}",Toast.LENGTH_LONG).show()
                }
            }
    }

}
