/**
 * Эта версия сделана по алгоритму в видеоуроке.
 * Компьютер ходит только рядом со своим значением,
 * проверяя при этом лучший вариант
 */

import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
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
        int x = 0;
        int y = 0;
        int tmpI = 0;
        int tmpJ = 0;

        boolean foundTurn = false; // Признак нахождения выигрышного хода
        boolean foundWin = false;

        if(SILLY_MODE){
            do {
                x = random.nextInt(SIZE);
                y = random.nextInt(SIZE);
            }while (!isCellValid(x, y));

            System.out.println("Комп выбрал ячейку " + (y + 1) + " " + (x + 1));
            map[y][x] = DOT_O;
        } else {
            for (int i = 0; i < SIZE; i++) {         // Проходим циклом по всему полю
                for (int j = 0; j < SIZE; j++) {     // по каждой клетке
                    if (isCellValid(j,i)) {          // Если клетка, вокруг которой ищем О, пустая, то
                        for (int newX = i - 1; newX <= i + 1; newX++) {       // Создаем новый цикл обхода всех
                            for (int newY = j - 1; newY <= j + 1; newY++) {   // соседних клеток для проверки их на символ О
                                if (newX >= 0 && newY >= 0 && newX < SIZE && newY < SIZE) {    // Если не вышли за размеры поля (массива)
                                    if (map[newX][newY] == DOT_O) {                            // проверяем каждую соседнюю клетку на наличие О
                                        // Включаем думатель
                                        map[i][j] = DOT_O; // Подставляем в клетку символ своего хода - О
                                        tmpI = i;          // Запоминаем координаты
                                        tmpJ = j;
                                        foundTurn = true;     // Включаем признак нацденного хода
                                        if(checkWin(DOT_O)) { // Если в этой клетке с этим символом мы победим, то
                                            System.out.println("Найден выигрыш! Комп выбрал ячейку " + (i + 1) + " " + (j + 1));
                                            foundWin = true;  // Включаем признак найденного выигрыша
                                            break;            // Выходим из цикла

                                        } else {                   // Если в этой клетке не победим, то
                                            map[i][j] = DOT_EMPTY; // восстановим её пустое значение
                                        }                          // и проверяем дальше

                                        map[i][j] = DOT_X;         // Подставляем в клетку символ хода врага - Х
                                        if(checkWin(DOT_X)) {      // Если в этой клетке враг победит, то
                                            System.out.println("Угроза выигрыша по Х! Комп выбрал ячейку " + (i + 1) + " " + (j + 1));
                                            foundWin = true;       // Включаем признак найденного выигрыша
                                            map[i][j] = DOT_O;     // Записываем в эту клетку свой символ - 0
                                            break;                 // Выходим из цикла

                                        } else {                   // Если в этой клетке никто не побеждает
                                            map[i][j] = DOT_EMPTY; // Восстанавливаем её пустое значение
                                        }
                                    }
                                }
                            }
                            if (foundWin)   // Если найден выигрыш
                                break;      // выходим из цикла
                        }
                        if(foundWin)    // Если Если найден выигрыш
                            break;      // выходим из цикла
                    }
                    if(foundWin)  // Если найден выигрыш
                        break;      // выходим из цикла
                }
                if(foundWin)            // Если найден выигрыш
                    break;              // выходим из цикла
            }
            if(!foundTurn && !foundWin){ // Если не нашлось ни одной клетки с ходом компа - это первый ход и он рандомный
                do {
                    x = random.nextInt(SIZE);
                    y = random.nextInt(SIZE);
                }while (!isCellValid(x, y));

                System.out.println("Первый ход компа. Комп выбрал ячейку " + (y + 1) + " " + (x + 1));
                map[y][x] = DOT_O;
            } else {
                if(foundTurn && !foundWin) { // Если нашлась клетка с O и не нашлось угроз или выигрыша
                    map[tmpI][tmpJ] = DOT_O; // Записываем в последние запомненные координаты проверенной клетки
                    System.out.println("Ходим рядом с О. Комп выбрал ячейку " + (tmpI + 1) + " " + (tmpJ + 1));
                }

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
