package game.loader;

import game.creature.move.DamageMove;
import game.creature.move.ElementType;
import game.creature.move.Move;
import game.creature.move.MoveCategory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class MoveRegister 
{
    private static final String MOVE_TXT_PATH = "registry/moves.txt";
    private static final Move FALLBACK_MOVE = new DamageMove(-1, "DUMMY", 0, 0.0, 0, ElementType.NONE, MoveCategory.SPECIAL);

    private static final Map<Integer, Move> movesById = new HashMap<>();
    private static final Map<String, Move> movesByName = new HashMap<>();

    public static Move getMove(String name) { return movesByName.getOrDefault(name, FALLBACK_MOVE); }
    public static Move getMove(int id) { return movesById.getOrDefault(id, FALLBACK_MOVE); }

    static {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(MOVE_TXT_PATH);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) continue;
                
                
                Move mv = loadMove(line.split(","));
                if (movesById.containsKey(mv.getId())) {
                    throw new ParseException(
                        "Already indexed move: " + mv.getName() + " / " + movesById.get(mv.getId()).getName(),
                        0
                    );
                }
                movesById.put(mv.getId(), mv);

                if (movesByName.containsKey(mv.getName())) {
                    throw new ParseException(
                        "Already named creature: " + mv.getId() + " / " + movesByName.get(mv.getName()).getId(),
                        0
                    );
                }
                movesByName.putIfAbsent(mv.getName(), mv);
            }
                
            stream.close();
        }
        catch (NumberFormatException e) {
            System.err.println("Erro de formatação numérica parsear o carregamento do Move em: " + MOVE_TXT_PATH + "\nO erro é: " + e);
        }
        catch (IOException e) {
            System.err.println("Erro ao carregar o registro de moves em: " + MOVE_TXT_PATH + "\nO erro é: " + e);
        }
        catch (ParseException e) {
            System.err.println("Erro ao parsear dois moves de mesmo ID em: " + MOVE_TXT_PATH + "\nO erro é: " + e);
        }
        catch (GameLoadingException e) {
            System.err.println("Erro ao parsear as espécies em: " + MOVE_TXT_PATH + "\nO erro é: " + e);
        }
    }

    private static Move loadMove(String[] traits) throws GameLoadingException 
    {
        if (traits.length < MoveData.values().length) {
            throw new GameLoadingException("Linha de movimento incompleta ou corrompida.");
        }

        int id = Integer.parseInt(traits[MoveData.ID.id()].trim());
        String name = traits[MoveData.NAME.id()].trim();
        int power = Integer.parseInt(traits[MoveData.POWER.id()].trim());
        double accuracy = Double.parseDouble(traits[MoveData.ACCURACY.id()].trim());
        int priority = Integer.parseInt(traits[MoveData.PRIORITY.id()].trim());
        
        ElementType elementType;
        try {
            elementType = ElementType.valueOf(traits[MoveData.ELEMENT_TYPE.id()].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw GameLoadingException.moveError(name, id, "Tipo elemental inválido: " + traits[MoveData.ELEMENT_TYPE.id()]);
        }

        MoveCategory category;
        try {
            category = MoveCategory.valueOf(traits[MoveData.CATEGORY.id()].trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw GameLoadingException.moveError(name, id, "Categoria de movimento inválida: " + traits[MoveData.CATEGORY.id()]);
        }

        if (category == MoveCategory.STATUS) {
            throw GameLoadingException.moveError(
                name, 
                id, 
                "Falha ao carregar: Movimentos de STATUS ainda não foram implementados no motor de batalha."
            );
        }

        return new DamageMove(id, name, power, accuracy, priority, elementType, category);
    }

    // Expecting: Name:String,Level:int or Id:int,Level:int
    public static Map<Integer, Move> loadMovePool(String[] traits, Integer currI, boolean forceLoad) throws GameLoadingException
    {
        if (traits.length % 2 != 0) {
            throw new GameLoadingException("Erro ao carregar a movepool. Nem todo move está pareado a um level (quantidade ímpar de dados)");
        }

        Map<Integer, Move> pool = new HashMap<>();
        for (int i = currI; i < traits.length - 1; i += 2) 
        {
            var t1 = traits[i];
            int level = Integer.parseInt(traits[i+1]);

            int id = -1;
            try { id = Integer.parseInt(t1); } catch (NumberFormatException e) { }

            if (id == -1)
            {
                Move mv = getMove(t1);
                if (forceLoad && mv == FALLBACK_MOVE) 
                    throw GameLoadingException.moveError(t1, id, "Move with name=" + t1 + " were not loaded and tryed to be put in a MovePool");
                pool.put(level, mv);
            }
            else
            {
                Move mv = getMove(id);
                if (forceLoad && mv == FALLBACK_MOVE) 
                    throw GameLoadingException.moveError("no-name-found", id, "Move with id=" + id + " were not loaded and tryed to be put in a MovePool");
                pool.put(level, mv);
            }
        }

        return pool;
    }

    private static enum MoveData {
        ID, NAME, POWER, ACCURACY, PRIORITY, ELEMENT_TYPE, CATEGORY;
        public int id() { return this.ordinal(); }
    }
}
