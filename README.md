# RPG Crafting

RPG Crafting introduces an alternative/replacement/addition to the traditional crafting system. It is heavily inspired by games like Valheim and Terraria.

# Differences to vanilla crafting

The traditional system provides a grid for ingredients, where the pattern made with ingredient items determines the output.

RPG Crafting takes the "stone cutter approach" and takes it to the next level. The player can select a recipe out of a list of available recipes.
The ingredients are (by default) taken out of the players inventory.

RPG Crafting uses simple ingredient lists. While this might look not as interesting as the vanilla system, it has several advantages

- multiple recipes can have the same ingredients
- ingredient amounts are not restricted
- unique recipe patterns are not required


# Content
RPG Crafting a custom recipe type, similar to the stonecutter recipe type.
But instead of a single item, the crafting input can contain multiple items. 
The player can choose a recipe out of a list of all valid recipes.



## Recipes
Recipes are defined via data packs.
Example:

```json
{
	"type": "rpgcrafting:rpg_crafting_recipe",
	"category": "misc",
	"ingredients": [
		{
			"item": "minecraft:oak_planks"
		},
		{
			"item": "minecraft:oak_planks"
		},
		{
			"item": "minecraft:oak_planks"
		},
		{
			"item": "minecraft:oak_planks"
		},
		{
			"item": "minecraft:stick"
		},
		{
			"item": "minecraft:stick"
		}
	],
	"result": {
		"id": "minecraft:oak_fence",
		"count": 3
	},
	"level": 1,
	"tab": 1,
	"recipeType": "STANDARD"
}
```

- "type", has to be "rpgcrafting:rpg_crafting_recipe" for RPG Crafting to recognise it.
- "category", should always be "misc". This is used by the vanilla recipe book to display recipes in different categories, but RPG Crafting doesn't use this feature.
- "ingredients", a list of ingredients, that must be present in the input inventories and which are consumed upon crafting. Like for vanilla recipes, this supports items and item tags.
- "result", an item stack that is given to the player upon crafting
- "level", an integer. This determines which level the crafting station has to be at a minimum.
- "tab", an integer. This determines at which crafting station this recipe is crafted.
  - 0: hand crafting
  - 1 to 4: the different tabs in the Crafting Bench screen
- "recipeType", either "STANDARD" or "SPECIAL". This is not used, when the "tab" is set to 0.

## Crafting Bench Screen
This screen is the most complex of the three.
Interacting with certain blocks opens this screen. Depending on the block, a different "tab" is initially selected. 
Each of these tabs represents a different crafting station, eg. a crafting bench or an anvil.
Each tab has two sub categories, "STANDARD" and "SPECIAL"
Each tab shows only the recipes that can be crafted at that crafting station and of the currently selected sub category.

## Hand Crafting Screen
Hand Crafting is a special "crafting station", that is accessed via a hotkey/button.
This screen doesn't have different tabs.
The level of Hand Crafting is determined by the new entity attribute "generic.hand_crafting_level".
Hand Crafting checks the players inventory for ingredients.

## Recipe List Screen
This screen has a single inventory slot in addition to the player inventory. The recipe list will show every single (unlocked) recipe, that has the item in the slot as an ingredient.
The "Craft" button can not be clicked, but it shows which level of which crafting station is required for the recipe.

# Customization
Data and Resource Packs and the config files can be used to extensively customize this mod.

