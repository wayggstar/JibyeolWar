package org.wayggstar.jibyeolWar

import org.bukkit.Bukkit
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.wayggstar.jibyeolWar.Ability.Abilities.Thor
import org.wayggstar.jibyeolWar.Ability.Ability
import java.io.File

class GameManager(private val plugin: JavaPlugin) {

    private lateinit var abilitiesConfig: FileConfiguration

    private val abilities: List<Ability> = listOf(
        Thor(),
    )

    init {
        loadAbilitiesConfig()
        initializeAbilitiesConfig()
    }

    private fun loadAbilitiesConfig() {
        val file = File(plugin.dataFolder, "abilities.yml")
        abilitiesConfig = YamlConfiguration.loadConfiguration(file)
    }

    private fun isAbilityEnabled(ability: Ability): Boolean {
        return abilitiesConfig.getBoolean("abilities.${ability.name}", true)
    }

    private fun initializeAbilitiesConfig() {
        val abilitiesFile = File(plugin.dataFolder, "abilities.yml")
        val config = YamlConfiguration.loadConfiguration(abilitiesFile)

        abilities.forEach { ability ->
            if (!config.contains("abilities.${ability.name}")) {
                config.set("abilities.${ability.name}", true)
            }
        }

        config.save(abilitiesFile)
    }

    fun assignRandomAbility(player: Player) {
        val enabledAbilities = abilities.filter { isAbilityEnabled(it) }
        if (enabledAbilities.isNotEmpty()) {
            val randomAbility = enabledAbilities.random()
            player.sendMessage("당신의 능력은 '${randomAbility.name}'입니다")
            player.sendMessage("등급: ${randomAbility.rank}")
            player.sendMessage("능력 설명:")
            randomAbility.description.forEach { player.sendMessage(it) }
            randomAbility.activate()
        } else {
            player.sendMessage("이용 가능한 능력이 없습니다.")
        }
    }

    fun startGame() {
        for (player in Bukkit.getOnlinePlayers()) {
            assignRandomAbility(player)
        }
    }
}