package soot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import soot.dava.DavaBody;
import soot.dava.toolkits.base.renamer.RemoveFullyQualifiedName;
import soot.jimple.toolkits.callgraph.VirtualCalls;
import soot.options.Options;
import soot.tagkit.AbstractHost;
import soot.util.IterableSet;
import soot.util.Numberable;
import soot.util.NumberedString;

public class SootMethod extends AbstractHost implements ClassMember, Numberable, MethodOrMethodContext {
   public static final String constructorName = "<init>";
   public static final String staticInitializerName = "<clinit>";
   public static boolean DEBUG = false;
   protected String name;
   protected Type[] parameterTypes;
   protected Type returnType;
   protected boolean isDeclared;
   protected SootClass declaringClass;
   protected int modifiers;
   protected boolean isPhantom;
   protected List<SootClass> exceptions;
   protected volatile Body activeBody;
   protected volatile MethodSource ms;
   protected NumberedString subsignature;
   protected int number;

   private Body getBodyFromMethodSource(String phaseName) {
      MethodSource ms = this.ms;
      synchronized(this) {
         if (this.activeBody == null) {
            if (ms == null) {
               throw new RuntimeException("No method source set for method " + this.getSignature());
            } else {
               return ms.getBody(this, phaseName);
            }
         } else {
            return this.activeBody;
         }
      }
   }

   public void setSource(MethodSource ms) {
      this.ms = ms;
   }

   public MethodSource getSource() {
      return this.ms;
   }

   public int equivHashCode() {
      return this.returnType.hashCode() * 101 + this.modifiers * 17 + this.name.hashCode();
   }

   public SootMethod(String name, List<Type> parameterTypes, Type returnType) {
      this(name, parameterTypes, returnType, 0, Collections.emptyList());
   }

   public SootMethod(String name, List<Type> parameterTypes, Type returnType, int modifiers) {
      this(name, parameterTypes, returnType, modifiers, Collections.emptyList());
   }

   public SootMethod(String name, List<Type> parameterTypes, Type returnType, int modifiers, List<SootClass> thrownExceptions) {
      this.isPhantom = false;
      this.exceptions = null;
      this.number = 0;
      this.name = name;
      if (parameterTypes != null && !parameterTypes.isEmpty()) {
         this.parameterTypes = (Type[])parameterTypes.toArray(new Type[parameterTypes.size()]);
      }

      this.returnType = returnType;
      this.modifiers = modifiers;
      if (this.exceptions == null && !thrownExceptions.isEmpty()) {
         this.exceptions = new ArrayList();
         this.exceptions.addAll(thrownExceptions);
      }

      Scene scene = Scene.v();
      this.subsignature = scene.getSubSigNumberer().findOrAdd(this.getSubSignature());
   }

   public String getName() {
      return this.name;
   }

   public void setDeclaringClass(SootClass declClass) {
      if (declClass != null) {
         this.declaringClass = declClass;
      }

      Scene.v().getMethodNumberer().add((Numberable)this);
   }

   public SootClass getDeclaringClass() {
      if (!this.isDeclared) {
         throw new RuntimeException("not declared: " + this.getName());
      } else {
         return this.declaringClass;
      }
   }

   public void setDeclared(boolean isDeclared) {
      this.isDeclared = isDeclared;
   }

   public boolean isDeclared() {
      return this.isDeclared;
   }

   public boolean isPhantom() {
      return this.isPhantom;
   }

   public boolean isConcrete() {
      return !this.isPhantom() && !this.isAbstract() && !this.isNative();
   }

   public void setPhantom(boolean value) {
      if (value) {
         if (!Scene.v().allowsPhantomRefs()) {
            throw new RuntimeException("Phantom refs not allowed");
         }

         if (this.declaringClass != null && !this.declaringClass.isPhantom()) {
            throw new RuntimeException("Declaring class would have to be phantom");
         }
      }

      this.isPhantom = value;
   }

   public void setName(String name) {
      boolean wasDeclared = this.isDeclared;
      SootClass oldDeclaringClass = this.declaringClass;
      if (wasDeclared) {
         oldDeclaringClass.removeMethod(this);
      }

      this.name = name;
      this.subsignature = Scene.v().getSubSigNumberer().findOrAdd(this.getSubSignature());
      if (wasDeclared) {
         oldDeclaringClass.addMethod(this);
      }

   }

   public int getModifiers() {
      return this.modifiers;
   }

   public void setModifiers(int modifiers) {
      if (this.declaringClass != null && !this.declaringClass.isApplicationClass()) {
         throw new RuntimeException("Cannot set modifiers of a method from a non-app class!");
      } else {
         this.modifiers = modifiers;
      }
   }

   public Type getReturnType() {
      return this.returnType;
   }

   public void setReturnType(Type t) {
      boolean wasDeclared = this.isDeclared;
      SootClass oldDeclaringClass = this.declaringClass;
      if (wasDeclared) {
         oldDeclaringClass.removeMethod(this);
      }

      this.returnType = t;
      this.subsignature = Scene.v().getSubSigNumberer().findOrAdd(this.getSubSignature());
      if (wasDeclared) {
         oldDeclaringClass.addMethod(this);
      }

   }

   public int getParameterCount() {
      return this.parameterTypes == null ? 0 : this.parameterTypes.length;
   }

   public Type getParameterType(int n) {
      return this.parameterTypes[n];
   }

   public List<Type> getParameterTypes() {
      return this.parameterTypes == null ? Collections.emptyList() : Arrays.asList(this.parameterTypes);
   }

   public void setParameterTypes(List<Type> l) {
      boolean wasDeclared = this.isDeclared;
      SootClass oldDeclaringClass = this.declaringClass;
      if (wasDeclared) {
         oldDeclaringClass.removeMethod(this);
      }

      this.parameterTypes = (Type[])l.toArray(new Type[l.size()]);
      this.subsignature = Scene.v().getSubSigNumberer().findOrAdd(this.getSubSignature());
      if (wasDeclared) {
         oldDeclaringClass.addMethod(this);
      }

   }

   public Body getActiveBody() {
      if (this.activeBody != null) {
         return this.activeBody;
      } else if (this.declaringClass != null && this.declaringClass.isPhantomClass()) {
         throw new RuntimeException("cannot get active body for phantom class: " + this.getSignature());
      } else if (!soot.jbco.Main.metrics) {
         throw new RuntimeException("no active body present for method " + this.getSignature());
      } else {
         return this.activeBody;
      }
   }

   public Body retrieveActiveBody() {
      if (this.hasActiveBody()) {
         return this.getActiveBody();
      } else {
         this.declaringClass.checkLevel(3);
         if (this.declaringClass.isPhantomClass()) {
            throw new RuntimeException("cannot get resident body for phantom class : " + this.getSignature() + "; maybe you want to call c.setApplicationClass() on this class!");
         } else {
            Body b = this.getBodyFromMethodSource("jb");
            this.setActiveBody(b);
            if (Options.v().drop_bodies_after_load()) {
               this.ms = null;
            }

            return b;
         }
      }
   }

   public void setActiveBody(Body body) {
      if (this.declaringClass != null && this.declaringClass.isPhantomClass()) {
         throw new RuntimeException("cannot set active body for phantom class! " + this);
      } else {
         this.setPhantom(false);
         if (!this.isConcrete()) {
            throw new RuntimeException("cannot set body for non-concrete method! " + this);
         } else {
            if (body != null && body.getMethod() != this) {
               body.setMethod(this);
            }

            this.activeBody = body;
         }
      }
   }

   public boolean hasActiveBody() {
      return this.activeBody != null;
   }

   public void releaseActiveBody() {
      this.activeBody = null;
   }

   public void addExceptionIfAbsent(SootClass e) {
      if (!this.throwsException(e)) {
         this.addException(e);
      }

   }

   public void addException(SootClass e) {
      if (DEBUG) {
         System.out.println("Adding exception " + e);
      }

      if (this.exceptions == null) {
         this.exceptions = new ArrayList();
      } else if (this.exceptions.contains(e)) {
         throw new RuntimeException("already throws exception " + e.getName());
      }

      this.exceptions.add(e);
   }

   public void removeException(SootClass e) {
      if (DEBUG) {
         System.out.println("Removing exception " + e);
      }

      if (this.exceptions == null) {
         throw new RuntimeException("does not throw exception " + e.getName());
      } else if (!this.exceptions.contains(e)) {
         throw new RuntimeException("does not throw exception " + e.getName());
      } else {
         this.exceptions.remove(e);
      }
   }

   public boolean throwsException(SootClass e) {
      return this.exceptions != null && this.exceptions.contains(e);
   }

   public void setExceptions(List<SootClass> exceptions) {
      if (exceptions != null && !exceptions.isEmpty()) {
         this.exceptions = new ArrayList(exceptions);
      } else {
         this.exceptions = null;
      }

   }

   public List<SootClass> getExceptions() {
      if (this.exceptions == null) {
         this.exceptions = new ArrayList();
      }

      return this.exceptions;
   }

   public List<SootClass> getExceptionsUnsafe() {
      return this.exceptions;
   }

   public boolean isStatic() {
      return Modifier.isStatic(this.getModifiers());
   }

   public boolean isPrivate() {
      return Modifier.isPrivate(this.getModifiers());
   }

   public boolean isPublic() {
      return Modifier.isPublic(this.getModifiers());
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

   public boolean isNative() {
      return Modifier.isNative(this.getModifiers());
   }

   public boolean isSynchronized() {
      return Modifier.isSynchronized(this.getModifiers());
   }

   public boolean isMain() {
      if (this.isPublic() && this.isStatic()) {
         NumberedString main_sig = Scene.v().getSubSigNumberer().findOrAdd("void main(java.lang.String[])");
         if (main_sig.equals(this.subsignature)) {
            return true;
         }
      }

      return false;
   }

   public boolean isConstructor() {
      return this.name.equals("<init>");
   }

   public boolean isStaticInitializer() {
      return this.name.equals("<clinit>");
   }

   public boolean isEntryMethod() {
      return this.isStatic() && this.subsignature.equals(VirtualCalls.v().sigClinit) ? true : this.isMain();
   }

   public boolean isJavaLibraryMethod() {
      SootClass cl = this.getDeclaringClass();
      return cl.isJavaLibraryClass();
   }

   public String getBytecodeParms() {
      StringBuffer buffer = new StringBuffer();
      Iterator typeIt = this.getParameterTypes().iterator();

      while(typeIt.hasNext()) {
         Type type = (Type)typeIt.next();
         buffer.append(AbstractJasminClass.jasminDescriptorOf(type));
      }

      return buffer.toString().intern();
   }

   public String getBytecodeSignature() {
      String name = this.getName();
      StringBuffer buffer = new StringBuffer();
      buffer.append("<" + Scene.v().quotedNameOf(this.getDeclaringClass().getName()) + ": ");
      buffer.append(name);
      buffer.append(AbstractJasminClass.jasminDescriptorOf(this.makeRef()));
      buffer.append(">");
      return buffer.toString().intern();
   }

   public String getSignature() {
      return getSignature(this.getDeclaringClass(), this.getName(), this.getParameterTypes(), this.getReturnType());
   }

   public static String getSignature(SootClass cl, String name, List<Type> params, Type returnType) {
      StringBuilder buffer = new StringBuilder();
      buffer.append("<");
      buffer.append(Scene.v().quotedNameOf(cl.getName()));
      buffer.append(": ");
      buffer.append(getSubSignatureImpl(name, params, returnType));
      buffer.append(">");
      return buffer.toString().intern();
   }

   public String getSubSignature() {
      String name = this.getName();
      List<Type> params = this.getParameterTypes();
      Type returnType = this.getReturnType();
      return getSubSignatureImpl(name, params, returnType);
   }

   public static String getSubSignature(String name, List<Type> params, Type returnType) {
      return getSubSignatureImpl(name, params, returnType);
   }

   private static String getSubSignatureImpl(String name, List<Type> params, Type returnType) {
      StringBuilder buffer = new StringBuilder();
      buffer.append(returnType.toQuotedString());
      buffer.append(" ");
      buffer.append(Scene.v().quotedNameOf(name));
      buffer.append("(");
      if (params != null) {
         for(int i = 0; i < params.size(); ++i) {
            buffer.append(((Type)params.get(i)).toQuotedString());
            if (i < params.size() - 1) {
               buffer.append(",");
            }
         }
      }

      buffer.append(")");
      return buffer.toString();
   }

   public NumberedString getNumberedSubSignature() {
      return this.subsignature;
   }

   public String toString() {
      return this.getSignature();
   }

   public String getDavaDeclaration() {
      if (this.getName().equals("<clinit>")) {
         return "static";
      } else {
         StringBuffer buffer = new StringBuffer();
         StringTokenizer st = new StringTokenizer(Modifier.toString(this.getModifiers()));
         if (st.hasMoreTokens()) {
            buffer.append(st.nextToken());
         }

         while(st.hasMoreTokens()) {
            buffer.append(" " + st.nextToken());
         }

         if (buffer.length() != 0) {
            buffer.append(" ");
         }

         if (this.getName().equals("<init>")) {
            buffer.append(this.getDeclaringClass().getShortJavaStyleName());
         } else {
            Type t = this.getReturnType();
            String tempString = t.toString();
            if (this.hasActiveBody()) {
               DavaBody body = (DavaBody)this.getActiveBody();
               IterableSet<String> importSet = body.getImportList();
               if (!importSet.contains(tempString)) {
                  body.addToImportList(tempString);
               }

               tempString = RemoveFullyQualifiedName.getReducedName(importSet, tempString, t);
            }

            buffer.append(tempString + " ");
            buffer.append(Scene.v().quotedNameOf(this.getName()));
         }

         buffer.append("(");
         Iterator<Type> typeIt = this.getParameterTypes().iterator();
         int var10 = 0;

         while(typeIt.hasNext()) {
            Type t = (Type)typeIt.next();
            String tempString = t.toString();
            if (this.hasActiveBody()) {
               DavaBody body = (DavaBody)this.getActiveBody();
               IterableSet<String> importSet = body.getImportList();
               if (!importSet.contains(tempString)) {
                  body.addToImportList(tempString);
               }

               tempString = RemoveFullyQualifiedName.getReducedName(importSet, tempString, t);
            }

            buffer.append(tempString + " ");
            buffer.append(" ");
            if (this.hasActiveBody()) {
               buffer.append(((DavaBody)this.getActiveBody()).get_ParamMap().get(new Integer(var10++)));
            } else if (t == BooleanType.v()) {
               buffer.append("z" + var10++);
            } else if (t == ByteType.v()) {
               buffer.append("b" + var10++);
            } else if (t == ShortType.v()) {
               buffer.append("s" + var10++);
            } else if (t == CharType.v()) {
               buffer.append("c" + var10++);
            } else if (t == IntType.v()) {
               buffer.append("i" + var10++);
            } else if (t == LongType.v()) {
               buffer.append("l" + var10++);
            } else if (t == DoubleType.v()) {
               buffer.append("d" + var10++);
            } else if (t == FloatType.v()) {
               buffer.append("f" + var10++);
            } else if (t == StmtAddressType.v()) {
               buffer.append("a" + var10++);
            } else if (t == ErroneousType.v()) {
               buffer.append("e" + var10++);
            } else if (t == NullType.v()) {
               buffer.append("n" + var10++);
            } else {
               buffer.append("r" + var10++);
            }

            if (typeIt.hasNext()) {
               buffer.append(", ");
            }
         }

         buffer.append(")");
         if (this.exceptions != null) {
            Iterator<SootClass> exceptionIt = this.getExceptions().iterator();
            if (exceptionIt.hasNext()) {
               buffer.append(" throws " + ((SootClass)exceptionIt.next()).getName());

               while(exceptionIt.hasNext()) {
                  buffer.append(", " + ((SootClass)exceptionIt.next()).getName());
               }
            }
         }

         return buffer.toString().intern();
      }
   }

   public String getDeclaration() {
      StringBuffer buffer = new StringBuffer();
      StringTokenizer st = new StringTokenizer(Modifier.toString(this.getModifiers()));
      if (st.hasMoreTokens()) {
         buffer.append(st.nextToken());
      }

      while(st.hasMoreTokens()) {
         buffer.append(" " + st.nextToken());
      }

      if (buffer.length() != 0) {
         buffer.append(" ");
      }

      buffer.append(this.getReturnType().toQuotedString() + " ");
      buffer.append(Scene.v().quotedNameOf(this.getName()));
      buffer.append("(");
      Iterator typeIt = this.getParameterTypes().iterator();

      while(typeIt.hasNext()) {
         Type t = (Type)typeIt.next();
         buffer.append(t.toQuotedString());
         if (typeIt.hasNext()) {
            buffer.append(", ");
         }
      }

      buffer.append(")");
      if (this.exceptions != null) {
         Iterator<SootClass> exceptionIt = this.getExceptions().iterator();
         if (exceptionIt.hasNext()) {
            buffer.append(" throws " + Scene.v().quotedNameOf(((SootClass)exceptionIt.next()).getName()));

            while(exceptionIt.hasNext()) {
               buffer.append(", " + Scene.v().quotedNameOf(((SootClass)exceptionIt.next()).getName()));
            }
         }
      }

      return buffer.toString().intern();
   }

   public final int getNumber() {
      return this.number;
   }

   public final void setNumber(int number) {
      this.number = number;
   }

   public SootMethod method() {
      return this;
   }

   public Context context() {
      return null;
   }

   public SootMethodRef makeRef() {
      return Scene.v().makeMethodRef(this.declaringClass, this.name, this.parameterTypes == null ? null : Arrays.asList(this.parameterTypes), this.returnType, this.isStatic());
   }

   public int getJavaSourceStartLineNumber() {
      super.getJavaSourceStartLineNumber();
      if (this.line == -1 && this.hasActiveBody()) {
         PatchingChain<Unit> unit = this.getActiveBody().getUnits();
         Iterator var2 = unit.iterator();

         while(var2.hasNext()) {
            Unit u = (Unit)var2.next();
            int l = u.getJavaSourceStartLineNumber();
            if (l > -1) {
               this.line = l - 1;
               break;
            }
         }
      }

      return this.line;
   }
}
