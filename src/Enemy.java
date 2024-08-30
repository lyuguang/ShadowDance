import bagel.Image;

public class Enemy {
    private final Image image = new Image("res/enemy.png");
    private int speed = 1;
    public final static int ENEMY_COLLISION_DIS = 104;
    private int x;
    private int y;


    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        draw();
        x += speed;
        // if the enemy out of the range, change the direction
        if(x >= 900 || x <= 100){
            speedChange();
        }
    }

    public void draw() {
        image.draw(x, y);
    }

    /**
     * Change the direction of the enemy
    **/
    private void speedChange() {
        speed = -speed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


}
