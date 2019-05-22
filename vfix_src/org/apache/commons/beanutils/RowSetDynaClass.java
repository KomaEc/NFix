package org.apache.commons.beanutils;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RowSetDynaClass extends JDBCDynaClass implements DynaClass, Serializable {
   protected int limit;
   protected List rows;

   public RowSetDynaClass(ResultSet resultSet) throws SQLException {
      this(resultSet, true, -1);
   }

   public RowSetDynaClass(ResultSet resultSet, int limit) throws SQLException {
      this(resultSet, true, limit);
   }

   public RowSetDynaClass(ResultSet resultSet, boolean lowerCase) throws SQLException {
      this(resultSet, lowerCase, -1);
   }

   public RowSetDynaClass(ResultSet resultSet, boolean lowerCase, int limit) throws SQLException {
      this.limit = -1;
      this.rows = new ArrayList();
      if (resultSet == null) {
         throw new NullPointerException();
      } else {
         this.lowerCase = lowerCase;
         this.limit = limit;
         this.introspect(resultSet);
         this.copy(resultSet);
      }
   }

   public List getRows() {
      return this.rows;
   }

   protected void copy(ResultSet resultSet) throws SQLException {
      int var2 = 0;

      while(resultSet.next() && (this.limit < 0 || var2++ < this.limit)) {
         DynaBean bean = this.createDynaBean();

         for(int i = 0; i < this.properties.length; ++i) {
            String name = this.properties[i].getName();
            bean.set(name, resultSet.getObject(name));
         }

         this.rows.add(bean);
      }

   }

   protected DynaBean createDynaBean() {
      return new BasicDynaBean(this);
   }
}
