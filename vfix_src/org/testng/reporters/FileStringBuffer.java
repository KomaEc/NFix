package org.testng.reporters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.Random;

public class FileStringBuffer implements IBuffer {
   private static int MAX = 100000;
   private static final boolean VERBOSE = System.getProperty("fileStringBuffer") != null;
   private File m_file;
   private StringBuilder m_sb;
   private final int m_maxCharacters;

   public FileStringBuffer() {
      this(MAX);
   }

   public FileStringBuffer(int maxCharacters) {
      this.m_sb = new StringBuilder();
      this.m_maxCharacters = maxCharacters;
   }

   public FileStringBuffer append(CharSequence s) {
      if (s == null) {
         throw new IllegalArgumentException("CharSequence (Argument 0 of FileStringBuffer#append) should not be null");
      } else {
         if (this.m_sb.length() > this.m_maxCharacters) {
            this.flushToFile();
         }

         if (s.length() < MAX) {
            this.m_sb.append(s);
         } else {
            this.flushToFile();

            try {
               copy(new StringReader(s.toString()), new FileWriter(this.m_file, true));
            } catch (IOException var3) {
               var3.printStackTrace();
            }
         }

         return this;
      }
   }

   public void toWriter(Writer fw) {
      if (fw == null) {
         throw new IllegalArgumentException("Writer (Argument 0 of FileStringBuffer#toWriter) should not be null");
      } else {
         try {
            BufferedWriter bw = new BufferedWriter(fw);
            if (this.m_file == null) {
               bw.write(this.m_sb.toString());
               bw.close();
            } else {
               this.flushToFile();
               copy(new FileReader(this.m_file), bw);
            }
         } catch (IOException var3) {
            var3.printStackTrace();
         }

      }
   }

   private static void copy(Reader input, Writer output) throws IOException {
      char[] buf = new char[MAX];

      while(true) {
         int length = input.read(buf);
         if (length < 0) {
            try {
               input.close();
            } catch (IOException var5) {
            }

            try {
               output.close();
            } catch (IOException var4) {
            }

            return;
         }

         output.write(buf, 0, length);
      }
   }

   private void flushToFile() {
      if (this.m_sb.length() != 0) {
         if (this.m_file == null) {
            try {
               this.m_file = File.createTempFile("testng", "fileStringBuffer");
               this.m_file.deleteOnExit();
               p("Created temp file " + this.m_file);
            } catch (IOException var4) {
               var4.printStackTrace();
            }
         }

         try {
            p("Size " + this.m_sb.length() + ", flushing to " + this.m_file);
            FileWriter fw = new FileWriter(this.m_file, true);
            fw.append(this.m_sb);
            fw.close();
         } catch (IOException var3) {
            var3.printStackTrace();
         }

         this.m_sb = new StringBuilder();
      }
   }

   private static void p(String s) {
      if (VERBOSE) {
         System.out.println("[FileStringBuffer] " + s);
      }

   }

   public String toString() {
      String result = null;
      if (this.m_file != null) {
         this.flushToFile();

         try {
            result = Files.readFile(this.m_file);
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      } else {
         result = this.m_sb.toString();
      }

      return result;
   }

   private static void save(File expected, String s) throws IOException {
      expected.delete();
      FileWriter expectedWriter = new FileWriter(expected);
      expectedWriter.append(s);
      expectedWriter.close();
   }

   public static void main(String[] args) throws IOException {
      String s = "abcdefghijklmnopqrstuvwxyz";
      FileStringBuffer fsb = new FileStringBuffer(10);
      StringBuilder control = new StringBuilder();
      Random r = new Random();

      for(int i = 0; i < 1000; ++i) {
         int start = Math.abs(r.nextInt() % 26);
         int length = Math.abs(r.nextInt() % (26 - start));
         String fragment = s.substring(start, start + length);
         p("... Appending " + fragment);
         fsb.append(fragment);
         control.append(fragment);
      }

      File expected = new File("/tmp/expected");
      expected.delete();
      FileWriter expectedWriter = new FileWriter(expected);
      expectedWriter.append(control);
      expectedWriter.close();
      File actual = new File("/tmp/actual");
      actual.delete();
      FileWriter actualWriter = new FileWriter(actual);
      fsb.toWriter(actualWriter);
      actualWriter.close();
   }
}
