/* Generated By:JavaCC: Do not edit this line. ParserVisitor.java Version 7.0.2 */
package analyzer.ast;

public interface ParserVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTProgram node, Object data);
  public Object visit(ASTNumberRegister node, Object data);
  public Object visit(ASTLive node, Object data);
  public Object visit(ASTLiveNode node, Object data);
  public Object visit(ASTInNode node, Object data);
  public Object visit(ASTOutNode node, Object data);
  public Object visit(ASTBlock node, Object data);
  public Object visit(ASTStmt node, Object data);
  public Object visit(ASTAssignStmt node, Object data);
  public Object visit(ASTAssignUnaryStmt node, Object data);
  public Object visit(ASTAssignDirectStmt node, Object data);
  public Object visit(ASTExpr node, Object data);
  public Object visit(ASTIdentifier node, Object data);
  public Object visit(ASTIntValue node, Object data);
}
/* JavaCC - OriginalChecksum=12d72eed012004f3ab91b0f7df30ece1 (do not edit this line) */
