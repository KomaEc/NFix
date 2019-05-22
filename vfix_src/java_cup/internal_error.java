package java_cup;

public class internal_error extends Exception {
   public internal_error(String msg) {
      super(msg);
   }

   public void crash() {
      ErrorManager.getManager().emit_fatal("JavaCUP Internal Error Detected: " + this.getMessage());
      this.printStackTrace();
      System.exit(-1);
   }
}
