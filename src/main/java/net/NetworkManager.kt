package net

import net.packets.incoming.IncomingPacketHandler
import net.packets.incoming.Result
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Kattoor on 11/05/2016.
 */

class NetworkManager(private val port: Int): Runnable {

    private val selector: Selector
    private val serverSocket: ServerSocketChannel

    init {
        selector = Selector.open()
        serverSocket = ServerSocketChannel.open()
        initServerSocket()
        Thread(this).start()
    }

    private fun initServerSocket() {
        serverSocket.socket().bind(InetSocketAddress(port))
        serverSocket.configureBlocking(false)
        serverSocket.register(selector, SelectionKey.OP_ACCEPT)
    }

    override fun run() {
        while (true) {
            if (selector.select() == 0)
                continue;
            val keySet: MutableSet<SelectionKey> = selector.selectedKeys()
            with (keySet) {
                filter { it.isAcceptable || it.isReadable }.forEach {
                    remove(it)
                    when {
                        it.isAcceptable -> {
                            val requestingSocket: SocketChannel = (it.channel() as ServerSocketChannel).accept()
                            requestingSocket.configureBlocking(false)
                            requestingSocket.register(selector, SelectionKey.OP_READ)
                        }
                        it.isReadable -> {
                            val buffer: ByteBuffer = ByteBuffer.allocate(512)
                            val socketChannel: SocketChannel = it.channel() as SocketChannel
                            var result: Result = Result(true, 0, false, 0)
                            do {
                                val bytesRead: Int = socketChannel.read(buffer)
                                buffer.flip()
                                result = IncomingPacketHandler.handlePacket(buffer, socketChannel, result)
                                if (!result.enoughBytes) {
                                    buffer.rewind()
                                    buffer.compact()
                                } else
                                    buffer.clear()
                            } while (!result.finished)
                            println("CLOSING")
                        }
                    }
                }
            }
        }
    }
}