package net.packets.incoming.impl

import net.packets.incoming.IncomingPacket
import net.packets.incoming.IncomingPacketHandler
import net.packets.incoming.Result
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

/**
 * Created by Kattoor on 12/05/2016.
 */

/// Sent when the game client window goes out of focus.
@IncomingPacket(3, 1) class FocusChangePacket : IncomingPacketHandler {

    override fun handlePacket(buffer: ByteBuffer, channel: SocketChannel, result: Result?): Result {
        val clientInFocus: Byte = buffer.get()
        println("Client is now ${if (clientInFocus.equals(1.toByte())) "in" else "out of"} focus")
        return Result(true, 0, true, 3)
    }
}