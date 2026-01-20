section .data
    newline db 10

section .bss
    digitSpace resb 100
    digitSpacePos resb 8

section .text
    global _start

_start:
    push 0
LOOP:
    inc qword [rsp]
    push qword [rsp]
    push qword [rsp]
    push 5
    pop rbx
    pop rax
    cmp rax, rbx
    setl al
    movzx rax, al
    push rax
    pop rax
    call _printNumberRAX
    push str_0
    pop rax
    call _printStringRAX
    pop rax
    cmp rax, 0
    je LOOP
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
    str_0 db 72, 73, 10, 0

