package user;

import java.util.Scanner;
import java.util.UUID;

public class Administrator extends User {

    public void run() {
    }

    public Administrator(UUID id, String name, Scanner s) {
        this.id = id;
        this.name = name;
        this.s = s;
    }

}
