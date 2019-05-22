package groovy.sql;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.expr.BinaryExpression;
import org.codehaus.groovy.ast.expr.BooleanExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.PropertyExpression;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.syntax.Token;

public class SqlWhereVisitor extends CodeVisitorSupport {
   private final StringBuffer buffer = new StringBuffer();
   private final List parameters = new ArrayList();

   public String getWhere() {
      return this.buffer.toString();
   }

   public void visitReturnStatement(ReturnStatement statement) {
      statement.getExpression().visit(this);
   }

   public void visitBinaryExpression(BinaryExpression expression) {
      Expression left = expression.getLeftExpression();
      Expression right = expression.getRightExpression();
      boolean leaf = right instanceof ConstantExpression;
      if (!leaf) {
         this.buffer.append("(");
      }

      left.visit(this);
      this.buffer.append(" ");
      Token token = expression.getOperation();
      this.buffer.append(this.tokenAsSql(token));
      this.buffer.append(" ");
      right.visit(this);
      if (!leaf) {
         this.buffer.append(")");
      }

   }

   public void visitBooleanExpression(BooleanExpression expression) {
      expression.getExpression().visit(this);
   }

   public void visitConstantExpression(ConstantExpression expression) {
      this.getParameters().add(expression.getValue());
      this.buffer.append("?");
   }

   public void visitPropertyExpression(PropertyExpression expression) {
      this.buffer.append(expression.getPropertyAsString());
   }

   public List getParameters() {
      return this.parameters;
   }

   protected String tokenAsSql(Token token) {
      switch(token.getType()) {
      case 123:
         return "=";
      case 162:
         return "or";
      case 164:
         return "and";
      default:
         return token.getText();
      }
   }
}
