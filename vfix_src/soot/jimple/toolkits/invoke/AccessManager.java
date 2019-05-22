package soot.jimple.toolkits.invoke;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.Body;
import soot.ClassMember;
import soot.Hierarchy;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.util.Chain;

public class AccessManager {
   public static boolean isAccessLegal(SootMethod container, ClassMember target) {
      SootClass targetClass = target.getDeclaringClass();
      SootClass containerClass = container.getDeclaringClass();
      if (!isAccessLegal(container, targetClass)) {
         return false;
      } else if (target.isPrivate() && !targetClass.getName().equals(containerClass.getName())) {
         return false;
      } else if (!target.isPrivate() && !target.isProtected() && !target.isPublic() && !targetClass.getPackageName().equals(containerClass.getPackageName())) {
         return false;
      } else if (target.isProtected()) {
         Hierarchy h = Scene.v().getActiveHierarchy();
         return h.isClassSuperclassOfIncluding(targetClass, containerClass);
      } else {
         return true;
      }
   }

   public static boolean isAccessLegal(SootMethod container, SootClass target) {
      return target.isPublic() || container.getDeclaringClass().getPackageName().equals(target.getPackageName());
   }

   public static boolean isAccessLegal(SootMethod container, Stmt stmt) {
      if (stmt.containsInvokeExpr()) {
         return isAccessLegal(container, (ClassMember)stmt.getInvokeExpr().getMethod());
      } else {
         if (stmt instanceof AssignStmt) {
            AssignStmt as = (AssignStmt)stmt;
            FieldRef r;
            if (as.getRightOp() instanceof FieldRef) {
               r = (FieldRef)as.getRightOp();
               return isAccessLegal(container, (ClassMember)r.getField());
            }

            if (as.getLeftOp() instanceof FieldRef) {
               r = (FieldRef)as.getLeftOp();
               return isAccessLegal(container, (ClassMember)r.getField());
            }
         }

         return true;
      }
   }

   public static void createAccessorMethods(Body body, Stmt before, Stmt after) {
      Chain units = body.getUnits();
      if (before != null && !units.contains(before)) {
         throw new RuntimeException();
      } else if (after != null && !units.contains(after)) {
         throw new RuntimeException();
      } else {
         ArrayList<Unit> unitList = new ArrayList();
         unitList.addAll(units);
         boolean bInside = before == null;
         Iterator var6 = unitList.iterator();

         while(var6.hasNext()) {
            Unit unit = (Unit)var6.next();
            Stmt s = (Stmt)unit;
            if (bInside) {
               if (s == after) {
                  return;
               }

               if (!isAccessLegal(body.getMethod(), s)) {
                  createAccessorMethod(body.getMethod(), s);
               }
            } else if (s == before) {
               bInside = true;
            }
         }

      }
   }

   public static String createAccessorName(ClassMember member, boolean setter) {
      SootClass target = member.getDeclaringClass();
      String name = "access$";
      if (member instanceof SootField) {
         SootField f = (SootField)member;
         if (setter) {
            name = name + "set$";
         } else {
            name = name + "get$";
         }

         name = name + f.getName();
      } else {
         SootMethod m = (SootMethod)member;
         name = name + m.getName() + "$";

         Type type;
         for(Iterator it = m.getParameterTypes().iterator(); it.hasNext(); name = name + type.toString().replaceAll("\\.", "\\$\\$") + "$") {
            type = (Type)it.next();
         }
      }

      return name;
   }

   public static void createAccessorMethod(SootMethod container, Stmt stmt) {
      Body containerBody = container.getActiveBody();
      Chain containerStmts = containerBody.getUnits();
      if (!containerStmts.contains(stmt)) {
         throw new RuntimeException();
      } else {
         if (stmt.containsInvokeExpr()) {
            createInvokeAccessor(container, stmt);
         } else {
            if (!(stmt instanceof AssignStmt)) {
               throw new RuntimeException("Expected class member access");
            }

            AssignStmt as = (AssignStmt)stmt;
            FieldRef ref;
            if (as.getLeftOp() instanceof FieldRef) {
               ref = (FieldRef)as.getLeftOp();
               createSetAccessor(container, as, ref);
            } else {
               if (!(as.getRightOp() instanceof FieldRef)) {
                  throw new RuntimeException("Expected class member access");
               }

               ref = (FieldRef)as.getRightOp();
               createGetAccessor(container, as, ref);
            }
         }

      }
   }

   private static void createGetAccessor(SootMethod container, AssignStmt as, FieldRef ref) {
      List parameterTypes = new LinkedList();
      List<SootClass> thrownExceptions = new LinkedList();
      Body accessorBody = Jimple.v().newBody();
      Chain accStmts = accessorBody.getUnits();
      LocalGenerator lg = new LocalGenerator(accessorBody);
      Body containerBody = container.getActiveBody();
      Chain containerStmts = containerBody.getUnits();
      SootClass target = ref.getField().getDeclaringClass();
      String name = createAccessorName(ref.getField(), false);
      SootMethod accessor = target.getMethodByNameUnsafe(name);
      if (accessor == null) {
         Type returnType = ref.getField().getType();
         Local thisLocal = lg.generateLocal(target.getType());
         if (ref instanceof InstanceFieldRef) {
            parameterTypes.add(target.getType());
            accStmts.addFirst(Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newParameterRef(target.getType(), 0)));
         }

         Local l = lg.generateLocal(ref.getField().getType());
         Object v;
         if (ref instanceof InstanceFieldRef) {
            v = Jimple.v().newInstanceFieldRef(thisLocal, ref.getFieldRef());
         } else {
            v = Jimple.v().newStaticFieldRef(ref.getFieldRef());
         }

         accStmts.add(Jimple.v().newAssignStmt(l, (Value)v));
         accStmts.add(Jimple.v().newReturnStmt(l));
         accessor = Scene.v().makeSootMethod(name, parameterTypes, returnType, 9, thrownExceptions);
         accessorBody.setMethod(accessor);
         accessor.setActiveBody(accessorBody);
         target.addMethod(accessor);
      }

      List args = new LinkedList();
      if (ref instanceof InstanceFieldRef) {
         args.add(((InstanceFieldRef)ref).getBase());
      }

      InvokeExpr newExpr = Jimple.v().newStaticInvokeExpr(accessor.makeRef(), (List)args);
      as.setRightOp(newExpr);
   }

   private static void createSetAccessor(SootMethod container, AssignStmt as, FieldRef ref) {
      List parameterTypes = new LinkedList();
      List<SootClass> thrownExceptions = new LinkedList();
      Body accessorBody = Jimple.v().newBody();
      Chain accStmts = accessorBody.getUnits();
      LocalGenerator lg = new LocalGenerator(accessorBody);
      Body containerBody = container.getActiveBody();
      Chain containerStmts = containerBody.getUnits();
      SootClass target = ref.getField().getDeclaringClass();
      String name = createAccessorName(ref.getField(), true);
      SootMethod accessor = target.getMethodByNameUnsafe(name);
      if (accessor == null) {
         Local thisLocal = lg.generateLocal(target.getType());
         int paramID = 0;
         if (ref instanceof InstanceFieldRef) {
            accStmts.add(Jimple.v().newIdentityStmt(thisLocal, Jimple.v().newParameterRef(target.getType(), paramID)));
            parameterTypes.add(target.getType());
            ++paramID;
         }

         parameterTypes.add(ref.getField().getType());
         Local l = lg.generateLocal(ref.getField().getType());
         accStmts.add(Jimple.v().newIdentityStmt(l, Jimple.v().newParameterRef(ref.getField().getType(), paramID)));
         ++paramID;
         if (ref instanceof InstanceFieldRef) {
            accStmts.add(Jimple.v().newAssignStmt(Jimple.v().newInstanceFieldRef(thisLocal, ref.getFieldRef()), l));
         } else {
            accStmts.add(Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(ref.getFieldRef()), l));
         }

         accStmts.addLast(Jimple.v().newReturnVoidStmt());
         Type returnType = VoidType.v();
         accessor = Scene.v().makeSootMethod(name, parameterTypes, returnType, 9, thrownExceptions);
         accessorBody.setMethod(accessor);
         accessor.setActiveBody(accessorBody);
         target.addMethod(accessor);
      }

      List args = new LinkedList();
      if (ref instanceof InstanceFieldRef) {
         args.add(((InstanceFieldRef)ref).getBase());
      }

      args.add(as.getRightOp());
      InvokeExpr newExpr = Jimple.v().newStaticInvokeExpr(accessor.makeRef(), (List)args);
      Stmt newStmt = Jimple.v().newInvokeStmt(newExpr);
      containerStmts.insertAfter((Object)newStmt, as);
      containerStmts.remove(as);
   }

   private static void createInvokeAccessor(SootMethod container, Stmt stmt) {
      List parameterTypes = new LinkedList();
      List<SootClass> thrownExceptions = new LinkedList();
      Body accessorBody = Jimple.v().newBody();
      Chain accStmts = accessorBody.getUnits();
      LocalGenerator lg = new LocalGenerator(accessorBody);
      Body containerBody = container.getActiveBody();
      Chain containerStmts = containerBody.getUnits();
      InvokeExpr expr = stmt.getInvokeExpr();
      SootMethod method = expr.getMethod();
      SootClass target = method.getDeclaringClass();
      String name = createAccessorName(method, true);
      SootMethod accessor = target.getMethodByNameUnsafe(name);
      LinkedList arguments;
      if (accessor == null) {
         arguments = new LinkedList();
         if (expr instanceof InstanceInvokeExpr) {
            parameterTypes.add(target.getType());
         }

         parameterTypes.addAll(method.getParameterTypes());
         Type returnType = method.getReturnType();
         thrownExceptions.addAll(method.getExceptions());
         int paramID = 0;

         Local resultLocal;
         for(Iterator it = parameterTypes.iterator(); it.hasNext(); ++paramID) {
            Type type = (Type)it.next();
            resultLocal = lg.generateLocal(type);
            accStmts.add(Jimple.v().newIdentityStmt(resultLocal, Jimple.v().newParameterRef(type, paramID)));
            arguments.add(resultLocal);
         }

         Object accExpr;
         if (expr instanceof StaticInvokeExpr) {
            accExpr = Jimple.v().newStaticInvokeExpr(method.makeRef(), (List)arguments);
         } else {
            Local thisLocal;
            if (expr instanceof VirtualInvokeExpr) {
               thisLocal = (Local)arguments.get(0);
               arguments.remove(0);
               accExpr = Jimple.v().newVirtualInvokeExpr(thisLocal, method.makeRef(), (List)arguments);
            } else {
               if (!(expr instanceof SpecialInvokeExpr)) {
                  throw new RuntimeException("");
               }

               thisLocal = (Local)arguments.get(0);
               arguments.remove(0);
               accExpr = Jimple.v().newSpecialInvokeExpr(thisLocal, method.makeRef(), (List)arguments);
            }
         }

         if (returnType instanceof VoidType) {
            Stmt s = Jimple.v().newInvokeStmt((Value)accExpr);
            accStmts.add(s);
            accStmts.add(Jimple.v().newReturnVoidStmt());
         } else {
            resultLocal = lg.generateLocal(returnType);
            Stmt s = Jimple.v().newAssignStmt(resultLocal, (Value)accExpr);
            accStmts.add(s);
            accStmts.add(Jimple.v().newReturnStmt(resultLocal));
         }

         accessor = Scene.v().makeSootMethod(name, parameterTypes, returnType, 9, thrownExceptions);
         accessorBody.setMethod(accessor);
         accessor.setActiveBody(accessorBody);
         target.addMethod(accessor);
      }

      arguments = new LinkedList();
      if (expr instanceof InstanceInvokeExpr) {
         arguments.add(((InstanceInvokeExpr)expr).getBase());
      }

      arguments.addAll(expr.getArgs());
      InvokeExpr newExpr = Jimple.v().newStaticInvokeExpr(accessor.makeRef(), (List)arguments);
      stmt.getInvokeExprBox().setValue(newExpr);
   }

   public static boolean ensureAccess(SootMethod container, ClassMember target, String options) {
      boolean accessors = options.equals("accessors");
      boolean allowChanges = !options.equals("none");
      boolean safeChangesOnly = !options.equals("unsafe");
      SootClass targetClass = target.getDeclaringClass();
      if (!ensureAccess(container, targetClass, options)) {
         return false;
      } else if (isAccessLegal(container, target)) {
         return true;
      } else if (!allowChanges && !accessors) {
         return false;
      } else if (target.getDeclaringClass().isApplicationClass()) {
         if (accessors) {
            return true;
         } else if (safeChangesOnly) {
            throw new RuntimeException("Not implemented yet!");
         } else {
            target.setModifiers(target.getModifiers() | 1);
            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean ensureAccess(SootMethod container, SootClass target, String options) {
      boolean accessors = options.equals("accessors");
      boolean allowChanges = !options.equals("none");
      boolean safeChangesOnly = !options.equals("unsafe");
      if (isAccessLegal(container, target)) {
         return true;
      } else if (!allowChanges && !accessors) {
         return false;
      } else if (safeChangesOnly && !accessors) {
         throw new RuntimeException("Not implemented yet!");
      } else if (accessors) {
         return false;
      } else if (target.isApplicationClass()) {
         target.setModifiers(target.getModifiers() | 1);
         return true;
      } else {
         return false;
      }
   }
}
