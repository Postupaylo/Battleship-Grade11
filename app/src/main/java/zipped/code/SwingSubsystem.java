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

public class SwingSubsystem {
    Ships ships = new Ships();
    JFrame frame;
    JPanel panel;
    Image image;
    Image grug;
    int mouseX;
    int mouseY;
    int gridX;
    int gridY;
    int keyIndex = 1;
    int pressedKey;
    int overAmount;
    int player1Score;
    int player2Score;
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

    GameController gameController;

    boolean playerFieldsAdded = false;

    public SwingSubsystem() {
        fieldFocused = new ArrayList<>();
        destroyer = ships.new Destroyer();
        submarine = ships.new Submarine();
        cruiser = ships.new Cruiser();
        battleship = ships.new Battleship();
        carrier = ships.new Carrier();
        destroyer2 = ships.new Destroyer();
        submarine2 = ships.new Submarine();
        cruiser2 = ships.new Cruiser();
        battleship2 = ships.new Battleship();
        carrier2 = ships.new Carrier();
        playerData1 = new PlayerData(destroyer, submarine, cruiser, battleship, carrier);
        playerData2 = new PlayerData(destroyer2, submarine2, cruiser2, battleship2, carrier2);
        gameController = new GameController();

        try {
            grug = ImageIO.read(new File("ship.png"));
        } catch (Exception e) {
            System.out.println("NO IMAGE FOUND!");
        }
        frame = new JFrame("Battleship");
        frame.setSize(1000, 1000);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                draw(g);
            }
        };

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                panel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                panel.repaint();
            }
        });

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = true; // Mouse is pressed
                mouseX = e.getX();
                mouseY = e.getY();
                panel.repaint(); // Refresh panel
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseReady = true;
                mousePressed = false; // Mouse is released
                cellPressed = false;
                panel.repaint();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_BACK_SPACE:
                        textInput = textInput.substring(0, textInput.length() - 1);
                        break;
                    case (KeyEvent.VK_SHIFT):
                        textInput = textInput.substring(0, textInput.length());
                        break;
                    case (KeyEvent.VK_ALT):
                        textInput = textInput.substring(0, textInput.length());
                        break;
                    case (KeyEvent.VK_CONTROL):
                        textInput = textInput.substring(0, textInput.length());
                        break;
                    case (KeyEvent.VK_ESCAPE):
                        textInput = textInput.substring(0, textInput.length());
                        break;
                    default:
                        textInput += String.valueOf(e.getKeyChar());
                        break;
                }
                if (e.getKeyCode() == KeyEvent.VK_R && keyReady) {
                    keyReady = false;
                    keyIndex++;
                    panel.repaint();
                }
            }

            public void keyReleased(KeyEvent k) {
                keyReady = true;
                panel.repaint();
            }
        });

        panel.setFocusable(true);
        panel.requestFocusInWindow();
        panel.setBackground(Color.blue);

        frame.setIconImage(grug);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void draw(Graphics g) {
        gameController.switchScreen(g);
        panel.repaint();
    }

    public class GameController {

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

        public GameController() {
        }

        public void switchScreen(Graphics g) {
            switch (gameScreen) {
                case startMenu:
                    startMenu(g);
                    break;
                case modeMenu:
                    modeSelectMenu(g);
                    break;
                case enterPlayer1Details:
                    player1Pass = getPlayerDetails(g);
                    break;
                case enterPlayer2Details:
                    player2Pass = getPlayer2Details(g);
                    break;
                case player1ShipSelector:
                    playerData1.drawGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10,
                            10, Color.black,
                            Color.black,
                            true);
                    if (roundedRectButton(g, frame.getWidth() - 250, 50, 200, 100, "Ready", Color.black,
                            Color.white, 30, 20)) {
                        gameController.gameScreen = gameController.checkPrePlayer2Details;
                    }
                    playerData1.shipSelector(g, frame.getWidth() / 2 + 700 / 2 + 250 / 2 + 30,
                            frame.getHeight() / 2, 250,
                            500);

                    break;
                case player2ShipSelector:
                    playerData2.drawGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10,
                            10, Color.black,
                            Color.black,
                            true);

                    if (roundedRectButton(g, frame.getWidth() - 250, 50, 200, 100, "Ready", Color.black,
                            Color.white, 30, 20)) {
                        gameScreen = checkPlayer1Details;
                    }
                    playerData2.shipSelector(g, frame.getWidth() / 2 + 700 / 2 + 250 / 2 + 30,
                            frame.getHeight() / 2, 250,
                            500);
                    break;

                case checkPlayer1Details:
                    String tempPass3 = checkPlayer1Details(g);
                    if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 150, 200, 125,
                            "Ok",
                            Color.BLACK, Color.WHITE, 40, 13)) {
                        if (tempPass3.equals(gameController.player1Pass)) {
                            gameController.gameScreen = gameController.player1Grid;
                            errorMessage = "";
                        } else {
                            errorMessage = "Wrong Password! Please try again";
                        }
                    }
                    drawCenteredText(g, errorMessage, frame.getWidth() / 2, frame.getHeight() - 100, 30,
                            Color.black, "Arial");
                    break;
                case checkPlayer2Details:
                    String tempPass4 = checkPlayer2Details(g);
                    if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 150, 200, 125,
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
                case checkPrePlayer1Details:
                    String tempPass = checkPlayer1Details(g);
                    if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 150, 200, 125,
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
                case checkPrePlayer2Details:
                    String tempPass2 = checkPlayer2Details(g);
                    if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 150, 200, 125,
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
                case player1Grid:
                    playerData1.setEnemyShips(playerData2.destroyer, playerData2.submarine, playerData2.cruiser,
                            playerData2.battleship, playerData2.carrier);
                    playerData2.setEnemyShips(playerData1.destroyer, playerData1.submarine, playerData1.cruiser,
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

                            180, 100, player1Score);
                    break;
                case player1SecondGrid:
                    playerData2.hasShot = false;
                    if (roundedRectButton(g, frame.getWidth() - 250, frame.getHeight() - 50 - 100, 200, 100, "Back",
                            Color.black, Color.white, 30, 20)) {
                        gameScreen = player1Grid;
                    }

                    if (playerData1.drawEnemyGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10,
                            10, Color.black,
                            Color.black,
                            true, playerData2.getAttemptedX(), playerData2.getAttemptedY())) {
                        hasPlayer2Shot = false;
                        hasPlayer1Shot = true;
                        gameScreen = checkPlayer2Details;
                    }

                    break;
                case player2Grid:
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

                            180, 100, player2Score);
                    break;
                case player2SecondGrid:
                    playerData1.hasShot = false;
                    if (roundedRectButton(g, frame.getWidth() - 250, frame.getHeight() - 50 - 100, 200, 100, "Back",
                            Color.black, Color.white, 30, 20)) {
                        gameScreen = player2Grid;
                    }

                    if (playerData2.drawEnemyGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10,
                            10, Color.black,
                            Color.black,
                            true, playerData1.getAttemptedX(), playerData1.getAttemptedY())) {
                        hasPlayer2Shot = true;
                        hasPlayer1Shot = false;
                        gameScreen = checkPlayer1Details;
                    }

                    break;
            }
        }

    }

    public class PlayerData {

        List<Integer> attemptedX = new ArrayList<>();
        List<Integer> attemptedY = new ArrayList<>();

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

        public PlayerData(Ships.Destroyer destroyer, Ships.Submarine submarine, Ships.Cruiser cruiser,
                Ships.Battleship battleship, Ships.Carrier carrier) {

            this.destroyer = destroyer;
            this.submarine = submarine;
            this.cruiser = cruiser;
            this.battleship = battleship;
            this.carrier = carrier;

            while (attemptedX.size() < 100) {
                attemptedX.add(-1);
            }
            while (attemptedY.size() < 100) {
                attemptedY.add(-1);
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

            Set<Point> shots = new HashSet<>();
            for (int i = 0; i < enemyShotsX.size(); i++) {
                shots.add(new Point(enemyShotsX.get(i), enemyShotsY.get(i)));
            }

            Set<Point> shipCells = new HashSet<>();
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

            for (int xIndex = 0; xIndex < xCell; xIndex++) {
                for (int yIndex = 0; yIndex < yCell; yIndex++) {
                    int cellX = startX + xIndex * cellWidth;
                    int cellY = startY + yIndex * cellHeight;
                    Point cell = new Point(xIndex, yIndex);
                    g.setColor(cellColor);
                    ((Graphics2D) g).setStroke(new BasicStroke(5));
                    g.drawRect((x - (width / 2) - xCell + (xIndex * (width / xCell))),
                            (y - (height / 2) - yCell + (yIndex * (height / yCell))), width / xCell,
                            height / yCell);

                    if (shots.contains(cell)) {
                        boolean hit = shipCells.contains(cell);
                        Color textColor = hit ? Color.red : Color.blue;
                        drawCenteredText(g, "X", cellX + cellWidth / 2, cellY + cellHeight + 12, 50, textColor,
                                "Arial");

                    }

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

        public void drawGrid(Graphics g, int x, int y, int width, int height, int xCell, int yCell,
                int lineThickness,
                Color lineColor, Color cellColor, boolean doHoverEffect) {

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

            switch (shipDirection) {
                case "up":
                    switch (shipMode) {
                        case "destroyer":
                            if (yIndex == 0)
                                yIndex++;
                            for (int x = 0; x < destroyer.shipLength; x++) {
                                destroyer.xPositions[x] = xIndex;
                                destroyer.yPositions[x] = yIndex - x;
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
            switch (keyIndex) {
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
                    keyIndex = 1;
                    break;
            }
            if (roundedRectButton(g, x - 200 / 2, (y - (y / 5)) + y / 2, 200, y / 5, "destroyer", Color.BLACK,
                    destroyer.shipColor, 25, 10)) {
                shipMode = "destroyer";
                destroyer.doRender = true;
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
        boolean hasShot = false;

        public boolean drawEnemyGrid(Graphics g, int x, int y, int width, int height, int xCell, int yCell,
                int lineThickness,
                Color lineColor, Color cellColor, boolean doHoverEffect, List<Integer> enemyShotsX,
                List<Integer> enemyShotsY) {

            int cellWidth = width / xCell;
            int cellHeight = height / yCell;
            int startX = x - width / 2 - xCell;
            int startY = y - height / 2 - yCell;

            // Convert to sets for fast lookup
            Set<Point> shots = new HashSet<>();
            for (int i = 0; i < enemyShotsX.size(); i++) {
                shots.add(new Point(enemyShotsX.get(i), enemyShotsY.get(i)));
            }

            Set<Point> attemptedShots = new HashSet<>();
            for (int i = 0; i < attemptedX.size(); i++) {
                attemptedShots.add(new Point(attemptedX.get(i), attemptedY.get(i)));
            }

            Set<Point> shipCells = new HashSet<>();
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

            Set<Point> EnemyShipCells = new HashSet<>();
            for (int i = 0; i < destroyer.shipLength; i++)
                shipCells.add(new Point(destroyer2.xPositions[i], destroyer2.yPositions[i]));
            for (int i = 0; i < submarine.shipLength; i++)
                shipCells.add(new Point(submarine2.xPositions[i], submarine2.yPositions[i]));
            for (int i = 0; i < cruiser.shipLength; i++)
                shipCells.add(new Point(cruiser2.xPositions[i], cruiser2.yPositions[i]));
            for (int i = 0; i < battleship.shipLength; i++)
                shipCells.add(new Point(battleship2.xPositions[i], battleship2.yPositions[i]));
            for (int i = 0; i < carrier.shipLength; i++)
                shipCells.add(new Point(carrier2.xPositions[i], carrier2.yPositions[i]));

            for (int xIndex = 0; xIndex < xCell; xIndex++) {
                for (int yIndex = 0; yIndex < yCell; yIndex++) {
                    int cellX = startX + xIndex * cellWidth;
                    int cellY = startY + yIndex * cellHeight;

                    g.setColor(cellColor);
                    g.drawRect(cellX, cellY, cellWidth, cellHeight);

                    Point cell = new Point(xIndex, yIndex);

                    if (attemptedShots.contains(cell)) {
                        g.setColor(Color.black);
                        g.fillRect(cellX, cellY, cellWidth, cellHeight);
                        boolean hit = shipCells.contains(cell);
                        Color textColor = hit ? Color.red : Color.blue;
                        drawCenteredText(g, "X", cellX + cellWidth / 2, cellY + cellHeight + 12, 50, textColor,
                                "Arial");

                    }
                    // else if (shots.contains(cell)) {
                    // g.setColor(Color.black);
                    // g.fillRect(cellX, cellY, cellWidth, cellHeight);
                    // boolean hit = shipCells.contains(cell);
                    // Color textColor = hit ? Color.red : Color.blue;
                    // drawCenteredText(g, "X", cellX + cellWidth / 2, cellY + cellHeight + 12, 50,
                    // textColor,
                    // "Arial");
                    // }

                    if (mouseX > cellX && mouseX < cellX + cellWidth &&
                            mouseY > cellY && mouseY < cellY + cellHeight) {
                        gridX = xIndex;
                        gridY = yIndex;

                        if (rectButton(g, cellX, cellY, cellWidth, cellHeight, "Click me!", cellColor, lineColor, 7)
                                && !hasShot) {
                            hasShot = true;
                            mouseReady = false;
                            attemptedX.add(xIndex);
                            attemptedY.add(yIndex);
                        }
                    }
                }
            }
            return hasShot;
        }
        // endregion

        public List<Integer> getAttemptedX() {
            return attemptedX;
        }

        public List<Integer> getAttemptedY() {
            return attemptedY;
        }

        public void setEnemyShips(Ships.Destroyer destroyer2, Ships.Submarine submarine2, Ships.Cruiser cruiser2,
                Ships.Battleship battleship2, Ships.Carrier carrier2) {
            this.destroyer2 = destroyer2;
            this.submarine2 = submarine2;
            this.cruiser2 = cruiser2;
            this.battleship2 = battleship2;
            this.carrier2 = carrier2;

        }
    }

    public String checkPlayer1Details(Graphics g) {
        String passAttempt = textField(g, frame.getWidth() / 2, frame.getHeight() / 2, 200, 30, Color.BLACK,
                Color.white, 10, 1);
        panel.setBackground(Color.ORANGE);
        drawCenteredText(g, "Player 1 : Enter your password", frame.getWidth() / 2,
                frame.getHeight() * 2 / 5, 50, Color.BLACK, "Arial");

        return passAttempt;
    }

    public String checkPlayer2Details(Graphics g) {
        String passAttempt = textField(g, frame.getWidth() / 2, frame.getHeight() / 2, 200, 30, Color.BLACK,
                Color.white, 10, 1);
        panel.setBackground(Color.ORANGE);
        drawCenteredText(g, "Player 2 : Enter your password", frame.getWidth() / 2,
                frame.getHeight() * 2 / 5, 50, Color.BLACK, "Arial");

        return passAttempt;
    }

    public void startMenu(Graphics g) {
        panel.setBackground(Color.ORANGE);
        drawCenteredText(g, "Battleship", frame.getWidth() / 2, 200, 100, Color.black, "Arial");
        if (mousePressed) {
            mouseReady = false;
            // menuMusicClip.stop();
            gameController.gameScreen = gameController.modeMenu;
        }
    }

    public void modeSelectMenu(Graphics g) {
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
        }
    }

    public String getPlayerDetails(Graphics g) {
        String player1Pass = textField(g, frame.getWidth() / 2, frame.getHeight() / 2, 200, 30, Color.BLACK,
                Color.white, 10, 1);

        panel.setBackground(Color.blue);
        drawCenteredText(g, "Player 1 Details", frame.getWidth() / 2, frame.getHeight() / 5, 100, Color.BLACK,
                "Arial");
        drawCenteredText(g, "Player 1 : Enter your password", frame.getWidth() / 2,
                frame.getHeight() * 2 / 5, 50, Color.BLACK, "Arial");

        drawCenteredText(g, "Password:", frame.getWidth() / 2 - 220, frame.getHeight() / 2 + 35, 40, Color.BLACK,
                "Arial");

        if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 150, 200, 125,
                "Ok",
                Color.BLACK, Color.WHITE, 40, 13) && mouseReady && !(player1Pass.equals(""))) {

            mouseReady = false;
            gameController.gameScreen = gameController.enterPlayer2Details;
        }
        return player1Pass;
    }

    public String getPlayer2Details(Graphics g) {
        String player2Pass = textField(g, frame.getWidth() / 2, frame.getHeight() / 2, 200, 30, Color.BLACK,
                Color.white, 10, 1);

        panel.setBackground(Color.blue);
        drawCenteredText(g, "Player 2 Details", frame.getWidth() / 2, frame.getHeight() / 5, 100, Color.BLACK,
                "Arial");
        drawCenteredText(g, "Player 2 : Enter your password", frame.getWidth() / 2,
                frame.getHeight() * 2 / 5, 50, Color.BLACK, "Arial");

        drawCenteredText(g, "Password:", frame.getWidth() / 2 - 220, frame.getHeight() / 2 + 35, 40, Color.BLACK,
                "Arial");

        if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 150, 200, 125,
                "Ok",
                Color.BLACK, Color.WHITE, 40, 13) && mouseReady && !(player2Pass.equals(""))) {

            mouseReady = false;
            gameController.gameScreen = gameController.checkPrePlayer1Details;
        }
        return player2Pass;
    }

    public void scoreboard(Graphics g, int x, int y, int width, int height, int score) {
        drawRect(g, x, y, width, height, Color.BLACK, true);
        drawCenteredText(g, "Score:", x, y, 20, Color.BLACK, "Arial");
        drawCenteredText(g, String.valueOf(score), x, y + 35, 20, Color.BLACK, "Arial");
    }

    public void print(Graphics g, String text) {
        drawCenteredText(g, text, frame.getWidth() / 2, 20, 10, Color.BLACK, "Arial");
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public String textField(Graphics g, int x, int y, int width, int height,
            Color backgroundColor, Color textColor, int textSize, int textFieldIndex) {

        int margin = width / 20;
        while (fieldFocused.size() <= 9) {
            fieldFocused.add(false); // Initialize with 'false' values
        }

        if (mousePressed
                && !(rectButton(g, x - width / 2, y - height / 2, width, height, "", backgroundColor, textColor,
                        textSize))) {
            for (int j = 0; j < fieldFocused.size(); j++) {
                fieldFocused.set(j, false);
            }
        }

        if (rectButton(g, x - width / 2, y - height / 2, width, height, "", backgroundColor, textColor, textSize)) {
            fieldFocused.set(textFieldIndex, true);
            textInput = "";
        }
        drawFillRect(g, x, y, width, height, backgroundColor, true);

        if (fieldFocused.get(textFieldIndex)) {
            g.setColor(textColor);
            Font font = new Font("Arial", Font.BOLD, textSize);
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics(font);

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

        return textInput;
    }

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
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - textHeight) / 2 + metrics.getAscent();
        g.drawString(label, textX, textY);
        return withinBoundaries(mouseX, mouseY, x, y, width, height) && mousePressed && mouseReady;
    }

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
        System.out.println(withinBoundaries(mouseX, mouseY, x, y, width, height) + " | " + mousePressed + " | " + mouseReady);
        return withinBoundaries(mouseX, mouseY, x, y, width, height) && mousePressed && mouseReady;
    }

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

    public void drawText(Graphics g, String text, int x, int y, int textSize, Color textColor, String textFont) {
        g.setColor(textColor);
        Font font = new Font("Arial", Font.BOLD, textSize);
        g.setFont(font);
        g.drawString(text, x, y);
    }

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

    public void drawImage(Graphics g, String imageName, int x, int y, int width, int height,
            boolean centerAroundPoint) {

        try {
            image = ImageIO.read(new File(imageName));
        } catch (Exception e) {
            System.out.println("NO IMAGE FOUND!");
        }

        if (centerAroundPoint) {
            g.drawImage(image, x - (width / 2), y - (height / 2), width, height, null);
        } else {
            g.drawImage(image, x, y, width, height, null);
        }

    }

    public void drawLine(Graphics g, int x1, int y1, int x2, int y2, int thickness, Color color) {
        g.setColor(color);
        ((Graphics2D) g).setStroke(new BasicStroke(20));
        g.drawLine(x1, y1, x2, y2);
    }

    public void drawFillRect(Graphics g, int x, int y, int width, int height, Color color,
            boolean centerAroundPoint) {
        g.setColor(color);
        if (centerAroundPoint) {
            g.fillRect(x - (width / 2), y - (height / 2), width, height);
        } else {
            g.fillRect(x, y, width, height);
        }

    }

    public void drawRect(Graphics g, int x, int y, int width, int height, Color color, boolean centerAroundPoint) {
        g.setColor(color);
        if (centerAroundPoint) {
            g.drawRect(x - (width / 2), y - (height / 2), width, height);
        } else {
            g.drawRect(x, y, width, height);
        }
    }

    public void drawFillCircle(Graphics g, int x, int y, int size, Color color, boolean centerAroundPoint) {
        g.setColor(color);
        if (centerAroundPoint) {
            g.fillOval(x - (size / 2), y - (size / 2), size, size);
        } else {
            g.fillOval(x, y, size, size);
        }
    }

    public void drawCircle(Graphics g, int x, int y, int size, Color color, boolean centerAroundPoint) {
        g.setColor(color);
        if (centerAroundPoint) {
            g.drawOval(x - (size / 2), y - (size / 2), size, size);
        } else {
            g.drawOval(x, y, size, size);
        }
    }

    private int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    private boolean withinBoundaries(int x, int y, int objectX, int objectY, int width, int height) {
        return x > objectX && x < objectX + width && y > objectY && y < objectY + height;
    }
}
