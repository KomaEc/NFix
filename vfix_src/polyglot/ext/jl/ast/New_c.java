package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.AmbTypeNode;
import polyglot.ast.CanonicalTypeNode;
import polyglot.ast.ClassBody;
import polyglot.ast.Expr;
import polyglot.ast.New;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Precedence;
import polyglot.ast.ProcedureCall;
import polyglot.ast.Special;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.ParsedClassType;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class New_c extends Expr_c implements New {
   protected Expr qualifier;
   protected TypeNode tn;
   protected List arguments;
   protected ClassBody body;
   protected ConstructorInstance ci;
   protected ParsedClassType anonType;
   // $FF: synthetic field
   static Class class$polyglot$ast$Expr;

   public New_c(Position pos, Expr qualifier, TypeNode tn, List arguments, ClassBody body) {
      super(pos);
      this.qualifier = qualifier;
      this.tn = tn;
      this.arguments = TypedList.copyAndCheck(arguments, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
      this.body = body;
   }

   public Expr qualifier() {
      return this.qualifier;
   }

   public New qualifier(Expr qualifier) {
      New_c n = (New_c)this.copy();
      n.qualifier = qualifier;
      return n;
   }

   public TypeNode objectType() {
      return this.tn;
   }

   public New objectType(TypeNode tn) {
      New_c n = (New_c)this.copy();
      n.tn = tn;
      return n;
   }

   public ParsedClassType anonType() {
      return this.anonType;
   }

   public New anonType(ParsedClassType anonType) {
      New_c n = (New_c)this.copy();
      n.anonType = anonType;
      return n;
   }

   public ProcedureInstance procedureInstance() {
      return this.constructorInstance();
   }

   public ConstructorInstance constructorInstance() {
      return this.ci;
   }

   public New constructorInstance(ConstructorInstance ci) {
      New_c n = (New_c)this.copy();
      n.ci = ci;
      return n;
   }

   public List arguments() {
      return this.arguments;
   }

   public ProcedureCall arguments(List arguments) {
      New_c n = (New_c)this.copy();
      n.arguments = TypedList.copyAndCheck(arguments, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
      return n;
   }

   public ClassBody body() {
      return this.body;
   }

   public New body(ClassBody body) {
      New_c n = (New_c)this.copy();
      n.body = body;
      return n;
   }

   protected New_c reconstruct(Expr qualifier, TypeNode tn, List arguments, ClassBody body) {
      if (qualifier == this.qualifier && tn == this.tn && CollectionUtil.equals(arguments, this.arguments) && body == this.body) {
         return this;
      } else {
         New_c n = (New_c)this.copy();
         n.tn = tn;
         n.qualifier = qualifier;
         n.arguments = TypedList.copyAndCheck(arguments, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
         n.body = body;
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr qualifier = (Expr)this.visitChild(this.qualifier, v);
      TypeNode tn = (TypeNode)this.visitChild(this.tn, v);
      List arguments = this.visitList(this.arguments, v);
      ClassBody body = (ClassBody)this.visitChild(this.body, v);
      return this.reconstruct(qualifier, tn, arguments, body);
   }

   public Context enterScope(Node child, Context c) {
      if (child == this.body && this.anonType != null && this.body != null) {
         c = c.pushClass(this.anonType, this.anonType);
      }

      return super.enterScope(child, c);
   }

   public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
      return (NodeVisitor)(this.body != null ? tb.bypass(this.body) : tb);
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      New_c n = this;
      if (this.body() != null) {
         TypeBuilder bodyTB = (TypeBuilder)tb.visitChildren();
         bodyTB = bodyTB.pushAnonClass(this.position());
         n = (New_c)this.body((ClassBody)this.body().visit(bodyTB));
         ParsedClassType type = bodyTB.currentClass();
         n = (New_c)n.anonType(type);
      }

      TypeSystem ts = tb.typeSystem();
      List l = new ArrayList(n.arguments.size());

      for(int i = 0; i < n.arguments.size(); ++i) {
         l.add(ts.unknownType(this.position()));
      }

      ConstructorInstance ci = ts.constructorInstance(this.position(), ts.Object(), Flags.NONE, l, Collections.EMPTY_LIST);
      n = (New_c)n.constructorInstance(ci);
      return n.type(ts.unknownType(this.position()));
   }

   public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
      if (this.qualifier != null) {
         ar = (AmbiguityRemover)ar.bypass(this.tn);
      }

      if (this.body != null) {
         ar = (AmbiguityRemover)ar.bypass(this.body);
      }

      return ar;
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      if (ar.kind() != AmbiguityRemover.ALL) {
         return this;
      } else if (this.qualifier != null) {
         return this;
      } else {
         ClassType ct = this.tn.type().toClass();
         if (ct.isMember() && !ct.flags().isStatic()) {
            NodeFactory nf = ar.nodeFactory();
            TypeSystem ts = ar.typeSystem();
            Context c = ar.context();
            Type outer = null;
            String name = ct.name();
            ClassType t = c.currentClass();
            if (t == this.anonType) {
               t = t.outer();
            }

            for(; t != null; t = t.outer()) {
               try {
                  t = ts.staticTarget(t).toClass();
                  ClassType mt = ts.findMemberClass(t, name, c.currentClass());
                  if (ts.equals(mt, ct)) {
                     outer = t;
                     break;
                  }
               } catch (SemanticException var10) {
               }
            }

            if (outer == null) {
               throw new SemanticException("Could not find non-static member class \"" + name + "\".", this.position());
            } else {
               Special q;
               if (outer.equals(c.currentClass())) {
                  q = nf.This(this.position());
               } else {
                  q = nf.This(this.position(), nf.CanonicalTypeNode(this.position(), outer));
               }

               return this.qualifier(q);
            }
         } else {
            return this;
         }
      }
   }

   public NodeVisitor typeCheckEnter(TypeChecker tc) throws SemanticException {
      if (this.qualifier != null) {
         tc = (TypeChecker)tc.bypass(this.tn);
      }

      if (this.body != null) {
         tc = (TypeChecker)tc.bypass(this.body);
      }

      return tc;
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      New_c n = this;
      if (this.qualifier != null) {
         Type qt = this.qualifier.type();
         if (!qt.isClass()) {
            throw new SemanticException("Cannot instantiate member class of a non-class type.", this.qualifier.position());
         }

         TypeNode tn = this.disambiguateTypeNode(tc, qt.toClass());
         ClassType ct = tn.type().toClass();
         if (!ct.isInnerClass()) {
            throw new SemanticException("Cannot provide a containing instance for non-inner class " + ct.fullName() + ".", this.qualifier.position());
         }

         n = (New_c)this.objectType(tn);
      } else {
         ClassType ct = this.tn.type().toClass();
         if (ct.isMember()) {
            for(ClassType t = ct; t.isMember(); t = t.outer()) {
               if (!t.flags().isStatic()) {
                  throw new SemanticException("Cannot allocate non-static member class \"" + t + "\".", this.position());
               }
            }
         }
      }

      return n.typeCheckEpilogue(tc);
   }

   protected Node typeCheckEpilogue(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      List argTypes = new ArrayList(this.arguments.size());
      Iterator i = this.arguments.iterator();

      while(i.hasNext()) {
         Expr e = (Expr)i.next();
         argTypes.add(e.type());
      }

      ClassType ct = this.tn.type().toClass();
      if (this.body == null) {
         if (ct.flags().isInterface()) {
            throw new SemanticException("Cannot instantiate an interface.", this.position());
         }

         if (ct.flags().isAbstract()) {
            throw new SemanticException("Cannot instantiate an abstract class.", this.position());
         }
      } else {
         if (ct.flags().isFinal()) {
            throw new SemanticException("Cannot create an anonymous subclass of a final class.", this.position());
         }

         if (ct.flags().isInterface() && !this.arguments.isEmpty()) {
            throw new SemanticException("Cannot pass arguments to an anonymous class that implements an interface.", ((Expr)this.arguments.get(0)).position());
         }
      }

      if (!ct.flags().isInterface()) {
         Context c = tc.context();
         if (this.body != null) {
            this.anonType.superType(ct);
            c = c.pushClass(this.anonType, this.anonType);
         }

         this.ci = ts.findConstructor(ct, argTypes, (ClassType)c.currentClass());
      } else {
         this.ci = ts.defaultConstructor(this.position(), ct);
      }

      New_c n = (New_c)this.constructorInstance(this.ci).type(ct);
      if (n.body == null) {
         return n;
      } else {
         if (!ct.flags().isInterface()) {
            this.anonType.superType(ct);
         } else {
            this.anonType.superType(ts.Object());
            this.anonType.addInterface(ct);
         }

         this.anonType.inStaticContext(tc.context().inStaticContext());
         n = (New_c)n.type(this.anonType);
         ClassBody body = n.typeCheckBody(tc, ct);
         return n.body(body);
      }
   }

   protected TypeNode partialDisambTypeNode(TypeNode tn, TypeChecker tc, ClassType outer) throws SemanticException {
      if (tn instanceof CanonicalTypeNode) {
         return tn;
      } else {
         String name = null;
         if (tn instanceof AmbTypeNode && ((AmbTypeNode)tn).qual() == null) {
            name = ((AmbTypeNode)tn).name();
            TypeSystem ts = tc.typeSystem();
            NodeFactory nf = tc.nodeFactory();
            Context c = tc.context();
            ClassType ct = ts.findMemberClass(outer, name, c.currentClass());
            return nf.CanonicalTypeNode(tn.position(), ct);
         } else {
            throw new SemanticException("Cannot instantiate an member class.", tn.position());
         }
      }
   }

   protected TypeNode disambiguateTypeNode(TypeChecker tc, ClassType ct) throws SemanticException {
      TypeNode tn = this.partialDisambTypeNode(this.tn, tc, ct);
      if (tn instanceof CanonicalTypeNode) {
         return tn;
      } else {
         Job sj = tc.job().spawn(tc.context(), tn, Pass.CLEAN_SUPER, Pass.DISAM_ALL);
         if (!sj.status()) {
            if (!sj.reportedErrors()) {
               throw new SemanticException("Could not disambiguate type.", this.tn.position());
            } else {
               throw new SemanticException();
            }
         } else {
            tn = (TypeNode)sj.ast();
            return (TypeNode)this.visitChild(tn, tc);
         }
      }
   }

   protected ClassBody typeCheckBody(TypeChecker tc, ClassType superType) throws SemanticException {
      Context bodyCtxt = tc.context().pushClass(this.anonType, this.anonType);
      Job sj = tc.job().spawn(bodyCtxt, this.body, Pass.CLEAN_SUPER, Pass.DISAM_ALL);
      if (!sj.status()) {
         if (!sj.reportedErrors()) {
            throw new SemanticException("Could not disambiguate body of anonymous " + (superType.flags().isInterface() ? "implementor" : "subclass") + " of \"" + superType + "\".");
         } else {
            throw new SemanticException();
         }
      } else {
         ClassBody b = (ClassBody)sj.ast();
         TypeChecker bodyTC = (TypeChecker)tc.context(bodyCtxt);
         b = (ClassBody)this.visitChild(b, bodyTC.visitChildren());
         bodyTC.typeSystem().checkClassConformance(this.anonType());
         return b;
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      if (child == this.qualifier) {
         ReferenceType t = this.ci.container();
         if (t.isClass() && t.toClass().isMember()) {
            t = t.toClass().container();
            return t;
         } else {
            return child.type();
         }
      } else {
         Iterator i = this.arguments.iterator();
         Iterator j = this.ci.formalTypes().iterator();

         while(i.hasNext() && j.hasNext()) {
            Expr e = (Expr)i.next();
            Type t = (Type)j.next();
            if (e == child) {
               return t;
            }
         }

         return child.type();
      }
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      if (this.ci == null) {
         throw new InternalCompilerError(this.position(), "Null constructor instance after type check.");
      } else {
         Iterator i = this.ci.throwTypes().iterator();

         while(i.hasNext()) {
            Type t = (Type)i.next();
            ec.throwsException(t, this.position());
         }

         return super.exceptionCheck(ec);
      }
   }

   public Precedence precedence() {
      return Precedence.LITERAL;
   }

   public String toString() {
      return (this.qualifier != null ? this.qualifier.toString() + "." : "") + "new " + this.tn + "(...)" + (this.body != null ? " " + this.body : "");
   }

   protected void printQualifier(CodeWriter w, PrettyPrinter tr) {
      if (this.qualifier != null) {
         this.print(this.qualifier, w, tr);
         w.write(".");
      }

   }

   protected void printArgs(CodeWriter w, PrettyPrinter tr) {
      w.write("(");
      w.begin(0);
      Iterator i = this.arguments.iterator();

      while(i.hasNext()) {
         Expr e = (Expr)i.next();
         this.print(e, w, tr);
         if (i.hasNext()) {
            w.write(",");
            w.allowBreak(0);
         }
      }

      w.end();
      w.write(")");
   }

   protected void printBody(CodeWriter w, PrettyPrinter tr) {
      if (this.body != null) {
         w.write(" {");
         this.print(this.body, w, tr);
         w.write("}");
      }

   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      this.printQualifier(w, tr);
      w.write("new ");
      this.print(this.tn, w, tr);
      this.printArgs(w, tr);
      this.printBody(w, tr);
   }

   public void translate(CodeWriter w, Translator tr) {
      w.write("new ");
      if (this.qualifier != null) {
         ClassType ct = this.tn.type().toClass();
         if (!ct.isMember()) {
            throw new InternalCompilerError("Cannot qualify a non-member class.", this.position());
         }

         tr.setOuterClass(ct.outer());
         this.print(this.tn, w, tr);
         tr.setOuterClass((ClassType)null);
      } else {
         this.print(this.tn, w, tr);
      }

      this.printArgs(w, tr);
      this.printBody(w, tr);
   }

   public Term entry() {
      if (this.qualifier != null) {
         return this.qualifier.entry();
      } else {
         Term afterArgs = this;
         if (this.body() != null) {
            afterArgs = this.body();
         }

         return listEntry(this.arguments, (Term)afterArgs);
      }
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      Term afterArgs = this;
      if (this.body() != null) {
         afterArgs = this.body();
      }

      if (this.qualifier != null) {
         v.visitCFG(this.qualifier, (Term)listEntry(this.arguments, (Term)afterArgs));
      }

      v.visitCFGList(this.arguments, (Term)afterArgs);
      if (this.body() != null) {
         v.visitCFG(this.body(), (Term)this);
      }

      return succs;
   }

   public List throwTypes(TypeSystem ts) {
      List l = new LinkedList();
      l.addAll(this.ci.throwTypes());
      l.addAll(ts.uncheckedExceptions());
      return l;
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
