import java.util.Scanner;
import java.util.UUID;

import user.*;

void main() {
    User u;

    Scanner scanner = new Scanner(System.in); 
    userloop:
    do {
        println("Please enter your user id to sign in, or 'new' to create a new user.");
        while(!scanner.hasNext());
        String in = scanner.next();
        if(in.equalsIgnoreCase("new")) {
            println("Enter the role for your new user: \n"+
            "'A': Administrator\n"+
            "'I': Intructor");

            while(!scanner.hasNext());
            char role = scanner.next().charAt(0);
            
            switch (role) {
                case 'A' -> {
                    println("Enter your name:");
                    scanner.next();
                    while(!scanner.hasNextLine());
                    String name = scanner.nextLine();
                    u = new Administrator(UUID.randomUUID(), name, scanner);
                    break userloop;
                }
                case 'I' -> {
                    println("Enter your name:");
                    scanner.next();
                    while(!scanner.hasNextLine());
                    String name = scanner.nextLine();
                    u = new Instructor(UUID.randomUUID(), name, scanner);
                    break userloop;
                }
                default -> {
                    println("Invalid role code. Please try again.");
                }
            }
        }
        else {
            println("not implemented");
            System.exit(0);
        }
    } while (true);

    u.run();
}
