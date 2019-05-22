package org.codehaus.classworlds;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

public interface ClassRealm {
   String getId();

   ClassWorld getWorld();

   void importFrom(String var1, String var2) throws NoSuchRealmException;

   void addConstituent(URL var1);

   ClassRealm locateSourceRealm(String var1);

   void setParent(ClassRealm var1);

   ClassRealm createChildRealm(String var1) throws DuplicateRealmException;

   ClassLoader getClassLoader();

   ClassRealm getParent();

   URL[] getConstituents();

   Class loadClass(String var1) throws ClassNotFoundException;

   URL getResource(String var1);

   Enumeration findResources(String var1) throws IOException;

   InputStream getResourceAsStream(String var1);

   void display();
}
