package user;

import java.util.UUID;
import java.util.Scanner;

public class Public extends User {

    public void run() {

    }

    public Public(UUID id, String name, Scanner s) {
        this.id = id;
        this.name = name;
        this.s = s;
    }

}
