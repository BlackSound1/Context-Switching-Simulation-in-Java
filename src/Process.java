import java.util.ArrayList;

/**
 * A class for defining a Process
 */
public class Process{
    private String PID;
    private int arrivalTime;
    private int totalExecutionTime;
    private ArrayList<Integer> IORequestTime;
    private ProcessState status;
    private int IOTimer;

    public Process() {
        PID = "NULL";
        arrivalTime = -1;
        totalExecutionTime = -1;
        IORequestTime = null;
        status = ProcessState.NEW;
        IOTimer = 0;
    }

    public Process(String PID, int arrivalTime, int totalExecutionTime, ArrayList<Integer> IORequestTime) {
        this.PID = PID;
        this.arrivalTime = arrivalTime;
        this.totalExecutionTime = totalExecutionTime;
        this.IORequestTime = IORequestTime;
        status = ProcessState.NEW;
        IOTimer = 0;
    }

    public String getPID() {
        return PID;
    }

    public void setPID(String PID) {
        this.PID = PID;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getTotalExecutionTime() {
        return totalExecutionTime;
    }

    public void setTotalExecutionTime(int totalExecutionTime) {
        this.totalExecutionTime = totalExecutionTime;
    }

    public ArrayList<Integer> getIORequestTime() {
        return IORequestTime;
    }

    public void setIORequestTime(ArrayList<Integer> IORequestTime) {
        this.IORequestTime = IORequestTime;
    }

    public ProcessState getStatus() {
        return status;
    }

    public void setStatus(ProcessState status) { this.status = status; }

    public int getIOTimer() { return IOTimer; }

    public void setIOTimer(int IOTimer) { this.IOTimer = IOTimer; }

    @Override
    public String toString() {
        return "Process{" +
                "PID='" + PID + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", totalExecutionTime=" + totalExecutionTime +
                ", IORequestTime=" + IORequestTime +
                ", status=" + status +
                '}';
    }
}
