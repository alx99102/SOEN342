import java.util.Scanner;
import java.util.UUID;
import user.*;

public class Main {
    public static void main(String[] args) {
        User u;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Please enter your user id to sign in, or 'new' to create a new user.");
            while (!scanner.hasNext()) {
            }
            String in = scanner.next();
            if (in.equalsIgnoreCase("new")) {
                System.out.println("Enter the role for your new user: \n" +
                        "'A': Administrator\n" +
                        "'I': Instructor");

                while (!scanner.hasNext()) {
                }
                char role = scanner.next().charAt(0);

                switch (role) {
                    case 'A':
                        System.out.println("Enter your name:");
                        scanner.next();
                        while (!scanner.hasNextLine()) {
                        }
                        String adminName = scanner.nextLine();
                        u = new Administrator(UUID.randomUUID(), adminName, scanner);
                        u.run();
                        return;

                    case 'I':
                        System.out.println("Enter your name:");
                        scanner.next();
                        while (!scanner.hasNextLine()) {
                        }
                        String instructorName = scanner.nextLine();
                        u = new Instructor(UUID.randomUUID(), instructorName, scanner);
                        u.run();
                        return;

                    default:
                        System.out.println("Invalid role code. Please try again.");
                        break;
                }
            } else {
                System.out.println("not implemented");
                System.exit(0);
            }
        }
    }
}
