package com.gzoltar.shaded.org.pitest.coverage.export;

import com.gzoltar.shaded.org.pitest.coverage.BlockCoverage;
import com.gzoltar.shaded.org.pitest.coverage.CoverageExporter;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.Location;
import com.gzoltar.shaded.org.pitest.util.ResultOutputStrategy;
import com.gzoltar.shaded.org.pitest.util.StringUtil;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DefaultCoverageExporter implements CoverageExporter {
   private final ResultOutputStrategy outputStrategy;

   public DefaultCoverageExporter(ResultOutputStrategy outputStrategy) {
      this.outputStrategy = outputStrategy;
   }

   public void recordCoverage(Collection<BlockCoverage> coverage) {
      Writer out = this.outputStrategy.createWriterForFile("linecoverage.xml");
      this.writeHeader(out);
      Iterator i$ = coverage.iterator();

      while(i$.hasNext()) {
         BlockCoverage each = (BlockCoverage)i$.next();
         this.writeLineCoverage(each, out);
      }

      this.writeFooterAndClose(out);
   }

   private void writeHeader(Writer out) {
      this.write(out, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      this.write(out, "<coverage>\n");
   }

   private void writeLineCoverage(BlockCoverage each, Writer out) {
      Location l = each.getBlock().getLocation();
      this.write(out, "<block classname='" + l.getClassName().asJavaName() + "'" + " method='" + StringUtil.escapeBasicHtmlChars(l.getMethodName().name()) + "' number='" + each.getBlock().getBlock() + "'>");
      this.write(out, "<tests>\n");
      List<String> ts = new ArrayList(each.getTests());
      Collections.sort(ts);
      Iterator i$ = ts.iterator();

      while(i$.hasNext()) {
         String test = (String)i$.next();
         this.write(out, "<test name='" + test + "'/>\n");
      }

      this.write(out, "</tests>\n");
      this.write(out, "</block>\n");
   }

   private void writeFooterAndClose(Writer out) {
      try {
         this.write(out, "</coverage>\n");
         out.close();
      } catch (IOException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   private void write(Writer out, String value) {
      try {
         out.write(value);
      } catch (IOException var4) {
         throw Unchecked.translateCheckedException(var4);
      }
   }
}
