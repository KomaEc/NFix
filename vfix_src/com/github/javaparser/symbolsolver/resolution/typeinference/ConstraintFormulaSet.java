package com.github.javaparser.symbolsolver.resolution.typeinference;

import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import java.util.LinkedList;
import java.util.List;

public class ConstraintFormulaSet {
   private List<ConstraintFormula> constraintFormulas = new LinkedList();
   private static final ConstraintFormulaSet EMPTY = new ConstraintFormulaSet();

   public ConstraintFormulaSet withConstraint(ConstraintFormula constraintFormula) {
      ConstraintFormulaSet newInstance = new ConstraintFormulaSet();
      newInstance.constraintFormulas.addAll(this.constraintFormulas);
      newInstance.constraintFormulas.add(constraintFormula);
      return newInstance;
   }

   public static ConstraintFormulaSet empty() {
      return EMPTY;
   }

   private ConstraintFormulaSet() {
   }

   public BoundSet reduce(TypeSolver typeSolver) {
      List<ConstraintFormula> constraints = new LinkedList(this.constraintFormulas);
      BoundSet boundSet = BoundSet.empty();

      while(constraints.size() > 0) {
         ConstraintFormula constraintFormula = (ConstraintFormula)constraints.remove(0);
         ConstraintFormula.ReductionResult reductionResult = constraintFormula.reduce(boundSet);
         constraints.addAll(reductionResult.getConstraintFormulas());
         boundSet.incorporate(reductionResult.getBoundSet(), typeSolver);
      }

      return boundSet;
   }

   public boolean isEmpty() {
      return this.constraintFormulas.isEmpty();
   }
}
