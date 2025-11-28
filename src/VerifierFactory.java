public class VerifierFactory {
    // الميثود دي هي "المصنع"
    // بتديها رقم المود (0, 3, 27) وهي ترجعلك الاوبجيكت المناسب
    public static Verifier getVerifier(int mode) {
        
        if (mode == 0) {
            return new SequentialVerifier(); // ده اللي إحنا لسه عاملينه
        } 
        else if (mode == 3) {
             return new Mode3Verifier(); // شيلنا الكومنت خلاص
        }

        else if (mode == 27) {
            return new Mode27Verifier();
            
        }
        
        return null;
    }
}
    

