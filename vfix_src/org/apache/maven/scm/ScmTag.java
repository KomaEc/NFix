package org.apache.maven.scm;

public class ScmTag extends ScmBranch {
   private static final long serialVersionUID = 2286671802987769257L;

   public String getType() {
      return "Tag";
   }

   public ScmTag(String name) {
      super(name);
   }
}
