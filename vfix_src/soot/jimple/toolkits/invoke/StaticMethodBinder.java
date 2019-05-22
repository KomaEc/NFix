package soot.jimple.toolkits.invoke;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.ClassMember;
import soot.G;
import soot.Hierarchy;
import soot.Local;
import soot.MethodOrMethodContext;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.TrapManager;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Filter;
import soot.jimple.toolkits.callgraph.InstanceInvokeEdgesPred;
import soot.jimple.toolkits.callgraph.Targets;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.options.SMBOptions;
import soot.util.Chain;

public class StaticMethodBinder extends SceneTransformer {
   public StaticMethodBinder(Singletons.Global g) {
   }

   public static StaticMethodBinder v() {
      return G.v().soot_jimple_toolkits_invoke_StaticMethodBinder();
   }

   protected void internalTransform(String phaseName, Map opts) {
      Filter instanceInvokesFilter = new Filter(new InstanceInvokeEdgesPred());
      SMBOptions options = new SMBOptions(opts);
      String modifierOptions = PhaseOptions.getString(opts, "allowed-modifier-changes");
      HashMap instanceToStaticMap = new HashMap();
      CallGraph cg = Scene.v().getCallGraph();
      Hierarchy hierarchy = Scene.v().getActiveHierarchy();
      Iterator classesIt = Scene.v().getApplicationClasses().iterator();

      label166:
      while(classesIt.hasNext()) {
         SootClass c = (SootClass)classesIt.next();
         LinkedList methodsList = new LinkedList();
         Iterator it = c.methodIterator();

         while(it.hasNext()) {
            methodsList.add(it.next());
         }

         label164:
         while(true) {
            SootMethod container;
            do {
               do {
                  if (methodsList.isEmpty()) {
                     continue label166;
                  }

                  container = (SootMethod)methodsList.removeFirst();
               } while(!container.isConcrete());
            } while(!instanceInvokesFilter.wrap(cg.edgesOutOf((MethodOrMethodContext)container)).hasNext());

            JimpleBody b = (JimpleBody)container.getActiveBody();
            List<Unit> unitList = new ArrayList();
            unitList.addAll(b.getUnits());
            Iterator unitIt = unitList.iterator();

            while(true) {
               Stmt s;
               InvokeExpr ie;
               SootMethod target;
               do {
                  do {
                     do {
                        Targets targets;
                        do {
                           do {
                              do {
                                 do {
                                    do {
                                       do {
                                          if (!unitIt.hasNext()) {
                                             continue label164;
                                          }

                                          s = (Stmt)unitIt.next();
                                       } while(!s.containsInvokeExpr());

                                       ie = s.getInvokeExpr();
                                    } while(ie instanceof StaticInvokeExpr);
                                 } while(ie instanceof SpecialInvokeExpr);

                                 targets = new Targets(instanceInvokesFilter.wrap(cg.edgesOutOf((Unit)s)));
                              } while(!targets.hasNext());

                              target = (SootMethod)targets.next();
                           } while(targets.hasNext());
                        } while(!AccessManager.ensureAccess(container, (ClassMember)target, modifierOptions));
                     } while(!target.getDeclaringClass().isApplicationClass());
                  } while(!target.isConcrete());
               } while(target.getDeclaringClass() == Scene.v().getSootClass("java.lang.Object"));

               if (!instanceToStaticMap.containsKey(target)) {
                  List newParameterTypes = new ArrayList();
                  newParameterTypes.add(RefType.v(target.getDeclaringClass().getName()));
                  newParameterTypes.addAll(target.getParameterTypes());

                  String newName;
                  for(newName = target.getName() + "_static"; target.getDeclaringClass().declaresMethod(newName, newParameterTypes, target.getReturnType()); newName = newName + "_static") {
                  }

                  SootMethod ct = Scene.v().makeSootMethod(newName, newParameterTypes, target.getReturnType(), target.getModifiers() | 8, target.getExceptions());
                  target.getDeclaringClass().addMethod(ct);
                  methodsList.addLast(ct);
                  ct.setActiveBody((Body)target.getActiveBody().clone());
                  Iterator oldUnits = target.getActiveBody().getUnits().iterator();
                  Iterator newUnits = ct.getActiveBody().getUnits().iterator();

                  while(true) {
                     Stmt st;
                     if (!newUnits.hasNext()) {
                        Body newBody = ct.getActiveBody();
                        Chain units = newBody.getUnits();
                        Iterator unitsIt = newBody.getUnits().snapshotIterator();

                        while(unitsIt.hasNext()) {
                           st = (Stmt)unitsIt.next();
                           if (st instanceof IdentityStmt) {
                              IdentityStmt is = (IdentityStmt)st;
                              if (is.getRightOp() instanceof ThisRef) {
                                 units.swapWith(st, Jimple.v().newIdentityStmt(is.getLeftOp(), Jimple.v().newParameterRef(is.getRightOp().getType(), 0)));
                              } else if (is.getRightOp() instanceof ParameterRef) {
                                 ParameterRef ro = (ParameterRef)is.getRightOp();
                                 ro.setIndex(ro.getIndex() + 1);
                              }
                           }
                        }

                        instanceToStaticMap.put(target, ct);
                        break;
                     }

                     Stmt oldStmt = (Stmt)oldUnits.next();
                     st = (Stmt)newUnits.next();
                     Iterator edges = cg.edgesOutOf((Unit)oldStmt);

                     while(edges.hasNext()) {
                        Edge e = (Edge)edges.next();
                        cg.addEdge(new Edge(ct, st, e.tgt(), e.kind()));
                        cg.removeEdge(e);
                     }
                  }
               }

               SootMethod clonedTarget = (SootMethod)instanceToStaticMap.get(target);
               Value thisToAdd = ((InstanceInvokeExpr)ie).getBase();
               if (options.insert_redundant_casts()) {
                  SootClass localType = ((RefType)((InstanceInvokeExpr)ie).getBase().getType()).getSootClass();
                  SootClass parameterType = target.getDeclaringClass();
                  if (localType.isInterface() || hierarchy.isClassSuperclassOf(localType, parameterType)) {
                     Local castee = Jimple.v().newLocal("__castee", parameterType.getType());
                     b.getLocals().add(castee);
                     b.getUnits().insertBefore((Unit)Jimple.v().newAssignStmt(castee, Jimple.v().newCastExpr(((InstanceInvokeExpr)ie).getBase(), parameterType.getType())), (Unit)s);
                     thisToAdd = castee;
                  }
               }

               List newArgs = new ArrayList();
               newArgs.add(thisToAdd);
               newArgs.addAll(ie.getArgs());
               StaticInvokeExpr sie = Jimple.v().newStaticInvokeExpr(clonedTarget.makeRef(), (List)newArgs);
               ValueBox ieBox = s.getInvokeExprBox();
               ieBox.setValue(sie);
               cg.addEdge(new Edge(container, s, clonedTarget));
               if (options.insert_null_checks()) {
                  boolean caught = TrapManager.isExceptionCaughtAt(Scene.v().getSootClass("java.lang.NullPointerException"), s, b);
                  if (caught) {
                     Stmt insertee = Jimple.v().newIfStmt(Jimple.v().newNeExpr(((InstanceInvokeExpr)ie).getBase(), NullConstant.v()), (Unit)s);
                     b.getUnits().insertBefore((Unit)insertee, (Unit)s);
                     ((IfStmt)insertee).setTarget(s);
                     ThrowManager.addThrowAfter(b, insertee);
                  } else {
                     Stmt throwPoint = ThrowManager.getNullPointerExceptionThrower(b);
                     b.getUnits().insertBefore((Unit)Jimple.v().newIfStmt(Jimple.v().newEqExpr(((InstanceInvokeExpr)ie).getBase(), NullConstant.v()), (Unit)throwPoint), (Unit)s);
                  }
               }

               if (target.isSynchronized()) {
                  clonedTarget.setModifiers(clonedTarget.getModifiers() & -33);
                  SynchronizerManager.v().synchronizeStmtOn(s, b, (Local)((InstanceInvokeExpr)ie).getBase());
               }

               LocalNameStandardizer.v().transform(b, phaseName + ".lns");
            }
         }
      }

   }
}
