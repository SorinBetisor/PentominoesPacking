package Phase3.Solvers.DancingLinks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Phase1.PentominoDatabase;
import Phase3.PiecesDB.ParcelDB;
import Phase3.PiecesDB.PentominoesDB;
import Phase3.Solvers.SearchWrapper;
import Phase3.Visualizer.FXVisualizer;

public class DLX3D {

    public static boolean[][] exactCoverMatrix;
    public static int depth = FXVisualizer.CARGO_DEPTH;
    public static int height = FXVisualizer.CARGO_HEIGHT;
    public static int width = FXVisualizer.CARGO_WIDTH;
    public static int totalValue = 0;
    // int[][][][] A = PentominoesDB.lPentInt;
    // int[][][][] B = PentominoesDB.pPentInt;
    // int[][][][] C = PentominoesDB.tPentInt;
    int[][][][] A = ParcelDB.aRotInt;
    int[][][][] B = ParcelDB.bRotInt;
    int[][][][] C = ParcelDB.cRotInt;
    public int[][][][][] shapes = new int[][][][][]{C,A,B};
    public int[] values = new int[]{5,4,3};
    DancingLinks2 dance = new DancingLinks2(width * height * depth);
    public static List<Row> rows = new ArrayList<Row>();
    public static int value = 0;


    public static boolean[] ThreeDto1D (boolean[][][] array3, int width, int height, int depth) {
        boolean[] array1 = new boolean[width * height * depth];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    array1[height * depth * x + depth * y + z] = array3[z][y][x];
                }
            }
        }

        return array1;
    }

    public boolean isPlaceable(int startX, int startY, int startZ, int[][][] shape){ 

        int shapeWidth = shape[0][0].length;
        int shapeHeight = shape[0].length;
        int shapeDepth = shape.length;

        if(startX+shapeWidth > width){
            return false;
        }

        if(startY+shapeHeight > height){
            return false;
        }

        return startZ + shapeDepth <= depth;
    }

    public static int pieceCount = 0;
    public void createPositions(){ 
        int currentPieceValue = 0;
        int typeNumber = 1;
        int nr = 0;
        for(int[][][][] typeOfShape : shapes){
            int shapeWidth = typeOfShape[0][0][0].length;
            int shapeHeight = typeOfShape[0][0].length;
            int shapeDepth = typeOfShape[0].length;
            currentPieceValue = values[typeNumber-1];
            for(int[][][] shape : typeOfShape){
                for(int zPlacementStart=0; zPlacementStart < depth; zPlacementStart++){
                    for(int yPlacementStart=0; yPlacementStart < height; yPlacementStart++){
                        for(int xPlacementStart=0; xPlacementStart < width; xPlacementStart++){
                            boolean can = isPlaceable(xPlacementStart, yPlacementStart, zPlacementStart, shape);
                            if(!can){
                                continue;
                            }
                            List<Integer> xs = getOccupiedCellsX(shape, xPlacementStart, yPlacementStart, zPlacementStart);
                            List<Integer> ys = getOccupiedCellsY(shape, xPlacementStart, yPlacementStart, zPlacementStart);
                            List<Integer> zs = getOccupiedCellsZ(shape, xPlacementStart, yPlacementStart, zPlacementStart);

                            int[] dobavkaFinal = new int[shapeDepth * shapeHeight * shapeWidth];
                            int[] dobavkaX = new int[xs.size() ];
                            int[] dobavkaY = new int[ys.size() ];
                            int[] dobavkaZ = new int[zs.size() ];
                            for(int i = 0; i < xs.size(); i++){
                                dobavkaX[i] = xs.get(i);
                            }
                            for(int i = 0; i < ys.size(); i++){
                                dobavkaY[i] = ys.get(i);
                            }
                            for(int i = 0; i < zs.size(); i++){
                                dobavkaZ[i] = zs.get(i);
                            }
                            for(int i = 0; i < dobavkaX.length; i++){
                                dobavkaFinal[i] = depth * height * dobavkaX[i] + depth * dobavkaY[i] + dobavkaZ[i];
                            }
                            // System.out.println(Arrays.toString(dobavkaFinal));
                            rows.add(new Row(nr, xPlacementStart, yPlacementStart, zPlacementStart, typeNumber, shape, currentPieceValue));
                            dance.AddRow(nr,typeNumber,dobavkaFinal,shape);
                            nr++;
                        }
                    }
                }
            }
            typeNumber++;    
        }
        dance.algorithmX(0);
    }

    public List<Integer> getOccupiedCellsX(int[][][] pieceToPlace, int x0, int y0, int z0)
    {
        List<Integer> xs = new ArrayList<Integer>();
        int distanceX = -1;
        for(int z1=0; z1<pieceToPlace.length; z1++)
        {
            for(int y1=0; y1<pieceToPlace[z1].length; y1++)
            {
                for(int x1=0; x1<pieceToPlace[z1][y1].length; x1++)
                {
                    if(pieceToPlace[z1][y1][x1] != 0 && x1 != 0)
                    {
                        distanceX = x1;
                        break;
                    }
                }
                if(distanceX != -1)
                {
                    break;
                }
            }
            if(distanceX != -1)
            {
                break;
            }
        }
        for(int z=0; z<pieceToPlace.length; z++)
        {
            for(int y=0; y<pieceToPlace[z].length; y++)
            {
                for(int x=0; x<pieceToPlace[z][y].length; x++)
                {
                    if(pieceToPlace[z][y][x] != 0)
                    {
                        xs.add(x + x0);
                    }
                }
            }
        }
        return xs;

    }

    public List<Integer> getOccupiedCellsY(int[][][] pieceToPlace, int x0 ,int y0, int z0)
    {
        List<Integer> ys = new ArrayList<Integer>();
        int distanceY = -1;
        for(int z=0; z<pieceToPlace.length; z++)
        {
            for(int y=0; y<pieceToPlace[z].length; y++)
            {
                for(int x=0; x<pieceToPlace[z][y].length; x++)
                {
                    if(pieceToPlace[z][y][x] != 0 && x != 0)
                    {
                        distanceY = y;
                        break;
                    }
                }
                if(distanceY != -1)
                {
                    break;
                }
            }
            if(distanceY != -1)
            {
                break;
            }
        }
        for(int z=0; z<pieceToPlace.length; z++)
        {
            for(int y=0; y<pieceToPlace[z].length; y++)
            {
                for(int x=0; x<pieceToPlace[z][y].length; x++)
                {
                    if(pieceToPlace[z][y][x] != 0)
                    {
                        ys.add(y + y0);
                    }
                }
            }
        }
        return ys;
    }

    public List<Integer> getOccupiedCellsZ(int[][][] pieceToPlace, int x0,int y0,int z0)
    {
        List<Integer> zs = new ArrayList<Integer>();
        int distanceZ = -1;
        for(int z = 0; z < pieceToPlace.length; z++)
        {
            for(int y = 0; y < pieceToPlace[z].length; y++)
            {
                for(int x = 0; x < pieceToPlace[z][y].length; x++)
                {
                    if(pieceToPlace[z][y][x] != 0 && x != 0)
                    {
                        distanceZ = z;
                        break;
                    }
                }
                if(distanceZ != -1)
                {
                    break;
                }
            }
            if(distanceZ != -1)
            {
                break;
            }
        }

        for (int i = 0; i < pieceToPlace.length; i++)
        {
            for (int j = 0; j < pieceToPlace[i].length; j++)
            {
                for (int k = 0; k < pieceToPlace[i][j].length; k++)
                {
                    if (pieceToPlace[i][j][k] != 0)
                    {
                        zs.add(i + z0);
                    }
                }
            }
        }
        return zs;
    }


    public static void main(String[] args) {
        DLX3D dlx = new DLX3D();
        dlx.createPositions();
        // System.out.println(dlx.getOccupiedCellsX(ParcelDB.aRotInt[0], 0, 0, 0).toString());
        // System.out.println(dlx.getOccupiedCellsY(ParcelDB.aRotInt[0], 0, 0, 0).toString());
        // System.out.println(dlx.getOccupiedCellsZ(ParcelDB.aRotInt[0], 0, 0, 0).toString());
        
    }

}
