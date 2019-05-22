package polyglot.ext.jl.ast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.ClassBody;
import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.MethodInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.util.TypedList;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;

public class ClassBody_c extends Term_c implements ClassBody {
   protected List members;
   private static final Collection TOPICS = CollectionUtil.list("types", "context");
   // $FF: synthetic field
   static Class class$polyglot$ast$ClassMember;

   public ClassBody_c(Position pos, List members) {
      super(pos);
      this.members = TypedList.copyAndCheck(members, class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, true);
   }

   public List members() {
      return this.members;
   }

   public ClassBody members(List members) {
      ClassBody_c n = (ClassBody_c)this.copy();
      n.members = TypedList.copyAndCheck(members, class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, true);
      return n;
   }

   public ClassBody addMember(ClassMember member) {
      ClassBody_c n = (ClassBody_c)this.copy();
      List l = new ArrayList(this.members.size() + 1);
      l.addAll(this.members);
      l.add(member);
      n.members = TypedList.copyAndCheck(l, class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, true);
      return n;
   }

   protected ClassBody_c reconstruct(List members) {
      if (!CollectionUtil.equals(members, this.members)) {
         ClassBody_c n = (ClassBody_c)this.copy();
         n.members = TypedList.copyAndCheck(members, class$polyglot$ast$ClassMember == null ? (class$polyglot$ast$ClassMember = class$("polyglot.ast.ClassMember")) : class$polyglot$ast$ClassMember, true);
         return n;
      } else {
         return this;
      }
   }

   public Node visitChildren(NodeVisitor v) {
      List members = this.visitList(this.members, v);
      return this.reconstruct(members);
   }

   public NodeVisitor disambiguateEnter(AmbiguityRemover ar) throws SemanticException {
      return (NodeVisitor)(ar.kind() != AmbiguityRemover.SUPER && ar.kind() != AmbiguityRemover.SIGNATURES ? ar : ar.bypassChildren(this));
   }

   public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
      if (ar.kind() == AmbiguityRemover.SIGNATURES) {
         List l = new ArrayList(this.members.size());
         Job j = ar.job();
         Iterator i = this.members.iterator();

         while(i.hasNext()) {
            ClassMember n = (ClassMember)i.next();
            if (n instanceof ClassDecl) {
               Job sj = j.spawn(ar.context(), n, Pass.CLEAN_SUPER, Pass.CLEAN_SUPER_ALL);
               if (!sj.status()) {
                  if (!sj.reportedErrors()) {
                     throw new SemanticException("Could not disambiguate class member.", n.position());
                  }

                  throw new SemanticException();
               }

               ClassDecl m = (ClassDecl)sj.ast();
               l.add(m.visit(ar.visitChildren()));
            } else {
               l.add(n.visit(ar.visitChildren()));
            }
         }

         return this.members(l);
      } else {
         return this;
      }
   }

   public String toString() {
      return "{ ... }";
   }

   protected void duplicateFieldCheck(TypeChecker tc) throws SemanticException {
      ClassType type = tc.context().currentClass();
      ArrayList l = new ArrayList(type.fields());

      for(int i = 0; i < l.size(); ++i) {
         FieldInstance fi = (FieldInstance)l.get(i);

         for(int j = i + 1; j < l.size(); ++j) {
            FieldInstance fj = (FieldInstance)l.get(j);
            if (fi.name().equals(fj.name())) {
               throw new SemanticException("Duplicate field \"" + fj + "\".", fj.position());
            }
         }
      }

   }

   protected void duplicateConstructorCheck(TypeChecker tc) throws SemanticException {
      ClassType type = tc.context().currentClass();
      ArrayList l = new ArrayList(type.constructors());

      for(int i = 0; i < l.size(); ++i) {
         ConstructorInstance ci = (ConstructorInstance)l.get(i);

         for(int j = i + 1; j < l.size(); ++j) {
            ConstructorInstance cj = (ConstructorInstance)l.get(j);
            if (ci.hasFormals(cj.formalTypes())) {
               throw new SemanticException("Duplicate constructor \"" + cj + "\".", cj.position());
            }
         }
      }

   }

   protected void duplicateMethodCheck(TypeChecker tc) throws SemanticException {
      ClassType type = tc.context().currentClass();
      TypeSystem ts = tc.typeSystem();
      ArrayList l = new ArrayList(type.methods());

      for(int i = 0; i < l.size(); ++i) {
         MethodInstance mi = (MethodInstance)l.get(i);

         for(int j = i + 1; j < l.size(); ++j) {
            MethodInstance mj = (MethodInstance)l.get(j);
            if (this.isSameMethod(ts, mi, mj)) {
               throw new SemanticException("Duplicate method \"" + mj + "\".", mj.position());
            }
         }
      }

   }

   protected void duplicateMemberClassCheck(TypeChecker tc) throws SemanticException {
      ClassType type = tc.context().currentClass();
      TypeSystem ts = tc.typeSystem();
      ArrayList l = new ArrayList(type.memberClasses());

      for(int i = 0; i < l.size(); ++i) {
         ClassType mi = (ClassType)l.get(i);

         for(int j = i + 1; j < l.size(); ++j) {
            ClassType mj = (ClassType)l.get(j);
            if (mi.name().equals(mj.name())) {
               throw new SemanticException("Duplicate member type \"" + mj + "\".", mj.position());
            }
         }
      }

   }

   protected boolean isSameMethod(TypeSystem ts, MethodInstance mi, MethodInstance mj) {
      return mi.isSameMethod(mj);
   }

   public Node typeCheck(TypeChecker tc) throws SemanticException {
      this.duplicateFieldCheck(tc);
      this.duplicateConstructorCheck(tc);
      this.duplicateMethodCheck(tc);
      this.duplicateMemberClassCheck(tc);
      return this;
   }

   public NodeVisitor exceptionCheckEnter(ExceptionChecker ec) throws SemanticException {
      return ec.pushNew();
   }

   public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
      if (!this.members.isEmpty()) {
         w.newline(4);
         w.begin(0);
         Iterator i = this.members.iterator();

         while(i.hasNext()) {
            ClassMember member = (ClassMember)i.next();
            this.printBlock(member, w, tr);
            if (i.hasNext()) {
               w.newline(0);
               w.newline(0);
            }
         }

         w.end();
         w.newline(0);
      }

   }

   public Term entry() {
      return this;
   }

   public List acceptCFG(CFGBuilder v, List succs) {
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
