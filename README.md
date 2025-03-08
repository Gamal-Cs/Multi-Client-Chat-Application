# Multi-Client Chat Application

A simple multi-client chat application built using **Java Sockets**. This application allows multiple clients to connect to a server and communicate with each other in real-time.

---

## Features
- **Multi-Client Support**: Multiple clients can connect to the server simultaneously.
- **Real-Time Messaging**: Messages are broadcast to all connected clients in real-time.
- **Username Identification**: Each client is identified by a unique username.
- **Graceful Disconnection**: Clients are notified when a user leaves the chat.
- **Thread-Safe Design**: Uses `CopyOnWriteArrayList` to handle concurrent modifications safely.

---

## Technologies Used
- **Java**: Core programming language.
- **Java Sockets**: For network communication.
- **Multithreading**: To handle multiple clients concurrently.

---

## How It Works
1. The **Server** listens for incoming client connections on a specified port.
2. Each **Client** connects to the server and provides a username.
3. Messages sent by a client are broadcast to all other connected clients.
4. When a client disconnects, the server notifies all other clients.

---

## Setup and Usage

### Prerequisites
- **Java Development Kit (JDK)**: Ensure you have JDK 8 or later installed.
- **IDE**: Any Java IDE (e.g., IntelliJ IDEA, Eclipse) or a terminal.

---

### Steps to Run the Application

#### 1. Clone the Repository
```bash
git clone https://github.com/your-username/multi-client-chat-app.git
cd multi-client-chat-app
