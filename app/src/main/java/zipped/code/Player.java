package zipped.code;

public class Player {
    public String name;
    public String password;

    public Player() {}

    public Player(String name, String password) {
        this.name = name;
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean ValidNameAndPassword() {
        
        if (password == null || password.isEmpty()) {
            return false;
            //throw new IllegalArgumentException("The password cannot be null or empty.");
        }
        if (password.length() < 1 || password.length() > 50) {
            return false;
            //throw new IllegalArgumentException("The password must be between 5 and 10 characters long.");
        }

        if (name == null || name.isEmpty()) {
            return false;
            //throw new IllegalArgumentException("The name cannot be null or empty.");
        }
        if (name.length() < 1 || name.length() > 50) {
            return false;
            //throw new IllegalArgumentException("The name must be between 3 and 15 characters long.");
        }
        return true;
        
        
    }
}