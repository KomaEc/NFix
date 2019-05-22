package org.apache.velocity.runtime.parser.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;

public class ASTStringLiteral extends SimpleNode {
   private boolean interpolate = true;
   private SimpleNode nodeTree = null;
   private String image = "";
   private String interpolateimage = "";
   private boolean containsLineComment;

   public ASTStringLiteral(int id) {
      super(id);
   }

   public ASTStringLiteral(Parser p, int id) {
      super(p, id);
   }

   public Object init(InternalContextAdapter context, Object data) throws TemplateInitException {
      super.init(context, data);
      this.interpolate = this.rsvc.getBoolean("runtime.interpolate.string.literals", true) && this.getFirstToken().image.startsWith("\"") && (this.getFirstToken().image.indexOf(36) != -1 || this.getFirstToken().image.indexOf(35) != -1);
      this.image = this.getFirstToken().image.substring(1, this.getFirstToken().image.length() - 1);
      this.containsLineComment = this.image.indexOf("##") != -1;
      if (!this.containsLineComment) {
         this.interpolateimage = this.image + " ";
      } else {
         this.interpolateimage = this.image;
      }

      if (this.interpolate) {
         BufferedReader br = new BufferedReader(new StringReader(this.interpolateimage));

         try {
            this.nodeTree = this.rsvc.parse(br, context != null ? context.getCurrentTemplateName() : "StringLiteral", false);
         } catch (ParseException var5) {
            throw new TemplateInitException("Problem parsing String literal.", var5, context != null ? context.getCurrentTemplateName() : "StringLiteral", this.getColumn(), this.getLine());
         }

         this.nodeTree.init(context, this.rsvc);
      }

      return data;
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object value(InternalContextAdapter context) {
      if (this.interpolate) {
         try {
            StringWriter writer = new StringWriter();
            this.nodeTree.render(context, writer);
            String ret = writer.toString();
            if (!this.containsLineComment && ret.length() > 0) {
               return ret.substring(0, ret.length() - 1);
            }

            return ret;
         } catch (ParseErrorException var4) {
            this.log.error("Error in interpolating string literal", var4);
         } catch (MethodInvocationException var5) {
            this.log.error("Error in interpolating string literal", var5);
         } catch (ResourceNotFoundException var6) {
            this.log.error("Error in interpolating string literal", var6);
         } catch (RuntimeException var7) {
            throw var7;
         } catch (IOException var8) {
            this.log.error("Error in interpolating string literal", var8);
         }
      }

      return this.image;
   }
}
