package org.apache.velocity.runtime.parser.node;

import java.math.BigDecimal;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;

public class ASTFloatingPointLiteral extends SimpleNode {
   private Number value = null;

   public ASTFloatingPointLiteral(int id) {
      super(id);
   }

   public ASTFloatingPointLiteral(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object init(InternalContextAdapter context, Object data) throws TemplateInitException {
      super.init(context, data);
      String str = this.getFirstToken().image;

      try {
         this.value = new Double(str);
      } catch (NumberFormatException var5) {
         this.value = new BigDecimal(str);
      }

      return data;
   }

   public Object value(InternalContextAdapter context) {
      return this.value;
   }
}
