package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import java.util.List;

public class SourceFile {
   private final String fileName;
   private final List<Line> lines;
   private final List<MutationGrouping> groups;

   public SourceFile(String fileName, List<Line> lines, List<MutationGrouping> groups) {
      this.fileName = fileName;
      this.lines = lines;
      this.groups = groups;
   }

   public String getFileName() {
      return this.fileName;
   }

   public List<Line> getLines() {
      return this.lines;
   }

   public List<MutationGrouping> getGroups() {
      return this.groups;
   }
}
