package org.codehaus.groovy.antlr.treewalker;

import groovyjarjarantlr.collections.AST;
import java.util.Iterator;
import java.util.List;
import org.codehaus.groovy.antlr.AntlrASTProcessor;
import org.codehaus.groovy.antlr.GroovySourceAST;

public class FlatNodeListTraversal extends TraversalHelper {
   public FlatNodeListTraversal(Visitor visitor) {
      super(visitor);
   }

   public AST process(AST t) {
      GroovySourceAST node = (GroovySourceAST)t;
      NodeCollector collector = new NodeCollector();
      AntlrASTProcessor internalTraversal = new PreOrderTraversal(collector);
      internalTraversal.process(t);
      List listOfAllNodesInThisAST = collector.getNodes();
      this.setUp(node);
      Iterator itr = listOfAllNodesInThisAST.iterator();

      while(itr.hasNext()) {
         GroovySourceAST currentNode = (GroovySourceAST)itr.next();
         this.accept(currentNode);
      }

      this.tearDown(node);
      return null;
   }

   protected void accept(GroovySourceAST currentNode) {
      this.openingVisit(currentNode);
      this.closingVisit(currentNode);
   }
}
