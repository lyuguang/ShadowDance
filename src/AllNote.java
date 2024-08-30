import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;

abstract public class AllNote {

    private final Image image;
    private final int appearanceFrame;
    private final int speed = 2;
    private int y;
    private boolean active = false;
    private boolean completed = false;
    public static int speedChange = 0;


    public AllNote(int appearanceFrame, Image image, int y) {
        this.image = image;
        this.appearanceFrame = appearanceFrame;
        this.y = y;
    }

    public boolean isActive() {
        return active;
    }
    public boolean isCompleted() {
        return completed;
    }

    public void deactivate() {
        active = false;
        completed = true;
    }

    public void update() {
        if (active) {
            y += speed + speedChange;
        }

        if (ShadowDance.getCurrFrame() >= appearanceFrame && !completed) {
            active = true;
        }
        if (getY() > Window.getHeight()) {
            deactivate();
        }
    }

    public void draw(int x) {
        if (active) {
            image.draw(x, y);
        }
    }

    abstract public int checkScore(Input input, Accuracy accuracy, int targetHeight, Keys relevantKey);

    public boolean isRemovableOnCollision() {
        return true;
    }

    public int getY() {
        return y;
    }
}
