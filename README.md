**Problem Statement:**
- Create a scheduler that allocates CPU time to processes in a cyclic manner.
- The quantum for each process is 10% of its remaining execution time.
- The scheduler prioritizes processes with the shortest remaining time but ensures fairness to avoid starvation.
- If processes have equal remaining times, the older process gets priority.

**Input/Output:**
- **Input File (`input.txt`):** Each line contains the arrival time and burst time of a process.
- **Output File (`output.txt`):** Logs events such as the start, pause, resume, and finish times of each process, as well as the total waiting time for each process.

**Sample Output:**
```plaintext
Time 1, Process 1, Started
Time 1, Process 1, Resumed
Time 2, Process 1, Paused
Time 2, Process 2, Started
...
Waiting Times:
Process 1: 4
Process 2: 2
Process 3: 2
```

**Implementation Requirements:**
- Simulate processes and the scheduler using threads, with only one process running at a time.
- The scheduler should manage the process execution based on the scheduling policy.
- The program must handle an arbitrary number of threads.

