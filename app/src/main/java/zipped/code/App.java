package zipped.code;

import java.awt.Color;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    SwingSubsystem swing = new SwingSubsystem();



    public static void main(String[] args) {
        System.out.println(new App().getGreeting());
       // new App().swing.drawGrid(null, 0, 0, 0, 0, 0, 0, 0, null, null, false, false);
        //new App().swing.rectButton(new App().swing.getGraphics(), 0, 0, 0, 0, null, null, null, 0);
        // if(new App().swing.rectButton(50, 50, 50, 50, "Click me", Color.RED, Color.black, 10)){
        //     System.out.println("test");
        // }
        
    }









}
