package bsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class NameSpace implements Serializable, BshClassManager.Listener, NameSource {
   public static final NameSpace JAVACODE = new NameSpace((BshClassManager)null, "Called from compiled Java code.");
   private String nsName;
   private NameSpace parent;
   private Hashtable variables;
   private Hashtable methods;
   protected Hashtable importedClasses;
   private Vector importedPackages;
   private Vector importedCommands;
   private Vector importedObjects;
   private Vector importedStatic;
   private String packageName;
   private transient BshClassManager classManager;
   private This thisReference;
   private Hashtable names;
   SimpleNode callerInfoNode;
   boolean isMethod;
   boolean isClass;
   Class classStatic;
   Object classInstance;
   private transient Hashtable classCache;
   Vector nameSourceListeners;

   void setClassStatic(Class var1) {
      this.classStatic = var1;
      this.importStatic(var1);
   }

   void setClassInstance(Object var1) {
      this.classInstance = var1;
      this.importObject(var1);
   }

   Object getClassInstance() throws UtilEvalError {
      if (this.classInstance != null) {
         return this.classInstance;
      } else if (this.classStatic != null) {
         throw new UtilEvalError("Can't refer to class instance from static context.");
      } else {
         throw new InterpreterError("Can't resolve class instance 'this' in: " + this);
      }
   }

   public NameSpace(NameSpace var1, String var2) {
      this(var1, (BshClassManager)null, var2);
   }

   public NameSpace(BshClassManager var1, String var2) {
      this((NameSpace)null, var1, var2);
   }

   public NameSpace(NameSpace var1, BshClassManager var2, String var3) {
      this.setName(var3);
      this.setParent(var1);
      this.setClassManager(var2);
      if (var2 != null) {
         var2.addListener(this);
      }

   }

   public void setName(String var1) {
      this.nsName = var1;
   }

   public String getName() {
      return this.nsName;
   }

   void setNode(SimpleNode var1) {
      this.callerInfoNode = var1;
   }

   SimpleNode getNode() {
      if (this.callerInfoNode != null) {
         return this.callerInfoNode;
      } else {
         return this.parent != null ? this.parent.getNode() : null;
      }
   }

   public Object get(String var1, Interpreter var2) throws UtilEvalError {
      CallStack var3 = new CallStack(this);
      return this.getNameResolver(var1).toObject(var3, var2);
   }

   public void setVariable(String var1, Object var2, boolean var3) throws UtilEvalError {
      boolean var4 = Interpreter.LOCALSCOPING ? var3 : true;
      this.setVariable(var1, var2, var3, var4);
   }

   void setLocalVariable(String var1, Object var2, boolean var3) throws UtilEvalError {
      this.setVariable(var1, var2, var3, false);
   }

   void setVariable(String var1, Object var2, boolean var3, boolean var4) throws UtilEvalError {
      if (this.variables == null) {
         this.variables = new Hashtable();
      }

      if (var2 == null) {
         throw new InterpreterError("null variable value");
      } else {
         Variable var5 = this.getVariableImpl(var1, var4);
         if (var5 != null) {
            try {
               var5.setValue(var2, 1);
            } catch (UtilEvalError var7) {
               throw new UtilEvalError("Variable assignment: " + var1 + ": " + var7.getMessage());
            }
         } else {
            if (var3) {
               throw new UtilEvalError("(Strict Java mode) Assignment to undeclared variable: " + var1);
            }

            this.variables.put(var1, new Variable(var1, var2, (Modifiers)null));
            this.nameSpaceChanged();
         }

      }
   }

   public void unsetVariable(String var1) {
      if (this.variables != null) {
         this.variables.remove(var1);
         this.nameSpaceChanged();
      }

   }

   public String[] getVariableNames() {
      return this.variables == null ? new String[0] : this.enumerationToStringArray(this.variables.keys());
   }

   public String[] getMethodNames() {
      return this.methods == null ? new String[0] : this.enumerationToStringArray(this.methods.keys());
   }

   public BshMethod[] getMethods() {
      return this.methods == null ? new BshMethod[0] : this.flattenMethodCollection(this.methods.elements());
   }

   private String[] enumerationToStringArray(Enumeration var1) {
      Vector var2 = new Vector();

      while(var1.hasMoreElements()) {
         var2.addElement(var1.nextElement());
      }

      String[] var3 = new String[var2.size()];
      var2.copyInto(var3);
      return var3;
   }

   private BshMethod[] flattenMethodCollection(Enumeration var1) {
      Vector var2 = new Vector();

      while(true) {
         while(var1.hasMoreElements()) {
            Object var3 = var1.nextElement();
            if (var3 instanceof BshMethod) {
               var2.addElement(var3);
            } else {
               Vector var4 = (Vector)var3;

               for(int var5 = 0; var5 < var4.size(); ++var5) {
                  var2.addElement(var4.elementAt(var5));
               }
            }
         }

         BshMethod[] var6 = new BshMethod[var2.size()];
         var2.copyInto(var6);
         return var6;
      }
   }

   public NameSpace getParent() {
      return this.parent;
   }

   public This getSuper(Interpreter var1) {
      return this.parent != null ? this.parent.getThis(var1) : this.getThis(var1);
   }

   public This getGlobal(Interpreter var1) {
      return this.parent != null ? this.parent.getGlobal(var1) : this.getThis(var1);
   }

   This getThis(Interpreter var1) {
      if (this.thisReference == null) {
         this.thisReference = This.getThis(this, var1);
      }

      return this.thisReference;
   }

   public BshClassManager getClassManager() {
      if (this.classManager != null) {
         return this.classManager;
      } else if (this.parent != null && this.parent != JAVACODE) {
         return this.parent.getClassManager();
      } else {
         System.out.println("experiment: creating class manager");
         this.classManager = BshClassManager.createClassManager((Interpreter)null);
         return this.classManager;
      }
   }

   void setClassManager(BshClassManager var1) {
      this.classManager = var1;
   }

   public void prune() {
      if (this.classManager == null) {
         this.setClassManager(BshClassManager.createClassManager((Interpreter)null));
      }

      this.setParent((NameSpace)null);
   }

   public void setParent(NameSpace var1) {
      this.parent = var1;
      if (var1 == null) {
         this.loadDefaultImports();
      }

   }

   public Object getVariable(String var1) throws UtilEvalError {
      return this.getVariable(var1, true);
   }

   public Object getVariable(String var1, boolean var2) throws UtilEvalError {
      Variable var3 = this.getVariableImpl(var1, var2);
      return this.unwrapVariable(var3);
   }

   protected Variable getVariableImpl(String var1, boolean var2) throws UtilEvalError {
      Variable var3 = null;
      if (var3 == null && this.isClass) {
         var3 = this.getImportedVar(var1);
      }

      if (var3 == null && this.variables != null) {
         var3 = (Variable)this.variables.get(var1);
      }

      if (var3 == null && !this.isClass) {
         var3 = this.getImportedVar(var1);
      }

      if (var2 && var3 == null && this.parent != null) {
         var3 = this.parent.getVariableImpl(var1, var2);
      }

      return var3;
   }

   public Variable[] getDeclaredVariables() {
      if (this.variables == null) {
         return new Variable[0];
      } else {
         Variable[] var1 = new Variable[this.variables.size()];
         int var2 = 0;

         for(Enumeration var3 = this.variables.elements(); var3.hasMoreElements(); var1[var2++] = (Variable)var3.nextElement()) {
         }

         return var1;
      }
   }

   protected Object unwrapVariable(Variable var1) throws UtilEvalError {
      return var1 == null ? Primitive.VOID : var1.getValue();
   }

   /** @deprecated */
   public void setTypedVariable(String var1, Class var2, Object var3, boolean var4) throws UtilEvalError {
      Modifiers var5 = new Modifiers();
      if (var4) {
         var5.addModifier(2, "final");
      }

      this.setTypedVariable(var1, var2, var3, var5);
   }

   public void setTypedVariable(String var1, Class var2, Object var3, Modifiers var4) throws UtilEvalError {
      if (this.variables == null) {
         this.variables = new Hashtable();
      }

      Variable var5 = this.getVariableImpl(var1, false);
      if (var5 != null && var5.getType() != null) {
         if (var5.getType() != var2) {
            throw new UtilEvalError("Typed variable: " + var1 + " was previously declared with type: " + var5.getType());
         } else {
            var5.setValue(var3, 0);
         }
      } else {
         this.variables.put(var1, new Variable(var1, var2, var3, var4));
      }
   }

   public void setMethod(String var1, BshMethod var2) throws UtilEvalError {
      if (this.methods == null) {
         this.methods = new Hashtable();
      }

      Object var3 = this.methods.get(var1);
      if (var3 == null) {
         this.methods.put(var1, var2);
      } else if (var3 instanceof BshMethod) {
         Vector var4 = new Vector();
         var4.addElement(var3);
         var4.addElement(var2);
         this.methods.put(var1, var4);
      } else {
         ((Vector)var3).addElement(var2);
      }

   }

   public BshMethod getMethod(String var1, Class[] var2) throws UtilEvalError {
      return this.getMethod(var1, var2, false);
   }

   public BshMethod getMethod(String var1, Class[] var2, boolean var3) throws UtilEvalError {
      BshMethod var4 = null;
      if (var4 == null && this.isClass && !var3) {
         var4 = this.getImportedMethod(var1, var2);
      }

      Object var5 = null;
      if (var4 == null && this.methods != null) {
         var5 = this.methods.get(var1);
         if (var5 != null) {
            BshMethod[] var7;
            if (var5 instanceof Vector) {
               Vector var6 = (Vector)var5;
               var7 = new BshMethod[var6.size()];
               var6.copyInto(var7);
            } else {
               var7 = new BshMethod[]{(BshMethod)var5};
            }

            Class[][] var10 = new Class[var7.length][];

            for(int var8 = 0; var8 < var7.length; ++var8) {
               var10[var8] = var7[var8].getParameterTypes();
            }

            int var9 = Reflect.findMostSpecificSignature(var2, var10);
            if (var9 != -1) {
               var4 = var7[var9];
            }
         }
      }

      if (var4 == null && !this.isClass && !var3) {
         var4 = this.getImportedMethod(var1, var2);
      }

      return !var3 && var4 == null && this.parent != null ? this.parent.getMethod(var1, var2) : var4;
   }

   public void importClass(String var1) {
      if (this.importedClasses == null) {
         this.importedClasses = new Hashtable();
      }

      this.importedClasses.put(Name.suffix(var1, 1), var1);
      this.nameSpaceChanged();
   }

   public void importPackage(String var1) {
      if (this.importedPackages == null) {
         this.importedPackages = new Vector();
      }

      if (this.importedPackages.contains(var1)) {
         this.importedPackages.remove(var1);
      }

      this.importedPackages.addElement(var1);
      this.nameSpaceChanged();
   }

   public void importCommands(String var1) {
      if (this.importedCommands == null) {
         this.importedCommands = new Vector();
      }

      var1 = var1.replace('.', '/');
      if (!var1.startsWith("/")) {
         var1 = "/" + var1;
      }

      if (var1.length() > 1 && var1.endsWith("/")) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      if (this.importedCommands.contains(var1)) {
         this.importedCommands.remove(var1);
      }

      this.importedCommands.addElement(var1);
      this.nameSpaceChanged();
   }

   public Object getCommand(String var1, Class[] var2, Interpreter var3) throws UtilEvalError {
      if (Interpreter.DEBUG) {
         Interpreter.debug("getCommand: " + var1);
      }

      BshClassManager var4 = var3.getClassManager();
      if (this.importedCommands != null) {
         for(int var5 = this.importedCommands.size() - 1; var5 >= 0; --var5) {
            String var6 = (String)this.importedCommands.elementAt(var5);
            String var7;
            if (var6.equals("/")) {
               var7 = var6 + var1 + ".bsh";
            } else {
               var7 = var6 + "/" + var1 + ".bsh";
            }

            Interpreter.debug("searching for script: " + var7);
            InputStream var8 = var4.getResourceAsStream(var7);
            if (var8 != null) {
               return this.loadScriptedCommand(var8, var1, var2, var7, var3);
            }

            String var9;
            if (var6.equals("/")) {
               var9 = var1;
            } else {
               var9 = var6.substring(1).replace('/', '.') + "." + var1;
            }

            Interpreter.debug("searching for class: " + var9);
            Class var10 = var4.classForName(var9);
            if (var10 != null) {
               return var10;
            }
         }
      }

      return this.parent != null ? this.parent.getCommand(var1, var2, var3) : null;
   }

   protected BshMethod getImportedMethod(String var1, Class[] var2) throws UtilEvalError {
      Class var5;
      Method var6;
      if (this.importedObjects != null) {
         for(int var3 = 0; var3 < this.importedObjects.size(); ++var3) {
            Object var4 = this.importedObjects.elementAt(var3);
            var5 = var4.getClass();
            var6 = Reflect.resolveJavaMethod(this.getClassManager(), var5, var1, var2, false);
            if (var6 != null) {
               return new BshMethod(var6, var4);
            }
         }
      }

      if (this.importedStatic != null) {
         for(int var7 = 0; var7 < this.importedStatic.size(); ++var7) {
            var5 = (Class)this.importedStatic.elementAt(var7);
            var6 = Reflect.resolveJavaMethod(this.getClassManager(), var5, var1, var2, true);
            if (var6 != null) {
               return new BshMethod(var6, (Object)null);
            }
         }
      }

      return null;
   }

   protected Variable getImportedVar(String var1) throws UtilEvalError {
      Class var4;
      Field var5;
      if (this.importedObjects != null) {
         for(int var2 = 0; var2 < this.importedObjects.size(); ++var2) {
            Object var3 = this.importedObjects.elementAt(var2);
            var4 = var3.getClass();
            var5 = Reflect.resolveJavaField(var4, var1, false);
            if (var5 != null) {
               return new Variable(var1, var5.getType(), new LHS(var3, var5));
            }
         }
      }

      if (this.importedStatic != null) {
         for(int var6 = 0; var6 < this.importedStatic.size(); ++var6) {
            var4 = (Class)this.importedStatic.elementAt(var6);
            var5 = Reflect.resolveJavaField(var4, var1, true);
            if (var5 != null) {
               return new Variable(var1, var5.getType(), new LHS(var5));
            }
         }
      }

      return null;
   }

   private BshMethod loadScriptedCommand(InputStream var1, String var2, Class[] var3, String var4, Interpreter var5) throws UtilEvalError {
      try {
         var5.eval(new InputStreamReader(var1), this, var4);
      } catch (EvalError var7) {
         Interpreter.debug(var7.toString());
         throw new UtilEvalError("Error loading script: " + var7.getMessage());
      }

      BshMethod var6 = this.getMethod(var2, var3);
      return var6;
   }

   void cacheClass(String var1, Class var2) {
      if (this.classCache == null) {
         this.classCache = new Hashtable();
      }

      this.classCache.put(var1, var2);
   }

   public Class getClass(String var1) throws UtilEvalError {
      Class var2 = this.getClassImpl(var1);
      if (var2 != null) {
         return var2;
      } else {
         return this.parent != null ? this.parent.getClass(var1) : null;
      }
   }

   private Class getClassImpl(String var1) throws UtilEvalError {
      Class var2 = null;
      if (this.classCache != null) {
         var2 = (Class)this.classCache.get(var1);
         if (var2 != null) {
            return var2;
         }
      }

      boolean var3 = !Name.isCompound(var1);
      if (var3) {
         if (var2 == null) {
            var2 = this.getImportedClassImpl(var1);
         }

         if (var2 != null) {
            this.cacheClass(var1, var2);
            return var2;
         }
      }

      var2 = this.classForName(var1);
      if (var2 != null) {
         if (var3) {
            this.cacheClass(var1, var2);
         }

         return var2;
      } else {
         if (Interpreter.DEBUG) {
            Interpreter.debug("getClass(): " + var1 + " not\tfound in " + this);
         }

         return null;
      }
   }

   private Class getImportedClassImpl(String var1) throws UtilEvalError {
      String var2 = null;
      if (this.importedClasses != null) {
         var2 = (String)this.importedClasses.get(var1);
      }

      if (var2 != null) {
         Class var7 = this.classForName(var2);
         if (var7 == null) {
            if (Name.isCompound(var2)) {
               try {
                  var7 = this.getNameResolver(var2).toClass();
               } catch (ClassNotFoundException var6) {
               }
            } else if (Interpreter.DEBUG) {
               Interpreter.debug("imported unpackaged name not found:" + var2);
            }

            if (var7 != null) {
               this.getClassManager().cacheClassInfo(var2, var7);
               return var7;
            } else {
               return null;
            }
         } else {
            return var7;
         }
      } else {
         if (this.importedPackages != null) {
            for(int var3 = this.importedPackages.size() - 1; var3 >= 0; --var3) {
               String var4 = (String)this.importedPackages.elementAt(var3) + "." + var1;
               Class var5 = this.classForName(var4);
               if (var5 != null) {
                  return var5;
               }
            }
         }

         BshClassManager var8 = this.getClassManager();
         if (var8.hasSuperImport()) {
            String var9 = var8.getClassNameByUnqName(var1);
            if (var9 != null) {
               return this.classForName(var9);
            }
         }

         return null;
      }
   }

   private Class classForName(String var1) {
      return this.getClassManager().classForName(var1);
   }

   public String[] getAllNames() {
      Vector var1 = new Vector();
      this.getAllNamesAux(var1);
      String[] var2 = new String[var1.size()];
      var1.copyInto(var2);
      return var2;
   }

   protected void getAllNamesAux(Vector var1) {
      Enumeration var2 = this.variables.keys();

      while(var2.hasMoreElements()) {
         var1.addElement(var2.nextElement());
      }

      Enumeration var3 = this.methods.keys();

      while(var3.hasMoreElements()) {
         var1.addElement(var3.nextElement());
      }

      if (this.parent != null) {
         this.parent.getAllNamesAux(var1);
      }

   }

   public void addNameSourceListener(NameSource.Listener var1) {
      if (this.nameSourceListeners == null) {
         this.nameSourceListeners = new Vector();
      }

      this.nameSourceListeners.addElement(var1);
   }

   public void doSuperImport() throws UtilEvalError {
      this.getClassManager().doSuperImport();
   }

   public String toString() {
      return "NameSpace: " + (this.nsName == null ? super.toString() : this.nsName + " (" + super.toString() + ")") + (this.isClass ? " (isClass) " : "") + (this.isMethod ? " (method) " : "") + (this.classStatic != null ? " (class static) " : "") + (this.classInstance != null ? " (class instance) " : "");
   }

   private synchronized void writeObject(ObjectOutputStream var1) throws IOException {
      this.names = null;
      var1.defaultWriteObject();
   }

   public Object invokeMethod(String var1, Object[] var2, Interpreter var3) throws EvalError {
      return this.invokeMethod(var1, var2, var3, (CallStack)null, (SimpleNode)null);
   }

   public Object invokeMethod(String var1, Object[] var2, Interpreter var3, CallStack var4, SimpleNode var5) throws EvalError {
      return this.getThis(var3).invokeMethod(var1, var2, var3, var4, var5, false);
   }

   public void classLoaderChanged() {
      this.nameSpaceChanged();
   }

   public void nameSpaceChanged() {
      this.classCache = null;
      this.names = null;
   }

   public void loadDefaultImports() {
      this.importClass("bsh.EvalError");
      this.importClass("bsh.Interpreter");
      this.importPackage("javax.swing.event");
      this.importPackage("javax.swing");
      this.importPackage("java.awt.event");
      this.importPackage("java.awt");
      this.importPackage("java.net");
      this.importPackage("java.util");
      this.importPackage("java.io");
      this.importPackage("java.lang");
      this.importCommands("/bsh/commands");
   }

   Name getNameResolver(String var1) {
      if (this.names == null) {
         this.names = new Hashtable();
      }

      Name var2 = (Name)this.names.get(var1);
      if (var2 == null) {
         var2 = new Name(this, var1);
         this.names.put(var1, var2);
      }

      return var2;
   }

   public int getInvocationLine() {
      SimpleNode var1 = this.getNode();
      return var1 != null ? var1.getLineNumber() : -1;
   }

   public String getInvocationText() {
      SimpleNode var1 = this.getNode();
      return var1 != null ? var1.getText() : "<invoked from Java code>";
   }

   public static Class identifierToClass(ClassIdentifier var0) {
      return var0.getTargetClass();
   }

   public void clear() {
      this.variables = null;
      this.methods = null;
      this.importedClasses = null;
      this.importedPackages = null;
      this.importedCommands = null;
      this.importedObjects = null;
      if (this.parent == null) {
         this.loadDefaultImports();
      }

      this.classCache = null;
      this.names = null;
   }

   public void importObject(Object var1) {
      if (this.importedObjects == null) {
         this.importedObjects = new Vector();
      }

      if (this.importedObjects.contains(var1)) {
         this.importedObjects.remove(var1);
      }

      this.importedObjects.addElement(var1);
      this.nameSpaceChanged();
   }

   public void importStatic(Class var1) {
      if (this.importedStatic == null) {
         this.importedStatic = new Vector();
      }

      if (this.importedStatic.contains(var1)) {
         this.importedStatic.remove(var1);
      }

      this.importedStatic.addElement(var1);
      this.nameSpaceChanged();
   }

   void setPackage(String var1) {
      this.packageName = var1;
   }

   String getPackage() {
      if (this.packageName != null) {
         return this.packageName;
      } else {
         return this.parent != null ? this.parent.getPackage() : null;
      }
   }

   static {
      JAVACODE.isMethod = true;
   }
}
