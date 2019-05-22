package org.apache.velocity.runtime.directive;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.context.Context;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.exception.TemplateInitException;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.util.introspection.Info;
import org.apache.velocity.util.introspection.IntrospectionCacheData;

public class Foreach extends Directive {
   private String counterName;
   private int counterInitialValue;
   private int maxNbrLoops;
   private String elementKey;
   protected Info uberInfo;

   public String getName() {
      return "foreach";
   }

   public int getType() {
      return 1;
   }

   public void init(RuntimeServices rs, InternalContextAdapter context, Node node) throws TemplateInitException {
      super.init(rs, context, node);
      this.counterName = this.rsvc.getString("directive.foreach.counter.name");
      this.counterInitialValue = this.rsvc.getInt("directive.foreach.counter.initial.value");
      this.maxNbrLoops = this.rsvc.getInt("directive.foreach.maxloops", Integer.MAX_VALUE);
      if (this.maxNbrLoops < 1) {
         this.maxNbrLoops = Integer.MAX_VALUE;
      }

      SimpleNode sn = (SimpleNode)node.jjtGetChild(0);
      if (sn instanceof ASTReference) {
         this.elementKey = ((ASTReference)sn).getRootString();
      } else {
         this.elementKey = sn.getFirstToken().image.substring(1);
      }

      this.uberInfo = new Info(context.getCurrentTemplateName(), this.getLine(), this.getColumn());
   }

   public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, MethodInvocationException, ResourceNotFoundException, ParseErrorException {
      Object listObject = node.jjtGetChild(2).value(context);
      if (listObject == null) {
         return false;
      } else {
         Iterator i = null;

         try {
            i = this.rsvc.getUberspect().getIterator(listObject, this.uberInfo);
         } catch (RuntimeException var12) {
            throw var12;
         } catch (Exception var13) {
            this.rsvc.getLog().error("Error getting iterator for #foreach", var13);
         }

         if (i == null) {
            return false;
         } else {
            int counter = this.counterInitialValue;
            boolean maxNbrLoopsExceeded = false;
            Object o = context.get(this.elementKey);
            Object savedCounter = context.get(this.counterName);

            for(Foreach.NullHolderContext nullHolderContext = null; !maxNbrLoopsExceeded && i.hasNext(); maxNbrLoopsExceeded = counter - this.counterInitialValue >= this.maxNbrLoops) {
               context.localPut(this.counterName, new Integer(counter));
               Object value = i.next();
               context.localPut(this.elementKey, value);
               if (value == null) {
                  if (nullHolderContext == null) {
                     nullHolderContext = new Foreach.NullHolderContext(this.elementKey, context);
                  }

                  node.jjtGetChild(3).render(nullHolderContext, writer);
               } else {
                  node.jjtGetChild(3).render(context, writer);
               }

               ++counter;
            }

            if (savedCounter != null) {
               context.put(this.counterName, savedCounter);
            } else {
               context.remove(this.counterName);
            }

            if (o != null) {
               context.put(this.elementKey, o);
            } else {
               context.remove(this.elementKey);
            }

            return true;
         }
      }
   }

   protected static class NullHolderContext implements InternalContextAdapter {
      private InternalContextAdapter innerContext;
      private String loopVariableKey;
      private boolean active;

      private NullHolderContext(String key, InternalContextAdapter context) {
         this.innerContext = null;
         this.loopVariableKey = "";
         this.active = true;
         this.innerContext = context;
         if (key != null) {
            this.loopVariableKey = key;
         }

      }

      public Object get(String key) throws MethodInvocationException {
         return this.active && this.loopVariableKey.equals(key) ? null : this.innerContext.get(key);
      }

      public Object put(String key, Object value) {
         if (this.loopVariableKey.equals(key) && value == null) {
            this.active = true;
         }

         return this.innerContext.put(key, value);
      }

      public Object localPut(String key, Object value) {
         return this.put(key, value);
      }

      public boolean containsKey(Object key) {
         return this.innerContext.containsKey(key);
      }

      public Object[] getKeys() {
         return this.innerContext.getKeys();
      }

      public Object remove(Object key) {
         if (this.loopVariableKey.equals(key)) {
            this.active = false;
         }

         return this.innerContext.remove(key);
      }

      public void pushCurrentTemplateName(String s) {
         this.innerContext.pushCurrentTemplateName(s);
      }

      public void popCurrentTemplateName() {
         this.innerContext.popCurrentTemplateName();
      }

      public String getCurrentTemplateName() {
         return this.innerContext.getCurrentTemplateName();
      }

      public Object[] getTemplateNameStack() {
         return this.innerContext.getTemplateNameStack();
      }

      public IntrospectionCacheData icacheGet(Object key) {
         return this.innerContext.icacheGet(key);
      }

      public void icachePut(Object key, IntrospectionCacheData o) {
         this.innerContext.icachePut(key, o);
      }

      public void setCurrentResource(Resource r) {
         this.innerContext.setCurrentResource(r);
      }

      public Resource getCurrentResource() {
         return this.innerContext.getCurrentResource();
      }

      public InternalContextAdapter getBaseContext() {
         return this.innerContext.getBaseContext();
      }

      public Context getInternalUserContext() {
         return this.innerContext.getInternalUserContext();
      }

      public EventCartridge attachEventCartridge(EventCartridge ec) {
         EventCartridge cartridge = this.innerContext.attachEventCartridge(ec);
         return cartridge;
      }

      public EventCartridge getEventCartridge() {
         return this.innerContext.getEventCartridge();
      }

      public boolean getAllowRendering() {
         return this.innerContext.getAllowRendering();
      }

      public void setAllowRendering(boolean v) {
         this.innerContext.setAllowRendering(v);
      }

      // $FF: synthetic method
      NullHolderContext(String x0, InternalContextAdapter x1, Object x2) {
         this(x0, x1);
      }
   }
}
