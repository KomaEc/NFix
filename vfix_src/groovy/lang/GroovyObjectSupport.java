package groovy.lang;

import org.codehaus.groovy.runtime.InvokerHelper;

public abstract class GroovyObjectSupport implements GroovyObject {
   private transient MetaClass metaClass = InvokerHelper.getMetaClass(this.getClass());

   public Object getProperty(String property) {
      return this.getMetaClass().getProperty(this, property);
   }

   public void setProperty(String property, Object newValue) {
      this.getMetaClass().setProperty(this, property, newValue);
   }

   public Object invokeMethod(String name, Object args) {
      return this.getMetaClass().invokeMethod(this, name, args);
   }

   public MetaClass getMetaClass() {
      if (this.metaClass == null) {
         this.metaClass = InvokerHelper.getMetaClass(this.getClass());
      }

      return this.metaClass;
   }

   public void setMetaClass(MetaClass metaClass) {
      this.metaClass = metaClass;
   }
}
