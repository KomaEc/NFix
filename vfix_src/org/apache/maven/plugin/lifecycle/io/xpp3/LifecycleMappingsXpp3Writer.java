package org.apache.maven.plugin.lifecycle.io.xpp3;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import org.apache.maven.plugin.lifecycle.Execution;
import org.apache.maven.plugin.lifecycle.Lifecycle;
import org.apache.maven.plugin.lifecycle.LifecycleConfiguration;
import org.apache.maven.plugin.lifecycle.Phase;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.pull.MXSerializer;
import org.codehaus.plexus.util.xml.pull.XmlSerializer;

public class LifecycleMappingsXpp3Writer {
   private XmlSerializer serializer;
   private String NAMESPACE;

   public void write(Writer writer, LifecycleConfiguration lifecycles) throws IOException {
      this.serializer = new MXSerializer();
      this.serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-indentation", "  ");
      this.serializer.setProperty("http://xmlpull.org/v1/doc/properties.html#serializer-line-separator", "\n");
      this.serializer.setOutput(writer);
      this.serializer.startDocument(lifecycles.getModelEncoding(), (Boolean)null);
      this.writeLifecycleConfiguration(lifecycles, "lifecycles", this.serializer);
      this.serializer.endDocument();
   }

   private void writeExecution(Execution execution, String tagName, XmlSerializer serializer) throws IOException {
      if (execution != null) {
         serializer.startTag(this.NAMESPACE, tagName);
         if (execution.getConfiguration() != null) {
            ((Xpp3Dom)execution.getConfiguration()).writeToSerializer(this.NAMESPACE, serializer);
         }

         if (execution.getGoals() != null && execution.getGoals().size() > 0) {
            serializer.startTag(this.NAMESPACE, "goals");
            Iterator iter = execution.getGoals().iterator();

            while(iter.hasNext()) {
               String goal = (String)iter.next();
               serializer.startTag(this.NAMESPACE, "goal").text(goal).endTag(this.NAMESPACE, "goal");
            }

            serializer.endTag(this.NAMESPACE, "goals");
         }

         serializer.endTag(this.NAMESPACE, tagName);
      }

   }

   private void writeLifecycle(Lifecycle lifecycle, String tagName, XmlSerializer serializer) throws IOException {
      if (lifecycle != null) {
         serializer.startTag(this.NAMESPACE, tagName);
         if (lifecycle.getId() != null) {
            serializer.startTag(this.NAMESPACE, "id").text(lifecycle.getId()).endTag(this.NAMESPACE, "id");
         }

         if (lifecycle.getPhases() != null && lifecycle.getPhases().size() > 0) {
            serializer.startTag(this.NAMESPACE, "phases");
            Iterator iter = lifecycle.getPhases().iterator();

            while(iter.hasNext()) {
               Phase o = (Phase)iter.next();
               this.writePhase(o, "phase", serializer);
            }

            serializer.endTag(this.NAMESPACE, "phases");
         }

         serializer.endTag(this.NAMESPACE, tagName);
      }

   }

   private void writeLifecycleConfiguration(LifecycleConfiguration lifecycleConfiguration, String tagName, XmlSerializer serializer) throws IOException {
      if (lifecycleConfiguration != null) {
         serializer.startTag(this.NAMESPACE, tagName);
         if (lifecycleConfiguration.getLifecycles() != null && lifecycleConfiguration.getLifecycles().size() > 0) {
            Iterator iter = lifecycleConfiguration.getLifecycles().iterator();

            while(iter.hasNext()) {
               Lifecycle o = (Lifecycle)iter.next();
               this.writeLifecycle(o, "lifecycle", serializer);
            }
         }

         serializer.endTag(this.NAMESPACE, tagName);
      }

   }

   private void writePhase(Phase phase, String tagName, XmlSerializer serializer) throws IOException {
      if (phase != null) {
         serializer.startTag(this.NAMESPACE, tagName);
         if (phase.getId() != null) {
            serializer.startTag(this.NAMESPACE, "id").text(phase.getId()).endTag(this.NAMESPACE, "id");
         }

         if (phase.getExecutions() != null && phase.getExecutions().size() > 0) {
            serializer.startTag(this.NAMESPACE, "executions");
            Iterator iter = phase.getExecutions().iterator();

            while(iter.hasNext()) {
               Execution o = (Execution)iter.next();
               this.writeExecution(o, "execution", serializer);
            }

            serializer.endTag(this.NAMESPACE, "executions");
         }

         if (phase.getConfiguration() != null) {
            ((Xpp3Dom)phase.getConfiguration()).writeToSerializer(this.NAMESPACE, serializer);
         }

         serializer.endTag(this.NAMESPACE, tagName);
      }

   }
}
