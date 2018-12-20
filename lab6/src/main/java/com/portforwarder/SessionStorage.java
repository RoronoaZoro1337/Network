package com.portforwarder;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SessionStorage {
    SessionStorage(SocketChannel serverKey, SocketChannel clientKey, int bufferSize){
        this.clientChannel = clientKey;
        this.serverChannel = serverKey;
        this.bufferSize = bufferSize;
    }

    int read() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        if (clientChannel.isConnected()) {
            int bytesRead = serverChannel.read(buffer);
            if (bytesRead > 0) {
                int written = clientChannel.write(ByteBuffer.wrap(buffer.array(), 0, bytesRead));
                System.out.println("Written: " + String.valueOf(written));
            }
            if (bytesRead == -1) {
                serverChannel.close();
                buffer.clear();
                return -1;
            } else {
                buffer.clear();
            }
        }
        return 0;
    }

    SocketChannel getServerKey(){
        return serverChannel;
    }

    private SocketChannel serverChannel;
    private SocketChannel clientChannel;
    private int bufferSize;
}