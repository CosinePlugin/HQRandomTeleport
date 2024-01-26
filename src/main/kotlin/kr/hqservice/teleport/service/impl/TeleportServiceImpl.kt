package kr.hqservice.teleport.service.impl

import kr.hqservice.teleport.HQRandomTeleport.Companion.plugin
import kr.hqservice.teleport.extension.async
import kr.hqservice.teleport.extension.sync
import kr.hqservice.teleport.repository.TeleportRepository
import kr.hqservice.teleport.service.TeleportService
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.block.BlockFace
import org.bukkit.entity.Boat
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue

class TeleportServiceImpl(
    private val repository: TeleportRepository
) : TeleportService {

    override fun teleport(world: World, player: Player) {
        val maxHeight = world.maxHeight

        val randomX = repository.getRandomX()
        val randomZ = repository.getRandomZ()

        async {
            for (height in maxHeight downTo 62) {
                val floorBlock = world.getBlockAt(randomX, height, randomZ)
                val floorBlockType = floorBlock.type

                if (floorBlockType != Material.WATER && !floorBlockType.isSolid) continue

                val upBlock = floorBlock.getRelative(BlockFace.UP)
                val upBlockType = upBlock.type

                val upBlock2 = upBlock.getRelative(BlockFace.UP)
                val upBlockType2 = upBlock2.type

                if (floorBlockType != Material.AIR && !upBlockType.isSolid && !upBlockType2.isSolid) {
                    sync {
                        val upLocation = upBlock.location.toCenterLocation()
                        player.teleport(upLocation)
                        if (repository.isBoatEnable() && floorBlockType == Material.WATER) {
                            val boat = world.spawnEntity(upLocation, EntityType.BOAT)
                            boat.setMetadata("temporary-boat", FixedMetadataValue(plugin, true))
                            if (boat is Boat) {
                                boat.addPassenger(player)
                            }
                        }
                    }
                    break
                }
            }
        }
    }

    private fun Location.toCenterLocation(): Location {
        return add(0.5, 0.0, 0.5)
    }
}