package info.penadidik.bluetoothchat.scan

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import info.penadidik.bluetoothchat.R

class DeviceScanAdapter(
    private val onDeviceSelected: (BluetoothDevice) -> Unit
) : RecyclerView.Adapter<DeviceScanAdapter.DeviceScanViewHolder>() {

    private var items = listOf<BluetoothDevice>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceScanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_device, parent, false)
        return DeviceScanViewHolder(view, onDeviceSelected)
    }

    override fun onBindViewHolder(holder: DeviceScanViewHolder, position: Int) {
        items.getOrNull(position)?.let { result ->
            holder.bind(result)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(results: List<BluetoothDevice>) {
        items = results
        notifyDataSetChanged()
    }

    class DeviceScanViewHolder(
        view: View,
        val onDeviceSelected: (BluetoothDevice) -> Unit
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val name = itemView.findViewById<TextView>(R.id.device_name)
        private val address = itemView.findViewById<TextView>(R.id.device_address)
        private var bluetoothDevice: BluetoothDevice? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(device: BluetoothDevice) {
            bluetoothDevice = device
            name.text = device.name
            address.text = device.address
        }

        override fun onClick(view: View) {
            bluetoothDevice?.let { device ->
                onDeviceSelected(device)
            }
        }
    }
}