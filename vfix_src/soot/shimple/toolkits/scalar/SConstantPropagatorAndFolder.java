package soot.shimple.toolkits.scalar;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.PhaseOptions;
import soot.Singletons;
import soot.Unit;
import soot.UnitBoxOwner;
import soot.Value;
import soot.ValueBox;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.shimple.ShimpleBody;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.scalar.UnitValueBoxPair;
import soot.util.Chain;

public class SConstantPropagatorAndFolder extends BodyTransformer {
   private static final Logger logger = LoggerFactory.getLogger(SConstantPropagatorAndFolder.class);
   protected ShimpleBody sb;
   protected boolean debug;

   public SConstantPropagatorAndFolder(Singletons.Global g) {
   }

   public static SConstantPropagatorAndFolder v() {
      return G.v().soot_shimple_toolkits_scalar_SConstantPropagatorAndFolder();
   }

   protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
      if (!(b instanceof ShimpleBody)) {
         throw new RuntimeException("SConstantPropagatorAndFolder requires a ShimpleBody.");
      } else {
         this.sb = (ShimpleBody)b;
         if (!this.sb.isSSA()) {
            throw new RuntimeException("ShimpleBody is not in proper SSA form as required by SConstantPropagatorAndFolder.  You may need to rebuild it or use ConstantPropagatorAndFolder instead.");
         } else {
            boolean pruneCFG = PhaseOptions.getBoolean(options, "prune-cfg");
            this.debug = Options.v().debug();
            this.debug |= this.sb.getOptions().debug();
            if (Options.v().verbose()) {
               logger.debug("[" + this.sb.getMethod().getName() + "] Propagating and folding constants (SSA)...");
            }

            SCPFAnalysis scpf = new SCPFAnalysis(new ExceptionalUnitGraph(this.sb));
            this.propagateResults(scpf.getResults());
            if (pruneCFG) {
               this.removeStmts(scpf.getDeadStmts());
               this.replaceStmts(scpf.getStmtsToReplace());
            }

         }
      }
   }

   protected void propagateResults(Map<Local, Constant> localToConstant) {
      Chain<Unit> units = this.sb.getUnits();
      Collection<Local> locals = this.sb.getLocals();
      ShimpleLocalDefs localDefs = new ShimpleLocalDefs(this.sb);
      ShimpleLocalUses localUses = new ShimpleLocalUses(this.sb);
      Iterator localsIt = locals.iterator();

      while(true) {
         Local local;
         Constant constant;
         do {
            if (!localsIt.hasNext()) {
               return;
            }

            local = (Local)localsIt.next();
            constant = (Constant)localToConstant.get(local);
         } while(constant instanceof SEvaluator.MetaConstant);

         DefinitionStmt stmt = (DefinitionStmt)localDefs.getDefsOf(local).get(0);
         ValueBox defSrcBox = stmt.getRightOpBox();
         Value defSrc = defSrcBox.getValue();
         if (defSrcBox.canContainValue(constant)) {
            defSrcBox.setValue(constant);
            if (defSrc instanceof UnitBoxOwner) {
               ((UnitBoxOwner)defSrc).clearUnitBoxes();
            }
         } else if (this.debug) {
            logger.warn("Couldn't propagate constant " + constant + " to box " + defSrcBox.getValue() + " in unit " + stmt);
         }

         Iterator usesIt = localUses.getUsesOf(local).iterator();

         while(usesIt.hasNext()) {
            UnitValueBoxPair pair = (UnitValueBoxPair)usesIt.next();
            ValueBox useBox = pair.getValueBox();
            if (useBox.canContainValue(constant)) {
               useBox.setValue(constant);
            } else if (this.debug) {
               logger.warn("Couldn't propagate constant " + constant + " to box " + useBox.getValue() + " in unit " + pair.getUnit());
            }
         }
      }
   }

   protected void removeStmts(List<IfStmt> deadStmts) {
      Chain units = this.sb.getUnits();
      Iterator deadIt = deadStmts.iterator();

      while(deadIt.hasNext()) {
         Unit dead = (Unit)deadIt.next();
         units.remove(dead);
         dead.clearUnitBoxes();
      }

   }

   protected void replaceStmts(Map<Stmt, GotoStmt> stmtsToReplace) {
      Chain units = this.sb.getUnits();
      Iterator stmtsIt = stmtsToReplace.keySet().iterator();

      while(stmtsIt.hasNext()) {
         Unit booted = (Unit)stmtsIt.next();
         Unit replacement = (Unit)stmtsToReplace.get(booted);
         units.swapWith(booted, replacement);
      }

   }
}
