package org.apache.maven.artifact.versioning;

public class Restriction {
   private final ArtifactVersion lowerBound;
   private final boolean lowerBoundInclusive;
   private final ArtifactVersion upperBound;
   private final boolean upperBoundInclusive;
   static final Restriction EVERYTHING = new Restriction((ArtifactVersion)null, false, (ArtifactVersion)null, false);

   public Restriction(ArtifactVersion lowerBound, boolean lowerBoundInclusive, ArtifactVersion upperBound, boolean upperBoundInclusive) {
      this.lowerBound = lowerBound;
      this.lowerBoundInclusive = lowerBoundInclusive;
      this.upperBound = upperBound;
      this.upperBoundInclusive = upperBoundInclusive;
   }

   public ArtifactVersion getLowerBound() {
      return this.lowerBound;
   }

   public boolean isLowerBoundInclusive() {
      return this.lowerBoundInclusive;
   }

   public ArtifactVersion getUpperBound() {
      return this.upperBound;
   }

   public boolean isUpperBoundInclusive() {
      return this.upperBoundInclusive;
   }

   public boolean containsVersion(ArtifactVersion version) {
      int comparison;
      if (this.lowerBound != null) {
         comparison = this.lowerBound.compareTo(version);
         if (comparison == 0 && !this.lowerBoundInclusive) {
            return false;
         }

         if (comparison > 0) {
            return false;
         }
      }

      if (this.upperBound != null) {
         comparison = this.upperBound.compareTo(version);
         if (comparison == 0 && !this.upperBoundInclusive) {
            return false;
         }

         if (comparison < 0) {
            return false;
         }
      }

      return true;
   }

   public int hashCode() {
      int result = 13;
      int result;
      if (this.lowerBound == null) {
         result = result + 1;
      } else {
         result = result + this.lowerBound.hashCode();
      }

      result *= this.lowerBoundInclusive ? 1 : 2;
      if (this.upperBound == null) {
         result -= 3;
      } else {
         result -= this.upperBound.hashCode();
      }

      result *= this.upperBoundInclusive ? 2 : 3;
      return result;
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof Restriction)) {
         return false;
      } else {
         Restriction restriction = (Restriction)other;
         if (this.lowerBound != null) {
            if (!this.lowerBound.equals(restriction.lowerBound)) {
               return false;
            }
         } else if (restriction.lowerBound != null) {
            return false;
         }

         if (this.lowerBoundInclusive != restriction.lowerBoundInclusive) {
            return false;
         } else {
            if (this.upperBound != null) {
               if (!this.upperBound.equals(restriction.upperBound)) {
                  return false;
               }
            } else if (restriction.upperBound != null) {
               return false;
            }

            return this.upperBoundInclusive == restriction.upperBoundInclusive;
         }
      }
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append(this.isLowerBoundInclusive() ? "[" : "(");
      if (this.getLowerBound() != null) {
         buf.append(this.getLowerBound().toString());
      }

      buf.append(",");
      if (this.getUpperBound() != null) {
         buf.append(this.getUpperBound().toString());
      }

      buf.append(this.isUpperBoundInclusive() ? "]" : ")");
      return buf.toString();
   }
}
