/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculatorwindow;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JTextPane;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
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
    private String _Text = "";
    public ExpressWindow(){
        _Parser = new Parser.Parser();
        _Parser.addObserver(this);
        this.addMouseListener(new TextClickListener(this));
        this.addMouseMotionListener(new TextMotionListener(this));
        //this.addKeyListener(new java.awt.event.KeyAdapter() {public void keyReleased(java.awt.event.KeyEvent evt) {parseText(evt);}});
        this.addKeyListener(new java.awt.event.KeyAdapter() {public void keyPressed(java.awt.event.KeyEvent evt) {SetText(evt);}});
        this.addKeyListener(new java.awt.event.KeyAdapter() {public void keyTyped(java.awt.event.KeyEvent evt) {Consume(evt);}});
    }
    private void Consume(java.awt.event.KeyEvent evt){
        //this.setText("");
        evt.consume();
        if(!_Text.equals("")){
            //ParseTreeNodes.ParseTreeNode.ClearSyntaxErrors();
            _Tree = _Parser.Parse(_Text);
            if(_Parser.ContainsErrors()){
                for(String s : _Parser.GetErrors()){
                    System.out.println("Parse Error: " + s);
                }
                this.setText(_Text);
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
                        System.out.println("Compute Error: " + s);
                    }
                }
                this.firePropertyChange("Tree", null, _Tree);//i believe this allows me to listen for this event in the parent.
            }
        }
    }
    private void SetText(java.awt.event.KeyEvent evt){
//        if(evt.isShiftDown()){
//            switch(evt.getKeyCode()){
//                case 1://exclamation point
//                    _Text += "!";
//                    break;
//                case 6://exponent
//                    _Text += "^";
//                    break;
//                case 7://andperstand
//                    _Text += "&";
//                    break;
//                case 8: // multiply
//                    _Text += "*";
//                    break;
//                case 9://left paren
//                    _Text += "(";
//                    break;
//                case 10: //right paren
//                    _Text += ")";
//                    break;
//                case 188:// lt
//                    _Text += "<";
//                    break;
//                case 190://gt
//                    _Text += ">";
//                    break;
//                default:
//                    //unknown
//                    break;
//            }
//        }else{
            switch (evt.getKeyCode()){//http://www.cambiaresearch.com/articles/15/javascript-char-codes-key-codes
                case 8://backspace
                    //check carat position
                    //check if there is selected text.
    //                this.getCaret();
    //                if(this.getSelectedText().equals("")){
    //                    this.getSelectionEnd();
    //                    this.getSelectionStart();
    //                }

                    if(_Text.length()>0){_Text = _Text.substring(0, _Text.length() -1);}
                    break;
                case 13://enter
                    break;
                case 16: //shift
                    break;
                case 46://delete
                    break;
                default:
                    _Text += evt.getKeyChar();
                    break;
            }
//        }
        

        evt.consume();
        this.setText("");
    }
    @Override
    public void update(Observable o, Object arg) {
        ParseTreeNodes.Token args = (ParseTreeNodes.Token)arg;
        if(args.GetToken()==ParseTreeNodes.Tokens.ERR){
        }else if(args.GetToken()==ParseTreeNodes.Tokens.EOF){    
        }else{
            if(args.GetHelpFile().equals("")){
                try {
                    this.getStyledDocument().insertString(args.GetPosition()-1, args.GetString(), null);
                } catch (BadLocationException ex) {
                    Logger.getLogger(ExpressWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                try {//https://community.oracle.com/thread/2089990?start=0&tstart=0
                    Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
                    Style regularBlue = this.getStyledDocument().addStyle("regularBlue", def);
                    StyleConstants.setForeground(regularBlue, Color.BLUE);
                    StyleConstants.setUnderline(regularBlue,true);
                    regularBlue.addAttribute("linkact",new URLLinkAction(args.GetHelpFile()));
                    this.getStyledDocument().insertString(args.GetPosition()-1, args.GetString(),regularBlue);
                } catch (BadLocationException ex) {
                    Logger.getLogger(ExpressWindow.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        }
    }
    private class TextClickListener extends MouseAdapter {
        private JTextPane _Pane;
        TextClickListener(JTextPane Pane){
            _Pane = Pane;
        }
        @Override
        public void mouseClicked( MouseEvent e ) {
            try{//cannot access the textpane because this is a separate class.
                Element elem = _Pane.getStyledDocument().getCharacterElement(_Pane.viewToModel(e.getPoint()));
                AttributeSet as = elem.getAttributes();
                URLLinkAction fla = (URLLinkAction)as.getAttribute("linkact");
                if(fla != null)
                    fla.execute();
              }
              catch(Exception x) {
                   x.printStackTrace();
              }
        }
    }
    private class TextMotionListener extends MouseInputAdapter {
        private JTextPane _Pane;
        TextMotionListener(JTextPane Pane){
           _Pane = Pane; 
        }
        public void mouseMoved(MouseEvent e) {
            Element elem = _Pane.getStyledDocument().getCharacterElement( _Pane.viewToModel(e.getPoint()));
            AttributeSet as = elem.getAttributes();
            if(StyleConstants.isUnderline(as))
                _Pane.setCursor(new Cursor(Cursor.HAND_CURSOR));
            else
                _Pane.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
    private class URLLinkAction extends AbstractAction{
        private String url;
        URLLinkAction(String bac){
            url=bac;
        }
        protected void execute() {
            try {
                String osName = System.getProperty("os.name").toLowerCase();
                Runtime rt = Runtime.getRuntime();
                if (osName.indexOf( "win" ) >= 0) {
                    rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);
                }else if (osName.indexOf("mac") >= 0) {
                    rt.exec( "open " + url);
                }else if (osName.indexOf("ix") >=0 || osName.indexOf("ux") >=0 || osName.indexOf("sun") >=0) {
                    String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror","netscape","opera","links","lynx"};
                    // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
                    StringBuffer cmd = new StringBuffer();
                    for (int i = 0 ; i < browsers.length ; i++){
                        cmd.append((i == 0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");
                        rt.exec(new String[] { "sh", "-c", cmd.toString() });
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        public void actionPerformed(ActionEvent e){
            execute();
        }
     }
}
