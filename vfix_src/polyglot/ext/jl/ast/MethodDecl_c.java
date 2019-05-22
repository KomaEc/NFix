package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.CodeDecl;
import polyglot.ast.Formal;
import polyglot.ast.MethodDecl;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.CodeInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.MethodInstance;
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
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class MethodDecl_c extends Term_c implements MethodDecl {
   protected Flags flags;
   protected TypeNode returnType;
   protected String name;
   protected List formals;
   protected List throwTypes;
   protected Block body;
   protected MethodInstance mi;
   private static final Collection TOPICS = CollectionUtil.list("types", "context");
   // $FF: synthetic field
   static Class class$polyglot$ast$Formal;
   // $FF: synthetic field
   static Class class$polyglot$ast$TypeNode;

   public MethodDecl_c(Position pos, Flags flags, TypeNode returnType, String name, List formals, List throwTypes, Block body) {
      super(pos);
      this.flags = flags;
      this.returnType = returnType;
      this.name = name;
      this.formals = TypedList.copyAndCheck(formals, class$polyglot$ast$Formal == null ? (class$polyglot$ast$Formal = class$("polyglot.ast.Formal")) : class$polyglot$ast$Formal, true);
      this.throwTypes = TypedList.copyAndCheck(throwTypes, class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, true);
      this.body = body;
   }

   public Flags flags() {
      return this.flags;
   }

   public MethodDecl flags(Flags flags) {
      MethodDecl_c n = (MethodDecl_c)this.copy();
      n.flags = flags;
      return n;
   }

   public TypeNode returnType() {
      return this.returnType;
   }

   public MethodDecl returnType(TypeNode returnType) {
      MethodDecl_c n = (MethodDecl_c)this.copy();
      n.returnType = returnType;
      return n;
   }

   public String name() {
      return this.name;
   }

   public MethodDecl name(String name) {
      MethodDecl_c n = (MethodDecl_c)this.copy();
      n.name = name;
      return n;
   }

   public List formals() {
      return Collections.unmodifiableList(this.formals);
   }

   public MethodDecl formals(List formals) {
      MethodDecl_c n = (MethodDecl_c)this.copy();
      n.formals = TypedList.copyAndCheck(formals, class$polyglot$ast$Formal == null ? (class$polyglot$ast$Formal = class$("polyglot.ast.Formal")) : class$polyglot$ast$Formal, true);
      return n;
   }

   public List throwTypes() {
      return Collections.unmodifiableList(this.throwTypes);
   }

   public MethodDecl throwTypes(List throwTypes) {
      MethodDecl_c n = (MethodDecl_c)this.copy();
      n.throwTypes = TypedList.copyAndCheck(throwTypes, class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, true);
      return n;
   }

   public Block body() {
      return this.body;
   }

   public CodeDecl body(Block body) {
      MethodDecl_c n = (MethodDecl_c)this.copy();
      n.body = body;
      return n;
   }

   public MethodInstance methodInstance() {
      return this.mi;
   }

   public MethodDecl methodInstance(MethodInstance mi) {
      MethodDecl_c n = (MethodDecl_c)this.copy();
      n.mi = mi;
      return n;
   }

   public CodeInstance codeInstance() {
      return this.procedureInstance();
   }

   public ProcedureInstance procedureInstance() {
      return this.mi;
   }

   protected MethodDecl_c reconstruct(TypeNode returnType, List formals, List throwTypes, Block body) {
      if (returnType == this.returnType && CollectionUtil.equals(formals, this.formals) && CollectionUtil.equals(throwTypes, this.throwTypes) && body == this.body) {
         return this;
      } else {
         MethodDecl_c n = (MethodDecl_c)this.copy();
         n.returnType = returnType;
         n.formals = TypedList.copyAndCheck(formals, class$polyglot$ast$Formal == null ? (class$polyglot$ast$Formal = class$("polyglot.ast.Formal")) : class$polyglot$ast$Formal, true);
         n.throwTypes = TypedList.copyAndCheck(throwTypes, class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, true);
         n.body = body;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      List formals = this.visitList(this.formals, v);
      TypeNode returnType = (TypeNode)this.visitChild(this.returnType, v);
      List throwTypes = this.visitList(this.throwTypes, v);
      Block body = (Block)this.visitChild(this.body, v);
      return this.reconstruct(returnType, formals, throwTypes, body);
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

      MethodInstance mi = ts.methodInstance(this.position(), ts.Object(), Flags.NONE, ts.unknownType(this.position()), this.name, l, m);
      return this.methodInstance(mi);
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
         MethodInstance mi = this.makeMethodInstance(ct, ts);
         return this.flags(mi.flags()).methodInstance(mi);
      } else {
         return this;
      }
   }

   public NodeVisitor addMembersEnter(AddMemberVisitor am) {
      ParsedClassType ct = am.context().currentClassScope();
      ct.addMethod(this.mi);
      return am.bypassChildren(this);
   }

   public Context enterScope(Context c) {
      if (Report.should_report((Collection)TOPICS, 5)) {
         Report.report(5, "enter scope of method " + this.name);
      }

      c = c.pushCode(this.mi);
      return c;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      if (tc.context().currentClass().flags().isInterface() && (this.flags().isProtected() || this.flags().isPrivate())) {
         throw new SemanticException("Interface methods must be public.", this.position());
      } else {
         try {
            ts.checkMethodFlags(this.flags());
         } catch (SemanticException var6) {
            throw new SemanticException(var6.getMessage(), this.position());
         }

         if (this.body == null && !this.flags().isAbstract() && !this.flags().isNative()) {
            throw new SemanticException("Missing method body.", this.position());
         } else if (this.body != null && this.flags().isAbstract()) {
            throw new SemanticException("An abstract method cannot have a body.", this.position());
         } else if (this.body != null && this.flags().isNative()) {
            throw new SemanticException("A native method cannot have a body.", this.position());
         } else {
            Iterator i = this.throwTypes().iterator();

            TypeNode tn;
            Type t;
            do {
               if (!i.hasNext()) {
                  if (this.flags().isStatic() && this.methodInstance().container().toClass().isInnerClass()) {
                     throw new SemanticException("Inner classes cannot declare static methods.", this.position());
                  }

                  this.overrideMethodCheck(tc);
                  return this;
               }

               tn = (TypeNode)i.next();
               t = tn.type();
            } while(t.isThrowable());

            throw new SemanticException("Type \"" + t + "\" is not a subclass of \"" + ts.Throwable() + "\".", tn.position());
         }
      }
   }

   protected void overrideMethodCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      Iterator j = this.mi.implemented().iterator();

      while(j.hasNext()) {
         MethodInstance mj = (MethodInstance)j.next();
         if (ts.isAccessible(mj, tc.context())) {
            ts.checkOverride(this.mi, mj);
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
      return this.flags.translate() + this.returnType + " " + this.name + "(...)";
   }

   public void prettyPrintHeader(Flags flags, CodeWriter w, PrettyPrinter tr) {
      w.begin(0);
      w.write(flags.translate());
      this.print(this.returnType, w, tr);
      w.write(" " + this.name + "(");
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
      this.prettyPrintHeader(this.flags(), w, tr);
      if (this.body != null) {
         this.printSubStmt(this.body, w, tr);
      } else {
         w.write(";");
      }

   }

   public void translate(CodeWriter w, Translator tr) {
      Context c = tr.context();
      Flags flags = this.flags();
      if (c.currentClass().flags().isInterface()) {
         flags = flags.clearPublic();
         flags = flags.clearAbstract();
      }

      this.prettyPrintHeader(this.flags(), w, tr);
      if (this.body != null) {
         this.printSubStmt(this.body, w, tr);
      } else {
         w.write(";");
      }

   }

   public void dump(CodeWriter w) {
      super.dump(w);
      if (this.mi != null) {
         w.allowBreak(4, " ");
         w.begin(0);
         w.write("(instance " + this.mi + ")");
         w.end();
      }

      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(name " + this.name + ")");
      w.end();
   }

   protected MethodInstance makeMethodInstance(ClassType ct, TypeSystem ts) throws SemanticException {
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

      Flags flags = this.flags;
      if (ct.flags().isInterface()) {
         flags = flags.Public().Abstract();
      }

      return ts.methodInstance(this.position(), ct, flags, this.returnType.type(), this.name, argTypes, excTypes);
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
