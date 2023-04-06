import java.nio.file.Path
import kotlin.io.path.div

val WORKING_DIRECTORY: Path = Path.of(System.getProperty("user.dir"))
val USER_HOME: Path = Path.of(System.getProperty("user.home"))
val CONFIG_DIR: Path = USER_HOME / ".config" / "kbr"