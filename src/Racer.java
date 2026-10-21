/**
 * Racer represents one racer participating in a team.
 * Each racer sets up equipment, crosses forward, waits for the team,
 * then crosses back using an available rope.
 * Student name: Abdul Muqeet Ahmed. Student number: 000916755
 */
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

public class Racer implements Runnable {

    /** Stores the name of the racer. */
    private final String name;

    /** Stores the team assigned to this racer. */
    private final Team assignedTeam;

    /**
     * Creates a racer with a name and assigned team.
     * @param name the name of the racer
     * @param assignedTeam the racer's team
     */
    public Racer(String name, Team assignedTeam) {
        this.name = name;
        this.assignedTeam = assignedTeam;
    }

    /**
     * Sets up, crosses forward, waits for the team, and crosses back.
     */
    @Override
    public void run() {
        try {
            setup(name);
            crossAvailableRope(1);
            assignedTeam.updateMidPoint();
            crossAvailableRope(2);
            System.out.println(name + " reached finish line");
            assignedTeam.finished();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (BrokenBarrierException e) {
            System.out.println(name + " could not complete the midpoint barrier");
        }
    }

    /**
     * Reserves any available rope, crosses, and returns it for another racer.
     * @param direction 1 for forward, 2 for return
     * @throws InterruptedException if the racer is interrupted while reserving a rope
     */
    public void crossAvailableRope(int direction) throws InterruptedException {
        RopeCrossing rope = RaceSimulation.getAvailableRope();
        try {
            cross(rope.getName(), name, direction);
        } finally {
            RaceSimulation.releaseRope(rope);
        }
    }

    /**
     * Wait an appropriate amount of time for the racer to cross, avg time = 1.5 sec
     * @param ropeName name of the rope crossing
     * @param racerName name of the racer
     * @param direction direction, always == 1 for A1, 1 or 2 for A2
     */
    public void cross(String ropeName, String racerName, int direction) {
        Random random = new Random();
        int low = 1000;
        int high = 2000;
        int waitTime = random.nextInt(high-low) + low;
        String dir = direction == 1 ? "Forward " : "Return ";
        System.out.println("> " + racerName + " started crossing " + dir + ropeName);
        try {
            Thread.sleep(waitTime);
        } catch(Exception e) {}
        System.out.println("# " + racerName + " finished crossing " + dir + ropeName +" in "
                + waitTime + " ms");
    }

    /**
     * Wait an appropriate amount of time for the racer to setup, average time = 1 sec
     * @param rName - racer name
     */
    public void setup(String rName) {
        Random random = new Random();
        int low = 800;
        int high = 1200;
        int waitTime = random.nextInt(high - low) + low;
        try {
            Thread.sleep(waitTime);
        } catch (Exception e) {}
        System.out.println(rName + " ready to cross - " + waitTime + " ms for gear settup");
    }
}
