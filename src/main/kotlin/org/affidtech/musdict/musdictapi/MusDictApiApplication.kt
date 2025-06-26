package org.affidtech.musdict.musdictapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MusDictApiApplication

fun main(args: Array<String>) {
	runApplication<MusDictApiApplication>(*args)
}
