fun Array<Boolean>.evaluate(): Int = if (this.indexOfFirst { x -> x } == this.indexOfLast { x -> x })
    indexOfFirst { x -> x } else -1

/**
 * Warum das hier nicht in der Sudoku Klasse ist? Naja damit sich Array<Boolean>.evaluate() nicht so allein fühlt!
 *
 * Die Methode is übrigens zu schlecht für n > 3, aber in der Theorie geht das so mit viel Zeit und Arbeitsspicher
  */

fun completeIt(sudoku: Sudoku?):Sudoku? {
    if(sudoku == null) return null
    sudoku.solve()
    if(!sudoku.isValid()) return null
    if(sudoku.isSolved()) return sudoku
    println(sudoku)
    var m = mutableListOf<Sudoku?>()
    for (y in 0 until sudoku.size*sudoku.size)
        for (x in 0 until sudoku.size*sudoku.size)
            if(sudoku.getFieldEntry(x, y).evaluate() == -1)
                for(e in sudoku.getFieldEntry(x, y).indices)
                    if(sudoku.getFieldEntry(x, y)[e]) {
                        val s = sudoku.getCopy()
                        s.setFieldEntry(x,y,e)
                        m.add(s)
                    }
    m.shuffle()

    m.removeAll { n -> n == null }

    println(m.size)

    for(n in m){
        val o = completeIt(n)
        if(o != null) return o
    }

    return null
}