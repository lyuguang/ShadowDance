import bagel.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * This solution is based on the sample solution for SWEN20003 Project 1, Semester 2, 2023
 * @author Guangxing Lyu
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final static int LEVEL1 = 1;
    private final static int LEVEL2 = 2;
    private final static int LEVEL3 = 3;
    private static int level;
    public final static String FONT_FILE = "res/FSO8BITR.TTF";
    private final static int TITLE_X = 220;
    private final static int TITLE_Y = 250;
    private final static int INS_X_OFFSET = 100;
    private final static int INS_Y_OFFSET = 190;
    private final static int SCORE_LOCATION = 35;
    private final Font TITLE_FONT = new Font(FONT_FILE, 64);
    private final Font INSTRUCTION_FONT = new Font(FONT_FILE, 24);
    private final Font SCORE_FONT = new Font(FONT_FILE, 30);
    private static final String INSTRUCTIONS = "SELECT LEVELS WITH\nNUMBERS KEYS\n\n    1        2        3";
    private static final int CLEAR_SCORE_LEVEL1 = 150;
    private static final int CLEAR_SCORE_LEVEL2 = 400;
    private static final int CLEAR_SCORE_LEVEL3 = 350;
    private static final String CLEAR_MESSAGE = "CLEAR!";
    private static final String PRESS_MESSAGE = "\n\n\n\n\n\nPRESS SPACE TO RETURN TO LEVEL SELECTION";
    private static final String TRY_AGAIN_MESSAGE = "TRY AGAIN";
    private Accuracy accuracy;  // checks for the accuracy of player's inputs
    private ArrayList<Lane> lanes;  // List of lanes where notes fall down
    private ArrayList<Enemy> enemies;  // List of enemies
    private Guardian guardian;
    private int score;
    private static int currFrame;
    private Track track;
    private boolean started = false;
    private boolean finished;
    private boolean paused;
    private final Random random = new Random();
    private int winScore;


    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }



    private void readCsv(String filename) {

        accuracy = new Accuracy();
        lanes = new ArrayList<>();
        enemies = new ArrayList<>();
        guardian = new Guardian();
        score = 0;
        currFrame = 0;
        finished = false;
        paused = false;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String textRead;
            while ((textRead = br.readLine()) != null) {
                String[] splitText = textRead.split(",");

                if (splitText[0].equals("Lane")) {
                    // reading lanes
                    String laneType = splitText[1];
                    int pos = Integer.parseInt(splitText[2]);
                    Lane lane = new Lane(laneType, pos);
                    lanes.add(lane);
                } else {
                    // reading notes
                    String dir = splitText[0];
                    Lane lane = null;

                    for(Lane l: lanes){  // find the lane with the same type
                        if(l.getType().equals(dir)){
                            lane = l;
                            break;
                        }
                    }

                    if (lane != null) {
                        switch (splitText[1]) {
                            case "Normal":
                                Note note = new Note(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(note);
                                break;
                            case "Hold":
                                HoldNote holdNote = new HoldNote(dir, Integer.parseInt(splitText[2]));
                                lane.addNote(holdNote);
                                break;
                            case "Bomb":
                                Bomb bomb = new Bomb(Integer.parseInt(splitText[2]));
                                lane.addNote(bomb);
                                break;
                            case "DoubleScore":
                                DoubleScore doubleScore = new DoubleScore(Integer.parseInt(splitText[2]));
                                lane.addNote(doubleScore);
                                break;
                            case "SlowDown":
                                SlowDown slowDown = new SlowDown(Integer.parseInt(splitText[2]));
                                lane.addNote(slowDown);
                                break;
                            case "SpeedUp":
                                SpeedUp speedUp = new SpeedUp(Integer.parseInt(splitText[2]));
                                lane.addNote(speedUp);
                                break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        if (!started) {
            // starting screen
            TITLE_FONT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTION_FONT.drawString(INSTRUCTIONS,
                    TITLE_X + INS_X_OFFSET, TITLE_Y + INS_Y_OFFSET);

            // select level
            if (input.wasPressed(Keys.NUM_1)) {
                level = LEVEL1;
                track = new Track("res/track1.wav");
                readCsv("res/level" + level + ".csv");
                started = true;
                track.start();
                winScore = CLEAR_SCORE_LEVEL1;
            }else if (input.wasPressed(Keys.NUM_2)) {
                level = LEVEL2;
                track = new Track("res/track2.wav");
                readCsv("res/level" + level + ".csv");
                started = true;
                track.start();
                winScore = CLEAR_SCORE_LEVEL2;
            }else if (input.wasPressed(Keys.NUM_3)) {
                level = LEVEL3;
                track = new Track("res/track3.wav");
                readCsv("res/level" + level + ".csv");
                started = true;
                track.start();
                winScore = CLEAR_SCORE_LEVEL3;
            }

        } else if (finished) {
            // end screen
            if (score >= winScore) {
                TITLE_FONT.drawString(CLEAR_MESSAGE,
                        WINDOW_WIDTH/2.0 - TITLE_FONT.getWidth(CLEAR_MESSAGE)/2,
                        WINDOW_HEIGHT/2.0);
            } else {
                TITLE_FONT.drawString(TRY_AGAIN_MESSAGE,
                        WINDOW_WIDTH/2.0 - TITLE_FONT.getWidth(TRY_AGAIN_MESSAGE)/2,
                        WINDOW_HEIGHT/2.0);
            }
            INSTRUCTION_FONT.drawString(PRESS_MESSAGE,
                    WINDOW_WIDTH/2.0 - INSTRUCTION_FONT.getWidth(PRESS_MESSAGE)/2,
                    WINDOW_HEIGHT/2.0);
            if(input.wasPressed(Keys.SPACE)){
                AllNote.speedChange = 0;
                started = false;
            }
        } else {
            // gameplay

            SCORE_FONT.drawString("Score " + score, SCORE_LOCATION, SCORE_LOCATION);

            if (paused) {
                if (input.wasPressed(Keys.TAB)) {
                    paused = false;
                    track.run();
                }

                for(Lane lane: lanes) {
                    lane.draw();
                }

                for(Enemy enemy: enemies){
                    enemy.draw();
                }

                guardian.draw();

            } else {
                currFrame++;
                for(Lane lane: lanes) {
                    score += lane.update(input, accuracy);
                }

                for(Enemy enemy: enemies){
                    enemy.update();
                }

                // update guardian and enemies in level 3
                if(level == LEVEL3) {
                    guardian.update(input, enemies);
                    // Every 600 frames, spawn a new enemy at a random position
                    if (currFrame >= 1 && currFrame % 600 == 0) {
                        int x = random.nextInt(800) + 100;  // x = [100, 900]
                        int y = random.nextInt(400) + 100;  // y = [100, 500]
                        Enemy newEnemy = new Enemy(x, y);  // create a new enemy at the random position (x, y)
                        enemies.add(newEnemy);
                    }

                    isCollision();
                }

                Accuracy.setScoreFactor();

                accuracy.update();

                // check if the game is finished
                finished = checkFinished();

                if (input.wasPressed(Keys.TAB)) {
                    paused = true;
                    track.pause();
                }
            }
        }

    }

    public static int getCurrFrame() {
        return currFrame;
    }

    private boolean checkFinished() {
        for(Lane lane: lanes) {
            if (!lane.isFinished()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks for collisions between enemies and notes.
     **/
    private void isCollision(){
        for(Enemy enemy: enemies) {
            enemy.update();

            for(Lane lane: lanes) {
                lane.checkCollision(enemy);
            }
        }
    }

}
