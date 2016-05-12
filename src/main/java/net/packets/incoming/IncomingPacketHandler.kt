package net.packets.incoming

import org.reflections.Reflections
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel
import java.util.*

/**
 * Created by Kattoor on 11/05/2016.
 */

interface IncomingPacketHandler {

    companion object {
        private val handlers: MutableMap<Byte, IncomingPacketHandler> = HashMap();

        fun handlePacket(buffer: ByteBuffer, channel: SocketChannel, result: Result): Result {
            val opcode = if (result.state != 0) result.opcode else buffer.get()
            val handler: IncomingPacketHandler = handlers.getOrElse(opcode, { lazyLoadHandler(opcode) } )
            val requiredPacketSize: Byte = handler.javaClass.getAnnotation(IncomingPacket::class.java).size
            if (!requiredPacketSize.equals(-1.toByte()) && buffer.limit() < requiredPacketSize)
                return Result(false, result.state, false, opcode)
            return handler.handlePacket(buffer, channel, result)
        }

        private fun lazyLoadHandler(opcode: Byte): IncomingPacketHandler {
            val allHandlers: Set<Class<out IncomingPacketHandler>> =
                    Reflections("net.packets.incoming.impl")
                            .getSubTypesOf(IncomingPacketHandler::class.java)

            val correctHandler: IncomingPacketHandler =
                    allHandlers
                            .find { it.getAnnotation(IncomingPacket::class.java).opcode == opcode }
                            .let { it?.newInstance() }
                            ?: throw RuntimeException("No incoming packet handler found for packet [$opcode]")

            handlers.put(opcode, correctHandler)
            return correctHandler
        }
    }

    fun handlePacket(buffer: ByteBuffer, channel: SocketChannel, result: Result?): Result;
}