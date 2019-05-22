package groovy.lang;

import java.lang.reflect.Array;

public class MetaArrayLengthProperty extends MetaProperty {
   public MetaArrayLengthProperty() {
      super("length", Integer.TYPE);
   }

   public Object getProperty(Object object) {
      return Array.getLength(object);
   }

   public void setProperty(Object object, Object newValue) {
      throw new ReadOnlyPropertyException("length", object.getClass());
   }
}
