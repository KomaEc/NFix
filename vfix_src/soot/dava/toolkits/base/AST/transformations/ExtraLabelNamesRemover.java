package soot.dava.toolkits.base.AST.transformations;

import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;

public class ExtraLabelNamesRemover extends DepthFirstAdapter {
   public ExtraLabelNamesRemover() {
   }

   public ExtraLabelNamesRemover(boolean verbose) {
      super(verbose);
   }
}
