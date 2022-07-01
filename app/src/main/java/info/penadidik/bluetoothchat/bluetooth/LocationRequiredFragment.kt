package info.penadidik.bluetoothchat.bluetooth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import info.penadidik.bluetoothchat.R
import info.penadidik.bluetoothchat.databinding.FragmentLocationRequiredBinding

class LocationRequiredFragment : Fragment() {

    private lateinit var binding: FragmentLocationRequiredBinding
    private val TAG = "LocationRequiredFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationRequiredBinding.inflate(inflater, container, false)

        // hide the error messages while checking the permissions
        binding.locationErrorMessage.visibility = View.GONE
        binding.grantPermissionButton.visibility = View.GONE
        // setup click listener on grant permission button
        binding.grantPermissionButton.setOnClickListener {
            checkLocationPermission()
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        checkLocationPermission()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult: ")
        when(requestCode) {
            LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Navigate to the chat fragment
                    findNavController().navigate(R.id.action_start_chat)
                } else {
                    showError()
                }
            }
        }
    }

    private fun showError() {
        binding.locationErrorMessage.visibility = View.VISIBLE
        binding.grantPermissionButton.visibility = View.VISIBLE
    }

    private fun checkLocationPermission() {
        val hasLocationPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasLocationPermission) {
            // Navigate to the chat fragment
            findNavController().navigate(R.id.action_start_chat)
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
        }
    }

    companion object {
        private const val TAG = "LocationRequiredFrag"
        private const val LOCATION_REQUEST_CODE = 0
    }
}