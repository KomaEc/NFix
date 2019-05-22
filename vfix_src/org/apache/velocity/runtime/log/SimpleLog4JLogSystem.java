package org.apache.velocity.runtime.log;

import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.velocity.runtime.RuntimeServices;

/** @deprecated */
public class SimpleLog4JLogSystem implements LogSystem {
   private RuntimeServices rsvc = null;
   private RollingFileAppender appender = null;
   protected Category logger = null;

   public void init(RuntimeServices rs) {
      this.rsvc = rs;
      String categoryname = (String)this.rsvc.getProperty("runtime.log.logsystem.log4j.category");
      if (categoryname != null) {
         this.logger = Category.getInstance(categoryname);
         this.logVelocityMessage(0, "SimpleLog4JLogSystem using category '" + categoryname + "'");
      } else {
         String logfile = this.rsvc.getString("runtime.log");

         try {
            this.internalInit(logfile);
            this.logVelocityMessage(0, "SimpleLog4JLogSystem initialized using logfile '" + logfile + "'");
         } catch (Exception var5) {
            System.err.println("PANIC : error configuring SimpleLog4JLogSystem : " + var5);
         }

      }
   }

   private void internalInit(String logfile) throws Exception {
      this.logger = Category.getInstance(this.getClass().getName());
      this.logger.setAdditivity(false);
      this.logger.setLevel(Level.DEBUG);
      this.appender = new RollingFileAppender(new PatternLayout("%d - %m%n"), logfile, true);
      this.appender.setMaxBackupIndex(1);
      this.appender.setMaximumFileSize(100000L);
      this.logger.addAppender(this.appender);
   }

   public void logVelocityMessage(int level, String message) {
      switch(level) {
      case 0:
         this.logger.debug(message);
         break;
      case 1:
         this.logger.info(message);
         break;
      case 2:
         this.logger.warn(message);
         break;
      case 3:
         this.logger.error(message);
         break;
      default:
         this.logger.debug(message);
      }

   }

   protected void finalize() throws Throwable {
      this.shutdown();
   }

   public void shutdown() {
      if (this.appender != null) {
         this.logger.removeAppender(this.appender);
         this.appender.close();
         this.appender = null;
      }

   }
}
