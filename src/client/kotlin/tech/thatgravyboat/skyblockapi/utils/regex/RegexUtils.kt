package tech.thatgravyboat.skyblockapi.utils.regex

object RegexUtils {

    fun Regex.match(input: CharSequence, vararg groups: String = arrayOf(), action: (Destructured) -> Unit = {}): Boolean {
        val match = matchEntire(input)
        match?.let { action(Destructured(it, *groups)) }
        return match != null
    }

    fun List<Regex>.match(input: CharSequence, vararg groups: String = arrayOf(), action: (Destructured) -> Unit = {}): Boolean {
        return any { it.match(input = input, groups = groups, action = action) }
    }

    fun Regex.anyMatch(input: List<CharSequence>, vararg groups: String = arrayOf(), action: (Destructured) -> Unit = {}): Boolean {
        return input.any { match(it, groups = groups, action = action) }
    }

    fun Regex.find(input: CharSequence, vararg groups: String = arrayOf(), action: (Destructured) -> Unit = {}): Boolean {
        val match = find(input)
        match?.let { action(Destructured(it, *groups)) }
        return match != null
    }

    fun Regex.findGroup(input: CharSequence, group: String): String? {
        return find(input)?.let { Destructured(it, group).component1() }
    }

    fun Regex.findGroups(input: CharSequence, vararg groups: String = arrayOf()): Destructured? {
        return find(input)?.let { Destructured(it, *groups) }
    }

    fun Regex.contains(input: CharSequence): Boolean = containsMatchIn(input)

    fun <T> Regex.findOrNull(input: CharSequence, vararg groups: String = arrayOf(), action: (Destructured) -> T): T? {
        return find(input)?.let { action(Destructured(it, *groups)) }
    }

    fun Regex.findThenNull(input: CharSequence, vararg groups: String = arrayOf(), action: (Destructured) -> Unit = {}): Unit? {
        val match = find(input) ?: return Unit
        action(Destructured(match, *groups))
        return null
    }

    fun List<Regex>.find(input: CharSequence, vararg groups: String = arrayOf(), action: (Destructured) -> Unit = {}): Boolean {
        return any { it.find(input = input, groups = groups, action = action) }
    }

    fun Regex.anyFound(input: List<CharSequence>, vararg groups: String = arrayOf(), action: (Destructured) -> Unit = {}): Boolean {
        return input.any { find(it, groups = groups, action = action) }
    }

    fun Regex.findAll(input: List<CharSequence>, vararg groups: String = arrayOf(), action: (Destructured) -> Unit = {}): Boolean {
        var globalFound = false
        input.forEach {
            val found = find(it, groups = groups, action = action)
            if (found) globalFound = true
        }
        return globalFound
    }
}

class Destructured internal constructor(private val match: MatchResult, private vararg val keys: String) {

    val string: String get() = match.groupValues[0]

    private fun group(key: String): String = match.groups[key]!!.value

    operator fun get(key: String): String? = match.groups[key]?.value
    operator fun get(index: Int): String? = match.groupValues.getOrNull(index)

    operator fun component1(): String = group(keys[0])
    operator fun component2(): String = group(keys[1])
    operator fun component3(): String = group(keys[2])
    operator fun component4(): String = group(keys[3])
    operator fun component5(): String = group(keys[4])
    operator fun component6(): String = group(keys[5])
    operator fun component7(): String = group(keys[6])
    operator fun component8(): String = group(keys[7])
    operator fun component9(): String = group(keys[8])
    operator fun component10(): String = group(keys[9])
    operator fun component11(): String = group(keys[10])
}
