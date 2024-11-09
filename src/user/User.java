package user;

import java.util.UUID;

public abstract class User {
    protected UUID id;
    protected String name;

    public abstract void run();

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract String getRole();
}
