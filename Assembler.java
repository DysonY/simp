import java.io.FileNotFoundException;

public class Assembler
{
    public Instruction[] codeMemory;  // 2^16 cells of program memory
    private int[] dataMemory; // 2^16 cells of data memory
    private int pc; // program counter
    private int sp; // stack pointer
    private int ret; // return flag

    public Assembler()
    {
        //this.codeMemory = new Instruction[65535];
        this.dataMemory = new int[65535];
        this.pc = 0;
        this.sp = 0;
        this.ret = 0;
    }

    public void assemble(String filepath)
    throws FileNotFoundException, AssemblyException
    {
        InstrGenerator generator = new InstrGenerator();
        Instruction[] program = generator.assemble(filepath);
        codeMemory = program.clone();
    }

    public void run()
    {
        while (ret == 0)
        {
            if (sp < 0) return;
            runInstruction();
        }
        System.out.println("Program completed with return value " + dataMemory[sp]);
    }

    private void runInstruction()
    {
        Instruction current = codeMemory[pc];
        System.out.println(current.toString());
        int op1 = current.op1;
        int op2 = current.op2;

        switch (current.instr) {
            case STORE:
                this.dataMemory[op2] = op1;
                break;
            
            case LOAD:
                this.dataMemory[sp++] = this.dataMemory[op1];
                break;
        
            case PUSH:
                this.dataMemory[sp++] = op1;
                break;

            case POP:
                sp--;
                break;

            case ADD:
                this.dataMemory[sp - 2] = this.dataMemory[sp - 1] + this.dataMemory[sp - 2];
                sp--;
                break;
            
            case SUB:
                this.dataMemory[sp - 2] = this.dataMemory[sp - 1] - this.dataMemory[sp - 2];
                sp--;
                break;
            
            case SWAP:
                int top = this.dataMemory[sp - 1];
                this.dataMemory[sp - 1] = this.dataMemory[sp - 2];
                this.dataMemory[sp - 2] = top;
                break;

            case AND:
                this.dataMemory[sp - 2] = this.dataMemory[sp - 1] & this.dataMemory[sp - 2];
                sp--;
                break;
            
            case OR:
                this.dataMemory[sp - 2] = this.dataMemory[sp - 1] | this.dataMemory[sp - 2];
                sp--;
                break;
            
            case NOT:
                this.dataMemory[sp - 1] = ~this.dataMemory[sp - 1];
                break;
            
            case XOR:
                this.dataMemory[sp - 2] = this.dataMemory[sp - 1] ^ this.dataMemory[sp - 2];
                sp--;
                break;
            
            case INC:
                this.dataMemory[sp - 1]++;
                break;
            
            case DEC:
                this.dataMemory[sp - 1]--;
                break;

            case BEQ:
                if (this.dataMemory[sp - 1] == op1) pc = op2;
                break;

            case BNE:
                if (this.dataMemory[sp - 1] != op1) pc = op2;
                break;
            
            case BR:
                pc = op1;
                break;
            
            case PRINT:
                char c = (char) this.dataMemory[sp - 1];
                System.out.print(c);
                break;
            
            case RET:
                this.ret = 1;
                return;

            default:
                break;
        }

        pc++;
    }

    public void printProgram()
    {
        pc = 0;
        while (codeMemory[pc] != null)
        {
            System.out.println(codeMemory[pc].toString());
            pc++;
        }
    }

    public static void main(String[] args)
    throws FileNotFoundException, AssemblyException
    {
        Assembler program = new Assembler();
        program.assemble("/Users/dysonye/Desktop/Projects/Java/simp/tests/hello.txt");
        //program.printProgram();
        program.run();
        
        /*
        program.codeMemory[0] = new Instruction(InstrName.PUSH, 0, 0);
        program.codeMemory[1] = new Instruction(InstrName.PUSH, 10, 0);
        program.codeMemory[2] = new Instruction(InstrName.PUSH, 33, 0);
        program.codeMemory[3] = new Instruction(InstrName.PUSH, 100, 0);
        program.codeMemory[4] = new Instruction(InstrName.PUSH, 108, 0);
        program.codeMemory[5] = new Instruction(InstrName.PUSH, 114, 0);
        program.codeMemory[6] = new Instruction(InstrName.PUSH, 111, 0);
        program.codeMemory[7] = new Instruction(InstrName.PUSH, 87, 0);
        program.codeMemory[8] = new Instruction(InstrName.PUSH, 32, 0);
        program.codeMemory[9] = new Instruction(InstrName.PUSH, 111, 0);
        program.codeMemory[10] = new Instruction(InstrName.PUSH, 108, 0);
        program.codeMemory[11] = new Instruction(InstrName.PUSH, 108, 0);
        program.codeMemory[12] = new Instruction(InstrName.PUSH, 101, 0);
        program.codeMemory[13] = new Instruction(InstrName.PUSH, 72, 0);

        program.codeMemory[14] = new Instruction(InstrName.LABEL, 0, 0); // loop
        program.codeMemory[15] = new Instruction(InstrName.BEQ, 0, 19);
        program.codeMemory[16] = new Instruction(InstrName.PRINT, 0, 0);
        program.codeMemory[17] = new Instruction(InstrName.POP, 0, 0);
        program.codeMemory[18] = new Instruction(InstrName.BR, 14, 0);
        program.codeMemory[19] = new Instruction(InstrName.LABEL, 0, 0); // endloop
        program.codeMemory[20] = new Instruction(InstrName.RET, 0, 0);
        program.run();
        */
    }
}
