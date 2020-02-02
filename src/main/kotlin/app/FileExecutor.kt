package app

import java.io.File
import java.io.RandomAccessFile
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.StandardCharsets

class FileExecutor {
    private lateinit var raf: RandomAccessFile
    private var globalOffset = 0L


    fun parseFile(file: File): List<Area>{
        raf = RandomAccessFile(file, "rw")
        val list = mutableListOf<Area>()
        while (raf.filePointer < file.length()){
            list.add(
                Area(
                    readNumber().toInt(),
                    readNumberKv().toInt(),
                    BigDecimal((readArea().toDouble() * 0.1)).setScale(1, RoundingMode.HALF_UP).toDouble(),
                    //readArea().toDouble() * 0.1,
                    readCategoryArea(),
                    readCategoryProtection(),
                    readOzu(),
                    readLesb()
                )
            )
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
    private fun readNumber() = readToken(29, 3)
    private fun readNumberKv() = readToken(6, 4)
    private fun readLesb() = readToken(13, 4)
    private fun readArea() = readToken(32, 5)
    private fun readOzu(): String{
        val s = readToken(42, 3)
        if (s.startsWith("\n")) return "000"
        return s
    }
    private fun readCategoryArea() = readToken(37, 4)

    private fun readToken(offset: Int, size: Int) : String{
        val arr = ByteArray(size)
        raf.seek(offset + globalOffset)
        raf.read(arr, 0, size)
        return arr.toString(StandardCharsets.US_ASCII)
    }

   // private fun writeCategoryProtection() = writeToken()


    private fun writeToken(offset: Int, string: String){
        val arr = string.toByteArray(StandardCharsets.US_ASCII)
        raf.write(arr, offset + globalOffset.toInt(), arr.size)
    }



}