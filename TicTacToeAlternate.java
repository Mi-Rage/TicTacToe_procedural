/**
 * Эта версия сделана по текстовомму заданию.
 * Компьютер анализирует все клетки и делает ход только там,
 * где считает необходимым, необязательно только рядом со своим ходом.
 */

import java.util.Random;
import java.util.Scanner;

public class TicTacToeAlternate {
    /*
    блок настроек игры
    */
    private static char[][] map;                // Матрица игры
    private static int SIZE = 3;                // Размер поля

    private static final char DOT_EMPTY = '*';  // Пустой символ
    private static final char DOT_X = 'X';      // Символ Х
    private static final char DOT_O = 'O';      // Символ О

    private static final boolean SILLY_MODE = false;      // false - комп умный, true - комп глупый

    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();




    public static void main(String[] args) {
        initMap();
        printMap();

        while(true){
            humanTurn();    // Ход человека
            if(isEndGame(DOT_X)){
                break;
            }

            computerTurn(); // Ход компьютера
            if(isEndGame(DOT_O)){
                break;
            }
        }
        System.out.println("Игра закончена!");
    }

    /*
    Инициализация игрового поля
     */
    private static void initMap(){
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                map[i][j] = DOT_EMPTY;
            }
        }
    }

    /**
     *Вывод поля на экран
     **/
    private static void printMap(){
        for (int i = 0; i <= SIZE; i++){
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++){
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Ход человека
     */
    private static void humanTurn(){
        int x, y; // x - номер столбца, y - номер строки

        do{
            System.out.println("Введите координаты ячейки через пробел:");
            y = scanner.nextInt() - 1;
            x = scanner.nextInt() - 1;

        }while(!isCellValid(x, y));

        map[y][x] = DOT_X;
    }

    /**
     * Ход компьютера
     */
    private static void computerTurn(){
        int x = -1;
        int y = -1;

        boolean foundWin = false; // Признак нахождения выигрышного хода


        if(SILLY_MODE){
            do {
                x = random.nextInt(SIZE);
                y = random.nextInt(SIZE);
            }while (!isCellValid(x, y));

            System.out.println("Комп выбрал ячейку " + (y + 1) + " " + (x + 1));
            map[y][x] = DOT_O;
        } else {
            for (int i = 0; i < SIZE; i++) {        // Проходим циклом по всему полю
                for (int j = 0; j < SIZE; j++) {    // по каждой клетке
                    if (isCellValid(i,j)){          // Если очередная клетка не занята
                        map[j][i] = DOT_O;          // Подставляем в неё символ своего хода - О
                        if(checkWin(DOT_O)){        // Если в этой клетке с этим символом мы победим, то
                            x = i;                  // Запоминаем координаты этой клетки
                            y = j;
                            foundWin = true;        // подтверждаем найденность выигрышного хода
                            break;                  // выходим из цикла
                        }

                        map[j][i] = DOT_X;         // Подставляем в клетку символ хода врага - Х
                        if(checkWin(DOT_X)) {      // Если в этой клетке враг победит, то
                            x = i;                  // Запоминаем координаты этой клетки
                            y = j;
                            foundWin = true;       // Включаем признак найденного выигрыша
                            break;                 // Выходим из цикла

                        }                       // Если в этой клетке никто не побеждает
                        map[j][i] = DOT_EMPTY;  // Восстанавливаем её пустое значение

                    }


                }
                if(foundWin) // Если ход найден - выходим из цикла
                    break;
            }
            if(!foundWin){                          // Если выигрышной комбинации нет
                do {                                // Делаем рандомный ход
                    x = random.nextInt(SIZE);
                    y = random.nextInt(SIZE);
                }while (!isCellValid(x, y));

                System.out.println("Комп выбрал ячейку " + (y + 1) + " " + (x + 1));
                map[y][x] = DOT_O;
            } else {                                // иначе, если выигрышная комбинация есть
                System.out.println("Комп выбрал ячейку " + (y + 1) + " " + (x + 1));
                map[y][x] = DOT_O;                  // ходим по запомненным координатам
            }
        }

    }

    /**
     * Метод валидации запрашиваемой ячейки на корректность введенных координат
     * @param x - координата по горизонтали
     * @param y  -координата по вертикали
     * @return boolean - признак валидности
     */
    private static boolean isCellValid(int x, int y){
        boolean result = true;

        if(x < 0 || x >= SIZE || y < 0 || y >= SIZE){
            result = false;
            return result;
        }
        if(map[y][x] != DOT_EMPTY){
            result = false;
        }

        return result;
    }

    /**
     * Метод проверки игры на окончание
     * $param playerSymbol - символ, которым играет игрок
     * $return boolean - признак завершения игры
     */
    private static boolean isEndGame(char playerSymbol){
        boolean result = false;

        printMap();

        // проверяем необходимость следующего хода
        if(checkWin(playerSymbol)){
            System.out.println("Победили " + playerSymbol);
            result = true;
        }
        if(isMapFull()){
            System.out.println("Ничья");
            result = true;
        }
        return result;
    }


    /**
     * Проверяем заполненность поля на 100%
     * $result boolean - признак заполнено или нет
     */
    private static boolean isMapFull() {
        boolean result = true;
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                if (map[i][j] == DOT_EMPTY){
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * Метод проверки выигрыша
     * $param playerSymbol - символ текущего игрока
     * $result boolean - признак победы игрока
     */
    private static boolean checkWin(char playerSymbol){
        // Изначально результат - без выигрыша
        boolean result = false;
        // Создадим переменные контроля выигрыша по строкам, столбцам и двум диагоналям
        int stringWin = 0, rawWin = 0, diagOneWin = 0, diagTwoWin = 0;

        // Создаем двойной цикл обхода всех клеток нашего поля для контроля выигрышной ситуации
        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                if(map[i][j] == playerSymbol){ // Если в строке есть символ проверяемого игрока - счетчик контроля +1
                    stringWin++;
                }
                if (map[j][i] == playerSymbol){ // Если в столбце есть символ проверяемого игрока - счетчик контроля +1
                    rawWin++;
                }
            }
            if (map[i][i] == playerSymbol){ // Если в одной диагонали есть символ игрока - счетчик контроля +1
                diagOneWin++;
            }
            if (map[i][(SIZE-1) - i] == playerSymbol){ // Если в другой диагонали есть символ игрока - счечик +1
                diagTwoWin++;
            }
            if (stringWin == SIZE || rawWin == SIZE){ // По строкам есть столько совпадений, сколько задан SIZE?
                result = true;                        // Если да - то возвращаем true
                return result;
            } else {                                  // Если нет - обнуляем переменные контроля и продолжаем цикл
                stringWin = 0;
                rawWin = 0;
            }
        }
        if (diagOneWin == SIZE || diagTwoWin == SIZE){ // По диагонали есть столько совпадений сколько задан SIZE?
            result = true;                             // Если да - то возвращаем true
            return result;
        }
        return result;                                  // Если ничего в циклах не нашлось - возвращаем что было, false
    }
}
