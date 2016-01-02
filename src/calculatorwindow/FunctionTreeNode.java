/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorwindow;

/**
 *
 * @author Will_and_Sara
 */
public class FunctionTreeNode extends javax.swing.tree.DefaultMutableTreeNode{
    private ParseTreeNodes.IDisplayToTreeNode _Node;
    public FunctionTreeNode(ParseTreeNodes.IDisplayToTreeNode Node){
        super(Node.DisplayName(),false);
        _Node = Node;
    }
    public String GetHelpDocument(){
        return _Node.HelpFilePath();
    }
    public String GetSyntax(){
        return _Node.Syntax();
    }
}
