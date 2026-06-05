import java.io.*;
import java.net.*;

public class MessagePrinter implements Runnable {

    private final Socket socket;
    // Volatile so the write from the Client thread is visible here immediately
    private volatile boolean running;

    public MessagePrinter(Socket socket) {
        this.socket = socket;
        this.running = true;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))) {

            while (running) {
                String msg = reader.readLine();

                // BUG FIX: null check MUST come before .equals() — if the server
                // closes the connection, readLine() returns null and calling
                // msg.equals() on it throws a NullPointerException.
                if (msg == null || msg.equals("bye")) {
                    running = false;
                    break;
                }

                System.out.println(msg);
            }

        } catch (IOException e) {
            if (running) {
                System.err.println("Connection lost: " + e.getMessage());
            }
        }
    }
}
