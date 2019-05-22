package org.apache.maven.plugin;

import java.util.ArrayList;
import java.util.List;
import org.apache.maven.plugin.descriptor.MojoDescriptor;
import org.codehaus.plexus.util.xml.Xpp3Dom;

public class MojoExecution {
   private final String executionId;
   private final MojoDescriptor mojoDescriptor;
   private Xpp3Dom configuration;
   private List forkedExecutions = new ArrayList();
   private List reports;

   public MojoExecution(MojoDescriptor mojoDescriptor) {
      this.mojoDescriptor = mojoDescriptor;
      this.executionId = null;
      this.configuration = null;
   }

   public MojoExecution(MojoDescriptor mojoDescriptor, String executionId) {
      this.mojoDescriptor = mojoDescriptor;
      this.executionId = executionId;
      this.configuration = null;
   }

   public MojoExecution(MojoDescriptor mojoDescriptor, Xpp3Dom configuration) {
      this.mojoDescriptor = mojoDescriptor;
      this.configuration = configuration;
      this.executionId = null;
   }

   public String getExecutionId() {
      return this.executionId;
   }

   public MojoDescriptor getMojoDescriptor() {
      return this.mojoDescriptor;
   }

   public Xpp3Dom getConfiguration() {
      return this.configuration;
   }

   public void addMojoExecution(MojoExecution execution) {
      this.forkedExecutions.add(execution);
   }

   public void setReports(List reports) {
      this.reports = reports;
   }

   public List getReports() {
      return this.reports;
   }

   public List getForkedExecutions() {
      return this.forkedExecutions;
   }

   public void setConfiguration(Xpp3Dom configuration) {
      this.configuration = configuration;
   }
}
