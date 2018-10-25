import java.io.IOException;
import java.net.*;
import java.util.*;

public class Sender extends Thread {

    public Sender (boolean isRoot) throws SocketException {

        if (!isRoot){
            addMessage("NEW CHILD", Message.NEWCHILD, Node.parent);
        }

        reciever = new Reciever(new InetSocketAddress(Node.port), Node.lossPersentage);
        reciever.start();
    }

    public void addMessage(String data, int code) {
        synchronized (Node.o) {
            messageSend.add(new Message(data, code, 0,
                    new InetSocketAddress(Node.port), UUID.randomUUID()));
        }
    }

    public void addMessage(String data, int code, InetSocketAddress reciver) {
        synchronized (Node.o){
            messageSend.add(new Message(data, code, 0,
                    new InetSocketAddress(Node.port), reciver, UUID.randomUUID()));
        }
    }

    public void makeAnswer(Message message) {
        synchronized (Node.o) {
            messageSend.add(new Message(message.getData(), Message.ANSWER, 0,
                    message.getReceiver(), message.getSender(), message.getGuid()));
        }
    }

    public void checkRecvMessage() {

        Iterator<Message> iterator = messageSend.iterator();

        while (iterator.hasNext()) {

            Message mes = iterator.next();

            if (reciever.isAnswer(mes)) {
                synchronized (Node.o){
                    iterator.remove();
                }
            }
        }

        LinkedList<Message> mes = reciever.getUsualMessage();

        if (mes != null) {
            for (Message message: mes) {
                makeAnswer(message);
            }
        }
    }

    private void sendMessage(Message mes) throws SocketException {

        DatagramSocket socket = new DatagramSocket();
        String packetMessage = mes.getCode() + ":" + mes.getGuid() + ":" + mes.getSender().getPort() + ":" + mes.getData();
        byte[] buf = packetMessage.getBytes();
        DatagramPacket pack = null;

        if (mes.getReceiver() == null) {
            for (InetSocketAddress addresses: Node.connectors) {
                pack = new DatagramPacket(buf, 0, buf.length, addresses);
            }
        } else {
            if (Node.connectors.contains(mes.getReceiver())) {
                pack = new DatagramPacket(buf, 0, buf.length, mes.getReceiver());
            } else {
                return;
            }
        }
        try {
            socket.send(pack);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteConnector(InetSocketAddress address) {
        if (Node.connectors.contains(address)) {
            if (address == Node.parent){
                Node.isRoot = true;
            }
            Node.connectors.remove(address);
        }
    }

    @Override
    public void run() {

        while (true) {

            Iterator<Message> iterator = messageSend.iterator();

            while (iterator.hasNext()) {

                Message mes = iterator.next();

                if (mes.getCountOfSend() == Message.STOPSEND){
                    synchronized (Node.o){
                        deleteConnector(mes.getSender());
                        iterator.remove();
                    }
                    continue;
                }

                if ((mes.getCode() == Message.USUAL) || (mes.getCode() == Message.NEWCHILD)){

                    try {
                        sendMessage(mes);
                    } catch(SocketException e) {
                        e.printStackTrace();
                    }

                    mes.addSend();
                }

                if (mes.getCode() == Message.ANSWER) {

                    try {
                        sendMessage(mes);
                    } catch(SocketException e) {
                        e.printStackTrace();
                    }

                    iterator.remove();
                }
            }
            checkRecvMessage();
        }
    }

    private static LinkedList<Message> messageSend = new LinkedList<>();
    Reciever reciever;
}
