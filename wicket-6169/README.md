# wicket-6169

## Commit
809b2da16cc0796ee4e5aca8ef816c200c01baf2

Parent : e1157cb783cb82d791bdf97869d4924bd369a1f0

## Patch
```diff
--- ./src/main/java/org/apache/wicket/protocol/http/AbstractRequestLogger.java	2019-06-20 11:05:10.076815516 +0800
+++ patch.txt	2019-06-20 11:12:19.180697266 +0800
@@ -134,8 +134,7 @@
 	@Override
 	public SessionData[] getLiveSessions()
 	{
-		final SessionData[] sessions = liveSessions.values().toArray(
-			new SessionData[liveSessions.values().size()]);
+		final SessionData[] sessions = liveSessions.values().toArray(new SessionData[0]);
 		Arrays.sort(sessions);
 		return sessions;
 	}

```

## pom
change the version from 8.0.0-SNAPSHOT to 8.0.0
```xml
<dependency>
	<groupId>org.apache.wicket</groupId>
	<artifactId>wicket-request</artifactId>
	<version>8.0.0</version>
</dependency>
<dependency>
	<groupId>org.apache.wicket</groupId>
	<artifactId>wicket-util</artifactId>
	<version>8.0.0</version>
</dependency>
```