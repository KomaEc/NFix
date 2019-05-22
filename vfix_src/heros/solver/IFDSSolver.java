package heros.solver;

import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.JoinLattice;
import heros.edgefunc.AllBottom;
import heros.edgefunc.AllTop;
import heros.edgefunc.EdgeIdentity;
import java.util.Map;
import java.util.Set;

public class IFDSSolver<N, D, M, I extends InterproceduralCFG<N, M>> extends IDESolver<N, D, M, IFDSSolver.BinaryDomain, I> {
   private static final EdgeFunction<IFDSSolver.BinaryDomain> ALL_BOTTOM;

   public IFDSSolver(IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
      super(createIDETabulationProblem(ifdsProblem));
   }

   static <N, D, M, I extends InterproceduralCFG<N, M>> IDETabulationProblem<N, D, M, IFDSSolver.BinaryDomain, I> createIDETabulationProblem(final IFDSTabulationProblem<N, D, M, I> ifdsProblem) {
      return new IDETabulationProblem<N, D, M, IFDSSolver.BinaryDomain, I>() {
         public FlowFunctions<N, D, M> flowFunctions() {
            return ifdsProblem.flowFunctions();
         }

         public I interproceduralCFG() {
            return ifdsProblem.interproceduralCFG();
         }

         public Map<N, Set<D>> initialSeeds() {
            return ifdsProblem.initialSeeds();
         }

         public D zeroValue() {
            return ifdsProblem.zeroValue();
         }

         public EdgeFunctions<N, D, M, IFDSSolver.BinaryDomain> edgeFunctions() {
            return new null.IFDSEdgeFunctions();
         }

         public JoinLattice<IFDSSolver.BinaryDomain> joinLattice() {
            return new JoinLattice<IFDSSolver.BinaryDomain>() {
               public IFDSSolver.BinaryDomain topElement() {
                  return IFDSSolver.BinaryDomain.TOP;
               }

               public IFDSSolver.BinaryDomain bottomElement() {
                  return IFDSSolver.BinaryDomain.BOTTOM;
               }

               public IFDSSolver.BinaryDomain join(IFDSSolver.BinaryDomain left, IFDSSolver.BinaryDomain right) {
                  return left == IFDSSolver.BinaryDomain.TOP && right == IFDSSolver.BinaryDomain.TOP ? IFDSSolver.BinaryDomain.TOP : IFDSSolver.BinaryDomain.BOTTOM;
               }
            };
         }

         public EdgeFunction<IFDSSolver.BinaryDomain> allTopFunction() {
            return new AllTop(IFDSSolver.BinaryDomain.TOP);
         }

         public boolean followReturnsPastSeeds() {
            return ifdsProblem.followReturnsPastSeeds();
         }

         public boolean autoAddZero() {
            return ifdsProblem.autoAddZero();
         }

         public int numThreads() {
            return ifdsProblem.numThreads();
         }

         public boolean computeValues() {
            return ifdsProblem.computeValues();
         }

         public boolean recordEdges() {
            return ifdsProblem.recordEdges();
         }

         class IFDSEdgeFunctions implements EdgeFunctions<N, D, M, IFDSSolver.BinaryDomain> {
            public EdgeFunction<IFDSSolver.BinaryDomain> getNormalEdgeFunction(N src, D srcNode, N tgt, D tgtNode) {
               return (EdgeFunction)(srcNode == ifdsProblem.zeroValue() ? IFDSSolver.ALL_BOTTOM : EdgeIdentity.v());
            }

            public EdgeFunction<IFDSSolver.BinaryDomain> getCallEdgeFunction(N callStmt, D srcNode, M destinationMethod, D destNode) {
               return (EdgeFunction)(srcNode == ifdsProblem.zeroValue() ? IFDSSolver.ALL_BOTTOM : EdgeIdentity.v());
            }

            public EdgeFunction<IFDSSolver.BinaryDomain> getReturnEdgeFunction(N callSite, M calleeMethod, N exitStmt, D exitNode, N returnSite, D retNode) {
               return (EdgeFunction)(exitNode == ifdsProblem.zeroValue() ? IFDSSolver.ALL_BOTTOM : EdgeIdentity.v());
            }

            public EdgeFunction<IFDSSolver.BinaryDomain> getCallToReturnEdgeFunction(N callStmt, D callNode, N returnSite, D returnSideNode) {
               return (EdgeFunction)(callNode == ifdsProblem.zeroValue() ? IFDSSolver.ALL_BOTTOM : EdgeIdentity.v());
            }
         }
      };
   }

   public Set<D> ifdsResultsAt(N statement) {
      return this.resultsAt(statement).keySet();
   }

   static {
      ALL_BOTTOM = new AllBottom(IFDSSolver.BinaryDomain.BOTTOM);
   }

   protected static enum BinaryDomain {
      TOP,
      BOTTOM;
   }
}
