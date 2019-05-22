package soot.dava.internal.AST;

import soot.UnitPrinter;
import soot.dava.toolkits.base.AST.analysis.Analysis;

public abstract class ASTCondition {
   public abstract void apply(Analysis var1);

   public abstract void toString(UnitPrinter var1);

   public abstract void flip();

   public abstract boolean isNotted();
}
