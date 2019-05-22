package groovy.sql;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.Tuple;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.codehaus.groovy.runtime.SqlGroovyMethods;

public class Sql {
   protected static final Logger LOG = Logger.getLogger(Sql.class.getName());
   private static final List<Object> EMPTY_LIST = Collections.emptyList();
   private static final Pattern NAMED_QUERY_PATTERN = Pattern.compile("(?::|\\?(\\d?)\\.)(\\w+)");
   private DataSource dataSource;
   private Connection useConnection;
   private int resultSetType = 1003;
   private int resultSetConcurrency = 1007;
   private int resultSetHoldability = -1;
   private int updateCount = 0;
   private Closure configureStatement;
   private boolean cacheConnection;
   private boolean cacheStatements;
   private boolean cacheNamedQueries = true;
   private boolean enableNamedQueries = true;
   private boolean withinBatch;
   private final Map<String, Statement> statementCache = new HashMap();
   private final Map<String, String> namedParamSqlCache = new HashMap();
   private final Map<String, List<Tuple>> namedParamIndexPropCache = new HashMap();
   public static final OutParameter ARRAY = new OutParameter() {
      public int getType() {
         return 2003;
      }
   };
   public static final OutParameter BIGINT = new OutParameter() {
      public int getType() {
         return -5;
      }
   };
   public static final OutParameter BINARY = new OutParameter() {
      public int getType() {
         return -2;
      }
   };
   public static final OutParameter BIT = new OutParameter() {
      public int getType() {
         return -7;
      }
   };
   public static final OutParameter BLOB = new OutParameter() {
      public int getType() {
         return 2004;
      }
   };
   public static final OutParameter BOOLEAN = new OutParameter() {
      public int getType() {
         return 16;
      }
   };
   public static final OutParameter CHAR = new OutParameter() {
      public int getType() {
         return 1;
      }
   };
   public static final OutParameter CLOB = new OutParameter() {
      public int getType() {
         return 2005;
      }
   };
   public static final OutParameter DATALINK = new OutParameter() {
      public int getType() {
         return 70;
      }
   };
   public static final OutParameter DATE = new OutParameter() {
      public int getType() {
         return 91;
      }
   };
   public static final OutParameter DECIMAL = new OutParameter() {
      public int getType() {
         return 3;
      }
   };
   public static final OutParameter DISTINCT = new OutParameter() {
      public int getType() {
         return 2001;
      }
   };
   public static final OutParameter DOUBLE = new OutParameter() {
      public int getType() {
         return 8;
      }
   };
   public static final OutParameter FLOAT = new OutParameter() {
      public int getType() {
         return 6;
      }
   };
   public static final OutParameter INTEGER = new OutParameter() {
      public int getType() {
         return 4;
      }
   };
   public static final OutParameter JAVA_OBJECT = new OutParameter() {
      public int getType() {
         return 2000;
      }
   };
   public static final OutParameter LONGVARBINARY = new OutParameter() {
      public int getType() {
         return -4;
      }
   };
   public static final OutParameter LONGVARCHAR = new OutParameter() {
      public int getType() {
         return -1;
      }
   };
   public static final OutParameter NULL = new OutParameter() {
      public int getType() {
         return 0;
      }
   };
   public static final OutParameter NUMERIC = new OutParameter() {
      public int getType() {
         return 2;
      }
   };
   public static final OutParameter OTHER = new OutParameter() {
      public int getType() {
         return 1111;
      }
   };
   public static final OutParameter REAL = new OutParameter() {
      public int getType() {
         return 7;
      }
   };
   public static final OutParameter REF = new OutParameter() {
      public int getType() {
         return 2006;
      }
   };
   public static final OutParameter SMALLINT = new OutParameter() {
      public int getType() {
         return 5;
      }
   };
   public static final OutParameter STRUCT = new OutParameter() {
      public int getType() {
         return 2002;
      }
   };
   public static final OutParameter TIME = new OutParameter() {
      public int getType() {
         return 92;
      }
   };
   public static final OutParameter TIMESTAMP = new OutParameter() {
      public int getType() {
         return 93;
      }
   };
   public static final OutParameter TINYINT = new OutParameter() {
      public int getType() {
         return -6;
      }
   };
   public static final OutParameter VARBINARY = new OutParameter() {
      public int getType() {
         return -3;
      }
   };
   public static final OutParameter VARCHAR = new OutParameter() {
      public int getType() {
         return 12;
      }
   };

   public static Sql newInstance(String url) throws SQLException {
      Connection connection = DriverManager.getConnection(url);
      return new Sql(connection);
   }

   public static Sql newInstance(String url, Properties properties) throws SQLException {
      Connection connection = DriverManager.getConnection(url, properties);
      return new Sql(connection);
   }

   public static Sql newInstance(String url, Properties properties, String driverClassName) throws SQLException, ClassNotFoundException {
      loadDriver(driverClassName);
      return newInstance(url, properties);
   }

   public static Sql newInstance(String url, String user, String password) throws SQLException {
      Connection connection = DriverManager.getConnection(url, user, password);
      return new Sql(connection);
   }

   public static Sql newInstance(String url, String user, String password, String driverClassName) throws SQLException, ClassNotFoundException {
      loadDriver(driverClassName);
      return newInstance(url, user, password);
   }

   public static Sql newInstance(String url, String driverClassName) throws SQLException, ClassNotFoundException {
      loadDriver(driverClassName);
      return newInstance(url);
   }

   public static Sql newInstance(Map<String, Object> args) throws SQLException, ClassNotFoundException {
      if (args.containsKey("driverClassName") && args.containsKey("driver")) {
         throw new IllegalArgumentException("Only one of 'driverClassName' and 'driver' should be provided");
      } else {
         Object driverClassName = args.remove("driverClassName");
         if (driverClassName == null) {
            driverClassName = args.remove("driver");
         }

         if (driverClassName != null) {
            loadDriver(driverClassName.toString());
         }

         Object url = args.remove("url");
         if (url == null) {
            throw new IllegalArgumentException("Argument 'url' is required");
         } else {
            Properties props = (Properties)args.remove("properties");
            if (props != null && args.containsKey("user")) {
               throw new IllegalArgumentException("Only one of 'properties' and 'user' should be supplied");
            } else if (props != null && args.containsKey("password")) {
               throw new IllegalArgumentException("Only one of 'properties' and 'password' should be supplied");
            } else if (args.containsKey("user") ^ args.containsKey("password")) {
               throw new IllegalArgumentException("Found one but not both of 'user' and 'password'");
            } else {
               Connection connection;
               if (props != null) {
                  connection = DriverManager.getConnection(url.toString(), props);
               } else if (args.containsKey("user")) {
                  Object user = args.remove("user");
                  Object password = args.remove("password");
                  connection = DriverManager.getConnection(url.toString(), user == null ? null : user.toString(), password == null ? null : password.toString());
               } else {
                  connection = DriverManager.getConnection(url.toString());
               }

               Sql result = (Sql)InvokerHelper.invokeConstructorOf((Class)Sql.class, args);
               result.setConnection(connection);
               return result;
            }
         }
      }
   }

   public int getResultSetType() {
      return this.resultSetType;
   }

   public void setResultSetType(int resultSetType) {
      this.resultSetType = resultSetType;
   }

   public int getResultSetConcurrency() {
      return this.resultSetConcurrency;
   }

   public void setResultSetConcurrency(int resultSetConcurrency) {
      this.resultSetConcurrency = resultSetConcurrency;
   }

   public int getResultSetHoldability() {
      return this.resultSetHoldability;
   }

   public void setResultSetHoldability(int resultSetHoldability) {
      this.resultSetHoldability = resultSetHoldability;
   }

   public static void loadDriver(String driverClassName) throws ClassNotFoundException {
      try {
         Class.forName(driverClassName);
      } catch (ClassNotFoundException var6) {
         try {
            Thread.currentThread().getContextClassLoader().loadClass(driverClassName);
         } catch (ClassNotFoundException var5) {
            try {
               Sql.class.getClassLoader().loadClass(driverClassName);
            } catch (ClassNotFoundException var4) {
               throw var6;
            }
         }
      }

   }

   public static InParameter ARRAY(Object value) {
      return in(2003, value);
   }

   public static InParameter BIGINT(Object value) {
      return in(-5, value);
   }

   public static InParameter BINARY(Object value) {
      return in(-2, value);
   }

   public static InParameter BIT(Object value) {
      return in(-7, value);
   }

   public static InParameter BLOB(Object value) {
      return in(2004, value);
   }

   public static InParameter BOOLEAN(Object value) {
      return in(16, value);
   }

   public static InParameter CHAR(Object value) {
      return in(1, value);
   }

   public static InParameter CLOB(Object value) {
      return in(2005, value);
   }

   public static InParameter DATALINK(Object value) {
      return in(70, value);
   }

   public static InParameter DATE(Object value) {
      return in(91, value);
   }

   public static InParameter DECIMAL(Object value) {
      return in(3, value);
   }

   public static InParameter DISTINCT(Object value) {
      return in(2001, value);
   }

   public static InParameter DOUBLE(Object value) {
      return in(8, value);
   }

   public static InParameter FLOAT(Object value) {
      return in(6, value);
   }

   public static InParameter INTEGER(Object value) {
      return in(4, value);
   }

   public static InParameter JAVA_OBJECT(Object value) {
      return in(2000, value);
   }

   public static InParameter LONGVARBINARY(Object value) {
      return in(-4, value);
   }

   public static InParameter LONGVARCHAR(Object value) {
      return in(-1, value);
   }

   public static InParameter NULL(Object value) {
      return in(0, value);
   }

   public static InParameter NUMERIC(Object value) {
      return in(2, value);
   }

   public static InParameter OTHER(Object value) {
      return in(1111, value);
   }

   public static InParameter REAL(Object value) {
      return in(7, value);
   }

   public static InParameter REF(Object value) {
      return in(2006, value);
   }

   public static InParameter SMALLINT(Object value) {
      return in(5, value);
   }

   public static InParameter STRUCT(Object value) {
      return in(2002, value);
   }

   public static InParameter TIME(Object value) {
      return in(92, value);
   }

   public static InParameter TIMESTAMP(Object value) {
      return in(93, value);
   }

   public static InParameter TINYINT(Object value) {
      return in(-6, value);
   }

   public static InParameter VARBINARY(Object value) {
      return in(-3, value);
   }

   public static InParameter VARCHAR(Object value) {
      return in(12, value);
   }

   public static InParameter in(final int type, final Object value) {
      return new InParameter() {
         public int getType() {
            return type;
         }

         public Object getValue() {
            return value;
         }
      };
   }

   public static OutParameter out(final int type) {
      return new OutParameter() {
         public int getType() {
            return type;
         }
      };
   }

   public static InOutParameter inout(final InParameter in) {
      return new InOutParameter() {
         public int getType() {
            return in.getType();
         }

         public Object getValue() {
            return in.getValue();
         }
      };
   }

   public static ResultSetOutParameter resultSet(final int type) {
      return new ResultSetOutParameter() {
         public int getType() {
            return type;
         }
      };
   }

   public static ExpandedVariable expand(final Object object) {
      return new ExpandedVariable() {
         public Object getObject() {
            return object;
         }
      };
   }

   public Sql(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public Sql(Connection connection) {
      if (connection == null) {
         throw new NullPointerException("Must specify a non-null Connection");
      } else {
         this.useConnection = connection;
      }
   }

   public Sql(Sql parent) {
      this.dataSource = parent.dataSource;
      this.useConnection = parent.useConnection;
   }

   private Sql() {
   }

   public DataSet dataSet(String table) {
      return new DataSet(this, table);
   }

   public DataSet dataSet(Class<?> type) {
      return new DataSet(this, type);
   }

   public void query(String sql, Closure closure) throws SQLException {
      Connection connection = this.createConnection();
      Statement statement = this.getStatement(connection, sql);
      ResultSet results = null;

      try {
         results = statement.executeQuery(sql);
         closure.call((Object)results);
      } catch (SQLException var10) {
         LOG.warning("Failed to execute: " + sql + " because: " + var10.getMessage());
         throw var10;
      } finally {
         this.closeResources(connection, statement, results);
      }

   }

   public void query(String sql, List<Object> params, Closure closure) throws SQLException {
      Connection connection = this.createConnection();
      PreparedStatement statement = null;
      ResultSet results = null;

      try {
         statement = this.getPreparedStatement(connection, sql, params);
         results = statement.executeQuery();
         closure.call((Object)results);
      } catch (SQLException var11) {
         LOG.warning("Failed to execute: " + sql + " because: " + var11.getMessage());
         throw var11;
      } finally {
         this.closeResources(connection, statement, results);
      }

   }

   public void query(GString gstring, Closure closure) throws SQLException {
      List<Object> params = this.getParameters(gstring);
      String sql = this.asSql(gstring, params);
      this.query(sql, params, closure);
   }

   public void eachRow(String sql, Closure closure) throws SQLException {
      this.eachRow(sql, (Closure)null, closure);
   }

   public void eachRow(String sql, Closure metaClosure, Closure rowClosure) throws SQLException {
      Connection connection = this.createConnection();
      Statement statement = this.getStatement(connection, sql);
      ResultSet results = null;

      try {
         results = statement.executeQuery(sql);
         if (metaClosure != null) {
            metaClosure.call((Object)results.getMetaData());
         }

         GroovyResultSet groovyRS = (new GroovyResultSetProxy(results)).getImpl();
         groovyRS.eachRow(rowClosure);
      } catch (SQLException var11) {
         LOG.warning("Failed to execute: " + sql + " because: " + var11.getMessage());
         throw var11;
      } finally {
         this.closeResources(connection, statement, results);
      }

   }

   public void eachRow(String sql, List<Object> params, Closure metaClosure, Closure closure) throws SQLException {
      Connection connection = this.createConnection();
      PreparedStatement statement = null;
      ResultSet results = null;

      try {
         statement = this.getPreparedStatement(connection, sql, params);
         results = statement.executeQuery();
         if (metaClosure != null) {
            metaClosure.call((Object)results.getMetaData());
         }

         GroovyResultSet groovyRS = (new GroovyResultSetProxy(results)).getImpl();

         while(groovyRS.next()) {
            closure.call((Object)groovyRS);
         }
      } catch (SQLException var12) {
         LOG.warning("Failed to execute: " + sql + " because: " + var12.getMessage());
         throw var12;
      } finally {
         this.closeResources(connection, statement, results);
      }

   }

   public void eachRow(String sql, List<Object> params, Closure closure) throws SQLException {
      this.eachRow(sql, params, (Closure)null, closure);
   }

   public void eachRow(GString gstring, Closure metaClosure, Closure closure) throws SQLException {
      List<Object> params = this.getParameters(gstring);
      String sql = this.asSql(gstring, params);
      this.eachRow(sql, params, metaClosure, closure);
   }

   public void eachRow(GString gstring, Closure closure) throws SQLException {
      this.eachRow((GString)gstring, (Closure)null, closure);
   }

   public List<GroovyRowResult> rows(String sql) throws SQLException {
      return this.rows(sql, (Closure)null);
   }

   public List<GroovyRowResult> rows(String sql, Closure metaClosure) throws SQLException {
      Sql.AbstractQueryCommand command = this.createQueryCommand(sql);
      ResultSet rs = null;

      List var6;
      try {
         rs = command.execute();
         List<GroovyRowResult> result = this.asList(sql, rs, metaClosure);
         rs = null;
         var6 = result;
      } finally {
         command.closeResources(rs);
      }

      return var6;
   }

   public List<GroovyRowResult> rows(String sql, List<Object> params) throws SQLException {
      return this.rows(sql, params, (Closure)null);
   }

   public List<GroovyRowResult> rows(String sql, Object[] params) throws SQLException {
      return this.rows(sql, Arrays.asList(params), (Closure)null);
   }

   public List<GroovyRowResult> rows(String sql, List<Object> params, Closure metaClosure) throws SQLException {
      Sql.AbstractQueryCommand command = this.createPreparedQueryCommand(sql, params);

      List var5;
      try {
         var5 = this.asList(sql, command.execute(), metaClosure);
      } finally {
         command.closeResources();
      }

      return var5;
   }

   public List<GroovyRowResult> rows(GString gstring) throws SQLException {
      return this.rows((GString)gstring, (Closure)null);
   }

   public List<GroovyRowResult> rows(GString gstring, Closure metaClosure) throws SQLException {
      List<Object> params = this.getParameters(gstring);
      String sql = this.asSql(gstring, params);
      return this.rows(sql, params, metaClosure);
   }

   public Object firstRow(String sql) throws SQLException {
      List<GroovyRowResult> rows = this.rows(sql);
      return rows.isEmpty() ? null : rows.get(0);
   }

   public Object firstRow(GString gstring) throws SQLException {
      List<Object> params = this.getParameters(gstring);
      String sql = this.asSql(gstring, params);
      return this.firstRow(sql, params);
   }

   public Object firstRow(String sql, List<Object> params) throws SQLException {
      List<GroovyRowResult> rows = this.rows(sql, params);
      return rows.isEmpty() ? null : rows.get(0);
   }

   public Object firstRow(String sql, Object[] params) throws SQLException {
      return this.firstRow(sql, Arrays.asList(params));
   }

   public boolean execute(String sql) throws SQLException {
      Connection connection = this.createConnection();
      Statement statement = null;

      boolean var5;
      try {
         statement = this.getStatement(connection, sql);
         boolean isResultSet = statement.execute(sql);
         this.updateCount = statement.getUpdateCount();
         var5 = isResultSet;
      } catch (SQLException var9) {
         LOG.warning("Failed to execute: " + sql + " because: " + var9.getMessage());
         throw var9;
      } finally {
         this.closeResources(connection, statement);
      }

      return var5;
   }

   public boolean execute(String sql, List<Object> params) throws SQLException {
      Connection connection = this.createConnection();
      PreparedStatement statement = null;

      boolean var6;
      try {
         statement = this.getPreparedStatement(connection, sql, params);
         boolean isResultSet = statement.execute();
         this.updateCount = statement.getUpdateCount();
         var6 = isResultSet;
      } catch (SQLException var10) {
         LOG.warning("Failed to execute: " + sql + " because: " + var10.getMessage());
         throw var10;
      } finally {
         this.closeResources(connection, statement);
      }

      return var6;
   }

   public boolean execute(String sql, Object[] params) throws SQLException {
      return this.execute(sql, Arrays.asList(params));
   }

   public boolean execute(GString gstring) throws SQLException {
      List<Object> params = this.getParameters(gstring);
      String sql = this.asSql(gstring, params);
      return this.execute(sql, params);
   }

   public List<List<Object>> executeInsert(String sql) throws SQLException {
      Connection connection = this.createConnection();
      Statement statement = null;

      List var5;
      try {
         statement = this.getStatement(connection, sql);
         this.updateCount = statement.executeUpdate(sql, 1);
         ResultSet keys = statement.getGeneratedKeys();
         var5 = this.calculateKeys(keys);
      } catch (SQLException var9) {
         LOG.warning("Failed to execute: " + sql + " because: " + var9.getMessage());
         throw var9;
      } finally {
         this.closeResources(connection, statement);
      }

      return var5;
   }

   public List<List<Object>> executeInsert(String sql, List<Object> params) throws SQLException {
      Connection connection = this.createConnection();
      PreparedStatement statement = null;

      List var6;
      try {
         statement = this.getPreparedStatement(connection, sql, params, 1);
         this.updateCount = statement.executeUpdate();
         ResultSet keys = statement.getGeneratedKeys();
         var6 = this.calculateKeys(keys);
      } catch (SQLException var10) {
         LOG.warning("Failed to execute: " + sql + " because: " + var10.getMessage());
         throw var10;
      } finally {
         this.closeResources(connection, statement);
      }

      return var6;
   }

   public List<List<Object>> executeInsert(String sql, Object[] params) throws SQLException {
      return this.executeInsert(sql, Arrays.asList(params));
   }

   public List<List<Object>> executeInsert(GString gstring) throws SQLException {
      List<Object> params = this.getParameters(gstring);
      String sql = this.asSql(gstring, params);
      return this.executeInsert(sql, params);
   }

   public int executeUpdate(String sql) throws SQLException {
      Connection connection = this.createConnection();
      Statement statement = null;

      int var4;
      try {
         statement = this.getStatement(connection, sql);
         this.updateCount = statement.executeUpdate(sql);
         var4 = this.updateCount;
      } catch (SQLException var8) {
         LOG.warning("Failed to execute: " + sql + " because: " + var8.getMessage());
         throw var8;
      } finally {
         this.closeResources(connection, statement);
      }

      return var4;
   }

   public int executeUpdate(String sql, List<Object> params) throws SQLException {
      Connection connection = this.createConnection();
      PreparedStatement statement = null;

      int var5;
      try {
         statement = this.getPreparedStatement(connection, sql, params);
         this.updateCount = statement.executeUpdate();
         var5 = this.updateCount;
      } catch (SQLException var9) {
         LOG.warning("Failed to execute: " + sql + " because: " + var9.getMessage());
         throw var9;
      } finally {
         this.closeResources(connection, statement);
      }

      return var5;
   }

   public int executeUpdate(String sql, Object[] params) throws SQLException {
      return this.executeUpdate(sql, Arrays.asList(params));
   }

   public int executeUpdate(GString gstring) throws SQLException {
      List<Object> params = this.getParameters(gstring);
      String sql = this.asSql(gstring, params);
      return this.executeUpdate(sql, params);
   }

   public int call(String sql) throws Exception {
      return this.call(sql, EMPTY_LIST);
   }

   public int call(GString gstring) throws Exception {
      List<Object> params = this.getParameters(gstring);
      String sql = this.asSql(gstring, params);
      return this.call(sql, params);
   }

   public int call(String sql, List<Object> params) throws Exception {
      Connection connection = this.createConnection();
      CallableStatement statement = connection.prepareCall(sql);

      int var5;
      try {
         LOG.fine(sql + " | " + params);
         this.setParameters(params, statement);
         this.configure(statement);
         var5 = statement.executeUpdate();
      } catch (SQLException var9) {
         LOG.warning("Failed to execute: " + sql + " because: " + var9.getMessage());
         throw var9;
      } finally {
         this.closeResources(connection, statement);
      }

      return var5;
   }

   public int call(String sql, Object[] params) throws Exception {
      return this.call(sql, Arrays.asList(params));
   }

   public void call(String sql, List<Object> params, Closure closure) throws Exception {
      Connection connection = this.createConnection();
      CallableStatement statement = connection.prepareCall(sql);
      ArrayList resultSetResources = new ArrayList();
      boolean var19 = false;

      try {
         var19 = true;
         LOG.fine(sql + " | " + params);
         this.setParameters(params, statement);
         statement.execute();
         List<Object> results = new ArrayList();
         int indx = 0;
         int inouts = 0;
         Iterator i$ = params.iterator();

         while(true) {
            if (!i$.hasNext()) {
               closure.call(results.toArray(new Object[inouts]));
               var19 = false;
               break;
            }

            Object value = i$.next();
            if (value instanceof OutParameter) {
               if (value instanceof ResultSetOutParameter) {
                  GroovyResultSet resultSet = CallResultSet.getImpl(statement, indx);
                  resultSetResources.add(resultSet);
                  results.add(resultSet);
               } else {
                  Object o = statement.getObject(indx + 1);
                  if (o instanceof ResultSet) {
                     GroovyResultSet resultSet = (new GroovyResultSetProxy((ResultSet)o)).getImpl();
                     results.add(resultSet);
                     resultSetResources.add(resultSet);
                  } else {
                     results.add(o);
                  }
               }

               ++inouts;
            }

            ++indx;
         }
      } catch (SQLException var20) {
         LOG.warning("Failed to execute: " + sql + " because: " + var20.getMessage());
         throw var20;
      } finally {
         if (var19) {
            this.closeResources(connection, statement);
            Iterator i$ = resultSetResources.iterator();

            while(i$.hasNext()) {
               GroovyResultSet rs = (GroovyResultSet)i$.next();
               this.closeResources((Connection)null, (Statement)null, rs);
            }

         }
      }

      this.closeResources(connection, statement);
      Iterator i$ = resultSetResources.iterator();

      while(i$.hasNext()) {
         GroovyResultSet rs = (GroovyResultSet)i$.next();
         this.closeResources((Connection)null, (Statement)null, rs);
      }

   }

   public void call(GString gstring, Closure closure) throws Exception {
      List<Object> params = this.getParameters(gstring);
      String sql = this.asSql(gstring, params);
      this.call(sql, params, closure);
   }

   public void close() {
      this.namedParamSqlCache.clear();
      this.namedParamIndexPropCache.clear();
      this.clearStatementCache();
      if (this.useConnection != null) {
         try {
            this.useConnection.close();
         } catch (SQLException var2) {
            LOG.finest("Caught exception closing connection: " + var2.getMessage());
         }
      }

   }

   public DataSource getDataSource() {
      return this.dataSource;
   }

   public void commit() throws SQLException {
      if (this.useConnection == null) {
         LOG.info("Commit operation not supported when using datasets unless using withTransaction or cacheConnection - attempt to commit ignored");
      } else {
         try {
            this.useConnection.commit();
         } catch (SQLException var2) {
            LOG.warning("Caught exception committing connection: " + var2.getMessage());
            throw var2;
         }
      }
   }

   public void rollback() throws SQLException {
      if (this.useConnection == null) {
         LOG.info("Rollback operation not supported when using datasets unless using withTransaction or cacheConnection - attempt to rollback ignored");
      } else {
         try {
            this.useConnection.rollback();
         } catch (SQLException var2) {
            LOG.warning("Caught exception rolling back connection: " + var2.getMessage());
            throw var2;
         }
      }
   }

   public int getUpdateCount() {
      return this.updateCount;
   }

   public Connection getConnection() {
      return this.useConnection;
   }

   private void setConnection(Connection connection) {
      this.useConnection = connection;
   }

   public void withStatement(Closure configureStatement) {
      this.configureStatement = configureStatement;
   }

   public synchronized void setCacheStatements(boolean cacheStatements) {
      this.cacheStatements = cacheStatements;
      if (!cacheStatements) {
         this.clearStatementCache();
      }

   }

   public boolean isCacheStatements() {
      return this.cacheStatements;
   }

   public synchronized void cacheConnection(Closure closure) throws SQLException {
      boolean savedCacheConnection = this.cacheConnection;
      this.cacheConnection = true;
      Connection connection = null;

      try {
         connection = this.createConnection();
         this.callClosurePossiblyWithConnection(closure, connection);
      } finally {
         this.cacheConnection = false;
         this.closeResources(connection, (Statement)null);
         this.cacheConnection = savedCacheConnection;
         if (this.dataSource != null && !this.cacheConnection) {
            this.useConnection = null;
         }

      }

   }

   public synchronized void withTransaction(Closure closure) throws SQLException {
      boolean savedCacheConnection = this.cacheConnection;
      this.cacheConnection = true;
      Connection connection = null;
      boolean savedAutoCommit = true;

      try {
         connection = this.createConnection();
         savedAutoCommit = connection.getAutoCommit();
         connection.setAutoCommit(false);
         this.callClosurePossiblyWithConnection(closure, connection);
         connection.commit();
      } catch (SQLException var11) {
         this.handleError(connection, var11);
         throw var11;
      } catch (RuntimeException var12) {
         this.handleError(connection, var12);
         throw var12;
      } catch (Error var13) {
         this.handleError(connection, var13);
         throw var13;
      } finally {
         if (connection != null) {
            connection.setAutoCommit(savedAutoCommit);
         }

         this.cacheConnection = false;
         this.closeResources(connection, (Statement)null);
         this.cacheConnection = savedCacheConnection;
         if (this.dataSource != null && !this.cacheConnection) {
            this.useConnection = null;
         }

      }

   }

   public boolean isWithinBatch() {
      return this.withinBatch;
   }

   public synchronized int[] withBatch(Closure closure) throws SQLException {
      return this.withBatch(0, closure);
   }

   public synchronized int[] withBatch(int batchSize, Closure closure) throws SQLException {
      boolean savedCacheConnection = this.cacheConnection;
      this.cacheConnection = true;
      Connection connection = null;
      BatchingStatementWrapper statement = null;
      boolean savedAutoCommit = true;
      boolean savedWithinBatch = this.withinBatch;

      int[] var9;
      try {
         this.withinBatch = true;
         connection = this.createConnection();
         savedAutoCommit = connection.getAutoCommit();
         connection.setAutoCommit(false);
         statement = new BatchingStatementWrapper(this.createStatement(connection), batchSize, LOG, connection);
         closure.call((Object)statement);
         int[] result = statement.executeBatch();
         connection.commit();
         var9 = result;
      } catch (SQLException var15) {
         this.handleError(connection, var15);
         throw var15;
      } catch (RuntimeException var16) {
         this.handleError(connection, var16);
         throw var16;
      } catch (Error var17) {
         this.handleError(connection, var17);
         throw var17;
      } finally {
         if (connection != null) {
            connection.setAutoCommit(savedAutoCommit);
         }

         this.cacheConnection = false;
         this.closeResources(statement);
         this.closeResources(connection);
         this.cacheConnection = savedCacheConnection;
         this.withinBatch = savedWithinBatch;
         if (this.dataSource != null && !this.cacheConnection) {
            this.useConnection = null;
         }

      }

      return var9;
   }

   public synchronized void cacheStatements(Closure closure) throws SQLException {
      boolean savedCacheStatements = this.cacheStatements;
      this.cacheStatements = true;
      Connection connection = null;

      try {
         connection = this.createConnection();
         this.callClosurePossiblyWithConnection(closure, connection);
      } finally {
         this.cacheStatements = false;
         this.closeResources(connection, (Statement)null);
         this.cacheStatements = savedCacheStatements;
      }

   }

   protected final ResultSet executeQuery(String sql) throws SQLException {
      Sql.AbstractQueryCommand command = this.createQueryCommand(sql);
      ResultSet rs = null;

      try {
         rs = command.execute();
      } finally {
         command.closeResources();
      }

      return rs;
   }

   protected final ResultSet executePreparedQuery(String sql, List<Object> params) throws SQLException {
      Sql.AbstractQueryCommand command = this.createPreparedQueryCommand(sql, params);
      ResultSet rs = null;

      try {
         rs = command.execute();
      } finally {
         command.closeResources();
      }

      return rs;
   }

   protected List<GroovyRowResult> asList(String sql, ResultSet rs) throws SQLException {
      return this.asList(sql, rs, (Closure)null);
   }

   protected List<GroovyRowResult> asList(String sql, ResultSet rs, Closure metaClosure) throws SQLException {
      ArrayList results = new ArrayList();

      try {
         if (metaClosure != null) {
            metaClosure.call((Object)rs.getMetaData());
         }

         while(rs.next()) {
            results.add(SqlGroovyMethods.toRowResult(rs));
         }

         ArrayList var5 = results;
         return var5;
      } catch (SQLException var9) {
         LOG.warning("Failed to retrieve row from ResultSet for: " + sql + " because: " + var9.getMessage());
         throw var9;
      } finally {
         rs.close();
      }
   }

   protected String asSql(GString gstring, List<Object> values) {
      String[] strings = gstring.getStrings();
      if (strings.length <= 0) {
         throw new IllegalArgumentException("No SQL specified in GString: " + gstring);
      } else {
         boolean nulls = false;
         StringBuffer buffer = new StringBuffer();
         boolean warned = false;
         Iterator<Object> iter = values.iterator();

         for(int i = 0; i < strings.length; ++i) {
            String text = strings[i];
            if (text != null) {
               buffer.append(text);
            }

            if (iter.hasNext()) {
               Object value = iter.next();
               if (value != null) {
                  if (value instanceof ExpandedVariable) {
                     buffer.append(((ExpandedVariable)value).getObject());
                     iter.remove();
                  } else {
                     boolean validBinding = true;
                     if (i < strings.length - 1) {
                        String nextText = strings[i + 1];
                        if ((text.endsWith("\"") || text.endsWith("'")) && (nextText.startsWith("'") || nextText.startsWith("\""))) {
                           if (!warned) {
                              LOG.warning("In Groovy SQL please do not use quotes around dynamic expressions (which start with $) as this means we cannot use a JDBC PreparedStatement and so is a security hole. Groovy has worked around your mistake but the security hole is still there. The expression so far is: " + buffer.toString() + "?" + nextText);
                              warned = true;
                           }

                           buffer.append(value);
                           iter.remove();
                           validBinding = false;
                        }
                     }

                     if (validBinding) {
                        buffer.append("?");
                     }
                  }
               } else {
                  nulls = true;
                  iter.remove();
                  buffer.append("?'\"?");
               }
            }
         }

         String sql = buffer.toString();
         if (nulls) {
            sql = this.nullify(sql);
         }

         return sql;
      }
   }

   protected String nullify(String sql) {
      int firstWhere = this.findWhereKeyword(sql);
      if (firstWhere >= 0) {
         Pattern[] patterns = new Pattern[]{Pattern.compile("(?is)^(.{" + firstWhere + "}.*?)!=\\s{0,1}(\\s*)\\?'\"\\?(.*)"), Pattern.compile("(?is)^(.{" + firstWhere + "}.*?)<>\\s{0,1}(\\s*)\\?'\"\\?(.*)"), Pattern.compile("(?is)^(.{" + firstWhere + "}.*?[^<>])=\\s{0,1}(\\s*)\\?'\"\\?(.*)")};
         String[] replacements = new String[]{"$1 is not $2null$3", "$1 is not $2null$3", "$1 is $2null$3"};

         for(int i = 0; i < patterns.length; ++i) {
            for(Matcher matcher = patterns[i].matcher(sql); matcher.matches(); matcher = patterns[i].matcher(sql)) {
               sql = matcher.replaceAll(replacements[i]);
            }
         }
      }

      return sql.replaceAll("\\?'\"\\?", "null");
   }

   protected int findWhereKeyword(String sql) {
      char[] chars = sql.toLowerCase().toCharArray();
      char[] whereChars = "where".toCharArray();
      int i = 0;
      boolean inString = false;

      for(int inWhere = 0; i < chars.length; ++i) {
         switch(chars[i]) {
         case '\'':
            inString = !inString;
            break;
         default:
            if (!inString && chars[i] == whereChars[inWhere]) {
               ++inWhere;
               if (inWhere == whereChars.length) {
                  return i;
               }
            }
         }
      }

      return -1;
   }

   protected List<Object> getParameters(GString gstring) {
      return new ArrayList(Arrays.asList(gstring.getValues()));
   }

   protected void setParameters(List<Object> params, PreparedStatement statement) throws SQLException {
      int i = 1;
      Iterator i$ = params.iterator();

      while(i$.hasNext()) {
         Object value = i$.next();
         this.setObject(statement, i++, value);
      }

   }

   protected void setObject(PreparedStatement statement, int i, Object value) throws SQLException {
      if (!(value instanceof InParameter) && !(value instanceof OutParameter)) {
         statement.setObject(i, value);
      } else {
         if (value instanceof InParameter) {
            InParameter in = (InParameter)value;
            Object val = in.getValue();
            if (null == val) {
               statement.setNull(i, in.getType());
            } else {
               statement.setObject(i, val, in.getType());
            }
         }

         if (value instanceof OutParameter) {
            try {
               OutParameter out = (OutParameter)value;
               ((CallableStatement)statement).registerOutParameter(i, out.getType());
            } catch (ClassCastException var6) {
               throw new SQLException("Cannot register out parameter.");
            }
         }
      }

   }

   protected Connection createConnection() throws SQLException {
      if ((this.cacheStatements || this.cacheConnection) && this.useConnection != null) {
         return this.useConnection;
      } else if (this.dataSource != null) {
         Connection con;
         try {
            con = (Connection)AccessController.doPrivileged(new PrivilegedExceptionAction<Connection>() {
               public Connection run() throws SQLException {
                  return Sql.this.dataSource.getConnection();
               }
            });
         } catch (PrivilegedActionException var4) {
            Exception e = var4.getException();
            if (e instanceof SQLException) {
               throw (SQLException)e;
            }

            throw (RuntimeException)e;
         }

         if (this.cacheStatements || this.cacheConnection) {
            this.useConnection = con;
         }

         return con;
      } else {
         return this.useConnection;
      }
   }

   protected void closeResources(Connection connection, Statement statement, ResultSet results) {
      if (results != null) {
         try {
            results.close();
         } catch (SQLException var5) {
            LOG.finest("Caught exception closing resultSet: " + var5.getMessage() + " - continuing");
         }
      }

      this.closeResources(connection, statement);
   }

   protected void closeResources(Connection connection, Statement statement) {
      if (!this.cacheStatements) {
         if (statement != null) {
            try {
               statement.close();
            } catch (SQLException var4) {
               LOG.finest("Caught exception closing statement: " + var4.getMessage() + " - continuing");
            }
         }

         this.closeResources(connection);
      }
   }

   private void closeResources(BatchingStatementWrapper statement) {
      if (!this.cacheStatements) {
         if (statement != null) {
            try {
               statement.close();
            } catch (SQLException var3) {
               LOG.finest("Caught exception closing statement: " + var3.getMessage() + " - continuing");
            }
         }

      }
   }

   protected void closeResources(Connection connection) {
      if (!this.cacheConnection) {
         if (connection != null && this.dataSource != null) {
            try {
               connection.close();
            } catch (SQLException var3) {
               LOG.finest("Caught exception closing connection: " + var3.getMessage() + " - continuing");
            }
         }

      }
   }

   protected void configure(Statement statement) {
      Closure configureStatement = this.configureStatement;
      if (configureStatement != null) {
         configureStatement.call((Object)statement);
      }

   }

   private List<List<Object>> calculateKeys(ResultSet keys) throws SQLException {
      List<List<Object>> autoKeys = new ArrayList();
      int count = keys.getMetaData().getColumnCount();

      while(keys.next()) {
         List<Object> rowKeys = new ArrayList(count);

         for(int i = 1; i <= count; ++i) {
            rowKeys.add(keys.getObject(i));
         }

         autoKeys.add(rowKeys);
      }

      return autoKeys;
   }

   private Statement createStatement(Connection connection) throws SQLException {
      return this.resultSetHoldability == -1 ? connection.createStatement(this.resultSetType, this.resultSetConcurrency) : connection.createStatement(this.resultSetType, this.resultSetConcurrency, this.resultSetHoldability);
   }

   private void handleError(Connection connection, Throwable t) throws SQLException {
      if (connection != null) {
         LOG.warning("Rolling back due to: " + t.getMessage());
         connection.rollback();
      }

   }

   private void callClosurePossiblyWithConnection(Closure closure, Connection connection) {
      if (closure.getMaximumNumberOfParameters() == 1) {
         closure.call((Object)connection);
      } else {
         closure.call();
      }

   }

   private void clearStatementCache() {
      Statement[] statements;
      synchronized(this.statementCache) {
         if (this.statementCache.isEmpty()) {
            return;
         }

         statements = new Statement[this.statementCache.size()];
         this.statementCache.values().toArray(statements);
         this.statementCache.clear();
      }

      Statement[] arr$ = statements;
      int len$ = statements.length;

      for(int i$ = 0; i$ < len$; ++i$) {
         Statement s = arr$[i$];

         try {
            s.close();
         } catch (Exception var7) {
            LOG.info("Failed to close statement. Already closed? Exception message: " + var7.getMessage());
         }
      }

   }

   private Statement getAbstractStatement(Sql.AbstractStatementCommand cmd, Connection connection, String sql) throws SQLException {
      Statement stmt;
      if (this.cacheStatements) {
         synchronized(this.statementCache) {
            stmt = (Statement)this.statementCache.get(sql);
            if (stmt == null) {
               stmt = cmd.execute(connection, sql);
               this.statementCache.put(sql, stmt);
            }
         }
      } else {
         stmt = cmd.execute(connection, sql);
      }

      return stmt;
   }

   private Statement getStatement(Connection connection, String sql) throws SQLException {
      LOG.fine(sql);
      Statement stmt = this.getAbstractStatement(new Sql.CreateStatementCommand(), connection, sql);
      this.configure(stmt);
      return stmt;
   }

   private PreparedStatement getPreparedStatement(Connection connection, String sql, List<Object> params, int returnGeneratedKeys) throws SQLException {
      SqlWithParams updated = this.checkForNamedParams(sql, params);
      LOG.fine(updated.getSql() + " | " + updated.getParams());
      PreparedStatement statement = (PreparedStatement)this.getAbstractStatement(new Sql.CreatePreparedStatementCommand(returnGeneratedKeys), connection, updated.getSql());
      this.setParameters(updated.getParams(), statement);
      this.configure(statement);
      return statement;
   }

   public SqlWithParams checkForNamedParams(String sql, List<Object> params) {
      if (this.enableNamedQueries && NAMED_QUERY_PATTERN.matcher(sql).find()) {
         String newSql;
         Object indexPropList;
         int i;
         if (this.cacheNamedQueries && this.namedParamSqlCache.containsKey(sql)) {
            newSql = (String)this.namedParamSqlCache.get(sql);
            indexPropList = (List)this.namedParamIndexPropCache.get(sql);
         } else {
            indexPropList = new ArrayList();
            StringBuilder sb = new StringBuilder();
            StringBuilder currentChunk = new StringBuilder();
            char[] chars = sql.toCharArray();
            i = 0;

            boolean inString;
            for(inString = false; i < chars.length; ++i) {
               switch(chars[i]) {
               case '\'':
                  inString = !inString;
                  if (inString) {
                     sb.append(this.adaptForNamedParams(currentChunk.toString(), (List)indexPropList));
                     currentChunk = new StringBuilder();
                     currentChunk.append(chars[i]);
                  } else {
                     currentChunk.append(chars[i]);
                     sb.append(currentChunk);
                     currentChunk = new StringBuilder();
                  }
                  break;
               default:
                  currentChunk.append(chars[i]);
               }
            }

            if (inString) {
               throw new IllegalStateException("Failed to process query. Unterminated ' character?");
            }

            sb.append(this.adaptForNamedParams(currentChunk.toString(), (List)indexPropList));
            newSql = sb.toString();
            this.namedParamSqlCache.put(sql, newSql);
            this.namedParamIndexPropCache.put(sql, indexPropList);
         }

         if (sql.equals(newSql)) {
            return new SqlWithParams(sql, params);
         } else {
            List<Object> updatedParams = new ArrayList();
            Iterator i$ = ((List)indexPropList).iterator();

            while(i$.hasNext()) {
               Tuple tuple = (Tuple)i$.next();
               i = (Integer)tuple.get(0);
               String prop = (String)tuple.get(1);
               if (i < 0 || i >= params.size()) {
                  throw new IllegalArgumentException("Invalid index " + i + " should be in range 1.." + params.size());
               }

               updatedParams.add(InvokerHelper.getProperty(params.get(i), prop));
            }

            return new SqlWithParams(newSql, updatedParams);
         }
      } else {
         return new SqlWithParams(sql, params);
      }
   }

   private String adaptForNamedParams(String sql, List<Tuple> indexPropList) {
      StringBuilder newSql = new StringBuilder();
      int txtIndex = 0;

      for(Matcher matcher = NAMED_QUERY_PATTERN.matcher(sql); matcher.find(); txtIndex = matcher.end()) {
         newSql.append(sql.substring(txtIndex, matcher.start())).append('?');
         String indexStr = matcher.group(1);
         int index = indexStr != null && indexStr.length() != 0 ? new Integer(matcher.group(1)) - 1 : 0;
         String prop = matcher.group(2);
         indexPropList.add(new Tuple(new Object[]{index, prop}));
      }

      newSql.append(sql.substring(txtIndex));
      return newSql.toString();
   }

   private PreparedStatement getPreparedStatement(Connection connection, String sql, List<Object> params) throws SQLException {
      return this.getPreparedStatement(connection, sql, params, 0);
   }

   public boolean isCacheNamedQueries() {
      return this.cacheNamedQueries;
   }

   public void setCacheNamedQueries(boolean cacheNamedQueries) {
      this.cacheNamedQueries = cacheNamedQueries;
   }

   public boolean isEnableNamedQueries() {
      return this.enableNamedQueries;
   }

   public void setEnableNamedQueries(boolean enableNamedQueries) {
      this.enableNamedQueries = enableNamedQueries;
   }

   protected Sql.AbstractQueryCommand createQueryCommand(String sql) {
      return new Sql.QueryCommand(sql);
   }

   protected Sql.AbstractQueryCommand createPreparedQueryCommand(String sql, List<Object> queryParams) {
      return new Sql.PreparedQueryCommand(sql, queryParams);
   }

   protected void setInternalConnection(Connection conn) {
   }

   protected final class QueryCommand extends Sql.AbstractQueryCommand {
      QueryCommand(String sql) {
         super(sql);
      }

      protected ResultSet runQuery(Connection connection) throws SQLException {
         this.statement = Sql.this.getStatement(connection, this.sql);
         return this.statement.executeQuery(this.sql);
      }
   }

   protected final class PreparedQueryCommand extends Sql.AbstractQueryCommand {
      private List<Object> params;

      PreparedQueryCommand(String sql, List<Object> queryParams) {
         super(sql);
         this.params = queryParams;
      }

      protected ResultSet runQuery(Connection connection) throws SQLException {
         PreparedStatement s = Sql.this.getPreparedStatement(connection, this.sql, this.params);
         this.statement = s;
         return s.executeQuery();
      }
   }

   protected abstract class AbstractQueryCommand {
      protected final String sql;
      protected Statement statement;
      private Connection connection;

      AbstractQueryCommand(String sql) {
         this.sql = sql;
      }

      final ResultSet execute() throws SQLException {
         this.connection = Sql.this.createConnection();
         Sql.this.setInternalConnection(this.connection);
         this.statement = null;

         try {
            ResultSet result = this.runQuery(this.connection);

            assert null != this.statement;

            return result;
         } catch (SQLException var2) {
            Sql.LOG.warning("Failed to execute: " + this.sql + " because: " + var2.getMessage());
            this.closeResources();
            this.connection = null;
            this.statement = null;
            throw var2;
         }
      }

      public final void closeResources() {
         Sql.this.closeResources(this.connection, this.statement);
      }

      public final void closeResources(ResultSet rs) {
         Sql.this.closeResources(this.connection, this.statement, rs);
      }

      protected abstract ResultSet runQuery(Connection var1) throws SQLException;
   }

   private class CreateStatementCommand extends Sql.AbstractStatementCommand {
      private CreateStatementCommand() {
         super(null);
      }

      Statement execute(Connection conn, String sql) throws SQLException {
         return Sql.this.createStatement(conn);
      }

      // $FF: synthetic method
      CreateStatementCommand(Object x1) {
         this();
      }
   }

   private class CreatePreparedStatementCommand extends Sql.AbstractStatementCommand {
      private final int returnGeneratedKeys;

      CreatePreparedStatementCommand(int returnGeneratedKeys) {
         super(null);
         this.returnGeneratedKeys = returnGeneratedKeys;
      }

      PreparedStatement execute(Connection connection, String sql) throws SQLException {
         if (this.returnGeneratedKeys != 0) {
            return connection.prepareStatement(sql, this.returnGeneratedKeys);
         } else {
            return (PreparedStatement)(this.appearsLikeStoredProc(sql) ? connection.prepareCall(sql) : connection.prepareStatement(sql));
         }
      }

      boolean appearsLikeStoredProc(String sql) {
         return sql.matches("\\s*[{]?\\s*[?]?\\s*[=]?\\s*[cC][aA][lL][lL].*");
      }
   }

   private abstract class AbstractStatementCommand {
      private AbstractStatementCommand() {
      }

      abstract Statement execute(Connection var1, String var2) throws SQLException;

      // $FF: synthetic method
      AbstractStatementCommand(Object x1) {
         this();
      }
   }
}
