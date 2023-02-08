import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.exit(1);
    }

    int port = 0;
    ServerSocket serverSocket;

    try {
      port = Integer.parseInt(args[0]);
      serverSocket = new ServerSocket(5555);
      System.out.println("Server is running...");
      ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);

      while (true) {
        fixedThreadPool.execute(new ClientHandler(serverSocket.accept()));
      }
    } catch (IOException e) {
      System.out.println(
              "Exception caught when trying to listen on port " + port + " or listening for a connection");
      System.out.println(e.getMessage());
    }
  }
}
