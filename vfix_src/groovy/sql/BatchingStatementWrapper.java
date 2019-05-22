package groovy.sql;

import groovy.lang.GroovyObjectSupport;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.codehaus.groovy.runtime.InvokerHelper;

public class BatchingStatementWrapper extends GroovyObjectSupport {
   private Statement delegate;
   private int batchSize;
   private int batchCount;
   private Connection connection;
   private Logger log;
   private List<Integer> results;

   public BatchingStatementWrapper(Statement delegate, int batchSize, Logger log, Connection connection) {
      this.delegate = delegate;
      this.batchSize = batchSize;
      this.connection = connection;
      this.log = log;
      this.batchCount = 0;
      this.results = new ArrayList();
   }

   public Object invokeMethod(String name, Object args) {
      return InvokerHelper.invokeMethod(this.delegate, name, args);
   }

   public void addBatch(String sql) throws SQLException {
      this.delegate.addBatch(sql);
      ++this.batchCount;
      if (this.batchSize != 0 && this.batchCount % this.batchSize == 0) {
         int[] result = this.delegate.executeBatch();
         this.connection.commit();
         int[] arr$ = result;
         int len$ = result.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            int i = arr$[i$];
            this.results.add(i);
         }

         this.log.fine("Successfully executed batch with " + result.length + " command(s)");
      }

   }

   public void clearBatch() throws SQLException {
      if (this.batchSize != 0) {
         this.results = new ArrayList();
      }

      this.delegate.clearBatch();
   }

   public int[] executeBatch() throws SQLException {
      int[] lastResult;
      if (this.batchSize == 0) {
         lastResult = this.delegate.executeBatch();
         this.log.fine("Successfully executed batch with " + lastResult.length + " command(s)");
         return lastResult;
      } else {
         lastResult = this.delegate.executeBatch();
         int[] result = lastResult;
         int i = lastResult.length;

         for(int i$ = 0; i$ < i; ++i$) {
            int i = result[i$];
            this.results.add(i);
         }

         this.log.fine("Successfully executed batch with " + lastResult.length + " command(s)");
         result = new int[this.results.size()];

         for(i = 0; i < this.results.size(); ++i) {
            result[i] = (Integer)this.results.get(i);
         }

         this.results = new ArrayList();
         return result;
      }
   }

   public void close() throws SQLException {
      this.delegate.close();
   }
}
