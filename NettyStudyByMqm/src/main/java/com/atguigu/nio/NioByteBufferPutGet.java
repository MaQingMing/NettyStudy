package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/6 20:29
 */
public class NioByteBufferPutGet {
    public static void main(String[] args) {

        ByteBuffer allocate = ByteBuffer.allocate(64);

//        allocate.putInt(10);
//        allocate.putLong(9L);
//        allocate.putChar('a');
//        allocate.putShort((short) 5);
//
//        allocate.getInt();
//        allocate.getLong();
        for (int i = 0; i < 64; i++) {
            allocate.put((byte) i);
        }
        //buffer调转
        allocate.flip();
        //只读
        ByteBuffer byteBuffer = allocate.asReadOnlyBuffer();
        while (byteBuffer.hasRemaining()){
            System.out.println(byteBuffer.get());
        }
    }
}
