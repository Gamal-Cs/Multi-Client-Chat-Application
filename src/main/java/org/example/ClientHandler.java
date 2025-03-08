package org.example;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClientHandler implements Runnable {

    public static CopyOnWriteArrayList<ClientHandler> clientHandlers = new CopyOnWriteArrayList<>(); // Store all the client handlers
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String clientUserName;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = reader.readLine(); // Read the client's username
            clientHandlers.add(this); // Add this client handler to the list
            broadcastMessage(this.clientUserName + " has joined the chat"); // Notify other clients
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (!socket.isClosed()) {
            try {
                messageFromClient = reader.readLine(); // Read messages from the client
                broadcastMessage(messageFromClient); // Broadcast the message to all clients
            } catch (IOException e) {
                closeEverything(socket, reader, writer);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        clientHandlers.forEach(clientHandler -> {
            try {
                if (!clientHandler.clientUserName.equals(this.clientUserName)) { // Exclude the sender
                    clientHandler.writer.write(messageToSend);
                    clientHandler.writer.newLine();
                    clientHandler.writer.flush();
                }
            } catch (IOException e) {
                closeEverything(clientHandler.socket, clientHandler.reader, clientHandler.writer);
            }
        });
    }

    public void removeClientHandler() {
        clientHandlers.remove(this); // Remove this client handler from the list
        broadcastMessage(this.clientUserName + " has left the chat"); // Notify other clients
    }

    public void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        removeClientHandler(); // Remove the client handler and notify others
        try {
            if (socket != null) socket.close();
            if (reader != null) reader.close();
            if (writer != null) writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}