package model;

public class SessionManager {

    // Singleton instance → only one session manager exists
    private static SessionManager instance;

    // Encapsulation: currentUser is private and can only be accessed through methods
    private User currentUser;

    // Private constructor → prevents direct instantiation from outside
    private SessionManager() {}

    // Get the single instance (Singleton pattern)
    // Abstraction: other code just calls getInstance() without worrying about creation
    public static SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    // Getter for current user
    // Encapsulation: controlled access to the private field
    public User getCurrentUser() { 
        return currentUser; 
    }

    // Setter for current user
    // Encapsulation: controlled way to update the session
    public void setCurrentUser(User user) { 
        this.currentUser = user; 
    }

    // Clear session → logout functionality
    // Abstraction: other code just calls clearSession() to remove user info
    public void clearSession() { 
        this.currentUser = null; 
    }
}