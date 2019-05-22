package org.codehaus.groovy.antlr;

import org.codehaus.groovy.control.ParserPlugin;
import org.codehaus.groovy.control.ParserPluginFactory;

public class AntlrParserPluginFactory extends ParserPluginFactory {
   public ParserPlugin createParserPlugin() {
      return new AntlrParserPlugin();
   }
}
