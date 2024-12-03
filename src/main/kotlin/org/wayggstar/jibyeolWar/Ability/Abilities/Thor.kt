package org.wayggstar.jibyeolWar.Ability.Abilities

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable
import org.wayggstar.jibyeolWar.Ability.Ability
import org.wayggstar.jibyeolWar.JibyeolWar
import kotlin.random.Random

class Thor : Ability, Listener {

    private val StunedTarget = mutableSetOf<Player>()
    private val enabledPlayers = mutableSetOf<Player>()

    override val name: String = "토르"
    override val description: List<String> = listOf(
        "§b번개§r를 다루는 §e신§r이다.",
        "철도끼로 사람을 공격할 시 5%확률로 번개를 내려친다.",
        "번개에 맞은적은 2초간 §b감전§r되어서 움직일 수 없다."
    )
    override val rank: String = "§aA"

    override fun activate() {
        val player = Bukkit.getPlayer("playerName")
        player?.let { enabledPlayers.add(it) }
    }

    override fun deactivate() {
        val player = Bukkit.getPlayer("playerName")
        player?.let { enabledPlayers.remove(it) }
    }

    @EventHandler
    fun onHitThor(e: EntityDamageByEntityEvent) {
        val attacker = e.damager as? Player

        if (attacker != null && enabledPlayers.contains(attacker)) {
            if (attacker.inventory.itemInMainHand.type == Material.IRON_AXE) {
                val chance = Random.nextInt(100)
                if (chance < 5) {
                    val location = e.entity.location
                    val lightning = e.entity.world.strikeLightning(location)
                    for (entity in lightning.location.chunk.getEntities()) {
                        if (entity is Player && entity != attacker) {
                            entity.damage(5.0)
                            stunPlayer(entity)
                        }
                    }
                }
            }
        }
    }

    private fun stunPlayer(player: Player) {
        StunedTarget.add(player)

        object : BukkitRunnable() {
            override fun run() {
                StunedTarget.remove(player)
                player.sendMessage("§b감전§a이 해제되었습니다")
            }
        }.runTaskLater(JibyeolWar.instance, 40L)
    }

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        if (StunedTarget.contains(player)) {
            event.isCancelled = true  // 이동을 막음
            player.sendMessage("§c당신은 §b감전§c당해 움직일 수 없습니다!")
        }
    }
}