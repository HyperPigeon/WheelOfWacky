summon marker ~ ~ ~ {Tags:["InitialPos"]}
tag @s add Excluded
tag @r[tag=!Excluded, distance=..64] add SecondPlayer
tp @s @p[tag=SecondPlayer]
tp @p[tag=SecondPlayer] @e[tag=InitialPos,limit=1]
kill @e[tag=InitialPos]
tag @a remove SecondPlayer
tag @s remove Excluded
