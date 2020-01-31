import java.io.File
import java.io.RandomAccessFile

class Parser {
    private lateinit var raf: RandomAccessFile


    fun Parser.parseFile(file: File): List<Area>{
        raf = RandomAccessFile(file, "rw")

        return emptyList()
    }

    private fun nextLine(){
        var byte = 0
        while (byte != 10 ) byte = raf.read()
    }

    private fun readCategoryProtection() = readToken(0, 6)
    private fun readNumber() = readToken(30, 3)
    private fun readNumberKv() = readToken(6, 4)
    private fun readLesb() = readToken(14, 4)
    private fun readArea() = readToken(33, 5)
    private fun readOzu() = readToken(42, 4)

    private fun readToken(offset: Int, size: Int) : String{
        val arr = ByteArray(size)
        raf.read(arr, offset, size)
        return arr.toString()
    }



}