package org.apache.velocity.context;

import java.util.HashMap;
import java.util.Stack;
import org.apache.velocity.app.event.EventCartridge;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.util.introspection.IntrospectionCacheData;

class InternalContextBase implements InternalHousekeepingContext, InternalEventContext {
   private static final long serialVersionUID = -245905472770843470L;
   private HashMap introspectionCache = new HashMap(33);
   private Stack templateNameStack = new Stack();
   private EventCartridge eventCartridge = null;
   private Resource currentResource = null;
   private boolean allowRendering = true;

   public void pushCurrentTemplateName(String s) {
      this.templateNameStack.push(s);
   }

   public void popCurrentTemplateName() {
      this.templateNameStack.pop();
   }

   public String getCurrentTemplateName() {
      return this.templateNameStack.empty() ? "<undef>" : (String)this.templateNameStack.peek();
   }

   public Object[] getTemplateNameStack() {
      return this.templateNameStack.toArray();
   }

   public IntrospectionCacheData icacheGet(Object key) {
      return (IntrospectionCacheData)this.introspectionCache.get(key);
   }

   public void icachePut(Object key, IntrospectionCacheData o) {
      this.introspectionCache.put(key, o);
   }

   public void setCurrentResource(Resource r) {
      this.currentResource = r;
   }

   public Resource getCurrentResource() {
      return this.currentResource;
   }

   public boolean getAllowRendering() {
      return this.allowRendering;
   }

   public void setAllowRendering(boolean v) {
      this.allowRendering = v;
   }

   public EventCartridge attachEventCartridge(EventCartridge ec) {
      EventCartridge temp = this.eventCartridge;
      this.eventCartridge = ec;
      return temp;
   }

   public EventCartridge getEventCartridge() {
      return this.eventCartridge;
   }
}
