package org.apache.maven.doxia.macro.snippet;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.apache.maven.doxia.macro.AbstractMacro;
import org.apache.maven.doxia.macro.MacroExecutionException;
import org.apache.maven.doxia.macro.MacroRequest;
import org.apache.maven.doxia.sink.Sink;
import org.codehaus.plexus.util.StringUtils;

public class SnippetMacro extends AbstractMacro {
   static final String EOL = System.getProperty("line.separator");
   private Map cache = new HashMap();
   private long timeout = 3600000L;
   private Map timeCached = new HashMap();
   private boolean debug = false;

   public void execute(Sink sink, MacroRequest request) throws MacroExecutionException {
      String id = (String)request.getParameter("id");
      this.required(id, "id");
      String urlParam = (String)request.getParameter("url");
      String fileParam = (String)request.getParameter("file");
      URL var6;
      if (!StringUtils.isEmpty(urlParam)) {
         try {
            var6 = new URL(urlParam);
         } catch (MalformedURLException var11) {
            throw new IllegalArgumentException(urlParam + " is a malformed URL");
         }
      } else {
         if (StringUtils.isEmpty(fileParam)) {
            throw new IllegalArgumentException("Either the 'url' or the 'file' param has to be given.");
         }

         File f = new File(fileParam);
         if (!f.isAbsolute()) {
            f = new File(request.getBasedir(), fileParam);
         }

         try {
            var6 = f.toURL();
         } catch (MalformedURLException var10) {
            throw new IllegalArgumentException(urlParam + " is a malformed URL");
         }
      }

      StringBuffer snippet;
      try {
         snippet = this.getSnippet(var6, id);
      } catch (IOException var9) {
         throw new MacroExecutionException("Error reading snippet", var9);
      }

      sink.verbatim(true);
      sink.text(snippet.toString());
      sink.verbatim_();
   }

   private StringBuffer getSnippet(URL url, String id) throws IOException {
      String cachedSnippet = (String)this.getCachedSnippet(url, id);
      StringBuffer result;
      if (cachedSnippet != null) {
         result = new StringBuffer(cachedSnippet);
         if (this.debug) {
            result.append("(Served from cache)");
         }
      } else {
         result = (new SnippetReader(url)).readSnippet(id);
         this.cacheSnippet(url, id, result.toString());
         if (this.debug) {
            result.append("(Fetched from url, cache content ").append(this.cache).append(")");
         }
      }

      return result;
   }

   private Object getCachedSnippet(URL url, String id) {
      if (this.isCacheTimedout(url, id)) {
         this.removeFromCache(url, id);
      }

      return this.cache.get(this.globalSnippetId(url, id));
   }

   boolean isCacheTimedout(URL url, String id) {
      return this.timeInCache(url, id) >= this.timeout;
   }

   long timeInCache(URL url, String id) {
      return System.currentTimeMillis() - this.getTimeCached(url, id);
   }

   long getTimeCached(URL url, String id) {
      String globalId = this.globalSnippetId(url, id);
      return this.timeCached.containsKey(globalId) ? (Long)this.timeCached.get(globalId) : 0L;
   }

   private void removeFromCache(URL url, String id) {
      String globalId = this.globalSnippetId(url, id);
      this.timeCached.remove(globalId);
      this.cache.remove(globalId);
   }

   private String globalSnippetId(URL url, String id) {
      return url + " " + id;
   }

   private void required(String id, String param) {
      if (id == null || "".equals(id)) {
         throw new IllegalArgumentException(param + " is a required parameter");
      }
   }

   public void cacheSnippet(URL url, String id, String content) {
      this.cache.put(this.globalSnippetId(url, id), content);
      this.timeCached.put(this.globalSnippetId(url, id), new Long(System.currentTimeMillis()));
   }

   public void setCacheTimeout(int time) {
      this.timeout = (long)time;
   }
}
