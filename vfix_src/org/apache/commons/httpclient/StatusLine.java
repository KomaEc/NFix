package org.apache.commons.httpclient;

public class StatusLine {
   private final String statusLine;
   private final String httpVersion;
   private final int statusCode;
   private final String reasonPhrase;

   public StatusLine(String statusLine) throws HttpException {
      int length = statusLine.length();
      int at = 0;
      int start = 0;

      try {
         while(Character.isWhitespace(statusLine.charAt(at))) {
            ++at;
            ++start;
         }

         int var10002 = at;
         at += 4;
         if (!"HTTP".equals(statusLine.substring(var10002, at))) {
            throw new HttpException("Status-Line '" + statusLine + "' does not start with HTTP");
         }

         at = statusLine.indexOf(" ", at);
         if (at <= 0) {
            throw new HttpException("Unable to parse HTTP-Version from the status line: '" + statusLine + "'");
         }

         for(this.httpVersion = statusLine.substring(start, at).toUpperCase(); statusLine.charAt(at) == ' '; ++at) {
         }

         int to = statusLine.indexOf(" ", at);
         if (to < 0) {
            to = length;
         }

         try {
            this.statusCode = Integer.parseInt(statusLine.substring(at, to));
         } catch (NumberFormatException var7) {
            throw new HttpException("Unable to parse status code from status line: '" + statusLine + "'");
         }

         at = to + 1;
         if (at < length) {
            this.reasonPhrase = statusLine.substring(at).trim();
         } else {
            this.reasonPhrase = "";
         }
      } catch (StringIndexOutOfBoundsException var8) {
         throw new HttpException("Status-Line '" + statusLine + "' is not valid");
      }

      this.statusLine = new String(statusLine);
   }

   public final int getStatusCode() {
      return this.statusCode;
   }

   public final String getHttpVersion() {
      return this.httpVersion;
   }

   public final String getReasonPhrase() {
      return this.reasonPhrase;
   }

   public final String toString() {
      return this.statusLine;
   }

   public static boolean startsWithHTTP(String s) {
      try {
         int at;
         for(at = 0; Character.isWhitespace(s.charAt(at)); ++at) {
         }

         return "HTTP".equals(s.substring(at, at + 4));
      } catch (StringIndexOutOfBoundsException var2) {
         return false;
      }
   }
}
