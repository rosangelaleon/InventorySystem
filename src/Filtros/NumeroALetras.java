package Filtros;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumeroALetras {
    private static final String[] UNIDADES = {"", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};
    private static final String[] DECENAS = {"", "diez", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa"};
    private static final String[] CENTENAS = {"", "ciento", "doscientos", "trescientos", "cuatrocientos", "quinientos", "seiscientos", "setecientos", "ochocientos", "novecientos"};

    public static String convertir(BigDecimal numero) {
        // Redondear para evitar problemas con decimales no deseados
        numero = numero.setScale(2, RoundingMode.HALF_UP);

        long parteEntera = numero.longValue(); // Parte entera
        int centavos = numero.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue(); // Parte decimal

        StringBuilder resultado = new StringBuilder();

        if (parteEntera == 0) {
            resultado.append("cero");
        } else {
            resultado.append(convertirNumero(parteEntera));
        }

        if (centavos > 0) {
            resultado.append(" con ").append(convertirNumero(centavos)).append(" centavos");
        }

        return resultado.toString().trim();
    }

    private static String convertirNumero(long numero) {
        if (numero == 0) return "";

        StringBuilder letras = new StringBuilder();

        // Manejo de millones
        if (numero / 1000000 > 0) {
            if (numero / 1000000 == 1) {
                letras.append("un millón ");
            } else {
                letras.append(convertirNumero(numero / 1000000)).append(" millones ");
            }
            numero %= 1000000;
        }

        // Manejo de miles
        if (numero / 1000 > 0) {
            if (numero / 1000 == 1) {
                letras.append("mil ");
            } else {
                letras.append(convertirNumero(numero / 1000)).append(" mil ");
            }
            numero %= 1000;
        }

        // Manejo de centenas
        if (numero / 100 > 0) {
            if (numero / 100 == 1 && numero % 100 == 0) {
                letras.append("cien ");
            } else {
                letras.append(CENTENAS[(int) (numero / 100)]).append(" ");
            }
            numero %= 100;
        }

        // Manejo de decenas y unidades
        if (numero > 0) {
            if (numero < 10) {
                letras.append(UNIDADES[(int) numero]);
            } else if (numero < 20) {
                switch ((int) numero) {
                    case 10 -> letras.append("diez");
                    case 11 -> letras.append("once");
                    case 12 -> letras.append("doce");
                    case 13 -> letras.append("trece");
                    case 14 -> letras.append("catorce");
                    case 15 -> letras.append("quince");
                    default -> letras.append("dieci").append(UNIDADES[(int) (numero - 10)]);
                }
            } else if (numero < 30) {
                letras.append("veinti").append(UNIDADES[(int) (numero - 20)]);
            } else {
                letras.append(DECENAS[(int) (numero / 10)]);
                if (numero % 10 > 0) {
                    letras.append(" y ").append(UNIDADES[(int) (numero % 10)]);
                }
            }
        }

        return letras.toString().trim();
    }

    public static String capitalizarPrimeraLetra(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    public static void main(String[] args) {
        // Pruebas
        BigDecimal monto1 = new BigDecimal("30.66");
        BigDecimal monto2 = new BigDecimal("21.00");
        BigDecimal monto3 = new BigDecimal("0");

        System.out.println(capitalizarPrimeraLetra(convertir(monto1))); // Treinta con sesenta y seis centavos
        System.out.println(capitalizarPrimeraLetra(convertir(monto2))); // Veintiuno
        System.out.println(capitalizarPrimeraLetra(convertir(monto3))); // Cero
    }
}
