// unsigned int fib(unsigned int n){
//    unsigned int i = n - 1, a = 1, b = 0, c = 0, d = 1, t;
//    if (n <= 0)
//      return 0;
//    while (i > 0){
//      if (i % 2 == 1){
//        t = d*(b + a) + c*b;
//        a = d*b + c*a;
//        b = t;
//      }
//      t = d*(2*c + d);
//      c = c*c + d*d;
//      d = t;
//      i = i / 2;
//    }
//    return a + b;
//  }

PRINT "Please enter the number of the fibonacci suite to compute:"
INPUT n

//    if (n <= 0)
//      return 0;
LD R0, n
BGTZ R0, validInput
PRINT #0
BR end

validInput:
//    unsigned int i = n - 1, a = 1, b = 0, c = 0, d = 1, t;
DEC R0
ST i, R0
ST a, #1
ST b, #0
ST c, #0
ST d, #1

//    while (i > 0){
beginWhile:
LD R0, i
BLETZ R0, printResult

//      if (i % 2 == 1){
MOD R0, R0, #2
DEC R0
BNETZ R0, afterIf

CLEAR

//        t = d*(b + a) + c*b;
//        a = d*b + c*a;
//        b = t;

// TODO:: PUT THE BLOCK 1 HERE !
LD R0, a
LD R1, b
ADD R2, R1, R0
LD R3, d
MUL R4, R3, R2
ST a, R0
ST b, R1
LD R0, c
MUL R1, R0, R1
ST t0, R2
ADD R2, R4, R1
ST d, R3
LD R0, b
MUL R3, R3, R0
ST t1, R4
ST t2, R1
LD R0, a
LD R4, c
MUL R1, R4, R0
ST t3, R2
ADD R2, R3, R1
ST a, R2
ST b, R2
// TODO:: END THE BLOCK 1 HERE ABOVE !

CLEAR

afterIf:
CLEAR

//      t = d*(2*c + d);
//      c = c*c + d*d;
//      d = t;
//      i = i / 2;

// TODO:: PUT THE BLOCK 2 HERE !
LD R0, c
MUL R1, #2, R0
LD R2, d
ADD R3, R1, R2
MUL R4, R2, R3
ST c, R0
MUL R0, R0, R0
ST t0, R1
MUL R1, R2, R2
ST d, R2
ADD R2, R0, R1
ST t1, R3
LD R0, i
DIV R3, R0, #2
ST c, R2
ST d, R4
ST i, R3
// TODO:: END THE BLOCK 2 HERE ABOVE!




// TODO:: This instruction is just a placeholder to let the code end, remove the code below!
// TODO:: Remove the placeholder above of this line!

CLEAR
BR beginWhile

//    return a + b;
printResult:
LD R0, a
LD R1, b
ADD R0, R0, R1
PRINT R0

end:
PRINT "END"