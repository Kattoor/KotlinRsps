package net.packets.incoming.impl

import net.packets.incoming.IncomingPacket
import net.packets.incoming.IncomingPacketHandler
import net.packets.incoming.Result
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*

/**
 * Created by Kattoor on 12/05/2016.
 */

@IncomingPacket(-1, -1) class LoginPacket : IncomingPacketHandler {

    override fun handlePacket(buffer: ByteBuffer, channel: SocketChannel, result: Result?): Result {
        when (result?.state) {
            0 -> {
                //region read
                if (buffer.limit() < 3)
                    return Result(false, 0, false, -1)
                val type: Byte = buffer.get()
                val nameHash: Byte = buffer.get()
                //endregion
                //region write
                buffer.clear()
                buffer.put(ByteArray(8, {0})) // empty byte array
                buffer.put(0); // response code
                buffer.putLong(12341234) // server key
                buffer.flip()
                while (buffer.hasRemaining())
                    channel.write(buffer)
                //endregion
                return Result(true, 1, false, -1)
            }
            1 -> {
                //region read
                if (buffer.limit() < 63)
                    return Result(false, 1, false, -1)
                val connectStatus: Byte = buffer.get()
                val size: Byte = buffer.get()
                println("size: " + size);
                val const255: Byte = buffer.get()
                val const317: Short = buffer.short
                val clientVersion: Byte = buffer.get()
                (1..9).forEach { buffer.int }
                var const10: Byte = buffer.get()
                var clientSessionKey: Long = buffer.long
                var serverSessionKey: Long = buffer.long
                var userId: Int = buffer.int
                val username: String
                val password: String
                try {
                    username = buffer.getString()
                    password = buffer.getString()
                } catch (ex: RuntimeException) {
                    buffer.rewind()
                    return Result(false, 2, false, -1)
                }
                //endregion
                //region write
                buffer.clear()
                buffer.put(2) // response code (2 is successful login)
                buffer.put(2) // player status (2 is an administrator)
                buffer.put(0) // flag
                buffer.flip()
                while (buffer.hasRemaining())
                    channel.write(buffer)
                //endregion
                return Result(true, 2, true, -1)
            }
            else -> return Result(true, 0, true, 0) // this never happens
        }
    }

    fun ByteBuffer.getString(): String {
        if (!hasRemaining()) throw RuntimeException("Not all bytes are here")
        var b: Byte
        val bArray: MutableList<Byte> = mutableListOf()
        var count: Int = 0;
        var isComplete: Boolean = false;
        while (hasRemaining()) {
            b = get()
            if (b.equals(10.toByte())) {
                isComplete = true
                break
            }
            bArray.add(b)
        }
        if (!isComplete) throw RuntimeException("Not all bytes are here")
        return String(ByteArray(bArray.size, { bArray.get(count++) }))
    }
}