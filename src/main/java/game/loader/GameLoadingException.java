package game.loader;

public class GameLoadingException extends Exception {
    public GameLoadingException(String message) { 
        super(message); 
    }

    public static GameLoadingException specieError(String name, int id, String message) {
        return new GameLoadingException("Specie " + name + " of id=" + id + " " + message);
    }

    public static GameLoadingException moveError(String name, int id, String message) {
        return new GameLoadingException("Move " + name + " of id=" + id + " " + message);
    }
}
