/**
 * @author Matthew Segal
 * @author Laura Boivin
 *
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class COMP346A2
{
    static int numOfCPUs = 0;

    public static void main(String[] args) {
        ArrayList<Process> listOfProcessObjects;
        ArrayList<CPU> listOfCPUObjects = new ArrayList<>();
        ArrayList<Thread> listOfThreadObjects = new ArrayList<>();

        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream(
                    "text/input.txt"));
        }

        catch (FileNotFoundException e) {
            System.out.println("Error in the path to the text file.");
            sc.close();
            System.exit(0);
        }

        // Gets the list of all Processes
        listOfProcessObjects = readFile(sc);

        // Fills up list of CPUs with new CPUs based on the numOfCPUs
        for (int i = 0; i < numOfCPUs; i++) {
            CPU cpu = new CPU();
            cpu.setCPUID(i);
            listOfCPUObjects.add(cpu);
        }

        System.out.println("TESTING");

        assignProcessesToCPUs(listOfProcessObjects, listOfCPUObjects);

        // Create listOfCPUObjects.size() threads
        /*for (Process process : listOfProcessObjects) {
            listOfThreadObjects.add(new Thread(listOfCPUObjects.get()));
        }*/

        for (CPU listOfCPUObject : listOfCPUObjects) {
            listOfThreadObjects.add(new Thread(listOfCPUObject));
        }



        // Run all Threads
        for (Thread thread: listOfThreadObjects) {
            thread.start();
        }

    }

    private static void assignProcessesToCPUs(ArrayList<Process> processes, ArrayList<CPU> cpus){
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
    }


    private static void firstComeFirstServe(){

    }

    private static void shortestJobFirst(){

    }

    private static void roundRobin(int timeQuantum){

    }

    private static void shortestRemainingTimeFirst(){

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

}

