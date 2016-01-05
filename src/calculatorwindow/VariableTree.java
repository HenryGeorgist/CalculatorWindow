/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorwindow;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
/**
 *
 * @author Will_and_Sara
 */
public class VariableTree extends javax.swing.JTree implements java.util.Observer{
    public VariableTree(String[] OrderedVariableNames){
       super(CreateRoot(OrderedVariableNames));
       this.setRootVisible(false);
       javax.swing.tree.DefaultTreeCellRenderer renderer = new javax.swing.tree.DefaultTreeCellRenderer();
       renderer.setLeafIcon(null);
       renderer.setOpenIcon(null);
       renderer.setClosedIcon(null);
       this.setCellRenderer(renderer);
       VariableTreeMouseListener VTML = new VariableTreeMouseListener(this);
       VTML.addObserver(this);
       this.addMouseListener(VTML);
    }
    private static javax.swing.tree.DefaultTreeModel CreateRoot(String[] VariableNames){
        javax.swing.tree.DefaultMutableTreeNode root = new javax.swing.tree.DefaultMutableTreeNode("ROOT",true);
        
        for(String s : VariableNames){
            root.add(new javax.swing.tree.DefaultMutableTreeNode(s,false));
        }
        return new javax.swing.tree.DefaultTreeModel(root, true);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.firePropertyChange("TextToInsert", "", "[" + arg.toString() + "]");
    }
    private class VariableTreeMouseListener extends java.util.Observable implements MouseListener {
        private javax.swing.JTree _Tree;
        public VariableTreeMouseListener(javax.swing.JTree Tree){
            _Tree = Tree;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount()==2 && javax.swing.SwingUtilities.isLeftMouseButton(e)){
                //raise event to notify window that text needs to be inserted where the carat position was last captured. 
                javax.swing.tree.TreePath Path = _Tree.getPathForLocation(e.getX(), e.getY());
                Object bla = Path.getLastPathComponent();
                javax.swing.tree.DefaultMutableTreeNode TN = (javax.swing.tree.DefaultMutableTreeNode)bla;
                this.setChanged();
                this.notifyObservers(TN.toString());
                
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {

        }
        @Override
        public void mouseReleased(MouseEvent e) {

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
