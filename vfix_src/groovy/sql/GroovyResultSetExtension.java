package groovy.sql;

import groovy.lang.Closure;
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MissingPropertyException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.InvokerInvocationException;

public class GroovyResultSetExtension extends GroovyObjectSupport {
   private boolean updated = false;
   private final ResultSet resultSet;

   protected ResultSet getResultSet() throws SQLException {
      return this.resultSet;
   }

   public GroovyResultSetExtension(ResultSet set) {
      this.resultSet = set;
   }

   public String toString() {
      try {
         StringBuffer sb = new StringBuffer("[");
         ResultSetMetaData metaData = this.resultSet.getMetaData();
         int count = metaData.getColumnCount();

         for(int i = 1; i <= count; ++i) {
            sb.append(metaData.getColumnName(i));
            sb.append(":");
            Object object = this.resultSet.getObject(i);
            if (object != null) {
               sb.append(object.toString());
            } else {
               sb.append("[null]");
            }

            if (i < count) {
               sb.append(", ");
            }
         }

         sb.append("]");
         return sb.toString();
      } catch (SQLException var6) {
         return super.toString();
      }
   }

   public Object invokeMethod(String name, Object args) {
      try {
         return InvokerHelper.invokeMethod(this.getResultSet(), name, args);
      } catch (SQLException var4) {
         throw new InvokerInvocationException(var4);
      }
   }

   public Object getProperty(String columnName) {
      try {
         return this.getResultSet().getObject(columnName);
      } catch (SQLException var3) {
         throw new MissingPropertyException(columnName, GroovyResultSetProxy.class, var3);
      }
   }

   public void setProperty(String columnName, Object newValue) {
      try {
         this.getResultSet().updateObject(columnName, newValue);
         this.updated = true;
      } catch (SQLException var4) {
         throw new MissingPropertyException(columnName, GroovyResultSetProxy.class, var4);
      }
   }

   public Object getAt(int index) throws SQLException {
      index = this.normalizeIndex(index);
      return this.getResultSet().getObject(index);
   }

   public void putAt(int index, Object newValue) throws SQLException {
      index = this.normalizeIndex(index);
      this.getResultSet().updateObject(index, newValue);
   }

   public void add(Map values) throws SQLException {
      this.getResultSet().moveToInsertRow();
      Iterator iter = values.entrySet().iterator();

      while(iter.hasNext()) {
         Entry entry = (Entry)iter.next();
         this.getResultSet().updateObject(entry.getKey().toString(), entry.getValue());
      }

      this.getResultSet().insertRow();
   }

   protected int normalizeIndex(int index) throws SQLException {
      if (index < 0) {
         int columnCount = this.getResultSet().getMetaData().getColumnCount();

         do {
            index += columnCount;
         } while(index < 0);
      }

      return index + 1;
   }

   public void eachRow(Closure closure) throws SQLException {
      while(this.next()) {
         closure.call((Object)this);
      }

   }

   public boolean next() throws SQLException {
      if (this.updated) {
         this.getResultSet().updateRow();
         this.updated = false;
      }

      return this.getResultSet().next();
   }

   public boolean previous() throws SQLException {
      if (this.updated) {
         this.getResultSet().updateRow();
         this.updated = false;
      }

      return this.getResultSet().previous();
   }
}
