package java_cup;

public class reduce_action extends parse_action {
   protected production _reduce_with;

   public reduce_action(production prod) throws internal_error {
      if (prod == null) {
         throw new internal_error("Attempt to create a reduce_action with a null production");
      } else {
         this._reduce_with = prod;
      }
   }

   public production reduce_with() {
      return this._reduce_with;
   }

   public int kind() {
      return 2;
   }

   public boolean equals(reduce_action other) {
      return other != null && other.reduce_with() == this.reduce_with();
   }

   public boolean equals(Object other) {
      return other instanceof reduce_action ? this.equals((reduce_action)other) : false;
   }

   public int hashCode() {
      return this.reduce_with().hashCode();
   }

   public String toString() {
      return "REDUCE(with prod " + this.reduce_with().index() + ")";
   }
}
