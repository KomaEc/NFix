package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.EquivalentValue;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AnyNewExpr;
import soot.jimple.ArrayRef;
import soot.jimple.CastExpr;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.ParameterRef;
import soot.jimple.Ref;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.infoflow.FakeJimpleLocal;
import soot.jimple.toolkits.pointer.CodeBlockRWSet;
import soot.jimple.toolkits.pointer.RWSet;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.BackwardFlowAnalysis;

public class LockableReferenceAnalysis extends BackwardFlowAnalysis<Unit, LocksetFlowInfo> {
   private static final Logger logger = LoggerFactory.getLogger(LockableReferenceAnalysis.class);
   UnitGraph graph;
   SootMethod method;
   CriticalSectionAwareSideEffectAnalysis tasea;
   RWSet contributingRWSet;
   CriticalSection tn;
   Stmt begin;
   boolean lostObjects;
   Map<Ref, EquivalentValue> refToBase;
   Map<Ref, EquivalentValue> refToIndex;
   static Set<SootMethod> analyzing = new HashSet();
   static int groupNum = 1;

   public LockableReferenceAnalysis(UnitGraph g) {
      super(g);
      this.graph = g;
      this.method = g.getBody().getMethod();
      this.contributingRWSet = null;
      this.tn = null;
      this.begin = null;
      this.lostObjects = false;
      this.refToBase = new HashMap();
      this.refToIndex = new HashMap();
   }

   public void printMsg(String msg) {
      logger.debug("[wjtp.tn] ");

      for(int i = 0; i < analyzing.size() - 1; ++i) {
         logger.debug("  ");
      }

      logger.debug("" + msg);
   }

   public List<EquivalentValue> getLocksetOf(CriticalSectionAwareSideEffectAnalysis tasea, RWSet contributingRWSet, CriticalSection tn) {
      analyzing.add(this.method);
      this.tasea = tasea;
      tasea.setExemptTransaction(tn);
      this.contributingRWSet = contributingRWSet;
      this.tn = tn;
      this.begin = tn == null ? null : tn.beginning;
      this.lostObjects = false;
      this.doAnalysis();
      if (this.lostObjects) {
         this.printMsg("Failed lockset:");
         analyzing.remove(this.method);
         return null;
      } else {
         List<EquivalentValue> lockset = new ArrayList();
         LocksetFlowInfo resultsInfo = null;
         Map<EquivalentValue, Integer> results = null;
         Unit u;
         if (this.begin == null) {
            for(Iterator var7 = this.graph.iterator(); var7.hasNext(); resultsInfo = (LocksetFlowInfo)this.getFlowBefore(u)) {
               u = (Unit)var7.next();
            }
         } else {
            resultsInfo = (LocksetFlowInfo)this.getFlowBefore(this.begin);
         }

         if (resultsInfo == null) {
            analyzing.remove(this.method);
            throw new RuntimeException("Why is getFlowBefore null???");
         } else {
            results = resultsInfo.groups;
            Map<Integer, List<EquivalentValue>> reversed = new HashMap();

            EquivalentValue bestLock;
            Integer group;
            Object keys;
            Iterator var15;
            for(var15 = results.entrySet().iterator(); var15.hasNext(); ((List)keys).add(bestLock)) {
               java.util.Map.Entry<EquivalentValue, Integer> e = (java.util.Map.Entry)var15.next();
               bestLock = (EquivalentValue)e.getKey();
               group = (Integer)e.getValue();
               if (!reversed.containsKey(group)) {
                  keys = new ArrayList();
                  reversed.put(group, keys);
               } else {
                  keys = (List)reversed.get(group);
               }
            }

            var15 = reversed.values().iterator();

            label92:
            while(var15.hasNext()) {
               List<EquivalentValue> objects = (List)var15.next();
               bestLock = null;
               Iterator var17 = objects.iterator();

               while(true) {
                  EquivalentValue object;
                  do {
                     if (!var17.hasNext()) {
                        group = (Integer)results.get(bestLock);
                        Iterator var19 = resultsInfo.refToBaseGroup.keySet().iterator();

                        Ref ref;
                        while(var19.hasNext()) {
                           ref = (Ref)var19.next();
                           if (group == resultsInfo.refToBaseGroup.get(ref)) {
                              this.refToBase.put(ref, bestLock);
                           }
                        }

                        var19 = resultsInfo.refToIndexGroup.keySet().iterator();

                        while(var19.hasNext()) {
                           ref = (Ref)var19.next();
                           if (group == resultsInfo.refToIndexGroup.get(ref)) {
                              this.refToIndex.put(ref, bestLock);
                           }
                        }

                        if (group >= 0) {
                           lockset.add(bestLock);
                        }
                        continue label92;
                     }

                     object = (EquivalentValue)var17.next();
                  } while(bestLock != null && !(object.getValue() instanceof IdentityRef) && (!(object.getValue() instanceof Ref) || bestLock instanceof IdentityRef));

                  bestLock = object;
               }
            }

            if (lockset.size() == 0) {
               this.printMsg("Empty lockset: S" + lockset.size() + "/G" + reversed.keySet().size() + "/O" + results.keySet().size() + " Method:" + this.method + " Begin:" + this.begin + " Result:" + results + " RW:" + contributingRWSet);
               this.printMsg("|= results:" + results + " refToBaseGroup:" + resultsInfo.refToBaseGroup);
            } else {
               this.printMsg("Healthy lockset: S" + lockset.size() + "/G" + reversed.keySet().size() + "/O" + results.keySet().size() + " " + lockset + " refToBase:" + this.refToBase + " refToIndex:" + this.refToIndex);
               this.printMsg("|= results:" + results + " refToBaseGroup:" + resultsInfo.refToBaseGroup);
            }

            analyzing.remove(this.method);
            return lockset;
         }
      }
   }

   public EquivalentValue baseFor(Ref ref) {
      return (EquivalentValue)this.refToBase.get(ref);
   }

   public EquivalentValue indexFor(Ref ref) {
      return (EquivalentValue)this.refToIndex.get(ref);
   }

   protected void merge(LocksetFlowInfo in1, LocksetFlowInfo in2, LocksetFlowInfo out) {
      LocksetFlowInfo tmpInfo = new LocksetFlowInfo();
      this.copy(in1, out);
      this.copy(in2, tmpInfo);
      Iterator var5 = tmpInfo.groups.keySet().iterator();

      while(true) {
         while(var5.hasNext()) {
            EquivalentValue key = (EquivalentValue)var5.next();
            Integer newvalue = (Integer)tmpInfo.groups.get(key);
            if (!out.groups.containsKey(key)) {
               out.groups.put(key, newvalue);
            } else if (out.groups.get(key) != tmpInfo.groups.get(key)) {
               Object oldvalue = out.groups.get(key);
               Iterator var9 = out.groups.entrySet().iterator();

               java.util.Map.Entry entry;
               while(var9.hasNext()) {
                  entry = (java.util.Map.Entry)var9.next();
                  if (entry.getValue() == oldvalue) {
                     entry.setValue(newvalue);
                  }
               }

               var9 = tmpInfo.groups.entrySet().iterator();

               while(var9.hasNext()) {
                  entry = (java.util.Map.Entry)var9.next();
                  if (entry.getValue() == oldvalue) {
                     entry.setValue(newvalue);
                  }
               }

               var9 = out.refToBaseGroup.entrySet().iterator();

               while(var9.hasNext()) {
                  entry = (java.util.Map.Entry)var9.next();
                  if (entry.getValue() == oldvalue) {
                     entry.setValue(newvalue);
                  }
               }

               var9 = out.refToIndexGroup.entrySet().iterator();

               while(var9.hasNext()) {
                  entry = (java.util.Map.Entry)var9.next();
                  if (entry.getValue() == oldvalue) {
                     entry.setValue(newvalue);
                  }
               }

               var9 = tmpInfo.refToBaseGroup.entrySet().iterator();

               while(var9.hasNext()) {
                  entry = (java.util.Map.Entry)var9.next();
                  if (entry.getValue() == oldvalue) {
                     entry.setValue(newvalue);
                  }
               }

               var9 = tmpInfo.refToIndexGroup.entrySet().iterator();

               while(var9.hasNext()) {
                  entry = (java.util.Map.Entry)var9.next();
                  if (entry.getValue() == oldvalue) {
                     entry.setValue(newvalue);
                  }
               }
            }
         }

         var5 = tmpInfo.refToBaseGroup.keySet().iterator();

         Ref ref;
         while(var5.hasNext()) {
            ref = (Ref)var5.next();
            if (!out.refToBaseGroup.containsKey(ref)) {
               out.refToBaseGroup.put(ref, tmpInfo.refToBaseGroup.get(ref));
            }
         }

         var5 = tmpInfo.refToIndexGroup.keySet().iterator();

         while(var5.hasNext()) {
            ref = (Ref)var5.next();
            if (!out.refToIndexGroup.containsKey(ref)) {
               out.refToIndexGroup.put(ref, tmpInfo.refToIndexGroup.get(ref));
            }
         }

         return;
      }
   }

   public Integer addFromSubanalysis(LocksetFlowInfo outInfo, LockableReferenceAnalysis la, Stmt stmt, Value lock) {
      Map<EquivalentValue, Integer> out = outInfo.groups;
      InvokeExpr ie = stmt.getInvokeExpr();
      this.printMsg("Attempting to bring up '" + lock + "' from inner lockset at (" + stmt.hashCode() + ") " + stmt);
      int newGroup;
      Value use;
      if (lock instanceof ThisRef && ie instanceof InstanceInvokeExpr) {
         use = ((InstanceInvokeExpr)ie).getBase();
         if (!out.containsKey(new EquivalentValue(use))) {
            newGroup = groupNum++;
            out.put(new EquivalentValue(use), newGroup);
            return newGroup;
         } else {
            return (Integer)out.get(new EquivalentValue(use));
         }
      } else if (lock instanceof ParameterRef) {
         use = ie.getArg(((ParameterRef)lock).getIndex());
         if (!out.containsKey(new EquivalentValue(use))) {
            newGroup = groupNum++;
            out.put(new EquivalentValue(use), newGroup);
            return newGroup;
         } else {
            return (Integer)out.get(new EquivalentValue(use));
         }
      } else if (lock instanceof StaticFieldRef) {
         if (!out.containsKey(new EquivalentValue(lock))) {
            newGroup = groupNum++;
            out.put(new EquivalentValue(lock), newGroup);
            return newGroup;
         } else {
            return (Integer)out.get(new EquivalentValue(lock));
         }
      } else {
         EquivalentValue baseEqVal;
         if (lock instanceof InstanceFieldRef) {
            if (((InstanceFieldRef)lock).getBase() instanceof FakeJimpleLocal) {
               ((FakeJimpleLocal)((InstanceFieldRef)lock).getBase()).setInfo(this);
            }

            baseEqVal = la.baseFor((Ref)lock);
            if (baseEqVal == null) {
               this.printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ previously lost base) at " + stmt);
               return 0;
            } else {
               Value base = baseEqVal.getValue();
               Integer baseGroup = this.addFromSubanalysis(outInfo, la, stmt, base);
               if (baseGroup == 0) {
                  this.printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ newly lost base) at " + stmt);
                  return 0;
               } else {
                  outInfo.refToBaseGroup.put((Ref)lock, baseGroup);
                  if (!out.containsKey(new EquivalentValue(lock))) {
                     int newGroup = groupNum++;
                     out.put(new EquivalentValue(lock), newGroup);
                     return newGroup;
                  } else {
                     return (Integer)out.get(new EquivalentValue(lock));
                  }
               }
            }
         } else if (lock instanceof ArrayRef) {
            if (((ArrayRef)lock).getBase() instanceof FakeJimpleLocal) {
               ((FakeJimpleLocal)((ArrayRef)lock).getBase()).setInfo(this);
            }

            if (((ArrayRef)lock).getIndex() instanceof FakeJimpleLocal) {
               ((FakeJimpleLocal)((ArrayRef)lock).getIndex()).setInfo(this);
            }

            baseEqVal = la.baseFor((Ref)lock);
            EquivalentValue indexEqVal = la.indexFor((Ref)lock);
            if (baseEqVal == null) {
               this.printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ previously lost base) at " + stmt);
               return 0;
            } else if (indexEqVal == null) {
               this.printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ previously lost index) at " + stmt);
               return 0;
            } else {
               Value base = baseEqVal.getValue();
               Value index = indexEqVal.getValue();
               Integer baseGroup = this.addFromSubanalysis(outInfo, la, stmt, base);
               if (baseGroup == 0) {
                  this.printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ newly lost base) at " + stmt);
                  return 0;
               } else {
                  Integer indexGroup = this.addFromSubanalysis(outInfo, la, stmt, index);
                  if (indexGroup == 0) {
                     this.printMsg("Lost Object from inner Lockset (InstanceFieldRef w/ newly lost index) at " + stmt);
                     return 0;
                  } else {
                     outInfo.refToBaseGroup.put((Ref)lock, baseGroup);
                     outInfo.refToIndexGroup.put((Ref)lock, indexGroup);
                     if (!out.containsKey(new EquivalentValue(lock))) {
                        int newGroup = groupNum++;
                        out.put(new EquivalentValue(lock), newGroup);
                        return newGroup;
                     } else {
                        return (Integer)out.get(new EquivalentValue(lock));
                     }
                  }
               }
            }
         } else if (lock instanceof Constant) {
            if (!out.containsKey(new EquivalentValue(lock))) {
               newGroup = groupNum++;
               out.put(new EquivalentValue(lock), newGroup);
               return newGroup;
            } else {
               return (Integer)out.get(new EquivalentValue(lock));
            }
         } else {
            this.printMsg("Lost Object from inner Lockset (unknown or unhandled object type) at " + stmt);
            return 0;
         }
      }
   }

   protected void flowThrough(LocksetFlowInfo inInfo, Unit u, LocksetFlowInfo outInfo) {
      this.copy(inInfo, outInfo);
      Stmt stmt = (Stmt)u;
      Map<EquivalentValue, Integer> out = outInfo.groups;
      Iterator allUsesIt;
      Object use;
      ArrayRef ar;
      if ((this.tn == null || this.tn.units.contains(stmt)) && !this.lostObjects) {
         CodeBlockRWSet stmtRW = null;
         Set<Value> allUses = new HashSet();
         RWSet stmtRead = this.tasea.readSet(this.method, stmt, this.tn, allUses);
         if (stmtRead != null) {
            stmtRW = (CodeBlockRWSet)stmtRead;
         }

         RWSet stmtWrite = this.tasea.writeSet(this.method, stmt, this.tn, allUses);
         if (stmtWrite != null) {
            if (stmtRW != null) {
               stmtRW.union(stmtWrite);
            } else {
               stmtRW = (CodeBlockRWSet)stmtWrite;
            }
         }

         if (stmtRW != null && stmtRW.hasNonEmptyIntersection(this.contributingRWSet)) {
            List<Value> uses = new ArrayList();
            allUsesIt = allUses.iterator();

            while(allUsesIt.hasNext()) {
               Value vEqVal = (Value)allUsesIt.next();
               use = vEqVal;
               if (stmt.containsFieldRef()) {
                  FieldRef fr = stmt.getFieldRef();
                  if (fr instanceof InstanceFieldRef && ((InstanceFieldRef)fr).getBase() == vEqVal) {
                     use = fr;
                  }
               }

               if (stmt.containsArrayRef()) {
                  ar = stmt.getArrayRef();
                  if (ar.getBase() == use) {
                     use = ar;
                  }
               }

               RWSet valRW = this.tasea.valueRWSet((Value)use, this.method, stmt, this.tn);
               if (valRW != null && valRW.hasNonEmptyIntersection(this.contributingRWSet)) {
                  uses.add(vEqVal);
               }
            }

            if (stmt.containsInvokeExpr()) {
               InvokeExpr ie = stmt.getInvokeExpr();
               SootMethod called = ie.getMethod();
               if (called.isConcrete()) {
                  if (!called.getDeclaringClass().toString().startsWith("java.util") && !called.getDeclaringClass().toString().startsWith("java.lang")) {
                     if (!analyzing.contains(called)) {
                        LockableReferenceAnalysis la = new LockableReferenceAnalysis(new BriefUnitGraph(called.retrieveActiveBody()));
                        List<EquivalentValue> innerLockset = la.getLocksetOf(this.tasea, stmtRW, (CriticalSection)null);
                        if (innerLockset != null && innerLockset.size() > 0) {
                           this.printMsg("innerLockset: " + innerLockset.toString());
                           Iterator var16 = innerLockset.iterator();

                           while(var16.hasNext()) {
                              EquivalentValue lockEqVal = (EquivalentValue)var16.next();
                              Value lock = lockEqVal.getValue();
                              if (this.addFromSubanalysis(outInfo, la, stmt, lock) == 0) {
                                 this.lostObjects = true;
                                 this.printMsg("Lost Object in addFromSubanalysis()");
                                 break;
                              }
                           }
                        } else {
                           this.printMsg("innerLockset: " + (innerLockset == null ? "Lost Objects" : "Mysteriously Empty"));
                           this.lostObjects = true;
                        }
                     } else {
                        this.lostObjects = true;
                        this.printMsg("Lost Object due to recursion " + stmt);
                     }
                  } else if (uses.size() <= 0) {
                     this.printMsg("Lost Object at library call at " + stmt);
                     this.lostObjects = true;
                  }
               } else if (uses.size() <= 0) {
                  this.lostObjects = true;
                  this.printMsg("Lost Object from non-concrete method call at " + stmt);
               }
            } else if (uses.size() <= 0) {
               this.lostObjects = true;
               this.printMsg("Lost Object SOMEHOW at " + stmt);
            }

            Iterator usesIt = uses.iterator();

            while(usesIt.hasNext() && !this.lostObjects) {
               use = (Value)usesIt.next();
               Local oldbase;
               if (use instanceof InstanceFieldRef) {
                  InstanceFieldRef ifr = (InstanceFieldRef)use;
                  oldbase = (Local)ifr.getBase();
                  if (!(oldbase instanceof FakeJimpleLocal)) {
                     Local newbase = new FakeJimpleLocal("fakethis", oldbase.getType(), oldbase, this);
                     Value node = Jimple.v().newInstanceFieldRef(newbase, ifr.getField().makeRef());
                     new EquivalentValue(node);
                     use = node;
                  }
               } else if (use instanceof ArrayRef) {
                  ar = (ArrayRef)use;
                  oldbase = (Local)ar.getBase();
                  Value oldindex = ar.getIndex();
                  if (!(oldbase instanceof FakeJimpleLocal)) {
                     Local newbase = new FakeJimpleLocal("fakethis", oldbase.getType(), oldbase, this);
                     Value newindex = oldindex instanceof Local ? new FakeJimpleLocal("fakeindex", oldindex.getType(), (Local)oldindex, this) : oldindex;
                     Value node = Jimple.v().newArrayRef(newbase, (Value)newindex);
                     new EquivalentValue(node);
                     use = node;
                  }
               }

               if (!out.containsKey(new EquivalentValue((Value)use))) {
                  out.put(new EquivalentValue((Value)use), groupNum++);
               }
            }
         }
      }

      if (this.graph.getBody().getUnits().getSuccOf((Unit)stmt) == this.begin) {
         out.clear();
      }

      if ((this.tn == null || this.tn.units.contains(stmt)) && !out.isEmpty() && stmt instanceof DefinitionStmt && !this.lostObjects) {
         DefinitionStmt ds = (DefinitionStmt)stmt;
         EquivalentValue lvalue = new EquivalentValue(ds.getLeftOp());
         Local oldbase;
         FakeJimpleLocal newbase;
         if (ds.getLeftOp() instanceof InstanceFieldRef) {
            InstanceFieldRef ifr = (InstanceFieldRef)ds.getLeftOp();
            oldbase = (Local)ifr.getBase();
            if (!(oldbase instanceof FakeJimpleLocal)) {
               Local newbase = new FakeJimpleLocal("fakethis", oldbase.getType(), oldbase, this);
               Value node = Jimple.v().newInstanceFieldRef(newbase, ifr.getField().makeRef());
               EquivalentValue nodeEqVal = new EquivalentValue(node);
               lvalue = nodeEqVal;
            }
         } else if (ds.getLeftOp() instanceof ArrayRef) {
            ArrayRef ar = (ArrayRef)ds.getLeftOp();
            oldbase = (Local)ar.getBase();
            Value oldindex = ar.getIndex();
            if (!(oldbase instanceof FakeJimpleLocal)) {
               newbase = new FakeJimpleLocal("fakethis", oldbase.getType(), oldbase, this);
               Value newindex = oldindex instanceof Local ? new FakeJimpleLocal("fakeindex", oldindex.getType(), (Local)oldindex, this) : oldindex;
               Value node = Jimple.v().newArrayRef(newbase, (Value)newindex);
               EquivalentValue nodeEqVal = new EquivalentValue(node);
               lvalue = nodeEqVal;
            }
         }

         EquivalentValue rvalue = new EquivalentValue(ds.getRightOp());
         if (ds.getRightOp() instanceof CastExpr) {
            rvalue = new EquivalentValue(((CastExpr)ds.getRightOp()).getOp());
         } else {
            Local oldbase;
            if (ds.getRightOp() instanceof InstanceFieldRef) {
               InstanceFieldRef ifr = (InstanceFieldRef)ds.getRightOp();
               oldbase = (Local)ifr.getBase();
               if (!(oldbase instanceof FakeJimpleLocal)) {
                  newbase = new FakeJimpleLocal("fakethis", oldbase.getType(), oldbase, this);
                  Value node = Jimple.v().newInstanceFieldRef(newbase, ifr.getField().makeRef());
                  EquivalentValue nodeEqVal = new EquivalentValue(node);
                  rvalue = nodeEqVal;
               }
            } else if (ds.getRightOp() instanceof ArrayRef) {
               ArrayRef ar = (ArrayRef)ds.getRightOp();
               oldbase = (Local)ar.getBase();
               Value oldindex = ar.getIndex();
               if (!(oldbase instanceof FakeJimpleLocal)) {
                  Local newbase = new FakeJimpleLocal("fakethis", oldbase.getType(), oldbase, this);
                  use = oldindex instanceof Local ? new FakeJimpleLocal("fakeindex", oldindex.getType(), (Local)oldindex, this) : oldindex;
                  ar = Jimple.v().newArrayRef(newbase, (Value)use);
                  EquivalentValue nodeEqVal = new EquivalentValue(ar);
                  rvalue = nodeEqVal;
               }
            }
         }

         if (out.containsKey(lvalue)) {
            Integer lvaluevalue = (Integer)out.get(lvalue);
            Integer rvaluevalue;
            java.util.Map.Entry entry;
            if (stmt instanceof IdentityStmt) {
               if (out.containsKey(rvalue)) {
                  rvaluevalue = (Integer)out.get(rvalue);
                  allUsesIt = out.entrySet().iterator();

                  while(allUsesIt.hasNext()) {
                     entry = (java.util.Map.Entry)allUsesIt.next();
                     if (entry.getValue() == lvaluevalue) {
                        entry.setValue(rvaluevalue);
                     }
                  }

                  allUsesIt = outInfo.refToBaseGroup.entrySet().iterator();

                  while(allUsesIt.hasNext()) {
                     entry = (java.util.Map.Entry)allUsesIt.next();
                     if (entry.getValue() == lvaluevalue) {
                        entry.setValue(rvaluevalue);
                     }
                  }

                  allUsesIt = outInfo.refToIndexGroup.entrySet().iterator();

                  while(allUsesIt.hasNext()) {
                     entry = (java.util.Map.Entry)allUsesIt.next();
                     if (entry.getValue() == lvaluevalue) {
                        entry.setValue(rvaluevalue);
                     }
                  }
               } else {
                  out.put(rvalue, lvaluevalue);
               }
            } else {
               if (!out.containsKey(rvalue)) {
                  if (!(rvalue.getValue() instanceof Local) && !(rvalue.getValue() instanceof StaticFieldRef) && !(rvalue.getValue() instanceof Constant)) {
                     Local oldbase;
                     if (rvalue.getValue() instanceof InstanceFieldRef) {
                        InstanceFieldRef ifr = (InstanceFieldRef)rvalue.getValue();
                        newbase = (FakeJimpleLocal)ifr.getBase();
                        oldbase = newbase.getRealLocal();
                        out.put(rvalue, lvaluevalue);
                        Integer baseGroup;
                        if (out.containsKey(new EquivalentValue(oldbase))) {
                           baseGroup = (Integer)out.get(new EquivalentValue(oldbase));
                        } else {
                           baseGroup = new Integer(-(groupNum++));
                        }

                        if (!outInfo.refToBaseGroup.containsKey(ifr)) {
                           outInfo.refToBaseGroup.put(ifr, baseGroup);
                        }

                        out.put(new EquivalentValue(oldbase), baseGroup);
                     } else if (rvalue.getValue() instanceof ArrayRef) {
                        ArrayRef ar = (ArrayRef)rvalue.getValue();
                        newbase = (FakeJimpleLocal)ar.getBase();
                        oldbase = newbase.getRealLocal();
                        FakeJimpleLocal newindex = ar.getIndex() instanceof FakeJimpleLocal ? (FakeJimpleLocal)ar.getIndex() : null;
                        Value oldindex = newindex != null ? newindex.getRealLocal() : ar.getIndex();
                        out.put(rvalue, lvaluevalue);
                        Integer indexGroup;
                        if (out.containsKey(new EquivalentValue((Value)oldindex))) {
                           indexGroup = (Integer)out.get(new EquivalentValue((Value)oldindex));
                        } else {
                           indexGroup = new Integer(-(groupNum++));
                        }

                        if (!outInfo.refToIndexGroup.containsKey(ar)) {
                           outInfo.refToIndexGroup.put(ar, indexGroup);
                        }

                        out.put(new EquivalentValue((Value)oldindex), indexGroup);
                        Integer baseGroup;
                        if (out.containsKey(new EquivalentValue(oldbase))) {
                           baseGroup = (Integer)out.get(new EquivalentValue(oldbase));
                        } else {
                           baseGroup = new Integer(-(groupNum++));
                        }

                        if (!outInfo.refToBaseGroup.containsKey(ar)) {
                           outInfo.refToBaseGroup.put(ar, baseGroup);
                        }

                        out.put(new EquivalentValue(oldbase), baseGroup);
                     } else if (rvalue.getValue() instanceof AnyNewExpr) {
                        this.printMsg("Ignored Object (assigned new value) at " + stmt);
                     } else {
                        this.printMsg("Lost Object (assigned unacceptable value) at " + stmt);
                        this.lostObjects = true;
                     }
                  } else {
                     out.put(rvalue, lvaluevalue);
                  }
               } else {
                  rvaluevalue = (Integer)out.get(rvalue);
                  allUsesIt = out.entrySet().iterator();

                  while(allUsesIt.hasNext()) {
                     entry = (java.util.Map.Entry)allUsesIt.next();
                     if (entry.getValue() == lvaluevalue) {
                        entry.setValue(rvaluevalue);
                     }
                  }

                  allUsesIt = outInfo.refToBaseGroup.entrySet().iterator();

                  while(allUsesIt.hasNext()) {
                     entry = (java.util.Map.Entry)allUsesIt.next();
                     if (entry.getValue() == lvaluevalue) {
                        entry.setValue(rvaluevalue);
                     }
                  }

                  allUsesIt = outInfo.refToIndexGroup.entrySet().iterator();

                  while(allUsesIt.hasNext()) {
                     entry = (java.util.Map.Entry)allUsesIt.next();
                     if (entry.getValue() == lvaluevalue) {
                        entry.setValue(rvaluevalue);
                     }
                  }
               }

               out.remove(lvalue);
            }
         }
      }

   }

   protected void copy(LocksetFlowInfo sourceInfo, LocksetFlowInfo destInfo) {
      destInfo.groups.clear();
      destInfo.groups.putAll(sourceInfo.groups);
      destInfo.refToBaseGroup.clear();
      destInfo.refToBaseGroup.putAll(sourceInfo.refToBaseGroup);
      destInfo.refToIndexGroup.clear();
      destInfo.refToIndexGroup.putAll(sourceInfo.refToIndexGroup);
   }

   protected LocksetFlowInfo newInitialFlow() {
      return new LocksetFlowInfo();
   }
}
