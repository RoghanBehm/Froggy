# Froggy
A compiler for a very simple stack-based, frog-themed language. Compiles to x86 assembly (NASM syntax). 

**Stack operations:** `PLOP` (push), `SPLASH` (pop), `GULP` (increment), `BURP` (decrement), `DUP`, `SWAP`, `OVER`  
**Arithmetic:** `ADD`, `SUB`, `MUL`, `DIV`  
**Comparisons:** `EQUALS`, `LESS_THAN`, `GREATER_THAN`, `LESS_EQ`, `GREATER_EQ`, `NOT_EQUAL`  
**Control flow:** `LILY` (label), `HOP` (jump), `LEAP` (conditional jump if zero)  
**I/O:** `RIBBIT` (print integer), `CROAK` (print string), `TONGUE` (input) *(to be implemented)*

An example program, `example.frog`, sits at the root of the project directory. Compile and run with `make run ARGS=example.frog`.

I primarily wrote this to familiarise myself with basic Java syntax and to refresh myself on assembly essentials.

## TODO
- Implement `CROAK` (out string top of stack)
- Implement `TONGUE` (input to stack)

## Attributions
- The lexer is adapted from that presented in Crafting Interpreter's treewalk interpreter.
- Integer printing implementation taken from [x86_64 Linux Assembly #8 - Subroutine to Print Integers](https://www.youtube.com/watch?v=XuUD0WQ9kaE).