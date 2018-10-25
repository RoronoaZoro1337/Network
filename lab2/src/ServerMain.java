public class ServerMain {
    public static void main(String args[]) {
        if (args.length != 0) {
            ServerManager serverManager = new ServerManager(args);
            serverManager.start();
        } else {
            System.out.println("ERROR: The command line is empty");
        }
    }
}
