package heros.solver;

import com.google.common.collect.Table;
import heros.InterproceduralCFG;
import heros.ItemPrinter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class FlowFunctionDotExport<N, D, M, I extends InterproceduralCFG<N, M>> {
   private final IDESolver<N, D, M, ?, I> solver;
   private final ItemPrinter<? super N, ? super D, ? super M> printer;
   private final Set<M> methodWhitelist;

   public FlowFunctionDotExport(IDESolver<N, D, M, ?, I> solver, ItemPrinter<? super N, ? super D, ? super M> printer) {
      this(solver, printer, (Set)null);
   }

   public FlowFunctionDotExport(IDESolver<N, D, M, ?, I> solver, ItemPrinter<? super N, ? super D, ? super M> printer, Set<M> methodWhitelist) {
      this.solver = solver;
      this.printer = printer;
      this.methodWhitelist = methodWhitelist;
   }

   private static <K, U> Set<U> getOrMakeSet(Map<K, Set<U>> map, K key) {
      if (map.containsKey(key)) {
         return (Set)map.get(key);
      } else {
         HashSet<U> toRet = new HashSet();
         map.put(key, toRet);
         return toRet;
      }
   }

   private String escapeLabelString(String toEscape) {
      return toEscape.replace("\\", "\\\\").replace("\"", "\\\"").replace("<", "\\<").replace(">", "\\>");
   }

   private void numberEdges(Table<N, N, Map<D, Set<D>>> edgeSet, FlowFunctionDotExport<N, D, M, I>.UnitFactTracker utf) {
      Iterator var3 = edgeSet.cellSet().iterator();

      while(true) {
         Table.Cell c;
         Object sourceUnit;
         Object destUnit;
         Object destMethod;
         Object sourceMethod;
         do {
            if (!var3.hasNext()) {
               return;
            }

            c = (Table.Cell)var3.next();
            sourceUnit = c.getRowKey();
            destUnit = c.getColumnKey();
            destMethod = this.solver.icfg.getMethodOf(destUnit);
            sourceMethod = this.solver.icfg.getMethodOf(sourceUnit);
         } while(this.isMethodFiltered(sourceMethod) && this.isMethodFiltered(destMethod));

         if (this.isMethodFiltered(destMethod)) {
            utf.registerStubUnit(destMethod, destUnit);
         } else {
            utf.registerUnit(destMethod, destUnit);
         }

         if (this.isMethodFiltered(sourceMethod)) {
            utf.registerStubUnit(sourceMethod, sourceUnit);
         } else {
            utf.registerUnit(sourceMethod, sourceUnit);
         }

         Iterator var9 = ((Map)c.getValue()).entrySet().iterator();

         while(var9.hasNext()) {
            Entry<D, Set<D>> entry = (Entry)var9.next();
            utf.registerFactAtUnit(sourceUnit, entry.getKey());
            Iterator var11 = ((Set)entry.getValue()).iterator();

            while(var11.hasNext()) {
               D destFact = var11.next();
               utf.registerFactAtUnit(destUnit, destFact);
            }
         }
      }
   }

   private boolean isMethodFiltered(M method) {
      return this.methodWhitelist != null && !this.methodWhitelist.contains(method);
   }

   private boolean isNodeFiltered(N node) {
      return this.isMethodFiltered(this.solver.icfg.getMethodOf(node));
   }

   private void printMethodUnits(Set<N> units, M method, PrintStream pf, FlowFunctionDotExport<N, D, M, I>.UnitFactTracker utf) {
      Iterator var5 = units.iterator();

      while(var5.hasNext()) {
         N methodUnit = var5.next();
         Set<D> loc = (Set)utf.factsForUnit.get(methodUnit);
         String unitText = this.escapeLabelString(this.printer.printNode(methodUnit, method));
         pf.print(utf.getUnitLabel(methodUnit) + " [shape=record,label=\"" + unitText + " ");
         Iterator var9 = loc.iterator();

         while(var9.hasNext()) {
            D hl = var9.next();
            pf.print("| <" + utf.getFactLabel(methodUnit, hl) + "> " + this.escapeLabelString(this.printer.printFact(hl)));
         }

         pf.println("\"];");
      }

   }

   public void dumpDotFile(String fileName) {
      File f = new File(fileName);
      PrintStream pf = null;

      try {
         pf = new PrintStream(f);
         FlowFunctionDotExport<N, D, M, I>.UnitFactTracker utf = new FlowFunctionDotExport.UnitFactTracker();
         this.numberEdges(this.solver.computedIntraPEdges, utf);
         this.numberEdges(this.solver.computedInterPEdges, utf);
         pf.println("digraph ifds {node[shape=record];");
         int methodCounter = 0;
         Iterator var6 = utf.methodToUnit.entrySet().iterator();

         Entry kv;
         while(var6.hasNext()) {
            kv = (Entry)var6.next();
            Set<N> intraProc = (Set)kv.getValue();
            pf.println("subgraph cluster" + methodCounter + " {");
            ++methodCounter;
            this.printMethodUnits(intraProc, kv.getKey(), pf, utf);
            Iterator var9 = intraProc.iterator();

            while(var9.hasNext()) {
               N methodUnit = var9.next();
               Map<N, Map<D, Set<D>>> flows = this.solver.computedIntraPEdges.row(methodUnit);
               Iterator var12 = flows.entrySet().iterator();

               while(var12.hasNext()) {
                  Entry<N, Map<D, Set<D>>> kv2 = (Entry)var12.next();
                  N destUnit = kv2.getKey();
                  Iterator var15 = ((Map)kv2.getValue()).entrySet().iterator();

                  while(var15.hasNext()) {
                     Entry<D, Set<D>> pointFlow = (Entry)var15.next();
                     Iterator var17 = ((Set)pointFlow.getValue()).iterator();

                     while(var17.hasNext()) {
                        D destFact = var17.next();
                        String edge = utf.getEdgePoint(methodUnit, pointFlow.getKey()) + " -> " + utf.getEdgePoint(destUnit, destFact);
                        pf.print(edge);
                        pf.println(";");
                     }
                  }
               }
            }

            pf.println("label=\"" + this.escapeLabelString(this.printer.printMethod(kv.getKey())) + "\";");
            pf.println("}");
         }

         var6 = utf.stubMethods.entrySet().iterator();

         while(var6.hasNext()) {
            kv = (Entry)var6.next();
            pf.println("subgraph cluster" + methodCounter++ + " {");
            this.printMethodUnits((Set)kv.getValue(), kv.getKey(), pf, utf);
            pf.println("label=\"" + this.escapeLabelString("[STUB] " + this.printer.printMethod(kv.getKey())) + "\";");
            pf.println("graph[style=dotted];");
            pf.println("}");
         }

         var6 = this.solver.computedInterPEdges.cellSet().iterator();

         while(true) {
            Table.Cell c;
            do {
               if (!var6.hasNext()) {
                  pf.println("}");
                  return;
               }

               c = (Table.Cell)var6.next();
            } while(this.isNodeFiltered(c.getRowKey()) && this.isNodeFiltered(c.getColumnKey()));

            Iterator var26 = ((Map)c.getValue()).entrySet().iterator();

            while(var26.hasNext()) {
               Entry<D, Set<D>> kv = (Entry)var26.next();
               Iterator var28 = ((Set)kv.getValue()).iterator();

               while(var28.hasNext()) {
                  D dFact = var28.next();
                  pf.print(utf.getEdgePoint(c.getRowKey(), kv.getKey()));
                  pf.print(" -> ");
                  pf.print(utf.getEdgePoint(c.getColumnKey(), dFact));
                  pf.println(" [style=dotted];");
               }
            }
         }
      } catch (FileNotFoundException var23) {
         throw new RuntimeException("Writing dot output failed", var23);
      } finally {
         if (pf != null) {
            pf.close();
         }

      }
   }

   private class UnitFactTracker {
      private FlowFunctionDotExport.Numberer<Pair<N, D>> factNumbers;
      private FlowFunctionDotExport.Numberer<N> unitNumbers;
      private Map<N, Set<D>> factsForUnit;
      private Map<M, Set<N>> methodToUnit;
      private Map<M, Set<N>> stubMethods;

      private UnitFactTracker() {
         this.factNumbers = new FlowFunctionDotExport.Numberer();
         this.unitNumbers = new FlowFunctionDotExport.Numberer();
         this.factsForUnit = new HashMap();
         this.methodToUnit = new HashMap();
         this.stubMethods = new HashMap();
      }

      public void registerFactAtUnit(N unit, D fact) {
         FlowFunctionDotExport.getOrMakeSet(this.factsForUnit, unit).add(fact);
         this.factNumbers.add(new Pair(unit, fact));
      }

      public void registerUnit(M method, N unit) {
         this.unitNumbers.add(unit);
         FlowFunctionDotExport.getOrMakeSet(this.methodToUnit, method).add(unit);
      }

      public void registerStubUnit(M method, N unit) {
         assert !this.methodToUnit.containsKey(method);

         this.unitNumbers.add(unit);
         FlowFunctionDotExport.getOrMakeSet(this.stubMethods, method).add(unit);
      }

      public String getUnitLabel(N unit) {
         return "u" + this.unitNumbers.get(unit);
      }

      public String getFactLabel(N unit, D fact) {
         return "f" + this.factNumbers.get(new Pair(unit, fact));
      }

      public String getEdgePoint(N unit, D fact) {
         return this.getUnitLabel(unit) + ":" + this.getFactLabel(unit, fact);
      }

      // $FF: synthetic method
      UnitFactTracker(Object x1) {
         this();
      }
   }

   private static class Numberer<D> {
      long counter;
      Map<D, Long> map;

      private Numberer() {
         this.counter = 1L;
         this.map = new HashMap();
      }

      public void add(D o) {
         if (!this.map.containsKey(o)) {
            this.map.put(o, Long.valueOf((long)(this.counter++)));
         }
      }

      public long get(D o) {
         if (o == null) {
            throw new IllegalArgumentException("Null key");
         } else if (!this.map.containsKey(o)) {
            throw new IllegalArgumentException("Failed to find number for: " + o);
         } else {
            return (Long)this.map.get(o);
         }
      }

      // $FF: synthetic method
      Numberer(Object x0) {
         this();
      }
   }
}
