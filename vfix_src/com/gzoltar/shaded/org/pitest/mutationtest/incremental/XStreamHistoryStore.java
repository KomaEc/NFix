package com.gzoltar.shaded.org.pitest.mutationtest.incremental;

import com.gzoltar.shaded.org.pitest.classinfo.ClassIdentifier;
import com.gzoltar.shaded.org.pitest.classinfo.ClassName;
import com.gzoltar.shaded.org.pitest.classinfo.HierarchicalClassId;
import com.gzoltar.shaded.org.pitest.coverage.CoverageDatabase;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.ClassHistory;
import com.gzoltar.shaded.org.pitest.mutationtest.DetectionStatus;
import com.gzoltar.shaded.org.pitest.mutationtest.HistoryStore;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationStatusTestPair;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationIdentifier;
import com.gzoltar.shaded.org.pitest.reloc.xstream.XStream;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.StreamException;
import com.gzoltar.shaded.org.pitest.reloc.xstream.io.xml.CompactWriter;
import com.gzoltar.shaded.org.pitest.util.Log;
import com.gzoltar.shaded.org.pitest.util.PitXmlDriver;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public class XStreamHistoryStore implements HistoryStore {
   private static final Logger LOG = Log.getLogger();
   private static final XStream XSTREAM_INSTANCE = configureXStream();
   private final WriterFactory outputFactory;
   private final BufferedReader input;
   private final Map<MutationIdentifier, MutationStatusTestPair> previousResults = new HashMap();
   private final Map<ClassName, ClassHistory> previousClassPath = new HashMap();

   public XStreamHistoryStore(WriterFactory output, Option<Reader> input) {
      this.outputFactory = output;
      this.input = this.createReader(input);
   }

   private static XStream configureXStream() {
      XStream xstream = new XStream(new PitXmlDriver());
      xstream.alias("classHistory", ClassHistory.class);
      xstream.alias("fullClassId", HierarchicalClassId.class);
      xstream.alias("classId", ClassIdentifier.class);
      xstream.alias("name", ClassName.class);
      xstream.alias("result", XStreamHistoryStore.IdResult.class);
      xstream.alias("statusTestPair", MutationStatusTestPair.class);
      xstream.alias("status", DetectionStatus.class);
      xstream.useAttributeFor(MutationStatusTestPair.class, "numberOfTestsRun");
      xstream.useAttributeFor(MutationStatusTestPair.class, "status");
      xstream.useAttributeFor(MutationStatusTestPair.class, "killingTest");
      xstream.useAttributeFor(ClassIdentifier.class, "name");
      xstream.useAttributeFor(ClassIdentifier.class, "hash");
      xstream.useAttributeFor(HierarchicalClassId.class, "hierarchicalHash");
      xstream.useAttributeFor(HierarchicalClassId.class, "classId");
      return xstream;
   }

   private BufferedReader createReader(Option<Reader> input) {
      return input.hasSome() ? new BufferedReader((Reader)input.value()) : null;
   }

   public void recordClassPath(Collection<HierarchicalClassId> ids, CoverageDatabase coverageInfo) {
      PrintWriter output = this.outputFactory.create();
      output.println(ids.size());
      Iterator i$ = ids.iterator();

      while(i$.hasNext()) {
         HierarchicalClassId each = (HierarchicalClassId)i$.next();
         ClassHistory coverage = new ClassHistory(each, coverageInfo.getCoverageIdForClass(each.getName()).toString(16));
         output.println(toXml(coverage));
      }

      output.flush();
   }

   public void recordResult(MutationResult result) {
      PrintWriter output = this.outputFactory.create();
      output.println(toXml(new XStreamHistoryStore.IdResult(result.getDetails().getId(), result.getStatusTestPair())));
      output.flush();
   }

   public Map<MutationIdentifier, MutationStatusTestPair> getHistoricResults() {
      return this.previousResults;
   }

   public Map<ClassName, ClassHistory> getHistoricClassPath() {
      return this.previousClassPath;
   }

   public void initialize() {
      if (this.input != null) {
         this.restoreClassPath();
         this.restoreResults();

         try {
            this.input.close();
         } catch (IOException var2) {
            throw Unchecked.translateCheckedException(var2);
         }
      }

   }

   private void restoreResults() {
      try {
         for(String line = this.input.readLine(); line != null; line = this.input.readLine()) {
            XStreamHistoryStore.IdResult result = (XStreamHistoryStore.IdResult)fromXml(line);
            this.previousResults.put(result.id, result.status);
         }
      } catch (IOException var3) {
         LOG.warning("Could not read previous results");
      } catch (StreamException var4) {
         LOG.warning("Could not read previous results");
      }

   }

   private void restoreClassPath() {
      try {
         long classPathSize = Long.valueOf(this.input.readLine());

         for(int i = 0; (long)i != classPathSize; ++i) {
            ClassHistory coverage = (ClassHistory)fromXml(this.input.readLine());
            this.previousClassPath.put(coverage.getName(), coverage);
         }
      } catch (IOException var6) {
         LOG.warning("Could not read previous classpath");
      } catch (StreamException var7) {
         LOG.warning("Could not read previous classpath");
      }

   }

   private static Object fromXml(String xml) {
      return XSTREAM_INSTANCE.fromXML(xml);
   }

   private static String toXml(Object o) {
      Writer writer = new StringWriter();
      XSTREAM_INSTANCE.marshal(o, new CompactWriter(writer));
      return writer.toString().replaceAll("\n", "");
   }

   private static class IdResult {
      final MutationIdentifier id;
      final MutationStatusTestPair status;

      IdResult(MutationIdentifier id, MutationStatusTestPair status) {
         this.id = id;
         this.status = status;
      }
   }
}
