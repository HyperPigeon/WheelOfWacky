execute unless entity @e[tag=blood_god_warrior] unless entity @p[tag=blood_god_chosen] run give @p netherite_sword[item_name='{"bold":true,"text":"Orphan Obliterator"}',enchantments={levels:{"minecraft:fire_aspect":2,"minecraft:looting":3,"minecraft:sharpness":5,"minecraft:sweeping_edge":3,"minecraft:mending":1,"minecraft:unbreaking":3}}] 1
execute unless entity @e[tag=blood_god_warrior] unless entity @p[tag=blood_god_chosen] run tell @p You have pleased the Blood God with your sacrifice!
tag @p add blood_god_chosen