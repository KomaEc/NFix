package org.testng.xml;

import java.io.InputStream;
import org.testng.TestNGException;

public interface IFileParser<T> {
   T parse(String var1, InputStream var2, boolean var3) throws TestNGException;
}
