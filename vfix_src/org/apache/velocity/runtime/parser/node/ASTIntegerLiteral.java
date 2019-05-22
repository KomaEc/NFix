package org.apache.velocity.runtime.parser.node;

import java.math.BigInteger;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;

public class ASTIntegerLiteral extends SimpleNode {
   private Number value = null;

   public ASTIntegerLiteral(int id) {
      super(id);
   }

   public ASTIntegerLiteral(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object init(InternalContextAdapter context, Object data) throws TemplateInitException {
      super.init(context, data);
      String str = this.getFirstToken().image;

      try {
         this.value = new Integer(str);
      } catch (NumberFormatException var7) {
         try {
            this.value = new Long(str);
         } catch (NumberFormatException var6) {
            this.value = new BigInteger(str);
         }
      }

      return data;
   }

   public Object value(InternalContextAdapter context) {
      return this.value;
   }
}
