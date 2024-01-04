package Phase3.PiecesDB;

public class PentominoesDB {
    public static int[][][][] tPentInt = { // T int
		{{{2}, {0}, {0}}, {{2}, {2}, {2}}, {{2}, {0}, {0}}},
		{{{2}, {2}, {2}}, {{0}, {2}, {0}}, {{0}, {2}, {0}}},
		{{{0}, {0}, {2}}, {{2}, {2}, {2}}, {{0}, {0}, {2}}},
		{{{0}, {2}, {0}}, {{0}, {2}, {0}}, {{2}, {2}, {2}}},
		{{{2, 0, 0}}, {{2, 2, 2}}, {{2, 0, 0}}},
		{{{0, 0, 2}}, {{2, 2, 2}}, {{0, 0, 2}}},
		{{{0, 2, 0}}, {{0, 2, 0}}, {{2, 2, 2}}},
		{{{2, 2, 2}}, {{0, 2, 0}}, {{0, 2, 0}}},
		{{{2, 2, 2}, {0, 2, 0}, {0, 2, 0}}},
		{{{2, 0, 0}, {2, 2, 2}, {2, 0, 0}}},
		{{{0, 2, 0}, {0, 2, 0}, {2, 2, 2}}},
		{{{0, 0, 2}, {2, 2, 2}, {0, 0, 2}}}
	};
    
    public static int[][][][] lPentInt = { // L int
		{{{3}, {0}}, {{3}, {0}}, {{3}, {0}}, {{3}, {3}}},
		{{{0}, {3}}, {{0}, {3}}, {{0}, {3}}, {{3}, {3}}},
		{{{3}, {3}}, {{3}, {0}}, {{3}, {0}}, {{3}, {0}}},
		{{{3}, {3}}, {{0}, {3}}, {{0}, {3}}, {{0}, {3}}},
		{{{3}, {3}, {3}, {3}}, {{3}, {0}, {0}, {0}}},
		{{{3}, {3}, {3}, {3}}, {{0}, {0}, {0}, {3}}},
		{{{3}, {0}, {0}, {0}}, {{3}, {3}, {3}, {3}}},
		{{{0}, {0}, {0}, {3}}, {{3}, {3}, {3}, {3}}},
		{{{3, 0}}, {{3, 0}}, {{3, 0}}, {{3, 3}}},
		{{{0, 3}}, {{0, 3}}, {{0, 3}}, {{3, 3}}},
		{{{3, 3}}, {{3, 0}}, {{3, 0}}, {{3, 0}}},
		{{{3, 3}}, {{0, 3}}, {{0, 3}}, {{0, 3}}},
		{{{3, 3}, {3, 0}, {3, 0}, {3, 0}}},
		{{{0, 3}, {0, 3}, {0, 3}, {3, 3}}},
		{{{3, 3}, {0, 3}, {0, 3}, {0, 3}}},
		{{{3, 0}, {3, 0}, {3, 0}, {3, 3}}},
		{{{3, 3, 3, 3}}, {{3, 0, 0, 0}}},
		{{{3, 3, 3, 3}}, {{0, 0, 0, 3}}},
		{{{3, 0, 0, 0}}, {{3, 3, 3, 3}}},
		{{{0, 0, 0, 3}}, {{3, 3, 3, 3}}},
		{{{3, 0, 0, 0}, {3, 3, 3, 3}}},
		{{{3, 3, 3, 3}, {0, 0, 0, 3}}},
		{{{3, 3, 3, 3}, {3, 0, 0, 0}}},
		{{{0, 0, 0, 3}, {3, 3, 3, 3}}}
	};
    
    public static int[][][][] pPentInt = { // P int
		{{{1}, {0}}, {{1}, {1}}, {{1}, {1}}},
		{{{1}, {1}}, {{1}, {1}}, {{0}, {1}}},
		{{{1}, {1}}, {{1}, {1}}, {{1}, {0}}},		
		{{{0}, {1}}, {{1}, {1}}, {{1}, {1}}},
		{{{1}, {1}, {1}}, {{1}, {1}, {0}}},
		{{{1}, {1}, {1}}, {{0}, {1}, {1}}},
		{{{0}, {1}, {1}}, {{1}, {1}, {1}}},
		{{{1}, {1}, {0}}, {{1}, {1}, {1}}},
		{{{1, 0}}, {{1, 1}}, {{1, 1}}},
		{{{0, 1}}, {{1, 1}}, {{1, 1}}},
		{{{1, 1}}, {{1, 1}}, {{0, 1}}},
		{{{1, 1}}, {{1, 1}}, {{1, 0}}},
		{{{1, 1}, {1, 1}, {1, 0}}},
		{{{0, 1}, {1, 1}, {1, 1}}},
		{{{1, 1}, {1, 1}, {0, 1}}},
		{{{1, 0}, {1, 1}, {1, 1}}},
		{{{1, 1, 0}}, {{1, 1, 1}}},
		{{{1, 1, 1}}, {{1, 1, 0}}},
		{{{1, 1, 1}}, {{0, 1, 1}}},
		{{{0, 1, 1}}, {{1, 1, 1}}},
		{{{1, 1, 0}, {1, 1, 1}}},
		{{{1, 1, 1}, {0, 1, 1}}},
		{{{1, 1, 1}, {1, 1, 0}}},
		{{{0, 1, 1}, {1, 1, 1}}}
	};

	public static boolean[][][][] tPentBool = { // T bool
		{{{true}, {false}, {false}}, {{true}, {true}, {true}}, {{true}, {false}, {false}}},
		{{{true}, {true}, {true}}, {{false}, {true}, {false}}, {{false}, {true}, {false}}},
		{{{false}, {false}, {true}}, {{true}, {true}, {true}}, {{false}, {false}, {true}}},
		{{{false}, {true}, {false}}, {{false}, {true}, {false}}, {{true}, {true}, {true}}},
		{{{true, false, false}}, {{true, true, true}}, {{true, false, false}}},
		{{{false, false, true}}, {{true, true, true}}, {{false, false, true}}},
		{{{false, true, false}}, {{false, true, false}}, {{true, true, true}}},
		{{{true, true, true}}, {{false, true, false}}, {{false, true, false}}},
		{{{true, true, true}, {false, true, false}, {false, true, false}}},
		{{{true, false, false}, {true, true, true}, {true, false, false}}},
		{{{false, true, false}, {false, true, false}, {true, true, true}}},
		{{{false, false, true}, {true, true, true}, {false, false, true}}}
	};
    
    public static boolean[][][][] lPentBool = { // L bool
		{{{true}, {false}}, {{true}, {false}}, {{true}, {false}}, {{true}, {true}}},
		{{{false}, {true}}, {{false}, {true}}, {{false}, {true}}, {{true}, {true}}},
		{{{true}, {true}}, {{true}, {false}}, {{true}, {false}}, {{true}, {false}}},
		{{{true}, {true}}, {{false}, {true}}, {{false}, {true}}, {{false}, {true}}},
		{{{true}, {true}, {true}, {true}}, {{true}, {false}, {false}, {false}}},
		{{{true}, {true}, {true}, {true}}, {{false}, {false}, {false}, {true}}},
		{{{true}, {false}, {false}, {false}}, {{true}, {true}, {true}, {true}}},
		{{{false}, {false}, {false}, {true}}, {{true}, {true}, {true}, {true}}},
		{{{true, false}}, {{true, false}}, {{true, false}}, {{true, true}}},
		{{{false, true}}, {{false, true}}, {{false, true}}, {{true, true}}},
		{{{true, true}}, {{true, false}}, {{true, false}}, {{true, false}}},
		{{{true, true}}, {{false, true}}, {{false, true}}, {{false, true}}},
		{{{true, true}, {true, false}, {true, false}, {true, false}}},
		{{{false, true}, {false, true}, {false, true}, {true, true}}},
		{{{true, true}, {false, true}, {false, true}, {false, true}}},
		{{{true, false}, {true, false}, {true, false}, {true, true}}},
		{{{true, true, true, true}}, {{true, false, false, false}}},
		{{{true, true, true, true}}, {{false, false, false, true}}},
		{{{true, false, false, false}}, {{true, true, true, true}}},
		{{{false, false, false, true}}, {{true, true, true, true}}},
		{{{true, false, false, false}, {true, true, true, true}}},
		{{{true, true, true, true}, {false, false, false, true}}},
		{{{true, true, true, true}, {true, false, false, false}}},
		{{{false, false, false, true}, {true, true, true, true}}}
	};
    
    public static boolean[][][][] pPentBool = { // P bool
		{{{true}, {false}}, {{true}, {true}}, {{true}, {true}}},
		{{{true}, {true}}, {{true}, {true}}, {{false}, {true}}},
		{{{true}, {true}}, {{true}, {true}}, {{true}, {false}}},		
		{{{false}, {true}}, {{true}, {true}}, {{true}, {true}}},
		{{{true}, {true}, {true}}, {{true}, {true}, {false}}},
		{{{true}, {true}, {true}}, {{false}, {true}, {true}}},
		{{{false}, {true}, {true}}, {{true}, {true}, {true}}},
		{{{true}, {true}, {false}}, {{true}, {true}, {true}}},
		{{{true, false}}, {{true, true}}, {{true, true}}},
		{{{false, true}}, {{true, true}}, {{true, true}}},
		{{{true, true}}, {{true, true}}, {{false, true}}},
		{{{true, true}}, {{true, true}}, {{true, false}}},
		{{{true, true}, {true, true}, {true, false}}},
		{{{false, true}, {true, true}, {true, true}}},
		{{{true, true}, {true, true}, {false, true}}},
		{{{true, false}, {true, true}, {true, true}}},
		{{{true, true, false}}, {{true, true, true}}},
		{{{true, true, true}}, {{true, true, false}}},
		{{{true, true, true}}, {{false, true, true}}},
		{{{false, true, true}}, {{true, true, true}}},
		{{{true, true, false}, {true, true, true}}},
		{{{true, true, true}, {false, true, true}}},
		{{{true, true, true}, {true, true, false}}},
		{{{false, true, true}, {true, true, true}}}
	};
}
