package chat.view;

import chat.model.Message;
import chat.controller.RemoteController;
import chat.model.MessageObserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class TextView extends UnicastRemoteObject implements MessageObserver, RemoteTextView {
    private static final String QUIT_COMMAND = ":q";
    String username; // To avoid using the User model class

    private final RemoteController remoteController;
    private final Scanner input;

    public TextView(RemoteController remoteController) throws RemoteException {
        this.remoteController = remoteController;
        this.input = new Scanner(System.in);
    }


    public void run() throws RemoteException {
        chooseUsernamePhase(input);
        messagingPhase(input);
        logoutPhase();
    }

    /**
     *
     *
     * PHASES ------------------------------------------------------------------------
     *
     *
     */

    public void chooseUsernamePhase(Scanner input) throws RemoteException {
        do {
            System.out.println("Choose username:");
            username = input.nextLine();
            if (!username.isEmpty()) {
                try {
                    remoteController.login(username, this, this);
                } catch (RemoteException e) {
                    System.err.println("Username already in use, choose a different one");
                    username = "";
                }
            }
        } while (username.isEmpty());
    }

    public void chooseGroupPhase(Scanner input) throws RemoteException {
        String groupName;
        do {
            System.out.println("Choose group:");
            groupName = input.nextLine();
            if (!groupName.isEmpty()) {
                //TODO implement function
            }
        } while (groupName.isEmpty());
    }

    public void messagingPhase(Scanner input) throws RemoteException {
        String text;
        Message message;
        System.out.println("Welcome!");
        do {
            text = input.nextLine();
            if (!text.equals(QUIT_COMMAND))
                message = remoteController.sendMessage(text, username);
        } while (!text.equals(QUIT_COMMAND));
    }

    public void logoutPhase() throws RemoteException {
        remoteController.logout(username, this);
    }

    /**
     *
     *
     * PHASES ------------------------------------------------------------------------
     *
     *
     */

    public void displayText(String text) {
        System.out.println(">>>>" + text);
    }

    @Override
    public void onNewUserJoined(String usernameJoined) throws RemoteException {
        System.out.println(usernameJoined + " joined the group");
    }

    @Override
    public void onLeave(String usernameLeft) throws RemoteException {
        System.out.println(usernameLeft + " left");
    }

    @Override
    public void onNewMessage(Message message) throws RemoteException {
        System.out.print(">>>" + message.getSender().getUsername() + ":");
        System.out.println(message.getMessage());
    }

    @Override
    public void error(String message) throws RemoteException {
        System.err.println(">>>" + message);
    }

    @Override
    public void ack(String message) throws RemoteException {
        System.out.println(">>>" + message);
    }
}
