package com.portforwarder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.ArrayList;

class PortForwarder {
    PortForwarder(String args[]) throws IOException {
        int l_port = Integer.valueOf(args[0]);
        InetAddress r_host = InetAddress.getByName(args[1]);
        int r_port = Integer.valueOf(args[2]);
        serverAddress = new InetSocketAddress(r_host, r_port);
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(l_port));
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    void start() throws IOException {
        while (true) {
            selector.select();
            for (SelectionKey key : selector.selectedKeys()) {
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        accept();
                    } else if (key.isConnectable()) {
                        connect(key);
                    } else if (key.isReadable()) {
                        read(key);
                    }
                }
            }
        }
    }

    private void accept() throws IOException {
        SocketChannel clientChannel;
        if((clientChannel = serverChannel.accept()) != null) {
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
            SocketChannel serverChannel = SocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.connect(serverAddress);
            serverChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_CONNECT);
            int bufferSize = 8192;
            sessionStorages.add(new SessionStorage(serverChannel, clientChannel, bufferSize));
            sessionStorages.add(new SessionStorage(clientChannel, serverChannel, bufferSize));
        }
    }

    private void connect(SelectionKey key) throws IOException {
        ((SocketChannel) key.channel()).finishConnect();
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();
        for (SessionStorage sessionStorage: sessionStorages){
            if(socketChannel == sessionStorage.getServerKey()){
                if (sessionStorage.read() != 0){
                    sessionStorages.remove(sessionStorage);
                    break;
                }
            }
        }
    }

    private ArrayList<SessionStorage> sessionStorages = new ArrayList<>();
    private InetSocketAddress serverAddress;
    private ServerSocketChannel serverChannel;
    private Selector selector;
}
