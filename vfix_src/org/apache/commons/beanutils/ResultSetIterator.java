package org.apache.commons.beanutils;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ResultSetIterator implements DynaBean, Iterator {
   protected boolean current = false;
   protected ResultSetDynaClass dynaClass = null;
   protected boolean eof = false;

   ResultSetIterator(ResultSetDynaClass dynaClass) {
      this.dynaClass = dynaClass;
   }

   public boolean contains(String name, String key) {
      throw new UnsupportedOperationException("FIXME - mapped properties not currently supported");
   }

   public Object get(String name) {
      if (this.dynaClass.getDynaProperty(name) == null) {
         throw new IllegalArgumentException(name);
      } else {
         try {
            return this.dynaClass.getResultSet().getObject(name);
         } catch (SQLException var3) {
            throw new RuntimeException("get(" + name + "): SQLException: " + var3);
         }
      }
   }

   public Object get(String name, int index) {
      throw new UnsupportedOperationException("FIXME - indexed properties not currently supported");
   }

   public Object get(String name, String key) {
      throw new UnsupportedOperationException("FIXME - mapped properties not currently supported");
   }

   public DynaClass getDynaClass() {
      return this.dynaClass;
   }

   public void remove(String name, String key) {
      throw new UnsupportedOperationException("FIXME - mapped operations not currently supported");
   }

   public void set(String name, Object value) {
      if (this.dynaClass.getDynaProperty(name) == null) {
         throw new IllegalArgumentException(name);
      } else {
         try {
            this.dynaClass.getResultSet().updateObject(name, value);
         } catch (SQLException var4) {
            throw new RuntimeException("set(" + name + "): SQLException: " + var4);
         }
      }
   }

   public void set(String name, int index, Object value) {
      throw new UnsupportedOperationException("FIXME - indexed properties not currently supported");
   }

   public void set(String name, String key, Object value) {
      throw new UnsupportedOperationException("FIXME - mapped properties not currently supported");
   }

   public boolean hasNext() {
      try {
         this.advance();
         return !this.eof;
      } catch (SQLException var2) {
         throw new RuntimeException("hasNext():  SQLException:  " + var2);
      }
   }

   public Object next() {
      try {
         this.advance();
         if (this.eof) {
            throw new NoSuchElementException();
         } else {
            this.current = false;
            return this;
         }
      } catch (SQLException var2) {
         throw new RuntimeException("next():  SQLException:  " + var2);
      }
   }

   public void remove() {
      throw new UnsupportedOperationException("remove()");
   }

   protected void advance() throws SQLException {
      if (!this.current && !this.eof) {
         if (this.dynaClass.getResultSet().next()) {
            this.current = true;
            this.eof = false;
         } else {
            this.current = false;
            this.eof = true;
         }
      }

   }
}
