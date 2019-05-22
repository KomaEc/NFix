package com.gzoltar.shaded.org.jacoco.report.internal.html.resources;

import com.gzoltar.shaded.org.jacoco.core.analysis.ICoverageNode;
import com.gzoltar.shaded.org.jacoco.report.internal.ReportOutputFolder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Resources {
   public static final String STYLESHEET = "report.css";
   public static final String PRETTIFY_STYLESHEET = "prettify.css";
   public static final String PRETTIFY_SCRIPT = "prettify.js";
   public static final String SORT_SCRIPT = "sort.js";
   public static final String REDBAR = "redbar.gif";
   public static final String GREENBAR = "greenbar.gif";
   private final ReportOutputFolder folder;

   public Resources(ReportOutputFolder root) {
      this.folder = root.subFolder("jacoco-resources");
   }

   public String getLink(ReportOutputFolder base, String name) {
      return this.folder.getLink(base, name);
   }

   public static String getElementStyle(ICoverageNode.ElementType type) {
      switch(type) {
      case GROUP:
         return "el_group";
      case BUNDLE:
         return "el_bundle";
      case PACKAGE:
         return "el_package";
      case SOURCEFILE:
         return "el_source";
      case CLASS:
         return "el_class";
      case METHOD:
         return "el_method";
      default:
         throw new AssertionError("Unknown element type: " + type);
      }
   }

   public void copyResources() throws IOException {
      this.copyResource("report.css");
      this.copyResource("report.gif");
      this.copyResource("group.gif");
      this.copyResource("bundle.gif");
      this.copyResource("package.gif");
      this.copyResource("source.gif");
      this.copyResource("class.gif");
      this.copyResource("method.gif");
      this.copyResource("session.gif");
      this.copyResource("sort.gif");
      this.copyResource("up.gif");
      this.copyResource("down.gif");
      this.copyResource("branchfc.gif");
      this.copyResource("branchnc.gif");
      this.copyResource("branchpc.gif");
      this.copyResource("redbar.gif");
      this.copyResource("greenbar.gif");
      this.copyResource("prettify.css");
      this.copyResource("prettify.js");
      this.copyResource("sort.js");
   }

   private void copyResource(String name) throws IOException {
      InputStream in = Resources.class.getResourceAsStream(name);
      OutputStream out = this.folder.createFile(name);
      byte[] buffer = new byte[256];

      int len;
      while((len = in.read(buffer)) != -1) {
         out.write(buffer, 0, len);
      }

      in.close();
      out.close();
   }
}
