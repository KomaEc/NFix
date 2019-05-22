package com.gzoltar.shaded.org.pitest.mutationtest.report.html;

import com.gzoltar.shaded.org.pitest.mutationtest.ListenerArguments;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListenerFactory;
import com.gzoltar.shaded.org.pitest.mutationtest.SourceLocator;
import java.util.Properties;

public class HtmlReportFactory implements MutationResultListenerFactory {
   public MutationResultListener getListener(Properties props, ListenerArguments args) {
      return new MutationHtmlReportListener(args.getCoverage(), args.getOutputStrategy(), args.getEngine().getMutatorNames(), new SourceLocator[]{args.getLocator()});
   }

   public String name() {
      return "HTML";
   }

   public String description() {
      return "Default html report plugin";
   }
}
