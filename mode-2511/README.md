# mode-2511

## Commit 
c5fb58831192e5381af0ba02ef8bb5cec6235c8f

## Patch
--- src/main/java/org/modeshape/jcr/cache/document/WritableSessionCache.java	2019-06-25 16:30:31.876793698 +0800
+++ patch.txt	2019-06-25 16:31:52.937635601 +0800
@@ -1024,18 +1024,23 @@
                         MutableChildReferences appended = mutableParent.appended(false);
-                        assert appended != null;
                         Path parentPath = sessionPaths.getPath(mutableParent);
-                        newPath = pathFactory().create(parentPath, appended.getChild(key).getName());
+                        ChildReference ref = appended.getChild(key);
+                        if (ref == null) {
+                            // the child is new, but doesn't appear in the 'appended' list, so it must've been reordered...
+                            ref = mutableParent.changedChildren().inserted(key);
+                        }
+                        assert ref != null;
+                        newPath = pathFactory().create(parentPath, ref.getName());
                         sessionPaths.put(key, newPath);
                     } else {
                         // do a regular lookup of the path, which *will load* the parent's child references
                         newPath = sessionPaths.getPath(node);
-                    }                    
+                    }
                     // Create an event ...
                     changes.nodeCreated(key, newParent, newPath, primaryType, mixinTypes, node.changedProperties());
                 } else {
