package groovyjarjarcommonscli;

public class BasicParser extends Parser {
   protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
      return arguments;
   }
}
