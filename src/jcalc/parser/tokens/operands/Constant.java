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
 * Class Constant
 * @author Benjamin J. Land
 */
public class Constant extends Operand implements FieldToken, StaticToken {
    
    private Number value;
    
    /** Creates a new instance of Constant */
    public Constant(String data) {
        this(data,"");
    }
    
    public Constant(String number, String sign) {
        super(sign + number);
        value = new Double(sign + number).doubleValue();
    }
    
    public Constant(String number, String sign, Value value) {
        super(sign + number);
        this.value = value.fromDouble(new Double(sign + number).doubleValue());
    }
    
    public Constant(Number num) {
        super(num.toString());
        value = num;
    }
    
    public Number evaluate() {
        return value;
    }

    public ReferenceType getObjectType() {
        return new ObjectType("java.lang.Number");
    }

    public String getName() {
        return "C" + Integer.toString(hashCode()).replace('-','_');
    }

    public Class<?> getFieldClass() {
        return Number.class;
    }

    public Object getFieldObject() {
        return value;
    }

    public int hashCode() {
        return value.hashCode();
    }

    public boolean equals(Object o) {
        return (o instanceof Constant) && ((Constant)o).getName().equals(getName());
    }

    public int compareTo(FieldToken f) {
        return getName().compareTo(f.getName());
    }
    
    public Token clone() {
        return new Constant(getData());
    }
    
    public void genCode(ClassGen cg, ConstantPoolGen cp, InstructionList il, InstructionFactory f) {
        il.append(f.createLoad(Type.OBJECT, 0));
        il.append(f.createFieldAccess(cg.getClassName(), getName(), getObjectType(), Constants.GETFIELD));
    }
    
}
