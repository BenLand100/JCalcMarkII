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

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.generic.ArrayType;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.FieldGen;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.MethodGen;
import com.sun.org.apache.bcel.internal.generic.ObjectType;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.Type;
import java.util.*;
import jcalc.base.*;
import jcalc.parser.tree.*;
import jcalc.parser.tokens.operands.Refrence;

/**
 * Class Compiler
 * @author Benjamin J. Land
 */
public class Compiler extends ClassLoader implements Constants {
    
    private static Compiler instance = new Compiler();
    private static TreeSet<String> mathExtendedFuncts = new TreeSet<String>(Arrays.asList(MathExtended.functions));
    
    /** Creates a new instance of Compiler */
    protected Compiler() {
        super();
    }
    
    protected static void genericConstructor(String name, ArrayList<FieldToken> fields, String type, ClassGen cg, ConstantPoolGen cp, InstructionFactory f) {
        InstructionList il = new InstructionList();
        Type[] types = new Type[fields.size()];
        String[] names = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            FieldToken field = fields.get(i);
            types[i] = field.getObjectType();
            names[i] = field.getName();
            cg.addField(new FieldGen(0, field.getObjectType(), field.getName(), cp).getField());
        }
        MethodGen method = new MethodGen(ACC_PUBLIC, Type.VOID, types, names, "<init>", name, il, cp);
        il.append(f.createLoad(Type.OBJECT, 0));
        il.append(new PUSH(cp, name));
        il.append(f.createInvoke(type, "<init>", Type.VOID, new Type[] { Type.STRING }, Constants.INVOKESPECIAL));
        for (FieldToken field : fields) {
            il.append(f.createLoad(Type.OBJECT, 0));
            il.append(f.createLoad(Type.OBJECT, 1 + fields.indexOf(field)));
            il.append(f.createFieldAccess(cg.getClassName(), field.getName(), field.getObjectType(), Constants.PUTFIELD));
        }
        il.append(f.createReturn(Type.VOID));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }
    
    protected static void evaluateFromList(List<Token> list, String name, ClassGen cg, ConstantPoolGen cp, InstructionFactory f) {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(ACC_PUBLIC, new ObjectType("java.lang.Number"), Type.NO_ARGS, new String[] {  }, "evaluate", name, il, cp);
        for (Token t : list)
            t.genCode(cg, cp, il, f);
        il.append(f.createReturn(Type.OBJECT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }
    
    protected static void valueAtFromList(List<Token> list, ArrayList<Variable> vars, String name, ClassGen cg, ConstantPoolGen cp, InstructionFactory f) {
        InstructionList il = new InstructionList();
        MethodGen method = new MethodGen(ACC_PUBLIC, new ObjectType("java.lang.Number"), new Type[] { new ArrayType(new ObjectType("java.lang.Number"), 1) }, new String[] { "arg0" }, "valueAt", name, il, cp);
        for (Token t : list) {
            if ((t instanceof Refrence) && vars.contains(((Refrence)t).getVar())) {
                il.append(f.createLoad(Type.OBJECT, 1));
                il.append(new PUSH(cp, vars.indexOf(((Refrence)t).getVar())));
                il.append(InstructionConstants.AALOAD);
            } else {
                t.genCode(cg, cp, il, f);
            }
        }
        il.append(f.createReturn(Type.OBJECT));
        method.setMaxStack();
        method.setMaxLocals();
        cg.addMethod(method.getMethod());
        il.dispose();
    }
    
    protected static Class<? extends Function> makeFunctionClass(List<Token> list, String name, ArrayList<FieldToken> fields, ArrayList<Variable> relative) {
        String fullName = "jcalc.base.functions.F" + Integer.toString(list.hashCode() ^ relative.hashCode()).replace('-','_');
        try {
            Class clazz = instance.loadClass(fullName);
            if (clazz != null)
                return (Class<? extends Function>) clazz;
        } catch (Exception e) { }
        ClassGen cg = new ClassGen(fullName, "jcalc.base.Function", null, ACC_PUBLIC | ACC_SUPER, new String[] {  });
        ConstantPoolGen cp = cg.getConstantPool();
        InstructionFactory f = new InstructionFactory(cg, cp);
        genericConstructor(name, fields, "jcalc.base.Function", cg, cp, f);
        valueAtFromList(list, relative, fullName, cg, cp, f);
        JavaClass jc = cg.getJavaClass();
        byte[] data = jc.getBytes();
        return (Class<? extends Function>) instance.defineClass(fullName, data, 0, data.length);
    }
    
    protected static Class<? extends Expression> makeExpressionClass(List<Token> list, ArrayList<FieldToken> fields) {
        String name = "jcalc.base.expressions.E" + Integer.toString(list.hashCode()).replace('-','_');
        try {
            Class clazz = instance.loadClass(name);
            if (clazz != null)
                return (Class<? extends Expression>) clazz;
        } catch (Exception e) { }
        ClassGen cg = new ClassGen(name, "jcalc.base.Expression", null, ACC_PUBLIC | ACC_SUPER, new String[] {  });
        ConstantPoolGen cp = cg.getConstantPool();
        InstructionFactory f = new InstructionFactory(cg, cp);
        genericConstructor(name, fields, "jcalc.base.Expression", cg, cp, f);
        evaluateFromList(list, name, cg, cp, f);
        JavaClass jc = cg.getJavaClass();
        byte[] data = jc.getBytes();
        return (Class<? extends Expression>) instance.defineClass(name, data, 0, data.length);
    }
    
    private static ArrayList<FieldToken> getFields(List<Token> toks) {
        TreeSet<FieldToken> fieldSet = new TreeSet<FieldToken>();
        for (Token tok : toks)
            if (tok instanceof FieldToken)
                fieldSet.add((FieldToken)tok);
        return new ArrayList<FieldToken>(fieldSet);
    }
    
    private static Class<? extends FieldToken>[] getClasses(ArrayList<FieldToken> fields) {
        ArrayList<Class<? extends Object>> classes = new ArrayList<Class<? extends Object>>();
        for (FieldToken field : fields)
            classes.add(field.getFieldClass());
        return (Class<? extends FieldToken>[]) classes.toArray(new Class[0]);
    }
    
    private static Object[] getObjects(List<FieldToken> fields) {
        Object[] obj = new Object[fields.size()];
        int i = 0;
        for (FieldToken field : fields)
            obj[i++] = field.getFieldObject();
        return obj;
    }
    
    public static Function genMathFunction(String name, String fnName, int args) {
        String fullName = "jcalc.base.functions." + fnName;
        Class<? extends Function> clazz = null;
        try {
            clazz = (Class<? extends Function>)instance.loadClass(fullName);
        } catch (Exception e) { }
        try {
            if (clazz == null) {
                ClassGen cg = new ClassGen(fullName, "jcalc.base.Function", null, ACC_PUBLIC | ACC_SUPER, new String[] {  });
                ConstantPoolGen cp = cg.getConstantPool();
                InstructionFactory f = new InstructionFactory(cg, cp);
                genericConstructor(fnName, new ArrayList<FieldToken>(), "jcalc.base.Function", cg, cp, f);
                InstructionList il = new InstructionList();
                MethodGen method = new MethodGen(ACC_PUBLIC, new ObjectType("java.lang.Number"), new Type[] { new ArrayType(new ObjectType("java.lang.Number"), 1) }, new String[] { "arg0" }, "valueAt", name, il, cp);
                if (mathExtendedFuncts.contains(name)) {
                    il.append(f.createLoad(Type.OBJECT, 1));
                    il.append(new PUSH(cp, 0));
                    il.append(InstructionConstants.AALOAD);
                    il.append(f.createInstanceOf(new ObjectType("jcalc.base.MathExtended")));
                    BranchInstruction _if = f.createBranchInstruction(Constants.IFEQ, null);
                    il.append(_if);
                    il.append(f.createLoad(Type.OBJECT, 1));
                    il.append(new PUSH(cp, 0));
                    il.append(InstructionConstants.AALOAD);
                    il.append(f.createInvoke("jcalc.base.MathExtended", name, new ObjectType("java.lang.Number"), Type.NO_ARGS, Constants.INVOKEINTERFACE));
                    BranchInstruction _goto= f.createBranchInstruction(Constants.GOTO, null);
                    il.append(_goto);
                    InstructionHandle _false = il.append(f.createLoad(Type.OBJECT, 1));
                    il.append(new PUSH(cp, 0));
                    il.append(InstructionConstants.AALOAD);
                    il.append(f.createInvoke("java.lang.Number", "doubleValue", Type.DOUBLE, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
                    Type[] types = new Type[args];
                    types[0] = Type.DOUBLE;
                    for (int i = 1; i < args; i++) {
                        il.append(f.createLoad(Type.OBJECT, 1));
                        il.append(new PUSH(cp, i));
                        il.append(InstructionConstants.AALOAD);
                        il.append(f.createInvoke("java.lang.Number", "doubleValue", Type.DOUBLE, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
                        types[i] = Type.DOUBLE;
                    }
                    il.append(f.createInvoke("java.lang.Math", name, Type.DOUBLE, types, Constants.INVOKESTATIC));
                    il.append(f.createInvoke("java.lang.Double", "valueOf", new ObjectType("java.lang.Double"), new Type[] { Type.DOUBLE }, Constants.INVOKESTATIC));
                    InstructionHandle _end = il.append(f.createReturn(Type.OBJECT));
                    _if.setTarget(_false);
                    _goto.setTarget(_end);
                } else {
                    Type[] types = new Type[args];
                    for (int i = 0; i < args; i++) {
                        il.append(f.createLoad(Type.OBJECT, 1));
                        il.append(new PUSH(cp, i));
                        il.append(InstructionConstants.AALOAD);
                        il.append(f.createInvoke("java.lang.Number", "doubleValue", Type.DOUBLE, Type.NO_ARGS, Constants.INVOKEVIRTUAL));
                        types[i] = Type.DOUBLE;
                    }
                    il.append(f.createInvoke("java.lang.Math", name, Type.DOUBLE, types, Constants.INVOKESTATIC));
                    il.append(f.createInvoke("java.lang.Double", "valueOf", new ObjectType("java.lang.Double"), new Type[] { Type.DOUBLE }, Constants.INVOKESTATIC));
                    il.append(f.createReturn(Type.OBJECT));
                }
                method.setMaxStack();
                method.setMaxLocals();
                cg.addMethod(method.getMethod());
                il.dispose();
                
                JavaClass jc = cg.getJavaClass();
                byte[] data = jc.getBytes();
                clazz = (Class<? extends Function>) instance.defineClass(fullName, data, 0, data.length);
            }
            return clazz.newInstance();
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
    
    public static Function genFunction(List<Token> toks, String name, Variable... relative) {
        toks = Trees.simplify(Trees.genTree(toks)).getTokens();
        ArrayList<FieldToken> fields = getFields(toks);
        Class<? extends Function> funct = makeFunctionClass(toks, name, fields, new ArrayList(Arrays.asList(relative)));
        try {
            Function f = funct.getConstructor(getClasses(fields)).newInstance(getObjects(fields));
            return f;
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
    
    public static Expression genExpression(List<Token> toks) {
        toks =Trees.simplify(Trees.genTree(toks)).getTokens();
        ArrayList<FieldToken> fields = getFields(toks);
        Class<? extends Expression> exp = makeExpressionClass(toks, fields);
        try {
            Expression e = exp.getConstructor(getClasses(fields)).newInstance(getObjects(fields));
            return e;
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
    
}
