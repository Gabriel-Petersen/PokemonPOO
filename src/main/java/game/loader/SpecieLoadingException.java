package game.loader;

public class SpecieLoadingException extends Exception {
    public SpecieLoadingException(String name, int id, String message) { 
        super("Specie " + name + " of id=" + id + " " + message); 
    } 
}
