package difflib.myers;

public class DiffException extends Exception {
   private static final long serialVersionUID = 1L;

   public DiffException() {
   }

   public DiffException(String msg) {
      super(msg);
   }
}
