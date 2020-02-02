package main.kotlin.app

import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.util.converter.DefaultStringConverter
import javafx.util.converter.IntegerStringConverter
import tornadofx.*
import app.*


/*class BaseEdit2App {

    companion object {
        @JvmStatic
        fun main(args : Array<String>) {
            launch<BaseEdit2>(args)

        }
    }
}*/

fun main(args : Array<String>) {
    launch<BaseEdit2>()

}


class BaseEdit2: App(ParentView::class)

class ParentView : View(){
    val controller: GenController by inject()

    var selected: Area? = null
    var selectedRow: Int = 0
    var tableView: TableView<Area>? = null
    lateinit var progressBar: ProgressBar
    val model: AreaModel by inject()
    val dataTypes = DataTypes()
    var colum: TableColumn<Area, String?>? = null


    override val root = vbox {

        hbox {
            button ("Открыть"){
                action {
                    val files = chooseFile("Выберите файл", mode = FileChooserMode.Single, filters = arrayOf())
                    //todo filters
                    progressBar.isVisible = true
                    controller.initData(files[0])
                    progressBar.isVisible = false

                }

            }


             button("Добавить"){
                 hboxConstraints { margin = Insets(10.0) }
                action {
                    if(selected == null) {
                        println("selected is null")
                        return@action
                    }
                    val item = selected!!
                    controller.tableData.add(selectedRow,
                        Area(0, item.numberKv, 0.0, item.categoryArea, "0", item.ozu, item.lesb)
                    )
                    tableView?.selectionModel?.select(selectedRow)

                }
                shortcut("Ctrl+Q")


            }
            button("Удалить"){
                hboxConstraints { margin = Insets(10.0) }
                action {
                    //find(app.main.kotlin.app.Modal::class).openModal()
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
            column("К. защитности", Area::categoryProtection).makeEditable().useComboBox(dataTypes.categoryProtection.keys.toList().asObservable())




            //useComboBox<Int>(dataTypes.categoryProtection.keys.toList().asObservable())
            readonlyColumn("К. земель", Area::categoryArea)
            column("ОЗУ", Area::ozu).makeEditable()
            column("lesb", Area::lesb).makeEditable()
            selectionModel.selectedItemProperty().onChange {
                selected = this.selectedItem
                selectedRow = this.selectedCell?.row ?: selectedRow
            }
            vgrow = Priority.ALWAYS


            //column("Кат. защ")

        }
        progressBar = progressbar {
            prefWidth = tableView!!.width
            //isVisible = false


        }
    }


}

class Modal(message: String) : Fragment(){
    override val root = stackpane {
        label(message)

    }
}

class AdvancedStringConverter(private val dataMap: Map<String, String>): DefaultStringConverter(){
    override fun toString(value: String): String {
        return super.toString(dataMap[value])
    }

    override fun fromString(value: String?): String {
        return dataMap.filterValues { it == value }.iterator().next().key
    }
}

class IntToStringConverter(private val dataMap: Map<Int, String>): IntegerStringConverter(){
    override fun toString(value: Int?): String {
        return dataMap[value]!!
    }

    override fun fromString(value: String?): Int {
        return dataMap.filterValues { it == value }.iterator().next().key
    }
}
