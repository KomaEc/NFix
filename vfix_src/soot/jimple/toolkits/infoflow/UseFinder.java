package soot.jimple.toolkits.infoflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.toolkits.scalar.Pair;
import soot.util.Chain;

public class UseFinder {
   ReachableMethods rm;
   Map<SootClass, List> classToExtFieldAccesses = new HashMap();
   Map<SootClass, ArrayList> classToIntFieldAccesses = new HashMap();
   Map<SootClass, List> classToExtCalls = new HashMap();
   Map<SootClass, ArrayList> classToIntCalls = new HashMap();

   public UseFinder() {
      this.rm = Scene.v().getReachableMethods();
      this.doAnalysis();
   }

   public UseFinder(ReachableMethods rm) {
      this.rm = rm;
      this.doAnalysis();
   }

   public List getExtFieldAccesses(SootClass sc) {
      if (this.classToExtFieldAccesses.containsKey(sc)) {
         return (List)this.classToExtFieldAccesses.get(sc);
      } else {
         throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
      }
   }

   public List getIntFieldAccesses(SootClass sc) {
      if (this.classToIntFieldAccesses.containsKey(sc)) {
         return (List)this.classToIntFieldAccesses.get(sc);
      } else {
         throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
      }
   }

   public List getExtCalls(SootClass sc) {
      if (this.classToExtCalls.containsKey(sc)) {
         return (List)this.classToExtCalls.get(sc);
      } else {
         throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
      }
   }

   public List getIntCalls(SootClass sc) {
      if (this.classToIntCalls.containsKey(sc)) {
         return (List)this.classToIntCalls.get(sc);
      } else {
         throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
      }
   }

   public List<SootMethod> getExtMethods(SootClass sc) {
      if (this.classToExtCalls.containsKey(sc)) {
         List extCalls = (List)this.classToExtCalls.get(sc);
         List<SootMethod> extMethods = new ArrayList();
         Iterator callIt = extCalls.iterator();

         while(callIt.hasNext()) {
            Pair call = (Pair)callIt.next();
            SootMethod calledMethod = ((Stmt)call.getO2()).getInvokeExpr().getMethod();
            if (!extMethods.contains(calledMethod)) {
               extMethods.add(calledMethod);
            }
         }

         return extMethods;
      } else {
         throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
      }
   }

   public List<SootField> getExtFields(SootClass sc) {
      if (this.classToExtFieldAccesses.containsKey(sc)) {
         List extAccesses = (List)this.classToExtFieldAccesses.get(sc);
         List<SootField> extFields = new ArrayList();
         Iterator accessIt = extAccesses.iterator();

         while(accessIt.hasNext()) {
            Pair access = (Pair)accessIt.next();
            SootField accessedField = ((Stmt)access.getO2()).getFieldRef().getField();
            if (!extFields.contains(accessedField)) {
               extFields.add(accessedField);
            }
         }

         return extFields;
      } else {
         throw new RuntimeException("UseFinder does not search non-application classes: " + sc);
      }
   }

   private void doAnalysis() {
      Chain appClasses = Scene.v().getApplicationClasses();
      Iterator appClassesIt = appClasses.iterator();

      SootClass appClass;
      while(appClassesIt.hasNext()) {
         appClass = (SootClass)appClassesIt.next();
         this.classToIntFieldAccesses.put(appClass, new ArrayList());
         this.classToExtFieldAccesses.put(appClass, new ArrayList());
         this.classToIntCalls.put(appClass, new ArrayList());
         this.classToExtCalls.put(appClass, new ArrayList());
      }

      appClassesIt = appClasses.iterator();

      label101:
      while(appClassesIt.hasNext()) {
         appClass = (SootClass)appClassesIt.next();
         Iterator methodsIt = appClass.getMethods().iterator();

         label99:
         while(true) {
            SootMethod method;
            do {
               do {
                  if (!methodsIt.hasNext()) {
                     continue label101;
                  }

                  method = (SootMethod)methodsIt.next();
               } while(!method.isConcrete());
            } while(!this.rm.contains(method));

            Body b = method.retrieveActiveBody();
            Iterator unitsIt = b.getUnits().iterator();

            while(true) {
               while(true) {
                  while(true) {
                     Stmt s;
                     Object otherClassList;
                     do {
                        if (!unitsIt.hasNext()) {
                           continue label99;
                        }

                        s = (Stmt)unitsIt.next();
                        if (s.containsFieldRef()) {
                           FieldRef fr = s.getFieldRef();
                           if (fr.getFieldRef().resolve().getDeclaringClass() == appClass) {
                              if (fr instanceof StaticFieldRef) {
                                 ((ArrayList)this.classToIntFieldAccesses.get(appClass)).add(new Pair(method, s));
                              } else if (fr instanceof InstanceFieldRef) {
                                 InstanceFieldRef ifr = (InstanceFieldRef)fr;
                                 if (!method.isStatic() && ifr.getBase().equivTo(b.getThisLocal())) {
                                    ((ArrayList)this.classToIntFieldAccesses.get(appClass)).add(new Pair(method, s));
                                 } else {
                                    ((List)this.classToExtFieldAccesses.get(appClass)).add(new Pair(method, s));
                                 }
                              }
                           } else {
                              otherClassList = (List)this.classToExtFieldAccesses.get(fr.getFieldRef().resolve().getDeclaringClass());
                              if (otherClassList == null) {
                                 otherClassList = new ArrayList();
                                 this.classToExtFieldAccesses.put(fr.getFieldRef().resolve().getDeclaringClass(), otherClassList);
                              }

                              ((List)otherClassList).add(new Pair(method, s));
                           }
                        }
                     } while(!s.containsInvokeExpr());

                     InvokeExpr ie = s.getInvokeExpr();
                     if (ie.getMethodRef().resolve().getDeclaringClass() == appClass) {
                        if (ie instanceof StaticInvokeExpr) {
                           ((ArrayList)this.classToIntCalls.get(appClass)).add(new Pair(method, s));
                        } else if (ie instanceof InstanceInvokeExpr) {
                           InstanceInvokeExpr iie = (InstanceInvokeExpr)ie;
                           if (!method.isStatic() && iie.getBase().equivTo(b.getThisLocal())) {
                              ((ArrayList)this.classToIntCalls.get(appClass)).add(new Pair(method, s));
                           } else {
                              ((List)this.classToExtCalls.get(appClass)).add(new Pair(method, s));
                           }
                        }
                     } else {
                        otherClassList = (List)this.classToExtCalls.get(ie.getMethodRef().resolve().getDeclaringClass());
                        if (otherClassList == null) {
                           otherClassList = new ArrayList();
                           this.classToExtCalls.put(ie.getMethodRef().resolve().getDeclaringClass(), otherClassList);
                        }

                        ((List)otherClassList).add(new Pair(method, s));
                     }
                  }
               }
            }
         }
      }

   }
}
