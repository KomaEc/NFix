package org.codehaus.groovy.antlr.treewalker;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.antlr.GroovySourceAST;

public class NodeCollector extends VisitorAdapter {
   private List nodes = new ArrayList();

   public List getNodes() {
      return this.nodes;
   }

   public void visitDefault(GroovySourceAST t, int visit) {
      if (visit == 1) {
         this.nodes.add(t);
      }

   }
}
