import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Write address of the group and port");
            return;
        }

        long delay_mc = 1000;
        int port;
        InetAddress address;

        try {
            port = Integer.parseInt(args[1]);
            address = InetAddress.getByName(args[0]);
        } catch(NumberFormatException | UnknownHostException e) {
            try {
                port = Integer.parseInt(args[0]);
                address = InetAddress.getByName(args[1]);
            } catch(NumberFormatException | UnknownHostException ex) {
                System.out.println("Incorrect address of the group or port");
                return;
            }
        }

        MulticastManager multicastManager = new MulticastManager(port, address, delay_mc);
        multicastManager.startReceive();
        multicastManager.startSend();

        while (true) {
            try {
                Thread.sleep(delay_mc);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
