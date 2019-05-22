package org.apache.velocity.runtime.directive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.HashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.context.VMContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.Token;
import org.apache.velocity.runtime.parser.node.ASTDirective;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.runtime.visitor.VMReferenceMungeVisitor;

public class VelocimacroProxy extends Directive {
   private String macroName = "";
   private String macroBody = "";
   private String[] argArray = null;
   private SimpleNode nodeTree = null;
   private int numMacroArgs = 0;
   private String namespace = "";
   private boolean init = false;
   private String[] callingArgs;
   private int[] callingArgTypes;
   private HashMap proxyArgHash = new HashMap();
   private boolean strictArguments;

   public String getName() {
      return this.macroName;
   }

   public int getType() {
      return 2;
   }

   public void setName(String name) {
      this.macroName = name;
   }

   public void setArgArray(String[] arr) {
      this.argArray = arr;
      this.numMacroArgs = this.argArray.length - 1;
   }

   public void setNodeTree(SimpleNode tree) {
      this.nodeTree = tree;
   }

   public int getNumArgs() {
      return this.numMacroArgs;
   }

   public void setMacrobody(String mb) {
      this.macroBody = mb;
   }

   public void setNamespace(String ns) {
      this.namespace = ns;
   }

   public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, MethodInvocationException {
      try {
         if (this.nodeTree != null) {
            if (!this.init) {
               this.nodeTree.init(context, this.rsvc);
               this.init = true;
            }

            VMContext vmc = new VMContext(context, this.rsvc);

            for(int i = 1; i < this.argArray.length; ++i) {
               VMProxyArg arg = (VMProxyArg)this.proxyArgHash.get(this.argArray[i]);
               vmc.addVMProxyArg(arg);
            }

            this.nodeTree.render(vmc, writer);
         } else {
            this.rsvc.getLog().error("VM error " + this.macroName + ". Null AST");
         }
      } catch (MethodInvocationException var7) {
         throw var7;
      } catch (RuntimeException var8) {
         throw var8;
      } catch (Exception var9) {
         this.rsvc.getLog().error("VelocimacroProxy.render() : exception VM = #" + this.macroName + "()", var9);
      }

      return true;
   }

   public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
      super.init(rs, context, node);
      this.strictArguments = rs.getConfiguration().getBoolean("velocimacro.arguments.strict", false);
      int i = node.jjtGetNumChildren();
      if (this.getNumArgs() != i) {
         for(Node parent = node.jjtGetParent(); parent != null; parent = parent.jjtGetParent()) {
            if (parent instanceof ASTDirective && StringUtils.equals(((ASTDirective)parent).getDirectiveName(), "macro")) {
               return;
            }
         }

         String errormsg = "VM #" + this.macroName + ": error : too " + (this.getNumArgs() > i ? "few" : "many") + " arguments to macro. Wanted " + this.getNumArgs() + " got " + i;
         if (this.strictArguments) {
            throw new TemplateInitException(errormsg, context.getCurrentTemplateName(), 0, 0);
         } else {
            this.rsvc.getLog().error(errormsg);
         }
      } else {
         this.callingArgs = this.getArgArray(node);
         this.setupMacro(this.callingArgs, this.callingArgTypes);
      }
   }

   public boolean setupMacro(String[] callArgs, int[] callArgTypes) {
      this.setupProxyArgs(callArgs, callArgTypes);
      this.parseTree(callArgs);
      return true;
   }

   private void parseTree(String[] callArgs) {
      try {
         BufferedReader br = new BufferedReader(new StringReader(this.macroBody));
         this.nodeTree = this.rsvc.parse(br, this.namespace, false);
         HashMap hm = new HashMap();

         for(int i = 1; i < this.argArray.length; ++i) {
            String arg = callArgs[i - 1];
            if (arg.charAt(0) == '$') {
               hm.put(this.argArray[i], arg);
            }
         }

         VMReferenceMungeVisitor v = new VMReferenceMungeVisitor(hm);
         this.nodeTree.jjtAccept(v, (Object)null);
      } catch (RuntimeException var6) {
         throw var6;
      } catch (Exception var7) {
         this.rsvc.getLog().error("VelocimacroManager.parseTree() : exception " + this.macroName, var7);
      }

   }

   private void setupProxyArgs(String[] callArgs, int[] callArgTypes) {
      for(int i = 1; i < this.argArray.length; ++i) {
         VMProxyArg arg = new VMProxyArg(this.rsvc, this.argArray[i], callArgs[i - 1], callArgTypes[i - 1]);
         this.proxyArgHash.put(this.argArray[i], arg);
      }

   }

   private String[] getArgArray(Node node) {
      int numArgs = node.jjtGetNumChildren();
      String[] args = new String[numArgs];
      this.callingArgTypes = new int[numArgs];
      int i = 0;
      Token t = null;

      for(Token tLast = null; i < numArgs; ++i) {
         args[i] = "";
         this.callingArgTypes[i] = node.jjtGetChild(i).getType();
         t = node.jjtGetChild(i).getFirstToken();

         for(tLast = node.jjtGetChild(i).getLastToken(); t != tLast; t = t.next) {
            args[i] = args[i] + t.image;
         }

         args[i] = args[i] + t.image;
      }

      return args;
   }
}
