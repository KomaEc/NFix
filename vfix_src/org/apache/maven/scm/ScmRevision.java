package org.apache.maven.scm;

public class ScmRevision extends AbstractScmVersion {
   private static final long serialVersionUID = 3831426256650754391L;

   public String getType() {
      return "Revision";
   }

   public ScmRevision(String name) {
      super(name);
   }
}
