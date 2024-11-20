package com.example.testappexchange.presentation.mainScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testappexchange.core.util.DataStatus
import com.example.testappexchange.core.util.calculateFee
import com.example.testappexchange.core.util.toFour
import com.example.testappexchange.core.util.toTwo
import com.example.testappexchange.data.local.models.UserAccountEntity
import com.example.testappexchange.core.util.NetworkManager
import com.example.testappexchange.domain.models.BalanceCurrency
import com.example.testappexchange.domain.models.Currency
import com.example.testappexchange.domain.repositories.Repository
import com.example.testappexchange.domain.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val repository: Repository,
    private val userRepository: UserRepository,
    networkManager: NetworkManager
) : ViewModel() {

    private val _isOnline:LiveData<Boolean> = networkManager
    val isOnline: LiveData<Boolean> = _isOnline

    private var _currencyList = MutableLiveData<DataStatus<List<Currency>>>()
    val currencyList: LiveData<DataStatus<List<Currency>>> = _currencyList

    private var _userInfo = MutableLiveData<DataStatus<UserAccountEntity>>()
    val userInfo: LiveData<DataStatus<UserAccountEntity>> = _userInfo

    private val _startBalanceSell = MutableLiveData<Double>()
    val startBalanceSell: LiveData<Double> = _startBalanceSell
    private val startBalanceBuy = MutableLiveData<Double>()

    val sellCurrencyName = MutableLiveData<String>()
    val buyCurrencyName = MutableLiveData<String>()

    private var sellAmount = MutableLiveData<Double>()

    private var _sellBalance = MutableLiveData<Double>(-1.0)
    val sellBalance: LiveData<Double> = _sellBalance

    private var _buyAmount = MutableLiveData<Double>()
    val buyAmount: LiveData<Double> = _buyAmount

    private var _buyBalance = MutableLiveData<Double>(-1.0)
    val buyBalance: LiveData<Double> = _buyBalance

    private var _rate = MutableLiveData<Double>()
    val rate: LiveData<Double> = _rate

    private var _fee = MutableLiveData<Double?>()
    val fee: LiveData<Double?> = _fee

    private var _startSellCurrency = MutableLiveData("EUR")
    val startSellCurrency: LiveData<String?> = _startSellCurrency

    private var _startBuyCurrency = MutableLiveData("USD")
    val startBuyCurrency: LiveData<String?> = _startBuyCurrency

    private var currentTransactionNum = MutableLiveData<Int>()


    suspend fun getCurrencies() =
        repository.getCurrenciesList()
            .retry(predicate = { if(it is IOException){
                    _currencyList.postValue(DataStatus.Error( "Internet error"))
                    delay(5000)
                    return@retry true
                    }
                false
                }
            )
            .catch { _currencyList.postValue(DataStatus.Error(it.message ?: "Some error")) }
            .collectLatest { updatedList ->
                _currencyList.postValue(DataStatus.Success(data = updatedList))
            }

        fun getUserInfo(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getAccountInfoFlow(login)
                .catch { _userInfo.postValue(DataStatus.Error(it.message)) }
                .collectLatest {
                    _userInfo.postValue(DataStatus.Success(data = it))
                    currentTransactionNum.postValue(it.transactionNum)
                    if(sellBalance.value?.toInt() == -1 && buyBalance.value?.toInt() == -1 ){
                        _sellBalance.postValue(it.currencies.find { it.name == _startSellCurrency.value }?.balance ?: 0.0)
                        _startBalanceSell.postValue(it.currencies.find { it.name == _startSellCurrency.value }?.balance ?: 0.0)
                        _buyBalance.postValue(it.currencies.find { it.name == _startBuyCurrency.value }?.balance ?: 0.0)
                        startBalanceBuy.postValue(it.currencies.find { it.name == _startBuyCurrency.value }?.balance ?: 0.0)
                        sellCurrencyName.postValue(_startSellCurrency.value)
                        buyCurrencyName.postValue(_startBuyCurrency.value)
                    }
                }
        }
    }
        fun calculateRate(sellCurrency: String, buyCurrency: String) {
            val data = _currencyList.value?.data ?: return

            val rateOfSellCurrency: Double = if (sellCurrency == "EUR") {
                1.0
            } else {
                data.firstOrNull { it.name == sellCurrency }?.rate?.toFour()
            } ?: return
            val rateOfBuyCurrency: Double = if (buyCurrency == "EUR") {
                1.0
            } else {
                data.firstOrNull { it.name == buyCurrency }?.rate?.toFour()
            } ?: return

            val rate = (rateOfBuyCurrency / rateOfSellCurrency)
            _rate.value = rate.toFour()
        }
        fun calculateSellBalance(currencyName: String) {
            val balance = _userInfo.value?.data?.currencies?.find { it.name == currencyName }?.balance ?: 0.0
            _sellBalance.value = balance
            _startBalanceSell.value = balance


        }
        fun calculateBuyBalance(currencyName: String) {
            val balance = _userInfo.value?.data?.currencies?.find { it.name == currencyName }?.balance ?: 0.0
            _buyBalance.value = balance
            startBalanceBuy.value = balance

        }
        fun calculateExchange(inputAmount: Double) {
            if (inputAmount == 0.0) {
                _fee.value = 0.0
                _buyAmount.value = 0.0
                _sellBalance.value = startBalanceSell.value
                _buyBalance.value = startBalanceBuy.value
            } else {
                _fee.value = inputAmount.calculateFee(transactionNum = currentTransactionNum.value!!).toTwo()
                sellAmount.value = (inputAmount - _fee.value!!).toTwo()
                _sellBalance.value = (startBalanceSell.value!! - inputAmount).toTwo()
                _buyAmount.value = (sellAmount.value!! * _rate.value!!).toTwo()
                _buyBalance.value = (startBalanceBuy.value!! + _buyAmount.value!!).toTwo()
            }
        }
        fun setCurrencyName(sellName: String, buyName: String) {
            sellCurrencyName.value = sellName
            buyCurrencyName.value = buyName
        }
        fun updateAccountBalance(inputAmount: Double) {

            val updatedSellCurrency = BalanceCurrency(
                name = sellCurrencyName.value!!,
                balance = startBalanceSell.value!! - sellAmount.value!! - _fee.value!!
            )

            val updatedBuyCurrency = BalanceCurrency(
                name = buyCurrencyName.value!!,
                balance = startBalanceBuy.value!! + _buyAmount.value!!
            )

            val balancesList: MutableList<BalanceCurrency> =
                _userInfo.value!!.data!!.currencies as MutableList

            val oldSellCurrency = balancesList.find { it.name == sellCurrencyName.value!! }

            val oldBuyCurrency = balancesList.find { it.name == buyCurrencyName.value!! }

            balancesList.remove(oldSellCurrency)
            if (oldBuyCurrency != null) {
                balancesList.remove(oldBuyCurrency)
            }

            balancesList.add(updatedSellCurrency)
            balancesList.add(updatedBuyCurrency)

            val user = UserAccountEntity(
                id = _userInfo.value!!.data!!.id,
                login = _userInfo.value!!.data!!.login,
                currencies = balancesList,
                transactionNum = currentTransactionNum.value!! + 1
            )
            _startBalanceSell.value = startBalanceSell.value!! - sellAmount.value!! - _fee.value!!
            startBalanceBuy.value = startBalanceBuy.value!! + _buyAmount.value!!
            _sellBalance.value = startBalanceSell.value
            _buyBalance.value = startBalanceBuy.value

            calculateExchange(inputAmount)
            viewModelScope.launch(Dispatchers.IO) {
                userRepository.saveNewUserAccount(userAccountEntity = user)
            }
        }
    }
