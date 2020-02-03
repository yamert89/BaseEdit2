package app

class DataTypes {
    val categoryArea: MutableMap<String, String> = mutableMapOf()
    val categoryProtection: MutableMap<String, String> = mutableMapOf()
    val ozu: MutableMap<String, String> = mutableMapOf()
    init {

        ozu.putAll(mapOf(
            "000" to "-",
            "021" to "уч. среди безлесн",
            "062" to "бз",
            "073" to "опушка",
            "113" to "глухари",
            "123" to "редкие жив и раст",
            "143" to "реликт и энд. породы",
            "152" to "реликт и энд. раст",
            "253" to "вокруг нас. пунктов",
            "363" to "запас < 50кбм",
            "445" to "бобры",
            "088" to "088"

        ))

        categoryProtection.putAll(mapOf(
            "304601" to "экспл",
            "121400" to "во",
            "110100" to "зп",
            "110200" to "но",
            "120800" to "зп по дорогам",
            "131802" to "зел. зона"
        ))

    }
}