package java_cup;

public abstract class production_part {
   protected String _label;

   public production_part(String lab) {
      this._label = lab;
   }

   public String label() {
      return this._label;
   }

   public abstract boolean is_action();

   public boolean equals(production_part other) {
      if (other == null) {
         return false;
      } else if (this.label() != null) {
         return this.label().equals(other.label());
      } else {
         return other.label() == null;
      }
   }

   public boolean equals(Object other) {
      return !(other instanceof production_part) ? false : this.equals((production_part)other);
   }

   public int hashCode() {
      return this.label() == null ? 0 : this.label().hashCode();
   }

   public String toString() {
      return this.label() != null ? this.label() + ":" : " ";
   }
}
