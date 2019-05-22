package org.apache.commons.digester;

import org.apache.commons.collections.ArrayStack;
import org.xml.sax.Attributes;

public class CallParamRule extends Rule {
   protected String attributeName;
   protected int paramIndex;
   protected boolean fromStack;
   protected int stackIndex;
   protected ArrayStack bodyTextStack;

   /** @deprecated */
   public CallParamRule(Digester digester, int paramIndex) {
      this(paramIndex);
   }

   /** @deprecated */
   public CallParamRule(Digester digester, int paramIndex, String attributeName) {
      this(paramIndex, attributeName);
   }

   public CallParamRule(int paramIndex) {
      this(paramIndex, (String)null);
   }

   public CallParamRule(int paramIndex, String attributeName) {
      this.attributeName = null;
      this.paramIndex = 0;
      this.fromStack = false;
      this.stackIndex = 0;
      this.paramIndex = paramIndex;
      this.attributeName = attributeName;
   }

   public CallParamRule(int paramIndex, boolean fromStack) {
      this.attributeName = null;
      this.paramIndex = 0;
      this.fromStack = false;
      this.stackIndex = 0;
      this.paramIndex = paramIndex;
      this.fromStack = fromStack;
   }

   public CallParamRule(int paramIndex, int stackIndex) {
      this.attributeName = null;
      this.paramIndex = 0;
      this.fromStack = false;
      this.stackIndex = 0;
      this.paramIndex = paramIndex;
      this.fromStack = true;
      this.stackIndex = stackIndex;
   }

   public void begin(Attributes attributes) throws Exception {
      Object param = null;
      if (this.attributeName != null) {
         param = attributes.getValue(this.attributeName);
      } else if (this.fromStack) {
         param = this.digester.peek(this.stackIndex);
         if (this.digester.log.isDebugEnabled()) {
            StringBuffer sb = new StringBuffer("[CallParamRule]{");
            sb.append(this.digester.match);
            sb.append("} Save from stack; from stack?").append(this.fromStack);
            sb.append("; object=").append(param);
            this.digester.log.debug(sb.toString());
         }
      }

      if (param != null) {
         Object[] parameters = (Object[])this.digester.peekParams();
         parameters[this.paramIndex] = param;
      }

   }

   public void body(String bodyText) throws Exception {
      if (this.attributeName == null && !this.fromStack) {
         if (this.bodyTextStack == null) {
            this.bodyTextStack = new ArrayStack();
         }

         this.bodyTextStack.push(bodyText.trim());
      }

   }

   public void end(String namespace, String name) {
      if (this.bodyTextStack != null && !this.bodyTextStack.empty()) {
         Object[] parameters = (Object[])this.digester.peekParams();
         parameters[this.paramIndex] = this.bodyTextStack.pop();
      }

   }

   public String toString() {
      StringBuffer sb = new StringBuffer("CallParamRule[");
      sb.append("paramIndex=");
      sb.append(this.paramIndex);
      sb.append(", attributeName=");
      sb.append(this.attributeName);
      sb.append(", from stack=");
      sb.append(this.fromStack);
      sb.append("]");
      return sb.toString();
   }
}
