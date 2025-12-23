package tech.thatgravyboat.skyblockapi.api.profile.pets

import me.owdding.ktmodules.Module
import tech.thatgravyboat.skyblockapi.api.data.SkyBlockRarity
import tech.thatgravyboat.skyblockapi.api.events.base.Subscription
import tech.thatgravyboat.skyblockapi.api.events.base.predicates.OnlyWidget
import tech.thatgravyboat.skyblockapi.api.events.chat.ChatReceivedEvent
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidget
import tech.thatgravyboat.skyblockapi.api.events.info.TabWidgetChangeEvent
import tech.thatgravyboat.skyblockapi.utils.extentions.parseFormattedDouble
import tech.thatgravyboat.skyblockapi.utils.regex.RegexGroup
import tech.thatgravyboat.skyblockapi.utils.regex.RegexUtils.anyMatch
import tech.thatgravyboat.skyblockapi.utils.regex.component.anyMatch
import tech.thatgravyboat.skyblockapi.utils.regex.component.match
import tech.thatgravyboat.skyblockapi.utils.regex.component.toComponentRegex
import tech.thatgravyboat.skyblockapi.utils.regex.matchWhen
import tech.thatgravyboat.skyblockapi.utils.text.TextProperties.stripped

@Module
object PetsAPI {

    private val widgetGroup = RegexGroup.TABLIST_WIDGET.group("pet")
    private val chatGroup = RegexGroup.CHAT.group("pet")
    private val inventoryGroup = RegexGroup.INVENTORY.group("pet")
    private val petRegex = widgetGroup.create(
        "pet",
        " \\[Lvl (?<level>\\d+)] (?<pet>[\\w ]+)(?: ✦)?",
    ).toComponentRegex()
    private val petXpRegex = widgetGroup.create(
        "xp",
        " (?<xp>[\\d,.]+)/(?<nextXp>[\\d,.mbkMBK]+) XP \\((?<percent>[\\d.]+)%\\)",
    )
    private val petOverflowXpRegex = widgetGroup.create(
        "overflowxp",
        " \\+(?<xp>[\\d,.]+) XP",
    )
    private val petMaxLevelRegex = widgetGroup.create(
        "max_level",
        " MAX LEVEL",
    )
    private val autoPetRegex = chatGroup.create(
        "auto_pet",
        "Autopet equipped your \\[Lvl (?<level>\\d+)] (?<name>.+)! VIEW RULE",
    ).toComponentRegex()
    private val summonRegex = chatGroup.create(
        "summon",
        "You (summoned|despawned) your (?<name>.+?)!",
    ).toComponentRegex()
    private val levelupRegex = chatGroup.create(
        "levelup",
        "Your (?<level>.+?) leveled up to level (?<name>\\d+)!",
    ).toComponentRegex()

    var pet: String? = null
        private set

    var rarity: SkyBlockRarity? = null
        private set

    var level: Int = 0
        private set

    var isMaxLevel: Boolean = false
        private set

    var xp: Double = 0.0
        private set

    var xpToNextLevel: Double = 0.0
        private set

    @Subscription
    @OnlyWidget(TabWidget.PET)
    fun onTabWidgetChange(event: TabWidgetChangeEvent) {
        this.reset()
        if (event.new.size < 2) return
        petRegex.anyMatch(event.newComponents, "level", "pet") { (level, pet) ->
            this.level = level.stripped.toIntOrNull() ?: 0
            this.pet = pet.stripped
            this.rarity = SkyBlockRarity.fromColorOrNull(pet.style.color?.value ?: 0)
        }
        petXpRegex.anyMatch(event.new, "xp", "nextXp", "percent") { (xp, nextXp, _) ->
            this.xp = xp.parseFormattedDouble()
            this.xpToNextLevel = nextXp.parseFormattedDouble()
        }
        this.isMaxLevel = petOverflowXpRegex.anyMatch(event.new, "xp") { (xp) ->
            this.xp += xp.parseFormattedDouble()
        } || petMaxLevelRegex.anyMatch(event.new)
    }

    @Subscription
    fun onChat(event: ChatReceivedEvent.Pre) {
        // TODO: COMPONENT REGEX SWITCH CASE
        autoPetRegex.match(event.component, "level", "name") { (level, name) ->
        }
        summonRegex.match(event.component, "name") { (name) ->
        }
        levelupRegex.match(event.component, "name", "level") { (name, level) ->
        }
    }

    private fun reset() {
        pet = null
        rarity = null
        level = 0
        isMaxLevel = false
        xp = 0.0
        xpToNextLevel = 0.0
    }
}

