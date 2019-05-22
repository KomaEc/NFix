package org.apache.velocity.runtime.parser;

public interface ParserTreeConstants {
   int JJTPROCESS = 0;
   int JJTVOID = 1;
   int JJTESCAPEDDIRECTIVE = 2;
   int JJTESCAPE = 3;
   int JJTCOMMENT = 4;
   int JJTFLOATINGPOINTLITERAL = 5;
   int JJTINTEGERLITERAL = 6;
   int JJTSTRINGLITERAL = 7;
   int JJTIDENTIFIER = 8;
   int JJTWORD = 9;
   int JJTDIRECTIVE = 10;
   int JJTBLOCK = 11;
   int JJTMAP = 12;
   int JJTOBJECTARRAY = 13;
   int JJTINTEGERRANGE = 14;
   int JJTMETHOD = 15;
   int JJTREFERENCE = 16;
   int JJTTRUE = 17;
   int JJTFALSE = 18;
   int JJTTEXT = 19;
   int JJTIFSTATEMENT = 20;
   int JJTELSESTATEMENT = 21;
   int JJTELSEIFSTATEMENT = 22;
   int JJTSETDIRECTIVE = 23;
   int JJTSTOP = 24;
   int JJTEXPRESSION = 25;
   int JJTASSIGNMENT = 26;
   int JJTORNODE = 27;
   int JJTANDNODE = 28;
   int JJTEQNODE = 29;
   int JJTNENODE = 30;
   int JJTLTNODE = 31;
   int JJTGTNODE = 32;
   int JJTLENODE = 33;
   int JJTGENODE = 34;
   int JJTADDNODE = 35;
   int JJTSUBTRACTNODE = 36;
   int JJTMULNODE = 37;
   int JJTDIVNODE = 38;
   int JJTMODNODE = 39;
   int JJTNOTNODE = 40;
   String[] jjtNodeName = new String[]{"process", "void", "EscapedDirective", "Escape", "Comment", "FloatingPointLiteral", "IntegerLiteral", "StringLiteral", "Identifier", "Word", "Directive", "Block", "Map", "ObjectArray", "IntegerRange", "Method", "Reference", "True", "False", "Text", "IfStatement", "ElseStatement", "ElseIfStatement", "SetDirective", "Stop", "Expression", "Assignment", "OrNode", "AndNode", "EQNode", "NENode", "LTNode", "GTNode", "LENode", "GENode", "AddNode", "SubtractNode", "MulNode", "DivNode", "ModNode", "NotNode"};
}
