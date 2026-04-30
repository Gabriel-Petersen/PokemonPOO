# Gustavo - Nucleo de criaturas

Objetivo
- Implementar os metadados basicos de Pokemon no pacote `game.creature`, sem entrar em logica de batalha.

Pacotes e arquivos (crie se nao existir)
- `src/game/creature/Pokemon.java`
- `src/game/creature/Species.java`
- `src/game/creature/Stats.java`
- `src/game/creature/move/ElementType.java`
- `src/game/creature/move/StatType.java`
- `src/game/creature/move/Move.java` (stub)
- `src/game/creature/move/status/StatusEffect.java` (stub)

O que implementar (traduzido do diagrama)
- `Stats`: guarda `hp`, `attack`, `defense`, `specialAttack`, `specialDefense`, `speed`; metodos `scaleForLevel`, `getValue`, `withValue`.
- `Species`: guarda `dexNumber`, `name`, `primaryType`, `secondaryType`, `baseStats`, `movePool`; metodos `resolveMovesForLevel` e `hasType`.
- `Pokemon`: guarda `id`, `nickname`, `level`, `currentHp`, `species`, `currentStats`, `moves`, `statusEffects`; metodos `isAlive`, `receiveDamage`, `heal`, `applyStatus`, `replaceMove`, `getEffectiveStats`, `getEffectiveStat`, `resolveStatusAtTurnStart`, `resolveStatusAtTurnEnd`, `levelUp`.
- `ElementType`: `NORMAL`, `FIRE`, `WATER`, `GRASS`, `ELECTRIC`, `ICE`, `FIGHTING`, `POISON`.
- `StatType`: `HP`, `ATTACK`, `DEFENSE`, `SPECIAL_ATTACK`, `SPECIAL_DEFENSE`, `SPEED`.
- `Move` e `StatusEffect`: stubs minimos so para compilar, sem logica de batalha.

Dependencias
- Nenhuma para comecar.
- `game.battle.Team` (Mateus) vai depender de `Pokemon`.

Guia do ciclo
1. Criar `ElementType` e `StatType`.
2. Implementar `Stats` com campos, getters e metodos simples.
3. Implementar `Species` com construtor e getters.
4. Implementar `Pokemon` com construtor e metodos basicos.
5. Criar stubs `Move` e `StatusEffect` apenas com assinaturas.

Referencias
- `diagrams/02-creatures-moves-status.puml`
- `diagrams/01-battle-core-actions.puml` (para ver como `Pokemon` entra no `Team`)
- `docs/01-visao-geral-da-engine.md` (contexto de arquitetura)