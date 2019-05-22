package soot.jimple.toolkits.thread.synchronization;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.NewExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.TransitiveTargets;
import soot.jimple.toolkits.pointer.CodeBlockRWSet;
import soot.jimple.toolkits.pointer.FullObjectSet;
import soot.jimple.toolkits.pointer.RWSet;
import soot.jimple.toolkits.pointer.SideEffectAnalysis;
import soot.jimple.toolkits.pointer.StmtRWSet;
import soot.jimple.toolkits.thread.EncapsulatedObjectAnalysis;
import soot.jimple.toolkits.thread.ThreadLocalObjectsAnalysis;

public class CriticalSectionAwareSideEffectAnalysis {
   PointsToAnalysis pa;
   CallGraph cg;
   Map<SootMethod, CodeBlockRWSet> methodToNTReadSet = new HashMap();
   Map<SootMethod, CodeBlockRWSet> methodToNTWriteSet = new HashMap();
   int rwsetcount = 0;
   CriticalSectionVisibleEdgesPred tve;
   TransitiveTargets tt;
   TransitiveTargets normaltt;
   SideEffectAnalysis normalsea;
   Collection<CriticalSection> criticalSections;
   EncapsulatedObjectAnalysis eoa;
   ThreadLocalObjectsAnalysis tlo;
   public Vector sigBlacklist;
   public Vector sigReadGraylist;
   public Vector sigWriteGraylist;
   public Vector subSigBlacklist;
   private HashMap<Stmt, RWSet> RCache = new HashMap();
   private HashMap<Stmt, RWSet> WCache = new HashMap();

   public void findNTRWSets(SootMethod method) {
      if (!this.methodToNTReadSet.containsKey(method) || !this.methodToNTWriteSet.containsKey(method)) {
         CodeBlockRWSet read = null;
         CodeBlockRWSet write = null;
         Iterator sIt = method.retrieveActiveBody().getUnits().iterator();

         while(true) {
            Stmt s;
            Local base;
            do {
               InvokeExpr ie;
               SootMethod calledMethod;
               do {
                  do {
                     boolean ignore;
                     label76:
                     do {
                        if (!sIt.hasNext()) {
                           this.methodToNTReadSet.put(method, read);
                           this.methodToNTWriteSet.put(method, write);
                           return;
                        }

                        s = (Stmt)sIt.next();
                        ignore = false;
                        if (this.criticalSections != null) {
                           Iterator tnIt = this.criticalSections.iterator();

                           CriticalSection tn;
                           do {
                              if (!tnIt.hasNext()) {
                                 continue label76;
                              }

                              tn = (CriticalSection)tnIt.next();
                           } while(!tn.units.contains(s) && tn.prepStmt != s);

                           ignore = true;
                        }
                     } while(ignore);

                     RWSet ntr = this.ntReadSet(method, s);
                     if (ntr != null) {
                        if (read == null) {
                           read = new CodeBlockRWSet();
                        }

                        read.union(ntr);
                     }

                     RWSet ntw = this.ntWriteSet(method, s);
                     if (ntw != null) {
                        if (write == null) {
                           write = new CodeBlockRWSet();
                        }

                        write.union(ntw);
                     }
                  } while(!s.containsInvokeExpr());

                  ie = s.getInvokeExpr();
                  calledMethod = ie.getMethod();
               } while(!calledMethod.getDeclaringClass().toString().startsWith("java.util") && !calledMethod.getDeclaringClass().toString().startsWith("java.lang"));

               base = null;
               if (ie instanceof InstanceInvokeExpr) {
                  base = (Local)((InstanceInvokeExpr)ie).getBase();
               }
            } while(this.tlo != null && base != null && this.tlo.isObjectThreadLocal(base, method));

            RWSet r = this.approximatedReadSet(method, s, base, true);
            if (read == null) {
               read = new CodeBlockRWSet();
            }

            if (r != null) {
               read.union(r);
            }

            RWSet w = this.approximatedWriteSet(method, s, base, true);
            if (write == null) {
               write = new CodeBlockRWSet();
            }

            if (w != null) {
               write.union(w);
            }
         }
      }
   }

   public void setExemptTransaction(CriticalSection tn) {
      this.tve.setExemptTransaction(tn);
   }

   public RWSet nonTransitiveReadSet(SootMethod method) {
      this.findNTRWSets(method);
      return (RWSet)this.methodToNTReadSet.get(method);
   }

   public RWSet nonTransitiveWriteSet(SootMethod method) {
      this.findNTRWSets(method);
      return (RWSet)this.methodToNTWriteSet.get(method);
   }

   public CriticalSectionAwareSideEffectAnalysis(PointsToAnalysis pa, CallGraph cg, Collection<CriticalSection> criticalSections, ThreadLocalObjectsAnalysis tlo) {
      this.pa = pa;
      this.cg = cg;
      this.tve = new CriticalSectionVisibleEdgesPred(criticalSections);
      this.tt = new TransitiveTargets(cg, new Filter(this.tve));
      this.normaltt = new TransitiveTargets(cg, (Filter)null);
      this.normalsea = new SideEffectAnalysis(pa, cg);
      this.criticalSections = criticalSections;
      this.eoa = new EncapsulatedObjectAnalysis();
      this.tlo = tlo;
      this.sigBlacklist = new Vector();
      this.sigReadGraylist = new Vector();
      this.sigWriteGraylist = new Vector();
      this.subSigBlacklist = new Vector();
   }

   private RWSet ntReadSet(SootMethod method, Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt a = (AssignStmt)stmt;
         Value r = a.getRightOp();
         return r instanceof NewExpr ? null : this.addValue(r, method, stmt);
      } else {
         return null;
      }
   }

   public RWSet approximatedReadSet(SootMethod method, Stmt stmt, Value specialRead, boolean allFields) {
      CodeBlockRWSet ret = new CodeBlockRWSet();
      if (specialRead != null) {
         if (specialRead instanceof Local) {
            Local vLocal = (Local)specialRead;
            PointsToSet base = this.pa.reachingObjects(vLocal);
            Type pType = vLocal.getType();
            Iterator fieldsIt;
            if (pType instanceof RefType) {
               SootClass baseTypeClass = ((RefType)pType).getSootClass();
               if (!baseTypeClass.isInterface()) {
                  List<SootClass> baseClasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(baseTypeClass);
                  if (!baseClasses.contains(RefType.v("java.lang.Exception").getSootClass())) {
                     fieldsIt = baseClasses.iterator();

                     while(fieldsIt.hasNext()) {
                        SootClass baseClass = (SootClass)fieldsIt.next();
                        Iterator baseFieldIt = baseClass.getFields().iterator();

                        while(baseFieldIt.hasNext()) {
                           SootField baseField = (SootField)baseFieldIt.next();
                           if (!baseField.isStatic()) {
                              ret.addFieldRef(base, baseField);
                           }
                        }
                     }
                  }
               }
            }

            if (!allFields) {
               CodeBlockRWSet allRW = ret;
               ret = new CodeBlockRWSet();
               RWSet normalRW;
               if (this.RCache.containsKey(stmt)) {
                  normalRW = (RWSet)this.RCache.get(stmt);
               } else {
                  normalRW = this.normalsea.readSet(method, stmt);
                  this.RCache.put(stmt, normalRW);
               }

               if (normalRW != null) {
                  fieldsIt = normalRW.getFields().iterator();

                  while(fieldsIt.hasNext()) {
                     Object field = fieldsIt.next();
                     if (allRW.containsField(field)) {
                        PointsToSet otherBase = normalRW.getBaseForField(field);
                        if (otherBase instanceof FullObjectSet) {
                           ret.addFieldRef(otherBase, field);
                        } else if (base.hasNonEmptyIntersection(otherBase)) {
                           ret.addFieldRef(base, field);
                        }
                     }
                  }
               }
            }
         } else if (specialRead instanceof FieldRef) {
            ret.union(this.addValue(specialRead, method, stmt));
         }
      }

      if (stmt.containsInvokeExpr()) {
         int argCount = stmt.getInvokeExpr().getArgCount();

         for(int i = 0; i < argCount; ++i) {
            ret.union(this.addValue(stmt.getInvokeExpr().getArg(i), method, stmt));
         }
      }

      if (stmt instanceof AssignStmt) {
         AssignStmt a = (AssignStmt)stmt;
         Value r = a.getRightOp();
         ret.union(this.addValue(r, method, stmt));
      }

      return ret;
   }

   public RWSet readSet(SootMethod method, Stmt stmt, CriticalSection tn, Set uses) {
      boolean ignore = false;
      if (stmt.containsInvokeExpr()) {
         InvokeExpr ie = stmt.getInvokeExpr();
         SootMethod calledMethod = ie.getMethod();
         if (!(ie instanceof StaticInvokeExpr) && ie instanceof InstanceInvokeExpr) {
            if (calledMethod.getSubSignature().startsWith("void <init>") && this.eoa.isInitMethodPureOnObject(calledMethod)) {
               ignore = true;
            } else if (this.tlo != null && !this.tlo.hasNonThreadLocalEffects(method, ie)) {
               ignore = true;
            }
         }
      }

      boolean inaccessibleUses = false;
      RWSet ret = new CodeBlockRWSet();
      this.tve.setExemptTransaction(tn);
      Iterator targets = this.tt.iterator((Unit)stmt);

      while(!ignore && targets.hasNext()) {
         SootMethod target = (SootMethod)targets.next();
         if (target.isConcrete() && !target.getDeclaringClass().toString().startsWith("java.util") && !target.getDeclaringClass().toString().startsWith("java.lang")) {
            RWSet ntr = this.nonTransitiveReadSet(target);
            if (ntr != null) {
               ret.union(ntr);
            }
         }
      }

      RWSet ntr = this.ntReadSet(method, stmt);
      if (!inaccessibleUses && ntr != null && stmt instanceof AssignStmt) {
         AssignStmt a = (AssignStmt)stmt;
         Value r = a.getRightOp();
         if (r instanceof InstanceFieldRef) {
            uses.add(((InstanceFieldRef)r).getBase());
         } else if (r instanceof StaticFieldRef) {
            uses.add(r);
         } else if (r instanceof ArrayRef) {
            uses.add(((ArrayRef)r).getBase());
         }
      }

      ret.union(ntr);
      if (stmt.containsInvokeExpr()) {
         InvokeExpr ie = stmt.getInvokeExpr();
         SootMethod calledMethod = ie.getMethod();
         if (calledMethod.getDeclaringClass().toString().startsWith("java.util") || calledMethod.getDeclaringClass().toString().startsWith("java.lang")) {
            Local base = null;
            if (ie instanceof InstanceInvokeExpr) {
               base = (Local)((InstanceInvokeExpr)ie).getBase();
            }

            if (this.tlo == null || base == null || !this.tlo.isObjectThreadLocal(base, method)) {
               RWSet r = this.approximatedReadSet(method, stmt, base, true);
               if (r != null) {
                  ret.union(r);
               }

               int argCount = stmt.getInvokeExpr().getArgCount();

               for(int i = 0; i < argCount; ++i) {
                  uses.add(ie.getArg(i));
               }

               if (base != null) {
                  uses.add(base);
               }
            }
         }
      }

      return ret;
   }

   private RWSet ntWriteSet(SootMethod method, Stmt stmt) {
      if (stmt instanceof AssignStmt) {
         AssignStmt a = (AssignStmt)stmt;
         Value l = a.getLeftOp();
         return this.addValue(l, method, stmt);
      } else {
         return null;
      }
   }

   public RWSet approximatedWriteSet(SootMethod method, Stmt stmt, Value v, boolean allFields) {
      CodeBlockRWSet ret = new CodeBlockRWSet();
      if (v != null) {
         if (v instanceof Local) {
            Local vLocal = (Local)v;
            PointsToSet base = this.pa.reachingObjects(vLocal);
            Type pType = vLocal.getType();
            Iterator fieldsIt;
            if (pType instanceof RefType) {
               SootClass baseTypeClass = ((RefType)pType).getSootClass();
               if (!baseTypeClass.isInterface()) {
                  List<SootClass> baseClasses = Scene.v().getActiveHierarchy().getSuperclassesOfIncluding(baseTypeClass);
                  if (!baseClasses.contains(RefType.v("java.lang.Exception").getSootClass())) {
                     fieldsIt = baseClasses.iterator();

                     while(fieldsIt.hasNext()) {
                        SootClass baseClass = (SootClass)fieldsIt.next();
                        Iterator baseFieldIt = baseClass.getFields().iterator();

                        while(baseFieldIt.hasNext()) {
                           SootField baseField = (SootField)baseFieldIt.next();
                           if (!baseField.isStatic()) {
                              ret.addFieldRef(base, baseField);
                           }
                        }
                     }
                  }
               }
            }

            if (!allFields) {
               CodeBlockRWSet allRW = ret;
               ret = new CodeBlockRWSet();
               RWSet normalRW;
               if (this.WCache.containsKey(stmt)) {
                  normalRW = (RWSet)this.WCache.get(stmt);
               } else {
                  normalRW = this.normalsea.writeSet(method, stmt);
                  this.WCache.put(stmt, normalRW);
               }

               if (normalRW != null) {
                  fieldsIt = normalRW.getFields().iterator();

                  while(fieldsIt.hasNext()) {
                     Object field = fieldsIt.next();
                     if (allRW.containsField(field)) {
                        PointsToSet otherBase = normalRW.getBaseForField(field);
                        if (otherBase instanceof FullObjectSet) {
                           ret.addFieldRef(otherBase, field);
                        } else if (base.hasNonEmptyIntersection(otherBase)) {
                           ret.addFieldRef(base, field);
                        }
                     }
                  }
               }
            }
         } else if (v instanceof FieldRef) {
            ret.union(this.addValue(v, method, stmt));
         }
      }

      if (stmt instanceof AssignStmt) {
         AssignStmt a = (AssignStmt)stmt;
         Value l = a.getLeftOp();
         ret.union(this.addValue(l, method, stmt));
      }

      return ret;
   }

   public RWSet writeSet(SootMethod method, Stmt stmt, CriticalSection tn, Set uses) {
      boolean ignore = false;
      if (stmt.containsInvokeExpr()) {
         InvokeExpr ie = stmt.getInvokeExpr();
         SootMethod calledMethod = ie.getMethod();
         if (!(ie instanceof StaticInvokeExpr) && ie instanceof InstanceInvokeExpr) {
            if (calledMethod.getSubSignature().startsWith("void <init>") && this.eoa.isInitMethodPureOnObject(calledMethod)) {
               ignore = true;
            } else if (this.tlo != null && !this.tlo.hasNonThreadLocalEffects(method, ie)) {
               ignore = true;
            }
         }
      }

      boolean inaccessibleUses = false;
      RWSet ret = new CodeBlockRWSet();
      this.tve.setExemptTransaction(tn);
      Iterator targets = this.tt.iterator((Unit)stmt);

      while(!ignore && targets.hasNext()) {
         SootMethod target = (SootMethod)targets.next();
         if (target.isConcrete() && !target.getDeclaringClass().toString().startsWith("java.util") && !target.getDeclaringClass().toString().startsWith("java.lang")) {
            RWSet ntw = this.nonTransitiveWriteSet(target);
            if (ntw != null) {
               ret.union(ntw);
            }
         }
      }

      RWSet ntw = this.ntWriteSet(method, stmt);
      if (!inaccessibleUses && ntw != null && stmt instanceof AssignStmt) {
         AssignStmt a = (AssignStmt)stmt;
         Value l = a.getLeftOp();
         if (l instanceof InstanceFieldRef) {
            uses.add(((InstanceFieldRef)l).getBase());
         } else if (l instanceof StaticFieldRef) {
            uses.add(l);
         } else if (l instanceof ArrayRef) {
            uses.add(((ArrayRef)l).getBase());
         }
      }

      ret.union(ntw);
      if (stmt.containsInvokeExpr()) {
         InvokeExpr ie = stmt.getInvokeExpr();
         SootMethod calledMethod = ie.getMethod();
         if (calledMethod.getDeclaringClass().toString().startsWith("java.util") || calledMethod.getDeclaringClass().toString().startsWith("java.lang")) {
            Local base = null;
            if (ie instanceof InstanceInvokeExpr) {
               base = (Local)((InstanceInvokeExpr)ie).getBase();
            }

            if (this.tlo == null || base == null || !this.tlo.isObjectThreadLocal(base, method)) {
               RWSet w = this.approximatedWriteSet(method, stmt, base, true);
               if (w != null) {
                  ret.union(w);
               }

               if (base != null) {
                  uses.add(base);
               }
            }
         }
      }

      return ret;
   }

   public RWSet valueRWSet(Value v, SootMethod m, Stmt s, CriticalSection tn) {
      RWSet ret = null;
      InstanceFieldRef ifr;
      if (this.tlo != null) {
         if (v instanceof InstanceFieldRef) {
            ifr = (InstanceFieldRef)v;
            if (m.isConcrete() && !m.isStatic() && m.retrieveActiveBody().getThisLocal().equivTo(ifr.getBase()) && this.tlo.isObjectThreadLocal(ifr, m)) {
               return null;
            }

            if (this.tlo.isObjectThreadLocal(ifr.getBase(), m)) {
               return null;
            }
         } else if (v instanceof ArrayRef && this.tlo.isObjectThreadLocal(((ArrayRef)v).getBase(), m)) {
            return null;
         }
      }

      PointsToSet base;
      if (v instanceof InstanceFieldRef) {
         ifr = (InstanceFieldRef)v;
         base = this.pa.reachingObjects((Local)ifr.getBase());
         ret = new StmtRWSet();
         ((RWSet)ret).addFieldRef(base, ifr.getField());
      } else if (v instanceof StaticFieldRef) {
         StaticFieldRef sfr = (StaticFieldRef)v;
         ret = new StmtRWSet();
         ((RWSet)ret).addGlobal(sfr.getField());
      } else if (v instanceof ArrayRef) {
         ArrayRef ar = (ArrayRef)v;
         base = this.pa.reachingObjects((Local)ar.getBase());
         ret = new StmtRWSet();
         ((RWSet)ret).addFieldRef(base, "ARRAY_ELEMENTS_NODE");
      } else {
         if (!(v instanceof Local)) {
            return null;
         }

         Local vLocal = (Local)v;
         base = this.pa.reachingObjects(vLocal);
         ret = new CodeBlockRWSet();
         CodeBlockRWSet stmtRW = new CodeBlockRWSet();
         RWSet rSet = this.readSet(m, s, tn, new HashSet());
         if (rSet != null) {
            stmtRW.union(rSet);
         }

         RWSet wSet = this.writeSet(m, s, tn, new HashSet());
         if (wSet != null) {
            stmtRW.union(wSet);
         }

         Iterator fieldsIt = stmtRW.getFields().iterator();

         while(fieldsIt.hasNext()) {
            Object field = fieldsIt.next();
            PointsToSet fieldBase = stmtRW.getBaseForField(field);
            if (base.hasNonEmptyIntersection(fieldBase)) {
               ((RWSet)ret).addFieldRef(base, field);
            }
         }
      }

      return (RWSet)ret;
   }

   protected RWSet addValue(Value v, SootMethod m, Stmt s) {
      RWSet ret = null;
      InstanceFieldRef ifr;
      if (this.tlo != null) {
         if (v instanceof InstanceFieldRef) {
            ifr = (InstanceFieldRef)v;
            if (m.isConcrete() && !m.isStatic() && m.retrieveActiveBody().getThisLocal().equivTo(ifr.getBase()) && this.tlo.isObjectThreadLocal(ifr, m)) {
               return null;
            }

            if (this.tlo.isObjectThreadLocal(ifr.getBase(), m)) {
               return null;
            }
         } else if (v instanceof ArrayRef && this.tlo.isObjectThreadLocal(((ArrayRef)v).getBase(), m)) {
            return null;
         }
      }

      if (v instanceof InstanceFieldRef) {
         ifr = (InstanceFieldRef)v;
         Local baseLocal = (Local)ifr.getBase();
         PointsToSet base = this.pa.reachingObjects(baseLocal);
         if (baseLocal.getType() instanceof RefType) {
            SootClass baseClass = ((RefType)baseLocal.getType()).getSootClass();
            if (Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(baseClass, RefType.v("java.lang.Exception").getSootClass())) {
               return null;
            }
         }

         ret = new StmtRWSet();
         ret.addFieldRef(base, ifr.getField());
      } else if (v instanceof StaticFieldRef) {
         StaticFieldRef sfr = (StaticFieldRef)v;
         ret = new StmtRWSet();
         ret.addGlobal(sfr.getField());
      } else if (v instanceof ArrayRef) {
         ArrayRef ar = (ArrayRef)v;
         PointsToSet base = this.pa.reachingObjects((Local)ar.getBase());
         ret = new StmtRWSet();
         ret.addFieldRef(base, "ARRAY_ELEMENTS_NODE");
      }

      return ret;
   }

   public String toString() {
      return "TransactionAwareSideEffectAnalysis: PA=" + this.pa + " CG=" + this.cg;
   }
}
