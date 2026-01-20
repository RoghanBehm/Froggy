section .data
    newline db 10

section .bss
    digitSpace resb 100
    digitSpacePos resb 8

section .text
    global _start

_start:
    push 42
    pop rax
    call _printNumberRAX
    push str_0
    pop rax
    call _printStringRAX
    push 10
    inc qword [rsp]
    inc qword [rsp]
    push qword [rsp]
    pop rax
    call _printNumberRAX
    dec qword [rsp]
    pop rax
    call _printNumberRAX
    push 99
    push qword [rsp]
    pop rax
    call _printNumberRAX
    pop rax
    call _printNumberRAX
    push 5
    push 7
    pop rax
    pop rbx
    push rax
    push rbx
    pop rax
    call _printNumberRAX
    pop rax
    call _printNumberRAX
    push 11
    push 22
    push qword [rsp + 8]
    pop rax
    call _printNumberRAX
    pop rax
    call _printNumberRAX
    pop rax
    call _printNumberRAX
    push 10
    push 32
    pop rbx
    pop rax
    add rax, rbx
    push rax
    pop rax
    call _printNumberRAX
    push 50
    push 8
    pop rbx
    pop rax
    sub rax, rbx
    push rax
    pop rax
    call _printNumberRAX
    push 6
    push 7
    pop rbx
    pop rax
    imul rax, rbx
    push rax
    pop rax
    call _printNumberRAX
    push 84
    push 2
    pop rbx
    pop rax
    cqo
    idiv rbx
    push rax
    pop rax
    call _printNumberRAX
    push 5
    push 5
    pop rbx
    pop rax
    cmp rax, rbx
    sete al
    movzx rax, al
    push rax
    pop rax
    call _printNumberRAX
    push 5
    push 3
    pop rbx
    pop rax
    cmp rax, rbx
    sete al
    movzx rax, al
    push rax
    pop rax
    call _printNumberRAX
    push 3
    push 5
    pop rbx
    pop rax
    cmp rax, rbx
    setl al
    movzx rax, al
    push rax
    pop rax
    call _printNumberRAX
    push 5
    push 3
    pop rbx
    pop rax
    cmp rax, rbx
    setg al
    movzx rax, al
    push rax
    pop rax
    call _printNumberRAX
    push 5
    push 5
    pop rbx
    pop rax
    cmp rax, rbx
    setle al
    movzx rax, al
    push rax
    pop rax
    call _printNumberRAX
    push 7
    push 5
    pop rbx
    pop rax
    cmp rax, rbx
    setge al
    movzx rax, al
    push rax
    pop rax
    call _printNumberRAX
    push 3
    push 5
    pop rbx
    pop rax
    cmp rax, rbx
    setne al
    movzx rax, al
    push rax
    pop rax
    call _printNumberRAX
    push str_1
    pop rax
    call _printStringRAX
    jmp SKIP
    push str_2
    pop rax
    call _printStringRAX
SKIP:
    push str_3
    pop rax
    call _printStringRAX
    push 0
    pop rax
    cmp rax, 0
    je ZERO_JUMP
    push str_4
    pop rax
    call _printStringRAX
ZERO_JUMP:
    push str_5
    pop rax
    call _printStringRAX
    push 1
    pop rax
    cmp rax, 0
    je NO_JUMP
    push str_6
    pop rax
    call _printStringRAX
NO_JUMP:
    push 0
COUNT_LOOP:
    inc qword [rsp]
    push qword [rsp]
    pop rax
    call _printNumberRAX
    push qword [rsp]
    push 3
    pop rbx
    pop rax
    cmp rax, rbx
    sete al
    movzx rax, al
    push rax
    push 0
    pop rbx
    pop rax
    cmp rax, rbx
    sete al
    movzx rax, al
    push rax
    pop rax
    cmp rax, 0
    je COUNT_LOOP
    pop rax
    push 999
    push 888
    pop rax
    pop rax
    call _printNumberRAX
    push str_7
    pop rax
    call _printStringRAX
    mov rax, 60
    xor rdi, rdi
    syscall
_printStringRAX:
    mov r8, rax
    xor rbx, rbx
_printStringLoop:
    mov cl, [rax]
    cmp cl, 0
    je _printStringDone
    inc rax
    inc rbx
    jmp _printStringLoop
_printStringDone:
    mov rax, 1
    mov rdi, 1
    mov rsi, r8
    mov rdx, rbx
    syscall
    ret
_printNumberRAX:
    mov rcx, digitSpace
    mov byte [rcx], 10
    inc rcx
    mov [digitSpacePos], rcx

_printNumberRAXLoop:
    mov rdx, 0
    mov rbx, 10
    div rbx
    push rax
    add rdx, 48

    mov rcx, [digitSpacePos]
    mov [rcx], dl
    inc rcx
    mov [digitSpacePos], rcx

    pop rax
    cmp rax, 0
    jne _printNumberRAXLoop

_printNumberRAXLoop2:
    mov rcx, [digitSpacePos]
    dec rcx

    mov rax, 1
    mov rdi, 1
    mov rsi, rcx
    mov rdx, 1
    syscall
    mov rcx, [digitSpacePos]
    dec rcx
    mov [digitSpacePos], rcx
    cmp rcx, digitSpace
    jge _printNumberRAXLoop2

    ret

section .data
    str_0 db 72, 101, 108, 108, 111, 32, 102, 114, 111, 109, 32, 70, 114, 111, 103, 103, 121, 33, 10, 0
    str_1 db 66, 101, 102, 111, 114, 101, 32, 106, 117, 109, 112, 10, 0
    str_2 db 84, 104, 105, 115, 32, 115, 104, 111, 117, 108, 100, 32, 110, 111, 116, 32, 112, 114, 105, 110, 116, 10, 0
    str_3 db 65, 102, 116, 101, 114, 32, 106, 117, 109, 112, 10, 0
    str_4 db 83, 104, 111, 117, 108, 100, 32, 110, 111, 116, 32, 112, 114, 105, 110, 116, 10, 0
    str_5 db 74, 117, 109, 112, 101, 100, 32, 98, 101, 99, 97, 117, 115, 101, 32, 122, 101, 114, 111, 10, 0
    str_6 db 68, 105, 100, 32, 110, 111, 116, 32, 106, 117, 109, 112, 10, 0
    str_7 db 65, 108, 108, 32, 116, 101, 115, 116, 115, 32, 99, 111, 109, 112, 108, 101, 116, 101, 33, 10, 0

