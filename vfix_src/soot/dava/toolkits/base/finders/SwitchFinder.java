package soot.dava.toolkits.base.finders;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import soot.G;
import soot.Singletons;
import soot.Value;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETSwitchNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.util.IterableSet;

public class SwitchFinder implements FactFinder {
   private IterableSet junkBody;
   private HashSet targetSet;
   private LinkedList targetList;
   private LinkedList snTargetList;
   private LinkedList tSuccList;
   private HashMap index2target;
   private HashMap tSucc2indexSet;
   private HashMap tSucc2target;
   private HashMap tSucc2Body;

   public SwitchFinder(Singletons.Global g) {
   }

   public static SwitchFinder v() {
      return G.v().soot_dava_toolkits_base_finders_SwitchFinder();
   }

   public void find(DavaBody davaBody, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
      Dava.v().log("SwitchFinder::find()");
      String defaultStr = "default";
      Iterator asgit = asg.iterator();

      label184:
      while(true) {
         AugmentedStmt as;
         Stmt s;
         do {
            if (!asgit.hasNext()) {
               return;
            }

            as = (AugmentedStmt)asgit.next();
            s = as.get_Stmt();
         } while(!(s instanceof TableSwitchStmt) && !(s instanceof LookupSwitchStmt));

         Value key = null;
         this.junkBody = new IterableSet();
         this.targetSet = new HashSet();
         this.targetList = new LinkedList();
         this.snTargetList = new LinkedList();
         this.tSuccList = new LinkedList();
         this.index2target = new HashMap();
         this.tSucc2indexSet = new HashMap();
         this.tSucc2target = new HashMap();
         this.tSucc2Body = new HashMap();
         int target_count;
         int i;
         if (s instanceof TableSwitchStmt) {
            TableSwitchStmt tss = (TableSwitchStmt)s;
            target_count = tss.getHighIndex() - tss.getLowIndex() + 1;

            for(i = 0; i < target_count; ++i) {
               this.build_Bindings(as, new Integer(i + tss.getLowIndex()), asg.get_AugStmt((Stmt)tss.getTarget(i)));
            }

            this.build_Bindings(as, "default", asg.get_AugStmt((Stmt)tss.getDefaultTarget()));
            key = tss.getKey();
         } else if (s instanceof LookupSwitchStmt) {
            LookupSwitchStmt lss = (LookupSwitchStmt)s;
            target_count = lss.getTargetCount();

            for(i = 0; i < target_count; ++i) {
               this.build_Bindings(as, new Integer(lss.getLookupValue(i)), asg.get_AugStmt((Stmt)lss.getTarget(i)));
            }

            this.build_Bindings(as, "default", asg.get_AugStmt((Stmt)lss.getDefaultTarget()));
            key = lss.getKey();
         }

         Iterator tsit = this.tSuccList.iterator();

         while(tsit.hasNext()) {
            AugmentedStmt tSucc = (AugmentedStmt)tsit.next();
            AugmentedStmt target = (AugmentedStmt)this.tSucc2target.get(tSucc);
            this.snTargetList.addLast(new SwitchNode(target, (TreeSet)this.tSucc2indexSet.get(tSucc), (IterableSet)this.tSucc2Body.get(tSucc)));
         }

         TreeSet targetHeads = new TreeSet();
         TreeSet killBodies = new TreeSet();
         asg.calculate_Reachability((Collection)this.targetList, (Set)this.targetSet, as);
         SwitchNodeGraph sng = new SwitchNodeGraph(this.snTargetList);
         killBodies.addAll(this.snTargetList);
         this.snTargetList = new LinkedList();
         LinkedList worklist = new LinkedList();
         worklist.addAll(sng.getHeads());

         SwitchNode sn;
         label120:
         while(!worklist.isEmpty()) {
            SwitchNode sn = (SwitchNode)worklist.removeFirst();
            this.snTargetList.addLast(sn);
            killBodies.remove(sn);
            sn = null;
            Iterator sit = sn.get_Succs().iterator();

            while(true) {
               SwitchNode ssn;
               do {
                  if (!sit.hasNext()) {
                     if (sn != null && sn.get_Score() > 0) {
                        worklist.addLast(sn);
                     }
                     continue label120;
                  }

                  ssn = (SwitchNode)sit.next();
               } while(sn != null && sn.get_Score() >= ssn.get_Score());

               sn = ssn;
            }
         }

         Iterator kit = killBodies.iterator();

         IterableSet tryBody;
         while(kit.hasNext()) {
            sn = (SwitchNode)kit.next();
            tryBody = sn.get_Body();
            tryBody.clear();
            tryBody.add(sn.get_AugStmt());
         }

         sng = new SwitchNodeGraph(this.snTargetList);
         targetHeads.addAll(sng.getHeads());
         LinkedList switchNodeList = new LinkedList();

         while(true) {
            while(!targetHeads.isEmpty() || !killBodies.isEmpty()) {
               SwitchNode nextNode;
               if (!targetHeads.isEmpty() && (targetHeads.isEmpty() || killBodies.isEmpty() || ((SwitchNode)targetHeads.first()).compareTo(killBodies.first()) <= 0)) {
                  nextNode = (SwitchNode)targetHeads.first();
                  targetHeads.remove(nextNode);

                  while(true) {
                     switchNodeList.addLast(nextNode);
                     if (nextNode.get_Succs().isEmpty()) {
                        break;
                     }

                     nextNode = (SwitchNode)nextNode.get_Succs().get(0);
                  }
               } else {
                  nextNode = (SwitchNode)killBodies.first();
                  killBodies.remove(nextNode);
                  switchNodeList.addLast(nextNode);
               }
            }

            IterableSet body = new IterableSet();
            body.add(as);
            kit = switchNodeList.iterator();

            while(kit.hasNext()) {
               sn = (SwitchNode)kit.next();
               body.addAll(sn.get_Body());
               if (sn.get_IndexSet().contains("default")) {
                  sn.get_IndexSet().clear();
                  sn.get_IndexSet().add("default");
               }
            }

            body.addAll(this.junkBody);
            kit = davaBody.get_ExceptionFacts().iterator();

            label181:
            while(true) {
               do {
                  if (!kit.hasNext()) {
                     SET.nest(new SETSwitchNode(as, key, body, switchNodeList, this.junkBody));
                     continue label184;
                  }

                  ExceptionNode en = (ExceptionNode)kit.next();
                  tryBody = en.get_TryBody();
               } while(!tryBody.contains(as));

               Iterator fbit = body.snapshotIterator();

               while(true) {
                  while(true) {
                     AugmentedStmt fbas;
                     do {
                        if (!fbit.hasNext()) {
                           continue label181;
                        }

                        fbas = (AugmentedStmt)fbit.next();
                     } while(tryBody.contains(fbas));

                     body.remove(fbas);
                     Iterator var19 = switchNodeList.iterator();

                     while(var19.hasNext()) {
                        SwitchNode sn = (SwitchNode)var19.next();
                        IterableSet switchBody = sn.get_Body();
                        if (switchBody.contains(fbas)) {
                           switchBody.remove(fbas);
                           break;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private IterableSet find_SubBody(AugmentedStmt switchAS, AugmentedStmt branchS) {
      IterableSet subBody = new IterableSet();
      LinkedList<AugmentedStmt> worklist = new LinkedList();
      subBody.add(branchS);
      branchS = (AugmentedStmt)branchS.bsuccs.get(0);
      if (branchS.get_Dominators().contains(switchAS)) {
         worklist.addLast(branchS);
         subBody.add(branchS);
      }

      while(!worklist.isEmpty()) {
         AugmentedStmt as = (AugmentedStmt)worklist.removeFirst();
         Iterator sit = as.csuccs.iterator();

         while(sit.hasNext()) {
            AugmentedStmt sas = (AugmentedStmt)sit.next();
            if (!subBody.contains(sas) && sas.get_Dominators().contains(branchS)) {
               worklist.addLast(sas);
               subBody.add(sas);
            }
         }
      }

      return subBody;
   }

   private void build_Bindings(AugmentedStmt swAs, Object index, AugmentedStmt target) {
      AugmentedStmt tSucc = (AugmentedStmt)target.bsuccs.get(0);
      if (this.targetSet.add(tSucc)) {
         this.targetList.addLast(tSucc);
      }

      this.index2target.put(index, target);
      TreeSet indices = null;
      if ((indices = (TreeSet)this.tSucc2indexSet.get(tSucc)) == null) {
         indices = new TreeSet(new IndexComparator());
         this.tSucc2indexSet.put(tSucc, indices);
         this.tSucc2target.put(tSucc, target);
         this.tSucc2Body.put(tSucc, this.find_SubBody(swAs, target));
         this.tSuccList.add(tSucc);
      } else {
         this.junkBody.add(target);
         Iterator sit = target.bsuccs.iterator();

         while(sit.hasNext()) {
            ((AugmentedStmt)sit.next()).bpreds.remove(target);
         }

         sit = target.csuccs.iterator();

         while(sit.hasNext()) {
            ((AugmentedStmt)sit.next()).cpreds.remove(target);
         }

         target.bsuccs.clear();
         target.csuccs.clear();
      }

      indices.add(index);
   }
}
