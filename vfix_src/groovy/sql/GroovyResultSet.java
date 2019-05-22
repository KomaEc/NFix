package groovy.sql;

import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface GroovyResultSet extends GroovyObject, ResultSet {
   Object getAt(int var1) throws SQLException;

   Object getAt(String var1);

   void putAt(int var1, Object var2) throws SQLException;

   void putAt(String var1, Object var2);

   void add(Map var1) throws SQLException;

   void eachRow(Closure var1) throws SQLException;
}
