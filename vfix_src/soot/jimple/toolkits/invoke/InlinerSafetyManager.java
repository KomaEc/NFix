package soot.jimple.toolkits.invoke;

import java.util.Iterator;
import soot.Body;
import soot.ClassMember;
import soot.Hierarchy;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;

public class InlinerSafetyManager {
   public static boolean checkSpecialInlineRestrictions(SootMethod container, SootMethod target, String options) {
      boolean accessors = options.equals("accessors");
      Body inlineeBody = target.getActiveBody();
      Iterator unitsIt = inlineeBody.getUnits().iterator();

      SootMethod specialTarget;
      do {
         InvokeExpr ie1;
         do {
            Stmt st;
            do {
               if (!unitsIt.hasNext()) {
                  return true;
               }

               st = (Stmt)unitsIt.next();
            } while(!st.containsInvokeExpr());

            ie1 = st.getInvokeExpr();
         } while(!(ie1 instanceof SpecialInvokeExpr));

         if (specialInvokePerformsLookupIn(ie1, container.getDeclaringClass()) || specialInvokePerformsLookupIn(ie1, target.getDeclaringClass())) {
            return false;
         }

         specialTarget = ie1.getMethod();
      } while(!specialTarget.isPrivate() || specialTarget.getDeclaringClass() == container.getDeclaringClass() || accessors);

      return false;
   }

   public static boolean checkAccessRestrictions(SootMethod container, SootMethod target, String modifierOptions) {
      Body inlineeBody = target.getActiveBody();
      Iterator unitsIt = inlineeBody.getUnits().iterator();

      while(unitsIt.hasNext()) {
         Stmt st = (Stmt)unitsIt.next();
         if (st.containsInvokeExpr()) {
            InvokeExpr ie1 = st.getInvokeExpr();
            if (!AccessManager.ensureAccess(container, (ClassMember)ie1.getMethod(), modifierOptions)) {
               return false;
            }
         }

         if (st instanceof AssignStmt) {
            Value lhs = ((AssignStmt)st).getLeftOp();
            Value rhs = ((AssignStmt)st).getRightOp();
            if (lhs instanceof FieldRef && !AccessManager.ensureAccess(container, (ClassMember)((FieldRef)lhs).getField(), modifierOptions)) {
               return false;
            }

            if (rhs instanceof FieldRef && !AccessManager.ensureAccess(container, (ClassMember)((FieldRef)rhs).getField(), modifierOptions)) {
               return false;
            }
         }
      }

      return true;
   }

   public static boolean ensureInlinability(SootMethod target, Stmt toInline, SootMethod container, String modifierOptions) {
      if (!canSafelyInlineInto(target, toInline, container)) {
         return false;
      } else if (!AccessManager.ensureAccess(container, (ClassMember)target, modifierOptions)) {
         return false;
      } else if (!checkSpecialInlineRestrictions(container, target, modifierOptions)) {
         return false;
      } else {
         return checkAccessRestrictions(container, target, modifierOptions);
      }
   }

   private static boolean canSafelyInlineInto(SootMethod inlinee, Stmt toInline, SootMethod container) {
      if (inlinee.getName().equals("<init>")) {
         return false;
      } else if (inlinee.getSignature().equals(container.getSignature())) {
         return false;
      } else if (!inlinee.isNative() && !inlinee.isAbstract()) {
         InvokeExpr ie = toInline.getInvokeExpr();
         Value base = ie instanceof InstanceInvokeExpr ? ((InstanceInvokeExpr)ie).getBase() : null;
         if (base != null && base.getType() instanceof RefType && invokeThrowsAccessErrorIn(((RefType)base.getType()).getSootClass(), inlinee, container)) {
            return false;
         } else {
            return !(ie instanceof SpecialInvokeExpr) || !specialInvokePerformsLookupIn(ie, inlinee.getDeclaringClass()) && !specialInvokePerformsLookupIn(ie, container.getDeclaringClass());
         }
      } else {
         return false;
      }
   }

   private static boolean invokeThrowsAccessErrorIn(SootClass base, SootMethod inlinee, SootMethod container) {
      SootClass inlineeClass = inlinee.getDeclaringClass();
      SootClass containerClass = container.getDeclaringClass();
      if (inlinee.isPrivate() && !inlineeClass.getName().equals(containerClass.getName())) {
         return true;
      } else if (!inlinee.isPrivate() && !inlinee.isProtected() && !inlinee.isPublic() && !inlineeClass.getPackageName().equals(containerClass.getPackageName())) {
         return true;
      } else {
         if (inlinee.isProtected()) {
            Hierarchy h = Scene.v().getActiveHierarchy();
            boolean saved = false;
            if (h.isClassSuperclassOfIncluding(inlineeClass, containerClass) || base != null && h.isClassSuperclassOfIncluding(base, containerClass)) {
               saved = true;
            }

            if (!saved) {
               return true;
            }
         }

         return false;
      }
   }

   static boolean specialInvokePerformsLookupIn(InvokeExpr ie, SootClass containerClass) {
      SootMethod m = ie.getMethod();
      if (m.getName().equals("<init>")) {
         return false;
      } else if (m.isPrivate()) {
         return false;
      } else {
         Hierarchy h = Scene.v().getActiveHierarchy();
         return h.isClassSuperclassOf(m.getDeclaringClass(), containerClass);
      }
   }
}
