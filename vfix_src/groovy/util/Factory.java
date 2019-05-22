package groovy.util;

import groovy.lang.Closure;
import java.util.Map;

public interface Factory {
   boolean isLeaf();

   boolean isHandlesNodeChildren();

   void onFactoryRegistration(FactoryBuilderSupport var1, String var2, String var3);

   Object newInstance(FactoryBuilderSupport var1, Object var2, Object var3, Map var4) throws InstantiationException, IllegalAccessException;

   boolean onHandleNodeAttributes(FactoryBuilderSupport var1, Object var2, Map var3);

   boolean onNodeChildren(FactoryBuilderSupport var1, Object var2, Closure var3);

   void onNodeCompleted(FactoryBuilderSupport var1, Object var2, Object var3);

   void setParent(FactoryBuilderSupport var1, Object var2, Object var3);

   void setChild(FactoryBuilderSupport var1, Object var2, Object var3);
}
