# Overview
Stack-based Instruction and Memory Processor, or SIMP for short, is a virtual stack machine for the SIMP ISA.

# ISA
SIMP supports the following instructions:
| Name | Assembly | Behavior |
| ---- | -------- | -------- |
| Add  | add | Pop 2 operands, add, and push result
| Sub  | sub | Pop 2 operands, subtract, and push result
| And  | and | Pop 2 operands, bitwise AND, and push result
| Or   | or  | Pop 2 operands, bitwise OR, and push result
| Not  | not | Pop 1 operand, bitwise NOT, and push result
| Xor  | xor | Pop 2 operands, bitwise XOR, and push result
| Inc  | inc | Increment value on top of stack
| Dec  | dec | Decrement value on top of stack
| Swap | swap | Swap top 2 items on stack
| Push | push *val* | Put *val* on top of stack
| Pop  | pop | Decrement stack pointer
| Store | store *val*, *addr* | Put *val* at memory location *addr*
| Load | load *addr* | Put value at *addr* on top of stack
| Branch if Equal | beq *val*, *label* | If top of stack equals *val*, branch to *label*
| Branch if Not Equal | bne *val*, *label* | If top of stack does not equal *val*, branch to *label*
| Branch Unconditional | br *label* | Jump to *label*
| Print | print | Print character encoded by top of stack
| Return | ret | Halt program, return top of stack