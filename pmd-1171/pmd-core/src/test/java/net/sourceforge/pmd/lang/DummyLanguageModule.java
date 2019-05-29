/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
package net.sourceforge.pmd.lang;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.lang.ast.DummyNode;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.ast.ParseException;
import net.sourceforge.pmd.lang.rule.AbstractRuleChainVisitor;
import net.sourceforge.pmd.lang.rule.AbstractRuleViolationFactory;
import net.sourceforge.pmd.lang.rule.ParametricRuleViolation;


/**
 * Dummy language used for testing PMD.
 */
public class DummyLanguageModule extends BaseLanguageModule {

    public static final String NAME = "Dummy";
    public static final String TERSE_NAME = "dummy";

    public DummyLanguageModule() {
        super(NAME, null, TERSE_NAME, DummyRuleChainVisitor.class, "dummy");
        addVersion("1.0", new Handler(), true);
        addVersion("1.1", new Handler(), false);
        addVersion("1.2", new Handler(), false);
        addVersion("1.3", new Handler(), false);
        addVersion("1.4", new Handler(), false);
        addVersion("1.5", new Handler(), false);
        addVersion("1.6", new Handler(), false);
        addVersion("1.7", new Handler(), false);
        addVersion("1.8", new Handler(), false);
    }

    public static class DummyRuleChainVisitor extends AbstractRuleChainVisitor {
        @Override
        protected void visit(Rule rule, Node node, RuleContext ctx) {
            rule.apply(Arrays.asList(node), ctx);
        }
        @Override
        protected void indexNodes(List<Node> nodes, RuleContext ctx) {
            for (Node n : nodes) {
                indexNode(n);
                List<Node> childs = new ArrayList<Node>();
                for (int i = 0; i < n.jjtGetNumChildren(); i++) {
                    childs.add(n.jjtGetChild(i));
                }
                indexNodes(childs, ctx);
            }
        }
    }
    
    public static class Handler extends AbstractLanguageVersionHandler {
        @Override
        public RuleViolationFactory getRuleViolationFactory() {
            return new RuleViolationFactory();
        }
        
        @Override
        public Parser getParser(ParserOptions parserOptions) {
            return new AbstractParser(parserOptions) {
                @Override
                public Node parse(String fileName, Reader source) throws ParseException {
                    DummyNode node = new DummyNode(1);
                    node.testingOnly__setBeginLine(1);
                    node.testingOnly__setBeginColumn(1);
                    node.setImage("Foo");
                    return node;
                }
                @Override
                public Map<Integer, String> getSuppressMap() {
                    return Collections.emptyMap();
                }
                @Override
                public boolean canParse() {
                    return true;
                }
                @Override
                protected TokenManager createTokenManager(Reader source) {
                    return null;
                }
            };
        }
    }

    public static class RuleViolationFactory extends AbstractRuleViolationFactory {
        @Override
        protected RuleViolation createRuleViolation(Rule rule, RuleContext ruleContext, Node node, String message) {
            return createRuleViolation(rule, ruleContext, node, message, 0, 0);
        }
        @Override
        protected RuleViolation createRuleViolation(Rule rule, RuleContext ruleContext, Node node, String message,
                int beginLine, int endLine) {
            ParametricRuleViolation<Node> rv = new ParametricRuleViolation<Node>(rule, ruleContext, node, message) {
                {
                    this.packageName = "foo"; // just for testing variable expansion
                }
            };
            rv.setLines(beginLine, endLine);
            return rv;
        }
    }
}
