grammar Little;

/* Keywords */
PROGRAM: 'PROGRAM';
BEGIN: 'BEGIN';
END: 'END';
FUNCTION: 'FUNCTION';
READ: 'READ';
WRITE: 'WRITE';
IF: 'IF';
ELSE: 'ELSE';
ENDIF: 'ENDIF';
WHILE: 'WHILE';
ENDWHILE: 'ENDWHILE';
CONTINUE: 'CONTINUE';
BREAK: 'BREAK';
RETURN: 'RETURN';
INT: 'INT';
VOID: 'VOID';
STRING: 'STRING';
FLOAT: 'FLOAT';

/* Operators */
ASSIGN: ':=';
PLUS: '+';
MINUS: '-';
MULTIPLY: '*';
DIVIDE: '/';
EQUAL: '=';
NOT_EQUAL: '!=';
LESS_THAN: '<';
GREATER_THAN: '>';
LEFT_PAREN: '(';
RIGHT_PAREN: ')';
SEMICOLON: ';';
COMMA: ',';
LESS_THAN_OR_EQUAL: '<=';
GREATER_THAN_OR_EQUAL: '>=';

/* Identifier */
IDENTIFIER: [a-zA-Z][a-zA-Z0-9]*;

/* Integer literal */
INTLITERAL: [0-9]+;

/* Float literal */
FLOATLITERAL: [0-9]* '.' [0-9]+ | '.' [0-9]+;

/* String literal */
STRINGLITERAL: '"' ~["]* '"';

/* Comment: Starts with "--" and lasts till the end of line */
COMMENT: '--' .*? ('\r'? '\n' | EOF) -> skip;

/* Skip whitespace and newlines */
WS: [ \t\r\n]+ -> skip;




/* Program */
program: PROGRAM id BEGIN pgm_body END;
id: IDENTIFIER;
pgm_body: decl func_declarations;
decl: string_decl decl | var_decl decl | /* empty */;



/* Global String Declaration */
string_decl: STRING id ASSIGN str SEMICOLON;
str: STRINGLITERAL;



/* Variable Declaration */
var_decl: var_type id_list SEMICOLON;
var_type: FLOAT | INT;
any_type: var_type | VOID;
id_list: id id_tail;
id_tail: COMMA id id_tail | /* empty */;



/* Function Parameter List */
param_decl_list: param_decl param_decl_tail | /* empty */;
param_decl: var_type id;
param_decl_tail: COMMA param_decl param_decl_tail | /* empty */;




/* Function Declarations */
func_declarations: func_decl func_declarations | /* empty */;
func_decl: FUNCTION any_type id LEFT_PAREN param_decl_list RIGHT_PAREN BEGIN func_body END;
func_body: decl stmt_list;




/* Statement List */
stmt_list: stmt stmt_list | /* empty */;
stmt: base_stmt | if_stmt | while_stmt;
base_stmt: assign_stmt | read_stmt | write_stmt | return_stmt;




/* Basic Statements */
assign_stmt: assign_expr SEMICOLON;
assign_expr: id ASSIGN expr;
read_stmt: READ LEFT_PAREN id_list RIGHT_PAREN SEMICOLON;
write_stmt: WRITE LEFT_PAREN id_list RIGHT_PAREN SEMICOLON;
return_stmt: RETURN expr SEMICOLON;




/* Expressions */
expr: expr_prefix factor;
expr_prefix: expr_prefix factor addop | /* empty */;
factor: factor_prefix postfix_expr;
factor_prefix: factor_prefix postfix_expr mulop | /* empty */;
postfix_expr: primary | call_expr;
call_expr: id LEFT_PAREN expr_list RIGHT_PAREN;
expr_list: expr expr_list_tail | /* empty */;
expr_list_tail: COMMA expr expr_list_tail | /* empty */;
primary: LEFT_PAREN expr RIGHT_PAREN | id | INTLITERAL | FLOATLITERAL;
addop: PLUS | MINUS;
mulop: MULTIPLY | DIVIDE;




/* Complex Statements and Condition */
if_stmt: IF LEFT_PAREN cond RIGHT_PAREN decl stmt_list else_part ENDIF;
else_part: ELSE decl stmt_list | /* empty */;
cond: expr compop expr;
compop: LESS_THAN | GREATER_THAN | EQUAL | NOT_EQUAL | LESS_THAN_OR_EQUAL | GREATER_THAN_OR_EQUAL;




/* While statements */
while_stmt: WHILE LEFT_PAREN cond RIGHT_PAREN decl stmt_list ENDWHILE;

