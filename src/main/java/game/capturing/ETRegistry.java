package game.capturing;

import game.loader.SpecieRegister;

public class ETRegistry
{
    public static final EncounterTable TABLE_0;
//    public static final EncounterTable TABLE_1;
//    public static final EncounterTable TABLE_2;

    static {
        TABLE_0 = EncounterTable.Builder.create()
                .addSpecie(SpecieRegister.getSpecie(19), 1, 3, 5)
                .addSpecie(SpecieRegister.getSpecie(1), 3, 4, 6)
                .addSpecie(SpecieRegister.getSpecie(25), 2, 4, 6)
                .build();

//        TABLE_1 = EncounterTable.Builder.create().build();
//
//        TABLE_2 = EncounterTable.Builder.create().build();
    }
}
