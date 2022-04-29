package net.adriantodt

import java.io.File

val ignorePrefixes = setOf<String>(
    "zum",
    "zun",
    "zot",
    "zer",
    "zim",
    "zon",
    "zath",
    "zam",
    "big",
    "beta",
    "city",
    "vila",
    "lexi",
    "ope",
    "volz"
)
val ignoreSuffixes = setOf<String>("otcore", "tais", "domit")

fun main() {
    val input = File("input.txt")
        .readLines()
        .map(String::toLowerCase)
        .filterNot { ignorePrefixes.any(it::startsWith) || ignoreSuffixes.any(it::endsWith) }
        .distinct()
        .toMutableList()


    val prefixes = mutableSetOf<String>(
        "silver", "xxx", "xx", "x", "y", "city", "ace", "beta", "big", "double", "drill", "trio", "ware", "tech",
        "silicon", "vila", "via", "uno", "uni", "lat", "lexi", "una", "job", "jay", "joy", "solo", "media", "cane",
        "j", "flex", "golden", "gold", "span", "trans", "tran", "silver", "open", "big", "blue", "good", "plus", "fax",
        "hex", "run", "sil", "voya", "high", "alpha", "trust", "year", "zoom", "zen", "zoo", "zap", "volt", "city",
        "can", "techi", "techno", "cone", "dalt", "dam", "dan", "dento", "ding", "dom", "don", "dong", "drip", "duo",
        "how", "inch", "indi", "indigo", "kan", "kay", "key", "kin", "kon", "konk", "lab", "lam", "lan", "lane", "line",
        "mat", "math", "onto", "orto", "over", "ozer", "rank", "red", "tamp", "geo", "go", "gogo"
    )
    val suffixes = mutableSetOf<String>(
        "double", "core", "zone", "fax", "ware", "fan", "job", "run", "trans", "traxit", "unicode", "biolex", "tron",
        "plex", "matex", "technology", "tech", "latex", "hotit"
    )

    repeat(8) {
        println("Running prefix pass")
        for (s in input.sorted()) {
            val matches = prefixes.filter(s::startsWith)
            if (matches.isEmpty()) {
                continue
            }
            input.remove(s)
            val prefix = matches.reduce { a, b -> if (a.length > b.length) a else b }
            val suffix = s.removePrefix(prefix).cleanup()
            if (suffix.isNotBlank()) {
                val match = suffixes.firstOrNull(suffix::endsWith)
                if (match == null) {
                    suffixes.add(suffix)
                } else if (suffix != match) {
                    println("[!] $s - $prefix - $suffix [$match]")
                }
            }
        }
        println("Running suffix pass")
        for (s in input.sorted()) {
            val matches = suffixes.filter(s::endsWith)
            if (matches.isEmpty()) {
                continue
            }
            input.remove(s)
            val suffix = matches.reduce { a, b -> if (a.length > b.length) a else b }
            val prefix = s.removeSuffix(suffix).cleanup()
            if (prefix.isNotBlank()) {
                val match = prefixes.firstOrNull(prefix::startsWith)
                if (match == null) {
                    prefixes.add(prefix)
                } else if (prefix != match) {
                    println("[!] $s - $prefix [$match] - $suffix")
                }
            }
        }
    }

    File("prefixes.txt").writeText(prefixes.sorted().joinToString("\n"))
    File("suffixes.txt").writeText(suffixes.sorted().joinToString("\n"))
    File("remaining.txt").writeText(input.sorted().joinToString("\n"))

    println(prefixes.sorted())
    println(suffixes.sorted())

    val output = prefixes.flatMap { p -> suffixes.map { s -> p.capitalize() + s } } +
        prefixes.flatMap { p -> suffixes.map { s -> p.capitalize() + "-" + s } }

    for (initial in 'a'..'z') {
        File("output-$initial.txt").writeText(
            output.filter { it.startsWith(initial.toString(), true) }.sorted().joinToString("\n")
        )
    }

    File("output.txt").writeText(output.sorted().joinToString("\n"))
}

tailrec fun String.cleanup(): String {
    return when {
        startsWith("-") -> {
            removePrefix("-").cleanup()
        }
        endsWith("-") -> {
            removeSuffix("-").cleanup()
        }
        else -> {
            this
        }
    }
}
