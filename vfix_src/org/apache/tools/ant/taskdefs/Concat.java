package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.filters.util.ChainReaderHelper;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.types.resources.Resources;
import org.apache.tools.ant.types.resources.Restrict;
import org.apache.tools.ant.types.resources.StringResource;
import org.apache.tools.ant.types.resources.selectors.Exists;
import org.apache.tools.ant.types.resources.selectors.Not;
import org.apache.tools.ant.types.resources.selectors.ResourceSelector;
import org.apache.tools.ant.util.ConcatResourceInputStream;
import org.apache.tools.ant.util.FileUtils;

public class Concat extends Task {
   private static final int BUFFER_SIZE = 8192;
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private static final ResourceSelector EXISTS = new Exists();
   private static final ResourceSelector NOT_EXISTS;
   private File destinationFile;
   private boolean append;
   private String encoding;
   private String outputEncoding;
   private boolean binary;
   private StringBuffer textBuffer;
   private Resources rc;
   private Vector filterChains;
   private boolean forceOverwrite = true;
   private Concat.TextElement footer;
   private Concat.TextElement header;
   private boolean fixLastLine = false;
   private String eolString;
   private Writer outputWriter = null;

   public Concat() {
      this.reset();
   }

   public void reset() {
      this.append = false;
      this.forceOverwrite = true;
      this.destinationFile = null;
      this.encoding = null;
      this.outputEncoding = null;
      this.fixLastLine = false;
      this.filterChains = null;
      this.footer = null;
      this.header = null;
      this.binary = false;
      this.outputWriter = null;
      this.textBuffer = null;
      this.eolString = System.getProperty("line.separator");
      this.rc = null;
   }

   public void setDestfile(File destinationFile) {
      this.destinationFile = destinationFile;
   }

   public void setAppend(boolean append) {
      this.append = append;
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
      if (this.outputEncoding == null) {
         this.outputEncoding = encoding;
      }

   }

   public void setOutputEncoding(String outputEncoding) {
      this.outputEncoding = outputEncoding;
   }

   public void setForce(boolean force) {
      this.forceOverwrite = force;
   }

   public Path createPath() {
      Path path = new Path(this.getProject());
      this.add(path);
      return path;
   }

   public void addFileset(FileSet set) {
      this.add(set);
   }

   public void addFilelist(FileList list) {
      this.add(list);
   }

   public void add(ResourceCollection c) {
      this.rc = this.rc == null ? new Resources() : this.rc;
      this.rc.add(c);
   }

   public void addFilterChain(FilterChain filterChain) {
      if (this.filterChains == null) {
         this.filterChains = new Vector();
      }

      this.filterChains.addElement(filterChain);
   }

   public void addText(String text) {
      if (this.textBuffer == null) {
         this.textBuffer = new StringBuffer(text.length());
      }

      this.textBuffer.append(text);
   }

   public void addHeader(Concat.TextElement headerToAdd) {
      this.header = headerToAdd;
   }

   public void addFooter(Concat.TextElement footerToAdd) {
      this.footer = footerToAdd;
   }

   public void setFixLastLine(boolean fixLastLine) {
      this.fixLastLine = fixLastLine;
   }

   public void setEol(FixCRLF.CrLf crlf) {
      String s = crlf.getValue();
      if (!s.equals("cr") && !s.equals("mac")) {
         if (!s.equals("lf") && !s.equals("unix")) {
            if (s.equals("crlf") || s.equals("dos")) {
               this.eolString = "\r\n";
            }
         } else {
            this.eolString = "\n";
         }
      } else {
         this.eolString = "\r";
      }

   }

   public void setWriter(Writer outputWriter) {
      this.outputWriter = outputWriter;
   }

   public void setBinary(boolean binary) {
      this.binary = binary;
   }

   private ResourceCollection validate() {
      this.sanitizeText();
      if (this.binary) {
         label125: {
            if (this.destinationFile == null) {
               throw new BuildException("destfile attribute is required for binary concatenation");
            }

            if (this.textBuffer != null) {
               throw new BuildException("Nested text is incompatible with binary concatenation");
            }

            if (this.encoding == null && this.outputEncoding == null) {
               if (this.filterChains != null) {
                  throw new BuildException("Setting filters is incompatible with binary concatenation");
               }

               if (this.fixLastLine) {
                  throw new BuildException("Setting fixlastline is incompatible with binary concatenation");
               }

               if (this.header == null && this.footer == null) {
                  break label125;
               }

               throw new BuildException("Nested header or footer is incompatible with binary concatenation");
            }

            throw new BuildException("Seting input or output encoding is incompatible with binary concatenation");
         }
      }

      if (this.destinationFile != null && this.outputWriter != null) {
         throw new BuildException("Cannot specify both a destination file and an output writer");
      } else if (this.rc == null && this.textBuffer == null) {
         throw new BuildException("At least one resource must be provided, or some text.");
      } else if (this.rc == null) {
         StringResource s = new StringResource();
         s.setProject(this.getProject());
         s.setValue(this.textBuffer.toString());
         return s;
      } else if (this.textBuffer != null) {
         throw new BuildException("Cannot include inline text when using resources.");
      } else {
         Restrict noexistRc = new Restrict();
         noexistRc.add(NOT_EXISTS);
         noexistRc.add((ResourceCollection)this.rc);
         Iterator i = noexistRc.iterator();

         while(i.hasNext()) {
            this.log(i.next() + " does not exist.", 0);
         }

         if (this.destinationFile != null) {
            i = this.rc.iterator();

            while(i.hasNext()) {
               Object o = i.next();
               if (o instanceof FileResource) {
                  File f = ((FileResource)o).getFile();
                  if (FILE_UTILS.fileNameEquals(f, this.destinationFile)) {
                     throw new BuildException("Input file \"" + f + "\" is the same as the output file.");
                  }
               }
            }
         }

         Restrict existRc = new Restrict();
         existRc.add(EXISTS);
         existRc.add((ResourceCollection)this.rc);
         boolean outofdate = this.destinationFile == null || this.forceOverwrite;
         Resource r;
         if (!outofdate) {
            for(Iterator i = existRc.iterator(); !outofdate && i.hasNext(); outofdate = r.getLastModified() == 0L || r.getLastModified() > this.destinationFile.lastModified()) {
               r = (Resource)i.next();
            }
         }

         if (!outofdate) {
            this.log(this.destinationFile + " is up-to-date.", 3);
            return null;
         } else {
            return existRc;
         }
      }
   }

   public void execute() {
      ResourceCollection c = this.validate();
      if (c != null) {
         if (c.size() < 1 && this.header == null && this.footer == null) {
            this.log("No existing resources and no nested text, doing nothing", 2);
         } else {
            if (this.binary) {
               this.binaryCat(c);
            } else {
               this.cat(c);
            }

         }
      }
   }

   private void binaryCat(ResourceCollection c) {
      this.log("Binary concatenation of " + c.size() + " resources to " + this.destinationFile);
      FileOutputStream out = null;
      ConcatResourceInputStream in = null;

      try {
         try {
            out = new FileOutputStream(this.destinationFile);
         } catch (Exception var18) {
            throw new BuildException("Unable to open " + this.destinationFile + " for writing", var18);
         }

         in = new ConcatResourceInputStream(c);
         ((ConcatResourceInputStream)in).setManagingComponent(this);
         Thread t = new Thread(new StreamPumper(in, out));
         t.start();

         try {
            t.join();
         } catch (InterruptedException var17) {
            try {
               t.join();
            } catch (InterruptedException var16) {
            }
         }
      } finally {
         FileUtils.close((InputStream)in);
         if (out != null) {
            try {
               out.close();
            } catch (Exception var15) {
               throw new BuildException("Unable to close " + this.destinationFile, var15);
            }
         }

      }

   }

   private void cat(ResourceCollection c) {
      OutputStream os = null;
      char[] buffer = new char[8192];

      try {
         PrintWriter writer = null;
         if (this.outputWriter != null) {
            writer = new PrintWriter(this.outputWriter);
         } else {
            if (this.destinationFile == null) {
               os = new LogOutputStream(this, 1);
            } else {
               File parent = this.destinationFile.getParentFile();
               if (!parent.exists()) {
                  parent.mkdirs();
               }

               os = new FileOutputStream(this.destinationFile.getAbsolutePath(), this.append);
            }

            if (this.outputEncoding == null) {
               writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter((OutputStream)os)));
            } else {
               writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter((OutputStream)os, this.outputEncoding)));
            }
         }

         if (this.header != null) {
            if (this.header.getFiltering()) {
               this.concatenate(buffer, writer, new StringReader(this.header.getValue()));
            } else {
               writer.print(this.header.getValue());
            }
         }

         if (c.size() > 0) {
            this.concatenate(buffer, writer, new Concat.MultiReader(c));
         }

         if (this.footer != null) {
            if (this.footer.getFiltering()) {
               this.concatenate(buffer, writer, new StringReader(this.footer.getValue()));
            } else {
               writer.print(this.footer.getValue());
            }
         }

         writer.flush();
         if (os != null) {
            ((OutputStream)os).flush();
         }
      } catch (IOException var9) {
         throw new BuildException("Error while concatenating: " + var9.getMessage(), var9);
      } finally {
         FileUtils.close((OutputStream)os);
      }

   }

   private void concatenate(char[] buffer, Writer writer, Reader in) throws IOException {
      if (this.filterChains != null) {
         ChainReaderHelper helper = new ChainReaderHelper();
         helper.setBufferSize(8192);
         helper.setPrimaryReader((Reader)in);
         helper.setFilterChains(this.filterChains);
         helper.setProject(this.getProject());
         in = new BufferedReader(helper.getAssembledReader());
      }

      while(true) {
         int nRead = ((Reader)in).read(buffer, 0, buffer.length);
         if (nRead == -1) {
            writer.flush();
            return;
         }

         writer.write(buffer, 0, nRead);
      }
   }

   private void sanitizeText() {
      if (this.textBuffer != null && this.textBuffer.substring(0).trim().length() == 0) {
         this.textBuffer = null;
      }

   }

   static {
      NOT_EXISTS = new Not(EXISTS);
   }

   private class MultiReader extends Reader {
      private Reader reader;
      private int lastPos;
      private char[] lastChars;
      private boolean needAddSeparator;
      private Iterator i;

      private MultiReader(ResourceCollection c) {
         this.reader = null;
         this.lastPos = 0;
         this.lastChars = new char[Concat.this.eolString.length()];
         this.needAddSeparator = false;
         this.i = c.iterator();
      }

      private Reader getReader() throws IOException {
         if (this.reader == null && this.i.hasNext()) {
            Resource r = (Resource)this.i.next();
            Concat.this.log("Concating " + r.toLongString(), 3);
            InputStream is = r.getInputStream();
            this.reader = new BufferedReader(Concat.this.encoding == null ? new InputStreamReader(is) : new InputStreamReader(is, Concat.this.encoding));
            Arrays.fill(this.lastChars, '\u0000');
         }

         return this.reader;
      }

      private void nextReader() throws IOException {
         this.close();
         this.reader = null;
      }

      public int read() throws IOException {
         if (this.needAddSeparator) {
            int ret = Concat.this.eolString.charAt(this.lastPos++);
            if (this.lastPos >= Concat.this.eolString.length()) {
               this.lastPos = 0;
               this.needAddSeparator = false;
            }

            return ret;
         } else {
            while(this.getReader() != null) {
               int ch = this.getReader().read();
               if (ch != -1) {
                  this.addLastChar((char)ch);
                  return ch;
               }

               this.nextReader();
               if (Concat.this.fixLastLine && this.isMissingEndOfLine()) {
                  this.needAddSeparator = true;
                  this.lastPos = 0;
               }
            }

            return -1;
         }
      }

      public int read(char[] cbuf, int off, int len) throws IOException {
         int amountRead = 0;

         label57:
         do {
            while(this.getReader() != null || this.needAddSeparator) {
               if (this.needAddSeparator) {
                  cbuf[off] = Concat.this.eolString.charAt(this.lastPos++);
                  if (this.lastPos >= Concat.this.eolString.length()) {
                     this.lastPos = 0;
                     this.needAddSeparator = false;
                  }

                  --len;
                  ++off;
                  ++amountRead;
                  continue label57;
               }

               int nRead = this.getReader().read(cbuf, off, len);
               if (nRead != -1 && nRead != 0) {
                  if (Concat.this.fixLastLine) {
                     for(int i = nRead; i > nRead - this.lastChars.length && i > 0; --i) {
                        this.addLastChar(cbuf[off + i - 1]);
                     }
                  }

                  len -= nRead;
                  off += nRead;
                  amountRead += nRead;
                  if (len == 0) {
                     return amountRead;
                  }
               } else {
                  this.nextReader();
                  if (Concat.this.fixLastLine && this.isMissingEndOfLine()) {
                     this.needAddSeparator = true;
                     this.lastPos = 0;
                  }
               }
            }

            if (amountRead == 0) {
               return -1;
            }

            return amountRead;
         } while(len != 0);

         return amountRead;
      }

      public void close() throws IOException {
         if (this.reader != null) {
            this.reader.close();
         }

      }

      private void addLastChar(char ch) {
         for(int i = this.lastChars.length - 2; i >= 0; --i) {
            this.lastChars[i] = this.lastChars[i + 1];
         }

         this.lastChars[this.lastChars.length - 1] = ch;
      }

      private boolean isMissingEndOfLine() {
         for(int i = 0; i < this.lastChars.length; ++i) {
            if (this.lastChars[i] != Concat.this.eolString.charAt(i)) {
               return true;
            }
         }

         return false;
      }

      // $FF: synthetic method
      MultiReader(ResourceCollection x1, Object x2) {
         this(x1);
      }
   }

   public static class TextElement extends ProjectComponent {
      private String value = "";
      private boolean trimLeading = false;
      private boolean trim = false;
      private boolean filtering = true;
      private String encoding = null;

      public void setFiltering(boolean filtering) {
         this.filtering = filtering;
      }

      private boolean getFiltering() {
         return this.filtering;
      }

      public void setEncoding(String encoding) {
         this.encoding = encoding;
      }

      public void setFile(File file) throws BuildException {
         if (!file.exists()) {
            throw new BuildException("File " + file + " does not exist.");
         } else {
            BufferedReader reader = null;

            try {
               if (this.encoding == null) {
                  reader = new BufferedReader(new FileReader(file));
               } else {
                  reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), this.encoding));
               }

               this.value = FileUtils.readFully(reader);
            } catch (IOException var7) {
               throw new BuildException(var7);
            } finally {
               FileUtils.close((Reader)reader);
            }

         }
      }

      public void addText(String value) {
         this.value = this.value + this.getProject().replaceProperties(value);
      }

      public void setTrimLeading(boolean strip) {
         this.trimLeading = strip;
      }

      public void setTrim(boolean trim) {
         this.trim = trim;
      }

      public String getValue() {
         if (this.value == null) {
            this.value = "";
         }

         if (this.value.trim().length() == 0) {
            this.value = "";
         }

         if (this.trimLeading) {
            char[] current = this.value.toCharArray();
            StringBuffer b = new StringBuffer(current.length);
            boolean startOfLine = true;
            int pos = 0;

            label45:
            while(true) {
               char ch;
               do {
                  while(true) {
                     if (pos >= current.length) {
                        this.value = b.toString();
                        break label45;
                     }

                     ch = current[pos++];
                     if (!startOfLine) {
                        break;
                     }

                     if (ch != ' ' && ch != '\t') {
                        startOfLine = false;
                        break;
                     }
                  }

                  b.append(ch);
               } while(ch != '\n' && ch != '\r');

               startOfLine = true;
            }
         }

         if (this.trim) {
            this.value = this.value.trim();
         }

         return this.value;
      }
   }
}
