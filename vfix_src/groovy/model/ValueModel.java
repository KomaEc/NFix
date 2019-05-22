package groovy.model;

public interface ValueModel {
   Object getValue();

   void setValue(Object var1);

   Class getType();

   boolean isEditable();
}
