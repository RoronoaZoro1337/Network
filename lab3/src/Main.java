import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException {
        if((args.length != 3) && ((args.length != 5))){
            System.out.println("Write name, loss percent, port and optional parent IP and parent port");
            return;
        }
        try{
            String name = args[0];
            int loss = Integer.parseInt(args[1]);
            int port = Integer.parseInt(args[2]);
            boolean isRoot = true;
            int parentPort = -1;
            InetAddress parentAddress = null;
            if (args.length > 3){
                parentPort = Integer.parseInt(args[3]);
                parentAddress = InetAddress.getByName(args[4]);
                isRoot = false;
            }
            Node MyNode = new Node(name, isRoot, loss, port, parentAddress, parentPort);
            MyNode.start();
        } catch (NumberFormatException | UnknownHostException e){
            System.out.println("Wrong arguments");
            return;
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
}

