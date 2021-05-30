public class Instruction
{
    public InstrName instr;
    public int op1;
    public int op2;

    public Instruction(InstrName instr, int op1, int op2)
    {
        this.instr = instr;
        this.op1 = op1;
        this.op2 = op2;
    }

    public void setInstruction(InstrName instr)
    {
        this.instr = instr;
    }

    // For debugging.
    public String toString()
    {
        switch (instr)
        {
            case ADD: return "add";
            case SUB: return "sub";
            case AND: return "and";
            case OR: return "or";
            case NOT: return "not";
            case XOR: return "xor";
            case INC: return "add";
            case DEC: return "sub";
            case SWAP: return "and";
            case PUSH: return "push " + op1;
            case POP: return "pop";
            case STORE: return "store " + op1 + ", " + op2;
            case LOAD: return "load " + op1;
            case BEQ: return "beq " + op1 + ", " + op2;
            case BNE: return "bne " + op1 + ", " + op2;
            case BR: return "br " + op1;
            case PRINT: return "print";
            case RET: return "ret";
            case LABEL: return ".label";
            default: return "";
        }
    }
}
