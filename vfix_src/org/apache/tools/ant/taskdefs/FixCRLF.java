package org.apache.tools.ant.taskdefs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.filters.ChainableReader;
import org.apache.tools.ant.filters.FixCrLfFilter;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.FilterChain;
import org.apache.tools.ant.types.FilterSetCollection;
import org.apache.tools.ant.util.FileUtils;

public class FixCRLF extends MatchingTask implements ChainableReader {
   public static final String ERROR_FILE_AND_SRCDIR = "srcdir and file are mutually exclusive";
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private boolean preserveLastModified = false;
   private File srcDir;
   private File destDir = null;
   private File file;
   private FixCrLfFilter filter = new FixCrLfFilter();
   private Vector fcv = null;
   private String encoding = null;
   private String outputEncoding = null;

   public final Reader chain(Reader rdr) {
      return this.filter.chain(rdr);
   }

   public void setSrcdir(File srcDir) {
      this.srcDir = srcDir;
   }

   public void setDestdir(File destDir) {
      this.destDir = destDir;
   }

   public void setJavafiles(boolean javafiles) {
      this.filter.setJavafiles(javafiles);
   }

   public void setFile(File file) {
      this.file = file;
   }

   public void setEol(FixCRLF.CrLf attr) {
      this.filter.setEol(FixCrLfFilter.CrLf.newInstance(attr.getValue()));
   }

   /** @deprecated */
   public void setCr(FixCRLF.AddAsisRemove attr) {
      this.log("DEPRECATED: The cr attribute has been deprecated,", 1);
      this.log("Please use the eol attribute instead", 1);
      String option = attr.getValue();
      FixCRLF.CrLf c = new FixCRLF.CrLf();
      if (option.equals("remove")) {
         c.setValue("lf");
      } else if (option.equals("asis")) {
         c.setValue("asis");
      } else {
         c.setValue("crlf");
      }

      this.setEol(c);
   }

   public void setTab(FixCRLF.AddAsisRemove attr) {
      this.filter.setTab(FixCrLfFilter.AddAsisRemove.newInstance(attr.getValue()));
   }

   public void setTablength(int tlength) throws BuildException {
      try {
         this.filter.setTablength(tlength);
      } catch (IOException var3) {
         throw new BuildException(var3);
      }
   }

   public void setEof(FixCRLF.AddAsisRemove attr) {
      this.filter.setEof(FixCrLfFilter.AddAsisRemove.newInstance(attr.getValue()));
   }

   public void setEncoding(String encoding) {
      this.encoding = encoding;
   }

   public void setOutputEncoding(String outputEncoding) {
      this.outputEncoding = outputEncoding;
   }

   public void setFixlast(boolean fixlast) {
      this.filter.setFixlast(fixlast);
   }

   public void setPreserveLastModified(boolean preserve) {
      this.preserveLastModified = preserve;
   }

   public void execute() throws BuildException {
      this.validate();
      String enc = this.encoding == null ? "default" : this.encoding;
      this.log("options: eol=" + this.filter.getEol().getValue() + " tab=" + this.filter.getTab().getValue() + " eof=" + this.filter.getEof().getValue() + " tablength=" + this.filter.getTablength() + " encoding=" + enc + " outputencoding=" + (this.outputEncoding == null ? enc : this.outputEncoding), 3);
      DirectoryScanner ds = super.getDirectoryScanner(this.srcDir);
      String[] files = ds.getIncludedFiles();

      for(int i = 0; i < files.length; ++i) {
         this.processFile(files[i]);
      }

   }

   private void validate() throws BuildException {
      if (this.file != null) {
         if (this.srcDir != null) {
            throw new BuildException("srcdir and file are mutually exclusive");
         }

         this.fileset.setFile(this.file);
         this.srcDir = this.file.getParentFile();
      }

      if (this.srcDir == null) {
         throw new BuildException("srcdir attribute must be set!");
      } else if (!this.srcDir.exists()) {
         throw new BuildException("srcdir does not exist!");
      } else if (!this.srcDir.isDirectory()) {
         throw new BuildException("srcdir is not a directory!");
      } else {
         if (this.destDir != null) {
            if (!this.destDir.exists()) {
               throw new BuildException("destdir does not exist!");
            }

            if (!this.destDir.isDirectory()) {
               throw new BuildException("destdir is not a directory!");
            }
         }

      }
   }

   private void processFile(String file) throws BuildException {
      File srcFile = new File(this.srcDir, file);
      long lastModified = srcFile.lastModified();
      File destD = this.destDir == null ? this.srcDir : this.destDir;
      FilterChain fc;
      if (this.fcv == null) {
         fc = new FilterChain();
         fc.add(this.filter);
         this.fcv = new Vector(1);
         this.fcv.add(fc);
      }

      File tmpFile = FILE_UTILS.createTempFile("fixcrlf", "", (File)null);
      tmpFile.deleteOnExit();

      try {
         FILE_UTILS.copyFile((File)srcFile, (File)tmpFile, (FilterSetCollection)null, this.fcv, false, false, this.encoding, this.outputEncoding == null ? this.encoding : this.outputEncoding, this.getProject());
         File destFile = new File(destD, file);
         boolean destIsWrong = true;
         if (destFile.exists()) {
            this.log("destFile exists", 4);
            destIsWrong = !FILE_UTILS.contentEquals(destFile, tmpFile);
            this.log(destFile + (destIsWrong ? " is being written" : " is not written, as the contents are identical"), 4);
         }

         if (destIsWrong) {
            FILE_UTILS.rename(tmpFile, destFile);
            if (this.preserveLastModified) {
               this.log("preserved lastModified", 4);
               FILE_UTILS.setFileLastModified(destFile, lastModified);
            }

            fc = null;
         }

      } catch (IOException var9) {
         throw new BuildException(var9);
      }
   }

   public static class CrLf extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"asis", "cr", "lf", "crlf", "mac", "unix", "dos"};
      }
   }

   public static class AddAsisRemove extends EnumeratedAttribute {
      public String[] getValues() {
         return new String[]{"add", "asis", "remove"};
      }
   }

   /** @deprecated */
   protected class OneLiner implements Enumeration {
      private static final int UNDEF = -1;
      private static final int NOTJAVA = 0;
      private static final int LOOKING = 1;
      private static final int INBUFLEN = 8192;
      private static final int LINEBUFLEN = 200;
      private static final char CTRLZ = '\u001a';
      private int state;
      private StringBuffer eolStr;
      private StringBuffer eofStr;
      private BufferedReader reader;
      private StringBuffer line;
      private boolean reachedEof;
      private File srcFile;

      public OneLiner(File srcFile) throws BuildException {
         this.state = FixCRLF.this.filter.getJavafiles() ? 1 : 0;
         this.eolStr = new StringBuffer(200);
         this.eofStr = new StringBuffer();
         this.line = new StringBuffer();
         this.reachedEof = false;
         this.srcFile = srcFile;

         try {
            this.reader = new BufferedReader((Reader)(FixCRLF.this.encoding == null ? new FileReader(srcFile) : new InputStreamReader(new FileInputStream(srcFile), FixCRLF.this.encoding)), 8192);
            this.nextLine();
         } catch (IOException var4) {
            throw new BuildException(srcFile + ": " + var4.getMessage(), var4, FixCRLF.this.getLocation());
         }
      }

      protected void nextLine() throws BuildException {
         int ch = true;
         int eolcount = 0;
         this.eolStr = new StringBuffer();
         this.line = new StringBuffer();

         try {
            int chx;
            for(chx = this.reader.read(); chx != -1 && chx != 13 && chx != 10; chx = this.reader.read()) {
               this.line.append((char)chx);
            }

            if (chx == -1 && this.line.length() == 0) {
               this.reachedEof = true;
            } else {
               switch((char)chx) {
               case '\n':
                  ++eolcount;
                  this.eolStr.append('\n');
                  break;
               case '\r':
                  ++eolcount;
                  this.eolStr.append('\r');
                  this.reader.mark(2);
                  chx = this.reader.read();
                  switch(chx) {
                  case -1:
                     break;
                  case 10:
                     ++eolcount;
                     this.eolStr.append('\n');
                     break;
                  case 13:
                     chx = this.reader.read();
                     if ((char)chx == '\n') {
                        eolcount += 2;
                        this.eolStr.append("\r\n");
                     } else {
                        this.reader.reset();
                     }
                     break;
                  default:
                     this.reader.reset();
                  }
               }

               if (eolcount == 0) {
                  int i = this.line.length();

                  do {
                     --i;
                  } while(i >= 0 && this.line.charAt(i) == 26);

                  if (i < this.line.length() - 1) {
                     this.eofStr.append(this.line.toString().substring(i + 1));
                     if (i < 0) {
                        this.line.setLength(0);
                        this.reachedEof = true;
                     } else {
                        this.line.setLength(i + 1);
                     }
                  }
               }

            }
         } catch (IOException var4) {
            throw new BuildException(this.srcFile + ": " + var4.getMessage(), var4, FixCRLF.this.getLocation());
         }
      }

      public String getEofStr() {
         return this.eofStr.substring(0);
      }

      public int getState() {
         return this.state;
      }

      public void setState(int state) {
         this.state = state;
      }

      public boolean hasMoreElements() {
         return !this.reachedEof;
      }

      public Object nextElement() throws NoSuchElementException {
         if (!this.hasMoreElements()) {
            throw new NoSuchElementException("OneLiner");
         } else {
            FixCRLF.OneLiner.BufferLine tmpLine = new FixCRLF.OneLiner.BufferLine(this.line.toString(), this.eolStr.substring(0));
            this.nextLine();
            return tmpLine;
         }
      }

      public void close() throws IOException {
         if (this.reader != null) {
            this.reader.close();
         }

      }

      class BufferLine {
         private int next = 0;
         private int column = 0;
         private int lookahead = -1;
         private String line;
         private String eolStr;

         public BufferLine(String line, String eolStr) throws BuildException {
            this.next = 0;
            this.column = 0;
            this.line = line;
            this.eolStr = eolStr;
         }

         public int getNext() {
            return this.next;
         }

         public void setNext(int next) {
            this.next = next;
         }

         public int getLookahead() {
            return this.lookahead;
         }

         public void setLookahead(int lookahead) {
            this.lookahead = lookahead;
         }

         public char getChar(int i) {
            return this.line.charAt(i);
         }

         public char getNextChar() {
            return this.getChar(this.next);
         }

         public char getNextCharInc() {
            return this.getChar(this.next++);
         }

         public int getColumn() {
            return this.column;
         }

         public void setColumn(int col) {
            this.column = col;
         }

         public int incColumn() {
            return this.column++;
         }

         public int length() {
            return this.line.length();
         }

         public int getEolLength() {
            return this.eolStr.length();
         }

         public String getLineString() {
            return this.line;
         }

         public String getEol() {
            return this.eolStr;
         }

         public String substring(int begin) {
            return this.line.substring(begin);
         }

         public String substring(int begin, int end) {
            return this.line.substring(begin, end);
         }

         public void setState(int state) {
            OneLiner.this.setState(state);
         }

         public int getState() {
            return OneLiner.this.getState();
         }
      }
   }
}
