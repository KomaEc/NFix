package corg.vfix.fl.spectrum;

public class FLStmt {
   private double suspicious;
   private int lineNumber;

   public FLStmt(int line, double s) {
      this.suspicious = s;
      this.lineNumber = line;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public double getSuspicious() {
      return this.suspicious;
   }

   public void print() {
      System.out.println("#" + this.lineNumber + ": " + this.suspicious);
   }
}
