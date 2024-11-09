package entities;

import java.util.UUID;

public class Offering {
    private UUID id;
    private String location;
    private String lessonType;
    private boolean isPrivate;
    private String startTime;
    private String endTime;
    private String date;
    private String status; //"Available", "Not Available", "Cancelled", .....
    private boolean availToPublic;

    public Offering(String location, String lessonType, boolean isPrivate, String startTime, String endTime, String date) {
        this.id = UUID.randomUUID();
        this.location = location;
        this.lessonType = lessonType;
        this.isPrivate = isPrivate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.status = "Available";
        this.availToPublic = true;
    }

    public Offering(String id, String location, String lessonType, boolean isPrivate, String startTime, String endTime, String date, String status, boolean availToPublic) {
        this.id = UUID.fromString(id);
        this.location = location;
        this.lessonType = lessonType;
        this.isPrivate = isPrivate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.status = status;
        this.availToPublic = availToPublic;
    }

    // Getters
    public UUID getId() {
        return this.id;
    }

    public String getLocation() {
        return this.location;
    }

    public String getLessonType() {
        return this.lessonType;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public String getDate() {
        return this.date;
    }

    public String getStatus() {
        return this.status;
    }

    public boolean isAvailToPublic() {
        return this.availToPublic;
    }

    // Setters
    public void setLocation(String location) {
        this.location = location;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAvailToPublic(boolean availToPublic) {
        this.availToPublic = availToPublic;
    }

    @Override
    public String toString() {
        return "Offering {" +
                "ID: " + this.id +
                ", Location: " + this.location +
                ", Lesson Type: " + this.lessonType +
                ", Is Private: " + this.isPrivate +
                ", Start Time: " + this.startTime +
                ", End Time: " + this.endTime +
                ", Date: " + this.date +
                ", Status: " + this.status +
                ", Available to Public: " + this.availToPublic +
                '}';
    }
}

