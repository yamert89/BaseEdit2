package app

import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class GenController: Controller() {
    val tableData = emptyList<Area>().toMutableList().asObservable()
    private var filePath = ""


    fun getData(): ObservableList<Area> {

        return tableData
    }

    fun save(){
        /*var name = "$filePath.bak"
        while (Files.exists(Paths.get(name))) name+="1"*/
        val res = File(filePath).renameTo(File("${filePath}_${LocalTime.now().format(DateTimeFormatter.ofPattern("HH_mm_ss"))}.bak"))
        print(res)
        FileExecutor().saveFile(File(filePath), tableData)
    }

    fun initData(file: File){
        tableData.addAll(FileExecutor().parseFile(file))
        filePath = file.absolutePath
        print("init data done")
    }


}


