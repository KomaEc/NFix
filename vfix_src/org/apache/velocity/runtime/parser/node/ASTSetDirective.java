package org.apache.velocity.runtime.parser.node;

import java.io.IOException;
import java.io.Writer;
import org.apache.velocity.app.event.EventHandlerUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.util.introspection.Info;

public class ASTSetDirective extends SimpleNode {
   private String leftReference = "";
   private Node right = null;
   private ASTReference left = null;
   boolean logOnNull = false;
   protected Info uberInfo;

   public ASTSetDirective(int id) {
      super(id);
   }

   public ASTSetDirective(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object init(InternalContextAdapter context, Object data) throws TemplateInitException {
      super.init(context, data);
      this.uberInfo = new Info(context.getCurrentTemplateName(), this.getLine(), this.getColumn());
      this.right = this.getRightHandSide();
      this.left = this.getLeftHandSide();
      this.logOnNull = this.rsvc.getBoolean("runtime.log.invalid.references", true);
      this.leftReference = this.left.getFirstToken().image.substring(1);
      return data;
   }

   public boolean render(InternalContextAdapter context, Writer writer) throws IOException, MethodInvocationException {
      Object value = this.right.value(context);
      String rightReference;
      if (!this.rsvc.getBoolean("directive.set.null.allowed", false) && value == null) {
         if (this.logOnNull) {
            boolean doit = EventHandlerUtil.shouldLogOnNullSet(this.rsvc, context, this.left.literal(), this.right.literal());
            if (doit && this.log.isInfoEnabled()) {
               this.log.info("RHS of #set statement is null. Context will not be modified. " + context.getCurrentTemplateName() + " [line " + this.getLine() + ", column " + this.getColumn() + "]");
            }
         }

         rightReference = null;
         if (this.right instanceof ASTExpression) {
            rightReference = ((ASTExpression)this.right).getLastToken().image;
         }

         EventHandlerUtil.invalidSetMethod(this.rsvc, context, this.leftReference, rightReference, this.uberInfo);
         return false;
      } else if (value == null) {
         rightReference = null;
         if (this.right instanceof ASTExpression) {
            rightReference = ((ASTExpression)this.right).getLastToken().image;
         }

         EventHandlerUtil.invalidSetMethod(this.rsvc, context, this.leftReference, rightReference, this.uberInfo);
         context.remove(this.leftReference);
         return false;
      } else {
         if (this.left.jjtGetNumChildren() == 0) {
            context.put(this.leftReference, value);
         } else {
            this.left.setValue(context, value);
         }

         return true;
      }
   }

   private ASTReference getLeftHandSide() {
      return (ASTReference)this.jjtGetChild(0);
   }

   private Node getRightHandSide() {
      return this.jjtGetChild(1);
   }
}
