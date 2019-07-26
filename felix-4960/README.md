# felix-4960

## Patch
```diff
--- src/main/java/org/apache/felix/framework/BundleRevisionImpl.java	2019-07-25 23:06:25.357232441 +0800
+++ npe.patch	2019-07-26 08:54:35.292587862 +0800
@@ -202,7 +202,7 @@
 
     static List<Capability> asCapabilityList(List reqs)
     {
-        return (List<Capability>) reqs;
+        return reqs;
     }
 
     public List<BundleCapability> getDeclaredCapabilities(String namespace)
@@ -229,7 +229,7 @@
 
     static List<Requirement> asRequirementList(List reqs)
     {
-        return (List<Requirement>) reqs;
+        return reqs;
     }
 
     public List<BundleRequirement> getDeclaredRequirements(String namespace)
@@ -517,6 +517,9 @@
         // each bundle class path entry...this isn't very
         // clean or meaningful, but the Spring guys want it.
         final List<Content> contentPath = getContentPath();
+        if (contentPath == null)
+            return Collections.emptyEnumeration();
+
         if (name.equals("/"))
         {
             for (int i = 0; i < contentPath.size(); i++)
@@ -676,4 +679,4 @@
     {
         return m_bundle.toString() + "(R " + m_id + ")";
     }
-}
+}
\ No newline at end of file
```