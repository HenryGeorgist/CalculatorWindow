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
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Will_and_Sara
 */
public class FunctionTree extends javax.swing.JTree implements java.util.Observer{
    public FunctionTree(){
       super(CreateRoot());
       this.setRootVisible(false);
       javax.swing.tree.DefaultTreeCellRenderer renderer = new javax.swing.tree.DefaultTreeCellRenderer();
       renderer.setLeafIcon(null);
       renderer.setOpenIcon(null);
       renderer.setClosedIcon(null);
       this.setCellRenderer(renderer);
       FunctionTreeMouseListener FTML = new FunctionTreeMouseListener(this);
       FTML.addObserver(this);
       this.addMouseListener(FTML);
    }
    private static javax.swing.tree.DefaultTreeModel CreateRoot(){
        List<Class<?>> clazzes = Reflector.getClassesForPackage(ParseTreeNodes.ParseTreeNode.class.getPackage());
        ParseTreeNodes.IDisplayToTreeNode D;
        javax.swing.tree.DefaultMutableTreeNode root = new javax.swing.tree.DefaultMutableTreeNode("ROOT",true);
        FunctionTreeNode node;
        for(Class<?> C : clazzes){
            for(Class<?> I : C.getInterfaces()){
                if(I.getName().equals("ParseTreeNodes.IDisplayToTreeNode")){
                    try {
                        D = (ParseTreeNodes.IDisplayToTreeNode)C.getConstructor().newInstance();
                        node = new FunctionTreeNode(D);
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

    @Override
    public void update(Observable o, Object arg) {
        //check to make sure it is a FunctionTreeNode, and then redirect with a property changed listener?
        if(arg.getClass().getName().equals("calculatorwindow.FunctionTreeNode")){
            FunctionTreeNode FTN = (FunctionTreeNode)arg;
            this.firePropertyChange("TextToInsert", "", FTN.GetSyntax());
        }
    }
    private class FunctionTreeMouseListener extends java.util.Observable implements MouseListener {
        private javax.swing.JTree _Tree;
        public FunctionTreeMouseListener(javax.swing.JTree Tree){
            _Tree = Tree;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount()==2 && javax.swing.SwingUtilities.isLeftMouseButton(e)){
                //raise event to notify window that text needs to be inserted where the carat position was last captured. 
                javax.swing.tree.TreePath Path = _Tree.getPathForLocation(e.getX(), e.getY());
                Object bla = Path.getLastPathComponent();
                if(bla.getClass().getName().equals("calculatorwindow.FunctionTreeNode")){
                    FunctionTreeNode FTN = (FunctionTreeNode)Path.getLastPathComponent();
                    this.setChanged();
                    this.notifyObservers(FTN);
                }
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.isPopupTrigger()){
                //make a pop up based on the tree node.
                javax.swing.tree.TreePath Path = _Tree.getPathForLocation(e.getX(), e.getY());
                Object bla = Path.getLastPathComponent();
                if(bla.getClass().getName().equals("calculatorwindow.FunctionTreeNode")){
                    FunctionTreeNode FTN = (FunctionTreeNode)Path.getLastPathComponent();
                    javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
                    javax.swing.JMenuItem MI =new javax.swing.JMenuItem("Get Help for: " + FTN.GetDisplayName());
                    //MI.addMouseListener(l);
                    popup.add(MI);
                    popup.show(_Tree,e.getX(),e.getY());
                }
            }
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.isPopupTrigger()){
                //make a pop up based on the tree node.
                javax.swing.tree.TreePath Path = _Tree.getPathForLocation(e.getX(), e.getY());
                Object bla = Path.getLastPathComponent();
                if(bla.getClass().getName().equals("calculatorwindow.FunctionTreeNode")){
                    FunctionTreeNode FTN = (FunctionTreeNode)Path.getLastPathComponent();
                    javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
                    popup.add(new javax.swing.JMenuItem("Get Help for: " + FTN.GetDisplayName()));
                    popup.show(_Tree,e.getX(),e.getY());
                }
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
