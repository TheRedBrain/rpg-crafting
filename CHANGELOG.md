# 1.4.0

## Additions

- added server config option to show locked handcrafted recipes
- added server config option to isolate crafting tabs (remove the buttons to switch to another crafting tab)
- add "is_consumed" boolean to (itemStack)ingredients ("true" by default). If set to "false", the ingredient is not consumed upon crafting.
- RPGCraftingRecipes now return recipe remainders to the players inventory

## Changes

- reworked normal ingredients to "rpg_ingredients"
- renamed "itemStackIngredients" into "rpg_item_stack_ingredients"
- changed all fields in the RPGCraftingRecipe JSON files to "snake_case", following vanillas example
- increased amount of visible ingredient stacks in RPG Crafting screens from 4 to 7
- increased amount of visible ingredient stacks in RPG Crafting screens from 4 to 8

# 1.3.0

- added Altar Shaped Crafting Tab Provider Block, unused by default. Acts as a simple API for add-on mods.
- added Enchanting Table Shaped Crafting Tab Provider Block, unused by default. Acts as a simple API for add-on mods.
- added Lectern Shaped Crafting Tab Provider Block, unused by default. Acts as a simple API for add-on mods.
- added support for multiple tab provider blocks per tab
- tab tooltips are now properly localized
- replaced game rules with server config settings
- internal improvements

# 1.2.0

- added an optional "completeComponentMatch" boolean field to each entry of the "itemStackIngredients" list, with a default of true
  - when set to false, an itemStack only has to contain the components listed in the recipe, to be considered a valid ingredient for the recipe (eventual additional components are ignored)

# 1.1.0

- added "itemStackIngredients" to RPGCraftingRecipe. This is a separate list of ingredients that consists of item stacks. When data components are defined for a stack, a stack with the exact same data components is required for the recipe. If no components are defined, any stack of that item type will work. Both types of ingredient lists are active at the same time. Note that existing recipes of the "rpgcrafting:rpg_crafting_recipe" recipe type must be updated!
- added "requiresUnlockAdvancement" boolean to RPGCraftingRecipe (false by default). If set to true, the recipe needs to be unlocked (e.g. by an advancement).
- added (in)active tab provider block java API
- added entity attributes for crafting tab levels
- hand-crafting screen now shows the hand-crafting level
- added advancement criterion for interacting with a crafting station (conditions include crafting tab and level)
- added loot tables and other survival relevant assets for crafting tab (level) providing blocks
- added client config option to disable item count in recipe description

- removed config option to set default hand-crafting level. It was causing more problems than worth the convenience. There are third-party mods that allow changing the default value of entity attributes.

- fixed lingering crafting result slots

# 1.0.0

First release!

#