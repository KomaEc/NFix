# wicket-5497

## Patch
```diff
--- src/main/java/org/apache/wicket/ajax/json/JsonUtils.java	2019-09-12 21:48:23.114173471 +0800
+++ npe.patch	2019-09-18 23:21:36.509524109 +0800
@@ -58,23 +58,23 @@
 						}
 					}
 				}
-				else if (value.getClass().isArray())
+				else if (value != null)
 				{
-					Object[] array = (Object[]) value;
-					for (Object v : array)
+					if (value.getClass().isArray())
 					{
-						if (v != null)
+						Object[] array = (Object[]) value;
+						for (Object v : array)
 						{
-							JSONObject object = new JSONObject();
-							object.put("name", name);
-							object.put("value", v);
-							jsonArray.put(object);
+							if (v != null)
+							{
+								JSONObject object = new JSONObject();
+								object.put("name", name);
+								object.put("value", v);
+								jsonArray.put(object);
+							}
 						}
 					}
-				}
-				else
-				{
-					if (value != null)
+					else
 					{
 						JSONObject object = new JSONObject();
 						object.put("name", name);
@@ -87,4 +87,4 @@
 
 		return jsonArray;
 	}
-}
+}
\ No newline at end of file
```