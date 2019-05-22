package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.NewArray;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.ArrayType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class NewArray_c extends Expr_c implements NewArray {
   protected TypeNode baseType;
   protected List dims;
   protected int addDims;
   protected ArrayInit init;
   // $FF: synthetic field
   static Class class$polyglot$ast$Expr;

   public NewArray_c(Position pos, TypeNode baseType, List dims, int addDims, ArrayInit init) {
      super(pos);
      this.baseType = baseType;
      this.dims = TypedList.copyAndCheck(dims, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
      this.addDims = addDims;
      this.init = init;
   }

   public TypeNode baseType() {
      return this.baseType;
   }

   public NewArray baseType(TypeNode baseType) {
      NewArray_c n = (NewArray_c)this.copy();
      n.baseType = baseType;
      return n;
   }

   public List dims() {
      return Collections.unmodifiableList(this.dims);
   }

   public NewArray dims(List dims) {
      NewArray_c n = (NewArray_c)this.copy();
      n.dims = TypedList.copyAndCheck(dims, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
      return n;
   }

   public int numDims() {
      return this.dims.size() + this.addDims;
   }

   public int additionalDims() {
      return this.addDims;
   }

   public NewArray additionalDims(int addDims) {
      NewArray_c n = (NewArray_c)this.copy();
      n.addDims = addDims;
      return n;
   }

   public ArrayInit init() {
      return this.init;
   }

   public NewArray init(ArrayInit init) {
      NewArray_c n = (NewArray_c)this.copy();
      n.init = init;
      return n;
   }

   protected NewArray_c reconstruct(TypeNode baseType, List dims, ArrayInit init) {
      if (baseType == this.baseType && CollectionUtil.equals(dims, this.dims) && init == this.init) {
         return this;
      } else {
         NewArray_c n = (NewArray_c)this.copy();
         n.baseType = baseType;
         n.dims = TypedList.copyAndCheck(dims, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
         n.init = init;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      TypeNode baseType = (TypeNode)this.visitChild(this.baseType, v);
      List dims = this.visitList(this.dims, v);
      ArrayInit init = (ArrayInit)this.visitChild(this.init, v);
      return this.reconstruct(baseType, dims, init);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      Iterator i = this.dims.iterator();

      Expr expr;
      do {
         if (!i.hasNext()) {
            ArrayType type = ts.arrayOf(this.baseType.type(), this.dims.size() + this.addDims);
            if (this.init != null) {
               this.init.typeCheckElements(type);
            }

            return this.type(type);
         }

         expr = (Expr)i.next();
      } while(ts.isImplicitCastValid(expr.type(), ts.Int()));

      throw new SemanticException("Array dimension must be an integer.", expr.position());
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      return child == this.init ? this.type : child.type();
   }

   public String toString() {
      return "new " + this.baseType + "[...]";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("new ");
      this.print(this.baseType, w, tr);
      Iterator i = this.dims.iterator();

      while(i.hasNext()) {
         Expr e = (Expr)i.next();
         w.write("[");
         this.printBlock(e, w, tr);
         w.write("]");
      }

      for(int i = 0; i < this.addDims; ++i) {
         w.write("[]");
      }

      if (this.init != null) {
         w.write(" ");
         this.print(this.init, w, tr);
      }

   }

   public Term entry() {
      return listEntry(this.dims, (Term)(this.init != null ? this.init.entry() : this));
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFGList(this.dims, (Term)(this.init != null ? this.init.entry() : this));
      if (this.init != null) {
         v.visitCFG(this.init, (Term)this);
      }

      return succs;
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
