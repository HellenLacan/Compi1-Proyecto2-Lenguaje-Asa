package fuentes;

import java_cup.runtime.Symbol;

%%
%{
    //Código de usuario
%}

%cup
%class scanner
%public
%line
%char
%column
%full
%ignorecase

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]

ComentarioMulti = {TraditionalComment}
ComentarioLinea = {EndOfLineComment}
TraditionalComment   = "</" [^/] ~"/>" | "</" "/"+ ">"
EndOfLineComment     = "-->" {InputCharacter}* {LineTerminator}?

digit = [0-9]
identificador = [:jletter:] [:jletterdigit:]*
numEntero = {digit}+ 
numDecimal = ({digit}+[.]{digit}*)|({digit}*[.]{digit}+)
cadena =  "\"" [^\"\n]* "\""
caracter = "'" [^\'\n] "'"


%%
/*************************************  3raa Area: Reglas Lexicas *******************************************************/

//-----> sym

"Importar"     { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._importar, yycolumn, yyline, yytext()); }
"Definir"      { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._definir, yycolumn, yyline, yytext()); }
"Decimal"      { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._decimal, yycolumn, yyline, yytext()); }
"Booleano"     { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._booleano, yycolumn, yyline, yytext()); }
"Texto"        { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._texto, yycolumn, yyline, yytext()); }
"Entero"       { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._entero, yycolumn, yyline, yytext()); }
"Vacio"        { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._vacio, yycolumn, yyline, yytext()); }
"verdadero"    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._verdadero, yycolumn, yyline, yytext()); }
"falso"        { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._falso, yycolumn, yyline, yytext()); }
"Principal"    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._principal, yycolumn, yyline, yytext()); }
"Retorno"      { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._retorno, yycolumn, yyline, yytext()); }
"Es_verdadero" { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._esVerdadero, yycolumn, yyline, yytext()); }
"Es_falso"     { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._esFalso, yycolumn, yyline, yytext()); }
"Cambiar_A"    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._cambiarA, yycolumn, yyline, yytext()); }
"Valor"        { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._valor, yycolumn, yyline, yytext()); }
"No_cumple"    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._noCumple, yycolumn, yyline, yytext()); }
"Para"         { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._para, yycolumn, yyline, yytext()); }
"Hasta_que"    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._hastaQue, yycolumn, yyline, yytext()); }
"Mientras_que" { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._mientrasQue, yycolumn, yyline, yytext()); }
"Romper"       { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._romper, yycolumn, yyline, yytext()); }
"Continuar"    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._continuar, yycolumn, yyline, yytext()); }
"Mostrar"      { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._mostrar, yycolumn, yyline, yytext()); }
"DibujarAST"   { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._dibujarAst, yycolumn, yyline, yytext()); }
"DibujarEXP"   { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._dibujarExp, yycolumn, yyline, yytext()); }
"DibujarTS"    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._dibujarTs, yycolumn, yyline, yytext()); }
"asa"          { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._asa, yycolumn, yyline, yytext()); }

","            { System.out.println("Reconocio "+yytext()+" coma"); return new Symbol(sym.coma, yycolumn, yyline, yytext()); }
"("            { System.out.println("Reconocio "+yytext()+" parentesis Ab"); return new Symbol(sym.parent_a, yycolumn, yyline, yytext()); }
")"            { System.out.println("Reconocio "+yytext()+" parentesis Cerr"); return new Symbol(sym.parent_c, yycolumn, yyline, yytext()); }
"&&"           { System.out.println("Reconocio "+yytext()+" And"); return new Symbol(sym.and, yycolumn, yyline, yytext()); }
"!"            { System.out.println("Reconocio "+yytext()+" not"); return new Symbol(sym.not, yycolumn, yyline, yytext()); }
"||"           { System.out.println("Reconocio "+yytext()+" Or"); return new Symbol(sym.or, yycolumn, yyline, yytext()); }
"~"            { System.out.println("Reconocio "+yytext()+" Distinto"); return new Symbol(sym.diferencia, yycolumn, yyline, yytext()); }
">="           { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym.mayorQ, yycolumn, yyline, yytext()); }
"<="           { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym.menorQ, yycolumn, yyline, yytext()); }
">"            { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym.mayor, yycolumn, yyline, yytext()); }
"<"            { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym.menor, yycolumn, yyline, yytext()); }
"!="           { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym.diferente, yycolumn, yyline, yytext()); }
"=="           { System.out.println("Reconocio "+yytext()+" igual"); return new Symbol(sym.igual, yycolumn, yyline, yytext()); }
"="            { System.out.println("Reconocio "+yytext()+" asignacion"); return new Symbol(sym.asignacion, yycolumn, yyline, yytext()); }
"^"            { System.out.println("Reconocio "+yytext()+" potencia"); return new Symbol(sym.potencia, yycolumn, yyline, yytext()); }
"+"            { System.out.println("Reconocio "+yytext()+" mas"); return new Symbol(sym.mas, yycolumn, yyline, yytext()); }
"-"            { System.out.println("Reconocio "+yytext()+" menos"); return new Symbol(sym.menos, yycolumn, yyline, yytext()); }
"*"            { System.out.println("Reconocio "+yytext()+" por"); return new Symbol(sym.por, yycolumn, yyline, yytext()); }
"/"            { System.out.println("Reconocio "+yytext()+" div"); return new Symbol(sym.div, yycolumn, yyline, yytext()); }
"%"            { System.out.println("Reconocio "+yytext()+" modo"); return new Symbol(sym.modular, yycolumn, yyline, yytext()); }
"."            { System.out.println("Reconocio "+yytext()+" modo"); return new Symbol(sym.punto, yycolumn, yyline, yytext()); }
";"            { System.out.println("Reconocio "+yytext()+" ptoYcoma"); return new Symbol(sym.ptoYcoma, yycolumn, yyline, yytext()); }
"{"            { System.out.println("Reconocio "+yytext()+" llaveA"); return new Symbol(sym.llave_a, yycolumn, yyline, yytext()); }
"}"            { System.out.println("Reconocio "+yytext()+" llaveC"); return new Symbol(sym.llave_c, yycolumn, yyline, yytext()); }
":"            { System.out.println("Reconocio "+yytext()+" llaveC"); return new Symbol(sym.dosPtos, yycolumn, yyline, yytext()); }
"++"           { System.out.println("Reconocio "+yytext()+" llaveC"); return new Symbol(sym.incremento, yycolumn, yyline, yytext()); }
"--"           { System.out.println("Reconocio "+yytext()+" llaveC"); return new Symbol(sym.decremento, yycolumn, yyline, yytext()); }

//-------> sym ER
{identificador}        { System.out.println("Reconocio "+yytext()+" identificador"); return new Symbol(sym.identificador, yyline, yycolumn,yytext());} 
 {numEntero}           { System.out.println("Reconocio "+yytext()+" num"); return new Symbol(sym.numEntero, yycolumn, yyline, yytext()); }
 {numDecimal}          { System.out.println("Reconocio "+yytext()+" decimal"); return new Symbol(sym.numDecimal, yycolumn, yyline, yytext()); } 
{cadena}               { System.out.println("Reconocio "+yytext()+" cadena"); return new Symbol(sym.cadena, yycolumn, yyline, yytext()); }
{caracter}             { System.out.println("Reconocio "+yytext()+" caracter"); return new Symbol(sym.caracter, yycolumn, yyline, yytext()); }

//------> Espacios
[ \t\r\n\f]                  {  /* Espacios en blanco, se ignoran */}
{ComentarioLinea}            {  System.out.println("Reconocio "+yytext()+" coment"); return new Symbol(sym.cadena, yycolumn, yyline, yytext());}
{ComentarioMulti}            {  System.out.println("Reconocio "+yytext()+" coment"); return new Symbol(sym.cadena, yycolumn, yyline, yytext());}

//------> Errores Lexicos
.                            { System.out.println("Error Lexico"+yytext()+" Linea "+yyline+" Columna "+yycolumn);}
