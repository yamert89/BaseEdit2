import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable
import java.io.File

class GenController: Controller() {
    val tableData = emptyList<Area>().toMutableList().asObservable()


    fun getData(): ObservableList<Area> {
        tableData.addAll(listOf(Area(1, 3, 3.0, "1101", "во", "0", "0"),
            Area(2, 3, 4.0, "1108", "экспл", "445", "0")).asObservable())
        return tableData
    }

    fun save(model: AreaModel){

    }

    fun initData(file: File) = Parser


}