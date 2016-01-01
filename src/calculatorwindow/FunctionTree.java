/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorwindow;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Will_and_Sara
 */
public class FunctionTree extends javax.swing.JTree{
    public FunctionTree(){
       super(CreateRoot());
       this.setRootVisible(false);
       javax.swing.tree.DefaultTreeCellRenderer renderer = new javax.swing.tree.DefaultTreeCellRenderer();
       renderer.setLeafIcon(null);
       this.setCellRenderer(renderer);
    }
    private static javax.swing.tree.DefaultTreeModel CreateRoot(){
        List<Class<?>> clazzes = Reflector.getClassesForPackage(ParseTreeNodes.ParseTreeNode.class.getPackage());
        ParseTreeNodes.IDisplayToTreeNode D;
        javax.swing.tree.DefaultMutableTreeNode root = new javax.swing.tree.DefaultMutableTreeNode("ROOT",true);
        javax.swing.tree.DefaultMutableTreeNode node;
        for(Class<?> C : clazzes){
            for(Class<?> I : C.getInterfaces()){
                if(I.getName().equals("ParseTreeNodes.IDisplayToTreeNode")){
                    try {
                        D = (ParseTreeNodes.IDisplayToTreeNode)C.getConstructor().newInstance();
                        node = new javax.swing.tree.DefaultMutableTreeNode(D.DisplayName(),false);
                        root.add(node);
                    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(FunctionTree.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } 
            }
        }
        return new javax.swing.tree.DefaultTreeModel(root, true);
    }
}
