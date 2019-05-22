package polyglot.visit;

import java.util.Stack;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.CachingResolver;
import polyglot.types.ClassType;
import polyglot.types.Context;
import polyglot.types.Flags;
import polyglot.types.ImportTable;
import polyglot.types.Named;
import polyglot.types.Package;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;

public class TypeBuilder extends HaltingVisitor {
   protected ImportTable importTable;
   protected Job job;
   protected TypeSystem ts;
   protected NodeFactory nf;
   protected TypeBuilder outer;
   protected boolean inCode;
   protected boolean global;
   protected ParsedClassType type;

   public TypeBuilder(Job job, TypeSystem ts, NodeFactory nf) {
      this.job = job;
      this.ts = ts;
      this.nf = nf;
      this.outer = null;
   }

   public TypeBuilder push() {
      TypeBuilder tb = (TypeBuilder)this.copy();
      tb.outer = this;
      return tb;
   }

   public TypeBuilder pop() {
      return this.outer;
   }

   public Job job() {
      return this.job;
   }

   public ErrorQueue errorQueue() {
      return this.job.compiler().errorQueue();
   }

   public NodeFactory nodeFactory() {
      return this.nf;
   }

   public TypeSystem typeSystem() {
      return this.ts;
   }

   public NodeVisitor begin() {
      Context context = this.job.context();
      if (context == null) {
         return this;
      } else {
         Stack s = new Stack();
         ParsedClassType ct = context.currentClassScope();

         while(ct != null) {
            s.push(ct);
            if (ct.isNested()) {
               ct = (ParsedClassType)ct.outer();
            } else {
               ct = null;
            }
         }

         if (context.importTable() != null) {
            this.setImportTable(context.importTable());
         }

         TypeBuilder tb = this;

         while(!s.isEmpty()) {
            ParsedClassType ct = (ParsedClassType)s.pop();

            try {
               tb = tb.pushClass(ct);
            } catch (SemanticException var6) {
               this.errorQueue().enqueue(5, var6.getMessage(), ct.position());
               return null;
            }

            if (ct.isLocal() || ct.isAnonymous()) {
               tb = tb.pushCode();
            }
         }

         return tb;
      }
   }

   public NodeVisitor enter(Node n) {
      try {
         return n.del().buildTypesEnter(this);
      } catch (SemanticException var4) {
         Position position = var4.position();
         if (position == null) {
            position = n.position();
         }

         if (var4.getMessage() != null) {
            this.errorQueue().enqueue(5, var4.getMessage(), position);
         }

         return this;
      }
   }

   public Node leave(Node old, Node n, NodeVisitor v) {
      try {
         return n.del().buildTypes((TypeBuilder)v);
      } catch (SemanticException var6) {
         Position position = var6.position();
         if (position == null) {
            position = n.position();
         }

         if (var6.getMessage() != null) {
            this.errorQueue().enqueue(5, var6.getMessage(), position);
         }

         return n;
      }
   }

   public TypeBuilder pushCode() {
      if (Report.should_report((String)"visit", 4)) {
         Report.report(4, "TB pushing code: " + this);
      }

      TypeBuilder tb = this.push();
      tb.inCode = true;
      tb.global = false;
      return tb;
   }

   protected TypeBuilder pushClass(ParsedClassType type) throws SemanticException {
      if (Report.should_report((String)"visit", 4)) {
         Report.report(4, "TB pushing class " + type + ": " + this);
      }

      TypeBuilder tb = this.push();
      tb.type = type;
      tb.inCode = false;
      if (this.importTable() != null && type.isTopLevel()) {
         tb.importTable().addClassImport(type.fullName());
      }

      return tb;
   }

   protected ParsedClassType newClass(Position pos, Flags flags, String name) throws SemanticException {
      TypeSystem ts = this.typeSystem();
      ParsedClassType ct = ts.createClassType(this.job.source());
      if (this.inCode) {
         ct.kind(ClassType.LOCAL);
         ct.outer(this.currentClass());
         ct.flags(flags);
         ct.name(name);
         ct.position(pos);
         if (this.currentPackage() != null) {
            ct.package_(this.currentPackage());
         }

         return ct;
      } else if (this.currentClass() != null) {
         ct.kind(ClassType.MEMBER);
         ct.outer(this.currentClass());
         ct.flags(flags);
         ct.name(name);
         ct.position(pos);
         this.currentClass().addMemberClass(ct);
         if (this.currentPackage() != null) {
            ct.package_(this.currentPackage());
         }

         ClassType container = ct.outer();

         boolean allMembers;
         for(allMembers = container.isMember() || container.isTopLevel(); container.isMember(); allMembers = allMembers && (container.isMember() || container.isTopLevel())) {
            container = container.outer();
         }

         if (allMembers) {
            this.typeSystem().parsedResolver().addNamed(this.typeSystem().getTransformedClassName(ct), ct);
         }

         return ct;
      } else {
         ct.kind(ClassType.TOP_LEVEL);
         ct.flags(flags);
         ct.name(name);
         ct.position(pos);
         if (this.currentPackage() != null) {
            ct.package_(this.currentPackage());
         }

         Named dup = ((CachingResolver)this.typeSystem().systemResolver()).check(ct.fullName());
         if (dup != null && dup.fullName().equals(ct.fullName())) {
            throw new SemanticException("Duplicate class \"" + ct.fullName() + "\".", pos);
         } else {
            this.typeSystem().parsedResolver().addNamed(ct.fullName(), ct);
            ((CachingResolver)this.typeSystem().systemResolver()).addNamed(ct.fullName(), ct);
            return ct;
         }
      }
   }

   public TypeBuilder pushAnonClass(Position pos) throws SemanticException {
      if (Report.should_report((String)"visit", 4)) {
         Report.report(4, "TB pushing anon class: " + this);
      }

      if (!this.inCode) {
         throw new InternalCompilerError("Can only push an anonymous class within code.");
      } else {
         TypeSystem ts = this.typeSystem();
         ParsedClassType ct = ts.createClassType(this.job().source());
         ct.kind(ClassType.ANONYMOUS);
         ct.outer(this.currentClass());
         ct.position(pos);
         if (this.currentPackage() != null) {
            ct.package_(this.currentPackage());
         }

         return this.pushClass(ct);
      }
   }

   public TypeBuilder pushClass(Position pos, Flags flags, String name) throws SemanticException {
      ParsedClassType t = this.newClass(pos, flags, name);
      return this.pushClass(t);
   }

   public ParsedClassType currentClass() {
      return this.type;
   }

   public Package currentPackage() {
      return this.importTable() == null ? null : this.importTable.package_();
   }

   public ImportTable importTable() {
      return this.importTable;
   }

   public void setImportTable(ImportTable it) {
      this.importTable = it;
   }

   public String toString() {
      return "(TB " + this.type + (this.inCode ? " inCode" : "") + (this.global ? " global" : "") + (this.outer == null ? ")" : " " + this.outer.toString() + ")");
   }
}
