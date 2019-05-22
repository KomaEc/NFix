package org.apache.commons.beanutils;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

abstract class JDBCDynaClass implements DynaClass, Serializable {
   protected boolean lowerCase = true;
   protected DynaProperty[] properties = null;
   protected Map propertiesMap = new HashMap();
   // $FF: synthetic field
   static Class class$java$lang$Object;

   public String getName() {
      return this.getClass().getName();
   }

   public DynaProperty getDynaProperty(String name) {
      if (name == null) {
         throw new IllegalArgumentException("No property name specified");
      } else {
         return (DynaProperty)this.propertiesMap.get(name);
      }
   }

   public DynaProperty[] getDynaProperties() {
      return this.properties;
   }

   public DynaBean newInstance() throws IllegalAccessException, InstantiationException {
      throw new UnsupportedOperationException("newInstance() not supported");
   }

   protected Class loadClass(String className) throws SQLException {
      try {
         ClassLoader cl = Thread.currentThread().getContextClassLoader();
         if (cl == null) {
            cl = this.getClass().getClassLoader();
         }

         return cl.loadClass(className);
      } catch (Exception var3) {
         throw new SQLException("Cannot load column class '" + className + "': " + var3);
      }
   }

   protected DynaProperty createDynaProperty(ResultSetMetaData metadata, int i) throws SQLException {
      String name = null;
      if (this.lowerCase) {
         name = metadata.getColumnName(i).toLowerCase();
      } else {
         name = metadata.getColumnName(i);
      }

      String className = null;

      try {
         className = metadata.getColumnClassName(i);
      } catch (SQLException var6) {
      }

      Class clazz = class$java$lang$Object == null ? (class$java$lang$Object = class$("java.lang.Object")) : class$java$lang$Object;
      if (className != null) {
         clazz = this.loadClass(className);
      }

      return new DynaProperty(name, clazz);
   }

   protected void introspect(ResultSet resultSet) throws SQLException {
      ArrayList list = new ArrayList();
      ResultSetMetaData metadata = resultSet.getMetaData();
      int n = metadata.getColumnCount();

      for(int i = 1; i <= n; ++i) {
         DynaProperty dynaProperty = this.createDynaProperty(metadata, i);
         if (dynaProperty != null) {
            list.add(dynaProperty);
         }
      }

      this.properties = (DynaProperty[])list.toArray(new DynaProperty[list.size()]);

      for(int i = 0; i < this.properties.length; ++i) {
         this.propertiesMap.put(this.properties[i].getName(), this.properties[i]);
      }

   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }
}
