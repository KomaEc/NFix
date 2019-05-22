package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Expr;
import polyglot.ast.Ext;
import polyglot.ast.JL;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StringUtil;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public abstract class Node_c implements Node {
   protected Position position;
   protected JL del;
   protected Ext ext;

   public Node_c(Position pos) {
      this.position = pos;
   }

   public void init(Node node) {
      if (node != this) {
         throw new InternalCompilerError("Cannot use a Node as a delegate or extension.");
      }
   }

   public Node node() {
      return this;
   }

   public JL del() {
      return (JL)(this.del != null ? this.del : this);
   }

   public Node del(JL del) {
      if (this.del == del) {
         return this;
      } else {
         JL old = this.del;
         this.del = null;
         Node_c n = (Node_c)this.copy();
         n.del = del != this ? del : null;
         if (n.del != null) {
            n.del.init(n);
         }

         this.del = old;
         return n;
      }
   }

   public Ext ext(int n) {
      if (n < 1) {
         throw new InternalCompilerError("n must be >= 1");
      } else {
         return n == 1 ? this.ext() : this.ext(n - 1).ext();
      }
   }

   public Node ext(int n, Ext ext) {
      if (n < 1) {
         throw new InternalCompilerError("n must be >= 1");
      } else if (n == 1) {
         return this.ext(ext);
      } else {
         Ext prev = this.ext(n - 1);
         if (prev == null) {
            throw new InternalCompilerError("cannot set the nth extension if there is no (n-1)st extension");
         } else {
            return this.ext(n - 1, prev.ext(ext));
         }
      }
   }

   public Ext ext() {
      return this.ext;
   }

   public Node ext(Ext ext) {
      if (this.ext == ext) {
         return this;
      } else {
         Ext old = this.ext;
         this.ext = null;
         Node_c n = (Node_c)this.copy();
         n.ext = ext;
         if (n.ext != null) {
            n.ext.init(n);
         }

         this.ext = old;
         return n;
      }
   }

   public Object copy() {
      try {
         Node_c n = (Node_c)super.clone();
         if (this.del != null) {
            n.del = (JL)this.del.copy();
            n.del.init(n);
         }

         if (this.ext != null) {
            n.ext = (Ext)this.ext.copy();
            n.ext.init(n);
         }

         return n;
      } catch (CloneNotSupportedException var2) {
         throw new InternalCompilerError("Java clone() weirdness.");
      }
   }

   public Position position() {
      return this.position;
   }

   public Node position(Position position) {
      Node_c n = (Node_c)this.copy();
      n.position = position;
      return n;
   }

   public Node visitChild(Node n, NodeVisitor v) {
      return n == null ? null : v.visitEdge(this, n);
   }

   public Node visit(NodeVisitor v) {
      return v.visitEdge((Node)null, this);
   }

   public Node visitEdge(Node parent, NodeVisitor v) {
      Node n = v.override(parent, this);
      if (n == null) {
         NodeVisitor v_ = v.enter(parent, this);
         if (v_ == null) {
            throw new InternalCompilerError("NodeVisitor.enter() returned null.");
         }

         n = this.del().visitChildren(v_);
         if (n == null) {
            throw new InternalCompilerError("Node_c.visitChildren() returned null.");
         }

         n = v.leave(parent, this, n, v_);
         if (n == null) {
            throw new InternalCompilerError("NodeVisitor.leave() returned null.");
         }
      }

      return n;
   }

   public List visitList(List l, NodeVisitor v) {
      if (l == null) {
         return null;
      } else {
         List result = l;
         List vl = new ArrayList(l.size());

         Node m;
         for(Iterator i = l.iterator(); i.hasNext(); vl.add(m)) {
            Node n = (Node)i.next();
            m = this.visitChild(n, v);
            if (n != m) {
               result = vl;
            }
         }

         return (List)result;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      return this;
   }

   public Context enterScope(Context c) {
      return c;
   }

   public Context enterScope(Node child, Context c) {
      return child.del().enterScope(c);
   }

   public void addDecls(Context c) {
   }

   public Node buildTypesOverride(TypeBuilder tb) throws SemanticException {
      return null;
   }

   public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
      return tb;
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      return this;
   }

   public Node disambiguateOverride(AmbiguityRemover ar) throws SemanticException {
      return null;
   }

   public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
      return ar;
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      return this;
   }

   public Node addMembersOverride(AddMemberVisitor am) throws SemanticException {
      return null;
   }

   public NodeVisitor addMembersEnter(AddMemberVisitor am) throws SemanticException {
      return am;
   }

   public Node addMembers(AddMemberVisitor am) throws SemanticException {
      return this;
   }

   public Node typeCheckOverride(TypeChecker tc) throws SemanticException {
      return null;
   }

   public NodeVisitor typeCheckEnter(TypeChecker tc) throws SemanticException {
      return tc;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      return this;
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      return child.type();
   }

   public Node exceptionCheckOverride(ExceptionChecker ec) throws SemanticException {
      return null;
   }

   public NodeVisitor exceptionCheckEnter(ExceptionChecker ec) throws SemanticException {
      return ec.push();
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      List l = this.del().throwTypes(ec.typeSystem());
      Iterator i = l.iterator();

      while(i.hasNext()) {
         ec.throwsException((Type)i.next(), this.position());
      }

      return this;
   }

   public List throwTypes(TypeSystem ts) {
      return Collections.EMPTY_LIST;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter pp) {
   }

   public void printBlock(Node n, CodeWriter w, PrettyPrinter pp) {
      w.begin(0);
      this.print(n, w, pp);
      w.end();
   }

   public void printSubStmt(Stmt stmt, CodeWriter w, PrettyPrinter pp) {
      if (stmt instanceof Block) {
         w.write(" ");
         this.print(stmt, w, pp);
      } else {
         w.allowBreak(4, " ");
         this.printBlock(stmt, w, pp);
      }

   }

   public void print(Node child, CodeWriter w, PrettyPrinter pp) {
      pp.print(this, child, w);
   }

   public void translate(CodeWriter w, Translator tr) {
      this.del().prettyPrint(w, tr);
   }

   public void dump(CodeWriter w) {
      w.write(StringUtil.getShortNameComponent(this.getClass().getName()));
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(del " + this.del() + ")");
      w.end();
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(ext ");
      if (this.ext() == null) {
         w.write("null");
      } else {
         this.ext().dump(w);
      }

      w.write(")");
      w.end();
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(position " + (this.position != null ? this.position.toString() : "UNKNOWN") + ")");
      w.end();
   }

   public String toString() {
      return this.getClass().getName();
   }
}
