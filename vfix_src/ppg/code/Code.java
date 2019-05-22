package ppg.code;

public abstract class Code {
   protected String value;

   public abstract Object clone();

   public void append(String s) {
      this.value = this.value + "\n" + s;
   }

   public void prepend(String s) {
      this.value = s + "\n" + this.value;
   }

   public abstract String toString();
}
