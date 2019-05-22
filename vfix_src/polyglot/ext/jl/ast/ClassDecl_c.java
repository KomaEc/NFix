package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ConstructorCall;
import polyglot.ast.ConstructorDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.ast.TypeNode;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.Named;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AddMemberVisitor;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeBuilder;
import polyglot.visit.TypeChecker;

public class ClassDecl_c extends Term_c implements ClassDecl {
   protected Flags flags;
   protected String name;
   protected TypeNode superClass;
   protected List interfaces;
   protected ClassBody body;
   protected ParsedClassType type;
   // $FF: synthetic field
   static Class class$polyglot$ast$TypeNode;

   public ClassDecl_c(Position pos, Flags flags, String name, TypeNode superClass, List interfaces, ClassBody body) {
      super(pos);
      this.flags = flags;
      this.name = name;
      this.superClass = superClass;
      this.interfaces = TypedList.copyAndCheck(interfaces, class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, true);
      this.body = body;
   }

   public Named declaration() {
      return this.type();
   }

   public ParsedClassType type() {
      return this.type;
   }

   public ClassDecl type(ParsedClassType type) {
      ClassDecl_c n = (ClassDecl_c)this.copy();
      n.type = type;
      return n;
   }

   public Flags flags() {
      return this.flags;
   }

   public ClassDecl flags(Flags flags) {
      ClassDecl_c n = (ClassDecl_c)this.copy();
      n.flags = flags;
      return n;
   }

   public String name() {
      return this.name;
   }

   public ClassDecl name(String name) {
      ClassDecl_c n = (ClassDecl_c)this.copy();
      n.name = name;
      return n;
   }

   public TypeNode superClass() {
      return this.superClass;
   }

   public ClassDecl superClass(TypeNode superClass) {
      ClassDecl_c n = (ClassDecl_c)this.copy();
      n.superClass = superClass;
      return n;
   }

   public List interfaces() {
      return this.interfaces;
   }

   public ClassDecl interfaces(List interfaces) {
      ClassDecl_c n = (ClassDecl_c)this.copy();
      n.interfaces = TypedList.copyAndCheck(interfaces, class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, true);
      return n;
   }

   public ClassBody body() {
      return this.body;
   }

   public ClassDecl body(ClassBody body) {
      ClassDecl_c n = (ClassDecl_c)this.copy();
      n.body = body;
      return n;
   }

   protected ClassDecl_c reconstruct(TypeNode superClass, List interfaces, ClassBody body) {
      if (superClass == this.superClass && CollectionUtil.equals(interfaces, this.interfaces) && body == this.body) {
         return this;
      } else {
         ClassDecl_c n = (ClassDecl_c)this.copy();
         n.superClass = superClass;
         n.interfaces = TypedList.copyAndCheck(interfaces, class$polyglot$ast$TypeNode == null ? (class$polyglot$ast$TypeNode = class$("polyglot.ast.TypeNode")) : class$polyglot$ast$TypeNode, true);
         n.body = body;
         return n;
      }
   }

   public Term entry() {
      return this.body().entry();
   }

   public List acceptCFG(CFGBuilder v, List succs) {
      v.visitCFG(this.body(), (Term)this);
      return succs;
   }

   public Node visitChildren(NodeVisitor v) {
      TypeNode superClass = (TypeNode)this.visitChild(this.superClass, v);
      List interfaces = this.visitList(this.interfaces, v);
      ClassBody body = (ClassBody)this.visitChild(this.body, v);
      return this.reconstruct(superClass, interfaces, body);
   }

   public NodeVisitor buildTypesEnter(TypeBuilder tb) throws SemanticException {
      tb = tb.pushClass(this.position(), this.flags, this.name);
      ParsedClassType ct = tb.currentClass();
      if (ct.isMember() && ct.outer().flags().isInterface()) {
         ct.flags(ct.flags().Public().Static());
      }

      if (ct.isMember() && ct.flags().isInterface()) {
         ct.flags(ct.flags().Static());
      }

      if (ct.flags().isInterface()) {
         ct.flags(ct.flags().Abstract());
      }

      return tb;
   }

   public Node buildTypes(TypeBuilder tb) throws SemanticException {
      ParsedClassType type = tb.currentClass();
      return (Node)(type != null ? this.type(type).flags(type.flags()) : this);
   }

   public Context enterScope(Node child, Context c) {
      if (child == this.body) {
         TypeSystem ts = c.typeSystem();
         c = c.pushClass(this.type, ts.staticTarget(this.type).toClass());
      }

      return super.enterScope(child, c);
   }

   public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
      return (NodeVisitor)(ar.kind() == AmbiguityRemover.SUPER ? ar.bypass(this.body) : ar);
   }

   protected void disambiguateSuperType(AmbiguityRemover ar) throws SemanticException {
      TypeSystem ts = ar.typeSystem();
      if (this.superClass != null) {
         Type t = this.superClass.type();
         if (!t.isCanonical()) {
            throw new SemanticException("Could not disambiguate super class of " + this.type + ".", this.superClass.position());
         }

         if (!t.isClass() || t.toClass().flags().isInterface()) {
            throw new SemanticException("Super class " + t + " of " + this.type + " is not a class.", this.superClass.position());
         }

         if (Report.should_report((String)"types", 3)) {
            Report.report(3, "setting super type of " + this.type + " to " + t);
         }

         this.type.superType(t);
         ts.checkCycles(t.toReference());
      } else if (ts.Object() != this.type && !ts.Object().fullName().equals(this.type.fullName())) {
         this.type.superType(ts.Object());
      } else {
         this.type.superType((Type)null);
      }

   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      if (ar.kind() == AmbiguityRemover.SIGNATURES) {
         Context ctxt = ar.context();
         this.type().inStaticContext(ctxt.inStaticContext());
      }

      if (ar.kind() == AmbiguityRemover.SIGNATURES) {
         ar.addSuperDependencies(this.type());
      }

      if (ar.kind() != AmbiguityRemover.SUPER) {
         return this;
      } else {
         TypeSystem ts = ar.typeSystem();
         if (Report.should_report((String)"types", 2)) {
            Report.report(2, "Cleaning " + this.type + ".");
         }

         this.disambiguateSuperType(ar);

         Type t;
         for(Iterator i = this.interfaces.iterator(); i.hasNext(); ts.checkCycles(t.toReference())) {
            TypeNode tn = (TypeNode)i.next();
            t = tn.type();
            if (!t.isCanonical()) {
               throw new SemanticException("Could not disambiguate super class of " + this.type + ".", tn.position());
            }

            if (!t.isClass() || !t.toClass().flags().isInterface()) {
               throw new SemanticException("Interface " + t + " of " + this.type + " is not an interface.", tn.position());
            }

            if (Report.should_report((String)"types", 3)) {
               Report.report(3, "adding interface of " + this.type + " to " + t);
            }

            if (!this.type.interfaces().contains(t)) {
               this.type.addInterface(t);
            }
         }

         return this;
      }
   }

   public Node addMembers(AddMemberVisitor tc) throws SemanticException {
      TypeSystem ts = tc.typeSystem();
      NodeFactory nf = tc.nodeFactory();
      return this.addDefaultConstructorIfNeeded(ts, nf);
   }

   protected Node addDefaultConstructorIfNeeded(TypeSystem ts, NodeFactory nf) {
      return (Node)(this.defaultConstructorNeeded() ? this.addDefaultConstructor(ts, nf) : this);
   }

   protected boolean defaultConstructorNeeded() {
      return this.flags().isInterface() ? false : this.type().constructors().isEmpty();
   }

   protected Node addDefaultConstructor(TypeSystem ts, NodeFactory nf) {
      ConstructorInstance ci = ts.defaultConstructor(this.position(), this.type);
      this.type.addConstructor(ci);
      Block block = null;
      if (this.type.superType() instanceof ClassType) {
         ConstructorInstance sci = ts.defaultConstructor(this.position(), (ClassType)this.type.superType());
         ConstructorCall cc = nf.SuperCall(this.position(), Collections.EMPTY_LIST);
         cc = cc.constructorInstance(sci);
         block = nf.Block(this.position(), (Stmt)cc);
      } else {
         block = nf.Block(this.position());
      }

      ConstructorDecl cd = nf.ConstructorDecl(this.position(), Flags.PUBLIC, this.name, Collections.EMPTY_LIST, Collections.EMPTY_LIST, block);
      cd = cd.constructorInstance(ci);
      return this.body(this.body.addMember(cd));
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      if (this.type().isNested() && this.type() instanceof Named) {
         for(ClassType container = this.type.outer(); container instanceof Named; container = container.outer()) {
            if (!container.isAnonymous()) {
               String name = container.name();
               if (name.equals(this.name)) {
                  throw new SemanticException("Cannot declare member class \"" + this.type + "\" inside class with the " + "same name.", this.position());
               }
            }

            if (!container.isNested()) {
               break;
            }
         }

         if (this.type().isLocal()) {
            Context ctxt = tc.context();
            if (ctxt.isLocal(this.name)) {
               Named nm = ctxt.find(this.name);
               if (nm instanceof Type) {
                  Type another = (Type)nm;
                  if (another.isClass() && another.toClass().isLocal()) {
                     throw new SemanticException("Cannot declare local class \"" + this.type + "\" within the same " + "method, constructor or initializer as another " + "local class of the same name.", this.position());
                  }
               }
            }
         }
      }

      if (this.type().isMember() && this.flags().isInterface() && this.type().outer().isInnerClass()) {
         throw new SemanticException("Inner classes cannot declare member interfaces.", this.position());
      } else if (this.type().isMember() && this.type().flags().isStatic() && this.type().outer().isInnerClass()) {
         throw new SemanticException("Inner classes cannot declare static member classes.", this.position());
      } else {
         if (this.type.superType() != null) {
            if (!this.type.superType().isClass()) {
               throw new SemanticException("Cannot extend non-class \"" + this.type.superType() + "\".", this.position());
            }

            if (this.type.superType().toClass().flags().isFinal()) {
               throw new SemanticException("Cannot extend final class \"" + this.type.superType() + "\".", this.position());
            }
         }

         TypeSystem ts = tc.typeSystem();

         try {
            if (this.type.isTopLevel()) {
               ts.checkTopLevelClassFlags(this.type.flags());
            }

            if (this.type.isMember()) {
               ts.checkMemberClassFlags(this.type.flags());
            }

            if (this.type.isLocal()) {
               ts.checkLocalClassFlags(this.type.flags());
            }
         } catch (SemanticException var6) {
            throw new SemanticException(var6.getMessage(), this.position());
         }

         ts.checkClassConformance(this.type);
         return this;
      }
   }

   public String toString() {
      return this.flags.clearInterface().translate() + (this.flags.isInterface() ? "interface " : "class ") + this.name + " " + this.body;
   }

   public void prettyPrintHeader(CodeWriter w, PrettyPrinter tr) {
      if (this.flags.isInterface()) {
         w.write(this.flags.clearInterface().clearAbstract().translate());
      } else {
         w.write(this.flags.translate());
      }

      if (this.flags.isInterface()) {
         w.write("interface ");
      } else {
         w.write("class ");
      }

      w.write(this.name);
      if (this.superClass() != null) {
         w.write(" extends ");
         this.print(this.superClass(), w, tr);
      }

      if (!this.interfaces.isEmpty()) {
         if (this.flags.isInterface()) {
            w.write(" extends ");
         } else {
            w.write(" implements ");
         }

         Iterator i = this.interfaces().iterator();

         while(i.hasNext()) {
            TypeNode tn = (TypeNode)i.next();
            this.print(tn, w, tr);
            if (i.hasNext()) {
               w.write(", ");
            }
         }
      }

      w.write(" {");
   }

   public void prettyPrintFooter(CodeWriter w, PrettyPrinter tr) {
      w.write("}");
      w.newline(0);
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      this.prettyPrintHeader(w, tr);
      this.print(this.body(), w, tr);
      this.prettyPrintFooter(w, tr);
   }

   public void dump(CodeWriter w) {
      super.dump(w);
      w.allowBreak(4, " ");
      w.begin(0);
      w.write("(name " + this.name + ")");
      w.end();
      if (this.type != null) {
         w.allowBreak(4, " ");
         w.begin(0);
         w.write("(type " + this.type + ")");
         w.end();
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
