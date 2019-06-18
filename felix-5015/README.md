# felix-5015

## Commit
ee515ac02e2821d90f269158d9b2893fc1bd8125
c6e7402aaed5541fceb7562fa1ba371e3acec1cd
parent : ac5ec40ee40ec92dc8124e22afa855ab2c4a850f

## Infer
Fail to locate

## Types
multiple null checks

## Patch
```diff
--- ./src/main/java/org/apache/felix/resolver/ResolverImpl.java	2019-06-18 19:29:05.232185414 +0800
+++ patch.txt	2019-06-18 19:31:58.255264238 +0800
@@ -40,7 +40,6 @@
 
 import org.apache.felix.resolver.util.ArrayMap;
 import org.apache.felix.resolver.util.OpenHashMap;
-
 import org.osgi.framework.namespace.BundleNamespace;
 import org.osgi.framework.namespace.ExecutionEnvironmentNamespace;
 import org.osgi.framework.namespace.HostNamespace;
@@ -1717,7 +1716,25 @@
     private static Set<Capability> getPackageSources(
             Capability cap, Map<Resource, Packages> resourcePkgMap)
     {
-        return resourcePkgMap.get(cap.getResource()).m_sources.get(cap);
+        Resource resource = cap.getResource();
+        if(resource == null)
+        {
+            return new HashSet<Capability>();
+        }
+
+        OpenHashMap<Capability, Set<Capability>> sources = resourcePkgMap.get(resource).m_sources;
+        if(sources == null)
+        {
+            return new HashSet<Capability>();
+        }
+
+        Set<Capability> packageSources = sources.get(cap);
+        if(packageSources == null)
+        {
+            return new HashSet<Capability>();
+        }
+
+        return packageSources;
     }
 
     private static void getPackageSourcesInternal(

```