package org.apache.velocity.runtime.resource.loader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.util.ExceptionUtils;
import org.apache.velocity.util.StringUtils;

public class DataSourceResourceLoader extends ResourceLoader {
   private String dataSourceName;
   private String tableName;
   private String keyColumn;
   private String templateColumn;
   private String timestampColumn;
   private InitialContext ctx;
   private DataSource dataSource;

   public void init(ExtendedProperties configuration) {
      this.dataSourceName = StringUtils.nullTrim(configuration.getString("resource.datasource"));
      this.tableName = StringUtils.nullTrim(configuration.getString("resource.table"));
      this.keyColumn = StringUtils.nullTrim(configuration.getString("resource.keycolumn"));
      this.templateColumn = StringUtils.nullTrim(configuration.getString("resource.templatecolumn"));
      this.timestampColumn = StringUtils.nullTrim(configuration.getString("resource.timestampcolumn"));
      if (this.dataSource != null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("DataSourceResourceLoader: using dataSource instance with table \"" + this.tableName + "\"");
            this.log.debug("DataSourceResourceLoader: using columns \"" + this.keyColumn + "\", \"" + this.templateColumn + "\" and \"" + this.timestampColumn + "\"");
         }

         this.log.trace("DataSourceResourceLoader initialized.");
      } else if (this.dataSourceName != null) {
         if (this.log.isDebugEnabled()) {
            this.log.debug("DataSourceResourceLoader: using \"" + this.dataSourceName + "\" datasource with table \"" + this.tableName + "\"");
            this.log.debug("DataSourceResourceLoader: using columns \"" + this.keyColumn + "\", \"" + this.templateColumn + "\" and \"" + this.timestampColumn + "\"");
         }

         this.log.trace("DataSourceResourceLoader initialized.");
      } else {
         this.log.warn("DataSourceResourceLoader not properly initialized. No DataSource was identified.");
      }

   }

   public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public boolean isSourceModified(Resource resource) {
      return resource.getLastModified() != this.readLastModified(resource, "checking timestamp");
   }

   public long getLastModified(Resource resource) {
      return this.readLastModified(resource, "getting timestamp");
   }

   public synchronized InputStream getResourceStream(String name) throws ResourceNotFoundException {
      if (org.apache.commons.lang.StringUtils.isEmpty(name)) {
         throw new ResourceNotFoundException("DataSourceResourceLoader: Template name was empty or null");
      } else {
         Connection conn = null;
         ResultSet rs = null;

         BufferedInputStream var13;
         try {
            String msg;
            try {
               conn = this.openDbConnection();
               rs = this.readData(conn, this.templateColumn, name);
               if (!rs.next()) {
                  throw new ResourceNotFoundException("DataSourceResourceLoader: could not find resource '" + name + "'");
               }

               InputStream ascStream = rs.getAsciiStream(this.templateColumn);
               if (ascStream == null) {
                  throw new ResourceNotFoundException("DataSourceResourceLoader: template column for '" + name + "' is null");
               }

               var13 = new BufferedInputStream(ascStream);
            } catch (SQLException var10) {
               msg = "DataSourceResourceLoader: database problem while getting resource '" + name + "': ";
               this.log.error(msg, var10);
               throw new ResourceNotFoundException(msg);
            } catch (NamingException var11) {
               msg = "DataSourceResourceLoader: database problem while getting resource '" + name + "': ";
               this.log.error(msg, var11);
               throw new ResourceNotFoundException(msg);
            }
         } finally {
            this.closeResultSet(rs);
            this.closeDbConnection(conn);
         }

         return var13;
      }
   }

   private long readLastModified(Resource resource, String operation) {
      long timeStamp = 0L;
      String name = resource.getName();
      if (name != null && name.length() != 0) {
         Connection conn = null;
         ResultSet rs = null;

         try {
            String msg;
            try {
               conn = this.openDbConnection();
               rs = this.readData(conn, this.timestampColumn, name);
               if (rs.next()) {
                  Timestamp ts = rs.getTimestamp(this.timestampColumn);
                  timeStamp = ts != null ? ts.getTime() : 0L;
               } else {
                  this.log.error("DataSourceResourceLoader: could not find resource " + name + " while " + operation);
               }
            } catch (SQLException var14) {
               msg = "DataSourceResourceLoader: database problem while " + operation + " of '" + name + "': ";
               this.log.error(msg, var14);
               throw ExceptionUtils.createRuntimeException(msg, var14);
            } catch (NamingException var15) {
               msg = "DataSourceResourceLoader: database problem while " + operation + " of '" + name + "': ";
               this.log.error(msg, var15);
               throw ExceptionUtils.createRuntimeException(msg, var15);
            }
         } finally {
            this.closeResultSet(rs);
            this.closeDbConnection(conn);
         }
      } else {
         this.log.error("DataSourceResourceLoader: Template name was empty or null");
      }

      return timeStamp;
   }

   private Connection openDbConnection() throws NamingException, SQLException {
      if (this.dataSource != null) {
         return this.dataSource.getConnection();
      } else {
         if (this.ctx == null) {
            this.ctx = new InitialContext();
         }

         this.dataSource = (DataSource)this.ctx.lookup(this.dataSourceName);
         return this.dataSource.getConnection();
      }
   }

   private void closeDbConnection(Connection conn) {
      if (conn != null) {
         try {
            conn.close();
         } catch (RuntimeException var3) {
            throw var3;
         } catch (Exception var4) {
            this.log.warn("DataSourceResourceLoader: problem when closing connection", var4);
         }
      }

   }

   private void closeResultSet(ResultSet rs) {
      if (rs != null) {
         try {
            rs.close();
         } catch (RuntimeException var3) {
            throw var3;
         } catch (Exception var4) {
            this.log.warn("DataSourceResourceLoader: problem when closing result set: ", var4);
         }
      }

   }

   private ResultSet readData(Connection conn, String columnNames, String templateName) throws SQLException {
      PreparedStatement ps = conn.prepareStatement("SELECT " + columnNames + " FROM " + this.tableName + " WHERE " + this.keyColumn + " = ?");
      ps.setString(1, templateName);
      return ps.executeQuery();
   }
}
