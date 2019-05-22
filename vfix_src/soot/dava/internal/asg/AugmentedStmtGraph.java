package soot.dava.internal.asg;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Unit;
import soot.dava.Dava;
import soot.jimple.IfStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.toolkits.graph.BriefUnitGraph;
import soot.toolkits.graph.DirectedGraph;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.toolkits.graph.TrapUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.util.IterableSet;

public class AugmentedStmtGraph implements DirectedGraph<AugmentedStmt> {
   private HashMap<Stmt, AugmentedStmt> binding;
   private HashMap<AugmentedStmt, AugmentedStmt> original2clone;
   private IterableSet<AugmentedStmt> aug_list;
   private IterableSet<Stmt> stmt_list;
   private List<AugmentedStmt> bheads;
   private List<AugmentedStmt> btails;
   private List<AugmentedStmt> cheads;
   private List<AugmentedStmt> ctails;

   public AugmentedStmtGraph(AugmentedStmtGraph other) {
      this();
      HashMap<AugmentedStmt, AugmentedStmt> old2new = new HashMap();
      Iterator var3 = other.aug_list.iterator();

      AugmentedStmt oas;
      while(var3.hasNext()) {
         oas = (AugmentedStmt)var3.next();
         Stmt s = oas.get_Stmt();
         AugmentedStmt nas = new AugmentedStmt(s);
         this.aug_list.add(nas);
         this.stmt_list.add(s);
         this.binding.put(s, nas);
         old2new.put(oas, nas);
      }

      var3 = other.aug_list.iterator();

      while(var3.hasNext()) {
         oas = (AugmentedStmt)var3.next();
         AugmentedStmt nas = (AugmentedStmt)old2new.get(oas);
         Iterator var9 = oas.bpreds.iterator();

         AugmentedStmt aug;
         while(var9.hasNext()) {
            aug = (AugmentedStmt)var9.next();
            nas.bpreds.add(old2new.get(aug));
         }

         if (nas.bpreds.isEmpty()) {
            this.bheads.add(nas);
         }

         var9 = oas.cpreds.iterator();

         while(var9.hasNext()) {
            aug = (AugmentedStmt)var9.next();
            nas.cpreds.add(old2new.get(aug));
         }

         if (nas.cpreds.isEmpty()) {
            this.cheads.add(nas);
         }

         var9 = oas.bsuccs.iterator();

         while(var9.hasNext()) {
            aug = (AugmentedStmt)var9.next();
            nas.bsuccs.add(old2new.get(aug));
         }

         if (nas.bsuccs.isEmpty()) {
            this.btails.add(nas);
         }

         var9 = oas.csuccs.iterator();

         while(var9.hasNext()) {
            aug = (AugmentedStmt)var9.next();
            nas.csuccs.add(old2new.get(aug));
         }

         if (nas.csuccs.isEmpty()) {
            this.ctails.add(nas);
         }
      }

      this.find_Dominators();
   }

   public AugmentedStmtGraph(BriefUnitGraph bug, TrapUnitGraph cug) {
      this();
      Dava.v().log("AugmentedStmtGraph::AugmentedStmtGraph() - cug.size() = " + cug.size());
      Iterator var3 = cug.iterator();

      while(var3.hasNext()) {
         Unit u = (Unit)var3.next();
         Stmt s = (Stmt)u;
         this.add_StmtBinding(s, new AugmentedStmt(s));
      }

      List<Unit> cugList = (new PseudoTopologicalOrderer()).newList(cug, false);
      Iterator var8 = cugList.iterator();

      while(var8.hasNext()) {
         Unit u = (Unit)var8.next();
         Stmt s = (Stmt)u;
         this.aug_list.add(this.get_AugStmt(s));
         this.stmt_list.add(s);
      }

      var8 = this.aug_list.iterator();

      while(var8.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var8.next();
         this.mirror_PredsSuccs(as, bug);
         this.mirror_PredsSuccs(as, cug);
      }

      this.find_Dominators();
   }

   public AugmentedStmtGraph() {
      this.binding = new HashMap();
      this.original2clone = new HashMap();
      this.aug_list = new IterableSet();
      this.stmt_list = new IterableSet();
      this.bheads = new LinkedList();
      this.btails = new LinkedList();
      this.cheads = new LinkedList();
      this.ctails = new LinkedList();
   }

   public void add_AugmentedStmt(AugmentedStmt as) {
      Stmt s = as.get_Stmt();
      this.aug_list.add(as);
      this.stmt_list.add(s);
      this.add_StmtBinding(s, as);
      if (as.bpreds.isEmpty()) {
         this.bheads.add(as);
      }

      if (as.cpreds.isEmpty()) {
         this.cheads.add(as);
      }

      if (as.bsuccs.isEmpty()) {
         this.btails.add(as);
      }

      if (as.csuccs.isEmpty()) {
         this.ctails.add(as);
      }

      this.check_List(as.bpreds, this.btails);
      this.check_List(as.bsuccs, this.bheads);
      this.check_List(as.cpreds, this.ctails);
      this.check_List(as.csuccs, this.cheads);
   }

   public boolean contains(Object o) {
      return this.aug_list.contains(o);
   }

   public AugmentedStmt get_CloneOf(AugmentedStmt as) {
      return (AugmentedStmt)this.original2clone.get(as);
   }

   public int size() {
      return this.aug_list.size();
   }

   private <T> void check_List(List<T> psList, List<T> htList) {
      Iterator var3 = psList.iterator();

      while(var3.hasNext()) {
         T t = var3.next();
         if (htList.contains(t)) {
            htList.remove(t);
         }
      }

   }

   public void calculate_Reachability(AugmentedStmt source, Set<AugmentedStmt> blockers, AugmentedStmt dominator) {
      if (blockers == null) {
         throw new RuntimeException("Tried to call AugmentedStmtGraph:calculate_Reachability() with null blockers.");
      } else if (source != null) {
         LinkedList<AugmentedStmt> worklist = new LinkedList();
         HashSet<AugmentedStmt> touchSet = new HashSet();
         worklist.addLast(source);
         touchSet.add(source);

         while(!worklist.isEmpty()) {
            AugmentedStmt as = (AugmentedStmt)worklist.removeFirst();
            Iterator var7 = as.csuccs.iterator();

            while(var7.hasNext()) {
               AugmentedStmt sas = (AugmentedStmt)var7.next();
               if (!touchSet.contains(sas) && sas.get_Dominators().contains(dominator)) {
                  touchSet.add(sas);
                  IterableSet<AugmentedStmt> reachers = sas.get_Reachers();
                  if (!reachers.contains(source)) {
                     reachers.add(source);
                  }

                  if (!blockers.contains(sas)) {
                     worklist.addLast(sas);
                  }
               }
            }
         }

      }
   }

   public void calculate_Reachability(Collection<AugmentedStmt> sources, Set<AugmentedStmt> blockers, AugmentedStmt dominator) {
      Iterator srcIt = sources.iterator();

      while(srcIt.hasNext()) {
         this.calculate_Reachability((AugmentedStmt)srcIt.next(), blockers, dominator);
      }

   }

   public void calculate_Reachability(AugmentedStmt source, AugmentedStmt blocker, AugmentedStmt dominator) {
      HashSet<AugmentedStmt> h = new HashSet();
      h.add(blocker);
      this.calculate_Reachability((AugmentedStmt)source, (Set)h, dominator);
   }

   public void calculate_Reachability(Collection<AugmentedStmt> sources, AugmentedStmt blocker, AugmentedStmt dominator) {
      HashSet<AugmentedStmt> h = new HashSet();
      h.add(blocker);
      this.calculate_Reachability((Collection)sources, (Set)h, dominator);
   }

   public void calculate_Reachability(AugmentedStmt source, AugmentedStmt dominator) {
      this.calculate_Reachability(source, Collections.emptySet(), dominator);
   }

   public void calculate_Reachability(Collection<AugmentedStmt> sources, AugmentedStmt dominator) {
      this.calculate_Reachability(sources, Collections.emptySet(), dominator);
   }

   public void calculate_Reachability(AugmentedStmt source) {
      this.calculate_Reachability((AugmentedStmt)source, (AugmentedStmt)null);
   }

   public void calculate_Reachability(Collection<AugmentedStmt> sources) {
      this.calculate_Reachability((Collection)sources, (AugmentedStmt)null);
   }

   public void add_StmtBinding(Stmt s, AugmentedStmt as) {
      this.binding.put(s, as);
   }

   public AugmentedStmt get_AugStmt(Stmt s) {
      AugmentedStmt as = (AugmentedStmt)this.binding.get(s);
      if (as == null) {
         throw new RuntimeException("Could not find augmented statement for: " + s.toString());
      } else {
         return as;
      }
   }

   public List<AugmentedStmt> getHeads() {
      return this.cheads;
   }

   public List<AugmentedStmt> getTails() {
      return this.ctails;
   }

   public Iterator<AugmentedStmt> iterator() {
      return this.aug_list.iterator();
   }

   public List<AugmentedStmt> getPredsOf(AugmentedStmt s) {
      return s.cpreds;
   }

   public List<AugmentedStmt> getPredsOf(Stmt s) {
      return this.get_AugStmt(s).cpreds;
   }

   public List<AugmentedStmt> getSuccsOf(AugmentedStmt s) {
      return s.csuccs;
   }

   public List<AugmentedStmt> getSuccsOf(Stmt s) {
      return this.get_AugStmt(s).csuccs;
   }

   public List<AugmentedStmt> get_BriefHeads() {
      return this.bheads;
   }

   public List<AugmentedStmt> get_BriefTails() {
      return this.btails;
   }

   public IterableSet<AugmentedStmt> get_ChainView() {
      return new IterableSet(this.aug_list);
   }

   public Object clone() {
      return new AugmentedStmtGraph(this);
   }

   public boolean remove_AugmentedStmt(AugmentedStmt toRemove) {
      if (!this.aug_list.contains(toRemove)) {
         return false;
      } else {
         Iterator var2 = toRemove.bpreds.iterator();

         AugmentedStmt sas;
         while(var2.hasNext()) {
            sas = (AugmentedStmt)var2.next();
            if (sas.bsuccs.contains(toRemove)) {
               sas.bsuccs.remove(toRemove);
            }
         }

         var2 = toRemove.cpreds.iterator();

         while(var2.hasNext()) {
            sas = (AugmentedStmt)var2.next();
            if (sas.csuccs.contains(toRemove)) {
               sas.csuccs.remove(toRemove);
            }
         }

         var2 = toRemove.bsuccs.iterator();

         while(var2.hasNext()) {
            sas = (AugmentedStmt)var2.next();
            if (sas.bpreds.contains(toRemove)) {
               sas.bpreds.remove(toRemove);
            }
         }

         var2 = toRemove.csuccs.iterator();

         while(var2.hasNext()) {
            sas = (AugmentedStmt)var2.next();
            if (sas.cpreds.contains(toRemove)) {
               sas.cpreds.remove(toRemove);
            }
         }

         this.aug_list.remove(toRemove);
         this.stmt_list.remove(toRemove.get_Stmt());
         this.bheads.remove(toRemove);
         this.btails.remove(toRemove);
         this.cheads.remove(toRemove);
         this.ctails.remove(toRemove);
         this.binding.remove(toRemove.get_Stmt());
         return true;
      }
   }

   public String toString() {
      StringBuffer b = new StringBuffer();
      String cr = "\n";
      b.append("AugmentedStmtGraph (size: " + this.size() + " stmts)" + cr);
      Iterator var3 = this.aug_list.iterator();

      while(var3.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var3.next();
         b.append("| .---" + cr + "| | AugmentedStmt " + as.toString() + cr + "| |" + cr + "| |  preds:");
         Iterator var5 = as.cpreds.iterator();

         AugmentedStmt das;
         while(var5.hasNext()) {
            das = (AugmentedStmt)var5.next();
            b.append(" " + das.toString());
         }

         b.append(cr + "| |" + cr + "| |  succs:");
         var5 = as.csuccs.iterator();

         while(var5.hasNext()) {
            das = (AugmentedStmt)var5.next();
            b.append(" " + das.toString());
         }

         b.append(cr + "| |" + cr + "| |  doms:");
         var5 = as.get_Dominators().iterator();

         while(var5.hasNext()) {
            das = (AugmentedStmt)var5.next();
            b.append(" " + das.toString());
         }

         b.append(cr + "| `---" + cr);
      }

      b.append("-" + cr);
      return b.toString();
   }

   private void mirror_PredsSuccs(AugmentedStmt as, UnitGraph ug) {
      Stmt s = as.get_Stmt();
      LinkedList<AugmentedStmt> preds = new LinkedList();
      LinkedList<AugmentedStmt> succs = new LinkedList();
      Iterator var6 = ug.getPredsOf((Unit)s).iterator();

      Unit u;
      AugmentedStmt so;
      while(var6.hasNext()) {
         u = (Unit)var6.next();
         so = this.get_AugStmt((Stmt)u);
         if (!preds.contains(so)) {
            preds.add(so);
         }
      }

      var6 = ug.getSuccsOf((Unit)s).iterator();

      while(var6.hasNext()) {
         u = (Unit)var6.next();
         so = this.get_AugStmt((Stmt)u);
         if (!succs.contains(so)) {
            succs.add(so);
         }
      }

      if (ug instanceof BriefUnitGraph) {
         as.bpreds = preds;
         as.bsuccs = succs;
         if (preds.size() == 0) {
            this.bheads.add(as);
         }

         if (succs.size() == 0) {
            this.btails.add(as);
         }
      } else {
         if (!(ug instanceof TrapUnitGraph)) {
            throw new RuntimeException("Unknown UnitGraph type: " + ug.getClass());
         }

         as.cpreds = preds;
         as.csuccs = succs;
         if (preds.size() == 0) {
            this.cheads.add(as);
         }

         if (succs.size() == 0) {
            this.ctails.add(as);
         }
      }

   }

   public IterableSet<AugmentedStmt> clone_Body(IterableSet<AugmentedStmt> oldBody) {
      HashMap<AugmentedStmt, AugmentedStmt> old2new = new HashMap();
      HashMap<AugmentedStmt, AugmentedStmt> new2old = new HashMap();
      IterableSet<AugmentedStmt> newBody = new IterableSet();
      Iterator var5 = oldBody.iterator();

      AugmentedStmt au;
      AugmentedStmt nas;
      while(var5.hasNext()) {
         au = (AugmentedStmt)var5.next();
         nas = (AugmentedStmt)au.clone();
         this.original2clone.put(au, nas);
         old2new.put(au, nas);
         new2old.put(nas, au);
         newBody.add(nas);
      }

      var5 = newBody.iterator();

      while(var5.hasNext()) {
         au = (AugmentedStmt)var5.next();
         nas = (AugmentedStmt)new2old.get(au);
         this.mirror_PredsSuccs(nas, nas.bpreds, au.bpreds, old2new);
         this.mirror_PredsSuccs(nas, nas.cpreds, au.cpreds, old2new);
         this.mirror_PredsSuccs(nas, nas.bsuccs, au.bsuccs, old2new);
         this.mirror_PredsSuccs(nas, nas.csuccs, au.csuccs, old2new);
      }

      var5 = newBody.iterator();

      while(var5.hasNext()) {
         au = (AugmentedStmt)var5.next();
         this.add_AugmentedStmt(au);
      }

      HashMap<Stmt, Stmt> so2n = new HashMap();
      Iterator var19 = oldBody.iterator();

      Stmt ns;
      while(var19.hasNext()) {
         nas = (AugmentedStmt)var19.next();
         Stmt os = nas.get_Stmt();
         ns = ((AugmentedStmt)old2new.get(nas)).get_Stmt();
         so2n.put(os, ns);
      }

      var19 = newBody.iterator();

      while(true) {
         while(var19.hasNext()) {
            nas = (AugmentedStmt)var19.next();
            AugmentedStmt oas = (AugmentedStmt)new2old.get(nas);
            ns = nas.get_Stmt();
            Stmt os = oas.get_Stmt();
            LookupSwitchStmt nlss;
            if (os instanceof IfStmt) {
               Unit target = ((IfStmt)os).getTarget();
               nlss = null;
               Unit newTgt;
               if ((newTgt = (Unit)so2n.get(target)) != null) {
                  ((IfStmt)ns).setTarget(newTgt);
               } else {
                  ((IfStmt)ns).setTarget(target);
               }
            } else {
               Unit target;
               Unit newTgt;
               int i;
               if (os instanceof TableSwitchStmt) {
                  TableSwitchStmt otss = (TableSwitchStmt)os;
                  TableSwitchStmt ntss = (TableSwitchStmt)ns;
                  target = otss.getDefaultTarget();
                  newTgt = null;
                  if ((newTgt = (Unit)so2n.get(target)) != null) {
                     ntss.setDefaultTarget(newTgt);
                  } else {
                     ntss.setDefaultTarget(target);
                  }

                  LinkedList<Unit> new_target_list = new LinkedList();
                  i = otss.getHighIndex() - otss.getLowIndex() + 1;

                  for(int i = 0; i < i; ++i) {
                     target = otss.getTarget(i);
                     newTgt = null;
                     if ((newTgt = (Unit)so2n.get(target)) != null) {
                        new_target_list.add(newTgt);
                     } else {
                        new_target_list.add(target);
                     }
                  }

                  ntss.setTargets(new_target_list);
               } else if (os instanceof LookupSwitchStmt) {
                  LookupSwitchStmt olss = (LookupSwitchStmt)os;
                  nlss = (LookupSwitchStmt)ns;
                  target = olss.getDefaultTarget();
                  newTgt = null;
                  if ((newTgt = (Unit)so2n.get(target)) != null) {
                     nlss.setDefaultTarget(newTgt);
                  } else {
                     nlss.setDefaultTarget(target);
                  }

                  Unit[] new_target_list = new Unit[olss.getTargetCount()];

                  for(i = 0; i < new_target_list.length; ++i) {
                     target = olss.getTarget(i);
                     newTgt = null;
                     if ((newTgt = (Unit)so2n.get(target)) != null) {
                        new_target_list[i] = newTgt;
                     } else {
                        new_target_list[i] = target;
                     }
                  }

                  nlss.setTargets(new_target_list);
                  nlss.setLookupValues(olss.getLookupValues());
               }
            }
         }

         return newBody;
      }
   }

   private void mirror_PredsSuccs(AugmentedStmt originalAs, List<AugmentedStmt> oldList, List<AugmentedStmt> newList, Map<AugmentedStmt, AugmentedStmt> old2new) {
      Iterator var5 = oldList.iterator();

      while(var5.hasNext()) {
         AugmentedStmt oldAs = (AugmentedStmt)var5.next();
         AugmentedStmt newAs = (AugmentedStmt)old2new.get(oldAs);
         if (newAs != null) {
            newList.add(newAs);
         } else {
            newList.add(oldAs);
            AugmentedStmt clonedAs = (AugmentedStmt)old2new.get(originalAs);
            if (oldList == originalAs.bpreds) {
               oldAs.bsuccs.add(clonedAs);
            } else if (oldList == originalAs.cpreds) {
               oldAs.csuccs.add(clonedAs);
            } else if (oldList == originalAs.bsuccs) {
               oldAs.bpreds.add(clonedAs);
            } else {
               if (oldList != originalAs.csuccs) {
                  throw new RuntimeException("Error mirroring preds and succs in Try block splitting.");
               }

               oldAs.cpreds.add(clonedAs);
            }
         }
      }

   }

   public void find_Dominators() {
      Iterator var1 = this.aug_list.iterator();

      AugmentedStmt as;
      while(var1.hasNext()) {
         as = (AugmentedStmt)var1.next();
         if (as.cpreds.size() != 0) {
            if (!as.get_Dominators().isEmpty()) {
               as.get_Dominators().clear();
            }

            as.get_Dominators().addAll(this.aug_list);
         } else {
            as.get_Dominators().clear();
         }
      }

      IterableSet<AugmentedStmt> worklist = new IterableSet();
      worklist.addAll(this.aug_list);

      while(true) {
         IterableSet pred_intersection;
         Iterator var5;
         AugmentedStmt pas;
         label63:
         do {
            if (worklist.isEmpty()) {
               return;
            }

            as = (AugmentedStmt)worklist.getFirst();
            worklist.removeFirst();
            pred_intersection = new IterableSet();
            boolean first_pred = true;
            var5 = as.cpreds.iterator();

            while(true) {
               while(true) {
                  if (!var5.hasNext()) {
                     continue label63;
                  }

                  pas = (AugmentedStmt)var5.next();
                  if (first_pred) {
                     pred_intersection.addAll(pas.get_Dominators());
                     if (!pred_intersection.contains(pas)) {
                        pred_intersection.add(pas);
                     }

                     first_pred = false;
                  } else {
                     Iterator piit = pred_intersection.snapshotIterator();

                     while(piit.hasNext()) {
                        AugmentedStmt pid = (AugmentedStmt)piit.next();
                        if (!pas.get_Dominators().contains(pid) && pas != pid) {
                           pred_intersection.remove(pid);
                        }
                     }
                  }
               }
            }
         } while(as.get_Dominators().equals(pred_intersection));

         var5 = as.csuccs.iterator();

         while(var5.hasNext()) {
            pas = (AugmentedStmt)var5.next();
            if (!worklist.contains(pas)) {
               worklist.add(pas);
            }
         }

         as.get_Dominators().clear();
         as.get_Dominators().addAll(pred_intersection);
      }
   }
}
