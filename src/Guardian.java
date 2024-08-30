import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;

public class Guardian {

    private final Image image = new Image("res/guardian.png");
    private final Image projectileImage = new Image("res/arrow.png");
    private final static int PROJECTILE_SPEED = 6;
    private final static int X_POS = 800;
    private final static int Y_POS = 600;
    private ArrayList<Projectile> projectiles = new ArrayList<>();


    public void update(Input input, ArrayList<Enemy> enemies) {
        draw();

        // Add new projectile if LEFT_SHIFT is pressed and there are enemies
        if (input.wasPressed(Keys.LEFT_SHIFT) && !enemies.isEmpty()) {
            projectiles.add(new Projectile(X_POS, Y_POS, enemies.get(0), projectileImage, PROJECTILE_SPEED));
        }

        // Check for collisions and remove collided projectiles and enemies
        removeCollidedItems(enemies);

        // Update remaining projectiles
        for (Projectile projectile : projectiles) {
            projectile.update();
        }
    }

    /**
     * To checks for collisions between projectiles and enemies.
     * If a collision is detected, the corresponding projectile and enemy
     * are added to removal lists. After checking all projectiles and enemies,
     * the collided ones are removed from their respective lists.
     **/
    private void removeCollidedItems(ArrayList<Enemy> enemies) {
        // Lists to hold projectiles and enemies that need to be removed due to collisions
        ArrayList<Projectile> projectilesToRemove = new ArrayList<>();
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();

        // Iterate over each projectile and check for collisions with each enemy
        for (Projectile projectile : projectiles) {
            for (Enemy enemy : enemies) {
                if (projectile.checkCollision(enemy)) {
                    enemiesToRemove.add(enemy);
                    projectilesToRemove.add(projectile);
                }
            }
        }

        // Remove collided projectiles and enemies
        projectiles.removeAll(projectilesToRemove);
        enemies.removeAll(enemiesToRemove);
    }

    public void draw() {
        for(Projectile projectile: projectiles){
            projectile.draw();
        }
        image.draw(X_POS, Y_POS);
    }

}
