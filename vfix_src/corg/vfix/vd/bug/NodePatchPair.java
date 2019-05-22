package corg.vfix.vd.bug;

import java.util.ArrayList;

public class NodePatchPair {
   private ArrayList<String> patches = new ArrayList();

   NodePatchPair() {
   }

   public ArrayList<String> getPatches() {
      return this.patches;
   }

   public void addPatch(String patch) {
      if (!this.patches.contains(patch)) {
         this.patches.add(patch);
      }

   }

   public int getPatchNumber() {
      return this.patches.size();
   }
}
