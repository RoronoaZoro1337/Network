public class Timer extends Thread{

    public Timer(){
        downTime = 0;
        kiloBytesPerSession = 0;
        kiloBytesPerSecond = 0;
        timeSession = 0;
        exitFlag = false;
        interruptFlag = false;
    }

    public void increaseNumOfBytes(final int readBytes){
        kiloBytesPerSecond = kiloBytesPerSecond + readBytes / BYTES_PER_KB;
        kiloBytesPerSession = kiloBytesPerSession + readBytes / BYTES_PER_KB;
    }

    public void setExitFlag(){
        exitFlag = true;
    }

    public boolean getInterruptFlag(){
        return interruptFlag;
    }

    private void checkDownTime() throws InterruptedException {
        if(kiloBytesPerSecond > 0){
            downTime = 0;
        } else {
            downTime++;
        }
        if(downTime > 5){
            throw new InterruptedException();
        }
    }

    public void run() {
        try {
            while (!exitFlag) {
                Thread.sleep(TIMER_PERIOD);
                timeSession++;
                System.out.println("Speed: " + kiloBytesPerSecond + " Kb/s "
                        + "Avg: " + kiloBytesPerSession/timeSession + " Kb/s");
                checkDownTime();
                kiloBytesPerSecond = 0;
            }
        } catch (InterruptedException e) {
            System.out.println("ERROR: The transferring was interrupted");
            interruptFlag = true;
        }
    }

    volatile private int kiloBytesPerSecond;
    volatile private long kiloBytesPerSession;
    volatile private int timeSession;
    volatile private boolean exitFlag;
    volatile private boolean interruptFlag;
    private char downTime;
    private final int TIMER_PERIOD = 1000;
    private final int BYTES_PER_KB = 1024;
}
