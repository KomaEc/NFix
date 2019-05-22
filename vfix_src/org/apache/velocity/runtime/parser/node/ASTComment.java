package org.apache.velocity.runtime.parser.node;

import java.io.IOException;
import java.io.Writer;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.runtime.parser.Token;

public class ASTComment extends SimpleNode {
   private static final char[] ZILCH = "".toCharArray();
   private char[] carr;

   public ASTComment(int id) {
      super(id);
   }

   public ASTComment(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object init(InternalContextAdapter context, Object data) {
      Token t = this.getFirstToken();
      int loc1 = t.image.indexOf("##");
      int loc2 = t.image.indexOf("#*");
      if (loc1 == -1 && loc2 == -1) {
         this.carr = ZILCH;
      } else {
         this.carr = t.image.substring(0, loc1 == -1 ? loc2 : loc1).toCharArray();
      }

      return data;
   }

   public boolean render(InternalContextAdapter context, Writer writer) throws IOException, MethodInvocationException, ParseErrorException, ResourceNotFoundException {
      if (context.getAllowRendering()) {
         writer.write(this.carr);
      }

      return true;
   }
}
