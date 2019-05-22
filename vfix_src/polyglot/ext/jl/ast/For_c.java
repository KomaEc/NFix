package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.For;
import polyglot.ast.ForInit;
import polyglot.ast.ForUpdate;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.types.Context;
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
import polyglot.visit.FlowGraph;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class For_c extends Loop_c implements For {
   protected List inits;
   protected Expr cond;
   protected List iters;
   protected Stmt body;
   // $FF: synthetic field
   static Class class$polyglot$ast$ForInit;
   // $FF: synthetic field
   static Class class$polyglot$ast$ForUpdate;

   public For_c(Position pos, List inits, Expr cond, List iters, Stmt body) {
      super(pos);
      this.inits = TypedList.copyAndCheck(inits, class$polyglot$ast$ForInit == null ? (class$polyglot$ast$ForInit = class$("polyglot.ast.ForInit")) : class$polyglot$ast$ForInit, true);
      this.cond = cond;
      this.iters = TypedList.copyAndCheck(iters, class$polyglot$ast$ForUpdate == null ? (class$polyglot$ast$ForUpdate = class$("polyglot.ast.ForUpdate")) : class$polyglot$ast$ForUpdate, true);
      this.body = body;
   }

   public List inits() {
      return Collections.unmodifiableList(this.inits);
   }

   public For inits(List inits) {
      For_c n = (For_c)this.copy();
      n.inits = TypedList.copyAndCheck(inits, class$polyglot$ast$ForInit == null ? (class$polyglot$ast$ForInit = class$("polyglot.ast.ForInit")) : class$polyglot$ast$ForInit, true);
      return n;
   }

   public Expr cond() {
      return this.cond;
   }

   public For cond(Expr cond) {
      For_c n = (For_c)this.copy();
      n.cond = cond;
      return n;
   }

   public List iters() {
      return Collections.unmodifiableList(this.iters);
   }

   public For iters(List iters) {
      For_c n = (For_c)this.copy();
      n.iters = TypedList.copyAndCheck(iters, class$polyglot$ast$ForUpdate == null ? (class$polyglot$ast$ForUpdate = class$("polyglot.ast.ForUpdate")) : class$polyglot$ast$ForUpdate, true);
      return n;
   }

   public Stmt body() {
      return this.body;
   }

   public For body(Stmt body) {
      For_c n = (For_c)this.copy();
      n.body = body;
      return n;
   }

   protected For_c reconstruct(List inits, Expr cond, List iters, Stmt body) {
      if (CollectionUtil.equals(inits, this.inits) && cond == this.cond && CollectionUtil.equals(iters, this.iters) && body == this.body) {
         return this;
      } else {
         For_c n = (For_c)this.copy();
         n.inits = TypedList.copyAndCheck(inits, class$polyglot$ast$ForInit == null ? (class$polyglot$ast$ForInit = class$("polyglot.ast.ForInit")) : class$polyglot$ast$ForInit, true);
         n.cond = cond;
         n.iters = TypedList.copyAndCheck(iters, class$polyglot$ast$ForUpdate == null ? (class$polyglot$ast$ForUpdate = class$("polyglot.ast.ForUpdate")) : class$polyglot$ast$ForUpdate, true);
         n.body = body;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      List inits = this.visitList(this.inits, v);
      Expr cond = (Expr)this.visitChild(this.cond, v);
      List iters = this.visitList(this.iters, v);
      Stmt body = (Stmt)this.visitChild(this.body, v);
      return this.reconstruct(inits, cond, iters, body);
   }

   public Context enterScope(Context c) {
      return c.pushBlock();
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      Type t = null;
      Iterator i = this.inits.iterator();

      while(i.hasNext()) {
         ForInit s = (ForInit)i.next();
         if (s instanceof LocalDecl) {
            LocalDecl d = (LocalDecl)s;
            Type dt = d.type().type();
            if (t == null) {
               t = dt;
            } else if (!t.equals(dt)) {
               throw new InternalCompilerError("Local variable declarations in a for loop initializer must all be the same type, in this case " + t + ", not " + dt + ".", d.position());
            }
         }
      }

      if (this.cond != null && !ts.isImplicitCastValid(this.cond.type(), ts.Boolean())) {
         throw new SemanticException("The condition of a for statement must have boolean type.", this.cond.position());
      } else {
         return this;
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.cond ? ts.Boolean() : child.type());
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("for (");
      w.begin(0);
      if (this.inits != null) {
         boolean first = true;
         Iterator i = this.inits.iterator();

         while(i.hasNext()) {
            ForInit s = (ForInit)i.next();
            this.printForInit(s, w, tr, first);
            first = false;
            if (i.hasNext()) {
               w.write(",");
               w.allowBreak(2, " ");
            }
         }
      }

      w.write(";");
      w.allowBreak(0);
      if (this.cond != null) {
         this.printBlock(this.cond, w, tr);
      }

      w.write(";");
      w.allowBreak(0);
      if (this.iters != null) {
         Iterator i = this.iters.iterator();

         while(i.hasNext()) {
            ForUpdate s = (ForUpdate)i.next();
            this.printForUpdate(s, w, tr);
            if (i.hasNext()) {
               w.write(",");
               w.allowBreak(2, " ");
            }
         }
      }

      w.end();
      w.write(")");
      this.printSubStmt(this.body, w, tr);
   }

   public String toString() {
      return "for (...) ...";
   }

   private void printForInit(ForInit s, CodeWriter w, PrettyPrinter tr, boolean printType) {
      boolean oldSemiColon = tr.appendSemicolon(false);
      boolean oldPrintType = tr.printType(printType);
      this.printBlock(s, w, tr);
      tr.printType(oldPrintType);
      tr.appendSemicolon(oldSemiColon);
   }

   private void printForUpdate(ForUpdate s, CodeWriter w, PrettyPrinter tr) {
      boolean oldSemiColon = tr.appendSemicolon(false);
      this.printBlock(s, w, tr);
      tr.appendSemicolon(oldSemiColon);
   }

   public Term entry() {
      return listEntry(this.inits, this.cond != null ? this.cond.entry() : this.body.entry());
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFGList(this.inits, this.cond != null ? this.cond.entry() : this.body.entry());
      if (this.cond != null) {
         if (this.condIsConstantTrue()) {
            v.visitCFG(this.cond, (Term)this.body.entry());
         } else {
            v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.body.entry(), FlowGraph.EDGE_KEY_FALSE, this);
         }
      }

      v.push(this).visitCFG(this.body, (Term)this.continueTarget());
      v.visitCFGList(this.iters, this.cond != null ? this.cond.entry() : this.body.entry());
      return succs;
   }

   public Term continueTarget() {
      return listEntry(this.iters, this.cond != null ? this.cond.entry() : this.body.entry());
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
