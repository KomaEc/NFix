package org.apache.maven.scm.log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScmLogDispatcher implements ScmLogger {
   private List<ScmLogger> listeners = new ArrayList();

   public void addListener(ScmLogger logger) {
      this.listeners.add(logger);
   }

   public void debug(String content, Throwable error) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         logger.debug(content, error);
      }

   }

   public void debug(String content) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         logger.debug(content);
      }

   }

   public void debug(Throwable error) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         logger.debug(error);
      }

   }

   public void error(String content, Throwable error) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         logger.error(content, error);
      }

   }

   public void error(String content) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         logger.error(content);
      }

   }

   public void error(Throwable error) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         logger.error(error);
      }

   }

   public void info(String content, Throwable error) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         if (logger.isInfoEnabled()) {
            logger.info(content, error);
         }
      }

   }

   public void info(String content) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         if (logger.isInfoEnabled()) {
            logger.info(content);
         }
      }

   }

   public void info(Throwable error) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         if (logger.isInfoEnabled()) {
            logger.info(error);
         }
      }

   }

   public boolean isDebugEnabled() {
      Iterator i$ = this.listeners.iterator();

      ScmLogger logger;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         logger = (ScmLogger)i$.next();
      } while(!logger.isDebugEnabled());

      return true;
   }

   public boolean isErrorEnabled() {
      Iterator i$ = this.listeners.iterator();

      ScmLogger logger;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         logger = (ScmLogger)i$.next();
      } while(!logger.isErrorEnabled());

      return true;
   }

   public boolean isInfoEnabled() {
      Iterator i$ = this.listeners.iterator();

      ScmLogger logger;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         logger = (ScmLogger)i$.next();
      } while(!logger.isInfoEnabled());

      return true;
   }

   public boolean isWarnEnabled() {
      Iterator i$ = this.listeners.iterator();

      ScmLogger logger;
      do {
         if (!i$.hasNext()) {
            return false;
         }

         logger = (ScmLogger)i$.next();
      } while(!logger.isWarnEnabled());

      return true;
   }

   public void warn(String content, Throwable error) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         logger.warn(content, error);
      }

   }

   public void warn(String content) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         logger.warn(content);
      }

   }

   public void warn(Throwable error) {
      Iterator i$ = this.listeners.iterator();

      while(i$.hasNext()) {
         ScmLogger logger = (ScmLogger)i$.next();
         logger.warn(error);
      }

   }
}
