package heros;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowFunctionCache<N, D, M> implements FlowFunctions<N, D, M> {
   protected final FlowFunctions<N, D, M> delegate;
   protected final LoadingCache<FlowFunctionCache<N, D, M>.NNKey, FlowFunction<D>> normalCache;
   protected final LoadingCache<FlowFunctionCache<N, D, M>.CallKey, FlowFunction<D>> callCache;
   protected final LoadingCache<FlowFunctionCache<N, D, M>.ReturnKey, FlowFunction<D>> returnCache;
   protected final LoadingCache<FlowFunctionCache<N, D, M>.NNKey, FlowFunction<D>> callToReturnCache;
   private final Logger logger = LoggerFactory.getLogger(this.getClass());

   public FlowFunctionCache(final FlowFunctions<N, D, M> delegate, CacheBuilder builder) {
      this.delegate = delegate;
      this.normalCache = builder.build(new CacheLoader<FlowFunctionCache<N, D, M>.NNKey, FlowFunction<D>>() {
         public FlowFunction<D> load(FlowFunctionCache<N, D, M>.NNKey key) throws Exception {
            return delegate.getNormalFlowFunction(key.getCurr(), key.getSucc());
         }
      });
      this.callCache = builder.build(new CacheLoader<FlowFunctionCache<N, D, M>.CallKey, FlowFunction<D>>() {
         public FlowFunction<D> load(FlowFunctionCache<N, D, M>.CallKey key) throws Exception {
            return delegate.getCallFlowFunction(key.getCallStmt(), key.getDestinationMethod());
         }
      });
      this.returnCache = builder.build(new CacheLoader<FlowFunctionCache<N, D, M>.ReturnKey, FlowFunction<D>>() {
         public FlowFunction<D> load(FlowFunctionCache<N, D, M>.ReturnKey key) throws Exception {
            return delegate.getReturnFlowFunction(key.getCallStmt(), key.getDestinationMethod(), key.getExitStmt(), key.getReturnSite());
         }
      });
      this.callToReturnCache = builder.build(new CacheLoader<FlowFunctionCache<N, D, M>.NNKey, FlowFunction<D>>() {
         public FlowFunction<D> load(FlowFunctionCache<N, D, M>.NNKey key) throws Exception {
            return delegate.getCallToReturnFlowFunction(key.getCurr(), key.getSucc());
         }
      });
   }

   public FlowFunction<D> getNormalFlowFunction(N curr, N succ) {
      return (FlowFunction)this.normalCache.getUnchecked(new FlowFunctionCache.NNKey(curr, succ));
   }

   public FlowFunction<D> getCallFlowFunction(N callStmt, M destinationMethod) {
      return (FlowFunction)this.callCache.getUnchecked(new FlowFunctionCache.CallKey(callStmt, destinationMethod));
   }

   public FlowFunction<D> getReturnFlowFunction(N callSite, M calleeMethod, N exitStmt, N returnSite) {
      return (FlowFunction)this.returnCache.getUnchecked(new FlowFunctionCache.ReturnKey(callSite, calleeMethod, exitStmt, returnSite));
   }

   public FlowFunction<D> getCallToReturnFlowFunction(N callSite, N returnSite) {
      return (FlowFunction)this.callToReturnCache.getUnchecked(new FlowFunctionCache.NNKey(callSite, returnSite));
   }

   public void printStats() {
      this.logger.debug("Stats for flow-function cache:\nNormal:         {}\nCall:           {}\nReturn:         {}\nCall-to-return: {}\n", this.normalCache.stats(), this.callCache.stats(), this.returnCache.stats(), this.callToReturnCache.stats());
   }

   private class ReturnKey extends FlowFunctionCache<N, D, M>.CallKey {
      private final N exitStmt;
      private final N returnSite;

      private ReturnKey(N callStmt, M destinationMethod, N exitStmt, N returnSite) {
         super(callStmt, destinationMethod, null);
         this.exitStmt = exitStmt;
         this.returnSite = returnSite;
      }

      public N getExitStmt() {
         return this.exitStmt;
      }

      public N getReturnSite() {
         return this.returnSite;
      }

      public int hashCode() {
         int prime = true;
         int result = super.hashCode();
         result = 31 * result + (this.exitStmt == null ? 0 : this.exitStmt.hashCode());
         result = 31 * result + (this.returnSite == null ? 0 : this.returnSite.hashCode());
         return result;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!super.equals(obj)) {
            return false;
         } else if (this.getClass() != obj.getClass()) {
            return false;
         } else {
            FlowFunctionCache<N, D, M>.ReturnKey other = (FlowFunctionCache.ReturnKey)obj;
            if (this.exitStmt == null) {
               if (other.exitStmt != null) {
                  return false;
               }
            } else if (!this.exitStmt.equals(other.exitStmt)) {
               return false;
            }

            if (this.returnSite == null) {
               if (other.returnSite != null) {
                  return false;
               }
            } else if (!this.returnSite.equals(other.returnSite)) {
               return false;
            }

            return true;
         }
      }

      // $FF: synthetic method
      ReturnKey(Object x1, Object x2, Object x3, Object x4, Object x5) {
         this(x1, x2, x3, x4);
      }
   }

   private class CallKey {
      private final N callStmt;
      private final M destinationMethod;

      private CallKey(N callStmt, M destinationMethod) {
         this.callStmt = callStmt;
         this.destinationMethod = destinationMethod;
      }

      public N getCallStmt() {
         return this.callStmt;
      }

      public M getDestinationMethod() {
         return this.destinationMethod;
      }

      public int hashCode() {
         int prime = true;
         int resultx = 1;
         int result = 31 * resultx + (this.callStmt == null ? 0 : this.callStmt.hashCode());
         result = 31 * result + (this.destinationMethod == null ? 0 : this.destinationMethod.hashCode());
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
            FlowFunctionCache<N, D, M>.CallKey other = (FlowFunctionCache.CallKey)obj;
            if (this.callStmt == null) {
               if (other.callStmt != null) {
                  return false;
               }
            } else if (!this.callStmt.equals(other.callStmt)) {
               return false;
            }

            if (this.destinationMethod == null) {
               if (other.destinationMethod != null) {
                  return false;
               }
            } else if (!this.destinationMethod.equals(other.destinationMethod)) {
               return false;
            }

            return true;
         }
      }

      // $FF: synthetic method
      CallKey(Object x1, Object x2, Object x3) {
         this(x1, x2);
      }
   }

   private class NNKey {
      private final N curr;
      private final N succ;

      private NNKey(N curr, N succ) {
         this.curr = curr;
         this.succ = succ;
      }

      public N getCurr() {
         return this.curr;
      }

      public N getSucc() {
         return this.succ;
      }

      public int hashCode() {
         int prime = true;
         int resultx = 1;
         int result = 31 * resultx + (this.curr == null ? 0 : this.curr.hashCode());
         result = 31 * result + (this.succ == null ? 0 : this.succ.hashCode());
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
            FlowFunctionCache<N, D, M>.NNKey other = (FlowFunctionCache.NNKey)obj;
            if (this.curr == null) {
               if (other.curr != null) {
                  return false;
               }
            } else if (!this.curr.equals(other.curr)) {
               return false;
            }

            if (this.succ == null) {
               if (other.succ != null) {
                  return false;
               }
            } else if (!this.succ.equals(other.succ)) {
               return false;
            }

            return true;
         }
      }

      // $FF: synthetic method
      NNKey(Object x1, Object x2, Object x3) {
         this(x1, x2);
      }
   }
}
