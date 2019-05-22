package org.apache.velocity.runtime.directive;

import java.io.IOException;
import java.io.Writer;
import org.apache.velocity.Template;
import org.apache.velocity.app.event.EventHandlerUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

public class Parse extends InputBase {
   public String getName() {
      return "parse";
   }

   public int getType() {
      return 2;
   }

   public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
      if (!context.getAllowRendering()) {
         return true;
      } else if (node.jjtGetChild(0) == null) {
         this.rsvc.getLog().error("#parse() null argument");
         return false;
      } else {
         Object value = node.jjtGetChild(0).value(context);
         if (value == null) {
            this.rsvc.getLog().error("#parse() null argument");
            return false;
         } else {
            String sourcearg = value.toString();
            String arg = EventHandlerUtil.includeEvent(this.rsvc, context, sourcearg, context.getCurrentTemplateName(), this.getName());
            boolean blockinput = false;
            if (arg == null) {
               blockinput = true;
            }

            Object[] templateStack = context.getTemplateNameStack();
            if (templateStack.length >= this.rsvc.getInt("directive.parse.max.depth", 20)) {
               StringBuffer path = new StringBuffer();

               for(int i = 0; i < templateStack.length; ++i) {
                  path.append(" > " + templateStack[i]);
               }

               this.rsvc.getLog().error("Max recursion depth reached (" + templateStack.length + ')' + " File stack:" + path);
               return false;
            } else {
               Template t = null;

               try {
                  if (!blockinput) {
                     t = this.rsvc.getTemplate(arg, this.getInputEncoding(context));
                  }
               } catch (ResourceNotFoundException var21) {
                  this.rsvc.getLog().error("#parse(): cannot find template '" + arg + "', called from template " + context.getCurrentTemplateName() + " at (" + this.getLine() + ", " + this.getColumn() + ")");
                  throw var21;
               } catch (ParseErrorException var22) {
                  this.rsvc.getLog().error("#parse(): syntax error in #parse()-ed template '" + arg + "', called from template " + context.getCurrentTemplateName() + " at (" + this.getLine() + ", " + this.getColumn() + ")");
                  throw var22;
               } catch (RuntimeException var23) {
                  throw var23;
               } catch (Exception var24) {
                  this.rsvc.getLog().error("#parse() : arg = " + arg + '.', var24);
                  return false;
               }

               boolean var11;
               try {
                  if (!blockinput) {
                     context.pushCurrentTemplateName(arg);
                     ((SimpleNode)t.getData()).render(context, writer);
                  }

                  return true;
               } catch (MethodInvocationException var25) {
                  throw var25;
               } catch (RuntimeException var26) {
                  throw var26;
               } catch (Exception var27) {
                  this.rsvc.getLog().error("Exception rendering #parse(" + arg + ')', var27);
                  var11 = false;
               } finally {
                  if (!blockinput) {
                     context.popCurrentTemplateName();
                  }

               }

               return var11;
            }
         }
      }
   }
}
