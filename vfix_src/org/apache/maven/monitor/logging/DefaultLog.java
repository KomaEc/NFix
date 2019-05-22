package org.apache.maven.monitor.logging;

import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.Logger;

public class DefaultLog implements Log {
   private final Logger logger;

   public DefaultLog(Logger logger) {
      this.logger = logger;
   }

   public void debug(CharSequence content) {
      this.logger.debug(this.toString(content));
   }

   private String toString(CharSequence content) {
      return content == null ? "" : content.toString();
   }

   public void debug(CharSequence content, Throwable error) {
      this.logger.debug(this.toString(content), error);
   }

   public void debug(Throwable error) {
      this.logger.debug("", error);
   }

   public void info(CharSequence content) {
      this.logger.info(this.toString(content));
   }

   public void info(CharSequence content, Throwable error) {
      this.logger.info(this.toString(content), error);
   }

   public void info(Throwable error) {
      this.logger.info("", error);
   }

   public void warn(CharSequence content) {
      this.logger.warn(this.toString(content));
   }

   public void warn(CharSequence content, Throwable error) {
      this.logger.warn(this.toString(content), error);
   }

   public void warn(Throwable error) {
      this.logger.warn("", error);
   }

   public void error(CharSequence content) {
      this.logger.error(this.toString(content));
   }

   public void error(CharSequence content, Throwable error) {
      this.logger.error(this.toString(content), error);
   }

   public void error(Throwable error) {
      this.logger.error("", error);
   }

   public boolean isDebugEnabled() {
      return this.logger.isDebugEnabled();
   }

   public boolean isInfoEnabled() {
      return this.logger.isInfoEnabled();
   }

   public boolean isWarnEnabled() {
      return this.logger.isWarnEnabled();
   }

   public boolean isErrorEnabled() {
      return this.logger.isErrorEnabled();
   }
}
