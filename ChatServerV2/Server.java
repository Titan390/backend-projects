import java.net.*;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    // CopyOnWriteArrayList is thread-safe: multiple ClientHandler threads call
    // broadcast() concurrently while the accept loop adds new entries.
    private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    /**
     * Broadcasts a message to every connected client except the sender.
     * Also removes any handler whose socket has gone away.
     */
    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client == sender) continue;
            client.send(message);
        }
    }

    /** Called by a ClientHandler when its client disconnects. */
    public void removeClient(ClientHandler handler) {
        clients.remove(handler);
        System.out.println("Client disconnected. Active clients: " + clients.size());
    }

    public void startServer() throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(7777)) {
            System.out.println("Server started on port 7777");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());

                ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);
                handler.send("Welcome! " + clients.size() + " client(s) online.");

                new Thread(handler).start();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().startServer();
    }
}
