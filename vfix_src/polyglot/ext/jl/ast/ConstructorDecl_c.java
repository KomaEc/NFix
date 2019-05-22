package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.CodeDecl;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Formal;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.CodeInstance;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.ParsedClassType;
import polyglot.types.ProcedureInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
import polyglot.util.TypedList;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class ConstructorDecl_c extends Term_c implements ConstructorDecl {
   protected Flags flags;
   protected String name;
   protected List formals;
   protected List throwTypes;
   protected Block body;
   protected ConstructorInstance ci;
   // $FF: synthetic field
   static Class class$polyglot$ast$Formal;
   // $FF: synthetic field
   static Class class$polyglot$ast$TypeNode;

   public ConstructorDecl_c(Position pos, Flags flags, String name, List formals, List throwTypes, Block body) {
      super(pos);
      this.flags = flags;
      this.name = name;
      this.formals = TypedList.copyAndCheck(formals, class$polyglot$ast$Formal == null ? (class$polyglot$ast$Formal = class$("polyglot.ast.Formal")) : class$polyglot$ast$Formal, true);
      this.throwTypes = TypedList.copyAndCheck(throwTypes, class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, true);
      this.body = body;
   }

   public Flags flags() {
      return this.flags;
   }

   public ConstructorDecl flags(Flags flags) {
      ConstructorDecl_c n = (ConstructorDecl_c)this.copy();
      n.flags = flags;
      return n;
   }

   public String name() {
      return this.name;
   }

   public ConstructorDecl name(String name) {
      ConstructorDecl_c n = (ConstructorDecl_c)this.copy();
      n.name = name;
      return n;
   }

   public List formals() {
      return Collections.unmodifiableList(this.formals);
   }

   public ConstructorDecl formals(List formals) {
      ConstructorDecl_c n = (ConstructorDecl_c)this.copy();
      n.formals = TypedList.copyAndCheck(formals, class$polyglot$ast$Formal == null ? (class$polyglot$ast$Formal = class$("polyglot.ast.Formal")) : class$polyglot$ast$Formal, true);
      return n;
   }

   public List throwTypes() {
      return Collections.unmodifiableList(this.throwTypes);
   }

   public ConstructorDecl throwTypes(List throwTypes) {
      ConstructorDecl_c n = (ConstructorDecl_c)this.copy();
      n.throwTypes = TypedList.copyAndCheck(throwTypes, class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, true);
      return n;
   }

   public Block body() {
      return this.body;
   }

   public CodeDecl body(Block body) {
      ConstructorDecl_c n = (ConstructorDecl_c)this.copy();
      n.body = body;
      return n;
   }

   public ConstructorInstance constructorInstance() {
      return this.ci;
   }

   public ProcedureInstance procedureInstance() {
      return this.ci;
   }

   public CodeInstance codeInstance() {
      return this.procedureInstance();
   }

   public ConstructorDecl constructorInstance(ConstructorInstance ci) {
      ConstructorDecl_c n = (ConstructorDecl_c)this.copy();
      n.ci = ci;
      return n;
   }

   protected ConstructorDecl_c reconstruct(List formals, List throwTypes, Block body) {
      if (CollectionUtil.equals(formals, this.formals) && CollectionUtil.equals(throwTypes, this.throwTypes) && body == this.body) {
         return this;
      } else {
         ConstructorDecl_c n = (ConstructorDecl_c)this.copy();
         n.formals = TypedList.copyAndCheck(formals, class$polyglot$ast$Formal == null ? (class$polyglot$ast$Formal = class$("polyglot.ast.Formal")) : class$polyglot$ast$Formal, true);
         n.throwTypes = TypedList.copyAndCheck(throwTypes, class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, true);
         n.body = body;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      List formals = this.visitList(this.formals, v);
      List throwTypes = this.visitList(this.throwTypes, v);
      Block body = (Block)this.visitChild(this.body, v);
      return this.reconstruct(formals, throwTypes, body);
   }

   public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
      return tb.pushCode();
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      TypeSystem ts = tb.typeSystem();
      List l = new ArrayList(this.formals.size());

      for(int i = 0; i < this.formals.size(); ++i) {
         l.add(ts.unknownType(this.position()));
      }

      List m = new ArrayList(this.throwTypes().size());

      for(int i = 0; i < this.throwTypes().size(); ++i) {
         m.add(ts.unknownType(this.position()));
      }

      ConstructorInstance ci = ts.constructorInstance(this.position(), ts.Object(), Flags.NONE, l, m);
      return this.constructorInstance(ci);
   }

   public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
      if (ar.kind() == AmbiguityRemover.SUPER) {
         return ar.bypassChildren(this);
      } else {
         return (NodeVisitor)(ar.kind() == AmbiguityRemover.SIGNATURES && this.body != null ? ar.bypass(this.body) : ar);
      }
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      if (ar.kind() == AmbiguityRemover.SIGNATURES) {
         Context c = ar.context();
         TypeSystem ts = ar.typeSystem();
         ParsedClassType ct = c.currentClassScope();
         ConstructorInstance ci = this.makeConstructorInstance(ct, ts);
         return this.constructorInstance(ci);
      } else {
         return this;
      }
   }

   public NodeVisitor addMembersEnter(AddMemberVisitor am) {
      ParsedClassType ct = am.context().currentClassScope();
      ct.addConstructor(this.ci);
      return am.bypassChildren(this);
   }

   public Context enterScope(Context c) {
      return c.pushCode(this.ci);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      Context c = tc.context();
      TypeSystem ts = tc.typeSystem();
      ClassType ct = c.currentClass();
      if (ct.flags().isInterface()) {
         throw new SemanticException("Cannot declare a constructor inside an interface.", this.position());
      } else if (ct.isAnonymous()) {
         throw new SemanticException("Cannot declare a constructor inside an anonymous class.", this.position());
      } else {
         String ctName = ct.name();
         if (!ctName.equals(this.name)) {
            throw new SemanticException("Constructor name \"" + this.name + "\" does not match name of containing class \"" + ctName + "\".", this.position());
         } else {
            try {
               ts.checkConstructorFlags(this.flags());
            } catch (SemanticException var9) {
               throw new SemanticException(var9.getMessage(), this.position());
            }

            if (this.body == null && !this.flags().isNative()) {
               throw new SemanticException("Missing constructor body.", this.position());
            } else if (this.body != null && this.flags().isNative()) {
               throw new SemanticException("A native constructor cannot have a body.", this.position());
            } else {
               Iterator i = this.throwTypes().iterator();

               TypeNode tn;
               Type t;
               do {
                  if (!i.hasNext()) {
                     return this;
                  }

                  tn = (TypeNode)i.next();
                  t = tn.type();
               } while(t.isThrowable());

               throw new SemanticException("Type \"" + t + "\" is not a subclass of \"" + ts.Throwable() + "\".", tn.position());
            }
         }
      }
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      TypeSystem ts = ec.typeSystem();
      SubtypeSet s = ec.throwsSet();
      Iterator i = s.iterator();

      Type t;
      boolean throwDeclared;
      do {
         do {
            if (!i.hasNext()) {
               ec.throwsSet().clear();
               return super.exceptionCheck(ec);
            }

            t = (Type)i.next();
            throwDeclared = false;
         } while(t.isUncheckedException());

         Iterator j = this.throwTypes().iterator();

         while(j.hasNext()) {
            TypeNode tn = (TypeNode)j.next();
            Type tj = tn.type();
            if (ts.isSubtype(t, tj)) {
               throwDeclared = true;
               break;
            }
         }
      } while(throwDeclared);

      ec.throwsSet().clear();
      Position pos = ec.exceptionPosition(t);
      throw new SemanticException("The exception \"" + t + "\" must either be caught or declared to be thrown.", pos == null ? this.position() : pos);
   }

   public String toString() {
      return this.flags.translate() + this.name + "(...)";
   }

   public void prettyPrintHeader(CodeWriter w, PrettyPrinter tr) {
      w.begin(0);
      w.write(this.flags().translate());
      w.write(this.name);
      w.write("(");
      w.begin(0);
      Iterator i = this.formals.iterator();

      while(i.hasNext()) {
         Formal f = (Formal)i.next();
         this.print(f, w, tr);
         if (i.hasNext()) {
            w.write(",");
            w.allowBreak(0, " ");
         }
      }

      w.end();
      w.write(")");
      if (!this.throwTypes().isEmpty()) {
         w.allowBreak(6);
         w.write("throws ");
         i = this.throwTypes().iterator();

         while(i.hasNext()) {
            TypeNode tn = (TypeNode)i.next();
            this.print(tn, w, tr);
            if (i.hasNext()) {
               w.write(",");
               w.allowBreak(4, " ");
            }
         }
      }

      w.end();
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      this.prettyPrintHeader(w, tr);
      if (this.body != null) {
         this.printSubStmt(this.body, w, tr);
      } else {
         w.write(";");
      }

   }

   public void dump(CodeWriter w) {
      super.dump(w);
      if (this.ci != null) {
         w.allowBreak(4, " ");
         w.begin(0);
         w.write("(instance " + this.ci + ")");
         w.end();
      }

   }

   protected ConstructorInstance makeConstructorInstance(ClassType ct, TypeSystem ts) throws SemanticException {
      List argTypes = new LinkedList();
      List excTypes = new LinkedList();
      Iterator i = this.formals.iterator();

      while(i.hasNext()) {
         Formal f = (Formal)i.next();
         argTypes.add(f.declType());
      }

      i = this.throwTypes().iterator();

      while(i.hasNext()) {
         TypeNode tn = (TypeNode)i.next();
         excTypes.add(tn.type());
      }

      return ts.constructorInstance(this.position(), ct, this.flags, argTypes, excTypes);
   }

   public Term entry() {
      return listEntry(this.formals(), (Term)(this.body() == null ? this : this.body().entry()));
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.body() == null) {
         v.visitCFGList(this.formals(), this);
      } else {
         v.visitCFGList(this.formals(), this.body().entry());
         v.visitCFG(this.body(), (Term)this);
      }

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
