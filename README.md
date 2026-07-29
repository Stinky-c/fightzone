# Fightzone

A JEXL scripting mod to determine how entities take damage.

## Configuration

```yaml
enabled: true # Controls if scripts are evaluated at all.
saveEnabled: false # Controls config file saving. Useful if you want to keep the config pretty
scripts: # A script has 3 parts
  - action: DENY # Must be any of `ALLOW`, `DENY`, `PASS`. Case-sensitive
    name: Player protection # A name to locate quickly
    # Every script element must start with a `!script`. This makes the element a jexl script
    # The YAML formatting style should not matter. Below is a literal block scalar, this is the easiest to use. 
    script: !script |
      target == 'minecraft:player'

  - action: DENY
    name: Wolf protection
    # If an attacker is a player and the target is a wolf, the damage is blocked
    script: !script |
      (attacker != null && attacker == 'minecraft:player' ) && target == 'minecraft:wolf'


```

- [JEXL Reference](https://commons.apache.org/proper/commons-jexl/reference/syntax.html)

### Variable List

#### `target`

- Type: `string`
- Nullable: `false`
- Examples
    - `minecraft:player`
    - `minecraft:wolf`
    - `othermod:creature`

The identifier for the entity taking damage.

#### `amount`

- Type: `float`
- Nullable: `false`
- Examples:
    - `0.0`
    - `1.0`
    - `0.5`

The amount of damage applying to the entity.

#### `damage_source`

- Type: `string`
- Nullable: `true`
- Examples:
    - `minecraft:arrow`
    - `minecraft:campfire`
    - `othermod:magic`

The identifier for the damage type being applied.

#### `attacker`

- Type: `string`
- Nullable: `true`
- Examples:
    - `null`
    - `minecraft:player`

The identifier for the attacking entity, is usually null when it's environmental damage like fire.

#### `event`

- Type: `string`
- Nullable: `false`
- Examples:
    - `death`
    - `damage`

A helper to differentiate between a death or damage event.