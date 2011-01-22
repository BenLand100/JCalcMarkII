/**
 *  Copyright 2009 by Benjamin J. Land (a.k.a. BenLand100)
 *
 *  This file is part of JCalc.
 *
 *  JCalc is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  JTuner is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with JCalc. If not, see <http://www.gnu.org/licenses/>.
 */

package jcalc.parser.tree;

import java.util.*;
import jcalc.parser.*;
import jcalc.parser.tokens.*;
import jcalc.parser.tokens.operands.Constant;

/**
 * Class Trees
 * @author Benjamin J. Land
 */
public class Trees {
    
    /** Creates a new instance of Trees */
    
    private Trees() {
    }
    
    public static boolean replace(TreeNode<? extends Token> tree, Token find, Token replace) {
        tree = tree.clone();
        tree.replace(find, replace);
        return true;
    }
    
    @Deprecated
    public static LinkedList<Token> getTokens(TreeNode<? extends Token> tree) {
        System.out.println(tree);
        LinkedList<Token> toks = new LinkedList<Token>();
        for (TreeNode<? extends Token> node : tree) {
            toks.add(node.get());
            System.out.println(node.get());
        }
        return toks;
    }
    
    public static TreeNode<? extends Token> simplify(TreeNode<? extends Token> tree) {
        if (tree == null) throw new RuntimeException("Operator Misplaced");
        if (tree.get() instanceof Operator) {
            TreeNode<? extends Token> left = simplify(tree.getLeft());
            TreeNode<? extends Token> right = simplify(tree.getRight());
            if (left.get() instanceof StaticToken && right.get() instanceof StaticToken)  {
                return new TreeNode<Constant>(new Constant(((Operator)tree.get()).preform(left.eval(), right.eval())));
            } else {
                TreeNode<Operator> op = new TreeNode<Operator>((Operator)tree.get().clone());
                op.setLeft(left);
                op.setRight(right);
                return op;
            }
        } else {
            return new TreeNode<Token>(tree.get());
        }
    }
    
    public static TreeNode<Token> genTree(List<Token> toks) {
        LinkedList<Token> data = new LinkedList<Token>(toks);
        Stack<TreeNode<? extends Token>> parents = new Stack<TreeNode<? extends Token>>();
        TreeNode root = new TreeNode(data.removeLast());
        TreeNode cur = root;
        Token last;
        while (data.size() > 0) {
            if ((last = data.removeLast()) instanceof Operator) {
                TreeNode op = new TreeNode(last);
                if (cur.getRight() == null) {
                    cur.setRight(op);
                    parents.push(cur);
                    cur = op;
                } else {
                    cur.setLeft(op);
                    parents.push(cur);
                    cur = op;
                }
            } else {
                TreeNode v = new TreeNode(last);
                if (cur.getRight() == null) {
                    cur.setRight(v);
                } else {
                    cur.setLeft(v);
                    while((cur != root) && (cur = parents.pop()).getLeft() != null);
                }
            }
        }
        return root;
    }
    
}
