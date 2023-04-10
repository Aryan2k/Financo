package com.example.financo.ui.home

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.financo.R
import com.example.financo.adapters.CountryListAdapter
import com.example.financo.databinding.ActivityHomeBinding
import com.example.financo.utils.FunctionUtils
import com.example.financo.utils.FunctionUtils.focusScreen
import com.example.financo.utils.FunctionUtils.toast
import com.example.financo.utils.RequestStatus
import com.example.financo.viewModel.HomeActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeActivityViewModel
    private lateinit var userDialog: Dialog
    private lateinit var countryListAdapter: CountryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[HomeActivityViewModel::class.java]
        setContentView(binding.root)
        setUpUI()
        handleLoadUserData()
        setUpClickListeners()
    }

    private fun setUpUI() {
        hideStatusBar()
        focusScreen(binding.root)
        userDialog = FunctionUtils.setUpDialog(getString(R.string.getting_user_details), binding.root.context)
    }

    @Suppress("DEPRECATION")
    private fun hideStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT
        supportActionBar?.hide()
    }

    private fun setUpClickListeners() {
        with(binding) {
            submitButtonContainer.setOnClickListener {
                if (nameEdit.text?.isBlank() == true) {  //  user name not entered
                    nameContainer.isErrorEnabled = true
                    nameContainer.error = getString(R.string.please_enter_your_name)
                    progressBar.visibility = View.INVISIBLE
                    nameContainer.rootView?.let { it1 -> FunctionUtils.animateView(it1) }
                    FunctionUtils.vibrateDevice(binding.root.context)
                } else {  //  user name entered, attempt to load the required details
                    nameContainer.isErrorEnabled = false
                    progressBar.visibility = View.INVISIBLE

                    viewModel.loadUserData(binding.nameEdit.text.toString().trim())
                }
            }
        }
    }

    private fun handleLoadUserData() {
        viewModel.loadUserDataLiveData.observe(this) {
            when (it.status) {
                RequestStatus.LOADING -> {
                    userDialog.show()
                    binding.shimmerContainer.root.visibility = View.VISIBLE
                    binding.shimmerContainer.shimmerLayout.startShimmer()
                }
                RequestStatus.SUCCESS -> {
                    binding.shimmerContainer.root.visibility = View.GONE
                    if (it.data != null)   //  user details retrieved, update UI
                    {
                        countryListAdapter = CountryListAdapter(viewModel.countryList)
                        binding.userListRv.adapter = countryListAdapter
                    } else {
                        toast(binding.root.context, getString(R.string.some_error_occurred))
                    }
                    userDialog.dismiss()
                }
                RequestStatus.EXCEPTION -> {
                    binding.shimmerContainer.root.visibility = View.GONE
                    toast(binding.root.context, getString(R.string.error_no_internet_connection))
                    userDialog.dismiss()
                }
            }
        }
    }
}