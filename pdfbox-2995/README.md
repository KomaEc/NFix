# pdfbox-2995

## Patch
```diff
--- src/main/java/org/apache/pdfbox/pdmodel/interactive/form/PDAcroForm.java	2019-08-21 11:26:20.685220383 +0800
+++ npe.patch	2019-08-21 11:45:01.172256382 +0800
@@ -29,7 +29,6 @@
 import org.apache.pdfbox.cos.COSDictionary;
 import org.apache.pdfbox.cos.COSName;
 import org.apache.pdfbox.cos.COSNumber;
-import org.apache.pdfbox.cos.COSString;
 import org.apache.pdfbox.pdmodel.PDDocument;
 import org.apache.pdfbox.pdmodel.PDPage;
 import org.apache.pdfbox.pdmodel.PDPageContentStream;
@@ -352,8 +351,7 @@
      */
     public String getDefaultAppearance()
     {
-        COSString defaultAppearance = (COSString) dictionary.getItem(COSName.DA);
-        return defaultAppearance.getString();
+        return dictionary.getString(COSName.DA,"");
     }
 
     /**
@@ -529,4 +527,4 @@
     {
         dictionary.setFlag(COSName.SIG_FLAGS, FLAG_APPEND_ONLY, appendOnly);
     }
-}
+}
\ No newline at end of file
```