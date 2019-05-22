package com.mks.api.util;

import com.mks.api.response.APIException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

class InternalAPILogListener {
   private static final String BAD_TYPE = "Bad log configuration type: {0}";
   private static final String BAD_ACTION = "Bad log configuration action: {0}";
   private static final String BAD_INTEGER = "Failure creating integer from {0}";
   private Writer writer;
   private Map messageCategoryIncludes = new Hashtable();
   private Map messageCategoryExcludes = new Hashtable();
   private Map exceptionCategoryIncludes = new Hashtable();
   private Map exceptionCategoryExcludes = new Hashtable();
   private Map messageFormats = Collections.synchronizedMap(new HashMap());
   private MessageFormat defaultMessageFormat = new MessageFormat("{2}({3}): {4}" + System.getProperty("line.separator"));
   private Map exceptionFormats = Collections.synchronizedMap(new HashMap());
   private MessageFormat defaultExceptionFormat = new MessageFormat("{2}({3}): {4}: {5}" + System.getProperty("line.separator"));
   public static final int MESSAGE = 0;
   public static final int EXCEPTION = 1;

   protected InternalAPILogListener() {
   }

   public InternalAPILogListener(Writer writer) {
      this.writer = writer;
   }

   public boolean willLogMessage(String category, int level) {
      return this.checkFilters(0, category, level);
   }

   public boolean willLogException(String category, int level) {
      return this.checkFilters(1, category, level);
   }

   private boolean checkFilters(int filterType, String category, int level) {
      Map categoryIncludes;
      Map categoryExcludes;
      if (filterType == 0) {
         categoryIncludes = this.messageCategoryIncludes;
         categoryExcludes = this.messageCategoryExcludes;
      } else {
         categoryIncludes = this.exceptionCategoryIncludes;
         categoryExcludes = this.exceptionCategoryExcludes;
      }

      Integer lvl;
      if (!categoryExcludes.isEmpty()) {
         lvl = (Integer)categoryExcludes.get(category);
         if (lvl != null && level >= lvl) {
            return false;
         }
      }

      if (!categoryIncludes.isEmpty()) {
         lvl = (Integer)categoryIncludes.get(category);
         if (lvl == null) {
            return false;
         }

         if (level > lvl) {
            return false;
         }
      }

      return true;
   }

   public void logMessage(Class klass, Object obj, String category, int level, Object threadData, String message) {
      if (this.checkFilters(0, category, level)) {
         MessageFormat format = (MessageFormat)this.messageFormats.get(category);
         if (format == null) {
            format = this.defaultMessageFormat;
         }

         try {
            this.writer.write(format.format(new Object[]{klass != null ? klass.getName() : "", obj != null ? obj.toString() : "", category != null ? category : "", new Integer(level), message, new Date(), threadData}));
            this.writer.flush();
         } catch (IOException var9) {
         }

      }
   }

   public void logException(Class klass, Object obj, String category, int level, Object threadData, Throwable exception) {
      if (this.checkFilters(1, category, level)) {
         StringWriter sw = new StringWriter();
         PrintWriter pw = new PrintWriter(sw);
         exception.printStackTrace(pw);
         pw.flush();
         String trace = sw.toString();
         MessageFormat format = (MessageFormat)this.exceptionFormats.get(category);
         if (format == null) {
            format = this.defaultExceptionFormat;
         }

         String msg = exception.getLocalizedMessage();

         try {
            this.writer.write(format.format(new Object[]{klass != null ? klass.getName() : "", obj != null ? obj.toString() : "", category != null ? category : "", new Integer(level), exception.getClass().getName(), msg != null ? msg : "No Message", trace, new Date(), threadData}));
            this.writer.flush();
         } catch (IOException var13) {
         }

      }
   }

   public void addCategoryIncludeFilter(int filterType, String category, int level) {
      if (filterType == 0) {
         this.put(this.messageCategoryIncludes, category, level);
      } else {
         this.put(this.exceptionCategoryIncludes, category, level);
      }

   }

   public void addCategoryExcludeFilter(int filterType, String category, int level) {
      if (filterType == 0) {
         this.put(this.messageCategoryExcludes, category, level);
      } else {
         this.put(this.exceptionCategoryExcludes, category, level);
      }

   }

   private void put(Map m, String category, int level) {
      m.put(category, new Integer(level));
   }

   public void removeCategoryIncludeFilter(int filterType, String category) {
      if (filterType == 0) {
         this.remove(this.messageCategoryIncludes, category);
      } else {
         this.remove(this.exceptionCategoryIncludes, category);
      }

   }

   public void removeCategoryExcludeFilter(int filterType, String category) {
      if (filterType == 0) {
         this.remove(this.messageCategoryExcludes, category);
      } else {
         this.remove(this.exceptionCategoryExcludes, category);
      }

   }

   private void remove(Map m, String category) {
      m.remove(category);
   }

   public synchronized void removeAllCategoryFilters(int filterType) {
      if (filterType == 0) {
         this.messageCategoryIncludes.clear();
         this.messageCategoryExcludes.clear();
      } else {
         this.exceptionCategoryIncludes.clear();
         this.exceptionCategoryExcludes.clear();
      }

   }

   public void setMessageFormat(String category, String format) {
      this.messageFormats.put(category, new MessageFormat(format));
   }

   public void setDefaultMessageFormat(String format) {
      this.defaultMessageFormat = new MessageFormat(format);
   }

   public void setExceptionFormat(String category, String format) {
      this.exceptionFormats.put(category, new MessageFormat(format));
   }

   public void setDefaultExceptionFormat(String format) {
      this.defaultExceptionFormat = new MessageFormat(format);
   }

   public void setWriter(Writer writer) {
      this.writer = writer;
   }

   public void configure(Properties settings, String prefix) throws APIException {
      if (prefix != null && !prefix.endsWith(".")) {
         prefix = prefix + ".";
      }

      Enumeration keys = settings.propertyNames();

      while(true) {
         String key;
         do {
            if (!keys.hasMoreElements()) {
               return;
            }

            key = (String)keys.nextElement();
         } while(prefix != null && !key.startsWith(prefix));

         String val = settings.getProperty(key);
         key = key.substring(prefix.length());
         InternalAPILogListener.ConfigurationRequest cr = new InternalAPILogListener.ConfigurationRequest(key, val);
         cr.execute();
      }
   }

   private class ConfigurationRequest {
      private final String[] Types = new String[]{"message.", "exception."};
      private final String[] Actions = new String[]{"defaultFormat", "format.", "excludeCategory.", "includeCategory."};
      private final int MessageType = 0;
      private final int ExceptionType = 1;
      private final int DefaultFormat = 0;
      private final int Format = 1;
      private final int ExcludeCategory = 2;
      private final int IncludeCategory = 3;
      private int type;
      private int action;
      private String modifier;
      private String value;

      public ConfigurationRequest(String key, String val) throws APIException {
         int i;
         for(i = 0; i < this.Types.length; ++i) {
            if (key.startsWith(this.Types[i])) {
               key = key.substring(this.Types[i].length());
               this.type = i;
               break;
            }
         }

         String[] args;
         if (i == this.Types.length) {
            args = new String[]{key};
            throw new APIException(MessageFormat.format("Bad log configuration type: {0}", (Object[])args));
         } else {
            for(i = 0; i < this.Actions.length; ++i) {
               if (key.startsWith(this.Actions[i])) {
                  key = key.substring(this.Actions[i].length());
                  this.action = i;
                  break;
               }
            }

            if (i == this.Actions.length) {
               args = new String[]{key};
               throw new APIException(MessageFormat.format("Bad log configuration action: {0}", (Object[])args));
            } else {
               this.modifier = key;
               this.value = val;
            }
         }
      }

      public void execute() throws APIException {
         int intValue = 0;
         if (this.action == 2 || this.action == 3) {
            try {
               intValue = Integer.parseInt(this.value);
            } catch (NumberFormatException var4) {
               String[] args = new String[]{this.value};
               throw new APIException(MessageFormat.format("Failure creating integer from {0}", (Object[])args));
            }
         }

         switch(this.action) {
         case 0:
            switch(this.type) {
            case 0:
               InternalAPILogListener.this.setDefaultMessageFormat(this.value);
               return;
            case 1:
               InternalAPILogListener.this.setDefaultExceptionFormat(this.value);
               return;
            default:
               return;
            }
         case 1:
            switch(this.type) {
            case 0:
               InternalAPILogListener.this.setMessageFormat(this.modifier, this.value);
               return;
            case 1:
               InternalAPILogListener.this.setExceptionFormat(this.modifier, this.value);
               return;
            default:
               return;
            }
         case 2:
            InternalAPILogListener.this.addCategoryExcludeFilter(this.type, this.modifier, intValue);
            break;
         case 3:
            InternalAPILogListener.this.addCategoryIncludeFilter(this.type, this.modifier, intValue);
         }

      }
   }
}
