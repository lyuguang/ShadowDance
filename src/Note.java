import bagel.*;

/**
 * Class for normal notes
 */
public class Note extends AllNote {
    private final static int START_Y = 100;


    public Note(String dir, int appearanceFrame) {
        super(appearanceFrame, new Image("res/note" + dir + ".png"), START_Y);
    }

    public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey) {
        if (isActive()) {
            // evaluate accuracy of the key press
            int score = accuracy.evaluateScore(getY(), targetHeight, input.wasPressed(relevantKey));

            if (score != Accuracy.NOT_SCORED) {
                deactivate();
                return score;
            }

        }

        return Accuracy.NOT_SCORED;
    }

}
