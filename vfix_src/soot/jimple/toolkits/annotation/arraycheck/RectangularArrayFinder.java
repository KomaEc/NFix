package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.G;
import soot.Local;
import soot.Scene;
import soot.SceneTransformer;
import soot.Singletons;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JArrayRef;
import soot.jimple.internal.JNewMultiArrayExpr;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.options.Options;
import soot.util.Chain;

public class RectangularArrayFinder extends SceneTransformer {
   private static final Logger logger = LoggerFactory.getLogger(RectangularArrayFinder.class);
   private final ExtendedHashMutableDirectedGraph agraph = new ExtendedHashMutableDirectedGraph();
   private final Set falseSet = new HashSet();
   private final Set<Object> trueSet = new HashSet();
   private CallGraph cg;

   public RectangularArrayFinder(Singletons.Global g) {
   }

   public static RectangularArrayFinder v() {
      return G.v().soot_jimple_toolkits_annotation_arraycheck_RectangularArrayFinder();
   }

   protected void internalTransform(String phaseName, Map<String, String> opts) {
      Scene sc = Scene.v();
      this.cg = sc.getCallGraph();
      Date start = new Date();
      if (Options.v().verbose()) {
         logger.debug("[ra] Finding rectangular arrays, start on " + start);
      }

      Chain appClasses = sc.getApplicationClasses();
      Iterator classIt = appClasses.iterator();

      while(classIt.hasNext()) {
         SootClass c = (SootClass)classIt.next();
         Iterator methodIt = c.methodIterator();

         while(methodIt.hasNext()) {
            SootMethod method = (SootMethod)methodIt.next();
            if (method.isConcrete() && sc.getReachableMethods().contains(method)) {
               this.recoverRectArray(method);
               this.addInfoFromMethod(method);
            }
         }
      }

      ArrayList changedNodeList;
      List startNodes;
      if (this.agraph.containsNode(BoolValue.v(false))) {
         changedNodeList = new ArrayList();
         startNodes = this.agraph.getSuccsOf(BoolValue.v(false));
         this.falseSet.addAll(startNodes);
         changedNodeList.addAll(startNodes);

         while(!changedNodeList.isEmpty()) {
            Object node = changedNodeList.remove(0);
            List succs = this.agraph.getSuccsOf(node);
            Iterator succsIt = succs.iterator();

            while(succsIt.hasNext()) {
               Object succ = succsIt.next();
               if (!this.falseSet.contains(succ)) {
                  this.falseSet.add(succ);
                  changedNodeList.add(succ);
               }
            }
         }
      }

      if (this.agraph.containsNode(BoolValue.v(true))) {
         changedNodeList = new ArrayList();
         startNodes = this.agraph.getSuccsOf(BoolValue.v(true));
         Iterator nodesIt = startNodes.iterator();

         Object node;
         while(nodesIt.hasNext()) {
            node = nodesIt.next();
            if (!this.falseSet.contains(node)) {
               changedNodeList.add(node);
               this.trueSet.add(node);
            }
         }

         while(!changedNodeList.isEmpty()) {
            node = changedNodeList.remove(0);
            List succs = this.agraph.getSuccsOf(node);
            Iterator succsIt = succs.iterator();

            while(succsIt.hasNext()) {
               Object succ = succsIt.next();
               if (!this.falseSet.contains(succ) && !this.trueSet.contains(succ)) {
                  this.trueSet.add(succ);
                  changedNodeList.add(succ);
               }
            }
         }
      }

      if (Options.v().debug()) {
         logger.debug("Rectangular Array :");
         Iterator nodeIt = this.trueSet.iterator();

         Object node;
         while(nodeIt.hasNext()) {
            node = nodeIt.next();
            logger.debug("" + node);
         }

         logger.debug("\nNon-rectangular Array :");
         nodeIt = this.falseSet.iterator();

         while(nodeIt.hasNext()) {
            node = nodeIt.next();
            logger.debug("" + node);
         }
      }

      Date finish = new Date();
      if (Options.v().verbose()) {
         long runtime = finish.getTime() - start.getTime();
         long mins = runtime / 60000L;
         long secs = runtime % 60000L / 1000L;
         logger.debug("[ra] Rectangular array finder finishes. It took " + mins + " mins and " + secs + " secs.");
      }

   }

   private void addInfoFromMethod(SootMethod method) {
      if (Options.v().verbose()) {
         logger.debug("[ra] Operating " + method.getSignature());
      }

      boolean needTransfer = true;
      Body body = method.getActiveBody();
      Set<Object> tmpNode = new HashSet();
      boolean trackReturn = false;
      Type rtnType = method.getReturnType();
      if (rtnType instanceof ArrayType && ((ArrayType)rtnType).numDimensions > 1) {
         trackReturn = true;
         needTransfer = true;
      }

      Set<Local> arrayLocal = new HashSet();
      Collection<Local> locals = body.getLocals();
      Iterator localIt = locals.iterator();

      while(localIt.hasNext()) {
         Local local = (Local)localIt.next();
         Type type = local.getType();
         if (type instanceof ArrayType) {
            if (((ArrayType)type).numDimensions > 1) {
               arrayLocal.add(local);
            } else {
               tmpNode.add(new MethodLocal(method, local));
            }
         }
      }

      ExtendedHashMutableDirectedGraph ehmdg = new ExtendedHashMutableDirectedGraph();
      Iterator unitIt = body.getUnits().snapshotIterator();

      while(true) {
         while(true) {
            while(true) {
               Stmt s;
               Value to;
               Value leftOp;
               Value rightOp;
               do {
                  do {
                     if (!unitIt.hasNext()) {
                        if (needTransfer) {
                           Iterator tmpNodeIt = tmpNode.iterator();

                           while(tmpNodeIt.hasNext()) {
                              ehmdg.skipNode(tmpNodeIt.next());
                           }

                           this.agraph.mergeWith(ehmdg);
                        }

                        return;
                     }

                     s = (Stmt)unitIt.next();
                     if (s.containsInvokeExpr()) {
                        InvokeExpr iexpr = s.getInvokeExpr();
                        int argnum = iexpr.getArgCount();

                        for(int i = 0; i < argnum; ++i) {
                           to = iexpr.getArg(i);
                           if (arrayLocal.contains(to)) {
                              needTransfer = true;
                              MethodLocal ml = new MethodLocal(method, (Local)to);
                              Targets targetIt = new Targets(this.cg.edgesOutOf((Unit)s));

                              while(targetIt.hasNext()) {
                                 SootMethod target = (SootMethod)targetIt.next();
                                 MethodParameter mp = new MethodParameter(target, i);
                                 ehmdg.addMutualEdge(ml, mp);
                              }
                           }
                        }
                     }

                     if (trackReturn && s instanceof ReturnStmt) {
                        leftOp = ((ReturnStmt)s).getOp();
                        if (leftOp instanceof Local) {
                           ehmdg.addMutualEdge(new MethodLocal(method, (Local)leftOp), new MethodReturn(method));
                        }
                     }
                  } while(!(s instanceof DefinitionStmt));

                  leftOp = ((DefinitionStmt)s).getLeftOp();
                  rightOp = ((DefinitionStmt)s).getRightOp();
               } while(!(leftOp.getType() instanceof ArrayType) && !(rightOp.getType() instanceof ArrayType));

               Object from = null;
               to = null;
               MethodLocal to;
               int index;
               if (leftOp instanceof Local && rightOp instanceof Local) {
                  if (arrayLocal.contains(leftOp) && arrayLocal.contains(rightOp)) {
                     index = ((ArrayType)((Local)leftOp).getType()).numDimensions;
                     int rightDims = ((ArrayType)((Local)rightOp).getType()).numDimensions;
                     to = new MethodLocal(method, (Local)leftOp);
                     from = new MethodLocal(method, (Local)rightOp);
                     ehmdg.addMutualEdge(from, to);
                     if (index != rightDims) {
                        ehmdg.addEdge(BoolValue.v(false), from);
                     }
                  } else if (!arrayLocal.contains(leftOp)) {
                     ehmdg.addEdge(BoolValue.v(false), new MethodLocal(method, (Local)rightOp));
                  }
               } else if (leftOp instanceof Local && rightOp instanceof ParameterRef) {
                  if (arrayLocal.contains(leftOp)) {
                     to = new MethodLocal(method, (Local)leftOp);
                     index = ((ParameterRef)rightOp).getIndex();
                     Object from = new MethodParameter(method, index);
                     ehmdg.addMutualEdge(from, to);
                     needTransfer = true;
                  }
               } else {
                  Local rOp;
                  if (leftOp instanceof Local && rightOp instanceof ArrayRef) {
                     rOp = (Local)((ArrayRef)rightOp).getBase();
                     if (arrayLocal.contains(rOp)) {
                        Object to = new ArrayReferenceNode(method, rOp);
                        from = new MethodLocal(method, rOp);
                        ehmdg.addMutualEdge(from, to);
                        tmpNode.add(to);
                        to = new MethodLocal(method, (Local)leftOp);
                        ehmdg.addMutualEdge(to, to);
                     }
                  } else if (leftOp instanceof ArrayRef && rightOp instanceof Local) {
                     rOp = (Local)((ArrayRef)leftOp).getBase();
                     if (arrayLocal.contains(rOp)) {
                        Object suspect = new MethodLocal(method, (Local)rightOp);
                        Object arrRef = new ArrayReferenceNode(method, rOp);
                        boolean doNothing = false;
                        if (ehmdg.containsNode(suspect)) {
                           List succs = ehmdg.getSuccsOf(suspect);
                           List preds = ehmdg.getSuccsOf(suspect);
                           Set neighbor = new HashSet();
                           neighbor.addAll(succs);
                           neighbor.addAll(preds);
                           if (neighbor.size() == 1) {
                              Object neighborOne = neighbor.toArray()[0];
                              if (arrRef.equals(neighborOne)) {
                                 doNothing = true;
                              }
                           }
                        }

                        if (!doNothing) {
                           ehmdg.addEdge(BoolValue.v(false), new MethodLocal(method, rOp));
                        }
                     }
                  } else if (leftOp instanceof Local && rightOp instanceof InvokeExpr) {
                     if (arrayLocal.contains(leftOp)) {
                        to = new MethodLocal(method, (Local)leftOp);
                        Targets targetIt = new Targets(this.cg.edgesOutOf((Unit)s));

                        while(targetIt.hasNext()) {
                           SootMethod target = (SootMethod)targetIt.next();
                           ehmdg.addMutualEdge(new MethodReturn(target), to);
                        }
                     }
                  } else {
                     Type ftype;
                     Type ltype;
                     if (leftOp instanceof FieldRef && rightOp instanceof Local) {
                        if (arrayLocal.contains(rightOp)) {
                           ftype = ((FieldRef)leftOp).getType();
                           ltype = ((Local)rightOp).getType();
                           Object to = ((FieldRef)leftOp).getField();
                           from = new MethodLocal(method, (Local)rightOp);
                           ehmdg.addMutualEdge(from, to);
                           if (!ftype.equals(ltype)) {
                              ehmdg.addEdge(BoolValue.v(false), to);
                           }

                           needTransfer = true;
                        }
                     } else if (leftOp instanceof Local && rightOp instanceof FieldRef) {
                        if (arrayLocal.contains(leftOp)) {
                           ftype = ((FieldRef)rightOp).getType();
                           ltype = ((Local)leftOp).getType();
                           to = new MethodLocal(method, (Local)leftOp);
                           Object from = ((FieldRef)rightOp).getField();
                           ehmdg.addMutualEdge(from, to);
                           if (!ftype.equals(ltype)) {
                              ehmdg.addEdge(BoolValue.v(false), to);
                           }

                           needTransfer = true;
                        }
                     } else if (leftOp instanceof Local && (rightOp instanceof NewArrayExpr || rightOp instanceof NewMultiArrayExpr)) {
                        if (arrayLocal.contains(leftOp)) {
                           ehmdg.addEdge(BoolValue.v(true), new MethodLocal(method, (Local)leftOp));
                        }
                     } else if (leftOp instanceof Local && rightOp instanceof CastExpr) {
                        rOp = (Local)((CastExpr)rightOp).getOp();
                        to = new MethodLocal(method, (Local)leftOp);
                        from = new MethodLocal(method, rOp);
                        if (arrayLocal.contains(leftOp) && arrayLocal.contains(rOp)) {
                           ArrayType lat = (ArrayType)leftOp.getType();
                           ArrayType rat = (ArrayType)rOp.getType();
                           if (lat.numDimensions == rat.numDimensions) {
                              ehmdg.addMutualEdge(from, to);
                           } else {
                              ehmdg.addEdge(BoolValue.v(false), from);
                              ehmdg.addEdge(BoolValue.v(false), to);
                           }
                        } else if (arrayLocal.contains(leftOp)) {
                           ehmdg.addEdge(BoolValue.v(false), to);
                        } else if (arrayLocal.contains(rOp)) {
                           ehmdg.addEdge(BoolValue.v(false), from);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void recoverRectArray(SootMethod method) {
      Body body = method.getActiveBody();
      HashSet<Local> malocal = new HashSet();
      Collection<Local> locals = body.getLocals();
      Iterator localsIt = locals.iterator();

      while(localsIt.hasNext()) {
         Local local = (Local)localsIt.next();
         Type type = local.getType();
         if (type instanceof ArrayType && ((ArrayType)type).numDimensions == 2) {
            malocal.add(local);
         }
      }

      if (malocal.size() != 0) {
         Chain<Unit> units = body.getUnits();

         for(Stmt stmt = (Stmt)units.getFirst(); stmt != null && stmt.fallsThrough(); stmt = (Stmt)units.getSuccOf(stmt)) {
            if (stmt instanceof AssignStmt) {
               Value leftOp = ((AssignStmt)stmt).getLeftOp();
               Value rightOp = ((AssignStmt)stmt).getRightOp();
               if (malocal.contains(leftOp) && rightOp instanceof NewArrayExpr) {
                  Local local = (Local)leftOp;
                  NewArrayExpr naexpr = (NewArrayExpr)rightOp;
                  Value size = naexpr.getSize();
                  if (size instanceof IntConstant) {
                     int firstdim = ((IntConstant)size).value;
                     if (firstdim <= 100) {
                        ArrayType localtype = (ArrayType)local.getType();
                        Type basetype = localtype.baseType;
                        Local[] tmplocals = new Local[firstdim];
                        int seconddim = this.lookforPattern(units, stmt, firstdim, local, basetype, tmplocals);
                        if (seconddim >= 0) {
                           this.transferPattern(units, stmt, firstdim, seconddim, local, basetype, tmplocals);
                        }
                     }
                  }
               }
            }
         }

      }
   }

   private int lookforPattern(Chain units, Stmt startpoint, int firstdim, Local local, Type basetype, Local[] tmplocals) {
      int seconddim = -1;
      int curdim = 0;
      Object curtmp = local;
      Stmt curstmt = startpoint;
      int fault = 99;
      byte state = 1;

      while(true) {
         curstmt = (Stmt)units.getSuccOf(curstmt);
         if (curstmt == null) {
            return -1;
         }

         if (!(curstmt instanceof AssignStmt)) {
            return -1;
         }

         Value leftOp = ((AssignStmt)curstmt).getLeftOp();
         Value rightOp = ((AssignStmt)curstmt).getRightOp();
         switch(state) {
         case 0:
            break;
         case 1:
            state = fault;
            if (!(rightOp instanceof NewArrayExpr)) {
               break;
            }

            NewArrayExpr naexpr = (NewArrayExpr)rightOp;
            Type type = naexpr.getBaseType();
            Value size = naexpr.getSize();
            if (!type.equals(basetype) || !(size instanceof IntConstant)) {
               break;
            }

            if (curdim == 0) {
               seconddim = ((IntConstant)size).value;
            } else if (((IntConstant)size).value != seconddim) {
               break;
            }

            curtmp = leftOp;
            state = 2;
            break;
         case 2:
            state = fault;
            if (leftOp instanceof ArrayRef) {
               Value base = ((ArrayRef)leftOp).getBase();
               Value idx = ((ArrayRef)leftOp).getIndex();
               if (base.equals(curtmp)) {
                  state = 2;
               } else if (base.equals(local) && idx instanceof IntConstant && curdim == ((IntConstant)idx).value && rightOp.equals(curtmp)) {
                  tmplocals[curdim] = (Local)curtmp;
                  ++curdim;
                  if (curdim >= firstdim) {
                     state = 3;
                  } else {
                     state = 1;
                  }
               }
            }
            break;
         case 3:
            return seconddim;
         default:
            return -1;
         }
      }
   }

   private void transferPattern(Chain units, Stmt startpoint, int firstdim, int seconddim, Local local, Type basetype, Local[] tmplocals) {
      ArrayType atype = (ArrayType)local.getType();
      List sizes = new ArrayList(2);
      sizes.add(IntConstant.v(firstdim));
      sizes.add(IntConstant.v(seconddim));
      Value nmexpr = new JNewMultiArrayExpr(atype, sizes);
      ((AssignStmt)startpoint).setRightOp(nmexpr);
      int curdim = 0;
      Local tmpcur = local;
      Stmt curstmt = (Stmt)units.getSuccOf(startpoint);

      while(true) {
         while(curdim < firstdim) {
            Value leftOp = ((AssignStmt)curstmt).getLeftOp();
            Value rightOp = ((AssignStmt)curstmt).getRightOp();
            if (tmplocals[curdim].equals(leftOp) && rightOp instanceof NewArrayExpr) {
               ArrayRef arexpr = new JArrayRef(local, IntConstant.v(curdim));
               ((AssignStmt)curstmt).setRightOp(arexpr);
               tmpcur = (Local)leftOp;
            } else if (leftOp instanceof ArrayRef && rightOp.equals(tmpcur)) {
               Stmt tmpstmt = curstmt;
               curstmt = (Stmt)units.getSuccOf(curstmt);
               units.remove(tmpstmt);
               ++curdim;
            } else {
               curstmt = (Stmt)units.getSuccOf(curstmt);
            }
         }

         return;
      }
   }

   public boolean isRectangular(Object obj) {
      return this.trueSet.contains(obj);
   }
}
