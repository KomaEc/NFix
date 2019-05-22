package soot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.baf.BafBody;
import soot.dava.toolkits.base.misc.PackageNamer;
import soot.options.Options;
import soot.tagkit.AbstractHost;
import soot.util.Chain;
import soot.util.EmptyChain;
import soot.util.HashChain;
import soot.util.Numberable;
import soot.util.NumberedString;
import soot.util.SmallNumberedMap;
import soot.validation.ClassFlagsValidator;
import soot.validation.ClassValidator;
import soot.validation.MethodDeclarationValidator;
import soot.validation.OuterClassValidator;
import soot.validation.ValidationException;

public class SootClass extends AbstractHost implements Numberable {
   private static final Logger logger = LoggerFactory.getLogger(SootClass.class);
   protected String name;
   protected String shortName;
   protected String fixedShortName;
   protected String packageName;
   protected String fixedPackageName;
   protected int modifiers;
   protected Chain<SootField> fields;
   protected SmallNumberedMap<SootMethod> subSigToMethods;
   protected List<SootMethod> methodList;
   protected Chain<SootClass> interfaces;
   protected boolean isInScene;
   protected SootClass superClass;
   protected SootClass outerClass;
   protected boolean isPhantom;
   public static final String INVOKEDYNAMIC_DUMMY_CLASS_NAME = "soot.dummy.InvokeDynamic";
   public static final int DANGLING = 0;
   public static final int HIERARCHY = 1;
   public static final int SIGNATURES = 2;
   public static final int BODIES = 3;
   private volatile int resolvingLevel;
   private RefType refType;
   protected int number;
   private static ClassValidator[] validators;

   public SootClass(String name, int modifiers) {
      this.resolvingLevel = 0;
      this.number = 0;
      if (name.charAt(0) == '[') {
         throw new RuntimeException("Attempt to make a class whose name starts with [");
      } else {
         this.setName(name);
         this.modifiers = modifiers;
         this.initializeRefType(name);
         if (Options.v().debug_resolver()) {
            logger.debug("created " + name + " with modifiers " + modifiers);
         }

         this.setResolvingLevel(3);
      }
   }

   protected void initializeRefType(String name) {
      this.refType = RefType.v(name);
      this.refType.setSootClass(this);
   }

   public SootClass(String name) {
      this(name, 0);
   }

   protected String levelToString(int level) {
      switch(level) {
      case 0:
         return "DANGLING";
      case 1:
         return "HIERARCHY";
      case 2:
         return "SIGNATURES";
      case 3:
         return "BODIES";
      default:
         throw new RuntimeException("unknown resolving level");
      }
   }

   public void checkLevel(int level) {
      int currentLevel = this.resolvingLevel();
      if (currentLevel < level) {
         if (Scene.v().doneResolving() && !Options.v().ignore_resolving_levels()) {
            this.checkLevelIgnoreResolving(level);
         }
      }
   }

   public void checkLevelIgnoreResolving(int level) {
      int currentLevel = this.resolvingLevel();
      if (currentLevel < level) {
         String hint = "\nIf you are extending Soot, try to add the following call before calling soot.Main.main(..):\nScene.v().addBasicClass(" + this.getName() + "," + this.levelToString(level) + ");\nOtherwise, try whole-program mode (-w).";
         throw new RuntimeException("This operation requires resolving level " + this.levelToString(level) + " but " + this.name + " is at resolving level " + this.levelToString(currentLevel) + hint);
      }
   }

   public int resolvingLevel() {
      return this.resolvingLevel;
   }

   public void setResolvingLevel(int newLevel) {
      this.resolvingLevel = newLevel;
   }

   public boolean isInScene() {
      return this.isInScene;
   }

   public void setInScene(boolean isInScene) {
      this.isInScene = isInScene;
      Scene.v().getClassNumberer().add((Numberable)this);
   }

   public int getFieldCount() {
      this.checkLevel(2);
      return this.fields == null ? 0 : this.fields.size();
   }

   public Chain<SootField> getFields() {
      this.checkLevel(2);
      return (Chain)(this.fields == null ? EmptyChain.v() : this.fields);
   }

   public void addField(SootField f) {
      this.checkLevel(2);
      if (f.isDeclared()) {
         throw new RuntimeException("already declared: " + f.getName());
      } else if (this.declaresField(f.getName(), f.getType())) {
         throw new RuntimeException("Field already exists : " + f.getName() + " of type " + f.getType());
      } else {
         if (this.fields == null) {
            this.fields = new HashChain();
         }

         this.fields.add(f);
         f.isDeclared = true;
         f.declaringClass = this;
      }
   }

   public void removeField(SootField f) {
      this.checkLevel(2);
      if (f.isDeclared() && f.getDeclaringClass() == this) {
         if (this.fields != null) {
            this.fields.remove(f);
         }

         f.isDeclared = false;
      } else {
         throw new RuntimeException("did not declare: " + f.getName());
      }
   }

   public SootField getField(String name, Type type) {
      SootField sf = this.getFieldUnsafe(name, type);
      if (sf == null) {
         throw new RuntimeException("No field " + name + " in class " + this.getName());
      } else {
         return sf;
      }
   }

   public SootField getFieldUnsafe(String name, Type type) {
      this.checkLevel(2);
      if (this.fields == null) {
         return null;
      } else {
         Iterator var3 = this.fields.getElementsUnsorted().iterator();

         SootField field;
         do {
            if (!var3.hasNext()) {
               return null;
            }

            field = (SootField)var3.next();
         } while(!field.getName().equals(name) || !field.getType().equals(type));

         return field;
      }
   }

   public SootField getFieldByName(String name) {
      SootField foundField = this.getFieldByNameUnsafe(name);
      if (foundField == null) {
         throw new RuntimeException("No field " + name + " in class " + this.getName());
      } else {
         return foundField;
      }
   }

   public SootField getFieldByNameUnsafe(String name) {
      this.checkLevel(2);
      if (this.fields == null) {
         return null;
      } else {
         SootField foundField = null;
         Iterator var3 = this.fields.getElementsUnsorted().iterator();

         while(var3.hasNext()) {
            SootField field = (SootField)var3.next();
            if (field.getName().equals(name)) {
               if (foundField != null) {
                  throw new RuntimeException("ambiguous field: " + name);
               }

               foundField = field;
            }
         }

         return foundField;
      }
   }

   public SootField getField(String subsignature) {
      SootField sf = this.getFieldUnsafe(subsignature);
      if (sf == null) {
         throw new RuntimeException("No field " + subsignature + " in class " + this.getName());
      } else {
         return sf;
      }
   }

   public SootField getFieldUnsafe(String subsignature) {
      this.checkLevel(2);
      if (this.fields == null) {
         return null;
      } else {
         Iterator var2 = this.fields.getElementsUnsorted().iterator();

         SootField field;
         do {
            if (!var2.hasNext()) {
               return null;
            }

            field = (SootField)var2.next();
         } while(!field.getSubSignature().equals(subsignature));

         return field;
      }
   }

   public boolean declaresField(String subsignature) {
      this.checkLevel(2);
      return this.getFieldUnsafe(subsignature) != null;
   }

   public SootMethod getMethod(NumberedString subsignature) {
      SootMethod ret = this.getMethodUnsafe(subsignature);
      if (ret == null) {
         throw new RuntimeException("No method " + subsignature + " in class " + this.getName());
      } else {
         return ret;
      }
   }

   public SootMethod getMethodUnsafe(NumberedString subsignature) {
      this.checkLevel(2);
      if (this.subSigToMethods == null) {
         return null;
      } else {
         SootMethod ret = (SootMethod)this.subSigToMethods.get(subsignature);
         return ret;
      }
   }

   public boolean declaresMethod(NumberedString subsignature) {
      this.checkLevel(2);
      if (this.subSigToMethods == null) {
         return false;
      } else {
         SootMethod ret = (SootMethod)this.subSigToMethods.get(subsignature);
         return ret != null;
      }
   }

   public SootMethod getMethod(String subsignature) {
      this.checkLevel(2);
      NumberedString numberedString = Scene.v().getSubSigNumberer().find(subsignature);
      if (numberedString == null) {
         throw new RuntimeException("No method " + subsignature + " in class " + this.getName());
      } else {
         return this.getMethod(numberedString);
      }
   }

   public SootMethod getMethodUnsafe(String subsignature) {
      this.checkLevel(2);
      NumberedString numberedString = Scene.v().getSubSigNumberer().find(subsignature);
      return numberedString == null ? null : this.getMethodUnsafe(numberedString);
   }

   public boolean declaresMethod(String subsignature) {
      this.checkLevel(2);
      NumberedString numberedString = Scene.v().getSubSigNumberer().find(subsignature);
      return numberedString == null ? false : this.declaresMethod(numberedString);
   }

   public boolean declaresFieldByName(String name) {
      this.checkLevel(2);
      if (this.fields == null) {
         return false;
      } else {
         Iterator var2 = this.fields.iterator();

         SootField field;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            field = (SootField)var2.next();
         } while(!field.getName().equals(name));

         return true;
      }
   }

   public boolean declaresField(String name, Type type) {
      this.checkLevel(2);
      if (this.fields == null) {
         return false;
      } else {
         Iterator var3 = this.fields.iterator();

         SootField field;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            field = (SootField)var3.next();
         } while(!field.getName().equals(name) || !field.getType().equals(type));

         return true;
      }
   }

   public int getMethodCount() {
      this.checkLevel(2);
      return this.subSigToMethods == null ? 0 : this.subSigToMethods.nonNullSize();
   }

   public Iterator<SootMethod> methodIterator() {
      this.checkLevel(2);
      return this.methodList == null ? Collections.emptyIterator() : new Iterator<SootMethod>() {
         final Iterator<SootMethod> internalIterator;
         private SootMethod currentMethod;

         {
            this.internalIterator = SootClass.this.methodList.iterator();
         }

         public boolean hasNext() {
            return this.internalIterator.hasNext();
         }

         public SootMethod next() {
            this.currentMethod = (SootMethod)this.internalIterator.next();
            return this.currentMethod;
         }

         public void remove() {
            this.internalIterator.remove();
            SootClass.this.subSigToMethods.put(this.currentMethod.getNumberedSubSignature(), (Object)null);
            this.currentMethod.setDeclared(false);
         }
      };
   }

   public List<SootMethod> getMethods() {
      this.checkLevel(2);
      return this.methodList == null ? Collections.emptyList() : this.methodList;
   }

   public SootMethod getMethod(String name, List<Type> parameterTypes, Type returnType) {
      SootMethod sm = this.getMethodUnsafe(name, parameterTypes, returnType);
      if (sm != null) {
         return sm;
      } else {
         throw new RuntimeException("Class " + this.getName() + " doesn't have method " + name + "(" + parameterTypes + ") : " + returnType);
      }
   }

   public SootMethod getMethodUnsafe(String name, List<Type> parameterTypes, Type returnType) {
      this.checkLevel(2);
      if (this.methodList == null) {
         return null;
      } else {
         Iterator var4 = this.methodList.iterator();

         SootMethod method;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            method = (SootMethod)var4.next();
         } while(!method.getName().equals(name) || !parameterTypes.equals(method.getParameterTypes()) || !returnType.equals(method.getReturnType()));

         return method;
      }
   }

   public SootMethod getMethod(String name, List<Type> parameterTypes) {
      this.checkLevel(2);
      SootMethod foundMethod = null;
      if (this.methodList == null) {
         return null;
      } else {
         Iterator var4 = this.methodList.iterator();

         while(var4.hasNext()) {
            SootMethod method = (SootMethod)var4.next();
            if (method.getName().equals(name) && parameterTypes.equals(method.getParameterTypes())) {
               if (foundMethod != null) {
                  throw new RuntimeException("ambiguous method");
               }

               foundMethod = method;
            }
         }

         if (foundMethod == null) {
            throw new RuntimeException("couldn't find method " + name + "(" + parameterTypes + ") in " + this);
         } else {
            return foundMethod;
         }
      }
   }

   public SootMethod getMethodByNameUnsafe(String name) {
      this.checkLevel(2);
      SootMethod foundMethod = null;
      if (this.methodList == null) {
         return null;
      } else {
         Iterator var3 = this.methodList.iterator();

         while(var3.hasNext()) {
            SootMethod method = (SootMethod)var3.next();
            if (method.getName().equals(name)) {
               if (foundMethod != null) {
                  throw new RuntimeException("ambiguous method: " + name + " in class " + this);
               }

               foundMethod = method;
            }
         }

         return foundMethod;
      }
   }

   public SootMethod getMethodByName(String name) {
      SootMethod foundMethod = this.getMethodByNameUnsafe(name);
      if (foundMethod == null) {
         throw new RuntimeException("couldn't find method " + name + "(*) in " + this);
      } else {
         return foundMethod;
      }
   }

   public boolean declaresMethod(String name, List<Type> parameterTypes) {
      this.checkLevel(2);
      if (this.methodList == null) {
         return false;
      } else {
         Iterator var3 = this.methodList.iterator();

         SootMethod method;
         do {
            if (!var3.hasNext()) {
               return false;
            }

            method = (SootMethod)var3.next();
         } while(!method.getName().equals(name) || !method.getParameterTypes().equals(parameterTypes));

         return true;
      }
   }

   public boolean declaresMethod(String name, List<Type> parameterTypes, Type returnType) {
      this.checkLevel(2);
      if (this.methodList == null) {
         return false;
      } else {
         Iterator var4 = this.methodList.iterator();

         SootMethod method;
         do {
            if (!var4.hasNext()) {
               return false;
            }

            method = (SootMethod)var4.next();
         } while(!method.getName().equals(name) || !method.getParameterTypes().equals(parameterTypes) || !method.getReturnType().equals(returnType));

         return true;
      }
   }

   public boolean declaresMethodByName(String name) {
      this.checkLevel(2);
      if (this.methodList == null) {
         return false;
      } else {
         Iterator var2 = this.methodList.iterator();

         SootMethod method;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            method = (SootMethod)var2.next();
         } while(!method.getName().equals(name));

         return true;
      }
   }

   public void addMethod(SootMethod m) {
      this.checkLevel(2);
      if (m.isDeclared()) {
         throw new RuntimeException("already declared: " + m.getName());
      } else {
         if (this.methodList == null) {
            this.methodList = new ArrayList();
            this.subSigToMethods = new SmallNumberedMap();
         }

         if (this.subSigToMethods.get(m.getNumberedSubSignature()) != null) {
            throw new RuntimeException("Attempting to add method " + m.getSubSignature() + " to class " + this + ", but the class already has a method with that signature.");
         } else {
            this.subSigToMethods.put(m.getNumberedSubSignature(), m);
            this.methodList.add(m);
            m.setDeclared(true);
            m.setDeclaringClass(this);
         }
      }
   }

   public synchronized SootMethod getOrAddMethod(SootMethod m) {
      this.checkLevel(2);
      if (m.isDeclared()) {
         throw new RuntimeException("already declared: " + m.getName());
      } else {
         if (this.methodList == null) {
            this.methodList = new ArrayList();
            this.subSigToMethods = new SmallNumberedMap();
         }

         SootMethod old = (SootMethod)this.subSigToMethods.get(m.getNumberedSubSignature());
         if (old != null) {
            return old;
         } else {
            this.subSigToMethods.put(m.getNumberedSubSignature(), m);
            this.methodList.add(m);
            m.setDeclared(true);
            m.setDeclaringClass(this);
            return m;
         }
      }
   }

   public synchronized SootField getOrAddField(SootField f) {
      this.checkLevel(2);
      if (f.isDeclared()) {
         throw new RuntimeException("already declared: " + f.getName());
      } else {
         SootField old = this.getFieldUnsafe(f.getName(), f.getType());
         if (old != null) {
            return old;
         } else {
            if (this.fields == null) {
               this.fields = new HashChain();
            }

            this.fields.add(f);
            f.isDeclared = true;
            f.declaringClass = this;
            return f;
         }
      }
   }

   public void removeMethod(SootMethod m) {
      this.checkLevel(2);
      if (m.isDeclared() && m.getDeclaringClass() == this) {
         if (this.subSigToMethods.get(m.getNumberedSubSignature()) == null) {
            throw new RuntimeException("Attempt to remove method " + m.getSubSignature() + " which is not in class " + this);
         } else {
            this.subSigToMethods.put(m.getNumberedSubSignature(), (Object)null);
            this.methodList.remove(m);
            m.setDeclared(false);
         }
      } else {
         throw new RuntimeException("incorrect declarer for remove: " + m.getName());
      }
   }

   public int getModifiers() {
      return this.modifiers;
   }

   public void setModifiers(int modifiers) {
      this.modifiers = modifiers;
   }

   public int getInterfaceCount() {
      this.checkLevel(1);
      return this.interfaces == null ? 0 : this.interfaces.size();
   }

   public Chain<SootClass> getInterfaces() {
      this.checkLevel(1);
      return (Chain)(this.interfaces == null ? EmptyChain.v() : this.interfaces);
   }

   public boolean implementsInterface(String name) {
      this.checkLevel(1);
      if (this.interfaces == null) {
         return false;
      } else {
         Iterator var2 = this.interfaces.iterator();

         SootClass sc;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            sc = (SootClass)var2.next();
         } while(!sc.getName().equals(name));

         return true;
      }
   }

   public void addInterface(SootClass interfaceClass) {
      this.checkLevel(1);
      if (this.implementsInterface(interfaceClass.getName())) {
         throw new RuntimeException("duplicate interface: " + interfaceClass.getName());
      } else {
         if (this.interfaces == null) {
            this.interfaces = new HashChain();
         }

         this.interfaces.add(interfaceClass);
      }
   }

   public void removeInterface(SootClass interfaceClass) {
      this.checkLevel(1);
      if (!this.implementsInterface(interfaceClass.getName())) {
         throw new RuntimeException("no such interface: " + interfaceClass.getName());
      } else {
         this.interfaces.remove(interfaceClass);
      }
   }

   public boolean hasSuperclass() {
      this.checkLevel(1);
      return this.superClass != null;
   }

   public SootClass getSuperclass() {
      this.checkLevel(1);
      if (this.superClass == null && !this.isPhantom()) {
         throw new RuntimeException("no superclass for " + this.getName());
      } else {
         return this.superClass;
      }
   }

   public SootClass getSuperclassUnsafe() {
      this.checkLevel(1);
      return this.superClass;
   }

   public void setSuperclass(SootClass c) {
      this.checkLevel(1);
      this.superClass = c;
   }

   public boolean hasOuterClass() {
      this.checkLevel(1);
      return this.outerClass != null;
   }

   public SootClass getOuterClass() {
      this.checkLevel(1);
      if (this.outerClass == null) {
         throw new RuntimeException("no outer class");
      } else {
         return this.outerClass;
      }
   }

   public SootClass getOuterClassUnsafe() {
      this.checkLevel(1);
      return this.outerClass;
   }

   public void setOuterClass(SootClass c) {
      this.checkLevel(1);
      this.outerClass = c;
   }

   public boolean isInnerClass() {
      return this.hasOuterClass();
   }

   public String getName() {
      return this.name;
   }

   public String getJavaStyleName() {
      if (PackageNamer.v().has_FixedNames()) {
         if (this.fixedShortName == null) {
            this.fixedShortName = PackageNamer.v().get_FixedClassName(this.name);
         }

         return !PackageNamer.v().use_ShortName(this.getJavaPackageName(), this.fixedShortName) ? this.getJavaPackageName() + "." + this.fixedShortName : this.fixedShortName;
      } else {
         return this.shortName;
      }
   }

   public String getShortJavaStyleName() {
      if (PackageNamer.v().has_FixedNames()) {
         if (this.fixedShortName == null) {
            this.fixedShortName = PackageNamer.v().get_FixedClassName(this.name);
         }

         return this.fixedShortName;
      } else {
         return this.shortName;
      }
   }

   public String getShortName() {
      return this.shortName;
   }

   public String getPackageName() {
      return this.packageName;
   }

   public String getJavaPackageName() {
      if (PackageNamer.v().has_FixedNames()) {
         if (this.fixedPackageName == null) {
            this.fixedPackageName = PackageNamer.v().get_FixedPackageName(this.packageName);
         }

         return this.fixedPackageName;
      } else {
         return this.packageName;
      }
   }

   public void setName(String name) {
      this.name = name.intern();
      this.shortName = name;
      this.packageName = "";
      int index = name.lastIndexOf(46);
      if (index > 0) {
         this.shortName = name.substring(index + 1);
         this.packageName = name.substring(0, index);
      }

      this.fixedShortName = null;
      this.fixedPackageName = null;
   }

   public boolean isInterface() {
      this.checkLevel(1);
      return Modifier.isInterface(this.getModifiers());
   }

   public boolean isEnum() {
      this.checkLevel(1);
      return Modifier.isEnum(this.getModifiers());
   }

   public boolean isSynchronized() {
      this.checkLevel(1);
      return Modifier.isSynchronized(this.getModifiers());
   }

   public boolean isConcrete() {
      return !this.isInterface() && !this.isAbstract();
   }

   public boolean isPublic() {
      return Modifier.isPublic(this.getModifiers());
   }

   public boolean containsBafBody() {
      Iterator methodIt = this.methodIterator();

      SootMethod m;
      do {
         if (!methodIt.hasNext()) {
            return false;
         }

         m = (SootMethod)methodIt.next();
      } while(!m.hasActiveBody() || !(m.getActiveBody() instanceof BafBody));

      return true;
   }

   public void setRefType(RefType refType) {
      this.refType = refType;
   }

   public boolean hasRefType() {
      return this.refType != null;
   }

   public RefType getType() {
      return this.refType;
   }

   public String toString() {
      return this.getName();
   }

   public void renameFieldsAndMethods(boolean privateOnly) {
      this.checkLevel(2);
      Iterator<SootField> methodIt = this.getFields().iterator();
      int methodCount = 0;
      String newMethodName;
      if (methodIt.hasNext()) {
         label40:
         while(true) {
            SootField f;
            do {
               if (!methodIt.hasNext()) {
                  break label40;
               }

               f = (SootField)methodIt.next();
            } while(privateOnly && !Modifier.isPrivate(f.getModifiers()));

            newMethodName = "__field" + methodCount++;
            f.setName(newMethodName);
         }
      }

      methodIt = this.methodIterator();
      methodCount = 0;
      if (methodIt.hasNext()) {
         while(true) {
            SootMethod m;
            do {
               if (!methodIt.hasNext()) {
                  return;
               }

               m = (SootMethod)methodIt.next();
            } while(privateOnly && !Modifier.isPrivate(m.getModifiers()));

            newMethodName = "__method" + methodCount++;
            m.setName(newMethodName);
         }
      }
   }

   public boolean isApplicationClass() {
      return Scene.v().getApplicationClasses().contains(this);
   }

   public void setApplicationClass() {
      if (!this.isApplicationClass()) {
         Chain<SootClass> c = Scene.v().getContainingChain(this);
         if (c != null) {
            c.remove(this);
         }

         Scene.v().getApplicationClasses().add(this);
         this.isPhantom = false;
      }
   }

   public boolean isLibraryClass() {
      return Scene.v().getLibraryClasses().contains(this);
   }

   public void setLibraryClass() {
      if (!this.isLibraryClass()) {
         Chain<SootClass> c = Scene.v().getContainingChain(this);
         if (c != null) {
            c.remove(this);
         }

         Scene.v().getLibraryClasses().add(this);
         this.isPhantom = false;
      }
   }

   public boolean isJavaLibraryClass() {
      return this.name.startsWith("java.") || this.name.startsWith("sun.") || this.name.startsWith("javax.") || this.name.startsWith("com.sun.") || this.name.startsWith("org.omg.") || this.name.startsWith("org.xml.") || this.name.startsWith("org.w3c.dom");
   }

   public boolean isPhantomClass() {
      return Scene.v().getPhantomClasses().contains(this);
   }

   public void setPhantomClass() {
      Chain<SootClass> c = Scene.v().getContainingChain(this);
      if (c != null) {
         c.remove(this);
      }

      Scene.v().getPhantomClasses().add(this);
      this.isPhantom = true;
   }

   public boolean isPhantom() {
      return this.isPhantom;
   }

   public boolean isPrivate() {
      return Modifier.isPrivate(this.getModifiers());
   }

   public boolean isProtected() {
      return Modifier.isProtected(this.getModifiers());
   }

   public boolean isAbstract() {
      return Modifier.isAbstract(this.getModifiers());
   }

   public boolean isFinal() {
      return Modifier.isFinal(this.getModifiers());
   }

   public boolean isStatic() {
      return Modifier.isStatic(this.getModifiers());
   }

   public final int getNumber() {
      return this.number;
   }

   public void setNumber(int number) {
      this.number = number;
   }

   public void rename(String newName) {
      this.name = newName;
      if (this.refType != null) {
         this.refType.setClassName(this.name);
      } else {
         this.refType = RefType.v(this.name);
      }

      Scene.v().addRefType(this.refType);
   }

   private static synchronized ClassValidator[] getValidators() {
      if (validators == null) {
         validators = new ClassValidator[]{OuterClassValidator.v(), MethodDeclarationValidator.v(), ClassFlagsValidator.v()};
      }

      return validators;
   }

   public void validate() {
      List<ValidationException> exceptionList = new ArrayList();
      this.validate(exceptionList);
      if (!exceptionList.isEmpty()) {
         throw (ValidationException)exceptionList.get(0);
      }
   }

   public void validate(List<ValidationException> exceptionList) {
      boolean runAllValidators = Options.v().debug() || Options.v().validate();
      ClassValidator[] var3 = getValidators();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ClassValidator validator = var3[var5];
         if (validator.isBasicValidator() || runAllValidators) {
            validator.validate(this, exceptionList);
         }
      }

   }
}
