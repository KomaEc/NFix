/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package net.sourceforge.pmd.lang.java.symboltable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTBlock;
import net.sourceforge.pmd.lang.java.ast.ASTCatchStatement;
import net.sourceforge.pmd.lang.java.ast.ASTEqualityExpression;
import net.sourceforge.pmd.lang.java.ast.ASTInitializer;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.symboltable.TypedNameDeclaration;
import net.sourceforge.pmd.lang.symboltable.NameDeclaration;
import net.sourceforge.pmd.lang.symboltable.NameOccurrence;
import net.sourceforge.pmd.lang.symboltable.Scope;

import org.junit.Test;
public class AcceptanceTest extends STBBaseTst {


    /**
     * Unit test for bug #1490
     *
     * @see <a href="https://sourceforge.net/p/pmd/bugs/1490/">#1490 [java] PMD Error while processing - NullPointerException</a>
     */
    @Test
    public void testNullPointerEnumValueOfOverloaded() {
        parseCode("public enum EsmDcVoltageSensor {\n"
                + "    A;\n"
                + "    void bar(int ... args) {\n"
                + "        int idx;\n"
                + "        int startIdx;\n"
                + "        String name = EsmDcVoltageSensor.valueOf((byte) (idx - startIdx)).getName();\n"
                + "    }\n"
                + "    public EsmDCVoltageSensor valueOf(byte b) {\n" // that's the overloaded method
                + "    }\n"
                + "}\n");
    }

    private static final String TEST_DEMO =
            "public class Foo  {" + PMD.EOL +
            " void bar(ArrayList buz) { " + PMD.EOL +
            "  buz.add(\"foo\");" + PMD.EOL +
            " } " + PMD.EOL +
            "}" + PMD.EOL;

    private static final String TEST_EQ =
            "public class Foo  {" + PMD.EOL +
            " boolean foo(String a, String b) { " + PMD.EOL +
            "  return a == b; " + PMD.EOL +
            " } " + PMD.EOL +
            "}" + PMD.EOL;

    private static final String TEST1 =
            "import java.io.*;" + PMD.EOL +
            "public class Foo  {" + PMD.EOL +
            " void buz( ) {" + PMD.EOL +
            "  Object o = new Serializable() { int x; };" + PMD.EOL +
            "  Object o1 = new Serializable() { int x; };" + PMD.EOL +
            " }" + PMD.EOL +
            "}" + PMD.EOL;

    private static final String TEST_INITIALIZERS =
            "public class Foo  {" + PMD.EOL +
            " {} " + PMD.EOL +
            " static {} " + PMD.EOL +
            "}" + PMD.EOL;

    private static final String TEST_CATCH_BLOCKS =
            "public class Foo  {" + PMD.EOL +
            " void foo() { " + PMD.EOL +
            "  try { " + PMD.EOL +
            "  } catch (Exception e) { " + PMD.EOL +
            "   e.printStackTrace(); " + PMD.EOL +
            "  } " + PMD.EOL +
            " } " + PMD.EOL +
            "}" + PMD.EOL;

    private static final String TEST_FIELD =
            "public class MyClass {" + PMD.EOL +
            " private int aaaaaaaaaa; " + PMD.EOL +
            " boolean bbbbbbbbbb = MyClass.ASCENDING; " + PMD.EOL +
            " private int zzzzzzzzzz;" + PMD.EOL +
            " private void doIt() {" + PMD.EOL +
            "  if (bbbbbbbbbb) {" + PMD.EOL +
            "  }" + PMD.EOL +
            " }" + PMD.EOL +
            "}" + PMD.EOL;

    public static final String TEST_INNER_CLASS =
            "public class Outer {" + PMD.EOL +
            "  private static class Inner {" + PMD.EOL +
            "    private int i;" + PMD.EOL +
            "    private Inner(int i) {" + PMD.EOL +
            "      this.i = i;" + PMD.EOL +
            "    }" + PMD.EOL +
            "  }" + PMD.EOL +
            "  public int modify(int i) {" + PMD.EOL +
            "    Inner in = new Inner(i);" + PMD.EOL +
            "    return in.i;" + PMD.EOL +
            "  }" + PMD.EOL +
            "}" + PMD.EOL;

    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(AcceptanceTest.class);
    }
}
