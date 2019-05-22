package ppg.atoms;

public class Terminal extends GrammarSymbol {
   public Terminal(String name, String label) {
      this.name = name;
      this.label = label;
   }

   public Terminal(String name) {
      this.name = name;
      this.label = null;
   }

   public Object clone() {
      return new Terminal(this.name, this.label);
   }

   public boolean equals(Object o) {
      if (o instanceof Terminal) {
         return this.name.equals(((Terminal)o).getName());
      } else {
         return o instanceof String ? this.name.equals(o) : false;
      }
   }
}
