# Froggy
A simple stack-based compiler with frog-themed operations. Compiles to x86-64 assembly (NASM syntax). 

## Operations
**Stack:** `PLOP` (push), `SPLASH` (pop), `GULP` (increment), `BURP` (decrement), `DUP`, `SWAP`, `OVER`  
**Arithmetic:** `ADD`, `SUB`, `MUL`, `DIV`  
**Comparison:** `EQUALS`, `LESS_THAN`, `GREATER_THAN`, `LESS_EQ`, `GREATER_EQ`, `NOT_EQUAL`  
**Control flow:** `LILY` (label), `HOP` (jump), `LEAP` (jump if zero)  
**I/O:** `RIBBIT` (print int), `CROAK` (print string)

## Example
```
PLOP 42
RIBBIT          // prints: 42
```

See `example.frog` for a simple loop.

See `test.frog` for an exhaustive test of all operations.

## Usage
Requires Java, NASM, and ld.

```bash
make run ARGS=examples/example.frog > example.asm    # compile to assembly
nasm -f elf64 example.asm                            # assemble
ld example.o -o example                              # link
./example                                            # run
```

## Notes
Written to practice Java syntax and refresh assembly basics. Lexer adapted from Crafting Interpreters. Print routines from Kupala's x86-64 assembly tutorials.

## Limitations
- `RIBBIT` treats all numbers as unsigned.
- REPL mode is a bit pointless as it just prints asm rather than executing anything, + it prints all of the boilerplate each time.