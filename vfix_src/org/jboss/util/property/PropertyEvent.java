package org.jboss.util.property;

import java.util.EventObject;
import org.jboss.util.NullArgumentException;

public class PropertyEvent extends EventObject {
   private static final long serialVersionUID = -5007209897739884086L;
   protected final String name;
   protected final String value;

   public PropertyEvent(Object source, String name, String value) {
      super(source);
      if (name == null) {
         throw new NullArgumentException("name");
      } else {
         this.name = name;
         this.value = value;
      }
   }

   public PropertyEvent(Object source, String name) {
      this(source, name, (String)null);
   }

   public final String getPropertyName() {
      return this.name;
   }

   public final String getPropertyValue() {
      return this.value;
   }

   public String toString() {
      return super.toString() + "{ name=" + this.name + ", value=" + this.value + " }";
   }
}
