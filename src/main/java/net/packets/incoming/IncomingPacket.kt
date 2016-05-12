package net.packets.incoming

/**
 * Created by Kattoor on 11/05/2016.
 */

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IncomingPacket(val opcode: Byte, val size: Byte)