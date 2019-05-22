package groovyjarjarantlr;

public interface TokenStream {
   Token nextToken() throws TokenStreamException;
}
