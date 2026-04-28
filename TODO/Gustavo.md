# Gustavo - Nucleo de criaturas (Pokemon)

Objetivo
- Implementar metadados basicos de Pokemon no dominio (sem logica de batalha).

Pacotes e arquivos (crie se nao existir)
- src/domain/creature/Pokemon.java
- src/domain/creature/Species.java
- src/domain/creature/Stats.java
- src/domain/move/ElementType.java
- src/domain/move/StatType.java
- src/domain/move/Move.java (stub)
- src/domain/status/StatusEffect.java (stub)

O que implementar (traduzido do diagrama)
- Stats: guarda hp, attack, defense, specialAttack, specialDefense, speed; metodos scaleForLevel, getValue, withValue.
- Species: guarda dexNumber, name, primaryType, secondaryType, baseStats, movePool; metodos resolveMovesForLevel e hasType.
- Pokemon: guarda nickname, level, currentHp, specie, currentStats, moves, statusEffects; metodos isAlive, receiveDamage, heal, applyStatus, replaceMove, getEffectiveStats, getEffectiveStat, resolveStatusAtTurnStart, resolveStatusAtTurnEnd, levelUp.
- ElementType enum: NORMAL, FIRE, WATER, GRASS, ELECTRIC, ICE, FIGHTING, POISON (metodo getWeakness pode retornar vazio por enquanto -> quando pronto Gabriel faz um leve rework para implementar de forma idiomática sem boilerplate de if-else/switch).
- StatType enum: HP, ATTACK, DEFENSE, SPECIAL_ATTACK, SPECIAL_DEFENSE, SPEED.
- Move e StatusEffect: stubs minimos so para compilar (assinaturas + retorno padrao), sem logica de batalha.

Dependencias
- Nenhuma para comecar.
- Mateus vai depender de Pokemon para montar Team.

Guia do ciclo
1. Criar enums ElementType e StatType.
2. Implementar Stats com getters e metodos simples.
3. Implementar Species com construtor e getters.
4. Implementar Pokemon com construtor e metodos basicos.
5. Criar stubs Move e StatusEffect apenas com assinaturas.

Referencias
- diagrams/02-creatures-moves-status.puml
- diagrams/01-battle-core-actions.puml (para ver como Pokemon entra no Team)
- docs/01-visao-geral-da-engine.md (contexto de arquitetura)
