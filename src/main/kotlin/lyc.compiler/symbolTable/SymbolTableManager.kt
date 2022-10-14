package lyc.compiler.symbolTable

class SymbolTableManager {
    private val symbolList: MutableList<Symbol>
    private var constStringNumber: Int

    init {
        symbolList = ArrayList()
        constStringNumber = 0
    }

    fun getSymbolList(): List<Symbol> {
        return symbolList
    }

    fun add(name: String, type: DataType?, value: String?, length: Int?) {
        if (!isInTable(name)) {
            symbolList.add(Symbol(name, type, value, length))
        }
    }

    fun addStringConstant(type: DataType?, value: String?, length: Int?): String {
        val name = "_constString$constStringNumber"
        constStringNumber++
        if (!isInTable(name)) {
            symbolList.add(Symbol(name, type, value, length))
        }
        return name
    }

    fun addIdentifiers(identifiers: ArrayList<String>, dataType: DataType?) {
        val i = identifiers.iterator()
        while (i.hasNext()) {
            // must be called before you can call i.remove()
            val id = i.next()
            if (!isInTable(id)) {
                add(id, dataType, "-", null)
            } else {
                throw Error("Error de sintaxis: la variable '$id' ya ha sido declarada.")
            }
            // Remove identifier from list
            i.remove()
        }
    }

    private fun isInTable(name: String): Boolean {
        return symbolList.stream().anyMatch { symbol: Symbol -> symbol.name == name }
    }
}