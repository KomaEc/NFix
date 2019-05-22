package org.codehaus.groovy.transform;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.control.SourceUnit;

public interface ASTTransformation {
   void visit(ASTNode[] var1, SourceUnit var2);
}
