import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class SlowDown extends AllNote {

    private final static int START_Y = 100;


    public SlowDown(int appearanceFrame) {
        super(appearanceFrame, new Image("res/noteSlowDown.png"), START_Y);
    }

    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        // Check if the slowDown is active
        if (isActive()) {
            int score =
                    accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.SLOW_DOWN);

            if (score != Accuracy.NOT_SCORED) {

                AllNote.speedChange -= 1;
                deactivate();
                return score;
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

