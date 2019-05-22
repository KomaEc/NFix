package java_cup;

public class nonassoc_action extends parse_action {
   public nonassoc_action() throws internal_error {
   }

   public int kind() {
      return 3;
   }

   public boolean equals(parse_action other) {
      return other != null && other.kind() == 3;
   }

   public boolean equals(Object other) {
      return other instanceof parse_action ? this.equals((parse_action)other) : false;
   }

   public int hashCode() {
      return 212853537;
   }

   public String toString() {
      return "NONASSOC";
   }
}
