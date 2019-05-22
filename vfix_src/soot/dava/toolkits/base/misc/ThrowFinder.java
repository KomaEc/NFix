package soot.dava.toolkits.base.misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.G;
import soot.MethodOrMethodContext;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.JExitMonitorStmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.CallGraphBuilder;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.Chain;
import soot.util.IterableSet;

public class ThrowFinder {
   private static final Logger logger = LoggerFactory.getLogger(ThrowFinder.class);
   private HashSet<SootMethod> registeredMethods;
   private HashMap<Stmt, HashSet<SootClass>> protectionSet;
   public static boolean DEBUG = false;

   public ThrowFinder(Singletons.Global g) {
   }

   public static ThrowFinder v() {
      return G.v().soot_dava_toolkits_base_misc_ThrowFinder();
   }

   public void find() {
      logger.debug("Verifying exception handling.. ");
      this.registeredMethods = new HashSet();
      this.protectionSet = new HashMap();
      CallGraph cg;
      if (Scene.v().hasCallGraph()) {
         cg = Scene.v().getCallGraph();
      } else {
         (new CallGraphBuilder()).build();
         cg = Scene.v().getCallGraph();
         Scene.v().releaseCallGraph();
      }

      IterableSet worklist = new IterableSet();
      logger.debug("\b. ");
      Iterator classIt = Scene.v().getApplicationClasses().iterator();

      while(classIt.hasNext()) {
         Iterator methodIt = ((SootClass)classIt.next()).methodIterator();

         while(methodIt.hasNext()) {
            SootMethod m = (SootMethod)methodIt.next();
            this.register_AreasOfProtection(m);
            worklist.add(m);
         }
      }

      HashMap<SootClass, IterableSet> subClassSet = new HashMap();
      HashMap<SootClass, IterableSet> superClassSet = new HashMap();
      HashSet<SootClass> applicationClasses = new HashSet();
      applicationClasses.addAll(Scene.v().getApplicationClasses());
      classIt = Scene.v().getApplicationClasses().iterator();

      IterableSet exceptionSet;
      while(classIt.hasNext()) {
         SootClass c = (SootClass)classIt.next();
         IterableSet superClasses = (IterableSet)superClassSet.get(c);
         if (superClasses == null) {
            superClasses = new IterableSet();
            superClassSet.put(c, superClasses);
         }

         IterableSet subClasses = (IterableSet)subClassSet.get(c);
         if (subClasses == null) {
            subClasses = new IterableSet();
            subClassSet.put(c, subClasses);
         }

         if (c.hasSuperclass()) {
            SootClass superClass = c.getSuperclass();
            exceptionSet = (IterableSet)subClassSet.get(superClass);
            if (exceptionSet == null) {
               exceptionSet = new IterableSet();
               subClassSet.put(superClass, exceptionSet);
            }

            exceptionSet.add(c);
            superClasses.add(superClass);
         }

         Iterator interfaceIt = c.getInterfaces().iterator();

         while(interfaceIt.hasNext()) {
            SootClass interfaceClass = (SootClass)interfaceIt.next();
            IterableSet interfaceClassSubClasses = (IterableSet)subClassSet.get(interfaceClass);
            if (interfaceClassSubClasses == null) {
               interfaceClassSubClasses = new IterableSet();
               subClassSet.put(interfaceClass, interfaceClassSubClasses);
            }

            interfaceClassSubClasses.add(c);
            superClasses.add(interfaceClass);
         }
      }

      HashMap<SootMethod, IterableSet> agreementMethodSet = new HashMap();
      Iterator worklistIt = worklist.iterator();

      SootClass c;
      SootMethod m;
      while(worklistIt.hasNext()) {
         m = (SootMethod)worklistIt.next();
         if (!m.isAbstract() && !m.isNative()) {
            List<SootClass> exceptionList = m.getExceptions();
            exceptionSet = new IterableSet(exceptionList);
            boolean changed = false;
            Iterator it = m.retrieveActiveBody().getUnits().iterator();

            while(it.hasNext()) {
               Unit u = (Unit)it.next();
               HashSet handled = (HashSet)this.protectionSet.get(u);
               if (u instanceof ThrowStmt) {
                  Type t = ((ThrowStmt)u).getOp().getType();
                  if (t instanceof RefType) {
                     c = ((RefType)t).getSootClass();
                     if (!this.handled_Exception(handled, c) && !exceptionSet.contains(c)) {
                        PatchingChain list = m.retrieveActiveBody().getUnits();
                        Unit pred = list.getPredOf(u);
                        if (!(pred instanceof JExitMonitorStmt)) {
                           exceptionSet.add(c);
                           changed = true;
                           if (DEBUG) {
                              System.out.println("Added exception which is explicitly thrown" + c.getName());
                           }
                        } else if (DEBUG) {
                           System.out.println("Found monitor exit" + pred + " hence not adding");
                        }
                     }
                  }
               }
            }

            it = cg.edgesOutOf((MethodOrMethodContext)m);

            label167:
            while(true) {
               Edge e;
               Stmt callSite;
               do {
                  if (!it.hasNext()) {
                     if (changed) {
                        exceptionList.clear();
                        exceptionList.addAll(exceptionSet);
                     }
                     break label167;
                  }

                  e = (Edge)it.next();
                  callSite = e.srcStmt();
               } while(callSite == null);

               HashSet handled = (HashSet)this.protectionSet.get(callSite);
               SootMethod target = e.tgt();
               Iterator exceptionIt = target.getExceptions().iterator();

               while(exceptionIt.hasNext()) {
                  SootClass exception = (SootClass)exceptionIt.next();
                  if (!this.handled_Exception(handled, exception) && !exceptionSet.contains(exception)) {
                     exceptionSet.add(exception);
                     changed = true;
                  }
               }
            }
         }

         this.find_OtherMethods(m, agreementMethodSet, subClassSet, applicationClasses);
         this.find_OtherMethods(m, agreementMethodSet, superClassSet, applicationClasses);
      }

      label148:
      while(!worklist.isEmpty()) {
         m = (SootMethod)worklist.getFirst();
         worklist.removeFirst();
         IterableSet agreementMethods = (IterableSet)agreementMethodSet.get(m);
         Iterator it;
         if (agreementMethods != null) {
            it = agreementMethods.iterator();

            while(it.hasNext()) {
               SootMethod otherMethod = (SootMethod)it.next();
               List<SootClass> otherExceptionsList = otherMethod.getExceptions();
               IterableSet otherExceptionSet = new IterableSet(otherExceptionsList);
               boolean changed = false;
               Iterator exceptionIt = m.getExceptions().iterator();

               while(exceptionIt.hasNext()) {
                  c = (SootClass)exceptionIt.next();
                  if (!otherExceptionSet.contains(c)) {
                     otherExceptionSet.add(c);
                     changed = true;
                  }
               }

               if (changed) {
                  otherExceptionsList.clear();
                  otherExceptionsList.addAll(otherExceptionSet);
                  if (!worklist.contains(otherMethod)) {
                     worklist.addLast(otherMethod);
                  }
               }
            }
         }

         it = cg.edgesOutOf((MethodOrMethodContext)m);

         while(true) {
            Stmt callingSite;
            Edge e;
            do {
               if (!it.hasNext()) {
                  continue label148;
               }

               e = (Edge)it.next();
               callingSite = e.srcStmt();
            } while(callingSite == null);

            SootMethod callingMethod = e.src();
            List<SootClass> exceptionList = callingMethod.getExceptions();
            IterableSet exceptionSet = new IterableSet(exceptionList);
            HashSet handled = (HashSet)this.protectionSet.get(callingSite);
            boolean changed = false;
            Iterator exceptionIt = m.getExceptions().iterator();

            while(exceptionIt.hasNext()) {
               SootClass exception = (SootClass)exceptionIt.next();
               if (!this.handled_Exception(handled, exception) && !exceptionSet.contains(exception)) {
                  exceptionSet.add(exception);
                  changed = true;
               }
            }

            if (changed) {
               exceptionList.clear();
               exceptionList.addAll(exceptionSet);
               if (!worklist.contains(callingMethod)) {
                  worklist.addLast(callingMethod);
               }
            }
         }
      }

   }

   private void find_OtherMethods(SootMethod startingMethod, HashMap<SootMethod, IterableSet> methodMapping, HashMap<SootClass, IterableSet> classMapping, HashSet<SootClass> applicationClasses) {
      IterableSet worklist = (IterableSet)((IterableSet)classMapping.get(startingMethod.getDeclaringClass())).clone();
      HashSet<SootClass> touchSet = new HashSet();
      touchSet.addAll(worklist);
      String signature = startingMethod.getSubSignature();

      while(true) {
         while(true) {
            SootClass currentClass;
            do {
               if (worklist.isEmpty()) {
                  return;
               }

               currentClass = (SootClass)worklist.getFirst();
               worklist.removeFirst();
            } while(!applicationClasses.contains(currentClass));

            IterableSet otherClasses;
            if (currentClass.declaresMethod(signature)) {
               otherClasses = (IterableSet)methodMapping.get(startingMethod);
               if (otherClasses == null) {
                  otherClasses = new IterableSet();
                  methodMapping.put(startingMethod, otherClasses);
               }

               otherClasses.add(currentClass.getMethod(signature));
            } else {
               otherClasses = (IterableSet)classMapping.get(currentClass);
               if (otherClasses != null) {
                  Iterator ocit = otherClasses.iterator();

                  while(ocit.hasNext()) {
                     SootClass otherClass = (SootClass)ocit.next();
                     if (!touchSet.contains(otherClass)) {
                        worklist.addLast(otherClass);
                        touchSet.add(otherClass);
                     }
                  }
               }
            }
         }
      }
   }

   private void register_AreasOfProtection(SootMethod m) {
      if (!this.registeredMethods.contains(m)) {
         this.registeredMethods.add(m);
         if (m.hasActiveBody()) {
            Body b = m.getActiveBody();
            Chain stmts = b.getUnits();
            Iterator trapIt = b.getTraps().iterator();

            while(trapIt.hasNext()) {
               Trap t = (Trap)trapIt.next();
               SootClass exception = t.getException();
               Iterator sit = stmts.iterator(t.getBeginUnit(), stmts.getPredOf(t.getEndUnit()));

               while(sit.hasNext()) {
                  Stmt s = (Stmt)sit.next();
                  HashSet<SootClass> handled = null;
                  if ((handled = (HashSet)this.protectionSet.get(s)) == null) {
                     handled = new HashSet();
                     this.protectionSet.put(s, handled);
                  }

                  if (!handled.contains(exception)) {
                     handled.add(exception);
                  }
               }
            }

         }
      }
   }

   private boolean handled_Exception(HashSet handledExceptions, SootClass c) {
      SootClass thrownException = c;
      if (this.is_HandledByRuntime(c)) {
         return true;
      } else if (handledExceptions == null) {
         return false;
      } else {
         while(!handledExceptions.contains(thrownException)) {
            if (!thrownException.hasSuperclass()) {
               return false;
            }

            thrownException = thrownException.getSuperclass();
         }

         return true;
      }
   }

   private boolean is_HandledByRuntime(SootClass c) {
      SootClass thrownException = c;
      SootClass runtimeException = Scene.v().getSootClass("java.lang.RuntimeException");

      for(SootClass error = Scene.v().getSootClass("java.lang.Error"); thrownException != runtimeException && thrownException != error; thrownException = thrownException.getSuperclass()) {
         if (!thrownException.hasSuperclass()) {
            return false;
         }
      }

      return true;
   }
}
