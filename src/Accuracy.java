import bagel.*;

/**
 * Class for dealing with accuracy of pressing the notes
 */
public class Accuracy {

    public static final int PERFECT_SCORE = 10;
    public static final int GOOD_SCORE = 5;
    public static final int BAD_SCORE = -1;
    public static final int MISS_SCORE = -5;
    public static final int NOT_SCORED = 0;
    public static final int SPECIAL_SCORE = 15;
    public static final int BOMB_SCORE = -9;  // Bomb score is a special case to check if the bomb is triggered
    public static final String PERFECT = "PERFECT";
    public static final String GOOD = "GOOD";
    public static final String BAD = "BAD";
    public static final String MISS = "MISS";
    public static final String SPEED_UP = "Speed Up";
    public static final String LANE_CLEAR = "Lane Clear";
    public static final String SLOW_DOWN = "Slow Down";
    public static final String DOUBLE_SCORE = "Double Score";
    private static final int PERFECT_RADIUS = 15;
    private static final int GOOD_RADIUS = 50;
    private static final int BAD_RADIUS = 100;
    private static final int MISS_RADIUS = 200;
    private static final Font ACCURACY_FONT = new Font(ShadowDance.FONT_FILE, 40);
    private static final int RENDER_FRAMES = 30;
    private String currAccuracy = null;
    private int frameCount = 0;
    public static int doubleScoreFactor = 1;
    public static int doubleScoreFrames = 0;

    public void setAccuracy(String accuracy) {
        currAccuracy = accuracy;
        frameCount = 0;
    }

    public static void setScoreFactor(){
        if (doubleScoreFrames > 0) {
            doubleScoreFrames--;

            if (doubleScoreFrames == 0) {
                Accuracy.doubleScoreFactor = 1;
            }
        }
    }

    public int evaluateScore(int height, int targetHeight, boolean triggered) {
        int distance = Math.abs(height - targetHeight);

        if (triggered) {
            if (distance <= PERFECT_RADIUS) {
                setAccuracy(PERFECT);
                return PERFECT_SCORE*doubleScoreFactor;
            } else if (distance <= GOOD_RADIUS) {
                setAccuracy(GOOD);
                return GOOD_SCORE*doubleScoreFactor;
            } else if (distance <= BAD_RADIUS) {
                setAccuracy(BAD);
                return BAD_SCORE*doubleScoreFactor;
            } else if (distance <= MISS_RADIUS) {
                setAccuracy(MISS);
                return MISS_SCORE*doubleScoreFactor;
            }

        } else if (height >= (Window.getHeight())) {
            setAccuracy(MISS);
            return MISS_SCORE;
        }

        return NOT_SCORED;
    }

    public void update() {
        frameCount++;
        if (currAccuracy != null && frameCount < RENDER_FRAMES) {
            ACCURACY_FONT.drawString(currAccuracy,
                    Window.getWidth()/2.0 - ACCURACY_FONT.getWidth(currAccuracy)/2,
                    Window.getHeight()/2.0);
        }
    }

    public int evaluateSpecialScore(int height, int targetHeight, boolean triggered, String message) {
        int distance = Math.abs(height - targetHeight);

        if (triggered && distance <= GOOD_RADIUS) {
            setAccuracy(message);
            return SPECIAL_SCORE;
        }

        return NOT_SCORED;
    }

}
