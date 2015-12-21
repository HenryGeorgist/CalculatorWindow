/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorwindow;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Q0HECWPL
 */
public class ExpressWindow extends JTextPane implements Observer{
    private ParseTreeNodes.ParseTreeNode _Tree;
    private Parser.Parser _Parser;
    public ExpressWindow(){
        _Parser = new Parser.Parser();
        _Parser.addObserver(this);
        this.addKeyListener(new java.awt.event.KeyAdapter() {public void keyReleased(java.awt.event.KeyEvent evt) {parseText(evt);}});
    }
    private void parseText(java.awt.event.KeyEvent evt){
        if(!this.getText().equals("")){
            //ParseTreeNodes.ParseTreeNode.ClearSyntaxErrors();
            _Tree = _Parser.Parse(this.getText());
            if(_Parser.ContainsErrors()){
                for(String s : _Parser.GetErrors()){
                    System.out.println("Parse Error: " + s);
                }
            }
                if(_Tree.ContainsSyntaxErrors()){
                    for(String s : _Tree.GetSyntaxErrors()){
                        System.out.println("Syntax Error: " + s);
                    }
                }else{
                    System.out.println(_Tree.ToString());
                    System.out.println(_Tree.Evaluate().Result());
                    if(_Tree.ContainsComputeErrors()){
                        for(String s :_Tree.GetComputeErrors()){
                            System.out.println(s);
                        }
                    }
                    this.firePropertyChange("Tree", null, _Tree);//i believe this allows me to listen for this event in the parent.
                }
        }        
    }
    @Override
    public void update(Observable o, Object arg) {
        ParseTreeNodes.Token args = (ParseTreeNodes.Token)arg;
        if(args.GetToken()==ParseTreeNodes.Tokens.ERR){
        }else if(args.GetToken()==ParseTreeNodes.Tokens.EOF){    
        }else{
            try {//https://community.oracle.com/thread/2089990?start=0&tstart=0
                Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
                Style regularBlue = this.getStyledDocument().addStyle("regularBlue", def);
                StyleConstants.setForeground(regularBlue, Color.BLUE);
                StyleConstants.setUnderline(regularBlue,true);
                //regularBlue.addAttribute("linkact",new URLLinkAction(args.GetHelpFile()));
                this.getStyledDocument().insertString(args.GetPosition()-1, args.GetString(),regularBlue);
                System.out.println(args.GetString() + " at position " + args.GetPosition());
            } catch (BadLocationException ex) {
                Logger.getLogger(ExpressWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
