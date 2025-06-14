package zipped.code;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class SwingSubsystem { // Main class
    // Variables
    Ships ships = new Ships();
    JFrame frame;
    JPanel panel;
    Image image;
    Image grug;
    Image backgroundImage;
    int mouseX;
    int mouseY;
    int gridX;
    int gridY;
    int keyIndex = 1;
    int pressedKey;
    int overAmount;
    int player1Score;
    int player2Score;
    int maxScore = 3;
    int tempScore1;
    int tempScore2;
    boolean keyPressed;
    boolean keyReady;
    boolean musicReady = true;
    boolean mousePressed;
    boolean mouseReady = true;
    boolean cellPressed;
    boolean switchBoat;
    boolean isFocused = false;
    boolean hasPlayer1Shot = false;
    boolean hasPlayer2Shot = false;
    boolean isMuted = false;
    String shipMode = "carrier";
    String shipDirection = "up";
    String textInput = "";
    Ships.Destroyer destroyer;
    Ships.Submarine submarine;
    Ships.Cruiser cruiser;
    Ships.Battleship battleship;
    Ships.Carrier carrier;
    Ships.Destroyer destroyer2;
    Ships.Submarine submarine2;
    Ships.Cruiser cruiser2;
    Ships.Battleship battleship2;
    Ships.Carrier carrier2;
    Clip menuMusicClip;
    Clip gameMusicClip;
    PlayerData playerData1;
    PlayerData playerData2;
    List<Boolean> fieldFocused;

    Player player1 = new Player();
    Player player2 = new Player();

    GameController gameController; // Object declaration for the game controller

    public SwingSubsystem() { // Main class contructor
        fieldFocused = new ArrayList<>(); // Array list that hold the boolean values for which custom text field is
                                          // focused
        destroyer = ships.new Destroyer(); // Object init for ships
        submarine = ships.new Submarine(); // Object init for ships
        cruiser = ships.new Cruiser(); // Object init for ships
        battleship = ships.new Battleship(); // Object init for ships
        carrier = ships.new Carrier(); // Object init for ships
        destroyer2 = ships.new Destroyer(); // Object init for ships
        submarine2 = ships.new Submarine(); // Object init for ships
        cruiser2 = ships.new Cruiser(); // Object init for ships
        battleship2 = ships.new Battleship(); // Object init for ships
        carrier2 = ships.new Carrier(); // Object init for ships
        playerData1 = new PlayerData(destroyer, submarine, cruiser, battleship, carrier); // feed ships into player 1
                                                                                          // data
        playerData2 = new PlayerData(destroyer2, submarine2, cruiser2, battleship2, carrier2); // feed ships into player
                                                                                               // 2 data
        gameController = new GameController(); // Init the gameController

        try { // Try catch surronding the ship image that is the frame icon
            grug = ImageIO.read(new File("ship.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE FOUND!"); // Debugging
        }

        try { // Try catch surronding the image for the background image
            backgroundImage = ImageIO.read(new File("waterbackground.jpg"));
        } catch (Exception e) {
            System.out.println("NO IMAGE FOUND!"); // Debugging
        }
        frame = new JFrame("Battleship"); // Create the frame with the name of Battleship
        frame.setSize(1000, 1000); // Make a starting frame size
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Extend the frame into fullscreen
        panel = new JPanel() { // Create the drawing panel
            @Override
            public void paintComponent(Graphics g) { // Override the paintCompnent function inside panel
                super.paintComponent(g); // Force paint
                draw(g); // Run the main drawing function
            }
        };

        panel.addMouseMotionListener(new MouseAdapter() { // Add a motion listener to the panel
            @Override
            public void mouseMoved(MouseEvent e) { // Detect mouse movements
                mouseX = e.getX(); // save the x position of the mouse relative to the frame
                mouseY = e.getY(); // save the y position of the mouse relative to the frame
                panel.repaint(); // Refresh
            }

            @Override
            public void mouseDragged(MouseEvent e) { // Detech mouse movements when the mouse is held
                mouseX = e.getX(); // save the x position of the mouse relative to the frame
                mouseY = e.getY(); // save the y position of the mouse relative to the frame
                panel.repaint(); // Refresh
            }
        });

        panel.addMouseListener(new MouseAdapter() { // Add a mouse listener (different from motion)
            @Override
            public void mousePressed(MouseEvent e) { // Detect when the mouse is pressed
                mousePressed = true; // Mouse is pressed to a variable
                mouseX = e.getX(); // save the x position of the mouse relative to the frame
                mouseY = e.getY(); // save the y position of the mouse relative to the frame
                panel.repaint(); // Refresh panel
            }

            @Override
            public void mouseReleased(MouseEvent e) { // Detech when the mouse is pressed and then released
                mouseReady = true; // Ready to click again (prevent multiple clicks when 1 is intended)
                mousePressed = false; // Mouse is released
                cellPressed = false; // reset cell pressed variable
                panel.repaint(); // Refresh
            }
        });

        panel.addKeyListener(new KeyAdapter() { // Add a key listener to the panel
            @Override
            public void keyPressed(KeyEvent e) { // Detect when a key is pressed
                switch (e.getKeyCode()) { // case switch detecting which key is pressed
                    // following code is for the custom text field
                    case KeyEvent.VK_BACK_SPACE:
                        textInput = textInput.substring(0, textInput.length() - 1); // Delete 1 character
                        break;
                    case (KeyEvent.VK_SHIFT):
                        textInput = textInput.substring(0, textInput.length()); // Dont write the weird unknown
                                                                                // character
                        break;
                    case (KeyEvent.VK_ALT):
                        textInput = textInput.substring(0, textInput.length()); // Dont write the weird unknown
                                                                                // character
                        break;
                    case (KeyEvent.VK_CONTROL):
                        textInput = textInput.substring(0, textInput.length()); // Dont write the weird unknown
                                                                                // character
                        break;
                    case (KeyEvent.VK_ESCAPE):
                        textInput = textInput.substring(0, textInput.length()); // Dont write the weird unknown
                                                                                // character
                        break;
                    default:
                        textInput += String.valueOf(e.getKeyChar()); // If any other keys are pressed save them to the
                                                                     // variable that the text field
                        break;
                }
                if (e.getKeyCode() == KeyEvent.VK_R && keyReady) { // If the R key is pressed
                    keyReady = false;
                    keyIndex++; // Rotate the ship
                    panel.repaint(); // Refresh
                }
            }

            public void keyReleased(KeyEvent k) { // If the key is released
                keyReady = true;
                panel.repaint(); // Refresh
            }
        });

        panel.setFocusable(true); // make the window focusable
        panel.requestFocusInWindow(); // Focus the window

        frame.setIconImage(grug); // Set the icon of the image
        frame.add(panel); // Add the panel to the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Make sure the frame closes properly
        frame.setResizable(false); // Make sure no one can resize the frame
        frame.setLocationRelativeTo(null); // Center the frame on your screen
        frame.setVisible(true); // Show the frame
    }

    public void draw(Graphics g) { // main drawing function that runs constantly
        g.drawImage(backgroundImage, 0, 0, frame.getWidth() + 5, frame.getHeight() + 5, null); // Background image
        muteMusicButton(g);
        if (player1Score == maxScore) { // If player 1 won
            drawCenteredText(g, "PLAYER 1 WON", frame.getWidth() / 2, frame.getHeight()/2, 50, Color.black, "Arial");
        } else if (player2Score == maxScore) { // If player 2 won
            drawCenteredText(g, "PLAYER 2 WON", frame.getWidth() / 2, frame.getHeight()/2, 50, Color.black, "Arial");
        } else { // If noone won
            gameController.switchScreen(g); // Run the game
        }
        if (!isMuted)
            gameMusic(gameController.gameScreen); // Run the music
        panel.repaint(); // Refresh
    }

    public class GameController { // Main game controller that brings together the player data and the functions

        // Variable declartions and init
        public String gameScreen = "startMenu";
        public String player1Pass;
        public String player2Pass;
        public String errorMessage = "";
        public final String startMenu = "startMenu";
        public final String modeMenu = "modeMenu";
        public final String enterPlayer1Details = "enterPlayer1Details";
        public final String enterPlayer2Details = "enterPlayer2Details";
        public final String checkPlayer1Details = "checkPlayer1Details";
        public final String checkPlayer2Details = "checkPlayer2Details";
        public final String checkPrePlayer1Details = "checkPrePlayer1Details";
        public final String checkPrePlayer2Details = "checkPrePlayer2Details";
        public final String player1ShipSelector = "player1ShipSelector";
        public final String player2ShipSelector = "player2ShipSelector";
        public final String player1Grid = "player1Grid";
        public final String player2Grid = "player2Grid";
        public final String player1SecondGrid = "player1SecondGrid";
        public final String player2SecondGrid = "player2SecondGrid";
        public int delayTime = 1000; // Delay time after a shot

        public void switchScreen(Graphics g) { // Main game function
            // Things labeled player1 and player2 are identical almost so comments have been
            // left out for player 2
            switch (gameScreen) {
                case startMenu: // runs the start menu code
                    startMenu(g);
                    break;
                case modeMenu: // Runs the mode select menu code
                    modeSelectMenu(g);
                    break;
                case enterPlayer1Details: // Gets the player 1 detail
                    // Create a button to go back to the previous page
                    if (roundedRectButton(g, 25, 25, 70, 35, "Back", Color.BLACK, Color.WHITE, 18, 13)) {
                        mouseReady = false;
                        gameController.gameScreen = gameController.startMenu;

                        try { // Error trap the music stopping
                            gameMusicClip.stop();
                        } catch (Exception e) {
                            System.out.println(e + ". (modeSelectMenu method)");
                        }
                        musicReady = true;
                    }
                    player1Pass = getPlayerDetails(g);
                    break;
                case enterPlayer2Details: // Gets the player 2 detail
                    player2Pass = getPlayer2Details(g);
                    break;
                case player1ShipSelector: // Runs the ship selector screen code for player 1
                    // Draws the grid with the ship selector
                    drawCenteredText(g, "Player 1's Ship Selector", frame.getWidth() / 2,
                            50, 30, Color.BLACK, "Arial");
                    playerData1.drawGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10,
                            10, Color.black,
                            Color.black,
                            true);
                    if (roundedRectButton(g, frame.getWidth() - 250, 50, 200, 100, "Ready", Color.black,
                            Color.white, 30, 20) && playerData1.destroyer.doRender && playerData1.submarine.doRender
                            && playerData1.cruiser.doRender && playerData1.battleship.doRender
                            && playerData1.carrier.doRender) {
                        gameController.gameScreen = gameController.checkPrePlayer2Details;
                    }
                    playerData1.shipSelector(g, frame.getWidth() / 2 + 700 / 2 + 250 / 2 + 30,
                            frame.getHeight() / 2, 250,
                            500);

                    break;
                case player2ShipSelector: // Runs the ship selector screen code for player 2
                    drawCenteredText(g, "Player 2's Ship Selector", frame.getWidth() / 2,
                            50, 30, Color.BLACK, "Arial");
                    playerData2.drawGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10,
                            10, Color.black,
                            Color.black,
                            true);

                    if (roundedRectButton(g, frame.getWidth() - 250, 50, 200, 100, "Ready", Color.black,
                            Color.white, 30, 20) && playerData2.destroyer.doRender && playerData2.submarine.doRender
                            && playerData2.cruiser.doRender && playerData2.battleship.doRender
                            && playerData2.carrier.doRender) {
                        gameScreen = checkPlayer1Details;
                    }
                    playerData2.shipSelector(g, frame.getWidth() / 2 + 700 / 2 + 250 / 2 + 30,
                            frame.getHeight() / 2, 250,
                            500);
                    break;

                case checkPlayer1Details: // Check player 1 password
                    String tempPass3 = checkPlayer1Details(g); // Create a temporary variable to store the attempted
                                                               // password
                    if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 200, 200, 125,
                            "Ok",
                            Color.BLACK, Color.WHITE, 40, 13)) {
                        if (tempPass3.equals(gameController.player1Pass)) { // Make sure its the right pass
                            gameController.gameScreen = gameController.player1Grid;
                            errorMessage = "";
                        } else {
                            errorMessage = "Wrong Password! Please try again";
                        }
                    }
                    drawCenteredText(g, errorMessage, frame.getWidth() / 2, frame.getHeight() - 100, 30,
                            Color.black, "Arial");
                    break;
                case checkPlayer2Details: // Check player 2 password
                    String tempPass4 = checkPlayer2Details(g);
                    if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 200, 200, 125,
                            "Ok",
                            Color.BLACK, Color.WHITE, 40, 13)) {
                        if (tempPass4.equals(gameController.player2Pass)) {
                            gameController.gameScreen = gameController.player2Grid;
                            errorMessage = "";
                        } else {
                            errorMessage = "Wrong Password! Please try again";
                        }
                    }
                    drawCenteredText(g, errorMessage, frame.getWidth() / 2, frame.getHeight() - 100, 30,
                            Color.black, "Arial");
                    break;
                case checkPrePlayer1Details: // Check player 1 and then go to ship selector
                    String tempPass = checkPlayer1Details(g);
                    if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 200, 200, 125,
                            "Ok",
                            Color.BLACK, Color.WHITE, 40, 13)) {
                        if (tempPass.equals(gameController.player1Pass)) {
                            gameController.gameScreen = gameController.player1ShipSelector;
                            errorMessage = "";
                        } else {
                            errorMessage = "Wrong Password! Please try again";
                        }
                    }
                    drawCenteredText(g, errorMessage, frame.getWidth() / 2, frame.getHeight() - 100, 30,
                            Color.black, "Arial");
                    break;
                case checkPrePlayer2Details: // Check player 2 and then go to ship selector
                    String tempPass2 = checkPlayer2Details(g);
                    if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 200, 200, 125,
                            "Ok",
                            Color.BLACK, Color.WHITE, 40, 13)) {
                        if (tempPass2.equals(gameController.player2Pass)) {
                            gameController.gameScreen = gameController.player2ShipSelector;
                            errorMessage = "";
                        } else {
                            errorMessage = "Wrong Password! Please try again";
                        }
                    }
                    drawCenteredText(g, errorMessage, frame.getWidth() / 2, frame.getHeight() - 100, 30,
                            Color.black, "Arial");
                    break;
                case player1Grid: // Runs the player 1 grid

                    drawCenteredText(g, "Player 1's Grid", frame.getWidth() / 2,
                            50, 30, Color.BLACK, "Arial");
                    playerData1.setEnemyShips(playerData2.destroyer, playerData2.submarine, playerData2.cruiser, // send
                                                                                                                 // data
                                                                                                                 // about
                                                                                                                 // the
                                                                                                                 // enemy
                                                                                                                 // ships
                                                                                                                 // to
                                                                                                                 // the
                                                                                                                 // player
                                                                                                                 // data
                                                                                                                 // class
                            playerData2.battleship, playerData2.carrier);
                    playerData2.setEnemyShips(playerData1.destroyer, playerData1.submarine, playerData1.cruiser, // Send
                                                                                                                 // data
                                                                                                                 // about
                                                                                                                 // own
                                                                                                                 // ships
                                                                                                                 // to
                                                                                                                 // the
                                                                                                                 // enemy
                            playerData1.battleship, playerData1.carrier);
                    playerData1.drawGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10,
                            10, Color.black,
                            Color.black,
                            true, playerData2.getAttemptedX(), playerData2.getAttemptedY());
                    if (roundedRectButton(g, frame.getWidth() - 250, 50, 200, 100, "Enemy Grid", Color.black,
                            Color.white, 30, 20)) {
                        gameController.gameScreen = gameController.player1SecondGrid;
                    }
                    scoreboard(g, frame.getWidth() / 2 - 700 / 2 - 180 / 2 - 50,
                            frame.getHeight() / 2 - 700 / 2 + 100 / 2,

                            180, 100, player1Score, 1);
                    break;
                case player1SecondGrid: // Runs the enemy code for player 1
                    playerData2.hasShot = false;
                    if (roundedRectButton(g, frame.getWidth() - 250, frame.getHeight() - 50 - 100, 200, 100, "Back",
                            Color.black, Color.white, 30, 20)) {
                        gameScreen = player1Grid;
                    }

                    if (playerData1.drawEnemyGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10,
                            20, Color.black,
                            Color.black,
                            true, playerData2.getAttemptedX(), playerData2.getAttemptedY(), 1)) {

                        Timer timer = new Timer(delayTime, e -> { // Create a timer from java swing to let the player
                                                                  // see if they hit the boat or not
                            hasPlayer2Shot = false;
                            hasPlayer1Shot = true;
                            gameScreen = checkPlayer2Details;

                            ((Timer) e.getSource()).stop(); // Stop after first run
                        });
                        timer.start();

                    }

                    scoreboard(g, frame.getWidth() / 2 - 700 / 2 - 180 / 2 - 50,
                            frame.getHeight() / 2 - 700 / 2 + 100 / 2,

                            180, 100, player1Score, 1);

                    break;
                case player2Grid: // Runs the player 2 grid
                    drawCenteredText(g, "Player 2's Grid", frame.getWidth() / 2,
                            50, 30, Color.BLACK, "Arial");
                    playerData1.setEnemyShips(playerData2.destroyer, playerData2.submarine, playerData2.cruiser,
                            playerData2.battleship, playerData2.carrier);
                    playerData2.setEnemyShips(playerData1.destroyer, playerData1.submarine, playerData1.cruiser,
                            playerData1.battleship, playerData1.carrier);
                    playerData2.drawGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10,
                            10, Color.black,
                            Color.black,
                            true, playerData1.getAttemptedX(), playerData1.getAttemptedY());
                    if (roundedRectButton(g, frame.getWidth() - 250, 50, 200, 100, "Enemy Grid", Color.black,
                            Color.white, 30, 20)) {
                        gameController.gameScreen = gameController.player2SecondGrid;
                    }
                    scoreboard(g, frame.getWidth() / 2 - 700 / 2 - 180 / 2 - 50,
                            frame.getHeight() / 2 - 700 / 2 + 100 / 2,

                            180, 100, player2Score, 1);
                    break;
                case player2SecondGrid: // Runs the enemy code for player 2
                    playerData1.hasShot = false;
                    if (roundedRectButton(g, frame.getWidth() - 250, frame.getHeight() - 50 - 100, 200, 100, "Back",
                            Color.black, Color.white, 30, 20)) {
                        gameScreen = player2Grid;
                    }

                    if (playerData2.drawEnemyGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10,
                            20, Color.black,
                            Color.black,
                            true, playerData1.getAttemptedX(), playerData1.getAttemptedY(), 2)) {

                        Timer timer = new Timer(delayTime, e -> {
                            hasPlayer2Shot = true;
                            hasPlayer1Shot = false;
                            gameScreen = checkPlayer1Details;

                            ((Timer) e.getSource()).stop(); // Stop after first run
                        });
                        timer.start();
                    }

                    scoreboard(g, frame.getWidth() / 2 - 700 / 2 - 180 / 2 - 50,
                            frame.getHeight() / 2 - 700 / 2 + 100 / 2,

                            180, 100, player2Score, 2);

                    break;
            }
        }

    }

    public class PlayerData { // The main class that holds infomation about the player, like ship data,
                              // attempted shots, score, and so on

        List<Integer> attemptedX = new ArrayList<>(); // Create a array list of integers
        List<Integer> attemptedY = new ArrayList<>();

        List<Point> drawnHitPoints = new ArrayList<>(); // Create an array list of points

        Set<Point> shipCells = new HashSet<>(); // Create a hashset for the shipcells for quick search up by hashing
        Set<Point> enemyShipCells = new HashSet<>();

        Ships.Destroyer destroyer; // declare self ships
        Ships.Submarine submarine;
        Ships.Cruiser cruiser;
        Ships.Battleship battleship;
        Ships.Carrier carrier;

        Ships.Destroyer destroyer2; // declare enemy ships
        Ships.Submarine submarine2;
        Ships.Cruiser cruiser2;
        Ships.Battleship battleship2;
        Ships.Carrier carrier2;

        boolean builtSelfShips = false; // Booleans to see if we init.. ed the ships yet to prevent lag
        boolean builtEnemyShips = false;
        boolean hasShot = false;

        Color crossColor;

        public PlayerData(Ships.Destroyer destroyer, Ships.Submarine submarine, Ships.Cruiser cruiser,
                Ships.Battleship battleship, Ships.Carrier carrier) { // Get self ships

            this.destroyer = destroyer;
            this.submarine = submarine;
            this.cruiser = cruiser;
            this.battleship = battleship;
            this.carrier = carrier; // connect to the ships declared above

            while (attemptedX.size() < 100) { // Init the array list with maximum 100 to allow the use of "set" to
                                              // prevent the list from growing past 100
                attemptedX.add(-10);
            }
            while (attemptedY.size() < 100) {
                attemptedY.add(-10);
            }

        }

        // region Draw Grid

        /**
         * A grid drawing function. The size of each cell will scale depending on the
         * width and height of the grid.
         * The grid comes with a built in "hovering" function that will highlight the
         * cell the mouse is hovering over.
         * Use {@link #getGridX() } and {@link #getGridY()} to get the selected cell
         *
         * @param g             is the graphics of the panel
         * @param x             The x position of the grid, it is the top left corner
         *                      unless centerAroundPoint is true, in which case its the
         *                      center
         * @param y             The y position of the grid, it is the top left corner
         *                      unless centerAroundPoint is true, in which case its the
         *                      center
         * @param width         The width of the grid
         * @param height        The height of the grid
         * @param xCell         The amount of cells in the x axis
         * @param yCell         The amount of cells in the y axis
         * @param lineThickness The thickness of each line between the cells
         * @param lineColor     The color of the line, uses the Color library
         * @param cellColor     The color of the cell, uses the Color library
         * @param doHoverEffect Boolean determining if the grid does the "hovering"
         *                      effect
         * @param enemyShotsX   The shots the enemy shot against this grid
         * @param enemyShotsY   The shots the enemy shot against this grid
         *
         */
        public void drawGrid(Graphics g, int x, int y, int width, int height, int xCell, int yCell,
                int lineThickness,
                Color lineColor, Color cellColor, boolean doHoverEffect, List<Integer> enemyShotsX,
                List<Integer> enemyShotsY) {

            int cellWidth = width / xCell;
            int cellHeight = height / yCell;
            int startX = x - width / 2 - xCell;
            int startY = y - height / 2 - yCell;

            Set<Point> enemyShots = new HashSet<>(); // Create points at each enemy shot for easy handling
            for (int i = 0; i < enemyShotsX.size(); i++) {
                enemyShots.add(new Point(enemyShotsX.get(i), enemyShotsY.get(i)));
            }

            if (!builtSelfShips) { // Create points that each position the ships are at
                for (int i = 0; i < destroyer.shipLength; i++)
                    shipCells.add(new Point(destroyer.xPositions[i], destroyer.yPositions[i]));
                for (int i = 0; i < submarine.shipLength; i++)
                    shipCells.add(new Point(submarine.xPositions[i], submarine.yPositions[i]));
                for (int i = 0; i < cruiser.shipLength; i++)
                    shipCells.add(new Point(cruiser.xPositions[i], cruiser.yPositions[i]));
                for (int i = 0; i < battleship.shipLength; i++)
                    shipCells.add(new Point(battleship.xPositions[i], battleship.yPositions[i]));
                for (int i = 0; i < carrier.shipLength; i++)
                    shipCells.add(new Point(carrier.xPositions[i], carrier.yPositions[i]));
            }

            for (int xIndex = 0; xIndex < xCell; xIndex++) { // main x index for loop to go through the x axis grid
                                                             // columns
                for (int yIndex = 0; yIndex < yCell; yIndex++) { // main y index for loop to go through the y axis grid
                                                                 // rows
                    g.setColor(cellColor);
                    ((Graphics2D) g).setStroke(new BasicStroke(5)); // Cast graphics 2d so we can set the stroke
                    g.drawRect((x - (width / 2) - xCell + (xIndex * (width / xCell))),
                            (y - (height / 2) - yCell + (yIndex * (height / yCell))), cellWidth,
                            cellHeight);

                    if (destroyer.doRender) { // If the ship should be rendered yet
                        for (int destroyerIndex = 0; destroyerIndex < destroyer.shipLength; destroyerIndex++) { // for
                                                                                                                // every
                                                                                                                // "block"
                                                                                                                // the
                                                                                                                // ship
                                                                                                                // takes
                                                                                                                // up
                            g.setColor(destroyer.shipColor); // set the color from the ships class
                            // draw the ship
                            g.fillRect(
                                    (x - (width / 2) - xCell
                                            + (destroyer.xPositions[destroyerIndex] * (width / xCell))),
                                    (y - (height / 2) - yCell
                                            + (destroyer.yPositions[destroyerIndex] * (height / yCell))),
                                    width / xCell,
                                    height / yCell);

                        }
                    }
                    if (submarine.doRender) {
                        for (int submarineIndex = 0; submarineIndex < submarine.shipLength; submarineIndex++) {
                            g.setColor(submarine.shipColor);
                            g.fillRect(
                                    (x - (width / 2) - xCell
                                            + (submarine.xPositions[submarineIndex] * (width / xCell))),
                                    (y - (height / 2) - yCell
                                            + (submarine.yPositions[submarineIndex] * (height / yCell))),
                                    width / xCell,
                                    height / yCell);
                        }
                    }
                    if (cruiser.doRender) {
                        for (int cruiserIndex = 0; cruiserIndex < cruiser.shipLength; cruiserIndex++) {
                            g.setColor(cruiser.shipColor);
                            g.fillRect(
                                    (x - (width / 2) - xCell
                                            + (cruiser.xPositions[cruiserIndex] * (width / xCell))),
                                    (y - (height / 2) - yCell
                                            + (cruiser.yPositions[cruiserIndex] * (height / yCell))),
                                    width / xCell,
                                    height / yCell);
                        }
                    }
                    if (battleship.doRender) {
                        for (int battleshipIndex = 0; battleshipIndex < battleship.shipLength; battleshipIndex++) {
                            g.setColor(battleship.shipColor);
                            g.fillRect(
                                    (x - (width / 2) - xCell
                                            + (battleship.xPositions[battleshipIndex] * (width / xCell))),
                                    (y - (height / 2) - yCell
                                            + (battleship.yPositions[battleshipIndex] * (height / yCell))),
                                    width / xCell,
                                    height / yCell);
                        }
                    }
                    if (carrier.doRender) {
                        for (int carrierIndex = 0; carrierIndex < carrier.shipLength; carrierIndex++) {
                            g.setColor(carrier.shipColor);
                            g.fillRect(
                                    (x - (width / 2) - xCell
                                            + (carrier.xPositions[carrierIndex] * (width / xCell))),
                                    (y - (height / 2) - yCell
                                            + (carrier.yPositions[carrierIndex] * (height / yCell))),
                                    width / xCell,
                                    height / yCell);

                        }
                    }

                    // Scan the grid and find which grid cell the mouse is hovering over
                    if (mouseX > (x - (width / 2) - xCell + (xIndex * (width / xCell)))
                            && mouseX < (x - (width / 2) - xCell + (xIndex * (width / xCell))) + width / xCell) {
                        if (mouseY > (y - (height / 2) - yCell + (yIndex * (height / yCell)))
                                && mouseY < (y - (height / 2) - yCell + (yIndex * (height / yCell)))
                                        + height / yCell) {
                            gridX = xIndex;
                            gridY = yIndex;
                            setShipPosition(xCell, yCell, xIndex, yIndex); // Set the ship position to the mouse

                            // draw a button only at the rectangle the mouse is hovering over to reduce the
                            // lag of drawing a button at each cell
                            if (rectButton(g, (x - (width / 2) - xCell + (xIndex * (width / xCell))),
                                    (y - (height / 2) - yCell + (yIndex * (height / yCell))), width / xCell,
                                    height / yCell,
                                    "Click me!", cellColor, lineColor, 7)) {

                                switchBoat = true; /*
                                                    * Tel's the {@link setShipPosition} method that we are ready to
                                                    * change ships
                                                    */

                                // a loop for the length of each ship, the calclations are simple so it was
                                // decided to keep the ship classes as they are
                                // This is the hitbox code of each ship
                                for (int destroyerLength = 0; destroyerLength < destroyer.shipLength; destroyerLength++) {
                                    for (int submarineLength = 0; submarineLength < submarine.shipLength; submarineLength++) {
                                        for (int cruiserLength = 0; cruiserLength < cruiser.shipLength; cruiserLength++) {
                                            for (int battleshipLength = 0; battleshipLength < battleship.shipLength; battleshipLength++) {
                                                for (int carrierLength = 0; carrierLength < carrier.shipLength; carrierLength++) {
                                                    switch (shipMode) { // A case switch for every combination of boats
                                                                        // that could overlap eachother, simple but
                                                                        // works
                                                        case "destroyer":
                                                            if (destroyer.xPositions[destroyerLength] == submarine.xPositions[submarineLength]
                                                                    && destroyer.yPositions[destroyerLength] == submarine.yPositions[submarineLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (destroyer.xPositions[destroyerLength] == cruiser.xPositions[cruiserLength]
                                                                    && destroyer.yPositions[destroyerLength] == cruiser.yPositions[cruiserLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (destroyer.xPositions[destroyerLength] == battleship.xPositions[battleshipLength]
                                                                    && destroyer.yPositions[destroyerLength] == battleship.yPositions[battleshipLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (destroyer.xPositions[destroyerLength] == carrier.xPositions[carrierLength]
                                                                    && destroyer.yPositions[destroyerLength] == carrier.yPositions[carrierLength]) {
                                                                switchBoat = false;
                                                            }

                                                            break;
                                                        case "submarine":
                                                            if (submarine.xPositions[submarineLength] == destroyer.xPositions[destroyerLength]
                                                                    && submarine.yPositions[submarineLength] == destroyer.yPositions[destroyerLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (submarine.xPositions[submarineLength] == cruiser.xPositions[cruiserLength]
                                                                    && submarine.yPositions[submarineLength] == cruiser.yPositions[cruiserLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (submarine.xPositions[submarineLength] == battleship.xPositions[battleshipLength]
                                                                    && submarine.yPositions[submarineLength] == battleship.yPositions[battleshipLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (submarine.xPositions[submarineLength] == carrier.xPositions[carrierLength]
                                                                    && submarine.yPositions[submarineLength] == carrier.yPositions[carrierLength]) {
                                                                switchBoat = false;
                                                            }
                                                            break;
                                                        case "cruiser":
                                                            if (cruiser.xPositions[cruiserLength] == destroyer.xPositions[destroyerLength]
                                                                    && cruiser.yPositions[cruiserLength] == destroyer.yPositions[destroyerLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (cruiser.xPositions[cruiserLength] == submarine.xPositions[submarineLength]
                                                                    && cruiser.yPositions[cruiserLength] == submarine.yPositions[submarineLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (cruiser.xPositions[cruiserLength] == battleship.xPositions[battleshipLength]
                                                                    && cruiser.yPositions[cruiserLength] == battleship.yPositions[battleshipLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (cruiser.xPositions[cruiserLength] == carrier.xPositions[carrierLength]
                                                                    && cruiser.yPositions[cruiserLength] == carrier.yPositions[carrierLength]) {
                                                                switchBoat = false;
                                                            }

                                                            break;
                                                        case "battleship":
                                                            if (battleship.xPositions[battleshipLength] == destroyer.xPositions[destroyerLength]
                                                                    && battleship.yPositions[battleshipLength] == destroyer.yPositions[destroyerLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (battleship.xPositions[battleshipLength] == submarine.xPositions[submarineLength]
                                                                    && battleship.yPositions[battleshipLength] == submarine.yPositions[submarineLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (battleship.xPositions[battleshipLength] == cruiser.xPositions[cruiserLength]
                                                                    && battleship.yPositions[battleshipLength] == cruiser.yPositions[cruiserLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (battleship.xPositions[battleshipLength] == carrier.xPositions[carrierLength]
                                                                    && battleship.yPositions[battleshipLength] == carrier.yPositions[carrierLength]) {
                                                                switchBoat = false;
                                                            }

                                                            break;
                                                        case "carrier":
                                                            if (carrier.xPositions[carrierLength] == destroyer.xPositions[destroyerLength]
                                                                    && carrier.yPositions[carrierLength] == destroyer.yPositions[destroyerLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (carrier.xPositions[carrierLength] == submarine.xPositions[submarineLength]
                                                                    && carrier.yPositions[carrierLength] == submarine.yPositions[submarineLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (carrier.xPositions[carrierLength] == cruiser.xPositions[cruiserLength]
                                                                    && carrier.yPositions[carrierLength] == cruiser.yPositions[cruiserLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (carrier.xPositions[carrierLength] == battleship.xPositions[battleshipLength]
                                                                    && carrier.yPositions[carrierLength] == battleship.yPositions[battleshipLength]) {
                                                                switchBoat = false;
                                                            }

                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                cellPressed = true;
                                if (switchBoat) {
                                    shipMode = "noBoatSelected"; // Place the boat by changing the string to something
                                                                 // other than the name of the ship
                                }

                            }
                        }
                    }

                    for (Point p : enemyShots) { // Go through every point in the enemy shots list
                        boolean hit = shipCells.contains(p); // if the shot hit a boat
                        crossColor = hit ? Color.red : Color.blue; // Turn table for color, blue for miss, red for hit
                        drawCenteredText(g, "X", startX + (int) p.getX() * cellWidth + cellWidth / 2,
                                startY + (int) p.getY() * cellHeight + cellHeight + 12, 50, crossColor,
                                "Arial"); // Draw the X or the cross
                    }

                }

            }
        }

        /**
         * A grid drawing function. The size of each cell will scale depending on the
         * width and height of the grid.
         * The grid comes with a built in "hovering" function that will highlight the
         * cell the mouse is hovering over.
         * Use {@link #getGridX() } and {@link #getGridY()} to get the selected cell
         *
         * @param g             is the graphics of the panel
         * @param x             The x position of the grid, it is the top left corner
         *                      unless centerAroundPoint is true, in which case its the
         *                      center
         * @param y             The y position of the grid, it is the top left corner
         *                      unless centerAroundPoint is true, in which case its the
         *                      center
         * @param width         The width of the grid
         * @param height        The height of the grid
         * @param xCell         The amount of cells in the x axis
         * @param yCell         The amount of cells in the y axis
         * @param lineThickness The thickness of each line between the cells
         * @param lineColor     The color of the line, uses the Color library
         * @param cellColor     The color of the cell, uses the Color library
         * @param doHoverEffect Boolean determining if the grid does the "hovering"
         *                      effect
         *
         */
        public void drawGrid(Graphics g, int x, int y, int width, int height, int xCell, int yCell,
                int lineThickness,
                Color lineColor, Color cellColor, boolean doHoverEffect) {

            // Code is the same as the grid drawing function above just this one handles the
            // ship selector
            for (int xIndex = 0; xIndex < xCell; xIndex++) {
                for (int yIndex = 0; yIndex < yCell; yIndex++) {

                    g.setColor(cellColor);
                    ((Graphics2D) g).setStroke(new BasicStroke(5));
                    g.drawRect((x - (width / 2) - xCell + (xIndex * (width / xCell))),
                            (y - (height / 2) - yCell + (yIndex * (height / yCell))), width / xCell,
                            height / yCell);

                    if (destroyer.doRender) {
                        for (int destroyerIndex = 0; destroyerIndex < destroyer.shipLength; destroyerIndex++) {
                            g.setColor(destroyer.shipColor);
                            g.fillRect(
                                    (x - (width / 2) - xCell
                                            + (destroyer.xPositions[destroyerIndex] * (width / xCell))),
                                    (y - (height / 2) - yCell
                                            + (destroyer.yPositions[destroyerIndex] * (height / yCell))),
                                    width / xCell,
                                    height / yCell);

                        }
                    }
                    if (submarine.doRender) {
                        for (int submarineIndex = 0; submarineIndex < submarine.shipLength; submarineIndex++) {
                            g.setColor(submarine.shipColor);
                            g.fillRect(
                                    (x - (width / 2) - xCell
                                            + (submarine.xPositions[submarineIndex] * (width / xCell))),
                                    (y - (height / 2) - yCell
                                            + (submarine.yPositions[submarineIndex] * (height / yCell))),
                                    width / xCell,
                                    height / yCell);
                        }
                    }
                    if (cruiser.doRender) {
                        for (int cruiserIndex = 0; cruiserIndex < cruiser.shipLength; cruiserIndex++) {
                            g.setColor(cruiser.shipColor);
                            g.fillRect(
                                    (x - (width / 2) - xCell
                                            + (cruiser.xPositions[cruiserIndex] * (width / xCell))),
                                    (y - (height / 2) - yCell
                                            + (cruiser.yPositions[cruiserIndex] * (height / yCell))),
                                    width / xCell,
                                    height / yCell);
                        }
                    }
                    if (battleship.doRender) {
                        for (int battleshipIndex = 0; battleshipIndex < battleship.shipLength; battleshipIndex++) {
                            g.setColor(battleship.shipColor);
                            g.fillRect(
                                    (x - (width / 2) - xCell
                                            + (battleship.xPositions[battleshipIndex] * (width / xCell))),
                                    (y - (height / 2) - yCell
                                            + (battleship.yPositions[battleshipIndex] * (height / yCell))),
                                    width / xCell,
                                    height / yCell);
                        }
                    }
                    if (carrier.doRender) {
                        for (int carrierIndex = 0; carrierIndex < carrier.shipLength; carrierIndex++) {
                            g.setColor(carrier.shipColor);
                            g.fillRect(
                                    (x - (width / 2) - xCell
                                            + (carrier.xPositions[carrierIndex] * (width / xCell))),
                                    (y - (height / 2) - yCell
                                            + (carrier.yPositions[carrierIndex] * (height / yCell))),
                                    width / xCell,
                                    height / yCell);
                        }
                    }

                    if (mouseX > (x - (width / 2) - xCell + (xIndex * (width / xCell)))
                            && mouseX < (x - (width / 2) - xCell + (xIndex * (width / xCell))) + width / xCell) {
                        if (mouseY > (y - (height / 2) - yCell + (yIndex * (height / yCell)))
                                && mouseY < (y - (height / 2) - yCell + (yIndex * (height / yCell)))
                                        + height / yCell) {
                            gridX = xIndex;
                            gridY = yIndex;
                            setShipPosition(xCell, yCell, xIndex, yIndex);

                            if (rectButton(g, (x - (width / 2) - xCell + (xIndex * (width / xCell))),
                                    (y - (height / 2) - yCell + (yIndex * (height / yCell))), width / xCell,
                                    height / yCell,
                                    "Click me!", cellColor, lineColor, 7)) {
                                switchBoat = true;
                                for (int destroyerLength = 0; destroyerLength < destroyer.shipLength; destroyerLength++) {
                                    for (int submarineLength = 0; submarineLength < submarine.shipLength; submarineLength++) {
                                        for (int cruiserLength = 0; cruiserLength < cruiser.shipLength; cruiserLength++) {
                                            for (int battleshipLength = 0; battleshipLength < battleship.shipLength; battleshipLength++) {
                                                for (int carrierLength = 0; carrierLength < carrier.shipLength; carrierLength++) {
                                                    switch (shipMode) {
                                                        case "destroyer":
                                                            if (destroyer.xPositions[destroyerLength] == submarine.xPositions[submarineLength]
                                                                    && destroyer.yPositions[destroyerLength] == submarine.yPositions[submarineLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (destroyer.xPositions[destroyerLength] == cruiser.xPositions[cruiserLength]
                                                                    && destroyer.yPositions[destroyerLength] == cruiser.yPositions[cruiserLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (destroyer.xPositions[destroyerLength] == battleship.xPositions[battleshipLength]
                                                                    && destroyer.yPositions[destroyerLength] == battleship.yPositions[battleshipLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (destroyer.xPositions[destroyerLength] == carrier.xPositions[carrierLength]
                                                                    && destroyer.yPositions[destroyerLength] == carrier.yPositions[carrierLength]) {
                                                                switchBoat = false;
                                                            }

                                                            break;
                                                        case "submarine":
                                                            if (submarine.xPositions[submarineLength] == destroyer.xPositions[destroyerLength]
                                                                    && submarine.yPositions[submarineLength] == destroyer.yPositions[destroyerLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (submarine.xPositions[submarineLength] == cruiser.xPositions[cruiserLength]
                                                                    && submarine.yPositions[submarineLength] == cruiser.yPositions[cruiserLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (submarine.xPositions[submarineLength] == battleship.xPositions[battleshipLength]
                                                                    && submarine.yPositions[submarineLength] == battleship.yPositions[battleshipLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (submarine.xPositions[submarineLength] == carrier.xPositions[carrierLength]
                                                                    && submarine.yPositions[submarineLength] == carrier.yPositions[carrierLength]) {
                                                                switchBoat = false;
                                                            }
                                                            break;
                                                        case "cruiser":
                                                            if (cruiser.xPositions[cruiserLength] == destroyer.xPositions[destroyerLength]
                                                                    && cruiser.yPositions[cruiserLength] == destroyer.yPositions[destroyerLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (cruiser.xPositions[cruiserLength] == submarine.xPositions[submarineLength]
                                                                    && cruiser.yPositions[cruiserLength] == submarine.yPositions[submarineLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (cruiser.xPositions[cruiserLength] == battleship.xPositions[battleshipLength]
                                                                    && cruiser.yPositions[cruiserLength] == battleship.yPositions[battleshipLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (cruiser.xPositions[cruiserLength] == carrier.xPositions[carrierLength]
                                                                    && cruiser.yPositions[cruiserLength] == carrier.yPositions[carrierLength]) {
                                                                switchBoat = false;
                                                            }

                                                            break;
                                                        case "battleship":
                                                            if (battleship.xPositions[battleshipLength] == destroyer.xPositions[destroyerLength]
                                                                    && battleship.yPositions[battleshipLength] == destroyer.yPositions[destroyerLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (battleship.xPositions[battleshipLength] == submarine.xPositions[submarineLength]
                                                                    && battleship.yPositions[battleshipLength] == submarine.yPositions[submarineLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (battleship.xPositions[battleshipLength] == cruiser.xPositions[cruiserLength]
                                                                    && battleship.yPositions[battleshipLength] == cruiser.yPositions[cruiserLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (battleship.xPositions[battleshipLength] == carrier.xPositions[carrierLength]
                                                                    && battleship.yPositions[battleshipLength] == carrier.yPositions[carrierLength]) {
                                                                switchBoat = false;
                                                            }

                                                            break;
                                                        case "carrier":
                                                            if (carrier.xPositions[carrierLength] == destroyer.xPositions[destroyerLength]
                                                                    && carrier.yPositions[carrierLength] == destroyer.yPositions[destroyerLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (carrier.xPositions[carrierLength] == submarine.xPositions[submarineLength]
                                                                    && carrier.yPositions[carrierLength] == submarine.yPositions[submarineLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (carrier.xPositions[carrierLength] == cruiser.xPositions[cruiserLength]
                                                                    && carrier.yPositions[carrierLength] == cruiser.yPositions[cruiserLength]) {
                                                                switchBoat = false;
                                                            }
                                                            if (carrier.xPositions[carrierLength] == battleship.xPositions[battleshipLength]
                                                                    && carrier.yPositions[carrierLength] == battleship.yPositions[battleshipLength]) {
                                                                switchBoat = false;
                                                            }

                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                cellPressed = true;
                                if (switchBoat) {
                                    shipMode = "noBoatSelected";
                                }

                            }
                        }
                    }
                }
            }
        }

        // endregion
        // region Set Ship Rotations
        public void setShipPosition(int xCell, int yCell, int xIndex,
                int yIndex) {

            switch (shipDirection) { // This case switch determines where each boat position should be for every
                                     // square depending on the rotation
                case "up":
                    switch (shipMode) {
                        case "destroyer":
                            if (yIndex == 0) // If your approaching the boundaries of the grid
                                yIndex++; // Move back for each block you go past the point above
                            for (int x = 0; x < destroyer.shipLength; x++) { // For each ship length
                                destroyer.xPositions[x] = xIndex; // X positions
                                destroyer.yPositions[x] = yIndex - x; // Y positions
                            }
                            break;
                        case "submarine":
                            if (yIndex < cruiser.shipLength)
                                yIndex += submarine.shipLength - 1 - yIndex;

                            for (int x = 0; x < submarine.shipLength; x++) {
                                submarine.xPositions[x] = xIndex;
                                submarine.yPositions[x] = yIndex - x;
                            }
                            break;
                        case "cruiser":
                            if (yIndex < cruiser.shipLength)
                                yIndex += cruiser.shipLength - 1 - yIndex;
                            for (int x = 0; x < cruiser.shipLength; x++) {
                                cruiser.xPositions[x] = xIndex;
                                cruiser.yPositions[x] = yIndex - x;
                            }
                            break;
                        case "battleship":
                            if (yIndex < battleship.shipLength)
                                yIndex += battleship.shipLength - 1 - yIndex;
                            for (int x = 0; x < battleship.shipLength; x++) {
                                battleship.xPositions[x] = xIndex;
                                battleship.yPositions[x] = yIndex - x;
                            }
                            break;
                        case "carrier":
                            if (yIndex < carrier.shipLength)
                                yIndex += carrier.shipLength - 1 - yIndex;
                            for (int x = 0; x < carrier.shipLength; x++) {
                                carrier.xPositions[x] = xIndex;
                                carrier.yPositions[x] = yIndex - x;
                            }
                            break;
                    }
                    break;
                case "right":
                    switch (shipMode) {
                        case "destroyer":
                            if (xIndex == xCell - destroyer.shipLength + 1)
                                xIndex--;
                            for (int x = 0; x < destroyer.shipLength; x++) {
                                destroyer.xPositions[x] = xIndex + x;
                                destroyer.yPositions[x] = yIndex;
                            }
                            break;
                        case "submarine":
                            if (xIndex >= xCell - submarine.shipLength + 1)
                                xIndex -= Math.abs(xCell - xIndex - submarine.shipLength);
                            for (int x = 0; x < submarine.shipLength; x++) {
                                submarine.xPositions[x] = xIndex + x;
                                submarine.yPositions[x] = yIndex;
                            }
                            break;
                        case "cruiser":
                            if (xIndex >= xCell - cruiser.shipLength + 1)
                                xIndex -= Math.abs(xCell - xIndex - cruiser.shipLength);
                            for (int x = 0; x < cruiser.shipLength; x++) {
                                cruiser.xPositions[x] = xIndex + x;
                                cruiser.yPositions[x] = yIndex;
                            }
                            break;
                        case "battleship":
                            if (xIndex >= xCell - battleship.shipLength + 1)
                                xIndex -= Math.abs(xCell - xIndex - battleship.shipLength);
                            for (int x = 0; x < battleship.shipLength; x++) {
                                battleship.xPositions[x] = xIndex + x;
                                battleship.yPositions[x] = yIndex;
                            }
                            break;
                        case "carrier":
                            if (xIndex >= xCell - carrier.shipLength + 1)
                                xIndex -= Math.abs(xCell - xIndex - carrier.shipLength);
                            for (int x = 0; x < carrier.shipLength; x++) {
                                carrier.xPositions[x] = xIndex + x;
                                carrier.yPositions[x] = yIndex;
                            }
                            break;
                    }
                    break;
                case "down":

                    switch (shipMode) {
                        case "destroyer":
                            if (yIndex == yCell - 1)
                                yIndex--;
                            for (int x = 0; x < destroyer.shipLength; x++) {
                                destroyer.xPositions[x] = xIndex;
                                destroyer.yPositions[x] = yIndex + x;
                            }
                            break;
                        case "submarine":
                            if (yIndex >= yCell - submarine.shipLength + 1)
                                yIndex -= Math.abs(yCell - yIndex - submarine.shipLength);
                            for (int x = 0; x < submarine.shipLength; x++) {
                                submarine.xPositions[x] = xIndex;
                                submarine.yPositions[x] = yIndex + x;
                            }
                            break;
                        case "cruiser":
                            if (yIndex >= yCell - cruiser.shipLength + 1)
                                yIndex -= Math.abs(yCell - yIndex - cruiser.shipLength);
                            for (int x = 0; x < cruiser.shipLength; x++) {
                                cruiser.xPositions[x] = xIndex;
                                cruiser.yPositions[x] = yIndex + x;
                            }
                            break;
                        case "battleship":
                            if (yIndex >= yCell - battleship.shipLength + 1)
                                yIndex -= Math.abs(yCell - yIndex - battleship.shipLength);
                            for (int x = 0; x < battleship.shipLength; x++) {
                                battleship.xPositions[x] = xIndex;
                                battleship.yPositions[x] = yIndex + x;
                            }
                            break;
                        case "carrier":
                            if (yIndex >= yCell - carrier.shipLength + 1)
                                yIndex -= Math.abs(yCell - yIndex - carrier.shipLength);
                            for (int x = 0; x < carrier.shipLength; x++) {
                                carrier.xPositions[x] = xIndex;
                                carrier.yPositions[x] = yIndex + x;
                            }
                            break;
                    }
                    break;
                case "left":

                    switch (shipMode) {
                        case "destroyer":
                            if (xIndex == 0)
                                xIndex++;
                            for (int x = 0; x < destroyer.shipLength; x++) {
                                destroyer.xPositions[x] = xIndex - x;
                                destroyer.yPositions[x] = yIndex;
                            }
                            break;
                        case "submarine":
                            if (xIndex < submarine.shipLength)
                                xIndex += submarine.shipLength - 1 - xIndex;
                            for (int x = 0; x < submarine.shipLength; x++) {
                                submarine.xPositions[x] = xIndex - x;
                                submarine.yPositions[x] = yIndex;
                            }
                            break;
                        case "cruiser":
                            if (xIndex < cruiser.shipLength)
                                xIndex += cruiser.shipLength - 1 - xIndex;
                            for (int x = 0; x < cruiser.shipLength; x++) {
                                cruiser.xPositions[x] = xIndex - x;
                                cruiser.yPositions[x] = yIndex;
                            }
                            break;
                        case "battleship":
                            if (xIndex < battleship.shipLength)
                                xIndex += battleship.shipLength - 1 - xIndex;
                            for (int x = 0; x < battleship.shipLength; x++) {
                                battleship.xPositions[x] = xIndex - x;
                                battleship.yPositions[x] = yIndex;
                            }
                            break;
                        case "carrier":
                            if (xIndex < carrier.shipLength)
                                xIndex += carrier.shipLength - 1 - xIndex;
                            for (int x = 0; x < carrier.shipLength; x++) {
                                carrier.xPositions[x] = xIndex - x;
                                carrier.yPositions[x] = yIndex;
                            }
                            break;
                    }
                    break;

            }
        }

        // endregion
        // region Ship Selector
        public void shipSelector(Graphics g, int x, int y, int width, int height) {
            drawRect(g, x, y, width, height, Color.BLACK, true);
            switch (keyIndex) { // Key index is changed by pressing r, which rotates the boat by changing the
                                // string
                case 1:
                    shipDirection = "up";
                    break;
                case 2:
                    shipDirection = "right";
                    break;
                case 3:
                    shipDirection = "down";
                    break;
                case 4:
                    shipDirection = "left";
                    break;
                default:
                    keyIndex = 1; // if you dont tell it a rotation, just do upwards
                    break;
            }
            // Buttons to select the ship you want to place
            if (roundedRectButton(g, x - 200 / 2, (y - (y / 5)) + y / 2, 200, y / 5, "destroyer", Color.BLACK,
                    destroyer.shipColor, 25, 10)) {
                shipMode = "destroyer"; // Select that ship
                destroyer.doRender = true; // Start drawing it
            }
            if (roundedRectButton(g, x - 200 / 2, (y - (y / 5) * 2) + y / 2, 200, y / 5, "submarine", Color.BLACK,
                    submarine.shipColor, 25, 10)) {
                shipMode = "submarine";
                submarine.doRender = true;
            }
            if (roundedRectButton(g, x - 200 / 2, (y - (y / 5) * 3) + y / 2, 200, y / 5, "cruiser", Color.BLACK,
                    cruiser.shipColor, 25, 10)) {
                shipMode = "cruiser";
                cruiser.doRender = true;
            }
            if (roundedRectButton(g, x - 200 / 2, (y - (y / 5) * 4) + y / 2, 200, y / 5, "battleship", Color.BLACK,
                    battleship.shipColor, 25, 10)) {
                shipMode = "battleship";
                battleship.doRender = true;
            }
            if (roundedRectButton(g, x - 200 / 2, (y - (y / 5) * 5) + y / 2, 200, y / 5, "carrier", Color.BLACK,
                    carrier.shipColor, 25, 10)) {
                shipMode = "carrier";
                carrier.doRender = true;
            }

        }

        // endregion
        // region Enemy Grid

        /**
         * A grid drawing function. The size of each cell will scale depending on the
         * width and height of the grid.
         * The grid comes with a built in "hovering" function that will highlight the
         * cell the mouse is hovering over.
         * 
         *
         * @param g             is the graphics of the panel
         * @param x             The x position of the grid, it is the top left corner
         *                      unless centerAroundPoint is true, in which case its the
         *                      center
         * @param y             The y position of the grid, it is the top left corner
         *                      unless centerAroundPoint is true, in which case its the
         *                      center
         * @param width         The width of the grid
         * @param height        The height of the grid
         * @param xCell         The amount of cells in the x axis
         * @param yCell         The amount of cells in the y axis
         * @param lineThickness The thickness of each line between the cells
         * @param lineColor     The color of the line, uses the Color library
         * @param cellColor     The color of the cell, uses the Color library
         * @param doHoverEffect Boolean determining if the grid does the "hovering"
         *                      effect
         * @param enemyShotsX   The shots of the enemy
         * @param enemyShotsY   The shots the enemy
         * @param player        The number player is using this grid
         *
         */
        public boolean drawEnemyGrid(Graphics g, int x, int y, int width, int height, int xCell, int yCell,
                int lineThickness,
                Color lineColor, Color cellColor, boolean doHoverEffect, List<Integer> enemyShotsX,
                List<Integer> enemyShotsY, int player) {

            // Create useful variables
            int cellWidth = width / xCell;
            int cellHeight = height / yCell;
            int startX = x - width / 2 - xCell;
            int startY = y - height / 2 - yCell;
            // Store the enemy shots in a hashset of points
            Set<Point> enemyShots = new HashSet<>();
            for (int i = 0; i < enemyShotsX.size(); i++) {
                enemyShots.add(new Point(enemyShotsX.get(i), enemyShotsY.get(i)));
            }

            // Make sure we built the boats only once to prevent lag
            if (!builtEnemyShips) {
                for (int i = 0; i < destroyer.shipLength; i++)
                    enemyShipCells.add(new Point(destroyer2.xPositions[i], destroyer2.yPositions[i]));
                for (int i = 0; i < submarine.shipLength; i++)
                    enemyShipCells.add(new Point(submarine2.xPositions[i], submarine2.yPositions[i]));
                for (int i = 0; i < cruiser.shipLength; i++)
                    enemyShipCells.add(new Point(cruiser2.xPositions[i], cruiser2.yPositions[i]));
                for (int i = 0; i < battleship.shipLength; i++)
                    enemyShipCells.add(new Point(battleship2.xPositions[i], battleship2.yPositions[i]));
                for (int i = 0; i < carrier.shipLength; i++)
                    enemyShipCells.add(new Point(carrier2.xPositions[i], carrier2.yPositions[i]));
            }

            for (int xIndex = 0; xIndex < xCell; xIndex++) { // Main x axis drawing loop
                for (int yIndex = 0; yIndex < yCell; yIndex++) { // Main y axis drawing loop
                    int cellX = startX + xIndex * cellWidth; // Get Cell x position depending on the index of the loops
                                                             // above
                    int cellY = startY + yIndex * cellHeight; // Get Cell y position depending on the index of the loops
                                                              // above

                    g.setColor(cellColor);
                    g.drawRect(cellX, cellY, cellWidth, cellHeight);

                    // Find the cell the mouse is hovering over
                    if (mouseX > cellX && mouseX < cellX + cellWidth &&
                            mouseY > cellY && mouseY < cellY + cellHeight) {
                        gridX = xIndex;
                        gridY = yIndex;

                        // Create a button only over the cell the mouse is hovering over to prevent lag
                        if (rectButton(g, cellX, cellY, cellWidth, cellHeight, "Click me!", cellColor, lineColor, 7)
                                && !hasShot) {
                            // Once the button is pressed
                            hasShot = true;
                            mouseReady = false;
                            attemptedX.add(xIndex); // Save it to a list to give to the enemy
                            attemptedY.add(yIndex);
                            registerShot(xIndex, yIndex); // Register the shot so we can save the position
                        }
                    }
                }
            }
            if (player == 1) {
                tempScore1 = 0;
            } else if (player == 2){
                tempScore2 = 0;
            }

            for (Point p : drawnHitPoints) { // Go throught all the hit points
                // code is simliar to other code above, comments will be less
                g.setColor(Color.black);
                g.fillRect(startX + p.x * cellWidth, startY + p.y * cellHeight, cellWidth, cellHeight);
                boolean hit = enemyShipCells.contains(p);
                Color textColor = hit ? Color.red : Color.blue;
                if (hit){ // If a enemy is hit
                if(player == 1){
                    tempScore1++; // Increase the score by 1
                } else if (player == 2){
                    tempScore2++;
                }
            }
                    
                drawCenteredText(g, "X", startX + p.x * cellWidth + cellWidth / 2,
                        startY + p.y * cellHeight + cellHeight + 12, 50, textColor,
                        "Arial");

            }
            // Depending on the player, make the temporary score the total after the loop is
            // complete

            return hasShot;
        }
        // endregion

        public void registerShot(int xIndex, int yIndex) {
            drawnHitPoints.add(new Point(xIndex, yIndex)); // Add the point to the list
        }

        public List<Integer> getAttemptedX() {
            return attemptedX; // Get the shots from the player
        }

        public List<Integer> getAttemptedY() {
            return attemptedY; // Get the shots from the player
        }

        public void setEnemyShips(Ships.Destroyer destroyer2, Ships.Submarine submarine2, Ships.Cruiser cruiser2,
                Ships.Battleship battleship2, Ships.Carrier carrier2) {
            this.destroyer2 = destroyer2;
            this.submarine2 = submarine2;
            this.cruiser2 = cruiser2;
            this.battleship2 = battleship2;
            this.carrier2 = carrier2;

        } // set the enemy ships for the other player, lets the enemy grid if it hit you
          // or not
    }

    public String checkPlayer1Details(Graphics g) { // Check the pass for player 1
        String passAttempt = textField(g, frame.getWidth() / 2, frame.getHeight() / 2, 200, 30, Color.BLACK,
                Color.white, 10, 1); // Use the custom text field that doesnt look ugly to get user input
        panel.setBackground(Color.ORANGE);
        drawCenteredText(g, "Player 1 : Enter your password", frame.getWidth() / 2,
                frame.getHeight() * 2 / 5, 50, Color.BLACK, "Arial");

        return passAttempt; // Return it for checking elsewhere
    }

    public String checkPlayer2Details(Graphics g) { // Same as function above
        String passAttempt = textField(g, frame.getWidth() / 2, frame.getHeight() / 2, 200, 30, Color.BLACK,
                Color.white, 10, 1);
        panel.setBackground(Color.ORANGE);
        drawCenteredText(g, "Player 2 : Enter your password", frame.getWidth() / 2,
                frame.getHeight() * 2 / 5, 50, Color.BLACK, "Arial");

        return passAttempt;
    }

    public void startMenu(Graphics g) { // The start menu interface
        panel.setBackground(Color.ORANGE);
        drawCenteredText(g, "Battleship", frame.getWidth() / 2, 200, 100, Color.black, "Arial");
        drawCenteredText(g, "Click anywhere to continue", frame.getWidth() / 2, frame.getHeight() / 2, 60, Color.black,
                "Arial");
        if (mousePressed && mouseReady) { // If you click anywhere
            mouseReady = false;
            gameController.gameScreen = gameController.enterPlayer1Details; // Allow player 1 to create password
            try { // error catch the music
                menuMusicClip.stop();
            } catch (Exception e) {
                System.out.println(e + ". (startMenu method)");
            }
            musicReady = true; // Ready to change music
        }
    }

    public void modeSelectMenu(Graphics g) { // Mode select menu (not used so will not be commented, since it was
                                             // decided that making a computer so out of reach for the time frame)
        panel.setBackground(Color.blue);
        drawCenteredText(g, "Select Game Mode", frame.getWidth() / 2, frame.getHeight() / 3, 100, Color.BLACK,
                "Arial");
        if (roundedRectButton(g, frame.getWidth() / 2 - 200 / 2 - 200, frame.getHeight() / 2 - 125 / 2 + 75, 200,
                125,
                "Player",
                Color.BLACK, Color.WHITE, 40, 13)) {
            mouseReady = false;
            gameController.gameScreen = gameController.enterPlayer1Details;
        }
        if (roundedRectButton(g, frame.getWidth() / 2 - 200 / 2 + 200, frame.getHeight() / 2 - 125 / 2 + 75, 200,
                125,
                "Comp",
                Color.BLACK, Color.WHITE, 40, 13)) {
            mouseReady = false;
            gameController.gameScreen = gameController.player1ShipSelector;
        }
        if (roundedRectButton(g, 25, 25, 70, 35, "Back", Color.BLACK, Color.WHITE, 18, 13)) {
            mouseReady = false;
            gameController.gameScreen = gameController.startMenu;

            try {
                gameMusicClip.stop();
            } catch (Exception e) {
                System.out.println(e + ". (modeSelectMenu method)");
            }
            musicReady = true;
        }
    }

    public String getPlayerDetails(Graphics g) { // alows the players to create their passwords
        String player1Pass = textField(g, frame.getWidth() / 2, frame.getHeight() / 2, 200, 30, Color.BLACK,
                Color.white, 10, 1); // get user input

        drawCenteredText(g, "Player 1 Details", frame.getWidth() / 2, frame.getHeight() / 5, 100, Color.BLACK,
                "Arial");
        drawCenteredText(g, "Player 1 : Enter your password", frame.getWidth() / 2,
                frame.getHeight() * 2 / 5, 50, Color.BLACK, "Arial");

        drawCenteredText(g, "Password:", frame.getWidth() / 2 - 220, frame.getHeight() / 2 + 35, 40, Color.BLACK,
                "Arial");

        if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 200, 200, 125,
                "Ok",
                Color.BLACK, Color.WHITE, 40, 13) && mouseReady && !(player1Pass.equals(""))) { // if they are ready and
                                                                                                // didn't just leave it
                                                                                                // blank (to prevent
                                                                                                // unfocusing mistakes)

            mouseReady = false;
            gameController.gameScreen = gameController.enterPlayer2Details;
        }
        return player1Pass;
    }

    public String getPlayer2Details(Graphics g) { // Same as function above
        String player2Pass = textField(g, frame.getWidth() / 2, frame.getHeight() / 2, 200, 30, Color.BLACK,
                Color.white, 10, 1);

        panel.setBackground(Color.blue);
        drawCenteredText(g, "Player 2 Details", frame.getWidth() / 2, frame.getHeight() / 5, 100, Color.BLACK,
                "Arial");
        drawCenteredText(g, "Player 2 : Enter your password", frame.getWidth() / 2,
                frame.getHeight() * 2 / 5, 50, Color.BLACK, "Arial");

        drawCenteredText(g, "Password:", frame.getWidth() / 2 - 220, frame.getHeight() / 2 + 35, 40, Color.BLACK,
                "Arial");

        if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 200, 200, 125,
                "Ok",
                Color.BLACK, Color.WHITE, 40, 13) && mouseReady && !(player2Pass.equals(""))) {

            mouseReady = false;
            gameController.gameScreen = gameController.checkPrePlayer1Details;
        }
        return player2Pass;
    }

    public void scoreboard(Graphics g, int x, int y, int width, int height, int score, int player) { // Score board
                                                                                                     // interface
        drawRect(g, x, y, width, height, Color.BLACK, true);
        drawCenteredText(g, "Score:", x, y, 20, Color.BLACK, "Arial");
        drawCenteredText(g, String.valueOf(score), x, y + 35, 20, Color.BLACK, "Arial");
        if (player == 1) {
            player1Score = tempScore1;
        } else if (player == 2) {
            player2Score = tempScore2;
        }
    }

    // Button to mute the music
    public void muteMusicButton(Graphics g) {
        if (rectButton(g, 0 + 50, frame.getHeight() - 150, 175, 100, "TOGGLE MUSIC", Color.black, Color.yellow, 20)) {
            mouseReady = false;
            isMuted = !isMuted;

            try { // Error trap
                if (isMuted) {
                    if (menuMusicClip != null && menuMusicClip.isRunning()) { // if the music is playing
                        menuMusicClip.stop(); // stop it
                    }
                    if (gameMusicClip != null && gameMusicClip.isRunning()) {
                        gameMusicClip.stop();
                    }
                } else {
                    if (menuMusicClip != null && gameController.gameScreen == "startMenu") { // If the music should be
                                                                                             // playing
                        menuMusicClip.start(); // start it
                    }
                    if (gameMusicClip != null) {
                        gameMusicClip.start();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void print(Graphics g, String text) { // Print function because the terminal is not visible in fullscreen
        drawCenteredText(g, text, frame.getWidth() / 2, 20, 10, Color.BLACK, "Arial");
    }

    public int getGridX() { // simple getter (what cell mouse is hovering over)
        return gridX;
    }

    public int getGridY() { // simpler getter
        return gridY;
    }

    public String textField(Graphics g, int x, int y, int width, int height,
            Color backgroundColor, Color textColor, int textSize, int textFieldIndex) { // Complete custom text field

        int margin = width / 20; // Initial offset
        while (fieldFocused.size() <= 9) {
            fieldFocused.add(false); // Initialize with false values
        }

        if (mousePressed
                && !(rectButton(g, x - width / 2, y - height / 2, width, height, "", backgroundColor, textColor,
                        textSize))) {
            for (int j = 0; j < fieldFocused.size(); j++) {
                fieldFocused.set(j, false);
            }
        } // If the button isnt pressed but the mouse is pressed, then all the textfields
          // currently on the screen should be unfocused

        if (rectButton(g, x - width / 2, y - height / 2, width, height, "", backgroundColor, textColor, textSize)) {
            fieldFocused.set(textFieldIndex, true);
            textInput = "";
        } // if the text field is pressed then the text it in should be erased and then
          // the field should be focused
        drawFillRect(g, x, y, width, height, backgroundColor, true);

        if (fieldFocused.get(textFieldIndex)) { // If the field is focused
            g.setColor(textColor);
            Font font = new Font("Arial", Font.BOLD, textSize);
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);
            // FontMetrics is used to get data about the string such as the width
            // to be able to detect when the string is about to leave the text field

            // Determine the visible string
            String visibleText = textInput;
            int totalWidth = metrics.stringWidth(visibleText);
            int maxTextWidth = width - 2 * margin;

            if (totalWidth > maxTextWidth) {
                // Start trimming from the left until it fits
                int start = 0;
                while (start < textInput.length()) {
                    String substring = textInput.substring(start);
                    if (metrics.stringWidth(substring) <= maxTextWidth) {
                        visibleText = substring;
                        break;
                    }
                    start++;
                }
            }

            // Draw the visible part of the text
            int textX = x - width / 2 + margin;
            int textY = y + (metrics.getHeight() / 4);
            g.drawString(visibleText, textX, textY);
        }

        return textInput; // return whats written
    }

    // An all in one button function that returns if the button is pressed
    public boolean rectButton(Graphics g, int x, int y, int width, int height, String label, Color buttonColor,
            Color textColor, int textSize) {
        g.setColor(buttonColor);
        g.fillRect(x, y, width, height);
        g.setColor(textColor);
        Font font = new Font("Arial", Font.BOLD, textSize);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int textWidth = metrics.stringWidth(label);
        int textHeight = metrics.getHeight();
        // Make sure the text is completely centered on the button, adjusting for the
        // height and width of characters
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - textHeight) / 2 + metrics.getAscent();
        g.drawString(label, textX, textY);
        return withinBoundaries(mouseX, mouseY, x, y, width, height) && mousePressed && mouseReady;
    }

    // same as the button above but has rounded corners so it looks a bit better
    public boolean roundedRectButton(Graphics g, int x, int y, int width, int height, String label,
            Color buttonColor,
            Color textColor, int textSize, int arc) {

        g.setColor(buttonColor);
        g.fillRoundRect(x, y, width, height, arc, arc);
        g.setColor(textColor);
        Font font = new Font("Arial", Font.BOLD, textSize);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int textWidth = metrics.stringWidth(label);
        int textHeight = metrics.getHeight();
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - textHeight) / 2 + metrics.getAscent();
        g.drawString(label, textX, textY);
        return withinBoundaries(mouseX, mouseY, x, y, width, height) && mousePressed && mouseReady;
    }

    // A circle button instead of rectangle (Isnt used)
    public boolean circleButton(Graphics g, int x, int y, int size, String label, Color buttonColor,
            Color textColor,
            int textSize) {
        g.setColor(buttonColor);
        g.fillOval(x, y, size, size);
        g.setColor(textColor);
        Font font = new Font("Arial", Font.BOLD, textSize);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int textWidth = metrics.stringWidth(label);
        int textHeight = metrics.getHeight();
        int textX = x + (size - textWidth) / 2;
        int textY = y + (size - textHeight) / 2 + metrics.getAscent();
        g.drawString(label, textX, textY);
        return distance(mouseX, mouseY, x, y) < size && mousePressed;
    }

    // Text drawing in 1 line
    public void drawText(Graphics g, String text, int x, int y, int textSize, Color textColor, String textFont) {
        g.setColor(textColor);
        Font font = new Font("Arial", Font.BOLD, textSize);
        g.setFont(font);
        g.drawString(text, x, y);
    }

    // Drawing text but centered around a point made possible by FontMetrics
    public void drawCenteredText(Graphics g, String text, int x, int y, int textSize, Color textColor,
            String textFont) {
        g.setColor(textColor);
        Font font = new Font("Arial", Font.BOLD, textSize);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        int textWidth = metrics.stringWidth(text);
        int textHeight = metrics.getHeight();
        int textX = x - (textWidth / 2);
        int textY = y - (textHeight / 2);
        g.drawString(text, textX, textY);
    }

    // Image drawing function in 1 line
    public void drawImage(Graphics g, String imageName, int x, int y, int width, int height,
            boolean centerAroundPoint) {

        try { // Read the file in a error trap
            image = ImageIO.read(new File(imageName));
        } catch (Exception e) {
            System.out.println("NO IMAGE FOUND!");
        }

        if (centerAroundPoint) { // draw the image depending if you want to center it around the point you feed
                                 // through the parameters or basic drawing
            g.drawImage(image, x - (width / 2), y - (height / 2), width, height, null);
        } else {
            g.drawImage(image, x, y, width, height, null);
        }

    }

    // Function for drawing a line (Not used)
    public void drawLine(Graphics g, int x1, int y1, int x2, int y2, int thickness, Color color) {
        g.setColor(color);
        ((Graphics2D) g).setStroke(new BasicStroke(20));
        g.drawLine(x1, y1, x2, y2);
    }

    // Function for drawing a filled rectangle, comes with centering around a point
    // support
    public void drawFillRect(Graphics g, int x, int y, int width, int height, Color color,
            boolean centerAroundPoint) {
        g.setColor(color);
        if (centerAroundPoint) {
            g.fillRect(x - (width / 2), y - (height / 2), width, height);
        } else {
            g.fillRect(x, y, width, height);
        }

    }

    // Same as function above just not filled
    public void drawRect(Graphics g, int x, int y, int width, int height, Color color, boolean centerAroundPoint) {
        g.setColor(color);
        if (centerAroundPoint) {
            g.drawRect(x - (width / 2), y - (height / 2), width, height);
        } else {
            g.drawRect(x, y, width, height);
        }
    }

    // Same as above just a circle
    public void drawFillCircle(Graphics g, int x, int y, int size, Color color, boolean centerAroundPoint) {
        g.setColor(color);
        if (centerAroundPoint) {
            g.fillOval(x - (size / 2), y - (size / 2), size, size);
        } else {
            g.fillOval(x, y, size, size);
        }
    }

    // Same as above just a non filled circle
    public void drawCircle(Graphics g, int x, int y, int size, Color color, boolean centerAroundPoint) {
        g.setColor(color);
        if (centerAroundPoint) {
            g.drawOval(x - (size / 2), y - (size / 2), size, size);
        } else {
            g.drawOval(x, y, size, size);
        }
    }

    // Formula to get the distance between 2 points
    private int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // Formula to determine if one object is within the boundaries of another
    private boolean withinBoundaries(int x, int y, int objectX, int objectY, int width, int height) {
        return x > objectX && x < objectX + width && y > objectY && y < objectY + height;
    }

    // Function to play music
    public void gameMusic(String gameScreen) { // method for all the music in the project
        switch (gameScreen) { // switch case for different game screens
            case "startMenu": // If its the start menu play this music
                if (musicReady) {
                    try { // Error trap because the music doesnt load instanly or the file isnt setup
                          // right
                        AudioInputStream menuSong = AudioSystem.getAudioInputStream(
                                new File("menuSong.wav")); // Read the file
                        menuMusicClip = AudioSystem.getClip(); // Setup
                        menuMusicClip.open(menuSong); // Play
                        menuMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop it
                    } catch (Exception e) {
                        System.out.println(e + ". menuSong problems");
                    }
                    musicReady = false;
                }
                break;
            default: // play this music if not the start menu
                if (musicReady) {
                    try { // Error trap because the music doesnt load instanly or the file isnt setup
                          // right
                        AudioInputStream gameSong = AudioSystem.getAudioInputStream(
                                new File("gameSong.wav")); // Read the file
                        gameMusicClip = AudioSystem.getClip(); // Setup
                        gameMusicClip.open(gameSong); // Play
                        gameMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // loop it
                    } catch (Exception e) {
                        System.out.println(e + ". gameSong problems");
                    }
                    musicReady = false;
                }
                break;
        }
    }

}
