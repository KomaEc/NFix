package heros.solver;

import com.google.common.collect.Maps;
import heros.EdgeFunction;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import java.util.Map;

public class JoinHandlingNodesIFDSSolver<N, D extends JoinHandlingNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {
   protected final Map<JoinHandlingNodesIFDSSolver<N, D, M, I>.CacheEntry, JoinHandlingNode<D>> cache = Maps.newHashMap();

   public JoinHandlingNodesIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
      super(ifdsProblem);
   }

   protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {
      JoinHandlingNodesIFDSSolver<N, D, M, I>.CacheEntry currentCacheEntry = new JoinHandlingNodesIFDSSolver.CacheEntry(target, sourceVal.createJoinKey(), targetVal.createJoinKey());
      boolean propagate = false;
      synchronized(this) {
         if (this.cache.containsKey(currentCacheEntry)) {
            JoinHandlingNode<D> existingTargetVal = (JoinHandlingNode)this.cache.get(currentCacheEntry);
            if (!existingTargetVal.handleJoin(targetVal)) {
               propagate = true;
            }
         } else {
            this.cache.put(currentCacheEntry, targetVal);
            propagate = true;
         }
      }

      if (propagate) {
         super.propagate(sourceVal, target, targetVal, f, relatedCallSite, isUnbalancedReturn);
      }

   }

   private class CacheEntry {
      private N n;
      private JoinHandlingNode.JoinKey sourceKey;
      private JoinHandlingNode.JoinKey targetKey;

      public CacheEntry(N n, JoinHandlingNode.JoinKey sourceKey, JoinHandlingNode.JoinKey targetKey) {
         this.n = n;
         this.sourceKey = sourceKey;
         this.targetKey = targetKey;
      }

      public int hashCode() {
         int prime = true;
         int resultx = 1;
         int result = 31 * resultx + (this.sourceKey == null ? 0 : this.sourceKey.hashCode());
         result = 31 * result + (this.targetKey == null ? 0 : this.targetKey.hashCode());
         result = 31 * result + (this.n == null ? 0 : this.n.hashCode());
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj == null) {
            return false;
         } else if (this.getClass() != obj.getClass()) {
            return false;
         } else {
            JoinHandlingNodesIFDSSolver<N, D, M, I>.CacheEntry other = (JoinHandlingNodesIFDSSolver.CacheEntry)obj;
            if (this.sourceKey == null) {
               if (other.sourceKey != null) {
                  return false;
               }
            } else if (!this.sourceKey.equals(other.sourceKey)) {
               return false;
            }

            if (this.targetKey == null) {
               if (other.targetKey != null) {
                  return false;
               }
            } else if (!this.targetKey.equals(other.targetKey)) {
               return false;
            }

            if (this.n == null) {
               if (other.n != null) {
                  return false;
               }
            } else if (!this.n.equals(other.n)) {
               return false;
            }

            return true;
         }
      }
   }
}
