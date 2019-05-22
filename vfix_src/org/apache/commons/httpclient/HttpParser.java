package org.apache.commons.httpclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpParser {
   private static final Log LOG;
   // $FF: synthetic field
   static Class class$org$apache$commons$httpclient$HttpParser;

   private HttpParser() {
   }

   public static byte[] readRawLine(InputStream inputStream) throws IOException {
      LOG.trace("enter HttpParser.readRawLine()");
      ByteArrayOutputStream buf = new ByteArrayOutputStream();

      int ch;
      while((ch = inputStream.read()) >= 0) {
         buf.write(ch);
         if (ch == 10) {
            break;
         }
      }

      return buf.size() == 0 ? null : buf.toByteArray();
   }

   public static String readLine(InputStream inputStream) throws IOException {
      LOG.trace("enter HttpParser.readLine()");
      byte[] rawdata = readRawLine(inputStream);
      if (rawdata == null) {
         return null;
      } else {
         int len = rawdata.length;
         int offset = 0;
         if (len > 0 && rawdata[len - 1] == 10) {
            ++offset;
            if (len > 1 && rawdata[len - 2] == 13) {
               ++offset;
            }
         }

         return HttpConstants.getString(rawdata, 0, len - offset);
      }
   }

   public static Header[] parseHeaders(InputStream is) throws IOException, HttpException {
      LOG.trace("enter HeaderParser.parseHeaders(HttpConnection, HeaderGroup)");
      ArrayList headers = new ArrayList();
      String name = null;
      StringBuffer value = null;

      while(true) {
         String line = readLine(is);
         if (line == null || line.length() < 1) {
            if (name != null) {
               headers.add(new Header(name, value.toString()));
            }

            return (Header[])headers.toArray(new Header[headers.size()]);
         }

         if (line.charAt(0) != ' ' && line.charAt(0) != '\t') {
            if (name != null) {
               headers.add(new Header(name, value.toString()));
            }

            int colon = line.indexOf(":");
            if (colon < 0) {
               throw new HttpException("Unable to parse header: " + line);
            }

            name = line.substring(0, colon).trim();
            value = new StringBuffer(line.substring(colon + 1).trim());
         } else if (value != null) {
            value.append(' ');
            value.append(line.trim());
         }
      }
   }

   // $FF: synthetic method
   static Class class$(String x0) {
      try {
         return Class.forName(x0);
      } catch (ClassNotFoundException var2) {
         throw new NoClassDefFoundError(var2.getMessage());
      }
   }

   static {
      LOG = LogFactory.getLog(class$org$apache$commons$httpclient$HttpParser == null ? (class$org$apache$commons$httpclient$HttpParser = class$("org.apache.commons.httpclient.HttpParser")) : class$org$apache$commons$httpclient$HttpParser);
   }
}
