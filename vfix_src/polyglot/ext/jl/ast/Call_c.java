package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import polyglot.ast.Call;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Precedence;
import polyglot.ast.ProcedureCall;
import polyglot.ast.Receiver;
import polyglot.ast.Special;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.MethodInstance;
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
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class Call_c extends Expr_c implements Call {
   protected Receiver target;
   protected String name;
   protected List arguments;
   protected MethodInstance mi;
   protected boolean targetImplicit;
   // $FF: synthetic field
   static Class class$polyglot$ast$Expr;

   public Call_c(Position pos, Receiver target, String name, List arguments) {
      super(pos);
      this.target = target;
      this.name = name;
      this.arguments = TypedList.copyAndCheck(arguments, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
      this.targetImplicit = target == null;
   }

   public Precedence precedence() {
      return Precedence.LITERAL;
   }

   public Receiver target() {
      return this.target;
   }

   public Call target(Receiver target) {
      Call_c n = (Call_c)this.copy();
      n.target = target;
      return n;
   }

   public String name() {
      return this.name;
   }

   public Call name(String name) {
      Call_c n = (Call_c)this.copy();
      n.name = name;
      return n;
   }

   public ProcedureInstance procedureInstance() {
      return this.methodInstance();
   }

   public MethodInstance methodInstance() {
      return this.mi;
   }

   public Call methodInstance(MethodInstance mi) {
      Call_c n = (Call_c)this.copy();
      n.mi = mi;
      return n;
   }

   public boolean isTargetImplicit() {
      return this.targetImplicit;
   }

   public Call targetImplicit(boolean targetImplicit) {
      if (targetImplicit == this.targetImplicit) {
         return this;
      } else {
         Call_c n = (Call_c)this.copy();
         n.targetImplicit = targetImplicit;
         return n;
      }
   }

   public List arguments() {
      return this.arguments;
   }

   public ProcedureCall arguments(List arguments) {
      Call_c n = (Call_c)this.copy();
      n.arguments = TypedList.copyAndCheck(arguments, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
      return n;
   }

   protected Call_c reconstruct(Receiver target, List arguments) {
      if (target == this.target && CollectionUtil.equals(arguments, this.arguments)) {
         return this;
      } else {
         Call_c n = (Call_c)this.copy();
         n.target = target;
         n.arguments = TypedList.copyAndCheck(arguments, class$polyglot$ast$Expr == null ? (class$polyglot$ast$Expr = class$("polyglot.ast.Expr")) : class$polyglot$ast$Expr, true);
         return n;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      Receiver target = (Receiver)this.visitChild(this.target, v);
      List arguments = this.visitList(this.arguments, v);
      return this.reconstruct(target, arguments);
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      Call_c n = (Call_c)super.buildTypes(tb);
      TypeSystem ts = tb.typeSystem();
      List l = new ArrayList(this.arguments.size());

      for(int i = 0; i < this.arguments.size(); ++i) {
         l.add(ts.unknownType(this.position()));
      }

      MethodInstance mi = ts.methodInstance(this.position(), ts.Object(), Flags.NONE, ts.unknownType(this.position()), this.name, l, Collections.EMPTY_LIST);
      return n.methodInstance(mi);
   }

   protected Node typeCheckNullTarget(TypeChecker tc, List argTypes) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      NodeFactory nf = tc.nodeFactory();
      Context c = tc.context();
      MethodInstance mi = c.findMethod(this.name, argTypes);
      Object r;
      if (mi.flags().isStatic()) {
         r = nf.CanonicalTypeNode(this.position(), mi.container()).type(mi.container());
      } else {
         ClassType scope = c.findMethodScope(this.name);
         if (!ts.equals(scope, c.currentClass())) {
            r = nf.This(this.position(), nf.CanonicalTypeNode(this.position(), scope)).type(scope);
         } else {
            r = nf.This(this.position()).type(scope);
         }
      }

      Receiver r = (Receiver)((Receiver)r).typeCheck(tc);
      return this.targetImplicit(true).target(r).del().typeCheck(tc);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      Context c = tc.context();
      List argTypes = new ArrayList(this.arguments.size());
      Iterator i = this.arguments.iterator();

      while(i.hasNext()) {
         Expr e = (Expr)i.next();
         argTypes.add(e.type());
      }

      if (this.target == null) {
         return this.typeCheckNullTarget(tc, argTypes);
      } else {
         ReferenceType targetType = this.findTargetType();
         MethodInstance mi = ts.findMethod(targetType, this.name, argTypes, (ClassType)c.currentClass());
         boolean staticContext = this.target instanceof TypeNode;
         if (staticContext && !mi.flags().isStatic()) {
            throw new SemanticException("Cannot call non-static method " + this.name + " of " + targetType + " in static " + "context.", this.position());
         } else if (this.target instanceof Special && ((Special)this.target).kind() == Special.SUPER && mi.flags().isAbstract()) {
            throw new SemanticException("Cannot call an abstract method of the super class", this.position());
         } else {
            Call_c call = (Call_c)this.methodInstance(mi).type(mi.returnType());
            call.checkConsistency(c);
            return call;
         }
      }
   }

   public ReferenceType findTargetType() throws SemanticException {
      Type t = this.target.type();
      if (t.isReference()) {
         return t.toReference();
      } else if (this.target instanceof Expr) {
         throw new SemanticException("Cannot invoke method \"" + this.name + "\" on " + "an expression of non-reference type " + t + ".", this.target.position());
      } else if (this.target instanceof TypeNode) {
         throw new SemanticException("Cannot invoke static method \"" + this.name + "\" on non-reference type " + t + ".", this.target.position());
      } else {
         throw new SemanticException("Receiver of method invocation must be a reference type.", this.target.position());
      }
   }

   public Type childExpectedType(Expr child, AscriptionVisitor av) {
      if (child == this.target) {
         return this.mi.container();
      } else {
         Iterator i = this.arguments.iterator();
         Iterator j = this.mi.formalTypes().iterator();

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
      StringBuffer sb = new StringBuffer();
      sb.append(this.targetImplicit ? "" : this.target.toString() + ".");
      sb.append(this.name);
      sb.append("(");
      int count = 0;
      Iterator i = this.arguments.iterator();

      while(i.hasNext()) {
         if (count++ > 2) {
            sb.append("...");
            break;
         }

         Expr n = (Expr)i.next();
         sb.append(n.toString());
         if (i.hasNext()) {
            sb.append(", ");
         }
      }

      sb.append(")");
      return sb.toString();
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (!this.targetImplicit) {
         if (this.target instanceof Expr) {
            this.printSubExpr((Expr)this.target, w, tr);
            w.write(".");
         } else if (this.target != null) {
            this.print(this.target, w, tr);
            w.write(".");
         }
      }

      w.write(this.name + "(");
      w.begin(0);
      Iterator i = this.arguments.iterator();

      while(i.hasNext()) {
         Expr e = (Expr)i.next();
         this.print(e, w, tr);
         if (i.hasNext()) {
            w.write(",");
            w.allowBreak(0, " ");
         }
      }

      w.end();
      w.write(")");
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
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(arguments " + this.arguments + ")");
      w.end();
   }

   public Term entry() {
      return this.target instanceof Expr ? ((Expr)this.target).entry() : listEntry(this.arguments, this);
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      if (this.target instanceof Expr) {
         Expr t = (Expr)this.target;
         v.visitCFG(t, (Term)listEntry(this.arguments, this));
      }

      v.visitCFGList(this.arguments, this);
      return succs;
   }

   public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
      if (this.mi == null) {
         throw new InternalCompilerError(this.position(), "Null method instance after type check.");
      } else {
         return super.exceptionCheck(ec);
      }
   }

   public List throwTypes(TypeSystem ts) {
      List l = new LinkedList();
      l.addAll(this.mi.throwTypes());
      l.addAll(ts.uncheckedExceptions());
      if (this.target instanceof Expr && !(this.target instanceof Special)) {
         l.add(ts.NullPointerException());
      }

      return l;
   }

   protected void checkConsistency(Context c) throws SemanticException {
      if (this.targetImplicit) {
         c.findMethod(this.name, this.mi.formalTypes());
      }

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
