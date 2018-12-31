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
WhiteSpace     = {LineTerminator} | [ \t\f]

ComentarioMulti = {TraditionalComment}
ComentarioLinea = {EndOfLineComment}
TraditionalComment   = "</" [^/] ~"/>" | "</" "/"+ ">"
EndOfLineComment     = "-->" {InputCharacter}* {LineTerminator}?

 identificador = [A-Za-zñÑ][_0-9A-Za-zñÑ|_]*
 numEntero = (\+|-)?.[0-9]+
 numDecimal = (\+|-)? [0-9]+(.[0-9]*)? | (.[0-9]*)?   
 cadena =  "\"" [^\"\n]* "\""

importar = "Importar"
definir = "Definir"
decimal = "Decimal"
booleano = "Booleano"
texto = "Texto"
entero = "Entero"
vacio = "Vacio"
_verdadero = "verdadero"
_falso = "falso"


punto = "."
ptoYcoma = ";"

%%
<YYINITIAL> {importar}    { return new Symbol(sym.importar, yyline, yycolumn,yytext());} 
<YYINITIAL> {definir}    { return new Symbol(sym.definir, yyline, yycolumn,yytext());} 
<YYINITIAL> {decimal}    { return new Symbol(sym.decimal, yyline, yycolumn,yytext());} 
<YYINITIAL> {booleano}    { return new Symbol(sym.booleano, yyline, yycolumn,yytext());} 
<YYINITIAL> {texto}    { return new Symbol(sym.texto, yyline, yycolumn,yytext());} 
<YYINITIAL> {entero}    { return new Symbol(sym.entero, yyline, yycolumn,yytext());} 
<YYINITIAL> {vacio}    { return new Symbol(sym.vacio, yyline, yycolumn,yytext());} 
<YYINITIAL> {_verdadero}    { return new Symbol(sym._verdadero, yyline, yycolumn,yytext());} 
<YYINITIAL> {_falso}    { return new Symbol(sym._falso, yyline, yycolumn,yytext());} 

<YYINITIAL> {punto}    { return new Symbol(sym.punto, yyline, yycolumn,yytext());} 
<YYINITIAL> {ptoYcoma}    { return new Symbol(sym.ptoYcoma, yyline, yycolumn,yytext());} 

<YYINITIAL> {numEntero}    {System.out.println("Entero-> " + yytext()); return new Symbol(sym.numEntero, yyline, yycolumn,yytext());} 
<YYINITIAL> {numDecimal}    {System.out.println("Decimal-> " + yytext()); return new Symbol(sym.numDecimal, yyline, yycolumn,yytext());} 
<YYINITIAL> {cadena}    {System.out.println("Cadena-> " + yytext()); return new Symbol(sym.cadena, yyline, yycolumn,yytext());} 

<YYINITIAL> {identificador}    {return new Symbol(sym.identificador, yyline, yycolumn,yytext());} 
<YYINITIAL> {LineTerminator}     { /*Espacios en blanco, ignorados*/ }
<YYINITIAL> {ComentarioLinea}      {System.out.println("Coment-> " + yytext());}
<YYINITIAL> {ComentarioMulti}      {System.out.println("ComentMulti-> " + yytext());}
<YYINITIAL> {WhiteSpace}         {}

<YYINITIAL> . {
        String errLex = "Error léxico, caracter irreconocible: '"+yytext()+"' en la línea: "+(yyline+1)+" y columna: "+yycolumn;
        System.err.println(errLex);
}