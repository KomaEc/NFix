package polyglot.ext.jl.ast;

import polyglot.ast.FloatLit;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class FloatLit_c extends Lit_c implements FloatLit {
   protected FloatLit.Kind kind;
   protected double value;

   public FloatLit_c(Position pos, FloatLit.Kind kind, double value) {
      super(pos);
      this.kind = kind;
      this.value = value;
   }

   public FloatLit.Kind kind() {
      return this.kind;
   }

   public FloatLit kind(FloatLit.Kind kind) {
      FloatLit_c n = (FloatLit_c)this.copy();
      n.kind = kind;
      return n;
   }

   public double value() {
      return this.value;
   }

   public FloatLit value(double value) {
      FloatLit_c n = (FloatLit_c)this.copy();
      n.value = value;
      return n;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      if (this.kind == FloatLit.FLOAT) {
         return this.type(tc.typeSystem().Float());
      } else if (this.kind == FloatLit.DOUBLE) {
         return this.type(tc.typeSystem().Double());
      } else {
         throw new InternalCompilerError("Unrecognized FloatLit kind " + this.kind);
      }
   }

   public String toString() {
      return Double.toString(this.value);
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (this.kind == FloatLit.FLOAT) {
         w.write(Float.toString((float)this.value) + "F");
      } else {
         if (this.kind != FloatLit.DOUBLE) {
            throw new InternalCompilerError("Unrecognized FloatLit kind " + this.kind);
         }

         w.write(Double.toString(this.value));
      }

   }

   public Object constantValue() {
      return this.kind == FloatLit.FLOAT ? new Float(this.value) : new Double(this.value);
   }

   public Precedence precedence() {
      return this.value < 0.0D ? Precedence.UNARY : Precedence.LITERAL;
   }
}
