package goodspace.bllsoneshot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BllsoneshotApplication

fun main(args: Array<String>) {
    runApplication<BllsoneshotApplication>(*args)
}
