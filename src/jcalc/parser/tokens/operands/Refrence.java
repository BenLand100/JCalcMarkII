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

package jcalc.parser.tokens.operands;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.ObjectType;
import com.sun.org.apache.bcel.internal.generic.ReferenceType;
import com.sun.org.apache.bcel.internal.generic.Type;
import jcalc.base.*;
import jcalc.parser.*;
import jcalc.parser.tokens.*;

/**
 * Class Refrence
 * @author Benjamin J. Land
 */
public class Refrence extends Operand implements FieldToken {
    
    private Variable var;
    private BaseCalc parent;
    
    /**
     * Makes a cloneable Refrence
     */
    private Refrence(String data) {
        super(data);
    }
    
    /** Creates a new instance of Refrence */
    public Refrence(String data, BaseCalc baseCalc) {
        super(data);
        parent = baseCalc;
        var = baseCalc.getVariable(data);
        if (var == null) throw new RuntimeException("Undefined Variable: " + data);
    }
    
    public Refrence(String data, final String sign, BaseCalc baseCalc) {
        super(sign + data);
        parent = baseCalc;
        final Variable temp = baseCalc.getVariable(data);
        if (temp == null) throw new RuntimeException("Undefined Variable: " + data);
        if (sign.equals("")) {
            var = temp;
        } else {
            if (sign.equals("-")) {
                var = new Variable(temp.getName(), temp.getValue()) {
                    public Number getValue() {
                        if (temp.getValue() instanceof Value) {
                            return  ((Value)temp.getValue()).negate((Value)temp.getValue());
                        } else {
                            return 0D - temp.getValue().doubleValue();
                        }
                    }
                    public void setValue(Number n) {
                        temp.setValue(n);
                    }
                };
            } else {
                var = new Variable(temp.getName(), temp.getValue()) {
                    public Number getValue() {
                        if (temp.getValue() instanceof Value) {
                            return  ((Value)temp.getValue()).positate((Value)temp.getValue());
                        } else {
                            return Math.abs(temp.getValue().doubleValue());
                        }
                    }
                    public void setValue(Number n) {
                        temp.setValue(n);
                    }
                };
            }
        }
    }
    
    public Refrence(Variable var) {
        super(var.getName());
        this.var = var;
        parent = null;
    }
    
    public Number evaluate() {
        return var.getValue();
    }
    
    public BaseCalc getBaseCalc() {
        return parent;
    }
    
    public String getName() {
        return var.getName();
    }
    
    public ReferenceType getObjectType() {
        return new ObjectType("jcalc.base.Variable");
    }
    
    public Object getFieldObject() {
        return getVar();
    }
    
    public Class<?> getFieldClass() {
        return Variable.class;
    }
    
    public Variable getVar() {
        return var;
    }
    
    public void setValue(double d) {
        var.setDouble(d);
    }
    
    public int hashCode() {
        return var.hashCode();
    }
    
    public boolean equals(Object o) {
        return (o instanceof FieldToken) && ((FieldToken)o).getName().equals(getName()) ||
                ((o instanceof Variable) && ((Variable)o).equals(var)) ||
                super.equals(o);
    }
    
    public int compareTo(FieldToken f) {
        return getName().compareTo(f.getName());
    }
    
    public Token clone() {
        Refrence ref = new Refrence(getData());
        ref.parent = parent;
        ref.var = var;
        return ref;
    }
    
    /**
     * The Variable var of the name getData() must be added to the class previously
     */
    public void genCode(ClassGen cg, ConstantPoolGen cp, InstructionList il, InstructionFactory f) {
        il.append(f.createLoad(Type.OBJECT, 0));
        il.append(f.createFieldAccess(cg.getClassName(), getData(), getObjectType(), Constants.GETFIELD));
        il.append(f.createInvoke("jcalc.base.Variable", "getValue", new ObjectType("java.lang.Number"), Type.NO_ARGS, Constants.INVOKEVIRTUAL));
    }
}
