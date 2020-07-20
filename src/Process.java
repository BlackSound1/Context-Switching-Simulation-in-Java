import java.util.ArrayList;

/**
 * A class for defining a Process
 */
public class Process implements Runnable{
    private String PID;
    private int arrivalTime;
    private int totalExecutionTime;
    private ArrayList<Integer> IORequestTime;
    private ProcessState status;

    public Process() {
        PID = "NULL";
        arrivalTime = -1;
        totalExecutionTime = -1;
        IORequestTime = null;
        status = null;
    }

    public Process(String PID, int arrivalTime, int totalExecutionTime, ArrayList<Integer> IORequestTime,
                   ProcessState status) {
        this.PID = PID;
        this.arrivalTime = arrivalTime;
        this.totalExecutionTime = totalExecutionTime;
        this.IORequestTime = IORequestTime;
        this.status = status;
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

    public void setStatus(ProcessState status) {
        this.status = status;
    }

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

    @Override
    public void run() {
        System.out.println("\n" + toString());
    }
}