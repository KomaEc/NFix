package org.jboss.util.xml;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import org.jboss.logging.Logger;
import org.jboss.util.JBossStringBuilder;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

public class JBossErrorHandler implements ErrorHandler, ErrorListener {
   private static final Logger log = Logger.getLogger(JBossErrorHandler.class);
   private String fileName;
   private JBossEntityResolver resolver;
   private boolean error;

   public JBossErrorHandler(String fileName, JBossEntityResolver resolver) {
      this.fileName = fileName;
      this.resolver = resolver;
      this.error = false;
   }

   public void error(SAXParseException e) {
      if (this.resolver == null || this.resolver.isEntityResolved()) {
         this.error = true;
         log.error(this.formatError("error", e));
      }

   }

   public void fatalError(SAXParseException e) {
      if (this.resolver == null || this.resolver.isEntityResolved()) {
         this.error = true;
         log.error(this.formatError("fatal", e));
      }

   }

   public void warning(SAXParseException e) {
      if (this.resolver == null || this.resolver.isEntityResolved()) {
         this.error = true;
         log.error(this.formatError("warning", e));
      }

   }

   public void error(TransformerException e) {
      if (this.resolver == null || this.resolver.isEntityResolved()) {
         this.error = true;
         log.error(this.formatError("error", e));
      }

   }

   public void fatalError(TransformerException e) {
      if (this.resolver == null || this.resolver.isEntityResolved()) {
         this.error = true;
         log.error(this.formatError("fatal", e));
      }

   }

   public void warning(TransformerException e) {
      if (this.resolver == null || this.resolver.isEntityResolved()) {
         this.error = true;
         log.error(this.formatError("warning", e));
      }

   }

   protected String formatError(String context, SAXParseException e) {
      JBossStringBuilder buffer = new JBossStringBuilder();
      buffer.append("File ").append(this.fileName);
      buffer.append(" process ").append(context);
      buffer.append(". Line: ").append(e.getLineNumber());
      buffer.append(". Error message: ").append(e.getMessage());
      return buffer.toString();
   }

   protected String formatError(String context, TransformerException e) {
      JBossStringBuilder buffer = new JBossStringBuilder();
      buffer.append("File ").append(this.fileName);
      buffer.append(" process ").append(context);
      buffer.append(". Location: ").append(e.getLocationAsString());
      buffer.append(". Error message: ").append(e.getMessage());
      return buffer.toString();
   }

   public boolean hadError() {
      return this.error;
   }
}
