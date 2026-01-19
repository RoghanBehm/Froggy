# Froggy
A compiler for a very simple stack-based, frog-themed language. Compiles to x86 assembly (NASM syntax). 


Stack operations: `PLOP` (push), `SPLASH` (pop), `ADD`, `SUB`, `MUL`, `DIV`, `DUP`, `SWAP`  
Control flow: `LILY` (label), `HOP` (jump), `LEAP` (conditional jump)  
**I/O:** `RIBBIT` (print), `CROAK` (input) *(to be implemented)*

An example program, `example.frog`, sits at the root of the project directory. Compile and run with `make run ARGS=example.frog`.

I primarily wrote this to familiarise myself with basic Java syntax and to refresh myself on assembly essentials.

## TODO
- Implement `RIBBIT` (output top of stack)
- Implement `CROAK` (input to stack)