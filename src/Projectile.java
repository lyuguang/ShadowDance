import bagel.DrawOptions;
import bagel.Image;

public class Projectile {

    private final static int PROJECTILE_DIS = 62;
    public int projectileSpeed;
    private int startX;
    private int startY;
    private double xDis;
    private double yDis;
    private final Image image;
    private double rotation;
    private boolean active;
    private final DrawOptions drawOptions = new DrawOptions();


    public Projectile(int x, int y, Enemy enemy,Image image, int projectileSpeed){

        this.startX = x;
        this.startY = y;
        this.projectileSpeed = projectileSpeed;
        this.image = image;
        this.active = true;

        // calculate the distance between the enemy and the projectile
        xDis = getX() - enemy.getX();
        yDis = getY() - enemy.getY();

        // normalize the distance
        double length = Math.sqrt(xDis * xDis + yDis * yDis);
        xDis /= length;
        yDis /= length;
    }

    public boolean isActive() {
        return active;
    }

    public boolean checkCollision(Enemy enemy) {
        // check if the projectile is active and if it is close enough to the enemy
        if (active && Math.abs(getX() - enemy.getX()) <= PROJECTILE_DIS && Math.abs(getY() - enemy.getY()) <= PROJECTILE_DIS) {
            active = false;
            return true;
        }
        return false;
    }

    public void update() {
        draw();
        move();
    }

    public void draw() {
        if(isActive()){
            // calculate the rotation angle based on the movement direction
            rotation = Math.atan2(yDis, xDis);
            drawOptions.setRotation(rotation+Math.PI);
            image.draw(startX, startY, drawOptions);
        }
    }

    public void move() {
        startX -= (double) (xDis * projectileSpeed);
        startY -= (double) (yDis * projectileSpeed);
    }
    public int getX() {
        return startX;
    }

    public int getY() {
        return startY;
    }
}

