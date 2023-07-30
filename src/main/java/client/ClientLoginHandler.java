package client;

import util.InputValidator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientLoginHandler {

    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;
    InputValidator inputValidator;

    public ClientLoginHandler(DataOutputStream toServer, DataInputStream fromServer) {
        this.toServer = toServer;
        this.fromServer = fromServer;
        inputValidator = new InputValidator();
    }
    public boolean login() throws IOException {
        return handleLoggingIn();
    }

    private boolean handleLoggingIn() throws IOException {
        System.out.println(fromServer.readUTF());
        int loggedIn = 0;
        while (loggedIn == 0) {
            System.out.println(fromServer.readUTF());
            String email = inputValidator.getValidString(new ArrayList<>() {{}});
          //  Scanner scan = new Scanner(System.in);
          //  String email = scan.next();
            toServer.writeUTF(email);

            System.out.println(fromServer.readUTF());

            String password = inputValidator.getValidString(new ArrayList<>() {{}});
            toServer.writeUTF(password);


            loggedIn = fromServer.readInt();
            if (loggedIn == 0) {
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());

                int choice = inputValidator.getValidInteger(1, 2);
                toServer.writeInt(choice);
                if (choice == 2) return false;

                System.out.println(loggedIn);
            }
        }
        System.out.println(fromServer.readUTF());
        return true;
    }

}
