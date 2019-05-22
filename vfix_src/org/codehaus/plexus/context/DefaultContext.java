package org.codehaus.plexus.context;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class DefaultContext implements Context {
   private static DefaultContext.Hidden HIDDEN_MAKER = new DefaultContext.Hidden();
   private Map contextData;
   private Context parent;
   private boolean readOnly;

   public DefaultContext(Map contextData, Context parent) {
      this.parent = parent;
      this.contextData = contextData;
   }

   public DefaultContext(Map contextData) {
      this(contextData, (Context)null);
   }

   public DefaultContext(Context parent) {
      this(new Hashtable(), parent);
   }

   public DefaultContext() {
      this((Context)null);
   }

   public boolean contains(Object key) {
      Object data = this.contextData.get(key);
      if (null != data) {
         return !(data instanceof DefaultContext.Hidden);
      } else {
         return null == this.parent ? false : this.parent.contains(key);
      }
   }

   public Object get(Object key) throws ContextException {
      Object data = this.contextData.get(key);
      if (data != null) {
         if (data instanceof DefaultContext.Hidden) {
            throw new ContextException("Unable to locate " + key);
         } else {
            return data;
         }
      } else if (this.parent == null) {
         throw new ContextException("Unable to resolve context key: " + key);
      } else {
         return this.parent.get(key);
      }
   }

   public void put(Object key, Object value) throws IllegalStateException {
      this.checkWriteable();
      if (null == value) {
         this.contextData.remove(key);
      } else {
         this.contextData.put(key, value);
      }

   }

   public void hide(Object key) throws IllegalStateException {
      this.checkWriteable();
      this.contextData.put(key, HIDDEN_MAKER);
   }

   protected Map getContextData() {
      return this.contextData;
   }

   protected Context getParent() {
      return this.parent;
   }

   public void makeReadOnly() {
      this.readOnly = true;
   }

   protected void checkWriteable() throws IllegalStateException {
      if (this.readOnly) {
         throw new IllegalStateException("Context is read only and can not be modified");
      }
   }

   private static class Hidden implements Serializable {
      private Hidden() {
      }

      // $FF: synthetic method
      Hidden(Object x0) {
         this();
      }
   }
}
