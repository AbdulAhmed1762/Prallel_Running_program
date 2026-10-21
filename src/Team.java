import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Team class represents a team of four racers in the rope crossing race.
 * All four racers must reach the midpoint before crossing back.
 * Student name: Abdul Muqeet Ahmed. Student number: 000916755
 */
public class Team implements Runnable {

    /** Stores the name of the team. */
    private String teamName;
    /** Stores the four racers assigned to the team. */
    private Racer[] racers = new Racer[4];
    /** Keeps track of the next available index in the racers array. */
    private int racerIndex = 0;
    /** Holds the four racers at the midpoint until everyone arrives. */
    private CyclicBarrier midPoint = new CyclicBarrier(4);
    /** Protects the number of racers who have finished the race. */
    private ReentrantLock finishLock = new ReentrantLock();
    /** Counts the racers who have finished both crossings. */
    private int finishedRacers = 0;

    /**
     * Creates a team with a name.
     * @param name the name of the team
     */
    public Team(String name) {
        this.teamName = name;
    }

    /**
     * Adds a racer to the team.
     * @param racer the racer being added
     */
    public void addRacer(Racer racer) {
        racers[racerIndex] = racer;
        racerIndex++;
    }

    /**
     * Waits at the midpoint until all four teammates have crossed forward.
     * @throws InterruptedException if the racer is interrupted
     * @throws BrokenBarrierException if the barrier cannot be completed
     */
    public void updateMidPoint() throws InterruptedException, BrokenBarrierException {
        midPoint.await();
    }

    /**
     * Records a racer finishing the return crossing and reports team completion.
     */
    public void finished() {
        finishLock.lock();
        try {
            finishedRacers++;
            if(finishedRacers == 4) {
                RaceSimulation.teamFinished(teamName);
            }
        } finally {
            finishLock.unlock();
        }
    }

    /**
     * Team coordination is performed by the racer threads at the barrier.
     * A separate team thread is not required for Assignment 2.
     */
    @Override
    public void run() {
    }
}
