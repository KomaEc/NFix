package soot.jimple.spark.geom.geomPA;

import soot.jimple.spark.geom.dataRep.RectangleNode;
import soot.jimple.spark.geom.dataRep.SegmentNode;

public abstract class IFigureManager {
   private static SegmentNode segHeader = null;
   private static SegmentNode rectHeader = null;

   protected static SegmentNode getSegmentNode() {
      SegmentNode ret = null;
      if (segHeader != null) {
         ret = segHeader;
         segHeader = ret.next;
         ret.next = null;
         ret.is_new = true;
      } else {
         ret = new SegmentNode();
      }

      return ret;
   }

   protected static RectangleNode getRectangleNode() {
      RectangleNode ret = null;
      if (rectHeader != null) {
         ret = (RectangleNode)rectHeader;
         rectHeader = ret.next;
         ret.next = null;
         ret.is_new = true;
      } else {
         ret = new RectangleNode();
      }

      return ret;
   }

   protected static SegmentNode reclaimSegmentNode(SegmentNode p) {
      SegmentNode q = p.next;
      p.next = segHeader;
      segHeader = p;
      return q;
   }

   protected static SegmentNode reclaimRectangleNode(SegmentNode p) {
      SegmentNode q = p.next;
      p.next = rectHeader;
      rectHeader = p;
      return q;
   }

   public static void cleanCache() {
      segHeader = null;
      rectHeader = null;
   }

   public abstract SegmentNode[] getFigures();

   public abstract int[] getSizes();

   public abstract boolean isThereUnprocessedFigures();

   public abstract void flush();

   public abstract SegmentNode addNewFigure(int var1, RectangleNode var2);

   public abstract void mergeFigures(int var1);

   public abstract void removeUselessSegments();
}
