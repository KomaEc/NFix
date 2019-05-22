package org.apache.maven.scm;

public class ScmBranch extends AbstractScmVersion {
   private static final long serialVersionUID = 6305050785257168739L;

   public String getType() {
      return "Branch";
   }

   public ScmBranch(String name) {
      super(name);
   }
}
