package org.codehaus.groovy.syntax;

import groovy.lang.GroovyRuntimeException;
import org.codehaus.groovy.ast.ASTNode;

public class RuntimeParserException extends GroovyRuntimeException {
   public RuntimeParserException(String message, ASTNode node) {
      super(message + "\n", node);
   }

   public void throwParserException() throws SyntaxException {
      throw new SyntaxException(this.getMessage(), this.getNode().getLineNumber(), this.getNode().getColumnNumber());
   }
}
