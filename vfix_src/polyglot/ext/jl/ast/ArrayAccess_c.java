package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.ArrayAccess;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.types.Flags;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class ArrayAccess_c extends Expr_c implements ArrayAccess {
   protected Expr array;
   protected Expr index;

   public ArrayAccess_c(Position pos, Expr array, Expr index) {
      super(pos);
      this.array = array;
      this.index = index;
   }

   public Precedence precedence() {
      return Precedence.LITERAL;
   }

   public Expr array() {
      return this.array;
   }

   public ArrayAccess array(Expr array) {
      ArrayAccess_c n = (ArrayAccess_c)this.copy();
      n.array = array;
      return n;
   }

   public Expr index() {
      return this.index;
   }

   public ArrayAccess index(Expr index) {
      ArrayAccess_c n = (ArrayAccess_c)this.copy();
      n.index = index;
      return n;
   }

   public Flags flags() {
      return Flags.NONE;
   }

   protected ArrayAccess_c reconstruct(Expr array, Expr index) {
      if (array == this.array && index == this.index) {
         return this;
      } else {
         ArrayAccess_c n = (ArrayAccess_c)this.copy();
         n.array = array;
         n.index = index;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr array = (Expr)this.visitChild(this.array, v);
      Expr index = (Expr)this.visitChild(this.index, v);
      return this.reconstruct(array, index);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!this.array.type().isArray()) {
         throw new SemanticException("Subscript can only follow an array type.", this.position());
      } else if (!ts.isImplicitCastValid(this.index.type(), ts.Int())) {
         throw new SemanticException("Array subscript must be an integer.", this.position());
      } else {
         return this.type(this.array.type().toArray().base());
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      if (child == this.index) {
         return ts.Int();
      } else {
         return (Type)(child == this.array ? ts.arrayOf(this.type) : child.type());
      }
   }

   public String toString() {
      return this.array + "[" + this.index + "]";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      this.printSubExpr(this.array, w, tr);
      w.write("[");
      this.printBlock(this.index, w, tr);
      w.write("]");
   }

   public Term entry() {
      return this.array.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.array, (Term)this.index.entry());
      v.visitCFG(this.index, (Term)this);
      return succs;
   }

   public List throwTypes(TypeSystem ts) {
      return CollectionUtil.list(ts.OutOfBoundsException(), ts.NullPointerException());
   }
}
