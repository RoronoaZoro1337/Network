import java.net.InetSocketAddress;
import java.util.UUID;

public class Message {

    public Message(String data, int code, int countOfSend, InetSocketAddress sender, UUID guid){
        this.data = data;
        this.code = code;
        this.countOfSend = countOfSend;
        startTime = 0L;
        this.sender = sender;
        this.guid = guid;
    }

    public Message(String data, int code, InetSocketAddress sender, UUID guid){
        this.data = data;
        this.code = code;
        startTime = 0L;
        this.sender = sender;
        this.guid = guid;
    }

    public Message(String data, int code, int countOfSend, InetSocketAddress sender, InetSocketAddress receiver, UUID guid){
        this.data = data;
        this.code = code;
        this.countOfSend = countOfSend;
        startTime = 0L;
        this.sender = sender;
        this.receiver = receiver;
        this.guid = guid;
    }

    boolean timeToDelete() {
        return System.currentTimeMillis() - startTime > 30000;
    }

    public String getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public int getCountOfSend() {
        return countOfSend;
    }

    public InetSocketAddress getSender() {
        return sender;
    }

    public InetSocketAddress getReceiver() {
        return receiver;
    }

    public UUID getGuid() {
        return guid;
    }

    public void addSend(){
        countOfSend++;
    }

    private String data;
    private int code;
    private int countOfSend;
    private Long startTime;
    private InetSocketAddress sender;
    private InetSocketAddress receiver = null;
    private UUID guid;
    static final int USUAL = 170;
    static final int ANSWER = 753;
    static final int STOPSEND = 3;
    static final int NEWCHILD = 12;
}
