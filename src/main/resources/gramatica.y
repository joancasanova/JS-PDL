%{
/* Código C necesario para la integración con el analizador léxico (lexer) */
#include <stdio.h>
#include <stdlib.h>

extern int yylex(void);
void yyerror(const char *s) {
  fprintf(stderr, "Error: %s\n", s);
}
%}

%token ID ENTERO CADENA PUT GET RETURN IF WHILE LET FUNCTION INT BOOLEAN STRING VOID EQ_OP ADD_OP
%left '+' EQ_OP ADD_OP
%right '!'

%start P

%%

P: B P
 | F P
 | /* empty */
 ;

B: IF '(' E ')' S
 | WHILE '(' E ')' '{' C '}'
 | LET ID T ';'
 | LET ID T '=' E ';'
 | S
 ;

T: INT
 | BOOLEAN
 | STRING
 ;

F: F1 '{' C '}'
 ;

F1: F2 '(' A ')'
 ;

F2: FUNCTION ID H
 ;

H: T
 | VOID
 ;

A: T ID K
 | VOID
 ;

K: ',' T ID K
 | /* empty */
 ;

C: B C
 | /* empty */
 ;

E: E EQ_OP U
 | U
 ;

U: U '+' V
 | V
 ;

V: '!' W
 | W
 ;

W: ID
 | '(' E ')'
 | ID '(' L ')'
 | ENTERO
 | CADENA
 ;

S: ID '=' E ';'
 | ID ADD_OP E ';'
 | ID '(' L ')' ';'
 | PUT E ';'
 | GET ID ';'
 | RETURN Z ';'
 ;

L: E Q
 | /* empty */
 ;

Q: ',' E Q
 | /* empty */
 ;

Z: E
 | /* empty */
 ;

%%

int main(void) {
    return yyparse();
}
