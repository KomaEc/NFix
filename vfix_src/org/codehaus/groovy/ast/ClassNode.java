package org.codehaus.groovy.ast;

import groovy.lang.GroovyObject;
import groovyjarjarasm.asm.Opcodes;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.codehaus.groovy.vmplugin.VMPluginFactory;

public class ClassNode extends AnnotatedNode implements Opcodes {
   public static final ClassNode[] EMPTY_ARRAY = new ClassNode[0];
   public static final ClassNode THIS = new ClassNode(Object.class);
   public static final ClassNode SUPER = new ClassNode(Object.class);
   private String name;
   private int modifiers;
   private ClassNode[] interfaces;
   private MixinNode[] mixins;
   private List<ConstructorNode> constructors;
   private List<Statement> objectInitializers;
   private ClassNode.MapOfLists methods;
   private List<MethodNode> methodsList;
   private LinkedList<FieldNode> fields;
   private List<PropertyNode> properties;
   private Map<String, FieldNode> fieldIndex;
   private ModuleNode module;
   private CompileUnit compileUnit;
   private boolean staticClass;
   private boolean scriptBody;
   private boolean script;
   private ClassNode superClass;
   protected boolean isPrimaryNode;
   protected List<InnerClassNode> innerClasses;
   private Map<CompilePhase, Map<Class<? extends ASTTransformation>, Set<ASTNode>>> transformInstances;
   protected Object lazyInitLock;
   protected Class clazz;
   private boolean lazyInitDone;
   private ClassNode componentType;
   private ClassNode redirect;
   private boolean annotated;
   private GenericsType[] genericsTypes;
   private boolean usesGenerics;
   private boolean placeholder;
   private MethodNode enclosingMethod;

   public ClassNode redirect() {
      return this.redirect == null ? this : this.redirect.redirect();
   }

   public void setRedirect(ClassNode cn) {
      if (this.isPrimaryNode) {
         throw new GroovyBugError("tried to set a redirect for a primary ClassNode (" + this.getName() + "->" + cn.getName() + ").");
      } else {
         if (cn != null) {
            cn = cn.redirect();
         }

         if (cn != this) {
            this.redirect = cn;
         }
      }
   }

   public ClassNode makeArray() {
      ClassNode cn;
      if (this.redirect != null) {
         cn = this.redirect().makeArray();
         cn.componentType = this;
         return cn;
      } else {
         if (this.clazz != null) {
            Class ret = Array.newInstance(this.clazz, 0).getClass();
            cn = new ClassNode(ret, this);
         } else {
            cn = new ClassNode(this);
         }

         return cn;
      }
   }

   public boolean isPrimaryClassNode() {
      return this.redirect().isPrimaryNode || this.componentType != null && this.componentType.isPrimaryClassNode();
   }

   private ClassNode(ClassNode componentType) {
      this(componentType.getName() + "[]", 1, ClassHelper.OBJECT_TYPE);
      this.componentType = componentType.redirect();
      this.isPrimaryNode = false;
   }

   private ClassNode(Class c, ClassNode componentType) {
      this(c);
      this.componentType = componentType;
      this.isPrimaryNode = false;
   }

   public ClassNode(Class c) {
      this(c.getName(), c.getModifiers(), (ClassNode)null, (ClassNode[])null, MixinNode.EMPTY_ARRAY);
      this.clazz = c;
      this.lazyInitDone = false;
      CompileUnit cu = this.getCompileUnit();
      if (cu != null) {
         cu.addClass(this);
      }

      this.isPrimaryNode = false;
   }

   private void lazyClassInit() {
      synchronized(this.lazyInitLock) {
         if (this.redirect != null) {
            throw new GroovyBugError("lazyClassInit called on a proxy ClassNode, that must not happen.A redirect() call is missing somewhere!");
         } else if (!this.lazyInitDone) {
            VMPluginFactory.getPlugin().configureClassNode(this.compileUnit, this);
            this.lazyInitDone = true;
         }
      }
   }

   public MethodNode getEnclosingMethod() {
      return this.redirect().enclosingMethod;
   }

   public void setEnclosingMethod(MethodNode enclosingMethod) {
      this.redirect().enclosingMethod = enclosingMethod;
   }

   public ClassNode(String name, int modifiers, ClassNode superClass) {
      this(name, modifiers, superClass, EMPTY_ARRAY, MixinNode.EMPTY_ARRAY);
   }

   public ClassNode(String name, int modifiers, ClassNode superClass, ClassNode[] interfaces, MixinNode[] mixins) {
      this.staticClass = false;
      this.scriptBody = false;
      this.lazyInitLock = new Object();
      this.lazyInitDone = true;
      this.componentType = null;
      this.redirect = null;
      this.genericsTypes = null;
      this.usesGenerics = false;
      this.enclosingMethod = null;
      this.name = name;
      this.modifiers = modifiers;
      this.superClass = superClass;
      this.interfaces = interfaces;
      this.mixins = mixins;
      this.isPrimaryNode = true;
      if (superClass != null) {
         this.usesGenerics = superClass.isUsingGenerics();
      }

      if (!this.usesGenerics && interfaces != null) {
         ClassNode[] arr$ = interfaces;
         int len$ = interfaces.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            ClassNode anInterface = arr$[i$];
            this.usesGenerics = this.usesGenerics || anInterface.isUsingGenerics();
         }
      }

      this.methods = new ClassNode.MapOfLists();
      this.methodsList = new ArrayList();
   }

   public void setSuperClass(ClassNode superClass) {
      this.redirect().superClass = superClass;
   }

   public List<FieldNode> getFields() {
      if (!this.redirect().lazyInitDone) {
         this.redirect().lazyClassInit();
      }

      if (this.redirect != null) {
         return this.redirect().getFields();
      } else {
         if (this.fields == null) {
            this.fields = new LinkedList();
         }

         return this.fields;
      }
   }

   public ClassNode[] getInterfaces() {
      if (!this.redirect().lazyInitDone) {
         this.redirect().lazyClassInit();
      }

      return this.redirect != null ? this.redirect().getInterfaces() : this.interfaces;
   }

   public void setInterfaces(ClassNode[] interfaces) {
      if (this.redirect != null) {
         this.redirect().setInterfaces(interfaces);
      } else {
         this.interfaces = interfaces;
      }

   }

   public MixinNode[] getMixins() {
      return this.redirect().mixins;
   }

   public List<MethodNode> getMethods() {
      if (!this.redirect().lazyInitDone) {
         this.redirect().lazyClassInit();
      }

      return this.redirect != null ? this.redirect().getMethods() : this.methodsList;
   }

   public List<MethodNode> getAbstractMethods() {
      List<MethodNode> result = new ArrayList(3);
      Iterator i$ = this.getDeclaredMethodsMap().values().iterator();

      while(i$.hasNext()) {
         MethodNode method = (MethodNode)i$.next();
         if (method.isAbstract()) {
            result.add(method);
         }
      }

      if (result.isEmpty()) {
         return null;
      } else {
         return result;
      }
   }

   public List<MethodNode> getAllDeclaredMethods() {
      return new ArrayList(this.getDeclaredMethodsMap().values());
   }

   public Set<ClassNode> getAllInterfaces() {
      Set<ClassNode> res = new HashSet();
      this.getAllInterfaces(res);
      return res;
   }

   private void getAllInterfaces(Set<ClassNode> res) {
      if (this.isInterface()) {
         res.add(this);
      }

      ClassNode[] arr$ = this.getInterfaces();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ClassNode anInterface = arr$[i$];
         res.add(anInterface);
         anInterface.getAllInterfaces(res);
      }

   }

   public Map<String, MethodNode> getDeclaredMethodsMap() {
      ClassNode parent = this.getSuperClass();
      Map<String, MethodNode> result = null;
      if (parent != null) {
         result = parent.getDeclaredMethodsMap();
      } else {
         result = new HashMap();
      }

      ClassNode[] arr$ = this.getInterfaces();
      int len$ = arr$.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ClassNode iface = arr$[i$];
         Map<String, MethodNode> ifaceMethodsMap = iface.getDeclaredMethodsMap();
         Iterator i$ = ifaceMethodsMap.keySet().iterator();

         while(i$.hasNext()) {
            String methSig = (String)i$.next();
            if (!((Map)result).containsKey(methSig)) {
               MethodNode methNode = (MethodNode)ifaceMethodsMap.get(methSig);
               ((Map)result).put(methSig, methNode);
            }
         }
      }

      Iterator i$ = this.getMethods().iterator();

      while(i$.hasNext()) {
         MethodNode method = (MethodNode)i$.next();
         String sig = method.getTypeDescriptor();
         ((Map)result).put(sig, method);
      }

      return (Map)result;
   }

   public String getName() {
      return this.redirect().name;
   }

   public String getUnresolvedName() {
      return this.name;
   }

   public String setName(String name) {
      return this.redirect().name = name;
   }

   public int getModifiers() {
      return this.redirect().modifiers;
   }

   public void setModifiers(int modifiers) {
      this.redirect().modifiers = modifiers;
   }

   public List<PropertyNode> getProperties() {
      ClassNode r = this.redirect();
      if (r.properties == null) {
         r.properties = new ArrayList();
      }

      return r.properties;
   }

   public List<ConstructorNode> getDeclaredConstructors() {
      if (!this.redirect().lazyInitDone) {
         this.redirect().lazyClassInit();
      }

      ClassNode r = this.redirect();
      if (r.constructors == null) {
         r.constructors = new ArrayList();
      }

      return r.constructors;
   }

   public ModuleNode getModule() {
      return this.redirect().module;
   }

   public PackageNode getPackage() {
      return this.getModule() == null ? null : this.getModule().getPackage();
   }

   public void setModule(ModuleNode module) {
      this.redirect().module = module;
      if (module != null) {
         this.redirect().compileUnit = module.getUnit();
      }

   }

   public void addField(FieldNode node) {
      ClassNode r = this.redirect();
      node.setDeclaringClass(r);
      node.setOwner(r);
      if (r.fields == null) {
         r.fields = new LinkedList();
      }

      if (r.fieldIndex == null) {
         r.fieldIndex = new HashMap();
      }

      r.fields.add(node);
      r.fieldIndex.put(node.getName(), node);
   }

   public void addFieldFirst(FieldNode node) {
      ClassNode r = this.redirect();
      node.setDeclaringClass(r);
      node.setOwner(r);
      if (r.fields == null) {
         r.fields = new LinkedList();
      }

      if (r.fieldIndex == null) {
         r.fieldIndex = new HashMap();
      }

      r.fields.addFirst(node);
      r.fieldIndex.put(node.getName(), node);
   }

   public void addProperty(PropertyNode node) {
      node.setDeclaringClass(this.redirect());
      FieldNode field = node.getField();
      this.addField(field);
      ClassNode r = this.redirect();
      if (r.properties == null) {
         r.properties = new ArrayList();
      }

      r.properties.add(node);
   }

   public PropertyNode addProperty(String name, int modifiers, ClassNode type, Expression initialValueExpression, Statement getterBlock, Statement setterBlock) {
      Iterator i$ = this.getProperties().iterator();

      PropertyNode pn;
      do {
         if (!i$.hasNext()) {
            PropertyNode node = new PropertyNode(name, modifiers, type, this.redirect(), initialValueExpression, getterBlock, setterBlock);
            this.addProperty(node);
            return node;
         }

         pn = (PropertyNode)i$.next();
      } while(!pn.getName().equals(name));

      if (pn.getInitialExpression() == null && initialValueExpression != null) {
         pn.getField().setInitialValueExpression(initialValueExpression);
      }

      if (pn.getGetterBlock() == null && getterBlock != null) {
         pn.setGetterBlock(getterBlock);
      }

      if (pn.getSetterBlock() == null && setterBlock != null) {
         pn.setSetterBlock(setterBlock);
      }

      return pn;
   }

   public boolean hasProperty(String name) {
      return this.getProperty(name) != null;
   }

   public PropertyNode getProperty(String name) {
      Iterator i$ = this.getProperties().iterator();

      PropertyNode pn;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         pn = (PropertyNode)i$.next();
      } while(!pn.getName().equals(name));

      return pn;
   }

   public void addConstructor(ConstructorNode node) {
      node.setDeclaringClass(this);
      ClassNode r = this.redirect();
      if (r.constructors == null) {
         r.constructors = new ArrayList();
      }

      r.constructors.add(node);
   }

   public ConstructorNode addConstructor(int modifiers, Parameter[] parameters, ClassNode[] exceptions, Statement code) {
      ConstructorNode node = new ConstructorNode(modifiers, parameters, exceptions, code);
      this.addConstructor(node);
      return node;
   }

   public void addMethod(MethodNode node) {
      node.setDeclaringClass(this);
      this.redirect().methodsList.add(node);
      this.redirect().methods.put(node.getName(), node);
   }

   public MethodNode addMethod(String name, int modifiers, ClassNode returnType, Parameter[] parameters, ClassNode[] exceptions, Statement code) {
      MethodNode other = this.getDeclaredMethod(name, parameters);
      if (other != null) {
         return other;
      } else {
         MethodNode node = new MethodNode(name, modifiers, returnType, parameters, exceptions, code);
         this.addMethod(node);
         return node;
      }
   }

   public boolean hasDeclaredMethod(String name, Parameter[] parameters) {
      MethodNode other = this.getDeclaredMethod(name, parameters);
      return other != null;
   }

   public boolean hasMethod(String name, Parameter[] parameters) {
      MethodNode other = this.getMethod(name, parameters);
      return other != null;
   }

   public MethodNode addSyntheticMethod(String name, int modifiers, ClassNode returnType, Parameter[] parameters, ClassNode[] exceptions, Statement code) {
      MethodNode answer = this.addMethod(name, modifiers | 4096, returnType, parameters, exceptions, code);
      answer.setSynthetic(true);
      return answer;
   }

   public FieldNode addField(String name, int modifiers, ClassNode type, Expression initialValue) {
      FieldNode node = new FieldNode(name, modifiers, type, this.redirect(), initialValue);
      this.addField(node);
      return node;
   }

   public FieldNode addFieldFirst(String name, int modifiers, ClassNode type, Expression initialValue) {
      FieldNode node = new FieldNode(name, modifiers, type, this.redirect(), initialValue);
      this.addFieldFirst(node);
      return node;
   }

   public void addInterface(ClassNode type) {
      boolean skip = false;
      ClassNode[] interfaces = this.redirect().interfaces;
      ClassNode[] newInterfaces = interfaces;
      int len$ = interfaces.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         ClassNode existing = newInterfaces[i$];
         if (type.equals(existing)) {
            skip = true;
         }
      }

      if (!skip) {
         newInterfaces = new ClassNode[interfaces.length + 1];
         System.arraycopy(interfaces, 0, newInterfaces, 0, interfaces.length);
         newInterfaces[interfaces.length] = type;
         this.redirect().interfaces = newInterfaces;
      }

   }

   public boolean equals(Object o) {
      if (this.redirect != null) {
         return this.redirect().equals(o);
      } else {
         ClassNode cn = (ClassNode)o;
         return cn.getName().equals(this.getName());
      }
   }

   public int hashCode() {
      return this.redirect != null ? this.redirect().hashCode() : this.getName().hashCode();
   }

   public void addMixin(MixinNode mixin) {
      MixinNode[] mixins = this.redirect().mixins;
      boolean skip = false;
      MixinNode[] newMixins = mixins;
      int len$ = mixins.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         MixinNode existing = newMixins[i$];
         if (mixin.equals(existing)) {
            skip = true;
         }
      }

      if (!skip) {
         newMixins = new MixinNode[mixins.length + 1];
         System.arraycopy(mixins, 0, newMixins, 0, mixins.length);
         newMixins[mixins.length] = mixin;
         this.redirect().mixins = newMixins;
      }

   }

   public FieldNode getDeclaredField(String name) {
      ClassNode r = this.redirect();
      if (r.fieldIndex == null) {
         r.fieldIndex = new HashMap();
      }

      return (FieldNode)r.fieldIndex.get(name);
   }

   public FieldNode getField(String name) {
      for(ClassNode node = this; node != null; node = node.getSuperClass()) {
         FieldNode fn = node.getDeclaredField(name);
         if (fn != null) {
            return fn;
         }
      }

      return null;
   }

   public FieldNode getOuterField(String name) {
      return null;
   }

   public ClassNode getOuterClass() {
      return null;
   }

   public void addObjectInitializerStatements(Statement statements) {
      this.getObjectInitializerStatements().add(statements);
   }

   public List<Statement> getObjectInitializerStatements() {
      if (this.objectInitializers == null) {
         this.objectInitializers = new ArrayList();
      }

      return this.objectInitializers;
   }

   private MethodNode getOrAddStaticConstructorNode() {
      MethodNode method = null;
      List declaredMethods = this.getDeclaredMethods("<clinit>");
      if (declaredMethods.isEmpty()) {
         method = this.addMethod("<clinit>", 8, ClassHelper.VOID_TYPE, Parameter.EMPTY_ARRAY, EMPTY_ARRAY, new BlockStatement());
         method.setSynthetic(true);
      } else {
         method = (MethodNode)declaredMethods.get(0);
      }

      return method;
   }

   public void addStaticInitializerStatements(List<Statement> staticStatements, boolean fieldInit) {
      MethodNode method = this.getOrAddStaticConstructorNode();
      BlockStatement block = null;
      Statement statement = method.getCode();
      if (statement == null) {
         block = new BlockStatement();
      } else if (statement instanceof BlockStatement) {
         block = (BlockStatement)statement;
      } else {
         block = new BlockStatement();
         block.addStatement(statement);
      }

      if (!fieldInit) {
         block.addStatements(staticStatements);
      } else {
         List<Statement> blockStatements = block.getStatements();
         staticStatements.addAll(blockStatements);
         blockStatements.clear();
         blockStatements.addAll(staticStatements);
      }

   }

   public void positionStmtsAfterEnumInitStmts(List<Statement> staticFieldStatements) {
      MethodNode method = this.getOrAddStaticConstructorNode();
      Statement statement = method.getCode();
      if (statement instanceof BlockStatement) {
         BlockStatement block = (BlockStatement)statement;
         List<Statement> blockStatements = block.getStatements();
         ListIterator litr = blockStatements.listIterator();

         while(true) {
            FieldExpression fExp;
            do {
               BinaryExpression bExp;
               do {
                  Statement stmt;
                  do {
                     do {
                        if (!litr.hasNext()) {
                           return;
                        }

                        stmt = (Statement)litr.next();
                     } while(!(stmt instanceof ExpressionStatement));
                  } while(!(((ExpressionStatement)stmt).getExpression() instanceof BinaryExpression));

                  bExp = (BinaryExpression)((ExpressionStatement)stmt).getExpression();
               } while(!(bExp.getLeftExpression() instanceof FieldExpression));

               fExp = (FieldExpression)bExp.getLeftExpression();
            } while(!fExp.getFieldName().equals("$VALUES"));

            Iterator i$ = staticFieldStatements.iterator();

            while(i$.hasNext()) {
               Statement tmpStmt = (Statement)i$.next();
               litr.add(tmpStmt);
            }
         }
      }
   }

   public List<MethodNode> getDeclaredMethods(String name) {
      if (!this.redirect().lazyInitDone) {
         this.redirect().lazyClassInit();
      }

      return this.redirect != null ? this.redirect().getDeclaredMethods(name) : this.methods.getNotNull(name);
   }

   public List<MethodNode> getMethods(String name) {
      List<MethodNode> answer = new ArrayList();

      for(ClassNode node = this; node != null; node = node.getSuperClass()) {
         answer.addAll(node.getDeclaredMethods(name));
      }

      return answer;
   }

   public MethodNode getDeclaredMethod(String name, Parameter[] parameters) {
      Iterator i$ = this.getDeclaredMethods(name).iterator();

      MethodNode method;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         method = (MethodNode)i$.next();
      } while(!this.parametersEqual(method.getParameters(), parameters));

      return method;
   }

   public MethodNode getMethod(String name, Parameter[] parameters) {
      Iterator i$ = this.getMethods(name).iterator();

      MethodNode method;
      do {
         if (!i$.hasNext()) {
            return null;
         }

         method = (MethodNode)i$.next();
      } while(!this.parametersEqual(method.getParameters(), parameters));

      return method;
   }

   public boolean isDerivedFrom(ClassNode type) {
      if (this.equals(ClassHelper.VOID_TYPE)) {
         return type.equals(ClassHelper.VOID_TYPE);
      } else if (type.equals(ClassHelper.OBJECT_TYPE)) {
         return true;
      } else {
         for(ClassNode node = this; node != null; node = node.getSuperClass()) {
            if (type.equals(node)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isDerivedFromGroovyObject() {
      return this.implementsInterface(ClassHelper.make(GroovyObject.class));
   }

   public boolean implementsInterface(ClassNode classNode) {
      ClassNode node = this.redirect();

      while(!node.declaresInterface(classNode)) {
         node = node.getSuperClass();
         if (node == null) {
            return false;
         }
      }

      return true;
   }

   public boolean declaresInterface(ClassNode classNode) {
      ClassNode[] interfaces = this.redirect().getInterfaces();
      ClassNode[] arr$ = interfaces;
      int len$ = interfaces.length;

      int i$;
      ClassNode cn;
      for(i$ = 0; i$ < len$; ++i$) {
         cn = arr$[i$];
         if (cn.equals(classNode)) {
            return true;
         }
      }

      arr$ = interfaces;
      len$ = interfaces.length;

      for(i$ = 0; i$ < len$; ++i$) {
         cn = arr$[i$];
         if (cn.declaresInterface(classNode)) {
            return true;
         }
      }

      return false;
   }

   public ClassNode getSuperClass() {
      if (!this.lazyInitDone && !this.isResolved()) {
         throw new GroovyBugError("ClassNode#getSuperClass for " + this.getName() + " called before class resolving");
      } else {
         ClassNode sn = this.redirect().getUnresolvedSuperClass();
         if (sn != null) {
            sn = sn.redirect();
         }

         return sn;
      }
   }

   public ClassNode getUnresolvedSuperClass() {
      return this.getUnresolvedSuperClass(true);
   }

   public ClassNode getUnresolvedSuperClass(boolean useRedirect) {
      if (!useRedirect) {
         return this.superClass;
      } else {
         if (!this.redirect().lazyInitDone) {
            this.redirect().lazyClassInit();
         }

         return this.redirect().superClass;
      }
   }

   public void setUnresolvedSuperClass(ClassNode sn) {
      this.superClass = sn;
   }

   public ClassNode[] getUnresolvedInterfaces() {
      return this.getUnresolvedInterfaces(true);
   }

   public ClassNode[] getUnresolvedInterfaces(boolean useRedirect) {
      if (!useRedirect) {
         return this.interfaces;
      } else {
         if (!this.redirect().lazyInitDone) {
            this.redirect().lazyClassInit();
         }

         return this.redirect().interfaces;
      }
   }

   public CompileUnit getCompileUnit() {
      if (this.redirect != null) {
         return this.redirect().getCompileUnit();
      } else {
         if (this.compileUnit == null && this.module != null) {
            this.compileUnit = this.module.getUnit();
         }

         return this.compileUnit;
      }
   }

   protected void setCompileUnit(CompileUnit cu) {
      if (this.redirect != null) {
         this.redirect().setCompileUnit(cu);
      }

      if (this.compileUnit != null) {
         this.compileUnit = cu;
      }

   }

   protected boolean parametersEqual(Parameter[] a, Parameter[] b) {
      if (a.length != b.length) {
         return false;
      } else {
         boolean answer = true;

         for(int i = 0; i < a.length; ++i) {
            if (!a[i].getType().equals(b[i].getType())) {
               answer = false;
               break;
            }
         }

         return answer;
      }
   }

   public String getPackageName() {
      int idx = this.getName().lastIndexOf(46);
      return idx > 0 ? this.getName().substring(0, idx) : null;
   }

   public String getNameWithoutPackage() {
      int idx = this.getName().lastIndexOf(46);
      return idx > 0 ? this.getName().substring(idx + 1) : this.getName();
   }

   public void visitContents(GroovyClassVisitor visitor) {
      Iterator i$ = this.getProperties().iterator();

      while(i$.hasNext()) {
         PropertyNode pn = (PropertyNode)i$.next();
         visitor.visitProperty(pn);
      }

      i$ = this.getFields().iterator();

      while(i$.hasNext()) {
         FieldNode fn = (FieldNode)i$.next();
         visitor.visitField(fn);
      }

      i$ = this.getDeclaredConstructors().iterator();

      while(i$.hasNext()) {
         ConstructorNode cn = (ConstructorNode)i$.next();
         visitor.visitConstructor(cn);
      }

      i$ = this.getMethods().iterator();

      while(i$.hasNext()) {
         MethodNode mn = (MethodNode)i$.next();
         visitor.visitMethod(mn);
      }

   }

   public MethodNode getGetterMethod(String getterName) {
      Iterator i$ = this.getDeclaredMethods(getterName).iterator();

      MethodNode method;
      do {
         if (!i$.hasNext()) {
            ClassNode parent = this.getSuperClass();
            if (parent != null) {
               return parent.getGetterMethod(getterName);
            }

            return null;
         }

         method = (MethodNode)i$.next();
      } while(!getterName.equals(method.getName()) || ClassHelper.VOID_TYPE == method.getReturnType() || method.getParameters().length != 0);

      return method;
   }

   public MethodNode getSetterMethod(String setterName) {
      Iterator i$ = this.getDeclaredMethods(setterName).iterator();

      MethodNode method;
      do {
         if (!i$.hasNext()) {
            ClassNode parent = this.getSuperClass();
            if (parent != null) {
               return parent.getSetterMethod(setterName);
            }

            return null;
         }

         method = (MethodNode)i$.next();
      } while(!setterName.equals(method.getName()) || ClassHelper.VOID_TYPE != method.getReturnType() || method.getParameters().length != 1);

      return method;
   }

   public boolean isStaticClass() {
      return this.redirect().staticClass;
   }

   public void setStaticClass(boolean staticClass) {
      this.redirect().staticClass = staticClass;
   }

   public boolean isScriptBody() {
      return this.redirect().scriptBody;
   }

   public void setScriptBody(boolean scriptBody) {
      this.redirect().scriptBody = scriptBody;
   }

   public boolean isScript() {
      return this.redirect().script || this.isDerivedFrom(ClassHelper.SCRIPT_TYPE);
   }

   public void setScript(boolean script) {
      this.redirect().script = script;
   }

   public String toString() {
      String ret = this.getName();
      if (this.genericsTypes != null) {
         ret = ret + " <";

         for(int i = 0; i < this.genericsTypes.length; ++i) {
            if (i != 0) {
               ret = ret + ", ";
            }

            GenericsType genericsType = this.genericsTypes[i];
            ret = ret + this.genericTypeAsString(genericsType);
         }

         ret = ret + ">";
      }

      if (this.redirect != null) {
         ret = ret + " -> " + this.redirect().toString();
      }

      return ret;
   }

   private String genericTypeAsString(GenericsType genericsType) {
      String ret = genericsType.getName();
      if (genericsType.getUpperBounds() != null) {
         ret = ret + " extends ";

         for(int i = 0; i < genericsType.getUpperBounds().length; ++i) {
            ClassNode classNode = genericsType.getUpperBounds()[i];
            if (classNode.equals(this)) {
               ret = ret + classNode.getName();
            } else {
               ret = ret + classNode.toString();
            }

            if (i + 1 < genericsType.getUpperBounds().length) {
               ret = ret + " & ";
            }
         }
      } else if (genericsType.getLowerBound() != null) {
         ClassNode classNode = genericsType.getLowerBound();
         if (classNode.equals(this)) {
            ret = ret + " super " + classNode.getName();
         } else {
            ret = ret + " super " + classNode;
         }
      }

      return ret;
   }

   public boolean hasPossibleMethod(String name, Expression arguments) {
      int count = 0;
      if (arguments instanceof TupleExpression) {
         TupleExpression tuple = (TupleExpression)arguments;
         count = tuple.getExpressions().size();
      }

      ClassNode node = this;

      do {
         Iterator i$ = this.getMethods(name).iterator();

         while(i$.hasNext()) {
            MethodNode method = (MethodNode)i$.next();
            if (method.getParameters().length == count && !method.isStatic()) {
               return true;
            }
         }

         node = node.getSuperClass();
      } while(node != null);

      return false;
   }

   public MethodNode tryFindPossibleMethod(String name, Expression arguments) {
      int count = false;
      if (!(arguments instanceof TupleExpression)) {
         return null;
      } else {
         TupleExpression tuple = (TupleExpression)arguments;
         int count = tuple.getExpressions().size();
         MethodNode res = null;
         ClassNode node = this;
         TupleExpression args = (TupleExpression)arguments;

         label76:
         do {
            Iterator i$ = node.getMethods(name).iterator();

            while(true) {
               while(true) {
                  MethodNode method;
                  boolean match;
                  int i;
                  do {
                     do {
                        if (!i$.hasNext()) {
                           node = node.getSuperClass();
                           continue label76;
                        }

                        method = (MethodNode)i$.next();
                     } while(method.getParameters().length != count);

                     match = true;

                     for(i = 0; i != count; ++i) {
                        if (!args.getType().isDerivedFrom(method.getParameters()[i].getType())) {
                           match = false;
                           break;
                        }
                     }
                  } while(!match);

                  if (res == null) {
                     res = method;
                  } else {
                     if (res.getParameters().length != count) {
                        return null;
                     }

                     if (node.equals(this)) {
                        return null;
                     }

                     match = true;

                     for(i = 0; i != count; ++i) {
                        if (!res.getParameters()[i].getType().equals(method.getParameters()[i].getType())) {
                           match = false;
                           break;
                        }
                     }

                     if (!match) {
                        return null;
                     }
                  }
               }
            }
         } while(node != null);

         return res;
      }
   }

   public boolean hasPossibleStaticMethod(String name, Expression arguments) {
      int count = 0;
      if (arguments instanceof TupleExpression) {
         TupleExpression tuple = (TupleExpression)arguments;
         count = tuple.getExpressions().size();
      } else if (arguments instanceof MapExpression) {
         count = 1;
      }

      Iterator i$ = this.getMethods(name).iterator();

      Parameter[] parameters;
      int nonDefaultParameters;
      do {
         MethodNode method;
         do {
            if (!i$.hasNext()) {
               return false;
            }

            method = (MethodNode)i$.next();
         } while(!method.isStatic());

         parameters = method.getParameters();
         if (parameters.length == count) {
            return true;
         }

         if (parameters.length > 0 && parameters[parameters.length - 1].getType().isArray() && count >= parameters.length - 1) {
            return true;
         }

         nonDefaultParameters = 0;
         Parameter[] arr$ = parameters;
         int len$ = parameters.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Parameter parameter = arr$[i$];
            if (!parameter.hasInitialExpression()) {
               ++nonDefaultParameters;
            }
         }
      } while(count >= parameters.length || nonDefaultParameters > count);

      return true;
   }

   public boolean isInterface() {
      return (this.getModifiers() & 512) > 0;
   }

   public boolean isResolved() {
      return this.redirect().clazz != null || this.componentType != null && this.componentType.isResolved();
   }

   public boolean isArray() {
      return this.componentType != null;
   }

   public ClassNode getComponentType() {
      return this.componentType;
   }

   public Class getTypeClass() {
      Class c = this.redirect().clazz;
      if (c != null) {
         return c;
      } else {
         ClassNode component = this.redirect().componentType;
         if (component != null && component.isResolved()) {
            ClassNode cn = component.makeArray();
            this.setRedirect(cn);
            return this.redirect().clazz;
         } else {
            throw new GroovyBugError("ClassNode#getTypeClass for " + this.getName() + " is called before the type class is set ");
         }
      }
   }

   public boolean hasPackageName() {
      return this.redirect().name.indexOf(46) > 0;
   }

   public void setAnnotated(boolean flag) {
      this.annotated = flag;
   }

   public boolean isAnnotated() {
      return this.annotated;
   }

   public GenericsType[] getGenericsTypes() {
      return this.genericsTypes;
   }

   public void setGenericsTypes(GenericsType[] genericsTypes) {
      this.usesGenerics = this.usesGenerics || genericsTypes != null;
      this.genericsTypes = genericsTypes;
   }

   public void setGenericsPlaceHolder(boolean b) {
      this.usesGenerics = this.usesGenerics || b;
      this.placeholder = b;
   }

   public boolean isGenericsPlaceHolder() {
      return this.placeholder;
   }

   public boolean isUsingGenerics() {
      return this.usesGenerics;
   }

   public void setUsingGenerics(boolean b) {
      this.usesGenerics = b;
   }

   public ClassNode getPlainNodeReference() {
      if (ClassHelper.isPrimitiveType(this)) {
         return this;
      } else {
         ClassNode n = new ClassNode(this.getName(), this.getModifiers(), this.getSuperClass(), (ClassNode[])null, (MixinNode[])null);
         n.isPrimaryNode = false;
         n.setRedirect(this.redirect);
         n.componentType = this.redirect().getComponentType();
         return n;
      }
   }

   public boolean isAnnotationDefinition() {
      return this.redirect().isPrimaryNode && this.isInterface() && (this.getModifiers() & 8192) != 0;
   }

   public List<AnnotationNode> getAnnotations() {
      if (this.redirect != null) {
         return this.redirect.getAnnotations();
      } else {
         this.lazyClassInit();
         return super.getAnnotations();
      }
   }

   public List<AnnotationNode> getAnnotations(ClassNode type) {
      if (this.redirect != null) {
         return this.redirect.getAnnotations(type);
      } else {
         this.lazyClassInit();
         return super.getAnnotations(type);
      }
   }

   public void addTransform(Class<? extends ASTTransformation> transform, ASTNode node) {
      GroovyASTTransformation annotation = (GroovyASTTransformation)transform.getAnnotation(GroovyASTTransformation.class);
      Set<ASTNode> nodes = (Set)((Map)this.getTransformInstances().get(annotation.phase())).get(transform);
      if (nodes == null) {
         nodes = new LinkedHashSet();
         ((Map)this.getTransformInstances().get(annotation.phase())).put(transform, nodes);
      }

      ((Set)nodes).add(node);
   }

   public Map<Class<? extends ASTTransformation>, Set<ASTNode>> getTransforms(CompilePhase phase) {
      return (Map)this.getTransformInstances().get(phase);
   }

   public void renameField(String oldName, String newName) {
      ClassNode r = this.redirect();
      if (r.fieldIndex == null) {
         r.fieldIndex = new HashMap();
      }

      Map<String, FieldNode> index = r.fieldIndex;
      index.put(newName, index.remove(oldName));
   }

   public void removeField(String oldName) {
      ClassNode r = this.redirect();
      if (r.fieldIndex == null) {
         r.fieldIndex = new HashMap();
      }

      Map<String, FieldNode> index = r.fieldIndex;
      r.fields.remove(index.get(oldName));
      index.remove(oldName);
   }

   public boolean isEnum() {
      return (this.getModifiers() & 16384) != 0;
   }

   public Iterator<InnerClassNode> getInnerClasses() {
      return (this.innerClasses == null ? Collections.emptyList() : this.innerClasses).iterator();
   }

   private Map<CompilePhase, Map<Class<? extends ASTTransformation>, Set<ASTNode>>> getTransformInstances() {
      if (this.transformInstances == null) {
         this.transformInstances = new EnumMap(CompilePhase.class);
         CompilePhase[] arr$ = CompilePhase.values();
         int len$ = arr$.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            CompilePhase phase = arr$[i$];
            this.transformInstances.put(phase, new HashMap());
         }
      }

      return this.transformInstances;
   }

   private static class MapOfLists {
      private Map<Object, List<MethodNode>> map;

      private MapOfLists() {
         this.map = new HashMap();
      }

      public List<MethodNode> get(Object key) {
         return (List)this.map.get(key);
      }

      public List<MethodNode> getNotNull(Object key) {
         List<MethodNode> ret = this.get(key);
         if (ret == null) {
            ret = Collections.emptyList();
         }

         return ret;
      }

      public void put(Object key, MethodNode value) {
         if (this.map.containsKey(key)) {
            this.get(key).add(value);
         } else {
            ArrayList<MethodNode> list = new ArrayList(2);
            list.add(value);
            this.map.put(key, list);
         }

      }

      // $FF: synthetic method
      MapOfLists(Object x0) {
         this();
      }
   }
}
