import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Mode27Verifier implements Verifier {

    @Override
    public void verify(int[][] board) {
        List<String> errors = new Vector<>();
        List<Thread> allThreads = new ArrayList<>();

        // 1. Rows Threads
        for (int i = 0; i < 9; i++) {
            final int currentRow = i;
            Thread t = new Thread(() -> {
                Utils.analyzeSegment(board[currentRow], "ROW", currentRow, errors);
            });
            allThreads.add(t);
        }

        // 2. Columns Threads
        for (int i = 0; i < 9; i++) {
            final int currentCol = i;
            Thread t = new Thread(() -> {
                int[] columnData = new int[9];
                for (int row = 0; row < 9; row++) {
                    columnData[row] = board[row][currentCol];
                }
                Utils.analyzeSegment(columnData, "COL", currentCol, errors);
            });
            allThreads.add(t);
        }

        // 3. Boxes Threads
        for (int i = 0; i < 9; i++) {
            final int currentBoxIndex = i;
            Thread t = new Thread(() -> {
                int boxRowStart = (currentBoxIndex / 3);
                int boxColStart = (currentBoxIndex % 3);
                
                int[] boxData = new int[9];
                int index = 0;
                
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        int actualRow = boxRowStart * 3 + r;
                        int actualCol = boxColStart * 3 + c;
                        boxData[index++] = board[actualRow][actualCol];
                    }
                }
                Utils.analyzeSegment(boxData, "BOX", currentBoxIndex, errors);
            });
            allThreads.add(t);
        }

        // تشغيل وانتظار
        for (Thread t : allThreads) t.start();
        
        for (Thread t : allThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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