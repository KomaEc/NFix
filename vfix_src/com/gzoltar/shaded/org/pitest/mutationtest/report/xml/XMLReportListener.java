package com.gzoltar.shaded.org.pitest.mutationtest.report.xml;

import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.mutationtest.ClassMutationResults;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResult;
import com.gzoltar.shaded.org.pitest.mutationtest.MutationResultListener;
import com.gzoltar.shaded.org.pitest.mutationtest.engine.MutationDetails;
import com.gzoltar.shaded.org.pitest.util.ResultOutputStrategy;
import com.gzoltar.shaded.org.pitest.util.StringUtil;
import com.gzoltar.shaded.org.pitest.util.Unchecked;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class XMLReportListener implements MutationResultListener {
   private final Writer out;

   public XMLReportListener(ResultOutputStrategy outputStrategy) {
      this(outputStrategy.createWriterForFile("mutations.xml"));
   }

   public XMLReportListener(Writer out) {
      this.out = out;
   }

   private void writeResult(ClassMutationResults metaData) {
      Iterator i$ = metaData.getMutations().iterator();

      while(i$.hasNext()) {
         MutationResult mutation = (MutationResult)i$.next();
         this.writeMutationResultXML(mutation);
      }

   }

   private void writeMutationResultXML(MutationResult result) {
      this.write(this.makeNode(this.makeMutationNode(result), this.makeMutationAttributes(result), Tag.mutation) + "\n");
   }

   private String makeMutationAttributes(MutationResult result) {
      return "detected='" + result.getStatus().isDetected() + "' status='" + result.getStatus() + "'";
   }

   private String makeMutationNode(MutationResult mutation) {
      MutationDetails details = mutation.getDetails();
      return this.makeNode(this.clean(details.getFilename()), Tag.sourceFile) + this.makeNode(this.clean(details.getClassName().asJavaName()), Tag.mutatedClass) + this.makeNode(this.clean(details.getMethod().name()), Tag.mutatedMethod) + this.makeNode(this.clean(details.getId().getLocation().getMethodDesc()), Tag.methodDescription) + this.makeNode("" + details.getLineNumber(), Tag.lineNumber) + this.makeNode(this.clean(details.getMutator()), Tag.mutator) + this.makeNode("" + details.getFirstIndex(), Tag.index) + this.makeNode(this.createKillingTestDesc(mutation.getKillingTest()), Tag.killingTest);
   }

   private String clean(String value) {
      return StringUtil.escapeBasicHtmlChars(value);
   }

   private String makeNode(String value, String attributes, Tag tag) {
      return value != null ? "<" + tag + " " + attributes + ">" + value + "</" + tag + ">" : "<" + tag + attributes + "/>";
   }

   private String makeNode(String value, Tag tag) {
      return value != null ? "<" + tag + ">" + value + "</" + tag + ">" : "<" + tag + "/>";
   }

   private String createKillingTestDesc(Option<String> killingTest) {
      return killingTest.hasSome() ? this.clean((String)killingTest.value()) : null;
   }

   private void write(String value) {
      try {
         this.out.write(value);
      } catch (IOException var3) {
         throw Unchecked.translateCheckedException(var3);
      }
   }

   public void runStart() {
      this.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      this.write("<mutations>\n");
   }

   public void handleMutationResult(ClassMutationResults metaData) {
      this.writeResult(metaData);
   }

   public void runEnd() {
      try {
         this.write("</mutations>\n");
         this.out.close();
      } catch (IOException var2) {
         throw Unchecked.translateCheckedException(var2);
      }
   }
}
