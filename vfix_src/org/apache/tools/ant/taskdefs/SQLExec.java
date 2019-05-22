package org.apache.tools.ant.taskdefs;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Union;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StringUtils;

public class SQLExec extends JDBCTask {
   private int goodSql = 0;
   private int totalSql = 0;
   private Connection conn = null;
   private Union resources = new Union();
   private Statement statement = null;
   private File srcFile = null;
   private String sqlCommand = "";
   private Vector transactions = new Vector();
   private String delimiter = ";";
   private String delimiterType = "normal";
   private boolean print = false;
   private boolean showheaders = true;
   private boolean showtrailers = true;
   private File output = null;
   private String onError = "abort";
   private String encoding = null;
   private boolean append = false;
   private boolean keepformat = false;
   private boolean escapeProcessing = true;
   private boolean expandProperties = false;

   public void setSrc(File srcFile) {
      this.srcFile = srcFile;
   }

   public void setExpandProperties(boolean expandProperties) {
      this.expandProperties = expandProperties;
   }

   public boolean getExpandProperties() {
      return this.expandProperties;
   }

   public void addText(String sql) {
      this.sqlCommand = this.sqlCommand + sql;
   }

   public void addFileset(FileSet set) {
      this.add(set);
   }

   public void add(ResourceCollection rc) {
      this.resources.add(rc);
   }

   public SQLExec.Transaction createTransaction() {
      SQLExec.Transaction t = new SQLExec.Transaction();
      this.transactions.addElement(t);
      return t;
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public void setDelimiter(String delimiter) {
      this.delimiter = delimiter;
   }

   public void setDelimiterType(SQLExec.DelimiterType delimiterType) {
      this.delimiterType = delimiterType.getValue();
   }

   public void setPrint(boolean print) {
      this.print = print;
   }

   public void setShowheaders(boolean showheaders) {
      this.showheaders = showheaders;
   }

   public void setShowtrailers(boolean showtrailers) {
      this.showtrailers = showtrailers;
   }

   public void setOutput(File output) {
      this.output = output;
   }

   public void setAppend(boolean append) {
      this.append = append;
   }

   public void setOnerror(SQLExec.OnError action) {
      this.onError = action.getValue();
   }

   public void setKeepformat(boolean keepformat) {
      this.keepformat = keepformat;
   }

   public void setEscapeProcessing(boolean enable) {
      this.escapeProcessing = enable;
   }

   public void execute() throws BuildException {
      Vector savedTransaction = (Vector)this.transactions.clone();
      String savedSqlCommand = this.sqlCommand;
      this.sqlCommand = this.sqlCommand.trim();

      try {
         if (this.srcFile == null && this.sqlCommand.length() == 0 && this.resources.size() == 0 && this.transactions.size() == 0) {
            throw new BuildException("Source file or resource collection, transactions or sql statement must be set!", this.getLocation());
         }

         if (this.srcFile != null && !this.srcFile.exists()) {
            throw new BuildException("Source file does not exist!", this.getLocation());
         }

         Iterator iter = this.resources.iterator();

         while(iter.hasNext()) {
            Resource r = (Resource)iter.next();
            SQLExec.Transaction t = this.createTransaction();
            t.setSrcResource(r);
         }

         SQLExec.Transaction t = this.createTransaction();
         t.setSrc(this.srcFile);
         t.addText(this.sqlCommand);
         this.conn = this.getConnection();
         if (this.isValidRdbms(this.conn)) {
            try {
               this.statement = this.conn.createStatement();
               this.statement.setEscapeProcessing(this.escapeProcessing);
               PrintStream out = System.out;

               try {
                  if (this.output != null) {
                     this.log("Opening PrintStream to output file " + this.output, 3);
                     out = new PrintStream(new BufferedOutputStream(new FileOutputStream(this.output.getAbsolutePath(), this.append)));
                  }

                  Enumeration e = this.transactions.elements();

                  while(e.hasMoreElements()) {
                     ((SQLExec.Transaction)e.nextElement()).runTransaction(out);
                     if (!this.isAutocommit()) {
                        this.log("Committing transaction", 3);
                        this.conn.commit();
                     }
                  }
               } finally {
                  if (out != null && out != System.out) {
                     out.close();
                  }

               }
            } catch (IOException var33) {
               this.closeQuietly();
               throw new BuildException(var33, this.getLocation());
            } catch (SQLException var34) {
               this.closeQuietly();
               throw new BuildException(var34, this.getLocation());
            } finally {
               try {
                  if (this.statement != null) {
                     this.statement.close();
                  }

                  if (this.conn != null) {
                     this.conn.close();
                  }
               } catch (SQLException var31) {
               }

            }

            this.log(this.goodSql + " of " + this.totalSql + " SQL statements executed successfully");
            return;
         }
      } finally {
         this.transactions = savedTransaction;
         this.sqlCommand = savedSqlCommand;
      }

   }

   protected void runStatements(Reader reader, PrintStream out) throws SQLException, IOException {
      StringBuffer sql = new StringBuffer();
      BufferedReader in = new BufferedReader(reader);

      while(true) {
         String line;
         do {
            while(true) {
               if ((line = in.readLine()) == null) {
                  if (sql.length() > 0) {
                     this.execSQL(sql.toString(), out);
                  }

                  return;
               }

               if (!this.keepformat) {
                  line = line.trim();
               }

               line = this.getProject().replaceProperties(line);
               if (this.keepformat) {
                  break;
               }

               if (!line.startsWith("//") && !line.startsWith("--")) {
                  StringTokenizer st = new StringTokenizer(line);
                  if (!st.hasMoreTokens()) {
                     break;
                  }

                  String token = st.nextToken();
                  if (!"REM".equalsIgnoreCase(token)) {
                     break;
                  }
               }
            }

            if (!this.keepformat) {
               sql.append(" ");
               sql.append(line);
            } else {
               sql.append("\n");
               sql.append(line);
            }

            if (!this.keepformat && line.indexOf("--") >= 0) {
               sql.append("\n");
            }
         } while((!this.delimiterType.equals("normal") || !StringUtils.endsWith(sql, this.delimiter)) && (!this.delimiterType.equals("row") || !line.equals(this.delimiter)));

         this.execSQL(sql.substring(0, sql.length() - this.delimiter.length()), out);
         sql.replace(0, sql.length(), "");
      }
   }

   protected void execSQL(String sql, PrintStream out) throws SQLException {
      if (!"".equals(sql.trim())) {
         ResultSet resultSet = null;

         try {
            ++this.totalSql;
            this.log("SQL: " + sql, 3);
            int updateCount = false;
            int updateCountTotal = 0;
            boolean ret = this.statement.execute(sql);
            int updateCount = this.statement.getUpdateCount();
            resultSet = this.statement.getResultSet();

            do {
               if (!ret) {
                  if (updateCount != -1) {
                     updateCountTotal += updateCount;
                  }
               } else if (this.print) {
                  this.printResults(resultSet, out);
               }

               ret = this.statement.getMoreResults();
               if (ret) {
                  updateCount = this.statement.getUpdateCount();
                  resultSet = this.statement.getResultSet();
               }
            } while(ret);

            this.log(updateCountTotal + " rows affected", 3);
            if (this.print && this.showtrailers) {
               out.println(updateCountTotal + " rows affected");
            }

            for(SQLWarning warning = this.conn.getWarnings(); warning != null; warning = warning.getNextWarning()) {
               this.log(warning + " sql warning", 3);
            }

            this.conn.clearWarnings();
            ++this.goodSql;
         } catch (SQLException var11) {
            this.log("Failed to execute: " + sql, 0);
            if (!this.onError.equals("continue")) {
               throw var11;
            }

            this.log(var11.toString(), 0);
         } finally {
            if (resultSet != null) {
               resultSet.close();
            }

         }

      }
   }

   /** @deprecated */
   protected void printResults(PrintStream out) throws SQLException {
      ResultSet rs = this.statement.getResultSet();

      try {
         this.printResults(rs, out);
      } finally {
         if (rs != null) {
            rs.close();
         }

      }

   }

   protected void printResults(ResultSet rs, PrintStream out) throws SQLException {
      if (rs != null) {
         this.log("Processing new result set.", 3);
         ResultSetMetaData md = rs.getMetaData();
         int columnCount = md.getColumnCount();
         StringBuffer line = new StringBuffer();
         if (this.showheaders) {
            for(int col = 1; col < columnCount; ++col) {
               line.append(md.getColumnName(col));
               line.append(",");
            }

            line.append(md.getColumnName(columnCount));
            out.println(line);
            line = new StringBuffer();
         }

         while(rs.next()) {
            boolean first = true;

            for(int col = 1; col <= columnCount; ++col) {
               String columnValue = rs.getString(col);
               if (columnValue != null) {
                  columnValue = columnValue.trim();
               }

               if (first) {
                  first = false;
               } else {
                  line.append(",");
               }

               line.append(columnValue);
            }

            out.println(line);
            line = new StringBuffer();
         }
      }

      out.println();
   }

   private void closeQuietly() {
      if (!this.isAutocommit() && this.conn != null && this.onError.equals("abort")) {
         try {
            this.conn.rollback();
         } catch (SQLException var2) {
         }
      }

   }

   public class Transaction {
      private Resource tSrcResource = null;
      private String tSqlCommand = "";

      public void setSrc(File src) {
         if (src != null) {
            this.setSrcResource(new FileResource(src));
         }

      }

      public void setSrcResource(Resource src) {
         if (this.tSrcResource != null) {
            throw new BuildException("only one resource per transaction");
         } else {
            this.tSrcResource = src;
         }
      }

      public void addText(String sql) {
         if (sql != null) {
            if (SQLExec.this.getExpandProperties()) {
               sql = SQLExec.this.getProject().replaceProperties(sql);
            }

            this.tSqlCommand = this.tSqlCommand + sql;
         }

      }

      public void addConfigured(ResourceCollection a) {
         if (a.size() != 1) {
            throw new BuildException("only single argument resource collections are supported.");
         } else {
            this.setSrcResource((Resource)a.iterator().next());
         }
      }

      private void runTransaction(PrintStream out) throws IOException, SQLException {
         if (this.tSqlCommand.length() != 0) {
            SQLExec.this.log("Executing commands", 2);
            SQLExec.this.runStatements(new StringReader(this.tSqlCommand), out);
         }

         if (this.tSrcResource != null) {
            SQLExec.this.log("Executing resource: " + this.tSrcResource.toString(), 2);
            InputStream is = null;
            InputStreamReader reader = null;

            try {
               is = this.tSrcResource.getInputStream();
               reader = SQLExec.this.encoding == null ? new InputStreamReader(is) : new InputStreamReader(is, SQLExec.this.encoding);
               SQLExec.this.runStatements(reader, out);
            } finally {
               FileUtils.close(is);
               FileUtils.close((Reader)reader);
            }
         }

      }
   }

   public static class OnError extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"continue", "stop", "abort"};
      }
   }

   public static class DelimiterType extends EnumeratedAttribute {
      public static final String NORMAL = "normal";
      public static final String ROW = "row";

      public String[] getValues() {
         return new String[]{"normal", "row"};
      }
   }
}
