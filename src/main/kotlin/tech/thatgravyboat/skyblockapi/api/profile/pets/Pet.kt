package tech.thatgravyboat.skyblockapi.api.profile.pets

import net.minecraft.world.item.ItemStack
import tech.thatgravyboat.skyblockapi.api.data.SkyBlockRarity
import tech.thatgravyboat.skyblockapi.api.datatype.DataTypes
import tech.thatgravyboat.skyblockapi.api.datatype.defaults.GenericDataTypes
import tech.thatgravyboat.skyblockapi.api.datatype.getData
import tech.thatgravyboat.skyblockapi.api.remote.api.SkyBlockId.Companion.getSkyBlockId
import tech.thatgravyboat.skyblockapi.api.remote.repo.RepoPetsData

data class Pet(
    var item: ItemStack,
) {
    val id get() = item.getSkyBlockId()
    val data get() = item.getData(DataTypes.PET_DATA)
    val rarity get() = data?.rarity
    val level get() = id?.let { RepoPetsData.getData(it).getLevel(rarity ?: SkyBlockRarity.COMMON, data?.exp ?: 0L) }
}
