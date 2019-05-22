package soot.dava.toolkits.base.finders;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETDoWhileNode;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETUnconditionalWhileNode;
import soot.dava.internal.SET.SETWhileNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.dava.internal.javaRep.DIntConstant;
import soot.dava.toolkits.base.misc.ConditionFlipper;
import soot.grimp.internal.GAssignStmt;
import soot.grimp.internal.GTableSwitchStmt;
import soot.jimple.AssignStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;
import soot.jimple.TableSwitchStmt;
import soot.jimple.internal.JGotoStmt;
import soot.toolkits.graph.StronglyConnectedComponentsFast;
import soot.util.IterableSet;
import soot.util.StationaryArrayList;

public class CycleFinder implements FactFinder {
   private static final Logger logger = LoggerFactory.getLogger(CycleFinder.class);

   public CycleFinder(Singletons.Global g) {
   }

   public static CycleFinder v() {
      return G.v().soot_dava_toolkits_base_finders_CycleFinder();
   }

   public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
      Dava.v().log("CycleFinder::find()");
      AugmentedStmtGraph wasg = (AugmentedStmtGraph)asg.clone();

      for(List component_list = this.build_component_list(wasg); !component_list.isEmpty(); component_list = this.build_component_list(wasg)) {
         IterableSet<AugmentedStmt> node_list = new IterableSet();
         Iterator var7 = component_list.iterator();

         while(var7.hasNext()) {
            List<AugmentedStmt> cal = (List)var7.next();
            node_list.clear();
            node_list.addAll(cal);
            IterableSet<AugmentedStmt> entry_points = this.get_EntryPoint(node_list);
            AugmentedStmt succ_stmt;
            if (entry_points.size() > 1) {
               LinkedList<AugmentedStmt> asgEntryPoints = new LinkedList();
               Iterator var21 = entry_points.iterator();

               while(var21.hasNext()) {
                  succ_stmt = (AugmentedStmt)var21.next();
                  asgEntryPoints.addLast(asg.get_AugStmt(succ_stmt.get_Stmt()));
               }

               IterableSet<AugmentedStmt> asgScc = new IterableSet();
               Iterator var24 = node_list.iterator();

               while(var24.hasNext()) {
                  AugmentedStmt au = (AugmentedStmt)var24.next();
                  asgScc.addLast(asg.get_AugStmt(au.get_Stmt()));
               }

               this.fix_MultiEntryPoint(body, asg, asgEntryPoints, asgScc);
               throw new RetriggerAnalysisException();
            }

            AugmentedStmt entry_point = (AugmentedStmt)entry_points.getFirst();
            AugmentedStmt characterizing_stmt = this.find_CharacterizingStmt(entry_point, node_list, wasg);
            succ_stmt = null;
            AugmentedStmt au;
            if (characterizing_stmt != null) {
               Iterator var13 = characterizing_stmt.bsuccs.iterator();

               while(var13.hasNext()) {
                  au = (AugmentedStmt)var13.next();
                  succ_stmt = au;
                  if (!node_list.contains(au)) {
                     break;
                  }
               }
            }

            wasg.calculate_Reachability((AugmentedStmt)succ_stmt, (Set)(new HashSet()), entry_point);
            IterableSet<AugmentedStmt> cycle_body = this.get_CycleBody(entry_point, succ_stmt, asg, wasg);
            au = null;
            if (characterizing_stmt != null) {
               Iterator var15 = body.get_ExceptionFacts().iterator();

               label93:
               while(true) {
                  IterableSet tryBody;
                  do {
                     if (!var15.hasNext()) {
                        break label93;
                     }

                     ExceptionNode en = (ExceptionNode)var15.next();
                     tryBody = en.get_TryBody();
                  } while(!tryBody.contains(asg.get_AugStmt(characterizing_stmt.get_Stmt())));

                  Iterator var18 = cycle_body.iterator();

                  while(var18.hasNext()) {
                     AugmentedStmt cbas = (AugmentedStmt)var18.next();
                     if (!tryBody.contains(cbas)) {
                        characterizing_stmt = null;
                        break label93;
                     }
                  }
               }
            }

            Object newNode;
            if (characterizing_stmt == null) {
               wasg.remove_AugmentedStmt(entry_point);
               newNode = new SETUnconditionalWhileNode(cycle_body);
            } else {
               body.consume_Condition(asg.get_AugStmt(characterizing_stmt.get_Stmt()));
               wasg.remove_AugmentedStmt(characterizing_stmt);
               IfStmt condition = (IfStmt)characterizing_stmt.get_Stmt();
               if (!cycle_body.contains(asg.get_AugStmt(condition.getTarget()))) {
                  condition.setCondition(ConditionFlipper.flip((ConditionExpr)condition.getCondition()));
               }

               if (characterizing_stmt == entry_point) {
                  newNode = new SETWhileNode(asg.get_AugStmt(characterizing_stmt.get_Stmt()), cycle_body);
               } else {
                  newNode = new SETDoWhileNode(asg.get_AugStmt(characterizing_stmt.get_Stmt()), asg.get_AugStmt(entry_point.get_Stmt()), cycle_body);
               }
            }

            if (newNode != null) {
               SET.nest((SETNode)newNode);
            }
         }
      }

   }

   private IterableSet<AugmentedStmt> get_EntryPoint(IterableSet<AugmentedStmt> nodeList) {
      IterableSet<AugmentedStmt> entryPoints = new IterableSet();
      Iterator var3 = nodeList.iterator();

      while(true) {
         while(var3.hasNext()) {
            AugmentedStmt as = (AugmentedStmt)var3.next();
            Iterator var5 = as.cpreds.iterator();

            while(var5.hasNext()) {
               AugmentedStmt po = (AugmentedStmt)var5.next();
               if (!nodeList.contains(po)) {
                  entryPoints.add(as);
                  break;
               }
            }
         }

         return entryPoints;
      }
   }

   private List<List<AugmentedStmt>> build_component_list(AugmentedStmtGraph asg) {
      List<List<AugmentedStmt>> c_list = new LinkedList();
      StronglyConnectedComponentsFast<AugmentedStmt> scc = new StronglyConnectedComponentsFast(asg);
      Iterator var4 = scc.getComponents().iterator();

      while(var4.hasNext()) {
         List<AugmentedStmt> wcomp = (List)var4.next();
         if (wcomp.size() > 1) {
            c_list.add(wcomp);
         } else if (wcomp.size() == 1) {
            AugmentedStmt as = (AugmentedStmt)wcomp.get(0);
            if (as.cpreds.contains(as) && as.csuccs.contains(as)) {
               List<AugmentedStmt> currentComponent = null;
               currentComponent = new StationaryArrayList();
               currentComponent.add(as);
               c_list.add(currentComponent);
            }
         }
      }

      return c_list;
   }

   private AugmentedStmt find_CharacterizingStmt(AugmentedStmt entry_point, IterableSet<AugmentedStmt> sc_component, AugmentedStmtGraph asg) {
      if (entry_point.get_Stmt() instanceof IfStmt) {
         Iterator var4 = entry_point.bsuccs.iterator();

         while(var4.hasNext()) {
            AugmentedStmt au = (AugmentedStmt)var4.next();
            if (!sc_component.contains(au)) {
               return entry_point;
            }
         }
      }

      IterableSet<AugmentedStmt> candidates = new IterableSet();
      HashMap<AugmentedStmt, AugmentedStmt> candSuccMap = new HashMap();
      HashSet<AugmentedStmt> blockers = new HashSet();
      Iterator var7 = entry_point.bpreds.iterator();

      Iterator var9;
      AugmentedStmt as;
      while(var7.hasNext()) {
         AugmentedStmt pas = (AugmentedStmt)var7.next();
         if (pas.get_Stmt() instanceof GotoStmt && pas.bpreds.size() == 1) {
            pas = (AugmentedStmt)pas.bpreds.get(0);
         }

         if (sc_component.contains(pas) && pas.get_Stmt() instanceof IfStmt) {
            var9 = pas.bsuccs.iterator();

            while(var9.hasNext()) {
               as = (AugmentedStmt)var9.next();
               if (!sc_component.contains(as)) {
                  candidates.add(pas);
                  candSuccMap.put(pas, as);
                  blockers.add(as);
                  break;
               }
            }
         }
      }

      if (candidates.isEmpty()) {
         return null;
      } else if (candidates.size() == 1) {
         return (AugmentedStmt)candidates.getFirst();
      } else {
         asg.calculate_Reachability((Collection)candidates, (Set)blockers, entry_point);
         IterableSet<AugmentedStmt> max_Reach_Set = null;
         int reachSize = 0;
         var9 = candidates.iterator();

         while(var9.hasNext()) {
            as = (AugmentedStmt)var9.next();
            int current_reach_size = ((AugmentedStmt)candSuccMap.get(as)).get_Reachers().intersection(candidates).size();
            if (current_reach_size > reachSize) {
               max_Reach_Set = new IterableSet();
               reachSize = current_reach_size;
            }

            if (current_reach_size == reachSize) {
               max_Reach_Set.add(as);
            }
         }

         candidates = max_Reach_Set;
         if (max_Reach_Set == null) {
            throw new RuntimeException("Did not find a suitable candidate");
         } else if (max_Reach_Set.size() == 1) {
            return (AugmentedStmt)max_Reach_Set.getFirst();
         } else {
            HashSet<Object> touchSet = new HashSet();
            LinkedList<AugmentedStmt> worklist = new LinkedList();
            worklist.addLast(entry_point);
            touchSet.add(entry_point);

            while(!worklist.isEmpty()) {
               Iterator var19 = ((AugmentedStmt)worklist.removeFirst()).csuccs.iterator();

               while(var19.hasNext()) {
                  AugmentedStmt so = (AugmentedStmt)var19.next();
                  if (candidates.contains(so)) {
                     return so;
                  }

                  if (sc_component.contains(so) && !touchSet.contains(so)) {
                     worklist.addLast(so);
                     touchSet.add(so);
                  }
               }
            }

            throw new RuntimeException("Somehow didn't find a condition for a do-while loop!");
         }
      }
   }

   private IterableSet<AugmentedStmt> get_CycleBody(AugmentedStmt entry_point, AugmentedStmt boundary_stmt, AugmentedStmtGraph asg, AugmentedStmtGraph wasg) {
      IterableSet<AugmentedStmt> cycle_body = new IterableSet();
      LinkedList<AugmentedStmt> worklist = new LinkedList();
      AugmentedStmt asg_ep = asg.get_AugStmt(entry_point.get_Stmt());
      worklist.add(entry_point);
      cycle_body.add(asg_ep);

      label47:
      while(!worklist.isEmpty()) {
         AugmentedStmt as = (AugmentedStmt)worklist.removeFirst();
         Iterator var9 = as.csuccs.iterator();

         while(true) {
            AugmentedStmt wsas;
            AugmentedStmt sas;
            do {
               do {
                  do {
                     do {
                        if (!var9.hasNext()) {
                           continue label47;
                        }

                        wsas = (AugmentedStmt)var9.next();
                        sas = asg.get_AugStmt(wsas.get_Stmt());
                     } while(cycle_body.contains(sas));
                  } while(cycle_body.contains(sas));
               } while(!sas.get_Dominators().contains(asg_ep));
            } while(boundary_stmt != null && (wsas.get_Reachers().contains(boundary_stmt) || wsas == boundary_stmt));

            worklist.add(wsas);
            cycle_body.add(sas);
         }
      }

      return cycle_body;
   }

   private void fix_MultiEntryPoint(DavaBody body, AugmentedStmtGraph asg, LinkedList<AugmentedStmt> entry_points, IterableSet<AugmentedStmt> scc) {
      AugmentedStmt naturalEntryPoint = this.get_NaturalEntryPoint(entry_points, scc);
      Local controlLocal = body.get_ControlLocal();
      Unit defaultTarget = naturalEntryPoint.get_Stmt();
      LinkedList<AugmentedStmt> targets = new LinkedList();

      for(int i = 0; i < entry_points.size(); ++i) {
         targets.add((Object)null);
      }

      TableSwitchStmt tss = new GTableSwitchStmt(controlLocal, 0, entry_points.size() - 2, targets, defaultTarget);
      AugmentedStmt dispatchStmt = new AugmentedStmt(tss);
      IterableSet<AugmentedStmt> predecessorSet = new IterableSet();
      IterableSet<AugmentedStmt> indirectionStmtSet = new IterableSet();
      IterableSet<AugmentedStmt> directionStmtSet = new IterableSet();
      int count = 0;
      Iterator epit = entry_points.iterator();

      label92:
      while(epit.hasNext()) {
         AugmentedStmt entryPoint = (AugmentedStmt)epit.next();
         GotoStmt gotoStmt = new JGotoStmt(entryPoint.get_Stmt());
         AugmentedStmt indirectionStmt = new AugmentedStmt(gotoStmt);
         indirectionStmtSet.add(indirectionStmt);
         tss.setTarget(count++, gotoStmt);
         dispatchStmt.add_BSucc(indirectionStmt);
         indirectionStmt.add_BPred(dispatchStmt);
         indirectionStmt.add_BSucc(entryPoint);
         entryPoint.add_BPred(indirectionStmt);
         asg.add_AugmentedStmt(indirectionStmt);
         LinkedList<AugmentedStmt> toRemove = new LinkedList();
         Iterator var20 = entryPoint.cpreds.iterator();

         while(true) {
            AugmentedStmt pas;
            do {
               do {
                  if (!var20.hasNext()) {
                     var20 = toRemove.iterator();

                     while(var20.hasNext()) {
                        pas = (AugmentedStmt)var20.next();
                        entryPoint.cpreds.remove(pas);
                        if (entryPoint.bpreds.contains(pas)) {
                           entryPoint.bpreds.remove(pas);
                        }
                     }
                     continue label92;
                  }

                  pas = (AugmentedStmt)var20.next();
               } while(pas == indirectionStmt);
            } while(entryPoint != naturalEntryPoint && scc.contains(pas));

            if (!scc.contains(pas)) {
               predecessorSet.add(pas);
            }

            AssignStmt asnStmt = new GAssignStmt(controlLocal, DIntConstant.v(count, (Type)null));
            AugmentedStmt directionStmt = new AugmentedStmt(asnStmt);
            directionStmtSet.add(directionStmt);
            this.patch_Stmt(pas.get_Stmt(), entryPoint.get_Stmt(), asnStmt);
            toRemove.addLast(pas);
            pas.csuccs.remove(entryPoint);
            pas.csuccs.add(directionStmt);
            if (pas.bsuccs.contains(entryPoint)) {
               pas.bsuccs.remove(entryPoint);
               pas.bsuccs.add(directionStmt);
            }

            directionStmt.cpreds.add(pas);
            if (pas.bsuccs.contains(directionStmt)) {
               directionStmt.bpreds.add(pas);
            }

            directionStmt.add_BSucc(dispatchStmt);
            dispatchStmt.add_BPred(directionStmt);
            asg.add_AugmentedStmt(directionStmt);
         }
      }

      asg.add_AugmentedStmt(dispatchStmt);
      Iterator var25 = body.get_ExceptionFacts().iterator();

      while(true) {
         while(true) {
            label57:
            while(var25.hasNext()) {
               ExceptionNode en = (ExceptionNode)var25.next();
               IterableSet<AugmentedStmt> tryBody = en.get_TryBody();
               Iterator var28 = entry_points.iterator();

               AugmentedStmt au;
               while(var28.hasNext()) {
                  au = (AugmentedStmt)var28.next();
                  if (!tryBody.contains(au)) {
                     continue label57;
                  }
               }

               en.add_TryStmts(indirectionStmtSet);
               en.add_TryStmt(dispatchStmt);
               var28 = predecessorSet.iterator();

               while(var28.hasNext()) {
                  au = (AugmentedStmt)var28.next();
                  if (!tryBody.contains(au)) {
                     continue label57;
                  }
               }

               en.add_TryStmts(directionStmtSet);
            }

            return;
         }
      }
   }

   private AugmentedStmt get_NaturalEntryPoint(LinkedList<AugmentedStmt> entry_points, IterableSet<AugmentedStmt> scc) {
      AugmentedStmt best_candidate = null;
      int minScore = 0;
      Iterator epit = entry_points.iterator();

      while(true) {
         AugmentedStmt entryPoint;
         HashSet touchSet;
         HashSet backTargets;
         do {
            if (!epit.hasNext()) {
               return best_candidate;
            }

            entryPoint = (AugmentedStmt)epit.next();
            touchSet = new HashSet();
            backTargets = new HashSet();
            touchSet.add(entryPoint);
            this.DFS(entryPoint, touchSet, backTargets, scc);
         } while(best_candidate != null && backTargets.size() >= minScore);

         minScore = touchSet.size();
         best_candidate = entryPoint;
      }
   }

   private void DFS(AugmentedStmt as, HashSet<AugmentedStmt> touchSet, HashSet<AugmentedStmt> backTargets, IterableSet<AugmentedStmt> scc) {
      Iterator var5 = as.csuccs.iterator();

      while(var5.hasNext()) {
         AugmentedStmt sas = (AugmentedStmt)var5.next();
         if (scc.contains(sas)) {
            if (touchSet.contains(sas)) {
               if (!backTargets.contains(sas)) {
                  backTargets.add(sas);
               }
            } else {
               touchSet.add(sas);
               this.DFS(sas, touchSet, backTargets, scc);
               touchSet.remove(sas);
            }
         }
      }

   }

   private void patch_Stmt(Stmt src, Stmt oldDst, Stmt newDst) {
      if (src instanceof GotoStmt) {
         ((GotoStmt)src).setTarget(newDst);
      } else if (src instanceof IfStmt) {
         IfStmt ifs = (IfStmt)src;
         if (ifs.getTarget() == oldDst) {
            ifs.setTarget(newDst);
         }

      } else {
         int i;
         if (src instanceof TableSwitchStmt) {
            TableSwitchStmt tss = (TableSwitchStmt)src;
            if (tss.getDefaultTarget() == oldDst) {
               tss.setDefaultTarget(newDst);
               return;
            }

            for(i = tss.getLowIndex(); i <= tss.getHighIndex(); ++i) {
               if (tss.getTarget(i) == oldDst) {
                  tss.setTarget(i, newDst);
                  return;
               }
            }
         }

         if (src instanceof LookupSwitchStmt) {
            LookupSwitchStmt lss = (LookupSwitchStmt)src;
            if (lss.getDefaultTarget() == oldDst) {
               lss.setDefaultTarget(newDst);
               return;
            }

            for(i = 0; i < lss.getTargetCount(); ++i) {
               if (lss.getTarget(i) == oldDst) {
                  lss.setTarget(i, newDst);
                  return;
               }
            }
         }

      }
   }
}
