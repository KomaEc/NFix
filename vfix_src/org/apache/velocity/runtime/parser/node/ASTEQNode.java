package org.apache.velocity.runtime.parser.node;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.util.TemplateNumber;

public class ASTEQNode extends SimpleNode {
   public ASTEQNode(int id) {
      super(id);
   }

   public ASTEQNode(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public boolean evaluate(InternalContextAdapter context) throws MethodInvocationException {
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
            return MathUtils.compare((Number)left, (Number)right) == 0;
         } else if (!left.getClass().isAssignableFrom(right.getClass()) && !right.getClass().isAssignableFrom(left.getClass())) {
            if (left.toString() != null && right.toString() != null) {
               return left.toString().equals(right.toString());
            } else {
               boolean culprit = left.toString() == null;
               this.log.error((culprit ? "Left" : "Right") + " string side " + "String representation (" + this.jjtGetChild(culprit ? 0 : 1).literal() + ") of '!=' operation has null value." + " Operation not possible. " + context.getCurrentTemplateName() + " [line " + this.getLine() + ", column " + this.getColumn() + "]");
               return false;
            }
         } else {
            return left.equals(right);
         }
      } else {
         this.log.error((left == null ? "Left" : "Right") + " side (" + this.jjtGetChild(left == null ? 0 : 1).literal() + ") of '==' operation " + "has null value. " + "If a reference, it may not be in the context." + " Operation not possible. " + context.getCurrentTemplateName() + " [line " + this.getLine() + ", column " + this.getColumn() + "]");
         return false;
      }
   }

   public Object value(InternalContextAdapter context) throws MethodInvocationException {
      return this.evaluate(context) ? Boolean.TRUE : Boolean.FALSE;
   }
}
