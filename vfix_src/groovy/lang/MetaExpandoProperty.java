package groovy.lang;

import java.util.Map.Entry;

public class MetaExpandoProperty extends MetaProperty {
   Object value = null;

   public MetaExpandoProperty(Entry entry) {
      super((String)entry.getKey(), Object.class);
      this.value = entry.getValue();
   }

   public Object getProperty(Object object) {
      return this.value;
   }

   public void setProperty(Object object, Object newValue) {
      this.value = newValue;
   }
}
