package froggy.src.froggy;

enum TokenType {
  // Stack operations
  RIBBIT,      // output int at top of stack
  CROAK,       // output string at top of stack
  TONGUE,      // input to stack
  PLOP,        // push number
  SPLASH,      // pop from stack
  GULP,        // increment top
  BURP,        // decrement top
  
  // Control flow
  HOP,         // unconditional jump
  LEAP,        // conditional jump (if top is 0)
  LILY,        // label definition
  
  // Stack manipulation
  DUP,         // duplicate top
  SWAP,        // swap top two
  OVER,        // copy second item to top
  
  // Arithmetic (operate on top two stack items)
  ADD,       
  SUB,         
  MUL,         
  DIV,         
  
  // Comparison (push 0 or 1)
  EQUALS,     
  LESS_THAN,
  GREATER_THAN,
  LESS_EQ,
  GREATER_EQ,   
  NOT_EQUAL,
  
  // Literals
  NUMBER,
  STRING,
  IDENTIFIER,  // for labels
  
  EOF
}