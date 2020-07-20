/**
 * @author Matthew Segal
 * @author Laura Boivin
 *
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class COMP346A2 {
    public static void main(String[] args) {
        Scanner sc = null;
        try {
            sc = new Scanner(new FileInputStream(
                    "/users/lauraboivin/Desktop/Comp346/A2/input.txt"));
        }

        catch (FileNotFoundException e) {
            System.out.println("Error in the path to the text file.");
            sc.close();
            System.exit(0);
        }

        readFile(sc);

        System.out.println("TEST");
        Thread TESTTHREAD = new Thread(new Process());
        TESTTHREAD.start();
    }


    private void firstComeFirstServe(){

    }

    private void shortestJobFirst(){

    }

    private void roundRobin(int timeQuantum){

    }

    private void shortestRemainingTimeFirst(){

    }

    private static ArrayList readFile(Scanner sc){
        int processNbr = 0;
        ArrayList<Process> listOfProcessObjects = new ArrayList<Process>();
        int numOfCPUs = 0;
        String processID;
        int arrivalTime;
        int totalExecTime;

        String str = null;

        while (sc.hasNextLine())
        {
            str = sc.nextLine();

            if (str.contains("numOfCPUs:")) {
                String cpus[] = str.split("\\s+");
                numOfCPUs = Integer.parseInt(cpus[cpus.length-1]);
            }

            if (str.contains("p" + processNbr)) {

                String lineArray[] = str.split("\\s+");

                processID = lineArray[0];
                arrivalTime = Integer.parseInt(lineArray[1]);
                totalExecTime = Integer.parseInt(lineArray[2]);
                ArrayList<Integer> IO_RequestAtTime = new ArrayList<Integer>();

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
       /*
       for (int i =0; i<listOfProcessObjects.size(); i++)
        {
            System.out.println(listOfProcessObjects.get(i));
       }
        */
        return listOfProcessObjects;
    }



    }

