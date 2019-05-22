package groovy.grape;

import java.net.URI;
import java.util.List;
import java.util.Map;

public interface GrapeEngine {
   Object grab(String var1);

   Object grab(Map var1);

   Object grab(Map var1, Map... var2);

   Map<String, Map<String, List<String>>> enumerateGrapes();

   URI[] resolve(Map var1, Map... var2);

   URI[] resolve(Map var1, List var2, Map... var3);

   Map[] listDependencies(ClassLoader var1);

   void addResolver(Map<String, Object> var1);
}
