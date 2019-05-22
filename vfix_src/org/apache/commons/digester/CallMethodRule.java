package org.apache.commons.digester;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class CallMethodRule extends Rule {
   protected String bodyText;
   private int targetOffset;
   protected String methodName;
   protected int paramCount;
   protected Class[] paramTypes;
   private String[] paramClassNames;
   protected boolean useExactMatch;
   // $FF: synthetic field
   static Class class$java$lang$String;

   /** @deprecated */
   public CallMethodRule(Digester digester, String methodName, int paramCount) {
      this(methodName, paramCount);
   }

   /** @deprecated */
   public CallMethodRule(Digester digester, String methodName, int paramCount, String[] paramTypes) {
      this(methodName, paramCount, paramTypes);
   }

   /** @deprecated */
   public CallMethodRule(Digester digester, String methodName, int paramCount, Class[] paramTypes) {
      this(methodName, paramCount, paramTypes);
   }

   public CallMethodRule(String methodName, int paramCount) {
      this(0, methodName, paramCount);
   }

   public CallMethodRule(int targetOffset, String methodName, int paramCount) {
      this.bodyText = null;
      this.targetOffset = 0;
      this.methodName = null;
      this.paramCount = 0;
      this.paramTypes = null;
      this.paramClassNames = null;
      this.useExactMatch = false;
      this.targetOffset = targetOffset;
      this.methodName = methodName;
      this.paramCount = paramCount;
      if (paramCount == 0) {
         this.paramTypes = new Class[]{class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String};
      } else {
         this.paramTypes = new Class[paramCount];

         for(int i = 0; i < this.paramTypes.length; ++i) {
            this.paramTypes[i] = class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String;
         }
      }

   }

   public CallMethodRule(String methodName) {
      this(0, methodName, 0, (Class[])((Class[])null));
   }

   public CallMethodRule(int targetOffset, String methodName) {
      this(targetOffset, methodName, 0, (Class[])((Class[])null));
   }

   public CallMethodRule(String methodName, int paramCount, String[] paramTypes) {
      this(0, methodName, paramCount, (String[])paramTypes);
   }

   public CallMethodRule(int targetOffset, String methodName, int paramCount, String[] paramTypes) {
      this.bodyText = null;
      this.targetOffset = 0;
      this.methodName = null;
      this.paramCount = 0;
      this.paramTypes = null;
      this.paramClassNames = null;
      this.useExactMatch = false;
      this.targetOffset = targetOffset;
      this.methodName = methodName;
      this.paramCount = paramCount;
      int i;
      if (paramTypes == null) {
         this.paramTypes = new Class[paramCount];

         for(i = 0; i < this.paramTypes.length; ++i) {
            this.paramTypes[i] = "abc".getClass();
         }
      } else {
         this.paramClassNames = new String[paramTypes.length];

         for(i = 0; i < this.paramClassNames.length; ++i) {
            this.paramClassNames[i] = paramTypes[i];
         }
      }

   }

   public CallMethodRule(String methodName, int paramCount, Class[] paramTypes) {
      this(0, methodName, paramCount, (Class[])paramTypes);
   }

   public CallMethodRule(int targetOffset, String methodName, int paramCount, Class[] paramTypes) {
      this.bodyText = null;
      this.targetOffset = 0;
      this.methodName = null;
      this.paramCount = 0;
      this.paramTypes = null;
      this.paramClassNames = null;
      this.useExactMatch = false;
      this.targetOffset = targetOffset;
      this.methodName = methodName;
      this.paramCount = paramCount;
      int i;
      if (paramTypes == null) {
         this.paramTypes = new Class[paramCount];

         for(i = 0; i < this.paramTypes.length; ++i) {
            this.paramTypes[i] = "abc".getClass();
         }
      } else {
         this.paramTypes = new Class[paramTypes.length];

         for(i = 0; i < this.paramTypes.length; ++i) {
            this.paramTypes[i] = paramTypes[i];
         }
      }

   }

   public boolean getUseExactMatch() {
      return this.useExactMatch;
   }

   public void setUseExactMatch(boolean useExactMatch) {
      this.useExactMatch = useExactMatch;
   }

   public void setDigester(Digester digester) {
      super.setDigester(digester);
      if (this.paramClassNames != null) {
         this.paramTypes = new Class[this.paramClassNames.length];

         for(int i = 0; i < this.paramClassNames.length; ++i) {
            try {
               this.paramTypes[i] = digester.getClassLoader().loadClass(this.paramClassNames[i]);
            } catch (ClassNotFoundException var4) {
               digester.getLogger().error("(CallMethodRule) Cannot load class " + this.paramClassNames[i], var4);
               this.paramTypes[i] = null;
            }
         }
      }

   }

   public void begin(Attributes attributes) throws Exception {
      if (this.paramCount > 0) {
         Object[] parameters = new Object[this.paramCount];

         for(int i = 0; i < parameters.length; ++i) {
            parameters[i] = null;
         }

         this.digester.pushParams(parameters);
      }

   }

   public void body(String bodyText) throws Exception {
      if (this.paramCount == 0) {
         this.bodyText = bodyText.trim();
      }

   }

   public void end() throws Exception {
      Object[] parameters = null;
      int i;
      if (this.paramCount > 0) {
         parameters = (Object[])this.digester.popParams();
         if (this.digester.log.isTraceEnabled()) {
            int i = 0;

            for(i = parameters.length; i < i; ++i) {
               this.digester.log.trace("[CallMethodRule](" + i + ")" + parameters[i]);
            }
         }

         if (this.paramCount == 1 && parameters[0] == null) {
            return;
         }
      } else if (this.paramTypes != null && this.paramTypes.length != 0) {
         if (this.bodyText == null) {
            return;
         }

         parameters = new Object[]{this.bodyText};
         if (this.paramTypes.length == 0) {
            this.paramTypes = new Class[1];
            this.paramTypes[0] = "abc".getClass();
         }
      }

      Object[] paramValues = new Object[this.paramTypes.length];

      for(i = 0; i < this.paramTypes.length; ++i) {
         if (parameters[i] != null && (!(parameters[i] instanceof String) || (class$java$lang$String == null ? (class$java$lang$String = class$("java.lang.String")) : class$java$lang$String).isAssignableFrom(this.paramTypes[i]))) {
            paramValues[i] = parameters[i];
         } else {
            paramValues[i] = ConvertUtils.convert((String)parameters[i], this.paramTypes[i]);
         }
      }

      Object target;
      if (this.targetOffset >= 0) {
         target = this.digester.peek(this.targetOffset);
      } else {
         target = this.digester.peek(this.digester.getCount() + this.targetOffset);
      }

      StringBuffer sb;
      if (target == null) {
         sb = new StringBuffer();
         sb.append("[CallMethodRule]{");
         sb.append(this.digester.match);
         sb.append("} Call target is null (");
         sb.append("targetOffset=");
         sb.append(this.targetOffset);
         sb.append(",stackdepth=");
         sb.append(this.digester.getCount());
         sb.append(")");
         throw new SAXException(sb.toString());
      } else {
         if (this.digester.log.isDebugEnabled()) {
            sb = new StringBuffer("[CallMethodRule]{");
            sb.append(this.digester.match);
            sb.append("} Call ");
            sb.append(target.getClass().getName());
            sb.append(".");
            sb.append(this.methodName);
            sb.append("(");

            for(int i = 0; i < paramValues.length; ++i) {
               if (i > 0) {
                  sb.append(",");
               }

               if (paramValues[i] == null) {
                  sb.append("null");
               } else {
                  sb.append(paramValues[i].toString());
               }

               sb.append("/");
               if (this.paramTypes[i] == null) {
                  sb.append("null");
               } else {
                  sb.append(this.paramTypes[i].getName());
               }
            }

            sb.append(")");
            this.digester.log.debug(sb.toString());
         }

         sb = null;
         Object result;
         if (this.useExactMatch) {
            result = MethodUtils.invokeExactMethod(target, this.methodName, paramValues, this.paramTypes);
         } else {
            result = MethodUtils.invokeMethod(target, this.methodName, paramValues, this.paramTypes);
         }

         this.processMethodCallResult(result);
      }
   }

   public void finish() throws Exception {
      this.bodyText = null;
   }

   protected void processMethodCallResult(Object result) {
   }

   public String toString() {
      StringBuffer sb = new StringBuffer("CallMethodRule[");
      sb.append("methodName=");
      sb.append(this.methodName);
      sb.append(", paramCount=");
      sb.append(this.paramCount);
      sb.append(", paramTypes={");
      if (this.paramTypes != null) {
         for(int i = 0; i < this.paramTypes.length; ++i) {
            if (i > 0) {
               sb.append(", ");
            }

            sb.append(this.paramTypes[i].getName());
         }
      }

      sb.append("}");
      sb.append("]");
      return sb.toString();
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
