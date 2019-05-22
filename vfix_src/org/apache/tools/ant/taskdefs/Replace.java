package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.StringUtils;

public class Replace extends MatchingTask {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private File src = null;
   private Replace.NestedString token = null;
   private Replace.NestedString value = new Replace.NestedString();
   private File propertyFile = null;
   private File replaceFilterFile = null;
   private Properties properties = null;
   private Vector replacefilters = new Vector();
   private File dir = null;
   private int fileCount;
   private int replaceCount;
   private boolean summary = false;
   private String encoding = null;

   public void execute() throws BuildException {
      Vector savedFilters = (Vector)this.replacefilters.clone();
      Properties savedProperties = this.properties == null ? null : (Properties)this.properties.clone();
      if (this.token != null) {
         StringBuffer val = new StringBuffer(this.value.getText());
         this.stringReplace(val, "\r\n", "\n");
         this.stringReplace(val, "\n", StringUtils.LINE_SEP);
         StringBuffer tok = new StringBuffer(this.token.getText());
         this.stringReplace(tok, "\r\n", "\n");
         this.stringReplace(tok, "\n", StringUtils.LINE_SEP);
         Replace.Replacefilter firstFilter = this.createPrimaryfilter();
         firstFilter.setToken(tok.toString());
         firstFilter.setValue(val.toString());
      }

      try {
         if (this.replaceFilterFile != null) {
            Properties props = this.getProperties(this.replaceFilterFile);
            Enumeration e = props.keys();

            while(e.hasMoreElements()) {
               String tok = e.nextElement().toString();
               Replace.Replacefilter replaceFilter = this.createReplacefilter();
               replaceFilter.setToken(tok);
               replaceFilter.setValue(props.getProperty(tok));
            }
         }

         this.validateAttributes();
         if (this.propertyFile != null) {
            this.properties = this.getProperties(this.propertyFile);
         }

         this.validateReplacefilters();
         this.fileCount = 0;
         this.replaceCount = 0;
         if (this.src != null) {
            this.processFile(this.src);
         }

         if (this.dir != null) {
            DirectoryScanner ds = super.getDirectoryScanner(this.dir);
            String[] srcs = ds.getIncludedFiles();

            for(int i = 0; i < srcs.length; ++i) {
               File file = new File(this.dir, srcs[i]);
               this.processFile(file);
            }
         }

         if (this.summary) {
            this.log("Replaced " + this.replaceCount + " occurrences in " + this.fileCount + " files.", 2);
         }
      } finally {
         this.replacefilters = savedFilters;
         this.properties = savedProperties;
      }

   }

   public void validateAttributes() throws BuildException {
      String message;
      if (this.src == null && this.dir == null) {
         message = "Either the file or the dir attribute must be specified";
         throw new BuildException(message, this.getLocation());
      } else if (this.propertyFile != null && !this.propertyFile.exists()) {
         message = "Property file " + this.propertyFile.getPath() + " does not exist.";
         throw new BuildException(message, this.getLocation());
      } else if (this.token == null && this.replacefilters.size() == 0) {
         message = "Either token or a nested replacefilter must be specified";
         throw new BuildException(message, this.getLocation());
      } else if (this.token != null && "".equals(this.token.getText())) {
         message = "The token attribute must not be an empty string.";
         throw new BuildException(message, this.getLocation());
      }
   }

   public void validateReplacefilters() throws BuildException {
      for(int i = 0; i < this.replacefilters.size(); ++i) {
         Replace.Replacefilter element = (Replace.Replacefilter)this.replacefilters.elementAt(i);
         element.validate();
      }

   }

   public Properties getProperties(File propertyFile) throws BuildException {
      Properties props = new Properties();
      FileInputStream in = null;

      try {
         String message;
         try {
            in = new FileInputStream(propertyFile);
            props.load(in);
         } catch (FileNotFoundException var10) {
            message = "Property file (" + propertyFile.getPath() + ") not found.";
            throw new BuildException(message);
         } catch (IOException var11) {
            message = "Property file (" + propertyFile.getPath() + ") cannot be loaded.";
            throw new BuildException(message);
         }
      } finally {
         FileUtils.close((InputStream)in);
      }

      return props;
   }

   private void processFile(File src) throws BuildException {
      if (!src.exists()) {
         throw new BuildException("Replace: source file " + src.getPath() + " doesn't exist", this.getLocation());
      } else {
         File temp = null;
         Replace.FileInput in = null;
         Replace.FileOutput out = null;

         try {
            in = new Replace.FileInput(src);
            temp = FILE_UTILS.createTempFile("rep", ".tmp", src.getParentFile());
            out = new Replace.FileOutput(temp);
            int repCountStart = this.replaceCount;
            this.logFilterChain(src.getPath());
            out.setInputBuffer(this.buildFilterChain(in.getOutputBuffer()));

            while(in.readChunk()) {
               if (this.processFilterChain()) {
                  out.process();
               }
            }

            this.flushFilterChain();
            out.flush();
            in.close();
            in = null;
            out.close();
            out = null;
            boolean changes = this.replaceCount != repCountStart;
            if (changes) {
               FILE_UTILS.rename(temp, src);
               temp = null;
            }
         } catch (IOException var10) {
            throw new BuildException("IOException in " + src + " - " + var10.getClass().getName() + ":" + var10.getMessage(), var10, this.getLocation());
         } finally {
            if (null != in) {
               in.closeQuietly();
            }

            if (null != out) {
               out.closeQuietly();
            }

            if (temp != null && !temp.delete()) {
               temp.deleteOnExit();
            }

         }

      }
   }

   private void flushFilterChain() {
      for(int i = 0; i < this.replacefilters.size(); ++i) {
         Replace.Replacefilter filter = (Replace.Replacefilter)this.replacefilters.elementAt(i);
         filter.flush();
      }

   }

   private boolean processFilterChain() {
      for(int i = 0; i < this.replacefilters.size(); ++i) {
         Replace.Replacefilter filter = (Replace.Replacefilter)this.replacefilters.elementAt(i);
         if (!filter.process()) {
            return false;
         }
      }

      return true;
   }

   private StringBuffer buildFilterChain(StringBuffer inputBuffer) {
      StringBuffer buf = inputBuffer;

      for(int i = 0; i < this.replacefilters.size(); ++i) {
         Replace.Replacefilter filter = (Replace.Replacefilter)this.replacefilters.elementAt(i);
         filter.setInputBuffer(buf);
         buf = filter.getOutputBuffer();
      }

      return buf;
   }

   private void logFilterChain(String filename) {
      for(int i = 0; i < this.replacefilters.size(); ++i) {
         Replace.Replacefilter filter = (Replace.Replacefilter)this.replacefilters.elementAt(i);
         this.log("Replacing in " + filename + ": " + filter.getToken() + " --> " + filter.getReplaceValue(), 3);
      }

   }

   public void setFile(File file) {
      this.src = file;
   }

   public void setSummary(boolean summary) {
      this.summary = summary;
   }

   public void setReplaceFilterFile(File replaceFilterFile) {
      this.replaceFilterFile = replaceFilterFile;
   }

   public void setDir(File dir) {
      this.dir = dir;
   }

   public void setToken(String token) {
      this.createReplaceToken().addText(token);
   }

   public void setValue(String value) {
      this.createReplaceValue().addText(value);
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public Replace.NestedString createReplaceToken() {
      if (this.token == null) {
         this.token = new Replace.NestedString();
      }

      return this.token;
   }

   public Replace.NestedString createReplaceValue() {
      return this.value;
   }

   public void setPropertyFile(File propertyFile) {
      this.propertyFile = propertyFile;
   }

   public Replace.Replacefilter createReplacefilter() {
      Replace.Replacefilter filter = new Replace.Replacefilter();
      this.replacefilters.addElement(filter);
      return filter;
   }

   private Replace.Replacefilter createPrimaryfilter() {
      Replace.Replacefilter filter = new Replace.Replacefilter();
      this.replacefilters.insertElementAt(filter, 0);
      return filter;
   }

   private void stringReplace(StringBuffer str, String str1, String str2) {
      for(int found = str.toString().indexOf(str1); found >= 0; found = str.toString().indexOf(str1, found + str2.length())) {
         str.replace(found, found + str1.length(), str2);
      }

   }

   private class FileOutput {
      private StringBuffer inputBuffer;
      private Writer writer;

      FileOutput(File out) throws IOException {
         if (Replace.this.encoding == null) {
            this.writer = new BufferedWriter(new FileWriter(out));
         } else {
            this.writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), Replace.this.encoding));
         }

      }

      void setInputBuffer(StringBuffer input) {
         this.inputBuffer = input;
      }

      boolean process() throws IOException {
         this.writer.write(this.inputBuffer.toString());
         this.inputBuffer.delete(0, this.inputBuffer.length());
         return false;
      }

      void flush() throws IOException {
         this.process();
         this.writer.flush();
      }

      void close() throws IOException {
         this.writer.close();
      }

      void closeQuietly() {
         FileUtils.close(this.writer);
      }
   }

   private class FileInput {
      private StringBuffer outputBuffer = new StringBuffer();
      private Reader reader;
      private char[] buffer = new char[4096];
      private static final int BUFF_SIZE = 4096;

      FileInput(File source) throws IOException {
         if (Replace.this.encoding == null) {
            this.reader = new BufferedReader(new FileReader(source));
         } else {
            this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), Replace.this.encoding));
         }

      }

      StringBuffer getOutputBuffer() {
         return this.outputBuffer;
      }

      boolean readChunk() throws IOException {
         int bufferLength = false;
         int bufferLengthx = this.reader.read(this.buffer);
         if (bufferLengthx < 0) {
            return false;
         } else {
            this.outputBuffer.append(new String(this.buffer, 0, bufferLengthx));
            return true;
         }
      }

      void close() throws IOException {
         this.reader.close();
      }

      void closeQuietly() {
         FileUtils.close(this.reader);
      }
   }

   public class Replacefilter {
      private String token;
      private String value;
      private String replaceValue;
      private String property;
      private StringBuffer inputBuffer;
      private StringBuffer outputBuffer = new StringBuffer();

      public void validate() throws BuildException {
         String message;
         if (this.token == null) {
            message = "token is a mandatory attribute of replacefilter.";
            throw new BuildException(message);
         } else if ("".equals(this.token)) {
            message = "The token attribute must not be an empty string.";
            throw new BuildException(message);
         } else if (this.value != null && this.property != null) {
            message = "Either value or property can be specified, but a replacefilter element cannot have both.";
            throw new BuildException(message);
         } else {
            if (this.property != null) {
               if (Replace.this.propertyFile == null) {
                  message = "The replacefilter's property attribute can only be used with the replacetask's propertyFile attribute.";
                  throw new BuildException(message);
               }

               if (Replace.this.properties == null || Replace.this.properties.getProperty(this.property) == null) {
                  message = "property \"" + this.property + "\" was not found in " + Replace.this.propertyFile.getPath();
                  throw new BuildException(message);
               }
            }

            this.replaceValue = this.getReplaceValue();
         }
      }

      public String getReplaceValue() {
         if (this.property != null) {
            return Replace.this.properties.getProperty(this.property);
         } else if (this.value != null) {
            return this.value;
         } else {
            return Replace.this.value != null ? Replace.this.value.getText() : "";
         }
      }

      public void setToken(String token) {
         this.token = token;
      }

      public String getToken() {
         return this.token;
      }

      public void setValue(String value) {
         this.value = value;
      }

      public String getValue() {
         return this.value;
      }

      public void setProperty(String property) {
         this.property = property;
      }

      public String getProperty() {
         return this.property;
      }

      StringBuffer getOutputBuffer() {
         return this.outputBuffer;
      }

      void setInputBuffer(StringBuffer input) {
         this.inputBuffer = input;
      }

      boolean process() {
         if (this.inputBuffer.length() > this.token.length()) {
            int pos = this.replace();
            pos = Math.max(this.inputBuffer.length() - this.token.length(), pos);
            this.outputBuffer.append(this.inputBuffer.substring(0, pos));
            this.inputBuffer.delete(0, pos);
            return true;
         } else {
            return false;
         }
      }

      void flush() {
         this.replace();
         this.outputBuffer.append(this.inputBuffer.toString());
         this.inputBuffer.delete(0, this.inputBuffer.length());
      }

      private int replace() {
         int found = this.inputBuffer.toString().indexOf(this.token);
         int pos = -1;

         while(found >= 0) {
            this.inputBuffer.replace(found, found + this.token.length(), this.replaceValue);
            pos = found + this.replaceValue.length();
            found = this.inputBuffer.toString().indexOf(this.token, pos);
            ++Replace.this.replaceCount;
         }

         return pos;
      }
   }

   public class NestedString {
      private StringBuffer buf = new StringBuffer();

      public void addText(String val) {
         this.buf.append(val);
      }

      public String getText() {
         return this.buf.toString();
      }
   }
}
