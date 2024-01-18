package Phase3.PiecesDB;

/**
 * The ParcelDB class represents a database of parcel information.
 * It contains static variables for storing values and arrays for different parcel types.
 * The class provides access to the parcel data through public static fields.
 */
public class ParcelDB {
    public static int aValue = 3;
    public static int bValue = 4;
    public static int cValue = 5;
    public static int aVolume = 16;
    public static int bVolume = 24;
    public static int cVolume = 27;

    // A Int
    public static int[][][][] aRotInt = {
            { { { 1, 1, 1, 1 }, { 1, 1, 1, 1 } }, { { 1, 1, 1, 1 }, { 1, 1, 1, 1 } } },
            { { { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 } }, { { 1, 1 }, { 1, 1 }, { 1, 1 }, { 1, 1 } } },
            { { { 1, 1 }, { 1, 1 } }, { { 1, 1 }, { 1, 1 } }, { { 1, 1 }, { 1, 1 } }, { { 1, 1 }, { 1, 1 } } }
    };

    // B Int
    public static int[][][][] bRotInt = {
            { { { 2, 2 }, { 2, 2 }, { 2, 2 } }, { { 2, 2 }, { 2, 2 }, { 2, 2 } }, { { 2, 2 }, { 2, 2 }, { 2, 2 } },
                    { { 2, 2 }, { 2, 2 }, { 2, 2 } } },
            { { { 2, 2 }, { 2, 2 }, { 2, 2 }, { 2, 2 } }, { { 2, 2 }, { 2, 2 }, { 2, 2 }, { 2, 2 } },
                    { { 2, 2 }, { 2, 2 }, { 2, 2 }, { 2, 2 } } },
            { { { 2, 2, 2 }, { 2, 2, 2 } }, { { 2, 2, 2 }, { 2, 2, 2 } }, { { 2, 2, 2 }, { 2, 2, 2 } },
                    { { 2, 2, 2 }, { 2, 2, 2 } } },
            { { { 2, 2, 2 }, { 2, 2, 2 }, { 2, 2, 2 }, { 2, 2, 2 } },
                    { { 2, 2, 2 }, { 2, 2, 2 }, { 2, 2, 2 }, { 2, 2, 2 } } },
            { { { 2, 2, 2, 2 }, { 2, 2, 2, 2 } }, { { 2, 2, 2, 2 }, { 2, 2, 2, 2 } },
                    { { 2, 2, 2, 2 }, { 2, 2, 2, 2 } } },
            { { { 2, 2, 2, 2 }, { 2, 2, 2, 2 }, { 2, 2, 2, 2 } }, { { 2, 2, 2, 2 }, { 2, 2, 2, 2 }, { 2, 2, 2, 2 } } }
    };

    // C Int
    public static int[][][][] cRotInt = {
            { { { 3, 3, 3 }, { 3, 3, 3 }, { 3, 3, 3 } }, { { 3, 3, 3 }, { 3, 3, 3 }, { 3, 3, 3 } },
                    { { 3, 3, 3 }, { 3, 3, 3 }, { 3, 3, 3 } } }
    };

    // A bool
    public static boolean[][][][] aRotBool = {
            { { { true, true, true, true }, { true, true, true, true } },
                    { { true, true, true, true }, { true, true, true, true } } },
            { { { true, true }, { true, true }, { true, true }, { true, true } },
                    { { true, true }, { true, true }, { true, true }, { true, true } } },
            { { { true, true }, { true, true } }, { { true, true }, { true, true } },
                    { { true, true }, { true, true } }, { { true, true }, { true, true } } }
    };

    // B bool
    public static boolean[][][][] bRotBool = {
            { { { true, true }, { true, true }, { true, true } }, { { true, true }, { true, true }, { true, true } },
                    { { true, true }, { true, true }, { true, true } },
                    { { true, true }, { true, true }, { true, true } } },
            { { { true, true }, { true, true }, { true, true }, { true, true } },
                    { { true, true }, { true, true }, { true, true }, { true, true } },
                    { { true, true }, { true, true }, { true, true }, { true, true } } },
            { { { true, true, true }, { true, true, true } }, { { true, true, true }, { true, true, true } },
                    { { true, true, true }, { true, true, true } }, { { true, true, true }, { true, true, true } } },
            { { { true, true, true }, { true, true, true }, { true, true, true }, { true, true, true } },
                    { { true, true, true }, { true, true, true }, { true, true, true }, { true, true, true } } },
            { { { true, true, true, true }, { true, true, true, true } },
                    { { true, true, true, true }, { true, true, true, true } },
                    { { true, true, true, true }, { true, true, true, true } } },
            { { { true, true, true, true }, { true, true, true, true }, { true, true, true, true } },
                    { { true, true, true, true }, { true, true, true, true }, { true, true, true, true } } }
    };

    // C bool
    public static boolean[][][][] cRotBool = {
            { { { true, true, true }, { true, true, true }, { true, true, true } },
                    { { true, true, true }, { true, true, true }, { true, true, true } },
                    { { true, true, true }, { true, true, true }, { true, true, true } } }
    };

    public static boolean[][][][][] boolParcelDB = { aRotBool, bRotBool, cRotBool };
    public static int[][][][][] intParcelDB = { aRotInt, bRotInt, cRotInt };
}
