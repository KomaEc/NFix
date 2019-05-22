package polyglot.ext.jl.ast;

import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;

public abstract class Expr_c extends Term_c implements Expr {
   protected Type type;

   public Expr_c(Position pos) {
      super(pos);
   }

   public Type type() {
      return this.type;
   }

   public Expr type(Type type) {
      if (type == this.type) {
         return this;
      } else {
         Expr_c n = (Expr_c)this.copy();
         n.type = type;
         return n;
      }
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      if (this.type != null) {
         w.allowBreak(4, " ");
         w.begin(0);
         w.write("(type " + this.type + ")");
         w.end();
      }

   }

   public Precedence precedence() {
      return Precedence.UNKNOWN;
   }

   public boolean isConstant() {
      return false;
   }

   public Object constantValue() {
      return null;
   }

   public String stringValue() {
      return (String)this.constantValue();
   }

   public boolean booleanValue() {
      return (Boolean)this.constantValue();
   }

   public byte byteValue() {
      return (Byte)this.constantValue();
   }

   public short shortValue() {
      return (Short)this.constantValue();
   }

   public char charValue() {
      return (Character)this.constantValue();
   }

   public int intValue() {
      return (Integer)this.constantValue();
   }

   public long longValue() {
      return (Long)this.constantValue();
   }

   public float floatValue() {
      return (Float)this.constantValue();
   }

   public double doubleValue() {
      return (Double)this.constantValue();
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      return this.type(tb.typeSystem().unknownType(this.position()));
   }

   public void printSubExpr(Expr expr, CodeWriter w, PrettyPrinter pp) {
      this.printSubExpr(expr, true, w, pp);
   }

   public void printSubExpr(Expr expr, boolean associative, CodeWriter w, PrettyPrinter pp) {
      if ((associative || !this.precedence().equals(expr.precedence())) && !this.precedence().isTighter(expr.precedence())) {
         this.printBlock(expr, w, pp);
      } else {
         w.write("(");
         this.printBlock(expr, w, pp);
         w.write(")");
      }

   }
}
