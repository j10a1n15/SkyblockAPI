package tech.thatgravyboat.skyblockapi.api.profile

import tech.thatgravyboat.skyblockapi.RemoveNextVersion
import tech.thatgravyboat.skyblockapi.api.data.SkyBlockRarity
import tech.thatgravyboat.skyblockapi.api.profile.pets.PetsAPI as NewPetsAPI

@RemoveNextVersion(ReplaceWith("tech.thatgravyboat.skyblockapi.api.profile.pets.PetsAPI"))
object PetsAPI {
    val pet: String? get() = NewPetsAPI.pet
    val rarity: SkyBlockRarity? get() = NewPetsAPI.rarity
    val level: Int get() = NewPetsAPI.level
    val isMaxLevel: Boolean get() = NewPetsAPI.isMaxLevel
    val xp: Double get() = NewPetsAPI.xp
    val xpToNextLevel: Double get() = NewPetsAPI.xpToNextLevel
}
