# CalculatorWindow
A simple interface for the Expression Parser.  Currently the runnable class is called Window. It shows an example of loading a dbfreader into a variable tree, and having a variable tree, function tree, and ExpressionWindow tied together in an operational example.

## ExpressionWindow
This is a text box that notifies through the PropertyChange event with the tag "Tree" that a valid expression tree has been created.  The tree is passed through the new property value argument.

An example instatiation would look something like this:

```java
//create an expression window, the expression window does the work, it needs all of the initial data so that the expression can          //be tested for validity, and so that example output can be shown.
ExpressionWindow EW = new ExpressionWindow(reader.getColumnNames(),ReaderTypesToParserTypes(),reader.getRow(0));
EW.setSize(404,200);
EW.setLocation(12,220);

//this listener allows the calculator to show the expression and output if the designer so chooses.
EW.addPropertyChangeListener(this);
```
Of course the user could set the size and position through the designer if the ExpressionWindow is added to the pallet.
However, the `ExpressionWindow EW = new ExpressionWindow(ColumnNames[], ColumnTypes[], SampleData[]);`  and the `EW.PropertyChangeListener(this);` must be made after the initComponents() call.

Here is an example with initialization of the ExpressionWindow without the dbfreader.
```java
String[] cols = {"A","B","C"};
ParseTreeNodes.TypeEnum[] types = {ParseTreeNodes.TypeEnum.DOUBLE,ParseTreeNodes.TypeEnum.STRING,ParseTreeNodes.TypeEnum.BOOLEAN};
Object[] SampleData = {2.0,"exhibit B",true};
ExpressionWindow EW = new ExpressionWindow(cols,types,SampleData);
```
To evaluate the expression for the first row, the PropertyChanged event must be monitored for the "Tree" argument.
```java
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("Tree".equals(evt.getPropertyName())){
            ParseTreeNodes.ParseTreeNode tree = (ParseTreeNodes.ParseTreeNode)evt.getNewValue();
            ExpressionLabel.setText(tree.ToString());
            ResultLabel.setText(tree.Evaluate().Result().toString());
        }
    }
```
Above you can see the text for the expression was being set for display into a label, and the result was being set for display into a different label.

## VariableTree
A VariableTree shows the user the available columns in the attached dbfreader (or any abstracted reader), and a double click of the tree item corresponding to a column or data element will insert the proper syntax to allow that column to be utilized in the evaluation of that expression.

Here is an example of initilization of the VariableTree:
```java
        //create an ExpressionWindow with some available columns...
        ExpressionWindow EW = new ExpressionWindow(cols,types,SampleData);
        //create a varable tree, this will show the user the availabe variables to include in their computation.
        VariableTree VT = new VariableTree(reader.getColumnNames());
        //this listener allows the variable tree to insert column nmaes with the correct syntax into the expression window.
        VT.addPropertyChangeListener(EW);
```
As with the ExpressionWindow, if the VariableTree is added to the pallet it can be placed using the designer, but the programmer will still have to set the listener and initialize the VariableTree.  Notice that the ExpressionWindow is the component monitoring for the property changed event in this case.

## FunctionTree
The FunctionTree is much like the VariableTree, it is a list of the avalilable functions (currently only the prefix functions) that can be utilized in the ExpressionWindow.  Double clicking on a FunctionTree element will insert the proprer syntax for that prefix function into the expression window.

```java
        //create an ExpressionWindow with some available columns...
        ExpressionWindow EW = new ExpressionWindow(cols,types,SampleData);
        //create a function tree, this allows the user to see the available functions, quiery their help, and insert their syntax.
        FunctionTree FT = new FunctionTree();
        //This listener allows the function tree to insert syntax to the expression window.
        FT.addPropertyChangeListener(EW);
```
Again, if the user wishes to place this element with the designer, it must be added to the pallet, but the propertychangedlistener will still have to be defined properly.  As with the VariableTree, the ExpressionWindow is handling the propertyChanged event.

There are no arguments for this element because the initalization is defined in the default constructor.  This element utilizes reflection to define the tree and its functionality.  For this element to work properly the ExpressionParser library must be referenced in the project executing the FunctionTree.
