import java.io.*;
import java.net.Socket;

public class Server implements Runnable {

    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
        timer = new Timer();
    }

    public void run() {
        try {
            String fileName = receiveMessage();
            long fileSize = Integer.parseInt(receiveMessage());
            String newFileName = getNewName(fileName);
            receiveFile("src/uploads/" + newFileName, fileSize);
            checkReceivedFile(fileName ,newFileName, fileSize);
        } catch (IOException | InterruptedException e) {
            System.out.println("ERROR: The file wasn't received or was damaged");
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

    private String receiveMessage() throws IOException {
        BufferedReader bf = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String message = bf.readLine();
        return message;
    }

    private String getNewName(String fileName){
        fileName = "../../testst/../test.txt";
        for(int i = 0; i < Integer.MAX_VALUE; i++) {
            File file = new File("src/uploads/" + i + fileName);
            if (!file.exists()) {
                return (i + fileName);
            }
        }
        return null;
    }

    private void receiveFile(final String fileName, final long fileSize) throws InterruptedException, IOException {
        InputStream in = clientSocket.getInputStream();
        BufferedOutputStream bos =
                new BufferedOutputStream(new FileOutputStream(fileName));
        byte[] bytes = new byte[PACKET_SIZE];

        timer.start();
        for(long count, total = 0; total != fileSize && !timer.getInterruptFlag();){
            count = in.read(bytes);
            bos.write(bytes, 0, (int)count);
            timer.increaseNumOfBytes((int)count);
            total += count;
        }

        bos.close();
        timer.setExitFlag();
        timer.join();
    }

    private void closeSockets(){
        try {
            clientSocket.close();
            System.out.println("Disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkReceivedFile(String fileName, String newFileName, long fileSize) throws IOException {
        File file = new File("src/uploads/" + newFileName);
        if (file.length() == fileSize && !timer.getInterruptFlag()){
            sendMessage(fileName + " was received successfully and renamed in " + newFileName + "\n");
            System.out.println("The file was received successfully");
        } else {
            file.delete();
            throw new IOException();
        }
    }

    private Timer timer;
    private static Socket clientSocket;
    private int PACKET_SIZE = 1024;
}
