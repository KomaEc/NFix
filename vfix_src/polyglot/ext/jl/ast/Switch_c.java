package polyglot.ext.jl.ast;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Case;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Switch;
import polyglot.ast.SwitchElement;
import polyglot.ast.Term;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.FlowGraph;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class Switch_c extends Stmt_c implements Switch {
   protected Expr expr;
   protected List elements;
   // $FF: synthetic field
   static Class class$polyglot$ast$SwitchElement;

   public Switch_c(Position pos, Expr expr, List elements) {
      super(pos);
      this.expr = expr;
      this.elements = TypedList.copyAndCheck(elements, class$polyglot$ast$SwitchElement == null ? (class$polyglot$ast$SwitchElement = class$("polyglot.ast.SwitchElement")) : class$polyglot$ast$SwitchElement, true);
   }

   public Expr expr() {
      return this.expr;
   }

   public Switch expr(Expr expr) {
      Switch_c n = (Switch_c)this.copy();
      n.expr = expr;
      return n;
   }

   public List elements() {
      return Collections.unmodifiableList(this.elements);
   }

   public Switch elements(List elements) {
      Switch_c n = (Switch_c)this.copy();
      n.elements = TypedList.copyAndCheck(elements, class$polyglot$ast$SwitchElement == null ? (class$polyglot$ast$SwitchElement = class$("polyglot.ast.SwitchElement")) : class$polyglot$ast$SwitchElement, true);
      return n;
   }

   protected Switch_c reconstruct(Expr expr, List elements) {
      if (expr == this.expr && CollectionUtil.equals(elements, this.elements)) {
         return this;
      } else {
         Switch_c n = (Switch_c)this.copy();
         n.expr = expr;
         n.elements = TypedList.copyAndCheck(elements, class$polyglot$ast$SwitchElement == null ? (class$polyglot$ast$SwitchElement = class$("polyglot.ast.SwitchElement")) : class$polyglot$ast$SwitchElement, true);
         return n;
      }
   }

   public Context enterScope(Context c) {
      return c.pushBlock();
   }

   public Node visitChildren(NodeVisitor v) {
      Expr expr = (Expr)this.visitChild(this.expr, v);
      List elements = this.visitList(this.elements, v);
      return this.reconstruct(expr, elements);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!ts.isImplicitCastValid(this.expr.type(), ts.Int())) {
         throw new SemanticException("Switch index must be an integer.", this.position());
      } else {
         Collection labels = new HashSet();
         Iterator i = this.elements.iterator();

         while(true) {
            Case c;
            Object key;
            String str;
            while(true) {
               SwitchElement s;
               do {
                  if (!i.hasNext()) {
                     return this;
                  }

                  s = (SwitchElement)i.next();
               } while(!(s instanceof Case));

               c = (Case)s;
               if (c.isDefault()) {
                  key = "default";
                  str = "default";
                  break;
               }

               if (c.expr().isConstant()) {
                  key = new Long(c.value());
                  str = c.expr().toString() + " (" + c.value() + ")";
                  break;
               }
            }

            if (labels.contains(key)) {
               throw new SemanticException("Duplicate case label: " + str + ".", c.position());
            }

            labels.add(key);
         }
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      return (Type)(child == this.expr ? ts.Int() : child.type());
   }

   public String toString() {
      return "switch (" + this.expr + ") { ... }";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("switch (");
      this.printBlock(this.expr, w, tr);
      w.write(") {");
      w.allowBreak(4, " ");
      w.begin(0);
      boolean lastWasCase = false;
      boolean first = true;

      for(Iterator i = this.elements.iterator(); i.hasNext(); first = false) {
         SwitchElement s = (SwitchElement)i.next();
         if (s instanceof Case) {
            if (lastWasCase) {
               w.newline(0);
            } else if (!first) {
               w.allowBreak(0, " ");
            }

            this.printBlock(s, w, tr);
            lastWasCase = true;
         } else {
            w.allowBreak(4, " ");
            this.print(s, w, tr);
            lastWasCase = false;
         }
      }

      w.end();
      w.allowBreak(0, " ");
      w.write("}");
   }

   public Term entry() {
      return this.expr.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      SwitchElement prev = null;
      List cases = new LinkedList();
      boolean hasDefault = false;
      Iterator i = this.elements.iterator();

      while(i.hasNext()) {
         SwitchElement s = (SwitchElement)i.next();
         if (s instanceof Case) {
            cases.add(s.entry());
            if (((Case)s).expr() == null) {
               hasDefault = true;
            }
         }
      }

      if (!hasDefault) {
         cases.add(this);
      }

      v.visitCFG(this.expr, FlowGraph.EDGE_KEY_OTHER, (List)cases);
      v.push(this).visitCFGList(this.elements, this);
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
