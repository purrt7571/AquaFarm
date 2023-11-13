package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;


public class Fish extends Entity {
    private final Fish_movement fishMovement;
    private int offsetX;
    private int offsetY;
    public boolean isDragging;
    private boolean isMoving;
    private boolean isSpeedIncreased;


    private final int originalSpeed;
    private final int increasedSpeed;
    private final Timer speedIncreaseTimer;

    public Fish(int initialX, int initialY, GamePanel gp) {
        super(gp);

        fishMovement = new Fish_movement(1920, 1080);
        fishMovement.setInitialPosition(initialX, initialY);
        this.gp = gp;
        getFishImages(); // Load fish images

        // Initialize dragging variables
        offsetX = 0;
        offsetY = 0;
        isDragging = false;
        isMoving = true;
        isSpeedIncreased = false;
        originalSpeed = 2; // Set the original speed
        increasedSpeed = 15; // Set the increased speed
        speedIncreaseTimer = new Timer();


        // Add a mouse listener to the game panel for dragging
        gp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                // Check if the mouse click is on the fish
                if (mouseX >= fishMovement.getFishX() && mouseX <= fishMovement.getFishX() + fish_left.getWidth() &&
                        mouseY >= fishMovement.getFishY() && mouseY <= fishMovement.getFishY() + fish_left.getHeight()) {
                    offsetX = mouseX - fishMovement.getFishX();
                    offsetY = mouseY - fishMovement.getFishY();
                    isDragging = true;
                    isMoving = false; // Pause fish movement while dragging
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    isDragging = false;
                    isMoving = true; // Resume fish movement when released

                    if (!isSpeedIncreased) {
                        isSpeedIncreased = true;
                        fishMovement.setSpeed(increasedSpeed);

                        // Schedule a timer to revert the speed back to the original value after 3 seconds
                        speedIncreaseTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                fishMovement.setSpeed(originalSpeed);
                                isSpeedIncreased = false;
                            }
                        }, 3000);
                    }
                }
            }
        });

        gp.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isDragging) {
                    int mouseX = e.getX();
                    int mouseY = e.getY();
                    fishMovement.setInitialPosition(mouseX - offsetX, mouseY - offsetY);
                }
            }
        });
    }

    public void move() {
        if (isMoving) {
            fishMovement.move();
        }
    }
    public void seek_food(List<Food.FoodLocation> foodLocations) {
        fishMovement.gotoFood(foodLocations);
    }



    private void getFishImages() {
        fish_left = null;
        fish_right = null;
    }

    public BufferedImage setup(String imageName) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage fishImage = null;

        try {
            fishImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/fish/" + imageName + ".png")));
            fishImage = uTool.scaledImage(fishImage, gp.tilesize, gp.tilesize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fishImage;
    }

    public int getFishX() {
        return fishMovement.getFishX();
    }

    public int getFishY() {
        return fishMovement.getFishY();
    }

    public void draw(Graphics2D g2) {
        BufferedImage fishImage = null;

        String currentDirection = fishMovement.getCurrentDirection();

        if (currentDirection.equals("left")) {
            fishImage = fish_left;
        } else if (currentDirection.equals("right")) {
            fishImage = fish_right;
        }

        g2.drawImage(fishImage, fishMovement.getFishX(), fishMovement.getFishY(), null);
    }

    public void callEatingLogic(Food food) {
        Eating_logic eatingLogic = new Eating_logic();
        eatingLogic.eatFood(food, this);
    }

    public int getFishWidth() {
        return fish_right.getWidth();
    }
    public int getFishHeight() {
        return fish_left.getHeight();
    }



}