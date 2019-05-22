package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.ClassMember;
import soot.DoubleType;
import soot.FastHierarchy;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jbco.IJbcoTransform;
import soot.jbco.util.BodyBuilder;
import soot.jbco.util.Rand;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.VirtualInvokeExpr;
import soot.util.Chain;

public class LibraryMethodWrappersBuilder extends SceneTransformer implements IJbcoTransform {
   private static final Logger logger = LoggerFactory.getLogger(LibraryMethodWrappersBuilder.class);
   public static final String name = "wjtp.jbco_blbc";
   public static final String[] dependencies = new String[]{"wjtp.jbco_blbc"};
   private static final Map<SootClass, Map<SootMethod, SootMethodRef>> libClassesToMethods = new HashMap();
   public static List<SootMethod> builtByMe = new ArrayList();
   private int newmethods = 0;
   private int methodcalls = 0;

   public String[] getDependencies() {
      return (String[])Arrays.copyOf(dependencies, dependencies.length);
   }

   public String getName() {
      return "wjtp.jbco_blbc";
   }

   public void outputSummary() {
      logger.info((String)"Created {} new methods. Replaced {} method calls.", (Object)this.newmethods, (Object)this.methodcalls);
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      if (this.isVerbose()) {
         logger.info("Building Library Wrapper Methods...");
      }

      BodyBuilder.retrieveAllBodies();
      Iterator applicationClassesIterator = Scene.v().getApplicationClasses().snapshotIterator();

      label103:
      while(applicationClassesIterator.hasNext()) {
         SootClass applicationClass = (SootClass)applicationClassesIterator.next();
         if (this.isVerbose()) {
            logger.info((String)"\tProcessing class {}", (Object)applicationClass.getName());
         }

         List<SootMethod> methods = new ArrayList(applicationClass.getMethods());
         Iterator var6 = methods.iterator();

         while(true) {
            Body body;
            do {
               SootMethod method;
               do {
                  do {
                     if (!var6.hasNext()) {
                        continue label103;
                     }

                     method = (SootMethod)var6.next();
                  } while(!method.isConcrete());
               } while(builtByMe.contains(method));

               body = getBodySafely(method);
            } while(body == null);

            int localName = 0;
            Unit first = getFirstNotIdentityStmt(body);
            Iterator unitIterator = body.getUnits().snapshotIterator();

            label99:
            while(unitIterator.hasNext()) {
               Unit unit = (Unit)unitIterator.next();
               Iterator var13 = unit.getUseBoxes().iterator();

               while(true) {
                  ValueBox valueBox;
                  InvokeExpr invokeExpr;
                  SootMethod invokedMethod;
                  do {
                     Value value;
                     do {
                        do {
                           if (!var13.hasNext()) {
                              continue label99;
                           }

                           valueBox = (ValueBox)var13.next();
                           value = valueBox.getValue();
                        } while(!(value instanceof InvokeExpr));
                     } while(value instanceof SpecialInvokeExpr);

                     invokeExpr = (InvokeExpr)value;
                     invokedMethod = getMethodSafely(invokeExpr);
                  } while(invokedMethod == null);

                  SootMethodRef invokedMethodRef = this.getNewMethodRef(invokedMethod);
                  if (invokedMethodRef == null) {
                     invokedMethodRef = this.buildNewMethod(applicationClass, invokedMethod, invokeExpr);
                     this.setNewMethodRef(invokedMethod, invokedMethodRef);
                     ++this.newmethods;
                  }

                  if (this.isVerbose()) {
                     logger.info("\t\t\tChanging {} to {}\tUnit: ", invokedMethod.getSignature(), invokedMethodRef.getSignature(), unit);
                  }

                  List<Value> args = invokeExpr.getArgs();
                  List<Type> parameterTypes = invokedMethodRef.parameterTypes();
                  int argsCount = args.size();
                  int paramCount = parameterTypes.size();
                  if (invokeExpr instanceof InstanceInvokeExpr || invokeExpr instanceof StaticInvokeExpr) {
                     if (invokeExpr instanceof InstanceInvokeExpr) {
                        ++argsCount;
                        args.add(((InstanceInvokeExpr)invokeExpr).getBase());
                     }

                     while(argsCount < paramCount) {
                        Type pType = (Type)parameterTypes.get(argsCount);
                        Local newLocal = Jimple.v().newLocal("newLocal" + localName++, pType);
                        body.getLocals().add(newLocal);
                        body.getUnits().insertBeforeNoRedirect(Jimple.v().newAssignStmt(newLocal, getConstantType(pType)), first);
                        args.add(newLocal);
                        ++argsCount;
                     }

                     valueBox.setValue(Jimple.v().newStaticInvokeExpr(invokedMethodRef, args));
                  }

                  ++this.methodcalls;
               }
            }
         }
      }

      Scene.v().releaseActiveHierarchy();
      Scene.v().setFastHierarchy(new FastHierarchy());
   }

   private SootMethodRef getNewMethodRef(SootMethod method) {
      Map<SootMethod, SootMethodRef> methods = (Map)libClassesToMethods.computeIfAbsent(method.getDeclaringClass(), (key) -> {
         return new HashMap();
      });
      return (SootMethodRef)methods.get(method);
   }

   private void setNewMethodRef(SootMethod sm, SootMethodRef smr) {
      Map<SootMethod, SootMethodRef> methods = (Map)libClassesToMethods.computeIfAbsent(sm.getDeclaringClass(), (key) -> {
         return new HashMap();
      });
      methods.put(sm, smr);
   }

   private SootMethodRef buildNewMethod(SootClass fromC, SootMethod sm, InvokeExpr origIE) {
      List<SootClass> availableClasses = getVisibleApplicationClasses(sm);
      int classCount = availableClasses.size();
      if (classCount == 0) {
         throw new RuntimeException("There appears to be no public non-interface Application classes!");
      } else {
         SootClass randomClass;
         String methodNewName;
         do {
            int index = Rand.getInt(classCount);
            if ((randomClass = (SootClass)availableClasses.get(index)) == fromC && classCount > 1) {
               index = Rand.getInt(classCount);
               randomClass = (SootClass)availableClasses.get(index);
            }

            List<SootMethod> methods = randomClass.getMethods();
            index = Rand.getInt(methods.size());
            SootMethod randMethod = (SootMethod)methods.get(index);
            methodNewName = randMethod.getName();
         } while(methodNewName.equals("<init>") || methodNewName.equals("<clinit>"));

         List<Type> smParamTypes = new ArrayList(sm.getParameterTypes());
         if (!sm.isStatic()) {
            smParamTypes.add(sm.getDeclaringClass().getType());
         }

         int extraParams = 0;
         int rtmp;
         if (randomClass.declaresMethod(methodNewName, smParamTypes)) {
            rtmp = Rand.getInt(classCount + 7);
            if (rtmp >= classCount) {
               rtmp -= classCount;
               smParamTypes.add(getPrimType(rtmp));
            } else {
               smParamTypes.add(((SootClass)availableClasses.get(rtmp)).getType());
            }

            ++extraParams;
         }

         rtmp = (sm.getModifiers() | 8 | 1) & 'ﯿ' & '\ufeff' & '\uffdf';
         SootMethod newMethod = Scene.v().makeSootMethod(methodNewName, smParamTypes, sm.getReturnType(), rtmp);
         randomClass.addMethod(newMethod);
         JimpleBody body = Jimple.v().newBody(newMethod);
         newMethod.setActiveBody(body);
         Chain<Local> locals = body.getLocals();
         PatchingChain<Unit> units = body.getUnits();
         List args = BodyBuilder.buildParameterLocals(units, locals, smParamTypes);

         while(extraParams-- > 0) {
            args.remove(args.size() - 1);
         }

         InvokeExpr ie = null;
         Local libObj;
         if (sm.isStatic()) {
            ie = Jimple.v().newStaticInvokeExpr(sm.makeRef(), args);
         } else {
            libObj = (Local)args.remove(args.size() - 1);
            if (origIE instanceof InterfaceInvokeExpr) {
               ie = Jimple.v().newInterfaceInvokeExpr(libObj, sm.makeRef(), args);
            } else if (origIE instanceof VirtualInvokeExpr) {
               ie = Jimple.v().newVirtualInvokeExpr(libObj, sm.makeRef(), args);
            }
         }

         if (sm.getReturnType() instanceof VoidType) {
            units.add((Unit)Jimple.v().newInvokeStmt((Value)ie));
            units.add((Unit)Jimple.v().newReturnVoidStmt());
         } else {
            libObj = Jimple.v().newLocal("returnValue", sm.getReturnType());
            locals.add(libObj);
            units.add((Unit)Jimple.v().newAssignStmt(libObj, (Value)ie));
            units.add((Unit)Jimple.v().newReturnStmt(libObj));
         }

         if (this.isVerbose()) {
            logger.info("{} was replaced by {} which calls {}", sm.getName(), newMethod.getName(), ie);
         }

         if (units.size() < 2) {
            logger.warn((String)"THERE AREN'T MANY UNITS IN THIS METHOD {}", (Object)units);
         }

         builtByMe.add(newMethod);
         return newMethod.makeRef();
      }
   }

   private static Type getPrimType(int idx) {
      switch(idx) {
      case 0:
         return IntType.v();
      case 1:
         return CharType.v();
      case 2:
         return ByteType.v();
      case 3:
         return LongType.v();
      case 4:
         return BooleanType.v();
      case 5:
         return DoubleType.v();
      case 6:
         return FloatType.v();
      default:
         return IntType.v();
      }
   }

   private static Value getConstantType(Type t) {
      if (t instanceof BooleanType) {
         return IntConstant.v(Rand.getInt(1));
      } else if (t instanceof IntType) {
         return IntConstant.v(Rand.getInt());
      } else if (t instanceof CharType) {
         return Jimple.v().newCastExpr(IntConstant.v(Rand.getInt()), CharType.v());
      } else if (t instanceof ByteType) {
         return Jimple.v().newCastExpr(IntConstant.v(Rand.getInt()), ByteType.v());
      } else if (t instanceof LongType) {
         return LongConstant.v(Rand.getLong());
      } else if (t instanceof FloatType) {
         return FloatConstant.v(Rand.getFloat());
      } else {
         return (Value)(t instanceof DoubleType ? DoubleConstant.v(Rand.getDouble()) : Jimple.v().newCastExpr(NullConstant.v(), t));
      }
   }

   private static Body getBodySafely(SootMethod method) {
      try {
         return method.getActiveBody();
      } catch (Exception var2) {
         logger.warn((String)"Getting Body from SootMethod {} caused exception that was suppressed.", (Throwable)var2);
         return method.retrieveActiveBody();
      }
   }

   private static Unit getFirstNotIdentityStmt(Body body) {
      Iterator unitIterator = body.getUnits().snapshotIterator();

      Unit unit;
      do {
         if (!unitIterator.hasNext()) {
            logger.debug("There are no non-identity units in the method body.");
            return null;
         }

         unit = (Unit)unitIterator.next();
      } while(unit instanceof IdentityStmt);

      return unit;
   }

   private static SootMethod getMethodSafely(InvokeExpr invokeExpr) {
      try {
         SootMethod invokedMethod = invokeExpr.getMethod();
         if (invokedMethod == null) {
            return null;
         } else if (!"<init>".equals(invokedMethod.getName()) && !"<clinit>".equals(invokedMethod.getName())) {
            SootClass invokedMethodClass = invokedMethod.getDeclaringClass();
            if (!invokedMethodClass.isLibraryClass()) {
               logger.debug((String)"Skipping wrapping method {} as it is not library one.", (Object)invokedMethod);
               return null;
            } else if (invokeExpr.getMethodRef().declaringClass().isInterface() && !invokedMethodClass.isInterface()) {
               logger.debug("Skipping wrapping method {} as original code suppose to execute it on interface {} but resolved code trying to execute it on class {}", invokedMethod, invokeExpr.getMethodRef().declaringClass(), invokedMethodClass);
               return null;
            } else {
               return invokedMethod;
            }
         } else {
            logger.debug((String)"Skipping wrapping method {} as it is constructor/initializer.", (Object)invokedMethod);
            return null;
         }
      } catch (RuntimeException var3) {
         logger.debug((String)("Cannot resolve method of InvokeExpr: " + invokeExpr.toString()), (Throwable)var3);
         return null;
      }
   }

   private static List<SootClass> getVisibleApplicationClasses(SootMethod visibleBy) {
      List<SootClass> result = new ArrayList();
      Iterator applicationClassesIterator = Scene.v().getApplicationClasses().snapshotIterator();

      while(applicationClassesIterator.hasNext()) {
         SootClass applicationClass = (SootClass)applicationClassesIterator.next();
         if (applicationClass.isConcrete() && !applicationClass.isInterface() && applicationClass.isPublic() && Scene.v().getActiveHierarchy().isVisible(applicationClass, (ClassMember)visibleBy)) {
            result.add(applicationClass);
         }
      }

      return result;
   }
}
