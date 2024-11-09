package user;

import java.util.Scanner;
import java.util.UUID;

public class Instructor extends User {
    private String phoneNumber;

    public Instructor(UUID id, String name, Scanner s) {
        this.id = id;
        this.name = name;
        this.s = s;
    }

    public void run() {
        // TODO: query from database to see if new user or existing
        System.out.print("Enter phone number to complete registration: ");
        while (!this.s.hasNext()) {
        }
        this.phoneNumber = this.s.next();

        // TODO: lesson types
    }
}
