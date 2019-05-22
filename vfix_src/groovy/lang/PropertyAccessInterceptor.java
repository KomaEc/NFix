package groovy.lang;

public interface PropertyAccessInterceptor extends Interceptor {
   Object beforeGet(Object var1, String var2);

   void beforeSet(Object var1, String var2, Object var3);
}
