package game.capturing;

import game.loader.SpecieRegister;

public class ETRegistry
{
    public static final EncounterTable TABLE_0;
    public static final EncounterTable TABLE_1;
    public static final EncounterTable TABLE_2;
    public static final EncounterTable TABLE_3;

    static {
        TABLE_0 = EncounterTable.Builder.create()
                .addSpecie(SpecieRegister.getSpecie(19), 2, 3, 4)
                .addSpecie(SpecieRegister.getSpecie(66), 1, 2, 4)
                .addSpecie(SpecieRegister.getSpecie(35), 1, 3, 5)
                .build();

        TABLE_1 = EncounterTable.Builder.create()
                .addSpecie(SpecieRegister.getSpecie(25), 2, 8, 12)
                .addSpecie(SpecieRegister.getSpecie(74), 2, 7, 11)
                .addSpecie(SpecieRegister.getSpecie(19), 3, 8, 11)
                .build();

        TABLE_2 = EncounterTable.Builder.create()
                .addSpecie(SpecieRegister.getSpecie(2), 2, 14, 19)
                .addSpecie(SpecieRegister.getSpecie(5), 2, 14, 19)
                .addSpecie(SpecieRegister.getSpecie(8), 2, 14, 19)
                .build();

        TABLE_3 = EncounterTable.Builder.create()
                .addSpecie(SpecieRegister.getSpecie(76), 2, 20, 26)
                .addSpecie(SpecieRegister.getSpecie(26), 2, 20, 26)
                .addSpecie(SpecieRegister.getSpecie(36), 2, 20, 26)
                .build();
    }
}
