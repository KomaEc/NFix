package org.apache.maven.plugin.descriptor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.plugin.lifecycle.Lifecycle;
import org.apache.maven.plugin.lifecycle.LifecycleConfiguration;
import org.apache.maven.plugin.lifecycle.io.xpp3.LifecycleMappingsXpp3Reader;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.component.repository.ComponentSetDescriptor;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class PluginDescriptor extends ComponentSetDescriptor {
   private String groupId;
   private String artifactId;
   private String version;
   private String goalPrefix;
   private String source;
   private boolean inheritedByDefault = true;
   private List artifacts;
   private Map lifecycleMappings;
   private ClassRealm classRealm;
   private Map artifactMap;
   private Set introducedDependencyArtifacts;
   private String name;
   private String description;

   public List getMojos() {
      return this.getComponents();
   }

   public void addMojo(MojoDescriptor mojoDescriptor) throws DuplicateMojoDescriptorException {
      MojoDescriptor existing = null;
      List mojos = this.getComponents();
      if (mojos != null && mojos.contains(mojoDescriptor)) {
         int indexOf = mojos.indexOf(mojoDescriptor);
         existing = (MojoDescriptor)mojos.get(indexOf);
      }

      if (existing != null) {
         throw new DuplicateMojoDescriptorException(this.getGoalPrefix(), mojoDescriptor.getGoal(), existing.getImplementation(), mojoDescriptor.getImplementation());
      } else {
         this.addComponentDescriptor(mojoDescriptor);
      }
   }

   public String getGroupId() {
      return this.groupId;
   }

   public void setGroupId(String groupId) {
      this.groupId = groupId;
   }

   public String getArtifactId() {
      return this.artifactId;
   }

   public void setArtifactId(String artifactId) {
      this.artifactId = artifactId;
   }

   public static String constructPluginKey(String groupId, String artifactId, String version) {
      return groupId + ":" + artifactId + ":" + version;
   }

   public String getPluginLookupKey() {
      return this.groupId + ":" + this.artifactId;
   }

   public String getId() {
      return constructPluginKey(this.groupId, this.artifactId, this.version);
   }

   public static String getDefaultPluginArtifactId(String id) {
      return "maven-" + id + "-plugin";
   }

   public static String getDefaultPluginGroupId() {
      return "org.apache.maven.plugins";
   }

   public static String getGoalPrefixFromArtifactId(String artifactId) {
      return "maven-plugin-plugin".equals(artifactId) ? "plugin" : artifactId.replaceAll("-?maven-?", "").replaceAll("-?plugin-?", "");
   }

   public String getGoalPrefix() {
      return this.goalPrefix;
   }

   public void setGoalPrefix(String goalPrefix) {
      this.goalPrefix = goalPrefix;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getVersion() {
      return this.version;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public String getSource() {
      return this.source;
   }

   public boolean isInheritedByDefault() {
      return this.inheritedByDefault;
   }

   public void setInheritedByDefault(boolean inheritedByDefault) {
      this.inheritedByDefault = inheritedByDefault;
   }

   public List getArtifacts() {
      return this.artifacts;
   }

   public void setArtifacts(List artifacts) {
      this.artifacts = artifacts;
      this.artifactMap = null;
   }

   public Map getArtifactMap() {
      if (this.artifactMap == null) {
         this.artifactMap = ArtifactUtils.artifactMapByVersionlessId(this.getArtifacts());
      }

      return this.artifactMap;
   }

   public boolean equals(Object object) {
      return this == object ? true : this.getId().equals(((PluginDescriptor)object).getId());
   }

   public int hashCode() {
      return 10 + this.getId().hashCode();
   }

   public MojoDescriptor getMojo(String goal) {
      if (this.getMojos() == null) {
         return null;
      } else {
         MojoDescriptor mojoDescriptor = null;
         Iterator i = this.getMojos().iterator();

         while(i.hasNext() && mojoDescriptor == null) {
            MojoDescriptor desc = (MojoDescriptor)i.next();
            if (goal.equals(desc.getGoal())) {
               mojoDescriptor = desc;
            }
         }

         return mojoDescriptor;
      }
   }

   public Lifecycle getLifecycleMapping(String lifecycle) throws IOException, XmlPullParserException {
      if (this.lifecycleMappings == null) {
         LifecycleMappingsXpp3Reader reader = new LifecycleMappingsXpp3Reader();
         InputStreamReader r = null;

         LifecycleConfiguration config;
         try {
            InputStream resourceAsStream = this.classRealm.getResourceAsStream("/META-INF/maven/lifecycle.xml");
            if (resourceAsStream == null) {
               throw new FileNotFoundException("Unable to find /META-INF/maven/lifecycle.xml in the plugin");
            }

            r = new InputStreamReader(resourceAsStream);
            config = reader.read(r, true);
         } finally {
            IOUtil.close((Reader)r);
         }

         HashMap map = new HashMap();
         Iterator i = config.getLifecycles().iterator();

         while(i.hasNext()) {
            Lifecycle l = (Lifecycle)i.next();
            map.put(l.getId(), l);
         }

         this.lifecycleMappings = map;
      }

      return (Lifecycle)this.lifecycleMappings.get(lifecycle);
   }

   public void setClassRealm(ClassRealm classRealm) {
      this.classRealm = classRealm;
   }

   public ClassRealm getClassRealm() {
      return this.classRealm;
   }

   public void setIntroducedDependencyArtifacts(Set introducedDependencyArtifacts) {
      this.introducedDependencyArtifacts = introducedDependencyArtifacts;
   }

   public Set getIntroducedDependencyArtifacts() {
      return this.introducedDependencyArtifacts != null ? this.introducedDependencyArtifacts : Collections.EMPTY_SET;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getDescription() {
      return this.description;
   }
}
