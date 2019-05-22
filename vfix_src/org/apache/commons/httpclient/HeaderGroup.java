package org.apache.commons.httpclient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HeaderGroup {
   private List headers = new ArrayList();

   public void clear() {
      this.headers.clear();
   }

   public void addHeader(Header header) {
      this.headers.add(header);
   }

   public void removeHeader(Header header) {
      this.headers.remove(header);
   }

   public void setHeaders(Header[] headers) {
      this.clear();

      for(int i = 0; i < headers.length; ++i) {
         this.addHeader(headers[i]);
      }

   }

   public Header getCondensedHeader(String name) {
      Header[] headers = this.getHeaders(name);
      if (headers.length == 0) {
         return null;
      } else if (headers.length == 1) {
         return new Header(headers[0].getName(), headers[0].getValue());
      } else {
         StringBuffer valueBuffer = new StringBuffer(headers[0].getValue());

         for(int i = 1; i < headers.length; ++i) {
            valueBuffer.append(", ");
            valueBuffer.append(headers[i].getValue());
         }

         return new Header(name.toLowerCase(), valueBuffer.toString());
      }
   }

   public Header[] getHeaders(String name) {
      ArrayList headersFound = new ArrayList();
      Iterator headerIter = this.headers.iterator();

      while(headerIter.hasNext()) {
         Header header = (Header)headerIter.next();
         if (header.getName().equalsIgnoreCase(name)) {
            headersFound.add(header);
         }
      }

      return (Header[])headersFound.toArray(new Header[headersFound.size()]);
   }

   public Header getFirstHeader(String name) {
      Iterator headerIter = this.headers.iterator();

      while(headerIter.hasNext()) {
         Header header = (Header)headerIter.next();
         if (header.getName().equalsIgnoreCase(name)) {
            return header;
         }
      }

      return null;
   }

   public Header getLastHeader(String name) {
      for(int i = this.headers.size() - 1; i >= 0; --i) {
         Header header = (Header)this.headers.get(i);
         if (header.getName().equalsIgnoreCase(name)) {
            return header;
         }
      }

      return null;
   }

   public Header[] getAllHeaders() {
      return (Header[])this.headers.toArray(new Header[this.headers.size()]);
   }

   public boolean containsHeader(String name) {
      Iterator headerIter = this.headers.iterator();

      while(headerIter.hasNext()) {
         Header header = (Header)headerIter.next();
         if (header.getName().equalsIgnoreCase(name)) {
            return true;
         }
      }

      return false;
   }
}
