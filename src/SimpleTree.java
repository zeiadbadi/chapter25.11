import javax.swing.*;

import javax.swing.event.*;

import java.awt.*;

import java.io.*;

import java.util.*;

public class SimpleTree extends JFrame {

    JScrollPane scrollpane;

    DisplayPanel panel;

    public SimpleTree(MyTree t) {

        panel = new DisplayPanel(t);

        panel.setPreferredSize(new Dimension(300, 300));

        scrollpane = new JScrollPane(panel);

        getContentPane().add(scrollpane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        pack();

    }

    public static void main(String[] args) {

        MyTree t = new MyTree();

        BufferedReader diskInput;

        String word;



        if(args.length!=1){

            System.out.println("usage: java DisplayTree textfile");

            System.exit(0);

        }

        try {

            diskInput = new BufferedReader(new InputStreamReader(

                    new FileInputStream(

                            new File(args[0]))));

            Scanner input=new Scanner(diskInput);

            while (input.hasNext()) {

                word=input.next();

                word=word.toLowerCase();

                t.root = t.insert(t.root, word);

                t.inputString= t.inputString + " " + word;


            }

        }

        catch (IOException e) {

            System.out.println("io exception");

        }

        t.computeNodePositions();

        t.maxheight=t.treeHeight(t.root);

        SimpleTree dt = new SimpleTree(t);

        dt.setVisible(true);

    }

}

class DisplayPanel extends JPanel {

    MyTree t;

    int xs;

    int ys;

    public DisplayPanel(MyTree t) {

        this.t = t;

        setBackground(Color.white);

        setForeground(Color.black);

    }

    protected void paintComponent(Graphics g) {

        g.setColor(getBackground());

        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(getForeground());

        Font MyFont = new Font("SansSerif",Font.PLAIN,10);

        g.setFont(MyFont);

        xs=10;

        ys=20;

        g.drawString("Binary Search tree for the input string:\n",xs,ys);

        ys=ys+10;;

        int start=0;



        if(t.inputString.length()<23*150){

            while((t.inputString.length()-start)>150){

                g.drawString(t.inputString.substring(start,start+150),xs,ys);

                start+=151;

                ys+=15;

            }

            g.drawString(t.inputString.substring(start,t.inputString.length()),xs,ys);

        }

        MyFont = new Font("SansSerif",Font.BOLD,20);

        g.setFont(MyFont);

        this.drawTree(g, t.root);

        revalidate();

    }

    public void drawTree(Graphics g, Node root) {

        int dx, dy, dx2, dy2;

        int SCREEN_WIDTH=800;

        int SCREEN_HEIGHT=700;

        int XSCALE, YSCALE;

        XSCALE=SCREEN_WIDTH/t.totalnodes;

        YSCALE=(SCREEN_HEIGHT-ys)/(t.maxheight+1);

        if (root != null) {
            drawTree(g, root.left);

            dx = root.xpos * XSCALE;

            dy = root.ypos * YSCALE +ys;

            String s = (String) root.data;

            g.drawString(s, dx, dy);

            if(root.left!=null){

                dx2 = root.left.xpos * XSCALE;

                dy2 = root.left.ypos * YSCALE +ys;

                g.drawLine(dx,dy,dx2,dy2);

            }

            if(root.right!=null){

                dx2 = root.right.xpos * XSCALE;

                dy2 = root.right.ypos * YSCALE + ys;

                g.drawLine(dx,dy,dx2,dy2);

            }

            drawTree(g, root.right);

        }

    }

}

class MyTree {

    String inputString= new String();

    Node root;

    int totalnodes = 0;

    int maxheight=0;

    MyTree() {

        root = null;

    }

    public int treeHeight(Node t){

        if(t==null) return -1;

        else return 1 + max(treeHeight(t.left),treeHeight(t.right));

    }

    public int max(int a, int b){

        if(a>b) return a; else return b;

    }

    public void computeNodePositions() {

        int depth = 1;

        inorder_traversal(root, depth);

    }



    public void inorder_traversal(Node t, int depth) {

        if (t != null) {

            inorder_traversal(t.left, depth + 1);

            t.xpos = totalnodes++;

            t.ypos = depth;

            inorder_traversal(t.right, depth + 1);

        }

    }



    public Node insert(Node root, String s) {

        if (root == null) {

            root = new Node(s, null, null);

            return root;

        }

        else {

            if (s.compareTo((String)(root.data)) == 0) {

                return root;

            } else   if (s.compareTo((String)(root.data)) < 0)

                root.left = insert(root.left, s);

            else

                root.right = insert(root.right, s);

            return root;

        }

    }

}

class Node {

    Object data;

    Node left;

    Node right;

    int xpos; 

    int ypos;

    Node(String x, Node l, Node r) {

        left = l;

        right = r;

        data = (Object) x;

    }

}