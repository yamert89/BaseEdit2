import javafx.geometry.Insets
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.TableView
import javafx.scene.input.KeyCombination
import javafx.util.StringConverter
import javafx.util.converter.IntegerStringConverter
import tornadofx.*

fun main() {
    launch<BaseEdit2>()
}

class BaseEdit2: App(ParentView::class)

class ParentView : View(){
    val controller: GenController by inject()
    var selected: Area? = null
    var selectedRow: Int = 0
    var tableView: TableView<Area>? = null
    val model: AreaModel by inject()
    val dataTypes = DataTypes()
    val d: Int? = null
    val l: Long? = null


    override val root = vbox {
        hbox {


             button("Добавить"){
                 hboxConstraints { margin = Insets(10.0) }
                action {
                    if(selected == null) {
                        println("selected is null")
                        return@action
                    }
                    val item = selected!!
                    controller.tableData.add(selectedRow, Area(0, item.numberKv, 0.0, item.categoryArea, 0, item.ozu, item.lesb))
                    tableView?.selectionModel?.select(selectedRow)

                }
                shortcut("Ctrl+Q")


            }
            button("Удалить"){
                hboxConstraints { margin = Insets(10.0) }
                action {
                    //find(Modal::class).openModal()
                    alert(Alert.AlertType.CONFIRMATION, "Удалить?", actionFn = {buttonType ->
                        if (buttonType == ButtonType.OK) controller.tableData.removeAt(selectedRow)
                    } )
                        //val res = alert.showAndWait()
                   /* if (res.get() == ButtonType.OK){
                        controller.tableData.removeAt(selectedRow)
                    }else alert.close()*/

                }
            }
            button("Сохранить") {  }




        }
        tableView = tableview(controller.getData()) {
            isEditable = true
            readonlyColumn("Кв", Area::numberKv)
            readonlyColumn("Выд", Area::number)
            column("Площадь", Area::area).makeEditable()
            column("К. защитности", Area::categoryProtection).makeEditable().useComboBox()
            column("dd", Area::temp).makeEditable().useComboBox(listOf("ddd", "sdsd").asObservable())

            //useComboBox<Int>(dataTypes.categoryProtection.keys.toList().asObservable())
            readonlyColumn("К. земель", Area::categoryArea)
            column("ОЗУ", Area::ozu).makeEditable()
            column("lesb", Area::lesb).makeEditable()
            selectionModel.selectedItemProperty().onChange {
                selected = this.selectedItem
                selectedRow = this.selectedCell?.row ?: selectedRow
            }

            //column("Кат. защ")

        }
    }


}

class Modal(message: String) : Fragment(){
    override val root = stackpane {
        label(message)

    }
}

