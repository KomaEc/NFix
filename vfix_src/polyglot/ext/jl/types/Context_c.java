package polyglot.ext.jl.types;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.CodeInstance;
import polyglot.types.Context;
import polyglot.types.FieldInstance;
import polyglot.types.ImportTable;
import polyglot.types.LocalInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Named;
import polyglot.types.NoMemberException;
import polyglot.types.Package;
import polyglot.types.ParsedClassType;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.types.VarInstance;
import polyglot.util.CollectionUtil;
import polyglot.util.Enum;
import polyglot.util.InternalCompilerError;

public class Context_c implements Context {
   protected Context outer;
   protected TypeSystem ts;
   public static final Context_c.Kind BLOCK = new Context_c.Kind("block");
   public static final Context_c.Kind CLASS = new Context_c.Kind("class");
   public static final Context_c.Kind CODE = new Context_c.Kind("code");
   public static final Context_c.Kind OUTER = new Context_c.Kind("outer");
   public static final Context_c.Kind SOURCE = new Context_c.Kind("source");
   protected ImportTable it;
   protected Context_c.Kind kind;
   protected ClassType type;
   protected ParsedClassType scope;
   protected CodeInstance code;
   protected Map types;
   protected Map vars;
   protected boolean inCode;
   protected boolean staticContext;
   private static final Collection TOPICS = CollectionUtil.list("types", "context");

   public Context_c(TypeSystem ts) {
      this.ts = ts;
      this.outer = null;
      this.kind = OUTER;
   }

   public boolean isBlock() {
      return this.kind == BLOCK;
   }

   public boolean isClass() {
      return this.kind == CLASS;
   }

   public boolean isCode() {
      return this.kind == CODE;
   }

   public boolean isOuter() {
      return this.kind == OUTER;
   }

   public boolean isSource() {
      return this.kind == SOURCE;
   }

   public TypeSystem typeSystem() {
      return this.ts;
   }

   public Object copy() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         throw new InternalCompilerError("Java clone() weirdness.");
      }
   }

   protected Context_c push() {
      Context_c v = (Context_c)this.copy();
      v.outer = this;
      v.types = null;
      v.vars = null;
      return v;
   }

   public Resolver outerResolver() {
      return (Resolver)(this.it != null ? this.it : this.ts.systemResolver());
   }

   public ImportTable importTable() {
      return this.it;
   }

   public Package package_() {
      return this.importTable().package_();
   }

   public boolean isLocal(String name) {
      if (this.isClass()) {
         return false;
      } else if (!this.isBlock() && !this.isCode() || this.findVariableInThisScope(name) == null && this.findInThisScope(name) == null) {
         return this.outer == null ? false : this.outer.isLocal(name);
      } else {
         return true;
      }
   }

   public MethodInstance findMethod(String name, List argTypes) throws SemanticException {
      if (Report.should_report((Collection)TOPICS, 3)) {
         Report.report(3, "find-method " + name + argTypes + " in " + this);
      }

      if (this.currentClass() != null && this.ts.hasMethodNamed(this.currentClass(), name)) {
         if (Report.should_report((Collection)TOPICS, 3)) {
            Report.report(3, "find-method " + name + argTypes + " -> " + this.currentClass());
         }

         return this.ts.findMethod(this.currentClass(), name, argTypes, (ClassType)this.currentClass());
      } else if (this.outer != null) {
         return this.outer.findMethod(name, argTypes);
      } else {
         throw new SemanticException("Method " + name + " not found.");
      }
   }

   public LocalInstance findLocal(String name) throws SemanticException {
      VarInstance vi = this.findVariableSilent(name);
      if (vi instanceof LocalInstance) {
         return (LocalInstance)vi;
      } else {
         throw new SemanticException("Local " + name + " not found.");
      }
   }

   public ClassType findFieldScope(String name) throws SemanticException {
      if (Report.should_report((Collection)TOPICS, 3)) {
         Report.report(3, "find-field-scope " + name + " in " + this);
      }

      VarInstance vi = this.findVariableInThisScope(name);
      if (vi instanceof FieldInstance) {
         if (Report.should_report((Collection)TOPICS, 3)) {
            Report.report(3, "find-field-scope " + name + " in " + vi);
         }

         return this.type;
      } else if (vi == null && this.outer != null) {
         return this.outer.findFieldScope(name);
      } else {
         throw new SemanticException("Field " + name + " not found.");
      }
   }

   public ClassType findMethodScope(String name) throws SemanticException {
      if (Report.should_report((Collection)TOPICS, 3)) {
         Report.report(3, "find-method-scope " + name + " in " + this);
      }

      if (this.currentClass() != null && this.ts.hasMethodNamed(this.currentClass(), name)) {
         if (Report.should_report((Collection)TOPICS, 3)) {
            Report.report(3, "find-method-scope " + name + " -> " + this.currentClass());
         }

         return this.currentClass();
      } else if (this.outer != null) {
         return this.outer.findMethodScope(name);
      } else {
         throw new SemanticException("Method " + name + " not found.");
      }
   }

   public FieldInstance findField(String name) throws SemanticException {
      VarInstance vi = this.findVariableSilent(name);
      if (vi instanceof FieldInstance) {
         FieldInstance fi = (FieldInstance)vi;
         if (!this.ts.isAccessible(fi, this)) {
            throw new SemanticException("Field " + name + " not accessible.");
         } else {
            if (Report.should_report((Collection)TOPICS, 3)) {
               Report.report(3, "find-field " + name + " -> " + fi);
            }

            return fi;
         }
      } else {
         throw new NoMemberException(3, "Field " + name + " not found.");
      }
   }

   public VarInstance findVariable(String name) throws SemanticException {
      VarInstance vi = this.findVariableSilent(name);
      if (vi != null) {
         if (Report.should_report((Collection)TOPICS, 3)) {
            Report.report(3, "find-var " + name + " -> " + vi);
         }

         return vi;
      } else {
         throw new SemanticException("Variable " + name + " not found.");
      }
   }

   public VarInstance findVariableSilent(String name) {
      if (Report.should_report((Collection)TOPICS, 3)) {
         Report.report(3, "find-var " + name + " in " + this);
      }

      VarInstance vi = this.findVariableInThisScope(name);
      if (vi != null) {
         if (Report.should_report((Collection)TOPICS, 3)) {
            Report.report(3, "find-var " + name + " -> " + vi);
         }

         return vi;
      } else {
         return this.outer != null ? this.outer.findVariableSilent(name) : null;
      }
   }

   protected String mapsToString() {
      return "types=" + this.types + " vars=" + this.vars;
   }

   public String toString() {
      return "(" + this.kind + " " + this.mapsToString() + " " + this.outer + ")";
   }

   public Context pop() {
      return this.outer;
   }

   public Named find(String name) throws SemanticException {
      if (Report.should_report((Collection)TOPICS, 3)) {
         Report.report(3, "find-type " + name + " in " + this);
      }

      if (this.isOuter()) {
         return this.outerResolver().find(name);
      } else if (this.isSource()) {
         return this.it.find(name);
      } else {
         Named type = this.findInThisScope(name);
         if (type != null) {
            if (Report.should_report((Collection)TOPICS, 3)) {
               Report.report(3, "find " + name + " -> " + type);
            }

            return type;
         } else if (this.outer != null) {
            return this.outer.find(name);
         } else {
            throw new SemanticException("Type " + name + " not found.");
         }
      }
   }

   public Context pushSource(ImportTable it) {
      Context_c v = this.push();
      v.kind = SOURCE;
      v.it = it;
      v.inCode = false;
      v.staticContext = false;
      return v;
   }

   public Context pushClass(ParsedClassType classScope, ClassType type) {
      if (Report.should_report((Collection)TOPICS, 4)) {
         Report.report(4, "push class " + classScope + " " + classScope.position());
      }

      Context_c v = this.push();
      v.kind = CLASS;
      v.scope = classScope;
      v.type = type;
      v.inCode = false;
      v.staticContext = false;
      if (!type.isAnonymous()) {
         v.addNamed(type);
      }

      return v;
   }

   public Context pushBlock() {
      if (Report.should_report((Collection)TOPICS, 4)) {
         Report.report(4, "push block");
      }

      Context_c v = this.push();
      v.kind = BLOCK;
      return v;
   }

   public Context pushStatic() {
      if (Report.should_report((Collection)TOPICS, 4)) {
         Report.report(4, "push static");
      }

      Context_c v = this.push();
      v.staticContext = true;
      return v;
   }

   public Context pushCode(CodeInstance ci) {
      if (Report.should_report((Collection)TOPICS, 4)) {
         Report.report(4, "push code " + ci + " " + ci.position());
      }

      Context_c v = this.push();
      v.kind = CODE;
      v.code = ci;
      v.inCode = true;
      v.staticContext = ci.flags().isStatic();
      return v;
   }

   public CodeInstance currentCode() {
      return this.code;
   }

   public boolean inCode() {
      return this.inCode;
   }

   public boolean inStaticContext() {
      return this.staticContext;
   }

   public ClassType currentClass() {
      return this.type;
   }

   public ParsedClassType currentClassScope() {
      return this.scope;
   }

   public void addVariable(VarInstance vi) {
      if (Report.should_report((Collection)TOPICS, 3)) {
         Report.report(3, "Adding " + vi + " to context.");
      }

      this.addVariableToThisScope(vi);
   }

   /** @deprecated */
   public void addMethod(MethodInstance mi) {
      if (Report.should_report((Collection)TOPICS, 3)) {
         Report.report(3, "Adding " + mi + " to context.");
      }

   }

   public void addNamed(Named t) {
      if (Report.should_report((Collection)TOPICS, 3)) {
         Report.report(3, "Adding type " + t + " to context.");
      }

      this.addNamedToThisScope(t);
   }

   public Named findInThisScope(String name) {
      Named t = null;
      if (this.types != null) {
         t = (Named)this.types.get(name);
      }

      if (t == null && this.isClass()) {
         if (!this.type.isAnonymous() && this.type.name().equals(name)) {
            return this.type;
         }

         try {
            return this.ts.findMemberClass(this.type, name, this.type);
         } catch (SemanticException var4) {
         }
      }

      return t;
   }

   public void addNamedToThisScope(Named type) {
      if (this.types == null) {
         this.types = new HashMap();
      }

      this.types.put(type.name(), type);
   }

   public ClassType findMethodContainerInThisScope(String name) {
      return this.isClass() && this.ts.hasMethodNamed(this.currentClass(), name) ? this.type : null;
   }

   public VarInstance findVariableInThisScope(String name) {
      VarInstance vi = null;
      if (this.vars != null) {
         vi = (VarInstance)this.vars.get(name);
      }

      if (vi == null && this.isClass()) {
         try {
            return this.ts.findField(this.type, name, (ClassType)this.type);
         } catch (SemanticException var4) {
            return null;
         }
      } else {
         return vi;
      }
   }

   public void addVariableToThisScope(VarInstance var) {
      if (this.vars == null) {
         this.vars = new HashMap();
      }

      this.vars.put(var.name(), var);
   }

   public static class Kind extends Enum {
      public Kind(String name) {
         super(name);
      }
   }
}
