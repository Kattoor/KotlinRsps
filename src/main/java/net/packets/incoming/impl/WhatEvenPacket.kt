package net.packets.incoming.impl

import net.packets.incoming.IncomingPacket
import net.packets.incoming.IncomingPacketHandler
import net.packets.incoming.Result
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

/**
 * Created by Kattoor on 12/05/2016.
 */
@IncomingPacket(-15, 5) class WhatEvenPacket: IncomingPacketHandler {

    override fun handlePacket(buffer: ByteBuffer, channel: SocketChannel, result: Result?): Result {
        println(buffer.get())
        println(buffer.get())
        println(buffer.get())
        println(buffer.get())
        return Result(true, 1, true, 1)
    }


}