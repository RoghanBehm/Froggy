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
    call _printNumberRAX
    pop rax
    cmp rax, 0
    je LOOP
    mov rax, 60
    xor rdi, rdi
    syscall
_printNumberRAX:
    mov rcx, digitSpace
    mov rbx, 10
    mov [rcx], rbx
    inc rcx
    mov [digitSpacePos], rcx

_printRAXLoop:
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
    jne _printRAXLoop

_printRAXLoop2:
    mov rcx, [digitSpacePos]

    mov rax, 1
    mov rdi, 1
    mov rsi, rcx
    mov rdx, 1
    syscall
    mov rcx, [digitSpacePos]
    dec rcx
    mov [digitSpacePos], rcx
    cmp rcx, digitSpace
    jge _printRAXLoop2

    ret
