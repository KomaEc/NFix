package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class LocalDecl_c extends Stmt_c implements LocalDecl {
   protected Flags flags;
   protected TypeNode type;
   protected String name;
   protected Expr init;
   protected LocalInstance li;

   public LocalDecl_c(Position pos, Flags flags, TypeNode type, String name, Expr init) {
      super(pos);
      this.flags = flags;
      this.type = type;
      this.name = name;
      this.init = init;
   }

   public Type declType() {
      return this.type.type();
   }

   public Flags flags() {
      return this.flags;
   }

   public LocalDecl flags(Flags flags) {
      LocalDecl_c n = (LocalDecl_c)this.copy();
      n.flags = flags;
      return n;
   }

   public TypeNode type() {
      return this.type;
   }

   public LocalDecl type(TypeNode type) {
      if (type == this.type) {
         return this;
      } else {
         LocalDecl_c n = (LocalDecl_c)this.copy();
         n.type = type;
         return n;
      }
   }

   public String name() {
      return this.name;
   }

   public LocalDecl name(String name) {
      if (name.equals(this.name)) {
         return this;
      } else {
         LocalDecl_c n = (LocalDecl_c)this.copy();
         n.name = name;
         return n;
      }
   }

   public Expr init() {
      return this.init;
   }

   public LocalDecl init(Expr init) {
      if (init == this.init) {
         return this;
      } else {
         LocalDecl_c n = (LocalDecl_c)this.copy();
         n.init = init;
         return n;
      }
   }

   public LocalDecl localInstance(LocalInstance li) {
      if (li == this.li) {
         return this;
      } else {
         LocalDecl_c n = (LocalDecl_c)this.copy();
         n.li = li;
         return n;
      }
   }

   public LocalInstance localInstance() {
      return this.li;
   }

   protected LocalDecl_c reconstruct(TypeNode type, Expr init) {
      if (this.type == type && this.init == init) {
         return this;
      } else {
         LocalDecl_c n = (LocalDecl_c)this.copy();
         n.type = type;
         n.init = init;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      TypeNode type = (TypeNode)this.visitChild(this.type, v);
      Expr init = (Expr)this.visitChild(this.init, v);
      return this.reconstruct(type, init);
   }

   public Context enterScope(Node child, Context c) {
      if (child == this.init) {
         c.addVariable(this.li);
      }

      return super.enterScope(child, c);
   }

   public void addDecls(Context c) {
      c.addVariable(this.li);
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      LocalDecl_c n = (LocalDecl_c)super.buildTypes(tb);
      TypeSystem ts = tb.typeSystem();
      LocalInstance li = ts.localInstance(this.position(), Flags.NONE, ts.unknownType(this.position()), this.name());
      return n.localInstance(li);
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      TypeSystem ts = ar.typeSystem();
      LocalInstance li = ts.localInstance(this.position(), this.flags(), this.declType(), this.name());
      return this.localInstance(li);
   }

   public NodeVisitor typeCheckEnter(TypeChecker tc) throws SemanticException {
      Context c = tc.context();
      LocalInstance outerLocal = null;

      try {
         outerLocal = c.findLocal(this.li.name());
      } catch (SemanticException var5) {
      }

      if (outerLocal != null && c.isLocal(this.li.name())) {
         throw new SemanticException("Local variable \"" + this.name + "\" multiply defined.  " + "Previous definition at " + outerLocal.position() + ".", this.position());
      } else {
         return super.typeCheckEnter(tc);
      }
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      LocalInstance li = this.li;
      if (li.flags().isFinal() && this.init() != null && this.init().isConstant()) {
         Object value = this.init().constantValue();
         li = li.constantValue(value);
      }

      try {
         ts.checkLocalFlags(this.flags);
      } catch (SemanticException var5) {
         throw new SemanticException(var5.getMessage(), this.position());
      }

      if (this.init != null) {
         if (this.init instanceof ArrayInit) {
            ((ArrayInit)this.init).typeCheckElements(this.type.type());
         } else if (!ts.isImplicitCastValid(this.init.type(), this.type.type()) && !ts.equals(this.init.type(), this.type.type()) && !ts.numericConversionValid(this.type.type(), this.init.constantValue())) {
            throw new SemanticException("The type of the variable initializer \"" + this.init.type() + "\" does not match that of " + "the declaration \"" + this.type.type() + "\".", this.init.position());
         }
      }

      return this.localInstance(li);
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      if (child == this.init) {
         TypeSystem ts = av.typeSystem();
         return ts.numericConversionValid(this.type.type(), child.constantValue()) ? child.type() : this.type.type();
      } else {
         return child.type();
      }
   }

   public String toString() {
      return this.flags.translate() + this.type + " " + this.name + (this.init != null ? " = " + this.init : "") + ";";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      boolean printSemi = tr.appendSemicolon(true);
      boolean printType = tr.printType(true);
      w.write(this.flags.translate());
      if (printType) {
         this.print(this.type, w, tr);
         w.write(" ");
      }

      w.write(this.name);
      if (this.init != null) {
         w.write(" =");
         w.allowBreak(2, " ");
         this.print(this.init, w, tr);
      }

      if (printSemi) {
         w.write(";");
      }

      tr.printType(printType);
      tr.appendSemicolon(printSemi);
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      if (this.li != null) {
         w.allowBreak(4, " ");
         w.begin(0);
         w.write("(instance " + this.li + ")");
         w.end();
      }

      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(name " + this.name + ")");
      w.end();
   }

   public Term entry() {
      return (Term)(this.init() != null ? this.init().entry() : this);
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.init() != null) {
         v.visitCFG(this.init(), (Term)this);
      }

      return succs;
   }
}
