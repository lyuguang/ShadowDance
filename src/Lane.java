import bagel.*;
import java.util.ArrayList;

/**
 * Class for the lanes which notes fall down
 */
public class Lane {

    private static final int HEIGHT = 384;
    private static final int TARGET_HEIGHT = 657;
    private final String type;
    private final Image image;
    private Keys relevantKey;
    private final int posX;
    private final ArrayList<AllNote> allNotes = new ArrayList<>();


    public Lane(String dir, int posX) {
        this.type = dir;
        this.posX = posX;
        image = new Image("res/lane" + dir + ".png");
        switch (dir) {
            case "Left":
                relevantKey = Keys.LEFT;
                break;
            case "Right":
                relevantKey = Keys.RIGHT;
                break;
            case "Up":
                relevantKey = Keys.UP;
                break;
            case "Down":
                relevantKey = Keys.DOWN;
                break;
            case "Special":
                relevantKey = Keys.SPACE;
                break;
        }
    }

    public String getType() {
        return type;
    }

    /**
     * updates all the notes in the lane
     */
    public int update(Input input, Accuracy accuracy) {
        draw();

        for(AllNote note: allNotes){
            note.update();
        }
        if(!allNotes.isEmpty()) {
            int score = allNotes.get(0).checkScore(input, accuracy, TARGET_HEIGHT, relevantKey);

            if (allNotes.get(0).isCompleted()) {
                allNotes.remove(allNotes.get(0));
            }

            if(score == Accuracy.BOMB_SCORE){
                while(!allNotes.isEmpty() && allNotes.get(0).isActive()){
                    allNotes.remove(0);
                }
            }else {
                return score;
            }

        }

        return Accuracy.NOT_SCORED;
    }

    public void addNote(AllNote n) {
        allNotes.add(n);
    }

    public void checkCollision(Enemy enemy) {
        ArrayList<AllNote> notesToRemove = new ArrayList<>();

        for (AllNote noteAndHold : allNotes) {
            double distance = Math.sqrt(Math.pow(noteAndHold.getY() - enemy.getY(), 2) + Math.pow(posX - enemy.getX(), 2));
            if (noteAndHold.isActive() && noteAndHold.isRemovableOnCollision() && distance <= Enemy.ENEMY_COLLISION_DIS) {
                notesToRemove.add(noteAndHold);
            }
        }

        allNotes.removeAll(notesToRemove);
    }

    /**
     * Finished when all the notes have been pressed or missed
     */
    public boolean isFinished() {
        return allNotes.isEmpty();
    }

    /**
     * draws the lane and the notes
     */
    public void draw() {
        image.draw(posX, HEIGHT);

        for(AllNote note: allNotes){
            note.draw(posX);
        }
    }

}
