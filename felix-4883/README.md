# felix-4883

## Patch
```diff
--- src/main/java/org/apache/felix/scr/impl/runtime/ServiceComponentRuntimeImpl.java	2019-07-24 15:16:47.724000695 +0800
+++ npe.patch	2019-07-24 15:21:08.103242506 +0800
@@ -200,7 +200,12 @@
 	        return null;
 
 		ServiceReferenceDTO dto = new ServiceReferenceDTO();
-		dto.bundle = serviceRef.getBundle().getBundleId();
+		Bundle bundle = serviceRef.getBundle();
+		if (bundle != null)
+		    dto.bundle = bundle.getBundleId();
+		else
+		    dto.bundle = -1; // No bundle ever has -1 as ID, so this indicates no bundle.
+
 		dto.id = (Long) serviceRef.getProperty(Constants.SERVICE_ID);
 		dto.properties = deepCopy( serviceRef );
 		Bundle[] usingBundles = serviceRef.getUsingBundles();
@@ -350,4 +355,4 @@
 		b.version = bundle.getVersion().toString();
 		return b;
 	}
-}
+}
\ No newline at end of file
```