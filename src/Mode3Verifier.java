import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector; // ده زي ArrayList بس آمن مع الـ Threads

public class Mode3Verifier implements Verifier {

    @Override
    public void verify(int[][] board) {
        // بنستخدم Vector عشان ده Thread-Safe
        // لو استخدمنا ArrayList عادي، الـ Threads ممكن تضرب وهي بتكتب فيه في نفس الوقت
        List<String> errors = new Vector<>();

        // -------------------------------------------------
        // Thread 1: مسؤول عن فحص الصفوف فقط
        // -------------------------------------------------
        Thread rowThread = new Thread(() -> {
            for (int row = 0; row < 9; row++) {
                if (!isValidUnit(board[row])) {
                    errors.add("ROW " + (row + 1) + " is invalid");
                }
            }
        });

        // -------------------------------------------------
        // Thread 2: مسؤول عن فحص العواميد فقط
        // -------------------------------------------------
        Thread colThread = new Thread(() -> {
            for (int col = 0; col < 9; col++) {
                int[] columnData = new int[9];
                for (int row = 0; row < 9; row++) {
                    columnData[row] = board[row][col];
                }
                if (!isValidUnit(columnData)) {
                    errors.add("COL " + (col + 1) + " is invalid");
                }
            }
        });

        // -------------------------------------------------
        // Thread 3: مسؤول عن فحص المربعات فقط
        // -------------------------------------------------
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
                    if (!isValidUnit(boxData)) {
                        int boxNumber = (boxRow * 3) + boxCol + 1;
                        errors.add("BOX " + boxNumber + " is invalid");
                    }
                }
            }
        });

        // 1. نشغل الـ 3 عمال
        rowThread.start();
        colThread.start();
        boxThread.start();

        // 2. الـ Main Thread لازم يستنى لحد ما التلاتة يخلصوا شغلهم
        try {
            rowThread.join();
            colThread.join();
            boxThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 3. نطبع النتيجة بعد ما كله خلص
        if (errors.isEmpty()) {
            System.out.println("VALID");
        } else {
            System.out.println("INVALID");
            // الترتيب هنا ممكن يختلف كل مرة حسب مين الثريد اللي خلص الأول
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }

    // نفس دالة التأكد المساعدة
    private boolean isValidUnit(int[] unit) {
        Set<Integer> seen = new HashSet<>();
        for (int num : unit) {
            if (num < 1 || num > 9 || seen.contains(num)) {
                return false;
            }
            seen.add(num);
        }
        return true;
    }
}