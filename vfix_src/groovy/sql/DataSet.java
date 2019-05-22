package groovy.sql;

import groovy.lang.Closure;
import groovy.lang.GroovyRuntimeException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.MethodNode;

public class DataSet extends Sql {
   private Closure where;
   private Closure sort;
   private boolean reversed = false;
   private DataSet parent;
   private String table;
   private SqlWhereVisitor visitor;
   private SqlOrderByVisitor sortVisitor;
   private String sql;
   private List params;
   private Sql delegate;

   public DataSet(Sql sql, Class type) {
      super(sql);
      this.delegate = sql;
      String table = type.getName();
      int idx = table.lastIndexOf(46);
      if (idx > 0) {
         table = table.substring(idx + 1);
      }

      this.table = table.toLowerCase();
   }

   public DataSet(Sql sql, String table) {
      super(sql);
      this.delegate = sql;
      this.table = table;
   }

   private DataSet(DataSet parent, Closure where) {
      super((Sql)parent);
      this.delegate = parent.delegate;
      this.table = parent.table;
      this.parent = parent;
      this.where = where;
   }

   private DataSet(DataSet parent, Closure where, Closure sort) {
      super((Sql)parent);
      this.delegate = parent.delegate;
      this.table = parent.table;
      this.parent = parent;
      this.where = where;
      this.sort = sort;
   }

   private DataSet(DataSet parent) {
      super((Sql)parent);
      this.delegate = parent.delegate;
      this.table = parent.table;
      this.parent = parent;
      this.reversed = true;
   }

   protected Connection createConnection() throws SQLException {
      return this.delegate.createConnection();
   }

   protected void closeResources(Connection connection, Statement statement, ResultSet results) {
      this.delegate.closeResources(connection, statement, results);
   }

   protected void closeResources(Connection connection, Statement statement) {
      this.delegate.closeResources(connection, statement);
   }

   public void cacheConnection(Closure closure) throws SQLException {
      this.delegate.cacheConnection(closure);
   }

   public void withTransaction(Closure closure) throws SQLException {
      this.delegate.withTransaction(closure);
   }

   public void commit() throws SQLException {
      this.delegate.commit();
   }

   public void rollback() throws SQLException {
      this.delegate.rollback();
   }

   public void add(Map<String, Object> map) throws SQLException {
      StringBuffer buffer = new StringBuffer("insert into ");
      buffer.append(this.table);
      buffer.append(" (");
      StringBuffer paramBuffer = new StringBuffer();
      boolean first = true;

      String column;
      for(Iterator i$ = map.keySet().iterator(); i$.hasNext(); buffer.append(column)) {
         column = (String)i$.next();
         if (first) {
            first = false;
            paramBuffer.append("?");
         } else {
            buffer.append(", ");
            paramBuffer.append(", ?");
         }
      }

      buffer.append(") values (");
      buffer.append(paramBuffer.toString());
      buffer.append(")");
      int answer = this.executeUpdate(buffer.toString(), new ArrayList(map.values()));
      if (answer != 1) {
         LOG.warning("Should have updated 1 row not " + answer + " when trying to add: " + map);
      }

   }

   public DataSet findAll(Closure where) {
      return new DataSet(this, where);
   }

   public DataSet sort(Closure sort) {
      return new DataSet(this, (Closure)null, sort);
   }

   public DataSet reverse() {
      if (this.sort == null) {
         throw new GroovyRuntimeException("reverse() only allowed immediately after a sort()");
      } else {
         return new DataSet(this);
      }
   }

   public void each(Closure closure) throws SQLException {
      this.eachRow(this.getSql(), this.getParameters(), closure);
   }

   private String getSqlWhere() {
      String whereClaus = "";
      String parentClaus = "";
      if (this.parent != null) {
         parentClaus = this.parent.getSqlWhere();
      }

      if (this.where != null) {
         whereClaus = whereClaus + this.getSqlWhereVisitor().getWhere();
      }

      if (parentClaus.length() == 0) {
         return whereClaus;
      } else {
         return whereClaus.length() == 0 ? parentClaus : parentClaus + " and " + whereClaus;
      }
   }

   private String getSqlOrderBy() {
      String sortByClaus = "";
      String parentClaus = "";
      if (this.parent != null) {
         parentClaus = this.parent.getSqlOrderBy();
      }

      if (this.reversed && parentClaus.length() > 0) {
         parentClaus = parentClaus + " DESC";
      }

      if (this.sort != null) {
         sortByClaus = sortByClaus + this.getSqlOrderByVisitor().getOrderBy();
      }

      if (parentClaus.length() == 0) {
         return sortByClaus;
      } else {
         return sortByClaus.length() == 0 ? parentClaus : parentClaus + ", " + sortByClaus;
      }
   }

   public String getSql() {
      if (this.sql == null) {
         this.sql = "select * from " + this.table;
         String whereClaus = this.getSqlWhere();
         if (whereClaus.length() > 0) {
            this.sql = this.sql + " where " + whereClaus;
         }

         String orerByClaus = this.getSqlOrderBy();
         if (orerByClaus.length() > 0) {
            this.sql = this.sql + " order by " + orerByClaus;
         }
      }

      return this.sql;
   }

   public List getParameters() {
      if (this.params == null) {
         this.params = new ArrayList();
         if (this.parent != null) {
            this.params.addAll(this.parent.getParameters());
         }

         this.params.addAll(this.getSqlWhereVisitor().getParameters());
      }

      return this.params;
   }

   protected SqlWhereVisitor getSqlWhereVisitor() {
      if (this.visitor == null) {
         this.visitor = new SqlWhereVisitor();
         this.visit(this.where, this.visitor);
      }

      return this.visitor;
   }

   protected SqlOrderByVisitor getSqlOrderByVisitor() {
      if (this.sortVisitor == null) {
         this.sortVisitor = new SqlOrderByVisitor();
         this.visit(this.sort, this.sortVisitor);
      }

      return this.sortVisitor;
   }

   private void visit(Closure closure, CodeVisitorSupport visitor) {
      if (closure != null) {
         ClassNode classNode = closure.getMetaClass().getClassNode();
         if (classNode == null) {
            throw new GroovyRuntimeException("Could not find the ClassNode for MetaClass: " + closure.getMetaClass());
         }

         List methods = classNode.getDeclaredMethods("doCall");
         if (!methods.isEmpty()) {
            MethodNode method = (MethodNode)methods.get(0);
            if (method != null) {
               org.codehaus.groovy.ast.stmt.Statement statement = method.getCode();
               if (statement != null) {
                  statement.visit(visitor);
               }
            }
         }
      }

   }

   public DataSet createView(Closure criteria) {
      return new DataSet(this, criteria);
   }

   public List rows() throws SQLException {
      return this.rows(this.getSql(), this.getParameters());
   }

   public Object firstRow() throws SQLException {
      List rows = this.rows();
      return rows.isEmpty() ? null : rows.get(0);
   }
}
