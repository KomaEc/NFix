package org.apache.commons.collections;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

public class ExtendedProperties extends Hashtable {
   private ExtendedProperties defaults;
   protected String file;
   protected String basePath;
   protected String fileSeparator;
   protected boolean isInitialized;
   protected static String include = "include";
   protected ArrayList keysAsListed;
   protected static final String START_TOKEN = "${";
   protected static final String END_TOKEN = "}";

   protected String interpolate(String base) {
      return this.interpolateHelper(base, (List)null);
   }

   protected String interpolateHelper(String base, List priorVariables) {
      if (base == null) {
         return null;
      } else {
         if (priorVariables == null) {
            priorVariables = new ArrayList();
            ((List)priorVariables).add(base);
         }

         int begin = true;
         int end = true;
         int prec = 0 - "}".length();
         String variable = null;

         StringBuffer result;
         int begin;
         int end;
         for(result = new StringBuffer(); (begin = base.indexOf("${", prec + "}".length())) > -1 && (end = base.indexOf("}", begin)) > -1; prec = end) {
            result.append(base.substring(prec + "}".length(), begin));
            variable = base.substring(begin + "${".length(), end);
            if (((List)priorVariables).contains(variable)) {
               String initialBase = ((List)priorVariables).remove(0).toString();
               ((List)priorVariables).add(variable);
               StringBuffer priorVariableSb = new StringBuffer();
               Iterator it = ((List)priorVariables).iterator();

               while(it.hasNext()) {
                  priorVariableSb.append(it.next());
                  if (it.hasNext()) {
                     priorVariableSb.append("->");
                  }
               }

               throw new IllegalStateException("infinite loop in property interpolation of " + initialBase + ": " + priorVariableSb.toString());
            }

            ((List)priorVariables).add(variable);
            Object value = this.getProperty(variable);
            if (value != null) {
               result.append(this.interpolateHelper(value.toString(), (List)priorVariables));
               ((List)priorVariables).remove(((List)priorVariables).size() - 1);
            } else if (this.defaults != null && this.defaults.getString(variable, (String)null) != null) {
               result.append(this.defaults.getString(variable));
            } else {
               result.append("${").append(variable).append("}");
            }
         }

         result.append(base.substring(prec + "}".length(), base.length()));
         return result.toString();
      }
   }

   private static String escape(String s) {
      StringBuffer buf = new StringBuffer(s);

      for(int i = 0; i < buf.length(); ++i) {
         char c = buf.charAt(i);
         if (c == ',' || c == '\\') {
            buf.insert(i, '\\');
            ++i;
         }
      }

      return buf.toString();
   }

   private static String unescape(String s) {
      StringBuffer buf = new StringBuffer(s);

      for(int i = 0; i < buf.length() - 1; ++i) {
         char c1 = buf.charAt(i);
         char c2 = buf.charAt(i + 1);
         if (c1 == '\\' && c2 == '\\') {
            buf.deleteCharAt(i);
         }
      }

      return buf.toString();
   }

   private static int countPreceding(String line, int index, char ch) {
      int i;
      for(i = index - 1; i >= 0 && line.charAt(i) == ch; --i) {
      }

      return index - 1 - i;
   }

   private static boolean endsWithSlash(String line) {
      if (!line.endsWith("\\")) {
         return false;
      } else {
         return countPreceding(line, line.length() - 1, '\\') % 2 == 0;
      }
   }

   public ExtendedProperties() {
      this.fileSeparator = System.getProperty("file.separator");
      this.isInitialized = false;
      this.keysAsListed = new ArrayList();
   }

   public ExtendedProperties(String file) throws IOException {
      this(file, (String)null);
   }

   public ExtendedProperties(String file, String defaultFile) throws IOException {
      this.fileSeparator = System.getProperty("file.separator");
      this.isInitialized = false;
      this.keysAsListed = new ArrayList();
      this.file = file;
      this.basePath = (new File(file)).getAbsolutePath();
      this.basePath = this.basePath.substring(0, this.basePath.lastIndexOf(this.fileSeparator) + 1);
      FileInputStream in = null;

      try {
         in = new FileInputStream(file);
         this.load(in);
      } finally {
         try {
            if (in != null) {
               in.close();
            }
         } catch (IOException var10) {
         }

      }

      if (defaultFile != null) {
         this.defaults = new ExtendedProperties(defaultFile);
      }

   }

   public boolean isInitialized() {
      return this.isInitialized;
   }

   public String getInclude() {
      return include;
   }

   public void setInclude(String inc) {
      include = inc;
   }

   public void load(InputStream input) throws IOException {
      this.load(input, (String)null);
   }

   public synchronized void load(InputStream input, String enc) throws IOException {
      ExtendedProperties.PropertiesReader reader = null;
      if (enc != null) {
         try {
            reader = new ExtendedProperties.PropertiesReader(new InputStreamReader(input, enc));
         } catch (UnsupportedEncodingException var15) {
         }
      }

      if (reader == null) {
         try {
            reader = new ExtendedProperties.PropertiesReader(new InputStreamReader(input, "8859_1"));
         } catch (UnsupportedEncodingException var14) {
            reader = new ExtendedProperties.PropertiesReader(new InputStreamReader(input));
         }
      }

      try {
         while(true) {
            String line = reader.readProperty();
            if (line == null) {
               return;
            }

            int equalSign = line.indexOf(61);
            if (equalSign > 0) {
               String key = line.substring(0, equalSign).trim();
               String value = line.substring(equalSign + 1).trim();
               if (!"".equals(value)) {
                  if (this.getInclude() != null && key.equalsIgnoreCase(this.getInclude())) {
                     File file = null;
                     if (value.startsWith(this.fileSeparator)) {
                        file = new File(value);
                     } else {
                        if (value.startsWith("." + this.fileSeparator)) {
                           value = value.substring(2);
                        }

                        file = new File(this.basePath + value);
                     }

                     if (file != null && file.exists() && file.canRead()) {
                        this.load(new FileInputStream(file));
                     }
                  } else {
                     this.addProperty(key, value);
                  }
               }
            }
         }
      } finally {
         this.isInitialized = true;
      }
   }

   public Object getProperty(String key) {
      Object obj = this.get(key);
      if (obj == null && this.defaults != null) {
         obj = this.defaults.get(key);
      }

      return obj;
   }

   public void addProperty(String key, Object value) {
      if (value instanceof String) {
         String str = (String)value;
         if (str.indexOf(",") > 0) {
            ExtendedProperties.PropertiesTokenizer tokenizer = new ExtendedProperties.PropertiesTokenizer(str);

            while(tokenizer.hasMoreTokens()) {
               String token = tokenizer.nextToken();
               this.addPropertyInternal(key, unescape(token));
            }
         } else {
            this.addPropertyInternal(key, unescape(str));
         }
      } else {
         this.addPropertyInternal(key, value);
      }

      this.isInitialized = true;
   }

   private void addPropertyDirect(String key, Object value) {
      if (!this.containsKey(key)) {
         this.keysAsListed.add(key);
      }

      this.put(key, value);
   }

   private void addPropertyInternal(String key, Object value) {
      Object current = this.get(key);
      if (current instanceof String) {
         List values = new Vector(2);
         values.add(current);
         values.add(value);
         this.put(key, values);
      } else if (current instanceof List) {
         ((List)current).add(value);
      } else {
         if (!this.containsKey(key)) {
            this.keysAsListed.add(key);
         }

         this.put(key, value);
      }

   }

   public void setProperty(String key, Object value) {
      this.clearProperty(key);
      this.addProperty(key, value);
   }

   public synchronized void save(OutputStream output, String header) throws IOException {
      if (output != null) {
         PrintWriter theWrtr = new PrintWriter(output);
         if (header != null) {
            theWrtr.println(header);
         }

         Enumeration theKeys = this.keys();

         while(theKeys.hasMoreElements()) {
            String key = (String)theKeys.nextElement();
            Object value = this.get(key);
            if (value != null) {
               if (value instanceof String) {
                  StringBuffer currentOutput = new StringBuffer();
                  currentOutput.append(key);
                  currentOutput.append("=");
                  currentOutput.append(escape((String)value));
                  theWrtr.println(currentOutput.toString());
               } else if (value instanceof List) {
                  List values = (List)value;
                  Iterator it = values.iterator();

                  while(it.hasNext()) {
                     String currentElement = (String)it.next();
                     StringBuffer currentOutput = new StringBuffer();
                     currentOutput.append(key);
                     currentOutput.append("=");
                     currentOutput.append(escape(currentElement));
                     theWrtr.println(currentOutput.toString());
                  }
               }
            }

            theWrtr.println();
            theWrtr.flush();
         }

      }
   }

   public void combine(ExtendedProperties props) {
      Iterator it = props.getKeys();

      while(it.hasNext()) {
         String key = (String)it.next();
         this.setProperty(key, props.get(key));
      }

   }

   public void clearProperty(String key) {
      if (this.containsKey(key)) {
         for(int i = 0; i < this.keysAsListed.size(); ++i) {
            if (this.keysAsListed.get(i).equals(key)) {
               this.keysAsListed.remove(i);
               break;
            }
         }

         this.remove(key);
      }

   }

   public Iterator getKeys() {
      return this.keysAsListed.iterator();
   }

   public Iterator getKeys(String prefix) {
      Iterator keys = this.getKeys();
      ArrayList matchingKeys = new ArrayList();

      while(keys.hasNext()) {
         Object key = keys.next();
         if (key instanceof String && ((String)key).startsWith(prefix)) {
            matchingKeys.add(key);
         }
      }

      return matchingKeys.iterator();
   }

   public ExtendedProperties subset(String prefix) {
      ExtendedProperties c = new ExtendedProperties();
      Iterator keys = this.getKeys();
      boolean validSubset = false;

      while(keys.hasNext()) {
         Object key = keys.next();
         if (key instanceof String && ((String)key).startsWith(prefix)) {
            if (!validSubset) {
               validSubset = true;
            }

            String newKey = null;
            if (((String)key).length() == prefix.length()) {
               newKey = prefix;
            } else {
               newKey = ((String)key).substring(prefix.length() + 1);
            }

            c.addPropertyDirect(newKey, this.get(key));
         }
      }

      if (validSubset) {
         return c;
      } else {
         return null;
      }
   }

   public void display() {
      Iterator i = this.getKeys();

      while(i.hasNext()) {
         String key = (String)i.next();
         Object value = this.get(key);
         System.out.println(key + " => " + value);
      }

   }

   public String getString(String key) {
      return this.getString(key, (String)null);
   }

   public String getString(String key, String defaultValue) {
      Object value = this.get(key);
      if (value instanceof String) {
         return this.interpolate((String)value);
      } else if (value == null) {
         return this.defaults != null ? this.interpolate(this.defaults.getString(key, defaultValue)) : this.interpolate(defaultValue);
      } else if (value instanceof List) {
         return this.interpolate((String)((List)value).get(0));
      } else {
         throw new ClassCastException('\'' + key + "' doesn't map to a String object");
      }
   }

   public Properties getProperties(String key) {
      return this.getProperties(key, new Properties());
   }

   public Properties getProperties(String key, Properties defaults) {
      String[] tokens = this.getStringArray(key);
      Properties props = new Properties(defaults);

      for(int i = 0; i < tokens.length; ++i) {
         String token = tokens[i];
         int equalSign = token.indexOf(61);
         if (equalSign <= 0) {
            throw new IllegalArgumentException('\'' + token + "' does not contain " + "an equals sign");
         }

         String pkey = token.substring(0, equalSign).trim();
         String pvalue = token.substring(equalSign + 1).trim();
         props.put(pkey, pvalue);
      }

      return props;
   }

   public String[] getStringArray(String key) {
      Object value = this.get(key);
      Object values;
      if (value instanceof String) {
         values = new Vector(1);
         ((List)values).add(value);
      } else {
         if (!(value instanceof List)) {
            if (value == null) {
               if (this.defaults != null) {
                  return this.defaults.getStringArray(key);
               }

               return new String[0];
            }

            throw new ClassCastException('\'' + key + "' doesn't map to a String/List object");
         }

         values = (List)value;
      }

      String[] tokens = new String[((List)values).size()];

      for(int i = 0; i < tokens.length; ++i) {
         tokens[i] = (String)((List)values).get(i);
      }

      return tokens;
   }

   public Vector getVector(String key) {
      return this.getVector(key, (Vector)null);
   }

   public Vector getVector(String key, Vector defaultValue) {
      Object value = this.get(key);
      if (value instanceof List) {
         return new Vector((List)value);
      } else if (value instanceof String) {
         Vector values = new Vector(1);
         values.add(value);
         this.put(key, values);
         return values;
      } else if (value == null) {
         if (this.defaults != null) {
            return this.defaults.getVector(key, defaultValue);
         } else {
            return defaultValue == null ? new Vector() : defaultValue;
         }
      } else {
         throw new ClassCastException('\'' + key + "' doesn't map to a Vector object");
      }
   }

   public List getList(String key) {
      return this.getList(key, (List)null);
   }

   public List getList(String key, List defaultValue) {
      Object value = this.get(key);
      if (value instanceof List) {
         return new ArrayList((List)value);
      } else if (value instanceof String) {
         List values = new ArrayList(1);
         values.add(value);
         this.put(key, values);
         return values;
      } else if (value == null) {
         if (this.defaults != null) {
            return this.defaults.getList(key, defaultValue);
         } else {
            return (List)(defaultValue == null ? new ArrayList() : defaultValue);
         }
      } else {
         throw new ClassCastException('\'' + key + "' doesn't map to a List object");
      }
   }

   public boolean getBoolean(String key) {
      Boolean b = this.getBoolean(key, (Boolean)null);
      if (b != null) {
         return b;
      } else {
         throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
      }
   }

   public boolean getBoolean(String key, boolean defaultValue) {
      return this.getBoolean(key, new Boolean(defaultValue));
   }

   public Boolean getBoolean(String key, Boolean defaultValue) {
      Object value = this.get(key);
      if (value instanceof Boolean) {
         return (Boolean)value;
      } else if (value instanceof String) {
         String s = this.testBoolean((String)value);
         Boolean b = new Boolean(s);
         this.put(key, b);
         return b;
      } else if (value == null) {
         return this.defaults != null ? this.defaults.getBoolean(key, defaultValue) : defaultValue;
      } else {
         throw new ClassCastException('\'' + key + "' doesn't map to a Boolean object");
      }
   }

   public String testBoolean(String value) {
      String s = value.toLowerCase();
      if (!s.equals("true") && !s.equals("on") && !s.equals("yes")) {
         return !s.equals("false") && !s.equals("off") && !s.equals("no") ? null : "false";
      } else {
         return "true";
      }
   }

   public byte getByte(String key) {
      Byte b = this.getByte(key, (Byte)null);
      if (b != null) {
         return b;
      } else {
         throw new NoSuchElementException('\'' + key + " doesn't map to an existing object");
      }
   }

   public byte getByte(String key, byte defaultValue) {
      return this.getByte(key, new Byte(defaultValue));
   }

   public Byte getByte(String key, Byte defaultValue) {
      Object value = this.get(key);
      if (value instanceof Byte) {
         return (Byte)value;
      } else if (value instanceof String) {
         Byte b = new Byte((String)value);
         this.put(key, b);
         return b;
      } else if (value == null) {
         return this.defaults != null ? this.defaults.getByte(key, defaultValue) : defaultValue;
      } else {
         throw new ClassCastException('\'' + key + "' doesn't map to a Byte object");
      }
   }

   public short getShort(String key) {
      Short s = this.getShort(key, (Short)null);
      if (s != null) {
         return s;
      } else {
         throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
      }
   }

   public short getShort(String key, short defaultValue) {
      return this.getShort(key, new Short(defaultValue));
   }

   public Short getShort(String key, Short defaultValue) {
      Object value = this.get(key);
      if (value instanceof Short) {
         return (Short)value;
      } else if (value instanceof String) {
         Short s = new Short((String)value);
         this.put(key, s);
         return s;
      } else if (value == null) {
         return this.defaults != null ? this.defaults.getShort(key, defaultValue) : defaultValue;
      } else {
         throw new ClassCastException('\'' + key + "' doesn't map to a Short object");
      }
   }

   public int getInt(String name) {
      return this.getInteger(name);
   }

   public int getInt(String name, int def) {
      return this.getInteger(name, def);
   }

   public int getInteger(String key) {
      Integer i = this.getInteger(key, (Integer)null);
      if (i != null) {
         return i;
      } else {
         throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
      }
   }

   public int getInteger(String key, int defaultValue) {
      Integer i = this.getInteger(key, (Integer)null);
      return i == null ? defaultValue : i;
   }

   public Integer getInteger(String key, Integer defaultValue) {
      Object value = this.get(key);
      if (value instanceof Integer) {
         return (Integer)value;
      } else if (value instanceof String) {
         Integer i = new Integer((String)value);
         this.put(key, i);
         return i;
      } else if (value == null) {
         return this.defaults != null ? this.defaults.getInteger(key, defaultValue) : defaultValue;
      } else {
         throw new ClassCastException('\'' + key + "' doesn't map to a Integer object");
      }
   }

   public long getLong(String key) {
      Long l = this.getLong(key, (Long)null);
      if (l != null) {
         return l;
      } else {
         throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
      }
   }

   public long getLong(String key, long defaultValue) {
      return this.getLong(key, new Long(defaultValue));
   }

   public Long getLong(String key, Long defaultValue) {
      Object value = this.get(key);
      if (value instanceof Long) {
         return (Long)value;
      } else if (value instanceof String) {
         Long l = new Long((String)value);
         this.put(key, l);
         return l;
      } else if (value == null) {
         return this.defaults != null ? this.defaults.getLong(key, defaultValue) : defaultValue;
      } else {
         throw new ClassCastException('\'' + key + "' doesn't map to a Long object");
      }
   }

   public float getFloat(String key) {
      Float f = this.getFloat(key, (Float)null);
      if (f != null) {
         return f;
      } else {
         throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
      }
   }

   public float getFloat(String key, float defaultValue) {
      return this.getFloat(key, new Float(defaultValue));
   }

   public Float getFloat(String key, Float defaultValue) {
      Object value = this.get(key);
      if (value instanceof Float) {
         return (Float)value;
      } else if (value instanceof String) {
         Float f = new Float((String)value);
         this.put(key, f);
         return f;
      } else if (value == null) {
         return this.defaults != null ? this.defaults.getFloat(key, defaultValue) : defaultValue;
      } else {
         throw new ClassCastException('\'' + key + "' doesn't map to a Float object");
      }
   }

   public double getDouble(String key) {
      Double d = this.getDouble(key, (Double)null);
      if (d != null) {
         return d;
      } else {
         throw new NoSuchElementException('\'' + key + "' doesn't map to an existing object");
      }
   }

   public double getDouble(String key, double defaultValue) {
      return this.getDouble(key, new Double(defaultValue));
   }

   public Double getDouble(String key, Double defaultValue) {
      Object value = this.get(key);
      if (value instanceof Double) {
         return (Double)value;
      } else if (value instanceof String) {
         Double d = new Double((String)value);
         this.put(key, d);
         return d;
      } else if (value == null) {
         return this.defaults != null ? this.defaults.getDouble(key, defaultValue) : defaultValue;
      } else {
         throw new ClassCastException('\'' + key + "' doesn't map to a Double object");
      }
   }

   public static ExtendedProperties convertProperties(Properties props) {
      ExtendedProperties c = new ExtendedProperties();
      Enumeration e = props.propertyNames();

      while(e.hasMoreElements()) {
         String s = (String)e.nextElement();
         c.setProperty(s, props.getProperty(s));
      }

      return c;
   }

   static class PropertiesTokenizer extends StringTokenizer {
      static final String DELIMITER = ",";

      public PropertiesTokenizer(String string) {
         super(string, ",");
      }

      public boolean hasMoreTokens() {
         return super.hasMoreTokens();
      }

      public String nextToken() {
         StringBuffer buffer = new StringBuffer();

         while(this.hasMoreTokens()) {
            String token = super.nextToken();
            if (!ExtendedProperties.endsWithSlash(token)) {
               buffer.append(token);
               break;
            }

            buffer.append(token.substring(0, token.length() - 1));
            buffer.append(",");
         }

         return buffer.toString().trim();
      }
   }

   static class PropertiesReader extends LineNumberReader {
      public PropertiesReader(Reader reader) {
         super(reader);
      }

      public String readProperty() throws IOException {
         StringBuffer buffer = new StringBuffer();

         for(String line = this.readLine(); line != null; line = this.readLine()) {
            line = line.trim();
            if (line.length() != 0 && line.charAt(0) != '#') {
               if (!ExtendedProperties.endsWithSlash(line)) {
                  buffer.append(line);
                  return buffer.toString();
               }

               line = line.substring(0, line.length() - 1);
               buffer.append(line);
            }
         }

         return null;
      }
   }
}
