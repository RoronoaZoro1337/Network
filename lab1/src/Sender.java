import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.TimerTask;

public class Sender extends TimerTask {

    @Override
    public void run() {
        try {
            socket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Sender (int port, InetAddress group, DatagramSocket socket) {
        this.group = group;
        this.port = port;
        this.socket = socket;
        data = "Something really important!".getBytes();
        datagramPacket = new DatagramPacket(data, data.length, group, port);
    }

    private int port;
    private DatagramSocket socket;
    private InetAddress group;
    byte [] data;
    DatagramPacket datagramPacket;
}