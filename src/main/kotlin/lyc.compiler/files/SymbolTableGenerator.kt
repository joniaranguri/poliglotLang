package lyc.compiler.files

import lyc.compiler.symbolTable.Symbol
import java.io.FileWriter
import java.io.IOException
import java.util.function.Consumer
import java.util.logging.Logger

class SymbolTableGenerator(private val symbolList: List<Symbol> = emptyList()) : FileGenerator {
    @Throws(IOException::class)
    override fun generate(fileWriter: FileWriter) {
        try {
            if (symbolList.isEmpty()) return
            fileWriter.write(String.format("%-30s|%-30s|%-30s|%-30s\n", "NOMBRE", "TIPODATO", "VALOR", "LONGITUD"))
            symbolList.forEach(Consumer<Symbol> { symbol: Symbol ->
                try {
                    fileWriter.write(symbol.toString() + "\n")
                } catch (e: IOException) {
                    LOGGER.severe("Ocurrio un error al guardar los symbols")
                    e.printStackTrace()
                }
            })
        } catch (e: Exception) {
            LOGGER.severe("Ocurrio un error al guardar el archivo")
            e.printStackTrace()
        }
    }

    companion object {
        val LOGGER: Logger = Logger.getLogger(SymbolTableGenerator::class.java.name)
    }
}