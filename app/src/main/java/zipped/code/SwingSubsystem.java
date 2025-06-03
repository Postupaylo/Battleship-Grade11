package zipped.code;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

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
    int score;
    boolean keyPressed;
    boolean keyReady;
    boolean mousePressed;
    boolean mouseReady;
    boolean cellPressed;
    boolean switchBoat;
    String gameScreen = "startScreen";
    String shipMode = "carrier";
    String shipDirection = "up";
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
    final String playerDetailsScreen = "playerDetailsScreen";
    final String Player2DetailsScreen = "player2DetailsScreen";

    public SwingSubsystem() {
        destroyer = ships.new Destroyer();
        submarine = ships.new Submarine();
        cruiser = ships.new Cruiser();
        battleship = ships.new Battleship();
        carrier = ships.new Carrier();

        try {
            grug = ImageIO.read(new File("grug.jpg"));
        } catch (Exception e) {
            System.out.println("NO IMAGE FOUND!");
        }

        frame = new JFrame("grug simulator");
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
                mousePressed = false; // Mouse is released
                cellPressed = false;
                panel.repaint();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
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
        switch (gameScreen) {
            case "startScreen":
                startMenu(g);
                break;
            case "modeSelectScreen":
                modeSelectMenu(g);
                break;
            case playerDetailsScreen:
                getPlayerDetails(g);
                break;
            case Player2DetailsScreen:
                getPlayer2Details(g);
                break;
            case "playerScreen":
                drawGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10, 10, Color.black, Color.black,
                        true);
                shipSelector(g, frame.getWidth() / 2 + 700 / 2 + 250 / 2 + 30, frame.getHeight() / 2, 250, 500);
                scoreboard(g, frame.getWidth() / 2 - 700 / 2 - 180 / 2 - 50, frame.getHeight() / 2 - 700 / 2 + 100 / 2,
                        180, 100, score);
                break;
            case "compScreen":
                compMenu(g);
                break;
        }
        panel.repaint();
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
    // region Draw Grid
    public void drawGrid(Graphics g, int x, int y, int width, int height, int xCell, int yCell, int lineThickness,
            Color lineColor, Color cellColor, boolean doHoverEffect) {
        for (int xIndex = 0; xIndex < xCell; xIndex++) {
            for (int yIndex = 0; yIndex < yCell; yIndex++) {
                g.setColor(cellColor);
                ((Graphics2D) g).setStroke(new BasicStroke(5));
                g.drawRect((x - (width / 2) - xCell + (xIndex * (width / xCell))),
                        (y - (height / 2) - yCell + (yIndex * (height / yCell))), width / xCell, height / yCell);
                if (destroyer.doRender) {
                    for (int destroyerIndex = 0; destroyerIndex < destroyer.shipLength; destroyerIndex++) {
                        g.setColor(destroyer.shipColor);
                        g.fillRect((x - (width / 2) - xCell + (destroyer.xPositions[destroyerIndex] * (width / xCell))),
                                (y - (height / 2) - yCell + (destroyer.yPositions[destroyerIndex] * (height / yCell))),
                                width / xCell,
                                height / yCell);

                    }
                }
                if (submarine.doRender) {
                    for (int submarineIndex = 0; submarineIndex < submarine.shipLength; submarineIndex++) {
                        g.setColor(submarine.shipColor);
                        g.fillRect((x - (width / 2) - xCell + (submarine.xPositions[submarineIndex] * (width / xCell))),
                                (y - (height / 2) - yCell + (submarine.yPositions[submarineIndex] * (height / yCell))),
                                width / xCell,
                                height / yCell);
                    }
                }
                if (cruiser.doRender) {
                    for (int cruiserIndex = 0; cruiserIndex < cruiser.shipLength; cruiserIndex++) {
                        g.setColor(cruiser.shipColor);
                        g.fillRect((x - (width / 2) - xCell + (cruiser.xPositions[cruiserIndex] * (width / xCell))),
                                (y - (height / 2) - yCell + (cruiser.yPositions[cruiserIndex] * (height / yCell))),
                                width / xCell,
                                height / yCell);
                    }
                }
                if (battleship.doRender) {
                    for (int battleshipIndex = 0; battleshipIndex < battleship.shipLength; battleshipIndex++) {
                        g.setColor(battleship.shipColor);
                        g.fillRect(
                                (x - (width / 2) - xCell + (battleship.xPositions[battleshipIndex] * (width / xCell))),
                                (y - (height / 2) - yCell
                                        + (battleship.yPositions[battleshipIndex] * (height / yCell))),
                                width / xCell,
                                height / yCell);
                    }
                }
                if (carrier.doRender) {
                    for (int carrierIndex = 0; carrierIndex < carrier.shipLength; carrierIndex++) {
                        g.setColor(carrier.shipColor);
                        g.fillRect((x - (width / 2) - xCell + (carrier.xPositions[carrierIndex] * (width / xCell))),
                                (y - (height / 2) - yCell + (carrier.yPositions[carrierIndex] * (height / yCell))),
                                width / xCell,
                                height / yCell);
                    }
                }
                if (mouseX > (x - (width / 2) - xCell + (xIndex * (width / xCell)))
                        && mouseX < (x - (width / 2) - xCell + (xIndex * (width / xCell))) + width / xCell) {
                    if (mouseY > (y - (height / 2) - yCell + (yIndex * (height / yCell)))
                            && mouseY < (y - (height / 2) - yCell + (yIndex * (height / yCell))) + height / yCell) {
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
    public void drawSecondGrid(Graphics g, int x, int y, int width, int height, int xCell, int yCell, int lineThickness,
            Color lineColor, Color cellColor, boolean doHoverEffect) {
        for (int xIndex = 0; xIndex < xCell; xIndex++) {
            for (int yIndex = 0; yIndex < yCell; yIndex++) {
                g.setColor(cellColor);
                ((Graphics2D) g).setStroke(new BasicStroke(5));
                g.drawRect((x - (width / 2) - xCell + (xIndex * (width / xCell))),
                        (y - (height / 2) - yCell + (yIndex * (height / yCell))), width / xCell, height / yCell);
                if (mouseX > (x - (width / 2) - xCell + (xIndex * (width / xCell)))
                        && mouseX < (x - (width / 2) - xCell + (xIndex * (width / xCell))) + width / xCell) {
                    if (mouseY > (y - (height / 2) - yCell + (yIndex * (height / yCell)))
                            && mouseY < (y - (height / 2) - yCell + (yIndex * (height / yCell))) + height / yCell) {
                        gridX = xIndex;
                        gridY = yIndex;
                        if (rectButton(g, (x - (width / 2) - xCell + (xIndex * (width / xCell))),
                                (y - (height / 2) - yCell + (yIndex * (height / yCell))), width / xCell,
                                height / yCell,
                                "Click me!", cellColor, lineColor, 7)) {

                        }
                    }
                }
            }
        }
    }
    // endregion

    public void print(Graphics g, String text) {
        drawCenteredText(g, text, frame.getWidth() / 2, 20, 10, Color.BLACK, "Arial");
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void drawCross(int x, int y) {

    }

    /**
     * A rectangluar button that returns its state, pressed or not
     *
     * @param g           is the graphics of the panel
     * @param x           being the x position
     * @param y           being the y position
     * @param width       being the width of the button
     * @param height      being the height of the button
     * @param label       is the text located inside the button
     * @param buttonColor is the color of the button, using the Color class
     * @param textColor   is the color of the text, using the Color class
     */
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
        return withinBoundaries(mouseX, mouseY, x, y, width, height) && mousePressed;
    }

    public boolean roundedRectButton(Graphics g, int x, int y, int width, int height, String label, Color buttonColor,
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
        return withinBoundaries(mouseX, mouseY, x, y, width, height) && mousePressed;
    }

    public boolean circleButton(Graphics g, int x, int y, int size, String label, Color buttonColor, Color textColor,
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

    public void drawFillRect(Graphics g, int x, int y, int width, int height, Color color, boolean centerAroundPoint) {
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

    public void startMenu(Graphics g) {
        panel.setBackground(Color.ORANGE);
        drawCenteredText(g, "Battleship", frame.getWidth() / 2, 200, 100, Color.black, "Arial");
        if (mousePressed && mouseReady) {
            mouseReady = false;
            gameScreen = "modeSelectScreen";
        }
        if (!mousePressed) {
            mouseReady = true;
        }
    }

    public void modeSelectMenu(Graphics g) {
        panel.setBackground(Color.blue);
        drawCenteredText(g, "Select Game Mode", frame.getWidth() / 2, frame.getHeight() / 3, 100, Color.BLACK, "Arial");
        if (roundedRectButton(g, frame.getWidth() / 2 - 200 / 2 - 200, frame.getHeight() / 2 - 125 / 2 + 75, 200, 125,
                "Player",
                Color.BLACK, Color.WHITE, 40, 13) && mouseReady) 
        {
            mouseReady = false;
            gameScreen = playerDetailsScreen;
        }
        if (!roundedRectButton(g, gridX, mouseY, mouseX, keyIndex, Player2DetailsScreen, null, null, gridY, gridX))
        {
            mouseReady = true;
        }
        if (roundedRectButton(g, frame.getWidth() / 2 - 200 / 2 + 200, frame.getHeight() / 2 - 125 / 2 + 75, 200, 125,
                "Comp",
                Color.BLACK, Color.WHITE, 40, 13)) {
            gameScreen = "compScreen";
        }
        if (roundedRectButton(g, 25, 25, 70, 35, "Back", Color.BLACK, Color.WHITE, 18, 13)) {
            gameScreen = "startScreen";
        }
    }

    public void getPlayerDetails(Graphics g) {
        panel.setBackground(Color.blue);
        drawCenteredText(g, "Player 1 Details", frame.getWidth() / 2, frame.getHeight() / 5, 100, Color.BLACK, "Arial");
        drawCenteredText(g, "Player 1 : Enter your name and a new password", frame.getWidth() / 2,
                frame.getHeight() * 2 / 5, 50, Color.BLACK, "Arial");
        if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 150, 200, 125,
                "Ok",
                Color.BLACK, Color.WHITE, 40, 13)) {

            gameScreen = Player2DetailsScreen;
            getPlayer2Details(g);
        }

    }

    public void getPlayer2Details(Graphics g) {
        panel.setBackground(Color.blue);
        drawCenteredText(g, "Player 2 Details", frame.getWidth() / 2, frame.getHeight() / 5, 100, Color.BLACK, "Arial");
        drawCenteredText(g, "Player 2 : Enter your name and a new password", frame.getWidth() / 2,
                frame.getHeight() * 2 / 5, 50, Color.BLACK, "Arial");
        if (roundedRectButton(g, frame.getWidth() - 300, frame.getHeight() - 300, 200, 125,
                "Ok",
                Color.BLACK, Color.WHITE, 40, 13)) {
            gameScreen = "playerScreen";
        }
    }

    public void compMenu(Graphics g) {
        panel.setBackground(Color.RED);
        if (roundedRectButton(g, 25, 25, 70, 35, "Back", Color.BLACK, Color.WHITE, 18, 13)) {
            gameScreen = "modeSelectScreen";
        }
    }

    public void scoreboard(Graphics g, int x, int y, int width, int height, int score) {
        drawRect(g, x, y, width, height, Color.BLACK, true);
        drawCenteredText(g, "Score:", x, y, 20, Color.BLACK, "Arial");
        drawCenteredText(g, String.valueOf(score), x, y + 35, 20, Color.BLACK, "Arial");
        if (roundedRectButton(g, 25, 25, 70, 35, "Back", Color.BLACK, Color.WHITE, 18, 13)) {
            gameScreen = "modeSelectScreen";
        }
    }
}
