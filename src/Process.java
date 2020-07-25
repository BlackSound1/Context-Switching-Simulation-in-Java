import com.sun.deploy.security.RootCertStore;

import java.util.ArrayList;

/**
 * A class for defining a Process
 */
public class Process implements Comparable{
    private String PID;
    private int arrivalTime;
    private int totalExecutionTime;
    private ArrayList<Integer> IORequestTime;
    private ProcessState status;
    private int IOTimer;
    //private int lifeTimeTimer;
    private boolean hasBeenProcessed;
    private boolean isWaiting;
    private int executionTime;

    public Process() {
        PID = "NULL";
        arrivalTime = -1;
        totalExecutionTime = -1;
        IORequestTime = null;
        status = ProcessState.NEW;
        IOTimer = 0;
        hasBeenProcessed = false;
        isWaiting = false;
        executionTime = 0;
    }

    public Process(String PID, int arrivalTime, int totalExecutionTime, ArrayList<Integer> IORequestTime) {
        this.PID = PID;
        this.arrivalTime = arrivalTime;
        this.totalExecutionTime = totalExecutionTime;
        this.IORequestTime = IORequestTime;
        status = ProcessState.NEW;
        IOTimer = 0;
        hasBeenProcessed = false;
        isWaiting = false;
        executionTime = 0;
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

    /*public int getLifeTimeTimer() { return lifeTimeTimer; }

    public void setLifeTimeTimer(int lifeTimeTimer) { this.lifeTimeTimer = lifeTimeTimer; }*/

    public boolean getHasBeenProcessed() { return hasBeenProcessed; }

    public void setHasBeenProcessed(boolean hasBeenProcessed) { this.hasBeenProcessed = hasBeenProcessed; }

    public boolean getIsWaiting() { return isWaiting; }

    public void setIsWaiting(boolean waiting) { isWaiting = waiting; }

    public int getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(int executionTime) {
        this.executionTime = executionTime;
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
    public int compareTo(Object process) {
        int arrivalTimeToCompare = ((Process)process).arrivalTime;
        return this.arrivalTime - arrivalTimeToCompare;
    }



}
