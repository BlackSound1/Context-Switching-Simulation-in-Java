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
        status = ProcessState.NEW;
    }

    public Process(String PID, int arrivalTime, int totalExecutionTime, ArrayList<Integer> IORequestTime) {
        this.PID = PID;
        this.arrivalTime = arrivalTime;
        this.totalExecutionTime = totalExecutionTime;
        this.IORequestTime = IORequestTime;
        status = ProcessState.NEW;
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
        status = ProcessState.READY;
        status = ProcessState.RUNNING;
        System.out.println("\n" + toString());
        status = ProcessState.TERMINATED;
    }
}
