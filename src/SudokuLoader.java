import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuLoader {

    // ميثود بتاخد مسار الملف وترجع مصفوفة 2D
    public int[][] loadBoard(String filePath) {
        int[][] board = new int[9][9];
        
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            
            // هنلف على الـ 9 سطور بتوع الملف
            for (int row = 0; row < 9; row++) {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    // السطر بيكون شكله كدة: "5,3,4,..."
                    // بنقسمه بالفاصلة عشان ناخد الأرقام لوحدها
                    String[] values = line.split(",");
                    
                    for (int col = 0; col < 9; col++) {
                        // بنحول النص لرقم ونحطه في المصفوفة
                        // trim() بتشيل أي مسافات زيادة لو موجودة
                        board[row][col] = Integer.parseInt(values[col].trim());
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found! " + e.getMessage());
            return null; // لو الملف مش موجود بنرجع null
        }
        
        return board;
    }
}