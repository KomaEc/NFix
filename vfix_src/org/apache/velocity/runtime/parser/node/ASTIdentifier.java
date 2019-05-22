package org.apache.velocity.runtime.parser.node;

import java.lang.reflect.InvocationTargetException;
import org.apache.velocity.app.event.EventHandlerUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.parser.Parser;
import org.apache.velocity.runtime.parser.ParserVisitor;
import org.apache.velocity.util.introspection.Info;
import org.apache.velocity.util.introspection.IntrospectionCacheData;
import org.apache.velocity.util.introspection.VelPropertyGet;

public class ASTIdentifier extends SimpleNode {
   private String identifier = "";
   protected Info uberInfo;

   public ASTIdentifier(int id) {
      super(id);
   }

   public ASTIdentifier(Parser p, int id) {
      super(p, id);
   }

   public Object jjtAccept(ParserVisitor visitor, Object data) {
      return visitor.visit(this, data);
   }

   public Object init(InternalContextAdapter context, Object data) throws TemplateInitException {
      super.init(context, data);
      this.identifier = this.getFirstToken().image;
      this.uberInfo = new Info(context.getCurrentTemplateName(), this.getLine(), this.getColumn());
      return data;
   }

   public Object execute(Object o, InternalContextAdapter context) throws MethodInvocationException {
      VelPropertyGet vg = null;

      try {
         IntrospectionCacheData icd = context.icacheGet(this);
         if (icd != null && o != null && icd.contextData == o.getClass()) {
            vg = (VelPropertyGet)icd.thingy;
         } else {
            vg = this.rsvc.getUberspect().getPropertyGet(o, this.identifier, this.uberInfo);
            if (vg != null && vg.isCacheable() && o != null) {
               icd = new IntrospectionCacheData();
               icd.contextData = o.getClass();
               icd.thingy = vg;
               context.icachePut(this, icd);
            }
         }
      } catch (RuntimeException var12) {
         throw var12;
      } catch (Exception var13) {
         this.log.error("ASTIdentifier.execute() : identifier = " + this.identifier, var13);
      }

      if (vg == null) {
         return null;
      } else {
         try {
            return vg.invoke(o);
         } catch (InvocationTargetException var8) {
            Throwable t = var8.getTargetException();
            if (t instanceof Exception) {
               try {
                  return EventHandlerUtil.methodException(this.rsvc, context, o.getClass(), vg.getMethodName(), (Exception)t);
               } catch (Exception var7) {
                  throw new MethodInvocationException("Invocation of method '" + vg.getMethodName() + "'" + " in  " + o.getClass() + " threw exception " + var8.getTargetException().toString(), var8.getTargetException(), vg.getMethodName(), context.getCurrentTemplateName(), this.getLine(), this.getColumn());
               }
            } else {
               throw new MethodInvocationException("Invocation of method '" + vg.getMethodName() + "'" + " in  " + o.getClass() + " threw exception " + var8.getTargetException().toString(), var8.getTargetException(), vg.getMethodName(), context.getCurrentTemplateName(), this.getLine(), this.getColumn());
            }
         } catch (IllegalArgumentException var9) {
            return null;
         } catch (RuntimeException var10) {
            throw var10;
         } catch (Exception var11) {
            this.log.error("ASTIdentifier() : exception invoking method for identifier '" + this.identifier + "' in " + o.getClass() + " : " + var11);
            return null;
         }
      }
   }
}
