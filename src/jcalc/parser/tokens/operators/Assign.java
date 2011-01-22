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

package jcalc.parser.tokens.operators;

import jcalc.base.Variable;
import jcalc.parser.FieldToken;
import jcalc.parser.Token;
import jcalc.parser.tokens.Operator;
import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.ObjectType;
import com.sun.org.apache.bcel.internal.generic.ReferenceType;
import com.sun.org.apache.bcel.internal.generic.Type;
/**
 * Nonstandard operation, dosen't do much preforming
 * @author Benjamin J. Land
 */
public class Assign extends Operator implements FieldToken {
    
    private Variable var;
    
    /** Creates a new instance of Assign */
    public Assign(String data, Variable var) {
        super(data);
        this.var = var;
        if (var == null) throw new RuntimeException("Undefined Variable: " + data);
    }
    
    public Token clone() {
        return new Assign(getData(), var);
    }
    
    public int precedence() {
        return 0;
    }
    
    public Variable getVar() {
        return var;
    }
    
    public static Number preform(Number a, Number b, Variable var) {
        var.setValue(a);
        return a;
    }
    
    public Number preform(Number a, Number b) {
        return preform(a,b,var);
    }
    
    public void genCode(ClassGen cg, ConstantPoolGen cp, InstructionList il, InstructionFactory f) {
        il.append(f.createLoad(Type.OBJECT, 0));
        il.append(f.createFieldAccess(cg.getClassName(), getName(), getObjectType(), Constants.GETFIELD));
        il.append(f.createInvoke("jcalc.parser.tokens.operators.Assign","preform",new ObjectType("java.lang.Number"),new Type[] {new ObjectType("java.lang.Number"),new ObjectType("java.lang.Number"),getObjectType()},Constants.INVOKESTATIC));
    }

    public Class<?> getFieldClass() {
        return Variable.class;
    }

    public ReferenceType getObjectType() {
        return new ObjectType("jcalc.base.Variable");
    }

    public Object getFieldObject() {
        return getVar();
    }

    public String getName() {
        return var.getName();
    }

    public int compareTo(FieldToken f) {
        return getName().compareTo(f.getName());
    }
    
    public int hashCode() {
        return getVar().hashCode();
    }
    
    public boolean equals(Object other) {
        return (other instanceof Assign) && ((Assign)other).getName().equals(getName());
    }
    
}
