package org.codehaus.groovy.ast.expr;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.GroovyCodeVisitor;

public class MapExpression extends Expression {
   private final List<MapEntryExpression> mapEntryExpressions;

   public MapExpression() {
      this(new ArrayList());
   }

   public MapExpression(List<MapEntryExpression> mapEntryExpressions) {
      this.mapEntryExpressions = mapEntryExpressions;
      this.setType(ClassHelper.MAP_TYPE);
   }

   public void addMapEntryExpression(MapEntryExpression expression) {
      this.mapEntryExpressions.add(expression);
   }

   public List<MapEntryExpression> getMapEntryExpressions() {
      return this.mapEntryExpressions;
   }

   public void visit(GroovyCodeVisitor visitor) {
      visitor.visitMapExpression(this);
   }

   public boolean isDynamic() {
      return false;
   }

   public Expression transformExpression(ExpressionTransformer transformer) {
      Expression ret = new MapExpression(this.transformExpressions(this.getMapEntryExpressions(), transformer, MapEntryExpression.class));
      ret.setSourcePosition(this);
      return ret;
   }

   public String toString() {
      return super.toString() + this.mapEntryExpressions;
   }

   public String getText() {
      StringBuffer sb = new StringBuffer(32);
      sb.append("[");
      int size = this.mapEntryExpressions.size();
      MapEntryExpression mapEntryExpression = null;
      if (size > 0) {
         mapEntryExpression = (MapEntryExpression)this.mapEntryExpressions.get(0);
         sb.append(mapEntryExpression.getKeyExpression().getText() + ":" + mapEntryExpression.getValueExpression().getText());

         for(int i = 1; i < size; ++i) {
            mapEntryExpression = (MapEntryExpression)this.mapEntryExpressions.get(i);
            sb.append(", " + mapEntryExpression.getKeyExpression().getText() + ":" + mapEntryExpression.getValueExpression().getText());
            if (sb.length() > 120 && i < size - 1) {
               sb.append(", ... ");
               break;
            }
         }
      }

      sb.append("]");
      return sb.toString();
   }

   public void addMapEntryExpression(Expression keyExpression, Expression valueExpression) {
      this.addMapEntryExpression(new MapEntryExpression(keyExpression, valueExpression));
   }
}
