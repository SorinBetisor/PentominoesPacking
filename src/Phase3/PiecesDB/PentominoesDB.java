package Phase3.PiecesDB;

/**
 * The PentominoesDB class represents a database of pentomino pieces.
 * It contains static fields that store the values and configurations of different pentomino pieces.
 * The values and configurations are stored as multi-dimensional arrays.
 * The class provides access to these values and configurations through its public static fields.
 */
public class PentominoesDB {
	public static int lValue = 3;
	public static int pValue = 4;
	public static int tValue = 5;
	public static int lVolume = 5;
	public static int pVolume = 5;
	public static int tVolume = 5;

	public static int[][][][] lPentInt = { // L int
			{ { { 1 }, { 0 } }, { { 1 }, { 0 } }, { { 1 }, { 0 } }, { { 1 }, { 1 } } },
			{ { { 0 }, { 1 } }, { { 0 }, { 1 } }, { { 0 }, { 1 } }, { { 1 }, { 1 } } },
			{ { { 1 }, { 1 } }, { { 1 }, { 0 } }, { { 1 }, { 0 } }, { { 1 }, { 0 } } },
			{ { { 1 }, { 1 } }, { { 0 }, { 1 } }, { { 0 }, { 1 } }, { { 0 }, { 1 } } },
			{ { { 1 }, { 1 }, { 1 }, { 1 } }, { { 1 }, { 0 }, { 0 }, { 0 } } },
			{ { { 1 }, { 1 }, { 1 }, { 1 } }, { { 0 }, { 0 }, { 0 }, { 1 } } },
			{ { { 1 }, { 0 }, { 0 }, { 0 } }, { { 1 }, { 1 }, { 1 }, { 1 } } },
			{ { { 0 }, { 0 }, { 0 }, { 1 } }, { { 1 }, { 1 }, { 1 }, { 1 } } },
			{ { { 1, 0 } }, { { 1, 0 } }, { { 1, 0 } }, { { 1, 1 } } },
			{ { { 0, 1 } }, { { 0, 1 } }, { { 0, 1 } }, { { 1, 1 } } },
			{ { { 1, 1 } }, { { 1, 0 } }, { { 1, 0 } }, { { 1, 0 } } },
			{ { { 1, 1 } }, { { 0, 1 } }, { { 0, 1 } }, { { 0, 1 } } },
			{ { { 1, 1 }, { 1, 0 }, { 1, 0 }, { 1, 0 } } },
			{ { { 0, 1 }, { 0, 1 }, { 0, 1 }, { 1, 1 } } },
			{ { { 1, 1 }, { 0, 1 }, { 0, 1 }, { 0, 1 } } },
			{ { { 1, 0 }, { 1, 0 }, { 1, 0 }, { 1, 1 } } },
			{ { { 1, 1, 1, 1 } }, { { 1, 0, 0, 0 } } },
			{ { { 1, 1, 1, 1 } }, { { 0, 0, 0, 1 } } },
			{ { { 1, 0, 0, 0 } }, { { 1, 1, 1, 1 } } },
			{ { { 0, 0, 0, 1 } }, { { 1, 1, 1, 1 } } },
			{ { { 1, 0, 0, 0 }, { 1, 1, 1, 1 } } },
			{ { { 1, 1, 1, 1 }, { 0, 0, 0, 1 } } },
			{ { { 1, 1, 1, 1 }, { 1, 0, 0, 0 } } },
			{ { { 0, 0, 0, 1 }, { 1, 1, 1, 1 } } }
	};

	public static int[][][][] pPentInt = { // P int
			{ { { 2 }, { 0 } }, { { 2 }, { 2 } }, { { 2 }, { 2 } } },
			{ { { 2 }, { 2 } }, { { 2 }, { 2 } }, { { 0 }, { 2 } } },
			{ { { 2 }, { 2 } }, { { 2 }, { 2 } }, { { 2 }, { 0 } } },
			{ { { 0 }, { 2 } }, { { 2 }, { 2 } }, { { 2 }, { 2 } } },
			{ { { 2 }, { 2 }, { 2 } }, { { 2 }, { 2 }, { 0 } } },
			{ { { 2 }, { 2 }, { 2 } }, { { 0 }, { 2 }, { 2 } } },
			{ { { 0 }, { 2 }, { 2 } }, { { 2 }, { 2 }, { 2 } } },
			{ { { 2 }, { 2 }, { 0 } }, { { 2 }, { 2 }, { 2 } } },
			{ { { 2, 0 } }, { { 2, 2 } }, { { 2, 2 } } },
			{ { { 0, 2 } }, { { 2, 2 } }, { { 2, 2 } } },
			{ { { 2, 2 } }, { { 2, 2 } }, { { 0, 2 } } },
			{ { { 2, 2 } }, { { 2, 2 } }, { { 2, 0 } } },
			{ { { 2, 2 }, { 2, 2 }, { 2, 0 } } },
			{ { { 0, 2 }, { 2, 2 }, { 2, 2 } } },
			{ { { 2, 2 }, { 2, 2 }, { 0, 2 } } },
			{ { { 2, 0 }, { 2, 2 }, { 2, 2 } } },
			{ { { 2, 2, 0 } }, { { 2, 2, 2 } } },
			{ { { 2, 2, 2 } }, { { 2, 2, 0 } } },
			{ { { 2, 2, 2 } }, { { 0, 2, 2 } } },
			{ { { 0, 2, 2 } }, { { 2, 2, 2 } } },
			{ { { 2, 2, 0 }, { 2, 2, 2 } } },
			{ { { 2, 2, 2 }, { 0, 2, 2 } } },
			{ { { 2, 2, 2 }, { 2, 2, 0 } } },
			{ { { 0, 2, 2 }, { 2, 2, 2 } } }
	};

	
	public static int[][][][] tPentInt = { // T int
			{ { { 3 }, { 0 }, { 0 } }, { { 3 }, { 3 }, { 3 } }, { { 3 }, { 0 }, { 0 } } },
			{ { { 3 }, { 3 }, { 3 } }, { { 0 }, { 3 }, { 0 } }, { { 0 }, { 3 }, { 0 } } },
			{ { { 0 }, { 0 }, { 3 } }, { { 3 }, { 3 }, { 3 } }, { { 0 }, { 0 }, { 3 } } },
			{ { { 0 }, { 3 }, { 0 } }, { { 0 }, { 3 }, { 0 } }, { { 3 }, { 3 }, { 3 } } },
			{ { { 3, 0, 0 } }, { { 3, 3, 3 } }, { { 3, 0, 0 } } },
			{ { { 0, 0, 3 } }, { { 3, 3, 3 } }, { { 0, 0, 3 } } },
			{ { { 0, 3, 0 } }, { { 0, 3, 0 } }, { { 3, 3, 3 } } },
			{ { { 3, 3, 3 } }, { { 0, 3, 0 } }, { { 0, 3, 0 } } },
			{ { { 3, 3, 3 }, { 0, 3, 0 }, { 0, 3, 0 } } },
			{ { { 3, 0, 0 }, { 3, 3, 3 }, { 3, 0, 0 } } },
			{ { { 0, 3, 0 }, { 0, 3, 0 }, { 3, 3, 3 } } },
			{ { { 0, 0, 3 }, { 3, 3, 3 }, { 0, 0, 3 } } }
	};

	public static boolean[][][][] tPentBool = { // T bool
			{ { { true }, { false }, { false } }, { { true }, { true }, { true } },
					{ { true }, { false }, { false } } },
			{ { { true }, { true }, { true } }, { { false }, { true }, { false } },
					{ { false }, { true }, { false } } },
			{ { { false }, { false }, { true } }, { { true }, { true }, { true } },
					{ { false }, { false }, { true } } },
			{ { { false }, { true }, { false } }, { { false }, { true }, { false } },
					{ { true }, { true }, { true } } },
			{ { { true, false, false } }, { { true, true, true } }, { { true, false, false } } },
			{ { { false, false, true } }, { { true, true, true } }, { { false, false, true } } },
			{ { { false, true, false } }, { { false, true, false } }, { { true, true, true } } },
			{ { { true, true, true } }, { { false, true, false } }, { { false, true, false } } },
			{ { { true, true, true }, { false, true, false }, { false, true, false } } },
			{ { { true, false, false }, { true, true, true }, { true, false, false } } },
			{ { { false, true, false }, { false, true, false }, { true, true, true } } },
			{ { { false, false, true }, { true, true, true }, { false, false, true } } }
	};

	public static boolean[][][][] lPentBool = { // L bool
			{ { { true }, { false } }, { { true }, { false } }, { { true }, { false } }, { { true }, { true } } },
			{ { { false }, { true } }, { { false }, { true } }, { { false }, { true } }, { { true }, { true } } },
			{ { { true }, { true } }, { { true }, { false } }, { { true }, { false } }, { { true }, { false } } },
			{ { { true }, { true } }, { { false }, { true } }, { { false }, { true } }, { { false }, { true } } },
			{ { { true }, { true }, { true }, { true } }, { { true }, { false }, { false }, { false } } },
			{ { { true }, { true }, { true }, { true } }, { { false }, { false }, { false }, { true } } },
			{ { { true }, { false }, { false }, { false } }, { { true }, { true }, { true }, { true } } },
			{ { { false }, { false }, { false }, { true } }, { { true }, { true }, { true }, { true } } },
			{ { { true, false } }, { { true, false } }, { { true, false } }, { { true, true } } },
			{ { { false, true } }, { { false, true } }, { { false, true } }, { { true, true } } },
			{ { { true, true } }, { { true, false } }, { { true, false } }, { { true, false } } },
			{ { { true, true } }, { { false, true } }, { { false, true } }, { { false, true } } },
			{ { { true, true }, { true, false }, { true, false }, { true, false } } },
			{ { { false, true }, { false, true }, { false, true }, { true, true } } },
			{ { { true, true }, { false, true }, { false, true }, { false, true } } },
			{ { { true, false }, { true, false }, { true, false }, { true, true } } },
			{ { { true, true, true, true } }, { { true, false, false, false } } },
			{ { { true, true, true, true } }, { { false, false, false, true } } },
			{ { { true, false, false, false } }, { { true, true, true, true } } },
			{ { { false, false, false, true } }, { { true, true, true, true } } },
			{ { { true, false, false, false }, { true, true, true, true } } },
			{ { { true, true, true, true }, { false, false, false, true } } },
			{ { { true, true, true, true }, { true, false, false, false } } },
			{ { { false, false, false, true }, { true, true, true, true } } }
	};

	public static boolean[][][][] pPentBool = { // P bool
			{ { { true }, { false } }, { { true }, { true } }, { { true }, { true } } },
			{ { { true }, { true } }, { { true }, { true } }, { { false }, { true } } },
			{ { { true }, { true } }, { { true }, { true } }, { { true }, { false } } },
			{ { { false }, { true } }, { { true }, { true } }, { { true }, { true } } },
			{ { { true }, { true }, { true } }, { { true }, { true }, { false } } },
			{ { { true }, { true }, { true } }, { { false }, { true }, { true } } },
			{ { { false }, { true }, { true } }, { { true }, { true }, { true } } },
			{ { { true }, { true }, { false } }, { { true }, { true }, { true } } },
			{ { { true, false } }, { { true, true } }, { { true, true } } },
			{ { { false, true } }, { { true, true } }, { { true, true } } },
			{ { { true, true } }, { { true, true } }, { { false, true } } },
			{ { { true, true } }, { { true, true } }, { { true, false } } },
			{ { { true, true }, { true, true }, { true, false } } },
			{ { { false, true }, { true, true }, { true, true } } },
			{ { { true, true }, { true, true }, { false, true } } },
			{ { { true, false }, { true, true }, { true, true } } },
			{ { { true, true, false } }, { { true, true, true } } },
			{ { { true, true, true } }, { { true, true, false } } },
			{ { { true, true, true } }, { { false, true, true } } },
			{ { { false, true, true } }, { { true, true, true } } },
			{ { { true, true, false }, { true, true, true } } },
			{ { { true, true, true }, { false, true, true } } },
			{ { { true, true, true }, { true, true, false } } },
			{ { { false, true, true }, { true, true, true } } }
	};
}
