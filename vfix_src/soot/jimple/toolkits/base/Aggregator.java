package soot.jimple.toolkits.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.Singletons;
import soot.Timers;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.MonitorStmt;
import soot.jimple.Stmt;
import soot.jimple.StmtBody;
import soot.options.Options;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.PseudoTopologicalOrderer;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;

public class Aggregator extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(Aggregator.class);

   public Aggregator(Singletons.Global g) {
   }

   public static Aggregator v() {
      return G.v().soot_jimple_toolkits_base_Aggregator();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      StmtBody body = (StmtBody)b;
      boolean onlyStackVars = PhaseOptions.getBoolean(options, "only-stack-locals");
      if (Options.v().time()) {
         Timers.v().aggregationTimer.start();
      }

      int aggregateCount = 1;
      boolean changed = false;
      Map<ValueBox, Zone> boxToZone = new HashMap(body.getUnits().size() * 2 + 1, 0.7F);
      Zonation zonation = new Zonation(body);
      Iterator var10 = body.getUnits().iterator();

      while(var10.hasNext()) {
         Unit u = (Unit)var10.next();
         Zone zone = zonation.getZoneOf(u);
         Iterator var13 = u.getUseBoxes().iterator();

         ValueBox box;
         while(var13.hasNext()) {
            box = (ValueBox)var13.next();
            boxToZone.put(box, zone);
         }

         var13 = u.getDefBoxes().iterator();

         while(var13.hasNext()) {
            box = (ValueBox)var13.next();
            boxToZone.put(box, zone);
         }
      }

      do {
         if (Options.v().verbose()) {
            logger.debug("[" + body.getMethod().getName() + "] Aggregating iteration " + aggregateCount + "...");
         }

         changed = internalAggregate(body, boxToZone, onlyStackVars);
         ++aggregateCount;
      } while(changed);

      if (Options.v().time()) {
         Timers.v().aggregationTimer.end();
      }

   }

   private static boolean internalAggregate(StmtBody body, Map<ValueBox, Zone> boxToZone, boolean onlyStackVars) {
      boolean hadAggregation = false;
      Chain<Unit> units = body.getUnits();
      ExceptionalUnitGraph graph = new ExceptionalUnitGraph(body);
      LocalDefs localDefs = LocalDefs.Factory.newLocalDefs((UnitGraph)graph);
      LocalUses localUses = LocalUses.Factory.newLocalUses((Body)body, localDefs);
      List<Unit> unitList = (new PseudoTopologicalOrderer()).newList(graph, false);
      Iterator var9 = unitList.iterator();

      label198:
      while(true) {
         AssignStmt s;
         UnitValueBoxPair usepair;
         Unit use;
         ValueBox useBox;
         boolean cantAggr;
         boolean propagatingInvokeExpr;
         boolean propagatingFieldRef;
         boolean propagatingArrayRef;
         ArrayList fieldRefList;
         ArrayList localsUsed;
         Value aggregatee;
         List path;
         do {
            List ld;
            do {
               do {
                  Local lhsLocal;
                  List lu;
                  do {
                     do {
                        Value lhs;
                        do {
                           Unit u;
                           do {
                              if (!var9.hasNext()) {
                                 return hadAggregation;
                              }

                              u = (Unit)var9.next();
                           } while(!(u instanceof AssignStmt));

                           s = (AssignStmt)u;
                           lhs = s.getLeftOp();
                        } while(!(lhs instanceof Local));

                        lhsLocal = (Local)lhs;
                     } while(onlyStackVars && !lhsLocal.getName().startsWith("$"));

                     lu = localUses.getUsesOf(s);
                  } while(lu.size() != 1);

                  usepair = (UnitValueBoxPair)lu.get(0);
                  use = usepair.unit;
                  useBox = usepair.valueBox;
                  ld = localDefs.getDefsOfAt(lhsLocal, use);
               } while(ld.size() != 1);
            } while(boxToZone.get(s.getRightOpBox()) != boxToZone.get(usepair.valueBox));

            cantAggr = false;
            propagatingInvokeExpr = false;
            propagatingFieldRef = false;
            propagatingArrayRef = false;
            fieldRefList = new ArrayList();
            localsUsed = new ArrayList();
            Iterator var25 = s.getUseBoxes().iterator();

            while(var25.hasNext()) {
               ValueBox vb = (ValueBox)var25.next();
               aggregatee = vb.getValue();
               if (aggregatee instanceof Local) {
                  localsUsed.add(aggregatee);
               } else if (aggregatee instanceof InvokeExpr) {
                  propagatingInvokeExpr = true;
               } else if (aggregatee instanceof ArrayRef) {
                  propagatingArrayRef = true;
               } else if (aggregatee instanceof FieldRef) {
                  propagatingFieldRef = true;
                  fieldRefList.add((FieldRef)aggregatee);
               }
            }

            path = graph.getExtendedBasicBlockPathBetween(s, use);
         } while(path == null);

         Iterator<Unit> pathIt = path.iterator();
         if (pathIt.hasNext()) {
            pathIt.next();
         }

         while(true) {
            while(true) {
               Iterator var28;
               ValueBox box;
               Value v;
               Stmt between;
               do {
                  if (!pathIt.hasNext() || cantAggr) {
                     if (!cantAggr) {
                        aggregatee = s.getRightOp();
                        if (usepair.valueBox.canContainValue(aggregatee)) {
                           boolean wasSimpleCopy = isSimpleCopy(usepair.unit);
                           usepair.valueBox.setValue(aggregatee);
                           units.remove(s);
                           hadAggregation = true;
                           if (wasSimpleCopy) {
                              usepair.unit.addAllTagsOf(s);
                           }
                        }
                     }
                     continue label198;
                  }

                  between = (Stmt)((Stmt)pathIt.next());
                  if (between != use) {
                     var28 = between.getDefBoxes().iterator();

                     while(var28.hasNext()) {
                        box = (ValueBox)var28.next();
                        v = box.getValue();
                        if (localsUsed.contains(v)) {
                           cantAggr = true;
                           break;
                        }

                        if (propagatingInvokeExpr || propagatingFieldRef || propagatingArrayRef) {
                           if (v instanceof FieldRef) {
                              if (propagatingInvokeExpr) {
                                 cantAggr = true;
                                 break;
                              }

                              if (propagatingFieldRef) {
                                 Iterator var31 = fieldRefList.iterator();

                                 while(var31.hasNext()) {
                                    FieldRef fieldRef = (FieldRef)var31.next();
                                    if (isSameField((FieldRef)v, fieldRef)) {
                                       cantAggr = true;
                                       break;
                                    }
                                 }
                              }
                           } else if (v instanceof ArrayRef) {
                              if (propagatingInvokeExpr) {
                                 cantAggr = true;
                                 break;
                              }

                              if (propagatingArrayRef) {
                                 cantAggr = true;
                                 break;
                              }
                           }
                        }
                     }

                     if (propagatingInvokeExpr && between instanceof MonitorStmt) {
                        cantAggr = true;
                     }
                  }
               } while(!propagatingInvokeExpr && !propagatingFieldRef && !propagatingArrayRef);

               var28 = between.getUseBoxes().iterator();

               while(var28.hasNext()) {
                  box = (ValueBox)var28.next();
                  if (between == use && box == useBox) {
                     break;
                  }

                  v = box.getValue();
                  if (v instanceof InvokeExpr || propagatingInvokeExpr && (v instanceof FieldRef || v instanceof ArrayRef)) {
                     cantAggr = true;
                     break;
                  }
               }
            }
         }
      }
   }

   private static boolean isSameField(FieldRef ref1, FieldRef ref2) {
      return ref1 == ref2 ? true : ref1.getFieldRef().equals(ref2.getFieldRef());
   }

   private static boolean isSimpleCopy(Unit u) {
      if (!(u instanceof DefinitionStmt)) {
         return false;
      } else {
         DefinitionStmt defstmt = (DefinitionStmt)u;
         if (!(defstmt.getRightOp() instanceof Local)) {
            return false;
         } else {
            return defstmt.getLeftOp() instanceof Local;
         }
      }
   }
}
