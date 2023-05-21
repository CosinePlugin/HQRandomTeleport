package kr.hqservice.teleport.service

import org.bukkit.World
import org.bukkit.entity.Player

interface TeleportService {

    fun teleport(world: World, player: Player)
}