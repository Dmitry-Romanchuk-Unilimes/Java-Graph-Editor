// 
// Decompiled by Procyon v0.5.36
// 

package userInterface.menuAndToolBar;

import graphStructure.Graph;
import userInterface.GraphController;
import userInterface.GraphEditor;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

public class MenuAndToolBar
{
    private GraphController controller;
    private StringTokenizer tok;
    private static int NO_MODE;
    private static int EDIT_MODE;
    private static int MOVE_MODE;
    private static int ROTATE_MODE;
    private static int RESIZE_MODE;
    private Vector actions;
    private HashMap groups;
    private JMenuBar menuBar;
    private JToolBar toolBar;
    private JLabel cursorLocationLabel;
    private Point cursorPoint;
    private JInternalFrame lastWindow;
    private int lastShownIndex;
    private JMenu windowMenu;
    private JMenuItem noWindowItem;
    static /* synthetic */ Class class$0;
    static /* synthetic */ Class class$1;
    
    static {
        MenuAndToolBar.NO_MODE = 0;
        MenuAndToolBar.EDIT_MODE = 1;
        MenuAndToolBar.MOVE_MODE = 2;
        MenuAndToolBar.ROTATE_MODE = 3;
        MenuAndToolBar.RESIZE_MODE = 4;
    }
    
    public MenuAndToolBar(final GraphController controller) {
        this.controller = controller;
        this.actions = new Vector();
        this.groups = new HashMap();
        this.cursorPoint = null;
        this.createControls(this.loadControls());
        this.hideControls();
    }
    
    public JToolBar getToolBar() {
        return this.toolBar;
    }
    
    public JMenuBar getMenuBar() {
        return this.menuBar;
    }
    
    public void updateCursorLocation(final Point cursorPoint) {
        this.cursorPoint = cursorPoint;
    }
    
    public Point getCursorPoint() {
        return this.cursorPoint;
    }
    
    public void setCursorLocationText(final String text) {
        this.cursorLocationLabel.setText(text);
    }
    
    private Vector loadControls() {
        final Vector v = new Vector();
        v.add("M,File,F,Load & Save Graphs");
        v.add("S,File,New Graph,/images/New.gif,Create a New Graph,N,true,1,newGraph");
        v.add("S,File,Load Graph,/images/Open.gif,Load a Graph From a File,L,isApplication,1,loadGraph");
        v.add("S,File,Save Graph,/images/Save.gif,Save the Current Graph To a File,S,false,1,saveGraph");
        v.add("S,File,Close Graph,/images/Delete.gif,Close the Current Graph Editor,C,false,1,closeGraph");
        v.add("S,File,Preferences,/images/Preferences.gif,JGraphEd Preferences,P,true,1,preferences");
        v.add("separator,space");
        v.add("M,Modes,M,Change Editor Mode");
        v.add("P,Modes,Choose an Operation Mode,/images/DefaultDisplay.gif,Choose an Operation Mode,Z,false,1,5,C,Modes,Edit,/images/Edit.gif,Graph Edit Mode,E,false,1,editMode,C,Modes,Grid,/images/Grid.gif,Graph Grid Mode,M,false,1,gridMode,C,Modes,Move,/images/Move.gif,Graph Move Mode,M,false,1,moveMode,C,Modes,Rotate,/images/Rotate.gif,Graph Rotate Mode,R,false,1,rotateMode,C,Modes,Resize,/images/Resize.gif,Graph Resize Mode,Z,false,1,resizeMode");
        v.add("M,Show,S,Coordinate or Label Showing Options");
        v.add("P,Show,Show,/images/DefaultDisplay.gif,Choose to Show Coordinates or Labels,Z,false,1,3,C,Show,Show Coordinates,/images/ShowCoords.gif,Show Coordinates,C,false,1,showCoords,C,Show,Show Labels,/images/ShowLabels.gif,Show Labels,L,false,1,showLabels,C,Show,Show Nothing,/images/ShowNothing.gif,Show Nothing,T,false,1,showNothing");
        v.add("M,Info,I,Info about the current graph");
        v.add("S,Info,Info,/images/Info.gif,Info about the current graph,I,false,1,info");
        v.add("S,Info,Log,/images/Log.gif,Log of Operations run on the current graph,L,false,1,log");
        v.add("M,Help,H,JGraphEd Help");
        v.add("S,Help,Help,/images/Help.gif,JGraphEd Help,H,true,1,help");
        v.add("separator,space");
        v.add("M,Undo,U,Undo or Redo Actions");
        v.add("S,Undo,Undo,/images/Undo.gif,Undo Last Command,U,false,1,undo");
        v.add("S,Undo,Redo,/images/Redo.gif,Redo Last Un-Done Command,R,false,1,redo");
        v.add("S,Undo,Toggle Undos,/images/ToggleUndos.gif,Disable Undos,D,true,1,toggleUndo");
        v.add("separator,space");
        v.add("M,Edit,E,Edit Options");
        v.add("S,Edit,Unselect All,/images/UnselectAll.gif,Unselect All Nodes and Edges,U,false,1,unselectAll");
        v.add("S,Edit,Remove Selected,/images/RemoveSelected.gif,Remove Selected Nodes and Edges,S,false,1,removeSelected");
        v.add("S,Edit,Remove All,/images/RemoveAll.gif,Remove All Nodes and Edges,R,false,1,removeAll");
        v.add("S,Edit,Remove Generated,/images/RemoveGenerated.gif,Remove Generated Edges,E,false,1,removeGenerated");
        v.add("S,Edit,Preserve Generated,/images/PreserveGenerated.gif,Preserve Generated Edges,P,false,1,preserveGenerated");
        v.add("separator,newline");
        v.add("M,Test,T,Test the Current Graph");
        v.add("S,Test,Connectivity Test,/images/TestConnectivity.gif,Is Current Graph Connected?,C,false,1,testConnectivity");
        v.add("S,Test,Biconnectivity Test,/images/TestBiconnectivity.gif,Is Current Graph Biconnected?,B,false,1,testBiconnectivity");
        v.add("S,Test,Planarity Test,/images/TestPlanarity.gif,Is Current Graph Planar?,P,false,1,testPlanarity");
        v.add("separator,space");
        v.add("M,Operation,O,Operate on Current Graph");
        v.add("S,Operation,Create Random,/images/CreateRandom.gif,Create X Random Nodes,R,false,1,createRandom");
        v.add("S,Operation,Embedding,/images/Embed.gif,Embed the Current Graph,E,false,1,embedding");
        v.add("S,Operation,Make Connected,/images/MakeConnected.gif,Connect the Current Graph,C,false,1,makeConnected");
        v.add("S,Operation,Make Biconnected,/images/MakeBiconnected.gif,Biconnect the Current Graph,B,false,1,makeBiconnected");
        v.add("S,Operation,Make Maximal,/images/MakeMaximal.gif,Triangulate the Current Graph,M,false,1,makeMaximal");
        v.add("S,Operation,Straight Line Embed,/images/StraightLineEmbed.gif,Straight Line Embed the Current Graph,S,false,1,straightLineEmbed");
        v.add("separator,space");
        v.add("M,Display,D,Graph Display Options");
        v.add("S,Display,Default,/images/DefaultDisplay.gif,Reset to Default Display,F,false,1,resetDisplay");
        v.add("S,Display,Depth First Search,/images/DFSDisplay.gif,Display Depth First Search,D,false,1,displayDFS");
        v.add("S,Display,Biconnected Components,/images/BiconnectedDisplay.gif,Display Biconnected Components,B,false,1,displayBiconnected");
        v.add("S,Display,ST Numbering,/images/STDisplay.gif,Display ST Numbering,S,false,1,displayST");
        v.add("S,Display,Canonical Ordering,/images/CanonicalDisplay.gif,Display Canonical Ordering,O,false,1,displayCanonical");
        v.add("S,Display,Normal Labeling,/images/NormalDisplay.gif,Display Normal Labeling,N,false,1,displayNormal");
        v.add("S,Display,Chan Tree Drawing,/images/ChanTreeDisplay.gif,Display Chan Tree Drawing,C,false,1,displayChanTree");
        v.add("S,Display,Minimum Spanning Tree,/images/MSTDisplay.gif,Display Minimum Spanning Tree,M,false,1,displayMST");
        v.add("S,Display,Dijkstra Shortest Path,/images/SPDisplay.gif,Display Shortest Path For Two Nodes,P,false,1,displayDijkstra");
        v.add("separator,space");
        v.add("M,Windows,W,List of Open Windows");
        v.add("S,Windows,None,null,No Windows Are Open, ,true,1, ");
        v.add("cursorLocationLabel");
        return v;
    }
    
    private void createControls(final Vector controls) {
        (this.toolBar = new JToolBar()).setFloatable(false);
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints layoutCons = new GridBagConstraints();
        this.toolBar.setLayout(layout);
        this.menuBar = new JMenuBar();
        boolean firstOfGroup = true;
        JMenu menu = null;
        for (int i = 0; i < controls.size(); ++i) {
            final String current = controls.elementAt(i);
            if (this.isSeparator(current)) {
                final JPanel panel = new JPanel();
                layoutCons.gridx = -1;
                layoutCons.gridy = -1;
                if (this.isNewLineSeparator(current)) {
                    layoutCons.gridwidth = 0;
                }
                else {
                    layoutCons.gridwidth = 1;
                }
                layoutCons.gridheight = 1;
                layoutCons.fill = 0;
                layoutCons.insets = new Insets(0, 0, 0, 0);
                layoutCons.anchor = 11;
                layoutCons.weightx = 0.0;
                layoutCons.weighty = 0.0;
                layout.setConstraints(panel, layoutCons);
                this.toolBar.add(panel);
            }
            else if (this.isMenu(current)) {
                menu = this.createMenu(current);
                this.menuBar.add(menu);
                if (menu.getText().equals("Windows")) {
                    this.windowMenu = menu;
                }
                firstOfGroup = false;
            }
            else if (this.isAction(current)) {
                final MenuAndToolBarAction action = this.createMenuAndToolbarAction(current);
                this.actions.addElement(action);
                if (!action.isCompound()) {
                    if (action.getText().equals("None")) {
                        this.noWindowItem = action.getMenuItem();
                    }
                    else {
                        final JButton button = action.getButton();
                        layoutCons.gridx = -1;
                        layoutCons.gridy = -1;
                        layoutCons.gridwidth = action.getWidth();
                        layoutCons.gridheight = 1;
                        layoutCons.fill = 0;
                        layoutCons.insets = new Insets(1, 1, 1, 1);
                        layoutCons.anchor = 11;
                        layoutCons.weightx = 0.0;
                        layoutCons.weighty = 0.0;
                        layout.setConstraints(button, layoutCons);
                        this.toolBar.add(button);
                    }
                    final JMenuItem menuItem = action.getMenuItem();
                    menu.add(menuItem);
                }
                else {
                    final ButtonChooser bc = ((CompoundMenuAndToolBarAction)action).getButtonChooser();
                    final Vector menuItems = ((CompoundMenuAndToolBarAction)action).getMenuItems();
                    for (int j = 0; j < menuItems.size(); ++j) {
                        menu.add(menuItems.elementAt(j));
                    }
                    layoutCons.gridx = -1;
                    layoutCons.gridy = -1;
                    layoutCons.gridwidth = action.getWidth();
                    layoutCons.gridheight = 1;
                    layoutCons.fill = 0;
                    layoutCons.insets = new Insets(1, 1, 1, 1);
                    layoutCons.anchor = 11;
                    layoutCons.weightx = 0.0;
                    layoutCons.weighty = 0.0;
                    layout.setConstraints(bc, layoutCons);
                    this.toolBar.add(bc);
                }
            }
            else if (this.isCursorLocationLabel(current)) {
                this.cursorLocationLabel = new JLabel();
                layoutCons.gridx = -1;
                layoutCons.gridy = -1;
                layoutCons.gridwidth = 2;
                layoutCons.gridheight = 1;
                layoutCons.fill = 0;
                layoutCons.insets = new Insets(1, 1, 1, 1);
                layoutCons.anchor = 11;
                layoutCons.weightx = 0.01;
                layoutCons.weighty = 0.01;
                layout.setConstraints(this.cursorLocationLabel, layoutCons);
                this.toolBar.add(this.cursorLocationLabel);
            }
        }
    }
    
    private boolean isSeparator(final String s) {
        return s.startsWith("separator");
    }
    
    private boolean isNewLineSeparator(final String s) {
        return this.isSeparator(s) && s.endsWith(",newline");
    }
    
    private boolean isMenu(final String s) {
        return s.startsWith("M");
    }
    
    private boolean isAction(final String s) {
        return s.startsWith("S") || s.startsWith("P");
    }
    
    private boolean isCursorLocationLabel(final String s) {
        return s.equals("cursorLocationLabel");
    }
    
    private JMenu createMenu(final String s) {
        this.tok = new StringTokenizer(s, ",");
        JMenu menu = null;
        if (this.tok.countTokens() == 4) {
            this.tok.nextToken();
            menu = new JMenu(this.tok.nextToken());
            menu.setMnemonic(this.getKeyCode(this.tok.nextToken()));
            menu.getAccessibleContext().setAccessibleDescription(this.tok.nextToken());
        }
        return menu;
    }
    
    private MenuAndToolBarAction createMenuAndToolbarAction(final String s) {
        return this.createMenuAndToolbarAction(new StringTokenizer(s, ","));
    }
    
    private MenuAndToolBarAction createMenuAndToolbarAction(final StringTokenizer tok) {
        MenuAndToolBarAction action = null;
        final String type = tok.nextToken();
        if ((type.equals("S") && tok.countTokens() == 8) || (type.equals("C") && tok.countTokens() >= 8)) {
            final String group = tok.nextToken();
            final String text = tok.nextToken();
            final String iconString = tok.nextToken();
            ImageIcon icon = null;
            if (!iconString.equals("null")) {
                Class class$0;
                if ((class$0 = MenuAndToolBar.class$0) == null) {
                    try {
                        class$0 = (MenuAndToolBar.class$0 = Class.forName("userInterface.menuAndToolBar.MenuAndToolBar"));
                    }
                    catch (ClassNotFoundException ex) {
                        throw new NoClassDefFoundError(ex.getMessage());
                    }
                }
                icon = new ImageIcon(class$0.getResource(iconString));
            }
            final String desc = tok.nextToken();
            final Integer mnemonic = new Integer(this.getKeyCode(tok.nextToken()));
            final String enabledString = tok.nextToken();
            boolean enabled;
            if (enabledString.equals("isApplication")) {
                enabled = this.controller.isApplication();
            }
            else {
                enabled = Boolean.valueOf(enabledString);
            }
            final int width = Integer.parseInt(tok.nextToken());
            Method method = null;
            try {
                Class class$2;
                if ((class$2 = MenuAndToolBar.class$1) == null) {
                    try {
                        class$2 = (MenuAndToolBar.class$1 = Class.forName("userInterface.GraphController"));
                    }
                    catch (ClassNotFoundException ex2) {
                        throw new NoClassDefFoundError(ex2.getMessage());
                    }
                }
                method = class$2.getDeclaredMethod(tok.nextToken(), (Class[])null);
            }
            catch (NoSuchMethodException ex4) {}
            action = new MenuAndToolBarAction(text, icon, desc, mnemonic, enabled, width, method, type.equals("C"), this.controller);
            this.addToGroup(group, action);
        }
        else if (type.equals("P") && tok.countTokens() > 8) {
            final String group = tok.nextToken();
            final String text = tok.nextToken();
            Class class$3;
            if ((class$3 = MenuAndToolBar.class$0) == null) {
                try {
                    class$3 = (MenuAndToolBar.class$0 = Class.forName("userInterface.menuAndToolBar.MenuAndToolBar"));
                }
                catch (ClassNotFoundException ex3) {
                    throw new NoClassDefFoundError(ex3.getMessage());
                }
            }
            final ImageIcon icon2 = new ImageIcon(class$3.getResource(tok.nextToken()));
            final String desc2 = tok.nextToken();
            final Integer mnemonic2 = new Integer(this.getKeyCode(tok.nextToken()));
            final boolean enabled2 = Boolean.valueOf(tok.nextToken());
            final int width2 = Integer.parseInt(tok.nextToken());
            final int numChildren = Integer.parseInt(tok.nextToken());
            if (tok.countTokens() == numChildren * 9) {
                final Vector v = new Vector(numChildren);
                for (int j = 0; j < numChildren; ++j) {
                    v.addElement(this.createMenuAndToolbarAction(tok));
                }
                action = new CompoundMenuAndToolBarAction(text, icon2, desc2, mnemonic2, enabled2, width2, v, this.controller);
                this.addToGroup(group, action);
            }
        }
        return action;
    }
    
    private int getKeyCode(final String ch) {
        if (ch.length() != 1) {
            return -1;
        }
        return ch.charAt(0);
    }
    
    public void removeWindow(final JMenuItem menuItem) {
        this.windowMenu.remove(menuItem);
        if (this.windowMenu.getItemCount() == 0) {
            this.windowMenu.add(this.noWindowItem);
        }
    }
    
    public void addWindow(final JMenuItem menuItem) {
        if (!this.windowContainsMenuItem(menuItem)) {
            if (this.windowMenu.getItemCount() > 0 && this.windowMenu.getItem(0) == this.noWindowItem) {
                this.windowMenu.remove(this.noWindowItem);
            }
            this.windowMenu.add(menuItem);
        }
    }
    
    public void showControls(final GraphEditor graphEditor) {
        MenuAndToolBarAction action = this.getActionWithText(graphEditor.getModeString());
        CompoundMenuAndToolBarAction compoundAction = action.getParentAction();
        compoundAction.getButtonChooser().setSelected(action.getButton());
        action.getMenuItem().setSelected(true);
        action = this.getActionWithText(graphEditor.getShowString());
        compoundAction = action.getParentAction();
        compoundAction.getButtonChooser().setSelected(action.getButton());
        action.getMenuItem().setSelected(true);
        this.enableAllControls();
        this.updateUndo(graphEditor.getGraph());
    }
    
    public void hideControls() {
        this.disableAllControls();
    }
    
    private void enableAllControls() {
        for (int i = 0; i < this.actions.size(); ++i) {
            this.actions.elementAt(i).setEnabled(true);
        }
        this.getActionWithText("Load Graph").setEnabled(this.controller.isApplication());
        this.getActionWithText("Save Graph").setEnabled(this.controller.isApplication());
    }
    
    private void disableAllControls() {
        for (int i = 0; i < this.actions.size(); ++i) {
            this.actions.elementAt(i).setEnabled(false);
        }
        this.getActionWithText("New Graph").setEnabled(true);
        this.getActionWithText("Load Graph").setEnabled(this.controller.isApplication());
        this.getActionWithText("Preferences").setEnabled(true);
        this.setEnabled("Help", true);
        this.setEnabled("Window", true);
    }
    
    private void setEnabled(final String group, final boolean enabled) {
        final Vector controls = this.getGroup(group);
        if (controls != null) {
            for (int i = 0; i < controls.size(); ++i) {
                controls.elementAt(i).setEnabled(enabled);
            }
        }
    }
    
    private MenuAndToolBarAction getActionWithText(final String text) {
        for (int i = 0; i < this.actions.size(); ++i) {
            if (this.actions.elementAt(i).equalsText(text)) {
                return this.actions.elementAt(i).getActionWithText(text);
            }
        }
        return null;
    }
    
    private boolean windowContainsMenuItem(final JMenuItem item) {
        boolean found = false;
        if (item == null) {
            return true;
        }
        for (int i = 0; i < this.windowMenu.getItemCount(); ++i) {
            if (this.windowMenu.getItem(i) == item) {
                found = true;
                break;
            }
        }
        return found;
    }
    
    private void addToGroup(final String group, final MenuAndToolBarAction action) {
        if (!this.groups.containsKey(group)) {
            this.groups.put(group, new Vector());
        }
        this.getGroup(group).add(action);
    }
    
    public Vector getGroup(final String group) {
        if (this.groups.containsKey(group)) {
            return this.groups.get(group);
        }
        return null;
    }
    
    public void updateUndo(final Graph g) {
        if (g.getTrackUndos()) {
            MenuAndToolBarAction action = this.getActionWithText("Redo");
            JMenuItem menuItem = action.getMenuItem();
            if (!g.hasMoreRedos()) {
                action.putValue("ShortDescription", "No more commands to Redo");
                menuItem.setText("Redo");
                action.setEnabled(false);
            }
            else {
                action.putValue("ShortDescription", "Redo " + g.peekRedo());
                menuItem.setText("Redo " + g.peekRedo());
                action.setEnabled(true);
            }
            action = this.getActionWithText("Undo");
            menuItem = action.getMenuItem();
            if (!g.hasMoreUndos()) {
                action.putValue("ShortDescription", "No more commands to Undo");
                menuItem.setText("Undo");
                action.setEnabled(false);
            }
            else {
                action.putValue("ShortDescription", "Undo " + g.peekUndo());
                menuItem.setText("Undo " + g.peekUndo());
                action.setEnabled(true);
            }
            action = this.getActionWithText("Toggle Undos");
            action.putValue("ShortDescription", "Disable Undos");
            action.setEnabled(true);
            final JButton button = action.getButton();
            Class class$1;
            if ((class$1 = MenuAndToolBar.class$1) == null) {
                try {
                    class$1 = (MenuAndToolBar.class$1 = Class.forName("userInterface.GraphController"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            button.setIcon(new ImageIcon(class$1.getResource("/images/DisableUndos.gif")));
            action.getMenuItem().setText("Disable Undos");
        }
        else {
            MenuAndToolBarAction action = this.getActionWithText("Redo");
            JMenuItem menuItem = action.getMenuItem();
            action.putValue("ShortDescription", "Redo");
            menuItem.setText("Redo");
            action.setEnabled(false);
            action = this.getActionWithText("Undo");
            menuItem = action.getMenuItem();
            action.putValue("ShortDescription", "Undo");
            menuItem.setText("Undo");
            action.setEnabled(false);
            action = this.getActionWithText("Toggle Undos");
            action.putValue("ShortDescription", "Enable Undos");
            action.setEnabled(true);
            final JButton button2 = action.getButton();
            Class class$2;
            if ((class$2 = MenuAndToolBar.class$1) == null) {
                try {
                    class$2 = (MenuAndToolBar.class$1 = Class.forName("userInterface.GraphController"));
                }
                catch (ClassNotFoundException ex2) {
                    throw new NoClassDefFoundError(ex2.getMessage());
                }
            }
            button2.setIcon(new ImageIcon(class$2.getResource("/images/EnableUndos.gif")));
            action.getMenuItem().setText("Enable Undos");
        }
    }
}
