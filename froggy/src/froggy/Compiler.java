package froggy.src.froggy;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class Compiler {
    private StringBuilder asm = new StringBuilder();
    private int current = 0;
    private List<Token> tokens;
    private int stringCounter = 0;
    private List<String> stringLiterals = new ArrayList<>();

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

        // Printing
        emit("_printStringRAX:");
        emit("    mov r8, rax");
        emit("    xor rbx, rbx");
        emit("_printStringLoop:");
        emit("    mov cl, [rax]");
        emit("    cmp cl, 0");
        emit("    je _printStringDone");
        emit("    inc rax");
        emit("    inc rbx");
        emit("    jmp _printStringLoop");
        emit("_printStringDone:");
        emit("    mov rax, 1");
        emit("    mov rdi, 1");
        emit("    mov rsi, r8");
        emit("    mov rdx, rbx");
        emit("    syscall");
        emit("    ret");

        emit("_printNumberRAX:");
        emit("    mov rcx, digitSpace");
        emit("    mov byte [rcx], 10");
        emit("    inc rcx");
        emit("    mov [digitSpacePos], rcx");
        emit("");
        emit("_printNumberRAXLoop:");
        emit("    mov rdx, 0");
        emit("    mov rbx, 10");
        emit("    div rbx"); // Extract digit
        emit("    push rax");
        emit("    add rdx, 48"); // Convert to ASCII code
        emit("");
        emit("    mov rcx, [digitSpacePos]");
        emit("    mov [rcx], dl"); // Write ASCII char to digit space
        emit("    inc rcx");
        emit("    mov [digitSpacePos], rcx"); // increment digit space ptr
        emit("");
        emit("    pop rax");
        emit("    cmp rax, 0");
        emit("    jne _printNumberRAXLoop");
        emit("");
        emit("_printNumberRAXLoop2:");
        emit("    mov rcx, [digitSpacePos]");
        emit("    dec rcx");
        emit("");
        emit("    mov rax, 1"); // write syscall
        emit("    mov rdi, 1"); // fd
        emit("    mov rsi, rcx"); // data address
        emit("    mov rdx, 1"); // num bytes
        emit("    syscall");

        emit("    mov rcx, [digitSpacePos]");
        emit("    dec rcx");
        emit("    mov [digitSpacePos], rcx"); // dec ptr
        emit("    cmp rcx, digitSpace");
        emit("    jge _printNumberRAXLoop2");
        emit("");
        emit("    ret");

        // Define strings
        if (!stringLiterals.isEmpty()) {
            emit("");
            emit("section .data");
            for (int i = 0; i < stringLiterals.size(); i++) {
                String bytesList = nasmDbBytes(stringLiterals.get(i), true);
                emit("    str_" + i + " db " + bytesList);
            }
        }

        return asm.toString();
    }

    private void emit(String str) {
        asm.append(str).append("\n");
    }

    private void compileToken(Token token) {
        switch (token.type()) {
            case RIBBIT -> {
                emit("    pop rax");
                emit("    call _printNumberRAX");
            }
            case CROAK -> {
                emit("    pop rax");
                emit("    call _printStringRAX");
            }
            case EOF, TONGUE -> {
            }
            case PLOP -> {
                if (peek().type() == TokenType.NUMBER) {
                    emit("    push " + (int) (double) consume(TokenType.NUMBER, "Expected number").literal());
                } else if (peek().type() == TokenType.STRING) {
                    String label = "str_" + stringCounter;
                    stringLiterals.add((String) consume(TokenType.STRING, "Expected string").literal());
                    stringCounter++;
                    emit("    push " + label);
                } else {
                    Froggy.error(peek().line(), "PLOP expects a number or string");
                }
            }
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

    private static String nasmDbBytes(String s, boolean newline) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            int u = bytes[i] & 0xFF;
            if (i != 0)
                out.append(", ");
            out.append(u);
        }

        if (newline) {
            if (bytes.length != 0)
                out.append(", ");
            out.append("10");
        }
        if (bytes.length != 0)
            out.append(", ");
        out.append("0"); // null terminator
        return out.toString();
    }

}
