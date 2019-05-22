package org.apache.velocity.runtime.directive;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.context.InternalContextAdapterImpl;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.Log;
import org.apache.velocity.runtime.parser.ParserTreeConstants;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.SimpleNode;

public class VMProxyArg {
   private static final int GENERALSTATIC = -1;
   private int type = 0;
   private SimpleNode nodeTree = null;
   private Object staticObject = null;
   private int numTreeChildren = 0;
   private String contextReference = null;
   private String callerReference = null;
   private String singleLevelRef = null;
   private boolean constant = false;
   private RuntimeServices rsvc = null;
   private Log log = null;

   public VMProxyArg(RuntimeServices rs, String contextRef, String callerRef, int t) {
      this.rsvc = rs;
      this.log = this.rsvc.getLog();
      this.contextReference = contextRef;
      this.callerReference = callerRef;
      this.type = t;
      this.setup();
      if (this.nodeTree != null) {
         this.numTreeChildren = this.nodeTree.jjtGetNumChildren();
      }

      if (this.type == 16 && this.numTreeChildren == 0) {
         this.singleLevelRef = ((ASTReference)this.nodeTree).getRootString();
      }

   }

   public boolean isConstant() {
      return this.constant;
   }

   public Object setObject(InternalContextAdapter context, Object o) {
      if (this.type == 16) {
         if (this.numTreeChildren > 0) {
            try {
               ((ASTReference)this.nodeTree).setValue(context, o);
            } catch (MethodInvocationException var4) {
               this.log.error("VMProxyArg.getObject() : method invocation error setting value", var4);
            }
         } else {
            context.put(this.singleLevelRef, o);
         }
      } else {
         this.type = -1;
         this.staticObject = o;
         this.log.error("VMProxyArg.setObject() : Programmer error : I am a constant!  No setting! : " + this.contextReference + " / " + this.callerReference);
      }

      return null;
   }

   public Object getObject(InternalContextAdapter context) throws MethodInvocationException {
      try {
         Object retObject = null;
         if (this.type == 16) {
            if (this.numTreeChildren == 0) {
               retObject = context.get(this.singleLevelRef);
            } else {
               retObject = this.nodeTree.execute((Object)null, context);
            }
         } else if (this.type == 12) {
            retObject = this.nodeTree.value(context);
         } else if (this.type == 13) {
            retObject = this.nodeTree.value(context);
         } else if (this.type == 14) {
            retObject = this.nodeTree.value(context);
         } else if (this.type == 17) {
            retObject = this.staticObject;
         } else if (this.type == 18) {
            retObject = this.staticObject;
         } else if (this.type == 7) {
            retObject = this.nodeTree.value(context);
         } else if (this.type == 6) {
            retObject = this.staticObject;
         } else if (this.type == 5) {
            retObject = this.staticObject;
         } else if (this.type == 19) {
            try {
               StringWriter writer = new StringWriter();
               this.nodeTree.render(context, writer);
               retObject = writer;
            } catch (RuntimeException var4) {
               throw var4;
            } catch (Exception var5) {
               this.log.error("VMProxyArg.getObject() : error rendering reference", var5);
            }
         } else if (this.type == -1) {
            retObject = this.staticObject;
         } else {
            this.log.error("Unsupported VM arg type : VM arg = " + this.callerReference + " type = " + this.type + "( VMProxyArg.getObject() )");
         }

         return retObject;
      } catch (MethodInvocationException var6) {
         this.log.error("VMProxyArg.getObject() : method invocation error getting value", var6);
         throw var6;
      }
   }

   private void setup() {
      switch(this.type) {
      case 5:
         this.constant = true;
         this.staticObject = new Double(this.callerReference);
         break;
      case 6:
         this.constant = true;
         this.staticObject = new Integer(this.callerReference);
         break;
      case 7:
      case 12:
      case 13:
      case 14:
      case 16:
      case 19:
         this.constant = false;

         try {
            String buff = "#include(" + this.callerReference + " ) ";
            BufferedReader br = new BufferedReader(new StringReader(buff));
            this.nodeTree = this.rsvc.parse(br, "VMProxyArg:" + this.callerReference, true);
            this.nodeTree = (SimpleNode)this.nodeTree.jjtGetChild(0).jjtGetChild(0);
            if (this.nodeTree != null) {
               if (this.nodeTree.getType() != this.type) {
                  this.log.error("VMProxyArg.setup() : programmer error : type doesn't match node type.");
               }

               InternalContextAdapter ica = new InternalContextAdapterImpl(new VelocityContext());
               ica.pushCurrentTemplateName("VMProxyArg : " + ParserTreeConstants.jjtNodeName[this.type]);
               this.nodeTree.init(ica, this.rsvc);
            }
         } catch (RuntimeException var4) {
            throw var4;
         } catch (Exception var5) {
            this.log.error("VMProxyArg.setup() : exception " + this.callerReference, var5);
         }
         break;
      case 8:
      case 10:
      case 11:
      case 15:
      default:
         this.log.error("VMProxyArg.setup() : unsupported type : " + this.callerReference);
         break;
      case 9:
         this.log.error("Unsupported arg type : " + this.callerReference + " You most likely intended to call a VM with a string literal, so enclose with ' or \" characters. (VMProxyArg.setup())");
         this.constant = true;
         this.staticObject = this.callerReference;
         break;
      case 17:
         this.constant = true;
         this.staticObject = Boolean.TRUE;
         break;
      case 18:
         this.constant = true;
         this.staticObject = Boolean.FALSE;
      }

   }

   public VMProxyArg(VMProxyArg model, InternalContextAdapter c) {
      this.contextReference = model.getContextReference();
      this.callerReference = model.getCallerReference();
      this.nodeTree = model.getNodeTree();
      this.staticObject = model.getStaticObject();
      this.type = model.getType();
      if (this.nodeTree != null) {
         this.numTreeChildren = this.nodeTree.jjtGetNumChildren();
      }

      if (this.type == 16 && this.numTreeChildren == 0) {
         this.singleLevelRef = ((ASTReference)this.nodeTree).getRootString();
      }

   }

   public String getCallerReference() {
      return this.callerReference;
   }

   public String getContextReference() {
      return this.contextReference;
   }

   public SimpleNode getNodeTree() {
      return this.nodeTree;
   }

   public Object getStaticObject() {
      return this.staticObject;
   }

   public int getType() {
      return this.type;
   }
}
