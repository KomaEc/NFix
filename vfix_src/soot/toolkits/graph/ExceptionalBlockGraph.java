package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.exceptions.ThrowableSet;
import soot.util.PhaseDumper;

public class ExceptionalBlockGraph extends BlockGraph implements ExceptionalGraph<Block> {
   Map<Block, List<Block>> blockToExceptionalPreds;
   Map<Block, List<Block>> blockToExceptionalSuccs;
   Map<Block, List<Block>> blockToUnexceptionalPreds;
   Map<Block, List<Block>> blockToUnexceptionalSuccs;
   Map<Block, Collection<ExceptionalBlockGraph.ExceptionDest>> blockToExceptionDests;
   ThrowAnalysis throwAnalysis;

   public ExceptionalBlockGraph(Body body) {
      this(new ExceptionalUnitGraph(body));
   }

   public ExceptionalBlockGraph(ExceptionalUnitGraph unitGraph) {
      super(unitGraph);
      PhaseDumper.v().dumpGraph(this);
   }

   protected Map<Unit, Block> buildBlocks(Set<Unit> leaders, UnitGraph uncastUnitGraph) {
      ExceptionalUnitGraph unitGraph = (ExceptionalUnitGraph)uncastUnitGraph;
      Map<Unit, Block> unitToBlock = super.buildBlocks(leaders, unitGraph);
      if (unitGraph.getBody().getTraps().size() == 0) {
         this.throwAnalysis = unitGraph.getThrowAnalysis();
         if (this.throwAnalysis == null) {
            throw new IllegalStateException("ExceptionalUnitGraph lacked a cached ThrowAnalysis for a Body with no Traps.");
         }
      } else {
         int initialMapSize = this.mBlocks.size() * 2 / 3;
         this.blockToUnexceptionalPreds = new HashMap(initialMapSize);
         this.blockToUnexceptionalSuccs = new HashMap(initialMapSize);
         this.blockToExceptionalPreds = new HashMap(initialMapSize);
         this.blockToExceptionalSuccs = new HashMap(initialMapSize);
         Iterator var6 = this.mBlocks.iterator();

         while(var6.hasNext()) {
            Block block = (Block)var6.next();
            Unit blockHead = block.getHead();
            List<Unit> exceptionalPredUnits = unitGraph.getExceptionalPredsOf(blockHead);
            List exceptionalSuccUnits;
            List unexceptionalPreds;
            if (exceptionalPredUnits.size() != 0) {
               List<Block> exceptionalPreds = this.mappedValues(exceptionalPredUnits, unitToBlock);
               exceptionalPreds = Collections.unmodifiableList(exceptionalPreds);
               this.blockToExceptionalPreds.put(block, exceptionalPreds);
               exceptionalSuccUnits = unitGraph.getUnexceptionalPredsOf(blockHead);
               unexceptionalPreds = null;
               if (exceptionalSuccUnits.size() == 0) {
                  unexceptionalPreds = Collections.emptyList();
               } else {
                  unexceptionalPreds = this.mappedValues(exceptionalSuccUnits, unitToBlock);
                  unexceptionalPreds = Collections.unmodifiableList(unexceptionalPreds);
               }

               this.blockToUnexceptionalPreds.put(block, unexceptionalPreds);
            }

            Unit blockTail = block.getTail();
            exceptionalSuccUnits = unitGraph.getExceptionalSuccsOf(blockTail);
            if (exceptionalSuccUnits.size() != 0) {
               unexceptionalPreds = this.mappedValues(exceptionalSuccUnits, unitToBlock);
               unexceptionalPreds = Collections.unmodifiableList(unexceptionalPreds);
               this.blockToExceptionalSuccs.put(block, unexceptionalPreds);
               List<Unit> unexceptionalSuccUnits = unitGraph.getUnexceptionalSuccsOf(blockTail);
               List<Block> unexceptionalSuccs = null;
               if (unexceptionalSuccUnits.size() == 0) {
                  unexceptionalSuccs = Collections.emptyList();
               } else {
                  unexceptionalSuccs = this.mappedValues(unexceptionalSuccUnits, unitToBlock);
                  unexceptionalSuccs = Collections.unmodifiableList(unexceptionalSuccs);
               }

               this.blockToUnexceptionalSuccs.put(block, unexceptionalSuccs);
            }
         }

         this.blockToExceptionDests = this.buildExceptionDests(unitGraph, unitToBlock);
      }

      return unitToBlock;
   }

   private <K, V> List<V> mappedValues(List<K> keys, Map<K, V> keyToValue) {
      List<V> result = new ArrayList(keys.size());
      Iterator var4 = keys.iterator();

      while(var4.hasNext()) {
         K key = var4.next();
         V value = keyToValue.get(key);
         if (value == null) {
            throw new IllegalStateException("No value corresponding to key: " + key.toString());
         }

         result.add(value);
      }

      return result;
   }

   private Map<Block, Collection<ExceptionalBlockGraph.ExceptionDest>> buildExceptionDests(ExceptionalUnitGraph unitGraph, Map<Unit, Block> unitToBlock) {
      Map<Block, Collection<ExceptionalBlockGraph.ExceptionDest>> result = new HashMap(this.mBlocks.size() * 2 + 1, 0.7F);
      Iterator var4 = this.mBlocks.iterator();

      while(var4.hasNext()) {
         Block block = (Block)var4.next();
         result.put(block, this.collectDests(block, unitGraph, unitToBlock));
      }

      return result;
   }

   private Collection<ExceptionalBlockGraph.ExceptionDest> collectDests(Block block, ExceptionalUnitGraph unitGraph, Map<Unit, Block> unitToBlock) {
      Unit blockHead = block.getHead();
      Unit blockTail = block.getTail();
      ArrayList<ExceptionalBlockGraph.ExceptionDest> blocksDests = null;
      ThrowableSet escapingThrowables = ThrowableSet.Manager.v().EMPTY;
      Map<Trap, ThrowableSet> trapToThrowables = null;
      int caughtCount = 0;
      Iterator var10 = block.iterator();

      label89:
      while(var10.hasNext()) {
         Unit unit2 = (Unit)var10.next();
         Unit unit = unit2;
         Collection<ExceptionalUnitGraph.ExceptionDest> unitDests = unitGraph.getExceptionDests(unit2);
         if (unitDests.size() != 1 && unit2 != blockHead && unit2 != blockTail) {
            throw new IllegalStateException("Multiple ExceptionDests associated with a unit which does not begin or end its block.");
         }

         Iterator var14 = unitDests.iterator();

         while(true) {
            while(true) {
               if (!var14.hasNext()) {
                  continue label89;
               }

               ExceptionalUnitGraph.ExceptionDest unitDest = (ExceptionalUnitGraph.ExceptionDest)var14.next();
               if (unitDest.getTrap() == null) {
                  try {
                     escapingThrowables = escapingThrowables.add(unitDest.getThrowables());
                  } catch (ThrowableSet.AlreadyHasExclusionsException var18) {
                     if (escapingThrowables != ThrowableSet.Manager.v().EMPTY) {
                        if (blocksDests == null) {
                           blocksDests = new ArrayList(10);
                        }

                        blocksDests.add(new ExceptionalBlockGraph.ExceptionDest((Trap)null, escapingThrowables, (Block)null));
                     }

                     escapingThrowables = unitDest.getThrowables();
                  }
               } else {
                  if (unit != blockHead && unit != blockTail) {
                     throw new IllegalStateException("Unit " + unit.toString() + " is not a block head or tail, yet it throws " + unitDest.getThrowables() + " to " + unitDest.getTrap());
                  }

                  ++caughtCount;
                  if (trapToThrowables == null) {
                     trapToThrowables = new HashMap(unitDests.size() * 2);
                  }

                  Trap trap = unitDest.getTrap();
                  ThrowableSet throwables = (ThrowableSet)trapToThrowables.get(trap);
                  if (throwables == null) {
                     throwables = unitDest.getThrowables();
                  } else {
                     throwables = throwables.add(unitDest.getThrowables());
                  }

                  trapToThrowables.put(trap, throwables);
               }
            }
         }
      }

      if (blocksDests == null) {
         blocksDests = new ArrayList(caughtCount + 1);
      } else {
         blocksDests.ensureCapacity(blocksDests.size() + caughtCount);
      }

      if (escapingThrowables != ThrowableSet.Manager.v().EMPTY) {
         ExceptionalBlockGraph.ExceptionDest escapingDest = new ExceptionalBlockGraph.ExceptionDest((Trap)null, escapingThrowables, (Block)null);
         blocksDests.add(escapingDest);
      }

      if (trapToThrowables != null) {
         var10 = trapToThrowables.entrySet().iterator();

         while(var10.hasNext()) {
            Entry<Trap, ThrowableSet> entry = (Entry)var10.next();
            Trap trap = (Trap)entry.getKey();
            Block trapBlock = (Block)unitToBlock.get(trap.getHandlerUnit());
            if (trapBlock == null) {
               throw new IllegalStateException("catching unit is not recorded as a block leader.");
            }

            ThrowableSet throwables = (ThrowableSet)entry.getValue();
            ExceptionalBlockGraph.ExceptionDest blockDest = new ExceptionalBlockGraph.ExceptionDest(trap, throwables, trapBlock);
            blocksDests.add(blockDest);
         }
      }

      return blocksDests;
   }

   public List<Block> getUnexceptionalPredsOf(Block b) {
      return this.blockToUnexceptionalPreds != null && this.blockToUnexceptionalPreds.containsKey(b) ? (List)this.blockToUnexceptionalPreds.get(b) : b.getPreds();
   }

   public List<Block> getUnexceptionalSuccsOf(Block b) {
      return this.blockToUnexceptionalSuccs != null && this.blockToUnexceptionalSuccs.containsKey(b) ? (List)this.blockToUnexceptionalSuccs.get(b) : b.getSuccs();
   }

   public List<Block> getExceptionalPredsOf(Block b) {
      return this.blockToExceptionalPreds != null && this.blockToExceptionalPreds.containsKey(b) ? (List)this.blockToExceptionalPreds.get(b) : Collections.emptyList();
   }

   public List<Block> getExceptionalSuccsOf(Block b) {
      return this.blockToExceptionalSuccs != null && this.blockToExceptionalSuccs.containsKey(b) ? (List)this.blockToExceptionalSuccs.get(b) : Collections.emptyList();
   }

   public Collection<ExceptionalBlockGraph.ExceptionDest> getExceptionDests(final Block b) {
      if (this.blockToExceptionDests == null) {
         ExceptionalBlockGraph.ExceptionDest e = new ExceptionalBlockGraph.ExceptionDest((Trap)null, (ThrowableSet)null, (Block)null) {
            private ThrowableSet throwables;

            public ThrowableSet getThrowables() {
               if (null == this.throwables) {
                  this.throwables = ThrowableSet.Manager.v().EMPTY;

                  Unit unit;
                  for(Iterator var1 = b.iterator(); var1.hasNext(); this.throwables = this.throwables.add(ExceptionalBlockGraph.this.throwAnalysis.mightThrow(unit))) {
                     unit = (Unit)var1.next();
                  }
               }

               return this.throwables;
            }
         };
         return Collections.singletonList(e);
      } else {
         return (Collection)this.blockToExceptionDests.get(b);
      }
   }

   public static class ExceptionDest implements ExceptionalGraph.ExceptionDest<Block> {
      private Trap trap;
      private ThrowableSet throwables;
      private Block handler;

      protected ExceptionDest(Trap trap, ThrowableSet throwables, Block handler) {
         this.trap = trap;
         this.throwables = throwables;
         this.handler = handler;
      }

      public Trap getTrap() {
         return this.trap;
      }

      public ThrowableSet getThrowables() {
         return this.throwables;
      }

      public Block getHandlerNode() {
         return this.handler;
      }

      public String toString() {
         StringBuffer buf = new StringBuffer();
         buf.append(this.getThrowables());
         buf.append(" -> ");
         if (this.trap == null) {
            buf.append("(escapes)");
         } else {
            buf.append(this.trap.toString());
            buf.append("handler: ");
            buf.append(this.getHandlerNode().toString());
         }

         return buf.toString();
      }
   }
}
