package org.apache.commons.httpclient;

public class Header extends NameValuePair {
   public Header() {
      this((String)null, (String)null);
   }

   public Header(String name, String value) {
      super(name, value);
   }

   public String toExternalForm() {
      return (null == this.getName() ? "" : this.getName()) + ": " + (null == this.getValue() ? "" : this.getValue()) + "\r\n";
   }

   public String toString() {
      return this.toExternalForm();
   }

   public HeaderElement[] getValues() throws HttpException {
      return HeaderElement.parse(this.getValue());
   }
}
