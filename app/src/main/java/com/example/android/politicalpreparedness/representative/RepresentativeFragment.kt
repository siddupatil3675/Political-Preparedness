package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.*

class DetailFragment : BaseFragment() {


    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 100
    }

    override val _viewModel: RepresentativeViewModel by inject()

    private lateinit var binding: FragmentRepresentativeBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentRepresentativeBinding.inflate(inflater)

        binding.representativeViewModel = _viewModel

        binding.apply {
            representativeViewModel = _viewModel

            val adapter = RepresentativeListAdapter()
            rvRepresentatives.adapter = adapter

            _viewModel.representatives.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                adapter.submitList(it)
            })

            buttonLocation.setOnClickListener {
                checkLocationPermission()
            }

            buttonSearch.setOnClickListener {
                hideKeyboard()
                _viewModel.getAddressFromIndividualFields(Address(addressLine1.text.toString(),
                        addressLine2.text.toString(),
                        city.text.toString(),
                        state.selectedItem.toString(),
                        zip.text.toString()))
            }

            _viewModel.address.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (isNetworkConnected()) {
                    _viewModel.getRepresentatives()
                } else {
                    _viewModel.showSnackBar.postValue(getString(R.string.no_internet_connecton))
                }
            })

            _viewModel.showLoading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
            })


        }

        return binding.root

    }

    private fun checkLocationPermission() {
        if (isFGPermissionGranted()) {
            getLocation()
        } else {
            requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun isFGPermissionGranted(): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                    it, Manifest.permission.ACCESS_FINE_LOCATION)
        } == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Timber.d("location foreground permissions granted")
                getLocation()
            } else {
                _viewModel.showSnackBar.postValue(getString(R.string.permission_denied_explanation))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
            fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            val address = geoCodeLocation(location)
                            _viewModel.setAddressFromGeoLocation(address)
                            updateAddressViews(address)
                        } else {
                            _viewModel.showSnackBar.value = "Cannot find location"
                        }
                    }
        } catch (e: Exception) {
            _viewModel.showSnackBar.value = "Cannot find representatives at location"
        }
    }

    private fun updateAddressViews(address: Address) {
        binding.apply {
            addressLine1.setText(address.line1)
            addressLine2.setText(address.line2)
            city.setText(address.city)
            val statesArray = resources.getStringArray(R.array.states)
            state.setSelection(statesArray.indexOf(address.state))
            zip.setText(address.zip)
        }
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}