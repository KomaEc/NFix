package polyglot.ext.jl.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Source;
import polyglot.main.Report;
import polyglot.types.ArrayType;
import polyglot.types.CachingResolver;
import polyglot.types.ClassContextResolver;
import polyglot.types.ClassType;
import polyglot.types.CompoundResolver;
import polyglot.types.ConstructorInstance;
import polyglot.types.Context;
import polyglot.types.FieldInstance;
import polyglot.types.Flags;
import polyglot.types.ImportTable;
import polyglot.types.InitializerInstance;
import polyglot.types.LazyClassInitializer;
import polyglot.types.LoadedClassResolver;
import polyglot.types.LocalInstance;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.types.Named;
import polyglot.types.NoClassException;
import polyglot.types.NoMemberException;
import polyglot.types.NullType;
import polyglot.types.Package;
import polyglot.types.PackageContextResolver;
import polyglot.types.ParsedClassType;
import polyglot.types.PrimitiveType;
import polyglot.types.ProcedureInstance;
import polyglot.types.ReferenceType;
import polyglot.types.Resolver;
import polyglot.types.SemanticException;
import polyglot.types.TableResolver;
import polyglot.types.TopLevelResolver;
import polyglot.types.Type;
import polyglot.types.TypeObject;
import polyglot.types.TypeSystem;
import polyglot.types.UnknownPackage;
import polyglot.types.UnknownQualifier;
import polyglot.types.UnknownType;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.StringUtil;

public class TypeSystem_c implements TypeSystem {
   protected TopLevelResolver systemResolver;
   protected TableResolver parsedResolver;
   protected LoadedClassResolver loadedResolver;
   protected Map flagsForName;
   protected ClassType OBJECT_;
   protected ClassType CLASS_;
   protected ClassType STRING_;
   protected ClassType THROWABLE_;
   protected final NullType NULL_ = this.createNull();
   protected final PrimitiveType VOID_;
   protected final PrimitiveType BOOLEAN_;
   protected final PrimitiveType CHAR_;
   protected final PrimitiveType BYTE_;
   protected final PrimitiveType SHORT_;
   protected final PrimitiveType INT_;
   protected final PrimitiveType LONG_;
   protected final PrimitiveType FLOAT_;
   protected final PrimitiveType DOUBLE_;
   protected UnknownType unknownType;
   protected UnknownPackage unknownPackage;
   protected UnknownQualifier unknownQualifier;
   protected LazyClassInitializer defaultClassInit;
   protected final Flags ACCESS_FLAGS;
   protected final Flags LOCAL_FLAGS;
   protected final Flags FIELD_FLAGS;
   protected final Flags CONSTRUCTOR_FLAGS;
   protected final Flags INITIALIZER_FLAGS;
   protected final Flags METHOD_FLAGS;
   protected final Flags TOP_LEVEL_CLASS_FLAGS;
   protected final Flags MEMBER_CLASS_FLAGS;
   protected final Flags LOCAL_CLASS_FLAGS;

   public TypeSystem_c() {
      this.VOID_ = this.createPrimitive(PrimitiveType.VOID);
      this.BOOLEAN_ = this.createPrimitive(PrimitiveType.BOOLEAN);
      this.CHAR_ = this.createPrimitive(PrimitiveType.CHAR);
      this.BYTE_ = this.createPrimitive(PrimitiveType.BYTE);
      this.SHORT_ = this.createPrimitive(PrimitiveType.SHORT);
      this.INT_ = this.createPrimitive(PrimitiveType.INT);
      this.LONG_ = this.createPrimitive(PrimitiveType.LONG);
      this.FLOAT_ = this.createPrimitive(PrimitiveType.FLOAT);
      this.DOUBLE_ = this.createPrimitive(PrimitiveType.DOUBLE);
      this.unknownType = new UnknownType_c(this);
      this.unknownPackage = new UnknownPackage_c(this);
      this.unknownQualifier = new UnknownQualifier_c(this);
      this.ACCESS_FLAGS = this.Public().Protected().Private();
      this.LOCAL_FLAGS = this.Final();
      this.FIELD_FLAGS = this.ACCESS_FLAGS.Static().Final().Transient().Volatile();
      this.CONSTRUCTOR_FLAGS = this.ACCESS_FLAGS.Synchronized().Native();
      this.INITIALIZER_FLAGS = this.Static();
      this.METHOD_FLAGS = this.ACCESS_FLAGS.Abstract().Static().Final().Native().Synchronized().StrictFP();
      this.TOP_LEVEL_CLASS_FLAGS = this.ACCESS_FLAGS.clear(this.Private()).Abstract().Final().StrictFP().Interface();
      this.MEMBER_CLASS_FLAGS = this.ACCESS_FLAGS.Static().Abstract().Final().StrictFP().Interface();
      this.LOCAL_CLASS_FLAGS = this.TOP_LEVEL_CLASS_FLAGS.clear(this.ACCESS_FLAGS);
   }

   public void initialize(LoadedClassResolver loadedResolver, ExtensionInfo extInfo) throws SemanticException {
      if (Report.should_report((String)"types", 1)) {
         Report.report(1, "Initializing " + this.getClass().getName());
      }

      this.parsedResolver = new TableResolver();
      this.loadedResolver = loadedResolver;
      CompoundResolver compoundResolver = new CompoundResolver(this.parsedResolver, loadedResolver);
      this.systemResolver = new CachingResolver(compoundResolver, extInfo);
      this.initFlags();
      this.initTypes();
   }

   protected void initTypes() throws SemanticException {
   }

   public TopLevelResolver systemResolver() {
      return this.systemResolver;
   }

   public TableResolver parsedResolver() {
      return this.parsedResolver;
   }

   public LoadedClassResolver loadedResolver() {
      return this.loadedResolver;
   }

   public ImportTable importTable(String sourceName, Package pkg) {
      this.assert_((TypeObject)pkg);
      return new ImportTable(this, this.systemResolver, pkg, sourceName);
   }

   public ImportTable importTable(Package pkg) {
      this.assert_((TypeObject)pkg);
      return new ImportTable(this, this.systemResolver, pkg);
   }

   public boolean packageExists(String name) {
      return this.systemResolver.packageExists(name);
   }

   protected void assert_(Collection l) {
      Iterator i = l.iterator();

      while(i.hasNext()) {
         TypeObject o = (TypeObject)i.next();
         this.assert_(o);
      }

   }

   protected void assert_(TypeObject o) {
      if (o != null && o.typeSystem() != this) {
         throw new InternalCompilerError("we are " + this + " but " + o + " is from " + o.typeSystem());
      }
   }

   public String wrapperTypeString(PrimitiveType t) {
      this.assert_((TypeObject)t);
      if (t.kind() == PrimitiveType.BOOLEAN) {
         return "java.lang.Boolean";
      } else if (t.kind() == PrimitiveType.CHAR) {
         return "java.lang.Character";
      } else if (t.kind() == PrimitiveType.BYTE) {
         return "java.lang.Byte";
      } else if (t.kind() == PrimitiveType.SHORT) {
         return "java.lang.Short";
      } else if (t.kind() == PrimitiveType.INT) {
         return "java.lang.Integer";
      } else if (t.kind() == PrimitiveType.LONG) {
         return "java.lang.Long";
      } else if (t.kind() == PrimitiveType.FLOAT) {
         return "java.lang.Float";
      } else if (t.kind() == PrimitiveType.DOUBLE) {
         return "java.lang.Double";
      } else if (t.kind() == PrimitiveType.VOID) {
         return "java.lang.Void";
      } else {
         throw new InternalCompilerError("Unrecognized primitive type.");
      }
   }

   public Context createContext() {
      return new Context_c(this);
   }

   public Resolver packageContextResolver(Resolver cr, Package p) {
      this.assert_((TypeObject)p);
      return new PackageContextResolver(this, p, cr);
   }

   public Resolver classContextResolver(ClassType type) {
      this.assert_((TypeObject)type);
      return new ClassContextResolver(this, type);
   }

   public FieldInstance fieldInstance(Position pos, ReferenceType container, Flags flags, Type type, String name) {
      this.assert_((TypeObject)container);
      this.assert_((TypeObject)type);
      return new FieldInstance_c(this, pos, container, flags, type, name);
   }

   public LocalInstance localInstance(Position pos, Flags flags, Type type, String name) {
      this.assert_((TypeObject)type);
      return new LocalInstance_c(this, pos, flags, type, name);
   }

   public ConstructorInstance defaultConstructor(Position pos, ClassType container) {
      this.assert_((TypeObject)container);
      Flags access = Flags.NONE;
      if (container.flags().isPrivate()) {
         access = access.Private();
      }

      if (container.flags().isProtected()) {
         access = access.Protected();
      }

      if (container.flags().isPublic()) {
         access = access.Public();
      }

      return this.constructorInstance(pos, container, access, Collections.EMPTY_LIST, Collections.EMPTY_LIST);
   }

   public ConstructorInstance constructorInstance(Position pos, ClassType container, Flags flags, List argTypes, List excTypes) {
      this.assert_((TypeObject)container);
      this.assert_((Collection)argTypes);
      this.assert_((Collection)excTypes);
      return new ConstructorInstance_c(this, pos, container, flags, argTypes, excTypes);
   }

   public InitializerInstance initializerInstance(Position pos, ClassType container, Flags flags) {
      this.assert_((TypeObject)container);
      return new InitializerInstance_c(this, pos, container, flags);
   }

   public MethodInstance methodInstance(Position pos, ReferenceType container, Flags flags, Type returnType, String name, List argTypes, List excTypes) {
      this.assert_((TypeObject)container);
      this.assert_((TypeObject)returnType);
      this.assert_((Collection)argTypes);
      this.assert_((Collection)excTypes);
      return new MethodInstance_c(this, pos, container, flags, returnType, name, argTypes, excTypes);
   }

   public boolean descendsFrom(Type child, Type ancestor) {
      this.assert_((TypeObject)child);
      this.assert_((TypeObject)ancestor);
      return child.descendsFromImpl(ancestor);
   }

   public boolean isCastValid(Type fromType, Type toType) {
      this.assert_((TypeObject)fromType);
      this.assert_((TypeObject)toType);
      return fromType.isCastValidImpl(toType);
   }

   public boolean isImplicitCastValid(Type fromType, Type toType) {
      this.assert_((TypeObject)fromType);
      this.assert_((TypeObject)toType);
      return fromType.isImplicitCastValidImpl(toType);
   }

   public boolean equals(TypeObject type1, TypeObject type2) {
      this.assert_(type1);
      this.assert_(type2);
      if (type1 instanceof TypeObject_c) {
         return ((TypeObject_c)type1).equalsImpl(type2);
      } else {
         throw new InternalCompilerError("Unknown implementation of TypeObject", type1.position());
      }
   }

   public boolean numericConversionValid(Type t, Object value) {
      this.assert_((TypeObject)t);
      return t.numericConversionValidImpl(value);
   }

   public boolean numericConversionValid(Type t, long value) {
      this.assert_((TypeObject)t);
      return t.numericConversionValidImpl(value);
   }

   public boolean isCanonical(Type type) {
      this.assert_((TypeObject)type);
      return type.isCanonical();
   }

   public boolean isAccessible(MemberInstance mi, Context context) {
      return this.isAccessible(mi, context.currentClass());
   }

   protected boolean isAccessible(MemberInstance mi, ClassType contextClass) {
      this.assert_((TypeObject)mi);
      ReferenceType target = mi.container();
      Flags flags = mi.flags();
      if (!target.isClass()) {
         return flags.isPublic();
      } else {
         ClassType targetClass = target.toClass();
         if (!this.classAccessible(targetClass, contextClass)) {
            return false;
         } else if (this.equals(targetClass, contextClass)) {
            return true;
         } else if (!this.isEnclosed(contextClass, targetClass) && !this.isEnclosed(targetClass, contextClass)) {
            ClassType ct = contextClass;

            while(!ct.isTopLevel()) {
               ct = ct.outer();
               if (this.isEnclosed(targetClass, ct)) {
                  return true;
               }
            }

            if (flags.isProtected()) {
               if (this.descendsFrom(contextClass, targetClass)) {
                  return true;
               }

               ct = contextClass;

               while(!ct.isTopLevel()) {
                  ct = ct.outer();
                  if (this.descendsFrom(ct, targetClass)) {
                     return true;
                  }
               }
            }

            return this.accessibleFromPackage(flags, targetClass.package_(), contextClass.package_());
         } else {
            return true;
         }
      }
   }

   public boolean classAccessible(ClassType targetClass, Context context) {
      return context.currentClass() == null ? this.classAccessibleFromPackage(targetClass, context.importTable().package_()) : this.classAccessible(targetClass, context.currentClass());
   }

   protected boolean classAccessible(ClassType targetClass, ClassType contextClass) {
      this.assert_((TypeObject)targetClass);
      if (targetClass.isMember()) {
         return this.isAccessible(targetClass, (ClassType)contextClass);
      } else if (!targetClass.isTopLevel()) {
         return true;
      } else if (this.equals(targetClass, contextClass)) {
         return true;
      } else {
         return this.isEnclosed(contextClass, targetClass) ? true : this.accessibleFromPackage(targetClass.flags(), targetClass.package_(), contextClass.package_());
      }
   }

   public boolean classAccessibleFromPackage(ClassType targetClass, Package pkg) {
      this.assert_((TypeObject)targetClass);
      if (!targetClass.isTopLevel() && !targetClass.isMember()) {
         return false;
      } else {
         Flags flags = targetClass.flags();
         if (targetClass.isMember()) {
            if (!targetClass.container().isClass()) {
               return flags.isPublic();
            }

            if (!this.classAccessibleFromPackage(targetClass.container().toClass(), pkg)) {
               return false;
            }
         }

         return this.accessibleFromPackage(flags, targetClass.package_(), pkg);
      }
   }

   protected boolean accessibleFromPackage(Flags flags, Package pkg1, Package pkg2) {
      if (flags.isPublic()) {
         return true;
      } else {
         if (flags.isPackage() || flags.isProtected()) {
            if (pkg1 == null && pkg2 == null) {
               return true;
            }

            if (pkg1 != null && pkg1.equals(pkg2)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isEnclosed(ClassType inner, ClassType outer) {
      return inner.isEnclosedImpl(outer);
   }

   public boolean hasEnclosingInstance(ClassType inner, ClassType encl) {
      return inner.hasEnclosingInstanceImpl(encl);
   }

   public void checkCycles(ReferenceType goal) throws SemanticException {
      this.checkCycles(goal, goal);
   }

   protected void checkCycles(ReferenceType curr, ReferenceType goal) throws SemanticException {
      this.assert_((TypeObject)curr);
      this.assert_((TypeObject)goal);
      if (curr != null) {
         ReferenceType superType = null;
         if (curr.superType() != null) {
            superType = curr.superType().toReference();
         }

         if (goal == superType) {
            throw new SemanticException("Circular inheritance involving " + goal, curr.position());
         } else {
            this.checkCycles(superType, goal);
            Iterator i = curr.interfaces().iterator();

            while(i.hasNext()) {
               Type si = (Type)i.next();
               if (si == goal) {
                  throw new SemanticException("Circular inheritance involving " + goal, curr.position());
               }

               this.checkCycles(si.toReference(), goal);
            }

            if (curr.isClass()) {
               this.checkCycles(curr.toClass().outer(), goal);
            }

         }
      }
   }

   public boolean canCoerceToString(Type t, Context c) {
      return !t.isVoid();
   }

   public boolean isThrowable(Type type) {
      this.assert_((TypeObject)type);
      return type.isThrowable();
   }

   public boolean isUncheckedException(Type type) {
      this.assert_((TypeObject)type);
      return type.isUncheckedException();
   }

   public Collection uncheckedExceptions() {
      List l = new ArrayList(2);
      l.add(this.Error());
      l.add(this.RuntimeException());
      return l;
   }

   public boolean isSubtype(Type t1, Type t2) {
      this.assert_((TypeObject)t1);
      this.assert_((TypeObject)t2);
      return t1.isSubtypeImpl(t2);
   }

   /** @deprecated */
   public FieldInstance findField(ReferenceType container, String name, Context c) throws SemanticException {
      ClassType ct = null;
      if (c != null) {
         ct = c.currentClass();
      }

      return this.findField(container, name, ct);
   }

   public FieldInstance findField(ReferenceType container, String name, ClassType currClass) throws SemanticException {
      Collection fields = this.findFields(container, name);
      if (fields.size() == 0) {
         throw new NoMemberException(3, "Field \"" + name + "\" not found in type \"" + container + "\".");
      } else {
         Iterator i = fields.iterator();
         FieldInstance fi = (FieldInstance)i.next();
         if (i.hasNext()) {
            FieldInstance fi2 = (FieldInstance)i.next();
            throw new SemanticException("Field \"" + name + "\" is ambiguous; it is defined in both " + fi.container() + " and " + fi2.container() + ".");
         } else if (currClass != null && !this.isAccessible(fi, (ClassType)currClass)) {
            throw new SemanticException("Cannot access " + fi + ".");
         } else {
            return fi;
         }
      }
   }

   public FieldInstance findField(ReferenceType container, String name) throws SemanticException {
      return this.findField(container, name, (ClassType)null);
   }

   protected Set findFields(ReferenceType container, String name) {
      this.assert_((TypeObject)container);
      if (container == null) {
         throw new InternalCompilerError("Cannot access field \"" + name + "\" within a null container type.");
      } else {
         FieldInstance fi = container.fieldNamed(name);
         if (fi != null) {
            return Collections.singleton(fi);
         } else {
            Set fields = new HashSet();
            if (container.superType() != null && container.superType().isReference()) {
               Set superFields = this.findFields(container.superType().toReference(), name);
               fields.addAll(superFields);
            }

            if (container.isClass()) {
               ClassType ct = container.toClass();
               Iterator i = ct.interfaces().iterator();

               while(i.hasNext()) {
                  Type it = (Type)i.next();
                  Set superFields = this.findFields(it.toReference(), name);
                  fields.addAll(superFields);
               }
            }

            return fields;
         }
      }
   }

   /** @deprecated */
   public ClassType findMemberClass(ClassType container, String name, Context c) throws SemanticException {
      return this.findMemberClass(container, name, c.currentClass());
   }

   public ClassType findMemberClass(ClassType container, String name, ClassType currClass) throws SemanticException {
      this.assert_((TypeObject)container);
      Set s = this.findMemberClasses(container, name);
      if (s.size() == 0) {
         throw new NoClassException(name, container);
      } else {
         Iterator i = s.iterator();
         ClassType t = (ClassType)i.next();
         if (i.hasNext()) {
            ClassType t2 = (ClassType)i.next();
            throw new SemanticException("Member type \"" + name + "\" is ambiguous; it is defined in both " + t.container() + " and " + t2.container() + ".");
         } else if (currClass != null && !this.isAccessible(t, (ClassType)currClass)) {
            throw new SemanticException("Cannot access member type \"" + t + "\".");
         } else {
            return t;
         }
      }
   }

   public Set findMemberClasses(ClassType container, String name) throws SemanticException {
      this.assert_((TypeObject)container);
      ClassType mt = container.memberClassNamed(name);
      if (mt != null) {
         if (!mt.isMember()) {
            throw new InternalCompilerError("Class " + mt + " is not a member class, " + " but is in " + container + "'s list of members.");
         } else if (mt.outer() != container) {
            throw new InternalCompilerError("Class " + mt + " has outer class " + mt.outer() + " but is a member of " + container);
         } else {
            return Collections.singleton(mt);
         }
      } else {
         Set memberClasses = new HashSet();
         if (container.superType() != null) {
            Set s = this.findMemberClasses(container.superType().toClass(), name);
            memberClasses.addAll(s);
         }

         Iterator i = container.interfaces().iterator();

         while(i.hasNext()) {
            Type it = (Type)i.next();
            Set s = this.findMemberClasses(it.toClass(), name);
            memberClasses.addAll(s);
         }

         return memberClasses;
      }
   }

   public ClassType findMemberClass(ClassType container, String name) throws SemanticException {
      return this.findMemberClass(container, name, (ClassType)null);
   }

   protected static String listToString(List l) {
      StringBuffer sb = new StringBuffer();
      Iterator i = l.iterator();

      while(i.hasNext()) {
         Object o = i.next();
         sb.append(o.toString());
         if (i.hasNext()) {
            sb.append(", ");
         }
      }

      return sb.toString();
   }

   /** @deprecated */
   public MethodInstance findMethod(ReferenceType container, String name, List argTypes, Context c) throws SemanticException {
      return this.findMethod(container, name, argTypes, c.currentClass());
   }

   public boolean hasMethodNamed(ReferenceType container, String name) {
      this.assert_((TypeObject)container);
      if (container == null) {
         throw new InternalCompilerError("Cannot access method \"" + name + "\" within a null container type.");
      } else if (!container.methodsNamed(name).isEmpty()) {
         return true;
      } else if (container.superType() != null && container.superType().isReference() && this.hasMethodNamed(container.superType().toReference(), name)) {
         return true;
      } else {
         if (container.isClass()) {
            ClassType ct = container.toClass();
            Iterator i = ct.interfaces().iterator();

            while(i.hasNext()) {
               Type it = (Type)i.next();
               if (this.hasMethodNamed(it.toReference(), name)) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public MethodInstance findMethod(ReferenceType container, String name, List argTypes, ClassType currClass) throws SemanticException {
      this.assert_((TypeObject)container);
      this.assert_((Collection)argTypes);
      List acceptable = this.findAcceptableMethods(container, name, argTypes, currClass);
      if (acceptable.size() == 0) {
         throw new NoMemberException(1, "No valid method call found for " + name + "(" + listToString(argTypes) + ")" + " in " + container + ".");
      } else {
         MethodInstance mi = (MethodInstance)this.findProcedure(acceptable, container, argTypes, currClass);
         if (mi == null) {
            throw new SemanticException("Reference to " + name + " is ambiguous, multiple methods match: " + acceptable);
         } else {
            return mi;
         }
      }
   }

   /** @deprecated */
   public ConstructorInstance findConstructor(ClassType container, List argTypes, Context c) throws SemanticException {
      return this.findConstructor(container, argTypes, c.currentClass());
   }

   public ConstructorInstance findConstructor(ClassType container, List argTypes, ClassType currClass) throws SemanticException {
      this.assert_((TypeObject)container);
      this.assert_((Collection)argTypes);
      List acceptable = this.findAcceptableConstructors(container, argTypes, currClass);
      if (acceptable.size() == 0) {
         throw new NoMemberException(2, "No valid constructor found for " + container + "(" + listToString(argTypes) + ").");
      } else {
         ConstructorInstance ci = (ConstructorInstance)this.findProcedure(acceptable, container, argTypes, currClass);
         if (ci == null) {
            throw new NoMemberException(2, "Reference to " + container + " is ambiguous, multiple " + "constructors match: " + acceptable);
         } else {
            return ci;
         }
      }
   }

   protected ProcedureInstance findProcedure(List acceptable, ReferenceType container, List argTypes, ClassType currClass) throws SemanticException {
      Collection maximal = this.findMostSpecificProcedures(acceptable, container, argTypes, currClass);
      return maximal.size() == 1 ? (ProcedureInstance)maximal.iterator().next() : null;
   }

   protected Collection findMostSpecificProcedures(List acceptable, ReferenceType container, List argTypes, ClassType currClass) throws SemanticException {
      this.assert_((TypeObject)container);
      this.assert_((Collection)argTypes);
      TypeSystem_c.MostSpecificComparator msc = new TypeSystem_c.MostSpecificComparator();
      Collections.sort(acceptable, msc);
      List maximal = new ArrayList(acceptable.size());
      Iterator i = acceptable.iterator();
      ProcedureInstance first = (ProcedureInstance)i.next();
      ((List)maximal).add(first);

      while(i.hasNext()) {
         ProcedureInstance p = (ProcedureInstance)i.next();
         if (msc.compare(first, p) >= 0) {
            ((List)maximal).add(p);
         }
      }

      if (((List)maximal).size() > 1) {
         List notAbstract = new ArrayList(((List)maximal).size());
         Iterator j = ((List)maximal).iterator();

         ProcedureInstance p;
         while(j.hasNext()) {
            p = (ProcedureInstance)j.next();
            if (!p.flags().isAbstract()) {
               notAbstract.add(p);
            }
         }

         if (notAbstract.size() == 1) {
            maximal = notAbstract;
         } else if (notAbstract.size() == 0) {
            j = ((List)maximal).iterator();
            first = (ProcedureInstance)j.next();

            while(j.hasNext()) {
               p = (ProcedureInstance)j.next();
               if (!first.hasFormals(p.formalTypes())) {
                  return (Collection)maximal;
               }
            }

            maximal = Collections.singletonList(first);
         }
      }

      return (Collection)maximal;
   }

   protected List findAcceptableMethods(ReferenceType container, String name, List argTypes, ClassType currClass) throws SemanticException {
      this.assert_((TypeObject)container);
      this.assert_((Collection)argTypes);
      List acceptable = new ArrayList();
      List unacceptable = new ArrayList();
      Set visitedTypes = new HashSet();
      LinkedList typeQueue = new LinkedList();
      typeQueue.addLast(container);

      while(true) {
         Type type;
         do {
            if (typeQueue.isEmpty()) {
               Iterator i = unacceptable.iterator();

               while(i.hasNext()) {
                  MethodInstance mi = (MethodInstance)i.next();
                  acceptable.removeAll(mi.overrides());
               }

               return acceptable;
            }

            type = (Type)typeQueue.removeFirst();
         } while(visitedTypes.contains(type));

         visitedTypes.add(type);
         if (Report.should_report((String)"types", 2)) {
            Report.report(2, "Searching type " + type + " for method " + name + "(" + listToString(argTypes) + ")");
         }

         if (!type.isReference()) {
            throw new SemanticException("Cannot call method in  non-reference type " + type + ".");
         }

         Iterator i = type.toReference().methods().iterator();

         while(i.hasNext()) {
            MethodInstance mi = (MethodInstance)i.next();
            if (Report.should_report((String)"types", 3)) {
               Report.report(3, "Trying " + mi);
            }

            if (this.methodCallValid(mi, name, argTypes)) {
               if (this.isAccessible(mi, (ClassType)currClass)) {
                  if (Report.should_report((String)"types", 3)) {
                     Report.report(3, "->acceptable: " + mi + " in " + mi.container());
                  }

                  acceptable.add(mi);
               } else {
                  unacceptable.add(mi);
               }
            }
         }

         if (type.toReference().superType() != null) {
            typeQueue.addLast(type.toReference().superType());
         }

         typeQueue.addAll(type.toReference().interfaces());
      }
   }

   protected List findAcceptableConstructors(ClassType container, List argTypes, ClassType currClass) throws SemanticException {
      this.assert_((TypeObject)container);
      this.assert_((Collection)argTypes);
      List acceptable = new ArrayList();
      if (Report.should_report((String)"types", 2)) {
         Report.report(2, "Searching type " + container + " for constructor " + container + "(" + listToString(argTypes) + ")");
      }

      Iterator i = container.constructors().iterator();

      while(i.hasNext()) {
         ConstructorInstance ci = (ConstructorInstance)i.next();
         if (Report.should_report((String)"types", 3)) {
            Report.report(3, "Trying " + ci);
         }

         if (this.callValid(ci, argTypes) && this.isAccessible(ci, (ClassType)currClass)) {
            if (Report.should_report((String)"types", 3)) {
               Report.report(3, "->acceptable: " + ci);
            }

            acceptable.add(ci);
         }
      }

      return acceptable;
   }

   public boolean moreSpecific(ProcedureInstance p1, ProcedureInstance p2) {
      return p1.moreSpecificImpl(p2);
   }

   public Type superType(ReferenceType type) {
      this.assert_((TypeObject)type);
      return type.superType();
   }

   public List interfaces(ReferenceType type) {
      this.assert_((TypeObject)type);
      return type.interfaces();
   }

   public Type leastCommonAncestor(Type type1, Type type2) throws SemanticException {
      this.assert_((TypeObject)type1);
      this.assert_((TypeObject)type2);
      if (this.equals(type1, type2)) {
         return type1;
      } else {
         if (type1.isNumeric() && type2.isNumeric()) {
            if (this.isImplicitCastValid(type1, type2)) {
               return type2;
            }

            if (this.isImplicitCastValid(type2, type1)) {
               return type1;
            }

            if (type1.isChar() && type2.isByte() || type1.isByte() && type2.isChar()) {
               return this.Int();
            }

            if (type1.isChar() && type2.isShort() || type1.isShort() && type2.isChar()) {
               return this.Int();
            }
         }

         if (type1.isArray() && type2.isArray()) {
            return this.arrayOf(this.leastCommonAncestor(type1.toArray().base(), type2.toArray().base()));
         } else if (type1.isReference() && type2.isNull()) {
            return type1;
         } else if (type2.isReference() && type1.isNull()) {
            return type2;
         } else if (type1.isReference() && type2.isReference()) {
            if (type1.isClass() && type1.toClass().flags().isInterface()) {
               return this.Object();
            } else if (type2.isClass() && type2.toClass().flags().isInterface()) {
               return this.Object();
            } else if (this.equals(type1, this.Object())) {
               return type1;
            } else if (this.equals(type2, this.Object())) {
               return type2;
            } else if (this.isSubtype(type1, type2)) {
               return type2;
            } else if (this.isSubtype(type2, type1)) {
               return type1;
            } else {
               Type t1 = this.leastCommonAncestor(type1.toReference().superType(), type2);
               Type t2 = this.leastCommonAncestor(type2.toReference().superType(), type1);
               if (this.equals(t1, t2)) {
                  return t1;
               } else {
                  return this.Object();
               }
            }
         } else {
            throw new SemanticException("No least common ancestor found for types \"" + type1 + "\" and \"" + type2 + "\".");
         }
      }
   }

   public boolean throwsSubset(ProcedureInstance p1, ProcedureInstance p2) {
      this.assert_((TypeObject)p1);
      this.assert_((TypeObject)p2);
      return p1.throwsSubsetImpl(p2);
   }

   public boolean hasFormals(ProcedureInstance pi, List formalTypes) {
      this.assert_((TypeObject)pi);
      this.assert_((Collection)formalTypes);
      return pi.hasFormalsImpl(formalTypes);
   }

   public boolean hasMethod(ReferenceType t, MethodInstance mi) {
      this.assert_((TypeObject)t);
      this.assert_((TypeObject)mi);
      return t.hasMethodImpl(mi);
   }

   public List overrides(MethodInstance mi) {
      return mi.overridesImpl();
   }

   public List implemented(MethodInstance mi) {
      return mi.implementedImpl(mi.container());
   }

   public boolean canOverride(MethodInstance mi, MethodInstance mj) {
      try {
         return mi.canOverrideImpl(mj, true);
      } catch (SemanticException var4) {
         throw new InternalCompilerError(var4);
      }
   }

   public void checkOverride(MethodInstance mi, MethodInstance mj) throws SemanticException {
      mi.canOverrideImpl(mj, false);
   }

   public boolean isSameMethod(MethodInstance m1, MethodInstance m2) {
      this.assert_((TypeObject)m1);
      this.assert_((TypeObject)m2);
      return m1.isSameMethodImpl(m2);
   }

   public boolean methodCallValid(MethodInstance prototype, String name, List argTypes) {
      this.assert_((TypeObject)prototype);
      this.assert_((Collection)argTypes);
      return prototype.methodCallValidImpl(name, argTypes);
   }

   public boolean callValid(ProcedureInstance prototype, List argTypes) {
      this.assert_((TypeObject)prototype);
      this.assert_((Collection)argTypes);
      return prototype.callValidImpl(argTypes);
   }

   public NullType Null() {
      return this.NULL_;
   }

   public PrimitiveType Void() {
      return this.VOID_;
   }

   public PrimitiveType Boolean() {
      return this.BOOLEAN_;
   }

   public PrimitiveType Char() {
      return this.CHAR_;
   }

   public PrimitiveType Byte() {
      return this.BYTE_;
   }

   public PrimitiveType Short() {
      return this.SHORT_;
   }

   public PrimitiveType Int() {
      return this.INT_;
   }

   public PrimitiveType Long() {
      return this.LONG_;
   }

   public PrimitiveType Float() {
      return this.FLOAT_;
   }

   public PrimitiveType Double() {
      return this.DOUBLE_;
   }

   protected ClassType load(String name) {
      try {
         return (ClassType)this.typeForName(name);
      } catch (SemanticException var3) {
         throw new InternalCompilerError("Cannot find class \"" + name + "\"; " + var3.getMessage(), var3);
      }
   }

   public Named forName(String name) throws SemanticException {
      try {
         return this.systemResolver.find(name);
      } catch (SemanticException var7) {
         if (!StringUtil.isNameShort(name)) {
            String containerName = StringUtil.getPackageComponent(name);
            String shortName = StringUtil.getShortNameComponent(name);

            try {
               Named container = this.forName(containerName);
               if (container instanceof ClassType) {
                  return this.classContextResolver((ClassType)container).find(shortName);
               }
            } catch (SemanticException var6) {
            }
         }

         throw var7;
      }
   }

   public Type typeForName(String name) throws SemanticException {
      return (Type)this.forName(name);
   }

   public ClassType Object() {
      return this.OBJECT_ != null ? this.OBJECT_ : (this.OBJECT_ = this.load("java.lang.Object"));
   }

   public ClassType Class() {
      return this.CLASS_ != null ? this.CLASS_ : (this.CLASS_ = this.load("java.lang.Class"));
   }

   public ClassType String() {
      return this.STRING_ != null ? this.STRING_ : (this.STRING_ = this.load("java.lang.String"));
   }

   public ClassType Throwable() {
      return this.THROWABLE_ != null ? this.THROWABLE_ : (this.THROWABLE_ = this.load("java.lang.Throwable"));
   }

   public ClassType Error() {
      return this.load("java.lang.Error");
   }

   public ClassType Exception() {
      return this.load("java.lang.Exception");
   }

   public ClassType RuntimeException() {
      return this.load("java.lang.RuntimeException");
   }

   public ClassType Cloneable() {
      return this.load("java.lang.Cloneable");
   }

   public ClassType Serializable() {
      return this.load("java.io.Serializable");
   }

   public ClassType NullPointerException() {
      return this.load("java.lang.NullPointerException");
   }

   public ClassType ClassCastException() {
      return this.load("java.lang.ClassCastException");
   }

   public ClassType OutOfBoundsException() {
      return this.load("java.lang.ArrayIndexOutOfBoundsException");
   }

   public ClassType ArrayStoreException() {
      return this.load("java.lang.ArrayStoreException");
   }

   public ClassType ArithmeticException() {
      return this.load("java.lang.ArithmeticException");
   }

   protected NullType createNull() {
      return new NullType_c(this);
   }

   protected PrimitiveType createPrimitive(PrimitiveType.Kind kind) {
      return new PrimitiveType_c(this, kind);
   }

   public Object placeHolder(TypeObject o) {
      this.assert_(o);
      return this.placeHolder(o, new HashSet());
   }

   public Object placeHolder(TypeObject o, Set roots) {
      this.assert_(o);
      if (o instanceof ClassType) {
         ClassType ct = (ClassType)o;
         if (!ct.isLocal() && !ct.isAnonymous()) {
            return new PlaceHolder_c(ct);
         } else {
            throw new InternalCompilerError("Cannot serialize " + o + ".");
         }
      } else {
         return o;
      }
   }

   public UnknownType unknownType(Position pos) {
      return this.unknownType;
   }

   public UnknownPackage unknownPackage(Position pos) {
      return this.unknownPackage;
   }

   public UnknownQualifier unknownQualifier(Position pos) {
      return this.unknownQualifier;
   }

   public Package packageForName(Package prefix, String name) throws SemanticException {
      return this.createPackage(prefix, name);
   }

   public Package packageForName(String name) throws SemanticException {
      if (name != null && !name.equals("")) {
         String s = StringUtil.getShortNameComponent(name);
         String p = StringUtil.getPackageComponent(name);
         return this.packageForName(this.packageForName(p), s);
      } else {
         return null;
      }
   }

   public Package createPackage(Package prefix, String name) {
      this.assert_((TypeObject)prefix);
      return new Package_c(this, prefix, name);
   }

   public Package createPackage(String name) {
      if (name != null && !name.equals("")) {
         String s = StringUtil.getShortNameComponent(name);
         String p = StringUtil.getPackageComponent(name);
         return this.createPackage(this.createPackage(p), s);
      } else {
         return null;
      }
   }

   public ArrayType arrayOf(Type type) {
      this.assert_((TypeObject)type);
      return this.arrayOf(type.position(), type);
   }

   public ArrayType arrayOf(Position pos, Type type) {
      this.assert_((TypeObject)type);
      return this.arrayType(pos, type);
   }

   protected ArrayType arrayType(Position pos, Type type) {
      return new ArrayType_c(this, pos, type);
   }

   public ArrayType arrayOf(Type type, int dims) {
      return this.arrayOf((Position)null, type, dims);
   }

   public ArrayType arrayOf(Position pos, Type type, int dims) {
      if (dims > 1) {
         return this.arrayOf(pos, this.arrayOf(pos, type, dims - 1));
      } else if (dims == 1) {
         return this.arrayOf(pos, type);
      } else {
         throw new InternalCompilerError("Must call arrayOf(type, dims) with dims > 0");
      }
   }

   public Type typeForClass(Class clazz) throws SemanticException {
      if (clazz == Void.TYPE) {
         return this.VOID_;
      } else if (clazz == Boolean.TYPE) {
         return this.BOOLEAN_;
      } else if (clazz == Byte.TYPE) {
         return this.BYTE_;
      } else if (clazz == Character.TYPE) {
         return this.CHAR_;
      } else if (clazz == Short.TYPE) {
         return this.SHORT_;
      } else if (clazz == Integer.TYPE) {
         return this.INT_;
      } else if (clazz == Long.TYPE) {
         return this.LONG_;
      } else if (clazz == Float.TYPE) {
         return this.FLOAT_;
      } else if (clazz == Double.TYPE) {
         return this.DOUBLE_;
      } else {
         return (Type)(clazz.isArray() ? this.arrayOf(this.typeForClass(clazz.getComponentType())) : (Type)this.systemResolver.find(clazz.getName()));
      }
   }

   public Set getTypeEncoderRootSet(Type t) {
      return Collections.singleton(t);
   }

   public String getTransformedClassName(ClassType ct) {
      StringBuffer sb = new StringBuffer(ct.fullName().length());
      if (!ct.isMember() && !ct.isTopLevel()) {
         return null;
      } else {
         do {
            if (!ct.isMember()) {
               sb.insert(0, ct.fullName());
               return sb.toString();
            }

            sb.insert(0, ct.name());
            sb.insert(0, '$');
            ct = ct.outer();
         } while(ct.isMember() || ct.isTopLevel());

         return null;
      }
   }

   public String translatePackage(Resolver c, Package p) {
      return p.translate(c);
   }

   public String translateArray(Resolver c, ArrayType t) {
      return t.translate(c);
   }

   public String translateClass(Resolver c, ClassType t) {
      return t.translate(c);
   }

   public String translatePrimitive(Resolver c, PrimitiveType t) {
      return t.translate(c);
   }

   public PrimitiveType primitiveForName(String name) throws SemanticException {
      if (name.equals("void")) {
         return this.Void();
      } else if (name.equals("boolean")) {
         return this.Boolean();
      } else if (name.equals("char")) {
         return this.Char();
      } else if (name.equals("byte")) {
         return this.Byte();
      } else if (name.equals("short")) {
         return this.Short();
      } else if (name.equals("int")) {
         return this.Int();
      } else if (name.equals("long")) {
         return this.Long();
      } else if (name.equals("float")) {
         return this.Float();
      } else if (name.equals("double")) {
         return this.Double();
      } else {
         throw new SemanticException("Unrecognized primitive type \"" + name + "\".");
      }
   }

   public LazyClassInitializer defaultClassInitializer() {
      if (this.defaultClassInit == null) {
         this.defaultClassInit = new LazyClassInitializer_c(this);
      }

      return this.defaultClassInit;
   }

   public final ParsedClassType createClassType() {
      return this.createClassType(this.defaultClassInitializer(), (Source)null);
   }

   public final ParsedClassType createClassType(Source fromSource) {
      return this.createClassType(this.defaultClassInitializer(), fromSource);
   }

   public final ParsedClassType createClassType(LazyClassInitializer init) {
      return this.createClassType(init, (Source)null);
   }

   public ParsedClassType createClassType(LazyClassInitializer init, Source fromSource) {
      return new ParsedClassType_c(this, init, fromSource);
   }

   public List defaultPackageImports() {
      List l = new ArrayList(1);
      l.add("java.lang");
      return l;
   }

   public PrimitiveType promote(Type t1, Type t2) throws SemanticException {
      if (!t1.isNumeric()) {
         throw new SemanticException("Cannot promote non-numeric type " + t1);
      } else if (!t2.isNumeric()) {
         throw new SemanticException("Cannot promote non-numeric type " + t2);
      } else {
         return this.promoteNumeric(t1.toPrimitive(), t2.toPrimitive());
      }
   }

   protected PrimitiveType promoteNumeric(PrimitiveType t1, PrimitiveType t2) {
      if (!t1.isDouble() && !t2.isDouble()) {
         if (!t1.isFloat() && !t2.isFloat()) {
            return !t1.isLong() && !t2.isLong() ? this.Int() : this.Long();
         } else {
            return this.Float();
         }
      } else {
         return this.Double();
      }
   }

   public PrimitiveType promote(Type t) throws SemanticException {
      if (!t.isNumeric()) {
         throw new SemanticException("Cannot promote non-numeric type " + t);
      } else {
         return this.promoteNumeric(t.toPrimitive());
      }
   }

   protected PrimitiveType promoteNumeric(PrimitiveType t) {
      return !t.isByte() && !t.isShort() && !t.isChar() ? t.toPrimitive() : this.Int();
   }

   public void checkMethodFlags(Flags f) throws SemanticException {
      if (!f.clear(this.METHOD_FLAGS).equals(Flags.NONE)) {
         throw new SemanticException("Cannot declare method with flags " + f.clear(this.METHOD_FLAGS) + ".");
      } else if (f.isAbstract() && f.isPrivate()) {
         throw new SemanticException("Cannot declare method that is both abstract and private.");
      } else if (f.isAbstract() && f.isStatic()) {
         throw new SemanticException("Cannot declare method that is both abstract and static.");
      } else if (f.isAbstract() && f.isFinal()) {
         throw new SemanticException("Cannot declare method that is both abstract and final.");
      } else if (f.isAbstract() && f.isNative()) {
         throw new SemanticException("Cannot declare method that is both abstract and native.");
      } else if (f.isAbstract() && f.isSynchronized()) {
         throw new SemanticException("Cannot declare method that is both abstract and synchronized.");
      } else if (f.isAbstract() && f.isStrictFP()) {
         throw new SemanticException("Cannot declare method that is both abstract and strictfp.");
      } else {
         this.checkAccessFlags(f);
      }
   }

   public void checkLocalFlags(Flags f) throws SemanticException {
      if (!f.clear(this.LOCAL_FLAGS).equals(Flags.NONE)) {
         throw new SemanticException("Cannot declare local variable with flags " + f.clear(this.LOCAL_FLAGS) + ".");
      }
   }

   public void checkFieldFlags(Flags f) throws SemanticException {
      if (!f.clear(this.FIELD_FLAGS).equals(Flags.NONE)) {
         throw new SemanticException("Cannot declare field with flags " + f.clear(this.FIELD_FLAGS) + ".");
      } else {
         this.checkAccessFlags(f);
      }
   }

   public void checkConstructorFlags(Flags f) throws SemanticException {
      if (!f.clear(this.CONSTRUCTOR_FLAGS).equals(Flags.NONE)) {
         throw new SemanticException("Cannot declare constructor with flags " + f.clear(this.CONSTRUCTOR_FLAGS) + ".");
      } else {
         this.checkAccessFlags(f);
      }
   }

   public void checkInitializerFlags(Flags f) throws SemanticException {
      if (!f.clear(this.INITIALIZER_FLAGS).equals(Flags.NONE)) {
         throw new SemanticException("Cannot declare initializer with flags " + f.clear(this.INITIALIZER_FLAGS) + ".");
      }
   }

   public void checkTopLevelClassFlags(Flags f) throws SemanticException {
      if (!f.clear(this.TOP_LEVEL_CLASS_FLAGS).equals(Flags.NONE)) {
         throw new SemanticException("Cannot declare a top-level class with flag(s) " + f.clear(this.TOP_LEVEL_CLASS_FLAGS) + ".");
      } else if (f.isFinal() && f.isInterface()) {
         throw new SemanticException("Cannot declare a final interface.");
      } else {
         this.checkAccessFlags(f);
      }
   }

   public void checkMemberClassFlags(Flags f) throws SemanticException {
      if (!f.clear(this.MEMBER_CLASS_FLAGS).equals(Flags.NONE)) {
         throw new SemanticException("Cannot declare a member class with flag(s) " + f.clear(this.MEMBER_CLASS_FLAGS) + ".");
      } else if (f.isStrictFP() && f.isInterface()) {
         throw new SemanticException("Cannot declare a strictfp interface.");
      } else if (f.isFinal() && f.isInterface()) {
         throw new SemanticException("Cannot declare a final interface.");
      } else {
         this.checkAccessFlags(f);
      }
   }

   public void checkLocalClassFlags(Flags f) throws SemanticException {
      if (f.isInterface()) {
         throw new SemanticException("Cannot declare a local interface.");
      } else if (!f.clear(this.LOCAL_CLASS_FLAGS).equals(Flags.NONE)) {
         throw new SemanticException("Cannot declare a local class with flag(s) " + f.clear(this.LOCAL_CLASS_FLAGS) + ".");
      } else {
         this.checkAccessFlags(f);
      }
   }

   public void checkAccessFlags(Flags f) throws SemanticException {
      int count = 0;
      if (f.isPublic()) {
         ++count;
      }

      if (f.isProtected()) {
         ++count;
      }

      if (f.isPrivate()) {
         ++count;
      }

      if (count > 1) {
         throw new SemanticException("Invalid access flags: " + f.retain(this.ACCESS_FLAGS) + ".");
      }
   }

   protected List abstractSuperInterfaces(ReferenceType rt) {
      List superInterfaces = new LinkedList();
      superInterfaces.add(rt);
      Iterator iter = rt.interfaces().iterator();

      while(iter.hasNext()) {
         ClassType interf = (ClassType)iter.next();
         superInterfaces.addAll(this.abstractSuperInterfaces(interf));
      }

      if (rt.superType() != null) {
         ClassType c = rt.superType().toClass();
         if (c.flags().isAbstract()) {
            superInterfaces.addAll(this.abstractSuperInterfaces(c));
         }
      }

      return superInterfaces;
   }

   public void checkClassConformance(ClassType ct) throws SemanticException {
      if (!ct.flags().isInterface()) {
         List superInterfaces = this.abstractSuperInterfaces(ct);
         Iterator i = superInterfaces.iterator();

         label89:
         while(i.hasNext()) {
            ReferenceType rt = (ReferenceType)i.next();
            Iterator j = rt.methods().iterator();

            MethodInstance mi;
            boolean implFound;
            do {
               do {
                  if (!j.hasNext()) {
                     continue label89;
                  }

                  mi = (MethodInstance)j.next();
               } while(!mi.flags().isAbstract());

               implFound = false;

               for(Object curr = ct; curr != null && !implFound; curr = ((ReferenceType)curr).superType() == null ? null : ((ReferenceType)curr).superType().toReference()) {
                  List possible = ((ReferenceType)curr).methods(mi.name(), mi.formalTypes());
                  Iterator k = possible.iterator();

                  label95: {
                     MethodInstance mj;
                     do {
                        do {
                           if (!k.hasNext()) {
                              break label95;
                           }

                           mj = (MethodInstance)k.next();
                        } while(mj.flags().isAbstract());
                     } while((!this.isAccessible(mi, (ClassType)ct) || !this.isAccessible(mj, (ClassType)ct)) && !this.isAccessible(mi, (ClassType)mj.container().toClass()));

                     if (!this.equals(ct, mj.container()) && !this.equals(ct, mi.container())) {
                        try {
                           this.checkOverride(mj, mi);
                        } catch (SemanticException var13) {
                           throw new SemanticException(var13.getMessage(), ct.position());
                        }
                     }

                     implFound = true;
                  }

                  if (curr == mi.container()) {
                     break;
                  }
               }
            } while(implFound || ct.flags().isAbstract());

            throw new SemanticException(ct.fullName() + " should be " + "declared abstract; it does not define " + mi.signature() + ", which is declared in " + rt.toClass().fullName(), ct.position());
         }

      }
   }

   public Type staticTarget(Type t) {
      return t;
   }

   protected void initFlags() {
      this.flagsForName = new HashMap();
      this.flagsForName.put("public", Flags.PUBLIC);
      this.flagsForName.put("private", Flags.PRIVATE);
      this.flagsForName.put("protected", Flags.PROTECTED);
      this.flagsForName.put("static", Flags.STATIC);
      this.flagsForName.put("final", Flags.FINAL);
      this.flagsForName.put("synchronized", Flags.SYNCHRONIZED);
      this.flagsForName.put("transient", Flags.TRANSIENT);
      this.flagsForName.put("native", Flags.NATIVE);
      this.flagsForName.put("interface", Flags.INTERFACE);
      this.flagsForName.put("abstract", Flags.ABSTRACT);
      this.flagsForName.put("volatile", Flags.VOLATILE);
      this.flagsForName.put("strictfp", Flags.STRICTFP);
   }

   public Flags createNewFlag(String name, Flags after) {
      Flags f = Flags.createFlag(name, name, name, after);
      this.flagsForName.put(name, f);
      return f;
   }

   public Flags NoFlags() {
      return Flags.NONE;
   }

   public Flags Public() {
      return Flags.PUBLIC;
   }

   public Flags Private() {
      return Flags.PRIVATE;
   }

   public Flags Protected() {
      return Flags.PROTECTED;
   }

   public Flags Static() {
      return Flags.STATIC;
   }

   public Flags Final() {
      return Flags.FINAL;
   }

   public Flags Synchronized() {
      return Flags.SYNCHRONIZED;
   }

   public Flags Transient() {
      return Flags.TRANSIENT;
   }

   public Flags Native() {
      return Flags.NATIVE;
   }

   public Flags Interface() {
      return Flags.INTERFACE;
   }

   public Flags Abstract() {
      return Flags.ABSTRACT;
   }

   public Flags Volatile() {
      return Flags.VOLATILE;
   }

   public Flags StrictFP() {
      return Flags.STRICTFP;
   }

   public Flags flagsForBits(int bits) {
      Flags f = Flags.NONE;
      if ((bits & 1) != 0) {
         f = f.Public();
      }

      if ((bits & 2) != 0) {
         f = f.Private();
      }

      if ((bits & 4) != 0) {
         f = f.Protected();
      }

      if ((bits & 8) != 0) {
         f = f.Static();
      }

      if ((bits & 16) != 0) {
         f = f.Final();
      }

      if ((bits & 32) != 0) {
         f = f.Synchronized();
      }

      if ((bits & 128) != 0) {
         f = f.Transient();
      }

      if ((bits & 256) != 0) {
         f = f.Native();
      }

      if ((bits & 512) != 0) {
         f = f.Interface();
      }

      if ((bits & 1024) != 0) {
         f = f.Abstract();
      }

      if ((bits & 64) != 0) {
         f = f.Volatile();
      }

      if ((bits & 2048) != 0) {
         f = f.StrictFP();
      }

      return f;
   }

   public Flags flagsForName(String name) {
      Flags f = (Flags)this.flagsForName.get(name);
      if (f == null) {
         throw new InternalCompilerError("No flag named \"" + name + "\".");
      } else {
         return f;
      }
   }

   public String toString() {
      return StringUtil.getShortNameComponent(this.getClass().getName());
   }

   protected class MostSpecificComparator implements Comparator {
      public int compare(Object o1, Object o2) {
         ProcedureInstance p1 = (ProcedureInstance)o1;
         ProcedureInstance p2 = (ProcedureInstance)o2;
         if (TypeSystem_c.this.moreSpecific(p1, p2)) {
            return -1;
         } else if (TypeSystem_c.this.moreSpecific(p2, p1)) {
            return 1;
         } else if (p1.flags().isAbstract() == p2.flags().isAbstract()) {
            return 0;
         } else {
            return p1.flags().isAbstract() ? 1 : -1;
         }
      }
   }
}
