package heros.solver;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.SynchronizedBy;
import heros.ThreadSafe;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@ThreadSafe
public class JumpFunctions<N, D, L> {
   @SynchronizedBy("consistent lock on this")
   protected Table<N, D, Map<D, EdgeFunction<L>>> nonEmptyReverseLookup = HashBasedTable.create();
   @SynchronizedBy("consistent lock on this")
   protected Table<D, N, Map<D, EdgeFunction<L>>> nonEmptyForwardLookup = HashBasedTable.create();
   @SynchronizedBy("consistent lock on this")
   protected Map<N, Table<D, D, EdgeFunction<L>>> nonEmptyLookupByTargetNode = new HashMap();
   @DontSynchronize("immutable")
   private final EdgeFunction<L> allTop;

   public JumpFunctions(EdgeFunction<L> allTop) {
      this.allTop = allTop;
   }

   public synchronized void addFunction(D sourceVal, N target, D targetVal, EdgeFunction<L> function) {
      assert sourceVal != null;

      assert target != null;

      assert targetVal != null;

      assert function != null;

      if (!function.equalTo(this.allTop)) {
         Map<D, EdgeFunction<L>> sourceValToFunc = (Map)this.nonEmptyReverseLookup.get(target, targetVal);
         if (sourceValToFunc == null) {
            sourceValToFunc = new LinkedHashMap();
            this.nonEmptyReverseLookup.put(target, targetVal, sourceValToFunc);
         }

         ((Map)sourceValToFunc).put(sourceVal, function);
         Map<D, EdgeFunction<L>> targetValToFunc = (Map)this.nonEmptyForwardLookup.get(sourceVal, target);
         if (targetValToFunc == null) {
            targetValToFunc = new LinkedHashMap();
            this.nonEmptyForwardLookup.put(sourceVal, target, targetValToFunc);
         }

         ((Map)targetValToFunc).put(targetVal, function);
         Table<D, D, EdgeFunction<L>> table = (Table)this.nonEmptyLookupByTargetNode.get(target);
         if (table == null) {
            table = HashBasedTable.create();
            this.nonEmptyLookupByTargetNode.put(target, table);
         }

         ((Table)table).put(sourceVal, targetVal, function);
      }
   }

   public synchronized Map<D, EdgeFunction<L>> reverseLookup(N target, D targetVal) {
      assert target != null;

      assert targetVal != null;

      Map<D, EdgeFunction<L>> res = (Map)this.nonEmptyReverseLookup.get(target, targetVal);
      return res == null ? Collections.emptyMap() : res;
   }

   public synchronized Map<D, EdgeFunction<L>> forwardLookup(D sourceVal, N target) {
      assert sourceVal != null;

      assert target != null;

      Map<D, EdgeFunction<L>> res = (Map)this.nonEmptyForwardLookup.get(sourceVal, target);
      return res == null ? Collections.emptyMap() : res;
   }

   public synchronized Set<Table.Cell<D, D, EdgeFunction<L>>> lookupByTarget(N target) {
      assert target != null;

      Table<D, D, EdgeFunction<L>> table = (Table)this.nonEmptyLookupByTargetNode.get(target);
      if (table == null) {
         return Collections.emptySet();
      } else {
         Set<Table.Cell<D, D, EdgeFunction<L>>> res = table.cellSet();
         return res == null ? Collections.emptySet() : res;
      }
   }

   public synchronized boolean removeFunction(D sourceVal, N target, D targetVal) {
      assert sourceVal != null;

      assert target != null;

      assert targetVal != null;

      Map<D, EdgeFunction<L>> sourceValToFunc = (Map)this.nonEmptyReverseLookup.get(target, targetVal);
      if (sourceValToFunc == null) {
         return false;
      } else if (sourceValToFunc.remove(sourceVal) == null) {
         return false;
      } else {
         if (sourceValToFunc.isEmpty()) {
            this.nonEmptyReverseLookup.remove(targetVal, targetVal);
         }

         Map<D, EdgeFunction<L>> targetValToFunc = (Map)this.nonEmptyForwardLookup.get(sourceVal, target);
         if (targetValToFunc == null) {
            return false;
         } else if (targetValToFunc.remove(targetVal) == null) {
            return false;
         } else {
            if (targetValToFunc.isEmpty()) {
               this.nonEmptyForwardLookup.remove(sourceVal, target);
            }

            Table<D, D, EdgeFunction<L>> table = (Table)this.nonEmptyLookupByTargetNode.get(target);
            if (table == null) {
               return false;
            } else if (table.remove(sourceVal, targetVal) == null) {
               return false;
            } else {
               if (table.isEmpty()) {
                  this.nonEmptyLookupByTargetNode.remove(target);
               }

               return true;
            }
         }
      }
   }

   public synchronized void clear() {
      this.nonEmptyForwardLookup.clear();
      this.nonEmptyLookupByTargetNode.clear();
      this.nonEmptyReverseLookup.clear();
   }
}
