package ppg.atoms;

public class Nonterminal extends GrammarSymbol {
   public Nonterminal(String name, String label) {
      this.name = name;
      this.label = label;
   }

   public Nonterminal(String name) {
      this.name = name;
      this.label = null;
   }

   public Object clone() {
      return new Nonterminal(this.name, this.label);
   }

   public boolean equals(Object o) {
      if (o instanceof Nonterminal) {
         return this.name.equals(((Nonterminal)o).getName());
      } else {
         return o instanceof String ? this.name.equals(o) : false;
      }
   }
}
