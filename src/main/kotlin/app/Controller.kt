package app

import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable
import java.io.File

class GenController: Controller() {
    val tableData = emptyList<Area>().toMutableList().asObservable()
    private var filePath = ""


    fun getData(): ObservableList<Area> {

        return tableData
    }

    fun save(){
        FileExecutor().saveFile(File(filePath + "_temp"), tableData)
    }

    fun initData(file: File){
        tableData.addAll(FileExecutor().parseFile(file))
        filePath = file.absolutePath
        print("init data done")
    }


}


