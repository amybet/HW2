package entityClasses;

// Represents a Post in the system
public class Post {
    private String id;
    private String content;
    private boolean isDeleted;

    // Constructor
    public Post(String id, String content) {
        this.id = id;
        this.content = content;
        this.isDeleted = false; // Post is initially not deleted
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void delete() {
        this.isDeleted = true; // Mark the post as deleted
    }
    
    // Additional CRUD methods can be implemented here
}