package java_cup;

public class shift_action extends parse_action {
   protected lalr_state _shift_to;

   public shift_action(lalr_state shft_to) throws internal_error {
      if (shft_to == null) {
         throw new internal_error("Attempt to create a shift_action to a null state");
      } else {
         this._shift_to = shft_to;
      }
   }

   public lalr_state shift_to() {
      return this._shift_to;
   }

   public int kind() {
      return 1;
   }

   public boolean equals(shift_action other) {
      return other != null && other.shift_to() == this.shift_to();
   }

   public boolean equals(Object other) {
      return other instanceof shift_action ? this.equals((shift_action)other) : false;
   }

   public int hashCode() {
      return this.shift_to().hashCode();
   }

   public String toString() {
      return "SHIFT(to state " + this.shift_to().index() + ")";
   }
}
