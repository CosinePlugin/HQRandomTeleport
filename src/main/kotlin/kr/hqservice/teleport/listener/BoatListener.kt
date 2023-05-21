package kr.hqservice.teleport.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.vehicle.VehicleExitEvent

class BoatListener : Listener {

    @EventHandler
    fun onGetOff(event: VehicleExitEvent) {
        val vehicle = event.vehicle
        if (vehicle.hasMetadata("temporary-boat")) {
            vehicle.remove()
        }
    }
}