package soot.jimple.toolkits.ide.exampleproblems;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Local;
import soot.NullType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;

public class IFDSLiveVariables extends DefaultJimpleIFDSTabulationProblem<Value, InterproceduralCFG<Unit, SootMethod>> {
   public IFDSLiveVariables(InterproceduralCFG<Unit, SootMethod> icfg) {
      super(icfg);
   }

   public FlowFunctions<Unit, Value, SootMethod> createFlowFunctionsFactory() {
      return new FlowFunctions<Unit, Value, SootMethod>() {
         public FlowFunction<Value> getNormalFlowFunction(Unit curr, Unit succ) {
            if (curr.getUseAndDefBoxes().isEmpty()) {
               return Identity.v();
            } else {
               final Stmt s = (Stmt)curr;
               return new FlowFunction<Value>() {
                  public Set<Value> computeTargets(Value source) {
                     List<ValueBox> defs = s.getDefBoxes();
                     if (!defs.isEmpty() && ((ValueBox)defs.get(0)).getValue().equivTo(source)) {
                        return Collections.emptySet();
                     } else if (!source.equals(IFDSLiveVariables.this.zeroValue())) {
                        return Collections.singleton(source);
                     } else {
                        Set<Value> liveVars = new HashSet();
                        Iterator var4 = s.getUseBoxes().iterator();

                        while(var4.hasNext()) {
                           ValueBox useBox = (ValueBox)var4.next();
                           Value value = useBox.getValue();
                           liveVars.add(value);
                        }

                        return liveVars;
                     }
                  }
               };
            }
         }

         public FlowFunction<Value> getCallFlowFunction(Unit callStmt, final SootMethod destinationMethod) {
            final Stmt s = (Stmt)callStmt;
            return new FlowFunction<Value>() {
               public Set<Value> computeTargets(Value source) {
                  if (!s.getDefBoxes().isEmpty()) {
                     Value callerSideReturnValue = ((ValueBox)s.getDefBoxes().get(0)).getValue();
                     if (callerSideReturnValue.equivTo(source)) {
                        Set<Value> calleeSideReturnValues = new HashSet();
                        Iterator var4 = IFDSLiveVariables.this.interproceduralCFG().getStartPointsOf(destinationMethod).iterator();

                        while(var4.hasNext()) {
                           Unit calleeUnit = (Unit)var4.next();
                           if (calleeUnit instanceof ReturnStmt) {
                              ReturnStmt returnStmt = (ReturnStmt)calleeUnit;
                              calleeSideReturnValues.add(returnStmt.getOp());
                           }
                        }

                        return calleeSideReturnValues;
                     }
                  }

                  return Collections.emptySet();
               }
            };
         }

         public FlowFunction<Value> getReturnFlowFunction(Unit callSite, SootMethod calleeMethod, Unit exitStmt, Unit returnSite) {
            Stmt s = (Stmt)callSite;
            InvokeExpr ie = s.getInvokeExpr();
            final List<Value> callArgs = ie.getArgs();
            final List<Local> paramLocals = new ArrayList();

            for(int i = 0; i < calleeMethod.getParameterCount(); ++i) {
               paramLocals.add(calleeMethod.getActiveBody().getParameterLocal(i));
            }

            return new FlowFunction<Value>() {
               public Set<Value> computeTargets(Value source) {
                  Set<Value> liveParamsAtCallee = new HashSet();

                  for(int i = 0; i < paramLocals.size(); ++i) {
                     if (((Local)paramLocals.get(i)).equivTo(source)) {
                        liveParamsAtCallee.add(callArgs.get(i));
                     }
                  }

                  return liveParamsAtCallee;
               }
            };
         }

         public FlowFunction<Value> getCallToReturnFlowFunction(Unit callSite, Unit returnSite) {
            if (callSite.getUseAndDefBoxes().isEmpty()) {
               return Identity.v();
            } else {
               final Stmt s = (Stmt)callSite;
               return new FlowFunction<Value>() {
                  public Set<Value> computeTargets(Value source) {
                     List<ValueBox> defs = s.getDefBoxes();
                     if (!defs.isEmpty() && ((ValueBox)defs.get(0)).getValue().equivTo(source)) {
                        return Collections.emptySet();
                     } else if (source.equals(IFDSLiveVariables.this.zeroValue())) {
                        Set<Value> liveVars = new HashSet();
                        List<Value> args = s.getInvokeExpr().getArgs();
                        Iterator var5 = s.getUseBoxes().iterator();

                        while(var5.hasNext()) {
                           ValueBox useBox = (ValueBox)var5.next();
                           Value value = useBox.getValue();
                           if (!args.contains(value)) {
                              liveVars.add(value);
                           }
                        }

                        return liveVars;
                     } else {
                        return Collections.singleton(source);
                     }
                  }
               };
            }
         }
      };
   }

   public Map<Unit, Set<Value>> initialSeeds() {
      return DefaultSeeds.make(this.interproceduralCFG().getStartPointsOf(Scene.v().getMainMethod()), this.zeroValue());
   }

   public Value createZeroValue() {
      return new JimpleLocal("<<zero>>", NullType.v());
   }
}
