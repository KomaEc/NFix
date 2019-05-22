package org.apache.maven.scm.provider.accurev;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategorisedElements {
   private List<File> memberElements = new ArrayList();
   private List<File> nonMemberElements = new ArrayList();

   public List<File> getMemberElements() {
      return this.memberElements;
   }

   public List<File> getNonMemberElements() {
      return this.nonMemberElements;
   }
}
