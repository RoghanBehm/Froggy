package froggy.src.froggy;

import java.util.List;

class Compiler {
    private StringBuilder asm = new StringBuilder();
    private int current = 0;
    private List<Token> tokens;

    Compiler(List<Token> tokens) {
        this.tokens = tokens;
    }

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
                emit("    push " + advance().literal());
                break;
            case SPLASH:
                emit("    pop rax");
                break;
            case GULP:
                emit("    inc qword [rsp]");
                break;
            case BURP:
                emit("    dec qword [rsp]");
                break;
            case HOP:
                emit("    jmp " + advance().lexeme());
                break;
            case LEAP:
                emit("    pop rax");
                emit("    cmp rax, 0");
                emit("    je " + advance().lexeme());
                break;
            case LILY:
                emit(advance().lexeme() + ":");
                break;
            case DUP:
                emit("    push qword [rsp]");
                break;
            case SWAP:
                emit("    pop rax");
                emit("    pop rbx");
                emit("    push rax");
                emit("    push rbx");
                break;
            case OVER:
                emit("    push qword [rsp + 8]");
                break;
            case ADD:
                emit("    pop rax");
                emit("    pop rbx");
                emit("    add rax, rbx");
                emit("    push rax");
                break;
            case SUB:
                emit("    pop rbx");
                emit("    pop rax");
                emit("    sub rax, rbx");
                emit("    push rax");
                break;
            case MUL:
                emit("    pop rax");
                emit("    pop rbx");
                emit("    imul rax, rbx");
                emit("    push rax");
                break;
            case DIV:
                emit("    pop rbx");
                emit("    pop rax");
                emit("    xor rdx, rdx");
                emit("    idiv rbx");
                emit("    push rax");
                break;
            case EQUALS:
                emit ("    pop rax");
                emit ("    pop rbx");
                emit("     cmp rax, rbx");
                emit("     sete al");
                emit("     movzx rax, al");
                emit("     push rax");
                break;
            case LESS_THAN:
                emit ("    pop rbx");
                emit ("    pop rax");
                emit("     cmp rax, rbx");
                emit("     setl al");
                emit("     movzx rax, al");
                emit("     push rax");
                break;
            case GREATER_THAN:
                emit ("    pop rbx");
                emit ("    pop rax");
                emit("     cmp rax, rbx");
                emit("     setg al");
                emit("     movzx rax, al");
                emit("     push rax");
                break;
            case LESS_EQ:
                emit ("    pop rbx");
                emit ("    pop rax");
                emit("     cmp rax, rbx");
                emit("     setle al");
                emit("     movzx rax, al");
                emit("     push rax");
                break;
            case GREATER_EQ:
                emit ("    pop rbx");
                emit ("    pop rax");
                emit("     cmp rax, rbx");
                emit("     setge al");
                emit("     movzx rax, al");
                emit("     push rax");
                break;
            case NOT_EQUAL:
                emit ("    pop rax");
                emit ("    pop rbx");
                emit("     cmp rax, rbx");
                emit("     setne al");
                emit("     movzx rax, al");
                emit("     push rax");
                break;
            case NUMBER:
                Froggy.error(token.line(), "Unexpected number: " + token.lexeme());
                break;
            case STRING:
                Froggy.error(token.line(), "Unexpected string: " + token.lexeme());
                break;
            case IDENTIFIER:
                Froggy.error(token.line(), "Unexpected identifier: " + token.lexeme());
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
 