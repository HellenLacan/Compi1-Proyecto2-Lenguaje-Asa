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

    /* comments */
    ComentarioMulti = {TraditionalComment}
    ComentarioLinea = {EndOfLineComment}

    TraditionalComment   = "</" [^/] ~"/>" | "</" "/"+ ">"
    // Comment can be the last line of the file, without line terminator.
    EndOfLineComment     = "-->" {InputCharacter}* {LineTerminator}?
    DocumentationComment = "/**" {CommentContent} "*"+ "/"
    CommentContent       = ( [^*] | \*+ [^/*] )*

cadena      = [A-Za-zñÑ][_0-9A-Za-zñÑ|_]*
numero = [0-9]+

SPACE   = [\ \r\t\f\t]
ENTER   = [\ \n]
CADENA =  "\"" [^\"\n]* "\""

importar = "Importar"
definir = "Definir"

punto = ".";
ptoYcoma = ";";  

%%
<YYINITIAL> {importar}    { return new Symbol(sym.importar, yyline, yycolumn,yytext());} 
<YYINITIAL> {definir}    { return new Symbol(sym.definir, yyline, yycolumn,yytext());} 

<YYINITIAL> {cadena}    { return new Symbol(sym.cadena, yyline, yycolumn,yytext());} 
<YYINITIAL> {numero}    {return new Symbol(sym.numero, yyline, yycolumn,yytext());}
<YYINITIAL> {SPACE}     { /*Espacios en blanco, ignorados*/ }
<YYINITIAL> {ENTER}     { /*Saltos de linea, ignorados*/}
<YYINITIAL> {ComentarioLinea}      {System.out.println("Coment\n" + yytext());}
<YYINITIAL> {ComentarioMulti}      {System.out.println("ComentMulti \n" + yytext());}

<YYINITIAL> . {
        String errLex = "Error léxico, caracter irreconocible: '"+yytext()+"' en la línea: "+(yyline+1)+" y columna: "+yycolumn;
        System.err.println(errLex);
}
