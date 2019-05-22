package groovy.sql;

import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.stmt.ReturnStatement;

public class SqlOrderByVisitor extends CodeVisitorSupport {
   private StringBuffer buffer = new StringBuffer();

   public String getOrderBy() {
      return this.buffer.toString();
   }

   public void visitReturnStatement(ReturnStatement statement) {
      statement.getExpression().visit(this);
   }

   public void visitPropertyExpression(PropertyExpression expression) {
      this.buffer.append(expression.getPropertyAsString());
   }
}
