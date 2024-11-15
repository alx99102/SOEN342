package entities;

import java.util.UUID;

public class Availability {
    private final UUID id;
    private final UUID instructorId;
    private final String city;

    public Availability(UUID instructorId, String city) {
        this.id = UUID.randomUUID();
        this.instructorId = instructorId;
        this.city = city;
    }

    public Availability(UUID id, UUID instructorId, String city) {
        this.id = id;
        this.instructorId = instructorId;
        this.city = city;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getInstructorId() {
        return this.instructorId;
    }

    public String getCity() {
        return this.city;
    }
}
