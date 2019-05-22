package org.codehaus.groovy.classgen;

import groovy.lang.GroovyRuntimeException;
import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.ClassWriter;
import groovyjarjarasm.asm.FieldVisitor;
import groovyjarjarasm.asm.Label;
import groovyjarjarasm.asm.MethodAdapter;
import groovyjarjarasm.asm.MethodVisitor;
import groovyjarjarasm.asm.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.ConstructorNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.InnerClassNode;
import org.codehaus.groovy.ast.InterfaceHelperClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.PackageNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.VariableScope;
import org.codehaus.groovy.ast.expr.AnnotationConstantExpression;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ArrayExpression;
import org.codehaus.groovy.ast.expr.AttributeExpression;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BitwiseNegationExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.CastExpression;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.ClosureListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.DeclarationExpression;
import org.codehaus.groovy.ast.expr.ElvisOperatorExpression;
import org.codehaus.groovy.ast.expr.EmptyExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.ExpressionTransformer;
import org.codehaus.groovy.ast.expr.FieldExpression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.ListExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.MethodPointerExpression;
import org.codehaus.groovy.ast.expr.NotExpression;
import org.codehaus.groovy.ast.expr.PostfixExpression;
import org.codehaus.groovy.ast.expr.PrefixExpression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.expr.RangeExpression;
import org.codehaus.groovy.ast.expr.RegexExpression;
import org.codehaus.groovy.ast.expr.SpreadExpression;
import org.codehaus.groovy.ast.expr.SpreadMapExpression;
import org.codehaus.groovy.ast.expr.StaticMethodCallExpression;
import org.codehaus.groovy.ast.expr.TernaryExpression;
import org.codehaus.groovy.ast.expr.TupleExpression;
import org.codehaus.groovy.ast.expr.UnaryMinusExpression;
import org.codehaus.groovy.ast.expr.UnaryPlusExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.AssertStatement;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.BreakStatement;
import org.codehaus.groovy.ast.stmt.CaseStatement;
import org.codehaus.groovy.ast.stmt.CatchStatement;
import org.codehaus.groovy.ast.stmt.ContinueStatement;
import org.codehaus.groovy.ast.stmt.DoWhileStatement;
import org.codehaus.groovy.ast.stmt.ExpressionStatement;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.IfStatement;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.stmt.SwitchStatement;
import org.codehaus.groovy.ast.stmt.SynchronizedStatement;
import org.codehaus.groovy.ast.stmt.ThrowStatement;
import org.codehaus.groovy.ast.stmt.TryCatchStatement;
import org.codehaus.groovy.ast.stmt.WhileStatement;
import org.codehaus.groovy.control.Janitor;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.syntax.RuntimeParserException;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.transform.powerassert.SourceText;
import org.codehaus.groovy.transform.powerassert.SourceTextNotAvailableException;

public class AsmClassGenerator extends ClassGenerator {
   private AsmClassGenerator.AssertionTracker assertionTracker;
   private final ClassVisitor cv;
   private MethodVisitor mv;
   private GeneratorContext context;
   private String sourceFile;
   private ClassNode classNode;
   private ClassNode outermostClass;
   private String internalClassName;
   private String internalBaseClassName;
   private CompileStack compileStack;
   private boolean outputReturn;
   private boolean leftHandExpression = false;
   static final MethodCallerMultiAdapter invokeMethodOnCurrent = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "invokeMethodOnCurrent", true, false);
   static final MethodCallerMultiAdapter invokeMethodOnSuper = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "invokeMethodOnSuper", true, false);
   static final MethodCallerMultiAdapter invokeMethod = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "invokeMethod", true, false);
   static final MethodCallerMultiAdapter invokeStaticMethod = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "invokeStaticMethod", true, true);
   static final MethodCallerMultiAdapter invokeNew = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "invokeNew", true, true);
   static final MethodCallerMultiAdapter setField = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "setField", false, false);
   static final MethodCallerMultiAdapter getField = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "getField", false, false);
   static final MethodCallerMultiAdapter setGroovyObjectField = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "setGroovyObjectField", false, false);
   static final MethodCallerMultiAdapter getGroovyObjectField = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "getGroovyObjectField", false, false);
   static final MethodCallerMultiAdapter setFieldOnSuper = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "setFieldOnSuper", false, false);
   static final MethodCallerMultiAdapter getFieldOnSuper = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "getFieldOnSuper", false, false);
   static final MethodCallerMultiAdapter setProperty = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "setProperty", false, false);
   static final MethodCallerMultiAdapter getProperty = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "getProperty", false, false);
   static final MethodCallerMultiAdapter setGroovyObjectProperty = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "setGroovyObjectProperty", false, false);
   static final MethodCallerMultiAdapter getGroovyObjectProperty = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "getGroovyObjectProperty", false, false);
   static final MethodCallerMultiAdapter setPropertyOnSuper = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "setPropertyOnSuper", false, false);
   static final MethodCallerMultiAdapter getPropertyOnSuper = MethodCallerMultiAdapter.newStatic(ScriptBytecodeAdapter.class, "getPropertyOnSuper", false, false);
   static final MethodCaller iteratorNextMethod = MethodCaller.newInterface(Iterator.class, "next");
   static final MethodCaller iteratorHasNextMethod = MethodCaller.newInterface(Iterator.class, "hasNext");
   static final MethodCaller assertFailedMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "assertFailed");
   static final MethodCaller isCaseMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "isCase");
   static final MethodCaller compareIdenticalMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "compareIdentical");
   static final MethodCaller compareNotIdenticalMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "compareNotIdentical");
   static final MethodCaller compareEqualMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "compareEqual");
   static final MethodCaller compareNotEqualMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "compareNotEqual");
   static final MethodCaller compareToMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "compareTo");
   static final MethodCaller compareLessThanMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "compareLessThan");
   static final MethodCaller compareLessThanEqualMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "compareLessThanEqual");
   static final MethodCaller compareGreaterThanMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "compareGreaterThan");
   static final MethodCaller compareGreaterThanEqualMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "compareGreaterThanEqual");
   static final MethodCaller findRegexMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "findRegex");
   static final MethodCaller matchRegexMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "matchRegex");
   static final MethodCaller regexPattern = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "regexPattern");
   static final MethodCaller spreadMap = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "spreadMap");
   static final MethodCaller despreadList = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "despreadList");
   static final MethodCaller getMethodPointer = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "getMethodPointer");
   static final MethodCaller invokeClosureMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "invokeClosure");
   static final MethodCaller unaryPlus = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "unaryPlus");
   static final MethodCaller unaryMinus = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "unaryMinus");
   static final MethodCaller bitwiseNegate = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "bitwiseNegate");
   static final MethodCaller asTypeMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "asType");
   static final MethodCaller castToTypeMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "castToType");
   static final MethodCaller createListMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "createList");
   static final MethodCaller createTupleMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "createTuple");
   static final MethodCaller createMapMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "createMap");
   static final MethodCaller createRangeMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "createRange");
   static final MethodCaller createPojoWrapperMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "createPojoWrapper");
   static final MethodCaller createGroovyObjectWrapperMethod = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "createGroovyObjectWrapper");
   static final MethodCaller selectConstructorAndTransformArguments = MethodCaller.newStatic(ScriptBytecodeAdapter.class, "selectConstructorAndTransformArguments");
   private Map<String, ClassNode> referencedClasses = new HashMap();
   private boolean passingParams;
   private ConstructorNode constructorNode;
   private MethodNode methodNode;
   private BytecodeHelper helper = new BytecodeHelper((MethodVisitor)null);
   public static final boolean CREATE_DEBUG_INFO = true;
   public static final boolean CREATE_LINE_NUMBER_INFO = true;
   public static final boolean ASM_DEBUG = false;
   private int lineNumber = -1;
   private ASTNode currentASTNode = null;
   private ClassWriter dummyClassWriter = null;
   private ClassNode interfaceClassLoadingClass;
   private boolean implicitThis = false;
   private Map<String, GenericsType> genericParameterNames = null;
   private ClassNode rightHandType;
   private static final String CONSTRUCTOR = "<$constructor$>";
   private List callSites = new ArrayList();
   private int callSiteArrayVarIndex;
   private HashMap<ClosureExpression, ClassNode> closureClassMap;
   private SourceUnit source;
   private static final String DTT = BytecodeHelper.getClassInternalName(DefaultTypeTransformation.class.getName());
   private boolean specialCallWithinConstructor = false;
   private static String[] sig = new String[255];
   private static final HashSet<String> names = new HashSet();
   private static final HashSet<String> basic = new HashSet();

   public AsmClassGenerator(SourceUnit source, GeneratorContext context, ClassVisitor classVisitor, ClassLoader classLoader, String sourceFile) {
      super(classLoader);
      this.source = source;
      this.context = context;
      this.cv = classVisitor;
      this.sourceFile = sourceFile;
      this.dummyClassWriter = new ClassWriter(1);
      new DummyClassGenerator(context, this.dummyClassWriter, classLoader, sourceFile);
      this.compileStack = new CompileStack();
      this.genericParameterNames = new HashMap();
      this.closureClassMap = new HashMap();
   }

   protected SourceUnit getSourceUnit() {
      return this.source;
   }

   public void visitClass(ClassNode classNode) {
      try {
         this.callSites.clear();
         if (classNode instanceof InterfaceHelperClassNode) {
            InterfaceHelperClassNode ihcn = (InterfaceHelperClassNode)classNode;
            this.callSites.addAll(ihcn.getCallSites());
         }

         this.referencedClasses.clear();
         this.classNode = classNode;
         this.outermostClass = null;
         this.internalClassName = BytecodeHelper.getClassInternalName(classNode);
         this.internalBaseClassName = BytecodeHelper.getClassInternalName(classNode.getSuperClass());
         this.cv.visit(this.getBytecodeVersion(), this.adjustedModifiers(classNode.getModifiers()), this.internalClassName, BytecodeHelper.getGenericsSignature(classNode), this.internalBaseClassName, BytecodeHelper.getClassInternalNames(classNode.getInterfaces()));
         this.cv.visitSource(this.sourceFile, (String)null);
         if (!classNode.getName().endsWith("package-info")) {
            this.visitAnnotations(classNode, this.cv);
            String innerClassName;
            if (classNode.isInterface()) {
               ClassNode owner = classNode;
               if (classNode instanceof InnerClassNode) {
                  owner = classNode.getOuterClass();
               }

               String outerClassName = owner.getName();
               innerClassName = outerClassName + "$" + this.context.getNextInnerClassIdx();
               this.interfaceClassLoadingClass = new InterfaceHelperClassNode(owner, innerClassName, 4128, ClassHelper.OBJECT_TYPE, this.callSites);
               super.visitClass(classNode);
               this.createInterfaceSyntheticStaticFields();
            } else {
               super.visitClass(classNode);
               if (!classNode.declaresInterface(ClassHelper.GENERATED_CLOSURE_Type)) {
                  this.createMopMethods();
               }

               this.generateCallSiteArray();
               this.createSyntheticStaticFields();
            }

            ClassNode innerClass;
            String innerClassInternalName;
            String outerClassName;
            for(Iterator i$ = this.innerClasses.iterator(); i$.hasNext(); this.cv.visitInnerClass(innerClassInternalName, outerClassName, innerClassName, this.adjustedModifiers(innerClass.getModifiers()))) {
               innerClass = (ClassNode)i$.next();
               innerClassName = innerClass.getName();
               innerClassInternalName = BytecodeHelper.getClassInternalName(innerClassName);
               int index = innerClassName.lastIndexOf(36);
               if (index >= 0) {
                  innerClassName = innerClassName.substring(index + 1);
               }

               outerClassName = this.internalClassName;
               MethodNode enclosingMethod = innerClass.getEnclosingMethod();
               if (enclosingMethod != null) {
                  outerClassName = null;
                  innerClassName = null;
               }
            }

            this.cv.visitEnd();
         } else {
            PackageNode packageNode = classNode.getPackage();
            if (packageNode != null) {
               Iterator i$ = packageNode.getAnnotations().iterator();

               while(i$.hasNext()) {
                  AnnotationNode an = (AnnotationNode)i$.next();
                  if (!an.isBuiltIn() && !an.hasSourceRetention()) {
                     groovyjarjarasm.asm.AnnotationVisitor av = this.getAnnotationVisitor(classNode, an, this.cv);
                     this.visitAnnotationAttributes(an, av);
                     av.visitEnd();
                  }
               }
            }

            this.cv.visitEnd();
         }
      } catch (GroovyRuntimeException var8) {
         var8.setModule(classNode.getModule());
         throw var8;
      }
   }

   private int adjustedModifiers(int modifiers) {
      boolean needsSuper = (modifiers & 512) == 0;
      return needsSuper ? modifiers | 32 : modifiers;
   }

   private void generateCallSiteArray() {
      if (!this.classNode.isInterface()) {
         this.cv.visitField(4106, "$callSiteArray", "Ljava/lang/ref/SoftReference;", (String)null, (Object)null);
         this.generateCreateCallSiteArray();
         this.generateGetCallSiteArray();
      }

   }

   private void generateGetCallSiteArray() {
      int visibility = this.classNode instanceof InterfaceHelperClassNode ? 1 : 2;
      MethodVisitor mv = this.cv.visitMethod(visibility + 4096 + 8, "$getCallSiteArray", "()[Lorg/codehaus/groovy/runtime/callsite/CallSite;", (String)null, (String[])null);
      mv.visitCode();
      mv.visitFieldInsn(178, this.internalClassName, "$callSiteArray", "Ljava/lang/ref/SoftReference;");
      Label l0 = new Label();
      mv.visitJumpInsn(198, l0);
      mv.visitFieldInsn(178, this.internalClassName, "$callSiteArray", "Ljava/lang/ref/SoftReference;");
      mv.visitMethodInsn(182, "java/lang/ref/SoftReference", "get", "()Ljava/lang/Object;");
      mv.visitTypeInsn(192, "org/codehaus/groovy/runtime/callsite/CallSiteArray");
      mv.visitInsn(89);
      mv.visitVarInsn(58, 0);
      Label l1 = new Label();
      mv.visitJumpInsn(199, l1);
      mv.visitLabel(l0);
      mv.visitMethodInsn(184, this.internalClassName, "$createCallSiteArray", "()Lorg/codehaus/groovy/runtime/callsite/CallSiteArray;");
      mv.visitVarInsn(58, 0);
      mv.visitTypeInsn(187, "java/lang/ref/SoftReference");
      mv.visitInsn(89);
      mv.visitVarInsn(25, 0);
      mv.visitMethodInsn(183, "java/lang/ref/SoftReference", "<init>", "(Ljava/lang/Object;)V");
      mv.visitFieldInsn(179, this.internalClassName, "$callSiteArray", "Ljava/lang/ref/SoftReference;");
      mv.visitLabel(l1);
      mv.visitVarInsn(25, 0);
      mv.visitFieldInsn(180, "org/codehaus/groovy/runtime/callsite/CallSiteArray", "array", "[Lorg/codehaus/groovy/runtime/callsite/CallSite;");
      mv.visitInsn(176);
      mv.visitMaxs(0, 0);
      mv.visitEnd();
   }

   private void generateCreateCallSiteArray() {
      List<String> callSiteInitMethods = new LinkedList();
      int index = 0;
      int methodIndex = 0;
      int size = this.callSites.size();
      boolean var5 = true;

      while(index < size) {
         ++methodIndex;
         String methodName = "$createCallSiteArray_" + methodIndex;
         callSiteInitMethods.add(methodName);
         MethodVisitor mv = this.cv.visitMethod(4106, methodName, "([Ljava/lang/String;)V", (String)null, (String[])null);
         mv.visitCode();
         int methodLimit = size;
         if (size - index > 5000) {
            methodLimit = index + 5000;
         }

         while(index < methodLimit) {
            mv.visitVarInsn(25, 0);
            mv.visitLdcInsn(index);
            mv.visitLdcInsn(this.callSites.get(index));
            mv.visitInsn(83);
            ++index;
         }

         mv.visitInsn(177);
         mv.visitMaxs(2, 1);
         mv.visitEnd();
      }

      this.mv = this.cv.visitMethod(4106, "$createCallSiteArray", "()Lorg/codehaus/groovy/runtime/callsite/CallSiteArray;", (String)null, (String[])null);
      this.mv.visitCode();
      this.mv.visitLdcInsn(size);
      this.mv.visitTypeInsn(189, "java/lang/String");
      this.mv.visitVarInsn(58, 0);
      Iterator i$ = callSiteInitMethods.iterator();

      while(i$.hasNext()) {
         String methodName = (String)i$.next();
         this.mv.visitVarInsn(25, 0);
         this.mv.visitMethodInsn(184, this.internalClassName, methodName, "([Ljava/lang/String;)V");
      }

      this.mv.visitTypeInsn(187, "org/codehaus/groovy/runtime/callsite/CallSiteArray");
      this.mv.visitInsn(89);
      this.visitClassExpression(new ClassExpression(this.classNode));
      this.mv.visitVarInsn(25, 0);
      this.mv.visitMethodInsn(183, "org/codehaus/groovy/runtime/callsite/CallSiteArray", "<init>", "(Ljava/lang/Class;[Ljava/lang/String;)V");
      this.mv.visitInsn(176);
      this.mv.visitMaxs(0, 0);
      this.mv.visitEnd();
   }

   public void visitGenericType(GenericsType genericsType) {
      ClassNode type = genericsType.getType();
      this.genericParameterNames.put(type.getName(), genericsType);
   }

   private void createMopMethods() {
      this.visitMopMethodList(this.classNode.getMethods(), true);
      this.visitMopMethodList(this.classNode.getSuperClass().getAllDeclaredMethods(), false);
   }

   private String[] buildExceptions(ClassNode[] exceptions) {
      if (exceptions == null) {
         return null;
      } else {
         String[] ret = new String[exceptions.length];

         for(int i = 0; i < exceptions.length; ++i) {
            ret[i] = BytecodeHelper.getClassInternalName(exceptions[i]);
         }

         return ret;
      }
   }

   private void visitMopMethodList(List methods, boolean isThis) {
      HashMap<Key, MethodNode> mops = new HashMap();
      LinkedList<MethodNode> mopCalls = new LinkedList();
      Iterator i$ = methods.iterator();

      while(i$.hasNext()) {
         Object method = i$.next();
         MethodNode mn = (MethodNode)method;
         if ((mn.getModifiers() & 1024) == 0 && !mn.isStatic() && !(isThis ^ (mn.getModifiers() & 5) == 0)) {
            String methodName = mn.getName();

            class Key {
               int hash = 0;
               String name;
               Parameter[] params;

               Key(String name, Parameter[] params) {
                  this.name = name;
                  this.params = params;
                  this.hash = name.hashCode() << 2 + params.length;
               }

               public int hashCode() {
                  return this.hash;
               }

               public boolean equals(Object obj) {
                  Key other = (Key)obj;
                  return other.name.equals(this.name) && AsmClassGenerator.this.equalParameterTypes(other.params, this.params);
               }
            }

            if (isMopMethod(methodName)) {
               mops.put(new Key(methodName, mn.getParameters()), mn);
            } else if (!methodName.startsWith("<")) {
               String name = getMopMethodName(mn, isThis);
               Key key = new Key(name, mn.getParameters());
               if (!mops.containsKey(key)) {
                  mops.put(key, mn);
                  mopCalls.add(mn);
               }
            }
         }
      }

      this.generateMopCalls(mopCalls, isThis);
      mopCalls.clear();
      mops.clear();
   }

   private boolean equalParameterTypes(Parameter[] p1, Parameter[] p2) {
      if (p1.length != p2.length) {
         return false;
      } else {
         for(int i = 0; i < p1.length; ++i) {
            if (!p1[i].getType().equals(p2[i].getType())) {
               return false;
            }
         }

         return true;
      }
   }

   private void generateMopCalls(LinkedList<MethodNode> mopCalls, boolean useThis) {
      Iterator i$ = mopCalls.iterator();

      while(i$.hasNext()) {
         MethodNode method = (MethodNode)i$.next();
         String name = getMopMethodName(method, useThis);
         Parameter[] parameters = method.getParameters();
         String methodDescriptor = BytecodeHelper.getMethodDescriptor(method.getReturnType(), method.getParameters());
         this.mv = this.cv.visitMethod(4097, name, methodDescriptor, (String)null, (String[])null);
         this.mv.visitVarInsn(25, 0);
         int newRegister = 1;
         BytecodeHelper helper = new BytecodeHelper(this.mv);
         Parameter[] arr$ = parameters;
         int len$ = parameters.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Parameter parameter = arr$[i$];
            ClassNode type = parameter.getType();
            helper.load(parameter.getType(), newRegister);
            ++newRegister;
            if (type == ClassHelper.double_TYPE || type == ClassHelper.long_TYPE) {
               ++newRegister;
            }
         }

         this.mv.visitMethodInsn(183, BytecodeHelper.getClassInternalName(method.getDeclaringClass()), method.getName(), methodDescriptor);
         helper.doReturn(method.getReturnType());
         this.mv.visitMaxs(0, 0);
         this.mv.visitEnd();
         this.classNode.addMethod(name, 4097, method.getReturnType(), parameters, (ClassNode[])null, (Statement)null);
      }

   }

   public static String getMopMethodName(MethodNode method, boolean useThis) {
      ClassNode declaringNode = method.getDeclaringClass();

      int distance;
      for(distance = 0; declaringNode != null; declaringNode = declaringNode.getSuperClass()) {
         ++distance;
      }

      return (useThis ? "this" : "super") + "$" + distance + "$" + method.getName();
   }

   public static boolean isMopMethod(String methodName) {
      return (methodName.startsWith("this$") || methodName.startsWith("super$")) && !methodName.contains("$dist$");
   }

   protected void visitConstructorOrMethod(MethodNode node, boolean isConstructor) {
      this.lineNumber = -1;
      Parameter[] parameters = node.getParameters();
      String methodType = BytecodeHelper.getMethodDescriptor(node.getReturnType(), parameters);
      String signature = BytecodeHelper.getGenericsMethodSignature(node);
      int modifiers = node.getModifiers();
      if (this.isVargs(node.getParameters())) {
         modifiers |= 128;
      }

      this.mv = this.cv.visitMethod(modifiers, node.getName(), methodType, signature, this.buildExceptions(node.getExceptions()));
      this.mv = new AsmClassGenerator.MyMethodAdapter();
      this.visitAnnotations(node, this.mv);

      for(int i = 0; i < parameters.length; ++i) {
         this.visitParameterAnnotations(parameters[i], i, this.mv);
      }

      this.helper = new BytecodeHelper(this.mv);
      if (this.classNode.isAnnotationDefinition() && !node.isStaticConstructor()) {
         this.visitAnnotationDefault(node, this.mv);
      } else if (!node.isAbstract()) {
         Statement code = node.getCode();
         if (code instanceof BytecodeSequence && ((BytecodeSequence)code).getInstructions().size() == 1 && ((BytecodeSequence)code).getInstructions().get(0) instanceof BytecodeInstruction) {
            ((BytecodeInstruction)((BytecodeSequence)code).getInstructions().get(0)).visit(this.mv);
         } else {
            this.visitStdMethod(node, isConstructor, parameters, code);
         }

         this.mv.visitInsn(0);
         this.mv.visitMaxs(0, 0);
      }

      this.mv.visitEnd();
   }

   private void visitStdMethod(MethodNode node, boolean isConstructor, Parameter[] parameters, Statement code) {
      if (isConstructor && (code == null || !((ConstructorNode)node).firstStatementIsSpecialConstructorCall())) {
         this.mv.visitVarInsn(25, 0);
         this.mv.visitMethodInsn(183, BytecodeHelper.getClassInternalName(this.classNode.getSuperClass()), "<init>", "()V");
      }

      this.compileStack.init(node.getVariableScope(), parameters, this.mv, this.classNode);
      if (this.isNotClinit()) {
         this.mv.visitMethodInsn(184, this.internalClassName, "$getCallSiteArray", "()[Lorg/codehaus/groovy/runtime/callsite/CallSite;");
         this.callSiteArrayVarIndex = this.compileStack.defineTemporaryVariable("$local$callSiteArray", ClassHelper.make(CallSite[].class), true);
      }

      super.visitConstructorOrMethod(node, isConstructor);
      if (!this.outputReturn || node.isVoidMethod()) {
         this.mv.visitInsn(177);
      }

      this.compileStack.clear();
   }

   void visitAnnotationDefaultExpression(groovyjarjarasm.asm.AnnotationVisitor av, ClassNode type, Expression exp) {
      if (type.isArray()) {
         ListExpression list = (ListExpression)exp;
         groovyjarjarasm.asm.AnnotationVisitor avl = av.visitArray((String)null);
         ClassNode componentType = type.getComponentType();
         Iterator i$ = list.getExpressions().iterator();

         while(i$.hasNext()) {
            Expression lExp = (Expression)i$.next();
            this.visitAnnotationDefaultExpression(avl, componentType, lExp);
         }
      } else if (!ClassHelper.isPrimitiveType(type) && !type.equals(ClassHelper.STRING_TYPE)) {
         if (ClassHelper.CLASS_Type.equals(type)) {
            ClassNode clazz = exp.getType();
            Type t = Type.getType(BytecodeHelper.getTypeDescription(clazz));
            av.visit((String)null, t);
         } else if (type.isDerivedFrom(ClassHelper.Enum_Type)) {
            PropertyExpression pExp = (PropertyExpression)exp;
            ClassExpression cExp = (ClassExpression)pExp.getObjectExpression();
            String desc = BytecodeHelper.getTypeDescription(cExp.getType());
            String name = pExp.getPropertyAsString();
            av.visitEnum((String)null, desc, name);
         } else {
            if (!type.implementsInterface(ClassHelper.Annotation_TYPE)) {
               throw new GroovyBugError("unexpected annotation type " + type.getName());
            }

            AnnotationConstantExpression avExp = (AnnotationConstantExpression)exp;
            AnnotationNode value = (AnnotationNode)avExp.getValue();
            groovyjarjarasm.asm.AnnotationVisitor avc = av.visitAnnotation((String)null, BytecodeHelper.getTypeDescription(avExp.getType()));
            this.visitAnnotationAttributes(value, avc);
         }
      } else {
         ConstantExpression constExp = (ConstantExpression)exp;
         av.visit((String)null, constExp.getValue());
      }

      av.visitEnd();
   }

   private void visitAnnotationDefault(MethodNode node, MethodVisitor mv) {
      if (node.hasAnnotationDefault()) {
         Expression exp = ((ReturnStatement)node.getCode()).getExpression();
         groovyjarjarasm.asm.AnnotationVisitor av = mv.visitAnnotationDefault();
         this.visitAnnotationDefaultExpression(av, node.getReturnType(), exp);
      }
   }

   private boolean isNotClinit() {
      return this.methodNode == null || !this.methodNode.getName().equals("<clinit>");
   }

   private boolean isVargs(Parameter[] p) {
      if (p.length == 0) {
         return false;
      } else {
         ClassNode clazz = p[p.length - 1].getType();
         return clazz.isArray();
      }
   }

   public void visitConstructor(ConstructorNode node) {
      this.constructorNode = node;
      this.methodNode = null;
      this.outputReturn = false;
      super.visitConstructor(node);
   }

   public void visitMethod(MethodNode node) {
      this.constructorNode = null;
      this.methodNode = node;
      this.outputReturn = false;
      super.visitMethod(node);
   }

   public void visitField(FieldNode fieldNode) {
      this.onLineNumber(fieldNode, "visitField: " + fieldNode.getName());
      ClassNode t = fieldNode.getType();
      String signature = BytecodeHelper.getGenericsBounds(t);
      FieldVisitor fv = this.cv.visitField(fieldNode.getModifiers(), fieldNode.getName(), BytecodeHelper.getTypeDescription(t), signature, (Object)null);
      this.visitAnnotations(fieldNode, fv);
      fv.visitEnd();
   }

   public void visitProperty(PropertyNode statement) {
      this.onLineNumber(statement, "visitProperty:" + statement.getField().getName());
      this.methodNode = null;
   }

   protected void visitStatement(Statement statement) {
      String name = statement.getStatementLabel();
      if (name != null) {
         Label label = this.compileStack.createLocalLabel(name);
         this.mv.visitLabel(label);
      }

   }

   public void visitBlockStatement(BlockStatement block) {
      this.visitStatement(block);
      this.compileStack.pushVariableScope(block.getVariableScope());
      super.visitBlockStatement(block);
      this.compileStack.pop();
   }

   private void visitExpressionOrStatement(Object o) {
      if (o != EmptyExpression.INSTANCE) {
         if (o instanceof Expression) {
            Expression expr = (Expression)o;
            this.visitAndAutoboxBoolean(expr);
            if (this.isPopRequired(expr)) {
               this.mv.visitInsn(87);
            }
         } else {
            ((Statement)o).visit(this);
         }

      }
   }

   private void visitForLoopWithClosureList(ForStatement loop) {
      this.compileStack.pushLoop(loop.getVariableScope(), loop.getStatementLabel());
      ClosureListExpression clExpr = (ClosureListExpression)loop.getCollectionExpression();
      this.compileStack.pushVariableScope(clExpr.getVariableScope());
      List expressions = clExpr.getExpressions();
      int size = expressions.size();
      int condIndex = (size - 1) / 2;

      for(int i = 0; i < condIndex; ++i) {
         this.visitExpressionOrStatement(expressions.get(i));
      }

      Label continueLabel = this.compileStack.getContinueLabel();
      Label breakLabel = this.compileStack.getBreakLabel();
      Label cond = new Label();
      this.mv.visitLabel(cond);
      Expression condExpr = (Expression)expressions.get(condIndex);
      if (condExpr == EmptyExpression.INSTANCE) {
         this.mv.visitIntInsn(16, 1);
      } else if (this.isComparisonExpression(condExpr)) {
         condExpr.visit(this);
      } else {
         this.visitAndAutoboxBoolean(condExpr);
         this.helper.unbox(ClassHelper.boolean_TYPE);
      }

      this.mv.visitJumpInsn(153, breakLabel);
      loop.getLoopBlock().visit(this);
      this.mv.visitLabel(continueLabel);

      for(int i = condIndex + 1; i < size; ++i) {
         this.visitExpressionOrStatement(expressions.get(i));
      }

      this.mv.visitJumpInsn(167, cond);
      this.mv.visitLabel(breakLabel);
      this.compileStack.pop();
      this.compileStack.pop();
   }

   public void visitForLoop(ForStatement loop) {
      this.onLineNumber(loop, "visitForLoop");
      this.visitStatement(loop);
      Parameter loopVar = loop.getVariable();
      if (loopVar == ForStatement.FOR_LOOP_DUMMY) {
         this.visitForLoopWithClosureList(loop);
      } else {
         this.compileStack.pushLoop(loop.getVariableScope(), loop.getStatementLabel());
         Variable variable = this.compileStack.defineVariable(loop.getVariable(), false);
         MethodCallExpression iterator = new MethodCallExpression(loop.getCollectionExpression(), "iterator", new ArgumentListExpression());
         iterator.visit(this);
         int iteratorIdx = this.compileStack.defineTemporaryVariable("iterator", ClassHelper.make(Iterator.class), true);
         Label continueLabel = this.compileStack.getContinueLabel();
         Label breakLabel = this.compileStack.getBreakLabel();
         this.mv.visitLabel(continueLabel);
         this.mv.visitVarInsn(25, iteratorIdx);
         iteratorHasNextMethod.call(this.mv);
         this.mv.visitJumpInsn(153, breakLabel);
         this.mv.visitVarInsn(25, iteratorIdx);
         iteratorNextMethod.call(this.mv);
         this.helper.storeVar(variable);
         loop.getLoopBlock().visit(this);
         this.mv.visitJumpInsn(167, continueLabel);
         this.mv.visitLabel(breakLabel);
         this.compileStack.pop();
      }
   }

   public void visitWhileLoop(WhileStatement loop) {
      this.onLineNumber(loop, "visitWhileLoop");
      this.visitStatement(loop);
      this.compileStack.pushLoop(loop.getStatementLabel());
      Label continueLabel = this.compileStack.getContinueLabel();
      Label breakLabel = this.compileStack.getBreakLabel();
      this.mv.visitLabel(continueLabel);
      Expression bool = loop.getBooleanExpression();
      boolean boolHandled = false;
      if (bool instanceof ConstantExpression) {
         ConstantExpression constant = (ConstantExpression)bool;
         if (constant.getValue() == Boolean.TRUE) {
            boolHandled = true;
         } else if (constant.getValue() == Boolean.FALSE) {
            boolHandled = true;
            this.mv.visitJumpInsn(167, breakLabel);
         }
      }

      if (!boolHandled) {
         bool.visit(this);
         this.mv.visitJumpInsn(153, breakLabel);
      }

      loop.getLoopBlock().visit(this);
      this.mv.visitJumpInsn(167, continueLabel);
      this.mv.visitLabel(breakLabel);
      this.compileStack.pop();
   }

   public void visitDoWhileLoop(DoWhileStatement loop) {
      this.onLineNumber(loop, "visitDoWhileLoop");
      this.visitStatement(loop);
      this.compileStack.pushLoop(loop.getStatementLabel());
      Label breakLabel = this.compileStack.getBreakLabel();
      Label continueLabel = this.compileStack.getContinueLabel();
      this.mv.visitLabel(continueLabel);
      loop.getLoopBlock().visit(this);
      loop.getBooleanExpression().visit(this);
      this.mv.visitJumpInsn(153, continueLabel);
      this.mv.visitLabel(breakLabel);
      this.compileStack.pop();
   }

   public void visitIfElse(IfStatement ifElse) {
      this.onLineNumber(ifElse, "visitIfElse");
      this.visitStatement(ifElse);
      ifElse.getBooleanExpression().visit(this);
      Label l0 = new Label();
      this.mv.visitJumpInsn(153, l0);
      this.compileStack.pushBooleanExpression();
      ifElse.getIfBlock().visit(this);
      this.compileStack.pop();
      Label l1 = new Label();
      this.mv.visitJumpInsn(167, l1);
      this.mv.visitLabel(l0);
      this.compileStack.pushBooleanExpression();
      ifElse.getElseBlock().visit(this);
      this.compileStack.pop();
      this.mv.visitLabel(l1);
   }

   public void visitTernaryExpression(TernaryExpression expression) {
      this.onLineNumber(expression, "visitTernaryExpression");
      BooleanExpression boolPart = expression.getBooleanExpression();
      Expression truePart = expression.getTrueExpression();
      final Expression falsePart = expression.getFalseExpression();
      if (expression instanceof ElvisOperatorExpression) {
         this.visitAndAutoboxBoolean(expression.getTrueExpression());
         boolPart = new BooleanExpression(new BytecodeExpression() {
            public void visit(MethodVisitor mv) {
               mv.visitInsn(89);
            }
         });
         truePart = BytecodeExpression.NOP;
         falsePart = new BytecodeExpression() {
            public void visit(MethodVisitor mv) {
               mv.visitInsn(87);
               AsmClassGenerator.this.visitAndAutoboxBoolean((Expression)falsePart);
            }
         };
      }

      boolPart.visit(this);
      Label l0 = new Label();
      this.mv.visitJumpInsn(153, l0);
      this.compileStack.pushBooleanExpression();
      this.visitAndAutoboxBoolean((Expression)truePart);
      this.compileStack.pop();
      Label l1 = new Label();
      this.mv.visitJumpInsn(167, l1);
      this.mv.visitLabel(l0);
      this.compileStack.pushBooleanExpression();
      this.visitAndAutoboxBoolean((Expression)falsePart);
      this.compileStack.pop();
      this.mv.visitLabel(l1);
   }

   public void visitAssertStatement(AssertStatement statement) {
      this.onLineNumber(statement, "visitAssertStatement");
      this.visitStatement(statement);
      boolean rewriteAssert = true;
      rewriteAssert = statement.getMessageExpression() == ConstantExpression.NULL;
      AsmClassGenerator.AssertionTracker oldTracker = this.assertionTracker;
      Janitor janitor = new Janitor();
      Label tryStart = new Label();
      if (rewriteAssert) {
         this.assertionTracker = new AsmClassGenerator.AssertionTracker();

         try {
            this.assertionTracker.sourceText = new SourceText(statement, this.source, janitor);
            this.mv.visitTypeInsn(187, "org/codehaus/groovy/transform/powerassert/ValueRecorder");
            this.mv.visitInsn(89);
            this.mv.visitMethodInsn(183, "org/codehaus/groovy/transform/powerassert/ValueRecorder", "<init>", "()V");
            this.assertionTracker.recorderIndex = this.compileStack.defineTemporaryVariable("recorder", true);
            this.mv.visitLabel(tryStart);
         } catch (SourceTextNotAvailableException var12) {
            rewriteAssert = false;
            this.assertionTracker = oldTracker;
         }
      }

      BooleanExpression booleanExpression = statement.getBooleanExpression();
      booleanExpression.visit(this);
      Label exceptionThrower = new Label();
      this.mv.visitJumpInsn(153, exceptionThrower);
      if (rewriteAssert) {
         this.mv.visitVarInsn(25, this.assertionTracker.recorderIndex);
         this.mv.visitMethodInsn(182, "org/codehaus/groovy/transform/powerassert/ValueRecorder", "clear", "()V");
      }

      Label afterAssert = new Label();
      this.mv.visitJumpInsn(167, afterAssert);
      this.mv.visitLabel(exceptionThrower);
      if (rewriteAssert) {
         this.mv.visitLdcInsn(this.assertionTracker.sourceText.getNormalizedText());
         this.mv.visitVarInsn(25, this.assertionTracker.recorderIndex);
         this.mv.visitMethodInsn(184, "org/codehaus/groovy/transform/powerassert/AssertionRenderer", "render", "(Ljava/lang/String;Lorg/codehaus/groovy/transform/powerassert/ValueRecorder;)Ljava/lang/String;");
      } else {
         this.writeSourclessAssertText(statement);
      }

      AsmClassGenerator.AssertionTracker savedTracker = this.assertionTracker;
      this.assertionTracker = null;
      this.visitAndAutoboxBoolean(statement.getMessageExpression());
      assertFailedMethod.call(this.mv);
      if (rewriteAssert) {
         Label tryEnd = new Label();
         this.mv.visitLabel(tryEnd);
         this.mv.visitJumpInsn(167, afterAssert);
         Label catchAny = new Label();
         this.mv.visitLabel(catchAny);
         this.mv.visitVarInsn(25, savedTracker.recorderIndex);
         this.mv.visitMethodInsn(182, "org/codehaus/groovy/transform/powerassert/ValueRecorder", "clear", "()V");
         this.mv.visitInsn(191);
         this.compileStack.addExceptionBlock(tryStart, tryEnd, catchAny, (String)null);
      }

      this.mv.visitLabel(afterAssert);
      this.assertionTracker = oldTracker;
      janitor.cleanup();
   }

   private void writeSourclessAssertText(AssertStatement statement) {
      BooleanExpression booleanExpression = statement.getBooleanExpression();
      String expressionText = booleanExpression.getText();
      List<String> list = new ArrayList();
      this.addVariableNames(booleanExpression, list);
      if (list.isEmpty()) {
         this.mv.visitLdcInsn(expressionText);
      } else {
         boolean first = true;
         this.mv.visitTypeInsn(187, "java/lang/StringBuffer");
         this.mv.visitInsn(89);
         this.mv.visitLdcInsn(expressionText + ". Values: ");
         this.mv.visitMethodInsn(183, "java/lang/StringBuffer", "<init>", "(Ljava/lang/String;)V");
         int tempIndex = this.compileStack.defineTemporaryVariable("assert", true);
         Iterator i$ = list.iterator();

         while(i$.hasNext()) {
            String name = (String)i$.next();
            String text = name + " = ";
            if (first) {
               first = false;
            } else {
               text = ", " + text;
            }

            this.mv.visitVarInsn(25, tempIndex);
            this.mv.visitLdcInsn(text);
            this.mv.visitMethodInsn(182, "java/lang/StringBuffer", "append", "(Ljava/lang/Object;)Ljava/lang/StringBuffer;");
            this.mv.visitInsn(87);
            this.mv.visitVarInsn(25, tempIndex);
            (new VariableExpression(name)).visit(this);
            this.mv.visitMethodInsn(184, "org/codehaus/groovy/runtime/InvokerHelper", "toString", "(Ljava/lang/Object;)Ljava/lang/String;");
            this.mv.visitMethodInsn(182, "java/lang/StringBuffer", "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;");
            this.mv.visitInsn(87);
         }

         this.mv.visitVarInsn(25, tempIndex);
         this.compileStack.removeVar(tempIndex);
      }

   }

   private void addVariableNames(Expression expression, List<String> list) {
      if (expression instanceof BooleanExpression) {
         BooleanExpression boolExp = (BooleanExpression)expression;
         this.addVariableNames(boolExp.getExpression(), list);
      } else if (expression instanceof BinaryExpression) {
         BinaryExpression binExp = (BinaryExpression)expression;
         this.addVariableNames(binExp.getLeftExpression(), list);
         this.addVariableNames(binExp.getRightExpression(), list);
      } else if (expression instanceof VariableExpression) {
         VariableExpression varExp = (VariableExpression)expression;
         list.add(varExp.getName());
      }

   }

   public void visitTryCatchFinally(TryCatchStatement statement) {
      this.visitStatement(statement);
      Statement tryStatement = statement.getTryStatement();
      Statement finallyStatement = statement.getFinallyStatement();
      Label tryStart = new Label();
      this.mv.visitLabel(tryStart);
      CompileStack.BlockRecorder tryBlock = this.makeBlockRecorder(finallyStatement);
      tryBlock.startRange(tryStart);
      tryStatement.visit(this);
      Label finallyStart = new Label();
      this.mv.visitJumpInsn(167, finallyStart);
      Label tryEnd = new Label();
      this.mv.visitLabel(tryEnd);
      tryBlock.closeRange(tryEnd);
      this.compileStack.pop();
      CompileStack.BlockRecorder catches = this.makeBlockRecorder(finallyStatement);
      Iterator i$ = statement.getCatchStatements().iterator();

      while(i$.hasNext()) {
         CatchStatement catchStatement = (CatchStatement)i$.next();
         ClassNode exceptionType = catchStatement.getExceptionType();
         String exceptionTypeInternalName = BytecodeHelper.getClassInternalName(exceptionType);
         Label catchStart = new Label();
         this.mv.visitLabel(catchStart);
         catches.startRange(catchStart);
         this.compileStack.pushState();
         this.compileStack.defineVariable(catchStatement.getVariable(), true);
         catchStatement.visit(this);
         this.mv.visitInsn(0);
         this.compileStack.pop();
         Label catchEnd = new Label();
         this.mv.visitLabel(catchEnd);
         catches.closeRange(catchEnd);
         this.mv.visitJumpInsn(167, finallyStart);
         this.compileStack.writeExceptionTable(tryBlock, catchStart, exceptionTypeInternalName);
      }

      Label catchAny = new Label();
      this.compileStack.writeExceptionTable(tryBlock, catchAny, (String)null);
      this.compileStack.writeExceptionTable(catches, catchAny, (String)null);
      this.compileStack.pop();
      this.mv.visitLabel(finallyStart);
      finallyStatement.visit(this);
      this.mv.visitInsn(0);
      Label skipCatchAll = new Label();
      this.mv.visitJumpInsn(167, skipCatchAll);
      this.mv.visitLabel(catchAny);
      int anyExceptionIndex = this.compileStack.defineTemporaryVariable("exception", true);
      finallyStatement.visit(this);
      this.mv.visitVarInsn(25, anyExceptionIndex);
      this.mv.visitInsn(191);
      this.mv.visitLabel(skipCatchAll);
   }

   private CompileStack.BlockRecorder makeBlockRecorder(final Statement finallyStatement) {
      final CompileStack.BlockRecorder block = new CompileStack.BlockRecorder();
      Runnable tryRunner = new Runnable() {
         public void run() {
            AsmClassGenerator.this.compileStack.pushBlockRecorderVisit(block);
            finallyStatement.visit(AsmClassGenerator.this);
            AsmClassGenerator.this.compileStack.popBlockRecorderVisit(block);
         }
      };
      block.excludedStatement = tryRunner;
      this.compileStack.pushBlockRecorder(block);
      return block;
   }

   public void visitSwitch(SwitchStatement statement) {
      this.onLineNumber(statement, "visitSwitch");
      this.visitStatement(statement);
      statement.getExpression().visit(this);
      Label breakLabel = this.compileStack.pushSwitch();
      int switchVariableIndex = this.compileStack.defineTemporaryVariable("switch", true);
      List caseStatements = statement.getCaseStatements();
      int caseCount = caseStatements.size();
      Label[] labels = new Label[caseCount + 1];

      int i;
      for(i = 0; i < caseCount; ++i) {
         labels[i] = new Label();
      }

      i = 0;

      for(Iterator iter = caseStatements.iterator(); iter.hasNext(); ++i) {
         CaseStatement caseStatement = (CaseStatement)iter.next();
         this.visitCaseStatement(caseStatement, switchVariableIndex, labels[i], labels[i + 1]);
      }

      statement.getDefaultStatement().visit(this);
      this.mv.visitLabel(breakLabel);
      this.compileStack.pop();
   }

   public void visitCaseStatement(CaseStatement statement) {
   }

   public void visitCaseStatement(CaseStatement statement, int switchVariableIndex, Label thisLabel, Label nextLabel) {
      this.onLineNumber(statement, "visitCaseStatement");
      this.mv.visitVarInsn(25, switchVariableIndex);
      statement.getExpression().visit(this);
      isCaseMethod.call(this.mv);
      Label l0 = new Label();
      this.mv.visitJumpInsn(153, l0);
      this.mv.visitLabel(thisLabel);
      statement.getCode().visit(this);
      if (nextLabel != null) {
         this.mv.visitJumpInsn(167, nextLabel);
      }

      this.mv.visitLabel(l0);
   }

   public void visitBreakStatement(BreakStatement statement) {
      this.onLineNumber(statement, "visitBreakStatement");
      this.visitStatement(statement);
      String name = statement.getLabel();
      Label breakLabel = this.compileStack.getNamedBreakLabel(name);
      this.compileStack.applyFinallyBlocks(breakLabel, true);
      this.mv.visitJumpInsn(167, breakLabel);
   }

   public void visitContinueStatement(ContinueStatement statement) {
      this.onLineNumber(statement, "visitContinueStatement");
      this.visitStatement(statement);
      String name = statement.getLabel();
      Label continueLabel = this.compileStack.getContinueLabel();
      if (name != null) {
         continueLabel = this.compileStack.getNamedContinueLabel(name);
      }

      this.compileStack.applyFinallyBlocks(continueLabel, false);
      this.mv.visitJumpInsn(167, continueLabel);
   }

   public void visitSynchronizedStatement(SynchronizedStatement statement) {
      this.onLineNumber(statement, "visitSynchronizedStatement");
      this.visitStatement(statement);
      statement.getExpression().visit(this);
      final int index = this.compileStack.defineTemporaryVariable("synchronized", ClassHelper.Integer_TYPE, true);
      Label synchronizedStart = new Label();
      Label synchronizedEnd = new Label();
      Label catchAll = new Label();
      this.mv.visitVarInsn(25, index);
      this.mv.visitInsn(194);
      this.mv.visitLabel(synchronizedStart);
      this.mv.visitInsn(0);
      Runnable finallyPart = new Runnable() {
         public void run() {
            AsmClassGenerator.this.mv.visitVarInsn(25, index);
            AsmClassGenerator.this.mv.visitInsn(195);
         }
      };
      CompileStack.BlockRecorder fb = new CompileStack.BlockRecorder(finallyPart);
      fb.startRange(synchronizedStart);
      this.compileStack.pushBlockRecorder(fb);
      statement.getCode().visit(this);
      fb.closeRange(catchAll);
      this.compileStack.writeExceptionTable(fb, catchAll, (String)null);
      this.compileStack.pop();
      finallyPart.run();
      this.mv.visitJumpInsn(167, synchronizedEnd);
      this.mv.visitLabel(catchAll);
      finallyPart.run();
      this.mv.visitInsn(191);
      this.mv.visitLabel(synchronizedEnd);
   }

   public void visitThrowStatement(ThrowStatement statement) {
      this.onLineNumber(statement, "visitThrowStatement");
      this.visitStatement(statement);
      statement.getExpression().visit(this);
      this.mv.visitTypeInsn(192, "java/lang/Throwable");
      this.mv.visitInsn(191);
   }

   public void visitReturnStatement(ReturnStatement statement) {
      this.onLineNumber(statement, "visitReturnStatement");
      this.visitStatement(statement);
      ClassNode returnType;
      if (this.methodNode != null) {
         returnType = this.methodNode.getReturnType();
      } else {
         if (this.constructorNode == null) {
            throw new GroovyBugError("I spotted a return that is neither in a method nor in a constructor... I can not handle that");
         }

         returnType = this.constructorNode.getReturnType();
      }

      if (returnType == ClassHelper.VOID_TYPE) {
         if (!statement.isReturningNullOrVoid()) {
            this.throwException("Cannot use return statement with an expression on a method that returns void");
         }

         this.compileStack.applyBlockRecorder();
         this.mv.visitInsn(177);
         this.outputReturn = true;
      } else {
         Expression expression = statement.getExpression();
         this.evaluateExpression(expression);
         if (returnType == ClassHelper.OBJECT_TYPE && expression.getType() != null && expression.getType() == ClassHelper.VOID_TYPE) {
            this.mv.visitInsn(1);
         } else {
            this.doConvertAndCast(returnType, expression, false, true, false);
         }

         if (this.compileStack.hasBlockRecorder()) {
            int returnValueIdx = this.compileStack.defineTemporaryVariable("returnValue", ClassHelper.OBJECT_TYPE, true);
            this.compileStack.applyBlockRecorder();
            this.helper.load(ClassHelper.OBJECT_TYPE, returnValueIdx);
         }

         this.helper.unbox(returnType);
         this.helper.doReturn(returnType);
         this.outputReturn = true;
      }
   }

   protected void doConvertAndCast(ClassNode type, Expression expression, boolean ignoreAutoboxing, boolean forceCast, boolean coerce) {
      ClassNode expType = this.getExpressionType(expression);
      if (!ignoreAutoboxing && ClassHelper.isPrimitiveType(type)) {
         type = ClassHelper.getWrapper(type);
      }

      if (forceCast || type != null && !expType.isDerivedFrom(type) && !expType.implementsInterface(type)) {
         this.doConvertAndCast(type, coerce);
      }

   }

   protected void evaluateExpression(Expression expression) {
      this.visitAndAutoboxBoolean(expression);
      if (!this.isPopRequired(expression)) {
         Expression assignExpr = this.createReturnLHSExpression(expression);
         if (assignExpr != null) {
            this.leftHandExpression = false;
            assignExpr.visit(this);
         }

      }
   }

   public void visitExpressionStatement(ExpressionStatement statement) {
      this.onLineNumber(statement, "visitExpressionStatement: " + statement.getExpression().getClass().getName());
      this.visitStatement(statement);
      Expression expression = statement.getExpression();
      this.visitAndAutoboxBoolean(expression);
      if (this.isPopRequired(expression)) {
         this.mv.visitInsn(87);
      }

   }

   public void visitDeclarationExpression(DeclarationExpression expression) {
      this.onLineNumber(expression, "visitDeclarationExpression: \"" + expression.getText() + "\"");
      this.evaluateEqual(expression, true);
   }

   public void visitBinaryExpression(BinaryExpression expression) {
      this.onLineNumber(expression, "visitBinaryExpression: \"" + expression.getOperation().getText() + "\" ");
      switch(expression.getOperation().getType()) {
      case 30:
         if (this.leftHandExpression) {
            this.throwException("Should not be called here. Possible reason: postfix operation on array.");
         } else {
            this.evaluateBinaryExpression("getAt", expression);
         }
         break;
      case 90:
         this.evaluateBinaryExpression(findRegexMethod, expression);
         break;
      case 94:
         this.evaluateBinaryExpression(matchRegexMethod, expression);
         break;
      case 100:
         this.evaluateEqual(expression, false);
         break;
      case 120:
         this.evaluateBinaryExpression(compareNotEqualMethod, expression);
         break;
      case 121:
      case 122:
         this.source.addError(new SyntaxException("Operators === and !== are not supported. Use this.is(that) instead", expression.getLineNumber(), expression.getColumnNumber()));
         break;
      case 123:
         this.evaluateBinaryExpression(compareEqualMethod, expression);
         break;
      case 124:
         this.evaluateBinaryExpression(compareLessThanMethod, expression);
         break;
      case 125:
         this.evaluateBinaryExpression(compareLessThanEqualMethod, expression);
         break;
      case 126:
         this.evaluateBinaryExpression(compareGreaterThanMethod, expression);
         break;
      case 127:
         this.evaluateBinaryExpression(compareGreaterThanEqualMethod, expression);
         break;
      case 128:
         this.evaluateCompareTo(expression);
         break;
      case 162:
         this.evaluateLogicalOrExpression(expression);
         break;
      case 164:
         this.evaluateLogicalAndExpression(expression);
         break;
      case 200:
         this.evaluateBinaryExpression("plus", expression);
         break;
      case 201:
         this.evaluateBinaryExpression("minus", expression);
         break;
      case 202:
         this.evaluateBinaryExpression("multiply", expression);
         break;
      case 203:
         this.evaluateBinaryExpression("div", expression);
         break;
      case 204:
         this.evaluateBinaryExpression("intdiv", expression);
         break;
      case 205:
         this.evaluateBinaryExpression("mod", expression);
         break;
      case 206:
         this.evaluateBinaryExpression("power", expression);
         break;
      case 210:
         this.evaluateBinaryExpressionWithAssignment("plus", expression);
         break;
      case 211:
         this.evaluateBinaryExpressionWithAssignment("minus", expression);
         break;
      case 212:
         this.evaluateBinaryExpressionWithAssignment("multiply", expression);
         break;
      case 213:
         this.evaluateBinaryExpressionWithAssignment("div", expression);
         break;
      case 214:
         this.evaluateBinaryExpressionWithAssignment("intdiv", expression);
         break;
      case 215:
         this.evaluateBinaryExpressionWithAssignment("mod", expression);
         break;
      case 216:
         this.evaluateBinaryExpressionWithAssignment("power", expression);
         break;
      case 280:
         this.evaluateBinaryExpression("leftShift", expression);
         break;
      case 281:
         this.evaluateBinaryExpression("rightShift", expression);
         break;
      case 282:
         this.evaluateBinaryExpression("rightShiftUnsigned", expression);
         break;
      case 285:
         this.evaluateBinaryExpressionWithAssignment("leftShift", expression);
         break;
      case 286:
         this.evaluateBinaryExpressionWithAssignment("rightShift", expression);
         break;
      case 287:
         this.evaluateBinaryExpressionWithAssignment("rightShiftUnsigned", expression);
         break;
      case 340:
         this.evaluateBinaryExpression("or", expression);
         break;
      case 341:
         this.evaluateBinaryExpression("and", expression);
         break;
      case 342:
         this.evaluateBinaryExpression("xor", expression);
         break;
      case 350:
         this.evaluateBinaryExpressionWithAssignment("or", expression);
         break;
      case 351:
         this.evaluateBinaryExpressionWithAssignment("and", expression);
         break;
      case 352:
         this.evaluateBinaryExpressionWithAssignment("xor", expression);
         break;
      case 544:
         this.evaluateInstanceof(expression);
         break;
      case 573:
         this.evaluateBinaryExpression(isCaseMethod, expression);
         break;
      default:
         this.throwException("Operation: " + expression.getOperation() + " not supported");
      }

      this.record(expression.getOperation(), this.isComparisonExpression(expression));
   }

   private void load(Expression exp) {
      boolean wasLeft = this.leftHandExpression;
      this.leftHandExpression = false;
      this.visitAndAutoboxBoolean(exp);
      this.leftHandExpression = wasLeft;
   }

   public void visitPostfixExpression(PostfixExpression expression) {
      switch(expression.getOperation().getType()) {
      case 250:
         this.evaluatePostfixMethod("next", expression.getExpression());
         break;
      case 260:
         this.evaluatePostfixMethod("previous", expression.getExpression());
      }

      this.record(expression);
   }

   private void record(Token token, boolean unboxedValue) {
      if (this.assertionTracker != null) {
         if (unboxedValue) {
            this.mv.visitInsn(89);
            this.helper.boxBoolean();
         }

         this.record(this.assertionTracker.sourceText.getNormalizedColumn(token.getStartLine(), token.getStartColumn()));
         if (unboxedValue) {
            this.mv.visitInsn(87);
         }

      }
   }

   private void record(Expression expression) {
      if (this.assertionTracker != null) {
         this.record(this.assertionTracker.sourceText.getNormalizedColumn(expression.getLineNumber(), expression.getColumnNumber()));
      }
   }

   private void recordBool(Expression expression) {
      if (this.assertionTracker != null) {
         this.mv.visitInsn(89);
         this.helper.boxBoolean();
         this.record(expression);
         this.mv.visitInsn(87);
      }
   }

   private void record(int normalizedColumn) {
      if (this.assertionTracker != null) {
         this.mv.visitVarInsn(25, this.assertionTracker.recorderIndex);
         this.helper.swapWithObject(ClassHelper.OBJECT_TYPE);
         this.mv.visitLdcInsn(normalizedColumn);
         this.mv.visitMethodInsn(182, "org/codehaus/groovy/transform/powerassert/ValueRecorder", "record", "(Ljava/lang/Object;I)Ljava/lang/Object;");
      }
   }

   private void throwException(String s) {
      throw new RuntimeParserException(s, this.currentASTNode);
   }

   public void visitPrefixExpression(PrefixExpression expression) {
      switch(expression.getOperation().getType()) {
      case 250:
         this.evaluatePrefixMethod("next", expression.getExpression());
         break;
      case 260:
         this.evaluatePrefixMethod("previous", expression.getExpression());
      }

      this.record(expression);
   }

   public void visitClosureExpression(ClosureExpression expression) {
      ClassNode innerClass = (ClassNode)this.closureClassMap.get(expression);
      if (innerClass == null) {
         innerClass = this.createClosureClass(expression);
         this.closureClassMap.put(expression, innerClass);
         this.addInnerClass(innerClass);
         innerClass.addInterface(ClassHelper.GENERATED_CLOSURE_Type);
      }

      String innerClassinternalName = BytecodeHelper.getClassInternalName(innerClass);
      this.passingParams = true;
      List constructors = innerClass.getDeclaredConstructors();
      ConstructorNode node = (ConstructorNode)constructors.get(0);
      Parameter[] localVariableParams = node.getParameters();
      this.mv.visitTypeInsn(187, innerClassinternalName);
      this.mv.visitInsn(89);
      if ((this.isStaticMethod() || this.specialCallWithinConstructor) && !this.classNode.declaresInterface(ClassHelper.GENERATED_CLOSURE_Type)) {
         this.visitClassExpression(new ClassExpression(this.classNode));
         this.visitClassExpression(new ClassExpression(this.getOutermostClass()));
      } else {
         this.mv.visitVarInsn(25, 0);
         this.loadThis();
      }

      for(int i = 2; i < localVariableParams.length; ++i) {
         Parameter param = localVariableParams[i];
         String name = param.getName();
         if (!this.compileStack.containsVariable(name) && this.compileStack.getScope().isReferencedClassVariable(name)) {
            this.visitFieldExpression(new FieldExpression(this.classNode.getDeclaredField(name)));
         } else {
            Variable v = this.compileStack.getVariable(name, !this.classNodeUsesReferences());
            if (v == null) {
               FieldNode field = this.classNode.getDeclaredField(name);
               this.mv.visitVarInsn(25, 0);
               this.mv.visitFieldInsn(180, this.internalClassName, name, BytecodeHelper.getTypeDescription(field.getType()));
               param.setClosureSharedVariable(false);
               v = this.compileStack.defineVariable(param, true);
               param.setClosureSharedVariable(true);
               v.setHolder(true);
            }

            this.mv.visitVarInsn(25, v.getIndex());
         }
      }

      this.passingParams = false;
      this.mv.visitMethodInsn(183, innerClassinternalName, "<init>", BytecodeHelper.getMethodDescriptor(ClassHelper.VOID_TYPE, localVariableParams));
   }

   private boolean classNodeUsesReferences() {
      boolean ret = this.classNode.getSuperClass() == ClassHelper.CLOSURE_TYPE;
      if (ret) {
         return ret;
      } else if (this.classNode instanceof InnerClassNode) {
         InnerClassNode inner = (InnerClassNode)this.classNode;
         return inner.isAnonymous();
      } else {
         return false;
      }
   }

   protected void loadThisOrOwner() {
      if (this.isInnerClass()) {
         this.visitFieldExpression(new FieldExpression(this.classNode.getDeclaredField("owner")));
      } else {
         this.loadThis();
      }

   }

   /** @deprecated */
   @Deprecated
   public void visitRegexExpression(RegexExpression expression) {
      expression.getRegex().visit(this);
      regexPattern.call(this.mv);
   }

   public void visitConstantExpression(ConstantExpression expression) {
      String constantName = expression.getConstantName();
      if ((this.methodNode == null || !this.methodNode.getName().equals("<clinit>")) && constantName != null) {
         this.mv.visitFieldInsn(178, this.internalClassName, constantName, BytecodeHelper.getTypeDescription(expression.getType()));
      } else {
         Object value = expression.getValue();
         this.helper.loadConstant(value);
      }

   }

   public void visitSpreadExpression(SpreadExpression expression) {
      throw new GroovyBugError("SpreadExpression should not be visited here");
   }

   public void visitSpreadMapExpression(SpreadMapExpression expression) {
      Expression subExpression = expression.getExpression();
      AsmClassGenerator.AssertionTracker old = this.assertionTracker;
      this.assertionTracker = null;
      subExpression.visit(this);
      spreadMap.call(this.mv);
      this.assertionTracker = old;
   }

   public void visitMethodPointerExpression(MethodPointerExpression expression) {
      Expression subExpression = expression.getExpression();
      subExpression.visit(this);
      this.loadDynamicName(expression.getMethodName());
      getMethodPointer.call(this.mv);
   }

   private void loadDynamicName(Expression name) {
      if (name instanceof ConstantExpression) {
         ConstantExpression ce = (ConstantExpression)name;
         Object value = ce.getValue();
         if (value instanceof String) {
            this.helper.loadConstant(value);
            return;
         }
      }

      (new CastExpression(ClassHelper.STRING_TYPE, name)).visit(this);
   }

   public void visitUnaryMinusExpression(UnaryMinusExpression expression) {
      Expression subExpression = expression.getExpression();
      subExpression.visit(this);
      unaryMinus.call(this.mv);
      this.record(expression);
   }

   public void visitUnaryPlusExpression(UnaryPlusExpression expression) {
      Expression subExpression = expression.getExpression();
      subExpression.visit(this);
      unaryPlus.call(this.mv);
      this.record(expression);
   }

   public void visitBitwiseNegationExpression(BitwiseNegationExpression expression) {
      Expression subExpression = expression.getExpression();
      subExpression.visit(this);
      bitwiseNegate.call(this.mv);
      this.record(expression);
   }

   public void visitCastExpression(CastExpression castExpression) {
      ClassNode type = castExpression.getType();
      this.visitAndAutoboxBoolean(castExpression.getExpression());
      ClassNode rht = this.rightHandType;
      this.rightHandType = castExpression.getExpression().getType();
      this.doConvertAndCast(type, castExpression.getExpression(), castExpression.isIgnoringAutoboxing(), false, castExpression.isCoerce());
      this.rightHandType = rht;
   }

   public void visitNotExpression(NotExpression expression) {
      Expression subExpression = expression.getExpression();
      subExpression.visit(this);
      if (!this.isComparisonExpression(subExpression) && !(subExpression instanceof BooleanExpression)) {
         this.helper.unbox(Boolean.TYPE);
      }

      this.helper.negateBoolean();
      this.recordBool(expression);
   }

   public void visitBooleanExpression(BooleanExpression expression) {
      this.compileStack.pushBooleanExpression();
      expression.getExpression().visit(this);
      if (!this.isComparisonExpression(expression.getExpression())) {
         this.helper.unbox(Boolean.TYPE);
      }

      this.compileStack.pop();
   }

   private void makeInvokeMethodCall(MethodCallExpression call, boolean useSuper, MethodCallerMultiAdapter adapter) {
      Expression objectExpression = call.getObjectExpression();
      if (!this.isStaticMethod() && !this.isStaticContext() && isThisExpression(call.getObjectExpression())) {
         objectExpression = new CastExpression(this.classNode, (Expression)objectExpression);
      }

      Expression messageName = new CastExpression(ClassHelper.STRING_TYPE, call.getMethod());
      if (useSuper) {
         ClassNode superClass = this.isInClosure() ? this.getOutermostClass().getSuperClass() : this.classNode.getSuperClass();
         this.makeCall(new ClassExpression(superClass), (Expression)objectExpression, messageName, call.getArguments(), adapter, call.isSafe(), call.isSpreadSafe(), false);
      } else {
         this.makeCall((Expression)objectExpression, messageName, call.getArguments(), adapter, call.isSafe(), call.isSpreadSafe(), call.isImplicitThis());
      }

   }

   private void makeCall(Expression receiver, Expression message, Expression arguments, MethodCallerMultiAdapter adapter, boolean safe, boolean spreadSafe, boolean implicitThis) {
      ClassNode cn = this.classNode;
      this.makeCall(new ClassExpression(cn), receiver, message, arguments, adapter, safe, spreadSafe, implicitThis);
   }

   private void makeCall(ClassExpression sender, Expression receiver, Expression message, Expression arguments, MethodCallerMultiAdapter adapter, boolean safe, boolean spreadSafe, boolean implicitThis) {
      if ((adapter == invokeMethod || adapter == invokeMethodOnCurrent || adapter == invokeStaticMethod) && !spreadSafe) {
         String methodName = this.getMethodName(message);
         if (methodName != null) {
            this.makeCallSite(receiver, methodName, arguments, safe, implicitThis, adapter == invokeMethodOnCurrent, adapter == invokeStaticMethod);
            return;
         }
      }

      boolean lhs = this.leftHandExpression;
      this.leftHandExpression = false;
      sender.visit(this);
      boolean oldVal = this.implicitThis;
      this.implicitThis = implicitThis;
      this.visitAndAutoboxBoolean(receiver);
      this.implicitThis = oldVal;
      if (message != null) {
         message.visit(this);
      }

      boolean containsSpreadExpression = containsSpreadExpression(arguments);
      int numberOfArguments = containsSpreadExpression ? -1 : argumentSize(arguments);
      if (numberOfArguments <= 0 && !containsSpreadExpression) {
         if (numberOfArguments > 0) {
            TupleExpression te = (TupleExpression)arguments;

            for(int i = 0; i < numberOfArguments; ++i) {
               Expression argument = te.getExpression(i);
               this.visitAndAutoboxBoolean(argument);
               if (argument instanceof CastExpression) {
                  this.loadWrapper(argument);
               }
            }
         }
      } else {
         ArgumentListExpression ae;
         if (arguments instanceof ArgumentListExpression) {
            ae = (ArgumentListExpression)arguments;
         } else if (arguments instanceof TupleExpression) {
            TupleExpression te = (TupleExpression)arguments;
            ae = new ArgumentListExpression(te.getExpressions());
         } else {
            ae = new ArgumentListExpression();
            ae.addExpression(arguments);
         }

         if (containsSpreadExpression) {
            this.despreadList(ae.getExpressions(), true);
         } else {
            ae.visit(this);
         }
      }

      adapter.call(this.mv, numberOfArguments, safe, spreadSafe);
      this.leftHandExpression = lhs;
   }

   private void makeGetPropertySite(Expression receiver, String methodName, boolean safe, boolean implicitThis) {
      if (this.isNotClinit()) {
         this.mv.visitVarInsn(25, this.callSiteArrayVarIndex);
      } else {
         this.mv.visitMethodInsn(184, this.getClassName(), "$getCallSiteArray", "()[Lorg/codehaus/groovy/runtime/callsite/CallSite;");
      }

      int index = this.allocateIndex(methodName);
      this.mv.visitLdcInsn(index);
      this.mv.visitInsn(50);
      boolean lhs = this.leftHandExpression;
      this.leftHandExpression = false;
      boolean oldVal = this.implicitThis;
      this.implicitThis = implicitThis;
      this.visitAndAutoboxBoolean(receiver);
      this.implicitThis = oldVal;
      if (!safe) {
         this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "callGetProperty", "(Ljava/lang/Object;)Ljava/lang/Object;");
      } else {
         this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "callGetPropertySafe", "(Ljava/lang/Object;)Ljava/lang/Object;");
      }

      this.leftHandExpression = lhs;
   }

   private void makeGroovyObjectGetPropertySite(Expression receiver, String methodName, boolean safe, boolean implicitThis) {
      if (this.isNotClinit()) {
         this.mv.visitVarInsn(25, this.callSiteArrayVarIndex);
      } else {
         this.mv.visitMethodInsn(184, this.getClassName(), "$getCallSiteArray", "()[Lorg/codehaus/groovy/runtime/callsite/CallSite;");
      }

      int index = this.allocateIndex(methodName);
      this.mv.visitLdcInsn(index);
      this.mv.visitInsn(50);
      boolean lhs = this.leftHandExpression;
      this.leftHandExpression = false;
      boolean oldVal = this.implicitThis;
      this.implicitThis = implicitThis;
      this.visitAndAutoboxBoolean(receiver);
      this.implicitThis = oldVal;
      if (!safe) {
         this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "callGroovyObjectGetProperty", "(Ljava/lang/Object;)Ljava/lang/Object;");
      } else {
         this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "callGroovyObjectGetPropertySafe", "(Ljava/lang/Object;)Ljava/lang/Object;");
      }

      this.leftHandExpression = lhs;
   }

   private String getMethodName(Expression message) {
      String methodName = null;
      if (message instanceof CastExpression) {
         CastExpression msg = (CastExpression)message;
         if (msg.getType() == ClassHelper.STRING_TYPE) {
            Expression methodExpr = msg.getExpression();
            if (methodExpr instanceof ConstantExpression) {
               methodName = methodExpr.getText();
            }
         }
      }

      if (methodName == null && message instanceof ConstantExpression) {
         ConstantExpression constantExpression = (ConstantExpression)message;
         methodName = constantExpression.getText();
      }

      return methodName;
   }

   private void makeCallSite(Expression receiver, String message, Expression arguments, boolean safe, boolean implicitThis, boolean callCurrent, boolean callStatic) {
      if (this.isNotClinit()) {
         this.mv.visitVarInsn(25, this.callSiteArrayVarIndex);
      } else {
         this.mv.visitMethodInsn(184, this.getClassName(), "$getCallSiteArray", "()[Lorg/codehaus/groovy/runtime/callsite/CallSite;");
      }

      int index = this.allocateIndex(message);
      this.mv.visitLdcInsn(index);
      this.mv.visitInsn(50);
      boolean constructor = message.equals("<$constructor$>");
      boolean lhs = this.leftHandExpression;
      this.leftHandExpression = false;
      boolean oldVal = this.implicitThis;
      this.implicitThis = implicitThis;
      this.visitAndAutoboxBoolean(receiver);
      this.implicitThis = oldVal;
      boolean containsSpreadExpression = containsSpreadExpression(arguments);
      int numberOfArguments = containsSpreadExpression ? -1 : argumentSize(arguments);
      if (numberOfArguments > 0 || containsSpreadExpression) {
         ArgumentListExpression ae;
         if (arguments instanceof ArgumentListExpression) {
            ae = (ArgumentListExpression)arguments;
         } else if (arguments instanceof TupleExpression) {
            TupleExpression te = (TupleExpression)arguments;
            ae = new ArgumentListExpression(te.getExpressions());
         } else {
            ae = new ArgumentListExpression();
            ae.addExpression(arguments);
         }

         if (containsSpreadExpression) {
            numberOfArguments = -1;
            this.despreadList(ae.getExpressions(), true);
         } else {
            numberOfArguments = ae.getExpressions().size();

            for(int i = 0; i < numberOfArguments; ++i) {
               Expression argument = ae.getExpression(i);
               this.visitAndAutoboxBoolean(argument);
               if (argument instanceof CastExpression) {
                  this.loadWrapper(argument);
               }
            }
         }
      }

      String desc;
      if (numberOfArguments != -1 && numberOfArguments > 4) {
         desc = getCreateArraySignature(numberOfArguments);
         this.mv.visitMethodInsn(184, "org/codehaus/groovy/runtime/ArrayUtil", "createArray", desc);
      }

      desc = getDescForParamNum(numberOfArguments);
      if (callStatic) {
         this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "callStatic", "(Ljava/lang/Class;" + desc);
      } else if (constructor) {
         this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "callConstructor", "(Ljava/lang/Object;" + desc);
      } else if (callCurrent) {
         this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "callCurrent", "(Lgroovy/lang/GroovyObject;" + desc);
      } else if (safe) {
         this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "callSafe", "(Ljava/lang/Object;" + desc);
      } else {
         this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "call", "(Ljava/lang/Object;" + desc);
      }

      this.leftHandExpression = lhs;
   }

   private static String getDescForParamNum(int numberOfArguments) {
      switch(numberOfArguments) {
      case 0:
         return ")Ljava/lang/Object;";
      case 1:
         return "Ljava/lang/Object;)Ljava/lang/Object;";
      case 2:
         return "Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;";
      case 3:
         return "Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;";
      case 4:
         return "Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;";
      default:
         return "[Ljava/lang/Object;)Ljava/lang/Object;";
      }
   }

   private static String getCreateArraySignature(int numberOfArguments) {
      if (sig[numberOfArguments] == null) {
         StringBuilder sb = new StringBuilder("(");

         for(int i = 0; i != numberOfArguments; ++i) {
            sb.append("Ljava/lang/Object;");
         }

         sb.append(")[Ljava/lang/Object;");
         sig[numberOfArguments] = sb.toString();
      }

      return sig[numberOfArguments];
   }

   private void makeBinopCallSite(Expression receiver, String message, Expression arguments) {
      this.prepareCallSite(message);
      boolean lhs = this.leftHandExpression;
      this.leftHandExpression = false;
      boolean oldVal = this.implicitThis;
      this.implicitThis = false;
      this.visitAndAutoboxBoolean(receiver);
      this.implicitThis = oldVal;
      this.visitAndAutoboxBoolean(arguments);
      this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "call", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
      this.leftHandExpression = lhs;
   }

   private void prepareCallSite(String message) {
      if (this.isNotClinit()) {
         this.mv.visitVarInsn(25, this.callSiteArrayVarIndex);
      } else {
         this.mv.visitMethodInsn(184, this.getClassName(), "$getCallSiteArray", "()[Lorg/codehaus/groovy/runtime/callsite/CallSite;");
      }

      int index = this.allocateIndex(message);
      this.mv.visitLdcInsn(index);
      this.mv.visitInsn(50);
   }

   private String getClassName() {
      String className;
      if (this.classNode.isInterface() && this.interfaceClassLoadingClass != null) {
         className = BytecodeHelper.getClassInternalName(this.interfaceClassLoadingClass);
      } else {
         className = this.internalClassName;
      }

      return className;
   }

   private int allocateIndex(String name) {
      this.callSites.add(name);
      return this.callSites.size() - 1;
   }

   private void despreadList(List<Expression> expressions, boolean wrap) {
      List<Expression> spreadIndexes = new ArrayList();
      List<Expression> spreadExpressions = new ArrayList();
      List<Expression> normalArguments = new ArrayList();

      for(int i = 0; i < expressions.size(); ++i) {
         Expression expr = (Expression)expressions.get(i);
         if (!(expr instanceof SpreadExpression)) {
            normalArguments.add(expr);
         } else {
            spreadIndexes.add(new ConstantExpression(i - spreadExpressions.size()));
            spreadExpressions.add(((SpreadExpression)expr).getExpression());
         }
      }

      this.visitTupleExpression(new ArgumentListExpression(normalArguments), wrap);
      (new TupleExpression(spreadExpressions)).visit(this);
      (new ArrayExpression(ClassHelper.int_TYPE, spreadIndexes, (List)null)).visit(this);
      despreadList.call(this.mv);
   }

   public void visitMethodCallExpression(MethodCallExpression call) {
      this.onLineNumber(call, "visitMethodCallExpression: \"" + call.getMethod() + "\":");
      if (this.isUnqualifiedClosureFieldCall(call)) {
         this.invokeClosure(call.getArguments(), call.getMethodAsString());
         this.record(call.getObjectExpression());
      } else {
         boolean isSuperMethodCall = usesSuper(call);
         MethodCallerMultiAdapter adapter = invokeMethod;
         if (isThisExpression(call.getObjectExpression())) {
            adapter = invokeMethodOnCurrent;
         }

         if (isSuperMethodCall) {
            adapter = invokeMethodOnSuper;
         }

         if (this.isStaticInvocation(call)) {
            adapter = invokeStaticMethod;
         }

         this.makeInvokeMethodCall(call, isSuperMethodCall, adapter);
         this.record(call.getMethod());
      }

   }

   private boolean isUnqualifiedClosureFieldCall(MethodCallExpression call) {
      String methodName = call.getMethodAsString();
      if (methodName == null) {
         return false;
      } else if (!call.isImplicitThis()) {
         return false;
      } else if (!isThisExpression(call.getObjectExpression())) {
         return false;
      } else {
         FieldNode field = this.classNode.getDeclaredField(methodName);
         if (field == null) {
            return false;
         } else if (this.isStaticInvocation(call) && !field.isStatic()) {
            return false;
         } else {
            Expression arguments = call.getArguments();
            return !this.classNode.hasPossibleMethod(methodName, arguments);
         }
      }
   }

   private void invokeClosure(Expression arguments, String methodName) {
      this.visitVariableExpression(new VariableExpression(methodName));
      if (arguments instanceof TupleExpression) {
         arguments.visit(this);
      } else {
         (new TupleExpression(arguments)).visit(this);
      }

      invokeClosureMethod.call(this.mv);
   }

   private boolean isStaticInvocation(MethodCallExpression call) {
      if (!isThisExpression(call.getObjectExpression())) {
         return false;
      } else if (this.isStaticMethod()) {
         return true;
      } else {
         return this.isStaticContext() && !call.isImplicitThis();
      }
   }

   protected boolean emptyArguments(Expression arguments) {
      return argumentSize(arguments) == 0;
   }

   protected static boolean containsSpreadExpression(Expression arguments) {
      List<Expression> args = null;
      if (arguments instanceof TupleExpression) {
         TupleExpression tupleExpression = (TupleExpression)arguments;
         args = tupleExpression.getExpressions();
      } else {
         if (!(arguments instanceof ListExpression)) {
            return arguments instanceof SpreadExpression;
         }

         ListExpression le = (ListExpression)arguments;
         args = le.getExpressions();
      }

      Iterator i$ = args.iterator();

      Expression arg;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         arg = (Expression)i$.next();
      } while(!(arg instanceof SpreadExpression));

      return true;
   }

   protected static int argumentSize(Expression arguments) {
      if (arguments instanceof TupleExpression) {
         TupleExpression tupleExpression = (TupleExpression)arguments;
         return tupleExpression.getExpressions().size();
      } else {
         return 1;
      }
   }

   public void visitStaticMethodCallExpression(StaticMethodCallExpression call) {
      this.onLineNumber(call, "visitStaticMethodCallExpression: \"" + call.getMethod() + "\":");
      this.makeCall(new ClassExpression(call.getOwnerType()), new ConstantExpression(call.getMethod()), call.getArguments(), invokeStaticMethod, false, false, false);
      this.record(call);
   }

   private void addGeneratedClosureConstructorCall(ConstructorCallExpression call) {
      this.mv.visitVarInsn(25, 0);
      ClassNode callNode = this.classNode.getSuperClass();
      TupleExpression arguments = (TupleExpression)call.getArguments();
      if (arguments.getExpressions().size() != 2) {
         throw new GroovyBugError("expected 2 arguments for closure constructor super call, but got" + arguments.getExpressions().size());
      } else {
         arguments.getExpression(0).visit(this);
         arguments.getExpression(1).visit(this);
         Parameter p = new Parameter(ClassHelper.OBJECT_TYPE, "_p");
         String descriptor = BytecodeHelper.getMethodDescriptor(ClassHelper.VOID_TYPE, new Parameter[]{p, p});
         this.mv.visitMethodInsn(183, BytecodeHelper.getClassInternalName(callNode), "<init>", descriptor);
      }
   }

   private void visitSpecialConstructorCall(ConstructorCallExpression call) {
      if (this.classNode.declaresInterface(ClassHelper.GENERATED_CLOSURE_Type)) {
         this.addGeneratedClosureConstructorCall(call);
      } else {
         ClassNode callNode = this.classNode;
         if (call.isSuperCall()) {
            callNode = callNode.getSuperClass();
         }

         List constructors = this.sortConstructors(call, callNode);
         call.getArguments().visit(this);
         this.mv.visitInsn(89);
         this.helper.pushConstant(constructors.size());
         this.visitClassExpression(new ClassExpression(callNode));
         selectConstructorAndTransformArguments.call(this.mv);
         this.mv.visitInsn(90);
         this.mv.visitInsn(4);
         this.mv.visitInsn(126);
         Label afterIf = new Label();
         this.mv.visitJumpInsn(153, afterIf);
         this.mv.visitInsn(3);
         this.mv.visitInsn(50);
         this.mv.visitTypeInsn(192, "[Ljava/lang/Object;");
         this.mv.visitLabel(afterIf);
         this.mv.visitInsn(95);
         if (this.constructorNode != null) {
            this.mv.visitVarInsn(25, 0);
         } else {
            this.mv.visitTypeInsn(187, BytecodeHelper.getClassInternalName(callNode));
         }

         this.mv.visitInsn(95);
         this.mv.visitIntInsn(16, 8);
         this.mv.visitInsn(122);
         Label[] targets = new Label[constructors.size()];
         int[] indices = new int[constructors.size()];

         for(int i = 0; i < targets.length; indices[i] = i++) {
            targets[i] = new Label();
         }

         Label defaultLabel = new Label();
         Label afterSwitch = new Label();
         this.mv.visitLookupSwitchInsn(defaultLabel, indices, targets);

         for(int i = 0; i < targets.length; ++i) {
            this.mv.visitLabel(targets[i]);
            if (this.constructorNode != null) {
               this.mv.visitInsn(95);
               this.mv.visitInsn(90);
            } else {
               this.mv.visitInsn(90);
               this.mv.visitInsn(93);
               this.mv.visitInsn(87);
            }

            ConstructorNode cn = (ConstructorNode)constructors.get(i);
            String descriptor = BytecodeHelper.getMethodDescriptor(ClassHelper.VOID_TYPE, cn.getParameters());
            Parameter[] parameters = cn.getParameters();

            for(int p = 0; p < parameters.length; ++p) {
               this.mv.visitInsn(89);
               this.helper.pushConstant(p);
               this.mv.visitInsn(50);
               ClassNode type = parameters[p].getType();
               if (ClassHelper.isPrimitiveType(type)) {
                  this.helper.unbox(type);
               } else {
                  this.helper.doCast(type);
               }

               this.helper.swapWithObject(type);
            }

            this.mv.visitInsn(87);
            this.mv.visitMethodInsn(183, BytecodeHelper.getClassInternalName(callNode), "<init>", descriptor);
            this.mv.visitJumpInsn(167, afterSwitch);
         }

         this.mv.visitLabel(defaultLabel);
         this.mv.visitTypeInsn(187, "java/lang/IllegalArgumentException");
         this.mv.visitInsn(89);
         this.mv.visitLdcInsn("illegal constructor number");
         this.mv.visitMethodInsn(183, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V");
         this.mv.visitInsn(191);
         this.mv.visitLabel(afterSwitch);
         if (this.constructorNode == null) {
            this.mv.visitInsn(95);
         }

         this.mv.visitInsn(87);
      }
   }

   private List sortConstructors(ConstructorCallExpression call, ClassNode callNode) {
      List constructors = new ArrayList(callNode.getDeclaredConstructors());
      Comparator comp = new Comparator() {
         public int compare(Object arg0, Object arg1) {
            ConstructorNode c0 = (ConstructorNode)arg0;
            ConstructorNode c1 = (ConstructorNode)arg1;
            String descriptor0 = BytecodeHelper.getMethodDescriptor(ClassHelper.VOID_TYPE, c0.getParameters());
            String descriptor1 = BytecodeHelper.getMethodDescriptor(ClassHelper.VOID_TYPE, c1.getParameters());
            return descriptor0.compareTo(descriptor1);
         }
      };
      Collections.sort(constructors, comp);
      return constructors;
   }

   public void visitConstructorCallExpression(ConstructorCallExpression call) {
      this.onLineNumber(call, "visitConstructorCallExpression: \"" + call.getType().getName() + "\":");
      if (call.isSpecialCall()) {
         this.specialCallWithinConstructor = true;
         this.visitSpecialConstructorCall(call);
         this.specialCallWithinConstructor = false;
      } else {
         Expression arguments = call.getArguments();
         if (arguments instanceof TupleExpression) {
            TupleExpression tupleExpression = (TupleExpression)arguments;
            int size = tupleExpression.getExpressions().size();
            if (size == 0) {
               arguments = MethodCallExpression.NO_ARGUMENTS;
            }
         }

         Expression receiverClass = new ClassExpression(call.getType());
         this.makeCallSite(receiverClass, "<$constructor$>", arguments, false, false, false, false);
         this.record(call);
      }
   }

   private static String makeFieldClassName(ClassNode type) {
      String internalName = BytecodeHelper.getClassInternalName(type);
      StringBuffer ret = new StringBuffer(internalName.length());

      for(int i = 0; i < internalName.length(); ++i) {
         char c = internalName.charAt(i);
         if (c == '/') {
            ret.append('$');
         } else if (c != ';') {
            ret.append(c);
         }
      }

      return ret.toString();
   }

   private static String getStaticFieldName(ClassNode type) {
      ClassNode componentType = type;

      String prefix;
      for(prefix = ""; componentType.isArray(); componentType = componentType.getComponentType()) {
         prefix = prefix + "$";
      }

      if (prefix.length() != 0) {
         prefix = "array" + prefix;
      }

      String name = prefix + "$class$" + makeFieldClassName(componentType);
      return name;
   }

   private void visitAttributeOrProperty(PropertyExpression expression, MethodCallerMultiAdapter adapter) {
      Expression objectExpression = expression.getObjectExpression();
      String name;
      if (isThisOrSuper(objectExpression)) {
         name = expression.getPropertyAsString();
         if (name != null) {
            FieldNode field = null;
            boolean privateSuperField = false;
            if (isSuperExpression(objectExpression)) {
               field = this.classNode.getSuperClass().getDeclaredField(name);
               if (field != null && (field.getModifiers() & 2) != 0) {
                  privateSuperField = true;
               }
            } else if (this.isNotExplicitThisInClosure(expression.isImplicitThis())) {
               field = this.classNode.getDeclaredField(name);
            }

            if (field != null && !privateSuperField) {
               this.visitFieldExpression(new FieldExpression(field));
               return;
            }
         }

         if (isSuperExpression(objectExpression)) {
            String prefix;
            if (this.leftHandExpression) {
               prefix = "set";
            } else {
               prefix = "get";
            }

            String propName = prefix + MetaClassHelper.capitalize(name);
            this.visitMethodCallExpression(new MethodCallExpression(objectExpression, propName, MethodCallExpression.NO_ARGUMENTS));
            return;
         }
      }

      name = expression.getPropertyAsString();
      if (expression.getObjectExpression() instanceof ClassExpression && name != null && name.equals("this")) {
         ClassNode type = expression.getObjectExpression().getType();
         ClassNode iterType = this.classNode;
         this.mv.visitVarInsn(25, 0);

         while(!iterType.equals(type)) {
            String ownerName = BytecodeHelper.getClassInternalName(iterType);
            iterType = iterType.getOuterClass();
            String typeName = BytecodeHelper.getTypeDescription(iterType);
            this.mv.visitFieldInsn(180, ownerName, "this$0", typeName);
         }

      } else {
         if (adapter == getProperty && !expression.isSpreadSafe() && name != null) {
            this.makeGetPropertySite(objectExpression, name, expression.isSafe(), expression.isImplicitThis());
         } else if (adapter == getGroovyObjectProperty && !expression.isSpreadSafe() && name != null) {
            this.makeGroovyObjectGetPropertySite(objectExpression, name, expression.isSafe(), expression.isImplicitThis());
         } else {
            this.makeCall(objectExpression, new CastExpression(ClassHelper.STRING_TYPE, expression.getProperty()), MethodCallExpression.NO_ARGUMENTS, adapter, expression.isSafe(), expression.isSpreadSafe(), expression.isImplicitThis());
         }

      }
   }

   private boolean isStaticContext() {
      if (this.compileStack != null && this.compileStack.getScope() != null) {
         return this.compileStack.getScope().isInStaticContext();
      } else if (!this.isInClosure()) {
         return false;
      } else if (this.constructorNode != null) {
         return false;
      } else {
         return this.classNode.isStaticClass() || this.methodNode.isStatic();
      }
   }

   public void visitPropertyExpression(PropertyExpression expression) {
      Expression objectExpression = expression.getObjectExpression();
      MethodCallerMultiAdapter adapter;
      if (this.leftHandExpression) {
         adapter = setProperty;
         if (this.isGroovyObject(objectExpression)) {
            adapter = setGroovyObjectProperty;
         }

         if (this.isStaticContext() && isThisOrSuper(objectExpression)) {
            adapter = setProperty;
         }
      } else {
         adapter = getProperty;
         if (this.isGroovyObject(objectExpression)) {
            adapter = getGroovyObjectProperty;
         }

         if (this.isStaticContext() && isThisOrSuper(objectExpression)) {
            adapter = getProperty;
         }
      }

      this.visitAttributeOrProperty(expression, adapter);
      this.record(expression.getProperty());
   }

   public void visitAttributeExpression(AttributeExpression expression) {
      Expression objectExpression = expression.getObjectExpression();
      MethodCallerMultiAdapter adapter;
      if (this.leftHandExpression) {
         adapter = setField;
         if (this.isGroovyObject(objectExpression)) {
            adapter = setGroovyObjectField;
         }

         if (usesSuper((PropertyExpression)expression)) {
            adapter = setFieldOnSuper;
         }
      } else {
         adapter = getField;
         if (this.isGroovyObject(objectExpression)) {
            adapter = getGroovyObjectField;
         }

         if (usesSuper((PropertyExpression)expression)) {
            adapter = getFieldOnSuper;
         }
      }

      this.visitAttributeOrProperty(expression, adapter);
      if (!this.leftHandExpression) {
         this.record(expression.getProperty());
      }

   }

   protected boolean isGroovyObject(Expression objectExpression) {
      return isThisExpression(objectExpression) || objectExpression.getType().isDerivedFromGroovyObject() && !(objectExpression instanceof ClassExpression);
   }

   public void visitFieldExpression(FieldExpression expression) {
      FieldNode field = expression.getField();
      if (field.isStatic()) {
         if (this.leftHandExpression) {
            this.storeStaticField(expression);
         } else {
            this.loadStaticField(expression);
         }
      } else {
         if (this.leftHandExpression) {
            this.storeThisInstanceField(expression);
         } else {
            this.loadInstanceField(expression);
         }

         this.record(expression);
      }

   }

   public void loadStaticField(FieldExpression fldExp) {
      FieldNode field = fldExp.getField();
      boolean holder = field.isHolder() && !this.isInClosureConstructor();
      ClassNode type = field.getType();
      String ownerName = field.getOwner().equals(this.classNode) ? this.internalClassName : BytecodeHelper.getClassInternalName(field.getOwner());
      if (holder) {
         this.mv.visitFieldInsn(178, ownerName, fldExp.getFieldName(), BytecodeHelper.getTypeDescription(type));
         this.mv.visitMethodInsn(182, "groovy/lang/Reference", "get", "()Ljava/lang/Object;");
      } else {
         this.mv.visitFieldInsn(178, ownerName, fldExp.getFieldName(), BytecodeHelper.getTypeDescription(type));
         if (ClassHelper.isPrimitiveType(type)) {
            this.helper.box(type);
         }
      }

   }

   public void loadInstanceField(FieldExpression fldExp) {
      FieldNode field = fldExp.getField();
      boolean holder = field.isHolder() && !this.isInClosureConstructor();
      ClassNode type = field.getType();
      String ownerName = field.getOwner().equals(this.classNode) ? this.internalClassName : BytecodeHelper.getClassInternalName(field.getOwner());
      this.mv.visitVarInsn(25, 0);
      this.mv.visitFieldInsn(180, ownerName, fldExp.getFieldName(), BytecodeHelper.getTypeDescription(type));
      if (holder) {
         this.mv.visitMethodInsn(182, "groovy/lang/Reference", "get", "()Ljava/lang/Object;");
      } else if (ClassHelper.isPrimitiveType(type)) {
         this.helper.box(type);
      }

   }

   public void storeThisInstanceField(FieldExpression expression) {
      FieldNode field = expression.getField();
      boolean holder = field.isHolder() && !this.isInClosureConstructor() && !expression.isUseReferenceDirectly();
      ClassNode type = field.getType();
      String ownerName = field.getOwner().equals(this.classNode) ? this.internalClassName : BytecodeHelper.getClassInternalName(field.getOwner());
      if (holder) {
         this.mv.visitVarInsn(25, 0);
         this.mv.visitFieldInsn(180, ownerName, expression.getFieldName(), BytecodeHelper.getTypeDescription(type));
         this.mv.visitInsn(95);
         this.mv.visitMethodInsn(182, "groovy/lang/Reference", "set", "(Ljava/lang/Object;)V");
      } else {
         if (this.isInClosureConstructor()) {
            this.helper.doCast(type);
         } else if (!ClassHelper.isPrimitiveType(type)) {
            this.doConvertAndCast(type);
         }

         this.mv.visitVarInsn(25, 0);
         this.mv.visitInsn(95);
         this.helper.unbox(type);
         this.helper.putField(field, ownerName);
      }

   }

   public void storeStaticField(FieldExpression expression) {
      FieldNode field = expression.getField();
      boolean holder = field.isHolder() && !this.isInClosureConstructor();
      ClassNode type = field.getType();
      String ownerName = field.getOwner().equals(this.classNode) ? this.internalClassName : BytecodeHelper.getClassInternalName(field.getOwner());
      if (holder) {
         this.mv.visitFieldInsn(178, ownerName, expression.getFieldName(), BytecodeHelper.getTypeDescription(type));
         this.mv.visitInsn(95);
         this.mv.visitMethodInsn(182, "groovy/lang/Reference", "set", "(Ljava/lang/Object;)V");
      } else {
         this.helper.doCast(type);
         this.mv.visitFieldInsn(179, ownerName, expression.getFieldName(), BytecodeHelper.getTypeDescription(type));
      }

   }

   protected void visitOuterFieldExpression(FieldExpression expression, ClassNode outerClassNode, int steps, boolean first) {
      FieldNode field = expression.getField();
      boolean isStatic = field.isStatic();
      int tempIdx = this.compileStack.defineTemporaryVariable((org.codehaus.groovy.ast.Variable)field, this.leftHandExpression && first);
      if (steps > 1 || !isStatic) {
         this.mv.visitVarInsn(25, 0);
         this.mv.visitFieldInsn(180, this.internalClassName, "owner", BytecodeHelper.getTypeDescription(outerClassNode));
      }

      if (steps == 1) {
         int opcode = this.leftHandExpression ? (isStatic ? 179 : 181) : (isStatic ? 178 : 180);
         String ownerName = BytecodeHelper.getClassInternalName(outerClassNode);
         if (this.leftHandExpression) {
            this.mv.visitVarInsn(25, tempIdx);
            boolean holder = field.isHolder() && !this.isInClosureConstructor();
            if (!holder) {
               this.doConvertAndCast(field.getType());
            }
         }

         this.mv.visitFieldInsn(opcode, ownerName, expression.getFieldName(), BytecodeHelper.getTypeDescription(field.getType()));
         if (!this.leftHandExpression && ClassHelper.isPrimitiveType(field.getType())) {
            this.helper.box(field.getType());
         }
      } else {
         this.visitOuterFieldExpression(expression, outerClassNode.getOuterClass(), steps - 1, false);
      }

   }

   public void visitVariableExpression(VariableExpression expression) {
      String variableName = expression.getName();
      ClassNode classNode = this.classNode;
      if (this.isInClosure()) {
         classNode = this.getOutermostClass();
      }

      if (!variableName.equals("this")) {
         if (variableName.equals("super")) {
            if (this.isStaticMethod()) {
               this.visitClassExpression(new ClassExpression(classNode.getSuperClass()));
            } else {
               this.loadThis();
            }

         } else {
            Variable variable = this.compileStack.getVariable(variableName, false);
            if (variable == null) {
               this.processClassVariable(variableName);
            } else {
               this.processStackVariable(variable, expression.isUseReferenceDirectly());
            }

            if (!this.leftHandExpression) {
               this.record(expression);
            }

         }
      } else {
         if (!this.isStaticMethod() && (this.implicitThis || !this.isStaticContext())) {
            this.loadThis();
         } else {
            this.visitClassExpression(new ClassExpression(classNode));
         }

      }
   }

   private void loadThis() {
      this.mv.visitVarInsn(25, 0);
      if (!this.implicitThis && this.isInClosure()) {
         this.mv.visitMethodInsn(182, "groovy/lang/Closure", "getThisObject", "()Ljava/lang/Object;");
      }

   }

   protected void processStackVariable(Variable variable, boolean useReferenceDirectly) {
      if (this.leftHandExpression) {
         this.helper.storeVar(variable);
      } else {
         this.helper.loadVar(variable, useReferenceDirectly);
      }

   }

   protected void processClassVariable(String name) {
      if (this.passingParams && this.isInScriptBody()) {
         this.mv.visitTypeInsn(187, "org/codehaus/groovy/runtime/ScriptReference");
         this.mv.visitInsn(89);
         this.loadThisOrOwner();
         this.mv.visitLdcInsn(name);
         this.mv.visitMethodInsn(183, "org/codehaus/groovy/runtime/ScriptReference", "<init>", "(Lgroovy/lang/Script;Ljava/lang/String;)V");
      } else {
         PropertyExpression pexp = new PropertyExpression(VariableExpression.THIS_EXPRESSION, name);
         pexp.setImplicitThis(true);
         this.visitPropertyExpression(pexp);
      }

   }

   protected void processFieldAccess(String name, FieldNode field, int steps) {
      FieldExpression expression = new FieldExpression(field);
      if (steps == 0) {
         this.visitFieldExpression(expression);
      } else {
         this.visitOuterFieldExpression(expression, this.classNode.getOuterClass(), steps, true);
      }

   }

   protected boolean isInScriptBody() {
      if (this.classNode.isScriptBody()) {
         return true;
      } else {
         return this.classNode.isScript() && this.methodNode != null && this.methodNode.getName().equals("run");
      }
   }

   protected boolean isPopRequired(Expression expression) {
      if (expression instanceof MethodCallExpression) {
         return expression.getType() != ClassHelper.VOID_TYPE;
      } else if (expression instanceof DeclarationExpression) {
         DeclarationExpression de = (DeclarationExpression)expression;
         return de.getLeftExpression() instanceof TupleExpression;
      } else {
         if (expression instanceof BinaryExpression) {
            BinaryExpression binExp = (BinaryExpression)expression;
            binExp.getOperation().getType();
         }

         if (expression instanceof ConstructorCallExpression) {
            ConstructorCallExpression cce = (ConstructorCallExpression)expression;
            return !cce.isSpecialCall();
         } else {
            return true;
         }
      }
   }

   protected void createInterfaceSyntheticStaticFields() {
      if (!this.referencedClasses.isEmpty()) {
         this.addInnerClass(this.interfaceClassLoadingClass);
         Iterator i$ = this.referencedClasses.keySet().iterator();

         while(i$.hasNext()) {
            String staticFieldName = (String)i$.next();
            this.interfaceClassLoadingClass.addField(staticFieldName, 4104, ClassHelper.CLASS_Type, new ClassExpression((ClassNode)this.referencedClasses.get(staticFieldName)));
         }

      }
   }

   protected void createSyntheticStaticFields() {
      Iterator i$ = this.referencedClasses.keySet().iterator();

      while(i$.hasNext()) {
         String staticFieldName = (String)i$.next();
         FieldNode fn = this.classNode.getDeclaredField(staticFieldName);
         if (fn != null) {
            boolean type = fn.getType() == ClassHelper.CLASS_Type;
            boolean modifiers = fn.getModifiers() == 4104;
            if (!type || !modifiers) {
               String text = "";
               if (!type) {
                  text = " with wrong type: " + fn.getType() + " (java.lang.Class needed)";
               }

               if (!modifiers) {
                  text = " with wrong modifiers: " + fn.getModifiers() + " (" + 4104 + " needed)";
               }

               this.throwException("tried to set a static syntethic field " + staticFieldName + " in " + this.classNode.getName() + " for class resolving, but found alreeady a node of that" + " name " + text);
            }
         } else {
            this.cv.visitField(4106, staticFieldName, "Ljava/lang/Class;", (String)null, (Object)null);
         }

         this.mv = this.cv.visitMethod(4106, "$get$" + staticFieldName, "()Ljava/lang/Class;", (String)null, (String[])null);
         this.mv.visitCode();
         this.mv.visitFieldInsn(178, this.internalClassName, staticFieldName, "Ljava/lang/Class;");
         this.mv.visitInsn(89);
         Label l0 = new Label();
         this.mv.visitJumpInsn(199, l0);
         this.mv.visitInsn(87);
         this.mv.visitLdcInsn(BytecodeHelper.getClassLoadingTypeDescription((ClassNode)this.referencedClasses.get(staticFieldName)));
         this.mv.visitMethodInsn(184, this.internalClassName, "class$", "(Ljava/lang/String;)Ljava/lang/Class;");
         this.mv.visitInsn(89);
         this.mv.visitFieldInsn(179, this.internalClassName, staticFieldName, "Ljava/lang/Class;");
         this.mv.visitLabel(l0);
         this.mv.visitInsn(176);
         this.mv.visitMaxs(0, 0);
         this.mv.visitEnd();
      }

      this.mv = this.cv.visitMethod(4104, "class$", "(Ljava/lang/String;)Ljava/lang/Class;", (String)null, (String[])null);
      Label l0 = new Label();
      this.mv.visitLabel(l0);
      this.mv.visitVarInsn(25, 0);
      this.mv.visitMethodInsn(184, "java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
      Label l1 = new Label();
      this.mv.visitLabel(l1);
      this.mv.visitInsn(176);
      Label l2 = new Label();
      this.mv.visitLabel(l2);
      this.mv.visitVarInsn(58, 1);
      this.mv.visitTypeInsn(187, "java/lang/NoClassDefFoundError");
      this.mv.visitInsn(89);
      this.mv.visitVarInsn(25, 1);
      this.mv.visitMethodInsn(182, "java/lang/ClassNotFoundException", "getMessage", "()Ljava/lang/String;");
      this.mv.visitMethodInsn(183, "java/lang/NoClassDefFoundError", "<init>", "(Ljava/lang/String;)V");
      this.mv.visitInsn(191);
      this.mv.visitTryCatchBlock(l0, l2, l2, "java/lang/ClassNotFoundException");
      this.mv.visitMaxs(3, 2);
   }

   public void visitClassExpression(ClassExpression expression) {
      ClassNode type = expression.getType();
      if (ClassHelper.isPrimitiveType(type)) {
         ClassNode objectType = ClassHelper.getWrapper(type);
         this.mv.visitFieldInsn(178, BytecodeHelper.getClassInternalName(objectType), "TYPE", "Ljava/lang/Class;");
      } else {
         String staticFieldName = getStaticFieldName(type);
         this.referencedClasses.put(staticFieldName, type);
         String internalClassName = this.internalClassName;
         if (this.classNode.isInterface()) {
            internalClassName = BytecodeHelper.getClassInternalName(this.interfaceClassLoadingClass);
            this.mv.visitFieldInsn(178, internalClassName, staticFieldName, "Ljava/lang/Class;");
         } else {
            this.mv.visitMethodInsn(184, internalClassName, "$get$" + staticFieldName, "()Ljava/lang/Class;");
         }
      }

   }

   public void visitRangeExpression(RangeExpression expression) {
      expression.getFrom().visit(this);
      expression.getTo().visit(this);
      this.helper.pushConstant(expression.isInclusive());
      createRangeMethod.call(this.mv);
   }

   public void visitMapEntryExpression(MapEntryExpression expression) {
      throw new GroovyBugError("MapEntryExpression should not be visited here");
   }

   public void visitMapExpression(MapExpression expression) {
      List entries = expression.getMapEntryExpressions();
      int size = entries.size();
      this.helper.pushConstant(size * 2);
      this.mv.visitTypeInsn(189, "java/lang/Object");
      int i = 0;
      Iterator iter = entries.iterator();

      while(iter.hasNext()) {
         Object object = iter.next();
         MapEntryExpression entry = (MapEntryExpression)object;
         this.mv.visitInsn(89);
         this.helper.pushConstant(i++);
         this.visitAndAutoboxBoolean(entry.getKeyExpression());
         this.mv.visitInsn(83);
         this.mv.visitInsn(89);
         this.helper.pushConstant(i++);
         this.visitAndAutoboxBoolean(entry.getValueExpression());
         this.mv.visitInsn(83);
      }

      createMapMethod.call(this.mv);
   }

   public void visitArgumentlistExpression(ArgumentListExpression ale) {
      if (containsSpreadExpression(ale)) {
         this.despreadList(ale.getExpressions(), true);
      } else {
         this.visitTupleExpression(ale, true);
      }

   }

   public void visitTupleExpression(TupleExpression expression) {
      this.visitTupleExpression(expression, false);
   }

   private void visitTupleExpression(TupleExpression expression, boolean useWrapper) {
      int size = expression.getExpressions().size();
      this.helper.pushConstant(size);
      this.mv.visitTypeInsn(189, "java/lang/Object");

      for(int i = 0; i < size; ++i) {
         this.mv.visitInsn(89);
         this.helper.pushConstant(i);
         Expression argument = expression.getExpression(i);
         this.visitAndAutoboxBoolean(argument);
         if (useWrapper && argument instanceof CastExpression) {
            this.loadWrapper(argument);
         }

         this.mv.visitInsn(83);
      }

   }

   private void loadWrapper(Expression argument) {
      ClassNode goalClass = argument.getType();
      this.visitClassExpression(new ClassExpression(goalClass));
      if (goalClass.isDerivedFromGroovyObject()) {
         createGroovyObjectWrapperMethod.call(this.mv);
      } else {
         createPojoWrapperMethod.call(this.mv);
      }

   }

   public void visitArrayExpression(ArrayExpression expression) {
      ClassNode elementType = expression.getElementType();
      String arrayTypeName = BytecodeHelper.getClassInternalName(elementType);
      List<Expression> sizeExpression = expression.getSizeExpression();
      int size = 0;
      int dimensions = 0;
      if (sizeExpression != null) {
         Iterator i$ = sizeExpression.iterator();

         while(i$.hasNext()) {
            Expression element = (Expression)i$.next();
            if (element == ConstantExpression.EMPTY_EXPRESSION) {
               break;
            }

            ++dimensions;
            this.visitAndAutoboxBoolean(element);
            this.helper.unbox(Integer.TYPE);
         }
      } else {
         size = expression.getExpressions().size();
         this.helper.pushConstant(size);
      }

      int storeIns = 83;
      if (sizeExpression != null) {
         arrayTypeName = BytecodeHelper.getTypeDescription(expression.getType());
         this.mv.visitMultiANewArrayInsn(arrayTypeName, dimensions);
      } else if (ClassHelper.isPrimitiveType(elementType)) {
         int primType = 0;
         if (elementType == ClassHelper.boolean_TYPE) {
            primType = 4;
            storeIns = 84;
         } else if (elementType == ClassHelper.char_TYPE) {
            primType = 5;
            storeIns = 85;
         } else if (elementType == ClassHelper.float_TYPE) {
            primType = 6;
            storeIns = 81;
         } else if (elementType == ClassHelper.double_TYPE) {
            primType = 7;
            storeIns = 82;
         } else if (elementType == ClassHelper.byte_TYPE) {
            primType = 8;
            storeIns = 84;
         } else if (elementType == ClassHelper.short_TYPE) {
            primType = 9;
            storeIns = 86;
         } else if (elementType == ClassHelper.int_TYPE) {
            primType = 10;
            storeIns = 79;
         } else if (elementType == ClassHelper.long_TYPE) {
            primType = 11;
            storeIns = 80;
         }

         this.mv.visitIntInsn(188, primType);
      } else {
         this.mv.visitTypeInsn(189, arrayTypeName);
      }

      int par;
      for(par = 0; par < size; ++par) {
         this.mv.visitInsn(89);
         this.helper.pushConstant(par);
         Expression elementExpression = expression.getExpression(par);
         if (elementExpression == null) {
            ConstantExpression.NULL.visit(this);
         } else if (!elementType.equals(elementExpression.getType())) {
            this.visitCastExpression(new CastExpression(elementType, elementExpression, true));
         } else {
            this.visitAndAutoboxBoolean(elementExpression);
         }

         this.mv.visitInsn(storeIns);
      }

      if (sizeExpression == null && ClassHelper.isPrimitiveType(elementType)) {
         par = this.compileStack.defineTemporaryVariable("par", true);
         this.mv.visitVarInsn(25, par);
      }

   }

   public void visitClosureListExpression(ClosureListExpression expression) {
      this.compileStack.pushVariableScope(expression.getVariableScope());
      List<Expression> expressions = expression.getExpressions();
      final int size = expressions.size();
      LinkedList<DeclarationExpression> declarations = new LinkedList();

      for(int i = 0; i < size; ++i) {
         Expression expr = (Expression)expressions.get(i);
         if (expr instanceof DeclarationExpression) {
            declarations.add((DeclarationExpression)expr);
            DeclarationExpression de = (DeclarationExpression)expr;
            BinaryExpression be = new BinaryExpression(de.getLeftExpression(), de.getOperation(), de.getRightExpression());
            expressions.set(i, be);
            de.setRightExpression(ConstantExpression.NULL);
            this.visitDeclarationExpression(de);
         }
      }

      LinkedList instructions = new LinkedList();
      BytecodeSequence seq = new BytecodeSequence(instructions);
      BlockStatement bs = new BlockStatement();
      bs.addStatement(seq);
      Parameter closureIndex = new Parameter(ClassHelper.int_TYPE, "__closureIndex");
      ClosureExpression ce = new ClosureExpression(new Parameter[]{closureIndex}, bs);
      ce.setVariableScope(expression.getVariableScope());
      instructions.add(ConstantExpression.NULL);
      final Label dflt = new Label();
      final Label tableEnd = new Label();
      final Label[] labels = new Label[size];
      instructions.add(new BytecodeInstruction() {
         public void visit(MethodVisitor mv) {
            mv.visitVarInsn(21, 1);
            mv.visitTableSwitchInsn(0, size - 1, dflt, labels);
         }
      });

      int listArrayVar;
      for(listArrayVar = 0; listArrayVar < size; ++listArrayVar) {
         final Label label = new Label();
         Object expr = expressions.get(listArrayVar);
         final boolean isStatement = expr instanceof Statement;
         labels[listArrayVar] = label;
         instructions.add(new BytecodeInstruction() {
            public void visit(MethodVisitor mv) {
               mv.visitLabel(label);
               if (!isStatement) {
                  mv.visitInsn(87);
               }

            }
         });
         instructions.add(expr);
         instructions.add(new BytecodeInstruction() {
            public void visit(MethodVisitor mv) {
               mv.visitJumpInsn(167, tableEnd);
            }
         });
      }

      instructions.add(new BytecodeInstruction() {
         public void visit(MethodVisitor mv) {
            mv.visitLabel(dflt);
         }
      });
      ConstantExpression text = new ConstantExpression("invalid index for closure");
      ConstructorCallExpression cce = new ConstructorCallExpression(ClassHelper.make(IllegalArgumentException.class), text);
      ThrowStatement ts = new ThrowStatement(cce);
      instructions.add(ts);
      instructions.add(new BytecodeInstruction() {
         public void visit(MethodVisitor mv) {
            mv.visitLabel(tableEnd);
            mv.visitInsn(176);
         }
      });
      this.visitClosureExpression(ce);
      this.helper.pushConstant(size);
      this.mv.visitTypeInsn(189, "java/lang/Object");
      listArrayVar = this.compileStack.defineTemporaryVariable("_listOfClosures", true);

      for(int i = 0; i < size; ++i) {
         this.mv.visitTypeInsn(187, "org/codehaus/groovy/runtime/CurriedClosure");
         this.mv.visitInsn(92);
         this.mv.visitInsn(95);
         this.helper.pushConstant(i);
         this.mv.visitMethodInsn(183, "org/codehaus/groovy/runtime/CurriedClosure", "<init>", "(Lgroovy/lang/Closure;I)V");
         this.mv.visitVarInsn(25, listArrayVar);
         this.mv.visitInsn(95);
         this.helper.pushConstant(i);
         this.mv.visitInsn(95);
         this.mv.visitInsn(83);
      }

      this.mv.visitInsn(87);
      this.mv.visitVarInsn(25, listArrayVar);
      createListMethod.call(this.mv);
      this.compileStack.removeVar(listArrayVar);
      this.compileStack.pop();
   }

   public void visitBytecodeSequence(BytecodeSequence bytecodeSequence) {
      List instructions = bytecodeSequence.getInstructions();
      Iterator iterator = instructions.iterator();

      while(iterator.hasNext()) {
         Object part = iterator.next();
         if (part == EmptyExpression.INSTANCE) {
            this.mv.visitInsn(1);
         } else if (part instanceof Expression) {
            this.visitAndAutoboxBoolean((Expression)part);
         } else if (part instanceof Statement) {
            Statement stm = (Statement)part;
            stm.visit(this);
            this.mv.visitInsn(1);
         } else {
            BytecodeInstruction runner = (BytecodeInstruction)part;
            runner.visit(this.mv);
         }
      }

   }

   public void visitListExpression(ListExpression expression) {
      this.onLineNumber(expression, "ListExpression");
      int size = expression.getExpressions().size();
      boolean containsSpreadExpression = containsSpreadExpression(expression);
      if (!containsSpreadExpression) {
         this.helper.pushConstant(size);
         this.mv.visitTypeInsn(189, "java/lang/Object");

         for(int i = 0; i < size; ++i) {
            this.mv.visitInsn(89);
            this.helper.pushConstant(i);
            this.visitAndAutoboxBoolean(expression.getExpression(i));
            this.mv.visitInsn(83);
         }
      } else {
         this.despreadList(expression.getExpressions(), false);
      }

      createListMethod.call(this.mv);
   }

   public void visitGStringExpression(GStringExpression expression) {
      this.mv.visitTypeInsn(187, "org/codehaus/groovy/runtime/GStringImpl");
      this.mv.visitInsn(89);
      int size = expression.getValues().size();
      this.helper.pushConstant(size);
      this.mv.visitTypeInsn(189, "java/lang/Object");

      for(int i = 0; i < size; ++i) {
         this.mv.visitInsn(89);
         this.helper.pushConstant(i);
         this.visitAndAutoboxBoolean(expression.getValue(i));
         this.mv.visitInsn(83);
      }

      List strings = expression.getStrings();
      size = strings.size();
      this.helper.pushConstant(size);
      this.mv.visitTypeInsn(189, "java/lang/String");

      for(int i = 0; i < size; ++i) {
         this.mv.visitInsn(89);
         this.helper.pushConstant(i);
         this.mv.visitLdcInsn(((ConstantExpression)strings.get(i)).getValue());
         this.mv.visitInsn(83);
      }

      this.mv.visitMethodInsn(183, "org/codehaus/groovy/runtime/GStringImpl", "<init>", "([Ljava/lang/Object;[Ljava/lang/String;)V");
   }

   public void visitAnnotations(AnnotatedNode node) {
   }

   private void visitAnnotations(AnnotatedNode targetNode, Object visitor) {
      Iterator i$ = targetNode.getAnnotations().iterator();

      while(i$.hasNext()) {
         AnnotationNode an = (AnnotationNode)i$.next();
         if (!an.isBuiltIn() && !an.hasSourceRetention()) {
            groovyjarjarasm.asm.AnnotationVisitor av = this.getAnnotationVisitor(targetNode, an, visitor);
            this.visitAnnotationAttributes(an, av);
            av.visitEnd();
         }
      }

   }

   private void visitParameterAnnotations(Parameter parameter, int paramNumber, MethodVisitor mv) {
      Iterator i$ = parameter.getAnnotations().iterator();

      while(i$.hasNext()) {
         AnnotationNode an = (AnnotationNode)i$.next();
         if (!an.isBuiltIn() && !an.hasSourceRetention()) {
            String annotationDescriptor = BytecodeHelper.getTypeDescription(an.getClassNode());
            groovyjarjarasm.asm.AnnotationVisitor av = mv.visitParameterAnnotation(paramNumber, annotationDescriptor, an.hasRuntimeRetention());
            this.visitAnnotationAttributes(an, av);
            av.visitEnd();
         }
      }

   }

   private groovyjarjarasm.asm.AnnotationVisitor getAnnotationVisitor(AnnotatedNode targetNode, AnnotationNode an, Object visitor) {
      String annotationDescriptor = BytecodeHelper.getTypeDescription(an.getClassNode());
      if (targetNode instanceof MethodNode) {
         return ((MethodVisitor)visitor).visitAnnotation(annotationDescriptor, an.hasRuntimeRetention());
      } else if (targetNode instanceof FieldNode) {
         return ((FieldVisitor)visitor).visitAnnotation(annotationDescriptor, an.hasRuntimeRetention());
      } else if (targetNode instanceof ClassNode) {
         return ((ClassVisitor)visitor).visitAnnotation(annotationDescriptor, an.hasRuntimeRetention());
      } else {
         this.throwException("Cannot create an AnnotationVisitor. Please report Groovy bug");
         return null;
      }
   }

   private void visitAnnotationAttributes(AnnotationNode an, groovyjarjarasm.asm.AnnotationVisitor av) {
      Map<String, Object> constantAttrs = new HashMap();
      Map<String, PropertyExpression> enumAttrs = new HashMap();
      Map<String, Object> atAttrs = new HashMap();
      Map<String, ListExpression> arrayAttrs = new HashMap();
      Iterator i$ = an.getMembers().keySet().iterator();

      while(i$.hasNext()) {
         String name = (String)i$.next();
         Expression expr = an.getMember(name);
         if (expr instanceof AnnotationConstantExpression) {
            atAttrs.put(name, ((AnnotationConstantExpression)expr).getValue());
         } else if (expr instanceof ConstantExpression) {
            constantAttrs.put(name, ((ConstantExpression)expr).getValue());
         } else if (expr instanceof ClassExpression) {
            constantAttrs.put(name, Type.getType(BytecodeHelper.getTypeDescription(expr.getType())));
         } else if (expr instanceof PropertyExpression) {
            enumAttrs.put(name, (PropertyExpression)expr);
         } else if (expr instanceof ListExpression) {
            arrayAttrs.put(name, (ListExpression)expr);
         }
      }

      i$ = constantAttrs.entrySet().iterator();

      Entry entry;
      while(i$.hasNext()) {
         entry = (Entry)i$.next();
         av.visit((String)entry.getKey(), entry.getValue());
      }

      i$ = enumAttrs.entrySet().iterator();

      while(i$.hasNext()) {
         entry = (Entry)i$.next();
         PropertyExpression propExp = (PropertyExpression)entry.getValue();
         av.visitEnum((String)entry.getKey(), BytecodeHelper.getTypeDescription(propExp.getObjectExpression().getType()), String.valueOf(((ConstantExpression)propExp.getProperty()).getValue()));
      }

      i$ = atAttrs.entrySet().iterator();

      while(i$.hasNext()) {
         entry = (Entry)i$.next();
         AnnotationNode atNode = (AnnotationNode)entry.getValue();
         groovyjarjarasm.asm.AnnotationVisitor av2 = av.visitAnnotation((String)entry.getKey(), BytecodeHelper.getTypeDescription(atNode.getClassNode()));
         this.visitAnnotationAttributes(atNode, av2);
         av2.visitEnd();
      }

      this.visitArrayAttributes(an, arrayAttrs, av);
   }

   private void visitArrayAttributes(AnnotationNode an, Map<String, ListExpression> arrayAttr, groovyjarjarasm.asm.AnnotationVisitor av) {
      if (!arrayAttr.isEmpty()) {
         groovyjarjarasm.asm.AnnotationVisitor av2;
         for(Iterator i$ = arrayAttr.entrySet().iterator(); i$.hasNext(); av2.visitEnd()) {
            Entry entry = (Entry)i$.next();
            av2 = av.visitArray((String)entry.getKey());
            List<Expression> values = ((ListExpression)entry.getValue()).getExpressions();
            if (!values.isEmpty()) {
               int arrayElementType = this.determineCommonArrayType(values);
               Iterator i$ = values.iterator();

               while(i$.hasNext()) {
                  Expression exprChild = (Expression)i$.next();
                  this.visitAnnotationArrayElement(exprChild, arrayElementType, av2);
               }
            }
         }

      }
   }

   private int determineCommonArrayType(List values) {
      Expression expr = (Expression)values.get(0);
      int arrayElementType = -1;
      if (expr instanceof AnnotationConstantExpression) {
         arrayElementType = 1;
      } else if (expr instanceof ConstantExpression) {
         arrayElementType = 2;
      } else if (expr instanceof ClassExpression) {
         arrayElementType = 3;
      } else if (expr instanceof PropertyExpression) {
         arrayElementType = 4;
      }

      return arrayElementType;
   }

   private void visitAnnotationArrayElement(Expression expr, int arrayElementType, groovyjarjarasm.asm.AnnotationVisitor av) {
      switch(arrayElementType) {
      case 1:
         AnnotationNode atAttr = (AnnotationNode)((AnnotationConstantExpression)expr).getValue();
         groovyjarjarasm.asm.AnnotationVisitor av2 = av.visitAnnotation((String)null, BytecodeHelper.getTypeDescription(atAttr.getClassNode()));
         this.visitAnnotationAttributes(atAttr, av2);
         av2.visitEnd();
         break;
      case 2:
         av.visit((String)null, ((ConstantExpression)expr).getValue());
         break;
      case 3:
         av.visit((String)null, Type.getType(BytecodeHelper.getTypeDescription(expr.getType())));
         break;
      case 4:
         PropertyExpression propExpr = (PropertyExpression)expr;
         av.visitEnum((String)null, BytecodeHelper.getTypeDescription(propExpr.getObjectExpression().getType()), String.valueOf(((ConstantExpression)propExpr.getProperty()).getValue()));
      }

   }

   protected boolean addInnerClass(ClassNode innerClass) {
      innerClass.setModule(this.classNode.getModule());
      return this.innerClasses.add(innerClass);
   }

   protected ClassNode createClosureClass(ClosureExpression expression) {
      ClassNode outerClass = this.getOutermostClass();
      String name = outerClass.getName() + "$" + this.context.getNextClosureInnerName(outerClass, this.classNode, this.methodNode);
      boolean staticMethodOrInStaticClass = this.isStaticMethod() || this.classNode.isStaticClass();
      Parameter[] parameters = expression.getParameters();
      if (parameters == null) {
         parameters = Parameter.EMPTY_ARRAY;
      } else if (parameters.length == 0) {
         Parameter it = new Parameter(ClassHelper.OBJECT_TYPE, "it", ConstantExpression.NULL);
         parameters = new Parameter[]{it};
         org.codehaus.groovy.ast.Variable ref = expression.getVariableScope().getDeclaredVariable("it");
         if (ref != null) {
            it.setClosureSharedVariable(ref.isClosureSharedVariable());
         }
      }

      Parameter[] localVariableParams = this.getClosureSharedVariables(expression);
      this.removeInitialValues(localVariableParams);
      InnerClassNode answer = new InnerClassNode(outerClass, name, 0, ClassHelper.CLOSURE_TYPE);
      answer.setEnclosingMethod(this.methodNode);
      answer.setSynthetic(true);
      answer.setUsingGenerics(outerClass.isUsingGenerics());
      if (staticMethodOrInStaticClass) {
         answer.setStaticClass(true);
      }

      if (this.isInScriptBody()) {
         answer.setScriptBody(true);
      }

      MethodNode method = answer.addMethod("doCall", 1, ClassHelper.OBJECT_TYPE, parameters, ClassNode.EMPTY_ARRAY, expression.getCode());
      method.setSourcePosition(expression);
      VariableScope varScope = expression.getVariableScope();
      if (varScope == null) {
         throw new RuntimeException("Must have a VariableScope by now! for expression: " + expression + " class: " + name);
      } else {
         method.setVariableScope(varScope.copy());
         if (parameters.length > 1 || parameters.length == 1 && parameters[0].getType() != null && parameters[0].getType() != ClassHelper.OBJECT_TYPE) {
            MethodNode call = answer.addMethod("call", 1, ClassHelper.OBJECT_TYPE, parameters, ClassNode.EMPTY_ARRAY, new ReturnStatement(new MethodCallExpression(VariableExpression.THIS_EXPRESSION, "doCall", new ArgumentListExpression(parameters))));
            call.setSourcePosition(expression);
         }

         BlockStatement block = new BlockStatement();
         VariableExpression outer = new VariableExpression("_outerInstance");
         outer.setSourcePosition(expression);
         block.getVariableScope().putReferencedLocalVariable(outer);
         VariableExpression thisObject = new VariableExpression("_thisObject");
         thisObject.setSourcePosition(expression);
         block.getVariableScope().putReferencedLocalVariable(thisObject);
         TupleExpression conArgs = new TupleExpression(outer, thisObject);
         block.addStatement(new ExpressionStatement(new ConstructorCallExpression(ClassNode.SUPER, conArgs)));
         Parameter[] params = localVariableParams;
         int len$ = localVariableParams.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Parameter param = params[i$];
            String paramName = param.getName();
            Expression initialValue = null;
            ClassNode type = param.getType();
            FieldNode paramField = null;
            initialValue = new VariableExpression(paramName);
            ClassNode realType = type;
            type = ClassHelper.makeReference();
            param.setType(ClassHelper.makeReference());
            paramField = answer.addField(paramName, 2, type, initialValue);
            paramField.setHolder(true);
            String methodName = Verifier.capitalize(paramName);
            Expression fieldExp = new FieldExpression(paramField);
            answer.addMethod("get" + methodName, 1, realType, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, new ReturnStatement(fieldExp));
         }

         params = new Parameter[2 + localVariableParams.length];
         params[0] = new Parameter(ClassHelper.OBJECT_TYPE, "_outerInstance");
         params[1] = new Parameter(ClassHelper.OBJECT_TYPE, "_thisObject");
         System.arraycopy(localVariableParams, 0, params, 2, localVariableParams.length);
         ASTNode sn = answer.addConstructor(1, params, ClassNode.EMPTY_ARRAY, block);
         sn.setSourcePosition(expression);
         return answer;
      }
   }

   private void removeInitialValues(Parameter[] params) {
      for(int i = 0; i < params.length; ++i) {
         if (params[i].hasInitialExpression()) {
            params[i] = new Parameter(params[i].getType(), params[i].getName());
         }
      }

   }

   protected Parameter[] getClosureSharedVariables(ClosureExpression ce) {
      VariableScope scope = ce.getVariableScope();
      Parameter[] ret = new Parameter[scope.getReferencedLocalVariablesCount()];
      int index = 0;

      for(Iterator iter = scope.getReferencedLocalVariablesIterator(); iter.hasNext(); ++index) {
         org.codehaus.groovy.ast.Variable element = (org.codehaus.groovy.ast.Variable)iter.next();
         Parameter p = new Parameter(element.getType(), element.getName());
         ret[index] = p;
      }

      return ret;
   }

   protected ClassNode getOutermostClass() {
      if (this.outermostClass == null) {
         for(this.outermostClass = this.classNode; this.outermostClass instanceof InnerClassNode; this.outermostClass = this.outermostClass.getOuterClass()) {
         }
      }

      return this.outermostClass;
   }

   protected void doConvertAndCast(ClassNode type) {
      this.doConvertAndCast(type, false);
   }

   protected void doConvertAndCast(ClassNode type, boolean coerce) {
      if (type != ClassHelper.OBJECT_TYPE) {
         if ((this.rightHandType == null || !this.rightHandType.isDerivedFrom(type) || !this.rightHandType.implementsInterface(type)) && this.isValidTypeForCast(type)) {
            this.visitClassExpression(new ClassExpression(type));
            if (coerce) {
               asTypeMethod.call(this.mv);
            } else {
               castToTypeMethod.call(this.mv);
            }
         }

         this.helper.doCast(type);
      }
   }

   protected void evaluateLogicalOrExpression(BinaryExpression expression) {
      this.visitBooleanExpression(new BooleanExpression(expression.getLeftExpression()));
      Label l0 = new Label();
      Label l2 = new Label();
      this.mv.visitJumpInsn(153, l0);
      this.mv.visitLabel(l2);
      this.visitConstantExpression(ConstantExpression.TRUE);
      Label l1 = new Label();
      this.mv.visitJumpInsn(167, l1);
      this.mv.visitLabel(l0);
      this.visitBooleanExpression(new BooleanExpression(expression.getRightExpression()));
      this.mv.visitJumpInsn(154, l2);
      this.visitConstantExpression(ConstantExpression.FALSE);
      this.mv.visitLabel(l1);
   }

   protected void evaluateLogicalAndExpression(BinaryExpression expression) {
      this.visitBooleanExpression(new BooleanExpression(expression.getLeftExpression()));
      Label l0 = new Label();
      this.mv.visitJumpInsn(153, l0);
      this.visitBooleanExpression(new BooleanExpression(expression.getRightExpression()));
      this.mv.visitJumpInsn(153, l0);
      this.visitConstantExpression(ConstantExpression.TRUE);
      Label l1 = new Label();
      this.mv.visitJumpInsn(167, l1);
      this.mv.visitLabel(l0);
      this.visitConstantExpression(ConstantExpression.FALSE);
      this.mv.visitLabel(l1);
   }

   protected void evaluateBinaryExpression(String method, BinaryExpression expression) {
      this.makeBinopCallSite(expression.getLeftExpression(), method, expression.getRightExpression());
   }

   protected void evaluateCompareTo(BinaryExpression expression) {
      Expression leftExpression = expression.getLeftExpression();
      leftExpression.visit(this);
      if (this.isComparisonExpression(leftExpression)) {
         this.helper.boxBoolean();
      }

      Expression rightExpression = expression.getRightExpression();
      rightExpression.visit(this);
      if (this.isComparisonExpression(rightExpression)) {
         this.helper.boxBoolean();
      }

      compareToMethod.call(this.mv);
   }

   protected void evaluateBinaryExpressionWithAssignment(String method, BinaryExpression expression) {
      Expression leftExpression = expression.getLeftExpression();
      if (leftExpression instanceof BinaryExpression) {
         BinaryExpression leftBinExpr = (BinaryExpression)leftExpression;
         if (leftBinExpr.getOperation().getType() == 30) {
            this.prepareCallSite("putAt");
            this.prepareCallSite(method);
            this.prepareCallSite("getAt");
            this.visitAndAutoboxBoolean(leftBinExpr.getLeftExpression());
            this.visitAndAutoboxBoolean(leftBinExpr.getRightExpression());
            this.mv.visitInsn(94);
            this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "call", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
            this.visitAndAutoboxBoolean(expression.getRightExpression());
            this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "call", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
            int resultVar = this.compileStack.defineTemporaryVariable("$result", true);
            this.mv.visitVarInsn(25, resultVar);
            this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "call", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
            this.mv.visitInsn(87);
            this.mv.visitVarInsn(25, resultVar);
            this.compileStack.removeVar(resultVar);
            return;
         }
      }

      this.evaluateBinaryExpression(method, expression);
      this.mv.visitInsn(89);
      this.doConvertAndCast(ClassHelper.getWrapper(leftExpression.getType()));
      this.leftHandExpression = true;
      this.evaluateExpression(leftExpression);
      this.leftHandExpression = false;
   }

   private void evaluateBinaryExpression(MethodCaller compareMethod, BinaryExpression expression) {
      Expression leftExp = expression.getLeftExpression();
      Expression rightExp = expression.getRightExpression();
      this.load(leftExp);
      this.load(rightExp);
      compareMethod.call(this.mv);
   }

   protected void evaluateEqual(BinaryExpression expression, boolean defineVariable) {
      Expression leftExpression = expression.getLeftExpression();
      if (leftExpression instanceof BinaryExpression) {
         BinaryExpression leftBinExpr = (BinaryExpression)leftExpression;
         if (leftBinExpr.getOperation().getType() == 30) {
            this.prepareCallSite("putAt");
            this.visitAndAutoboxBoolean(leftBinExpr.getLeftExpression());
            this.visitAndAutoboxBoolean(leftBinExpr.getRightExpression());
            this.visitAndAutoboxBoolean(expression.getRightExpression());
            int resultVar = this.compileStack.defineTemporaryVariable("$result", true);
            this.mv.visitVarInsn(25, resultVar);
            this.mv.visitMethodInsn(185, "org/codehaus/groovy/runtime/callsite/CallSite", "call", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;");
            this.mv.visitInsn(87);
            this.mv.visitVarInsn(25, resultVar);
            this.compileStack.removeVar(resultVar);
            return;
         }
      }

      Expression rightExpression = expression.getRightExpression();
      if (!(leftExpression instanceof TupleExpression)) {
         ClassNode type = null;
         if (expression instanceof DeclarationExpression) {
            type = leftExpression.getType();
         } else {
            type = this.getLHSType(leftExpression);
         }

         this.assignmentCastAndVisit(type, rightExpression);
      } else {
         this.visitAndAutoboxBoolean(rightExpression);
      }

      this.rightHandType = rightExpression.getType();
      this.leftHandExpression = true;
      if (leftExpression instanceof TupleExpression) {
         TupleExpression tuple = (TupleExpression)leftExpression;
         int i = 0;
         Expression lhsExpr = new BytecodeExpression() {
            public void visit(MethodVisitor mv) {
               mv.visitInsn(95);
               mv.visitInsn(90);
            }
         };
         Iterator i$ = tuple.getExpressions().iterator();

         while(i$.hasNext()) {
            Expression e = (Expression)i$.next();
            VariableExpression var = (VariableExpression)e;
            MethodCallExpression call = new MethodCallExpression(lhsExpr, "getAt", new ArgumentListExpression(new ConstantExpression(i)));
            ClassNode type = this.getLHSType(var);
            this.assignmentCastAndVisit(type, call);
            ++i;
            if (defineVariable) {
               this.compileStack.defineVariable(var, true);
            } else {
               this.visitVariableExpression(var);
            }
         }
      } else if (defineVariable) {
         VariableExpression var = (VariableExpression)leftExpression;
         this.compileStack.defineVariable(var, true);
      } else {
         this.mv.visitInsn(89);
         leftExpression.visit(this);
      }

      this.rightHandType = null;
      this.leftHandExpression = false;
   }

   private void assignmentCastAndVisit(ClassNode type, Expression rightExpression) {
      if (ClassHelper.isPrimitiveType(type)) {
         this.visitAndAutoboxBoolean(rightExpression);
      } else if (!rightExpression.getType().isDerivedFrom(type)) {
         this.visitCastExpression(new CastExpression(type, rightExpression));
      } else {
         this.visitAndAutoboxBoolean(rightExpression);
      }

   }

   protected ClassNode getLHSType(Expression leftExpression) {
      ClassNode type;
      if (leftExpression instanceof VariableExpression) {
         VariableExpression varExp = (VariableExpression)leftExpression;
         type = varExp.getType();
         if (this.isValidTypeForCast(type)) {
            return type;
         }

         String variableName = varExp.getName();
         Variable variable = this.compileStack.getVariable(variableName, false);
         if (variable != null) {
            if (variable.isHolder()) {
               return type;
            }

            if (variable.isProperty()) {
               return variable.getType();
            }

            type = variable.getType();
            if (this.isValidTypeForCast(type)) {
               return type;
            }
         } else {
            FieldNode field = this.classNode.getDeclaredField(variableName);
            if (field == null) {
               field = this.classNode.getOuterField(variableName);
            }

            if (field != null) {
               type = field.getType();
               if (!field.isHolder() && this.isValidTypeForCast(type)) {
                  return type;
               }
            }
         }
      } else if (leftExpression instanceof FieldExpression) {
         FieldExpression fieldExp = (FieldExpression)leftExpression;
         type = fieldExp.getType();
         if (this.isValidTypeForCast(type)) {
            return type;
         }
      }

      return leftExpression.getType();
   }

   protected boolean isValidTypeForCast(ClassNode type) {
      return type != ClassHelper.DYNAMIC_TYPE && type != ClassHelper.REFERENCE_TYPE;
   }

   public void visitBytecodeExpression(BytecodeExpression cle) {
      cle.visit(this.mv);
   }

   protected void visitAndAutoboxBoolean(Expression expression) {
      expression.visit(this);
      if (this.isComparisonExpression(expression)) {
         this.helper.boxBoolean();
      }

   }

   private void execMethodAndStoreForSubscriptOperator(String method, Expression expression) {
      this.execMethodAndStoreForSubscriptOperator(method, expression, (Expression)null);
   }

   private void execMethodAndStoreForSubscriptOperator(String method, Expression expression, Expression getAtResultExp) {
      this.makeCallSite(getAtResultExp == null ? expression : getAtResultExp, method, MethodCallExpression.NO_ARGUMENTS, false, false, false, false);
      if (expression instanceof BinaryExpression) {
         BinaryExpression be = (BinaryExpression)expression;
         if (be.getOperation().getType() == 30) {
            this.mv.visitInsn(89);
            final int resultIdx = this.compileStack.defineTemporaryVariable("postfix_" + method, true);
            BytecodeExpression result = new BytecodeExpression() {
               public void visit(MethodVisitor mv) {
                  mv.visitVarInsn(25, resultIdx);
               }
            };
            TupleExpression args = new ArgumentListExpression();
            args.addExpression(be.getRightExpression());
            args.addExpression(result);
            this.makeCallSite(be.getLeftExpression(), "putAt", args, false, false, false, false);
            this.mv.visitInsn(87);
            this.compileStack.removeVar(resultIdx);
         }
      }

      if (expression instanceof VariableExpression || expression instanceof FieldExpression || expression instanceof PropertyExpression) {
         this.mv.visitInsn(89);
         this.leftHandExpression = true;
         expression.visit(this);
         this.leftHandExpression = false;
      }

   }

   protected void evaluatePrefixMethod(String method, Expression expression) {
      this.execMethodAndStoreForSubscriptOperator(method, expression);
   }

   protected void evaluatePostfixMethod(String method, Expression expression) {
      boolean getAtOp = false;
      BinaryExpression be = null;
      Expression getAtResultExp = null;
      String varName = "tmp_postfix_" + method;
      final int idx = this.compileStack.defineTemporaryVariable(varName, false);
      if (expression instanceof BinaryExpression) {
         be = (BinaryExpression)expression;
         if (be.getOperation().getType() == 30) {
            getAtOp = true;
            be.getRightExpression().visit(this);
            this.mv.visitVarInsn(58, idx);
            BytecodeExpression newRightExp = new BytecodeExpression() {
               public void visit(MethodVisitor mv) {
                  mv.visitVarInsn(25, idx);
               }
            };
            be.setRightExpression(newRightExp);
         }
      }

      expression.visit(this);
      final int tempIdx = this.compileStack.defineTemporaryVariable("postfix_" + method, true);
      if (getAtOp) {
         getAtResultExp = new BytecodeExpression() {
            public void visit(MethodVisitor mv) {
               mv.visitVarInsn(25, tempIdx);
            }
         };
      }

      this.execMethodAndStoreForSubscriptOperator(method, expression, getAtResultExp);
      this.mv.visitInsn(87);
      this.mv.visitVarInsn(25, tempIdx);
      this.compileStack.removeVar(tempIdx);
      this.compileStack.removeVar(idx);
   }

   protected void evaluateInstanceof(BinaryExpression expression) {
      this.visitAndAutoboxBoolean(expression.getLeftExpression());
      Expression rightExp = expression.getRightExpression();
      if (rightExp instanceof ClassExpression) {
         ClassExpression classExp = (ClassExpression)rightExp;
         ClassNode classType = classExp.getType();
         String classInternalName = BytecodeHelper.getClassInternalName(classType);
         this.mv.visitTypeInsn(193, classInternalName);
      } else {
         throw new RuntimeException("Right hand side of the instanceof keyword must be a class name, not: " + rightExp);
      }
   }

   protected boolean argumentsUseStack(Expression arguments) {
      return arguments instanceof TupleExpression || arguments instanceof ClosureExpression;
   }

   private static boolean isThisExpression(Expression expression) {
      if (expression instanceof VariableExpression) {
         VariableExpression varExp = (VariableExpression)expression;
         return varExp.getName().equals("this");
      } else {
         return false;
      }
   }

   private static boolean isSuperExpression(Expression expression) {
      if (expression instanceof VariableExpression) {
         VariableExpression varExp = (VariableExpression)expression;
         return varExp.getName().equals("super");
      } else {
         return false;
      }
   }

   private static boolean isThisOrSuper(Expression expression) {
      return isThisExpression(expression) || isSuperExpression(expression);
   }

   protected Expression createReturnLHSExpression(Expression expression) {
      if (expression instanceof BinaryExpression) {
         BinaryExpression binExpr = (BinaryExpression)expression;
         if (binExpr.getOperation().isA(1100)) {
            return this.createReusableExpression(binExpr.getLeftExpression());
         }
      }

      return null;
   }

   protected Expression createReusableExpression(Expression expression) {
      ExpressionTransformer transformer = new ExpressionTransformer() {
         public Expression transform(Expression expression) {
            if (expression instanceof PostfixExpression) {
               PostfixExpression postfixExp = (PostfixExpression)expression;
               return postfixExp.getExpression();
            } else if (expression instanceof PrefixExpression) {
               PrefixExpression prefixExp = (PrefixExpression)expression;
               return prefixExp.getExpression();
            } else {
               return expression;
            }
         }
      };
      return transformer.transform(expression.transformExpression(transformer));
   }

   protected boolean isComparisonExpression(Expression expression) {
      if (expression instanceof BinaryExpression) {
         BinaryExpression binExpr = (BinaryExpression)expression;
         switch(binExpr.getOperation().getType()) {
         case 94:
         case 120:
         case 121:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 544:
         case 573:
            return true;
         }
      } else if (expression instanceof BooleanExpression) {
         return true;
      }

      return false;
   }

   protected void onLineNumber(ASTNode statement, String message) {
      if (statement != null) {
         int line = statement.getLineNumber();
         this.currentASTNode = statement;
         if (line >= 0) {
            if (line != this.lineNumber) {
               this.lineNumber = line;
               if (this.mv != null) {
                  Label l = new Label();
                  this.mv.visitLabel(l);
                  this.mv.visitLineNumber(line, l);
               }

            }
         }
      }
   }

   private boolean isInnerClass() {
      return this.classNode instanceof InnerClassNode;
   }

   protected boolean isFieldOrVariable(String name) {
      return this.compileStack.containsVariable(name) || this.classNode.getDeclaredField(name) != null;
   }

   protected ClassNode getExpressionType(Expression expression) {
      if (this.isComparisonExpression(expression)) {
         return ClassHelper.boolean_TYPE;
      } else {
         if (expression instanceof VariableExpression) {
            VariableExpression varExpr = (VariableExpression)expression;
            if (varExpr.isThisExpression()) {
               return this.classNode;
            }

            if (varExpr.isSuperExpression()) {
               return this.classNode.getSuperClass();
            }

            Variable variable = this.compileStack.getVariable(varExpr.getName(), false);
            if (variable != null && !variable.isHolder()) {
               ClassNode type = variable.getType();
               if (!variable.isDynamicTyped()) {
                  return type;
               }
            }

            if (variable == null) {
               org.codehaus.groovy.ast.Variable var = this.compileStack.getScope().getReferencedClassVariable(varExpr.getName());
               if (var != null && !var.isDynamicTyped()) {
                  return var.getType();
               }
            }
         }

         return expression.getType();
      }
   }

   protected boolean isInClosureConstructor() {
      return this.constructorNode != null && this.classNode.getOuterClass() != null && this.classNode.getSuperClass() == ClassHelper.CLOSURE_TYPE;
   }

   protected boolean isInClosure() {
      return this.classNode.getOuterClass() != null && this.classNode.getSuperClass() == ClassHelper.CLOSURE_TYPE;
   }

   protected boolean isNotExplicitThisInClosure(boolean implicitThis) {
      return implicitThis || !this.isInClosure();
   }

   protected boolean isStaticMethod() {
      return this.methodNode != null && this.methodNode.isStatic();
   }

   protected CompileUnit getCompileUnit() {
      CompileUnit answer = this.classNode.getCompileUnit();
      if (answer == null) {
         answer = this.context.getCompileUnit();
      }

      return answer;
   }

   public static boolean usesSuper(MethodCallExpression call) {
      Expression expression = call.getObjectExpression();
      if (expression instanceof VariableExpression) {
         VariableExpression varExp = (VariableExpression)expression;
         String variable = varExp.getName();
         return variable.equals("super");
      } else {
         return false;
      }
   }

   public static boolean usesSuper(PropertyExpression pe) {
      Expression expression = pe.getObjectExpression();
      if (expression instanceof VariableExpression) {
         VariableExpression varExp = (VariableExpression)expression;
         String variable = varExp.getName();
         return variable.equals("super");
      } else {
         return false;
      }
   }

   protected int getBytecodeVersion() {
      if (!this.classNode.isUsingGenerics() && !this.classNode.isAnnotated() && !this.classNode.isAnnotationDefinition()) {
         return 47;
      } else {
         String target = this.getCompileUnit().getConfig().getTargetBytecode();
         return "1.5".equals(target) ? 49 : 47;
      }
   }

   static {
      Collections.addAll(names, new String[]{"plus", "minus", "multiply", "div", "compareTo", "or", "and", "xor", "intdiv", "mod", "leftShift", "rightShift", "rightShiftUnsigned"});
      Collections.addAll(basic, new String[]{"plus", "minus", "multiply", "div"});
   }

   private class MyMethodAdapter extends MethodAdapter {
      private String boxingDesc = null;

      public MyMethodAdapter() {
         super(AsmClassGenerator.this.mv);
      }

      private void dropBoxing() {
         if (this.boxingDesc != null) {
            super.visitMethodInsn(184, AsmClassGenerator.DTT, "box", this.boxingDesc);
            this.boxingDesc = null;
         }

      }

      public void visitInsn(int opcode) {
         this.dropBoxing();
         super.visitInsn(opcode);
      }

      public void visitIntInsn(int opcode, int operand) {
         this.dropBoxing();
         super.visitIntInsn(opcode, operand);
      }

      public void visitVarInsn(int opcode, int var) {
         this.dropBoxing();
         super.visitVarInsn(opcode, var);
      }

      public void visitTypeInsn(int opcode, String desc) {
         this.dropBoxing();
         super.visitTypeInsn(opcode, desc);
      }

      public void visitFieldInsn(int opcode, String owner, String name, String desc) {
         this.dropBoxing();
         super.visitFieldInsn(opcode, owner, name, desc);
      }

      public void visitMethodInsn(int opcode, String owner, String name, String desc) {
         if (this.boxing(opcode, owner, name)) {
            this.boxingDesc = desc;
            this.dropBoxing();
         } else if (this.unboxing(opcode, owner, name)) {
            if (this.boxingDesc != null) {
               this.boxingDesc = null;
            } else {
               super.visitMethodInsn(opcode, owner, name, desc);
            }
         } else {
            this.dropBoxing();
            super.visitMethodInsn(opcode, owner, name, desc);
         }

      }

      private boolean boxing(int opcode, String owner, String name) {
         return opcode == 184 && owner.equals(AsmClassGenerator.DTT) && name.equals("box");
      }

      private boolean unboxing(int opcode, String owner, String name) {
         return opcode == 184 && owner.equals(AsmClassGenerator.DTT) && name.endsWith("Unbox");
      }

      public void visitJumpInsn(int opcode, Label label) {
         this.dropBoxing();
         super.visitJumpInsn(opcode, label);
      }

      public void visitLabel(Label label) {
         this.dropBoxing();
         super.visitLabel(label);
      }

      public void visitLdcInsn(Object cst) {
         this.dropBoxing();
         super.visitLdcInsn(cst);
      }

      public void visitIincInsn(int var, int increment) {
         this.dropBoxing();
         super.visitIincInsn(var, increment);
      }

      public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels) {
         this.dropBoxing();
         super.visitTableSwitchInsn(min, max, dflt, labels);
      }

      public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
         this.dropBoxing();
         super.visitLookupSwitchInsn(dflt, keys, labels);
      }

      public void visitMultiANewArrayInsn(String desc, int dims) {
         this.dropBoxing();
         super.visitMultiANewArrayInsn(desc, dims);
      }

      public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
         this.dropBoxing();
         super.visitTryCatchBlock(start, end, handler, type);
      }
   }

   private static class AssertionTracker {
      int recorderIndex;
      SourceText sourceText;

      private AssertionTracker() {
      }

      // $FF: synthetic method
      AssertionTracker(Object x0) {
         this();
      }
   }
}
