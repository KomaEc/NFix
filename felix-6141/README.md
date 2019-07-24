# felix-6141

## Patch
```diff
--- src/main/java/org/osgi/util/converter/ConvertingImpl.java	2019-07-24 16:28:23.314887188 +0800
+++ npe.patch	2019-07-24 16:31:49.610796513 +0800
@@ -265,9 +265,9 @@
 			return c.convert(entry.getKey()).to(cls);
 		} else if (cls.equals(valueCls)) {
 			return c.convert(entry.getValue()).to(cls);
-		} else if (cls.isAssignableFrom(keyCls)) {
+		} else if (keyCls != null && cls.isAssignableFrom(keyCls)) {
 			return c.convert(entry.getKey()).to(cls);
-		} else if (cls.isAssignableFrom(valueCls)) {
+		} else if (valueCls != null && cls.isAssignableFrom(valueCls)) {
 			return c.convert(entry.getValue()).to(cls);
 		} else if (entry.getKey() instanceof String) {
 			return c.convert(entry.getKey()).to(cls);
@@ -1293,4 +1293,4 @@
 		}
 		return setters;
 	}
-}
+}
\ No newline at end of file
```