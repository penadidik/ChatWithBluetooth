package info.penadidik.bluetoothchat.chat

sealed class Message(val text: String) {
    class RemoteMessage(text: String) : Message(text)
    class LocalMessage(text: String) : Message(text)
}