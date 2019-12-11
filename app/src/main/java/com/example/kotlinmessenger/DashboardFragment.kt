package com.example.kotlinmessenger


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.kotlinmessenger.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {
        lateinit var binding: FragmentDashboardBinding
        lateinit var auth : FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.signoutButton.setOnClickListener{
            auth.signOut()
            it.findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
        }
        return binding.root
    }


}
