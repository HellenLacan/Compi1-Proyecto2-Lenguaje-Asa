package fuentes;

import java_cup.runtime.Symbol;

//Scanner para generar C3D

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
cadena =  "\"" [^\"\n]* "\""
numDecimal = ({digit}+[.]{digit}*)|({digit}*[.]{digit}+)



%%
/*************************************  3raa Area: Reglas Lexicas *******************************************************/

//-----> sym

"importar"    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._importar, yycolumn, yyline, yytext()); }
"definir"     { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._definir, yycolumn, yyline, yytext()); }
"decimal"      { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._decimal, yycolumn, yyline, yytext()); }
"booleano"       { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._booleano, yycolumn, yyline, yytext()); }
"texto"      { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._texto, yycolumn, yyline, yytext()); }
"entero"      { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._entero, yycolumn, yyline, yytext()); }
"vacio"       { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._vacio, yycolumn, yyline, yytext()); }
"verdadero"   { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._verdadero, yycolumn, yyline, yytext()); }
"falso"   { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym._falso, yycolumn, yyline, yytext()); }

","      { System.out.println("Reconocio "+yytext()+" coma"); return new Symbol(sym.coma, yycolumn, yyline, yytext()); }
"("    { System.out.println("Reconocio "+yytext()+" parentesis Ab"); return new Symbol(sym.parent_a, yycolumn, yyline, yytext()); }
")"    { System.out.println("Reconocio "+yytext()+" parentesis Cerr"); return new Symbol(sym.parent_c, yycolumn, yyline, yytext()); }
"&&"        { System.out.println("Reconocio "+yytext()+" And"); return new Symbol(sym.and, yycolumn, yyline, yytext()); }
"!"      { System.out.println("Reconocio "+yytext()+" Negacion"); return new Symbol(sym.negacion, yycolumn, yyline, yytext()); }
"||"       { System.out.println("Reconocio "+yytext()+" Or"); return new Symbol(sym.or, yycolumn, yyline, yytext()); }
"~"     { System.out.println("Reconocio "+yytext()+" Distinto"); return new Symbol(sym.diferencia, yycolumn, yyline, yytext()); }
">="        { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym.mayorQ, yycolumn, yyline, yytext()); }
"<="    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym.menorQ, yycolumn, yyline, yytext()); }
">"      { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym.mayor, yycolumn, yyline, yytext()); }
"<"    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym.menor, yycolumn, yyline, yytext()); }
"!="    { System.out.println("Reconocio "+yytext()+" Reservada"); return new Symbol(sym.diferente, yycolumn, yyline, yytext()); }
"=="    { System.out.println("Reconocio "+yytext()+" igual"); return new Symbol(sym.igual, yycolumn, yyline, yytext()); }
"="   { System.out.println("Reconocio "+yytext()+" asignacion"); return new Symbol(sym.asignacion, yycolumn, yyline, yytext()); }
"^"    { System.out.println("Reconocio "+yytext()+" potencia"); return new Symbol(sym.potencia, yycolumn, yyline, yytext()); }
"+"         { System.out.println("Reconocio "+yytext()+" mas"); return new Symbol(sym.mas, yycolumn, yyline, yytext()); }
"-"         { System.out.println("Reconocio "+yytext()+" menos"); return new Symbol(sym.menos, yycolumn, yyline, yytext()); }
"*"         { System.out.println("Reconocio "+yytext()+" por"); return new Symbol(sym.por, yycolumn, yyline, yytext()); }
"/"         { System.out.println("Reconocio "+yytext()+" div"); return new Symbol(sym.div, yycolumn, yyline, yytext()); }
"%"         { System.out.println("Reconocio "+yytext()+" modo"); return new Symbol(sym.modular, yycolumn, yyline, yytext()); }
"."         { System.out.println("Reconocio "+yytext()+" modo"); return new Symbol(sym.punto, yycolumn, yyline, yytext()); }
";"         { System.out.println("Reconocio "+yytext()+" ptoYcoma"); return new Symbol(sym.ptoYcoma, yycolumn, yyline, yytext()); }

//-------> sym ER
{identificador}       { System.out.println("Reconocio "+yytext()+" identificador"); return new Symbol(sym.identificador, yyline, yycolumn,yytext());} 
 {numEntero}          { System.out.println("Reconocio "+yytext()+" num"); return new Symbol(sym.numEntero, yycolumn, yyline, yytext()); }
 {numDecimal}          { System.out.println("Reconocio "+yytext()+" decimal"); return new Symbol(sym.numDecimal, yycolumn, yyline, yytext()); } 
{cadena}             { System.out.println("Reconocio "+yytext()+" cadena"); return new Symbol(sym.cadena, yycolumn, yyline, yytext()); }

//------> Espacios
[ \t\r\n\f]                  {  /* Espacios en blanco, se ignoran */}
{ComentarioLinea}            {  System.out.println("Reconocio "+yytext()+" coment"); return new Symbol(sym.cadena, yycolumn, yyline, yytext());}
{ComentarioMulti}            {  System.out.println("Reconocio "+yytext()+" coment"); return new Symbol(sym.cadena, yycolumn, yyline, yytext());}

//------> Errores Lexicos
.                            { System.out.println("Error Lexico"+yytext()+" Linea "+yyline+" Columna "+yycolumn);}
