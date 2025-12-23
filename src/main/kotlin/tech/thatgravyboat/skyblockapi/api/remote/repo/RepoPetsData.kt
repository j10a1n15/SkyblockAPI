package tech.thatgravyboat.skyblockapi.api.remote.repo

import me.owdding.ktcodecs.FieldName
import me.owdding.ktcodecs.GenerateCodec
import me.owdding.ktcodecs.NamedCodec
import me.owdding.ktmodules.Module
import tech.thatgravyboat.skyblockapi.api.SkyBlockAPI
import tech.thatgravyboat.skyblockapi.api.data.SkyBlockRarity
import tech.thatgravyboat.skyblockapi.api.remote.api.SkyBlockId
import tech.thatgravyboat.skyblockapi.generated.SkyblockAPICodecs

@Module
object RepoPetsData {
    private val rarityOffsets: MutableList<Int> = mutableListOf()
    private val xpCurve: MutableList<Int> = mutableListOf()
    private val overwrites: MutableMap<SkyBlockId, RepoPetData> = mutableMapOf()
    private val defaultData: RepoPetData

    init {
        SkyBlockAPI.getRepo("pets", SkyblockAPICodecs.getCodec<RepoPets>()).let {
            this.rarityOffsets.addAll(it.rarityOffsets)
            this.xpCurve.addAll(it.xpCurve)
            this.overwrites.putAll(it.overwrites)
            defaultData = RepoPetData(rarityOffsets, xpCurve)
        }
    }

    fun getData(skyblockId: SkyBlockId): RepoPetData = overwrites[skyblockId] ?: defaultData

    @GenerateCodec
    data class RepoPets(
        @FieldName("rarity_offsets") val rarityOffsets: List<Int>,
        @FieldName("xp_curve") val xpCurve: List<Int>,
        val overwrites: Map<SkyBlockId, RepoPetData>,
    )

    @GenerateCodec
    @NamedCodec("PetsData")
    data class RepoPetData(
        @FieldName("xp_curve") val xpCurve: List<Int> = RepoPetsData.xpCurve,
        @FieldName("rarity_offsets") val rarityOffsets: List<Int> = RepoPetsData.rarityOffsets,
        @FieldName("level_cap") val levelCap: Int = 100,
    ) {
        fun getTotalXp(rarity: SkyBlockRarity, level: Int): Int {
            val curve = getCurveForRarity(rarity)
            return curve.take(level - 1).sum()
        }

        fun getLevel(rarity: SkyBlockRarity, xp: Long): Int {
            val curve = getCurveForRarity(rarity)
            var total = 0
            for ((index, value) in curve.withIndex()) {
                total += value
                if (total > xp) {
                    return index + 1
                }
            }
            return levelCap
        }


        private fun getOffset(rarity: SkyBlockRarity): Int {
            val ordinal = rarity.ordinal.coerceIn(0, rarityOffsets.size - 1)
            return rarityOffsets[ordinal]
        }

        private fun getCurveForRarity(rarity: SkyBlockRarity): List<Int> = xpCurve.drop(getOffset(rarity)).take(levelCap - 1)
    }
}
