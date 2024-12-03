package org.wayggstar.jibyeolWar.Ability

interface Ability {
    fun activate()
    fun deactivate()
    val name: String
    val rank: String
    val description: List<String>
}