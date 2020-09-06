import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Definition for a binary tree node.
 */
class TreeNode {

    // **** class members ****
    int         val;
    TreeNode    left;
    TreeNode    right;

    // **** constructors ****
    TreeNode() {}

    TreeNode(int val) { this.val = val; }

    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
        }

    // **** display the value of the TreeNode object (not needed for solution) ****
    @Override
    public String toString() {
        return "" + this.val;
    }
}


/**
 * 894. All Possible Full Binary Trees
 * https://leetcode.com/problems/all-possible-full-binary-trees/
 */
public class Solution {


    /*
     * This method implements a breadth-first search traversal of a binary tree.
     * This method is iterative.
     * It displays all nodes at each level on a separate line.
     */
    static void bfsTraversal(TreeNode root, StringBuilder sb) {

        // **** end of tree flag ****
        boolean endOfTree = false;

        // **** define the current and next queues ****
        List<TreeNode> currentQ = new LinkedList<TreeNode>();
        List<TreeNode> nextQ    = new LinkedList<TreeNode>();
    
        // **** add the root node to the current queue ****
        currentQ.add(root);

        // **** ****
        sb.append("[");

        // **** loop while the current queue has entries ****
        while (!currentQ.isEmpty()) {

            // **** remove the next node from the current queue ****
            TreeNode n = currentQ.remove(0);

            // **** display the node value ****
            if (n != null)
                sb.append(n.toString());
            else
                sb.append("null");

            // **** add left and right children to the next queue ****
            if (n != null) {
                if (n.left != null)
                    nextQ.add(n.left);
                else
                    nextQ.add(null);

                if (n.right != null)
                    nextQ.add(n.right);
                else
                    nextQ.add(null);
            }

            // **** check if the current queue is empty (reached end of level) ****
            if (currentQ.isEmpty()) {

                // **** check if we have all null nodes in the next queue ****
                boolean allNulls = true;
                for (TreeNode t : nextQ) {
                    if (t != null) {
                        allNulls = false;
                        break;
                    }
                }

                // **** point the current to the next queue ****
                currentQ = nextQ;

                // **** clear the next queue ****
                nextQ = new LinkedList<TreeNode>();

                // **** clear the current queue (all null nodes) ****
                if (allNulls)
                    currentQ = new LinkedList<TreeNode>();

                // **** flag we are done with this tree ****
                if (allNulls)
                    endOfTree = true;
            }

            // **** display ',' (node separator) ****
            if (endOfTree)
                sb.append("]");
            else
                sb.append(',');
        }
    }


    /**
     * Return a list of all possible full binary trees with N nodes.
     * Each element of the answer is the root node of one possible tree.
     * 1 <= N <= 20
     * Recursive function.
     */
    static List<TreeNode> allPossibleFBT(int N) {
        
        // **** list of compliant binary tree(s) ****
        List<TreeNode> lst = new ArrayList<TreeNode>();

        // **** if N is even (no possible answer) ****
        if (N % 2 == 0)
            return lst;

        // **** tree with single node ****
        if (N == 1) {
            lst.add(new TreeNode(0));
            return lst;
        }

        // **** only consider odd counts ****
        for (int i = 1; i < N; i += 2) {

            // **** left trees ****
            for (TreeNode left : allPossibleFBT(i)) {

                // **** right trees ****
                for (TreeNode right : allPossibleFBT(N - i - 1)) {

                    // **** create and populate root node ****
                    TreeNode root = new TreeNode(0);
                    root.left   = left;
                    root.right  = right;

                    // **** add tree to list ****
                    lst.add(root);
                }
            }
        }

        // **** return list of trees ****
        return lst;
    }



    // **** for memoization (caching) ****
    private static Map<Integer, List<TreeNode>> cache = new HashMap<Integer, List<TreeNode>>();
    

    /**
     * Return a list of all possible full binary trees with N nodes.
     * Each element of the answer is the root node of one possible tree.
     * 1 <= N <= 20
     * Recursive function.
     * Uses memoization (caching).
     */
    static List<TreeNode> allPossibleFBTMemo(int N) {

        // **** list of compliant binary tree(s) ****
        List<TreeNode> lst = new ArrayList<TreeNode>();

        // **** empty list ****
        if (N % 2 == 0)
            return lst;

        // ***** check if in cache ****
        if (cache.containsKey(N)) {

            // ???? ????
            System.out.println("allPossibleFBTMemo <<< cache hit N: " + N);

            // **** cache this tree ****
            return cache.get(N);
        }

        // **** tree with single node ****
        if (N == 1) {

            // **** add node to list ****
            lst.add(new TreeNode(0));

            // **** cache this tree ****
            cache.put(N, lst);

            // ???? ????
            System.out.println("allPossibleFBTMemo <<< cached N: " + N);

            // **** return this list ****
            return lst;
        }

        // **** *****
        for (int i = 0; i < N; i++) {

            // **** go into left tree ****
            List<TreeNode> left = allPossibleFBTMemo(i);

            // **** go into right tree ****
            List<TreeNode> right = allPossibleFBTMemo(N - i - 1);

            // **** add node to the tree ****
            for (TreeNode l : left) {
                for (TreeNode r : right) {
                    lst.add(new TreeNode(0, l, r));
                }
            }
        }

        // **** cache this tree ****
        cache.put(N, lst);

        // ???? ????
        System.out.println("allPossibleFBTMemo <<< cached N: " + N);

        // **** return this list ****
        return lst;
    }


    /**
     * Return a string with the list of trees.
     */
    static String displayBTList(List<TreeNode> lst) throws Exception {

        // **** ****
        StringBuilder sb = new StringBuilder();

        try {
            // **** generate the string for all BTs ****
            sb.append("[");
            for (int i = 0; i < lst.size(); i++) {

                bfsTraversal(lst.get(i), sb);

                if (i < lst.size() - 1)
                    sb.append(",");
            }
            sb.append("]");
        } catch (Exception e) {
            System.out.println("displayBTList <<<< EXCEPTION ==>" + e.getMessage() + "<==");
        }

        // **** return the string ****
        return sb.toString();
    }


    /**
     * Test scaffolding.
     */
    public static void main(String[] args) throws Exception{

        // **** ****
        List<TreeNode> lst = null;

        // **** open scanner ****
        Scanner sc = new Scanner(System.in);

        // **** read N ****
        int N = sc.nextInt();

        // **** close scanner ****
        sc.close();

        // ???? ????
        System.out.println("main <<< N: " + N);

        // **** check number of nodes in the BT ****
        if ((N < 1) || N > 20) {
            throw new Exception("EXCEPTION - N > 20");
        }

        // **** generate list of full binary trees ****
        lst = allPossibleFBT(N);

        // **** display the binary tree list ****
        System.out.println("main <<< lst " + displayBTList(lst));

        // **** display the length of the list ****
        System.out.println("main <<< lst.size: " + lst.size());

        // **** generate list of full binary trees (with memoization) ****
        lst = allPossibleFBTMemo(N);     

        // **** display the binary tree list ****
        System.out.println("main <<< lst " + displayBTList(lst));

        // **** display the length of the list ****
        System.out.println("main <<< lst.size: " + lst.size());

        // ???? display the cache ????
        // System.out.println("main <<< cache: " + cache.toString());

        // ???? ????
        System.out.println("main <<< String max length: " + Integer.MAX_VALUE);
    }

 }