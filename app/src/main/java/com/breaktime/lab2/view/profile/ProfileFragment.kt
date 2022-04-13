package com.breaktime.lab2.view.profile

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.breaktime.lab2.R
import com.breaktime.lab2.databinding.FragmentProfileBinding
import com.breaktime.lab2.util.Preferences
import com.breaktime.lab2.util.ResourcesProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentProfileBinding

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    @Inject
    lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )
        binding.info.setOnClickListener {
            findNavController().navigate(R.id.aboutFragment)
        }
        binding.theme.setOnClickListener {
            changeNightMode(checkNightMode())
            binding.theme.setImageDrawable(getDayNightImgRes())
        }
        binding.theme.setImageDrawable(getDayNightImgRes())
        val map = childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment
        map.getMapAsync(this)
        return binding.root
    }

    private fun getDayNightImgRes(): Drawable? {
        return if (checkNightMode()) {
            resourcesProvider.getDrawable(R.drawable.ic_sun_24)
        } else {
            resourcesProvider.getDrawable(R.drawable.ic_moon_24)
        }
    }

    private fun changeNightMode(isNight: Boolean) {
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setNightMode(false)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setNightMode(true)
        }
    }

    private fun checkNightMode() =
        preferences.getNightMode()

    private fun setNightMode(isNight: Boolean) =
        preferences.setNightMode(isNight)

    override fun onMapReady(googleMap: GoogleMap) {
        if (isLocationRetrievingNotPermitted()) {
            return
        }

        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}