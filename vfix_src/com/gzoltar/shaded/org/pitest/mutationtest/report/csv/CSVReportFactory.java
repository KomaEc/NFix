package com.gzoltar.shaded.org.pitest.mutationtest.report.csv;

import com.gzoltar.shaded.org.pitest.mutationtest.ListenerArguments;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListenerFactory;
import java.util.Properties;

public class CSVReportFactory implements MutationResultListenerFactory {
   public MutationResultListener getListener(Properties props, ListenerArguments args) {
      return new CSVReportListener(args.getOutputStrategy());
   }

   public String name() {
      return "CSV";
   }

   public String description() {
      return "Default csv report plugin";
   }
}
