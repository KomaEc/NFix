package org.apache.velocity.runtime.directive;

import java.io.IOException;
import java.io.Writer;
import org.apache.velocity.app.event.EventHandlerUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.resource.ContentResource;

public class Include extends InputBase {
   private String outputMsgStart = "";
   private String outputMsgEnd = "";

   public String getName() {
      return "include";
   }

   public int getType() {
      return 2;
   }

   public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
      super.init(rs, context, node);
      this.outputMsgStart = this.rsvc.getString("directive.include.output.errormsg.start");
      this.outputMsgStart = this.outputMsgStart + " ";
      this.outputMsgEnd = this.rsvc.getString("directive.include.output.errormsg.end");
      this.outputMsgEnd = " " + this.outputMsgEnd;
   }

   public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, MethodInvocationException, ResourceNotFoundException {
      int argCount = node.jjtGetNumChildren();

      for(int i = 0; i < argCount; ++i) {
         Node n = node.jjtGetChild(i);
         if (n.getType() != 7 && n.getType() != 16) {
            this.rsvc.getLog().error("#include() invalid argument type: " + n.toString());
            this.outputErrorToStream(writer, "error with arg " + i + " please see log.");
         } else if (!this.renderOutput(n, context, writer)) {
            this.outputErrorToStream(writer, "error with arg " + i + " please see log.");
         }
      }

      return true;
   }

   private boolean renderOutput(Node node, InternalContextAdapter context, Writer writer) throws IOException, MethodInvocationException, ResourceNotFoundException {
      if (node == null) {
         this.rsvc.getLog().error("#include() null argument");
         return false;
      } else {
         Object value = node.value(context);
         if (value == null) {
            this.rsvc.getLog().error("#include()  null argument");
            return false;
         } else {
            String sourcearg = value.toString();
            String arg = EventHandlerUtil.includeEvent(this.rsvc, context, sourcearg, context.getCurrentTemplateName(), this.getName());
            boolean blockinput = false;
            if (arg == null) {
               blockinput = true;
            }

            ContentResource resource = null;

            try {
               if (!blockinput) {
                  resource = this.rsvc.getContent(arg, this.getInputEncoding(context));
               }
            } catch (ResourceNotFoundException var10) {
               this.rsvc.getLog().error("#include(): cannot find resource '" + arg + "', called from template " + context.getCurrentTemplateName() + " at (" + this.getLine() + ", " + this.getColumn() + ")");
               throw var10;
            } catch (RuntimeException var11) {
               throw var11;
            } catch (Exception var12) {
               this.rsvc.getLog().error("#include(): arg = '" + arg + "', called from template " + context.getCurrentTemplateName() + " at (" + this.getLine() + ", " + this.getColumn() + ')', var12);
            }

            if (blockinput) {
               return true;
            } else if (resource == null) {
               return false;
            } else {
               writer.write((String)resource.getData());
               return true;
            }
         }
      }
   }

   private void outputErrorToStream(Writer writer, String msg) throws IOException {
      if (this.outputMsgStart != null && this.outputMsgEnd != null) {
         writer.write(this.outputMsgStart);
         writer.write(msg);
         writer.write(this.outputMsgEnd);
      }

   }
}
