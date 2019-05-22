package soot.jimple.toolkits.reflection;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.BooleanType;
import soot.Local;
import soot.PatchingChain;
import soot.PhaseOptions;
import soot.PrimType;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.ClassConstant;
import soot.jimple.GotoStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.toolkits.scalar.CopyPropagator;
import soot.jimple.toolkits.scalar.DeadAssignmentEliminator;
import soot.jimple.toolkits.scalar.NopEliminator;
import soot.options.CGOptions;
import soot.options.Options;
import soot.rtlib.tamiflex.DefaultHandler;
import soot.rtlib.tamiflex.IUnexpectedReflectiveCallHandler;
import soot.rtlib.tamiflex.OpaquePredicate;
import soot.rtlib.tamiflex.ReflectiveCalls;
import soot.rtlib.tamiflex.SootSig;
import soot.rtlib.tamiflex.UnexpectedReflectiveCall;
import soot.toolkits.scalar.UnusedLocalEliminator;
import soot.util.Chain;
import soot.util.HashChain;

public class ReflectiveCallsInliner extends SceneTransformer {
   private final boolean useCaching = false;
   private static final String ALREADY_CHECKED_FIELDNAME = "SOOT$Reflection$alreadyChecked";
   private ReflectionTraceInfo RTI;
   private SootMethodRef UNINTERPRETED_METHOD;
   private boolean initialized = false;
   private int callSiteId;
   private int callNum;
   private SootClass reflectiveCallsClass;
   private static final List<String> fieldSets = Arrays.asList("set", "setBoolean", "setByte", "setChar", "setInt", "setLong", "setFloat", "setDouble", "setShort");
   private static final List<String> fieldGets = Arrays.asList("get", "getBoolean", "getByte", "getChar", "getInt", "getLong", "getFloat", "getDouble", "getShort");

   protected void internalTransform(String phaseName, Map options) {
      if (!this.initialized) {
         CGOptions cgOptions = new CGOptions(PhaseOptions.v().getPhaseOptions("cg"));
         String logFilePath = cgOptions.reflection_log();
         this.RTI = new ReflectionTraceInfo(logFilePath);
         Scene.v().getSootClass(SootSig.class.getName()).setApplicationClass();
         Scene.v().getSootClass(UnexpectedReflectiveCall.class.getName()).setApplicationClass();
         Scene.v().getSootClass(IUnexpectedReflectiveCallHandler.class.getName()).setApplicationClass();
         Scene.v().getSootClass(DefaultHandler.class.getName()).setApplicationClass();
         Scene.v().getSootClass(OpaquePredicate.class.getName()).setApplicationClass();
         Scene.v().getSootClass(ReflectiveCalls.class.getName()).setApplicationClass();
         this.reflectiveCallsClass = new SootClass("soot.rtlib.tamiflex.ReflectiveCallsWrapper", 1);
         Scene.v().addClass(this.reflectiveCallsClass);
         this.reflectiveCallsClass.setApplicationClass();
         this.UNINTERPRETED_METHOD = Scene.v().makeMethodRef(Scene.v().getSootClass("soot.rtlib.tamiflex.OpaquePredicate"), "getFalse", Collections.emptyList(), BooleanType.v(), true);
         this.initializeReflectiveCallsTable();
         this.callSiteId = 0;
         this.callNum = 0;
         this.initialized = true;
      }

      Body b;
      for(Iterator var7 = this.RTI.methodsContainingReflectiveCalls().iterator(); var7.hasNext(); this.cleanup(b)) {
         SootMethod m = (SootMethod)var7.next();
         m.retrieveActiveBody();
         b = m.getActiveBody();
         Set<String> fieldGetSignatures = this.RTI.classForNameClassNames(m);
         if (!fieldGetSignatures.isEmpty()) {
            this.inlineRelectiveCalls(m, fieldGetSignatures, ReflectionTraceInfo.Kind.ClassForName);
            if (Options.v().validate()) {
               b.validate();
            }
         }

         fieldGetSignatures = this.RTI.classNewInstanceClassNames(m);
         if (!fieldGetSignatures.isEmpty()) {
            this.inlineRelectiveCalls(m, fieldGetSignatures, ReflectionTraceInfo.Kind.ClassNewInstance);
            if (Options.v().validate()) {
               b.validate();
            }
         }

         fieldGetSignatures = this.RTI.constructorNewInstanceSignatures(m);
         if (!fieldGetSignatures.isEmpty()) {
            this.inlineRelectiveCalls(m, fieldGetSignatures, ReflectionTraceInfo.Kind.ConstructorNewInstance);
            if (Options.v().validate()) {
               b.validate();
            }
         }

         fieldGetSignatures = this.RTI.methodInvokeSignatures(m);
         if (!fieldGetSignatures.isEmpty()) {
            this.inlineRelectiveCalls(m, fieldGetSignatures, ReflectionTraceInfo.Kind.MethodInvoke);
            if (Options.v().validate()) {
               b.validate();
            }
         }

         fieldGetSignatures = this.RTI.fieldSetSignatures(m);
         if (!fieldGetSignatures.isEmpty()) {
            this.inlineRelectiveCalls(m, fieldGetSignatures, ReflectionTraceInfo.Kind.FieldSet);
            if (Options.v().validate()) {
               b.validate();
            }
         }

         fieldGetSignatures = this.RTI.fieldGetSignatures(m);
         if (!fieldGetSignatures.isEmpty()) {
            this.inlineRelectiveCalls(m, fieldGetSignatures, ReflectionTraceInfo.Kind.FieldGet);
            if (Options.v().validate()) {
               b.validate();
            }
         }
      }

   }

   private void cleanup(Body b) {
      CopyPropagator.v().transform(b);
      DeadAssignmentEliminator.v().transform(b);
      UnusedLocalEliminator.v().transform(b);
      NopEliminator.v().transform(b);
   }

   private void initializeReflectiveCallsTable() {
      int callSiteId = 0;
      SootClass reflCallsClass = Scene.v().getSootClass("soot.rtlib.tamiflex.ReflectiveCalls");
      SootMethod clinit = reflCallsClass.getMethodByName("<clinit>");
      Body body = clinit.retrieveActiveBody();
      PatchingChain<Unit> units = body.getUnits();
      LocalGenerator localGen = new LocalGenerator(body);
      Chain<Unit> newUnits = new HashChain();
      SootClass setClass = Scene.v().getSootClass("java.util.Set");
      SootMethodRef addMethodRef = setClass.getMethodByName("add").makeRef();
      Iterator var10 = this.RTI.methodsContainingReflectiveCalls().iterator();

      while(true) {
         SootMethod m;
         SootFieldRef fieldRef;
         Local setLocal;
         Iterator var14;
         String methodSig;
         InterfaceInvokeExpr invokeExpr;
         do {
            if (!var10.hasNext()) {
               Unit secondLastStmt = units.getPredOf(units.getLast());
               units.insertAfter((Chain)newUnits, (Unit)secondLastStmt);
               if (Options.v().validate()) {
                  body.validate();
               }

               return;
            }

            m = (SootMethod)var10.next();
            if (!this.RTI.classForNameClassNames(m).isEmpty()) {
               fieldRef = Scene.v().makeFieldRef(reflCallsClass, "classForName", RefType.v("java.util.Set"), true);
               setLocal = localGen.generateLocal(RefType.v("java.util.Set"));
               newUnits.add(Jimple.v().newAssignStmt(setLocal, Jimple.v().newStaticFieldRef(fieldRef)));
               var14 = this.RTI.classForNameClassNames(m).iterator();

               while(var14.hasNext()) {
                  methodSig = (String)var14.next();
                  invokeExpr = Jimple.v().newInterfaceInvokeExpr(setLocal, addMethodRef, (Value)StringConstant.v(callSiteId + methodSig));
                  newUnits.add(Jimple.v().newInvokeStmt(invokeExpr));
               }

               ++callSiteId;
            }

            if (!this.RTI.classNewInstanceClassNames(m).isEmpty()) {
               fieldRef = Scene.v().makeFieldRef(reflCallsClass, "classNewInstance", RefType.v("java.util.Set"), true);
               setLocal = localGen.generateLocal(RefType.v("java.util.Set"));
               newUnits.add(Jimple.v().newAssignStmt(setLocal, Jimple.v().newStaticFieldRef(fieldRef)));
               var14 = this.RTI.classNewInstanceClassNames(m).iterator();

               while(var14.hasNext()) {
                  methodSig = (String)var14.next();
                  invokeExpr = Jimple.v().newInterfaceInvokeExpr(setLocal, addMethodRef, (Value)StringConstant.v(callSiteId + methodSig));
                  newUnits.add(Jimple.v().newInvokeStmt(invokeExpr));
               }

               ++callSiteId;
            }

            if (!this.RTI.constructorNewInstanceSignatures(m).isEmpty()) {
               fieldRef = Scene.v().makeFieldRef(reflCallsClass, "constructorNewInstance", RefType.v("java.util.Set"), true);
               setLocal = localGen.generateLocal(RefType.v("java.util.Set"));
               newUnits.add(Jimple.v().newAssignStmt(setLocal, Jimple.v().newStaticFieldRef(fieldRef)));
               var14 = this.RTI.constructorNewInstanceSignatures(m).iterator();

               while(var14.hasNext()) {
                  methodSig = (String)var14.next();
                  invokeExpr = Jimple.v().newInterfaceInvokeExpr(setLocal, addMethodRef, (Value)StringConstant.v(callSiteId + methodSig));
                  newUnits.add(Jimple.v().newInvokeStmt(invokeExpr));
               }

               ++callSiteId;
            }
         } while(this.RTI.methodInvokeSignatures(m).isEmpty());

         fieldRef = Scene.v().makeFieldRef(reflCallsClass, "methodInvoke", RefType.v("java.util.Set"), true);
         setLocal = localGen.generateLocal(RefType.v("java.util.Set"));
         newUnits.add(Jimple.v().newAssignStmt(setLocal, Jimple.v().newStaticFieldRef(fieldRef)));
         var14 = this.RTI.methodInvokeSignatures(m).iterator();

         while(var14.hasNext()) {
            methodSig = (String)var14.next();
            invokeExpr = Jimple.v().newInterfaceInvokeExpr(setLocal, addMethodRef, (Value)StringConstant.v(callSiteId + methodSig));
            newUnits.add(Jimple.v().newInvokeStmt(invokeExpr));
         }

         ++callSiteId;
      }
   }

   private void addCaching() {
      SootClass method = Scene.v().getSootClass("java.lang.reflect.Method");
      method.addField(Scene.v().makeSootField("SOOT$Reflection$alreadyChecked", BooleanType.v()));
      SootClass constructor = Scene.v().getSootClass("java.lang.reflect.Constructor");
      constructor.addField(Scene.v().makeSootField("SOOT$Reflection$alreadyChecked", BooleanType.v()));
      SootClass clazz = Scene.v().getSootClass("java.lang.Class");
      clazz.addField(Scene.v().makeSootField("SOOT$Reflection$alreadyChecked", BooleanType.v()));
      ReflectionTraceInfo.Kind[] var4 = ReflectionTraceInfo.Kind.values();
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         ReflectionTraceInfo.Kind k = var4[var6];
         this.addCaching(k);
      }

   }

   private void addCaching(ReflectionTraceInfo.Kind kind) {
      SootClass c;
      String methodName;
      switch(kind) {
      case ClassNewInstance:
         c = Scene.v().getSootClass("java.lang.Class");
         methodName = "knownClassNewInstance";
         break;
      case ConstructorNewInstance:
         c = Scene.v().getSootClass("java.lang.reflect.Constructor");
         methodName = "knownConstructorNewInstance";
         break;
      case MethodInvoke:
         c = Scene.v().getSootClass("java.lang.reflect.Method");
         methodName = "knownMethodInvoke";
         break;
      case ClassForName:
         return;
      default:
         throw new IllegalStateException("unknown kind: " + kind);
      }

      SootClass reflCallsClass = Scene.v().getSootClass("soot.rtlib.tamiflex.ReflectiveCalls");
      SootMethod m = reflCallsClass.getMethodByName(methodName);
      JimpleBody body = (JimpleBody)m.retrieveActiveBody();
      LocalGenerator localGen = new LocalGenerator(body);
      Unit firstStmt = body.getFirstNonIdentityStmt();
      Unit firstStmt = body.getUnits().getPredOf((Unit)firstStmt);
      Stmt jumpTarget = Jimple.v().newNopStmt();
      Chain<Unit> newUnits = new HashChain();
      InstanceFieldRef fieldRef = Jimple.v().newInstanceFieldRef(body.getParameterLocal(m.getParameterCount() - 1), Scene.v().makeFieldRef(c, "SOOT$Reflection$alreadyChecked", BooleanType.v(), false));
      Local alreadyCheckedLocal = localGen.generateLocal(BooleanType.v());
      newUnits.add(Jimple.v().newAssignStmt(alreadyCheckedLocal, fieldRef));
      newUnits.add(Jimple.v().newIfStmt(Jimple.v().newEqExpr(alreadyCheckedLocal, IntConstant.v(0)), (Unit)jumpTarget));
      newUnits.add(Jimple.v().newReturnVoidStmt());
      newUnits.add(jumpTarget);
      InstanceFieldRef fieldRef2 = Jimple.v().newInstanceFieldRef(body.getParameterLocal(m.getParameterCount() - 1), Scene.v().makeFieldRef(c, "SOOT$Reflection$alreadyChecked", BooleanType.v(), false));
      newUnits.add(Jimple.v().newAssignStmt(fieldRef2, IntConstant.v(1)));
      body.getUnits().insertAfter((Chain)newUnits, (Unit)firstStmt);
      if (Options.v().validate()) {
         body.validate();
      }

   }

   private void inlineRelectiveCalls(SootMethod m, Set<String> targets, ReflectionTraceInfo.Kind callKind) {
      if (!m.hasActiveBody()) {
         m.retrieveActiveBody();
      }

      Body b = m.getActiveBody();
      PatchingChain<Unit> units = b.getUnits();
      Iterator<Unit> iter = units.snapshotIterator();
      LocalGenerator localGen = new LocalGenerator(b);

      while(true) {
         HashChain newUnits;
         Stmt s;
         InvokeExpr ie;
         boolean found;
         Type fieldSetGetType;
         do {
            do {
               if (!iter.hasNext()) {
                  ++this.callSiteId;
                  return;
               }

               newUnits = new HashChain();
               s = (Stmt)iter.next();
            } while(!s.containsInvokeExpr());

            ie = s.getInvokeExpr();
            found = false;
            fieldSetGetType = null;
            if (callKind != ReflectionTraceInfo.Kind.ClassForName || !ie.getMethodRef().getSignature().equals("<java.lang.Class: java.lang.Class forName(java.lang.String)>") && !ie.getMethodRef().getSignature().equals("<java.lang.Class: java.lang.Class forName(java.lang.String,boolean,java.lang.ClassLoader)>")) {
               Local methodLocal;
               if (callKind == ReflectionTraceInfo.Kind.ClassNewInstance && ie.getMethodRef().getSignature().equals("<java.lang.Class: java.lang.Object newInstance()>")) {
                  found = true;
                  methodLocal = (Local)((InstanceInvokeExpr)ie).getBase();
                  newUnits.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(Scene.v().getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownClassNewInstance(int,java.lang.Class)>").makeRef(), IntConstant.v(this.callSiteId), methodLocal)));
               } else if (callKind == ReflectionTraceInfo.Kind.ConstructorNewInstance && ie.getMethodRef().getSignature().equals("<java.lang.reflect.Constructor: java.lang.Object newInstance(java.lang.Object[])>")) {
                  found = true;
                  methodLocal = (Local)((InstanceInvokeExpr)ie).getBase();
                  newUnits.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(Scene.v().getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownConstructorNewInstance(int,java.lang.reflect.Constructor)>").makeRef(), IntConstant.v(this.callSiteId), methodLocal)));
               } else {
                  Value recv;
                  if (callKind == ReflectionTraceInfo.Kind.MethodInvoke && ie.getMethodRef().getSignature().equals("<java.lang.reflect.Method: java.lang.Object invoke(java.lang.Object,java.lang.Object[])>")) {
                     found = true;
                     methodLocal = (Local)((InstanceInvokeExpr)ie).getBase();
                     recv = ie.getArg(0);
                     newUnits.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(Scene.v().getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownMethodInvoke(int,java.lang.Object,java.lang.reflect.Method)>").makeRef(), IntConstant.v(this.callSiteId), recv, methodLocal)));
                  } else {
                     Value field;
                     SootMethod sootMethod;
                     if (callKind == ReflectionTraceInfo.Kind.FieldSet) {
                        sootMethod = ie.getMethodRef().resolve();
                        if (sootMethod.getDeclaringClass().getName().equals("java.lang.reflect.Field") && fieldSets.contains(sootMethod.getName())) {
                           found = true;
                           fieldSetGetType = sootMethod.getParameterType(1);
                           recv = ie.getArg(0);
                           field = ((InstanceInvokeExpr)ie).getBase();
                           newUnits.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(Scene.v().getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownFieldSet(int,java.lang.Object,java.lang.reflect.Field)>").makeRef(), IntConstant.v(this.callSiteId), recv, field)));
                        }
                     } else if (callKind == ReflectionTraceInfo.Kind.FieldGet) {
                        sootMethod = ie.getMethodRef().resolve();
                        if (sootMethod.getDeclaringClass().getName().equals("java.lang.reflect.Field") && fieldGets.contains(sootMethod.getName())) {
                           found = true;
                           fieldSetGetType = sootMethod.getReturnType();
                           recv = ie.getArg(0);
                           field = ((InstanceInvokeExpr)ie).getBase();
                           newUnits.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(Scene.v().getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownFieldSet(int,java.lang.Object,java.lang.reflect.Field)>").makeRef(), IntConstant.v(this.callSiteId), recv, field)));
                        }
                     }
                  }
               }
            } else {
               found = true;
               Value classNameValue = ie.getArg(0);
               newUnits.add(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(Scene.v().getMethod("<soot.rtlib.tamiflex.ReflectiveCalls: void knownClassForName(int,java.lang.String)>").makeRef(), IntConstant.v(this.callSiteId), classNameValue)));
            }
         } while(!found);

         NopStmt endLabel = Jimple.v().newNopStmt();
         Iterator var29 = targets.iterator();

         while(var29.hasNext()) {
            String target = (String)var29.next();
            NopStmt jumpTarget = Jimple.v().newNopStmt();
            Local predLocal = localGen.generateLocal(BooleanType.v());
            StaticInvokeExpr staticInvokeExpr = Jimple.v().newStaticInvokeExpr(this.UNINTERPRETED_METHOD);
            newUnits.add(Jimple.v().newAssignStmt(predLocal, staticInvokeExpr));
            newUnits.add(Jimple.v().newIfStmt(Jimple.v().newEqExpr(IntConstant.v(0), predLocal), (Unit)jumpTarget));
            SootMethod newMethod = this.createNewMethod(callKind, target, fieldSetGetType);
            List<Value> args = new LinkedList();
            switch(callKind) {
            case ClassNewInstance:
            case ClassForName:
               break;
            case ConstructorNewInstance:
               args.add((Value)ie.getArgs().get(0));
               break;
            case MethodInvoke:
               args.add((Value)ie.getArgs().get(0));
               args.add((Value)ie.getArgs().get(1));
               break;
            case FieldSet:
               args.add((Value)ie.getArgs().get(0));
               args.add((Value)ie.getArgs().get(1));
               break;
            case FieldGet:
               args.add((Value)ie.getArgs().get(0));
               break;
            default:
               throw new IllegalStateException();
            }

            StaticInvokeExpr methodInvokeExpr = Jimple.v().newStaticInvokeExpr(newMethod.makeRef(), (List)args);
            Local retLocal = localGen.generateLocal(newMethod.getReturnType());
            newUnits.add(Jimple.v().newAssignStmt(retLocal, methodInvokeExpr));
            if (s instanceof AssignStmt) {
               AssignStmt assignStmt = (AssignStmt)s;
               Value leftOp = assignStmt.getLeftOp();
               AssignStmt newAssignStmt = Jimple.v().newAssignStmt(leftOp, retLocal);
               newUnits.add(newAssignStmt);
            }

            GotoStmt gotoStmt = Jimple.v().newGotoStmt((Unit)endLabel);
            newUnits.add(gotoStmt);
            newUnits.add(jumpTarget);
         }

         Unit end = (Unit)newUnits.getLast();
         units.insertAfter((Chain)newUnits, (Unit)s);
         units.remove(s);
         units.insertAfter((Unit)s, (Unit)end);
         units.insertAfter((Unit)endLabel, (Unit)s);
      }
   }

   private SootMethod createNewMethod(ReflectionTraceInfo.Kind callKind, String target, Type fieldSetGetType) {
      List<Type> parameterTypes = new LinkedList();
      Type returnType = null;
      switch(callKind) {
      case ClassNewInstance:
         returnType = RefType.v("java.lang.Object");
         break;
      case ConstructorNewInstance:
         parameterTypes.add(ArrayType.v(RefType.v("java.lang.Object"), 1));
         returnType = RefType.v("java.lang.Object");
         break;
      case MethodInvoke:
         parameterTypes.add(RefType.v("java.lang.Object"));
         parameterTypes.add(ArrayType.v(RefType.v("java.lang.Object"), 1));
         returnType = RefType.v("java.lang.Object");
         break;
      case ClassForName:
         returnType = RefType.v("java.lang.Class");
         break;
      case FieldSet:
         parameterTypes.add(RefType.v("java.lang.Object"));
         parameterTypes.add(fieldSetGetType);
         returnType = VoidType.v();
         break;
      case FieldGet:
         parameterTypes.add(RefType.v("java.lang.Object"));
         returnType = fieldSetGetType;
         break;
      default:
         throw new IllegalStateException();
      }

      SootMethod newMethod = Scene.v().makeSootMethod("reflectiveCall" + this.callNum++, parameterTypes, (Type)returnType, 9);
      Body newBody = Jimple.v().newBody(newMethod);
      newMethod.setActiveBody(newBody);
      this.reflectiveCallsClass.addMethod(newMethod);
      PatchingChain<Unit> newUnits = newBody.getUnits();
      LocalGenerator localGen = new LocalGenerator(newBody);
      Value replacement = null;
      Local[] paramLocals = null;
      Local freshLocal;
      RefType objectType;
      Local retLocal;
      SootField field;
      Local boxedOrCasted;
      SootMethod method;
      RefType targetType;
      Local argArrayLocal;
      switch(callKind) {
      case ClassNewInstance:
         objectType = RefType.v(target);
         freshLocal = localGen.generateLocal(objectType);
         replacement = Jimple.v().newNewExpr(objectType);
         break;
      case ConstructorNewInstance:
         method = Scene.v().getMethod(target);
         paramLocals = new Local[method.getParameterCount()];
         if (method.getParameterCount() > 0) {
            ArrayType arrayType = ArrayType.v(RefType.v("java.lang.Object"), 1);
            argArrayLocal = localGen.generateLocal(arrayType);
            newUnits.add((Unit)Jimple.v().newIdentityStmt(argArrayLocal, Jimple.v().newParameterRef(arrayType, 0)));
            int i = 0;

            for(Iterator var29 = method.getParameterTypes().iterator(); var29.hasNext(); ++i) {
               Type paramType = (Type)var29.next();
               paramLocals[i] = localGen.generateLocal(paramType);
               this.unboxParameter(argArrayLocal, i, paramLocals, paramType, newUnits, localGen);
            }
         }

         targetType = method.getDeclaringClass().getType();
         freshLocal = localGen.generateLocal(targetType);
         replacement = Jimple.v().newNewExpr(targetType);
         break;
      case MethodInvoke:
         method = Scene.v().getMethod(target);
         targetType = RefType.v("java.lang.Object");
         argArrayLocal = localGen.generateLocal(targetType);
         newUnits.add((Unit)Jimple.v().newIdentityStmt(argArrayLocal, Jimple.v().newParameterRef(targetType, 0)));
         paramLocals = new Local[method.getParameterCount()];
         if (method.getParameterCount() > 0) {
            ArrayType arrayType = ArrayType.v(RefType.v("java.lang.Object"), 1);
            boxedOrCasted = localGen.generateLocal(arrayType);
            newUnits.add((Unit)Jimple.v().newIdentityStmt(boxedOrCasted, Jimple.v().newParameterRef(arrayType, 1)));
            int i = 0;

            for(Iterator var19 = method.getParameterTypes().iterator(); var19.hasNext(); ++i) {
               Type paramType = (Type)var19.next();
               paramLocals[i] = localGen.generateLocal(paramType);
               this.unboxParameter(boxedOrCasted, i, paramLocals, paramType, newUnits, localGen);
            }
         }

         RefType targetType = method.getDeclaringClass().getType();
         freshLocal = localGen.generateLocal(targetType);
         replacement = Jimple.v().newCastExpr(argArrayLocal, method.getDeclaringClass().getType());
         break;
      case ClassForName:
         freshLocal = localGen.generateLocal(RefType.v("java.lang.Class"));
         replacement = ClassConstant.v(target.replace('.', '/'));
         break;
      case FieldSet:
      case FieldGet:
         objectType = RefType.v("java.lang.Object");
         retLocal = localGen.generateLocal(objectType);
         newUnits.add((Unit)Jimple.v().newIdentityStmt(retLocal, Jimple.v().newParameterRef(objectType, 0)));
         field = Scene.v().getField(target);
         freshLocal = localGen.generateLocal(field.getDeclaringClass().getType());
         replacement = Jimple.v().newCastExpr(retLocal, field.getDeclaringClass().getType());
         break;
      default:
         throw new InternalError("Unknown kind of reflective call " + callKind);
      }

      AssignStmt replStmt = Jimple.v().newAssignStmt(freshLocal, (Value)replacement);
      newUnits.add((Unit)replStmt);
      retLocal = localGen.generateLocal((Type)returnType);
      SootMethod method;
      InvokeStmt invokeStmt;
      SpecialInvokeExpr constrCallExpr;
      switch(callKind) {
      case ClassNewInstance:
         SootClass targetClass = Scene.v().getSootClass(target);
         constrCallExpr = Jimple.v().newSpecialInvokeExpr(freshLocal, Scene.v().makeMethodRef(targetClass, "<init>", Collections.emptyList(), VoidType.v(), false));
         invokeStmt = Jimple.v().newInvokeStmt(constrCallExpr);
         newUnits.add((Unit)invokeStmt);
         newUnits.add((Unit)Jimple.v().newAssignStmt(retLocal, freshLocal));
         break;
      case ConstructorNewInstance:
         method = Scene.v().getMethod(target);
         constrCallExpr = Jimple.v().newSpecialInvokeExpr(freshLocal, method.makeRef(), Arrays.asList(paramLocals));
         invokeStmt = Jimple.v().newInvokeStmt(constrCallExpr);
         newUnits.add((Unit)invokeStmt);
         newUnits.add((Unit)Jimple.v().newAssignStmt(retLocal, freshLocal));
         break;
      case MethodInvoke:
         method = Scene.v().getMethod(target);
         Object invokeExpr;
         if (method.isStatic()) {
            invokeExpr = Jimple.v().newStaticInvokeExpr(method.makeRef(), Arrays.asList(paramLocals));
         } else {
            invokeExpr = Jimple.v().newVirtualInvokeExpr(freshLocal, method.makeRef(), Arrays.asList(paramLocals));
         }

         if (method.getReturnType().equals(VoidType.v())) {
            invokeStmt = Jimple.v().newInvokeStmt((Value)invokeExpr);
            newUnits.add((Unit)invokeStmt);
            AssignStmt assignStmt = Jimple.v().newAssignStmt(retLocal, NullConstant.v());
            newUnits.add((Unit)assignStmt);
         } else {
            AssignStmt assignStmt = Jimple.v().newAssignStmt(retLocal, (Value)invokeExpr);
            newUnits.add((Unit)assignStmt);
         }
         break;
      case ClassForName:
         newUnits.add((Unit)Jimple.v().newAssignStmt(retLocal, freshLocal));
         break;
      case FieldSet:
         argArrayLocal = localGen.generateLocal(fieldSetGetType);
         newUnits.insertBeforeNoRedirect(Jimple.v().newIdentityStmt(argArrayLocal, Jimple.v().newParameterRef(fieldSetGetType, 1)), replStmt);
         SootField field = Scene.v().getField(target);
         boxedOrCasted = localGen.generateLocal(field.getType());
         this.insertCastOrUnboxingCode(boxedOrCasted, argArrayLocal, newUnits);
         Object fieldRef;
         if (field.isStatic()) {
            fieldRef = Jimple.v().newStaticFieldRef(field.makeRef());
         } else {
            fieldRef = Jimple.v().newInstanceFieldRef(freshLocal, field.makeRef());
         }

         newUnits.add((Unit)Jimple.v().newAssignStmt((Value)fieldRef, boxedOrCasted));
         break;
      case FieldGet:
         field = Scene.v().getField(target);
         Local value = localGen.generateLocal(field.getType());
         Object fieldRef;
         if (field.isStatic()) {
            fieldRef = Jimple.v().newStaticFieldRef(field.makeRef());
         } else {
            fieldRef = Jimple.v().newInstanceFieldRef(freshLocal, field.makeRef());
         }

         newUnits.add((Unit)Jimple.v().newAssignStmt(value, (Value)fieldRef));
         this.insertCastOrBoxingCode(retLocal, value, newUnits);
      }

      if (!returnType.equals(VoidType.v())) {
         newUnits.add((Unit)Jimple.v().newReturnStmt(retLocal));
      }

      if (Options.v().validate()) {
         newBody.validate();
      }

      this.cleanup(newBody);
      return newMethod;
   }

   private void insertCastOrUnboxingCode(Local lhs, Local rhs, Chain<Unit> newUnits) {
      if (lhs.getType() instanceof PrimType) {
         if (rhs.getType() instanceof PrimType) {
            newUnits.add(Jimple.v().newAssignStmt(lhs, Jimple.v().newCastExpr(rhs, lhs.getType())));
         } else {
            RefType boxedType = (RefType)rhs.getType();
            SootMethodRef ref = Scene.v().makeMethodRef(boxedType.getSootClass(), lhs.getType().toString() + "Value", Collections.emptyList(), lhs.getType(), false);
            newUnits.add(Jimple.v().newAssignStmt(lhs, Jimple.v().newVirtualInvokeExpr(rhs, ref)));
         }
      }

   }

   private void insertCastOrBoxingCode(Local lhs, Local rhs, Chain<Unit> newUnits) {
      if (lhs.getType() instanceof RefLikeType) {
         if (rhs.getType() instanceof RefLikeType) {
            newUnits.add(Jimple.v().newAssignStmt(lhs, Jimple.v().newCastExpr(rhs, lhs.getType())));
         } else {
            RefType boxedType = ((PrimType)rhs.getType()).boxedType();
            SootMethodRef ref = Scene.v().makeMethodRef(boxedType.getSootClass(), "valueOf", Collections.singletonList(rhs.getType()), boxedType, true);
            newUnits.add(Jimple.v().newAssignStmt(lhs, Jimple.v().newStaticInvokeExpr(ref, (Value)rhs)));
         }
      }

   }

   private void unboxParameter(Local argsArrayLocal, int paramIndex, Local[] paramLocals, Type paramType, Chain<Unit> newUnits, LocalGenerator localGen) {
      ArrayRef arrayRef = Jimple.v().newArrayRef(argsArrayLocal, IntConstant.v(paramIndex));
      AssignStmt assignStmt;
      if (paramType instanceof PrimType) {
         PrimType primType = (PrimType)paramType;
         RefType boxedType = primType.boxedType();
         SootMethodRef ref = Scene.v().makeMethodRef(boxedType.getSootClass(), paramType + "Value", Collections.emptyList(), paramType, false);
         Local boxedLocal = localGen.generateLocal(RefType.v("java.lang.Object"));
         AssignStmt arrayLoad = Jimple.v().newAssignStmt(boxedLocal, arrayRef);
         newUnits.add(arrayLoad);
         Local castedLocal = localGen.generateLocal(boxedType);
         AssignStmt cast = Jimple.v().newAssignStmt(castedLocal, Jimple.v().newCastExpr(boxedLocal, boxedType));
         newUnits.add(cast);
         VirtualInvokeExpr unboxInvokeExpr = Jimple.v().newVirtualInvokeExpr(castedLocal, ref);
         assignStmt = Jimple.v().newAssignStmt(paramLocals[paramIndex], unboxInvokeExpr);
      } else {
         Local boxedLocal = localGen.generateLocal(RefType.v("java.lang.Object"));
         AssignStmt arrayLoad = Jimple.v().newAssignStmt(boxedLocal, arrayRef);
         newUnits.add(arrayLoad);
         Local castedLocal = localGen.generateLocal(paramType);
         AssignStmt cast = Jimple.v().newAssignStmt(castedLocal, Jimple.v().newCastExpr(boxedLocal, paramType));
         newUnits.add(cast);
         assignStmt = Jimple.v().newAssignStmt(paramLocals[paramIndex], castedLocal);
      }

      newUnits.add(assignStmt);
   }
}
