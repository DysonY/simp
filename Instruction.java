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
}
