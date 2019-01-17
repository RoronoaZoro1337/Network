import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

public class Node {

    public Node(String name, boolean isRoot, int loss, int port, InetAddress parentAddress, int parentPort) throws SocketException {
        this.isRoot = isRoot;
        this.loss = loss;
        this.port = port;
        this.name = name;
        if (!isRoot){
            parent = new InetSocketAddress(parentAddress, parentPort);
            connectors.add(parent);
        }
        sender = new Sender(isRoot);
        sender.start();
    }

    public void start(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            String data = scanner.nextLine();
            if (data != null) {
                sender.addMessage(data, Message.USUAL);
            }
        }
    }

    static final Object o = new Object();
    static boolean isRoot;
    static int loss;
    static int port;
    static String name;
    static InetSocketAddress parent;
    static ArrayList<InetSocketAddress> connectors = new ArrayList<>();
    Sender sender;
}
