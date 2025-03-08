package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String clientUserName;

    public Client(Socket socket, String clientUserName) {
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUserName = clientUserName;
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }

    private void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
        try {
            if (socket != null) socket.close();
            if (reader != null) reader.close();
            if (writer != null) writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        try {
            writer.write(clientUserName); // Send the username to the server
            writer.newLine();
            writer.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                writer.write(clientUserName + ": " + messageToSend); // Send the message to the server
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }
    }

    public void listenForMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        msgFromGroupChat = reader.readLine(); // Read messages from the server
                        if (msgFromGroupChat == null) {
                            System.out.println("Disconnected from the server.");
                            closeEverything(socket, reader, writer);
                            System.exit(0);
                        }
                        System.out.println(msgFromGroupChat);
                    } catch (IOException e) {
                        closeEverything(socket, reader, writer);
                        break;
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username For the group chat: ");
        String username = scanner.nextLine();
        Socket socket1 = new Socket("localhost", 4999);
        Client client = new Client(socket1, username);
        client.listenForMessages();
        client.sendMessage();
    }
}