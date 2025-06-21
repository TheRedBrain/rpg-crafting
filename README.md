# RPG Crafting

Introduces an alternative/replacement/addition to the traditional crafting system. It is heavily inspired by games like Valheim and Terraria.

## Content

RPG Crafting adds a custom recipe type, similar to the stone cutter recipe type, but instead of a single item the crafting input can contain multiple items.

It also adds several blocks which open different screens on interaction.

The mod does not add crafting recipes for the new system, however. 

## Differences to vanilla crafting

The traditional system provides a grid for ingredients, where the pattern made with ingredient items determines the output.

RPG Crafting takes the "stone cutter approach" and takes it to the next level. The player can select a recipe out of a list of unlocked recipes.
The ingredients are (by default) taken out of the players inventory.

RPG Crafting uses simple ingredient lists. While this might look not as interesting as the vanilla system, it has several advantages

- multiple recipes can have the same ingredients
- ingredient amounts are not restricted
- unique recipe patterns are not required

## Recipes

Recipes are defined via data packs.
Example:

```json
{
  "type": "rpgcrafting:rpg_crafting_recipe",
  "category": "misc",
  "itemStackIngredients": [
    {
      "id": "minecraft:stick",
      "count": 2
    }
  ],
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
    }
  ],
  "result": {
    "id": "minecraft:oak_fence",
    "count": 3
  },
  "level": 1,
  "tab": 1,
  "recipeType": "STANDARD",
  "requiresUnlockAdvancement": false
}
```

- "type", has to be "rpgcrafting:rpg_crafting_recipe" for RPG Crafting to recognise it.
- "category", should always be "misc". This is used by the vanilla recipe book to display recipes in different categories, but RPG Crafting doesn't use this feature.
- "itemStackIngredients", a list of itemStacks, that must be present in the input inventories and which are consumed upon crafting. . When data components are defined for a stack, a stack with the exact same data components is required for the recipe. If no components are defined, any stack of that item type will work.
- "ingredients", a list of ingredients, that must be present in the input inventories and which are consumed upon crafting. Like for vanilla recipes, this supports items and item tags.
- "result", an item stack that is given to the player upon crafting
- "level", an integer. This determines which level the crafting station has to be at a minimum.
- "tab", an integer. This determines at which crafting station this recipe is crafted.
  - 0: handcrafting
  - 1 to 4: the different tabs in the Crafting Bench screen
- "recipeType", either "STANDARD" or "SPECIAL". This is not used, when the "tab" is set to 0.

### Recipe Descriptions

Recipes can optionally have a description, that is displayed on crafting screens and the recipe list.

The localization key has the following syntax:

"recipe.<recipe_namespace>.<recipe_path>.description"

Note that "recipe_path" must include eventual subfolders in the recipe directory.

## Crafting Bench Screen

This screen is the most complex of the three.

Interacting with 'crafting tab provider blocks' opens this screen. Depending on the block, a different "tab" is initially selected. 

Each of these tabs represents a different crafting station and has two sub categories, "STANDARD" and "SPECIAL".

Each tab shows only the recipes that can be crafted at that crafting station and of the currently selected sub category.

By default, there are 4 Crafting Tab Provider Blocks which look like a crafting table, an anvil, a cauldron and a furnace.

If other 'crafting tab provider blocks' are in a configurable radius around the interacted block, the corresponding tab can be accessed via a button.

Each crafting tab has a level. It is 0 by default and can be increased in two ways:
- by placing specific blocks in a configurable radius around the interacted block. This is controlled by block tags.
- via entity attributes. Each crafting tab has a corresponding attribute (e.g. "rpgcrafting:crafting_tab_1_level" for the first tab)

The server config provides options to customize how exactly the crafting tab level is calculated:
- addition: the levels provided by both options are added up
- higher value: the higher value of both options is used
- blocks required: the attribute level is used, but the maximum value is determined by the block level

### (In-)Active Tab Provider Blocks

RPG Crafting provides a simple Java API to implement custom Tab (Level) Provider Blocks that can be (de-)activated.

Use it by implementing the [TabProvider](https://github.com/TheRedBrain/rpg-crafting/blob/1.21.1/src/main/java/com/github/theredbrain/rpgcrafting/block/TabProvider.java) interface in your block class.

Note that blocks still need to be in the corresponding block tags, to count towards tab( level)s.

## Stash Crafting

The stash is an additional player specific inventory, similar to the ender chest inventory. Both inventories can be accessed by interacting with 'storage area provider blocks'.

Depending on the 'storage area provider block' different slots of those inventories are accessible. If other 'storage area provider blocks' are in a configurable radius around the interacted block, the corresponding slots are accessible, too.

There is also a button that toggles 'stash crafting'. When enabled, items in the stash/ender chest inventory are used for crafting.

## Handcrafting Screen

Handcrafting is a special "crafting station", that is accessed via a hotkey/button. This screen doesn't have different tabs.

The level of handcrafting is determined by the new entity attribute "rpgcrafting:generic.hand_crafting_level".

Handcrafting checks the players inventory for ingredients.

## Recipe List Screen

This screen has a single inventory slot in addition to the player inventory and is accessed via a hotkey/button.

The recipe list will show every single recipe, that has the item in the slot as an ingredient.

The "Craft" button can not be clicked, but it shows which level of which crafting station is required for the recipe.

## Customization

Data and resource Packs, game rules and the config files can be used to extensively customize this mod.
