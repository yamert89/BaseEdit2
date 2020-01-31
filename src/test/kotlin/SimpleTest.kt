import java.io.File
import java.io.RandomAccessFile

fun main(){
    rfsTest()
}

fun rfsTest(){
    val file = File("D://test.007")
    val raf = RandomAccessFile(file, "rw")
   // print("1${10.toChar()}2")
    val arr = ByteArray(6)
    raf.read(arr, 0, arr.size)
    print(arr[0])
}