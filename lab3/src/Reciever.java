import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

public class Reciever extends Thread {

    public Reciever(InetSocketAddress address, int lossPersentage) throws SocketException {
        this.address = address;
        this.lossPersentage = lossPersentage;
        socket = new DatagramSocket();
    }

    public boolean isAnswer(Message mes) {

        Iterator<Message> iterator = messagesRecv.iterator();

        while (iterator.hasNext()) {

            Message message = iterator.next();

            if ((message.getGuid().equals(mes.getGuid())) && (message.getCode() == Message.ANSWER)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public LinkedList<Message> getUsualMessage() {

        LinkedList<Message> usualMessage = new LinkedList<>();

        for (Message message : messagesRecv) {

            if ((message.getCode() == Message.USUAL) || (message.getCode() == Message.NEWCHILD)) {
                usualMessage.add(message);
            }
        }

        if (usualMessage.isEmpty()) {
            return null;
        } else {
            return usualMessage;
        }
    }

    private boolean containsMessage(Message mes) {

        for (Message message : messagesRecv) {
            if (message.getGuid().equals(mes.getGuid())) {
                return true;
            }
        }

        return false;
    }

    private void handlingMessage(Message message) {

        int code = message.getCode();

        if (code == Message.NEWCHILD) {
            Node.connectors.add(message.getSender());
            synchronized (Node.o) {
                messagesRecv.add(message);
            }
        }

        if (code == Message.USUAL){

            if(!containsMessage(message)) {
                System.out.println(message.getData());
                synchronized (Node.o) {
                    messagesRecv.add(message);
                }
            }
        }

        if (code == Message.ANSWER) {
            synchronized (Node.o) {
                messagesRecv.add(message);
            }
        }
    }

    @Override
    public void run() {
        try {

            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            Message message;
            Random rnd = new Random(System.currentTimeMillis());

            while (true) {
                socket.receive(packet);

                if (rnd.nextInt(100) < lossPersentage) {
                    continue;
                }

                String data = new String(buf, 0, packet.getLength(), "UTF-8");
                String[] messageParts = data.split(":");
                int code = Integer.parseInt(messageParts[0]);
                String guid = messageParts[1];
                int port = Integer.parseInt(messageParts[2]);
                String Messagedata = messageParts[3];
                message = new Message(Messagedata, code,  new InetSocketAddress(packet.getAddress(), port), UUID.fromString(guid));
                handlingMessage(message);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private InetSocketAddress address;
    private DatagramSocket socket;
    private int lossPersentage;
    private static LinkedList<Message> messagesRecv = new LinkedList<>();
}
