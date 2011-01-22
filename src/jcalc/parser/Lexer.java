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
import java.util.regex.*;
import jcalc.base.*;
import jcalc.parser.tokens.*;
import jcalc.parser.tokens.operands.*;
import jcalc.parser.tokens.operators.Assign;

/**
 * Class Lexer
 * @author Benjamin J. Land
 */
public class Lexer {
    
    public static final String VARIABLE = "[a-zA-Z]\\w*(?!\\w)(?!\\[)";
    public static final String FUNCTION = "[a-zA-Z]\\w*(?=\\[)";
    public static final String OPERATOR = "[+-/*^%]|(?:\\>\\>)";
    public static final String SIGNED = "(?:(?<=(?:"+OPERATOR+")|(?:^))(?:\\+|\\-)\\s*)?";
    public static final String NUMBER = "\\d+(?:(?=\\.)(?:\\.\\d+(?:(?=e)(?:e\\d+))?))?";
    public static final String GROUPER = "[\\[\\]\\(\\)]";
    
    public static final String SPLITTER = "\\s*("+SIGNED+")(?:("+NUMBER+")|("+OPERATOR+")|("+GROUPER+")|("+VARIABLE+")|("+FUNCTION+"))\\s*";
    
    private BaseCalc bc;
    
    /** Creates a new instance of Lexer */
    public Lexer(BaseCalc baseCalc) {
        bc = baseCalc;
    }
    
    public LinkedList<Token> analyse(String data) {
        Matcher m = Pattern.compile(SPLITTER).matcher(data);
        LinkedList<Token> parsed = new LinkedList<Token>();
        Stack<Token> oop = new Stack<Token>();
        int tokEnd = 0;
        while (m.find()) {
            if (m.start() != tokEnd) throw new RuntimeException("InvalidToken: " + data.substring(tokEnd, m.start()));
            tokEnd = m.end();
            String item;
            String sign = m.group(1).trim();
            for (int i = 2; i <= 6; i++) {
                if ((item = m.group(i)) != null) {
                    item = item.trim();
                    switch (i) {
                        case 2:
                            parsed.add(new Constant(item, sign, bc.getPrecision()));
                            break;
                        case 3:
                            if (item.equals(">>")) {
                                m.find();
                                Refrence ref = new Refrence(m.group(5), m.group(1), bc);
                                Operator op = new Assign(item, ref.getVar());
                                while (!oop.empty() && (!(oop.peek() instanceof Grouper) && ((Operator)oop.peek()).precedes(op)))
                                    parsed.add(oop.pop());
                                oop.push(op);
                                parsed.add(ref);
                            } else {
                                Operator op = Operator.genOperator(item);
                                while (!oop.empty() && (!(oop.peek() instanceof Grouper) && ((Operator)oop.peek()).precedes(op)))
                                    parsed.add(oop.pop());
                                oop.push(op);
                            }
                            break;
                        case 4:
                            Grouper g = new Grouper(item);
                            if (g.isCloser()) {
                                if (oop.empty()) throw new RuntimeException("Missing Opener");
                                while (!(oop.peek() instanceof Grouper))
                                    parsed.add(oop.pop());
                                oop.pop();
                            } else {
                                if (sign.equals("")) {
                                    oop.push(g);
                                } else {
                                    int start = m.start(4) + 1;
                                    if (data.charAt(start) == ')') throw new RuntimeException("Empty Evaluation");
                                    int end = start;
                                    int depth = 0;
                                    String rawData = null;
                                    evaluationParser:
                                        for (; end < data.length(); end++) {
                                            switch (data.charAt(end)) {
                                                case '(':
                                                    depth++;
                                                    break;
                                                case ')':
                                                    if (depth == 0) {
                                                        rawData = data.substring(start, end);
                                                        end++;
                                                        break evaluationParser;
                                                    }
                                                    depth--;
                                                    break;
                                            }
                                        }
                                        if (rawData == null) throw new RuntimeException("Missing Closer");
                                        parsed.add(Evaluation.genEvaluation(rawData, m.group(1), bc));
                                        m = m.region(end, data.length());
                                        tokEnd = end;
                                }
                            }
                            break;
                        case 5:
                            parsed.add(new Refrence(item, sign, bc));
                            break;
                        case 6:
                            LinkedList<String> args = new LinkedList<String>();
                            int start = m.end() + 1;
                            int end = start;
                            if (data.charAt(start) != ']') {
                                int depth = 0;
                                functArgsParser:
                                    for (; end < data.length(); end++) {
                                        switch (data.charAt(end)) {
                                            case ',':
                                                if (depth == 0) {
                                                    args.add(data.substring(start, end));
                                                    start = end + 1;
                                                }
                                                break;
                                            case '[':
                                                depth++;
                                                break;
                                            case ']':
                                                if (depth == 0) {
                                                    args.add(data.substring(start, end));
                                                    end++;
                                                    break functArgsParser;
                                                }
                                                depth--;
                                                break;
                                        }
                                    }
                            } else {
                                end++;
                            }
                            if (data.charAt(end - 1) != ']') throw new RuntimeException("Incomplete Function Definition");
                            Function funct = bc.getFunction(item);
                            Vector<Expression> simpleArgs = funct.simplifyArgs(args, bc);
                            String dat = data.substring(m.start(),end);
                            parsed.add(new Extension(funct, m.group(1), simpleArgs.toArray(new Expression[0]), dat));
                            m = m.region(end, data.length());
                            tokEnd = end;
                    }
                    break;
                }
            }
        }
        while (!oop.empty()) {
            if (oop.peek() instanceof Grouper) throw new RuntimeException("Missing Closer");
            parsed.add(oop.pop());
        }
        if (parsed.size() == 0) throw new RuntimeException("Not an Expression");
        return parsed;
    }
}
