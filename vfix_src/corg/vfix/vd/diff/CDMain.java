package corg.vfix.vd.diff;

import java.io.IOException;

public class CDMain {
   public static void main(String[] args) throws IOException {
      String project = args[0];
      int id = Integer.valueOf(args[1]);
      int patchNum = Integer.valueOf(args[2]);
      CodeDiff cd = new CodeDiff(project, id, patchNum);
      System.out.println("Compare Java Files: ");
      cd.diff();
      cd.extractTwoFiles();
   }
}
