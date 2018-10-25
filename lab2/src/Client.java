import java.io.*;
import java.net.Socket;

public class Client extends Thread {

    public Client(final Socket clientSocket, final File file) {
        this.clientSocket = clientSocket;
        this.file = file;
    }

    public void run() {
        try {
            sendMessage(file.getName() + "\n");
            sendMessage(file.length() + "\n");
            sendFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSockets();
        }
    }

    private void sendMessage(String message) throws IOException {
        BufferedWriter write = new BufferedWriter(
                new OutputStreamWriter(clientSocket.getOutputStream()));
        write.write(message);
        write.flush();
    }

    private void receiveMessage() throws IOException {
        BufferedReader read = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String message = read.readLine();
        System.out.println(message);
    }

    private void sendFile(File file) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));;
        BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
        byte[] buffer = new byte[PACKET_SIZE];

        for(int count; (count = bis.read(buffer)) >= 0;){
            out.write(buffer, 0, count);
        }

        out.flush();
        receiveMessage();
        out.close();
        bis.close();
    }

    private void closeSockets(){
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket clientSocket;
    private File file;
    private int PACKET_SIZE = 1024;
}
