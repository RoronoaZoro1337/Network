import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.HashMap;
import java.util.Timer;

public class MulticastManager {

    public MulticastManager(int port, InetAddress addrGroup, long delay_mc) {
        this.port = port;
        this.delay = delay_mc;
        timer = new Timer();
        connectors = new HashMap<>();
        try {
            group = addrGroup;
            socketSend = new DatagramSocket();
            socketReceive = new MulticastSocket(port);
            socketReceive.joinGroup(group);
            receiver = new Thread(new Reciever(socketReceive, connectors));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSend(){
        timer.scheduleAtFixedRate(new Sender(port, group, socketSend), 0, delay);
    }

    public void startReceive(){
        receiver.start();
    }

    private int port;
    private DatagramSocket socketSend;
    private MulticastSocket socketReceive;
    private InetAddress group;
    private HashMap<String, Long> connectors;
    private Timer timer;
    private Thread receiver;
    private long delay;
}
