package heros;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EdgeFunctionCache<N, D, M, V> implements EdgeFunctions<N, D, M, V> {
   protected final EdgeFunctions<N, D, M, V> delegate;
   protected final LoadingCache<EdgeFunctionCache<N, D, M, V>.NDNDKey, EdgeFunction<V>> normalCache;
   protected final LoadingCache<EdgeFunctionCache<N, D, M, V>.CallKey, EdgeFunction<V>> callCache;
   protected final LoadingCache<EdgeFunctionCache<N, D, M, V>.ReturnKey, EdgeFunction<V>> returnCache;
   protected final LoadingCache<EdgeFunctionCache<N, D, M, V>.NDNDKey, EdgeFunction<V>> callToReturnCache;
   Logger logger = LoggerFactory.getLogger(this.getClass());

   public EdgeFunctionCache(final EdgeFunctions<N, D, M, V> delegate, CacheBuilder builder) {
      this.delegate = delegate;
      this.normalCache = builder.build(new CacheLoader<EdgeFunctionCache<N, D, M, V>.NDNDKey, EdgeFunction<V>>() {
         public EdgeFunction<V> load(EdgeFunctionCache<N, D, M, V>.NDNDKey key) throws Exception {
            return delegate.getNormalEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
         }
      });
      this.callCache = builder.build(new CacheLoader<EdgeFunctionCache<N, D, M, V>.CallKey, EdgeFunction<V>>() {
         public EdgeFunction<V> load(EdgeFunctionCache<N, D, M, V>.CallKey key) throws Exception {
            return delegate.getCallEdgeFunction(key.getCallSite(), key.getD1(), key.getCalleeMethod(), key.getD2());
         }
      });
      this.returnCache = builder.build(new CacheLoader<EdgeFunctionCache<N, D, M, V>.ReturnKey, EdgeFunction<V>>() {
         public EdgeFunction<V> load(EdgeFunctionCache<N, D, M, V>.ReturnKey key) throws Exception {
            return delegate.getReturnEdgeFunction(key.getCallSite(), key.getCalleeMethod(), key.getExitStmt(), key.getD1(), key.getReturnSite(), key.getD2());
         }
      });
      this.callToReturnCache = builder.build(new CacheLoader<EdgeFunctionCache<N, D, M, V>.NDNDKey, EdgeFunction<V>>() {
         public EdgeFunction<V> load(EdgeFunctionCache<N, D, M, V>.NDNDKey key) throws Exception {
            return delegate.getCallToReturnEdgeFunction(key.getN1(), key.getD1(), key.getN2(), key.getD2());
         }
      });
   }

   public EdgeFunction<V> getNormalEdgeFunction(N curr, D currNode, N succ, D succNode) {
      return (EdgeFunction)this.normalCache.getUnchecked(new EdgeFunctionCache.NDNDKey(curr, currNode, succ, succNode));
   }

   public EdgeFunction<V> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
      return (EdgeFunction)this.callCache.getUnchecked(new EdgeFunctionCache.CallKey(callStmt, srcNode, destinationMethod, destNode));
   }

   public EdgeFunction<V> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
      return (EdgeFunction)this.returnCache.getUnchecked(new EdgeFunctionCache.ReturnKey(callSite, calleeMethod, exitStmt, exitNode, returnSite, retNode));
   }

   public EdgeFunction<V> getCallToReturnEdgeFunction(N callSite, D callNode, N returnSite, D returnSideNode) {
      return (EdgeFunction)this.callToReturnCache.getUnchecked(new EdgeFunctionCache.NDNDKey(callSite, callNode, returnSite, returnSideNode));
   }

   public void printStats() {
      this.logger.debug("Stats for edge-function cache:\nNormal:         {}\nCall:           {}\nReturn:         {}\nCall-to-return: {}\n", this.normalCache.stats(), this.callCache.stats(), this.returnCache.stats(), this.callToReturnCache.stats());
   }

   private class ReturnKey extends EdgeFunctionCache<N, D, M, V>.CallKey {
      private final N exitStmt;
      private final N returnSite;

      public ReturnKey(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
         super(callSite, exitNode, calleeMethod, retNode);
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
            EdgeFunctionCache<N, D, M, V>.ReturnKey other = (EdgeFunctionCache.ReturnKey)obj;
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
   }

   private class CallKey {
      private final N callSite;
      private final M calleeMethod;
      private final D d1;
      private final D d2;

      public CallKey(N callSite, D d1, M calleeMethod, D d2) {
         this.callSite = callSite;
         this.calleeMethod = calleeMethod;
         this.d1 = d1;
         this.d2 = d2;
      }

      public N getCallSite() {
         return this.callSite;
      }

      public D getD1() {
         return this.d1;
      }

      public M getCalleeMethod() {
         return this.calleeMethod;
      }

      public D getD2() {
         return this.d2;
      }

      public int hashCode() {
         int prime = true;
         int resultx = 1;
         int result = 31 * resultx + (this.d1 == null ? 0 : this.d1.hashCode());
         result = 31 * result + (this.d2 == null ? 0 : this.d2.hashCode());
         result = 31 * result + (this.callSite == null ? 0 : this.callSite.hashCode());
         result = 31 * result + (this.calleeMethod == null ? 0 : this.calleeMethod.hashCode());
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
            EdgeFunctionCache<N, D, M, V>.CallKey other = (EdgeFunctionCache.CallKey)obj;
            if (this.d1 == null) {
               if (other.d1 != null) {
                  return false;
               }
            } else if (!this.d1.equals(other.d1)) {
               return false;
            }

            if (this.d2 == null) {
               if (other.d2 != null) {
                  return false;
               }
            } else if (!this.d2.equals(other.d2)) {
               return false;
            }

            if (this.callSite == null) {
               if (other.callSite != null) {
                  return false;
               }
            } else if (!this.callSite.equals(other.callSite)) {
               return false;
            }

            if (this.calleeMethod == null) {
               if (other.calleeMethod != null) {
                  return false;
               }
            } else if (!this.calleeMethod.equals(other.calleeMethod)) {
               return false;
            }

            return true;
         }
      }
   }

   private class NDNDKey {
      private final N n1;
      private final N n2;
      private final D d1;
      private final D d2;

      public NDNDKey(N n1, D d1, N n2, D d2) {
         this.n1 = n1;
         this.n2 = n2;
         this.d1 = d1;
         this.d2 = d2;
      }

      public N getN1() {
         return this.n1;
      }

      public D getD1() {
         return this.d1;
      }

      public N getN2() {
         return this.n2;
      }

      public D getD2() {
         return this.d2;
      }

      public int hashCode() {
         int prime = true;
         int resultx = 1;
         int result = 31 * resultx + (this.d1 == null ? 0 : this.d1.hashCode());
         result = 31 * result + (this.d2 == null ? 0 : this.d2.hashCode());
         result = 31 * result + (this.n1 == null ? 0 : this.n1.hashCode());
         result = 31 * result + (this.n2 == null ? 0 : this.n2.hashCode());
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
            EdgeFunctionCache<N, D, M, V>.NDNDKey other = (EdgeFunctionCache.NDNDKey)obj;
            if (this.d1 == null) {
               if (other.d1 != null) {
                  return false;
               }
            } else if (!this.d1.equals(other.d1)) {
               return false;
            }

            if (this.d2 == null) {
               if (other.d2 != null) {
                  return false;
               }
            } else if (!this.d2.equals(other.d2)) {
               return false;
            }

            if (this.n1 == null) {
               if (other.n1 != null) {
                  return false;
               }
            } else if (!this.n1.equals(other.n1)) {
               return false;
            }

            if (this.n2 == null) {
               if (other.n2 != null) {
                  return false;
               }
            } else if (!this.n2.equals(other.n2)) {
               return false;
            }

            return true;
         }
      }
   }
}
