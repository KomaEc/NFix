package soot.jbco.bafTransformations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import soot.Body;
import soot.BodyTransformer;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LongType;
import soot.PatchingChain;
import soot.PrimType;
import soot.RefType;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.baf.Baf;
import soot.baf.DupInst;
import soot.baf.FieldArgInst;
import soot.baf.IdentityInst;
import soot.baf.IncInst;
import soot.baf.InstanceCastInst;
import soot.baf.InstanceOfInst;
import soot.baf.LoadInst;
import soot.baf.MethodArgInst;
import soot.baf.NewArrayInst;
import soot.baf.NewInst;
import soot.baf.NewMultiArrayInst;
import soot.baf.NoArgInst;
import soot.baf.OpTypeArgInst;
import soot.baf.PopInst;
import soot.baf.PrimitiveCastInst;
import soot.baf.PushInst;
import soot.baf.ReturnInst;
import soot.baf.SpecialInvokeInst;
import soot.baf.StoreInst;
import soot.baf.SwapInst;
import soot.baf.TargetArgInst;
import soot.jbco.IJbcoTransform;
import soot.jbco.Main;
import soot.jbco.util.Rand;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LongConstant;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;
import soot.toolkits.graph.BriefUnitGraph;
import soot.util.Chain;

public class FindDuplicateSequences extends BodyTransformer implements IJbcoTransform {
   int[] totalcounts = new int[512];
   public static String[] dependancies = new String[]{"bb.jbco_j2bl", "bb.jbco_rds", "bb.jbco_ful", "bb.lp"};
   public static String name = "bb.jbco_rds";

   public String[] getDependencies() {
      return dependancies;
   }

   public String getName() {
      return name;
   }

   public void outputSummary() {
      out.println("Duplicate Sequences:");

      for(int count = this.totalcounts.length - 1; count >= 0; --count) {
         if (this.totalcounts[count] > 0) {
            out.println("\t" + count + " total: " + this.totalcounts[count]);
         }
      }

   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      int weight = Main.getWeight(phaseName, b.getMethod().getSignature());
      if (weight != 0) {
         if (output) {
            out.println("Checking " + b.getMethod().getName() + " for duplicate sequences..");
         }

         List<Unit> illegalUnits = new ArrayList();
         List<Unit> seenUnits = new ArrayList();
         List<Unit> workList = new ArrayList();
         PatchingChain<Unit> units = b.getUnits();
         BriefUnitGraph bug = new BriefUnitGraph(b);
         workList.addAll(bug.getHeads());

         while(true) {
            Unit u;
            ArrayList tmpWorkList;
            do {
               if (workList.size() <= 0) {
                  seenUnits = null;
                  int controlLocalIndex = 0;
                  int longestSeq = units.size() / 2 - 1;
                  if (longestSeq > 20) {
                     longestSeq = 20;
                  }

                  tmpWorkList = null;
                  Collection<Local> bLocals = b.getLocals();
                  int[] counts = new int[longestSeq + 1];
                  Map<Local, Local> bafToJLocals = (Map)Main.methods2Baf2JLocals.get(b.getMethod());
                  boolean changed = true;
                  Map<Unit, Stack<Type>> stackHeightsBefore = null;

                  label296:
                  for(int count = longestSeq; count > 2; --count) {
                     Unit[] uArry = (Unit[])units.toArray(new Unit[units.size()]);
                     if (uArry.length <= 0) {
                        return;
                     }

                     List<List<Unit>> candidates = new ArrayList();
                     List<Unit> unitIDs = new ArrayList();
                     if (changed) {
                        stackHeightsBefore = StackTypeHeightCalculator.calculateStackHeights(b, bafToJLocals);
                        bug = StackTypeHeightCalculator.bug;
                        changed = false;
                     }

                     int found;
                     Unit p;
                     int jj;
                     label201:
                     for(int i = 0; i < uArry.length; ++i) {
                        unitIDs.add(uArry[i]);
                        if (i + count <= uArry.length) {
                           List<Unit> seq = new ArrayList();

                           for(int j = 0; j < count; ++j) {
                              Unit u = uArry[i + j];
                              if (u instanceof IdentityInst || u instanceof ReturnInst || illegalUnits.contains(u)) {
                                 break;
                              }

                              if (j > 0) {
                                 List<Unit> preds = bug.getPredsOf(u);
                                 if (preds.size() > 0) {
                                    found = 0;
                                    Iterator pit = preds.iterator();

                                    while(true) {
                                       while(pit.hasNext()) {
                                          p = (Unit)pit.next();

                                          for(jj = 0; jj < count; ++jj) {
                                             if (p == uArry[i + jj]) {
                                                ++found;
                                                break;
                                             }
                                          }
                                       }

                                       if (found < preds.size()) {
                                          continue label201;
                                       }
                                       break;
                                    }
                                 }
                              }

                              seq.add(u);
                           }

                           if (seq.size() == count && ((Unit)seq.get(seq.size() - 1)).fallsThrough()) {
                              candidates.add(seq);
                           }
                        }
                     }

                     Map<List<Unit>, List<List<Unit>>> selected = new HashMap();

                     Unit p;
                     List seq;
                     List matches;
                     for(int i = 0; i < candidates.size(); ++i) {
                        seq = (List)candidates.get(i);
                        List<List<Unit>> matches = new ArrayList();

                        for(int j = 0; j < uArry.length - count; ++j) {
                           if (!this.overlap(uArry, seq, j, count)) {
                              boolean found = false;

                              label249:
                              for(int k = 0; k < count; ++k) {
                                 p = (Unit)seq.get(k);
                                 found = false;
                                 Unit v = uArry[j + k];
                                 if (!this.equalUnits(p, v, b) || illegalUnits.contains(v)) {
                                    break;
                                 }

                                 if (k > 0) {
                                    List<Unit> preds = bug.getPredsOf(v);
                                    if (preds.size() > 0) {
                                       int fcount = 0;
                                       Iterator pit = preds.iterator();

                                       while(true) {
                                          while(pit.hasNext()) {
                                             p = (Unit)pit.next();

                                             for(int jj = 0; jj < count; ++jj) {
                                                if (p == uArry[j + jj]) {
                                                   ++fcount;
                                                   break;
                                                }
                                             }
                                          }

                                          if (fcount < preds.size()) {
                                             break label249;
                                          }
                                          break;
                                       }
                                    }
                                 }

                                 if (!((Stack)stackHeightsBefore.get(p)).equals(stackHeightsBefore.get(v))) {
                                    break;
                                 }

                                 found = true;
                              }

                              if (found) {
                                 List<Unit> foundSeq = new ArrayList();

                                 for(int m = 0; m < count; ++m) {
                                    foundSeq.add(uArry[j + m]);
                                 }

                                 matches.add(foundSeq);
                              }
                           }
                        }

                        if (matches.size() > 0) {
                           boolean done = false;

                           for(found = 0; found < seq.size(); ++found) {
                              if (!unitIDs.contains(seq.get(found))) {
                                 done = true;
                              } else {
                                 unitIDs.remove(seq.get(found));
                              }
                           }

                           if (!done) {
                              matches = cullOverlaps(b, unitIDs, matches);
                              if (matches.size() > 0) {
                                 selected.put(seq, matches);
                              }
                           }
                        }
                     }

                     if (selected.size() > 0) {
                        Iterator keys = selected.keySet().iterator();

                        while(true) {
                           do {
                              do {
                                 if (!keys.hasNext()) {
                                    continue label296;
                                 }

                                 seq = (List)keys.next();
                                 matches = (List)selected.get(seq);
                              } while(matches.size() < 1);
                           } while(Rand.getInt(10) <= weight);

                           changed = true;
                           Local controlLocal = Baf.v().newLocal("controlLocalfordups" + controlLocalIndex, IntType.v());
                           bLocals.add(controlLocal);
                           bafToJLocals.put(controlLocal, Jimple.v().newLocal("controlLocalfordups" + controlLocalIndex++, IntType.v()));
                           int var10001 = seq.size();
                           counts[var10001] += matches.size();
                           List<Unit> jumps = new ArrayList();
                           Unit first = (Unit)seq.get(0);
                           Unit store = Baf.v().newStoreInst(IntType.v(), controlLocal);
                           units.insertBefore((Unit)store, (Unit)first);
                           Unit pushUnit = Baf.v().newPushInst(IntConstant.v(0));
                           units.insertBefore((Unit)pushUnit, (Unit)store);
                           jj = 1;
                           Iterator values = matches.iterator();

                           while(values.hasNext()) {
                              List<Unit> next = (List)values.next();
                              Unit jump = units.getSuccOf((Unit)next.get(next.size() - 1));
                              p = (Unit)next.get(0);
                              Unit storet = (Unit)store.clone();
                              units.insertBefore(storet, p);
                              pushUnit = Baf.v().newPushInst(IntConstant.v(jj++));
                              units.insertBefore((Unit)pushUnit, (Unit)storet);
                              Unit goUnit = Baf.v().newGotoInst(first);
                              units.insertAfter((Unit)goUnit, (Unit)storet);
                              jumps.add(jump);
                           }

                           Unit insertAfter = (Unit)seq.get(seq.size() - 1);
                           Unit swUnit = Baf.v().newTableSwitchInst(units.getSuccOf(insertAfter), 1, jumps.size(), jumps);
                           units.insertAfter((Unit)swUnit, (Unit)insertAfter);
                           Unit loadUnit = Baf.v().newLoadInst(IntType.v(), controlLocal);
                           units.insertAfter((Unit)loadUnit, (Unit)insertAfter);
                           values = matches.iterator();

                           while(values.hasNext()) {
                              List<Unit> next = (List)values.next();
                              units.removeAll(next);
                           }
                        }
                     }
                  }

                  boolean dupsExist = false;
                  if (output) {
                     System.out.println("Duplicate Sequences for " + b.getMethod().getName());
                  }

                  for(int count = longestSeq; count >= 0; --count) {
                     if (counts[count] > 0) {
                        if (output) {
                           out.println(count + " total: " + counts[count]);
                        }

                        dupsExist = true;
                        int[] var42 = this.totalcounts;
                        var42[count] += counts[count];
                     }
                  }

                  if (!dupsExist) {
                     if (output) {
                        out.println("\tnone");
                     }
                  } else if (debug) {
                     StackTypeHeightCalculator.calculateStackHeights(b);
                  }

                  return;
               }

               u = (Unit)workList.remove(0);
            } while(seenUnits.contains(u));

            if (u instanceof NewInst) {
               RefType t = ((NewInst)u).getBaseType();
               tmpWorkList = new ArrayList();
               tmpWorkList.add(u);

               Unit v;
               for(; tmpWorkList.size() > 0; illegalUnits.add(v)) {
                  v = (Unit)tmpWorkList.remove(0);
                  if (v instanceof SpecialInvokeInst) {
                     SpecialInvokeInst si = (SpecialInvokeInst)v;
                     if (si.getMethodRef().getSignature().indexOf("void <init>") < 0 || si.getMethodRef().declaringClass() != t.getSootClass()) {
                        tmpWorkList.addAll(bug.getSuccsOf(v));
                     }
                  } else {
                     tmpWorkList.addAll(bug.getSuccsOf(v));
                  }
               }
            }

            seenUnits.add(u);
            workList.addAll(bug.getSuccsOf(u));
         }
      }
   }

   private boolean equalUnits(Object o1, Object o2, Body b) {
      if (o1.getClass() != o2.getClass()) {
         return false;
      } else {
         List<Trap> l1 = this.getTrapsForUnit(o1, b);
         List<Trap> l2 = this.getTrapsForUnit(o2, b);
         if (l1.size() != l2.size()) {
            return false;
         } else {
            for(int i = 0; i < l1.size(); ++i) {
               if (l1.get(i) != l2.get(i)) {
                  return false;
               }
            }

            if (o1 instanceof NoArgInst) {
               return true;
            } else if (o1 instanceof TargetArgInst) {
               if (!(o1 instanceof OpTypeArgInst)) {
                  return ((TargetArgInst)o1).getTarget() == ((TargetArgInst)o2).getTarget();
               } else {
                  return ((TargetArgInst)o1).getTarget() == ((TargetArgInst)o2).getTarget() && ((OpTypeArgInst)o1).getOpType() == ((OpTypeArgInst)o2).getOpType();
               }
            } else if (o1 instanceof OpTypeArgInst) {
               return ((OpTypeArgInst)o1).getOpType() == ((OpTypeArgInst)o2).getOpType();
            } else if (o1 instanceof MethodArgInst) {
               return ((MethodArgInst)o1).getMethod() == ((MethodArgInst)o2).getMethod();
            } else if (o1 instanceof FieldArgInst) {
               return ((FieldArgInst)o1).getField() == ((FieldArgInst)o2).getField();
            } else if (o1 instanceof PrimitiveCastInst) {
               return ((PrimitiveCastInst)o1).getFromType() == ((PrimitiveCastInst)o2).getFromType() && ((PrimitiveCastInst)o1).getToType() == ((PrimitiveCastInst)o2).getToType();
            } else if (o1 instanceof DupInst) {
               return this.compareDups(o1, o2);
            } else if (o1 instanceof LoadInst) {
               return ((LoadInst)o1).getLocal() == ((LoadInst)o2).getLocal();
            } else if (o1 instanceof StoreInst) {
               return ((StoreInst)o1).getLocal() == ((StoreInst)o2).getLocal();
            } else if (o1 instanceof PushInst) {
               return this.equalConstants(((PushInst)o1).getConstant(), ((PushInst)o2).getConstant());
            } else if (o1 instanceof IncInst && this.equalConstants(((IncInst)o1).getConstant(), ((IncInst)o2).getConstant())) {
               return ((IncInst)o1).getLocal() == ((IncInst)o2).getLocal();
            } else if (o1 instanceof InstanceCastInst) {
               return this.equalTypes(((InstanceCastInst)o1).getCastType(), ((InstanceCastInst)o2).getCastType());
            } else if (o1 instanceof InstanceOfInst) {
               return this.equalTypes(((InstanceOfInst)o1).getCheckType(), ((InstanceOfInst)o2).getCheckType());
            } else if (o1 instanceof NewArrayInst) {
               return this.equalTypes(((NewArrayInst)o1).getBaseType(), ((NewArrayInst)o2).getBaseType());
            } else if (o1 instanceof NewInst) {
               return this.equalTypes(((NewInst)o1).getBaseType(), ((NewInst)o2).getBaseType());
            } else if (o1 instanceof NewMultiArrayInst) {
               return this.equalTypes(((NewMultiArrayInst)o1).getBaseType(), ((NewMultiArrayInst)o2).getBaseType()) && ((NewMultiArrayInst)o1).getDimensionCount() == ((NewMultiArrayInst)o2).getDimensionCount();
            } else if (o1 instanceof PopInst) {
               return ((PopInst)o1).getWordCount() == ((PopInst)o2).getWordCount();
            } else if (!(o1 instanceof SwapInst)) {
               return false;
            } else {
               return ((SwapInst)o1).getFromType() == ((SwapInst)o2).getFromType() && ((SwapInst)o1).getToType() == ((SwapInst)o2).getToType();
            }
         }
      }
   }

   private List<Trap> getTrapsForUnit(Object o, Body b) {
      ArrayList<Trap> list = new ArrayList();
      Chain<Trap> traps = b.getTraps();
      if (traps.size() != 0) {
         PatchingChain<Unit> units = b.getUnits();
         Iterator it = traps.iterator();

         while(true) {
            while(it.hasNext()) {
               Trap t = (Trap)it.next();
               Iterator tit = units.iterator(t.getBeginUnit(), t.getEndUnit());

               while(tit.hasNext()) {
                  if (tit.next() == o) {
                     list.add(t);
                     break;
                  }
               }
            }

            return list;
         }
      } else {
         return list;
      }
   }

   private boolean overlap(Object[] units, List<?> list, int idx, int count) {
      if (idx >= 0 && list != null && list.size() != 0) {
         Object first = list.get(0);
         Object last = list.get(list.size() - 1);

         for(int i = idx; i < idx + count; ++i) {
            if (i < units.length && (first == units[i] || last == units[i])) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean equalConstants(Constant c1, Constant c2) {
      Type t = c1.getType();
      if (t != c2.getType()) {
         return false;
      } else if (t instanceof IntType) {
         return ((IntConstant)c1).value == ((IntConstant)c2).value;
      } else if (t instanceof FloatType) {
         return ((FloatConstant)c1).value == ((FloatConstant)c2).value;
      } else if (t instanceof LongType) {
         return ((LongConstant)c1).value == ((LongConstant)c2).value;
      } else if (t instanceof DoubleType) {
         return ((DoubleConstant)c1).value == ((DoubleConstant)c2).value;
      } else if (c1 instanceof StringConstant && c2 instanceof StringConstant) {
         return ((StringConstant)c1).value == ((StringConstant)c2).value;
      } else {
         return c1 instanceof NullConstant && c2 instanceof NullConstant;
      }
   }

   private boolean compareDups(Object o1, Object o2) {
      DupInst d1 = (DupInst)o1;
      DupInst d2 = (DupInst)o2;
      List<Type> l1 = d1.getOpTypes();
      List<Type> l2 = d2.getOpTypes();

      for(int k = 0; k < 2; ++k) {
         if (k == 1) {
            l1 = d1.getUnderTypes();
            l2 = d2.getUnderTypes();
         }

         if (l1.size() != l2.size()) {
            return false;
         }

         for(int i = 0; i < l1.size(); ++i) {
            if (l1.get(i) != l2.get(i)) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean equalTypes(Type t1, Type t2) {
      if (t1 instanceof RefType) {
         if (t2 instanceof RefType) {
            RefType rt1 = (RefType)t1;
            RefType rt2 = (RefType)t2;
            return rt1.compareTo(rt2) == 0;
         } else {
            return false;
         }
      } else if (t1 instanceof PrimType && t2 instanceof PrimType) {
         return t1.getClass() == t2.getClass();
      } else {
         return false;
      }
   }

   private static List<List<Unit>> cullOverlaps(Body b, List<Unit> ids, List<List<Unit>> matches) {
      List<List<Unit>> newMatches = new ArrayList();

      for(int i = 0; i < matches.size(); ++i) {
         List<Unit> match = (List)matches.get(i);
         Iterator<Unit> it = match.iterator();
         boolean clean = true;

         while(it.hasNext()) {
            if (!ids.contains(it.next())) {
               clean = false;
               break;
            }
         }

         if (clean) {
            List<UnitBox> targs = b.getUnitBoxes(true);

            for(int j = 0; j < targs.size() && clean; ++j) {
               Unit u = ((UnitBox)targs.get(j)).getUnit();
               it = match.iterator();

               while(it.hasNext()) {
                  if (u == it.next()) {
                     clean = false;
                     break;
                  }
               }
            }
         }

         if (clean) {
            it = match.iterator();

            while(it.hasNext()) {
               ids.remove(it.next());
            }

            newMatches.add(match);
         }
      }

      return newMatches;
   }
}
