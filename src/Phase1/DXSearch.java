package Phase1;
public class DXSearch {
    private static final char[] INPUT = Search.INPUT;
    private static final int HORIZONTAL_GRID_SIZE = Search.HORIZONTAL_GRID_SIZE;
    private static final int VERTICAL_GRID_SIZE = Search.VERTICAL_GRID_SIZE;

    public static void main(String[] args) {
        DancingLinks dance = new DancingLinks(60);
        int nr = 0;
        for (int i = 0; i < INPUT.length; i++) {
            int pentID = characterToID(INPUT[i]);
            for (int mutation = 0; mutation < PentominoDatabase.data[pentID].length; mutation++) {
                int[][] pieceToPlace = PentominoDatabase.data[pentID][mutation];
                for (int x = 0; x < HORIZONTAL_GRID_SIZE; x++) {
                    for (int y = 0; y < VERTICAL_GRID_SIZE; y++) {
                        boolean can = true;

                        for (int j = 0; j < pieceToPlace.length && can; j++) {
                            if (y + pieceToPlace[j].length > VERTICAL_GRID_SIZE || y+pieceToPlace[j].length < 0)
                                can = false;
                            if (x+pieceToPlace.length > HORIZONTAL_GRID_SIZE || x+pieceToPlace.length < 0)
                                can=false;
                        }

                        if(!can) {continue;}
                        dance.AddRow(nr, new int[] { pentID, mutation,x,y });
                        System.out.println(nr+" "+pentID + " " + mutation + " "+x+" "+y); //y?
                        nr++;
                    }
                }
            }
        }
        dance.algorithmX(0);
        // System.out.println(15125125);

    }

    public static int characterToID(char character) {
		int pentID = -1;
		if (character == 'X') {
			pentID = 0;
		} else if (character == 'I') {
			pentID = 1;
		} else if (character == 'Z') {
			pentID = 2;
		} else if (character == 'T') {
			pentID = 3;
		} else if (character == 'U') {
			pentID = 4;
		} else if (character == 'V') {
			pentID = 5;
		} else if (character == 'W') {
			pentID = 6;
		} else if (character == 'Y') {
			pentID = 7;
		} else if (character == 'L') {
			pentID = 8;
		} else if (character == 'P') {
			pentID = 9;
		} else if (character == 'N') {
			pentID = 10;
		} else if (character == 'F') {
			pentID = 11;
		}
		return pentID;
	}

}
