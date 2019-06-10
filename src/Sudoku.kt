class Sudoku(val size: Int) {
    private val fieldSize = size * size * size * size
    private val field: Array<Array<Boolean>> = Array(fieldSize) { Array(size * size) { true } }
    private val maxNumLength = (-size * size).toString().length

    /// This is fucking terrible
    override fun toString(): String {
        val sb = StringBuilder("")
        for (i in field.indices) {

            if (i % size == 0)
                sb.append("|")
            if (i % (size * size) == 0 && i != 0)
                sb.append("\n|")


            if (i % (size * size * size) == 0) {
                for (l in 1 until maxNumLength * size * size + size)
                    sb.append("-")
                sb.append("|\n|")
            }

            val entry = (field[i].evaluate()+1).toString()
            for (k in 0 until maxNumLength - entry.length)
                sb.append(" ")
            sb.append(entry)

        }
        sb.append("|\n|")
        for (l in 0 until maxNumLength * size * size + size - 1)
            sb.append("-")
        sb.append("|")
        return sb.toString()
    }

    fun getFieldEntry(x:Int, y:Int) = this.field[size*size*y+x]
    fun setFieldEntry(x:Int, y:Int, value:Int){
        this.getFieldEntry(x,y).fill(false)
        this.getFieldEntry(x,y)[value] = true
    }
    private fun removePossibility(x:Int, y:Int, value:Int, _x:Int, _y:Int):Boolean{
        if((x == _x && y == _y) || !this.getFieldEntry(x,y)[value])
            return false
        this.getFieldEntry(x,y)[value] = false
        return true
    }
    private fun checkoutPossibility(x:Int, y:Int, value:Int, _x:Int, _y:Int) = if((x == _x && y == _y)) false else this.getFieldEntry(x,y)[value]

    fun isSolved() = !this.getLoadableString().contains("!")

    fun isValid(): Boolean{
        for(a in this.field)
            if(!a.contains(true))
                return false
        return true
    }

    public fun solve(): Boolean{
        var iteration = 1
        while (removeTrivial() || removeLessTrivial()){
            //println("$iteration. remove Iteration")
            iteration++
        }
        return iteration != 1
    }

    private fun removeTrivial():Boolean{
        var changed = false
        for (y in 0 until size*size)
            for (x in 0 until size*size) {
                val value = this.getFieldEntry(x, y).evaluate()
                if (value != -1) {
                    val a = x/size
                    val b = y/size
                    for (z in 0 until size*size){
                        changed = removePossibility(z,y,value,x,y) || changed
                        changed = removePossibility(x,z,value,x,y) || changed
                        changed = removePossibility(a*size + (z % size),b*size + (z/size),value,x,y) || changed
                    }
                }
            }
        return changed
    }

    private fun removeLessTrivial():Boolean{
        var changed = false
        for (y in 0 until size*size)
            for (x in 0 until size*size)
                if(this.getFieldEntry(x, y).evaluate() == -1)
                    for(e in this.getFieldEntry(x, y).indices)
                        if(this.getFieldEntry(x, y)[e]){
                            var p1 = true
                            var p2 = true
                            var p3 = true
                            val a = x/size
                            val b = y/size
                            for (z in 0 until size*size){
                                p1 = p1 && !checkoutPossibility(z,y,e,x,y)
                                p2 = p2 && !checkoutPossibility(x,z,e,x,y)
                                p3 = p3 && !checkoutPossibility(a*size + (z % size),b*size + (z/size),e,x,y)
                            }
                            if(p1 || p2 || p3){
                                changed = true
                                this.setFieldEntry(x, y, e)
                                break
                            }
                        }
        return changed
    }

    fun getCopy():Sudoku{
        val s = Sudoku(this.size)
        s.loadString(this.getLoadableString())
        return s
    }

    fun getLoadableString():String{
        var sb = StringBuilder("")
        for(i in this.field.indices) {
            if (i != 0 && i % (size * size) == 0)
                sb.append("\n")
            val s = if(this.field[i].evaluate() == -1) "!" else ((this.field[i].evaluate()+48).toChar()).toString()
            sb.append(s)
        }
        return sb.toString()
    }


    public fun loadString(s:String){
        var x = 0
        var y = 0
        for(i in s){
            if(i=='\n'){
                x = 0
                y++
                continue
            }
            if(i != '!')
                this.setFieldEntry(x,y,i.toInt()-48)
            x++
        }
    }
}