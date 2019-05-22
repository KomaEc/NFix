package groovy.lang;

import java.lang.reflect.Method;

public interface MutableMetaClass extends MetaClass {
   boolean isModified();

   void addNewInstanceMethod(Method var1);

   void addNewStaticMethod(Method var1);

   void addMetaMethod(MetaMethod var1);

   void addMetaBeanProperty(MetaBeanProperty var1);
}
