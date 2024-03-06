package com.atguigu.nio;

/**
 * @author mqm
 * @version 1.0
 * @date 2024/3/6 21:15
 */

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 *  Scattering :将数据写入到buffer，可以采用buffer数组，依次写入
 *  Gathering : 从buffer 读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws Exception {

        //使用ServerSocketChannel 和 SocketChannel 网络
        ServerSocketChannel open = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口
        open.socket().bind(inetSocketAddress);
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0]=ByteBuffer.allocate(5);
        byteBuffers[1]=ByteBuffer.allocate(3);

        //等客户端连接(telnet)
        SocketChannel accept = open.accept();
        int messageLength = 8;         //假定从客户端接受8 个字节
        //循环读取
        while (true){
            int byteRead = 0;
            while (byteRead<messageLength){
                long l = accept.read(byteBuffers);
                byteRead+=l;    //累计读取的数字
                System.out.println("byteRead = " + byteRead);
                //使用流打印的方式，看看这个buffer的position 和limit
                Arrays.asList(byteBuffers).stream().map(byteBuffer -> "position="+byteBuffer);

                    Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

                    //将数据读出到客户端
                long bytewrite = 0;
                while (bytewrite<messageLength){
                    long read = accept.read(byteBuffers);
                    bytewrite+=l;
                }

                //将所有的buffer 进行clear
                Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());

            }
        }

    }
}
