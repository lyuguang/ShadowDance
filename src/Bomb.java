import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class Bomb extends AllNote {

    private final static int START_Y = 100;


    public Bomb(int appearanceFrame) {
        super(appearanceFrame, new Image("res/noteBomb.png"), START_Y);
    }

    /**
     * Override the checkScore method from the parent class
     **/
    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        // Check if the bomb is active
        if (isActive()) {
            int score =
                    accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.LANE_CLEAR);

            // If the score is not "not scored", deactivate the bomb and return the bomb's score
            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return Accuracy.BOMB_SCORE;
            }
        }
        return Accuracy.NOT_SCORED;
    }

    /**
     * The enemy cannot be removed the special note
     **/
    @Override
    public boolean isRemovableOnCollision() {
        return false;
    }
}

