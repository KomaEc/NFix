# pdfbox-2948

## Patch
```diff
--- src/main/java/org/apache/pdfbox/pdmodel/common/PDStream.java	2019-08-20 22:08:42.204077720 +0800
+++ npe.patch	2019-08-20 23:08:49.213747438 +0800
@@ -247,20 +247,23 @@
         InputStream is = stream.createRawInputStream();
         ByteArrayOutputStream os = new ByteArrayOutputStream();
         List<COSName> filters = getFilters();
-        for (int i = 0; i < filters.size(); i++)
+        if (filters != null)
         {
-            COSName nextFilter = filters.get(i);
-            if (stopFilters.contains(nextFilter.getName()))
+            for (int i = 0; i < filters.size(); i++)
             {
-                break;
-            }
-            else
-            {
-                Filter filter = FilterFactory.INSTANCE.getFilter(nextFilter);
-                filter.decode(is, os, stream, i);
-                IOUtils.closeQuietly(is);
-                is = new ByteArrayInputStream(os.toByteArray());
-                os.reset();
+                COSName nextFilter = filters.get(i);
+                if ((stopFilters != null) && stopFilters.contains(nextFilter.getName()))
+                {
+                    break;
+                }
+                else
+                {
+                    Filter filter = FilterFactory.INSTANCE.getFilter(nextFilter);
+                    filter.decode(is, os, stream, i);
+                    IOUtils.closeQuietly(is);
+                    is = new ByteArrayInputStream(os.toByteArray());
+                    os.reset();
+                }
             }
         }
         return is;
@@ -567,4 +570,4 @@
     {
         this.stream.setInt(COSName.DL, decodedStreamLength);
     }
-}
+}
\ No newline at end of file
```