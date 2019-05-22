package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.ProcedureCall;
import polyglot.ast.Term;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.ProcedureInstance;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class ConstructorCall_c extends Stmt_c implements ConstructorCall {
   protected ConstructorCall.Kind kind;
   protected Expr qualifier;
   protected List arguments;
   protected ConstructorInstance ci;
   // $FF: synthetic field
   static Class class$polyglot$ast$Expr;

   public ConstructorCall_c(Position pos, ConstructorCall.Kind kind, Expr qualifier, List arguments) {
      super(pos);
      this.kind = kind;
      this.qualifier = qualifier;
      this.arguments = TypedList.copyAndCheck(arguments, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
   }

   public Expr qualifier() {
      return this.qualifier;
   }

   public ConstructorCall qualifier(Expr qualifier) {
      ConstructorCall_c n = (ConstructorCall_c)this.copy();
      n.qualifier = qualifier;
      return n;
   }

   public ConstructorCall.Kind kind() {
      return this.kind;
   }

   public ConstructorCall kind(ConstructorCall.Kind kind) {
      ConstructorCall_c n = (ConstructorCall_c)this.copy();
      n.kind = kind;
      return n;
   }

   public List arguments() {
      return Collections.unmodifiableList(this.arguments);
   }

   public ProcedureCall arguments(List arguments) {
      ConstructorCall_c n = (ConstructorCall_c)this.copy();
      n.arguments = TypedList.copyAndCheck(arguments, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
      return n;
   }

   public ProcedureInstance procedureInstance() {
      return this.constructorInstance();
   }

   public ConstructorInstance constructorInstance() {
      return this.ci;
   }

   public ConstructorCall constructorInstance(ConstructorInstance ci) {
      ConstructorCall_c n = (ConstructorCall_c)this.copy();
      n.ci = ci;
      return n;
   }

   public Context enterScope(Context c) {
      return c.pushStatic();
   }

   protected ConstructorCall_c reconstruct(Expr qualifier, List arguments) {
      if (qualifier == this.qualifier && CollectionUtil.equals(arguments, this.arguments)) {
         return this;
      } else {
         ConstructorCall_c n = (ConstructorCall_c)this.copy();
         n.qualifier = qualifier;
         n.arguments = TypedList.copyAndCheck(arguments, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Expr qualifier = (Expr)this.visitChild(this.qualifier, v);
      List arguments = this.visitList(this.arguments, v);
      return this.reconstruct(qualifier, arguments);
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      TypeSystem ts = tb.typeSystem();
      if (this.kind == ConstructorCall.SUPER && tb.currentClass() == ts.Object()) {
         return tb.nodeFactory().Empty(this.position());
      } else {
         ConstructorCall_c n = (ConstructorCall_c)super.buildTypes(tb);
         List l = new ArrayList(this.arguments.size());

         for(int i = 0; i < this.arguments.size(); ++i) {
            l.add(ts.unknownType(this.position()));
         }

         ConstructorInstance ci = ts.constructorInstance(this.position(), ts.Object(), Flags.NONE, l, Collections.EMPTY_LIST);
         return n.constructorInstance(ci);
      }
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      Context c = tc.context();
      ClassType ct = c.currentClass();
      Type superType = ct.superType();
      if (this.qualifier != null) {
         label83: {
            if (this.kind != ConstructorCall.SUPER) {
               throw new SemanticException("Can only qualify a \"super\"constructor invocation.", this.position());
            }

            if (superType.isClass() && superType.toClass().isInnerClass() && !superType.toClass().inStaticContext()) {
               Type qt = this.qualifier.type();
               if (qt.isClass() && qt.isSubtype(superType.toClass().outer())) {
                  break label83;
               }

               throw new SemanticException("The type of the qualifier \"" + qt + "\" does not match the immediately enclosing " + "class  of the super class \"" + superType.toClass().outer() + "\".", this.qualifier.position());
            }

            throw new SemanticException("The class \"" + superType + "\"" + " is not an inner class, or was declared in a static " + "context; a qualified constructor invocation cannot " + "be used.", this.position());
         }
      }

      if (this.kind == ConstructorCall.SUPER) {
         if (!superType.isClass()) {
            throw new SemanticException("Super type of " + ct + " is not a class.", this.position());
         }

         if (this.qualifier == null && superType.isClass() && superType.toClass().isInnerClass()) {
            ClassType superContainer = superType.toClass().outer();

            ClassType e;
            for(e = ct; e != null && (!e.isSubtype(superContainer) || !ct.hasEnclosingInstance(e)); e = e.outer()) {
            }

            if (e == null) {
               throw new SemanticException(ct + " must have an enclosing instance" + " that is a subtype of " + superContainer, this.position());
            }

            if (e == ct) {
               throw new SemanticException(ct + " is a subtype of " + superContainer + "; an enclosing instance that is a subtype of " + superContainer + " must be specified in the super constructor call.", this.position());
            }
         }

         ct = ct.superType().toClass();
      }

      List argTypes = new LinkedList();
      Iterator iter = this.arguments.iterator();

      while(iter.hasNext()) {
         Expr e = (Expr)iter.next();
         argTypes.add(e.type());
      }

      ConstructorInstance ci = ts.findConstructor(ct, argTypes, (ClassType)c.currentClass());
      return this.constructorInstance(ci);
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      TypeSystem ts = av.typeSystem();
      if (child == this.qualifier) {
         return ts.Object();
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

   public String toString() {
      return (this.qualifier != null ? this.qualifier + "." : "") + this.kind + "(...)";
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (this.qualifier != null) {
         this.print(this.qualifier, w, tr);
         w.write(".");
      }

      w.write(this.kind + "(");
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
      w.write(");");
   }

   public Term entry() {
      return this.qualifier != null ? this.qualifier.entry() : listEntry(this.arguments, this);
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.qualifier != null) {
         v.visitCFG(this.qualifier, (Term)listEntry(this.arguments, this));
      }

      v.visitCFGList(this.arguments, this);
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
