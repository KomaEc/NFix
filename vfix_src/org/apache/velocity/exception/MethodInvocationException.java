package org.apache.velocity.exception;

import org.apache.commons.lang.StringUtils;

public class MethodInvocationException extends VelocityException implements ExtendedParseException {
   private static final long serialVersionUID = 7305685093478106342L;
   private String referenceName = "";
   private final String methodName;
   private final int lineNumber;
   private final int columnNumber;
   private final String templateName;

   public MethodInvocationException(String message, Throwable e, String methodName, String templateName, int lineNumber, int columnNumber) {
      super(message, e);
      this.methodName = methodName;
      this.templateName = templateName;
      this.lineNumber = lineNumber;
      this.columnNumber = columnNumber;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setReferenceName(String ref) {
      this.referenceName = ref;
   }

   public String getReferenceName() {
      return this.referenceName;
   }

   public int getColumnNumber() {
      return this.columnNumber;
   }

   public int getLineNumber() {
      return this.lineNumber;
   }

   public String getTemplateName() {
      return this.templateName;
   }

   public String getMessage() {
      StringBuffer message = new StringBuffer();
      message.append(super.getMessage());
      message.append(" @ ");
      message.append(StringUtils.isNotEmpty(this.templateName) ? this.templateName : "<unknown template>");
      message.append("[").append(this.lineNumber).append(",").append(this.columnNumber).append("]");
      return message.toString();
   }
}
