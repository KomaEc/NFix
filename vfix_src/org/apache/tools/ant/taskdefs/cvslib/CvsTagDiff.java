package org.apache.tools.ant.taskdefs.cvslib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.AbstractCvsTask;
import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.DOMUtils;
import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CvsTagDiff extends AbstractCvsTask {
   private static final FileUtils FILE_UTILS = FileUtils.getFileUtils();
   private static final DOMElementWriter DOM_WRITER = new DOMElementWriter();
   static final String FILE_STRING = "File ";
   static final String TO_STRING = " to ";
   static final String FILE_IS_NEW = " is new;";
   static final String REVISION = "revision ";
   static final String FILE_HAS_CHANGED = " changed from revision ";
   static final String FILE_WAS_REMOVED = " is removed";
   private String mypackage;
   private String mystartTag;
   private String myendTag;
   private String mystartDate;
   private String myendDate;
   private File mydestfile;

   public void setPackage(String p) {
      this.mypackage = p;
   }

   public void setStartTag(String s) {
      this.mystartTag = s;
   }

   public void setStartDate(String s) {
      this.mystartDate = s;
   }

   public void setEndTag(String s) {
      this.myendTag = s;
   }

   public void setEndDate(String s) {
      this.myendDate = s;
   }

   public void setDestFile(File f) {
      this.mydestfile = f;
   }

   public void execute() throws BuildException {
      this.validate();
      this.addCommandArgument("rdiff");
      this.addCommandArgument("-s");
      if (this.mystartTag != null) {
         this.addCommandArgument("-r");
         this.addCommandArgument(this.mystartTag);
      } else {
         this.addCommandArgument("-D");
         this.addCommandArgument(this.mystartDate);
      }

      if (this.myendTag != null) {
         this.addCommandArgument("-r");
         this.addCommandArgument(this.myendTag);
      } else {
         this.addCommandArgument("-D");
         this.addCommandArgument(this.myendDate);
      }

      StringTokenizer myTokenizer = new StringTokenizer(this.mypackage);

      while(myTokenizer.hasMoreTokens()) {
         this.addCommandArgument(myTokenizer.nextToken());
      }

      this.setCommand("");
      File tmpFile = null;

      try {
         tmpFile = FILE_UTILS.createTempFile("cvstagdiff", ".log", (File)null);
         tmpFile.deleteOnExit();
         this.setOutput(tmpFile);
         super.execute();
         CvsTagEntry[] entries = this.parseRDiff(tmpFile);
         this.writeTagDiff(entries);
      } finally {
         if (tmpFile != null) {
            tmpFile.delete();
         }

      }

   }

   private CvsTagEntry[] parseRDiff(File tmpFile) throws BuildException {
      BufferedReader reader = null;

      try {
         reader = new BufferedReader(new FileReader(tmpFile));
         String toBeRemoved = "File " + this.mypackage + "/";
         int headerLength = toBeRemoved.length();
         Vector entries = new Vector();
         String line = reader.readLine();

         for(CvsTagEntry entry = null; null != line; line = reader.readLine()) {
            if (line.length() > headerLength) {
               if (line.startsWith(toBeRemoved)) {
                  line = line.substring(headerLength);
               } else {
                  line = line.substring("File ".length());
               }

               int index;
               String filename;
               String rev;
               boolean indexrev;
               int indexrev;
               if ((index = line.indexOf(" is new;")) != -1) {
                  filename = line.substring(0, index);
                  rev = null;
                  indexrev = true;
                  if ((indexrev = line.indexOf("revision ", index)) != -1) {
                     rev = line.substring(indexrev + "revision ".length());
                  }

                  entry = new CvsTagEntry(filename, rev);
                  entries.addElement(entry);
                  this.log(entry.toString(), 3);
               } else if ((index = line.indexOf(" changed from revision ")) != -1) {
                  filename = line.substring(0, index);
                  int revSeparator = line.indexOf(" to ", index);
                  String prevRevision = line.substring(index + " changed from revision ".length(), revSeparator);
                  String revision = line.substring(revSeparator + " to ".length());
                  entry = new CvsTagEntry(filename, revision, prevRevision);
                  entries.addElement(entry);
                  this.log(entry.toString(), 3);
               } else if ((index = line.indexOf(" is removed")) != -1) {
                  filename = line.substring(0, index);
                  rev = null;
                  indexrev = true;
                  if ((indexrev = line.indexOf("revision ", index)) != -1) {
                     rev = line.substring(indexrev + "revision ".length());
                  }

                  entry = new CvsTagEntry(filename, (String)null, rev);
                  entries.addElement(entry);
                  this.log(entry.toString(), 3);
               }
            }
         }

         CvsTagEntry[] array = new CvsTagEntry[entries.size()];
         entries.copyInto(array);
         CvsTagEntry[] var26 = array;
         return var26;
      } catch (IOException var20) {
         throw new BuildException("Error in parsing", var20);
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException var19) {
               this.log(var19.toString(), 0);
            }
         }

      }
   }

   private void writeTagDiff(CvsTagEntry[] entries) throws BuildException {
      FileOutputStream output = null;

      try {
         output = new FileOutputStream(this.mydestfile);
         PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "UTF-8"));
         writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
         Document doc = DOMUtils.newDocument();
         Element root = doc.createElement("tagdiff");
         if (this.mystartTag != null) {
            root.setAttribute("startTag", this.mystartTag);
         } else {
            root.setAttribute("startDate", this.mystartDate);
         }

         if (this.myendTag != null) {
            root.setAttribute("endTag", this.myendTag);
         } else {
            root.setAttribute("endDate", this.myendDate);
         }

         root.setAttribute("cvsroot", this.getCvsRoot());
         root.setAttribute("package", this.mypackage);
         DOM_WRITER.openElement(root, writer, 0, "\t");
         writer.println();
         int i = 0;

         for(int c = entries.length; i < c; ++i) {
            this.writeTagEntry(doc, writer, entries[i]);
         }

         DOM_WRITER.closeElement(root, writer, 0, "\t", true);
         writer.flush();
         writer.close();
      } catch (UnsupportedEncodingException var17) {
         this.log(var17.toString(), 0);
      } catch (IOException var18) {
         throw new BuildException(var18.toString(), var18);
      } finally {
         if (null != output) {
            try {
               output.close();
            } catch (IOException var16) {
               this.log(var16.toString(), 0);
            }
         }

      }

   }

   private void writeTagEntry(Document doc, PrintWriter writer, CvsTagEntry entry) throws IOException {
      Element ent = doc.createElement("entry");
      Element f = DOMUtils.createChildElement(ent, "file");
      DOMUtils.appendCDATAElement(f, "name", entry.getFile());
      if (entry.getRevision() != null) {
         DOMUtils.appendTextElement(f, "revision", entry.getRevision());
      }

      if (entry.getPreviousRevision() != null) {
         DOMUtils.appendTextElement(f, "prevrevision", entry.getPreviousRevision());
      }

      DOM_WRITER.write(ent, writer, 1, "\t");
   }

   private void validate() throws BuildException {
      if (null == this.mypackage) {
         throw new BuildException("Package/module must be set.");
      } else if (null == this.mydestfile) {
         throw new BuildException("Destfile must be set.");
      } else if (null == this.mystartTag && null == this.mystartDate) {
         throw new BuildException("Start tag or start date must be set.");
      } else if (null != this.mystartTag && null != this.mystartDate) {
         throw new BuildException("Only one of start tag and start date must be set.");
      } else if (null == this.myendTag && null == this.myendDate) {
         throw new BuildException("End tag or end date must be set.");
      } else if (null != this.myendTag && null != this.myendDate) {
         throw new BuildException("Only one of end tag and end date must be set.");
      }
   }
}
