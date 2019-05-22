package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Catch;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class Catch_c extends Stmt_c implements Catch {
   protected Formal formal;
   protected Block body;

   public Catch_c(Position pos, Formal formal, Block body) {
      super(pos);
      this.formal = formal;
      this.body = body;
   }

   public Type catchType() {
      return this.formal.declType();
   }

   public Formal formal() {
      return this.formal;
   }

   public Catch formal(Formal formal) {
      Catch_c n = (Catch_c)this.copy();
      n.formal = formal;
      return n;
   }

   public Block body() {
      return this.body;
   }

   public Catch body(Block body) {
      Catch_c n = (Catch_c)this.copy();
      n.body = body;
      return n;
   }

   protected Catch_c reconstruct(Formal formal, Block body) {
      if (formal == this.formal && body == this.body) {
         return this;
      } else {
         Catch_c n = (Catch_c)this.copy();
         n.formal = formal;
         n.body = body;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Formal formal = (Formal)this.visitChild(this.formal, v);
      Block body = (Block)this.visitChild(this.body, v);
      return this.reconstruct(formal, body);
   }

   public Context enterScope(Context c) {
      return c.pushBlock();
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (!this.catchType().isThrowable()) {
         throw new SemanticException("Can only throw subclasses of \"" + ts.Throwable() + "\".", this.formal.position());
      } else {
         return this;
      }
   }

   public String toString() {
      return "catch (" + this.formal + ") " + this.body;
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write("catch (");
      this.printBlock(this.formal, w, tr);
      w.write(")");
      this.printSubStmt(this.body, w, tr);
   }

   public Term entry() {
      return this.formal.entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.formal, (Term)this.body.entry());
      v.visitCFG(this.body, (Term)this);
      return succs;
   }
}
