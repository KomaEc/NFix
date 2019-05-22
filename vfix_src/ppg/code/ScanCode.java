package ppg.code;

public class ScanCode extends Code {
   public ScanCode(String scanCode) {
      this.value = scanCode;
   }

   public Object clone() {
      return new ScanCode(this.value.toString());
   }

   public String toString() {
      return "scan with {:" + this.value + ":};";
   }
}
