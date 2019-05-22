package org.codehaus.plexus.interpolation;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class InterpolatorFilterReader extends FilterReader {
   private Interpolator interpolator;
   private String replaceData;
   private int replaceIndex;
   private int previousIndex;
   public static final String DEFAULT_BEGIN_TOKEN = "${";
   public static final String DEFAULT_END_TOKEN = "}";
   private String beginToken;
   private String orginalBeginToken;
   private String endToken;
   private boolean interpolateWithPrefixPattern;
   private String escapeString;
   private boolean useEscape;
   private boolean preserveEscapeString;

   public InterpolatorFilterReader(Reader in, Interpolator interpolator) {
      this(in, interpolator, "${", "}");
   }

   public InterpolatorFilterReader(Reader in, Interpolator interpolator, String beginToken, String endToken) {
      super(in);
      this.replaceData = null;
      this.replaceIndex = -1;
      this.previousIndex = -1;
      this.interpolateWithPrefixPattern = true;
      this.useEscape = false;
      this.preserveEscapeString = false;
      this.interpolator = interpolator;
      this.beginToken = beginToken;
      this.endToken = endToken;
      this.orginalBeginToken = this.beginToken;
   }

   public long skip(long n) throws IOException {
      if (n < 0L) {
         throw new IllegalArgumentException("skip value is negative");
      } else {
         for(long i = 0L; i < n; ++i) {
            if (this.read() == -1) {
               return i;
            }
         }

         return n;
      }
   }

   public int read(char[] cbuf, int off, int len) throws IOException {
      for(int i = 0; i < len; ++i) {
         int ch = this.read();
         if (ch == -1) {
            if (i == 0) {
               return -1;
            }

            return i;
         }

         cbuf[off + i] = (char)ch;
      }

      return len;
   }

   public int read() throws IOException {
      if (this.replaceIndex != -1 && this.replaceIndex < this.replaceData.length()) {
         int ch = this.replaceData.charAt(this.replaceIndex++);
         if (this.replaceIndex >= this.replaceData.length()) {
            this.replaceIndex = -1;
         }

         return ch;
      } else {
         int ch = true;
         int ch;
         if (this.previousIndex != -1 && this.previousIndex < this.endToken.length()) {
            ch = this.endToken.charAt(this.previousIndex++);
         } else {
            ch = this.in.read();
         }

         if (ch == this.beginToken.charAt(0) || this.useEscape && ch == this.orginalBeginToken.charAt(0)) {
            StringBuffer key = new StringBuffer();
            key.append((char)ch);
            int beginTokenMatchPos = 1;

            do {
               if (this.previousIndex != -1 && this.previousIndex < this.endToken.length()) {
                  ch = this.endToken.charAt(this.previousIndex++);
               } else {
                  ch = this.in.read();
               }

               if (ch == -1) {
                  break;
               }

               key.append((char)ch);
               if (beginTokenMatchPos < this.beginToken.length() && ch != this.beginToken.charAt(beginTokenMatchPos++) && this.useEscape && this.orginalBeginToken.length() > beginTokenMatchPos - 1 && ch != this.orginalBeginToken.charAt(beginTokenMatchPos - 1)) {
                  ch = -1;
                  break;
               }

               if (this.useEscape && this.orginalBeginToken == this.endToken && key.toString().startsWith(this.beginToken)) {
                  ch = this.in.read();
                  key.append((char)ch);
               }
            } while(ch != this.endToken.charAt(0));

            if (ch != -1 && this.endToken.length() > 1) {
               int endTokenMatchPos = 1;

               do {
                  if (this.previousIndex != -1 && this.previousIndex < this.endToken.length()) {
                     ch = this.endToken.charAt(this.previousIndex++);
                  } else {
                     ch = this.in.read();
                  }

                  if (ch == -1) {
                     break;
                  }

                  key.append((char)ch);
                  if (ch != this.endToken.charAt(endTokenMatchPos++)) {
                     ch = -1;
                     break;
                  }
               } while(endTokenMatchPos < this.endToken.length());
            }

            if (ch == -1) {
               this.replaceData = key.toString();
               this.replaceIndex = 1;
               return this.replaceData.charAt(0);
            } else {
               String value = null;

               try {
                  boolean escapeFound = false;
                  if (this.useEscape && key.toString().startsWith(this.escapeString + this.orginalBeginToken)) {
                     String keyStr = key.toString();
                     if (!this.preserveEscapeString) {
                        value = keyStr.substring(this.escapeString.length(), keyStr.length());
                     } else {
                        value = keyStr;
                     }

                     escapeFound = true;
                  }

                  if (!escapeFound) {
                     if (this.interpolateWithPrefixPattern) {
                        value = this.interpolator.interpolate(key.toString(), "");
                     } else {
                        value = this.interpolator.interpolate(key.toString());
                     }
                  }
               } catch (InterpolationException var7) {
                  IllegalArgumentException error = new IllegalArgumentException(var7.getMessage());
                  error.initCause(var7);
                  throw error;
               }

               if (value != null) {
                  if (value.length() != 0) {
                     this.replaceData = value;
                     this.replaceIndex = 0;
                  }

                  return this.read();
               } else {
                  this.previousIndex = 0;
                  this.replaceData = key.substring(0, key.length() - this.endToken.length());
                  this.replaceIndex = 0;
                  return this.beginToken.charAt(0);
               }
            }
         } else {
            return ch;
         }
      }
   }

   public boolean isInterpolateWithPrefixPattern() {
      return this.interpolateWithPrefixPattern;
   }

   public void setInterpolateWithPrefixPattern(boolean interpolateWithPrefixPattern) {
      this.interpolateWithPrefixPattern = interpolateWithPrefixPattern;
   }

   public String getEscapeString() {
      return this.escapeString;
   }

   public void setEscapeString(String escapeString) {
      if (escapeString != null && escapeString.length() >= 1) {
         this.escapeString = escapeString;
         this.orginalBeginToken = this.beginToken;
         this.beginToken = escapeString + this.beginToken;
         this.useEscape = escapeString != null && escapeString.length() >= 1;
      }

   }

   public boolean isPreserveEscapeString() {
      return this.preserveEscapeString;
   }

   public void setPreserveEscapeString(boolean preserveEscapeString) {
      this.preserveEscapeString = preserveEscapeString;
   }
}
