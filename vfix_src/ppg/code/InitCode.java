package ppg.code;

public class InitCode extends Code {
   public InitCode(String initCode) {
      this.value = initCode;
   }

   public Object clone() {
      return new InitCode(this.value.toString());
   }

   public String toString() {
      return "init code {:\n" + this.value + "\n:}\n";
   }
}
