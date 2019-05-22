package difflib.myers;

public class DifferentiationFailedException extends DiffException {
   private static final long serialVersionUID = 1L;

   public DifferentiationFailedException() {
   }

   public DifferentiationFailedException(String msg) {
      super(msg);
   }
}
