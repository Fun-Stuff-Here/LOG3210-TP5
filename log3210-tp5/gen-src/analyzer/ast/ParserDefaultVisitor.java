/* Generated By:JavaCC: Do not edit this line. ParserDefaultVisitor.java Version 7.0.2 */
package analyzer.ast;

public class ParserDefaultVisitor implements ParserVisitor{
  public Object defaultVisit(SimpleNode node, Object data){
    node.childrenAccept(this, data);
    return data;
  }
  public Object visit(SimpleNode node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTProgram node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTNumberRegister node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTLive node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTLiveNode node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTInNode node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTOutNode node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTBlock node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTStmt node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTAssignStmt node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTAssignUnaryStmt node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTAssignDirectStmt node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTExpr node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTIdentifier node, Object data){
    return defaultVisit(node, data);
  }
  public Object visit(ASTIntValue node, Object data){
    return defaultVisit(node, data);
  }
}
/* JavaCC - OriginalChecksum=c107a5f89f6ede3f37aea579271f8644 (do not edit this line) */
