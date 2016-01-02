/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorwindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
       renderer.setOpenIcon(null);
       renderer.setClosedIcon(null);
       this.setCellRenderer(renderer);
       this.addMouseListener(new FunctionTreeMouseListener(this));
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
                        boolean contains = false;
                        int index = 0;
                        String Path = D.getClass().getPackage().getName();
                        Path = Path.replace(".", " ");
                        String[] pathparts = Path.split(" ");
                        String parentName = pathparts[pathparts.length-1];
                        for(int i = 0; i<root.getChildCount();i++){
                            if(root.getChildAt(i).toString().equals(parentName)){contains = true;index = i;}
                        }
                        if(contains){
                            javax.swing.tree.DefaultMutableTreeNode parentnode = (javax.swing.tree.DefaultMutableTreeNode)root.getChildAt(index);
                            parentnode.add(node);

                        }else{
                            javax.swing.tree.DefaultMutableTreeNode parentnode = new javax.swing.tree.DefaultMutableTreeNode(parentName,true);
                            parentnode.add(node);
                            root.add(parentnode);
                        }
                    } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        Logger.getLogger(FunctionTree.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 
            }
        }
        return new javax.swing.tree.DefaultTreeModel(root, true);
    }
    private class FunctionTreeMouseListener implements MouseListener{
        private javax.swing.JTree _Tree;
        public FunctionTreeMouseListener(javax.swing.JTree Tree){
            _Tree = Tree;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount()==2 && javax.swing.SwingUtilities.isLeftMouseButton(e)){
                //raise event to notify window that text needs to be inserted where the carat position was last captured. 
                int row = _Tree.getClosestRowForLocation(e.getX(), e.getY());
                _Tree.setSelectionRow(row);
                System.out.println(row);
                System.out.println(e.getComponent());
                //_Tree.popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.isPopupTrigger()){
                //make a pop up based on the tree node.
            }
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger()){
                //make a pop up based on the tree node.
            }
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        @Override
        public void mouseExited(MouseEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
