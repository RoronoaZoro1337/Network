import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ClientManager {

    public ClientManager(final String[] args){
        parseArgs(args);
        client = new Client(clientSocket, file);
    }

    public void start(){
        client.start();
    }

    private void checkName(String name) throws IOException{
        fileName = name;
        if(fileName.length() > MAX_NAME_SIZE){
            throw new IOException();
        }
    }

    private void checkFile() throws ParserConfigurationException{
        file = new File("/home/ilya/IdeaProjects/Network/lab2/src/" + fileName);
        if(file.length() > MAX_FILE_SIZE){
            throw new ParserConfigurationException();
        }
    }

    private void createSocket(String address, int port) throws IOException{
        clientSocket = new Socket(address, port);
    }

    private void parseArgs(final String[] args){
        try {
            checkName(args[0]);
            createSocket(args[1], Integer.parseInt(args[2]));
            checkFile();
        } catch (IOException e) {
            System.out.println("ERROR: The parameters are typed incorrectly");
            System.exit(1);
        } catch (ParserConfigurationException e) {
            System.out.println("ERROR: " + fileName + " is too large for transferring");
            System.exit(1);
        }
    }

    private Client client;
    private String fileName;
    private File file;
    private Socket clientSocket;
    private int MAX_NAME_SIZE = 4096;
    private int MAX_FILE_SIZE = 1073741824;
}