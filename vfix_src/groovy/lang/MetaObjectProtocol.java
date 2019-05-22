package groovy.lang;

import java.util.List;

public interface MetaObjectProtocol {
   List<MetaProperty> getProperties();

   List<MetaMethod> getMethods();

   List<MetaMethod> respondsTo(Object var1, String var2, Object[] var3);

   List<MetaMethod> respondsTo(Object var1, String var2);

   MetaProperty hasProperty(Object var1, String var2);

   MetaProperty getMetaProperty(String var1);

   MetaMethod getStaticMetaMethod(String var1, Object[] var2);

   MetaMethod getMetaMethod(String var1, Object[] var2);

   Class getTheClass();

   Object invokeConstructor(Object[] var1);

   Object invokeMethod(Object var1, String var2, Object[] var3);

   Object invokeMethod(Object var1, String var2, Object var3);

   Object invokeStaticMethod(Object var1, String var2, Object[] var3);

   Object getProperty(Object var1, String var2);

   void setProperty(Object var1, String var2, Object var3);

   Object getAttribute(Object var1, String var2);

   void setAttribute(Object var1, String var2, Object var3);
}
