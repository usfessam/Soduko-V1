import java.util.List;
import java.util.Vector;

public class Mode3Verifier implements Verifier {

    @Override
    public void verify(int[][] board) {
        List<String> errors = new Vector<>();

        // Thread 1: Rows
        Thread rowThread = new Thread(() -> {
            for (int row = 0; row < 9; row++) {
                // استخدام Utils بدل الكود القديم
                Utils.analyzeSegment(board[row], "ROW", row, errors);
            }
        });

        // Thread 2: Columns
        Thread colThread = new Thread(() -> {
            for (int col = 0; col < 9; col++) {
                int[] columnData = new int[9];
                for (int row = 0; row < 9; row++) {
                    columnData[row] = board[row][col];
                }
                Utils.analyzeSegment(columnData, "COL", col, errors);
            }
        });

        // Thread 3: Boxes
        Thread boxThread = new Thread(() -> {
            for (int boxRow = 0; boxRow < 3; boxRow++) {
                for (int boxCol = 0; boxCol < 3; boxCol++) {
                    int[] boxData = new int[9];
                    int index = 0;
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            int currentRow = boxRow * 3 + i;
                            int currentCol = boxCol * 3 + j;
                            boxData[index++] = board[currentRow][currentCol];
                        }
                    }
                    int boxNumber = (boxRow * 3) + boxCol;
                    Utils.analyzeSegment(boxData, "BOX", boxNumber, errors);
                }
            }
        });

        rowThread.start();
        colThread.start();
        boxThread.start();

        try {
            rowThread.join();
            colThread.join();
            boxThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        printResult(errors);
    }

    private void printResult(List<String> errors) {
        boolean hasRealErrors = false;
        for (String err : errors) {
            if (!err.startsWith("-")) hasRealErrors = true;
        }

        if (!hasRealErrors) {
            System.out.println("VALID");
        } else {
            System.out.println("INVALID");
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }
}