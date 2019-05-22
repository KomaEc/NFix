package groovy.lang;

import java.util.EventListener;

public interface MetaClassRegistryChangeEventListener extends EventListener {
   void updateConstantMetaClass(MetaClassRegistryChangeEvent var1);
}
