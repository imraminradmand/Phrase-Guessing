import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountsService {

    public static void main(String[] args) throws IOException {
      //  if (args.length != 1) {
      //      System.exit(1);
       // }

        int port = 0;
        ServerSocket serverSocket;

        try {
           // port = Integer.parseInt(args[0]);
            serverSocket = new ServerSocket(7777);

            System.out.println("Server is running...");

            while (true) {
                Socket socket = serverSocket.accept();

                InputStreamReader socketInputReader = new InputStreamReader(socket.getInputStream());
                OutputStream socketOutStream = socket.getOutputStream();

                BufferedReader socketInput = new BufferedReader(socketInputReader);
                PrintWriter socketOutput = new PrintWriter(socketOutStream, true);



                while (true) {
                    try {
                        String clientResponse = socketInput.readLine();

                        if (clientResponse != null) {
                            String[] clientArgs = clientResponse.split(" ");

                            //PROTOCOL: <get> <username> <password>
                            //          <post> <username> <score>
                            File myObj = new File("users.txt");
                            Scanner myReader = new Scanner(myObj);
                            if (clientArgs[0].equalsIgnoreCase("get")) {
                                while (myReader.hasNextLine()) {
                                    String data = myReader.nextLine();
                                    String[] arg = data.split(" ");

                                    if (arg[0].equals(clientArgs[1])) {

                                        socketOutput.println(data);
                                        break;
                                    }
                                }
                                myReader.close();
                            } else if (clientArgs[0].equalsIgnoreCase("insert")) {
                                ArrayList<String> lines = new ArrayList<>();
                                while (myReader.hasNextLine()) {
                                    String data = myReader.nextLine();
                                    lines.add(data);
                                }
                                myReader.close();

                                for (String line : lines) {
                                    String[] arg = line.split(" ");
                                    if (arg[0].equals(clientArgs[1])) {
                                        lines.remove(line);
                                        break;
                                    }
                                }

                                File file = new File("users.txt");

                                if (file.delete()) {
                                    file.createNewFile();
                                }

                                FileWriter writer = new FileWriter("users.txt");
                                lines.add(clientResponse);

                                for (String s : lines) {
                                    writer.write(s + '\n');
                                }
                                writer.close();
                            }
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                //socketInput.close();
                //socketOutput.close();
                //socket.close();
            }
        } catch (IOException e) {
            System.out.println(
                    "Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

}
