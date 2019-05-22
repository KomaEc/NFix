package java_cup;

public class symbol_part extends production_part {
   protected symbol _the_symbol;

   public symbol_part(symbol sym, String lab) throws internal_error {
      super(lab);
      if (sym == null) {
         throw new internal_error("Attempt to construct a symbol_part with a null symbol");
      } else {
         this._the_symbol = sym;
      }
   }

   public symbol_part(symbol sym) throws internal_error {
      this(sym, (String)null);
   }

   public symbol the_symbol() {
      return this._the_symbol;
   }

   public boolean is_action() {
      return false;
   }

   public boolean equals(symbol_part other) {
      return other != null && super.equals((production_part)other) && this.the_symbol().equals(other.the_symbol());
   }

   public boolean equals(Object other) {
      return !(other instanceof symbol_part) ? false : this.equals((symbol_part)other);
   }

   public int hashCode() {
      return super.hashCode() ^ (this.the_symbol() == null ? 0 : this.the_symbol().hashCode());
   }

   public String toString() {
      return this.the_symbol() != null ? super.toString() + this.the_symbol() : super.toString() + "$$MISSING-SYMBOL$$";
   }
}
