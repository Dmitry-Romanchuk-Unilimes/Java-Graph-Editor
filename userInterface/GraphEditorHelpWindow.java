// 
// Decompiled by Procyon v0.5.36
// 

package userInterface;

import dataStructure.DoublyLinkedList;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

public class GraphEditorHelpWindow extends JGraphEdInternalFrame implements HyperlinkListener, TreeSelectionListener, ActionListener
{
    public static int WIDTH;
    public static int HEIGHT;
    private JTree contentsTree;
    private JEditorPane contentPane;
    private JToolBar toolBar;
    private DoublyLinkedList pageList;
    private JButton backButton;
    private JButton upButton;
    private JButton forwardButton;
    private boolean navigation;
    static /* synthetic */ Class class$0;
    
    static {
        GraphEditorHelpWindow.WIDTH = 700;
        GraphEditorHelpWindow.HEIGHT = 400;
    }
    
    public GraphEditorHelpWindow(final GraphController controller) {
        super(controller, "JGraphEd Help", true, true, true, false);
        (this.toolBar = new JToolBar()).setFloatable(false);
        final GridBagLayout tlayout = new GridBagLayout();
        final GridBagConstraints tlayoutCons = new GridBagConstraints();
        this.toolBar.setLayout(tlayout);
        this.navigation = false;
        Class class$0;
        if ((class$0 = GraphEditorHelpWindow.class$0) == null) {
            try {
                class$0 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
            }
            catch (ClassNotFoundException ex) {
                throw new NoClassDefFoundError(ex.getMessage());
            }
        }
        (this.backButton = new JButton(new ImageIcon(class$0.getResource("/images/Back.gif")))).setEnabled(false);
        this.backButton.setToolTipText("Move Back to the Previously Viewed Page");
        this.backButton.addActionListener(this);
        tlayoutCons.gridx = -1;
        tlayoutCons.gridy = -1;
        tlayoutCons.gridwidth = 1;
        tlayoutCons.gridheight = 1;
        tlayoutCons.fill = 0;
        tlayoutCons.insets = new Insets(1, 1, 1, 1);
        tlayoutCons.anchor = 11;
        tlayoutCons.weightx = 0.0;
        tlayoutCons.weighty = 0.0;
        tlayout.setConstraints(this.backButton, tlayoutCons);
        this.toolBar.add(this.backButton);
        Class class$2;
        if ((class$2 = GraphEditorHelpWindow.class$0) == null) {
            try {
                class$2 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
            }
            catch (ClassNotFoundException ex2) {
                throw new NoClassDefFoundError(ex2.getMessage());
            }
        }
        (this.upButton = new JButton(new ImageIcon(class$2.getResource("/images/Up.gif")))).setToolTipText("Move Up One Page in the Contents Tree");
        this.upButton.setEnabled(false);
        this.upButton.addActionListener(this);
        tlayoutCons.gridx = -1;
        tlayoutCons.gridy = -1;
        tlayoutCons.gridwidth = 1;
        tlayoutCons.gridheight = 1;
        tlayoutCons.fill = 0;
        tlayoutCons.insets = new Insets(1, 1, 1, 1);
        tlayoutCons.anchor = 11;
        tlayoutCons.weightx = 0.0;
        tlayoutCons.weighty = 0.0;
        tlayout.setConstraints(this.upButton, tlayoutCons);
        this.toolBar.add(this.upButton);
        Class class$3;
        if ((class$3 = GraphEditorHelpWindow.class$0) == null) {
            try {
                class$3 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
            }
            catch (ClassNotFoundException ex3) {
                throw new NoClassDefFoundError(ex3.getMessage());
            }
        }
        (this.forwardButton = new JButton(new ImageIcon(class$3.getResource("/images/Forward.gif")))).setEnabled(false);
        this.forwardButton.setToolTipText("Move Forward to a Recently Viewed Page");
        this.forwardButton.addActionListener(this);
        tlayoutCons.gridx = -1;
        tlayoutCons.gridy = -1;
        tlayoutCons.gridwidth = 1;
        tlayoutCons.gridheight = 1;
        tlayoutCons.fill = 0;
        tlayoutCons.insets = new Insets(1, 1, 1, 1);
        tlayoutCons.anchor = 11;
        tlayoutCons.weightx = 0.0;
        tlayoutCons.weighty = 0.0;
        tlayout.setConstraints(this.forwardButton, tlayoutCons);
        this.toolBar.add(this.forwardButton);
        this.pageList = new DoublyLinkedList();
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(this.toolBar, "North");
        this.buildContentsTree();
        this.contentsTree.getSelectionModel().setSelectionMode(1);
        this.contentsTree.addTreeSelectionListener(this);
        (this.contentPane = new JEditorPane()).setEditable(false);
        this.contentPane.addHyperlinkListener(this);
        try {
            final JEditorPane contentPane = this.contentPane;
            Class class$4;
            if ((class$4 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$4 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex4) {
                    throw new NoClassDefFoundError(ex4.getMessage());
                }
            }
            contentPane.setPage(class$4.getResource("/help/index.htm"));
        }
        catch (Exception e) {
            System.out.println("Couldn't create help URL: /help/index.htm");
        }
        this.pageList.enqueue(((DefaultMutableTreeNode)this.contentsTree.getModel().getRoot()).getUserObject());
        final JScrollPane scrollPane1 = new JScrollPane(this.contentsTree, 20, 30);
        final JScrollPane scrollPane2 = new JScrollPane(this.contentPane, 20, 30);
        final JSplitPane hSplitPane = new JSplitPane(1, scrollPane1, scrollPane2);
        hSplitPane.setOneTouchExpandable(true);
        hSplitPane.setResizeWeight(0.3);
        this.getContentPane().add(hSplitPane, "Center");
        this.addInternalFrameListener(controller);
        this.setSize(GraphEditorHelpWindow.WIDTH, GraphEditorHelpWindow.HEIGHT);
        this.setVisible(true);
    }
    
    private void buildContentsTree() {
        try {
            Class class$0;
            if ((class$0 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$0 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex) {
                    throw new NoClassDefFoundError(ex.getMessage());
                }
            }
            final DefaultMutableTreeNode root = new DefaultMutableTreeNode(new LinkAndDescription(class$0.getResource("/help/index.htm"), "JGraphEd Help Contents"));
            Class class$2;
            if ((class$2 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$2 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex2) {
                    throw new NoClassDefFoundError(ex2.getMessage());
                }
            }
            DefaultMutableTreeNode nodeOne = new DefaultMutableTreeNode(new LinkAndDescription(class$2.getResource("/help/using.htm"), "Using JGraphEd"));
            root.add(nodeOne);
            Class class$3;
            if ((class$3 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$3 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex3) {
                    throw new NoClassDefFoundError(ex3.getMessage());
                }
            }
            DefaultMutableTreeNode nodeTwo = new DefaultMutableTreeNode(new LinkAndDescription(class$3.getResource("/help/commands.htm"), "Commands - ToolBar Button and Menu Items"));
            nodeOne.add(nodeTwo);
            Class class$4;
            if ((class$4 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$4 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex4) {
                    throw new NoClassDefFoundError(ex4.getMessage());
                }
            }
            nodeTwo = new DefaultMutableTreeNode(new LinkAndDescription(class$4.getResource("/help/modes.htm"), "Modes of Operation"));
            nodeOne.add(nodeTwo);
            Class class$5;
            if ((class$5 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$5 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex5) {
                    throw new NoClassDefFoundError(ex5.getMessage());
                }
            }
            DefaultMutableTreeNode nodeThree = new DefaultMutableTreeNode(new LinkAndDescription(class$5.getResource("/help/edit.htm"), "Edit Mode"));
            nodeTwo.add(nodeThree);
            Class class$6;
            if ((class$6 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$6 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex6) {
                    throw new NoClassDefFoundError(ex6.getMessage());
                }
            }
            nodeThree = new DefaultMutableTreeNode(new LinkAndDescription(class$6.getResource("/help/move.htm"), "Move Mode"));
            nodeTwo.add(nodeThree);
            Class class$7;
            if ((class$7 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$7 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex7) {
                    throw new NoClassDefFoundError(ex7.getMessage());
                }
            }
            nodeThree = new DefaultMutableTreeNode(new LinkAndDescription(class$7.getResource("/help/rotate.htm"), "Rotate Mode"));
            nodeTwo.add(nodeThree);
            Class class$8;
            if ((class$8 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$8 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex8) {
                    throw new NoClassDefFoundError(ex8.getMessage());
                }
            }
            nodeThree = new DefaultMutableTreeNode(new LinkAndDescription(class$8.getResource("/help/resize.htm"), "Resize Mode"));
            nodeTwo.add(nodeThree);
            Class class$9;
            if ((class$9 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$9 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex9) {
                    throw new NoClassDefFoundError(ex9.getMessage());
                }
            }
            nodeTwo = new DefaultMutableTreeNode(new LinkAndDescription(class$9.getResource("/help/preferences.htm"), "Preferences"));
            nodeOne.add(nodeTwo);
            Class class$10;
            if ((class$10 = GraphEditorHelpWindow.class$0) == null) {
                try {
                    class$10 = (GraphEditorHelpWindow.class$0 = Class.forName("userInterface.GraphEditorHelpWindow"));
                }
                catch (ClassNotFoundException ex10) {
                    throw new NoClassDefFoundError(ex10.getMessage());
                }
            }
            nodeOne = new DefaultMutableTreeNode(new LinkAndDescription(class$10.getResource("/help/about.htm"), "About JGraphEd"));
            root.add(nodeOne);
            this.contentsTree = new JTree(root);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void hyperlinkUpdate(final HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                this.contentsTree.getSelectionModel().setSelectionPath(this.pathForLink(e.getURL()));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void valueChanged(final TreeSelectionEvent e) {
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.contentsTree.getLastSelectedPathComponent();
        if (node != null) {
            final LinkAndDescription linkAndDesc = (LinkAndDescription)node.getUserObject();
            if (linkAndDesc.getURL() != null) {
                try {
                    if (!this.navigation) {
                        this.pageList.enqueueAfterCurrent(linkAndDesc);
                    }
                    else {
                        this.navigation = false;
                    }
                    this.contentPane.setPage(linkAndDesc.getURL());
                    this.update();
                }
                catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }
    
    private TreePath pathForLink(final URL url) {
        final TreeNode treeNode = this.nodeForLink(url);
        return new TreePath(((DefaultTreeModel)this.contentsTree.getModel()).getPathToRoot(treeNode));
    }
    
    private TreeNode nodeForLink(final URL url) {
        return this.findNodeWithLink((DefaultMutableTreeNode)this.contentsTree.getModel().getRoot(), url);
    }
    
    private TreeNode findNodeWithLink(final DefaultMutableTreeNode parentNode, final URL url) {
        if (parentNode.getUserObject().equals(url)) {
            return parentNode;
        }
        for (int i = 0; i < parentNode.getChildCount(); ++i) {
            final TreeNode childNode = this.findNodeWithLink((DefaultMutableTreeNode)parentNode.getChildAt(i), url);
            if (childNode != null) {
                return childNode;
            }
        }
        return null;
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.backButton) {
            if (this.pageList.hasPrev()) {
                this.navigation = true;
                this.pageList.toPrev();
                final LinkAndDescription lad = (LinkAndDescription)this.pageList.getCurrent();
                this.contentsTree.getSelectionModel().setSelectionPath(this.pathForLink(lad.getURL()));
            }
        }
        else if (e.getSource() == this.forwardButton) {
            if (this.pageList.hasNext()) {
                this.navigation = true;
                this.pageList.toNext();
                final LinkAndDescription lad = (LinkAndDescription)this.pageList.getCurrent();
                this.contentsTree.getSelectionModel().setSelectionPath(this.pathForLink(lad.getURL()));
            }
        }
        else if (e.getSource() == this.upButton) {
            final LinkAndDescription lad = (LinkAndDescription)((DefaultMutableTreeNode)((DefaultMutableTreeNode)this.contentsTree.getLastSelectedPathComponent()).getParent()).getUserObject();
            this.contentsTree.getSelectionModel().setSelectionPath(this.pathForLink(lad.getURL()));
        }
    }
    
    private void update() {
        this.backButton.setEnabled(this.pageList.hasPrev());
        this.forwardButton.setEnabled(this.pageList.hasNext());
        this.upButton.setEnabled(!((DefaultMutableTreeNode)this.contentsTree.getLastSelectedPathComponent()).isRoot());
    }
}
