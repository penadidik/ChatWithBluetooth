package info.penadidik.bluetoothchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import info.penadidik.bluetoothchat.utils.ChatServer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        ChatServer.startServer(application)
    }

    override fun onStop() {
        super.onStop()
        ChatServer.stopServer()
    }
}