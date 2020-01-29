import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.input.KeyCombination
import tornadofx.*

fun main() {
    launch<BaseEdit2>()
}

class BaseEdit2: App(ParentView::class)

class ParentView : View(){
    val controller: GenController by inject()
    var selectedItem: Area? = null
    var selectedRow: Int = 0
    var tableView: TableView<Area>? = null
    val model: AreaModel by inject()


    override val root = vbox {
        hbox {
             button("Добавить"){
                action {
                    if(selectedItem == null) {
                        println("selected is null")
                        return@action
                    }
                    val item = selectedItem!!
                    controller.tableData.add(Area(0, item.numberKv, 0.0, item.categoryArea, 0, item.ozu, item.lesb))

                    controller.tableData.add(Area(2, 3, 3.0, 1101, 121400, 0, 0))

                }
                shortcut("Ctrl+Q")


            }
            button("Удалить"){
                action {  }
            }




        }
        tableView = tableview(controller.getData()) {
            isEditable = true
            column("Кв", Area::numberKv)
            column("Выд", Area::number)
            column("Площадь", Area::area).makeEditable()
            selectionModel.selectedItemProperty().onChange {
                selectedItem = this
            }

            //column("Кат. защ")

        }
    }


}

