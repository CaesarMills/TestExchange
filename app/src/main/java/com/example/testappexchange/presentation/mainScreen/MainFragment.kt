package com.example.testappexchange.presentation.mainScreen
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.testappexchange.R
import com.example.testappexchange.core.util.DataStatus
import com.example.testappexchange.core.util.toFour
import com.example.testappexchange.core.util.toTwo
import com.example.testappexchange.databinding.FragmentMainBinding
import com.example.testappexchange.presentation.adapters.CustomAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private val args by navArgs<MainFragmentArgs>()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var customAdapter = CustomAdapter()
    private var myList:Array<String> = emptyArray()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        getCurrencyInfoFromNetwork()
        getUserInfo()
        setupUserBalanceRecycleView()
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPresentation.text = "${args.UserLogin}, your balance:"

        viewModel.isOnline.observe(viewLifecycleOwner){
            Log.d("IsOnlineUi","$it")
            when(it){
                true -> {
                  binding.apply {
                      llCalculation.visibility = View.VISIBLE
                      llError.visibility = View.GONE
                  }
                }
                false ->{
                    binding.apply {
                        llCalculation.visibility = View.GONE
                        llError.visibility = View.VISIBLE
                    }
                }
            }
        }
        viewModel.currencyList.observe(viewLifecycleOwner){ it ->
            when(it){
                is DataStatus.Error -> Toast.makeText(requireContext(), "Some problem ${it.message}", Toast.LENGTH_SHORT).show()
                is DataStatus.Loading -> Toast.makeText(requireContext(), "Loading information from server", Toast.LENGTH_SHORT).show()
                is DataStatus.Success -> {
                    myList = it.data?.map {it.name}?.toTypedArray() ?: emptyArray()
                    val sellName = binding.tvSellCurrency.text.toString()
                    val buyName = binding.tvBuyCurrency.text.toString()
                     Log.d("CurrenciesName", "sell name - $sellName buy name - $buyName")
                    viewModel.calculateRate(sellCurrency = sellName, buyCurrency = buyName)
                }
            }

        }
        viewModel.rate.observe(viewLifecycleOwner){
            binding.tvRateInfo.text = it.toString()
        }
        viewModel.sellBalance.observe(viewLifecycleOwner){
            binding.tvBalance.text = it.toTwo().toString()
        }
        viewModel.fee.observe(viewLifecycleOwner){
            binding.tvFeeInfo.text = it!!.toFour().toString()
        }
        viewModel.buyBalance.observe(viewLifecycleOwner){
            binding.tvReceiveBalance.text = it.toTwo().toString()
        }
        viewModel.buyAmount.observe(viewLifecycleOwner){
            binding.tvReceive.text = it.toString()
        }
        viewModel.sellCurrencyName.observe(viewLifecycleOwner){
            binding.apply {
                tvSellCurrency.text = it
                tvSellCurrencyNameInfo.text = "1 $it ="
                tvInfoFeeCurrencyName.text = it
            }
        }
        viewModel.buyCurrencyName.observe(viewLifecycleOwner){
            binding.apply {
                tvBuyCurrency.text = it
                tvBuyCurrencyNameInfo.text = it
            }
        }
        viewModel.startSellCurrency.observe(viewLifecycleOwner){
            binding.tvSellCurrency.text = it
        }

        viewModel.startBuyCurrency.observe(viewLifecycleOwner){
            binding.tvBuyCurrency.text = it
        }

        viewModel.userInfo.observe(viewLifecycleOwner){
            customAdapter.submitList(it.data!!.currencies!!.toList())
        }


        setupView()
    }

    @SuppressLint("ResourceAsColor")
    private fun setupView(){
        binding.apply {
            imSelectSell.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Chose currency to sell")
                    .setSingleChoiceItems(
                        myList, -1
                    ) { dialog, which ->
                        val chosenSellCurrency = myList[which]
                        val buyCurrencyName = tvBuyCurrency.text.toString()
                        val inputAmount = binding.edSellCurrency.text.toString()
                        viewModel.setCurrencyName(sellName = chosenSellCurrency , buyName = buyCurrencyName)
                        if(inputAmount.isNotEmpty()){
                            viewModel.calculateRate(chosenSellCurrency,buyCurrencyName)
                            viewModel.calculateSellBalance(chosenSellCurrency)
                            viewModel.calculateExchange(inputAmount.toDouble().toTwo())
                            setupExchangeButton()
                        }else{
                            viewModel.calculateSellBalance(chosenSellCurrency)
                            viewModel.calculateExchange(0.0)
                        }
                        dialog.dismiss()
                    }
                    .show()

            }
            imSelectBuy.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Chose currency to buy")
                    .setSingleChoiceItems(myList, -1) { dialog, which ->
                        val chosenBuyCurrency = myList[which]
                        val sellCurrencyName = tvSellCurrency.text.toString()
                        viewModel.setCurrencyName(sellName = sellCurrencyName, buyName = chosenBuyCurrency)
                        viewModel.calculateRate(sellCurrencyName,chosenBuyCurrency)
                        viewModel.calculateBuyBalance(chosenBuyCurrency)
                        val inputAmount = binding.edSellCurrency.text
                        if(!inputAmount.isNullOrEmpty()){
                            viewModel.calculateExchange(inputAmount.toString().toDouble().toTwo())
                        }else{
                            viewModel.calculateBuyBalance(chosenBuyCurrency)
                            viewModel.calculateExchange(0.0)
                        }
                        dialog.dismiss()
                    }
                    .show()
            }
            edSellCurrency.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    val inputAmount = p0.toString()
                        if(inputAmount.isNotEmpty()){
                            viewModel.calculateExchange(inputAmount.toDouble().toTwo())
                        }else{
                            viewModel.calculateExchange(0.0)
                        }
                        setupExchangeButton()
                    }


            })
            btnMax.setOnClickListener {
                if(viewModel.startBalanceSell.value!! > 0.0){
                    val maxAmount = viewModel.startBalanceSell.value
                    edSellCurrency.setText(maxAmount.toString())
                    viewModel.calculateExchange(maxAmount!!)
                } else {
                    edSellCurrency.setText(0.0.toString())
                    Toast.makeText(requireContext(), "You have 0 balance", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    private fun getUserInfo(){
        val login = args.UserLogin
        viewModel.getUserInfo(login)
    }

    private fun setupUserBalanceRecycleView() {
        binding.rvBalance.apply {
            adapter = customAdapter
        }
    }
    private fun setupExchangeButton(){
        val balance = viewModel.startBalanceSell.value
        val inputAmount = binding.edSellCurrency.text
        if (inputAmount.isNullOrEmpty()){
            viewModel.calculateExchange(0.0)
            return
        } else{
            binding.apply {
                if(balance!! > 0){
                    btnExchange.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.dark_blue)
                    btnExchange.text = "Exchange"
                    btnExchange.setOnClickListener {
                        if(inputAmount.isNotEmpty() && (inputAmount.toString().toDouble() != 0.0)){
                            Log.d("Input","$inputAmount")
                            viewModel.updateAccountBalance(inputAmount.toString().toDouble().toTwo())
                        }else{
                            return@setOnClickListener
                        }

                    }
                }
                if(balance == 0.0 || inputAmount.toString().toDouble()>balance ){
                    btnExchange.backgroundTintList = ContextCompat.getColorStateList(requireContext(),R.color.light_red)
                    btnExchange.text = "Out of limit"
                    btnExchange.setOnClickListener {
                        Toast.makeText(requireContext(), "Out of limit", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun getCurrencyInfoFromNetwork() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.getCurrencies()
            }
        }
    }

    }


