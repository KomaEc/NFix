package com.gzoltar.shaded.org.jacoco.report.internal;

import com.gzoltar.shaded.org.jacoco.report.IMultiReportOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class ReportOutputFolder {
   private final IMultiReportOutput output;
   private final ReportOutputFolder parent;
   private final String path;
   private final Map<String, ReportOutputFolder> subFolders;
   private final NormalizedFileNames fileNames;

   public ReportOutputFolder(IMultiReportOutput output) {
      this(output, (ReportOutputFolder)null, "");
   }

   private ReportOutputFolder(IMultiReportOutput output, ReportOutputFolder parent, String path) {
      this.subFolders = new HashMap();
      this.output = output;
      this.parent = parent;
      this.path = path;
      this.fileNames = new NormalizedFileNames();
   }

   public ReportOutputFolder subFolder(String name) {
      String normalizedName = this.normalize(name);
      ReportOutputFolder folder = (ReportOutputFolder)this.subFolders.get(normalizedName);
      if (folder != null) {
         return folder;
      } else {
         folder = new ReportOutputFolder(this.output, this, this.path + normalizedName + "/");
         this.subFolders.put(normalizedName, folder);
         return folder;
      }
   }

   public OutputStream createFile(String name) throws IOException {
      return this.output.createFile(this.path + this.normalize(name));
   }

   public String getLink(ReportOutputFolder base, String name) {
      if (base.isAncestorOf(this)) {
         return this.path.substring(base.path.length()) + this.normalize(name);
      } else if (base.parent == null) {
         throw new IllegalArgumentException("Folders with different roots.");
      } else {
         return "../" + this.getLink(base.parent, name);
      }
   }

   private boolean isAncestorOf(ReportOutputFolder folder) {
      if (this == folder) {
         return true;
      } else {
         return folder.parent == null ? false : this.isAncestorOf(folder.parent);
      }
   }

   private String normalize(String name) {
      return this.fileNames.getFileName(name);
   }
}
