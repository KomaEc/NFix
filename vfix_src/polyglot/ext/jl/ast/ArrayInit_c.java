package polyglot.ext.jl.ast;

import java.util.Iterator;
import java.util.List;
import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class ArrayInit_c extends Expr_c implements ArrayInit {
   protected List elements;
   // $FF: synthetic field
   static Class class$polyglot$ast$Expr;

   public ArrayInit_c(Position pos, List elements) {
      super(pos);
      this.elements = TypedList.copyAndCheck(elements, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
   }

   public List elements() {
      return this.elements;
   }

   public ArrayInit elements(List elements) {
      ArrayInit_c n = (ArrayInit_c)this.copy();
      n.elements = TypedList.copyAndCheck(elements, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
      return n;
   }

   protected ArrayInit_c reconstruct(List elements) {
      if (!CollectionUtil.equals(elements, this.elements)) {
         ArrayInit_c n = (ArrayInit_c)this.copy();
         n.elements = TypedList.copyAndCheck(elements, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      List elements = this.visitList(this.elements, v);
      return this.reconstruct(elements);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      Type type = null;
      Iterator i = this.elements.iterator();

      while(i.hasNext()) {
         Expr e = (Expr)i.next();
         if (type == null) {
            type = e.type();
         } else {
            type = ts.leastCommonAncestor(type, e.type());
         }
      }

      if (type == null) {
         return this.type(ts.Null());
      } else {
         return this.type(ts.arrayOf(type));
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      if (this.elements.isEmpty()) {
         return child.type();
      } else {
         Type t = av.toType();
         if (!t.isArray()) {
            throw new InternalCompilerError("Type of array initializer must be an array.", this.position());
         } else {
            t = t.toArray().base();
            TypeSystem ts = av.typeSystem();
            Iterator i = this.elements.iterator();

            Expr e;
            do {
               if (!i.hasNext()) {
                  return child.type();
               }

               e = (Expr)i.next();
            } while(e != child);

            if (ts.numericConversionValid(t, e.constantValue())) {
               return child.type();
            } else {
               return t;
            }
         }
      }
   }

   public void typeCheckElements(Type lhsType) throws SemanticException {
      TypeSystem ts = lhsType.typeSystem();
      if (!lhsType.isArray()) {
         throw new SemanticException("Cannot initialize " + lhsType + " with " + this.type + ".", this.position());
      } else {
         Type t = lhsType.toArray().base();
         Iterator i = this.elements.iterator();

         while(i.hasNext()) {
            Expr e = (Expr)i.next();
            Type s = e.type();
            if (e instanceof ArrayInit) {
               ((ArrayInit)e).typeCheckElements(t);
            } else if (!ts.isImplicitCastValid(s, t) && !ts.equals(s, t) && !ts.numericConversionValid(t, e.constantValue())) {
               throw new SemanticException("Cannot assign " + s + " to " + t + ".", e.position());
            }
         }

      }
   }

   public String toString() {
      return "{ ... }";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("{ ");
      Iterator i = this.elements.iterator();

      while(i.hasNext()) {
         Expr e = (Expr)i.next();
         this.print(e, w, tr);
         if (i.hasNext()) {
            w.write(", ");
         }
      }

      w.write(" }");
   }

   public Term entry() {
      return listEntry(this.elements, this);
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFGList(this.elements, this);
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
