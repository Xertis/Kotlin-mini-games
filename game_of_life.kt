import kotlin.random.Random

fun print_array(array: Array<Array<Int>>) {
    for (line in array) {
        for (item in line) {
            var unit = "  "
            when (item) {
                0 -> unit = "⬛"
                1 -> unit = "⬜"
            }
            print(unit)
        }
        println()
    }
    println()
}

fun noise(array: Array<Array<Int>>): Array<Array<Int>> {
    for (i in array.indices) {
        for (j in array[i].indices) {
            val unit = Random.nextInt(0, 2)

            if (unit == 1) {
                array[i][j] = 1
            }
        }
    }

    return array
}

fun get_count(array: Array<Array<Int>>, x: Int, y: Int): Int {
    var count: Int = 0

    for (neighbour_x in x-1..x+1) {
        for (neighbour_y in y-1..y+1) {
            if (neighbour_x < 0 || neighbour_x >= array[0].size) {
                continue
            } else if (neighbour_y < 0 || neighbour_y >= array.size) {
                continue
            } else if (neighbour_x == x && neighbour_y == y) {
                continue
            }

            if (array[neighbour_y][neighbour_x] == 1) {
                count++
            }
        }
    }

    return  count
}

fun game_gen(array: Array<Array<Int>>): Array<Array<Int>> {
    val new_array = Array(array.size, {Array(array[0].size, {0})})
    for (y in array.indices) {
        for (x in array[y].indices) {
            val neighbors = get_count(array, x, y)
            val is_alive = array[y][x] == 1

            new_array[y][x] = when {
                is_alive && (neighbors == 2 || neighbors == 3) -> 1
                !is_alive && neighbors == 3 -> 1
                else -> 0
            }
        }
    }

    return new_array
}

fun main() {
    var grid: Array<Array<Int>> = Array(16, {Array(16, {0})})
    grid = noise(grid)

    for (i in 0 .. 25) {
        print_array(grid)
        grid = game_gen(grid)
    }
}
