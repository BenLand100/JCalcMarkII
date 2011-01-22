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
import com.sun.org.apache.bcel.internal.generic.ArrayType;
import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.ObjectType;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.ReferenceType;
import com.sun.org.apache.bcel.internal.generic.Type;
import java.util.*;
import jcalc.base.*;
import jcalc.parser.*;
import jcalc.parser.tokens.*;

/**
 * Class Extension
 * @author Benjamin J. Land
 */
public class Extension extends Operand implements FieldToken {
    
    Function funct;
    Expression[] args;
    
    /** Creates a new instance of Extension */
    public Extension(Function funct, Expression[] args, String data) {
        super(data);
        this.funct = funct;
        this.args = args;
    }
    
    public Extension(final Function funct, String sign, Expression[] args, String data) {
        super(data);
        this.args = args;
        if (sign.equals("")) {
            this.funct = funct;
        } else if (sign.equals("-")) {
            this.funct = new Function(funct.getName()) {
                public Number valueAt(Number... args) {
                    Number value = funct.valueAt(args);
                    if (value instanceof Value) {
                        return ((Value)value).negate((Value)value);
                    } else {
                        return -value.doubleValue();
                    }
                }
            };
        } else {
            this.funct = new Function(funct.getName()) {
                public Number valueAt(Number... args) {
                    Number value = funct.valueAt(args);
                    if (value instanceof Value) {
                        return ((Value)value).positate((Value)value);
                    } else {
                        return Math.abs(value.doubleValue());
                    }
                }
            };
        }
    }
    
    public Token clone() {
        return new Extension(funct, args, getData());
    }
    
    public Number evaluate() {
        return funct.valueAt(args);
    }
    
    public String getName() {
        return funct.getName();
    }
    
    public ReferenceType getObjectType() {
        return new ObjectType("java.util.Vector");
    }
    
    public Object getFieldObject() {
        Vector obj = new Vector();
        obj.add(funct);
        for (Number num : args)
            obj.add(num);
        return obj;
    }
    
    public Class<?> getFieldClass() {
        return Vector.class;
    }
    
    public void genCode(ClassGen cg, ConstantPoolGen cp, InstructionList il, InstructionFactory f) {
        il.append(f.createLoad(Type.OBJECT, 0));
        il.append(f.createFieldAccess(cg.getClassName(), getName(), new ObjectType("java.util.Vector"), Constants.GETFIELD));
        il.append(new PUSH(cp, 0));
        il.append(f.createInvoke("java.util.Vector", "get", Type.OBJECT, new Type[] { Type.INT }, Constants.INVOKEVIRTUAL));
        il.append(f.createCheckCast(new ObjectType("jcalc.base.Function")));
        il.append(new PUSH(cp, args.length));
        il.append(f.createNewArray(new ObjectType("java.lang.Number"), (short) 1));
        for (int i = 0; i < args.length; i++) {
            il.append(InstructionConstants.DUP);
            il.append(new PUSH(cp, i));
            il.append(f.createLoad(Type.OBJECT, 0));
            il.append(f.createFieldAccess(cg.getClassName(), getName(), new ObjectType("java.util.Vector"), Constants.GETFIELD));
            il.append(new PUSH(cp, i + 1));
            il.append(f.createInvoke("java.util.Vector", "get", Type.OBJECT, new Type[] { Type.INT }, Constants.INVOKEVIRTUAL));
            il.append(f.createCheckCast(new ObjectType("jcalc.base.Expression")));
            il.append(f.createInvoke("jcalc.base.Expression", "evaluate", new ObjectType("java.lang.Number"), Type.NO_ARGS, Constants.INVOKEVIRTUAL));
            il.append(InstructionConstants.AASTORE);
        }
        il.append(f.createInvoke("jcalc.base.Function", "valueAt", new ObjectType("java.lang.Number"), new Type[] { new ArrayType(new ObjectType("java.lang.Number"), 1) }, Constants.INVOKEVIRTUAL));
    }
    
    public boolean equals(Object o) {
        return (o instanceof FieldToken) && ((FieldToken)o).getName().equals(getName());
    }
    
    public int compareTo(FieldToken f) {
        return getName().compareTo(f.getName());
    }
    
}
