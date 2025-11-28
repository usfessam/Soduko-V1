import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SequentialVerifier implements Verifier {

    @Override
    public void verify(int[][] board) {
        List<String> errors = new ArrayList<>(); // هنخزن هنا كل الغلطات اللي هنلاقيها

        // 1. فحص الصفوف (Rows)
        for (int row = 0; row < 9; row++) {
            if (!isValidUnit(board[row])) {
                errors.add("ROW " + (row + 1) + " is invalid");
            }
        }

        // 2. فحص العواميد (Cols)
        for (int col = 0; col < 9; col++) {
            int[] columnData = new int[9];
            for (int row = 0; row < 9; row++) {
                columnData[row] = board[row][col];
            }
            if (!isValidUnit(columnData)) {
                errors.add("COL " + (col + 1) + " is invalid");
            }
        }

        // 3. فحص المربعات (3x3 Boxes) - دي الأصعب
        // بنلف على الـ 9 مربعات، كل مربع بدايته (boxRow, boxCol)
        for (int boxRow = 0; boxRow < 3; boxRow++) {
            for (int boxCol = 0; boxCol < 3; boxCol++) {
                int[] boxData = new int[9];
                int index = 0;
                
                // جوه كل مربع بنجيب الـ 9 أرقام بتوعه
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        // معادلة تحويل مكان المربع لإحداثيات حقيقية
                        int currentRow = boxRow * 3 + i;
                        int currentCol = boxCol * 3 + j;
                        boxData[index++] = board[currentRow][currentCol];
                    }
                }
                
                if (!isValidUnit(boxData)) {
                    int boxNumber = (boxRow * 3) + boxCol + 1;
                    errors.add("BOX " + boxNumber + " is invalid");
                }
            }
        }

        // الطباعة النهائية
        if (errors.isEmpty()) {
            System.out.println("VALID");
        } else {
            System.out.println("INVALID");
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }

    // ميثود مساعدة: بتاخد 9 أرقام وتشوف هل فيهم تكرار ولا لأ
    private boolean isValidUnit(int[] unit) {
        Set<Integer> seen = new HashSet<>();
        for (int num : unit) {
            // لو الرقم أقل من 1 أو أكبر من 9، أو شوفناه قبل كدة -> يبقى غلط
            if (num < 1 || num > 9 || seen.contains(num)) {
                return false;
            }
            seen.add(num);
        }
        return true;
    }
}