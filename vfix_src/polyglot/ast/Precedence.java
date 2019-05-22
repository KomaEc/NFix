package polyglot.ast;

import polyglot.util.Enum;

public class Precedence extends Enum {
   private int value;
   public static final Precedence LITERAL = new Precedence("literal", 0);
   public static final Precedence UNARY = new Precedence("unary", 10);
   public static final Precedence CAST = new Precedence("cast", 10);
   public static final Precedence MUL = new Precedence("*", 20);
   public static final Precedence STRING_ADD = new Precedence("string+", 30);
   public static final Precedence ADD = new Precedence("+", 40);
   public static final Precedence SHIFT = new Precedence("<<", 50);
   public static final Precedence RELATIONAL = new Precedence("<", 60);
   public static final Precedence INSTANCEOF = new Precedence("isa", 70);
   public static final Precedence EQUAL = new Precedence("==", 80);
   public static final Precedence BIT_AND = new Precedence("&", 90);
   public static final Precedence BIT_XOR = new Precedence("^", 100);
   public static final Precedence BIT_OR = new Precedence("|", 110);
   public static final Precedence COND_AND = new Precedence("&&", 120);
   public static final Precedence COND_OR = new Precedence("||", 130);
   public static final Precedence CONDITIONAL = new Precedence("?:", 140);
   public static final Precedence ASSIGN = new Precedence("=", 130);
   public static final Precedence UNKNOWN = new Precedence("unknown", 999);

   public Precedence(String name, int value) {
      super("prec_" + name);
      this.value = value;
   }

   public int hashCode() {
      return this.value;
   }

   public boolean equals(Object o) {
      return o instanceof Precedence && this.equals((Precedence)o);
   }

   public boolean equals(Precedence p) {
      return this.value == p.value;
   }

   public boolean isTighter(Precedence p) {
      return this.value < p.value;
   }
}
