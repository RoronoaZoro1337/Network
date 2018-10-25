import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerManager {

    public ServerManager(String[] args){
        parseArgs(args);
        System.out.println("Listening");
    }

    public void start() {
        while (true) {
            try {
                Socket socket = server.accept();
                System.out.println("Connected");
                new Thread(new Server(socket)).start();
            } catch (IOException e) {
                    System.out.println("ERROR: A client can't be connected");
            }
        }
    }

    private void parseArgs(String[] args){
        try {
            port = Integer.parseInt(args[0]);
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("ERROR: The parameters are typed incorrectly");
            System.exit(1);
        }
    }

    private int port;
    private ServerSocket server;
}
