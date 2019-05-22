package org.codehaus.groovy.transform.powerassert;

import org.codehaus.groovy.GroovyBugError;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.ModuleNode;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

@GroovyASTTransformation(
   phase = CompilePhase.CANONICALIZATION
)
public class AssertionTransformation implements ASTTransformation {
   public void visit(ASTNode[] nodes, SourceUnit sourceUnit) {
      if (!(nodes[0] instanceof ModuleNode)) {
         throw new GroovyBugError("tried to apply AssertionTransformation to " + nodes[0]);
      }
   }
}
