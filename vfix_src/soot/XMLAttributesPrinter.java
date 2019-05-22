package soot;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.xml.TagCollector;

public class XMLAttributesPrinter {
   private static final Logger logger = LoggerFactory.getLogger(XMLAttributesPrinter.class);
   private String useFilename;
   private String outputDir;
   FileOutputStream streamOut = null;
   PrintWriter writerOut = null;

   private void setOutputDir(String dir) {
      this.outputDir = dir;
   }

   private String getOutputDir() {
      return this.outputDir;
   }

   public XMLAttributesPrinter(String filename, String outputDir) {
      this.setInFilename(filename);
      this.setOutputDir(outputDir);
      this.initAttributesDir();
      this.createUseFilename();
   }

   private void initFile() {
      try {
         this.streamOut = new FileOutputStream(this.getUseFilename());
         this.writerOut = new PrintWriter(new OutputStreamWriter(this.streamOut));
         this.writerOut.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
         this.writerOut.println("<attributes>");
      } catch (IOException var2) {
         logger.debug("" + var2.getMessage());
      }

   }

   private void finishFile() {
      this.writerOut.println("</attributes>");
      this.writerOut.close();
   }

   public void printAttrs(SootClass c, TagCollector tc) {
      this.printAttrs(c, tc, false);
   }

   public void printAttrs(SootClass c) {
      this.printAttrs(c, new TagCollector(), true);
   }

   private void printAttrs(SootClass c, TagCollector tc, boolean includeBodyTags) {
      tc.collectKeyTags(c);
      tc.collectTags(c, includeBodyTags);
      if (!tc.isEmpty()) {
         this.initFile();
         tc.printTags(this.writerOut);
         tc.printKeys(this.writerOut);
         this.finishFile();
      }
   }

   private void initAttributesDir() {
      StringBuffer sb = new StringBuffer();
      String attrDir = "attributes";
      sb.append(this.getOutputDir());
      sb.append(System.getProperty("file.separator"));
      sb.append(attrDir);
      File dir = new File(sb.toString());
      if (!dir.exists()) {
         try {
            dir.mkdirs();
         } catch (SecurityException var5) {
            logger.debug("Unable to create " + attrDir);
         }
      }

   }

   private void createUseFilename() {
      String tmp = this.getInFilename();
      tmp = tmp.substring(0, tmp.lastIndexOf(46));
      int slash = tmp.lastIndexOf(System.getProperty("file.separator"));
      if (slash != -1) {
         tmp = tmp.substring(slash + 1, tmp.length());
      }

      StringBuffer sb = new StringBuffer();
      String attrDir = "attributes";
      sb.append(this.getOutputDir());
      sb.append(System.getProperty("file.separator"));
      sb.append(attrDir);
      sb.append(System.getProperty("file.separator"));
      sb.append(tmp);
      sb.append(".xml");
      this.setUseFilename(sb.toString());
   }

   private void setInFilename(String file) {
      this.useFilename = file;
   }

   private String getInFilename() {
      return this.useFilename;
   }

   private void setUseFilename(String file) {
      this.useFilename = file;
   }

   private String getUseFilename() {
      return this.useFilename;
   }
}
