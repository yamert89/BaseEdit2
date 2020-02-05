package app

import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable
import java.io.File
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class GenController: Controller() {
    val tableData = emptyList<Area>().toMutableList().asObservable()
    private var filePath = ""
    private val dataTypes = DataTypes()


    fun getData(): ObservableList<Area> {

        return tableData
    }

    fun save(path: String?){
        /*var name = "$filePath.bak"
        while (Files.exists(Paths.get(name))) name+="1"*/
        if (path == null){
            File(filePath).renameTo(File("${filePath}_${LocalTime.now().format(DateTimeFormatter.ofPattern("HH_mm_ss"))}.bak"))
        }else filePath = path
        FileExecutor().saveFile(File(filePath), tableData)
    }

    fun initData(file: File){
        tableData.addAll(FileExecutor().parseFile(file))
        filePath = file.absolutePath
        print("init data done")
    }

    fun executeUtil(param1: Pair<Any?, String>, param2: Pair<Any?, String>, resParam: Pair<Any, String>){
        val filteredData = tableData.filter {
            (if(param1.first != dataTypes.EMTPTY && param1.first != null) {
                when(param1.first){
                    dataTypes.KV -> it.numberKv == param1.second.toInt()
                    dataTypes.CATEGORY_AREA -> it.categoryArea == param1.second
                    dataTypes.CATEGORY_PROTECTION -> it.categoryProtection == param1.second
                    dataTypes.OZU -> it.ozu == param1.second
                    dataTypes.LESB -> it.lesb == param1.second
                    else -> throw IllegalArgumentException("invalid param")
                }
            } else true) &&
                    (if(param2.first != "" && param2.first != null) {
                        when(param2.first){
                            dataTypes.KV -> it.numberKv == param2.second.toInt()
                            dataTypes.CATEGORY_AREA -> it.categoryArea == param2.second
                            dataTypes.CATEGORY_PROTECTION -> it.categoryProtection == param2.second
                            dataTypes.OZU -> it.ozu == param2.second
                            dataTypes.LESB -> it.lesb == param2.second
                            else -> throw IllegalArgumentException("invalid param")
                        }
                    } else true)

        }

        filteredData.forEach {
            when(resParam.first){
                dataTypes.CATEGORY_AREA -> it.categoryArea = resParam.second
                dataTypes.CATEGORY_PROTECTION -> it.categoryProtection = resParam.second
                dataTypes.OZU -> it.ozu = resParam.second
                dataTypes.LESB -> it.lesb = resParam.second
            }
        }
    }


}


