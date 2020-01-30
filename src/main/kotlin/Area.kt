import javafx.beans.property.*
import tornadofx.*

class Area(number: Int, numberKv: Int, area: Double, categoryArea: Int, categoryProtection: Int, ozu: Int, lesb: Int, temp: String = "df") {
    var numberProperty = SimpleIntegerProperty(this, "number", number)
    var number by numberProperty

    var numberKvProperty = SimpleIntegerProperty(this, "numberKv", numberKv)
    var numberKv by numberKvProperty

    var areaProperty = SimpleDoubleProperty(this, "area", area)
    var area by areaProperty

    var categoryAreaProperty = SimpleIntegerProperty(this, "catArea", categoryArea)
    var categoryArea by categoryAreaProperty

    var categoryProtectionProperty = SimpleIntegerProperty(this, "catProt", categoryProtection)
    var categoryProtection by categoryProtectionProperty

    var ozuProperty = SimpleIntegerProperty(this, "ozu", ozu)
    var ozu by ozuProperty

    var lesbProperty = SimpleIntegerProperty(this, "lesb", lesb)
    var lesb by lesbProperty

    var tempProperty = SimpleStringProperty(this, "temp", temp)
    var temp by tempProperty
}

class AreaModel: ItemViewModel<Area>(){
    val number = bind(Area::numberProperty) as IntegerProperty
    val numberKv = bind(Area::numberKvProperty) as IntegerProperty
    val area = bind(Area::areaProperty) as DoubleProperty
    val categoryArea = bind(Area::categoryAreaProperty) as IntegerProperty
    val categoryProtection = bind(Area::categoryProtectionProperty) as IntegerProperty
    val ozu = bind(Area::ozuProperty) as IntegerProperty
    val lesb = bind(Area::lesbProperty) as IntegerProperty
    val temp = bind(Area::tempProperty) as StringProperty
}



