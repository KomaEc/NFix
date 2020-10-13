# pdfbox-3479

## Patch
```diff
--- src/main/java/org/apache/pdfbox/pdmodel/interactive/form/AppearanceGeneratorHelper.java	2019-09-04 12:48:50.332169659 +0800
+++ npe.patch	2019-09-04 13:07:41.325433378 +0800
@@ -129,6 +129,10 @@
                     appearanceStream = new PDAppearanceStream(field.getAcroForm().getDocument());
                     
                     PDRectangle rect = widget.getRectangle();
+                    if (rect == null)
+                    {
+                        throw new IOException("widget of field " + field.getFullyQualifiedName() + " has no rectangle");
+                    }
                     
                     // Calculate the entries for the bounding box and the transformation matrix
                     // settings for the appearance stream
```