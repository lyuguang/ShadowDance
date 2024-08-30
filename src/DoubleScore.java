import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class DoubleScore extends AllNote {
    private final static int START_Y = 100;


    public DoubleScore(int appearanceFrame) {
        super(appearanceFrame, new Image("res/note2x.png"), START_Y);
    }

    @Override
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        // Check if the doubleScore is active
        if (isActive())  {
            int score =
                    accuracy.evaluateSpecialScore(getY(), targetHeight, input.wasPressed(relevantKey), Accuracy.DOUBLE_SCORE);

            if (score != Accuracy.NOT_SCORED) {

                Accuracy.doubleScoreFrames = 480;

                Accuracy.doubleScoreFactor += 1;
                deactivate();
                return Accuracy.doubleScoreFactor;
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
