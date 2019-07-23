# math-1123

## Patch
```diff
--- src/main/java/org/apache/commons/math3/geometry/partitioning/BSPTree.java	2019-07-23 10:12:16.601147298 +0800
+++ npe.patch	2019-07-23 10:22:56.724511037 +0800
@@ -26,21 +26,17 @@
 import org.apache.commons.math3.util.FastMath;
 
 /** This class represent a Binary Space Partition tree.
-
  * <p>BSP trees are an efficient way to represent space partitions and
  * to associate attributes with each cell. Each node in a BSP tree
  * represents a convex region which is partitioned in two convex
  * sub-regions at each side of a cut hyperplane. The root tree
  * contains the complete space.</p>
-
  * <p>The main use of such partitions is to use a boolean attribute to
  * define an inside/outside property, hence representing arbitrary
  * polytopes (line segments in 1D, polygons in 2D and polyhedrons in
  * 3D) and to operate on them.</p>
-
  * <p>Another example would be to represent Voronoi tesselations, the
  * attribute of each cell holding the defining point of the cell.</p>
-
  * <p>The application-defined attributes are shared among copied
  * instances and propagated to split parts. These attributes are not
  * used by the BSP-tree algorithms themselves, so the application can
@@ -51,16 +47,13 @@
  * tree is modified in any way after attributes have been set, some
  * internal nodes may become leaf nodes and some leaf nodes may become
  * internal nodes.</p>
-
  * <p>One of the main sources for the development of this package was
  * Bruce Naylor, John Amanatides and William Thibault paper <a
  * href="http://www.cs.yorku.ca/~amana/research/bsptSetOp.pdf">Merging
  * BSP Trees Yields Polyhedral Set Operations</a> Proc. Siggraph '90,
  * Computer Graphics 24(4), August 1990, pp 115-124, published by the
  * Association for Computing Machinery (ACM).</p>
-
  * @param <S> Type of the space.
-
  * @version $Id$
  * @since 3.0
  */
@@ -294,7 +287,7 @@
      */
     private SubHyperplane<S> fitToCell(final SubHyperplane<S> sub) {
         SubHyperplane<S> s = sub;
-        for (BSPTree<S> tree = this; tree.parent != null; tree = tree.parent) {
+        for (BSPTree<S> tree = this; tree.parent != null && s != null; tree = tree.parent) {
             if (tree == tree.parent.plus) {
                 s = s.split(tree.parent.cut.getHyperplane()).getPlus();
             } else {
@@ -734,4 +727,4 @@
         }
     }
 
-}
+}
\ No newline at end of file

```