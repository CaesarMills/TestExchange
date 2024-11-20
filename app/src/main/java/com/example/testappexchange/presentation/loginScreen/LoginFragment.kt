package com.example.testappexchange.presentation.loginScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testappexchange.R
import com.example.testappexchange.databinding.FragmentLoginBinding
import com.example.testappexchange.presentation.mainScreen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupAllViews()
        return binding.root
    }

    private fun setupAllViews() {
        binding.apply {
            tvRegister.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            btnLogin.setOnClickListener {
                val inputLogin = binding.edLogin.text.toString().trim()
                val inputPin = binding.edPinCode.text.toString().trim()
                lifecycleScope.launch(Dispatchers.IO) {
                    val isLoginCorrect = viewModel.loginIn(login = inputLogin, pin = inputPin)
                    withContext(Dispatchers.Main) {
                        if (isLoginCorrect) {
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment(inputLogin))
                        } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Wrong login or Pin",
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}