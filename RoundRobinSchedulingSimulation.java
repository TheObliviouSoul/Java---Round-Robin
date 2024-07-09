import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RoundRobinSchedulingSimulation {
    // Entry point of the program.
    public static void main(String[] args) {
        // Read the list of processes from the input file.
        List<Process> processes = readProcessesFromFile("input.txt");
        // Check if no processes were found or if there was an issue reading the file.
        if (processes.isEmpty()) {
            System.out.println("No processes found or unable to read the file.");
            return;
        }

        // Create a scheduler object with the list of processes.
        Scheduler scheduler = new Scheduler(processes);
        // Start the scheduler in a new thread.
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();
        try {
            // Wait for the scheduler to finish all processes.
            schedulerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Scheduling was interrupted.");
        }
    }

    // Reads processes from a file and returns a list of Process objects.
    private static List<Process> readProcessesFromFile(String fileName) {
        List<Process> processes = new ArrayList<>();
        String absolutePath = "/Users/rycy/Library/CloudStorage/OneDrive-Personal/Documents/Concordia/COEN346/Assignments/Programming Assignment 3/Programming_Assignment_3_Ryan_Rahul/src/" + fileName;
        File file = new File(absolutePath);
        try (Scanner scanner = new Scanner(file)) {
            int processId = 1; // Initialize process ID to assign unique IDs to each process.
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\s+");
                    if (parts.length == 2) {
                        try {
                            int arrivalTime = Integer.parseInt(parts[0]);
                            int burstTime = Integer.parseInt(parts[1]);
                            // Add a new process to the list with the read values.
                            processes.add(new Process(processId++, arrivalTime, burstTime));
                        } catch (NumberFormatException e) {
                            System.err.println("Skipping invalid line: " + line);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
        }
        return processes;
    }
}
