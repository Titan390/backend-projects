import java.io.*;
import java.net.*;

public class Client {

    public void startClient() throws IOException {
        try (Socket socket = new Socket("localhost", 7777);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");

            MessagePrinter msgPrinter = new MessagePrinter(socket);
            Thread printerThread = new Thread(msgPrinter);
            printerThread.setDaemon(true); // dies automatically when main thread exits
            printerThread.start();

            String message;
            while ((message = console.readLine()) != null) {
                writer.println(message);

                if (message.equals("bye")) {
                    msgPrinter.stop();
                    printerThread.interrupt();
                    break;
                }
            }

        }
        // try-with-resources closes socket, writer, and console automatically
        System.out.println("Disconnected.");
    }

    public static void main(String[] args) throws IOException {
        new Client().startClient();
    }
}
