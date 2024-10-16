package tech.thatgravyboat.skyblockapi.api.area.island

import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.OnlyIn
import tech.thatgravyboat.skyblockapi.api.events.info.ScoreboardUpdateEvent
import tech.thatgravyboat.skyblockapi.api.location.SkyBlockIsland
import tech.thatgravyboat.skyblockapi.modules.Module
import tech.thatgravyboat.skyblockapi.utils.extentions.parseColonDuration
import tech.thatgravyboat.skyblockapi.utils.regex.RegexGroup
import tech.thatgravyboat.skyblockapi.utils.regex.RegexUtils.anyMatch
import kotlin.time.Duration

@Module
object PrivateIslandAPI {

    private val regexGroup = RegexGroup.SCOREBOARD.group("private_island")

    private val flightDurationRegex = regexGroup.create(
        "flight_duration",
        "Flight Duration: (?<duration>[\\d:]+)"
    )

    var flightDuration: Duration = Duration.ZERO
        private set

    @Subscription
    @OnlyIn(SkyBlockIsland.PRIVATE_ISLAND, SkyBlockIsland.GARDEN)
    fun onScoreboardUpdate(event: ScoreboardUpdateEvent) {
        flightDurationRegex.anyMatch(event.added, "duration") { (duration) ->
            flightDuration = duration.parseColonDuration() ?: Duration.ZERO
        }
    }
}
