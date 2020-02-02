package main.kotlin.app

import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.Priority
import javafx.util.converter.DefaultStringConverter
import javafx.util.converter.IntegerStringConverter
import tornadofx.*
import app.*
import javafx.scene.input.KeyCharacterCombination
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.paint.Color


fun main(args : Array<String>) {
    launch<BaseEdit2>()

}


class BaseEdit2: App(ParentView::class)

class ParentView : View(){
    val controller: GenController by inject()

    var selected: Area? = null
    var selectedRow: Int = 0
    var tableView: TableView<Area>? = null
    var progressBar: ProgressBar = ProgressBar()
    val model: AreaModel by inject()
    val dataTypes = DataTypes()
    var colum: TableColumn<Area, String?>? = null


    override val root = vbox {

        hbox {

            button ("Открыть"){
                action {
                    val files = chooseFile("Выберите файл", mode = FileChooserMode.Single, filters = arrayOf())
                    //todo filters
                    //progressBar.isVisible = true
                    println("start")
                    runAsyncWithProgress/*(progress = progressBar)*/ {
                        controller.initData(files[0])
                        println("end init")
                    }ui{
                        //progressBar.isVisible = false
                        println("visible false")
                    }
                    println("end")



                }
                hboxConstraints { margin = Insets(10.0) }


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
                        Area(0, item.numberKv, 0.0, item.categoryArea, "0", item.ozu, item.lesb, item.rawData)
                    )
                    tableView?.selectionModel?.select(selectedRow)

                }
                //shortcut(KeyCharacterCombination("+"))
                shortcut(KeyCodeCombination(KeyCode.ADD))


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
                shortcut(KeyCodeCombination(KeyCode.SUBTRACT))
            }
            button("Сохранить") {
                hboxConstraints { margin = Insets(10.0) }
                action {
                    runAsyncWithProgress {
                        controller.save()
                    }

                }


            }




        }
        tableView = tableview(controller.getData()) {
            isEditable = true
                // readonlyColumn("№", )
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
        //progressBar.attachTo(this)
        //progressBar.prefWidth = this.width
        progressBar = progressbar {
            prefWidth = tableView!!.width
            style(true) {
                borderColor += box(Color.RED)
            }

           /* progressProperty().addListener {
                observableValue, old, new -> print("VALUE: $observableValue $old $new")
            }*/

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

