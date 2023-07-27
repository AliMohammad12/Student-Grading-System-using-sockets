package model;

public class Account {
    private int id;
    private String email;
    private String hashedPassword; // Store the hashed password as a byte array
    private String role;

    public Account(String email, String hashedPassword, String role) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
