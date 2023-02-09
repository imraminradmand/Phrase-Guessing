import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class AccountHandler implements Runnable{
    private Socket socket;
    public AccountHandler(Socket socket){
        this.socket = socket;
    }

    /*
    * Main function
    *
    * The goal of this function is to handle the server requests by utilizing file IO.
    *
    *     The protocol that is implemented follows as:
    * GET <username> <password> - The return statement pass through the socket will either be the user's score or if the account does not exist
    *                            it will return !noaccount!
    * POST <username> <password> <score> - This will return !success! if it is successful, if it fails there is no output.
    *
    * */
    @Override
    public void run() {
        try {
            InputStreamReader socketInputReader = new InputStreamReader(socket.getInputStream());
            OutputStream socketOutStream = socket.getOutputStream();

            BufferedReader socketInput = new BufferedReader(socketInputReader);
            PrintWriter socketOutput = new PrintWriter(socketOutStream, true);

            while (true) {
                String clientResponse = socketInput.readLine();
                System.out.println(clientResponse);
                if (clientResponse != null) {
                    String[] clientArgs = clientResponse.split(" ");

                    if (clientArgs[0].equals("exit")){
                        break;
                    }

                    socketOutput.println(fileIO(clientArgs));
                }
            }
            socketOutput.close();
            socketInput.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    * METHOD: fileIO
    *
    * Purpose: The main function that controls thread syncrhonization for file IO (makes sure that only one thread can write or read at once).
    *
    * */
    private synchronized String fileIO(String[] clientArgs) throws IOException {
        if(clientArgs[0].equalsIgnoreCase("get")){
            return readFromFile(clientArgs);
        }else {
            return writeToFile(clientArgs);
        }
    }

    /*
    * METHOD: readFrmFile
    *
    * Purpose: The goal of the method is to provide capability to read from the file. This will return the data for a specified user account or it will
    *          return an invalid account.
    * */
    private String readFromFile(String[] clientArgs) throws FileNotFoundException {
        File myObj = new File("users.txt");
        Scanner myReader = new Scanner(myObj);
        String result = "!noaccount!";
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] arg = data.split(" ");

            if (arg[0].equals(clientArgs[1]) && arg[1].equals(clientArgs[2])) {
                result = data;
                break;
            }
        }
        myReader.close();
        return result;
    }

    /*
    * METHOD: writeToFile
    *
    * GOAL: The purpose of this file is to write an account and score to the file. The file is copied, deleted and recreated with the contents plus
    *       the specified client arguments.
    *
    * */
    private String writeToFile(String[] clientArgs) throws IOException {
        File file = new File("users.txt");
        Scanner myReader = new Scanner(file);
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

        if (file.delete()) {
            file.createNewFile();


            FileWriter writer = new FileWriter("users.txt");
            lines.add(clientArgs[1] + " " + clientArgs[2] + " " + clientArgs[3]);

            for (String s : lines) {
                writer.write(s + '\n');
            }
            writer.close();

            return ("!success!");
        } else {
            return ("!fail!");
        }
    }
}
