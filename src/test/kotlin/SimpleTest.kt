import java.io.File
import java.io.RandomAccessFile

fun main(){
  write()
}

fun rfsTest(){
    val file = File("J://esn.log")
    val raf = RandomAccessFile(file, "rw")
   // print("1${10.toChar()}2")
    val arr = ByteArray(6)
    raf.read(arr, 0, arr.size)
    print(arr[0])
}

fun backspace(){
    val file = File("J://esn2.log")
    val raf = RandomAccessFile(file, "rw")
    println(raf.readLine())
    raf.seek(2)
    raf.write(byteArrayOf(8.toByte(), 8.toByte()))
    println(raf.readLine())
    raf.close()
}

fun write(){
    val file = File("J://esn2.log")
    val raf = RandomAccessFile(file, "rw")
    raf.setLength(raf.length() + 2)
    raf.write(byteArrayOf(48.toByte(), 48.toByte()), 0, 2)
    raf.close()
}