import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

public class Node {

    public Node(boolean isRoot, int lossPersentage, int port, InetAddress parentAddress, int parentPort) throws SocketException {

        this.isRoot = isRoot;
        this.lossPersentage = lossPersentage;
        this.port = port;

        if (!isRoot) {
            parent = new InetSocketAddress(parentAddress, parentPort);
            connectors.add(parent);
        }

        sender = new Sender(isRoot);
        sender.start();
    }

    public void start() {

        Scanner scanner = new Scanner(System.in);

        while(true) {
            sender.addMessage(scanner.nextLine(), Message.USUAL);
        }
    }

    static final Object o = new Object();
    static boolean isRoot;
    static int lossPersentage;
    static int port;
    static InetSocketAddress parent;
    static ArrayList<InetSocketAddress> connectors;
    Sender sender;
}

