/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EjecucionExpresiones;

/**
 *
 * @author Hellen
 */
public class ExpresionAritmetica {

    public Object sumar(String tipo1, String num1, String tipo2, String num2) {
        if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Booleano")) {
            int n1 = 0;
            int n2 = 0;
            int resultado = 0;
            if (num1.equalsIgnoreCase("verdadero")) {
                n1 = 1;
            }

            if (num2.equalsIgnoreCase("verdadero")) {
                n2 = 1;
            }

            resultado = n1 + n2;
            return resultado + "@entero";

        } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Decimal")) {
            int n1 = 0;
            double n2 = 0;
            double resultado = 0;
            if (num1.equalsIgnoreCase("verdadero")) {
                n1 = 1;
            }

            n2 = Double.parseDouble(num2);

            resultado = n1 + n2;
            return resultado + "@decimal";

        } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Texto")) {
            int n1 = 0;
            String resultado = "";
            if (num1.equalsIgnoreCase("verdadero")) {
                n1 = 1;
            }
            if (n1 == 0) {
                resultado = "0 " + num2;
            } else {
                resultado = "1 " + num2;
            }
            return resultado + "@texto";

        } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Entero")) {
            int n1 = 0;
            int n2 = 0;
            int resultado = 0;
            if (num1.equalsIgnoreCase("verdadero")) {
                n1 = 1;
            }

            n2 = Integer.parseInt(num2);

            resultado = n1 + n2;
            return resultado + "@entero";

        } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Booleano")) {
            double n1 = 0;
            int n2 = 0;
            double resultado = 0;

            if (num2.equalsIgnoreCase("verdadero")) {
                n2 = 1;
            }

            n1 = Double.parseDouble(num1);

            resultado = n1 + n2;
            return resultado + "@decimal";

        } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Decimal")) {

            double n1 = 0;
            double n2 = 0;
            double resultado = 0;

            n1 = Double.parseDouble(num1);
            n2 = Double.parseDouble(num2);

            resultado = n1 + n2;
            return resultado + "@decimal";

        } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Texto")) {
            double n1 = 0;
            String resultado = "";

            n1 = Double.parseDouble(num1);

            resultado = n1 + " " + num2;
            return resultado + "@texto";

        } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Entero")) {
            double n1 = 0;
            double n2 = 0;
            double resultado = 0;

            n1 = Double.parseDouble(num1);
            n2 = Double.parseDouble(num2);
            resultado = n1 + n2;
            return resultado + "@decimal";

        } else if (tipo1.equalsIgnoreCase("Texto") && tipo2.equalsIgnoreCase("Booleano")) {
            int n2 = 0;
            String resultado = "";
            if (num2.equalsIgnoreCase("verdadero")) {
                n2 = 1;
            }

            resultado = num1 + " " + n2;
            return resultado + "@texto";

        } else if (tipo1.equalsIgnoreCase("Texto") && tipo2.equalsIgnoreCase("Decimal")) {
            double n2 = 0;
            String resultado = "";

            n2 = Double.parseDouble(num2);
            resultado = num1 + " " + n2;
            return resultado + "@texto";

        } else if (tipo1.equalsIgnoreCase("Texto") && tipo2.equalsIgnoreCase("Texto")) {
            return num1 + num2 + "@texto";
        } else if (tipo1.equalsIgnoreCase("Texto") && tipo2.equalsIgnoreCase("Entero")) {
            return num1 + num2 + "@texto";
        } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Booleano")) {
            int n1 = 0;
            int n2 = 0;
            int resultado = 0;
            if (num2.equalsIgnoreCase("verdadero")) {
                n2 = 1;
            }

            n1 = Integer.parseInt(num1);

            resultado = n1 + n2;
            return resultado + "@entero";
        } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Decimal")) {
            double n1 = 0;
            double n2 = 0;
            double resultado = 0;

            n1 = Double.parseDouble(num1);
            n2 = Double.parseDouble(num2);

            resultado = n1 + n2;
            return resultado + "@decimal";
        } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Texto")) {
            String resultado = "";

            resultado = num1 + " " + num2;
            return resultado + "@texto";
        } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Entero")) {
            int n1 = 0;
            int n2 = 0;
            int resultado = 0;

            n1 = Integer.parseInt(num1);
            n2 = Integer.parseInt(num2);

            resultado = n1 + n2;
            return resultado + "@entero";
        }

        return "error";
    }

    public Object restar(String tipo1, String num1, String tipo2, String num2) {
        if (sonTiposIncompatibles(tipo1, tipo2)) {
            return "error tipos Incompatibles";
        } else {
            if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Booleano")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                resultado = n1 - n2;
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Decimal")) {
                int n1 = 0;
                double n2 = 0;
                double resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                n2 = Double.parseDouble(num2);

                resultado = n1 - n2;
                return resultado + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Entero")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                n2 = Integer.parseInt(num2);

                resultado = n1 - n2;
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Booleano")) {
                double n1 = 0;
                int n2 = 0;
                double resultado = 0;

                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                n1 = Double.parseDouble(num1);

                resultado = n1 - n2;
                return resultado + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Decimal")) {

                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);

                resultado = n1 - n2;
                return resultado + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Entero")) {
                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);
                resultado = n1 - n2;
                return resultado + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Booleano")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                n1 = Integer.parseInt(num1);

                resultado = n1 - n2;
                return resultado + "@entero";
            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Decimal")) {
                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);

                resultado = n1 - n2;
                return resultado + "@decimal";
            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Entero")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;

                n1 = Integer.parseInt(num1);
                n2 = Integer.parseInt(num2);

                resultado = n1 - n2;
                return resultado + "@entero";
            }
        }
        return "error";
    }

    public Object multiplicar(String tipo1, String num1, String tipo2, String num2) {
        if (sonTiposIncompatibles(tipo1, tipo2)) {
            return "error tipos Incompatibles";
        } else {
            if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Booleano")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                resultado = n1 * n2;
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Decimal")) {
                int n1 = 0;
                double n2 = 0;
                double resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                n2 = Double.parseDouble(num2);

                resultado = n1 * n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Entero")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                n2 = Integer.parseInt(num2);

                resultado = n1 * n2;
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Booleano")) {
                double n1 = 0;
                int n2 = 0;
                double resultado = 0;

                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                n1 = Double.parseDouble(num1);

                resultado = n1 * n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Decimal")) {

                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);

                resultado = n1 * n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Entero")) {
                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);
                resultado = n1 * n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Booleano")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                n1 = Integer.parseInt(num1);

                resultado = n1 * n2;
                return resultado + "@entero";
            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Decimal")) {
                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);

                resultado = n1 * n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";
            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Entero")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;

                n1 = Integer.parseInt(num1);
                n2 = Integer.parseInt(num2);

                resultado = n1 * n2;
                return resultado + "@entero";
            }
        }
        return "error";
    }

    public Object dividir(String tipo1, String num1, String tipo2, String num2) {
        if (sonTiposIncompatibles(tipo1, tipo2)) {
            return "error tipos Incompatibles";
        } else {
            if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Booleano")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                resultado = n1 / n2;
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Decimal")) {
                int n1 = 0;
                double n2 = 0;
                double resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                n2 = Double.parseDouble(num2);

                resultado = n1 / n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Entero")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                n2 = Integer.parseInt(num2);

                resultado = n1 / n2;
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Booleano")) {
                double n1 = 0;
                int n2 = 0;
                double resultado = 0;

                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                n1 = Double.parseDouble(num1);

                resultado = n1 / n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Decimal")) {

                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);

                resultado = n1 / n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Entero")) {
                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);
                resultado = n1 / n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Booleano")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                n1 = Integer.parseInt(num1);

                resultado = n1 / n2;
                return resultado + "@entero";
            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Decimal")) {
                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);

                resultado = n1 / n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";
            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Entero")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;

                n1 = Integer.parseInt(num1);
                n2 = Integer.parseInt(num2);

                if (n2 != 0) {
                    resultado = n1 / n2;
                    return resultado + "@entero";
                } else {
                    System.out.println("No es posible dividir dentro de 0");
                    return "error";
                }
            }
        }
        return "error";
    }

    public Object modular(String tipo1, String num1, String tipo2, String num2) {
        if (sonTiposIncompatibles(tipo1, tipo2)) {
            return "error tipos Incompatibles";
        } else {
            if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Booleano")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                resultado = n1 % n2;
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Decimal")) {
                int n1 = 0;
                double n2 = 0;
                double resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                n2 = Double.parseDouble(num2);

                resultado = n1 % n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Entero")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                n2 = Integer.parseInt(num2);

                resultado = n1 % n2;
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Booleano")) {
                double n1 = 0;
                int n2 = 0;
                double resultado = 0;

                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                n1 = Double.parseDouble(num1);

                resultado = n1 % n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Decimal")) {

                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);

                resultado = n1 % n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Entero")) {
                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);
                resultado = n1 % n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Booleano")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                n1 = Integer.parseInt(num1);

                resultado = n1 % n2;
                return resultado + "@entero";
            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Decimal")) {
                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);

                resultado = n1 % n2;
                String res = String.format("%.2f", resultado);
                return res + "@decimal";
            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Entero")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;

                n1 = Integer.parseInt(num1);
                n2 = Integer.parseInt(num2);

                resultado = n1 % n2;
                return resultado + "@entero";
            }
        }
        return "error";
    }

    public Object potencia(String tipo1, String num1, String tipo2, String num2) {
        if (sonTiposIncompatibles(tipo1, tipo2)) {
            return "error tipos Incompatibles";
        } else {
            if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Booleano")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                resultado = (int) Math.pow(n1, n2);
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Decimal")) {
                int n1 = 0;
                double n2 = 0;
                double resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                n2 = Double.parseDouble(num2);

                resultado = Math.pow(n1, n2);
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Entero")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num1.equalsIgnoreCase("verdadero")) {
                    n1 = 1;
                }

                n2 = Integer.parseInt(num2);

                resultado = (int) Math.pow(n1, n2);
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Booleano")) {
                double n1 = 0;
                int n2 = 0;
                double resultado = 0;

                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                n1 = Double.parseDouble(num1);

                resultado = Math.pow(n1, n2);
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Decimal")) {

                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);

                resultado = Math.pow(n1, n2);
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Entero")) {
                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);
                resultado = Math.pow(n1, n2);
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Booleano")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;
                if (num2.equalsIgnoreCase("verdadero")) {
                    n2 = 1;
                }

                n1 = Integer.parseInt(num1);
                resultado = (int) Math.pow(n1, n2);
                return resultado + "@entero";

            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Decimal")) {
                double n1 = 0;
                double n2 = 0;
                double resultado = 0;

                n1 = Double.parseDouble(num1);
                n2 = Double.parseDouble(num2);

                resultado = Math.pow(n1, n2);
                String res = String.format("%.2f", resultado);
                return res + "@decimal";

            } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Entero")) {
                int n1 = 0;
                int n2 = 0;
                int resultado = 0;

                n1 = Integer.parseInt(num1);
                n2 = Integer.parseInt(num2);

                resultado = (int) Math.pow(n1, n2);
                return resultado + "@entero";
            }
        }
        return "error";
    }

    public boolean sonTiposIncompatibles(String tipo1, String tipo2) {
        if (tipo1.equalsIgnoreCase("Booleano") && tipo2.equalsIgnoreCase("Texto")) {
            return true;
        } else if (tipo1.equalsIgnoreCase("Decimal") && tipo2.equalsIgnoreCase("Texto")) {
            return true;
        } else if (tipo1.equalsIgnoreCase("Texto") && tipo2.equalsIgnoreCase("Booleano")) {
            return true;
        } else if (tipo1.equalsIgnoreCase("Texto") && tipo2.equalsIgnoreCase("Decimal")) {
            return true;
        } else if (tipo1.equalsIgnoreCase("Texto") && tipo2.equalsIgnoreCase("texto")) {
            return true;
        } else if (tipo1.equalsIgnoreCase("Texto") && tipo2.equalsIgnoreCase("Entero")) {
            return true;
        } else if (tipo1.equalsIgnoreCase("Entero") && tipo2.equalsIgnoreCase("Texto")) {
            return true;
        }
        return false;
    }

    public String getTipo(String exp) {
        Boolean condNum = false;
        Double numero = 0.0;
        if (exp.equalsIgnoreCase("verdadero")) {
            return "verdadero";
        } else if (exp.equalsIgnoreCase("falso")) {
            return "falso";
        } else {
            try {
                numero = Double.parseDouble(exp);
                condNum = true;
            } catch (NumberFormatException excepcion) {
            }
            if (condNum == true) {
                if (numero % 1 == 0) {
                    return "Entero";
                } else {
                    return "Decimal";
                }
            } else {
                return "String";
            }
        }
    }

}
