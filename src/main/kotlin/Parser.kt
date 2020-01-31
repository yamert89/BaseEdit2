import java.io.File
import java.io.RandomAccessFile

class Parser {
    private lateinit var raf: RandomAccessFile
    private var globalOffset = 0L


    fun Parser.parseFile(file: File): List<Area>{
        raf = RandomAccessFile(file, "rw")
        val list = mutableListOf<Area>()
        while (raf.filePointer < file.length()){
            list.add(Area(
                readNumber().toInt(),
                readNumberKv().toInt(),
                (readArea().toDouble() * 0.1),
                readCategoryArea(),
                readCategoryProtection(),
                readOzu(),
                readLesb()
            ))
            nextLine()
        }
        raf.close()
        return list
    }

    private fun nextLine(){
        var byte = 0
        while (byte != 10 ) byte = raf.read()
        globalOffset = raf.filePointer
    }

    private fun readCategoryProtection() = readToken(0, 6)
    private fun readNumber() = readToken(30, 3)
    private fun readNumberKv() = readToken(6, 4)
    private fun readLesb() = readToken(14, 4)
    private fun readArea() = readToken(33, 5)
    private fun readOzu() = readToken(42, 4)
    private fun readCategoryArea() = readToken(38, 4)

    private fun readToken(offset: Int, size: Int) : String{
        val arr = ByteArray(size)
        raf.read(arr, offset + globalOffset.toInt(), size)
        return arr.toString()
    }



}