# Fightzone

A JEXL evaluation to determine if players take damage.

## Language

```jexl

```

- [Language Reference](https://commons.apache.org/proper/commons-jexl/reference/syntax.html)
-

### Variable List

#### `target`

- Type: `string`
- Nullable: `false`
- Examples
    - `minecraft:player`
    - `minecraft:wolf`
    - `othermod:creature`

The identifier for the entity taking damage

#### `amount`

- Type: `float`
- Nullable: `false`
- Examples:
    - `0.0`
    - `1.0`
    - `0.5`

The amount of damage applying to the entity

#### `damage_source`

- Type: `string`
- Nullable: `true`
- Examples:
    - `minecraft:arrow`
    - `minecraft:campfire`
    - `othermod:magic`

### Function List
