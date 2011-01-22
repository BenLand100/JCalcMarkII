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
 * !!!!!!!!!!!!!!!!!!!DEFECTIVE!!!!!!!!!!!!!!!!!!!
 * Class TreeIterator
 * @author Benjamin J. Land
 */
@Deprecated
public class TreeIterator implements Iterator<TreeNode<? extends Token>> {
    
    private TreeNode<? extends Token> root;
    private TreeNode<? extends Token> next;
    private Stack<TreeNode<? extends Token>> parents = new Stack<TreeNode<? extends Token>>();
    private Stack<Boolean> right = new Stack<Boolean>();
    private boolean upped;
    
    /** Creates a new instance of TreeIterator */
    public TreeIterator(TreeNode<? extends Token> root) {
        this.root = root;
        next = root;
        while (next.get() instanceof Operator) {
            parents.push(next);
            right.push(true);
            next = next.getRight();
        }
    }
    
    public TreeNode<? extends Token> next() {
        TreeNode<? extends Token> result = next;
        try {
            if (next.get() instanceof Operator) {
                if (upped) {
                    if (right.peek()) {
                        right.pop();
                        right.push(false);
                        next = parents.peek().getLeft();
                        while (next.get() instanceof Operator) {
                            parents.push(next);
                            right.push(true);
                            next = next.getRight();
                        }
                    } else {
                        right.pop();
                        upped = true;
                        next = parents.pop();
                    }
                } else {
                    while (next.get() instanceof Operator) {
                        parents.push(next);
                        right.push(true);
                        next = next.getRight();
                    }
                }
            } else {
                if (right.peek()) {
                    right.pop();
                    right.push(false);
                    next = parents.peek().getLeft();
                } else {
                    right.pop();
                    upped = true;
                    next = parents.pop();
                }
            }
        } catch (Exception e) {
            next = null;
        }
        return result;
    }
    
    public boolean hasNext() {
        return next != null;
    }
    
    public void remove() {
        throw new RuntimeException("Can not use an iterator to modify Trees.");
    }
    
}
