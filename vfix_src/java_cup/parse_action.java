package java_cup;

public class parse_action {
   public static final int ERROR = 0;
   public static final int SHIFT = 1;
   public static final int REDUCE = 2;
   public static final int NONASSOC = 3;

   public int kind() {
      return 0;
   }

   public boolean equals(parse_action other) {
      return other != null && other.kind() == 0;
   }

   public boolean equals(Object other) {
      return other instanceof parse_action ? this.equals((parse_action)other) : false;
   }

   public int hashCode() {
      return 212853027;
   }

   public String toString() {
      return "ERROR";
   }
}
