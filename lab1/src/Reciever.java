import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Reciever implements Runnable {

    Reciever(MulticastSocket socket, HashMap<String, Long> connectors ){
        this.socket = socket;
        this.connectors = connectors;
    }

    @Override
    public void run() {
        try {
            socket.setSoTimeout(3000);
            while (true) {
                socket.receive(input);
                String key = input.getAddress().toString() + " " + input.getPort();
                timer = System.currentTimeMillis();
                if (!connectors.containsKey(key)) {
                    connectors.put(key, timer);
                    printer();
                }else {
                    connectors.put(key, timer);
                }
                countConnectors = getCountConnectors();
                checkConnectors();
                printer();
            }
        } catch (SocketException e) {
            checkConnectors();
            printer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getCountConnectors(){
        return connectors.size();
    }

    public HashMap<String, Long> getConnectors(){
        return connectors;
    }

    public void printer() {
        int newCount = getCountConnectors();
        if (countConnectors != newCount) {
            System.out.println(getConnectors().keySet());
            countConnectors = newCount;
        }
    }

    private void checkConnectors(){
        for (Iterator<Map.Entry<String, Long>> iterator = connectors.entrySet().iterator(); iterator.hasNext(); ) {
            HashMap.Entry<String, Long> elem = iterator.next();
            if (timer - (elem.getValue()) > 3000) {
                connectors.remove(elem.getKey());
            }
        }
    }

    private byte[] buf = new byte[256];
    private MulticastSocket socket;
    private DatagramPacket input = new DatagramPacket(buf, buf.length);
    private HashMap<String, Long> connectors;
    private long timer;
    private int countConnectors = 0;

}
