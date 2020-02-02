package app

import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable
import java.io.File

class GenController: Controller() {
    val tableData = emptyList<Area>().toMutableList().asObservable()


    fun getData(): ObservableList<Area> {

        return tableData
    }

    fun save(model: AreaModel){

    }

    fun initData(file: File){
        tableData.addAll(FileExecutor().parseFile(file))
    }


}


