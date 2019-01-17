package com.sokcsproxy.main;

import com.sokcsproxy.portforwarder.PortForwarder;
import java.io.IOException;

public class Main {
    public static void main(String args[]){
        try {
            new PortForwarder(args).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
