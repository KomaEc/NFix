package soot.dava;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecompilationException extends RuntimeException {
   private static final Logger logger = LoggerFactory.getLogger(DecompilationException.class);

   public DecompilationException() {
      System.out.println("DECOMPILATION INCOMPLETE:");
   }

   public DecompilationException(String message) {
      super("DECOMPILATION INCOMPLETE" + message);
   }

   public void report() {
      System.out.println("\n\nPlease report this exception to nomair.naeem@mail.mcgill.ca");
      System.out.println("Please include the soot version, sample code and this output.\n\n");
   }
}
