package org.apache.commons.digester;

import org.apache.commons.beanutils.MethodUtils;

public class SetNextRule extends Rule {
   protected String methodName;
   protected String paramType;
   protected boolean useExactMatch;

   /** @deprecated */
   public SetNextRule(Digester digester, String methodName) {
      this(methodName);
   }

   /** @deprecated */
   public SetNextRule(Digester digester, String methodName, String paramType) {
      this(methodName, paramType);
   }

   public SetNextRule(String methodName) {
      this((String)methodName, (String)null);
   }

   public SetNextRule(String methodName, String paramType) {
      this.methodName = null;
      this.paramType = null;
      this.useExactMatch = false;
      this.methodName = methodName;
      this.paramType = paramType;
   }

   public boolean isExactMatch() {
      return this.useExactMatch;
   }

   public void setExactMatch(boolean useExactMatch) {
      this.useExactMatch = useExactMatch;
   }

   public void end() throws Exception {
      Object child = this.digester.peek(0);
      Object parent = this.digester.peek(1);
      if (this.digester.log.isDebugEnabled()) {
         if (parent == null) {
            this.digester.log.debug("[SetNextRule]{" + this.digester.match + "} Call [NULL PARENT]." + this.methodName + "(" + child + ")");
         } else {
            this.digester.log.debug("[SetNextRule]{" + this.digester.match + "} Call " + parent.getClass().getName() + "." + this.methodName + "(" + child + ")");
         }
      }

      Class[] paramTypes = new Class[1];
      if (this.paramType != null) {
         paramTypes[0] = this.digester.getClassLoader().loadClass(this.paramType);
      } else {
         paramTypes[0] = child.getClass();
      }

      if (this.useExactMatch) {
         MethodUtils.invokeExactMethod(parent, this.methodName, new Object[]{child}, paramTypes);
      } else {
         MethodUtils.invokeMethod(parent, this.methodName, new Object[]{child}, paramTypes);
      }

   }

   public String toString() {
      StringBuffer sb = new StringBuffer("SetNextRule[");
      sb.append("methodName=");
      sb.append(this.methodName);
      sb.append(", paramType=");
      sb.append(this.paramType);
      sb.append("]");
      return sb.toString();
   }
}
