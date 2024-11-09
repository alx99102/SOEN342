package user;

import java.util.UUID;
import java.util.Scanner;

public abstract class User {
    public UUID id;
    public String name;
    public Scanner s;

    public abstract void run();
}