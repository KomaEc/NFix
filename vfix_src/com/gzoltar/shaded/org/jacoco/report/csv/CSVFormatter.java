package com.gzoltar.shaded.org.jacoco.report.csv;

import com.gzoltar.shaded.org.jacoco.core.data.ExecutionData;
import com.gzoltar.shaded.org.jacoco.core.data.SessionInfo;
import com.gzoltar.shaded.org.jacoco.report.ILanguageNames;
import com.gzoltar.shaded.org.jacoco.report.IReportVisitor;
import com.gzoltar.shaded.org.jacoco.report.JavaNames;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.List;

public class CSVFormatter {
   private ILanguageNames languageNames = new JavaNames();
   private String outputEncoding = "UTF-8";

   public void setLanguageNames(ILanguageNames languageNames) {
      this.languageNames = languageNames;
   }

   public ILanguageNames getLanguageNames() {
      return this.languageNames;
   }

   public void setOutputEncoding(String outputEncoding) {
      this.outputEncoding = outputEncoding;
   }

   public IReportVisitor createVisitor(OutputStream output) throws IOException {
      final DelimitedWriter writer = new DelimitedWriter(new OutputStreamWriter(output, this.outputEncoding));
      final ClassRowWriter rowWriter = new ClassRowWriter(writer, this.languageNames);

      class Visitor extends CSVGroupHandler implements IReportVisitor {
         Visitor() {
            super(rowWriter);
         }

         public void visitInfo(List<SessionInfo> sessionInfos, Collection<ExecutionData> executionData) throws IOException {
         }

         public void visitEnd() throws IOException {
            writer.close();
         }
      }

      return new Visitor();
   }
}
