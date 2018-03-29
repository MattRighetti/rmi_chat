package chat.controller;

import chat.model.Message;
import chat.model.MessageObserver;
import chat.model.User;
import chat.view.RemoteTextView;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteController extends Remote {
    void login(String username, RemoteTextView remoteTextView, MessageObserver messageObserver) throws RemoteException;

    void logout(String username, RemoteTextView remoteTextView) throws RemoteException;

    Message sendMessage(String message, String username) throws RemoteException;
}