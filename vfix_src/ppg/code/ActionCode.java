package ppg.code;

public class ActionCode extends Code {
   public ActionCode(String actionCode) {
      this.value = actionCode;
   }

   public Object clone() {
      return new ActionCode(this.value.toString());
   }

   public String toString() {
      return "action code {:\n" + this.value + "\n:}\n";
   }
}
