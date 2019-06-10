import java.util.Scanner

fun main(){
    val sudoku = Sudoku(Scanner(System.`in`).nextInt())
    val generated = completeIt(sudoku)
    println(generated)
}
