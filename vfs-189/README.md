# vfs-189

## Commit
f052896ac6ece2b36c36706604780dbda90b7f41

Parent : 3acd9f3eee0bc74e898e5cb8b50e85fe7a496d70

## Patch
```diff
--- src/main/java/org/apache/commons/vfs2/impl/DefaultFileSystemManager.java	2019-06-24 21:16:51.156777115 +0800
+++ patch.txt	2019-06-24 21:20:46.256786786 +0800
@@ -862,8 +862,11 @@
     public FileName resolveName(final FileName base, final String name,
             final NameScope scope) throws FileSystemException
     {
+        if (base == null) {
+            throw new FileSystemException("Invalid base filename.");
+        }
         final FileName realBase;
-        if (base != null && VFS.isUriStyle() && base.isFile())
+        if (VFS.isUriStyle() && base.isFile())
         {
             realBase = base.getParent();
         }
@@ -1338,4 +1341,4 @@
             throw new FileSystemException(e);
         }
     }
-}
+}
```
