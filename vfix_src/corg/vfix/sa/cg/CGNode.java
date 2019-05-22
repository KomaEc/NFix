package corg.vfix.sa.cg;

import corg.vfix.fl.spectrum.FLSpectrum;
import java.util.ArrayList;
import soot.SootMethod;
import soot.Unit;

public class CGNode {
   private SootMethod mtd;
   private boolean executed = false;
   private boolean mod = false;
   private ArrayList<Unit> modUnits;
   private boolean ref = false;
   private ArrayList<Unit> refUnits;

   public CGNode(SootMethod m) {
      this.mtd = m;
      this.executed = FLSpectrum.query(this.mtd);
      this.modUnits = new ArrayList();
      this.refUnits = new ArrayList();
   }

   public void setMod() {
      this.mod = true;
   }

   public void addModUnit(Unit unit) {
      if (!this.modUnits.contains(unit)) {
         this.modUnits.add(unit);
      }

   }

   public void addRefUnit(Unit unit) {
      if (!this.refUnits.contains(unit)) {
         this.refUnits.add(unit);
      }

   }

   public ArrayList<Unit> getModUnits() {
      return this.modUnits;
   }

   public ArrayList<Unit> getRefUnits() {
      return this.refUnits;
   }

   public void clearMod() {
      this.mod = false;
      this.modUnits.clear();
   }

   public boolean hasMod() {
      return this.mod;
   }

   public void setRef() {
      this.ref = true;
   }

   public void clearRef() {
      this.ref = false;
      this.refUnits.clear();
   }

   public boolean hasRef() {
      return this.ref;
   }

   public SootMethod getMtd() {
      return this.mtd;
   }

   public boolean isExecuted() {
      return this.executed;
   }

   public boolean equals(CGNode node) {
      return this.mtd != null && node != null ? this.mtd.equals(node.getMtd()) : false;
   }

   public String toString() {
      return this.mtd.getName();
   }
}
