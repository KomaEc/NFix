package polyglot.ext.jl.ast;

import polyglot.ast.IntLit;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class IntLit_c extends NumLit_c implements IntLit {
   protected IntLit.Kind kind;

   public IntLit_c(Position pos, IntLit.Kind kind, long value) {
      super(pos, value);
      this.kind = kind;
   }

   public boolean boundary() {
      return this.kind == IntLit.INT && (int)this.value == Integer.MIN_VALUE || this.kind == IntLit.LONG && this.value == Long.MIN_VALUE;
   }

   public long value() {
      return this.longValue();
   }

   public IntLit value(long value) {
      IntLit_c n = (IntLit_c)this.copy();
      n.value = value;
      return n;
   }

   public IntLit.Kind kind() {
      return this.kind;
   }

   public IntLit kind(IntLit.Kind kind) {
      IntLit_c n = (IntLit_c)this.copy();
      n.kind = kind;
      return n;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      IntLit.Kind kind = this.kind();
      if (kind == IntLit.INT) {
         return this.type(ts.Int());
      } else if (kind == IntLit.LONG) {
         return this.type(ts.Long());
      } else {
         throw new InternalCompilerError("Unrecognized IntLit kind " + kind);
      }
   }

   public String positiveToString() {
      if (this.kind() == IntLit.LONG) {
         return this.boundary() ? "9223372036854775808L" : Long.toString(this.value) + "L";
      } else {
         return this.boundary() ? "2147483648" : Long.toString(this.value);
      }
   }

   public String toString() {
      return this.kind() == IntLit.LONG ? Long.toString(this.value) + "L" : Long.toString(this.value);
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write(this.toString());
   }

   public Object constantValue() {
      return this.kind() == IntLit.LONG ? new Long(this.value) : new Integer((int)this.value);
   }

   public Precedence precedence() {
      return this.value < 0L && !this.boundary() ? Precedence.UNARY : Precedence.LITERAL;
   }
}
