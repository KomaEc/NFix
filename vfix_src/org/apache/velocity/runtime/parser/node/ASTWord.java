package org.apache.velocity.runtime.parser.node;

import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;

public class ASTWord extends SimpleNode {
   public ASTWord(int id) {
      super(id);
   }

   public ASTWord(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }
}
