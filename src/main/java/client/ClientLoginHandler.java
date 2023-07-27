package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class ClientLoginHandler {

    private DataOutputStream toServer = null;
    private DataInputStream fromServer = null;

    public ClientLoginHandler(DataOutputStream toServer, DataInputStream fromServer) {
        this.toServer = toServer;
        this.fromServer = fromServer;
    }
    public boolean login() throws IOException {
        return handleLoggingIn();
    }

    private boolean handleLoggingIn() throws IOException {
        System.out.println(fromServer.readUTF());
        int loggedIn = 0;
        while (loggedIn == 0) {
            Scanner scan = new Scanner(System.in);
            System.out.println(fromServer.readUTF());
            String email = scan.next();
            toServer.writeUTF(email);

            System.out.println(fromServer.readUTF());
            String password = scan.next();
            toServer.writeUTF(password);

            loggedIn = fromServer.readInt();
            if (loggedIn == 0) {
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());
                System.out.println(fromServer.readUTF());

                int choice = scan.nextInt();
                toServer.writeInt(choice);
                if (choice == 2) return false;
            }
        }
        System.out.println(fromServer.readUTF());
        return true;
    }

}
