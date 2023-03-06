import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameHandlerInterface extends Remote {
    public String startGame(
            String player,
            int number_of_words,
            int failed_attempt_factor
    ) throws RemoteException;
    public String guessLetter(String player,char letter) throws RemoteException;
    public String guessPhrase(String player, String phrase) throws IOException;
    public String endGame(String player) throws RemoteException;
    public String restartGame(String player) throws RemoteException;
    public boolean addWord(String word) throws RemoteException;
    public boolean removeWord(String word) throws RemoteException;
    public boolean checkWord(String word) throws RemoteException;

    public boolean login(String username, String password) throws RemoteException;
    public boolean register(String username, String password) throws RemoteException;

    public String getScore(String username, String password) throws RemoteException;

}
