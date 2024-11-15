package entities;

import user.User;

import java.util.UUID;

public class Booking {
    private final UUID id;
    private final Offering offering;
    private final User client;


    public Booking(UUID id, Offering offering, User client) {
        this.id = id;
        this.offering = offering;
        this.client = client;
    }

    public UUID getId() {
        return id;
    }

    public Offering getOffering() {
        return offering;
    }

    public User getClient() {
        return client;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", offering=" + offering +
                ", client=" + client +
                '}';
    }
}
