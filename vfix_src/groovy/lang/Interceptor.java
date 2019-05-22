package groovy.lang;

public interface Interceptor {
   Object beforeInvoke(Object var1, String var2, Object[] var3);

   Object afterInvoke(Object var1, String var2, Object[] var3, Object var4);

   boolean doInvoke();
}
