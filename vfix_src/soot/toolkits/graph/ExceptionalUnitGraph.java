package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import soot.Body;
import soot.RefType;
import soot.Scene;
import soot.Timers;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.baf.Inst;
import soot.baf.NewInst;
import soot.baf.ReturnInst;
import soot.baf.ReturnVoidInst;
import soot.baf.StaticGetInst;
import soot.baf.StaticPutInst;
import soot.baf.ThrowInst;
import soot.jimple.InvokeExpr;
import soot.jimple.NewExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.options.Options;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.exceptions.ThrowableSet;
import soot.util.ArraySet;
import soot.util.Chain;
import soot.util.PhaseDumper;

public class ExceptionalUnitGraph extends UnitGraph implements ExceptionalGraph<Unit> {
   protected Map<Unit, List<Unit>> unitToUnexceptionalSuccs;
   protected Map<Unit, List<Unit>> unitToUnexceptionalPreds;
   protected Map<Unit, List<Unit>> unitToExceptionalSuccs;
   protected Map<Unit, List<Unit>> unitToExceptionalPreds;
   protected Map<Unit, Collection<ExceptionalUnitGraph.ExceptionDest>> unitToExceptionDests;
   protected ThrowAnalysis throwAnalysis;

   public ExceptionalUnitGraph(Body body, ThrowAnalysis throwAnalysis, boolean omitExceptingUnitEdges) {
      super(body);
      this.initialize(throwAnalysis, omitExceptingUnitEdges);
   }

   public ExceptionalUnitGraph(Body body, ThrowAnalysis throwAnalysis) {
      this(body, throwAnalysis, Options.v().omit_excepting_unit_edges());
   }

   public ExceptionalUnitGraph(Body body) {
      this(body, Scene.v().getDefaultThrowAnalysis(), Options.v().omit_excepting_unit_edges());
   }

   protected ExceptionalUnitGraph(Body body, boolean ignoredBogusParameter) {
      super(body);
   }

   protected void initialize(ThrowAnalysis throwAnalysis, boolean omitExceptingUnitEdges) {
      int size = this.unitChain.size();
      Set<Unit> trapUnitsThatAreHeads = Collections.emptySet();
      if (Options.v().time()) {
         Timers.v().graphTimer.start();
      }

      this.unitToUnexceptionalSuccs = new LinkedHashMap(size * 2 + 1, 0.7F);
      this.unitToUnexceptionalPreds = new LinkedHashMap(size * 2 + 1, 0.7F);
      this.buildUnexceptionalEdges(this.unitToUnexceptionalSuccs, this.unitToUnexceptionalPreds);
      this.throwAnalysis = throwAnalysis;
      if (this.body.getTraps().size() == 0) {
         this.unitToExceptionDests = Collections.emptyMap();
         this.unitToExceptionalSuccs = Collections.emptyMap();
         this.unitToExceptionalPreds = Collections.emptyMap();
         this.unitToSuccs = this.unitToUnexceptionalSuccs;
         this.unitToPreds = this.unitToUnexceptionalPreds;
      } else {
         this.unitToExceptionDests = this.buildExceptionDests(throwAnalysis);
         this.unitToExceptionalSuccs = new LinkedHashMap(this.unitToExceptionDests.size() * 2 + 1, 0.7F);
         this.unitToExceptionalPreds = new LinkedHashMap(this.body.getTraps().size() * 2 + 1, 0.7F);
         trapUnitsThatAreHeads = this.buildExceptionalEdges(throwAnalysis, this.unitToExceptionDests, this.unitToExceptionalSuccs, this.unitToExceptionalPreds, omitExceptingUnitEdges);
         this.unitToSuccs = this.combineMapValues(this.unitToUnexceptionalSuccs, this.unitToExceptionalSuccs);
         this.unitToPreds = this.combineMapValues(this.unitToUnexceptionalPreds, this.unitToExceptionalPreds);
      }

      this.buildHeadsAndTails(trapUnitsThatAreHeads);
      if (Options.v().time()) {
         Timers.v().graphTimer.end();
      }

      PhaseDumper.v().dumpGraph(this);
   }

   protected Map<Unit, Collection<ExceptionalUnitGraph.ExceptionDest>> buildExceptionDests(ThrowAnalysis throwAnalysis) {
      Chain<Unit> units = this.body.getUnits();
      Map<Unit, ThrowableSet> unitToUncaughtThrowables = new LinkedHashMap(units.size());
      Map<Unit, Collection<ExceptionalUnitGraph.ExceptionDest>> result = null;
      Iterator var5 = this.body.getTraps().iterator();

      while(var5.hasNext()) {
         Trap trap = (Trap)var5.next();
         RefType catcher = trap.getException().getType();
         Iterator unitIt = units.iterator(trap.getBeginUnit(), units.getPredOf(trap.getEndUnit()));

         while(unitIt.hasNext()) {
            Unit unit = (Unit)unitIt.next();
            ThrowableSet thrownSet = (ThrowableSet)unitToUncaughtThrowables.get(unit);
            if (thrownSet == null) {
               thrownSet = throwAnalysis.mightThrow(unit);
            }

            ThrowableSet.Pair catchableAs = thrownSet.whichCatchableAs(catcher);
            if (!catchableAs.getCaught().equals(ThrowableSet.Manager.v().EMPTY)) {
               result = this.addDestToMap(result, unit, trap, catchableAs.getCaught());
               unitToUncaughtThrowables.put(unit, catchableAs.getUncaught());
            } else {
               assert thrownSet.equals(catchableAs.getUncaught()) : "ExceptionalUnitGraph.buildExceptionDests(): catchableAs.caught == EMPTY, but catchableAs.uncaught != thrownSet" + System.getProperty("line.separator") + this.body.getMethod().getSubSignature() + " Unit: " + unit.toString() + System.getProperty("line.separator") + " catchableAs.getUncaught() == " + catchableAs.getUncaught().toString() + System.getProperty("line.separator") + " thrownSet == " + thrownSet.toString();
            }
         }
      }

      var5 = unitToUncaughtThrowables.entrySet().iterator();

      while(var5.hasNext()) {
         Entry<Unit, ThrowableSet> entry = (Entry)var5.next();
         Unit unit = (Unit)entry.getKey();
         ThrowableSet escaping = (ThrowableSet)entry.getValue();
         if (escaping != ThrowableSet.Manager.v().EMPTY) {
            result = this.addDestToMap(result, unit, (Trap)null, escaping);
         }
      }

      if (result == null) {
         result = Collections.emptyMap();
      }

      return result;
   }

   private Map<Unit, Collection<ExceptionalUnitGraph.ExceptionDest>> addDestToMap(Map<Unit, Collection<ExceptionalUnitGraph.ExceptionDest>> map, Unit u, Trap t, ThrowableSet caught) {
      Collection<ExceptionalUnitGraph.ExceptionDest> dests = map == null ? null : (Collection)((Map)map).get(u);
      if (dests == null) {
         if (t == null) {
            return (Map)map;
         }

         if (map == null) {
            map = new LinkedHashMap(this.unitChain.size() * 2 + 1);
         }

         dests = new ArrayList(3);
         ((Map)map).put(u, dests);
      }

      ((Collection)dests).add(new ExceptionalUnitGraph.ExceptionDest(t, caught));
      return (Map)map;
   }

   protected Set<Unit> buildExceptionalEdges(ThrowAnalysis throwAnalysis, Map<Unit, Collection<ExceptionalUnitGraph.ExceptionDest>> unitToExceptionDests, Map<Unit, List<Unit>> unitToSuccs, Map<Unit, List<Unit>> unitToPreds, boolean omitExceptingUnitEdges) {
      Set<Unit> trapsThatAreHeads = new ArraySet();
      Unit entryPoint = (Unit)this.unitChain.getFirst();
      Iterator var8 = unitToExceptionDests.entrySet().iterator();

      Unit pred;
      Collection throwerDests;
      label131:
      while(var8.hasNext()) {
         Entry<Unit, Collection<ExceptionalUnitGraph.ExceptionDest>> entry = (Entry)var8.next();
         pred = (Unit)entry.getKey();
         List<Unit> throwersPreds = this.getUnexceptionalPredsOf(pred);
         throwerDests = (Collection)entry.getValue();
         boolean alwaysAddSelfEdges = !omitExceptingUnitEdges || mightHaveSideEffects(pred);
         ThrowableSet predThrowables = null;
         ThrowableSet selfThrowables = null;
         if (pred instanceof ThrowInst) {
            ThrowInst throwInst = (ThrowInst)pred;
            predThrowables = throwAnalysis.mightThrowImplicitly(throwInst);
            selfThrowables = throwAnalysis.mightThrowExplicitly(throwInst);
         } else if (pred instanceof ThrowStmt) {
            ThrowStmt throwStmt = (ThrowStmt)pred;
            predThrowables = throwAnalysis.mightThrowImplicitly(throwStmt);
            selfThrowables = throwAnalysis.mightThrowExplicitly(throwStmt);
         }

         Iterator var33 = throwerDests.iterator();

         while(true) {
            Unit catcher;
            RefType trapsType;
            do {
               ExceptionalUnitGraph.ExceptionDest dest;
               do {
                  if (!var33.hasNext()) {
                     continue label131;
                  }

                  dest = (ExceptionalUnitGraph.ExceptionDest)var33.next();
               } while(dest.getTrap() == null);

               catcher = dest.getTrap().getHandlerUnit();
               trapsType = dest.getTrap().getException().getType();
               if (predThrowables == null || predThrowables.catchableAs(trapsType)) {
                  if (pred == entryPoint) {
                     trapsThatAreHeads.add(catcher);
                  }

                  Iterator var20 = throwersPreds.iterator();

                  while(var20.hasNext()) {
                     Unit pred = (Unit)var20.next();
                     this.addEdge(unitToSuccs, unitToPreds, pred, catcher);
                  }
               }
            } while(!alwaysAddSelfEdges && (selfThrowables == null || !selfThrowables.catchableAs(trapsType)));

            this.addEdge(unitToSuccs, unitToPreds, pred, catcher);
         }
      }

      LinkedList<CFGEdge> workList = new LinkedList();
      Iterator var23 = this.body.getTraps().iterator();

      while(true) {
         Unit handlerStart;
         Iterator var28;

         class CFGEdge {
            Unit head;
            Unit tail;

            CFGEdge(Unit head, Unit tail) {
               if (tail == null) {
                  throw new RuntimeException("invalid CFGEdge(" + (head == null ? "null" : head.toString()) + ',' + "null" + ')');
               } else {
                  this.head = head;
                  this.tail = tail;
               }
            }

            public boolean equals(Object rhs) {
               if (rhs == this) {
                  return true;
               } else if (!(rhs instanceof CFGEdge)) {
                  return false;
               } else {
                  CFGEdge rhsEdge = (CFGEdge)rhs;
                  return this.head == rhsEdge.head && this.tail == rhsEdge.tail;
               }
            }

            public int hashCode() {
               int result = 17;
               int resultx = 37 * result + this.head.hashCode();
               resultx = 37 * resultx + this.tail.hashCode();
               return resultx;
            }
         }

         do {
            if (!var23.hasNext()) {
               while(workList.size() > 0) {
                  CFGEdge edgeToThrower = (CFGEdge)workList.removeFirst();
                  pred = edgeToThrower.head;
                  handlerStart = edgeToThrower.tail;
                  throwerDests = this.getExceptionDests(handlerStart);
                  var28 = throwerDests.iterator();

                  while(var28.hasNext()) {
                     ExceptionalUnitGraph.ExceptionDest dest = (ExceptionalUnitGraph.ExceptionDest)var28.next();
                     if (dest.getTrap() != null) {
                        Unit handlerStart = dest.getTrap().getHandlerUnit();
                        boolean edgeAdded = false;
                        if (pred == null) {
                           if (!trapsThatAreHeads.contains(handlerStart)) {
                              trapsThatAreHeads.add(handlerStart);
                              edgeAdded = true;
                           }
                        } else if (!this.getExceptionalSuccsOf(pred).contains(handlerStart)) {
                           this.addEdge(unitToSuccs, unitToPreds, pred, handlerStart);
                           edgeAdded = true;
                        }

                        if (edgeAdded && this.mightThrowToIntraproceduralCatcher(handlerStart)) {
                           workList.addLast(new CFGEdge(pred, handlerStart));
                        }
                     }
                  }
               }

               return trapsThatAreHeads;
            }

            Trap trap = (Trap)var23.next();
            handlerStart = trap.getHandlerUnit();
         } while(!this.mightThrowToIntraproceduralCatcher(handlerStart));

         List<Unit> handlerPreds = this.getUnexceptionalPredsOf(handlerStart);
         var28 = handlerPreds.iterator();

         Unit pred;
         while(var28.hasNext()) {
            pred = (Unit)var28.next();
            workList.addLast(new CFGEdge(pred, handlerStart));
         }

         handlerPreds = this.getExceptionalPredsOf(handlerStart);
         var28 = handlerPreds.iterator();

         while(var28.hasNext()) {
            pred = (Unit)var28.next();
            workList.addLast(new CFGEdge(pred, handlerStart));
         }

         if (trapsThatAreHeads.contains(handlerStart)) {
            workList.addLast(new CFGEdge((Unit)null, handlerStart));
         }
      }
   }

   static boolean mightHaveSideEffects(Unit u) {
      if (u instanceof Inst) {
         Inst i = (Inst)u;
         return i.containsInvokeExpr() || i instanceof StaticPutInst || i instanceof StaticGetInst || i instanceof NewInst;
      } else if (u instanceof Stmt) {
         Iterator var1 = u.getUseBoxes().iterator();

         Value v;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            ValueBox vb = (ValueBox)var1.next();
            v = vb.getValue();
         } while(!(v instanceof StaticFieldRef) && !(v instanceof InvokeExpr) && !(v instanceof NewExpr));

         return true;
      } else {
         return false;
      }
   }

   private boolean mightThrowToIntraproceduralCatcher(Unit u) {
      Collection<ExceptionalUnitGraph.ExceptionDest> dests = this.getExceptionDests(u);
      Iterator var3 = dests.iterator();

      ExceptionalUnitGraph.ExceptionDest dest;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         dest = (ExceptionalUnitGraph.ExceptionDest)var3.next();
      } while(dest.getTrap() == null);

      return true;
   }

   protected void buildHeadsAndTails() throws IllegalStateException {
      throw new IllegalStateException("ExceptionalUnitGraph uses buildHeadsAndTails(List) instead of buildHeadsAndTails()");
   }

   private void buildHeadsAndTails(Set<Unit> additionalHeads) {
      this.heads = new ArrayList(additionalHeads.size() + 1);
      this.heads.addAll(additionalHeads);
      if (this.unitChain.isEmpty()) {
         throw new IllegalStateException("No body for method " + this.body.getMethod().getSignature());
      } else {
         Unit entryPoint = (Unit)this.unitChain.getFirst();
         if (!this.heads.contains(entryPoint)) {
            this.heads.add(entryPoint);
         }

         this.tails = new ArrayList();
         Iterator var3 = this.unitChain.iterator();

         while(true) {
            Unit u;
            label46:
            do {
               while(var3.hasNext()) {
                  u = (Unit)var3.next();
                  if (!(u instanceof ReturnStmt) && !(u instanceof ReturnVoidStmt) && !(u instanceof ReturnInst) && !(u instanceof ReturnVoidInst)) {
                     continue label46;
                  }

                  this.tails.add(u);
               }

               return;
            } while(!(u instanceof ThrowStmt) && !(u instanceof ThrowInst));

            Collection<ExceptionalUnitGraph.ExceptionDest> dests = this.getExceptionDests(u);
            int escapeMethodCount = 0;
            Iterator var7 = dests.iterator();

            while(var7.hasNext()) {
               ExceptionalUnitGraph.ExceptionDest dest = (ExceptionalUnitGraph.ExceptionDest)var7.next();
               if (dest.getTrap() == null) {
                  ++escapeMethodCount;
               }
            }

            if (escapeMethodCount > 0) {
               this.tails.add(u);
            }
         }
      }
   }

   public Collection<ExceptionalUnitGraph.ExceptionDest> getExceptionDests(final Unit u) {
      Collection<ExceptionalUnitGraph.ExceptionDest> result = (Collection)this.unitToExceptionDests.get(u);
      if (result == null) {
         ExceptionalUnitGraph.ExceptionDest e = new ExceptionalUnitGraph.ExceptionDest((Trap)null, (ThrowableSet)null) {
            private ThrowableSet throwables;

            public ThrowableSet getThrowables() {
               if (null == this.throwables) {
                  this.throwables = ExceptionalUnitGraph.this.throwAnalysis.mightThrow(u);
               }

               return this.throwables;
            }
         };
         return Collections.singletonList(e);
      } else {
         return result;
      }
   }

   public List<Unit> getUnexceptionalPredsOf(Unit u) {
      List<Unit> preds = (List)this.unitToUnexceptionalPreds.get(u);
      return preds == null ? Collections.emptyList() : preds;
   }

   public List<Unit> getUnexceptionalSuccsOf(Unit u) {
      List<Unit> succs = (List)this.unitToUnexceptionalSuccs.get(u);
      return succs == null ? Collections.emptyList() : succs;
   }

   public List<Unit> getExceptionalPredsOf(Unit u) {
      List<Unit> preds = (List)this.unitToExceptionalPreds.get(u);
      return preds == null ? Collections.emptyList() : preds;
   }

   public List<Unit> getExceptionalSuccsOf(Unit u) {
      List<Unit> succs = (List)this.unitToExceptionalSuccs.get(u);
      return succs == null ? Collections.emptyList() : succs;
   }

   ThrowAnalysis getThrowAnalysis() {
      return this.throwAnalysis;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      Iterator var2 = this.unitChain.iterator();

      while(var2.hasNext()) {
         Unit u = (Unit)var2.next();
         buf.append("  preds: " + this.getPredsOf(u) + "\n");
         buf.append("  unexceptional preds: " + this.getUnexceptionalPredsOf(u) + "\n");
         buf.append("  exceptional preds: " + this.getExceptionalPredsOf(u) + "\n");
         buf.append(u.toString() + '\n');
         buf.append("  exception destinations: " + this.getExceptionDests(u) + "\n");
         buf.append("  unexceptional succs: " + this.getUnexceptionalSuccsOf(u) + "\n");
         buf.append("  exceptional succs: " + this.getExceptionalSuccsOf(u) + "\n");
         buf.append("  succs " + this.getSuccsOf(u) + "\n\n");
      }

      return buf.toString();
   }

   public static class ExceptionDest implements ExceptionalGraph.ExceptionDest<Unit> {
      private Trap trap;
      private ThrowableSet throwables;

      protected ExceptionDest(Trap trap, ThrowableSet throwables) {
         this.trap = trap;
         this.throwables = throwables;
      }

      public Trap getTrap() {
         return this.trap;
      }

      public ThrowableSet getThrowables() {
         return this.throwables;
      }

      public Unit getHandlerNode() {
         return this.trap == null ? null : this.trap.getHandlerUnit();
      }

      public String toString() {
         StringBuffer buf = new StringBuffer();
         buf.append(this.getThrowables());
         buf.append(" -> ");
         if (this.trap == null) {
            buf.append("(escapes)");
         } else {
            buf.append(this.trap.toString());
         }

         return buf.toString();
      }
   }
}
