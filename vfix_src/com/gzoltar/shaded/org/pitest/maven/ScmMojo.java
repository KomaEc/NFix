package com.gzoltar.shaded.org.pitest.maven;

import com.gzoltar.shaded.org.pitest.functional.F;
import com.gzoltar.shaded.org.pitest.functional.FCollection;
import com.gzoltar.shaded.org.pitest.functional.Option;
import com.gzoltar.shaded.org.pitest.functional.predicate.Predicate;
import com.gzoltar.shaded.org.pitest.mutationtest.config.PluginServices;
import com.gzoltar.shaded.org.pitest.mutationtest.config.ReportOptions;
import com.gzoltar.shaded.org.pitest.mutationtest.tooling.CombinedStatistics;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.command.status.StatusScmResult;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.repository.ScmRepository;
import org.codehaus.plexus.util.StringUtils;

public class ScmMojo extends PitMojo {
   private ScmManager manager;
   private HashSet<String> include;
   private String connectionType;
   private File basedir;
   private File scmRootDir;

   public ScmMojo(RunPitStrategy executionStrategy, ScmManager manager, Predicate<Artifact> filter, PluginServices plugins) {
      super(executionStrategy, filter, plugins);
      this.manager = manager;
   }

   public ScmMojo() {
   }

   protected Option<CombinedStatistics> analyse() throws MojoExecutionException {
      this.targetClasses = makeConcreteList(this.findModifiedClassNames());
      if (this.targetClasses.isEmpty()) {
         this.getLog().info((CharSequence)"No locally modified files found - nothing to mutation test");
         return Option.none();
      } else {
         this.logClassNames();
         this.defaultTargetTestsToGroupNameIfNoValueSet();
         ReportOptions data = (new MojoToReportOptionsConverter(this, new SurefireConfigConverter(), this.filter)).convert();
         data.setFailWhenNoMutations(false);
         return Option.some(this.goalStrategy.execute(this.detectBaseDir(), data, this.plugins, new HashMap()));
      }
   }

   private void defaultTargetTestsToGroupNameIfNoValueSet() {
      if (this.getTargetTests() == null) {
         this.targetTests = makeConcreteList(Collections.singletonList(this.getProject().getGroupId() + "*"));
      }

   }

   private void logClassNames() {
      Iterator i$ = this.targetClasses.iterator();

      while(i$.hasNext()) {
         String each = (String)i$.next();
         this.getLog().info((CharSequence)("Will mutate locally changed class " + each));
      }

   }

   private List<String> findModifiedClassNames() throws MojoExecutionException {
      File sourceRoot = new File(this.project.getBuild().getSourceDirectory());
      List<String> modifiedPaths = this.findModifiedPaths();
      return FCollection.flatMap(modifiedPaths, new PathToJavaClassConverter(sourceRoot.getAbsolutePath()));
   }

   private List<String> findModifiedPaths() throws MojoExecutionException {
      try {
         Set<ScmFileStatus> statusToInclude = this.makeStatusSet();
         List<String> modifiedPaths = new ArrayList();
         ScmRepository repository = this.manager.makeScmRepository(this.getSCMConnection());
         File scmRoot = this.scmRoot();
         this.getLog().info((CharSequence)("Scm root dir is " + scmRoot));
         StatusScmResult status = this.manager.status(repository, new ScmFileSet(scmRoot));
         Iterator i$ = status.getChangedFiles().iterator();

         while(i$.hasNext()) {
            ScmFile file = (ScmFile)i$.next();
            if (statusToInclude.contains(file.getStatus())) {
               modifiedPaths.add(file.getPath());
            }
         }

         return modifiedPaths;
      } catch (ScmException var8) {
         throw new MojoExecutionException("Error while querying scm", var8);
      }
   }

   private Set<ScmFileStatus> makeStatusSet() {
      if (this.include != null && !this.include.isEmpty()) {
         Set<ScmFileStatus> s = new HashSet();
         FCollection.mapTo(this.include, stringToMavenScmStatus(), s);
         return s;
      } else {
         return new HashSet(Arrays.asList(ScmStatus.ADDED.getStatus(), ScmStatus.MODIFIED.getStatus()));
      }
   }

   private static F<String, ScmFileStatus> stringToMavenScmStatus() {
      return new F<String, ScmFileStatus>() {
         public ScmFileStatus apply(String a) {
            return ScmStatus.valueOf(a.toUpperCase()).getStatus();
         }
      };
   }

   private File scmRoot() {
      return this.scmRootDir != null ? this.scmRootDir : this.basedir;
   }

   private String getSCMConnection() throws MojoExecutionException {
      if (this.project.getScm() == null) {
         throw new MojoExecutionException("No SCM Connection configured.");
      } else {
         String scmConnection = this.project.getScm().getConnection();
         if ("connection".equalsIgnoreCase(this.connectionType) && StringUtils.isNotEmpty(scmConnection)) {
            return scmConnection;
         } else {
            String scmDeveloper = this.project.getScm().getDeveloperConnection();
            if ("developerconnection".equalsIgnoreCase(this.connectionType) && StringUtils.isNotEmpty(scmDeveloper)) {
               return scmDeveloper;
            } else {
               throw new MojoExecutionException("SCM Connection is not set.");
            }
         }
      }
   }

   public void setConnectionType(String connectionType) {
      this.connectionType = connectionType;
   }

   public void setScmRootDir(File scmRootDir) {
      this.scmRootDir = scmRootDir;
   }

   private static ArrayList<String> makeConcreteList(List<String> list) {
      return new ArrayList(list);
   }
}
