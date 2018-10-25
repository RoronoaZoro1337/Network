import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {

        String name;
        int lossPersentage;
        int port;
        InetAddress parentAddress;
        int parentPort;
        boolean isRoot = true;

        if (args.length != 3 && args.length != 5) {
            System.out.println("Write node name, loss percentage, yours port and optional parent ip address and parent port");
            return;
        }

        try {

            name = args[0];
            lossPersentage = Integer.parseInt(args[1]);
            port = Integer.parseInt(args[2]);
            parentAddress = null;
            parentPort = -1;

            if (args.length > 3) {
                parentAddress = InetAddress.getByName(args[3]);
                parentPort = Integer.parseInt(args[4]);
                isRoot = false;
            }

            Node MyNode = new Node(isRoot, lossPersentage, port, parentAddress, parentPort);

        } catch(NumberFormatException | UnknownHostException ex) {
            System.out.println("Wrong format of argument");
            return;
        } catch(SocketException e) {
            e.printStackTrace();
        }
    }
}
