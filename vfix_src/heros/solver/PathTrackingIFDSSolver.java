package heros.solver;

import com.google.common.collect.Maps;
import heros.EdgeFunction;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import java.util.Map;

/** @deprecated */
@Deprecated
public class PathTrackingIFDSSolver<N, D extends LinkedNode<D>, M, I extends InterproceduralCFG<N, M>> extends IFDSSolver<N, D, M, I> {
   protected final Map<PathTrackingIFDSSolver<N, D, M, I>.CacheEntry, LinkedNode<D>> cache = Maps.newHashMap();

   public PathTrackingIFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
      super(ifdsProblem);
   }

   protected void propagate(D sourceVal, N target, D targetVal, EdgeFunction<IFDSSolver.BinaryDomain> f, N relatedCallSite, boolean isUnbalancedReturn) {
      PathTrackingIFDSSolver<N, D, M, I>.CacheEntry currentCacheEntry = new PathTrackingIFDSSolver.CacheEntry(target, sourceVal, targetVal);
      boolean propagate = false;
      synchronized(this) {
         if (this.cache.containsKey(currentCacheEntry)) {
            LinkedNode<D> existingTargetVal = (LinkedNode)this.cache.get(currentCacheEntry);
            if (existingTargetVal != targetVal) {
               existingTargetVal.addNeighbor(targetVal);
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
      private D sourceVal;
      private D targetVal;

      public CacheEntry(N n, D sourceVal, D targetVal) {
         this.n = n;
         this.sourceVal = sourceVal;
         this.targetVal = targetVal;
      }

      public int hashCode() {
         int prime = true;
         int resultx = 1;
         int result = 31 * resultx + (this.sourceVal == null ? 0 : this.sourceVal.hashCode());
         result = 31 * result + (this.targetVal == null ? 0 : this.targetVal.hashCode());
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
            PathTrackingIFDSSolver<N, D, M, I>.CacheEntry other = (PathTrackingIFDSSolver.CacheEntry)obj;
            if (this.sourceVal == null) {
               if (other.sourceVal != null) {
                  return false;
               }
            } else if (!this.sourceVal.equals(other.sourceVal)) {
               return false;
            }

            if (this.targetVal == null) {
               if (other.targetVal != null) {
                  return false;
               }
            } else if (!this.targetVal.equals(other.targetVal)) {
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
