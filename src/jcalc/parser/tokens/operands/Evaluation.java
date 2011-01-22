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

/*
 * Evaluation.java
 *
 * Created on May 8, 2007, 6:59 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package jcalc.parser.tokens.operands;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.ObjectType;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.ReferenceType;
import com.sun.org.apache.bcel.internal.generic.Type;
import java.util.*;
import jcalc.base.*;
import jcalc.parser.*;
import jcalc.parser.Compiler;
import jcalc.parser.tokens.Operand;
import jcalc.parser.tree.Trees;

/**
 *
 * @author Benjamin J. Land
 */
public class Evaluation extends Operand implements FieldToken {
    
    private String name;
    private String sign;
    private Expression exp;
    
    /** Creates a new instance of Evaluation */
    public Evaluation(String name, String data, String sign, Expression exp) {
        super(data);
        this.name = name;
        this.sign = sign;
        this.exp = exp;
    }
    
    public Token clone() {
        return new Evaluation(name,getData(), sign, exp);
    }
    
    public static Evaluation genEvaluation(String rawData, String sign, BaseCalc bc) {
        List<Token> toks = bc.getLexer().analyse(rawData);
        String fancyData = Trees.genTree(toks).postfix();
        Expression exp = Compiler.genExpression(toks);
        String name = "exp" + (toks.hashCode() ^ sign.hashCode());
        name = name.replace('-','_');
        return new Evaluation(name, fancyData, sign, exp);
    }
    
    public ReferenceType getObjectType() {
        return new ObjectType("java.util.Vector");
    }
    
    public String getSign() {
        return sign;
    }
    
    public String getName() {
        return name;
    }
    
    public Object getFieldObject() {
        Vector object = new Vector();
        object.add(exp);
        object.add(sign);
        return object;
    }
    
    public Class<?> getFieldClass() {
        return Vector.class;
    }
    
    public static Number evaluate(Expression exp, String sign) {
        Number value = exp.evaluate();
        if (sign.equals("+")) {
            if (value instanceof Value) {
                return ((Value)value).positate((Value)value);
            } else {
                return Math.abs(value.doubleValue());
            }
        } else if (sign.equals("-")) {
            if (value instanceof Value) {
                return ((Value)value).negate((Value)value);
            } else {
                return -value.doubleValue();
            }
        } else {
            return value;
        }
    }
    
    public Number evaluate() {
        return evaluate(exp, sign);
    }
    
    public void genCode(ClassGen cg, ConstantPoolGen cp, InstructionList il, InstructionFactory f) {
        il.append(f.createLoad(Type.OBJECT, 0));
        il.append(f.createFieldAccess(cg.getClassName(), getName(), getObjectType(), Constants.GETFIELD));
        il.append(new PUSH(cp, 0));
        il.append(f.createInvoke("java.util.Vector", "get", Type.OBJECT, new Type[] { Type.INT }, Constants.INVOKEVIRTUAL));
        il.append(f.createCheckCast(new ObjectType("jcalc.base.Expression")));
        il.append(f.createLoad(Type.OBJECT, 0));
        il.append(f.createFieldAccess(cg.getClassName(), getName(), getObjectType(), Constants.GETFIELD));
        il.append(new PUSH(cp, 1));
        il.append(f.createInvoke("java.util.Vector", "get", Type.OBJECT, new Type[] { Type.INT }, Constants.INVOKEVIRTUAL));
        il.append(f.createCheckCast(new ObjectType("java.lang.String")));
        il.append(f.createInvoke("jcalc.parser.tokens.operands.Evaluation", "evaluate", new ObjectType("java.lang.Number"), new Type[] { new ObjectType("jcalc.base.Expression"), new ObjectType("java.lang.String") }, Constants.INVOKESTATIC));
    }
    
    public boolean equals(Object o) {
        return (o instanceof Evaluation) && ((Evaluation)o).getName().equals(getName()) && ((Evaluation)o).getSign().equals(getSign());
    }
    
    public int hashCode() {
        return getName().hashCode() ^ getSign().hashCode();
    }
    
    public int compareTo(FieldToken f) {
        return getName().compareTo(f.getName());
    }
    
}
