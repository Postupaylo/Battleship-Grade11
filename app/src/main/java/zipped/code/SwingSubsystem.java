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
    boolean keyPressed;
    boolean keyReady;
    boolean mousePressed;
    boolean cellPressed;
    String shipMode = "carrier";
    String shipDirection = "up";
    Ships.Destroyer destroyer;
    Ships.Submarine submarine;
    Ships.Cruiser cruiser;
    Ships.Battleship battleship;
    Ships.Carrier carrier;

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
        drawGrid(g, frame.getWidth() / 2, frame.getHeight() / 2, 700, 700, 10, 10, 10, Color.black, Color.black, true);
        shipSelector(g, frame.getWidth() - 300 + 50,
                frame.getHeight() / 2, 300, 600);
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
                            cellPressed = true;
                            shipMode = "noBoatSelected";

                        }
                    }
                }
            }
        }
    }

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
        if (roundedRectButton(g, x - 200 / 2, (y - (y/5)) + y/2, 200, y/5, "destroyer", Color.BLACK, destroyer.shipColor, 25, 10)) {
            shipMode = "destroyer";
            destroyer.doRender = true;
        }
        if (roundedRectButton(g, x - 200 / 2, (y - (y/5) * 2) + y/2, 200, y/5, "submarine", Color.BLACK, submarine.shipColor, 25, 10)) {
            shipMode = "submarine";
            submarine.doRender = true;
        }
        if (roundedRectButton(g, x - 200 / 2, (y - (y/5) * 3) + y/2, 200, y/5, "cruiser", Color.BLACK, cruiser.shipColor, 25, 10)) {
            shipMode = "cruiser";
            cruiser.doRender = true;
        }
        if (roundedRectButton(g, x - 200 / 2, (y - (y/5) * 4) + y/2, 200, y/5, "battleship", Color.BLACK, battleship.shipColor, 25, 10)) {
            shipMode = "battleship";
            battleship.doRender = true;
        }
        if (roundedRectButton(g, x - 200 / 2, (y - (y/5) * 5) + y/2, 200, y/5, "carrier", Color.BLACK, carrier.shipColor, 25, 10)) {
            shipMode = "carrier";
            carrier.doRender = true;
        }

    }

    public void print(Graphics g, String text) {
        drawCenteredText(g, text, frame.getWidth() / 2, 20, 10, Color.BLACK, "Arial");
    }

    public void drawShip() {
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
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

}
