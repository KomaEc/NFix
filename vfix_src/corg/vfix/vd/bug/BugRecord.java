package corg.vfix.vd.bug;

import java.util.ArrayList;
import java.util.Iterator;

public class BugRecord {
   private String project;
   private int id;
   private double time;
   private int passedPatch;
   public int numOfTotalExecuted;
   public int numOfVfgExecuted;
   public int numOfVfgNodes;
   private ArrayList<NodePatchPair> patches;

   public BugRecord(String project, int id) {
      this.project = project;
      this.id = id;
      this.patches = new ArrayList();
   }

   public int getNthPatch() {
      return this.passedPatch;
   }

   public void setTime(double t) {
      this.time = t;
   }

   public int getNumOfPatch() {
      int count = 0;

      NodePatchPair npp;
      for(Iterator var3 = this.patches.iterator(); var3.hasNext(); count += npp.getPatchNumber()) {
         npp = (NodePatchPair)var3.next();
      }

      return count;
   }

   public double getTime() {
      return this.time;
   }

   public void setPatchPass(int p) {
      this.passedPatch = p;
   }

   public String getBugID() {
      return this.project + "-" + this.id;
   }

   public void addNodePatch(ArrayList<String> ps) {
      if (ps.size() != 0) {
         NodePatchPair npp = new NodePatchPair();
         Iterator var4 = ps.iterator();

         while(var4.hasNext()) {
            String patch = (String)var4.next();
            npp.addPatch(patch);
         }

         this.patches.add(npp);
      }
   }

   public String getFinalOperation() {
      int count = 0;
      Iterator var3 = this.patches.iterator();

      while(var3.hasNext()) {
         NodePatchPair npp = (NodePatchPair)var3.next();
         Iterator var5 = npp.getPatches().iterator();

         while(var5.hasNext()) {
            String op = (String)var5.next();
            ++count;
            if (count == this.passedPatch) {
               return op;
            }
         }
      }

      return "";
   }

   public int getCurOperationNum() {
      int count = 0;
      Iterator var3 = this.patches.iterator();

      while(var3.hasNext()) {
         NodePatchPair npp = (NodePatchPair)var3.next();
         count += npp.getPatchNumber();
         if (count >= this.passedPatch) {
            return npp.getPatchNumber();
         }
      }

      return -1;
   }

   public int getTotalOperations() {
      int total = 0;

      NodePatchPair npp;
      for(Iterator var3 = this.patches.iterator(); var3.hasNext(); total += npp.getPatchNumber()) {
         npp = (NodePatchPair)var3.next();
      }

      return total;
   }

   public double getAverageOperations() {
      double total = 0.0D;

      NodePatchPair npp;
      for(Iterator var4 = this.patches.iterator(); var4.hasNext(); total += (double)npp.getPatchNumber()) {
         npp = (NodePatchPair)var4.next();
      }

      return total / (double)this.patches.size();
   }
}
