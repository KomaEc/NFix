package corg.vfix.sa.analysis;

import corg.vfix.sa.cg.CG;
import corg.vfix.sa.cg.CGEdge;
import corg.vfix.sa.cg.CGNode;
import java.util.ArrayList;
import java.util.Iterator;
import soot.SootMethod;

public class ModRefAnalyzor {
   private static void clearMod(CG cg) {
      Iterator var2 = cg.getNodes().iterator();

      while(var2.hasNext()) {
         CGNode node = (CGNode)var2.next();
         node.clearMod();
      }

   }

   private static void clearRef(CG cg) {
      Iterator var2 = cg.getNodes().iterator();

      while(var2.hasNext()) {
         CGNode node = (CGNode)var2.next();
         node.clearRef();
      }

   }

   public static void refAnalysis(CG cg, SootMethod mtd) {
      ArrayList<SootMethod> mtds = new ArrayList();
      mtds.add(mtd);
      refAnalysis(cg, mtds);
   }

   public static void refAnalysis(CG cg, ArrayList<SootMethod> mtds) {
      clearRef(cg);
      Iterator var3 = mtds.iterator();

      while(var3.hasNext()) {
         SootMethod mtd = (SootMethod)var3.next();
         Iterator var5 = cg.getNodes().iterator();

         while(var5.hasNext()) {
            CGNode node = (CGNode)var5.next();
            if (node.getMtd().equals(mtd)) {
               node.setRef();
            }
         }
      }

      boolean finished = false;

      while(!finished) {
         finished = true;
         Iterator var8 = cg.getEdges().iterator();

         while(var8.hasNext()) {
            CGEdge edge = (CGEdge)var8.next();
            if (edge.getTgt().hasRef() && !edge.getSrc().hasRef()) {
               finished = false;
               edge.getSrc().setRef();
               edge.getSrc().addRefUnit(edge.getUnit());
            }
         }
      }

   }

   public static void modAnalysis(CG cg, ArrayList<SootMethod> mtds) {
      clearMod(cg);
      Iterator var3 = mtds.iterator();

      while(var3.hasNext()) {
         SootMethod mtd = (SootMethod)var3.next();
         Iterator var5 = cg.getNodes().iterator();

         while(var5.hasNext()) {
            CGNode node = (CGNode)var5.next();
            if (node.getMtd().equals(mtd)) {
               node.setMod();
            }
         }
      }

      boolean finished = false;

      while(!finished) {
         finished = true;
         Iterator var8 = cg.getEdges().iterator();

         while(var8.hasNext()) {
            CGEdge edge = (CGEdge)var8.next();
            if (edge.getTgt().hasMod() && !edge.getSrc().hasMod()) {
               finished = false;
               edge.getSrc().setMod();
               edge.getSrc().addModUnit(edge.getUnit());
            }
         }
      }

   }

   public static void modAnalysis(CG cg, SootMethod mtd) {
      ArrayList<SootMethod> mtds = new ArrayList();
      mtds.add(mtd);
      modAnalysis(cg, mtds);
   }
}
