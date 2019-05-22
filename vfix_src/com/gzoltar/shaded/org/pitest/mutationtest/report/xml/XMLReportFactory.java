package com.gzoltar.shaded.org.pitest.mutationtest.report.xml;

import com.gzoltar.shaded.org.pitest.mutationtest.ListenerArguments;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListenerFactory;
import java.util.Properties;

public class XMLReportFactory implements MutationResultListenerFactory {
   public MutationResultListener getListener(Properties props, ListenerArguments args) {
      return new XMLReportListener(args.getOutputStrategy());
   }

   public String name() {
      return "XML";
   }

   public String description() {
      return "Default xml report plugin";
   }
}
