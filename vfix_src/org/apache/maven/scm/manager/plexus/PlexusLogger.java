package org.apache.maven.scm.manager.plexus;

import org.apache.maven.scm.log.ScmLogger;
import org.codehaus.plexus.logging.Logger;

public class PlexusLogger implements ScmLogger {
   private Logger logger;

   public PlexusLogger(Logger logger) {
      this.logger = logger;
   }

   public boolean isDebugEnabled() {
      return this.logger.isDebugEnabled();
   }

   public void debug(String content) {
      this.logger.debug(content);
   }

   public void debug(String content, Throwable error) {
      this.logger.debug(content, error);
   }

   public void debug(Throwable error) {
      this.logger.debug("", error);
   }

   public boolean isInfoEnabled() {
      return this.logger.isInfoEnabled();
   }

   public void info(String content) {
      this.logger.info(content);
   }

   public void info(String content, Throwable error) {
      this.logger.info(content, error);
   }

   public void info(Throwable error) {
      this.logger.info("", error);
   }

   public boolean isWarnEnabled() {
      return this.logger.isWarnEnabled();
   }

   public void warn(String content) {
      this.logger.warn(content);
   }

   public void warn(String content, Throwable error) {
      this.logger.warn(content, error);
   }

   public void warn(Throwable error) {
      this.logger.warn("", error);
   }

   public boolean isErrorEnabled() {
      return this.logger.isErrorEnabled();
   }

   public void error(String content) {
      this.logger.error(content);
   }

   public void error(String content, Throwable error) {
      this.logger.error(content, error);
   }

   public void error(Throwable error) {
      this.logger.error("", error);
   }
}
