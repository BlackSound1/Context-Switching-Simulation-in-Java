import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Matthew Segal
 * @author Laura Boivin
 *
 */
public class COMP346A2
{
    static int numOfCPUs = 0;
    static Queue<CPU> readyQueue = new LinkedList<>();
    static Queue<Process> processQueue = new LinkedList<>();
    static Scanner sc;

    public static void main(String[] args) {
        ArrayList<Process> listOfProcessObjects;
        ArrayList<CPU> listOfCPUObjects = new ArrayList<>();

        sc = null;
        setScannerToFileMode();

        // Gets the list of all Processes
        listOfProcessObjects = readFile();

        // Fills up list of CPUs with new CPUs based on the numOfCPUs
        for (int i = 0; i < numOfCPUs; i++) {
            CPU cpu = new CPU();
            cpu.setCPUID(i);
            listOfCPUObjects.add(cpu);
        }

        // MAIN CODE
        System.out.println("Now performing a simulation based on FCFS...\n");
        firstComeFirstServe(listOfCPUObjects, listOfProcessObjects);
        System.out.println("FCFS simulation finished!\n");

        // Gets the list of all Processes
        resetScanner();
        listOfProcessObjects = readFile();

        System.out.println("Now performing a simulation based on SJF...\n");
        shortestJobFirst(listOfCPUObjects, listOfProcessObjects);
        System.out.println("SJF simulation finished!\n");

        // Gets the list of all Processes
        resetScanner();
        listOfProcessObjects = readFile();

        System.out.println("Now performing a simulation based on SRTF...\n");
        shortestRemainingTimeFirst(listOfCPUObjects, listOfProcessObjects);
        System.out.println("SRTF simulation finished!\n");

        // Gets the list of all Processes
        resetScanner();
        listOfProcessObjects = readFile();

        System.out.print("Now for Round-robin. Please choose an integer time-quantum: ");
        // Sets up Scanner for user input
        sc.close(); // closes Scanner to the file
        setScannerToUserMode();
        int quantum = sc.nextInt();

        roundRobin(quantum, listOfCPUObjects, listOfProcessObjects);
        System.out.println("RR simulation finished!\n");

        System.out.println("THE PROGRAM FINISHES");

        sc.close(); // closes Scanner to user input
    }

    private static void resetScanner(){
        sc.close();
        setScannerToFileMode();
    }

    private static void setScannerToFileMode(){
        try {
            sc = new Scanner(new FileInputStream("text/input.txt"));
        }catch (FileNotFoundException e) {
            System.out.println("Error in the path to the text file.");
            System.exit(0);
        }
    }

    private static void setScannerToUserMode(){
        sc = new Scanner(System.in);
    }

    private static void resetAllCPUs(ArrayList<CPU> cpus){
        for (CPU cpu: cpus) {
            cpu.setState(CPUState.READY);
            readyQueue.add(cpu);
        }
    }

    private static void printProcesses(ArrayList<Process> listOfProcessObjects){
        // DISPLAYS INFO OF EACH PROCESS
        System.out.println("The Process are as follows:");
        for (Process process : listOfProcessObjects) {
            System.out.println(process.toString());
        }
    }

    private static boolean checkIfAllTerminated(ArrayList<Process> processes){
        int terminatedCounter = 0;
        for (Process process : processes) {
            if (process.getStatus().equals(ProcessState.TERMINATED)){
                terminatedCounter++;
            }
        }
        return terminatedCounter == processes.size();
    }

    private static void displayCPUUtilization(ArrayList<CPU> cpus, int timeUnit){
        for (CPU cpu : cpus) {
            System.out.println("CPU:" + cpu.getCPUID() + " has been used for " + cpu.getUtilization() + "/"
                    + timeUnit + " time");
        }
    }

    private static void firstComeFirstServe(ArrayList<CPU> cpus, ArrayList<Process> processes){
        resetAllCPUs(cpus);

        printProcesses(processes);

        int timeUnit = 0;

        while (!checkIfAllTerminated(processes)) {
            //System.out.println("timeUnit is now "+timeUnit);
            // Handles checking if each Process is TERMINATED
            /*int terminatedCounter = 0;
            for (Process process : processes) {
                if (process.getStatus().equals(ProcessState.TERMINATED)){
                    terminatedCounter++;
                }
            }

            // If each Process is TERMINATED, leave the loop
            if (terminatedCounter == processes.size() ){ break; }*/

            // IO REQUESTS
            // Loops through all CPUs
            for (CPU cpu : cpus) {
                // If the CPU has a Process
                if (cpu.getProcess() != null) {
                    Process currentProcess = cpu.getProcess(); // Gets the Process

                    //L: this isn't associated with the unit time, only the execution time
                    if (currentProcess.getIORequestTime() != null && currentProcess.getIORequestTime().size() != 0 &&
                            currentProcess.getIORequestTime().get(0) == currentProcess.getExecutionTime()) {

                        // Remove Process from CPU, set CPU state to READY and add the CPU to the ready Queue
                        cpu.setProcess(null);
                        cpu.setState(CPUState.READY);
                        //System.out.println("Process "+currentProcess.getPID()+" is waiting for io");
                        //System.out.println("CPU "+ cpu.getCPUID()+ " is now ready and in the ready queue");
                        readyQueue.add(cpu);

                        currentProcess.getIORequestTime().remove(0); // Removes the used-up IO time

                        currentProcess.setStatus(ProcessState.WAITING);
                        currentProcess.setIsWaiting(true);
                    }
                }
            }

            // Loops through the processList, if the ioRequest timer is done, add the process to the process queue and if there is one that has an arrival time of now,
            // add it to the processQueue otherwise if the process has reached its total execution time then change the status to terminated
            for (Process process : processes) {
                if (process.getIOTimer() == 2) {
                    process.setIOTimer(0); // Resets IOTimer
                    process.setIsWaiting(false); // Resets if its waiting
                    processQueue.add(process);
                    process.setStatus(ProcessState.READY); //L: just a logical swap of order
                    //System.out.println("Process " + process.getPID() + " added to processQueue");
                }

                if (!readyQueue.isEmpty() && !processQueue.isEmpty()) {
                    if (process.getStatus().equals(ProcessState.READY)) {
                        CPU currentCPU = readyQueue.remove(); // Remove CPU from readyQueue
                        //System.out.print(readyQueue);
                        // Gets current Process from Queue
                        Process currentProcess = processQueue.remove();// Remove Process from processQueue

                        //System.out.println("Current CPU:" + currentCPU.getCPUID());
                        currentCPU.setProcess(currentProcess); // Set the Process to the CPU
                        //System.out.println("Process " + currentProcess.getPID() + " added to CPU " + currentCPU.getCPUID());

                        // Sets the statuses of the Process and CPU

                        currentCPU.setState(CPUState.BUSY);
                        //System.out.println("CPU " + currentCPU.getCPUID() + " is now busy");
                        currentProcess.setStatus(ProcessState.RUNNING);
                    }
                }

                if (process.getArrivalTime() == timeUnit) {
                    process.setStatus(ProcessState.READY);
                    processQueue.add(process);
                    //System.out.println("Process " + process.getPID() + " added to processQueue");
                }

                // If the execution time is over, set it to TERMINATED
                if (process.getTotalExecutionTime() == process.getExecutionTime()) {
                    process.setStatus(ProcessState.TERMINATED);
                    //System.out.println("Process " + process.getPID() + " is TERMINATED");

                    for (CPU cpu : cpus) {
                        if (cpu.getProcess() != null && cpu.getProcess().getStatus().equals(ProcessState.TERMINATED)) {
                            cpu.setProcess(null);
                            cpu.setState(CPUState.READY);
                            readyQueue.add(cpu);
                        }
                    }
                }
            }

            // Increase the lifetime timer for all processes that have started to be serviced and aren't WAITING
            // and increase the IOTimer if it's WAITING
            for (Process pro : processes) {
                if (pro.getStatus() != ProcessState.TERMINATED) {
                    /*if (pro.getHasBeenProcessed() && !pro.getIsWaiting()){
                        pro.setLifeTimeTimer(pro.getLifeTimeTimer() + 1);
                    }*/
                    if (pro.getIsWaiting()) {
                        pro.setIOTimer(pro.getIOTimer() + 1);
                    }
                }
            }

            // Increases executionTime as long a a Process is on a CPU (therefore RUNNING)
            // Also, increments a CPUs utilization time
            for (CPU cpu : cpus) {
                if (cpu.getProcess() != null && cpu.getProcess().getStatus().equals(ProcessState.RUNNING)) { // L: changed condition from "not terminated" to "is running" because the cpu execution time should not increase if process status is "waiting"
                    Process currentProcess = cpu.getProcess();
                    currentProcess.setExecutionTime(currentProcess.getExecutionTime() + 1);
                    //System.out.println("Process " + currentProcess.getPID() + " execution time is now " + currentProcess.getExecutionTime());
                    cpu.setUtilization(cpu.getUtilization() + 1);
                }
            }
            timeUnit++;
            //System.out.println("LOOPING");
        }

        System.out.println("\nHere are the methods at the end of the FCFS simulation:");
        for (Process p : processes) {
            System.out.println(p.toString());
        }

        System.out.println();

        // HANDLES DISPLAYING CPU UTILIZATION
        displayCPUUtilization(cpus, timeUnit);
    }


    private static void shortestJobFirst(ArrayList<CPU> cpus, ArrayList<Process> processes){
        resetAllCPUs(cpus);

        printProcesses(processes);

        System.out.println("\nHere are the methods at the end of the SJF simulation:");
        for (Process p : processes) {
            System.out.println(p.toString());
        }

        System.out.println();

        // HANDLES DISPLAYING CPU UTILIZATION
        //displayCPUUtilization(cpus, timeUnit);
    }

    private static void roundRobin(int timeQuantum, ArrayList<CPU> cpus, ArrayList<Process> processes){
        resetAllCPUs(cpus);

        printProcesses(processes);

        System.out.println("\nHere are the methods at the end of the RR simulation:");
        for (Process p : processes) {
            System.out.println(p.toString());
        }

        System.out.println();

        // HANDLES DISPLAYING CPU UTILIZATION
        //displayCPUUtilization(cpus, timeUnit);
    }

    private static void shortestRemainingTimeFirst(ArrayList<CPU> cpus, ArrayList<Process> processes){
        resetAllCPUs(cpus);

        printProcesses(processes);

        System.out.println("\nHere are the methods at the end of the SRTF simulation:");
        for (Process p : processes) {
            System.out.println(p.toString());
        }

        System.out.println();

        // HANDLES DISPLAYING CPU UTILIZATION
        //displayCPUUtilization(cpus, timeUnit);
    }

    private static ArrayList<Process> readFile()
    {
        int processNbr = 0;
        ArrayList<Process> listOfProcessObjects = new ArrayList<>();
        String processID;
        int arrivalTime;
        int totalExecTime;

        String str;

        while (sc.hasNextLine())
        {
            str = sc.nextLine();

            if (str.contains("numOfCPUs:")) {
                String[] cpus = str.split("\\s+");
                numOfCPUs = Integer.parseInt(cpus[cpus.length-1]);
            }

            if (str.contains("p" + processNbr)) {

                String[] lineArray = str.split("\\s+");

                processID = lineArray[0];
                arrivalTime = Integer.parseInt(lineArray[1]);
                totalExecTime = Integer.parseInt(lineArray[2]);
                ArrayList<Integer> IO_RequestAtTime = new ArrayList<>();

                if(lineArray.length>3)
                {
                    for (int j = 3; j < lineArray.length; j++)
                    {
                        IO_RequestAtTime.add(Integer.parseInt(lineArray[j]));
                    }
                }
                else
                {
                    IO_RequestAtTime = null;
                }

                listOfProcessObjects.add(new Process(processID, arrivalTime, totalExecTime, IO_RequestAtTime));
                IO_RequestAtTime = null;
                processNbr++;
            }
        }
        return listOfProcessObjects;
    }

    /*private static void resetAllProcesses(ArrayList<Process> processes){
        for (Process process : processes) {
            process.setStatus(ProcessState.NEW);
            processQueue.add(process);
        }
    }*/
}

