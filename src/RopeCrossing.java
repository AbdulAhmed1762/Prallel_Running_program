/**
 * The RopeCrossing class represents a rope used by racers to cross the gorge.
 * Each rope can only be occupied by one racer at a time.
 * Student name: Abdul Muqeet Ahmed. Student number: 000916755
 */
public class RopeCrossing implements Runnable {

    /** Stores the name of the rope. */
    private String name;
    /** Stores whether a racer is currently using the rope. */
    private boolean occupied = false;
    /** Stores the total number of times the rope has been crossed. */
    private int crossingCount = 0;

    /**
     * Creates a RopeCrossing with a name.
     * @param name the name of the rope
     */
    public RopeCrossing(String name) {
        this.name = name;
    }

    /**
     * Rope selection is managed by RaceSimulation using a lock and semaphore.
     * A separate rope thread is not required for Assignment 2.
     */
    @Override
    public void run() {
    }

    /**
     * Checks whether this rope is currently occupied.
     * Called while the shared rope lock is held.
     * @return true if a racer is using the rope
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Reserves the rope for a racer.
     * Called while the shared rope lock is held.
     */
    public void reserve() {
        occupied = true;
    }

    /**
     * Releases the rope and records one completed crossing.
     * Called while the shared rope lock is held.
     */
    public void release() {
        crossingCount++;
        occupied = false;
    }

    /**
     * Returns the name of the rope.
     * @return the name of the rope
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the total number of crossings on this rope.
     * @return the number of crossings
     */
    public int getCrossingCount() {
        return crossingCount;
    }
}
