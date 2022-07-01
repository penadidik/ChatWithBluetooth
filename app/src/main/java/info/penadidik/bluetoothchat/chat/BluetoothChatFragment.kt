package info.penadidik.bluetoothchat.chat

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.HandlerCompat.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import info.penadidik.bluetoothchat.R
import info.penadidik.bluetoothchat.databinding.FragmentBluetoothChatBinding
import info.penadidik.bluetoothchat.utils.ChatServer
import info.penadidik.bluetoothchat.utils.gone
import info.penadidik.bluetoothchat.utils.visible

class BluetoothChatFragment : Fragment() {

    private lateinit var binding: FragmentBluetoothChatBinding
    private val TAG = "BluetoothChatFragment"
    private val adapter = MessageAdapter()

    private val deviceConnectionObserver = Observer<DeviceConnectionState> { state ->
        when (state) {
            is DeviceConnectionState.Connected -> {
                val device = state.device
                Log.d(TAG, "Gatt connection observer: have device $device")
                chatWith(device)
            }
            is DeviceConnectionState.Disconnected -> {
                showDisconnected()
            }
        }

    }

    private val connectionRequestObserver = Observer<BluetoothDevice> { device ->
        Log.d(TAG, "Connection request observer: have device $device")
        ChatServer.setCurrentChatConnection(device)
    }

    private val messageObserver = Observer<Message> { message ->
        Log.d(TAG, "Have message ${message.text}")
        adapter.addMessage(message)
    }

    private val inputMethodManager by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBluetoothChatBinding.inflate(inflater, container, false)

        Log.d(TAG, "chatWith: set adapter $adapter")
        binding.messages.layoutManager = LinearLayoutManager(context)
        binding.messages.adapter = adapter

        showDisconnected()

        binding.connectDevices.setOnClickListener {
            findNavController().navigate(R.id.action_find_new_device)
        }

        binding.endChat.setOnClickListener {
            BluetoothAdapter.getDefaultAdapter().disable()
            Handler().postDelayed({
                BluetoothAdapter.getDefaultAdapter().enable()
                showDisconnected()
            }, 1000)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        requireActivity().setTitle(R.string.chat_title)
        ChatServer.connec tionRequest.observe(viewLifecycleOwner, connectionRequestObserver)
        ChatServer.deviceConnection.observe(viewLifecycleOwner, deviceConnectionObserver)
        ChatServer.messages.observe(viewLifecycleOwner, messageObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun chatWith(device: BluetoothDevice) {
        binding.connectedContainer.visible()
        binding.notConnectedContainer.gone()

        val chattingWithString = resources.getString(R.string.chatting_with_device, device.address)
        binding.connectedDeviceName.text = chattingWithString
        binding.sendMessage.setOnClickListener {
            val message = binding.messageText.text.toString()
            // only send message if it is not empty
            if (message.isNotEmpty()) {
                ChatServer.sendMessage(message)
                // clear message
                binding.messageText.setText("")
            }
        }
    }

    private fun showDisconnected() {
        hideKeyboard()
        binding.notConnectedContainer.visible()
        binding.connectedContainer.gone()
    }

    private fun hideKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}