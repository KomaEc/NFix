package org.apache.maven.plugin.surefire.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.surefire.shade.org.apache.maven.shared.utils.io.MatchPatterns;
import org.apache.maven.surefire.util.DefaultScanResult;

public class DependencyScanner {
   private final List<File> dependenciesToScan;
   protected final List<String> includes;
   @Nonnull
   protected final List<String> excludes;
   protected final List<String> specificTests;

   public DependencyScanner(List<File> dependenciesToScan, List<String> includes, @Nonnull List<String> excludes, List<String> specificTests) {
      this.dependenciesToScan = dependenciesToScan;
      this.includes = includes;
      this.excludes = excludes;
      this.specificTests = specificTests;
   }

   public DefaultScanResult scan() throws MojoExecutionException {
      DependencyScanner.Matcher matcher = new DependencyScanner.Matcher(this.includes, this.excludes, this.specificTests);
      List<String> found = new ArrayList();
      Iterator i$ = this.dependenciesToScan.iterator();

      while(i$.hasNext()) {
         File artifact = (File)i$.next();

         try {
            found.addAll(this.scanArtifact(artifact, matcher));
         } catch (IOException var6) {
            throw new MojoExecutionException("Could not scan dependency " + artifact.toString(), var6);
         }
      }

      return new DefaultScanResult(found);
   }

   private List<String> scanArtifact(File artifact, DependencyScanner.Matcher matcher) throws IOException {
      List<String> found = new ArrayList();
      if (artifact != null && artifact.isFile()) {
         JarFile jar = null;

         try {
            jar = new JarFile(artifact);
            Enumeration entries = jar.entries();

            while(entries.hasMoreElements()) {
               JarEntry entry = (JarEntry)entries.nextElement();
               if (matcher.shouldInclude(entry.getName())) {
                  found.add(ScannerUtil.convertJarFileResourceToJavaClassName(entry.getName()));
               }
            }
         } finally {
            if (jar != null) {
               jar.close();
            }

         }
      }

      return found;
   }

   public static List<File> filter(List<Artifact> artifacts, List<String> groupArtifactIds) {
      List<File> matches = new ArrayList();
      if (groupArtifactIds != null && artifacts != null) {
         Iterator i$ = artifacts.iterator();

         while(i$.hasNext()) {
            Artifact artifact = (Artifact)i$.next();
            Iterator i$ = groupArtifactIds.iterator();

            while(i$.hasNext()) {
               String groups = (String)i$.next();
               String[] groupArtifact = groups.split(":");
               if (groupArtifact.length != 2) {
                  throw new IllegalArgumentException("dependencyToScan argument should be in format 'groupid:artifactid': " + groups);
               }

               if (artifact.getGroupId().matches(groupArtifact[0]) && artifact.getArtifactId().matches(groupArtifact[1])) {
                  matches.add(artifact.getFile());
               }
            }
         }

         return matches;
      } else {
         return matches;
      }
   }

   private class Matcher {
      private MatchPatterns includes;
      private MatchPatterns excludes;
      private SpecificFileFilter specificTestFilter;

      public Matcher(@Nullable List<String> includes, @Nonnull List<String> excludes, @Nullable List<String> specificTests) {
         String[] specific = specificTests == null ? new String[0] : ScannerUtil.processIncludesExcludes(specificTests);
         this.specificTestFilter = new SpecificFileFilter(specific);
         if (includes != null && includes.size() > 0) {
            this.includes = MatchPatterns.from(ScannerUtil.processIncludesExcludes(includes));
         } else {
            this.includes = MatchPatterns.from("**");
         }

         this.excludes = MatchPatterns.from(ScannerUtil.processIncludesExcludes(excludes));
      }

      public boolean shouldInclude(String name) {
         if (!name.endsWith(".class")) {
            return false;
         } else {
            name = ScannerUtil.convertSlashToSystemFileSeparator(name);
            boolean isIncluded = this.includes.matches(name, false);
            boolean isExcluded = this.excludes.matches(name, false);
            return isIncluded && !isExcluded && this.specificTestFilter.accept(name);
         }
      }
   }
}
