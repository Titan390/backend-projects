import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final PrintWriter writer;
    private final Server server;
    private boolean open;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.open = true;
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    /** Sends a message to this specific client. */
    public void send(String message) {
        writer.println(message);
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))) {

            while (open) {
                String message = reader.readLine();

                // null means the client closed the connection abruptly
                if (message == null || message.equals("bye")) {
                    open = false;
                    server.broadcast("A client has left the chat.", this);
                    server.removeClient(this);
                    break; // BUG FIX: was falling through to broadcast a second time
                }

                server.broadcast(message, this);
            }

        } catch (IOException e) {
            System.err.println("ClientHandler error: " + e.getMessage());
            server.removeClient(this);
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}
