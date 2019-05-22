package com.gzoltar.shaded.org.pitest.mutationtest.report.csv;

import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.ClassMutationResults;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import com.gzoltar.shaded.org.pitest.util.ResultOutputStrategy;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class CSVReportListener implements MutationResultListener {
   private final Writer out;

   public CSVReportListener(ResultOutputStrategy outputStrategy) {
      this(outputStrategy.createWriterForFile("mutations.csv"));
   }

   public CSVReportListener(Writer out) {
      this.out = out;
   }

   private String createKillingTestDesc(Option<String> killingTest) {
      return killingTest.hasSome() ? (String)killingTest.value() : "none";
   }

   private String makeCsv(Object... os) {
      StringBuilder sb = new StringBuilder();

      for(int i = 0; i != os.length; ++i) {
         sb.append(os[i].toString());
         if (i != os.length - 1) {
            sb.append(",");
         }
      }

      return sb.toString();
   }

   public void runStart() {
   }

   public void runEnd() {
      try {
         this.out.close();
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2);
      }
   }

   public void handleMutationResult(ClassMutationResults metaData) {
      try {
         Iterator i$ = metaData.getMutations().iterator();

         while(i$.hasNext()) {
            MutationResult mutation = (MutationResult)i$.next();
            this.out.write(this.makeCsv(mutation.getDetails().getFilename(), mutation.getDetails().getClassName().asJavaName(), mutation.getDetails().getMutator(), mutation.getDetails().getMethod(), mutation.getDetails().getLineNumber(), mutation.getStatus(), this.createKillingTestDesc(mutation.getKillingTest())) + System.getProperty("line.separator"));
         }

      } catch (IOException var4) {
         throw Unchecked.translateCheckedException(var4);
      }
   }
}
