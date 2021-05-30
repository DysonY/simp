import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class InstrGenerator
{
    private HashMap<String, Integer> labelAddresses;
    private Instruction[] codeMemory;

    public InstrGenerator()
    {
        this.labelAddresses = new HashMap<String, Integer>();
        this.codeMemory = new Instruction[65535];
    }

    // Assemble in two passes
    public Instruction[] assemble(String filepath)
    throws FileNotFoundException, AssemblyException
    {
        resetState();

        File fileObj = new File(filepath);
        firstPass(fileObj);
        secondPass(fileObj);
        //for (String s : labelAddresses.keySet())
        //    System.out.println(s + " " + labelAddresses.get(s));
        return codeMemory;
    }

    // Test if line is blank/comment
    private boolean isBlank(String line)
    {
        for (int i = 0; i < line.length(); i++)
        {
            if (line.charAt(i) == ' ' || line.charAt(i) == '\t') continue;
            if (line.charAt(i) == ';') break;
            else return false;
        }
        return true;
    }

    // If line is a label, return the label; otherwise, return null
    public String getLabel(String line)
    throws AssemblyException
    {
        if (line.length() == 0) return null;

        // Burn leading whitespace
        int i = 0;
        while (line.charAt(i) == ' ' || line.charAt(i) == '\t') i++;

        // Read alphanumeric characters into label
        String label = "";
        if (line.charAt(i) == '.')
        {
            if (++i >= line.length())
            {
                throw new AssemblyException("Invalid label declaration.");
            }

            while (i < line.length())
            {
                if (Character.isLetterOrDigit(line.charAt(i))) label += line.charAt(i);
                else if (Character.isWhitespace(line.charAt(i))) break;
                else throw new AssemblyException("Invalid label declaration.");
                i++;
            }
        } else {
            return null;
        }

        // Burn trailing whitespace
        while (i < line.length())
        {
            char c = line.charAt(i++);
            if (!Character.isWhitespace(c))
            {
                if (c == ';') break;
                throw new AssemblyException("Invalid label declaration.");
            }
        }

        return label;
    }

    // Validate number of arguments
    private void validateNumOfArgs(int numArgs, int target, int lineNum)
    throws AssemblyException
    {
        if (numArgs != target)
        {
            throw new AssemblyException("Error on line " + lineNum +
                                        ": Expected " + target +
                                        " arguments, found " + numArgs);
        }
    }

    // Retrieve address of label
    private int getAddrOfLabel(String label)
    throws AssemblyException
    {
        if (labelAddresses.containsKey(label))
        {
            return labelAddresses.get(label);
        }
        else
        {
            throw new AssemblyException("Label " + label + " not found.");
        }
    }

    // Parse non-label instruction
    private Instruction parseInstruction(String line, int lineNum)
    throws AssemblyException
    {
        String[] args = line.trim().split("[,\\s]+");

        // TODO: print line number on exceptions
        if (args.length > 3)
        {
            throw new AssemblyException("Too many arguments on line " + lineNum +
                                        ": Found " + args.length + " arguments.");
        }

        // Generate instruction from arguments
        switch (args[0])
        {
            case "add":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.ADD, 0, 0);
            
            case "sub":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.SUB, 0, 0);
            
            case "and":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.AND, 0, 0);
            
            case "or":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.OR, 0, 0);
            
            case "not":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.NOT, 0, 0);
            
            case "xor":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.XOR, 0, 0);
            
            case "inc":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.INC, 0, 0);
            
            case "dec":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.DEC, 0, 0);
            
            case "swap":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.SWAP, 0, 0);
            
            case "push":
                validateNumOfArgs(args.length, 2, lineNum);
                return new Instruction(InstrName.PUSH, Integer.parseInt(args[1]), 0);
            
            case "pop":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.POP, 0, 0);
            
            case "store":
                validateNumOfArgs(args.length, 3, lineNum);
                return new Instruction(InstrName.STORE, Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            
            case "load":
                validateNumOfArgs(args.length, 2, lineNum);
                return new Instruction(InstrName.LOAD, Integer.parseInt(args[1]), 0);
            
            case "beq":
                System.out.println("DEBUG beq");
                validateNumOfArgs(args.length, 3, lineNum);
                return new Instruction(InstrName.BEQ, Integer.parseInt(args[1]), getAddrOfLabel(args[2]));
            
            case "bne":
                validateNumOfArgs(args.length, 3, lineNum);
                return new Instruction(InstrName.BNE, Integer.parseInt(args[1]), getAddrOfLabel(args[2]));
            
            case "br":
                validateNumOfArgs(args.length, 2, lineNum);
                return new Instruction(InstrName.BR, getAddrOfLabel(args[1]), 0);
            
            case "print":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.PRINT, 0, 0);
            
            case "ret":
                validateNumOfArgs(args.length, 1, lineNum);
                return new Instruction(InstrName.RET, 0, 0);

            default:
                throw new AssemblyException("Invalid instruction " + args[0] + " at line " + lineNum);
        }
    }

    // First pass: get location of labels
    private void firstPass(File fileObj)
    throws FileNotFoundException, AssemblyException
    {
        int pc = 0;
        Scanner input = new Scanner(fileObj);

        while (input.hasNextLine())
        {
            String line = input.nextLine();
            if (isBlank(line)) continue;

            String label = getLabel(line);
            if (label != null)
            {
                if (labelAddresses.containsKey(label))
                {
                    input.close();
                    throw new AssemblyException("Duplicate label '" + label + "' found.");
                }
                if (label != null && label != "")
                {
                    this.labelAddresses.put(label, pc);
                    this.codeMemory[pc] = new Instruction(InstrName.LABEL, 0, 0);
                }
            }
            pc++;
        }

        input.close();
    }

    // Second pass: read instructions into memory
    private void secondPass(File fileObj)
    throws FileNotFoundException, AssemblyException
    {
        int pc = 0;
        int lineNum = 0;
        Scanner input = new Scanner(fileObj);

        while (input.hasNextLine())
        {
            String line = input.nextLine();

            // If label or blank line is found, continue
            if (getLabel(line) != null || isBlank(line))
            {
                lineNum++;
                continue;
            }

            // Else parse instruction, set codeMemory[pc] := instruction, increment pc
            codeMemory[pc] = parseInstruction(line, lineNum);
            
            pc++;
            lineNum++;
        }

        input.close();
    }

    private void resetState()
    {
        this.labelAddresses = new HashMap<String, Integer>();
        this.codeMemory = new Instruction[65535];
    }

    public static void main(String[] args)
    throws FileNotFoundException, AssemblyException
    {
        InstrGenerator gen = new InstrGenerator();
        gen.assemble("/Users/dysonye/Desktop/Projects/Java/simp/tests/hello.txt");
    }
}
