package lyc.compiler.symbolTable

class Symbol(var name: String, var type: DataType?, var value: String?, var length: Int?) {

    private fun toStringObject(o: Any?): String {
        return if (o != null) "" + o else "-"
    }

    override fun toString(): String {
        return String.format(
            "%-30s|%-30s|%-30s|%-30s",
            toStringObject(name),
            toStringObject(type),
            toStringObject(value),
            toStringObject(length)
        )
    }
}