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
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Will_and_Sara
 */
public class FunctionTree extends javax.swing.JTree{
    public FunctionTree(javax.swing.tree.DefaultMutableTreeNode node, boolean askallowchildren){
        super(node,askallowchildren);
    }
    public FunctionTree(){
        List<Class<?>> clazzes = Reflector.getClassesForPackage(ParseTreeNodes.ParseTreeNode.class.getPackage());
        ParseTreeNodes.IDisplayToTreeNode D;
        for(Class<?> C : clazzes){
            for(Class<?> I : C.getInterfaces()){
                if(I.getName().equals("ParseTreeNodes.IDisplayToTreeNode")){
                    try {
                        D = (ParseTreeNodes.IDisplayToTreeNode)C.getConstructor().newInstance();
                        System.out.println(D.DisplayName());
                        javax.swing.tree.DefaultMutableTreeNode root = new javax.swing.tree.DefaultMutableTreeNode(D.DisplayName());
                        super.setRootVisible(true);
                        super.updateUI();
                        super.setModel(new DefaultTreeModel(root,true));
                    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(FunctionTree.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } 
            }
        }
    }
}
