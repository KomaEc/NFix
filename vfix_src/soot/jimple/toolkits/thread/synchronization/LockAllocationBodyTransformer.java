package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.EquivalentValue;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.VoidType;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.toolkits.infoflow.FakeJimpleLocal;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.Pair;
import soot.util.Chain;

public class LockAllocationBodyTransformer extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(LockAllocationBodyTransformer.class);
   private static final LockAllocationBodyTransformer instance = new LockAllocationBodyTransformer();
   private static boolean addedGlobalLockDefs = false;
   private static int throwableNum = 0;
   static int baseLocalNum = 0;
   static int lockNumber = 0;
   static Map<EquivalentValue, StaticFieldRef> lockEqValToLock = new HashMap();

   private LockAllocationBodyTransformer() {
   }

   public static LockAllocationBodyTransformer v() {
      return instance;
   }

   protected void internalTransform(Body b, String phase, Map opts) {
      throw new RuntimeException("Not Supported");
   }

   protected void internalTransform(Body b, FlowSet fs, List<CriticalSectionGroup> groups, boolean[] insertedGlobalLock) {
      JimpleBody j = (JimpleBody)b;
      SootMethod thisMethod = b.getMethod();
      PatchingChain<Unit> units = b.getUnits();
      Iterator<Unit> unitIt = units.iterator();
      Unit firstUnit = j.getFirstNonIdentityStmt();
      Unit lastUnit = units.getLast();
      Local[] lockObj = new Local[groups.size()];
      boolean[] addedLocalLockObj = new boolean[groups.size()];
      SootField[] globalLockObj = new SootField[groups.size()];

      int tempNum;
      for(tempNum = 1; tempNum < groups.size(); ++tempNum) {
         lockObj[tempNum] = Jimple.v().newLocal("lockObj" + tempNum, RefType.v("java.lang.Object"));
         addedLocalLockObj[tempNum] = false;
         globalLockObj[tempNum] = null;
      }

      CriticalSectionGroup clinitMethod;
      for(tempNum = 1; tempNum < groups.size(); ++tempNum) {
         clinitMethod = (CriticalSectionGroup)groups.get(tempNum);
         if (!clinitMethod.useDynamicLock && !clinitMethod.useLocksets) {
            if (!insertedGlobalLock[tempNum]) {
               try {
                  globalLockObj[tempNum] = Scene.v().getMainClass().getFieldByName("globalLockObj" + tempNum);
               } catch (RuntimeException var30) {
                  globalLockObj[tempNum] = Scene.v().makeSootField("globalLockObj" + tempNum, RefType.v("java.lang.Object"), 9);
                  Scene.v().getMainClass().addField(globalLockObj[tempNum]);
               }

               insertedGlobalLock[tempNum] = true;
            } else {
               globalLockObj[tempNum] = Scene.v().getMainClass().getFieldByName("globalLockObj" + tempNum);
            }
         }
      }

      int lockNum;
      if (!addedGlobalLockDefs) {
         SootClass mainClass = Scene.v().getMainClass();
         clinitMethod = null;
         JimpleBody clinitBody = null;
         Stmt firstStmt = null;
         boolean addingNewClinit = !mainClass.declaresMethod("void <clinit>()");
         SootMethod clinitMethod;
         if (addingNewClinit) {
            clinitMethod = Scene.v().makeSootMethod("<clinit>", new ArrayList(), VoidType.v(), 9);
            clinitBody = Jimple.v().newBody(clinitMethod);
            clinitMethod.setActiveBody(clinitBody);
            mainClass.addMethod(clinitMethod);
         } else {
            clinitMethod = mainClass.getMethod("void <clinit>()");
            clinitBody = (JimpleBody)clinitMethod.getActiveBody();
            firstStmt = clinitBody.getFirstNonIdentityStmt();
         }

         PatchingChain<Unit> clinitUnits = clinitBody.getUnits();

         for(lockNum = 1; lockNum < groups.size(); ++lockNum) {
            CriticalSectionGroup tnGroup = (CriticalSectionGroup)groups.get(lockNum);
            if (!tnGroup.useDynamicLock && !tnGroup.useLocksets) {
               clinitBody.getLocals().add(lockObj[lockNum]);
               Stmt newStmt = Jimple.v().newAssignStmt(lockObj[lockNum], Jimple.v().newNewExpr(RefType.v("java.lang.Object")));
               if (addingNewClinit) {
                  clinitUnits.add((Unit)newStmt);
               } else {
                  clinitUnits.insertBeforeNoRedirect(newStmt, firstStmt);
               }

               SootClass objectClass = Scene.v().loadClassAndSupport("java.lang.Object");
               RefType type = RefType.v(objectClass);
               SootMethod initMethod = objectClass.getMethod("void <init>()");
               Stmt initStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(lockObj[lockNum], initMethod.makeRef(), Collections.EMPTY_LIST));
               if (addingNewClinit) {
                  clinitUnits.add((Unit)initStmt);
               } else {
                  clinitUnits.insertBeforeNoRedirect(initStmt, firstStmt);
               }

               Stmt assignStmt = Jimple.v().newAssignStmt(Jimple.v().newStaticFieldRef(globalLockObj[lockNum].makeRef()), lockObj[lockNum]);
               if (addingNewClinit) {
                  clinitUnits.add((Unit)assignStmt);
               } else {
                  clinitUnits.insertBeforeNoRedirect(assignStmt, firstStmt);
               }
            }
         }

         if (addingNewClinit) {
            clinitUnits.add((Unit)Jimple.v().newReturnVoidStmt());
         }

         addedGlobalLockDefs = true;
      }

      tempNum = 1;
      Iterator fsIt = fs.iterator();
      AssignStmt newPrep = null;

      while(true) {
         CriticalSection tn;
         do {
            if (!fsIt.hasNext()) {
               return;
            }

            tn = ((SynchronizedRegionFlowPair)fsIt.next()).tn;
         } while(tn.setNumber == -1);

         if (tn.wholeMethod) {
            thisMethod.setModifiers(thisMethod.getModifiers() & -33);
         }

         Local clo = null;
         SynchronizedRegion csr = null;
         lockNum = 0;

         Stmt endExitmonitor;
         Stmt tmp;
         for(boolean moreLocks = true; moreLocks; ++lockNum) {
            Object lock;
            InstanceFieldRef ifr;
            Iterator var44;
            Pair earlyEnd;
            if (tn.group.useDynamicLock) {
               lock = getLockFor((EquivalentValue)tn.lockObject);
               if (lock instanceof Ref) {
                  if (lock instanceof InstanceFieldRef) {
                     ifr = (InstanceFieldRef)lock;
                     if (ifr.getBase() instanceof FakeJimpleLocal) {
                        lock = this.reconstruct(b, units, ifr, tn.entermonitor != null ? tn.entermonitor : tn.beginning, tn.entermonitor != null);
                     }
                  }

                  if (!b.getLocals().contains(lockObj[tn.setNumber])) {
                     b.getLocals().add(lockObj[tn.setNumber]);
                  }

                  newPrep = Jimple.v().newAssignStmt(lockObj[tn.setNumber], (Value)lock);
                  if (tn.wholeMethod) {
                     units.insertBeforeNoRedirect(newPrep, firstUnit);
                  } else {
                     units.insertBefore((Unit)newPrep, (Unit)tn.entermonitor);
                  }

                  clo = lockObj[tn.setNumber];
               } else {
                  if (!(lock instanceof Local)) {
                     throw new RuntimeException("Unknown type of lock (" + lock + "): expected Ref or Local");
                  }

                  clo = (Local)lock;
               }

               csr = tn;
               moreLocks = false;
            } else if (!tn.group.useLocksets) {
               if (!addedLocalLockObj[tn.setNumber]) {
                  b.getLocals().add(lockObj[tn.setNumber]);
               }

               addedLocalLockObj[tn.setNumber] = true;
               newPrep = Jimple.v().newAssignStmt(lockObj[tn.setNumber], Jimple.v().newStaticFieldRef(globalLockObj[tn.setNumber].makeRef()));
               if (tn.wholeMethod) {
                  units.insertBeforeNoRedirect(newPrep, firstUnit);
               } else {
                  units.insertBefore((Unit)newPrep, (Unit)tn.entermonitor);
               }

               clo = lockObj[tn.setNumber];
               csr = tn;
               moreLocks = false;
            } else {
               lock = getLockFor((EquivalentValue)tn.lockset.get(lockNum));
               if (lock instanceof FieldRef) {
                  if (lock instanceof InstanceFieldRef) {
                     ifr = (InstanceFieldRef)lock;
                     if (ifr.getBase() instanceof FakeJimpleLocal) {
                        lock = this.reconstruct(b, units, ifr, tn.entermonitor != null ? tn.entermonitor : tn.beginning, tn.entermonitor != null);
                     }
                  }

                  Local lockLocal = Jimple.v().newLocal("locksetObj" + tempNum, RefType.v("java.lang.Object"));
                  ++tempNum;
                  b.getLocals().add(lockLocal);
                  newPrep = Jimple.v().newAssignStmt(lockLocal, (Value)lock);
                  if (tn.entermonitor != null) {
                     units.insertBefore((Unit)newPrep, (Unit)tn.entermonitor);
                  } else {
                     units.insertBeforeNoRedirect(newPrep, tn.beginning);
                  }

                  clo = lockLocal;
               } else {
                  if (!(lock instanceof Local)) {
                     throw new RuntimeException("Unknown type of lock (" + lock + "): expected FieldRef or Local");
                  }

                  clo = (Local)lock;
               }

               if (lockNum + 1 >= tn.lockset.size()) {
                  moreLocks = false;
               } else {
                  moreLocks = true;
               }

               if (lockNum <= 0) {
                  csr = tn;
               } else {
                  SynchronizedRegion nsr = new SynchronizedRegion();
                  nsr.beginning = ((SynchronizedRegion)csr).beginning;
                  var44 = ((SynchronizedRegion)csr).earlyEnds.iterator();

                  while(var44.hasNext()) {
                     earlyEnd = (Pair)var44.next();
                     tmp = (Stmt)earlyEnd.getO2();
                     nsr.earlyEnds.add(new Pair(tmp, (Object)null));
                  }

                  nsr.last = ((SynchronizedRegion)csr).last;
                  if (((SynchronizedRegion)csr).end != null) {
                     endExitmonitor = (Stmt)((SynchronizedRegion)csr).end.getO2();
                     nsr.after = endExitmonitor;
                  }

                  csr = nsr;
               }
            }

            if (((SynchronizedRegion)csr).prepStmt != null) {
            }

            Stmt newEntermonitor = Jimple.v().newEnterMonitorStmt(clo);
            if (((SynchronizedRegion)csr).entermonitor != null) {
               units.insertBefore((Unit)newEntermonitor, (Unit)((SynchronizedRegion)csr).entermonitor);
               units.remove(((SynchronizedRegion)csr).entermonitor);
               ((SynchronizedRegion)csr).entermonitor = newEntermonitor;
            } else {
               units.insertBeforeNoRedirect(newEntermonitor, ((SynchronizedRegion)csr).beginning);
               ((SynchronizedRegion)csr).entermonitor = newEntermonitor;
            }

            List<Pair<Stmt, Stmt>> newEarlyEnds = new ArrayList();
            var44 = ((SynchronizedRegion)csr).earlyEnds.iterator();

            while(var44.hasNext()) {
               earlyEnd = (Pair)var44.next();
               tmp = (Stmt)earlyEnd.getO1();
               Stmt exitmonitor = (Stmt)earlyEnd.getO2();
               Stmt newExitmonitor = Jimple.v().newExitMonitorStmt(clo);
               Stmt tmp;
               if (exitmonitor != null) {
                  if (newPrep != null) {
                     tmp = (Stmt)newPrep.clone();
                     units.insertBefore((Unit)tmp, (Unit)exitmonitor);
                  }

                  units.insertBefore((Unit)newExitmonitor, (Unit)exitmonitor);
                  units.remove(exitmonitor);
                  newEarlyEnds.add(new Pair(tmp, newExitmonitor));
               } else {
                  if (newPrep != null) {
                     tmp = (Stmt)newPrep.clone();
                     units.insertBefore((Unit)tmp, (Unit)tmp);
                  }

                  units.insertBefore((Unit)newExitmonitor, (Unit)tmp);
                  newEarlyEnds.add(new Pair(tmp, newExitmonitor));
               }
            }

            ((SynchronizedRegion)csr).earlyEnds = newEarlyEnds;
            ExitMonitorStmt newExitmonitor;
            Stmt lastEnd;
            if (((SynchronizedRegion)csr).after != null) {
               newExitmonitor = Jimple.v().newExitMonitorStmt(clo);
               if (((SynchronizedRegion)csr).end != null) {
                  lastEnd = (Stmt)((SynchronizedRegion)csr).end.getO2();
                  if (newPrep != null) {
                     tmp = (Stmt)newPrep.clone();
                     units.insertBefore((Unit)tmp, (Unit)lastEnd);
                  }

                  units.insertBefore((Unit)newExitmonitor, (Unit)lastEnd);
                  units.remove(lastEnd);
                  ((SynchronizedRegion)csr).end = new Pair(((SynchronizedRegion)csr).end.getO1(), newExitmonitor);
               } else {
                  if (newPrep != null) {
                     lastEnd = (Stmt)newPrep.clone();
                     units.insertBefore((Unit)lastEnd, (Unit)((SynchronizedRegion)csr).after);
                  }

                  units.insertBefore((Unit)newExitmonitor, (Unit)((SynchronizedRegion)csr).after);
                  Stmt newGotoStmt = Jimple.v().newGotoStmt((Unit)((SynchronizedRegion)csr).after);
                  units.insertBeforeNoRedirect(newGotoStmt, ((SynchronizedRegion)csr).after);
                  ((SynchronizedRegion)csr).end = new Pair(newGotoStmt, newExitmonitor);
                  ((SynchronizedRegion)csr).last = newGotoStmt;
               }
            }

            newExitmonitor = Jimple.v().newExitMonitorStmt(clo);
            if (((SynchronizedRegion)csr).exceptionalEnd != null) {
               lastEnd = (Stmt)((SynchronizedRegion)csr).exceptionalEnd.getO2();
               if (newPrep != null) {
                  tmp = (Stmt)newPrep.clone();
                  units.insertBefore((Unit)tmp, (Unit)lastEnd);
               }

               units.insertBefore((Unit)newExitmonitor, (Unit)lastEnd);
               units.remove(lastEnd);
               ((SynchronizedRegion)csr).exceptionalEnd = new Pair(((SynchronizedRegion)csr).exceptionalEnd.getO1(), newExitmonitor);
            } else {
               lastEnd = null;
               if (((SynchronizedRegion)csr).end != null) {
                  lastEnd = (Stmt)((SynchronizedRegion)csr).end.getO1();
               } else {
                  Iterator var55 = ((SynchronizedRegion)csr).earlyEnds.iterator();

                  label299:
                  while(true) {
                     Stmt end;
                     do {
                        if (!var55.hasNext()) {
                           break label299;
                        }

                        Pair earlyEnd = (Pair)var55.next();
                        end = (Stmt)earlyEnd.getO1();
                     } while(lastEnd != null && (!units.contains(lastEnd) || !units.contains(end) || !units.follows((Unit)end, (Unit)lastEnd)));

                     lastEnd = end;
                  }
               }

               if (((SynchronizedRegion)csr).last == null) {
                  ((SynchronizedRegion)csr).last = lastEnd;
               }

               if (lastEnd == null) {
                  throw new RuntimeException("Lock Region has no ends!  Where should we put the exception handling???");
               }

               Local throwableLocal = Jimple.v().newLocal("throwableLocal" + throwableNum++, RefType.v("java.lang.Throwable"));
               b.getLocals().add(throwableLocal);
               Stmt newCatch = Jimple.v().newIdentityStmt(throwableLocal, Jimple.v().newCaughtExceptionRef());
               if (((SynchronizedRegion)csr).last == null) {
                  throw new RuntimeException("WHY IS clr.last NULL???");
               }

               if (newCatch == null) {
                  throw new RuntimeException("WHY IS newCatch NULL???");
               }

               units.insertAfter((Unit)newCatch, (Unit)((SynchronizedRegion)csr).last);
               units.insertAfter((Unit)newExitmonitor, (Unit)newCatch);
               Stmt newThrow = Jimple.v().newThrowStmt(throwableLocal);
               units.insertAfter((Unit)newThrow, (Unit)newExitmonitor);
               SootClass throwableClass = Scene.v().loadClassAndSupport("java.lang.Throwable");
               b.getTraps().addFirst(Jimple.v().newTrap(throwableClass, (Unit)newExitmonitor, (Unit)newThrow, (Unit)newCatch));
               b.getTraps().addFirst(Jimple.v().newTrap(throwableClass, (Unit)((SynchronizedRegion)csr).beginning, (Unit)lastEnd, (Unit)newCatch));
               ((SynchronizedRegion)csr).exceptionalEnd = new Pair(newThrow, newExitmonitor);
            }
         }

         Iterator var45 = tn.notifys.iterator();

         Unit uWait;
         while(var45.hasNext()) {
            uWait = (Unit)var45.next();
            endExitmonitor = (Stmt)uWait;
            Stmt newNotify = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(clo, endExitmonitor.getInvokeExpr().getMethodRef().declaringClass().getMethod("void notifyAll()").makeRef(), Collections.EMPTY_LIST));
            if (newPrep != null) {
               tmp = (Stmt)newPrep.clone();
               units.insertBefore((Unit)tmp, (Unit)endExitmonitor);
               units.insertBefore((Unit)newNotify, (Unit)tmp);
            } else {
               units.insertBefore((Unit)newNotify, (Unit)endExitmonitor);
            }

            this.redirectTraps(b, endExitmonitor, newNotify);
            units.remove(endExitmonitor);
         }

         var45 = tn.waits.iterator();

         while(var45.hasNext()) {
            uWait = (Unit)var45.next();
            endExitmonitor = (Stmt)uWait;
            ((InstanceInvokeExpr)endExitmonitor.getInvokeExpr()).setBase(clo);
            if (newPrep != null) {
               units.insertBefore((Unit)((Stmt)newPrep.clone()), (Unit)endExitmonitor);
            }
         }
      }
   }

   public InstanceFieldRef reconstruct(Body b, PatchingChain<Unit> units, InstanceFieldRef lock, Stmt insertBefore, boolean redirect) {
      logger.debug("Reconstructing " + lock);
      if (!(lock.getBase() instanceof FakeJimpleLocal)) {
         logger.debug("  base is not a FakeJimpleLocal");
         return lock;
      } else {
         FakeJimpleLocal fakeBase = (FakeJimpleLocal)lock.getBase();
         if (!(fakeBase.getInfo() instanceof LockableReferenceAnalysis)) {
            throw new RuntimeException("InstanceFieldRef cannot be reconstructed due to missing LocksetAnalysis info: " + lock);
         } else {
            LockableReferenceAnalysis la = (LockableReferenceAnalysis)fakeBase.getInfo();
            EquivalentValue baseEqVal = la.baseFor(lock);
            if (baseEqVal == null) {
               throw new RuntimeException("InstanceFieldRef cannot be reconstructed due to lost base from Lockset");
            } else {
               Value base = baseEqVal.getValue();
               Local baseLocal;
               InstanceFieldRef newLock;
               if (base instanceof InstanceFieldRef) {
                  newLock = this.reconstruct(b, units, (InstanceFieldRef)base, insertBefore, redirect);
                  baseLocal = Jimple.v().newLocal("baseLocal" + baseLocalNum++, newLock.getType());
                  b.getLocals().add(baseLocal);
                  Stmt baseAssign = Jimple.v().newAssignStmt(baseLocal, newLock);
                  if (redirect) {
                     units.insertBefore((Unit)baseAssign, (Unit)insertBefore);
                  } else {
                     units.insertBeforeNoRedirect(baseAssign, insertBefore);
                  }
               } else {
                  if (!(base instanceof Local)) {
                     throw new RuntimeException("InstanceFieldRef cannot be reconstructed because it's base is of an unsupported type" + base.getType() + ": " + base);
                  }

                  baseLocal = (Local)base;
               }

               newLock = Jimple.v().newInstanceFieldRef(baseLocal, lock.getField().makeRef());
               logger.debug("  as " + newLock);
               return newLock;
            }
         }
      }
   }

   public static Value getLockFor(EquivalentValue lockEqVal) {
      Value lock = lockEqVal.getValue();
      if (lock instanceof InstanceFieldRef) {
         return lock;
      } else if (lock instanceof ArrayRef) {
         return ((ArrayRef)lock).getBase();
      } else if (lock instanceof Local) {
         return lock;
      } else if (!(lock instanceof StaticFieldRef) && !(lock instanceof NewStaticLock)) {
         throw new RuntimeException("Unknown type of lock (" + lock + "): expected FieldRef, ArrayRef, or Local");
      } else if (lockEqValToLock.containsKey(lockEqVal)) {
         return (Value)lockEqValToLock.get(lockEqVal);
      } else {
         SootClass lockClass = null;
         StaticFieldRef clinitMethod;
         if (lock instanceof StaticFieldRef) {
            clinitMethod = (StaticFieldRef)lock;
            lockClass = clinitMethod.getField().getDeclaringClass();
         } else if (lock instanceof NewStaticLock) {
            DeadlockAvoidanceEdge dae = (DeadlockAvoidanceEdge)lock;
            lockClass = dae.getLockClass();
         }

         clinitMethod = null;
         JimpleBody clinitBody = null;
         Stmt firstStmt = null;
         boolean addingNewClinit = !lockClass.declaresMethod("void <clinit>()");
         SootMethod clinitMethod;
         if (addingNewClinit) {
            clinitMethod = Scene.v().makeSootMethod("<clinit>", new ArrayList(), VoidType.v(), 9);
            clinitBody = Jimple.v().newBody(clinitMethod);
            clinitMethod.setActiveBody(clinitBody);
            lockClass.addMethod(clinitMethod);
         } else {
            clinitMethod = lockClass.getMethod("void <clinit>()");
            clinitBody = (JimpleBody)clinitMethod.getActiveBody();
            firstStmt = clinitBody.getFirstNonIdentityStmt();
         }

         PatchingChain<Unit> clinitUnits = clinitBody.getUnits();
         Local lockLocal = Jimple.v().newLocal("objectLockLocal" + lockNumber, RefType.v("java.lang.Object"));
         clinitBody.getLocals().add(lockLocal);
         Stmt newStmt = Jimple.v().newAssignStmt(lockLocal, Jimple.v().newNewExpr(RefType.v("java.lang.Object")));
         if (addingNewClinit) {
            clinitUnits.add((Unit)newStmt);
         } else {
            clinitUnits.insertBeforeNoRedirect(newStmt, firstStmt);
         }

         SootClass objectClass = Scene.v().loadClassAndSupport("java.lang.Object");
         RefType type = RefType.v(objectClass);
         SootMethod initMethod = objectClass.getMethod("void <init>()");
         Stmt initStmt = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(lockLocal, initMethod.makeRef(), Collections.EMPTY_LIST));
         if (addingNewClinit) {
            clinitUnits.add((Unit)initStmt);
         } else {
            clinitUnits.insertBeforeNoRedirect(initStmt, firstStmt);
         }

         SootField actualLockObject = Scene.v().makeSootField("objectLockGlobal" + lockNumber, RefType.v("java.lang.Object"), 9);
         ++lockNumber;
         lockClass.addField(actualLockObject);
         StaticFieldRef actualLockSfr = Jimple.v().newStaticFieldRef(actualLockObject.makeRef());
         Stmt assignStmt = Jimple.v().newAssignStmt(actualLockSfr, lockLocal);
         if (addingNewClinit) {
            clinitUnits.add((Unit)assignStmt);
         } else {
            clinitUnits.insertBeforeNoRedirect(assignStmt, firstStmt);
         }

         if (addingNewClinit) {
            clinitUnits.add((Unit)Jimple.v().newReturnVoidStmt());
         }

         lockEqValToLock.put(lockEqVal, actualLockSfr);
         return actualLockSfr;
      }
   }

   public void redirectTraps(Body b, Unit oldUnit, Unit newUnit) {
      Chain<Trap> traps = b.getTraps();
      Iterator var5 = traps.iterator();

      while(var5.hasNext()) {
         Trap trap = (Trap)var5.next();
         if (trap.getHandlerUnit() == oldUnit) {
            trap.setHandlerUnit(newUnit);
         }

         if (trap.getBeginUnit() == oldUnit) {
            trap.setBeginUnit(newUnit);
         }

         if (trap.getEndUnit() == oldUnit) {
            trap.setEndUnit(newUnit);
         }
      }

   }
}
