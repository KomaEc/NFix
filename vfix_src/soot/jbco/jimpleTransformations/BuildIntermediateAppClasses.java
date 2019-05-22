package soot.jbco.jimpleTransformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import soot.Body;
import soot.ClassMember;
import soot.FastHierarchy;
import soot.Hierarchy;
import soot.Local;
import soot.PatchingChain;
import soot.PrimType;
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
import soot.jbco.Main;
import soot.jbco.util.BodyBuilder;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.ThisRef;
import soot.util.Chain;

public class BuildIntermediateAppClasses extends SceneTransformer implements IJbcoTransform {
   private static int newclasses = 0;
   private static int newmethods = 0;
   public static String[] dependancies = new String[]{"wjtp.jbco_bapibm"};
   public static String name = "wjtp.jbco_bapibm";

   public void outputSummary() {
      out.println("New buffer classes created: " + newclasses);
      out.println("New buffer class methods created: " + newmethods);
   }

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   protected void internalTransform(String phaseName, Map<String, String> options) {
      if (output) {
         out.println("Building Intermediate Classes...");
      }

      BodyBuilder.retrieveAllBodies();
      Iterator it = Scene.v().getApplicationClasses().snapshotIterator();

      while(true) {
         ArrayList initMethodsToRewrite;
         HashMap methodsToAdd;
         SootClass sc;
         SootClass originalSuperclass;
         label107:
         do {
            if (!it.hasNext()) {
               newclasses = Main.IntermediateAppClasses.size();
               Scene.v().releaseActiveHierarchy();
               Scene.v().getActiveHierarchy();
               Scene.v().setFastHierarchy(new FastHierarchy());
               return;
            }

            initMethodsToRewrite = new ArrayList();
            methodsToAdd = new HashMap();
            sc = (SootClass)it.next();
            originalSuperclass = sc.getSuperclass();
            if (output) {
               out.println("Processing " + sc.getName() + " with super " + originalSuperclass.getName());
            }

            Iterator methodIterator = sc.methodIterator();

            while(true) {
               SootMethod method;
               String subSig;
               do {
                  do {
                     if (!methodIterator.hasNext()) {
                        continue label107;
                     }

                     method = (SootMethod)methodIterator.next();
                  } while(!method.isConcrete());

                  try {
                     method.getActiveBody();
                  } catch (Exception var29) {
                     if (method.retrieveActiveBody() == null) {
                        throw new RuntimeException(method.getSignature() + " has no body. This was not expected dude.");
                     }
                  }

                  subSig = method.getSubSignature();
               } while(subSig.equals("void main(java.lang.String[])") && method.isPublic() && method.isStatic());

               if (subSig.indexOf("init>(") > 0) {
                  if (subSig.startsWith("void <init>(")) {
                     initMethodsToRewrite.add(method);
                  }
               } else {
                  Scene.v().releaseActiveHierarchy();
                  this.findAccessibleInSuperClassesBySubSig(sc, subSig).ifPresent((m) -> {
                     SootMethod var10000 = (SootMethod)methodsToAdd.put(subSig, m);
                  });
               }
            }
         } while(methodsToAdd.size() <= 0);

         String fullName = ClassRenamer.v().getNewName(ClassRenamer.getPackageName(sc.getName()), (String)null);
         if (output) {
            out.println("\tBuilding " + fullName);
         }

         SootClass mediatingClass = new SootClass(fullName, sc.getModifiers() & -17);
         Main.IntermediateAppClasses.add(mediatingClass);
         mediatingClass.setSuperclass(originalSuperclass);
         Scene.v().addClass(mediatingClass);
         mediatingClass.setApplicationClass();
         mediatingClass.setInScene(true);
         ThisRef thisRef = new ThisRef(mediatingClass.getType());

         for(Iterator var12 = methodsToAdd.keySet().iterator(); var12.hasNext(); ++newmethods) {
            String subSig = (String)var12.next();
            SootMethod originalSuperclassMethod = (SootMethod)methodsToAdd.get(subSig);
            List<Type> paramTypes = originalSuperclassMethod.getParameterTypes();
            Type returnType = originalSuperclassMethod.getReturnType();
            List<SootClass> exceptions = originalSuperclassMethod.getExceptions();
            int modifiers = originalSuperclassMethod.getModifiers() & -1025 & -257;
            String newMethodName = MethodRenamer.v().getNewName();
            SootMethod newMethod = Scene.v().makeSootMethod(newMethodName, paramTypes, returnType, modifiers, exceptions);
            mediatingClass.addMethod(newMethod);
            Body body = Jimple.v().newBody(newMethod);
            newMethod.setActiveBody(body);
            Chain<Local> locals = body.getLocals();
            PatchingChain<Unit> units = body.getUnits();
            BodyBuilder.buildThisLocal(units, thisRef, locals);
            BodyBuilder.buildParameterLocals(units, locals, paramTypes);
            if (returnType instanceof VoidType) {
               units.add((Unit)Jimple.v().newReturnVoidStmt());
            } else if (returnType instanceof PrimType) {
               units.add((Unit)Jimple.v().newReturnStmt(IntConstant.v(0)));
            } else {
               units.add((Unit)Jimple.v().newReturnStmt(NullConstant.v()));
            }

            ++newmethods;
            newMethod = Scene.v().makeSootMethod(originalSuperclassMethod.getName(), paramTypes, returnType, modifiers, exceptions);
            mediatingClass.addMethod(newMethod);
            Body body = Jimple.v().newBody(newMethod);
            newMethod.setActiveBody(body);
            Chain<Local> locals = body.getLocals();
            PatchingChain<Unit> units = body.getUnits();
            Local ths = BodyBuilder.buildThisLocal(units, thisRef, locals);
            List<Local> args = BodyBuilder.buildParameterLocals(units, locals, paramTypes);
            SootMethodRef superclassMethodRef = originalSuperclassMethod.makeRef();
            if (returnType instanceof VoidType) {
               units.add((Unit)Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(ths, superclassMethodRef, args)));
               units.add((Unit)Jimple.v().newReturnVoidStmt());
            } else {
               Local loc = Jimple.v().newLocal("retValue", returnType);
               body.getLocals().add(loc);
               units.add((Unit)Jimple.v().newAssignStmt(loc, Jimple.v().newSpecialInvokeExpr(ths, superclassMethodRef, args)));
               units.add((Unit)Jimple.v().newReturnStmt(loc));
            }
         }

         sc.setSuperclass(mediatingClass);
         int i = initMethodsToRewrite.size();

         while(i-- > 0) {
            SootMethod im = (SootMethod)initMethodsToRewrite.remove(i);
            Body b = im.getActiveBody();
            Local thisLocal = b.getThisLocal();
            Iterator uIt = b.getUnits().snapshotIterator();

            while(uIt.hasNext()) {
               Iterator var37 = ((Unit)uIt.next()).getUseBoxes().iterator();

               while(var37.hasNext()) {
                  ValueBox valueBox = (ValueBox)var37.next();
                  Value v = valueBox.getValue();
                  if (v instanceof SpecialInvokeExpr) {
                     SpecialInvokeExpr sie = (SpecialInvokeExpr)v;
                     SootMethodRef smr = sie.getMethodRef();
                     if (sie.getBase().equivTo(thisLocal) && smr.declaringClass().getName().equals(originalSuperclass.getName()) && smr.getSubSignature().getString().startsWith("void <init>")) {
                        SootMethod newSuperInit;
                        if (!mediatingClass.declaresMethod("<init>", smr.parameterTypes())) {
                           List<Type> paramTypes = smr.parameterTypes();
                           newSuperInit = Scene.v().makeSootMethod("<init>", paramTypes, smr.returnType());
                           mediatingClass.addMethod(newSuperInit);
                           JimpleBody body = Jimple.v().newBody(newSuperInit);
                           newSuperInit.setActiveBody(body);
                           PatchingChain<Unit> initUnits = body.getUnits();
                           Collection<Local> locals = body.getLocals();
                           Local ths = BodyBuilder.buildThisLocal(initUnits, thisRef, locals);
                           List<Local> args = BodyBuilder.buildParameterLocals(initUnits, locals, paramTypes);
                           initUnits.add((Unit)Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(ths, smr, args)));
                           initUnits.add((Unit)Jimple.v().newReturnVoidStmt());
                        } else {
                           newSuperInit = mediatingClass.getMethod("<init>", smr.parameterTypes());
                        }

                        sie.setMethodRef(newSuperInit.makeRef());
                     }
                  }
               }
            }
         }
      }
   }

   private Optional<SootMethod> findAccessibleInSuperClassesBySubSig(SootClass base, String subSig) {
      Hierarchy hierarchy = Scene.v().getActiveHierarchy();
      Iterator var4 = hierarchy.getSuperclassesOfIncluding(base.getSuperclass()).iterator();

      while(var4.hasNext()) {
         SootClass superClass = (SootClass)var4.next();
         if (superClass.isLibraryClass() && superClass.declaresMethod(subSig)) {
            SootMethod method = superClass.getMethod(subSig);
            if (hierarchy.isVisible(base, (ClassMember)method)) {
               return Optional.of(method);
            }
         }
      }

      return Optional.empty();
   }
}
