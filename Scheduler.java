import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

// The Scheduler class implements a runnable interface to be executed in a separate thread.
// It simulates scheduling processes based on a modified round-robin algorithm
// where the quantum is dynamically calculated as 10% of a process's remaining execution time.
public class Scheduler implements Runnable {
    private List<Process> processes; // List of all processes to be scheduled.
    private final LinkedList<Process> readyQueue = new LinkedList<>(); // Queue of processes ready to run.
    private static int currentTime = 0; // Tracks the current time in the simulation.
    private PrintWriter writer; // For writing the output to a file.

    // Constructor takes a list of processes, sorts them by arrival time to ensure correct scheduling order.
    public Scheduler(List<Process> processes) {
        this.processes = processes;
        // Sort processes by arrival time to handle them in the order they come in.
        this.processes.sort(Comparator.comparingInt(Process::getArrivalTime));
    }

    @Override
    public void run() {
        try {
            // Initialize PrintWriter to write scheduling events to "output.txt".
            writer = new PrintWriter(new FileWriter("output.txt", false));
            // Loop until all processes are completed.
            while (!allProcessesCompleted()) {
                // Move processes that have arrived by the current time to the ready queue.
                moveArrivedProcessesToReadyQueue();
                // Check if there are processes ready to run.
                if (!readyQueue.isEmpty()) {
                    Process nextProcess = readyQueue.poll(); // Retrieve and remove the head of the queue.
                    // Quantum is dynamically calculated as 10% of the process's remaining time.
                    double quantum = nextProcess.isFinished() ? 0 : Math.ceil(nextProcess.getRemainingTime() * 0.1);
                    // Log start or resume event for the process.
                    if (nextProcess.isFirstExecution()) {
                        writer.println("Time " + currentTime + ", Process " + nextProcess.getId() + ", Started");
                    } else {
                        writer.println("Time " + currentTime + ", Process " + nextProcess.getId() + ", Resumed");
                    }
                    // Execute process for a quantum of time.
                    nextProcess.execute(currentTime);
                    // Update current time by the quantum after execution.
                    currentTime += quantum;
                    // Check if process is finished to log the event, otherwise, it is paused and returned to the queue.
                    if (nextProcess.isFinished()) {
                        writer.println("Time " + currentTime + ", Process " + nextProcess.getId() + ", Finished");
                    } else {
                        writer.println("Time " + currentTime + ", Process " + nextProcess.getId() + ", Paused");
                        readyQueue.add(nextProcess); // Add the process back to the end of the queue if not finished.
                    }
                } else {
                    // If no process is ready, simply increment the time.
                    currentTime++;
                }
            }
            // After all processes are completed, output their waiting times.
            outputWaitingTimes();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Ensure the writer is closed to flush any output to the file.
            if (writer != null) {
                writer.close();
            }
        }
    }

    // Method to output waiting times of all processes after completion.
    private void outputWaitingTimes() {
        writer.println("\nWaiting Times:");
        for (Process process : processes) {
            writer.println("Process " + process.getId() + ": " + process.getWaitingTime());
        }
        writer.flush(); // Ensure data is written to the file.
    }

    // Moves processes that have arrived (based on current time) to the ready queue.
    private void moveArrivedProcessesToReadyQueue() {
        for (Process process : processes) {
            if (process.getArrivalTime() <= currentTime && !process.isFinished() && !readyQueue.contains(process)) {
                readyQueue.add(process);
            }
        }
    }

    // Checks if all processes have been completed.
    private boolean allProcessesCompleted() {
        return processes.stream().allMatch(Process::isFinished) && readyQueue.isEmpty();
    }
}