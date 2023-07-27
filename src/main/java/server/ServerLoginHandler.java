package server;

import model.Account;
import service.AccountService;
import util.PasswordHasher;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerLoginHandler {
    private AccountService accountService;
    private DataInputStream inputFromClient = null;
    private DataOutputStream outputToClient = null;

    public ServerLoginHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    public Account login() throws IOException {
        return handleLoggingIn();
    }

    private Account handleLoggingIn() throws IOException {
        outputToClient.writeUTF("[Server] Signing In!");
        boolean loggedIn = false;
        Account account = null  ;
        while (!loggedIn) {
            outputToClient.writeUTF("- Please enter your email: ");
            String email = inputFromClient.readUTF();
            outputToClient.writeUTF("- Please enter your password: ");
            String password = inputFromClient.readUTF();

            boolean found = accountService.emailExists(email);
            boolean success = false;
            if (found) {
                account = accountService.getAccountByEmail(email);
                if (PasswordHasher.verifyPassword(account.getHashedPassword(), password)) {
                    success = true;
                    loggedIn = true;
                }
            }
            outputToClient.writeInt(success ? 1 : 0);
            if (!success){
                outputToClient.writeUTF("[Server] Wrong login credential!");
                outputToClient.writeUTF("- Press 1 to try again.");
                outputToClient.writeUTF("- Press 2 to go back.");

                int choice = inputFromClient.readInt();
                if (choice == 2) return null;
            }
        }
        outputToClient.writeUTF("[Server] You have been logged in successfully");
        return account;
    }

    public void setInputFromClient(DataInputStream inputFromClient) {
        this.inputFromClient = inputFromClient;
    }

    public void setOutputToClient(DataOutputStream outputToClient) {
        this.outputToClient = outputToClient;
    }
}
