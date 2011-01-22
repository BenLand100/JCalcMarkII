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

package jcalc;

import java.util.*;
import jcalc.base.*;
import jcalc.gui.JCalc;
import jcalc.parser.*;
import jcalc.parser.Compiler;


/**
 * Responsible for testing and starting the JCalc subsystem.
 *
 * @author Benjamin J. Land
 * @version 1.0
 */
public class Main {
    
    /** Creates a new instance of Main */
    private Main() {
    }
    
    private static void testCompiledVsInterpreted(Lexer l) {
        LinkedList<Token> toks = l.analyse("(5*4)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)*(5*4)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)/(0.75 / 5)*3+9*2/6/6*6+(8/4)*2+(0.75/2)");
        Expression compiled = Compiler.genExpression(toks);
        Expression interpreted = Interpreter.genExpression(toks);
        long compTime = System.nanoTime();
        System.out.println("Compiled: " + compiled.evaluate());
        for (int i = 0; i < 10000; i++) compiled.evaluate();
        compTime = System.nanoTime() - compTime;
        System.out.println("Time: " + compTime);
        long interTime = System.nanoTime();
        System.out.println("Interpreted: " + interpreted.evaluate());
        for (int i = 0; i < 10000; i++) interpreted.evaluate();
        interTime = System.nanoTime() - interTime;
        System.out.println("Time: " + interTime);
    }
    
    private static void testFunctions(BaseCalc bc) {
        Lexer l = bc.getLexer();
        LinkedList<Token> toks = l.analyse("X ^ Y");
        Function f1 = Compiler.genFunction(toks, "Power", bc.getVariable("X"));
        Function f2 = Interpreter.genFunction(toks, "Power", bc.getVariable("X"));
        System.out.println(f1.valueAt(2));
        System.out.println(f2.valueAt(2));
    }
    
    private static void testExtensions(BaseCalc bc) {
        Lexer l = bc.getLexer();
        Function pow = Compiler.genFunction(l.analyse("X^Y"),"Pow", bc.getVariable("X"), bc.getVariable("Y"));
        bc.regFunction(pow);
        Expression test = Compiler.genExpression(l.analyse("Pow[5,3]"));
        System.out.println(test.evaluate());
    }
    
    /**
     * Starts JCalc 
     *
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        BaseCalc bc = new BaseCalc();
        Lexer l = bc.getLexer();
        JCalc frame = new JCalc(bc);
        frame.setVisible(true);
        System.out.println(bc.evaluate("Sin[Radians[90]]"));
        testExtensions(bc);
        testCompiledVsInterpreted(l);
        testFunctions(bc);
        //TreeNode<? extends Token> root = Trees.simplify(Trees.genTree(l.analyse("1+2+3+4+5")));
        //System.out.println(root);
        //System.out.println(Trees.getTokens(root));
    }
    
}
