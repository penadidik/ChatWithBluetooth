package info.penadidik.bluetoothchat.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import info.penadidik.bluetoothchat.R
import info.penadidik.bluetoothchat.databinding.FragmentEnableBluetoothBinding
import info.penadidik.bluetoothchat.utils.ChatServer
import info.penadidik.bluetoothchat.utils.REQUEST_ENABLE_BT

class EnableBluetoothFragment : Fragment() {

    private lateinit var binding: FragmentEnableBluetoothBinding
    private val TAG = "EnableBluetoothFragment"

    private val bluetoothEnableObserver = Observer<Boolean> { shouldPrompt ->
        if (!shouldPrompt) {
            findNavController().navigate(R.id.action_check_location_permissions)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChatServer.requestEnableBluetooth.observe(this, bluetoothEnableObserver)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnableBluetoothBinding.inflate(inflater, container, false)

        binding.errorAction.setOnClickListener {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        return binding.root
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ENABLE_BT -> {
                if (resultCode == Activity.RESULT_OK) {
                    ChatServer.startServer(requireActivity().application)
                }
                super.onActivityResult(requestCode, resultCode, data)
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }
}