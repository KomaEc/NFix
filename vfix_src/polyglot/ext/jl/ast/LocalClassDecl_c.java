package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.ClassDecl;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.types.Context;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;

public class LocalClassDecl_c extends Stmt_c implements LocalClassDecl {
   protected ClassDecl decl;

   public LocalClassDecl_c(Position pos, ClassDecl decl) {
      super(pos);
      this.decl = decl;
   }

   public ClassDecl decl() {
      return this.decl;
   }

   public LocalClassDecl decl(ClassDecl decl) {
      LocalClassDecl_c n = (LocalClassDecl_c)this.copy();
      n.decl = decl;
      return n;
   }

   protected LocalClassDecl_c reconstruct(ClassDecl decl) {
      if (decl != this.decl) {
         LocalClassDecl_c n = (LocalClassDecl_c)this.copy();
         n.decl = decl;
         return n;
      } else {
         return this;
      }
   }

   public Term entry() {
      return this.decl().entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.decl(), (Term)this);
      return succs;
   }

   public Node visitChildren(NodeVisitor v) {
      ClassDecl decl = (ClassDecl)this.visitChild(this.decl, v);
      return this.reconstruct(decl);
   }

   public void addDecls(Context c) {
      if (!this.decl.type().toClass().isLocal()) {
         throw new InternalCompilerError("Non-local " + this.decl.type() + " found in method body.");
      } else {
         c.addNamed(this.decl.type().toClass());
      }
   }

   public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
      return ar.bypassChildren(this);
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      if (ar.kind() == AmbiguityRemover.ALL) {
         Job sj = ar.job().spawn(ar.context(), this.decl, Pass.CLEAN_SUPER, Pass.ADD_MEMBERS_ALL);
         if (!sj.status()) {
            if (!sj.reportedErrors()) {
               throw new SemanticException("Could not disambiguate local class \"" + this.decl.name() + "\".", this.position());
            } else {
               throw new SemanticException();
            }
         } else {
            ClassDecl d = (ClassDecl)sj.ast();
            LocalClassDecl n = this.decl(d);
            return n.visitChildren(ar);
         }
      } else {
         return this;
      }
   }

   public String toString() {
      return this.decl.toString();
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      this.printBlock(this.decl, w, tr);
      w.write(";");
   }
}
