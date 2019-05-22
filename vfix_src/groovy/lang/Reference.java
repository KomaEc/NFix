package groovy.lang;

import java.io.Serializable;
import org.codehaus.groovy.runtime.InvokerHelper;

public class Reference<T> extends GroovyObjectSupport implements Serializable {
   private T value;

   public Reference() {
   }

   public Reference(T value) {
      this.value = value;
   }

   public Object getProperty(String property) {
      Object value = this.get();
      return value != null ? InvokerHelper.getProperty(value, property) : super.getProperty(property);
   }

   public void setProperty(String property, Object newValue) {
      Object value = this.get();
      if (value != null) {
         InvokerHelper.setProperty(value, property, newValue);
      } else {
         super.setProperty(property, newValue);
      }

   }

   public Object invokeMethod(String name, Object args) {
      Object value = this.get();
      if (value != null) {
         try {
            return InvokerHelper.invokeMethod(value, name, args);
         } catch (Exception var5) {
            return super.invokeMethod(name, args);
         }
      } else {
         return super.invokeMethod(name, args);
      }
   }

   public T get() {
      return this.value;
   }

   public void set(T value) {
      this.value = value;
   }
}
