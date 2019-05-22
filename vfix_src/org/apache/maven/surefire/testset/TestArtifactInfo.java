package org.apache.maven.surefire.testset;

public class TestArtifactInfo {
   private final String version;
   private final String classifier;

   public TestArtifactInfo(String version, String classifier) {
      this.version = version;
      this.classifier = classifier;
   }

   public String getVersion() {
      return this.version;
   }

   public String getClassifier() {
      return this.classifier;
   }
}
