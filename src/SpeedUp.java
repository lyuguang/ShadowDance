import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class SpeedUp extends AllNote {

    private final static int START_Y = 100;


    public SpeedUp(int appearanceFrame) {
        super(appearanceFrame, new Image("res/noteSpeedUp.png"), START_Y);
    }

    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        // Check if the speedUp is active
        if (isActive()) {
            int score =
                    accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.SPEED_UP);

            if (score != Accuracy.NOT_SCORED) {
                AllNote.speedChange += 1;
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

