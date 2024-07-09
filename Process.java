public class Process {
    private final int id;
    private final int arrivalTime;
    private final int totalBurstTime;
    private int remainingTime;
    private int waitingTime = 0;
    private boolean firstExecution = true;
    private int lastResumeTime;

    // Constructor to initialize process attributes.
    public Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.totalBurstTime = burstTime;
        this.remainingTime = burstTime;
        this.lastResumeTime = arrivalTime; // Set to arrival time initially.
    }

    // Executes the process for a quantum of time and updates waiting and remaining times.
    public void execute(int currentTime) {
        if (firstExecution) {
            firstExecution = false;
            // Calculate waiting time from arrival to first execution.
            waitingTime += currentTime - arrivalTime;
        } else {
            // Update waiting time for subsequent executions.
            waitingTime += currentTime - lastResumeTime;
        }
        // Quantum is 10% of the remaining time, rounded up.
        int quantum = (int) Math.ceil(this.remainingTime * 0.1);
        this.remainingTime -= quantum;
        if (this.remainingTime < 0) {
            this.remainingTime = 0; // Ensure remaining time is not negative.
        }
        // Update lastResumeTime for the next execution.
        lastResumeTime = currentTime + quantum;
    }

    // Setter for lastResumeTime, used when process is resumed to update its resume time
    public void updateLastResumeTime(int currentTime) {
        lastResumeTime = currentTime;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Getter for arrivalTime
    public int getArrivalTime() {
        return arrivalTime;
    }

    // Getter for totalBurstTime
    public int getTotalBurstTime() {
        return totalBurstTime;
    }

    // Getter for remainingTime
    public int getRemainingTime() {
        return remainingTime;
    }

    // Getter for waitingTime
    public int getWaitingTime() {
        return waitingTime;
    }

    // Method to check if the process has finished its execution
    public boolean isFinished() {
        return remainingTime == 0;
    }

    // Method to check if it's the first execution of the process
    public boolean isFirstExecution() {
        return firstExecution;
    }
}
