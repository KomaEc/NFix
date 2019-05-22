package org.apache.maven.surefire.shade.org.apache.maven.shared.artifact.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.ArtifactUtils;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.codehaus.plexus.logging.Logger;

public class PatternIncludesArtifactFilter implements ArtifactFilter, StatisticsReportingArtifactFilter {
   private final List positivePatterns;
   private final List negativePatterns;
   private final boolean actTransitively;
   private final Set patternsTriggered;
   private final List filteredArtifactIds;

   public PatternIncludesArtifactFilter(List patterns) {
      this(patterns, false);
   }

   public PatternIncludesArtifactFilter(List patterns, boolean actTransitively) {
      this.patternsTriggered = new HashSet();
      this.filteredArtifactIds = new ArrayList();
      this.actTransitively = actTransitively;
      List pos = new ArrayList();
      List neg = new ArrayList();
      if (patterns != null && !patterns.isEmpty()) {
         Iterator it = patterns.iterator();

         while(it.hasNext()) {
            String pattern = (String)it.next();
            if (pattern.startsWith("!")) {
               neg.add(pattern.substring(1));
            } else {
               pos.add(pattern);
            }
         }
      }

      this.positivePatterns = pos;
      this.negativePatterns = neg;
   }

   public boolean include(Artifact artifact) {
      boolean shouldInclude = this.patternMatches(artifact);
      if (!shouldInclude) {
         this.addFilteredArtifactId(artifact.getId());
      }

      return shouldInclude;
   }

   protected boolean patternMatches(Artifact artifact) {
      return this.positiveMatch(artifact) == Boolean.TRUE || this.negativeMatch(artifact) == Boolean.FALSE;
   }

   protected void addFilteredArtifactId(String artifactId) {
      this.filteredArtifactIds.add(artifactId);
   }

   private Boolean negativeMatch(Artifact artifact) {
      return this.negativePatterns != null && !this.negativePatterns.isEmpty() ? this.match(artifact, this.negativePatterns) : null;
   }

   protected Boolean positiveMatch(Artifact artifact) {
      return this.positivePatterns != null && !this.positivePatterns.isEmpty() ? this.match(artifact, this.positivePatterns) : null;
   }

   private boolean match(Artifact artifact, List patterns) {
      String shortId = ArtifactUtils.versionlessKey(artifact);
      String id = artifact.getDependencyConflictId();
      String wholeId = artifact.getId();
      if (this.matchAgainst(wholeId, patterns, false)) {
         return true;
      } else if (this.matchAgainst(id, patterns, false)) {
         return true;
      } else if (this.matchAgainst(shortId, patterns, false)) {
         return true;
      } else {
         if (this.actTransitively) {
            List depTrail = artifact.getDependencyTrail();
            if (depTrail != null && depTrail.size() > 1) {
               Iterator iterator = depTrail.iterator();

               while(iterator.hasNext()) {
                  String trailItem = (String)iterator.next();
                  if (this.matchAgainst(trailItem, patterns, true)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   private boolean matchAgainst(String value, List patterns, boolean regionMatch) {
      Iterator iterator = patterns.iterator();

      String pattern;
      do {
         if (!iterator.hasNext()) {
            return false;
         }

         pattern = (String)iterator.next();
         String[] patternTokens = pattern.split(":");
         String[] tokens = value.split(":");
         boolean matched = patternTokens.length <= tokens.length;

         int i;
         for(i = 0; matched && i < patternTokens.length; ++i) {
            matched = this.matches(tokens[i], patternTokens[i]);
         }

         if (!matched && patternTokens.length < tokens.length && patternTokens.length > 0 && "*".equals(patternTokens[0])) {
            matched = true;

            for(i = 0; matched && i < patternTokens.length; ++i) {
               matched = this.matches(tokens[i + (tokens.length - patternTokens.length)], patternTokens[i]);
            }
         }

         if (matched) {
            this.patternsTriggered.add(pattern);
            return true;
         }
      } while(!regionMatch || value.indexOf(pattern) <= -1);

      this.patternsTriggered.add(pattern);
      return true;
   }

   private boolean matches(String token, String pattern) {
      boolean matches;
      if (!"*".equals(pattern) && pattern.length() != 0) {
         String prefix;
         if (pattern.startsWith("*") && pattern.endsWith("*")) {
            prefix = pattern.substring(1, pattern.length() - 1);
            matches = token.indexOf(prefix) != -1;
         } else if (pattern.startsWith("*")) {
            prefix = pattern.substring(1, pattern.length());
            matches = token.endsWith(prefix);
         } else if (pattern.endsWith("*")) {
            prefix = pattern.substring(0, pattern.length() - 1);
            matches = token.startsWith(prefix);
         } else if (!pattern.startsWith("[") && !pattern.startsWith("(")) {
            matches = token.equals(pattern);
         } else {
            matches = this.isVersionIncludedInRange(token, pattern);
         }
      } else {
         matches = true;
      }

      return matches;
   }

   private boolean isVersionIncludedInRange(String version, String range) {
      try {
         return VersionRange.createFromVersionSpec(range).containsVersion(new DefaultArtifactVersion(version));
      } catch (InvalidVersionSpecificationException var4) {
         return false;
      }
   }

   public void reportMissedCriteria(Logger logger) {
      if (!this.positivePatterns.isEmpty() || !this.negativePatterns.isEmpty()) {
         List missed = new ArrayList();
         missed.addAll(this.positivePatterns);
         missed.addAll(this.negativePatterns);
         missed.removeAll(this.patternsTriggered);
         if (!missed.isEmpty() && logger.isWarnEnabled()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("The following patterns were never triggered in this ");
            buffer.append(this.getFilterDescription());
            buffer.append(':');
            Iterator it = missed.iterator();

            while(it.hasNext()) {
               String pattern = (String)it.next();
               buffer.append("\no  '").append(pattern).append("'");
            }

            buffer.append("\n");
            logger.warn(buffer.toString());
         }
      }

   }

   public String toString() {
      return "Includes filter:" + this.getPatternsAsString();
   }

   protected String getPatternsAsString() {
      StringBuffer buffer = new StringBuffer();
      Iterator it = this.positivePatterns.iterator();

      while(it.hasNext()) {
         String pattern = (String)it.next();
         buffer.append("\no '").append(pattern).append("'");
      }

      return buffer.toString();
   }

   protected String getFilterDescription() {
      return "artifact inclusion filter";
   }

   public void reportFilteredArtifacts(Logger logger) {
      if (!this.filteredArtifactIds.isEmpty() && logger.isDebugEnabled()) {
         StringBuffer buffer = new StringBuffer("The following artifacts were removed by this " + this.getFilterDescription() + ": ");
         Iterator it = this.filteredArtifactIds.iterator();

         while(it.hasNext()) {
            String artifactId = (String)it.next();
            buffer.append('\n').append(artifactId);
         }

         logger.debug(buffer.toString());
      }

   }

   public boolean hasMissedCriteria() {
      if (this.positivePatterns.isEmpty() && this.negativePatterns.isEmpty()) {
         return false;
      } else {
         List missed = new ArrayList();
         missed.addAll(this.positivePatterns);
         missed.addAll(this.negativePatterns);
         missed.removeAll(this.patternsTriggered);
         return !missed.isEmpty();
      }
   }
}
