package heros;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class ZeroedFlowFunctions<N, D, M> implements FlowFunctions<N, D, M> {
   protected final FlowFunctions<N, D, M> delegate;
   protected final D zeroValue;

   public ZeroedFlowFunctions(FlowFunctions<N, D, M> delegate, D zeroValue) {
      this.delegate = delegate;
      this.zeroValue = zeroValue;
   }

   public FlowFunction<D> getNormalFlowFunction(N curr, N succ) {
      return new ZeroedFlowFunctions.ZeroedFlowFunction(this.delegate.getNormalFlowFunction(curr, succ));
   }

   public FlowFunction<D> getCallFlowFunction(N callStmt, M destinationMethod) {
      return new ZeroedFlowFunctions.ZeroedFlowFunction(this.delegate.getCallFlowFunction(callStmt, destinationMethod));
   }

   public FlowFunction<D> getReturnFlowFunction(N callSite, M calleeMethod, N exitStmt, N returnSite) {
      return new ZeroedFlowFunctions.ZeroedFlowFunction(this.delegate.getReturnFlowFunction(callSite, calleeMethod, exitStmt, returnSite));
   }

   public FlowFunction<D> getCallToReturnFlowFunction(N callSite, N returnSite) {
      return new ZeroedFlowFunctions.ZeroedFlowFunction(this.delegate.getCallToReturnFlowFunction(callSite, returnSite));
   }

   protected class ZeroedFlowFunction implements FlowFunction<D> {
      protected FlowFunction<D> del;

      private ZeroedFlowFunction(FlowFunction<D> del) {
         this.del = del;
      }

      public Set<D> computeTargets(D source) {
         if (source == ZeroedFlowFunctions.this.zeroValue) {
            HashSet<D> res = new LinkedHashSet(this.del.computeTargets(source));
            res.add(ZeroedFlowFunctions.this.zeroValue);
            return res;
         } else {
            return this.del.computeTargets(source);
         }
      }

      // $FF: synthetic method
      ZeroedFlowFunction(FlowFunction x1, Object x2) {
         this(x1);
      }
   }
}
