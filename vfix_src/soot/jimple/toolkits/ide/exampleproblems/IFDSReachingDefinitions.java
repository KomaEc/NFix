package soot.jimple.toolkits.ide.exampleproblems;

import heros.DefaultSeeds;
import heros.FlowFunction;
import heros.FlowFunctions;
import heros.InterproceduralCFG;
import heros.flowfunc.Identity;
import heros.flowfunc.KillAll;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.EquivalentValue;
import soot.Local;
import soot.NullType;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.DefinitionStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.internal.JimpleLocal;
import soot.jimple.toolkits.ide.DefaultJimpleIFDSTabulationProblem;
import soot.toolkits.scalar.Pair;

public class IFDSReachingDefinitions extends DefaultJimpleIFDSTabulationProblem<Pair<Value, Set<DefinitionStmt>>, InterproceduralCFG<Unit, SootMethod>> {
   public IFDSReachingDefinitions(InterproceduralCFG<Unit, SootMethod> icfg) {
      super(icfg);
   }

   public FlowFunctions<Unit, Pair<Value, Set<DefinitionStmt>>, SootMethod> createFlowFunctionsFactory() {
      return new FlowFunctions<Unit, Pair<Value, Set<DefinitionStmt>>, SootMethod>() {
         public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getNormalFlowFunction(Unit curr, Unit succ) {
            if (curr instanceof DefinitionStmt) {
               final DefinitionStmt assignment = (DefinitionStmt)curr;
               return new FlowFunction<Pair<Value, Set<DefinitionStmt>>>() {
                  public Set<Pair<Value, Set<DefinitionStmt>>> computeTargets(Pair<Value, Set<DefinitionStmt>> source) {
                     if (source != IFDSReachingDefinitions.this.zeroValue()) {
                        return ((Value)source.getO1()).equivTo(assignment.getLeftOp()) ? Collections.emptySet() : Collections.singleton(source);
                     } else {
                        LinkedHashSet<Pair<Value, Set<DefinitionStmt>>> res = new LinkedHashSet();
                        res.add(new Pair(assignment.getLeftOp(), Collections.singleton(assignment)));
                        return res;
                     }
                  }
               };
            } else {
               return Identity.v();
            }
         }

         public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getCallFlowFunction(Unit callStmt, final SootMethod destinationMethod) {
            Stmt stmt = (Stmt)callStmt;
            InvokeExpr invokeExpr = stmt.getInvokeExpr();
            final List<Value> args = invokeExpr.getArgs();
            final List<Local> localArguments = new ArrayList(args.size());
            Iterator var7 = args.iterator();

            while(var7.hasNext()) {
               Value value = (Value)var7.next();
               if (value instanceof Local) {
                  localArguments.add((Local)value);
               } else {
                  localArguments.add((Object)null);
               }
            }

            return new FlowFunction<Pair<Value, Set<DefinitionStmt>>>() {
               public Set<Pair<Value, Set<DefinitionStmt>>> computeTargets(Pair<Value, Set<DefinitionStmt>> source) {
                  if (!destinationMethod.getName().equals("<clinit>") && !destinationMethod.getSubSignature().equals("void run()") && localArguments.contains(source.getO1())) {
                     int paramIndex = args.indexOf(source.getO1());
                     Pair<Value, Set<DefinitionStmt>> pair = new Pair(new EquivalentValue(Jimple.v().newParameterRef(destinationMethod.getParameterType(paramIndex), paramIndex)), source.getO2());
                     return Collections.singleton(pair);
                  } else {
                     return Collections.emptySet();
                  }
               }
            };
         }

         public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getReturnFlowFunction(final Unit callSite, SootMethod calleeMethod, final Unit exitStmt, Unit returnSite) {
            if (!(callSite instanceof DefinitionStmt)) {
               return KillAll.v();
            } else {
               return (FlowFunction)(exitStmt instanceof ReturnVoidStmt ? KillAll.v() : new FlowFunction<Pair<Value, Set<DefinitionStmt>>>() {
                  public Set<Pair<Value, Set<DefinitionStmt>>> computeTargets(Pair<Value, Set<DefinitionStmt>> source) {
                     if (exitStmt instanceof ReturnStmt) {
                        ReturnStmt returnStmt = (ReturnStmt)exitStmt;
                        if (returnStmt.getOp().equivTo(source.getO1())) {
                           DefinitionStmt definitionStmt = (DefinitionStmt)callSite;
                           Pair<Value, Set<DefinitionStmt>> pair = new Pair(definitionStmt.getLeftOp(), source.getO2());
                           return Collections.singleton(pair);
                        }
                     }

                     return Collections.emptySet();
                  }
               });
            }
         }

         public FlowFunction<Pair<Value, Set<DefinitionStmt>>> getCallToReturnFlowFunction(Unit callSite, Unit returnSite) {
            if (!(callSite instanceof DefinitionStmt)) {
               return Identity.v();
            } else {
               final DefinitionStmt definitionStmt = (DefinitionStmt)callSite;
               return new FlowFunction<Pair<Value, Set<DefinitionStmt>>>() {
                  public Set<Pair<Value, Set<DefinitionStmt>>> computeTargets(Pair<Value, Set<DefinitionStmt>> source) {
                     return ((Value)source.getO1()).equivTo(definitionStmt.getLeftOp()) ? Collections.emptySet() : Collections.singleton(source);
                  }
               };
            }
         }
      };
   }

   public Map<Unit, Set<Pair<Value, Set<DefinitionStmt>>>> initialSeeds() {
      return DefaultSeeds.make(Collections.singleton(Scene.v().getMainMethod().getActiveBody().getUnits().getFirst()), this.zeroValue());
   }

   public Pair<Value, Set<DefinitionStmt>> createZeroValue() {
      return new Pair(new JimpleLocal("<<zero>>", NullType.v()), Collections.emptySet());
   }
}
