package froggy.src.froggy;

import java.util.List;

class Compiler {
    private StringBuilder asm = new StringBuilder();
    private int current = 0;
    private List<Token> tokens;
    public String compile() {

        // Header
        emit("section .data");
        emit("    newline db 10");  // For printing
        emit("");
        emit("section .bss");
        emit("    digitSpace resb 100");
        emit("    digitSpacePos resb 8");
        emit("");
        emit("section .text");
        emit("    global _start");
        emit("");
        emit("_start:");

        // Code gen
        while (!isAtEnd()) {
            compileToken(advance());
        }


        // Exit
        emit("    mov rax, 60");
        emit("    xor rdi, rdi");
        emit("    syscall");

        return asm.toString();
    }

    private void emit(String str) {
        asm.append(str).append("\n");
    }




    private void compileToken(Token token) {
        switch (token.type()) {
            case RIBBIT:
                break;
            case CROAK:
                break;
            case PLOP:
                break;
            case SPLASH:
                break;
            case GULP:
                break;
            case BURP:
                break;
            case HOP:
                emit("    jmp " + advance().lexeme());
                break;
            case LEAP:
                break;
            case LILY:
                emit(advance().lexeme() + ":");
                break;
            case DUP:
                break;
            case SWAP:
                break;
            case OVER:
                break;
            case ADD:
                break;
            case SUB:
                break;
            case MUL:
                break;
            case DIV:
                break;
            case EQUALS:
                break;
            case LESS_THAN:
                break;
            case NUMBER:
                break;
            case STRING:
                break;
            case IDENTIFIER:
                break;
            case EOF:
                break;
            default:
                break;
        }



    }
        private boolean isAtEnd() {
            return current >= tokens.size() || peek().type() == TokenType.EOF;
        }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token advance() {
        current++;
        return previous();
    }
}
 