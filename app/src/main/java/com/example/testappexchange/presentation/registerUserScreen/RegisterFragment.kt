package com.example.testappexchange.presentation.registerUserScreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testappexchange.R
import com.example.testappexchange.core.util.isValidEmail
import com.example.testappexchange.data.local.models.UserAccountEntity
import com.example.testappexchange.data.local.models.UserProfileEntity
import com.example.testappexchange.databinding.FragmentRegisterBinding
import com.example.testappexchange.domain.models.BalanceCurrency
import com.example.testappexchange.domain.models.Currency
import com.example.testappexchange.presentation.mainScreen.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        setupViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupViews(){
        binding.apply {

            edLogin.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    txLayoutLogin.helperText = validName()
                } else {
                    txLayoutLogin.helperText = ""
                }
            }

            edPin.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    txLayoutPinCode.helperText = validPinCode()
                } else {
                    edPin.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                        override fun afterTextChanged(p0: Editable?) {
                            txLayoutPinCode.helperText = validPinCode()
                        }
                    })
                }
            }

            edMail.setOnFocusChangeListener { _, focused ->
                if (!focused) {
                    txLayoutMail.helperText = validMail()
                } else {
                    txLayoutMail.helperText = ""
                }
            }

            btnLogin.setOnClickListener {
                val inputName = edLogin.text.toString().trim()
                if (!validUserInformation()) {
                    Toast.makeText(
                        requireContext(),
                        "You need correctly input all fields ",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val isLoginExist = viewModel.isLoginExist(inputName)
                        withContext(Dispatchers.Main){
                            if (isLoginExist) {
                                Toast.makeText(
                                    requireContext(),
                                    "User with this login is already exist",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                saveUserProfile()
                                saveUserAccount()
                                Toast.makeText(
                                    requireContext(),
                                    "User successful register",
                                    Toast.LENGTH_SHORT
                                ).show()
                                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToMainFragment(inputName))
                            }
                        }
                    }
                }
            }

        }
    }

    private fun validName(): String {
        val name = binding.edLogin.text.toString().trim()
        if (name.isEmpty()) {
            return "Required input your login name"
        }
        if (name.length<4){
            return "Minimum 4 characters"
        }
        else
            return "Done"
    }

    private fun validMail(): String {
        val mail = binding.edMail.text.toString().trim()
        if (!mail.isValidEmail()) {
            return "Invalid mail"
        }
        if(mail.isEmpty()){
            return "Required input your mail"
        }
        else
            return "Done"

    }

    private fun validPinCode(): String {
        val pinCode = binding.edPin.text.toString().trim()
        return if (pinCode.length<4) {
            "Minimum 4 characters"
        }else
            "Done"
    }

    private fun validUserInformation(): Boolean{
        return validPinCode() == "Done" &&
                validName() == "Done" &&
                validMail() == "Done"
    }

    private fun saveUserProfile(){
        val inputLogin = binding.edLogin.text.toString().trim()
        val inputMail = binding.edMail.text.toString().trim()
        val inputPin = binding.edPin.text.toString().trim()
        val newUser: UserProfileEntity = UserProfileEntity(0, login = inputLogin, mail = inputMail, pin = inputPin)
        viewModel.saveUserProfile(newUser)
    }
    private fun saveUserAccount(){
        val inputLogin = binding.edLogin.text.toString().trim()
        val newUserCurrencyBalance: List<BalanceCurrency> = listOf(BalanceCurrency("EUR", 1000.0))
        val newUser: UserAccountEntity = UserAccountEntity(0,  login = inputLogin, currencies = newUserCurrencyBalance, transactionNum = 1 )
        viewModel.saveUserAccount(newUser)
    }

}