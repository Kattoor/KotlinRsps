package net.packets.incoming.impl

import net.packets.incoming.IncomingPacket
import net.packets.incoming.IncomingPacketHandler
import net.packets.incoming.Result
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

/**
 * Created by Kattoor on 12/05/2016.
 */

/// Sent when there are no actions being performed by the player for this cycle.
@IncomingPacket(0, 0) class IdlePacket : IncomingPacketHandler {

    override fun handlePacket(buffer: ByteBuffer, channel: SocketChannel, result: Result?): Result {
        return Result(true, 0, true, 0)
    }
}