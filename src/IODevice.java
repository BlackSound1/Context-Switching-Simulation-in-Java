public class IODevice {
    private int IOID;
    private Process process;

    public IODevice() {
        IOID = -1;
        process = null;
    }

    public IODevice(int IOID, Process process) {
        this.IOID = IOID;
        this.process = process;
    }

    public int getIOID() {
        return IOID;
    }

    public void setIOID(int IOID) {
        this.IOID = IOID;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }


}
