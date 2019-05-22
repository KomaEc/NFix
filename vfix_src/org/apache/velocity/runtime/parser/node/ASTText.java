package org.apache.velocity.runtime.parser.node;

import java.io.IOException;
import java.io.Writer;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.runtime.parser.Token;

public class ASTText extends SimpleNode {
   private char[] ctext;

   public ASTText(int id) {
      super(id);
   }

   public ASTText(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object init(InternalContextAdapter context, Object data) throws TemplateInitException {
      Token t = this.getFirstToken();
      String text = NodeUtils.tokenLiteral(t);
      this.ctext = text.toCharArray();
      return data;
   }

   public boolean render(InternalContextAdapter context, Writer writer) throws IOException {
      if (context.getAllowRendering()) {
         writer.write(this.ctext);
      }

      return true;
   }
}
