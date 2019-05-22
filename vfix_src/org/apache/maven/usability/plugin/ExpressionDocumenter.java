package org.apache.maven.usability.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.maven.usability.plugin.io.xpp3.ParamdocXpp3Reader;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class ExpressionDocumenter {
   private static final String[] EXPRESSION_ROOTS = new String[]{"project", "settings", "session", "plugin", "rootless"};
   private static final String EXPRESSION_DOCO_ROOTPATH = "META-INF/maven/plugin-expressions/";
   private static Map expressionDocumentation;

   public static Map load() throws ExpressionDocumentationException {
      if (expressionDocumentation == null) {
         expressionDocumentation = new HashMap();
         ClassLoader docLoader = initializeDocLoader();

         for(int i = 0; i < EXPRESSION_ROOTS.length; ++i) {
            InputStream docStream = null;

            try {
               docStream = docLoader.getResourceAsStream("META-INF/maven/plugin-expressions/" + EXPRESSION_ROOTS[i] + ".paramdoc.xml");
               if (docStream != null) {
                  Map doco = parseExpressionDocumentation(docStream);
                  expressionDocumentation.putAll(doco);
               }
            } catch (IOException var8) {
               throw new ExpressionDocumentationException("Failed to read documentation for expression root: " + EXPRESSION_ROOTS[i], var8);
            } catch (XmlPullParserException var9) {
               throw new ExpressionDocumentationException("Failed to parse documentation for expression root: " + EXPRESSION_ROOTS[i], var9);
            } finally {
               IOUtil.close(docStream);
            }
         }
      }

      return expressionDocumentation;
   }

   private static Map parseExpressionDocumentation(InputStream docStream) throws IOException, XmlPullParserException {
      Reader reader = new BufferedReader(new InputStreamReader(docStream));
      ParamdocXpp3Reader paramdocReader = new ParamdocXpp3Reader();
      ExpressionDocumentation documentation = paramdocReader.read(reader, true);
      List expressions = documentation.getExpressions();
      Map bySyntax = new HashMap();
      if (expressions != null && !expressions.isEmpty()) {
         Iterator it = expressions.iterator();

         while(it.hasNext()) {
            Expression expr = (Expression)it.next();
            bySyntax.put(expr.getSyntax(), expr);
         }
      }

      return bySyntax;
   }

   private static ClassLoader initializeDocLoader() throws ExpressionDocumentationException {
      String myResourcePath = ExpressionDocumenter.class.getName().replace('.', '/') + ".class";
      URL myResource = ExpressionDocumenter.class.getClassLoader().getResource(myResourcePath);
      String myClasspathEntry = myResource.getPath();
      myClasspathEntry = myClasspathEntry.substring(0, myClasspathEntry.length() - (myResourcePath.length() + 2));
      if (myClasspathEntry.startsWith("file:")) {
         myClasspathEntry = myClasspathEntry.substring("file:".length());
      }

      URL docResource;
      try {
         docResource = (new File(myClasspathEntry)).toURL();
      } catch (MalformedURLException var5) {
         throw new ExpressionDocumentationException("Cannot construct expression documentation classpath resource base.", var5);
      }

      return new URLClassLoader(new URL[]{docResource});
   }
}
