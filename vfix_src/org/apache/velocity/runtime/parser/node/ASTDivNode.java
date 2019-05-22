package org.apache.velocity.runtime.parser.node;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.util.TemplateNumber;

public class ASTDivNode extends SimpleNode {
   public ASTDivNode(int id) {
      super(id);
   }

   public ASTDivNode(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object value(InternalContextAdapter context) throws MethodInvocationException {
      Object left = this.jjtGetChild(0).value(context);
      Object right = this.jjtGetChild(1).value(context);
      if (left != null && right != null) {
         if (left instanceof TemplateNumber) {
            left = ((TemplateNumber)left).getAsNumber();
         }

         if (right instanceof TemplateNumber) {
            right = ((TemplateNumber)right).getAsNumber();
         }

         if (left instanceof Number && right instanceof Number) {
            if (MathUtils.isZero((Number)right)) {
               this.log.error("Right side of division operation is zero. Must be non-zero. " + context.getCurrentTemplateName() + " [line " + this.getLine() + ", column " + this.getColumn() + "]");
               return null;
            } else {
               return MathUtils.divide((Number)left, (Number)right);
            }
         } else {
            this.log.error((!(left instanceof Number) ? "Left" : "Right") + " side of division operation is not a number. " + context.getCurrentTemplateName() + " [line " + this.getLine() + ", column " + this.getColumn() + "]");
            return null;
         }
      } else {
         this.log.error((left == null ? "Left" : "Right") + " side (" + this.jjtGetChild(left == null ? 0 : 1).literal() + ") of division operation has null value." + " Operation not possible. " + context.getCurrentTemplateName() + " [line " + this.getLine() + ", column " + this.getColumn() + "]");
         return null;
      }
   }
}
