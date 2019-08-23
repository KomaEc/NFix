# pdfbox-2266

## Patch
```diff
--- src/main/java/org/apache/pdfbox/pdmodel/graphics/color/PDPattern.java	2019-08-18 11:00:23.002751465 +0800
+++ npe.patch	2019-08-18 11:43:31.013437186 +0800
@@ -16,6 +16,7 @@
  */
 package org.apache.pdfbox.pdmodel.graphics.color;
 
+import java.awt.Color;
 import org.apache.pdfbox.cos.COSName;
 import org.apache.pdfbox.pdmodel.graphics.pattern.PDAbstractPattern;
 import org.apache.pdfbox.pdmodel.graphics.pattern.PDShadingPattern;
@@ -30,6 +31,8 @@
 import java.awt.image.WritableRaster;
 import java.io.IOException;
 import java.util.Map;
+import org.apache.commons.logging.Log;
+import org.apache.commons.logging.LogFactory;
 
 /**
  * A Pattern color space is either a Tiling pattern or a Shading pattern.
@@ -38,7 +41,12 @@
  */
 public final class PDPattern extends PDSpecialColorSpace
 {
-    private Map<String, PDAbstractPattern> patterns;
+    /**
+     * log instance.
+     */
+    private static final Log LOG = LogFactory.getLog(PDPattern.class);
+    
+    private final Map<String, PDAbstractPattern> patterns;
     private PDColorSpace underlyingColorSpace;
 
     /**
@@ -122,6 +130,11 @@
         {
             PDShadingPattern shadingPattern = (PDShadingPattern)pattern;
             PDShading shading = shadingPattern.getShading();
+            if (shading == null)
+            {
+                LOG.error("shadingPattern ist null, will be filled with transparency");
+                return new Color(0,0,0,0);
+            }
             return shading.toPaint(shadingPattern.getMatrix(), pageHeight);
         }
     }
@@ -145,4 +158,4 @@
     {
         return "Pattern";
     }
-}
+}
\ No newline at end of file
```