package polyglot.ext.jl.ast;

import java.util.Iterator;
import java.util.List;
import polyglot.ast.ArrayInit;
import polyglot.ast.Expr;
import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.InitializerInstance;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class FieldDecl_c extends Term_c implements FieldDecl {
   protected Flags flags;
   protected TypeNode type;
   protected String name;
   protected Expr init;
   protected FieldInstance fi;
   protected InitializerInstance ii;

   public FieldDecl_c(Position pos, Flags flags, TypeNode type, String name, Expr init) {
      super(pos);
      this.flags = flags;
      this.type = type;
      this.name = name;
      this.init = init;
   }

   public InitializerInstance initializerInstance() {
      return this.ii;
   }

   public FieldDecl initializerInstance(InitializerInstance ii) {
      FieldDecl_c n = (FieldDecl_c)this.copy();
      n.ii = ii;
      return n;
   }

   public Type declType() {
      return this.type.type();
   }

   public Flags flags() {
      return this.flags;
   }

   public FieldDecl flags(Flags flags) {
      FieldDecl_c n = (FieldDecl_c)this.copy();
      n.flags = flags;
      return n;
   }

   public TypeNode type() {
      return this.type;
   }

   public FieldDecl type(TypeNode type) {
      FieldDecl_c n = (FieldDecl_c)this.copy();
      n.type = type;
      return n;
   }

   public String name() {
      return this.name;
   }

   public FieldDecl name(String name) {
      FieldDecl_c n = (FieldDecl_c)this.copy();
      n.name = name;
      return n;
   }

   public Expr init() {
      return this.init;
   }

   public FieldDecl init(Expr init) {
      FieldDecl_c n = (FieldDecl_c)this.copy();
      n.init = init;
      return n;
   }

   public FieldDecl fieldInstance(FieldInstance fi) {
      FieldDecl_c n = (FieldDecl_c)this.copy();
      n.fi = fi;
      return n;
   }

   public FieldInstance fieldInstance() {
      return this.fi;
   }

   protected FieldDecl_c reconstruct(TypeNode type, Expr init) {
      if (this.type == type && this.init == init) {
         return this;
      } else {
         FieldDecl_c n = (FieldDecl_c)this.copy();
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

   public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
      return tb.pushCode();
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      TypeSystem ts = tb.typeSystem();
      Object n;
      if (this.init != null) {
         ClassType ct = tb.currentClass();
         Flags f = this.flags.isStatic() ? Flags.STATIC : Flags.NONE;
         InitializerInstance ii = ts.initializerInstance(this.init.position(), ct, f);
         n = this.initializerInstance(ii);
      } else {
         n = this;
      }

      FieldInstance fi = ts.fieldInstance(((FieldDecl)n).position(), ts.Object(), Flags.NONE, ts.unknownType(this.position()), ((FieldDecl)n).name());
      return ((FieldDecl)n).fieldInstance(fi);
   }

   public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
      if (ar.kind() == AmbiguityRemover.SUPER) {
         return ar.bypassChildren(this);
      } else {
         return (NodeVisitor)(ar.kind() == AmbiguityRemover.SIGNATURES && this.init != null ? ar.bypass(this.init) : ar);
      }
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      if (ar.kind() == AmbiguityRemover.SIGNATURES) {
         Context c = ar.context();
         TypeSystem ts = ar.typeSystem();
         ParsedClassType ct = c.currentClassScope();
         Flags f = this.flags;
         if (ct.flags().isInterface()) {
            f = f.Public().Static().Final();
         }

         FieldInstance fi = ts.fieldInstance(this.position(), ct, f, this.declType(), this.name);
         return this.flags(f).fieldInstance(fi);
      } else {
         if (ar.kind() == AmbiguityRemover.ALL) {
            this.checkFieldInstanceConstant();
         }

         return this;
      }
   }

   protected void checkFieldInstanceConstant() {
      FieldInstance fi = this.fi;
      if (this.init != null && fi.flags().isFinal() && this.init.isConstant()) {
         Object value = this.init.constantValue();
         fi.setConstantValue(value);
      }

   }

   public NodeVisitor addMembersEnter(AddMemberVisitor am) {
      ParsedClassType ct = am.context().currentClassScope();
      FieldInstance fi = this.fi;
      if (fi == null) {
         throw new InternalCompilerError("null field instance");
      } else {
         if (Report.should_report((String)"types", 5)) {
            Report.report(5, "adding " + fi + " to " + ct);
         }

         ct.addField(fi);
         return am.bypassChildren(this);
      }
   }

   public Context enterScope(Context c) {
      return this.ii != null ? c.pushCode(this.ii) : c;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      this.checkFieldInstanceConstant();

      try {
         ts.checkFieldFlags(this.flags);
      } catch (SemanticException var4) {
         throw new SemanticException(var4.getMessage(), this.position());
      }

      if (!tc.context().currentClass().flags().isInterface() || !this.flags.isProtected() && !this.flags.isPrivate()) {
         if (this.init != null) {
            if (this.init instanceof ArrayInit) {
               ((ArrayInit)this.init).typeCheckElements(this.type.type());
            } else {
               boolean intConversion = false;
               if (!ts.isImplicitCastValid(this.init.type(), this.type.type()) && !ts.equals(this.init.type(), this.type.type()) && !ts.numericConversionValid(this.type.type(), this.init.constantValue())) {
                  throw new SemanticException("The type of the variable initializer \"" + this.init.type() + "\" does not match that of " + "the declaration \"" + this.type.type() + "\".", this.init.position());
               }
            }
         }

         if (!this.flags().isStatic() || !this.fieldInstance().container().toClass().isInnerClass() || this.flags().isFinal() && this.init != null && this.init.isConstant()) {
            return this;
         } else {
            throw new SemanticException("Inner classes cannot declare static fields, unless they are compile-time constant fields.", this.position());
         }
      } else {
         throw new SemanticException("Interface members must be public.", this.position());
      }
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      TypeSystem ts = ec.typeSystem();
      SubtypeSet s = ec.throwsSet();
      Iterator i = s.iterator();

      Type t;
      do {
         if (!i.hasNext()) {
            ec.throwsSet().clear();
            return super.exceptionCheck(ec);
         }

         t = (Type)i.next();
      } while(t.isUncheckedException());

      ec.throwsSet().clear();
      throw new SemanticException("A field initializer may not throw a " + t + ".", this.position());
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      if (child == this.init) {
         TypeSystem ts = av.typeSystem();
         return ts.numericConversionValid(this.type.type(), child.constantValue()) ? child.type() : this.type.type();
      } else {
         return child.type();
      }
   }

   public Term entry() {
      return (Term)(this.init != null ? this.init.entry() : this);
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.init != null) {
         v.visitCFG(this.init, (Term)this);
      }

      return succs;
   }

   public String toString() {
      return this.flags.translate() + this.type + " " + this.name + (this.init != null ? " = " + this.init : "");
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      boolean isInterface = this.fi != null && this.fi.container() != null && this.fi.container().toClass().flags().isInterface();
      Flags f = this.flags;
      if (isInterface) {
         f = f.clearPublic();
         f = f.clearStatic();
         f = f.clearFinal();
      }

      w.write(f.translate());
      this.print(this.type, w, tr);
      w.write(" ");
      w.write(this.name);
      if (this.init != null) {
         w.write(" =");
         w.allowBreak(2, " ");
         this.print(this.init, w, tr);
      }

      w.write(";");
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      if (this.fi != null) {
         w.allowBreak(4, " ");
         w.begin(0);
         w.write("(instance " + this.fi + ")");
         w.end();
      }

      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(name " + this.name + ")");
      w.end();
   }
}
