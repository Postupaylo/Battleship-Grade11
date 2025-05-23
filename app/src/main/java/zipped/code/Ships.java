package zipped.code;

import java.awt.Color;

public class Ships {

    class Destroyer {
        Color shipColor = Color.red;
        int shipLength = 2;
        boolean doRender = false;
        int[] xPositions = new int[shipLength];
        int[] yPositions = new int[shipLength];

        public Destroyer() {
            for (int x = 0; x > shipLength; x++) {
                xPositions[x] = 0;
                yPositions[x] = 0;
            }
        }

        public void setXPosition(int index, int position) {
            xPositions[index] = position;
        }

        public void setYPosition(int index, int position) {
            yPositions[index] = position;
        }

    }

    public class Submarine {
        Color shipColor = Color.white;
        int shipLength = 3;
        boolean doRender = false;
        int[] xPositions = new int[3];
        int[] yPositions = new int[3];

        public Submarine() {
            for (int x = 0; x > 2; x++) {
                xPositions[x] = 0;
                yPositions[x] = 0;
            }
        }

        public void setXPosition(int index, int position) {
            xPositions[index] = position;
        }

        public void setYPosition(int index, int position) {
            yPositions[index] = position;
        }

    }

    public class Cruiser {
        Color shipColor =  new Color(255,0,255);
        int shipLength = 3;
        boolean doRender = false;
        int[] xPositions = new int[3];
        int[] yPositions = new int[3];

        public Cruiser() {
            for (int x = 0; x > 2; x++) {
                xPositions[x] = 0;
                yPositions[x] = 0;
            }
        }

        public void setXPosition(int index, int position) {
            xPositions[index] = position;
        }

        public void setYPosition(int index, int position) {
            yPositions[index] = position;
        }

    }

    public class Battleship {
        Color shipColor = Color.green;
        int shipLength = 4;
        boolean doRender = false;
        int[] xPositions = new int[4];
        int[] yPositions = new int[4];

        public Battleship() {
            for (int x = 0; x > 2; x++) {
                xPositions[x] = 0;
                yPositions[x] = 0;
            }
        }

        public void setXPosition(int index, int position) {
            xPositions[index] = position;
        }

        public void setYPosition(int index, int position) {
            yPositions[index] = position;
        }

    }

    public class Carrier {
        Color shipColor = Color.yellow;
        int shipLength = 5;
        boolean doRender = false;
        int[] xPositions = new int[5];
        int[] yPositions = new int[5];

        public Carrier() {
            for (int x = 0; x > 2; x++) {
                xPositions[x] = 0;
                yPositions[x] = 0;
            }
        }

        public void setXPosition(int index, int position) {
            xPositions[index] = position;
        }

        public void setYPosition(int index, int position) {
            yPositions[index] = position;
        }

    }

}
