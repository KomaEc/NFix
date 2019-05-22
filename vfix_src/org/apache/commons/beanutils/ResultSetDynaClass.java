package org.apache.commons.beanutils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class ResultSetDynaClass extends JDBCDynaClass implements DynaClass {
   protected ResultSet resultSet;

   public ResultSetDynaClass(ResultSet resultSet) throws SQLException {
      this(resultSet, true);
   }

   public ResultSetDynaClass(ResultSet resultSet, boolean lowerCase) throws SQLException {
      this.resultSet = null;
      if (resultSet == null) {
         throw new NullPointerException();
      } else {
         this.resultSet = resultSet;
         this.lowerCase = lowerCase;
         this.introspect(resultSet);
      }
   }

   public Iterator iterator() {
      return new ResultSetIterator(this);
   }

   ResultSet getResultSet() {
      return this.resultSet;
   }

   protected Class loadClass(String className) throws SQLException {
      try {
         return this.getClass().getClassLoader().loadClass(className);
      } catch (Exception var3) {
         throw new SQLException("Cannot load column class '" + className + "': " + var3);
      }
   }
}
