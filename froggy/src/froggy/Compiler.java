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

        // Header (for RIBBIT and CROAK)
        emit("section .data");
        emit("    newline db 10");
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
            case RIBBIT, CROAK, EOF -> {
            }
            case PLOP -> emit("    push " + consume(TokenType.NUMBER, "Expected number").literal());
            case SPLASH -> emit("    pop rax");
            case GULP -> emit("    inc qword [rsp]");
            case BURP -> emit("    dec qword [rsp]");
            case HOP -> emit("    jmp " + consume(TokenType.IDENTIFIER, "Expected label after HOP").lexeme());
            case LEAP -> {
                Token label = consume(TokenType.IDENTIFIER, "Expected label after LEAP");
                emit("    pop rax");
                emit("    cmp rax, 0");
                emit("    je " + label.lexeme());
            }
            case LILY -> emit(consume(TokenType.IDENTIFIER, "Expected label name after LILY").lexeme() + ":");
            case DUP -> emit("    push qword [rsp]");
            case SWAP -> {
                emit("    pop rax");
                emit("    pop rbx");
                emit("    push rax");
                emit("    push rbx");
            }
            case OVER -> emit("    push qword [rsp + 8]");
            case ADD -> {
                emit("    pop rbx");
                emit("    pop rax");
                emit("    add rax, rbx");
                emit("    push rax");
            }
            case SUB -> {
                emit("    pop rbx");
                emit("    pop rax");
                emit("    sub rax, rbx");
                emit("    push rax");
            }
            case MUL -> {
                emit("    pop rbx");
                emit("    pop rax");
                emit("    imul rax, rbx");
                emit("    push rax");
            }
            case DIV -> {
                emit("    pop rbx");
                emit("    pop rax");
                emit("    cqo");
                emit("    idiv rbx");
                emit("    push rax");
            }
            case EQUALS -> cmpSetPush("sete");
            case NOT_EQUAL -> cmpSetPush("setne");
            case LESS_THAN -> cmpSetPush("setl");
            case GREATER_THAN -> cmpSetPush("setg");
            case LESS_EQ -> cmpSetPush("setle");
            case GREATER_EQ -> cmpSetPush("setge");
            case NUMBER -> Froggy.error(token.line(), "Unexpected number: " + token.lexeme());
            case STRING -> Froggy.error(token.line(), "Unexpected string: " + token.lexeme());
            case IDENTIFIER -> Froggy.error(token.line(), "Unexpected identifier: " + token.lexeme());
            default -> Froggy.error(token.line(), "Unhandled token: " + token.type());
        }

    }

    private boolean isAtEnd() {
        return peek().type() == TokenType.EOF;
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

    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;
        return peek().type() == type;
    }

    private Token consume(TokenType type, String message) {
        if (check(type))
            return advance();
        Froggy.error(peek().line(), message);
        throw new RuntimeException(message);
    }

    private void cmpSetPush(String setcc) {
        emit("    pop rbx");
        emit("    pop rax");
        emit("    cmp rax, rbx");
        emit("    " + setcc + " al");
        emit("    movzx rax, al");
        emit("    push rax");
    }

}
