package org.apache.maven.plugin.descriptor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.Mojo;
import org.codehaus.plexus.component.repository.ComponentDescriptor;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.xml.XmlPlexusConfiguration;

public class MojoDescriptor extends ComponentDescriptor implements Cloneable {
   public static String MAVEN_PLUGIN = "maven-plugin";
   public static final String SINGLE_PASS_EXEC_STRATEGY = "once-per-session";
   public static final String MULTI_PASS_EXEC_STRATEGY = "always";
   private static final String DEFAULT_INSTANTIATION_STRATEGY = "per-lookup";
   private static final String DEFAULT_LANGUAGE = "java";
   private List parameters;
   private Map parameterMap;
   private String executionStrategy = "once-per-session";
   private String goal;
   private String phase;
   private String since;
   private String executePhase;
   private String executeGoal;
   private String executeLifecycle;
   private String deprecated;
   private boolean aggregator = false;
   private String dependencyResolutionRequired = null;
   private boolean projectRequired = true;
   private boolean onlineRequired = false;
   private PlexusConfiguration mojoConfiguration;
   private PluginDescriptor pluginDescriptor;
   private boolean inheritedByDefault = true;
   private boolean directInvocationOnly = false;
   private boolean requiresReports = false;

   public MojoDescriptor() {
      this.setInstantiationStrategy("per-lookup");
      this.setComponentFactory("java");
   }

   public String getLanguage() {
      return this.getComponentFactory();
   }

   public void setLanguage(String language) {
      this.setComponentFactory(language);
   }

   public String getDeprecated() {
      return this.deprecated;
   }

   public void setDeprecated(String deprecated) {
      this.deprecated = deprecated;
   }

   public List getParameters() {
      return this.parameters;
   }

   public void setParameters(List parameters) throws DuplicateParameterException {
      Iterator it = parameters.iterator();

      while(it.hasNext()) {
         Parameter parameter = (Parameter)it.next();
         this.addParameter(parameter);
      }

   }

   public void addParameter(Parameter parameter) throws DuplicateParameterException {
      if (this.parameters != null && this.parameters.contains(parameter)) {
         throw new DuplicateParameterException(parameter.getName() + " has been declared multiple times in mojo with goal: " + this.getGoal() + " (implementation: " + this.getImplementation() + ")");
      } else {
         if (this.parameters == null) {
            this.parameters = new LinkedList();
         }

         this.parameters.add(parameter);
      }
   }

   public Map getParameterMap() {
      if (this.parameterMap == null) {
         this.parameterMap = new HashMap();
         if (this.parameters != null) {
            Iterator iterator = this.parameters.iterator();

            while(iterator.hasNext()) {
               Parameter pd = (Parameter)iterator.next();
               this.parameterMap.put(pd.getName(), pd);
            }
         }
      }

      return this.parameterMap;
   }

   public void setDependencyResolutionRequired(String requiresDependencyResolution) {
      this.dependencyResolutionRequired = requiresDependencyResolution;
   }

   public String isDependencyResolutionRequired() {
      return this.dependencyResolutionRequired;
   }

   public void setProjectRequired(boolean requiresProject) {
      this.projectRequired = requiresProject;
   }

   public boolean isProjectRequired() {
      return this.projectRequired;
   }

   public void setOnlineRequired(boolean requiresOnline) {
      this.onlineRequired = requiresOnline;
   }

   public boolean isOnlineRequired() {
      return this.onlineRequired;
   }

   public boolean requiresOnline() {
      return this.onlineRequired;
   }

   public String getPhase() {
      return this.phase;
   }

   public void setPhase(String phase) {
      this.phase = phase;
   }

   public String getSince() {
      return this.since;
   }

   public void setSince(String since) {
      this.since = since;
   }

   public String getGoal() {
      return this.goal;
   }

   public void setGoal(String goal) {
      this.goal = goal;
   }

   public String getExecutePhase() {
      return this.executePhase;
   }

   public void setExecutePhase(String executePhase) {
      this.executePhase = executePhase;
   }

   public boolean alwaysExecute() {
      return "always".equals(this.executionStrategy);
   }

   public String getExecutionStrategy() {
      return this.executionStrategy;
   }

   public void setExecutionStrategy(String executionStrategy) {
      this.executionStrategy = executionStrategy;
   }

   public PlexusConfiguration getMojoConfiguration() {
      if (this.mojoConfiguration == null) {
         this.mojoConfiguration = new XmlPlexusConfiguration("configuration");
      }

      return this.mojoConfiguration;
   }

   public void setMojoConfiguration(PlexusConfiguration mojoConfiguration) {
      this.mojoConfiguration = mojoConfiguration;
   }

   public String getRole() {
      return Mojo.ROLE;
   }

   public String getRoleHint() {
      return this.getId();
   }

   public String getId() {
      return this.getPluginDescriptor().getId() + ":" + this.getGoal();
   }

   public String getFullGoalName() {
      return this.getPluginDescriptor().getGoalPrefix() + ":" + this.getGoal();
   }

   public String getComponentType() {
      return MAVEN_PLUGIN;
   }

   public PluginDescriptor getPluginDescriptor() {
      return this.pluginDescriptor;
   }

   public void setPluginDescriptor(PluginDescriptor pluginDescriptor) {
      this.pluginDescriptor = pluginDescriptor;
   }

   public boolean isInheritedByDefault() {
      return this.inheritedByDefault;
   }

   public void setInheritedByDefault(boolean inheritedByDefault) {
      this.inheritedByDefault = inheritedByDefault;
   }

   public boolean equals(Object object) {
      if (this == object) {
         return true;
      } else if (object instanceof MojoDescriptor) {
         MojoDescriptor other = (MojoDescriptor)object;
         if (!this.compareObjects(this.getPluginDescriptor(), other.getPluginDescriptor())) {
            return false;
         } else {
            return this.compareObjects(this.getGoal(), other.getGoal());
         }
      } else {
         return false;
      }
   }

   private boolean compareObjects(Object first, Object second) {
      if ((first != null || second == null) && (first == null || second != null)) {
         return first.equals(second);
      } else {
         return false;
      }
   }

   public int hashCode() {
      int result = 1;
      String goal = this.getGoal();
      if (goal != null) {
         result += goal.hashCode();
      }

      PluginDescriptor pd = this.getPluginDescriptor();
      if (pd != null) {
         result -= pd.hashCode();
      }

      return result;
   }

   public String getExecuteLifecycle() {
      return this.executeLifecycle;
   }

   public void setExecuteLifecycle(String executeLifecycle) {
      this.executeLifecycle = executeLifecycle;
   }

   public void setAggregator(boolean aggregator) {
      this.aggregator = aggregator;
   }

   public boolean isAggregator() {
      return this.aggregator;
   }

   public boolean isDirectInvocationOnly() {
      return this.directInvocationOnly;
   }

   public void setDirectInvocationOnly(boolean directInvocationOnly) {
      this.directInvocationOnly = directInvocationOnly;
   }

   public boolean isRequiresReports() {
      return this.requiresReports;
   }

   public void setRequiresReports(boolean requiresReports) {
      this.requiresReports = requiresReports;
   }

   public void setExecuteGoal(String executeGoal) {
      this.executeGoal = executeGoal;
   }

   public String getExecuteGoal() {
      return this.executeGoal;
   }
}
