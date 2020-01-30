import javafx.collections.ObservableList
import tornadofx.Controller
import tornadofx.asObservable

class GenController: Controller() {
    val tableData = emptyList<Area>().toMutableList().asObservable()


    fun getData(): ObservableList<Area> {
        tableData.addAll(listOf(Area(1, 3, 3.0, 1101, 121400, 0, 0),
            Area(2, 3, 4.0, 1108, 304601, 445, 0)).asObservable())
        return tableData
    }

    fun save(model: AreaModel){

    }
}