package org.codehaus.groovy.control;

import org.codehaus.groovy.antlr.AntlrParserPluginFactory;

public abstract class ParserPluginFactory {
   public static ParserPluginFactory newInstance(boolean useNewParser) {
      if (useNewParser) {
         Class type = null;
         String name = "org.codehaus.groovy.antlr.AntlrParserPluginFactory";

         try {
            type = Class.forName(name);
         } catch (ClassNotFoundException var10) {
            try {
               type = ParserPluginFactory.class.getClassLoader().loadClass(name);
            } catch (ClassNotFoundException var9) {
               ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
               if (contextClassLoader != null) {
                  try {
                     type = contextClassLoader.loadClass(name);
                  } catch (ClassNotFoundException var8) {
                  }
               }
            }
         }

         if (type != null) {
            try {
               return (ParserPluginFactory)type.newInstance();
            } catch (Exception var7) {
               throw new RuntimeException("Could not create AntlrParserPluginFactory: " + var7, var7);
            }
         }
      }

      return new AntlrParserPluginFactory();
   }

   public abstract ParserPlugin createParserPlugin();
}
