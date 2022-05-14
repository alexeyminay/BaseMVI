package com.alexey.minay.lib.stateManager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

class StatePublisher(
    private val port: Int = DEFAULT_PORT
) {

    var isContinuePublish = false
        private set

    private val mHost = "127.0.0.1"
    private var mGetMessageCallback: () -> String = { "" }

    suspend fun start() = withContext(Dispatchers.IO) {
        if (isContinuePublish) {
            return@withContext
        }
        isContinuePublish = true
        try {
            val address = InetAddress.getByName(mHost)
            ServerSocket(port, 0, address).use { serverSocket ->
                startPublishing(serverSocket)
            }
        } catch (e: Exception) {
            isContinuePublish = false
            e.printStackTrace()
        }
    }

    fun stop() {
        isContinuePublish = false
    }

    fun setGetMessageCallback(getMessageCallback: () -> String) {
        mGetMessageCallback = getMessageCallback
    }

    private fun startPublishing(serverSocket: ServerSocket) {
        while (isContinuePublish) {
            serverSocket.accept().use { socket ->
                publishState(socket)
            }
        }
    }

    private fun publishState(socket: Socket) {
        DataOutputStream(socket.getOutputStream()).use { dataOutputStream ->
            val message = mGetMessageCallback()
            writeMessage(dataOutputStream, message)
        }
    }

    private fun writeMessage(dataOutputStream: DataOutputStream, message: String) {
        dataOutputStream.bufferedWriter().use { bufferedWriter ->
            bufferedWriter.write(message)
            bufferedWriter.close()
        }
    }

    companion object {
        private const val DEFAULT_PORT = 6190
    }

}