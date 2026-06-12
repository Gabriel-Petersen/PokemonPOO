package game.loader;

import engine.assets.AssetManager;
import game.creature.Specie;
import game.creature.Stats;
import game.creature.move.ElementType;
import game.creature.move.Move;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class SpecieRegister {
    private static final String SPECIE_TXT_PATH = "registry/species.txt";
    private static final boolean FORCE_LOAD = true;

    private static final Map<Integer, Specie> specieIdMap = new HashMap<>();
    private static final Map<String, Specie> specieNameMap = new HashMap<>();
    
    public static Specie getSpecie(String name) { return specieNameMap.getOrDefault(name, null); }
    public static Specie getSpecie(Integer dexId) { return specieIdMap.getOrDefault(dexId, null); }

    static {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(SPECIE_TXT_PATH);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String line;
            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("//")) continue;
                
                Specie sp = loadSpecie(line.split(","));
                if (specieIdMap.containsKey(sp.getDexNumber())) {
                    throw new ParseException(
                        "Already indexed creature: " + sp.getName() + " / " + specieIdMap.get(sp.getDexNumber()).getName(),
                        0
                    );
                }
                specieIdMap.putIfAbsent(sp.getDexNumber(), sp);

                if (specieNameMap.containsKey(sp.getName())) {
                    throw new ParseException(
                        "Already named creature: " + sp.getDexNumber() + " / " + specieNameMap.get(sp.getName()).getDexNumber(),
                        0
                    );
                }
                specieNameMap.putIfAbsent(sp.getName(), sp);
            }
                
            stream.close();
        }
        catch (NumberFormatException e) {
            System.err.println("Erro ao parsear o stat de espécies em: " + SPECIE_TXT_PATH + "\nO erro é: " + e);
        }
        catch (IOException e) {
            System.err.println("Erro ao carregar o registro de espécies em: " + SPECIE_TXT_PATH + "\nO erro é: " + e);
        } 
        catch (ParseException e) {
            System.err.println("Erro ao parsear duas espécies de mesmo ID em: " + SPECIE_TXT_PATH + "\nO erro é: " + e);
        }
        catch (GameLoadingException e) {
            System.err.println("Erro ao parsear as espécies em: " + SPECIE_TXT_PATH + "\nO erro é: " + e);
        }
    }

    private static Specie loadSpecie(String[] traits) throws NumberFormatException, GameLoadingException
    {
        Stats stats = null;
        int id = -1;
        Integer evolutionID = null;
        String name, frPath, bkPath;
        name = frPath = bkPath = "";
        Map<Integer, Move> movePool = null;
        ElementType t1 = null, t2 = null;
        for (Integer i = 0; i < traits.length; i++)
        {
            if (i == SpecieData.HP.id()) {
                stats = parseStats(traits);
                i = SpecieData.SPEED.id();
            }
            else if (i == SpecieData.MOVEPOOL.id()) {
                movePool = new HashMap<>();
                i = MoveRegister.loadMovePool(traits, i, movePool, FORCE_LOAD);
            }
            else
            {
                switch (SpecieData.values()[i]) {
                    case ID -> id = Integer.parseInt(traits[i]);
                    case NAME -> name = traits[i];
                    case TYPE1 -> {
                        try { t1 = ElementType.valueOf(traits[i]); } 
                        catch (IllegalArgumentException | NullPointerException e) { t1 = null; }
                    }
                    case TYPE2 -> {
                        try { t2 = ElementType.valueOf(traits[i]); } 
                        catch (IllegalArgumentException | NullPointerException e) { t2 = null; }
                    }
                    case FR_SPRITE_PATH -> frPath = traits[i];
                    case BK_SPRITE_PATH -> bkPath = traits[i];
                    case EVOLUTION_ID -> {
                        try { evolutionID = Integer.valueOf(traits[i]); if (evolutionID == -1) evolutionID = null; } 
                        catch (NumberFormatException e) { evolutionID = null; }
                    } 
                    default -> throw GameLoadingException.specieError(name, id, "No data mapped for i == " + i);
                }
            }
        }

        Specie sp;
        if (t1 == null)
            throw GameLoadingException.specieError(name, id, "must have at least a valid primary type (Type1)");
        if (evolutionID == null)
            sp = new Specie(id, name, stats, t1, t2, AssetManager.getSprite(frPath), AssetManager.getSprite(bkPath));
        else
        {
            var evo = specieIdMap.get(evolutionID);
            if (evo == null)
                throw GameLoadingException.specieError(name, id, "is trying to evolve to unloaded specie of id=" + evolutionID);
            sp = new Specie(id, name, stats, t1, t2, evo, AssetManager.getSprite(frPath), AssetManager.getSprite(bkPath));
        }
            
        sp.loadMovepool(movePool);
        return sp;
    }

    private static Stats parseStats(String[] traits) throws NumberFormatException
    {
        int hpIndex = SpecieData.HP.id();
        int[] stats = new int[6];
        for (int i = hpIndex; i <= SpecieData.SPEED.id(); i++) {
            stats[i - hpIndex] = Integer.parseInt(traits[i]);
        }
        return new Stats(stats[0], stats[1], stats[2], stats[3], stats[4], stats[5]);
    }

    private static enum SpecieData {
        ID, NAME, TYPE1, TYPE2, HP, ATK, DEF, SPATK, SPDEF, SPEED, FR_SPRITE_PATH, BK_SPRITE_PATH, EVOLUTION_ID, MOVEPOOL;
        public int id() { return this.ordinal(); }
    }
}
