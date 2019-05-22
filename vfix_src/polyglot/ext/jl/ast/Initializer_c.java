package polyglot.ext.jl.ast;

import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.CodeDecl;
import polyglot.ast.Initializer;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.ClassType;
import polyglot.types.CodeInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class Initializer_c extends Term_c implements Initializer {
   protected Flags flags;
   protected Block body;
   protected InitializerInstance ii;

   public Initializer_c(Position pos, Flags flags, Block body) {
      super(pos);
      this.flags = flags;
      this.body = body;
   }

   public Flags flags() {
      return this.flags;
   }

   public Initializer flags(Flags flags) {
      Initializer_c n = (Initializer_c)this.copy();
      n.flags = flags;
      return n;
   }

   public InitializerInstance initializerInstance() {
      return this.ii;
   }

   public CodeInstance codeInstance() {
      return this.initializerInstance();
   }

   public Initializer initializerInstance(InitializerInstance ii) {
      Initializer_c n = (Initializer_c)this.copy();
      n.ii = ii;
      return n;
   }

   public Block body() {
      return this.body;
   }

   public CodeDecl body(Block body) {
      Initializer_c n = (Initializer_c)this.copy();
      n.body = body;
      return n;
   }

   protected Initializer_c reconstruct(Block body) {
      if (body != this.body) {
         Initializer_c n = (Initializer_c)this.copy();
         n.body = body;
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Block body = (Block)this.visitChild(this.body, v);
      return this.reconstruct(body);
   }

   public Context enterScope(Context c) {
      return c.pushCode(this.ii);
   }

   public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
      return tb.pushCode();
   }

   public Term entry() {
      return this.body().entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.body(), (Term)this);
      return succs;
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      TypeSystem ts = tb.typeSystem();
      ClassType ct = tb.currentClass();
      InitializerInstance ii = ts.initializerInstance(this.position(), ct, this.flags);
      return this.initializerInstance(ii);
   }

   public NodeVisitor addMembersEnter(AddMemberVisitor am) {
      return am.bypassChildren(this);
   }

   public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
      return (NodeVisitor)(ar.kind() != AmbiguityRemover.SUPER && ar.kind() != AmbiguityRemover.SIGNATURES ? ar : ar.bypassChildren(this));
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();

      try {
         ts.checkInitializerFlags(this.flags());
      } catch (SemanticException var4) {
         throw new SemanticException(var4.getMessage(), this.position());
      }

      if (this.flags().isStatic() && this.initializerInstance().container().toClass().isInnerClass()) {
         throw new SemanticException("Inner classes cannot declare static initializers.", this.position());
      } else {
         return this;
      }
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      TypeSystem ts = ec.typeSystem();
      SubtypeSet s = ec.throwsSet();
      Iterator i = s.iterator();

      while(i.hasNext()) {
         Type t = (Type)i.next();
         if (!t.isUncheckedException()) {
            if (this.initializerInstance().flags().isStatic()) {
               throw new SemanticException("A static initializer block may not throw a " + t + ".", ec.exceptionPosition(t));
            }

            if (!this.initializerInstance().container().toClass().isAnonymous()) {
               throw new SemanticException("An instance initializer block may not throw a " + t + ".", ec.exceptionPosition(t));
            }
         }
      }

      return super.exceptionCheck(ec);
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      w.write(this.flags.translate());
      this.printBlock(this.body, w, tr);
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      if (this.ii != null) {
         w.allowBreak(4, " ");
         w.begin(0);
         w.write("(instance " + this.ii + ")");
         w.end();
      }

   }

   public String toString() {
      return this.flags.translate() + "{ ... }";
   }
}
