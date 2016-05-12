package net.packets.incoming

/**
 * Created by Kattoor on 12/05/2016.
 */

data class Result(val enoughBytes: Boolean, val state: Int, val finished: Boolean, val opcode: Byte)
