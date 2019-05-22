package org.codehaus.groovy.antlr.treewalker;

import java.util.ArrayList;
import java.util.Collections;
import org.codehaus.groovy.antlr.GroovySourceAST;

public class SourceCodeTraversal extends TraversalHelper {
   public SourceCodeTraversal(Visitor visitor) {
      super(visitor);
   }

   public void setUp(GroovySourceAST t) {
      super.setUp(t);
      this.unvisitedNodes = new ArrayList();
      this.traverse(t);
      Collections.sort(this.unvisitedNodes);
   }

   private void traverse(GroovySourceAST t) {
      if (t != null) {
         if (this.unvisitedNodes != null) {
            this.unvisitedNodes.add(t);
         }

         GroovySourceAST child = (GroovySourceAST)t.getFirstChild();
         if (child != null) {
            this.traverse(child);
         }

         GroovySourceAST sibling = (GroovySourceAST)t.getNextSibling();
         if (sibling != null) {
            this.traverse(sibling);
         }

      }
   }

   protected void accept(GroovySourceAST currentNode) {
      if (currentNode != null && this.unvisitedNodes != null && this.unvisitedNodes.size() > 0) {
         if (!this.unvisitedNodes.contains(currentNode)) {
            return;
         }

         this.push(currentNode);
         switch(currentNode.getType()) {
         case 5:
         case 64:
         case 117:
         case 142:
         case 146:
         case 148:
            this.accept_v_FirstChild_v_RestOfTheChildren(currentNode);
            break;
         case 6:
         case 7:
         case 8:
         case 9:
         case 20:
         case 27:
         case 28:
         case 59:
         case 61:
            this.accept_v_AllChildren_v(currentNode);
            break;
         case 10:
         case 11:
         case 12:
         case 16:
         case 17:
         case 18:
         case 24:
         case 25:
         case 29:
         case 30:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 48:
         case 50:
         case 51:
         case 52:
         case 54:
         case 55:
         case 56:
         case 57:
         case 62:
         case 68:
         case 70:
         case 75:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 94:
         case 95:
         case 96:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 118:
         case 119:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 128:
         case 129:
         case 130:
         case 131:
         case 133:
         case 138:
         case 139:
         case 140:
         case 141:
         case 145:
         case 147:
         case 152:
         case 155:
         case 156:
         case 169:
         case 178:
         case 179:
         case 186:
         case 189:
         default:
            this.accept_v_FirstChild_v(currentNode);
            break;
         case 13:
         case 14:
         case 15:
         case 21:
         case 45:
         case 53:
         case 60:
         case 63:
         case 86:
         case 87:
         case 97:
         case 98:
         case 99:
         case 109:
         case 110:
         case 121:
         case 137:
         case 143:
         case 144:
         case 149:
         case 150:
         case 151:
         case 170:
         case 171:
         case 172:
         case 173:
         case 181:
         case 182:
         case 183:
         case 184:
         case 185:
         case 187:
         case 188:
         case 190:
            this.accept_FirstChild_v_RestOfTheChildren(currentNode);
            break;
         case 19:
         case 32:
         case 47:
         case 69:
         case 71:
         case 72:
         case 74:
         case 76:
            this.accept_v_FirstChild_v_SecondChild_v___LastChild_v(currentNode);
            break;
         case 22:
         case 134:
            this.accept_v_FirstChildsFirstChild_v_RestOfTheChildren(currentNode);
            break;
         case 23:
            this.accept_SecondChild_v_ThirdChild_v(currentNode);
            break;
         case 26:
         case 44:
            if (currentNode.getNumberOfChildren() == 2 && currentNode.childAt(1) != null && currentNode.childAt(1).getType() == 49) {
               this.accept_FirstChild_v_SecondChild(currentNode);
            } else {
               GroovySourceAST lastChild = currentNode.childAt(currentNode.getNumberOfChildren() - 1);
               if (lastChild != null && lastChild.getType() == 49) {
                  this.accept_FirstChild_v_RestOfTheChildren_v_LastChild(currentNode);
                  break;
               }

               this.accept_FirstChild_v_RestOfTheChildren_v(currentNode);
            }
            break;
         case 31:
         case 153:
            this.accept_FirstChild_v_SecondChildsChildren_v(currentNode);
            break;
         case 46:
            this.accept_v_FirstChild_SecondChild_v_ThirdChild_v(currentNode);
            break;
         case 49:
            if (currentNode.childAt(0) != null && currentNode.childAt(0).getType() == 50) {
               this.accept_v_AllChildren_v(currentNode);
               break;
            }

            this.accept_v_FirstChild_v_RestOfTheChildren_v(currentNode);
            break;
         case 58:
         case 135:
         case 136:
         case 154:
            this.accept_v_FirstChild_v_RestOfTheChildren_v(currentNode);
            break;
         case 65:
            this.accept_v_FirstChild_2ndv_SecondChild_v___LastChild_v(currentNode);
            break;
         case 66:
         case 120:
         case 157:
         case 158:
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 167:
         case 168:
         case 174:
         case 175:
         case 176:
         case 177:
         case 180:
            if (currentNode.childAt(1) != null) {
               this.accept_FirstChild_v_RestOfTheChildren(currentNode);
            } else {
               this.accept_v_FirstChild_v_RestOfTheChildren(currentNode);
            }
            break;
         case 67:
            this.accept_FirstSecondAndThirdChild_v_v_ForthChild(currentNode);
            break;
         case 73:
            this.accept_v_Siblings_v(currentNode);
            break;
         case 93:
            this.accept_FirstChild_v_SecondChild_v_ThirdChild_v(currentNode);
            break;
         case 132:
            this.accept_v_FirstChildsFirstChild_v_Child2_Child3_v_Child4_v___v_LastChild(currentNode);
         }

         this.pop();
      }

   }
}
