package groovy.lang;

import java.util.List;
import org.codehaus.groovy.ast.ClassNode;

public interface MetaClass extends MetaObjectProtocol {
   Object invokeMethod(Class var1, Object var2, String var3, Object[] var4, boolean var5, boolean var6);

   Object getProperty(Class var1, Object var2, String var3, boolean var4, boolean var5);

   void setProperty(Class var1, Object var2, String var3, Object var4, boolean var5, boolean var6);

   Object invokeMissingMethod(Object var1, String var2, Object[] var3);

   Object invokeMissingProperty(Object var1, String var2, Object var3, boolean var4);

   Object getAttribute(Class var1, Object var2, String var3, boolean var4);

   void setAttribute(Class var1, Object var2, String var3, Object var4, boolean var5, boolean var6);

   void initialize();

   List<MetaProperty> getProperties();

   List<MetaMethod> getMethods();

   ClassNode getClassNode();

   List<MetaMethod> getMetaMethods();

   int selectConstructorAndTransformArguments(int var1, Object[] var2);

   MetaMethod pickMethod(String var1, Class[] var2);
}
