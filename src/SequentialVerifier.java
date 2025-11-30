import java.util.ArrayList;
import java.util.List;

public class SequentialVerifier implements Verifier {

    @Override
    public void verify(int[][] board) {
        List<String> errors = new ArrayList<>();

        // 1. فحص الصفوف
        for (int row = 0; row < 9; row++) {
            // ندهنا Utils هنا
            Utils.analyzeSegment(board[row], "ROW", row, errors);
        }
        if (!errors.isEmpty()) errors.add("------------------------------------------");

        // 2. فحص العواميد
        for (int col = 0; col < 9; col++) {
            int[] columnData = new int[9];
            for (int row = 0; row < 9; row++) {
                columnData[row] = board[row][col];
            }
            Utils.analyzeSegment(columnData, "COL", col, errors);
        }
        if (!errors.isEmpty()) errors.add("------------------------------------------");

        // 3. فحص المربعات
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

        printResult(errors);
    }

    private void printResult(List<String> errors) {
        // فلتر بسيط عشان لو الفواصل بس هي اللي موجودة
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