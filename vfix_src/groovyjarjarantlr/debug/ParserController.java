package groovyjarjarantlr.debug;

public interface ParserController extends ParserListener {
   void checkBreak();

   void setParserEventSupport(ParserEventSupport var1);
}
