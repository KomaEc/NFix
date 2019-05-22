package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.DOMElementWriter;
import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.util.XMLFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class EchoXML extends XMLFragment {
   private File file;
   private boolean append;
   private static final String ERROR_NO_XML = "No nested XML specified";

   public void setFile(File f) {
      this.file = f;
   }

   public void setAppend(boolean b) {
      this.append = b;
   }

   public void execute() {
      DOMElementWriter writer = new DOMElementWriter(!this.append);
      Object os = null;

      try {
         if (this.file != null) {
            os = new FileOutputStream(this.file.getAbsolutePath(), this.append);
         } else {
            os = new LogOutputStream(this, 2);
         }

         Node n = this.getFragment().getFirstChild();
         if (n == null) {
            throw new BuildException("No nested XML specified");
         }

         writer.write((Element)n, (OutputStream)os);
      } catch (BuildException var8) {
         throw var8;
      } catch (Exception var9) {
         throw new BuildException(var9);
      } finally {
         FileUtils.close((OutputStream)os);
      }

   }
}
