package org.apache.velocity.runtime.parser.node;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang.ArrayUtils;
import org.apache.velocity.app.event.EventHandlerUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.util.introspection.Info;
import org.apache.velocity.util.introspection.IntrospectionCacheData;
import org.apache.velocity.util.introspection.VelMethod;

public class ASTMethod extends SimpleNode {
   private String methodName = "";
   private int paramCount = 0;
   protected Info uberInfo;

   public ASTMethod(int id) {
      super(id);
   }

   public ASTMethod(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object init(InternalContextAdapter context, Object data) throws TemplateInitException {
      super.init(context, data);
      this.uberInfo = new Info(context.getCurrentTemplateName(), this.getLine(), this.getColumn());
      this.methodName = this.getFirstToken().image;
      this.paramCount = this.jjtGetNumChildren() - 1;
      return data;
   }

   public Object execute(Object o, InternalContextAdapter context) throws MethodInvocationException {
      VelMethod method = null;
      Object[] params = new Object[this.paramCount];

      try {
         Class[] paramClasses = this.paramCount > 0 ? new Class[this.paramCount] : ArrayUtils.EMPTY_CLASS_ARRAY;

         for(int j = 0; j < this.paramCount; ++j) {
            params[j] = this.jjtGetChild(j + 1).value(context);
            if (params[j] != null) {
               paramClasses[j] = params[j].getClass();
            }
         }

         ASTMethod.MethodCacheKey mck = new ASTMethod.MethodCacheKey(this.methodName, paramClasses);
         IntrospectionCacheData icd = context.icacheGet(mck);
         if (icd != null && o != null && icd.contextData == o.getClass()) {
            method = (VelMethod)icd.thingy;
         } else {
            method = this.rsvc.getUberspect().getMethod(o, this.methodName, params, new Info(context.getCurrentTemplateName(), this.getLine(), this.getColumn()));
            if (method != null && o != null) {
               icd = new IntrospectionCacheData();
               icd.contextData = o.getClass();
               icd.thingy = method;
               context.icachePut(mck, icd);
            }
         }

         if (method == null) {
            return null;
         }
      } catch (MethodInvocationException var12) {
         throw var12;
      } catch (RuntimeException var13) {
         throw var13;
      } catch (Exception var14) {
         this.log.error("ASTMethod.execute() : exception from introspection", var14);
         return null;
      }

      try {
         Object obj = method.invoke(o, params);
         return obj == null && method.getReturnType() == Void.TYPE ? "" : obj;
      } catch (InvocationTargetException var9) {
         Throwable t = var9.getTargetException();
         if (t instanceof Exception) {
            try {
               return EventHandlerUtil.methodException(this.rsvc, context, o.getClass(), this.methodName, (Exception)t);
            } catch (Exception var8) {
               throw new MethodInvocationException("Invocation of method '" + this.methodName + "' in  " + o.getClass() + " threw exception " + var8.toString(), var8, this.methodName, context.getCurrentTemplateName(), this.getLine(), this.getColumn());
            }
         } else {
            throw new MethodInvocationException("Invocation of method '" + this.methodName + "' in  " + o.getClass() + " threw exception " + var9.getTargetException().toString(), var9.getTargetException(), this.methodName, context.getCurrentTemplateName(), this.getLine(), this.getColumn());
         }
      } catch (RuntimeException var10) {
         throw var10;
      } catch (Exception var11) {
         this.log.error("ASTMethod.execute() : exception invoking method '" + this.methodName + "' in " + o.getClass(), var11);
         return null;
      }
   }

   public String getMethodName() {
      return this.methodName;
   }

   public static class MethodCacheKey {
      private final String methodName;
      private final Class[] params;

      public MethodCacheKey(String methodName, Class[] params) {
         this.methodName = methodName != null ? methodName : "";
         this.params = params != null ? params : ArrayUtils.EMPTY_CLASS_ARRAY;
      }

      public boolean equals(Object o) {
         if (o instanceof ASTMethod.MethodCacheKey) {
            ASTMethod.MethodCacheKey other = (ASTMethod.MethodCacheKey)o;
            if (this.params.length == other.params.length && this.methodName.equals(other.methodName)) {
               for(int i = 0; i < this.params.length; ++i) {
                  if (this.params[i] == null) {
                     if (this.params[i] != other.params[i]) {
                        return false;
                     }
                  } else if (!this.params[i].equals(other.params[i])) {
                     return false;
                  }
               }

               return true;
            }
         }

         return false;
      }

      public int hashCode() {
         int result = 17;

         for(int i = 0; i < this.params.length; ++i) {
            Class param = this.params[i];
            if (param != null) {
               result = result * 37 + param.hashCode();
            }
         }

         result = result * 37 + this.methodName.hashCode();
         return result;
      }
   }
}
