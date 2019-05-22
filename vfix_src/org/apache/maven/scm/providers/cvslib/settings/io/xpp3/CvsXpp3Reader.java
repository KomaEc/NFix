package org.apache.maven.scm.providers.cvslib.settings.io.xpp3;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.maven.scm.providers.cvslib.settings.Settings;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.EntityReplacementMap;
import org.codehaus.plexus.util.xml.pull.MXParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParser;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class CvsXpp3Reader {
   private boolean addDefaultEntities = true;

   private boolean checkFieldWithDuplicate(XmlPullParser parser, String tagName, String alias, Set parsed) throws XmlPullParserException {
      if (!parser.getName().equals(tagName) && !parser.getName().equals(alias)) {
         return false;
      } else if (!parsed.add(tagName)) {
         throw new XmlPullParserException("Duplicated tag: '" + tagName + "'", parser, (Throwable)null);
      } else {
         return true;
      }
   }

   private void checkUnknownAttribute(XmlPullParser parser, String attribute, String tagName, boolean strict) throws XmlPullParserException, IOException {
      if (strict) {
         throw new XmlPullParserException("Unknown attribute '" + attribute + "' for tag '" + tagName + "'", parser, (Throwable)null);
      }
   }

   private void checkUnknownElement(XmlPullParser parser, boolean strict) throws XmlPullParserException, IOException {
      if (strict) {
         throw new XmlPullParserException("Unrecognised tag: '" + parser.getName() + "'", parser, (Throwable)null);
      } else {
         int unrecognizedTagCount = 1;

         while(unrecognizedTagCount > 0) {
            int eventType = parser.next();
            if (eventType == 2) {
               ++unrecognizedTagCount;
            } else if (eventType == 3) {
               --unrecognizedTagCount;
            }
         }

      }
   }

   public boolean getAddDefaultEntities() {
      return this.addDefaultEntities;
   }

   private boolean getBooleanValue(String s, String attribute, XmlPullParser parser) throws XmlPullParserException {
      return this.getBooleanValue(s, attribute, parser, (String)null);
   }

   private boolean getBooleanValue(String s, String attribute, XmlPullParser parser, String defaultValue) throws XmlPullParserException {
      if (s != null && s.length() != 0) {
         return Boolean.valueOf(s);
      } else {
         return defaultValue != null ? Boolean.valueOf(defaultValue) : false;
      }
   }

   private byte getByteValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Byte.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a byte", parser, var6);
            }
         }
      }

      return 0;
   }

   private char getCharacterValue(String s, String attribute, XmlPullParser parser) throws XmlPullParserException {
      return s != null ? s.charAt(0) : '\u0000';
   }

   private Date getDateValue(String s, String attribute, XmlPullParser parser) throws XmlPullParserException {
      return this.getDateValue(s, attribute, (String)null, parser);
   }

   private Date getDateValue(String s, String attribute, String dateFormat, XmlPullParser parser) throws XmlPullParserException {
      if (s != null) {
         String effectiveDateFormat = dateFormat;
         if (dateFormat == null) {
            effectiveDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
         }

         if ("long".equals(effectiveDateFormat)) {
            try {
               return new Date(Long.parseLong(s));
            } catch (NumberFormatException var7) {
               throw new XmlPullParserException(var7.getMessage(), parser, var7);
            }
         } else {
            try {
               DateFormat dateParser = new SimpleDateFormat(effectiveDateFormat, Locale.US);
               return dateParser.parse(s);
            } catch (ParseException var8) {
               throw new XmlPullParserException(var8.getMessage(), parser, var8);
            }
         }
      } else {
         return null;
      }
   }

   private double getDoubleValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Double.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a floating point number", parser, var6);
            }
         }
      }

      return 0.0D;
   }

   private float getFloatValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Float.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a floating point number", parser, var6);
            }
         }
      }

      return 0.0F;
   }

   private int getIntegerValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Integer.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be an integer", parser, var6);
            }
         }
      }

      return 0;
   }

   private long getLongValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Long.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a long integer", parser, var6);
            }
         }
      }

      return 0L;
   }

   private String getRequiredAttributeValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s == null && strict) {
         throw new XmlPullParserException("Missing required value for attribute '" + attribute + "'", parser, (Throwable)null);
      } else {
         return s;
      }
   }

   private short getShortValue(String s, String attribute, XmlPullParser parser, boolean strict) throws XmlPullParserException {
      if (s != null) {
         try {
            return Short.valueOf(s);
         } catch (NumberFormatException var6) {
            if (strict) {
               throw new XmlPullParserException("Unable to parse element '" + attribute + "', must be a short integer", parser, var6);
            }
         }
      }

      return 0;
   }

   private String getTrimmedValue(String s) {
      if (s != null) {
         s = s.trim();
      }

      return s;
   }

   private int nextTag(XmlPullParser parser) throws IOException, XmlPullParserException {
      int eventType = parser.next();
      if (eventType == 4) {
         eventType = parser.next();
      }

      if (eventType != 2 && eventType != 3) {
         throw new XmlPullParserException("expected START_TAG or END_TAG not " + XmlPullParser.TYPES[eventType], parser, (Throwable)null);
      } else {
         return eventType;
      }
   }

   public Settings read(Reader reader, boolean strict) throws IOException, XmlPullParserException {
      XmlPullParser parser = this.addDefaultEntities ? new MXParser(EntityReplacementMap.defaultEntityReplacementMap) : new MXParser();
      parser.setInput(reader);
      return this.read((XmlPullParser)parser, strict);
   }

   public Settings read(Reader reader) throws IOException, XmlPullParserException {
      return this.read(reader, true);
   }

   public Settings read(InputStream in, boolean strict) throws IOException, XmlPullParserException {
      return this.read((Reader)ReaderFactory.newXmlReader(in), strict);
   }

   public Settings read(InputStream in) throws IOException, XmlPullParserException {
      return this.read((Reader)ReaderFactory.newXmlReader(in));
   }

   private Settings parseSettings(XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      String tagName = parser.getName();
      Settings settings = new Settings();

      String key;
      for(int i = parser.getAttributeCount() - 1; i >= 0; --i) {
         key = parser.getAttributeName(i);
         parser.getAttributeValue(i);
         if (key.indexOf(58) < 0 && !"xmlns".equals(key)) {
            this.checkUnknownAttribute(parser, key, tagName, strict);
         }
      }

      HashSet parsed = new HashSet();

      while(true) {
         while((strict ? parser.nextTag() : this.nextTag(parser)) == 2) {
            if (this.checkFieldWithDuplicate(parser, "changeLogCommandDateFormat", (String)null, parsed)) {
               settings.setChangeLogCommandDateFormat(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "useCvsrc", (String)null, parsed)) {
               settings.setUseCvsrc(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "useCvsrc", parser, "false"));
            } else if (this.checkFieldWithDuplicate(parser, "compressionLevel", (String)null, parsed)) {
               settings.setCompressionLevel(this.getIntegerValue(this.getTrimmedValue(parser.nextText()), "compressionLevel", parser, strict));
            } else if (this.checkFieldWithDuplicate(parser, "traceCvsCommand", (String)null, parsed)) {
               settings.setTraceCvsCommand(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "traceCvsCommand", parser, "false"));
            } else if (this.checkFieldWithDuplicate(parser, "temporaryFilesDirectory", (String)null, parsed)) {
               settings.setTemporaryFilesDirectory(this.getTrimmedValue(parser.nextText()));
            } else if (this.checkFieldWithDuplicate(parser, "cvsVariables", (String)null, parsed)) {
               while(parser.nextTag() == 2) {
                  key = parser.getName();
                  String value = parser.nextText().trim();
                  settings.addCvsVariable(key, value);
               }
            } else if (this.checkFieldWithDuplicate(parser, "useForceTag", (String)null, parsed)) {
               settings.setUseForceTag(this.getBooleanValue(this.getTrimmedValue(parser.nextText()), "useForceTag", parser, "true"));
            } else {
               this.checkUnknownElement(parser, strict);
            }
         }

         return settings;
      }
   }

   private Settings read(XmlPullParser parser, boolean strict) throws IOException, XmlPullParserException {
      for(int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
         if (eventType == 2) {
            if (strict && !"cvs-settings".equals(parser.getName())) {
               throw new XmlPullParserException("Expected root element 'cvs-settings' but found '" + parser.getName() + "'", parser, (Throwable)null);
            }

            Settings settings = this.parseSettings(parser, strict);
            settings.setModelEncoding(parser.getInputEncoding());
            return settings;
         }
      }

      throw new XmlPullParserException("Expected root element 'cvs-settings' but found no element at all: invalid XML document", parser, (Throwable)null);
   }

   public void setAddDefaultEntities(boolean addDefaultEntities) {
      this.addDefaultEntities = addDefaultEntities;
   }
}
