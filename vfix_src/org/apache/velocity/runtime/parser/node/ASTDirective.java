package org.apache.velocity.runtime.parser.node;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.util.ExceptionUtils;

public class ASTDirective extends SimpleNode {
   private Directive directive = null;
   private String directiveName = "";
   private boolean isDirective;

   public ASTDirective(int id) {
      super(id);
   }

   public ASTDirective(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object init(InternalContextAdapter context, Object data) throws TemplateInitException {
      super.init(context, data);
      if (this.parser.isDirective(this.directiveName)) {
         this.isDirective = true;

         try {
            this.directive = (Directive)this.parser.getDirective(this.directiveName).getClass().newInstance();
         } catch (InstantiationException var5) {
            throw ExceptionUtils.createRuntimeException("Couldn't initialize directive of class " + this.parser.getDirective(this.directiveName).getClass().getName(), var5);
         } catch (IllegalAccessException var6) {
            throw ExceptionUtils.createRuntimeException("Couldn't initialize directive of class " + this.parser.getDirective(this.directiveName).getClass().getName(), var6);
         }

         this.directive.init(this.rsvc, context, this);
         this.directive.setLocation(this.getLine(), this.getColumn());
      } else if (this.rsvc.isVelocimacro(this.directiveName, context.getCurrentTemplateName())) {
         this.isDirective = true;
         this.directive = this.rsvc.getVelocimacro(this.directiveName, context.getCurrentTemplateName());

         try {
            this.directive.init(this.rsvc, context, this);
         } catch (TemplateInitException var4) {
            throw new TemplateInitException(var4.getMessage(), (ParseException)var4.getWrappedThrowable(), var4.getTemplateName(), var4.getColumnNumber() + this.getColumn(), var4.getLineNumber() + this.getLine());
         }

         this.directive.setLocation(this.getLine(), this.getColumn());
      } else {
         this.isDirective = false;
      }

      return data;
   }

   public boolean render(InternalContextAdapter context, Writer writer) throws IOException, MethodInvocationException, ResourceNotFoundException, ParseErrorException {
      if (this.isDirective) {
         this.directive.render(context, writer, this);
      } else if (context.getAllowRendering()) {
         writer.write("#");
         writer.write(this.directiveName);
      }

      return true;
   }

   public void setDirectiveName(String str) {
      this.directiveName = str;
   }

   public String getDirectiveName() {
      return this.directiveName;
   }

   public String toString() {
      return (new ToStringBuilder(this)).appendSuper(super.toString()).append("directiveName", (Object)this.getDirectiveName()).toString();
   }
}
