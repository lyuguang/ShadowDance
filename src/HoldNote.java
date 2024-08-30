import bagel.*;

/**
 * Class for hold notes
 */
public class HoldNote extends AllNote {
    private static final int HEIGHT_OFFSET = 82;
    private final static int START_Y = 24;
    private boolean holdStarted = false;


    public HoldNote(String dir, int appearanceFrame) {
        super(appearanceFrame, new Image("res/holdNote" + dir + ".png"), START_Y);
    }

    public void startHold() {
        holdStarted = true;
    }

    /**
     * scored twice, once at the start of the hold and once at the end
     */
    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive() && !holdStarted) {
            int score = accuracy.evaluateScore(getBottomHeight(), targetHeight, input.wasPressed(relevantKey));

            if (score == Accuracy.MISS_SCORE) {
                deactivate();
                return score;
            } else if (score != Accuracy.NOT_SCORED) {
                startHold();
                return score;
            }
        } else if (isActive() && holdStarted) {

            int score = accuracy.evaluateScore(getTopHeight(), targetHeight, input.wasReleased(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            } else if (input.wasReleased(relevantKey)) {
                deactivate();
                accuracy.setAccuracy(Accuracy.MISS);
                return Accuracy.MISS_SCORE;
            }
        }

        return Accuracy.NOT_SCORED;
    }

    /**
     * gets the location of the start of the note
     */
    private int getBottomHeight() {
        return getY() + HEIGHT_OFFSET;
    }

    /**
     * gets the location of the end of the note
     */
    private int getTopHeight() {
        return getY() - HEIGHT_OFFSET;
    }
}
