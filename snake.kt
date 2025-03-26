import kotlin.random.Random

const val WIDTH = 16
const val HEIGHT = 16

const val START_X = 0
const val START_Y = 0


fun print_array(array: Array<ByteArray>) {
    for (line in array) {
        for (item in line) {
            var unit = " "
            when (item) {
                0.toByte() -> unit = "⬛"
                1.toByte() -> unit = "⬜"
                2.toByte() -> unit = "❎"
                3.toByte() -> unit = "🍎"
            }
            print(unit)
        }
        println()
    }
    println()
}

fun ask_move(): Pair<Int, Int> {
    var (summandX, summandY) = Pair(0, 0)

    when (readlnOrNull() ?: "a") {
        "a" -> {summandX = -1; summandY = 0}
        "d" -> {summandX = 1; summandY = 0}
        "w" -> {summandX = 0; summandY = -1}
        "s" -> {summandX = 0; summandY = 1}
    }

    return Pair(summandX, summandY)
}

fun spawn_apple(array: Array<ByteArray>): Array<ByteArray> {
    var x = Random.nextInt(0, WIDTH)
    var y = Random.nextInt(0, HEIGHT)
    while (array[y][x] != 0.toByte()) {
        x = Random.nextInt(0, WIDTH)
        y = Random.nextInt(0, HEIGHT)
    }
    array[y][x] = 3

    return array
}

fun tail_move(array: Array<ByteArray>, tail: MutableList<IntArray>, x: Int, y: Int): MutableList<IntArray> {
    var nextX = x
    var nextY = y

    for (unit in tail) {
        val (oldX, oldY) = unit

        unit[0] = nextX
        unit[1] = nextY

        array[nextY][nextX] = 2

        nextX = oldX
        nextY = oldY
    }

    array[nextY][nextX] = 0

    return tail
}

fun main() {
    var grid: Array<ByteArray> = Array(HEIGHT) { ByteArray(WIDTH) }
    var (x, y) = Pair(START_X, START_Y)
    var tail: MutableList<IntArray> = mutableListOf(intArrayOf(START_X, START_Y))
    grid[y][x] = 1

    grid = spawn_apple(grid)

    while (true) {
        print_array(grid)
        val pos = ask_move()

        if (x+pos.first < 0 || x+pos.first >= WIDTH) {
            continue
        } else if (y+pos.second < 0 || y+pos.second >= HEIGHT) {
            continue
        }

        x += pos.first
        y += pos.second

        val unit = grid[y][x]

        if (unit == 3.toByte()) {
            tail.add(intArrayOf(x.toInt(), y.toInt()))
            grid = spawn_apple(grid)
        } else if (unit == 2.toByte()) {
            println("Game over")

            val score = tail.size - 1
            print("Score: $score")
            break
        }

        tail = tail_move(grid, tail, x, y)
        grid[y][x] = 1
    }
}