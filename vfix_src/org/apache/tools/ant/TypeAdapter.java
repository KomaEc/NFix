package org.apache.tools.ant;

public interface TypeAdapter {
   void setProject(Project var1);

   Project getProject();

   void setProxy(Object var1);

   Object getProxy();

   void checkProxyClass(Class var1);
}
