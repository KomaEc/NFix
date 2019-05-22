package ppg;

public class PPGError extends Throwable {
   private String filename;
   private String error;
   private int line;

   public PPGError(String file, int lineNum, String errorMsg) {
      this.filename = file;
      this.line = lineNum;
      this.error = errorMsg;
   }

   public String getMessage() {
      return this.filename + ":" + this.line + ": syntax error: " + this.error;
   }
}
