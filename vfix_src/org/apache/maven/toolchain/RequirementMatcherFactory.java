package org.apache.maven.toolchain;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

public final class RequirementMatcherFactory {
   private RequirementMatcherFactory() {
   }

   public static RequirementMatcher createExactMatcher(String provideValue) {
      return new RequirementMatcherFactory.ExactMatcher(provideValue);
   }

   public static RequirementMatcher createVersionMatcher(String provideValue) {
      return new RequirementMatcherFactory.VersionMatcher(provideValue);
   }

   private static final class VersionMatcher implements RequirementMatcher {
      DefaultArtifactVersion version;

      private VersionMatcher(String version) {
         this.version = new DefaultArtifactVersion(version);
      }

      public boolean matches(String requirement) {
         try {
            VersionRange range = VersionRange.createFromVersionSpec(requirement);
            if (range.hasRestrictions()) {
               return range.containsVersion(this.version);
            } else {
               return range.getRecommendedVersion().compareTo(this.version) == 0;
            }
         } catch (InvalidVersionSpecificationException var3) {
            var3.printStackTrace();
            return false;
         }
      }

      // $FF: synthetic method
      VersionMatcher(String x0, Object x1) {
         this(x0);
      }
   }

   private static final class ExactMatcher implements RequirementMatcher {
      private String provides;

      private ExactMatcher(String provides) {
         this.provides = provides;
      }

      public boolean matches(String requirement) {
         return this.provides.equalsIgnoreCase(requirement);
      }

      // $FF: synthetic method
      ExactMatcher(String x0, Object x1) {
         this(x0);
      }
   }
}
