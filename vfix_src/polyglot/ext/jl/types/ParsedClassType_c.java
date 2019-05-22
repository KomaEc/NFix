package polyglot.ext.jl.types;

import java.util.LinkedList;
import java.util.List;
import polyglot.frontend.Source;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.LazyClassInitializer;
import polyglot.types.MethodInstance;
import polyglot.types.Package;
import polyglot.types.ParsedClassType;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.TypedList;

public class ParsedClassType_c extends ClassType_c implements ParsedClassType {
   protected transient LazyClassInitializer init;
   protected transient Source fromSource;
   protected Type superType;
   protected List interfaces;
   protected List methods;
   protected List fields;
   protected List constructors;
   protected List memberClasses;
   protected Package package_;
   protected Flags flags;
   protected ClassType.Kind kind;
   protected String name;
   protected ClassType outer;
   protected boolean inStaticContext = false;
   // $FF: synthetic field
   static Class class$polyglot$types$ConstructorInstance;
   // $FF: synthetic field
   static Class class$polyglot$types$Type;
   // $FF: synthetic field
   static Class class$polyglot$types$MethodInstance;
   // $FF: synthetic field
   static Class class$polyglot$types$FieldInstance;

   protected ParsedClassType_c() {
   }

   public ParsedClassType_c(TypeSystem ts, LazyClassInitializer init, Source fromSource) {
      super(ts);
      this.init = init;
      this.fromSource = fromSource;
      if (init == null) {
         throw new InternalCompilerError("Null lazy class initializer");
      }
   }

   public Source fromSource() {
      return this.fromSource;
   }

   public ClassType.Kind kind() {
      return this.kind;
   }

   public void inStaticContext(boolean inStaticContext) {
      this.inStaticContext = inStaticContext;
   }

   public boolean inStaticContext() {
      return this.inStaticContext;
   }

   public ClassType outer() {
      if (this.isTopLevel()) {
         return null;
      } else if (this.outer == null) {
         throw new InternalCompilerError("Nested classes must have outer classes.");
      } else {
         return this.outer;
      }
   }

   public String name() {
      if (this.isAnonymous()) {
         throw new InternalCompilerError("Anonymous classes cannot have names.");
      } else if (this.name == null) {
         throw new InternalCompilerError("Non-anonymous classes must have names.");
      } else {
         return this.name;
      }
   }

   public Type superType() {
      return this.superType;
   }

   public Package package_() {
      return this.package_;
   }

   public Flags flags() {
      return this.isAnonymous() ? Flags.NONE : this.flags;
   }

   protected boolean initialized() {
      return this.methods != null && this.constructors != null && this.fields != null && this.memberClasses != null && this.interfaces != null;
   }

   protected void freeInit() {
      if (this.initialized()) {
         this.init = null;
      } else if (this.init == null) {
         throw new InternalCompilerError("Null lazy class initializer");
      }

   }

   public void flags(Flags flags) {
      this.flags = flags;
   }

   public void kind(ClassType.Kind kind) {
      this.kind = kind;
   }

   public void outer(ClassType outer) {
      if (this.isTopLevel()) {
         throw new InternalCompilerError("Top-level classes cannot have outer classes.");
      } else {
         this.outer = outer;
      }
   }

   public void name(String name) {
      if (this.isAnonymous()) {
         throw new InternalCompilerError("Anonymous classes cannot have names.");
      } else {
         this.name = name;
      }
   }

   public void position(Position pos) {
      this.position = pos;
   }

   public void package_(Package p) {
      this.package_ = p;
   }

   public void superType(Type t) {
      this.superType = t;
   }

   public void addInterface(Type t) {
      this.interfaces().add(t);
   }

   public void addMethod(MethodInstance mi) {
      this.methods().add(mi);
   }

   public void addConstructor(ConstructorInstance ci) {
      this.constructors().add(ci);
   }

   public void addField(FieldInstance fi) {
      this.fields().add(fi);
   }

   public void addMemberClass(ClassType t) {
      this.memberClasses().add(t);
   }

   public List constructors() {
      if (this.constructors == null) {
         this.constructors = new TypedList(new LinkedList(), class$polyglot$types$ConstructorInstance == null ? (class$polyglot$types$ConstructorInstance = class$("polyglot.types.ConstructorInstance")) : class$polyglot$types$ConstructorInstance, false);
         this.init.initConstructors(this);
         this.freeInit();
      }

      return this.constructors;
   }

   public List memberClasses() {
      if (this.memberClasses == null) {
         this.memberClasses = new TypedList(new LinkedList(), class$polyglot$types$Type == null ? (class$polyglot$types$Type = class$("polyglot.types.Type")) : class$polyglot$types$Type, false);
         this.init.initMemberClasses(this);
         this.freeInit();
      }

      return this.memberClasses;
   }

   public List methods() {
      if (this.methods == null) {
         this.methods = new TypedList(new LinkedList(), class$polyglot$types$MethodInstance == null ? (class$polyglot$types$MethodInstance = class$("polyglot.types.MethodInstance")) : class$polyglot$types$MethodInstance, false);
         this.init.initMethods(this);
         this.freeInit();
      }

      return this.methods;
   }

   public List fields() {
      if (this.fields == null) {
         this.fields = new TypedList(new LinkedList(), class$polyglot$types$FieldInstance == null ? (class$polyglot$types$FieldInstance = class$("polyglot.types.FieldInstance")) : class$polyglot$types$FieldInstance, false);
         this.init.initFields(this);
         this.freeInit();
      }

      return this.fields;
   }

   public List interfaces() {
      if (this.interfaces == null) {
         this.interfaces = new TypedList(new LinkedList(), class$polyglot$types$Type == null ? (class$polyglot$types$Type = class$("polyglot.types.Type")) : class$polyglot$types$Type, false);
         this.init.initInterfaces(this);
         this.freeInit();
      }

      return this.interfaces;
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
