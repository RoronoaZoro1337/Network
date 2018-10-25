public class ClientMain {
    public static void main(String[] args) {
        if (args.length != 0) {
            ClientManager clientManager = new ClientManager(args);
            clientManager.start();
        } else {
            System.out.println("ERROR: The command line is empty");
        }
    }
}
