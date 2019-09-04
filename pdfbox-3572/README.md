# pdfbox-3572

## Patch
```diff
--- src/main/java/org/apache/pdfbox/pdmodel/encryption/SecurityHandler.java	2019-09-04 14:05:57.645260143 +0800
+++ npe.patch	2019-09-04 14:13:47.966443247 +0800
@@ -267,7 +267,11 @@
             int n;
             while ((n = data.read(buffer)) != -1)
             {
-                output.write(decryptCipher.update(buffer, 0, n));
+                byte[] dst = decryptCipher.update(buffer, 0, n);
+                if (dst != null)
+                {
+                    output.write(dst);
+                }
             }
             output.write(decryptCipher.doFinal());
         }
@@ -641,4 +645,4 @@
      * @return true if a protection policy has been set.
      */
     public abstract boolean hasProtectionPolicy();
-}
+}
\ No newline at end of file
```