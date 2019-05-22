package soot.jimple.toolkits.ide.exampleproblems;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Gen;
import heros.flowfunc.Identity;
import heros.flowfunc.Kill;
import heros.flowfunc.KillAll;
import heros.flowfunc.Transfer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Local;
import soot.NullType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;

public class IFDSLocalInfoFlow extends DefaultJimpleIFDSTabulationProblem<Local, InterproceduralCFG<Unit, SootMethod>> {
   public IFDSLocalInfoFlow(InterproceduralCFG<Unit, SootMethod> icfg) {
      super(icfg);
   }

   public FlowFunctions<Unit, Local, SootMethod> createFlowFunctionsFactory() {
      return new FlowFunctions<Unit, Local, SootMethod>() {
         public FlowFunction<Local> getNormalFlowFunction(Unit src, Unit dest) {
            if (src instanceof IdentityStmt && IFDSLocalInfoFlow.this.interproceduralCFG().getMethodOf(src) == Scene.v().getMainMethod()) {
               IdentityStmt is = (IdentityStmt)src;
               Local leftLocalx = (Local)is.getLeftOp();
               Value rightx = is.getRightOp();
               if (rightx instanceof ParameterRef) {
                  return new Gen(leftLocalx, IFDSLocalInfoFlow.this.zeroValue());
               }
            }

            if (src instanceof AssignStmt) {
               AssignStmt assignStmt = (AssignStmt)src;
               Value right = assignStmt.getRightOp();
               if (assignStmt.getLeftOp() instanceof Local) {
                  Local leftLocal = (Local)assignStmt.getLeftOp();
                  if (right instanceof Local) {
                     Local rightLocal = (Local)right;
                     return new Transfer(leftLocal, rightLocal);
                  }

                  return new Kill(leftLocal);
               }
            }

            return Identity.v();
         }

         public FlowFunction<Local> getCallFlowFunction(Unit src, final SootMethod dest) {
            Stmt s = (Stmt)src;
            InvokeExpr ie = s.getInvokeExpr();
            final List<Value> callArgs = ie.getArgs();
            final List<Local> paramLocals = new ArrayList();

            for(int i = 0; i < dest.getParameterCount(); ++i) {
               paramLocals.add(dest.getActiveBody().getParameterLocal(i));
            }

            return new FlowFunction<Local>() {
               public Set<Local> computeTargets(Local source) {
                  if (dest.getName().equals("<clinit>") && dest.getParameterCount() == 0) {
                     return Collections.emptySet();
                  } else {
                     Set<Local> taintsInCaller = new HashSet();

                     for(int i = 0; i < callArgs.size(); ++i) {
                        if (((Value)callArgs.get(i)).equivTo(source)) {
                           taintsInCaller.add(paramLocals.get(i));
                        }
                     }

                     return taintsInCaller;
                  }
               }
            };
         }

         public FlowFunction<Local> getReturnFlowFunction(Unit callSite, SootMethod callee, Unit exitStmt, Unit retSite) {
            if (exitStmt instanceof ReturnStmt) {
               ReturnStmt returnStmt = (ReturnStmt)exitStmt;
               Value op = returnStmt.getOp();
               if (op instanceof Local && callSite instanceof DefinitionStmt) {
                  DefinitionStmt defnStmt = (DefinitionStmt)callSite;
                  Value leftOp = defnStmt.getLeftOp();
                  if (leftOp instanceof Local) {
                     final Local tgtLocal = (Local)leftOp;
                     final Local retLocal = (Local)op;
                     return new FlowFunction<Local>() {
                        public Set<Local> computeTargets(Local source) {
                           return source == retLocal ? Collections.singleton(tgtLocal) : Collections.emptySet();
                        }
                     };
                  }
               }
            }

            return KillAll.v();
         }

         public FlowFunction<Local> getCallToReturnFlowFunction(Unit call, Unit returnSite) {
            return Identity.v();
         }
      };
   }

   public Local createZeroValue() {
      return new JimpleLocal("zero", NullType.v());
   }

   public Map<Unit, Set<Local>> initialSeeds() {
      return DefaultSeeds.make(Collections.singleton(Scene.v().getMainMethod().getActiveBody().getUnits().getFirst()), this.zeroValue());
   }
}
