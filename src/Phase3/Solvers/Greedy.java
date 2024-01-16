package Phase3.Solvers;
import Phase3.PiecesDB.PentominoesDB;

public class Greedy {
    //red - 3
    //blue - 4
    //green - 5
    public static int currentValue = 0;
    public static int[][][][][] parcels = {PentominoesDB.lPentInt,PentominoesDB.pPentInt,PentominoesDB.tPentInt};
    public static int[][][] fillParcels(int[][][] field) {
        int[] values = {3,4,5};
        for(int parcel=0;parcel<3;parcel++)
        {
            int bestValueIndex = 0;
            for(int i=0;i<3;i++)
            {
                if(values[i]>values[bestValueIndex])
                {
                    bestValueIndex = i;
                }
            }
            for(int rotation = 0;rotation<parcels[bestValueIndex].length;rotation++)
            {
                int[][][] currentPiece = parcels[bestValueIndex][rotation];
                boolean can = true;
                while(can)
                {
                    can = stackPiece(field,currentPiece);
                    if(can)
                    {
                        currentValue += values[bestValueIndex];
                    }
                }
                stackPiece(field, currentPiece);
            }
            values[bestValueIndex] = 0;
        }
        return null;
    }
    

    public static boolean stackPiece(int[][][] field, int[][][] piece) {
        for (int z = 0; z < field.length; z++) {
            for (int y = 0; y < field[z].length; y++) {
                for (int x = 0; x < field[z][y].length; x++) {
                    int[] placementCoords = new int[]{z, y, x};
                    if (SearchWrapper.canPlacePiece(field, piece, placementCoords)) {
                        SearchWrapper.addPiece(field, piece, placementCoords);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
}
