import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Mode27Verifier implements Verifier {

    @Override
    public void verify(int[][] board) {
        // مخزن الأخطاء الآمن (Thread-Safe)
        List<String> errors = new Vector<>();
        
        // لستة هنحوش فيها كل الـ Threads عشان نعرف نشغلهم ونستناهم
        List<Thread> allThreads = new ArrayList<>();

        // -------------------------------------------------
        // أولاً: 9 Threads للصفوف
        // -------------------------------------------------
        for (int i = 0; i < 9; i++) {
            final int currentRow = i; // لازم متغير final أو effectively final عشان يدخل جوه الثريد
            Thread t = new Thread(() -> {
                if (!isValidUnit(board[currentRow])) {
                    errors.add("ROW " + (currentRow + 1) + " is invalid");
                }
            });
            allThreads.add(t);
        }

        // -------------------------------------------------
        // ثانياً: 9 Threads للعواميد
        // -------------------------------------------------
        for (int i = 0; i < 9; i++) {
            final int currentCol = i;
            Thread t = new Thread(() -> {
                int[] columnData = new int[9];
                for (int row = 0; row < 9; row++) {
                    columnData[row] = board[row][currentCol];
                }
                if (!isValidUnit(columnData)) {
                    errors.add("COL " + (currentCol + 1) + " is invalid");
                }
            });
            allThreads.add(t);
        }

        // -------------------------------------------------
        // ثالثاً: 9 Threads للمربعات
        // -------------------------------------------------
        for (int i = 0; i < 9; i++) {
            final int currentBoxIndex = i;
            Thread t = new Thread(() -> {
                // حسبة رياضية عشان نعرف المربع رقم (i) ده بيبدأ منين
                int boxRowStart = (currentBoxIndex / 3); // 0, 1, or 2
                int boxColStart = (currentBoxIndex % 3); // 0, 1, or 2
                
                int[] boxData = new int[9];
                int index = 0;
                
                // نلم الـ 9 أرقام بتوع المربع ده
                for (int r = 0; r < 3; r++) {
                    for (int c = 0; c < 3; c++) {
                        int actualRow = boxRowStart * 3 + r;
                        int actualCol = boxColStart * 3 + c;
                        boxData[index++] = board[actualRow][actualCol];
                    }
                }
                
                if (!isValidUnit(boxData)) {
                    errors.add("BOX " + (currentBoxIndex + 1) + " is invalid");
                }
            });
            allThreads.add(t);
        }

        // 1. ياللا يا رجالة كله يشتغل! (Start all 27 threads)
        for (Thread t : allThreads) {
            t.start();
        }

        // 2. ياللا يا Main استنى لما كله يخلص (Join)
        for (Thread t : allThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 3. اطبع النتيجة
        if (errors.isEmpty()) {
            System.out.println("VALID");
        } else {
            System.out.println("INVALID");
            for (String error : errors) {
                System.out.println(error);
            }
        }
    }

    // نفس دالة المساعدة اللي معانا من الأول
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