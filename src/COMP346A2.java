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
    static Queue<IODevice> IOReadyQueue = new LinkedList<>();

    public static void main(String[] args) {
        ArrayList<Process> listOfProcessObjects;
        ArrayList<CPU> listOfCPUObjects = new ArrayList<>();
        IODevice ioDevice = new IODevice();
        ioDevice.setIOID(0);

        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream("text/input.txt"));
        }catch (FileNotFoundException e) {
            System.out.println("Error in the path to the text file.");
            System.exit(0);
        }

        // Gets the list of all Processes
        listOfProcessObjects = readFile(sc);

        /*// Sort by arrival time
        Collections.sort(listOfProcessObjects);

        // Sets all Processes to READY and insert into the Queue
        for (Process process: listOfProcessObjects) {
            process.setStatus(ProcessState.READY);
            processQueue.add(process);
        }*/

        // Fills up list of CPUs with new CPUs based on the numOfCPUs
        for (int i = 0; i < numOfCPUs; i++) {
            CPU cpu = new CPU();
            cpu.setCPUID(i);
            listOfCPUObjects.add(cpu);
        }

        // Sets all CPUs to READY and inserts into Queue
        for (CPU cpu: listOfCPUObjects) {
            cpu.setState(CPUState.READY);
            readyQueue.add(cpu);
        }

        /*// Inserts IODevice into it's Queue
        IOReadyQueue.add(ioDevice);*/

        // DISPLAYS INFO OF EACH PROCESS
        for (Process process : listOfProcessObjects) {
            System.out.println(process.toString());
        }

        // MAIN CODE
        System.out.println("Now performing a simulation based on FCFS...");
        firstComeFirstServe(listOfCPUObjects, listOfProcessObjects, ioDevice);
        System.out.println("FCFS simulation finished!");

        System.out.println("Now performing a simulation based on SJF...");
        shortestJobFirst(listOfCPUObjects, listOfProcessObjects, ioDevice);
        System.out.println("SJF simulation finished!");

        System.out.println("Now performing a simulation based on SRTF...");
        shortestRemainingTimeFirst(listOfCPUObjects, listOfProcessObjects, ioDevice);
        System.out.println("SRTF simulation finished!");

        System.out.print("Now for Round-robin. Please choose an integer time-quantum: ");
        // Sets up Scanner for user input
        sc.close(); // closes Scanner to the file
        sc = new Scanner(System.in);
        int quantum = sc.nextInt();
        sc.close(); // closes Scanner to user input

        roundRobin(quantum, listOfCPUObjects, listOfProcessObjects, ioDevice);
        System.out.println("RR simulation finished!");

        System.out.println("THE PROGRAM FINISHES");
    }

    private static void resetAllIO(IODevice device){
        device.setStatus(IODeviceState.READY);
    }

    private static void resetAllCPUs(ArrayList<CPU> cpus){
        for (CPU cpu: cpus) {
            cpu.setState(CPUState.READY);
            readyQueue.add(cpu);
        }
    }

    private static void resetAllProcesses(ArrayList<Process> processes){
        for (Process process : processes) {
            process.setStatus(ProcessState.READY);
            processQueue.add(process);
        }
    }

    private static void firstComeFirstServe(ArrayList<CPU> cpus, ArrayList<Process> processes, IODevice device){
        resetAllCPUs(cpus);
        //resetAllProcesses(processes);
        //resetAllIO(device);

        int timeUnit = 0;

        while (true){
            // Handles checking if each Process is TERMINATED
            int terminatedCounter = 0;
            for (Process process : processes) {
                if (process.getStatus().equals(ProcessState.TERMINATED)){
                    terminatedCounter++;
                }
            }

            // If each Process is TERMINATED, leave the loop
            if (terminatedCounter == processes.size() ){ break; }

            System.out.println(processes.get(0));

            for (Process process : processes) {
                if (!process.getStatus().equals(ProcessState.TERMINATED) && process.getIOTimer() == 2) {
                    process.setStatus(ProcessState.READY);
                    process.setIOTimer(0); // Resets IOTimer
                    process.setIsWaiting(false); // Resets if its waiting
                    processQueue.add(process);
                    System.out.println("Process " + process.getPID() + "added to processQueue");
                }
            }

            // Loops through the processList, and if there is one that has an arrival time of now,
            // add it to the processQueue
            for (Process process: processes) {
                if (process.getArrivalTime() == timeUnit){
                    process.setStatus(ProcessState.READY);
                    processQueue.add(process);
                    // If the execution time is over, set it to TERMINATED
                }else if (process.getTotalExecutionTime() == process.getExecutionTime()){
                    process.setStatus(ProcessState.TERMINATED);
                    System.out.println("Process " + process.getPID() + " is TERMINATED");
                }


            }

            /*// Handles putting Processes back into processQueue after their IOTimers are up
            for (Process process: processes) {
                if (process.getStatus() != ProcessState.TERMINATED && process.getIOTimer() == 2){
                    process.setStatus(ProcessState.READY);
                    process.setIOTimer(0); // Resets IOTimer
                    process.setIsWaiting(false); // Resets if its waiting
                    processQueue.add(process);
                }
            }*/

            // IO REQUESTS
            // Loops through all CPUs
            for (CPU cpu: cpus) {
                // If the CPU has a Process
                if (cpu.getProcess() != null){
                    Process currentProcess = cpu.getProcess(); // Gets the Process

                    // If there is an IO time right now
                    //ArrayList<Integer> IORequests = currentProcess.getIORequestTime();

                    if (currentProcess.getIORequestTime() != null && currentProcess.getIORequestTime().get(0) + currentProcess.getExecutionTime() == timeUnit){
                        // Remove Process from CPU, set CPU state to READY and add the CPU to the ready Queue
                        cpu.setProcess(null);
                        cpu.setState(CPUState.READY);
                        readyQueue.add(cpu);

                        currentProcess.getIORequestTime().remove(0); // Removes the used-up IO time

                        currentProcess.setStatus(ProcessState.WAITING);
                        currentProcess.setIsWaiting(true);
                    }
                }
            }



            //ASSIGNS CPU IF POSSIBLE
            // First check if readyQueue has available CPUs
            if (!readyQueue.isEmpty()){
                // Then check if processQueue has Processes waiting to be processed
                if (!processQueue.isEmpty()){
                    // Gets current Process from Queue
                    Process currentProcess = processQueue.peek();

                    // Assign a Process to a CPU if the current time is its arrival time, or
                    // if its arrival time has passed
                    if (currentProcess.getArrivalTime() <= timeUnit){
                        CPU currentCPU = readyQueue.remove(); // Remove CPU from readyQueue
                        processQueue.remove(); // Remove Process from processQueue
                        currentCPU.setProcess(currentProcess); // Set the Process to the CPU
                        System.out.println("Process " + currentProcess.getPID() + " added to CPU " + currentCPU.getCPUID());

                        // Sets the statuses of the Process and CPU
                        currentCPU.setState(CPUState.BUSY);
                        currentProcess.setStatus(ProcessState.RUNNING);
                    }
                }
            }

            // Increase the lifetime timer for all processes that have started to be serviced and aren't WAITING
            // and increase the IOTimer if it's WAITING
            for (Process pro: processes) {
                if (pro.getStatus() != ProcessState.TERMINATED){
                    /*if (pro.getHasBeenProcessed() && !pro.getIsWaiting()){
                        pro.setLifeTimeTimer(pro.getLifeTimeTimer() + 1);
                    }*/
                    if (pro.getIsWaiting()){
                        pro.setIOTimer(pro.getIOTimer() + 1);
                    }
                }
            }

            // Increases executionTime as long a a Process is on a CPU (therefore RUNNING)
            // Also, increments a CPUs utilization time
            for (CPU cpu : cpus) {
                if (cpu.getProcess() != null && !cpu.getProcess().getStatus().equals(ProcessState.TERMINATED)){
                    Process currentProcess = cpu.getProcess();
                    currentProcess.setExecutionTime(currentProcess.getExecutionTime() + 1);
                    System.out.println("Process " + currentProcess.getPID() + " execution time is now " + currentProcess.getExecutionTime());
                    cpu.setUtilization(cpu.getUtilization() + 1);
                }
            }

            ++timeUnit;

            System.out.println("LOOPING");
        }

        for (Process p : processes) {
            System.out.println(p.toString());
        }

        // HANDLES DISPLAYING CPU UTILIZATION
        for (CPU cpu : cpus) {
            System.out.println("CPU:" + cpu.getCPUID() + " has been used for " + cpu.getUtilization() + "/"
                    + timeUnit + " time");
        }
    }

    private static void shortestJobFirst(ArrayList<CPU> cpus, ArrayList<Process> processes, IODevice device){
        resetAllCPUs(cpus);
        resetAllProcesses(processes);
        resetAllIO(device);
    }

    private static void roundRobin(int timeQuantum, ArrayList<CPU> cpus, ArrayList<Process> processes, IODevice device){
        resetAllCPUs(cpus);
        resetAllProcesses(processes);
        resetAllIO(device);
    }

    private static void shortestRemainingTimeFirst(ArrayList<CPU> cpus, ArrayList<Process> processes, IODevice device){
        resetAllCPUs(cpus);
        resetAllProcesses(processes);
        resetAllIO(device);
    }

    private static ArrayList<Process> readFile(Scanner sc)
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

    /*private static void assignProcessesToCPUs(ArrayList<Process> processes, ArrayList<CPU> cpus){
        // If there are more Processes than CPUs...
        if (processes.size() > cpus.size()){
            // Loop cpus.size() times and...
            for (int i = 0; i < cpus.size(); i++) {
                // assign Processes to CPUs
                cpus.get(i).setProcess(processes.get(i));
            }
        // If there are more CPUs than Processes...
        }else if (processes.size() < cpus.size()){
            // Loop processes.size() times and...
            for (int i = 0; i < processes.size(); i++) {
                // assign Processes to CPUs
                cpus.get(i).setProcess(processes.get(i));
            }
        // If they have the same count...
        }else {
            // It doesn't matter if we count processes.size() or cpus.size() times
            for (int i = 0; i < processes.size(); i++) {
                // assign Processes to CPUs
                cpus.get(i).setProcess(processes.get(i));
            }
        }
    }*/
}

