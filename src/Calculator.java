import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*
 * Я нашла, как написать проще, но так как проверяется умение что-то написать, а не гуглить,
 * я создала монстра длинной с войну и мир.
 */
public class Calculator {
    private static final HashMap<Integer, String> romansPieces = new HashMap<>();
    private static final ArrayList<String> allRomansInUse = new ArrayList<>();
    private static final ArrayList<Integer> allArabicsInUse = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        fillingIn();
        System.out.println("Type an expression that would look like a + b, don't forget spaces!");
        System.out.println("Type \"stop\" to stop calculations");
        while (scanner.hasNext()) {
            String expression = scanner.nextLine();
            //Чтобы можно было завершить программу не через ошибку
            if (expression.equals("stop")) {
                break;
            }
            String calculated=calc(expression);
            System.out.println(calculated);
            System.out.println("Type the next expression");
        }
    }

    // Метод заполняет списки римских и арабский цифр. Списки нужны, чтобы контролировать введенные значения в
    // калькуляторе. Если числа нет в списке - то оно нарушает условия задачи и выходит ошибка.
    public static void fillingIn() {
        romansPieces.put(100, "C");
        romansPieces.put(90, "XC");
        romansPieces.put(50, "L");
        romansPieces.put(40, "XL");
        romansPieces.put(10, "X");
        romansPieces.put(9, "IX");
        romansPieces.put(5, "V");
        romansPieces.put(4, "IV");
        romansPieces.put(1, "I");
        //Массив для десятков. Нужен для двузначных чисел.
        String[] dozens = new String[10];
        int count = 0;
        for (int i = 1; i <= 100; i++) {
            //Заполнение первых десяти цифр. Нужен для однозначных чисел и первого разряда двузначных чисел.
            if (i < 10) {
                if (romansPieces.containsKey(i)) {
                    allRomansInUse.add(romansPieces.get(i));
                } else {
                    String number = allRomansInUse.get(allRomansInUse.size() - 1) +
                            "I";
                    allRomansInUse.add(number);
                }
            }
            //Заполнение последующих римский цифр до 100.
            else {
                //Заполнение массива десятков для последующего использования в двузначных числах.
                // Так же в общий список римских цифр вносятся основные элементы, которые нельзя собрать
                // путем добавления X к числу.
                if (i % 10 == 0) {
                    if (romansPieces.containsKey(i)) {
                        allRomansInUse.add(romansPieces.get(i));
                        dozens[count] = romansPieces.get(i);
                        count++;
                    } else {
                        String basis = allRomansInUse.get(allRomansInUse.size() - 10);
                        allRomansInUse.add(basis + "X");
                        dozens[count] = basis + "X";
                        count++;
                    }
                }
                //Заполнение оставшихся римских цифр
                else {
                    int firstDigit = i / 10;
                    int secondDigit = i % 10;
                    String secondDigitRoman = allRomansInUse.get(secondDigit - 1);
                    String makeshiftNumber = dozens[firstDigit - 1] + secondDigitRoman;
                    allRomansInUse.add(makeshiftNumber);
                }
            }
        }
        //заполнение списка арабских цифр
        for (int i = -100; i <= 100; i++) {
            allArabicsInUse.add(i);
        }
    }

    //Метод проводит вычисления с римскими и арабскими цифрами.
    public static String calc(String input){
        String result="";
        try {
            String[] parsed = input.split(" ");
            if (!(parsed.length == 3)) {
                throw new Exception();
            }

            //Для римских цифр
            if (allRomansInUse.contains(parsed[0]) && allRomansInUse.contains(parsed[2])) {
                int a = allRomansInUse.indexOf(parsed[0]) + 1;
                int b = allRomansInUse.indexOf(parsed[2]) + 1;
                if (a < 1 || a > 10 || b < 1 || b > 10) {
                    throw new Exception();
                }
                String sign = parsed[1];
                result = switch (sign) {
                    case "+" -> String.valueOf(allRomansInUse.get(a + b - 1));
                    case "-" -> String.valueOf(allRomansInUse.get(a - b - 1));
                    case "*" -> String.valueOf(allRomansInUse.get(a * b - 1));
                    case "/" -> String.valueOf(allRomansInUse.get(Math.round(a / b - 1)));
                    default -> throw new Exception();
                };
                //Для арабских цифр
            } else if (allArabicsInUse.contains(Integer.parseInt(parsed[0]))
                    && allArabicsInUse.contains(Integer.parseInt(parsed[2]))) {
                int a = Integer.parseInt(parsed[0]);
                int b = Integer.parseInt(parsed[2]);
                if (a < 1 || a > 10 || b < 1 || b > 10) {
                    throw new Exception();
                }
                String sign = parsed[1];
                result = switch (sign) {
                    case "+" -> String.valueOf(a + b);
                    case "-" -> String.valueOf(a - b);
                    case "*" -> String.valueOf(a * b);
                    case "/" -> String.valueOf(Math.round(a / b));
                    default -> throw new Exception();
                };
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Something went wrong");
        }
        return result;
    }




}
