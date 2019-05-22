package soot.dava.toolkits.base.AST.traversals;

import java.util.ArrayList;
import java.util.List;
import soot.dava.toolkits.base.AST.analysis.DepthFirstAdapter;
import soot.jimple.DefinitionStmt;

public class AllDefinitionsFinder extends DepthFirstAdapter {
   ArrayList<DefinitionStmt> allDefs = new ArrayList();

   public AllDefinitionsFinder() {
   }

   public AllDefinitionsFinder(boolean verbose) {
      super(verbose);
   }

   public void inDefinitionStmt(DefinitionStmt s) {
      this.allDefs.add(s);
   }

   public List<DefinitionStmt> getAllDefs() {
      return this.allDefs;
   }
}
