package com.atguigu.nio;

import java.nio.IntBuffer;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/5 21:02
 */
public class BasicBuffer {
    public static void main(String[] args) {

        //举例说明Buffer的使用（简单说明）
        //创建一个Buffer,大小为5，既可以存放5个int
        IntBuffer allocate = IntBuffer.allocate(5);
        for (int i = 0; i < allocate.capacity(); i++) {
            allocate.put(i++);
        }
        //如何从buffer读取数据
        //将buffer转换，读写转换
        allocate.flip();
        while (allocate.hasRemaining()){
            System.out.println("allocate.get() = " + allocate.get());
        }
    }
}
