package Phase2.helperClasses;

import Phase2.Tetris;

public class Criteria {

    public static int[] columnHeights = new int[Tetris.HORIZONTAL_GRID_SIZE];

    public static int calculateHeight(int[][] field) {
        int currentHeight = 0;
        int maxHeight = 0;
        for (int j = 0; j < field[0].length; j++) {
            for (int i = Tetris.VERTICAL_GRID_SIZE - 1; i >= 0; i--) {
                if (field[i][j] != -1) {
                    currentHeight = Tetris.VERTICAL_GRID_SIZE - i + 1;
                }

            }
            columnHeights[j] = currentHeight;
            // System.out.println("Column " + j + " height: " + currentHeight);
            if (currentHeight > maxHeight) {
                maxHeight = currentHeight;
            }

        }
        return maxHeight;
    }
    

    public static int calculateGaps(int[][] field) {
        boolean[][] checked = new boolean[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                checked[i][j] = false;
            }
        }
        return calculateGaps(field, checked);
    }

    // TODO: ADD GAP HOLE CRITERIA
    public static int calculateGaps(int[][] field, boolean[][] checked) {
        int gaps = -1;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (checked[i][j])
                    continue;
                if (field[i][j] != -1) {
                    checked[i][j] = true;
                    continue;
                }
                gaps++;
                floodFillHoles(i, j, field, checked);
            }
        }
        return gaps;
    }

    private static int countBlocksAbove(int row, int col, int[][] field) {
        int count = 0;
        for (int i = row - 1; i >= 0; i--) {
            if (field[i][col] != -1) {
                count++;
            }
        }
        return count;
    }

    public static int calculateBlocksAboveGaps(int[][] field) {
        boolean[][] checked = new boolean[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                checked[i][j] = false;
            }
        }
        int blocks = 0;
        int blocksum = 0;
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[0].length; j++) {
                if (checked[i][j])
                    continue;
                if (field[i][j] != -1) {
                    checked[i][j] = true;
                    continue;
                }
                blocks = countBlocksAbove(i, j, field);
                blocksum += blocks;
                floodFillHoles(i, j, field, checked);
            }
        }
        return blocksum;
    }

    private static void floodFillHoles(int row, int col, int[][] field, boolean[][] checked) {
        if (row < 0 || row >= field.length || col < 0 || col >= field[0].length)
            return;
        if (checked[row][col] || field[row][col] != -1)
            return;
        checked[row][col] = true;
        floodFillHoles(row + 1, col, field, checked);
        floodFillHoles(row - 1, col, field, checked);
        floodFillHoles(row, col + 1, field, checked);
        floodFillHoles(row, col - 1, field, checked);
    }

    public static int calculateRowTransitions(int[][] matrix) {
        int totalTransitions = 0;

        for (int[] row : matrix) {
            int prevCell = row[0];

            for (int cell : row) {
                // Check if both cells are non-empty before counting the transition
                if ((prevCell == -1 && cell != -1) || (prevCell != -1 && cell == -1)) {
                    totalTransitions++;
                }

                prevCell = cell;
            }
        }

        return totalTransitions;
    }

    public static int calculateColumnTransitions(int[][] matrix) {
        int totalTransitions = 0;

        for (int col = 0; col < matrix[0].length; col++) {
            int prevCell = matrix[0][col];

            for (int row = 1; row < matrix.length; row++) {
                int cell = matrix[row][col];

                if ((prevCell == -1 && cell != -1) || (prevCell != -1 && cell == -1)) {
                    totalTransitions++;
                }

                prevCell = cell;
            }
        }

        return totalTransitions;
    }

    public static int calculateWellSums(int[][] newboard) {
        int wellPoints = 0;
        int w = newboard[0].length;
        int h = newboard.length;

        for (int x = 0; x < w; x++) {
            int wellDepth = 0;

            for (int y = h - 1; y >= 0; y--) {
                if (x == 0) {
                    if (newboard[y][x] == -1 && newboard[y][x + 1] >= 0) {
                        wellDepth++;
                    } else if (newboard[y][x] >= 0) {
                        wellPoints += wellDepth * (wellDepth + 1) / 2;
                        wellDepth = 0;
                    }
                } else if (x == w - 1) {
                    if (newboard[y][x] == -1 && newboard[y][x - 1] >= 0) {
                        wellDepth++;
                    } else if (newboard[y][x] >= 0) {
                        wellPoints += wellDepth * (wellDepth + 1) / 2;
                        wellDepth = 0;
                    }
                } else {
                    if (newboard[y][x] == -1 && newboard[y][x - 1] >= 0 && newboard[y][x + 1] >= 0) {
                        wellDepth++;
                    } else if (newboard[y][x] >= 0) {
                        wellPoints += wellDepth * (wellDepth + 1) / 2;
                        wellDepth = 0;
                    }
                }
            }

            wellPoints += wellDepth * (wellDepth + 1) / 2;
        }

        return wellPoints;
    }

    public static int calculateClearRows(int[][] field) {
        int clearRowCount = 0;

        for (int i = 0; i < field.length; i++) {
            boolean canClear = true;
            for (int j = 0; j < field[0].length; j++) {
                if (field[i][j] == -1) {
                    canClear = false;
                    break;
                }
            }
            if (canClear) {
                clearRowCount++;
            }
        }

        return clearRowCount;
    }

    public static int calculateBumpiness() {
        return calculateBumpinness(columnHeights);
    }

    public static int calculateBumpinness(int[] array) {
        if (array == null || array.length < 2) {
            return 0; // There are no adjacent elements to calculate differences
        }

        int sum = 0;

        for (int i = 0; i < array.length - 1; i++) {
            int absoluteDifference = Math.abs(array[i] - array[i + 1]);
            sum += absoluteDifference;
        }

        return sum;
    }

    public static int calculateFloorTouchingBlocks(int[][] matrix) {
        int cols = matrix[0].length;
        int floorTouchingBlocks = 0;
        // Iterate over the elements of the bottom row
        for (int col = 0; col < cols; col++) {
            if (matrix[matrix.length - 1][col] != -1) {
                floorTouchingBlocks++;
            }
        }
        return floorTouchingBlocks;
    }

    public static int calculateWallTouchingBlocks(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int wallTouchingBlocks = 0;

        // Iterate over the left wall
        for (int row = 0; row < rows; row++) {
            if (matrix[row][0] != -1) {
                wallTouchingBlocks++;
            }
        }

        // Iterate over the right wall
        for (int row = 0; row < rows; row++) {
            if (matrix[row][cols - 1] != -1) {
                wallTouchingBlocks++;
            }
        }

        return wallTouchingBlocks;
    }

    public static int calculateEdgesTouchingBlocks(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int edgesTouchingBlocks = 0;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (matrix[row][col] != -1) {
                    if (row + 1 < rows) {
                        if (matrix[row + 1][col] != -1) {
                            edgesTouchingBlocks++;
                        }
                    }
                    if (row - 1 >= 0) {
                        if (matrix[row - 1][col] != -1) {
                            edgesTouchingBlocks++;
                        }
                    }
                    if (col + 1 < cols) {
                        if (matrix[row][col + 1] != -1) {
                            edgesTouchingBlocks++;
                        }
                    }
                    if (col - 1 >= 0) {
                        if (matrix[row][col - 1] != -1) {
                            edgesTouchingBlocks++;
                        }
                    }
                }
            }
        }
        return edgesTouchingBlocks;
    }

    public static int calculateAggregateHeight(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int heightSum = 0;
        int[] columnHeights = new int[cols];
    
        for (int j = 0; j < cols; j++) {
            int height = -1;
            for (int i = 0; i < rows; i++) {
                if (matrix[i][j] != -1) {
                    height = Math.max(height, rows - i);
                }
            }
            columnHeights[j] = height; // Do not add 1 here, as it's already the correct height
            if(columnHeights[j] == -1)
                columnHeights[j] = 0;
        }
    
        for (int height : columnHeights) {
            heightSum += height;
        }
        return heightSum;
    }
    
    public static int calculateBumpiness(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int bumpinessSum = 0;
    
        int[] columnHeights = new int[cols];
    
        for (int j = 0; j < cols; j++) {
            int height = -1;
            for (int i = 0; i < rows; i++) {
                if (matrix[i][j] != -1) {
                    height = Math.max(height, rows - i);
                }
            }
            columnHeights[j] = height; // Do not add 1 here, as it's already the correct height
            if(columnHeights[j] == -1)
                columnHeights[j] = 0;
        }

        for (int i = 0; i < columnHeights.length - 1; i++) {
            int absoluteDifference = Math.abs(columnHeights[i] - columnHeights[i + 1]);
            bumpinessSum += absoluteDifference;
        }
    
        return bumpinessSum;
    }
    

    public static void main(String[] args) {

        int[][] matrix = {
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, 2, -1, -1 },
                { -1, -1, -1, -1, -1 },
                { -1, -1, 2, 2, 2 }
        };

        // System.out.println(calculateHeight(matrix));
        // System.out.println(calculateWellSums(matrix));
        // System.out.println(calculateGaps(matrix));
        // int[] columnHeights = calculateColumnHeights(matrix);

        // System.out.println("Bumpiness: " + calculateBumpiness(matrix));
        // System.out.println("Floor touching blocks: " +
        // countFloorTouchingBlocks(matrix));
        // System.out.println(calculateColumnTransitions(matrix));
        // System.out.println(calculateGaps(matrix));
        // System.out.println(calculateBlocksAboveGaps(matrix));
        System.out.println(calculateAggregateHeight(matrix));
    }

}
