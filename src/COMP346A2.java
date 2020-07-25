import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

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

        // Sets all Processes to READY and insert into the Queue
        for (Process process: listOfProcessObjects) {
            process.setStatus(ProcessState.READY);
            processQueue.add(process);
        }

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

        // Inserts IODevice into it's Queue
        IOReadyQueue.add(ioDevice);

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
        resetAllProcesses(processes);
        resetAllIO(device);
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

