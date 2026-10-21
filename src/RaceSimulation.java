import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This Java program simulates two teams of four racers crossing three ropes.
 * Racers select available ropes for forward and return crossings.
 * Student name: Abdul Muqeet Ahmed. Student number: 000916755
 */
public class RaceSimulation {

    /** Represents the first rope. */
    public static RopeCrossing Rope1 = new RopeCrossing("ROPE ONE");
    /** Represents the second rope. */
    public static RopeCrossing Rope2 = new RopeCrossing("ROPE TWO");
    /** Represents the third rope. */
    public static RopeCrossing Rope3 = new RopeCrossing("ROPE THREE");
    /** Stores the ropes that racers can select. */
    public static RopeCrossing[] ropes = {Rope1, Rope2, Rope3};
    /** Stores the two teams competing in the race. */
    public static Team[] teams = {new Team("Team 0"), new Team("Team 1")};
    /** Stores the eight racers in the simulation. */
    public static Racer[] racers = new Racer[8];
    /** Stores the eight racer threads. */
    public static Thread[] threads = new Thread[8];
    /** Counts how many ropes are currently available. */
    private static Semaphore availableRopes = new Semaphore(3, true);
    /** Protects selection and release of the ropes. */
    private static ReentrantLock ropeLock = new ReentrantLock(true);
    /** Protects the order in which the teams finish. */
    private static ReentrantLock resultLock = new ReentrantLock();
    /** Stores the race starting time. */
    private static long startTime;
    /** Stores the first team to finish. */
    private static String winner = null;

    /**
     * Reserves an available rope without busy waiting.
     * @return the rope reserved for this racer
     * @throws InterruptedException if interrupted while waiting for a rope
     */
    public static RopeCrossing getAvailableRope() throws InterruptedException {
        availableRopes.acquire();
        ropeLock.lock();
        try {
            for(int i = 0; i < ropes.length; i++) {
                if(!ropes[i].isOccupied()) {
                    ropes[i].reserve();
                    return ropes[i];
                }
            }
        } finally {
            ropeLock.unlock();
        }
        availableRopes.release();
        throw new IllegalStateException("No available rope after acquiring semaphore");
    }

    /**
     * Releases a rope after a crossing and signals that it is available.
     * @param rope the rope that the racer has finished using
     */
    public static void releaseRope(RopeCrossing rope) {
        ropeLock.lock();
        try {
            rope.release();
        } finally {
            ropeLock.unlock();
        }
        availableRopes.release();
    }

    /**
     * Records a team's completion time and determines the winning team.
     * @param teamName the name of the team that finished
     */
    public static void teamFinished(String teamName) {
        resultLock.lock();
        try {
            float time = (System.currentTimeMillis() - startTime) / 1000.0f;
            System.out.printf("%s all racers finished! @ time = %.3f seconds%n", teamName, time);
            if(winner == null) {
                winner = teamName;
                System.out.println(teamName + " finished first");
            } else {
                System.out.println(teamName + " finished second");
            }
        } finally {
            resultLock.unlock();
        }
    }

    /**
     * Creates the racers, starts their threads and reports the race results.
     * @param args command line arguments
     * @throws InterruptedException if interrupted while joining racer threads
     */
    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < racers.length; i++) {
            int teamIndex = i / 4;
            racers[i] = new Racer("Team " + teamIndex + " Racer " + (i % 4), teams[teamIndex]);
            teams[teamIndex].addRacer(racers[i]);
            threads[i] = new Thread(racers[i]);
        }

        startTime = System.currentTimeMillis();
        for(int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        for(int i = 0; i < threads.length; i++) {
            threads[i].join();
        }

        System.out.println("Rope use statistics:");
        for(int i = 0; i < ropes.length; i++) {
            System.out.println(ropes[i].getName() + " was used " + ropes[i].getCrossingCount() + " times");
        }
        float time = (System.currentTimeMillis() - startTime) / 1000.0f;
        System.out.printf("Total Execution Time = %.3f sec%n", time);
        System.out.println(" Winner is " + winner);
    }
}
