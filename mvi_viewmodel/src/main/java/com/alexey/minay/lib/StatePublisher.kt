package com.alexey.minay.lib

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class StatePublisher(
    private val port: Int = DEFAULT_PORT
) {

    val host = "127.0.0.1"

    private var mServerSocket: ServerSocket? = null
    private var mContinuePublish = false

    suspend fun start() = withContext(Dispatchers.IO) {
        mContinuePublish = true
        var socket: Socket? = null
        var dataOutputStream: DataOutputStream? = null
        try {
            val addres = InetAddress.getByName(host)
            mServerSocket = ServerSocket(port, 0, addres)
            while (true) {
                socket = mServerSocket?.accept()
                dataOutputStream = DataOutputStream(socket!!.getOutputStream())
                val messageFromClient = getHtml()
                val bufferedWriter = dataOutputStream.bufferedWriter()
                bufferedWriter.write(messageFromClient)
                bufferedWriter.close()
                dataOutputStream.close()
                socket.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                dataOutputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                socket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun stop() {
        mContinuePublish = false
    }

    private fun getHtml(): String {
        val body = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1>My First Heading</h1>\n" +
                "<p>My first paragraph.</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>"
        val header = "HTTP/1.1 200 OK\\r\\nContent-Type: text/html\\r\\n\\r\\n"
        return header + body
    }

    private fun tryOrCatch(block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val DEFAULT_PORT = 6190
    }

}