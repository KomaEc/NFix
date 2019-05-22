package polyglot.types;

import java.util.List;

public interface ReferenceType extends Type {
   Type superType();

   List interfaces();

   List fields();

   List methods();

   FieldInstance fieldNamed(String var1);

   List methodsNamed(String var1);

   List methods(String var1, List var2);

   boolean hasMethod(MethodInstance var1);

   boolean hasMethodImpl(MethodInstance var1);
}
