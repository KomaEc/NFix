package soot.JastAddJ;

import beaver.Parser;
import java.io.IOException;
import java.io.InputStream;

public interface JavaParser {
   CompilationUnit parse(InputStream var1, String var2) throws IOException, Parser.Exception;
}
