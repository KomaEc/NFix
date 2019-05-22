package soot.dava.toolkits.base.finders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import soot.G;
import soot.Local;
import soot.RefType;
import soot.Singletons;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETSynchronizedBlockNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.GotoStmt;
import soot.jimple.MonitorStmt;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.internal.JGotoStmt;
import soot.toolkits.graph.StronglyConnectedComponentsFast;
import soot.util.IterableSet;

public class SynchronizedBlockFinder implements FactFinder {
   private HashMap<AugmentedStmt, Map<Value, Integer>> as2ml;
   private DavaBody davaBody;
   private IterableSet monitorLocalSet;
   private IterableSet monitorEnterSet;
   private final Integer WHITE = new Integer(0);
   private final Integer GRAY = new Integer(1);
   private final Integer BLACK = new Integer(2);
   private final int UNKNOWN = -100000;
   private final Integer VARIABLE_INCR = new Integer(-100000);
   private final String THROWABLE = "java.lang.Throwable";

   public SynchronizedBlockFinder(Singletons.Global g) {
   }

   public static SynchronizedBlockFinder v() {
      return G.v().soot_dava_toolkits_base_finders_SynchronizedBlockFinder();
   }

   public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
      this.davaBody = body;
      Dava.v().log("SynchronizedBlockFinder::find()");
      this.as2ml = new HashMap();
      IterableSet synchronizedBlockFacts = body.get_SynchronizedBlockFacts();
      synchronizedBlockFacts.clear();
      this.set_MonitorLevels(asg);
      Map<AugmentedStmt, IterableSet> as2synchSet = this.build_SynchSets();
      IterableSet usedMonitors = new IterableSet();
      AugmentedStmt previousStmt = null;

      AugmentedStmt as;
      for(Iterator asgit = asg.iterator(); asgit.hasNext(); previousStmt = as) {
         as = (AugmentedStmt)asgit.next();
         if (as.get_Stmt() instanceof EnterMonitorStmt) {
            IterableSet synchSet = (IterableSet)as2synchSet.get(as);
            if (synchSet != null) {
               IterableSet synchBody = this.get_BodyApproximation(as, synchSet);
               Value local = ((EnterMonitorStmt)as.get_Stmt()).getOp();
               Value copiedLocal = null;
               if (previousStmt != null) {
                  Stmt previousS = previousStmt.get_Stmt();
                  if (previousS instanceof DefinitionStmt) {
                     DefinitionStmt previousDef = (DefinitionStmt)previousS;
                     Value rightPrevious = previousDef.getRightOp();
                     if (rightPrevious.toString().compareTo(local.toString()) == 0) {
                        copiedLocal = previousDef.getLeftOp();
                     }
                  }
               }

               Integer level = (Integer)((Map)this.as2ml.get(as)).get(local);
               Iterator enit = body.get_ExceptionFacts().iterator();
               boolean done = false;
               IterableSet origSynchBody = synchBody;

               label82:
               while(enit.hasNext()) {
                  ExceptionNode en = (ExceptionNode)enit.next();
                  synchBody = (IterableSet)origSynchBody.clone();
                  if (this.verify_CatchBody(en, synchBody, local, copiedLocal)) {
                     if (SET.nest(new SETSynchronizedBlockNode(en, local))) {
                        done = true;
                        Iterator ssit = synchSet.iterator();

                        while(true) {
                           while(true) {
                              AugmentedStmt ssas;
                              Stmt sss;
                              do {
                                 if (!ssit.hasNext()) {
                                    synchronizedBlockFacts.add(en);
                                    break label82;
                                 }

                                 ssas = (AugmentedStmt)ssit.next();
                                 sss = ssas.get_Stmt();
                              } while(!(sss instanceof MonitorStmt));

                              if (((MonitorStmt)sss).getOp() == local && ((Integer)((Map)this.as2ml.get(ssas)).get(local)).equals(level) && !usedMonitors.contains(ssas)) {
                                 usedMonitors.add(ssas);
                              } else if (((MonitorStmt)sss).getOp() == copiedLocal && !usedMonitors.contains(ssas)) {
                                 usedMonitors.add(ssas);
                              }
                           }
                        }
                     }
                     break;
                  }
               }

               if (!done) {
                  throw new RuntimeException("Could not verify approximated Synchronized body!\nMethod:\n" + body.getMethod() + "Body:\n===============================================================\n" + body.getUnits() + "===============================================================\n");
               }
            }
         }
      }

      IterableSet<AugmentedStmt> monitorFacts = body.get_MonitorFacts();
      monitorFacts.clear();
      Iterator var23 = asg.iterator();

      while(var23.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)var23.next();
         if (as.get_Stmt() instanceof MonitorStmt && !usedMonitors.contains(as)) {
            monitorFacts.add(as);
         }
      }

   }

   private void find_VariableIncreasing(AugmentedStmtGraph asg, HashMap local2level_template, LinkedList<AugmentedStmt> viAugStmts, HashMap<AugmentedStmt, LinkedList<Value>> as2locals) {
      StronglyConnectedComponentsFast scc = new StronglyConnectedComponentsFast(asg);
      IterableSet viSeeds = new IterableSet();
      HashMap as2color = new HashMap();
      HashMap as2rml = new HashMap();
      Iterator asgit = asg.iterator();

      while(asgit.hasNext()) {
         as2rml.put(asgit.next(), local2level_template.clone());
      }

      Iterator sccit = scc.getTrueComponents().iterator();

      while(sccit.hasNext()) {
         List componentList = (List)sccit.next();
         IterableSet component = new IterableSet();
         component.addAll(componentList);
         Iterator cit = component.iterator();

         while(cit.hasNext()) {
            as2color.put(cit.next(), this.WHITE);
         }

         AugmentedStmt seedStmt = (AugmentedStmt)component.getFirst();
         this.DFS_Scc(seedStmt, component, as2rml, as2color, seedStmt, viSeeds);
      }

      IterableSet worklist = new IterableSet();
      worklist.addAll(viSeeds);

      HashMap locals;
      AugmentedStmt as;
      HashMap local2level;
      Iterator mlsit;
      while(!worklist.isEmpty()) {
         as = (AugmentedStmt)worklist.getFirst();
         worklist.removeFirst();
         local2level = (HashMap)as2rml.get(as);
         mlsit = as.csuccs.iterator();

         while(mlsit.hasNext()) {
            AugmentedStmt sas = (AugmentedStmt)mlsit.next();
            locals = (HashMap)as2rml.get(sas);
            Iterator mlsit = this.monitorLocalSet.iterator();

            while(mlsit.hasNext()) {
               Value local = (Value)mlsit.next();
               if (local2level.get(local) == this.VARIABLE_INCR && locals.get(local) != this.VARIABLE_INCR) {
                  locals.put(local, this.VARIABLE_INCR);
                  if (!worklist.contains(sas)) {
                     worklist.addLast(sas);
                  }
               }
            }
         }
      }

      asgit = asg.iterator();

      while(asgit.hasNext()) {
         as = (AugmentedStmt)asgit.next();
         local2level = (HashMap)as2rml.get(as);
         mlsit = this.monitorLocalSet.iterator();

         while(mlsit.hasNext()) {
            Value local = (Value)mlsit.next();
            if (local2level.get(local) == this.VARIABLE_INCR) {
               if (!viAugStmts.isEmpty()) {
                  if (viAugStmts.getLast() != as) {
                     viAugStmts.addLast(as);
                  }
               } else {
                  viAugStmts.addLast(as);
               }

               locals = null;
               LinkedList locals;
               if ((locals = (LinkedList)as2locals.get(as)) == null) {
                  locals = new LinkedList();
                  as2locals.put(as, locals);
               }

               locals.addLast(local);
            }
         }
      }

   }

   private void DFS_Scc(AugmentedStmt as, IterableSet component, HashMap as2rml, HashMap as2color, AugmentedStmt seedStmt, IterableSet viSeeds) {
      as2color.put(as, this.GRAY);
      Stmt s = as.get_Stmt();
      HashMap<Value, Integer> local2level = (HashMap)as2rml.get(as);
      if (s instanceof MonitorStmt) {
         Value local = ((MonitorStmt)s).getOp();
         if (s instanceof EnterMonitorStmt) {
            local2level.put(local, new Integer((Integer)local2level.get(local) + 1));
         } else {
            local2level.put(local, new Integer((Integer)local2level.get(local) - 1));
         }
      }

      Iterator sit = as.csuccs.iterator();

      while(true) {
         while(true) {
            AugmentedStmt sas;
            do {
               if (!sit.hasNext()) {
                  as2color.put(as, this.BLACK);
                  return;
               }

               sas = (AugmentedStmt)sit.next();
            } while(!component.contains(sas));

            HashMap<Value, Integer> slocal2level = (HashMap)as2rml.get(sas);
            Integer scolor = (Integer)as2color.get(sas);
            Iterator mlsit;
            Value local;
            if (scolor.equals(this.WHITE)) {
               mlsit = this.monitorLocalSet.iterator();

               while(mlsit.hasNext()) {
                  local = (Value)mlsit.next();
                  slocal2level.put(local, local2level.get(local));
               }

               this.DFS_Scc(sas, component, as2rml, as2color, seedStmt, viSeeds);
            } else {
               mlsit = this.monitorLocalSet.iterator();

               while(mlsit.hasNext()) {
                  local = (Value)mlsit.next();
                  if ((Integer)slocal2level.get(local) < (Integer)local2level.get(local)) {
                     slocal2level.put(local, this.VARIABLE_INCR);
                     if (!viSeeds.contains(sas)) {
                        viSeeds.add(sas);
                     }
                  }
               }
            }
         }
      }
   }

   private Map<AugmentedStmt, IterableSet> build_SynchSets() {
      HashMap<AugmentedStmt, IterableSet> as2synchSet = new HashMap();
      Iterator mesit = this.monitorEnterSet.iterator();

      while(true) {
         label41:
         while(mesit.hasNext()) {
            AugmentedStmt headAs = (AugmentedStmt)mesit.next();
            Value local = ((EnterMonitorStmt)headAs.get_Stmt()).getOp();
            IterableSet synchSet = new IterableSet();
            int monitorLevel = (Integer)((Map)this.as2ml.get(headAs)).get(local);
            IterableSet worklist = new IterableSet();
            worklist.add(headAs);

            while(!worklist.isEmpty()) {
               AugmentedStmt as = (AugmentedStmt)worklist.getFirst();
               worklist.removeFirst();
               Stmt s = as.get_Stmt();
               if (s instanceof DefinitionStmt && ((DefinitionStmt)s).getLeftOp() == local) {
                  continue label41;
               }

               synchSet.add(as);
               Iterator sit = as.csuccs.iterator();

               while(sit.hasNext()) {
                  AugmentedStmt sas = (AugmentedStmt)sit.next();
                  int sml = (Integer)((Map)this.as2ml.get(sas)).get(local);
                  if (sas.get_Dominators().contains(headAs) && sml >= monitorLevel && !worklist.contains(sas) && !synchSet.contains(sas)) {
                     worklist.addLast(sas);
                  }
               }
            }

            as2synchSet.put(headAs, synchSet);
         }

         return as2synchSet;
      }
   }

   private void set_MonitorLevels(AugmentedStmtGraph asg) {
      this.monitorLocalSet = new IterableSet();
      this.monitorEnterSet = new IterableSet();
      Iterator asgit = asg.iterator();

      while(asgit.hasNext()) {
         AugmentedStmt as = (AugmentedStmt)asgit.next();
         Stmt s = as.get_Stmt();
         if (s instanceof MonitorStmt) {
            Value local = ((MonitorStmt)s).getOp();
            if (!this.monitorLocalSet.contains(local)) {
               this.monitorLocalSet.add(local);
            }

            if (s instanceof EnterMonitorStmt) {
               this.monitorEnterSet.add(as);
            }
         }
      }

      HashMap local2level_template = new HashMap();
      Iterator mlsit = this.monitorLocalSet.iterator();

      while(mlsit.hasNext()) {
         local2level_template.put(mlsit.next(), new Integer(0));
      }

      asgit = asg.iterator();

      while(asgit.hasNext()) {
         this.as2ml.put(asgit.next(), (Map)local2level_template.clone());
      }

      LinkedList<AugmentedStmt> viAugStmts = new LinkedList();
      HashMap<AugmentedStmt, LinkedList<Value>> incrAs2locals = new HashMap();
      this.find_VariableIncreasing(asg, local2level_template, viAugStmts, incrAs2locals);
      Iterator viasit = viAugStmts.iterator();

      while(viasit.hasNext()) {
         AugmentedStmt vias = (AugmentedStmt)viasit.next();
         Map local2level = (Map)this.as2ml.get(vias);
         Iterator lit = ((LinkedList)incrAs2locals.get(vias)).iterator();

         while(lit.hasNext()) {
            local2level.put(lit.next(), this.VARIABLE_INCR);
         }
      }

      IterableSet worklist = new IterableSet();
      worklist.addAll(this.monitorEnterSet);

      while(!worklist.isEmpty()) {
         AugmentedStmt as = (AugmentedStmt)worklist.getFirst();
         worklist.removeFirst();
         Map<Value, Integer> cur_local2level = (Map)this.as2ml.get(as);
         Iterator pit = as.cpreds.iterator();

         label86:
         while(pit.hasNext()) {
            AugmentedStmt pas = (AugmentedStmt)pit.next();
            Stmt s = as.get_Stmt();
            Map pred_local2level = (Map)this.as2ml.get(pas);
            mlsit = this.monitorLocalSet.iterator();

            while(true) {
               Value local;
               int predLevel;
               int curLevel;
               do {
                  Stmt ps;
                  do {
                     if (!mlsit.hasNext()) {
                        continue label86;
                     }

                     local = (Value)mlsit.next();
                     predLevel = (Integer)pred_local2level.get(local);
                     ps = pas.get_Stmt();
                  } while(predLevel == -100000);

                  if (ps instanceof ExitMonitorStmt) {
                     ExitMonitorStmt ems = (ExitMonitorStmt)ps;
                     if (ems.getOp() == local && predLevel > 0) {
                        --predLevel;
                     }
                  }

                  if (s instanceof EnterMonitorStmt) {
                     EnterMonitorStmt ems = (EnterMonitorStmt)s;
                     if (ems.getOp() == local && predLevel >= 0) {
                        ++predLevel;
                     }
                  }

                  curLevel = (Integer)cur_local2level.get(local);
               } while(predLevel <= curLevel);

               cur_local2level.put(local, new Integer(predLevel));
               Iterator sit = as.csuccs.iterator();

               while(sit.hasNext()) {
                  Object so = sit.next();
                  if (!worklist.contains(so)) {
                     worklist.add(so);
                  }
               }
            }
         }
      }

   }

   private void removeOtherDominatedStmts(IterableSet synchBody, AugmentedStmt sas) {
      ArrayList toRemove = new ArrayList();
      Iterator it = synchBody.iterator();

      AugmentedStmt as;
      while(it.hasNext()) {
         as = (AugmentedStmt)it.next();
         IterableSet doms = as.get_Dominators();
         if (doms.contains(sas)) {
            toRemove.add(as);
         }
      }

      it = toRemove.iterator();

      while(it.hasNext()) {
         as = (AugmentedStmt)it.next();
         synchBody.remove(as);
      }

   }

   private boolean verify_CatchBody(ExceptionNode en, IterableSet synchBody, Value monitorVariable, Value copiedLocal) {
      IterableSet tryBodySet = en.get_TryBody();
      Iterator synchIt = tryBodySet.iterator();
      AugmentedStmt as = null;

      Iterator succIt;
      AugmentedStmt entryPoint;
      label268:
      while(synchIt.hasNext()) {
         as = (AugmentedStmt)synchIt.next();
         succIt = as.bsuccs.iterator();

         while(succIt.hasNext()) {
            entryPoint = (AugmentedStmt)succIt.next();
            if (!tryBodySet.contains(entryPoint)) {
               break label268;
            }
         }
      }

      if (as != null) {
         succIt = as.bsuccs.iterator();

         while(succIt.hasNext()) {
            entryPoint = (AugmentedStmt)succIt.next();
            synchBody.remove(entryPoint);
            this.removeOtherDominatedStmts(synchBody, entryPoint);
         }
      }

      Iterator tempIt = en.get_TryBody().iterator();

      label251:
      while(true) {
         AugmentedStmt as;
         do {
            if (!tempIt.hasNext()) {
               AugmentedStmt synchEnter = null;
               synchIt = synchBody.iterator();

               label215:
               while(synchIt.hasNext()) {
                  as = (AugmentedStmt)synchIt.next();
                  succIt = as.cpreds.iterator();

                  while(succIt.hasNext()) {
                     entryPoint = (AugmentedStmt)succIt.next();
                     if (!synchBody.contains(entryPoint)) {
                        Stmt pasStmt = entryPoint.get_Stmt();
                        if (pasStmt instanceof EnterMonitorStmt) {
                           synchEnter = as;
                           break label215;
                        }
                     }
                  }
               }

               if (synchEnter == null) {
                  throw new RuntimeException("Could not find enter stmt of the synchBody: " + this.davaBody.getMethod().getSignature());
               }

               boolean unChanged = false;

               Iterator pit;
               AugmentedStmt as;
               label204:
               while(!unChanged) {
                  unChanged = true;
                  List<AugmentedStmt> toRemove = new ArrayList();
                  synchIt = synchBody.iterator();

                  while(true) {
                     do {
                        if (!synchIt.hasNext()) {
                           if (toRemove.size() > 0) {
                              synchBody.removeAll(toRemove);
                              unChanged = false;
                           }
                           continue label204;
                        }

                        entryPoint = (AugmentedStmt)synchIt.next();
                     } while(entryPoint == synchEnter);

                     pit = entryPoint.cpreds.iterator();
                     boolean remove = true;

                     while(pit.hasNext()) {
                        as = (AugmentedStmt)pit.next();
                        if (synchBody.contains(as)) {
                           remove = false;
                        }
                     }

                     if (remove) {
                        toRemove.add(entryPoint);
                     }
                  }
               }

               if (!en.get_Body().equals(synchBody)) {
                  return false;
               }

               if (en.get_Exception().getName().equals("java.lang.Throwable") && en.get_CatchList().size() <= 1) {
                  IterableSet catchBody = en.get_CatchBody();
                  entryPoint = null;
                  pit = catchBody.iterator();

                  label179:
                  while(pit.hasNext()) {
                     AugmentedStmt as = (AugmentedStmt)pit.next();
                     Iterator pit = as.cpreds.iterator();

                     while(pit.hasNext()) {
                        AugmentedStmt pas = (AugmentedStmt)pit.next();
                        if (!catchBody.contains(pas)) {
                           entryPoint = as;
                           break label179;
                        }
                     }
                  }

                  Unit entryPointTarget = null;
                  if (entryPoint.get_Stmt() instanceof GotoStmt) {
                     entryPointTarget = ((JGotoStmt)entryPoint.get_Stmt()).getTarget();
                  }

                  as = entryPoint;
                  if (entryPoint.bsuccs.size() != 1) {
                     return false;
                  }

                  while(as.get_Stmt() instanceof GotoStmt) {
                     as = (AugmentedStmt)as.bsuccs.get(0);
                     if (as.bsuccs.size() != 1) {
                        return false;
                     }

                     if (entryPointTarget != null) {
                        if (as.get_Stmt() != entryPointTarget && as.cpreds.size() != 1 && as.cpreds.size() != 1) {
                           return false;
                        }
                     } else if (as != entryPoint && as.cpreds.size() != 1) {
                        return false;
                     }
                  }

                  Stmt s = as.get_Stmt();
                  if (!(s instanceof DefinitionStmt)) {
                     return false;
                  }

                  DefinitionStmt ds = (DefinitionStmt)s;
                  Value asnFrom = ds.getRightOp();
                  if (asnFrom instanceof CaughtExceptionRef && ((RefType)((CaughtExceptionRef)asnFrom).getType()).getSootClass().getName().equals("java.lang.Throwable")) {
                     Value throwlocal = ds.getLeftOp();
                     IterableSet esuccs = new IterableSet();
                     esuccs.addAll(as.csuccs);
                     esuccs.removeAll(as.bsuccs);
                     as = (AugmentedStmt)as.bsuccs.get(0);

                     for(s = as.get_Stmt(); s instanceof DefinitionStmt && ((DefinitionStmt)s).getRightOp().toString().compareTo(throwlocal.toString()) == 0; s = as.get_Stmt()) {
                        throwlocal = ((DefinitionStmt)s).getLeftOp();
                        as = (AugmentedStmt)as.bsuccs.get(0);
                     }

                     if (as.bsuccs.size() != 1) {
                        return false;
                     }

                     if (as.cpreds.size() != 1) {
                        return false;
                     }

                     this.checkProtectionArea(as, ds);
                     s = as.get_Stmt();
                     if (!(s instanceof ExitMonitorStmt)) {
                        return false;
                     }

                     if (((ExitMonitorStmt)s).getOp() != monitorVariable && ((ExitMonitorStmt)s).getOp() != copiedLocal) {
                        return false;
                     }

                     as = (AugmentedStmt)as.bsuccs.get(0);
                     if (as.bsuccs.size() == 0 && as.cpreds.size() == 1 && this.verify_ESuccs(as, esuccs)) {
                        s = as.get_Stmt();
                        if (s instanceof ThrowStmt && ((ThrowStmt)s).getOp() == throwlocal) {
                           return true;
                        }

                        return false;
                     }

                     return false;
                  }

                  return false;
               }

               return false;
            }

            as = (AugmentedStmt)tempIt.next();
         } while(!(as.get_Stmt() instanceof ExitMonitorStmt));

         List csuccs = as.csuccs;
         csuccs.remove(as.bsuccs);
         succIt = csuccs.iterator();

         while(true) {
            while(true) {
               while(true) {
                  do {
                     if (!succIt.hasNext()) {
                        continue label251;
                     }

                     entryPoint = (AugmentedStmt)succIt.next();
                  } while(!(entryPoint.get_Stmt() instanceof GotoStmt));

                  Unit target = ((JGotoStmt)entryPoint.get_Stmt()).getTarget();
                  if (target instanceof DefinitionStmt) {
                     DefinitionStmt defStmt = (DefinitionStmt)target;
                     Value asnFrom = defStmt.getRightOp();
                     if (!(asnFrom instanceof CaughtExceptionRef)) {
                        synchBody.remove(entryPoint);
                        this.removeOtherDominatedStmts(synchBody, entryPoint);
                     } else {
                        Value leftOp = defStmt.getLeftOp();
                        HashSet params = new HashSet();
                        params.addAll(this.davaBody.get_CaughtRefs());
                        Iterator localIt = this.davaBody.getLocals().iterator();
                        String typeName = "";

                        while(localIt.hasNext()) {
                           Local local = (Local)localIt.next();
                           if (local.toString().compareTo(leftOp.toString()) == 0) {
                              Type t = local.getType();
                              typeName = t.toString();
                              break;
                           }
                        }

                        if (typeName.compareTo("java.lang.Throwable") != 0) {
                           synchBody.remove(entryPoint);
                           this.removeOtherDominatedStmts(synchBody, entryPoint);
                        }
                     }
                  } else {
                     synchBody.remove(entryPoint);
                     synchBody.remove(entryPoint.bsuccs.get(0));
                     this.removeOtherDominatedStmts(synchBody, entryPoint);
                  }
               }
            }
         }
      }
   }

   private boolean checkProtectionArea(AugmentedStmt as, DefinitionStmt s) {
      IterableSet esuccs = new IterableSet();
      esuccs.addAll(as.csuccs);
      esuccs.removeAll(as.bsuccs);
      Iterator it = esuccs.iterator();

      while(it.hasNext()) {
         AugmentedStmt tempas = (AugmentedStmt)it.next();
         Stmt temps = tempas.get_Stmt();
         if (temps instanceof GotoStmt) {
            Unit target = ((GotoStmt)temps).getTarget();
            if (target != s) {
               return false;
            }
         } else if (temps != s) {
            return false;
         }
      }

      return true;
   }

   private boolean verify_ESuccs(AugmentedStmt as, IterableSet ref) {
      IterableSet esuccs = new IterableSet();
      esuccs.addAll(as.csuccs);
      esuccs.removeAll(as.bsuccs);
      return esuccs.equals(ref);
   }

   private IterableSet get_BodyApproximation(AugmentedStmt head, IterableSet synchSet) {
      IterableSet body = (IterableSet)synchSet.clone();
      Value local = ((EnterMonitorStmt)head.get_Stmt()).getOp();
      Integer level = (Integer)((Map)this.as2ml.get(head)).get(local);
      body.remove(head);
      Iterator bit = body.snapshotIterator();

      label50:
      while(true) {
         AugmentedStmt as;
         Stmt s;
         do {
            do {
               do {
                  if (!bit.hasNext()) {
                     return body;
                  }

                  as = (AugmentedStmt)bit.next();
                  s = as.get_Stmt();
               } while(!(s instanceof ExitMonitorStmt));
            } while(((ExitMonitorStmt)s).getOp() != local);
         } while(!((Integer)((Map)this.as2ml.get(as)).get(local)).equals(level));

         Iterator sit = as.csuccs.iterator();

         while(true) {
            AugmentedStmt sas;
            Stmt ss;
            do {
               do {
                  if (!sit.hasNext()) {
                     continue label50;
                  }

                  sas = (AugmentedStmt)sit.next();
               } while(!sas.get_Dominators().contains(head));

               ss = sas.get_Stmt();
            } while(!(ss instanceof GotoStmt) && !(ss instanceof ThrowStmt));

            if (!body.contains(sas)) {
               body.add(sas);
            }
         }
      }
   }
}
