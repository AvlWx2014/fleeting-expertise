import java.io.InputStream

object Resources {
    fun loadResource(path: String): InputStream = this::class.java.getResourceAsStream(path) ?: error("Could not find resource $path")
}