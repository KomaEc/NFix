package org.apache.tools.ant.taskdefs;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public abstract class JDBCTask extends Task {
   private static Hashtable loaderMap = new Hashtable(3);
   private boolean caching = true;
   private Path classpath;
   private AntClassLoader loader;
   private boolean autocommit = false;
   private String driver = null;
   private String url = null;
   private String userId = null;
   private String password = null;
   private String rdbms = null;
   private String version = null;

   public void setClasspath(Path classpath) {
      this.classpath = classpath;
   }

   public void setCaching(boolean enable) {
      this.caching = enable;
   }

   public Path createClasspath() {
      if (this.classpath == null) {
         this.classpath = new Path(this.getProject());
      }

      return this.classpath.createPath();
   }

   public void setClasspathRef(Reference r) {
      this.createClasspath().setRefid(r);
   }

   public void setDriver(String driver) {
      this.driver = driver.trim();
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public void setAutocommit(boolean autocommit) {
      this.autocommit = autocommit;
   }

   public void setRdbms(String rdbms) {
      this.rdbms = rdbms;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   protected boolean isValidRdbms(Connection conn) {
      if (this.rdbms == null && this.version == null) {
         return true;
      } else {
         try {
            DatabaseMetaData dmd = conn.getMetaData();
            String theVersion;
            if (this.rdbms != null) {
               theVersion = dmd.getDatabaseProductName().toLowerCase();
               this.log("RDBMS = " + theVersion, 3);
               if (theVersion == null || theVersion.indexOf(this.rdbms) < 0) {
                  this.log("Not the required RDBMS: " + this.rdbms, 3);
                  return false;
               }
            }

            if (this.version != null) {
               theVersion = dmd.getDatabaseProductVersion().toLowerCase(Locale.ENGLISH);
               this.log("Version = " + theVersion, 3);
               if (theVersion == null || !theVersion.startsWith(this.version) && theVersion.indexOf(" " + this.version) < 0) {
                  this.log("Not the required version: \"" + this.version + "\"", 3);
                  return false;
               }
            }

            return true;
         } catch (SQLException var4) {
            this.log("Failed to obtain required RDBMS information", 0);
            return false;
         }
      }
   }

   protected static Hashtable getLoaderMap() {
      return loaderMap;
   }

   protected AntClassLoader getLoader() {
      return this.loader;
   }

   protected Connection getConnection() throws BuildException {
      if (this.userId == null) {
         throw new BuildException("UserId attribute must be set!", this.getLocation());
      } else if (this.password == null) {
         throw new BuildException("Password attribute must be set!", this.getLocation());
      } else if (this.url == null) {
         throw new BuildException("Url attribute must be set!", this.getLocation());
      } else {
         try {
            this.log("connecting to " + this.getUrl(), 3);
            Properties info = new Properties();
            info.put("user", this.getUserId());
            info.put("password", this.getPassword());
            Connection conn = this.getDriver().connect(this.getUrl(), info);
            if (conn == null) {
               throw new SQLException("No suitable Driver for " + this.url);
            } else {
               conn.setAutoCommit(this.autocommit);
               return conn;
            }
         } catch (SQLException var3) {
            throw new BuildException(var3, this.getLocation());
         }
      }
   }

   private Driver getDriver() throws BuildException {
      if (this.driver == null) {
         throw new BuildException("Driver attribute must be set!", this.getLocation());
      } else {
         Driver driverInstance = null;

         try {
            Class dc;
            if (this.classpath != null) {
               synchronized(loaderMap) {
                  if (this.caching) {
                     this.loader = (AntClassLoader)loaderMap.get(this.driver);
                  }

                  if (this.loader == null) {
                     this.log("Loading " + this.driver + " using AntClassLoader with classpath " + this.classpath, 3);
                     this.loader = this.getProject().createClassLoader(this.classpath);
                     if (this.caching) {
                        loaderMap.put(this.driver, this.loader);
                     }
                  } else {
                     this.log("Loading " + this.driver + " using a cached AntClassLoader.", 3);
                  }
               }

               dc = this.loader.loadClass(this.driver);
            } else {
               this.log("Loading " + this.driver + " using system loader.", 3);
               dc = Class.forName(this.driver);
            }

            driverInstance = (Driver)dc.newInstance();
            return driverInstance;
         } catch (ClassNotFoundException var6) {
            throw new BuildException("Class Not Found: JDBC driver " + this.driver + " could not be loaded", var6, this.getLocation());
         } catch (IllegalAccessException var7) {
            throw new BuildException("Illegal Access: JDBC driver " + this.driver + " could not be loaded", var7, this.getLocation());
         } catch (InstantiationException var8) {
            throw new BuildException("Instantiation Exception: JDBC driver " + this.driver + " could not be loaded", var8, this.getLocation());
         }
      }
   }

   public void isCaching(boolean value) {
      this.caching = value;
   }

   public Path getClasspath() {
      return this.classpath;
   }

   public boolean isAutocommit() {
      return this.autocommit;
   }

   public String getUrl() {
      return this.url;
   }

   public String getUserId() {
      return this.userId;
   }

   public void setUserid(String userId) {
      this.userId = userId;
   }

   public String getPassword() {
      return this.password;
   }

   public String getRdbms() {
      return this.rdbms;
   }

   public String getVersion() {
      return this.version;
   }
}
