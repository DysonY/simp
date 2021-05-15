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
    throws FileNotFoundException, InvalidLabelException, DuplicateLabelException
    {
        File fileObj = new File(filepath);
        firstPass(fileObj);
        for (String s : labelAddresses.keySet())
            System.out.println(s + " " + labelAddresses.get(s));
        return null;
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
    throws InvalidLabelException
    {
        if (line.length() == 0) return null;

        // Burn leading whitespace
        int i = 0;
        while (line.charAt(i) == ' ' || line.charAt(i) == '\t') i++;

        // Read alphanumeric characters into label
        String label = "";
        if (line.charAt(i) == '.')
        {
            if (++i >= line.length()) {
                throw new InvalidLabelException("Invalid label declaration.");
            }

            while (i < line.length())
            {
                if (Character.isLetterOrDigit(line.charAt(i))) label += line.charAt(i);
                else if (Character.isWhitespace(line.charAt(i))) break;
                else throw new InvalidLabelException("Invalid label declaration.");;
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
                throw new InvalidLabelException("Invalid label declaration.");
            }
        }

        return label;
    }

    // First pass: get location of labels
    private void firstPass(File fileObj)
    throws FileNotFoundException, InvalidLabelException, DuplicateLabelException
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
                    throw new DuplicateLabelException("Duplicate label " + label + " found.");
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
    throws FileNotFoundException
    {
        int pc = 0;
        Scanner input = new Scanner(fileObj);

        while (input.hasNextLine())
        {
            String line = input.nextLine();

            // If label is found, continue
            if (codeMemory[pc] != null)
            {
                pc++;
                continue;
            }
            
            // else parse instruction, set codeMemory[pc] := instruction, increment pc
        }

        input.close();
    }

    public static void main(String[] args)
    throws FileNotFoundException, InvalidLabelException, DuplicateLabelException
    {
        InstrGenerator gen = new InstrGenerator();
        gen.assemble("/Users/dysonye/Desktop/Projects/Java/simp/tests/hello.txt");
    }
}
