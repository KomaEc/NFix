package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.types.Context;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;

public abstract class AbstractBlock_c extends Stmt_c implements Block {
   protected List statements;
   // $FF: synthetic field
   static Class class$polyglot$ast$Stmt;

   public AbstractBlock_c(Position pos, List statements) {
      super(pos);
      this.statements = TypedList.copyAndCheck(statements, class$polyglot$ast$Stmt == null ? (class$polyglot$ast$Stmt = class$("polyglot.ast.Stmt")) : class$polyglot$ast$Stmt, true);
   }

   public List statements() {
      return this.statements;
   }

   public Block statements(List statements) {
      AbstractBlock_c n = (AbstractBlock_c)this.copy();
      n.statements = TypedList.copyAndCheck(statements, class$polyglot$ast$Stmt == null ? (class$polyglot$ast$Stmt = class$("polyglot.ast.Stmt")) : class$polyglot$ast$Stmt, true);
      return n;
   }

   public Block append(Stmt stmt) {
      List l = new ArrayList(this.statements.size() + 1);
      l.addAll(this.statements);
      l.add(stmt);
      return this.statements(l);
   }

   public Block prepend(Stmt stmt) {
      List l = new ArrayList(this.statements.size() + 1);
      l.add(stmt);
      l.addAll(this.statements);
      return this.statements(l);
   }

   protected AbstractBlock_c reconstruct(List statements) {
      if (!CollectionUtil.equals(statements, this.statements)) {
         AbstractBlock_c n = (AbstractBlock_c)this.copy();
         n.statements = TypedList.copyAndCheck(statements, class$polyglot$ast$Stmt == null ? (class$polyglot$ast$Stmt = class$("polyglot.ast.Stmt")) : class$polyglot$ast$Stmt, true);
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      List statements = this.visitList(this.statements, v);
      return this.reconstruct(statements);
   }

   public Context enterScope(Context c) {
      return c.pushBlock();
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.begin(0);
      Iterator i = this.statements.iterator();

      while(i.hasNext()) {
         Stmt n = (Stmt)i.next();
         this.printBlock(n, w, tr);
         if (i.hasNext()) {
            w.newline(0);
         }
      }

      w.end();
   }

   public Term entry() {
      return listEntry(this.statements, this);
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFGList(this.statements, this);
      return succs;
   }

   public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("{");
      int count = 0;
      Iterator i = this.statements.iterator();

      while(i.hasNext()) {
         if (count++ > 2) {
            sb.append(" ...");
            break;
         }

         Stmt n = (Stmt)i.next();
         sb.append(" ");
         sb.append(n.toString());
      }

      sb.append(" }");
      return sb.toString();
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
