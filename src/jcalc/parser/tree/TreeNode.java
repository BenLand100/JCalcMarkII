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

/**
 * Class TreeNode
 * @author Benjamin J. Land
 */
public class TreeNode<E extends Token> implements Iterable<TreeNode<? extends Token>>, Cloneable {
        
        E value;
        
        TreeNode left = null;
        TreeNode right = null;
        
        public TreeNode(E val) {
            value = val;
        }
        
        public <T extends Token> boolean replace(T find, T replace) {
            boolean found = false;
            if (value.equals(find)) {
                value = (E) replace;
                found = true;
            } 
            return (left != null ? left.replace(find, replace) : false) | (right != null ? right.replace(find, replace) : false) | found;
        }
        
        @Deprecated
        public Iterator<TreeNode<? extends Token>> iterator() {
            return new TreeIterator(this);
        }
        
        public LinkedList<Token> getTokens() {
            LinkedList<Token> toks = new LinkedList<Token>();
            addTokens(toks);
            return toks;
        }
        
        protected void addTokens(LinkedList<Token> toks) {
            if (left != null) left.addTokens(toks);
            if (right != null) right.addTokens(toks);
            toks.add(get());
        }
        
        public void setLeft(TreeNode t) {
            left = t;
        }
        
        public void setRight(TreeNode t) {
            right = t;
        }
        
        public TreeNode<? extends Token> getLeft() {
            return left;
        }
        
        public TreeNode<? extends Token> getRight() {
            return right;
        }
        
        public E get() {
            return value;
        }
        
        public String infix() {
            return (left == null ? "" : (left.value instanceof Operand ? left.infix() : "(" + left.infix() + ")")) +
                    (value instanceof Operand ? value : " " + value + " ") +
                    (right == null ? "" : (right.value instanceof Operand ? right.infix() : "(" + right.infix() + ")"));
        }
        
        public String postfix() {
            return (left == null ? "" : left.postfix()) + (right == null ? "" : right.postfix()) + value + " ";
        }
        
        public String toString() {
            return postfix();
        }
        
        public TreeNode<E> clone() {
            TreeNode<E> root = new TreeNode<E>(value);
            if (getLeft() != null) 
                root.setLeft(getLeft().clone());
            if (getRight() != null) 
                root.setRight(getRight().clone());
            return root;
        }
        
        public Number eval() {
            if (value instanceof Operator) {
                return ((Operator)value).preform(getLeft().eval(), getRight().eval());
            } else {
                return ((Operand)value).evaluate();
            }
        }
        
    }
