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

package jcalc.parser;

import java.util.*;
import jcalc.base.*;
import jcalc.parser.tokens.operands.Refrence;
import jcalc.parser.tree.*;


/**
 * Class Interpreter
 * @author Benjamin J. Land
 */
public class Interpreter {
    
    /** Creates a new instance of Interpreter */
    private Interpreter() {
    }
    
    public static Function genFunction(List<Token> toks, String name, Variable... relative) {
        final TreeNode root = Trees.simplify(Trees.genTree(toks));
        Variable[] tempArray = new Variable[relative.length];
        for (int i = 0; i < relative.length; i++) {
            tempArray[i] = new Variable("arg" + i);
            root.replace(new Refrence(relative[i]), new Refrence(tempArray[i]));
        }
        final Variable[] instanceVars = tempArray;
        return new Function(name) {
            public Number valueAt(Number... args) {
                for (int i = 0; i < instanceVars.length; i++) {
                    instanceVars[i].setValue(args[i]);
                }
                return root.eval();
            }
        };
    }
    
    public static Expression genExpression(List<Token> toks) {
        toks = Trees.simplify(Trees.genTree(toks)).getTokens();
        final TreeNode root = Trees.genTree(toks);
        return new Expression(root.infix()) {
            public Number evaluate() {
                return root.eval();
            }
        };
    }
    
}
