package org.apache.tools.ant.filters;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Parameter;
import org.apache.tools.ant.util.FileUtils;

public final class ReplaceTokens extends BaseParamFilterReader implements ChainableReader {
   private static final char DEFAULT_BEGIN_TOKEN = '@';
   private static final char DEFAULT_END_TOKEN = '@';
   private String queuedData = null;
   private String replaceData = null;
   private int replaceIndex = -1;
   private int queueIndex = -1;
   private Hashtable hash = new Hashtable();
   private char beginToken = '@';
   private char endToken = '@';

   public ReplaceTokens() {
   }

   public ReplaceTokens(Reader in) {
      super(in);
   }

   private int getNextChar() throws IOException {
      if (this.queueIndex != -1) {
         int ch = this.queuedData.charAt(this.queueIndex++);
         if (this.queueIndex >= this.queuedData.length()) {
            this.queueIndex = -1;
         }

         return ch;
      } else {
         return this.in.read();
      }
   }

   public int read() throws IOException {
      if (!this.getInitialized()) {
         this.initialize();
         this.setInitialized(true);
      }

      if (this.replaceIndex != -1) {
         int ch = this.replaceData.charAt(this.replaceIndex++);
         if (this.replaceIndex >= this.replaceData.length()) {
            this.replaceIndex = -1;
         }

         return ch;
      } else {
         int ch = this.getNextChar();
         if (ch != this.beginToken) {
            return ch;
         } else {
            StringBuffer key = new StringBuffer("");

            do {
               ch = this.getNextChar();
               if (ch == -1) {
                  break;
               }

               key.append((char)ch);
            } while(ch != this.endToken);

            if (ch == -1) {
               if (this.queuedData != null && this.queueIndex != -1) {
                  this.queuedData = key.toString() + this.queuedData.substring(this.queueIndex);
               } else {
                  this.queuedData = key.toString();
               }

               this.queueIndex = 0;
               return this.beginToken;
            } else {
               key.setLength(key.length() - 1);
               String replaceWith = (String)this.hash.get(key.toString());
               if (replaceWith != null) {
                  if (replaceWith.length() > 0) {
                     this.replaceData = replaceWith;
                     this.replaceIndex = 0;
                  }

                  return this.read();
               } else {
                  String newData = key.toString() + this.endToken;
                  if (this.queuedData != null && this.queueIndex != -1) {
                     this.queuedData = newData + this.queuedData.substring(this.queueIndex);
                  } else {
                     this.queuedData = newData;
                  }

                  this.queueIndex = 0;
                  return this.beginToken;
               }
            }
         }
      }
   }

   public void setBeginToken(char beginToken) {
      this.beginToken = beginToken;
   }

   private char getBeginToken() {
      return this.beginToken;
   }

   public void setEndToken(char endToken) {
      this.endToken = endToken;
   }

   private char getEndToken() {
      return this.endToken;
   }

   public void addConfiguredToken(ReplaceTokens.Token token) {
      this.hash.put(token.getKey(), token.getValue());
   }

   private Properties getPropertiesFromFile(String fileName) {
      FileInputStream in = null;
      Properties props = new Properties();

      try {
         in = new FileInputStream(fileName);
         props.load(in);
      } catch (IOException var8) {
         var8.printStackTrace();
      } finally {
         FileUtils.close((InputStream)in);
      }

      return props;
   }

   private void setTokens(Hashtable hash) {
      this.hash = hash;
   }

   private Hashtable getTokens() {
      return this.hash;
   }

   public Reader chain(Reader rdr) {
      ReplaceTokens newFilter = new ReplaceTokens(rdr);
      newFilter.setBeginToken(this.getBeginToken());
      newFilter.setEndToken(this.getEndToken());
      newFilter.setTokens(this.getTokens());
      newFilter.setInitialized(true);
      return newFilter;
   }

   private void initialize() {
      Parameter[] params = this.getParameters();
      if (params != null) {
         for(int i = 0; i < params.length; ++i) {
            if (params[i] != null) {
               String type = params[i].getType();
               String name;
               String value;
               if ("tokenchar".equals(type)) {
                  name = params[i].getName();
                  value = params[i].getValue();
                  if ("begintoken".equals(name)) {
                     if (value.length() == 0) {
                        throw new BuildException("Begin token cannot be empty");
                     }

                     this.beginToken = params[i].getValue().charAt(0);
                  } else if ("endtoken".equals(name)) {
                     if (value.length() == 0) {
                        throw new BuildException("End token cannot be empty");
                     }

                     this.endToken = params[i].getValue().charAt(0);
                  }
               } else if ("token".equals(type)) {
                  name = params[i].getName();
                  value = params[i].getValue();
                  this.hash.put(name, value);
               } else if ("propertiesfile".equals(type)) {
                  Properties props = this.getPropertiesFromFile(params[i].getValue());
                  Enumeration e = props.keys();

                  while(e.hasMoreElements()) {
                     String key = (String)e.nextElement();
                     String value = props.getProperty(key);
                     this.hash.put(key, value);
                  }
               }
            }
         }
      }

   }

   public static class Token {
      private String key;
      private String value;

      public final void setKey(String key) {
         this.key = key;
      }

      public final void setValue(String value) {
         this.value = value;
      }

      public final String getKey() {
         return this.key;
      }

      public final String getValue() {
         return this.value;
      }
   }
}
